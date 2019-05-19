package st.extreme.klingklong.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Sends on klong port, listens on kling port
 */
public class DemoSocketKlong {

  protected static final int DEFAULT_KLONG_PORT = 8539;

  private static final String KLONG = "Klong";

  public static void main(String[] args) {
    Receiver receiver = new Receiver(KLONG, DemoSocketKling.DEFAUT_KLING_PORT);
    receiver.run();
  }

  private static final class Receiver extends Thread {
    private final String ourName;
    private final int listeningPort;

    public Receiver(String ourName, int listeningPort) {
      this.ourName = ourName;
      this.listeningPort = listeningPort;
    }

    @Override
    public void run() {
      System.out.println(ourName + " is starting to listen on port " + listeningPort);
      try (ServerSocket serverSocket = new ServerSocket(listeningPort);
          Socket listeningSocket = serverSocket.accept();
          BufferedReader in = new BufferedReader(new InputStreamReader(listeningSocket.getInputStream()));) {
        String inputLine;

        System.out.println("waiting for data...");
        while ((inputLine = in.readLine()) != null) {
          System.out.println("got " + inputLine);
          if ("bye".equals(inputLine))
            break;
        }

        System.out.println(ourName + " receiver is closing in 2 seconds");
        TimeUnit.SECONDS.sleep(2);
      } catch (IOException e) {
        System.out.println("Exception caught when trying to listen on port " + listeningPort + " or listening for a connection");
        System.out.println(e.getMessage());
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

}
