package st.extreme.klingklong;

import static st.extreme.klingklong.util.Horn.honk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

import st.extreme.klingklong.util.Horn.Temperature;

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
    try (ServerSocket serverSocket = new ServerSocket(listeningPort);
        Socket listeningSocket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(listeningSocket.getInputStream()));) {
      try {
        readSemaphore.acquire();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        String message = Message.afterReceiving(inputLine);
        if (Sender.STOP_SIGNAL.equals(message)) {
          break;
        } else {
          messageConsumer.accept(message);
        }
      }
    } catch (IOException e) {
      honk(Temperature.COSY, "Exception caught when trying to listen on port " + listeningPort + " or listening for a connection");
      e.printStackTrace();
    }
    readSemaphore.release();
    honk(Temperature.COSY, "receiver thread is terminating now");
  }

}
