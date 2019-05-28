package st.extreme.klingklong;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

final class Sender extends Thread {

  static final String STOP_SIGNAL = "__^ 3B@?)8Eu6.x6u2t?7?cw#2e+2W8)P?R3 ^__";

  private final String ourName;
  private final InetAddress remoteHost;
  private final int sendingPort;
  private final int localReceivingPort;
  private final AtomicBoolean running;
  private PrintWriter writer;

  public Sender(InetAddress remoteHost, int sendingPort, int localReceivingPort) throws UnknownHostException {
    this.ourName = InetAddress.getLocalHost().getHostName();
    this.remoteHost = remoteHost;
    this.sendingPort = sendingPort;
    this.localReceivingPort = localReceivingPort;
    this.running = new AtomicBoolean(true);
  }

  @Override
  public void run() {
    System.out.println(ourName + " is creating a sending socket on port " + sendingPort);
    try (Socket sendingSocket = waitForRemoteAcceptance()) {
      try {
        // since writer is a member used for sending, we cannot embed it into a try with resources block
        writer = new PrintWriter(sendingSocket.getOutputStream(), true);
        runningLoop();
      } finally {
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
    }
  }

  void send(String message) {
    if (STOP_SIGNAL.equals(message)) {
      System.out.println("sending STOP signal");
    } else {
      System.out.println(String.format("sending %s", message));
    }
    writer.println(Message.forSending(message));
  }

  void close() {
    System.out.println("closing sender");
    send(STOP_SIGNAL);
    try {
      sendLocalSTOP();
    } catch (IOException e) {
      e.printStackTrace();
    }
    running.set(false);
  }

  private void sendLocalSTOP() throws UnknownHostException, IOException {
    System.out.println("sending local STOP signal");
    try (Socket localReceivingSocket = new Socket(InetAddress.getLocalHost(), localReceivingPort);
        PrintWriter out = new PrintWriter(localReceivingSocket.getOutputStream(), true)) {
      out.println(Message.forSending(STOP_SIGNAL));
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
            String info = String.format("%s is waiting for remote %s/%d to accept a connection", ourName, remoteHost.getHostName(),
                sendingPort);
            System.out.println(info);
          }
          TimeUnit.SECONDS.sleep(1);
          secondsWaited++;
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
    return sendingSocket;
  }

  private void runningLoop() {
    long count = 0;
    while (isRunning()) {
      try {
        if (count % 200 == 0) {
          System.out.println("sender running");
        }
        TimeUnit.MILLISECONDS.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  final boolean isRunning() {
    return running.get();
  }

}
