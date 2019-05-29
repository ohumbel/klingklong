package st.extreme.klingklong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;

public class Receiver extends Thread {
  private final int listeningPort;
  private final Consumer<String> messageConsumer;

  public Receiver(int listeningPort, Consumer<String> messageConsumer) throws UnknownHostException {
    this.listeningPort = listeningPort;
    this.messageConsumer = messageConsumer;
  }

  @Override
  public void run() {
    System.out.println("receiver is starting to listen on port " + listeningPort);
    try (ServerSocket serverSocket = new ServerSocket(listeningPort);
        Socket listeningSocket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(listeningSocket.getInputStream()));) {
      String inputLine;
      System.out.println("receiver is waiting for data...");
      while ((inputLine = in.readLine()) != null) {
        System.out.println("got an input line");
        String message = Message.afterReceiving(inputLine);
        if (Sender.STOP_SIGNAL.equals(message)) {
          System.out.println("got <STOP> signal");
          break;
        } else {
          System.out.println("got " + message);
          messageConsumer.accept(message);
        }
      }
      System.out.println("closing receiver");
    } catch (IOException e) {
      System.out.println("Exception caught when trying to listen on port " + listeningPort + " or listening for a connection");
      e.printStackTrace();
    }
  }

}
