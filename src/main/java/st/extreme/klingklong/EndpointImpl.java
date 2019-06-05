package st.extreme.klingklong;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Implementation of an endpoint.
 */
public class EndpointImpl implements Endpoint {

  private final List<MessageListener> messageListeners;
  private final Semaphore senderSemaphore;
  private final Semaphore receiverSemaphore;

  private Receiver receiver;
  private Sender sender;

  public EndpointImpl() {
    messageListeners = new ArrayList<>();
    senderSemaphore = new Semaphore(0, true);
    receiverSemaphore = new Semaphore(0, true);
  }

  @Override
  final public void configure(Configuration configuration) throws ConfigurationException, UnknownHostException {
    System.out.println("endpoint configures");
    // TODO check for invalid configuration
    receiver = new Receiver(configuration.getLocalPort(), this::messageReceived, receiverSemaphore);
    sender = new Sender(configuration.getRemoteHost(), configuration.getRemotePort(), configuration.getLocalPort(), senderSemaphore);
  }

  @Override
  final public void connect() throws ConnectionError {
    System.out.println("endpoint connecting ...");
    receiver.start();
    sender.start();
    try {
      senderSemaphore.acquire();
      System.out.println("endpoint acquired the sender permit and releases receiver now");
      receiverSemaphore.release();
    } catch (InterruptedException e) {
      throw new ConnectionError("Error while waiting for sender", e);
    }
    System.out.println("endpoint is now connected");
  }

  @Override
  final public void send(String message) {
    sender.send(message);
  }

  @Override
  final public void addMessageListener(MessageListener messageListener) {
    System.out.println("endpoint adding message listener");
    messageListeners.add(messageListener);
  }

  @Override
  final public void removeMessageListener(MessageListener messageListener) {
    System.out.println("endpoint removing message listener");
    messageListeners.remove(messageListener);
  }

  @Override
  final public void close() throws Exception {
    System.out.println("closing endpoint");
    // remove message listeners
    messageListeners.clear();
    // stop sender (this implicitly stops the remote)
    sender.close();
    // force closing of receiver
    if (receiver.isAlive()) {
      receiver.interrupt(); // this magically lets the receiver read pending lines
      receiverSemaphore.acquire();
    }
    System.out.println("endpoint is now closed");
  }

  final private void messageReceived(String message) {
    System.out.println(String.format("endpoint received message '%s'", message));
    messageListeners.forEach(listener -> listener.onMessage(message));
  }

}
