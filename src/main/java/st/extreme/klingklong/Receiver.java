package st.extreme.klingklong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;

public class Receiver extends Thread {
  private final int listeningPort;
  private final Consumer<String> messageConsumer;
  private final String ourName;

  public Receiver(int listeningPort, Consumer<String> messageConsumer) throws UnknownHostException {
    this.listeningPort = listeningPort;
    this.messageConsumer = messageConsumer;
    this.ourName = InetAddress.getLocalHost().getHostName();
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
        if (Sender.STOP_SIGNAL.equals(msg)) {
          System.out.println("got <STOP> signal from remote");
          break;
        } else {
          System.out.println("got " + msg);
          messageConsumer.accept(msg);
        }
      }
      System.out.println(ourName + " closing receiver");
    } catch (IOException e) {
      System.out.println("Exception caught when trying to listen on port " + listeningPort + " or listening for a connection");
      e.printStackTrace();
    }
  }

}
