package st.extreme.klingklong;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

final class Sender extends Thread {

  static final String STOP_SIGNAL = "__^ 3B@?)8Eu6.x6u2t?7?cw#2e+2W8)P?R3 ^__";

  private final String ourName;
  private final InetAddress remoteHost;
  private final int sendingPort;

  public Sender(InetAddress remoteHost, int sendingPort) throws UnknownHostException {
    this.ourName = InetAddress.getLocalHost().getHostName();
    this.remoteHost = remoteHost;
    this.sendingPort = sendingPort;
  }

  @Override
  public void run() {
    // TODO
  }

  void send(String message) {
    // TODO
  }

  void close() {
    // TODO
  }

  // TODO: only used for stop?
  void sendLocal(String message, int localReceivingPort) throws UnknownHostException, IOException {
    System.out.println(String.format("sending local message %s", message));
    try (Socket localReceivingSocket = new Socket(InetAddress.getLocalHost(), localReceivingPort);
        PrintWriter out = new PrintWriter(localReceivingSocket.getOutputStream(), true)) {
      out.println(Message.forSending(message));
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

}
