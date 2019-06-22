package st.extreme.klingklong;

import static st.extreme.klingklong.util.Horn.honk;
import static st.extreme.klingklong.util.Temperature.COSY;
import static st.extreme.klingklong.util.Temperature.HOT;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * Implementation of an endpoint.
 */
public class EndpointImpl implements Endpoint {

  private final Set<MessageListener> messageListeners;
  private final Semaphore senderSemaphore;
  private final Semaphore receiverSemaphore;

  private Receiver receiver;
  private Sender sender;

  public EndpointImpl() {
    messageListeners = new HashSet<>();
    senderSemaphore = new Semaphore(0, true);
    receiverSemaphore = new Semaphore(0, true);
  }

  @Override
  final public void configure(Configuration configuration) throws ConfigurationException {
    // TODO check for invalid configuration
    receiver = new ReceiverImpl(configuration.getLocalPort(), this::messageReceived, receiverSemaphore);
    sender = new SenderImpl(configuration.getRemoteHost(), configuration.getRemotePort(), configuration.getLocalPort(), senderSemaphore);
  }

  @Override
  final public void connect() throws ConnectionException {
    honk(COSY, "endpoint is connecting ...");
    receiver.start();
    sender.start();
    try {
      senderSemaphore.acquire();
      receiverSemaphore.release();
    } catch (InterruptedException e) {
      throw new ConnectionException("Error while waiting for sender", e);
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
    honk(HOT, "endpoint is closing");
    // remove message listeners
    messageListeners.clear();
    // stop sender (this implicitly stops the remote)
    if (sender != null) {
      sender.close();
    }
    // close receiver
    if (receiver != null) {
      if (receiver.isAlive()) {
        receiverSemaphore.acquire();
      }
    }
    honk(COSY, "endpoint is now closed");
  }

  final void messageReceived(String message) {
    messageListeners.forEach(listener -> listener.onMessage(message));
  }

}
