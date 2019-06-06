package st.extreme.klingklong;

import static st.extreme.klingklong.util.Horn.honk;

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
    try (Socket sendingSocket = waitForRemoteAcceptance()) {
      try {
        // since writer is a member used for sending, we cannot embed it into a try with resources block
        writer = new PrintWriter(sendingSocket.getOutputStream(), true);
        connectedSemaphore.release();
        runningLoop();
      } finally {
        if (writer != null) {
          writer.close();
        }
      }
    } catch (Exception e) {
      honk(String.format("connection error with remote host %s", remoteHost.getHostName()));
      e.printStackTrace();
    }
    closedSemaphore.release();
    honk("sender thread is terminating now");
  }

  void send(String message) {
    writer.println(Message.forSending(message));
  }

  void close() {
    send(STOP_SIGNAL);
    sendLocalSTOP();
    closedSemaphore.release();
    try {
      closedSemaphore.acquire();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void sendLocalSTOP() {
    try (Socket localReceivingSocket = new Socket(InetAddress.getLocalHost(), localReceivingPort);
        PrintWriter localWriter = new PrintWriter(localReceivingSocket.getOutputStream(), true)) {
      localWriter.println(Message.forSending(STOP_SIGNAL));
    } catch (IOException e) {
      // ignore
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
            String info = String.format("waiting for remote %s/%d to accept a connection", remoteHost.getHostName(), sendingPort);
            honk(info);
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
    try {
      closedSemaphore.acquire();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
