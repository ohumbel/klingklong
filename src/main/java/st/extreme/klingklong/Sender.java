package st.extreme.klingklong;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

final class Sender extends Thread {

  static final String STOP_SIGNAL = "__^ 3B@?)8Eu6.x6u2t?7?cw#2e+2W8)P?R3 ^__";

  private final InetAddress remoteHost;
  private final int sendingPort;
  private final int localReceivingPort;
  private final Semaphore connectedSemaphore;
  private final Semaphore closedSemaphore;
  private PrintWriter writer;

  public Sender(InetAddress remoteHost, int sendingPort, int localReceivingPort, Semaphore connectedSemaphore) throws UnknownHostException {
    this.remoteHost = remoteHost;
    this.sendingPort = sendingPort;
    this.localReceivingPort = localReceivingPort;
    this.connectedSemaphore = connectedSemaphore;
    closedSemaphore = new Semaphore(0, true);
  }

  @Override
  public void run() {
    System.out.println(String.format("sender is creating a sending socket on port %d", sendingPort));
    try (Socket sendingSocket = waitForRemoteAcceptance()) {
      try {
        System.out.println("sender is creating a writer to the sending port");
        // since writer is a member used for sending, we cannot embed it into a try with resources block
        writer = new PrintWriter(sendingSocket.getOutputStream(), true);
        connectedSemaphore.release();
        runningLoop();
      } finally {
        System.out.println("sender is closing the writer");
        if (writer != null) {
          writer.close();
        }
      }
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host " + remoteHost.getHostName());
      e.printStackTrace();
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to " + remoteHost.getHostName());
      e.printStackTrace();
    } finally {
      System.out.println("sender is closing the socket");
    }
    System.out.println("sender thread terminating");
  }

  void send(String message) {
    if (STOP_SIGNAL.equals(message)) {
      System.out.println("sending STOP signal");
    } else {
      System.out.println(String.format("sending '%s'", message));
    }
    writer.println(Message.forSending(message));
  }

  void close() {
    System.out.println("sender starts closing");
    send(STOP_SIGNAL);
    sendLocalSTOP();
    closedSemaphore.release();
  }

  private void sendLocalSTOP() {
    // TODO maybe have a state somewhere who is closed already
    System.out.println("sending local STOP signal");
    try (Socket localReceivingSocket = new Socket(InetAddress.getLocalHost(), localReceivingPort);
        PrintWriter out = new PrintWriter(localReceivingSocket.getOutputStream(), true)) {
      out.println(Message.forSending(STOP_SIGNAL));
      System.out.println("sendLocalSTOP was successful");
    } catch (IOException e) {
      System.out.println("sendLocalSTOP was not successful");
    }
  }

  private Socket waitForRemoteAcceptance() throws UnknownHostException {
    Socket sendingSocket = null;
    int secondsWaited = 0;
    while (sendingSocket == null) {
      try { // do not use AutoCloseable here
        sendingSocket = new Socket(remoteHost, sendingPort);
      } catch (IOException ioe) {
        // remote not ready yet, wait a bit
        try {
          if (secondsWaited % 5 == 0) {
            String info = String.format("sender is waiting for remote %s/%d to accept a connection", remoteHost.getHostName(), sendingPort);
            System.out.println(info);
          }
          TimeUnit.SECONDS.sleep(1);
          secondsWaited++;
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
    System.out.println("sender is not waiting any more");
    return sendingSocket;
  }

  private void runningLoop() {
    try {
      closedSemaphore.acquire();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("sender ends running loop");
  }

}
