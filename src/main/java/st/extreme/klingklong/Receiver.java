package st.extreme.klingklong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

final public class Receiver extends Thread {
  private final String ourName;
  private final int listeningPort;

  public Receiver(int listeningPort) throws UnknownHostException {
    this.ourName = InetAddress.getLocalHost().getHostName();
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
        String msg = Message.afterReceiving(inputLine);
        if (Sender.STOP_SENDING_SIGNAL.equals(msg)) {
          System.out.println("got <STOP> signal from remote");
          break;
        } else {
          System.out.println("got " + msg);
        }
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