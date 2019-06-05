package st.extreme.klingklong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

public class Receiver extends Thread {
  private final int listeningPort;
  private final Consumer<String> messageConsumer;
  private final Semaphore readSemaphore;

  public Receiver(int listeningPort, Consumer<String> messageConsumer, Semaphore readSemaphore) throws UnknownHostException {
    this.listeningPort = listeningPort;
    this.messageConsumer = messageConsumer;
    this.readSemaphore = readSemaphore;
  }

  @Override
  public void run() {
    System.out.println(String.format("receiver is waiting to start to listen on port %d", listeningPort));
    try (ServerSocket serverSocket = new ServerSocket(listeningPort);
        Socket listeningSocket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(listeningSocket.getInputStream()));) {
      String inputLine;

      System.out.println("receiver is waiting for release from endpoint");
      try {
        readSemaphore.acquire();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      System.out.println("receiver was released and is now reading/waiting for data.");
      while ((inputLine = in.readLine()) != null) {
        System.out.println("receiver got an input line");
        String message = Message.afterReceiving(inputLine);
        if (Sender.STOP_SIGNAL.equals(message)) {
          System.out.println("receiver got <STOP> signal");
          break;
        } else {
          System.out.println(String.format("receiver got '%s'", message));
          messageConsumer.accept(message);
        }
      }
      System.out.println("receiver is closing");
    } catch (IOException e) {
      System.out.println("Exception caught when trying to listen on port " + listeningPort + " or listening for a connection");
      e.printStackTrace();
    }
    readSemaphore.release();
    System.out.println("receiver thread is terminating now");
  }

}
