package st.extreme.klingklong;

import static st.extreme.klingklong.util.Horn.honk;
import static st.extreme.klingklong.util.Temperature.COSY;
import static st.extreme.klingklong.util.Temperature.HOT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

public class ReceiverImpl extends Thread implements Receiver {
  private final int listeningPort;
  private final Consumer<String> messageConsumer;
  private final Semaphore readSemaphore;

  public ReceiverImpl(int listeningPort, Consumer<String> messageConsumer, Semaphore readSemaphore) throws UnknownHostException {
    this.listeningPort = listeningPort;
    this.messageConsumer = messageConsumer;
    this.readSemaphore = readSemaphore;
  }

  @Override
  public void run() {
    try (ServerSocket serverSocket = new ServerSocket(listeningPort);
        Socket listeningSocket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(listeningSocket.getInputStream()));) {
      readSemaphore.acquireUninterruptibly();
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        String message = Message.afterReceiving(inputLine);
        if (Sender.STOP_SIGNAL.equals(message)) {
          honk(HOT, "receiver got STOP signal");
          break;
        } else {
          messageConsumer.accept(message);
        }
      }
    } catch (IOException e) {
      honk(COSY, "Exception caught when trying to listen on port " + listeningPort + " or listening for a connection");
      e.printStackTrace();
    }
    readSemaphore.release();
    honk(COSY, "receiver thread is terminating now");
  }

}
