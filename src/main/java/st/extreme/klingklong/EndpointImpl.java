package st.extreme.klingklong;

import static st.extreme.klingklong.util.Horn.honk;
import static st.extreme.klingklong.util.Horn.Temperature.COSY;

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
    // TODO check for invalid configuration
    receiver = new Receiver(configuration.getLocalPort(), this::messageReceived, receiverSemaphore);
    sender = new Sender(configuration.getRemoteHost(), configuration.getRemotePort(), configuration.getLocalPort(), senderSemaphore);
  }

  @Override
  final public void connect() throws ConnectionError {
    honk(COSY, "endpoint is connecting ...");
    receiver.start();
    sender.start();
    try {
      senderSemaphore.acquire();
      receiverSemaphore.release();
    } catch (InterruptedException e) {
      throw new ConnectionError("Error while waiting for sender", e);
    }
  }

  @Override
  final public void send(String message) {
    sender.send(message);
  }

  @Override
  final public void addMessageListener(MessageListener messageListener) {
    messageListeners.add(messageListener);
  }

  @Override
  final public void removeMessageListener(MessageListener messageListener) {
    messageListeners.remove(messageListener);
  }

  @Override
  final public void close() throws Exception {
    // remove message listeners
    messageListeners.clear();
    // stop sender (this implicitly stops the remote)
    sender.close();
    // force closing of receiver
    if (receiver.isAlive()) {
      receiver.interrupt(); // this magically lets the receiver read pending lines
      receiverSemaphore.acquire();
    }
    honk(COSY, "endpoint is now closed");
  }

  final private void messageReceived(String message) {
    messageListeners.forEach(listener -> listener.onMessage(message));
  }

}
