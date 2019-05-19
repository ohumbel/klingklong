package st.extreme.klingklong.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * Sends on kling port, listens on klong port
 */
public class DemoSocketKling {

  protected static final int DEFAUT_KLING_PORT = 8533;

  public static void main(String[] args) {
    new Sender().run();
  }

  private static final class Sender extends Thread {
    @Override
    public void run() {
      String remoteHostName = "localhost";
      int sendingPort = DEFAUT_KLING_PORT;
      System.out.println("kling is creating a sending socket on port " + sendingPort);
      try (Socket sendingSocket = waitForRemoteAcceptance(remoteHostName, sendingPort);
          PrintWriter out = new PrintWriter(sendingSocket.getOutputStream(), true);) {
        TimeUnit.SECONDS.sleep(2);
        String msg = "a message from kling";
        System.out.println("sending " + msg);
        out.println(msg);

        TimeUnit.SECONDS.sleep(2);
        msg = "bye";
        System.out.println("sending " + msg);
        out.println(msg);

        System.out.println("stop sending in 2 seconds");
        TimeUnit.SECONDS.sleep(2);
      } catch (UnknownHostException e) {
        System.err.println("Don't know about host " + remoteHostName);
        e.printStackTrace();
      } catch (IOException e) {
        System.err.println("Couldn't get I/O for the connection to " + remoteHostName);
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    private Socket waitForRemoteAcceptance(String remoteHostName, int sendingPort) throws UnknownHostException {
      Socket sendingSocket = null;
      int secondsWaited = 0;
      while (sendingSocket == null) {
        try { // do not use Autoclosable here
          sendingSocket = new Socket(remoteHostName, sendingPort);
        } catch (IOException ioe) {
          // remote not ready yet, wait a bit
          try {
            if (secondsWaited % 5 == 0) {
              System.out.println("kling is waiting for klong to accept a connection");
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

}
