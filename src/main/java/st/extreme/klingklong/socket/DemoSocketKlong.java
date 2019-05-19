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

  public static void main(String[] args) {
    new Receiver().run();
  }

  private static final class Receiver extends Thread {
    @Override
    public void run() {
      // listen on kling port
      int listeningPortNumber = DemoSocketKling.DEFAUT_KLING_PORT;

      System.out.println("klong is starting to listen on port " + listeningPortNumber);
      try (ServerSocket serverSocket = new ServerSocket(listeningPortNumber);
          Socket listeningSocket = serverSocket.accept();
          BufferedReader in = new BufferedReader(new InputStreamReader(listeningSocket.getInputStream()));) {
        String inputLine;

        System.out.println("waiting for data...");
        while ((inputLine = in.readLine()) != null) {
          System.out.println("got " + inputLine);
          if ("bye".equals(inputLine))
            break;
        }

        System.out.println("closing in 2 seconds");
        TimeUnit.SECONDS.sleep(2);
      } catch (IOException e) {
        System.out.println("Exception caught when trying to listen on port " + listeningPortNumber + " or listening for a connection");
        System.out.println(e.getMessage());
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

}
