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
    String remoteHostName = "localhost";
    int sendingPort = DEFAUT_KLING_PORT;
    System.out.println("creating sending socket on port " + sendingPort);
    try (Socket sendingSocket = new Socket(remoteHostName, sendingPort);
        PrintWriter out = new PrintWriter(sendingSocket.getOutputStream(), true);
    // BufferedReader in = new BufferedReader(new InputStreamReader(sendingSocket.getInputStream()));
    ) {
      // BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
      String fromServer;
      String fromUser;

      // while ((fromServer = in.readLine()) != null) {
      // System.out.println("Server: " + fromServer);
      // if (fromServer.equals("Bye."))
      // break;
      //
      // fromUser = stdIn.readLine();
      // if (fromUser != null) {
      // System.out.println("Client: " + fromUser);
      // out.println(fromUser);
      // }
      // }

      TimeUnit.SECONDS.sleep(2);
      String msg = "message 1";
      System.out.println("sending " + msg);
      out.println(msg);

      TimeUnit.SECONDS.sleep(2);
      msg = "bye";
      System.out.println("sending " + msg);
      out.println(msg);

      System.out.println("quitting in 2 seconds");
      TimeUnit.SECONDS.sleep(2);
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host " + remoteHostName);
      System.exit(1);
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to " + remoteHostName);
      System.exit(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
