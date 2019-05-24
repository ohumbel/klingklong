package st.extreme.klingklong.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import st.extreme.klingklong.Message;

final public class DemoSender extends Thread {

  static final String STOP_SENDING_SIGNAL = "__sStopP^ !@#$%^&*() ^sSenNdiNngG__";

  private final String ourName;
  private final InetAddress remoteHost;
  private final int sendingPort;

  public DemoSender(InetAddress remoteHost, int sendingPort) throws UnknownHostException {
    this.ourName = InetAddress.getLocalHost().getHostName();
    this.remoteHost = remoteHost;
    this.sendingPort = sendingPort;
  }

  @Override
  public void run() {
    System.out.println(ourName + " is creating a sending socket on port " + sendingPort);
    try (Socket sendingSocket = waitForRemoteAcceptance(); //
        PrintWriter out = new PrintWriter(sendingSocket.getOutputStream(), true)) {
      TimeUnit.SECONDS.sleep(2);
      String msg = "a message from " + ourName;
      System.out.println("sending " + msg);
      out.println(Message.forSending(msg));

      TimeUnit.SECONDS.sleep(2);
      msg = "a message from " + ourName + "\n\t.. on two lines";
      System.out.println("sending " + msg);
      out.println(Message.forSending(msg));

      TimeUnit.SECONDS.sleep(2);
      System.out.println("sending <STOP> signal");
      out.println(Message.forSending(STOP_SENDING_SIGNAL));

      System.out.println(ourName + " sender stops sending in 2 seconds");
      TimeUnit.SECONDS.sleep(2);
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host " + remoteHost.getHostName());
      e.printStackTrace();
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to " + remoteHost.getHostName());
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
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
            System.out
                .println(ourName + " is waiting for remote " + remoteHost.getHostName() + "/" + sendingPort + " to accept a connection");
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