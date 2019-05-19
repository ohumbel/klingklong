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
    // step 1: listen on kling port
    int listeningPortNumber = DemoSocketKling.DEFAUT_KLING_PORT;

    System.out.println("starting to listen on port " + listeningPortNumber);
    try (ServerSocket serverSocket = new ServerSocket(listeningPortNumber);
        Socket listeningSocket = serverSocket.accept();
        // PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(listeningSocket.getInputStream()));) {

      String inputLine, outputLine;

      // Initiate conversation with client
      // KnockKnockProtocol kkp = new KnockKnockProtocol();
      // outputLine = kkp.processInput(null);
      outputLine = "";
      // out.println(outputLine);

      System.out.println("waiting for data...");
      while ((inputLine = in.readLine()) != null) {
        System.out.println("got " + inputLine);
        // outputLine = kkp.processInput(inputLine);
        // out.println(outputLine);
        if (inputLine.equals("bye"))
          break;
      }

      System.out.println("closing in 2 seconds");
      TimeUnit.SECONDS.sleep(2);
    } catch (IOException e) {
      System.out.println("Exception caught when trying to listen on port " + listeningPortNumber + " or listening for a connection");
      System.out.println(e.getMessage());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
