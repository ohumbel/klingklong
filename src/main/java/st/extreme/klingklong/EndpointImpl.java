package st.extreme.klingklong;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation of an endpoint.
 */
public class EndpointImpl implements Endpoint {

  private Receiver receiver;
  private Sender sender;
  private List<MessageListener> messageListeners;
  private AtomicBoolean running = new AtomicBoolean(false);

  @Override
  // TODO move into constructor ?
  final public void configure(Configuration configuration) throws ConfigurationException, UnknownHostException {
    System.out.println("creating endpoint");
    // TODO check for invalid configuration
    receiver = new Receiver(configuration.getLocalPort(), this::messageReceived);
    sender = new Sender(configuration.getRemoteHost(), configuration.getRemotePort(), configuration.getLocalPort());
    messageListeners = new ArrayList<>();
  }

  @Override
  final public void connect() throws ConnectionError {
    System.out.println("connecting endpoint...");
    if (!isRunning()) {
      receiver.start();
      sender.start();
      // TODO handshake (or change javadoc)
      running.set(true);
    }
  }

  @Override
  final public void send(String message) {
    if (isRunning()) {
      sender.send(message);
    }
  }

  @Override
  final public void addMessageListener(MessageListener messageListener) {
    if (isRunning()) {
      System.out.println("adding message listener");
      messageListeners.add(messageListener);
    }
  }

  @Override
  final public void removeMessageListener(MessageListener messageListener) {
    if (isRunning()) {
      System.out.println("removing message listener");
      messageListeners.remove(messageListener);
    }
  }

  @Override
  final public void close() throws Exception {
    System.out.println("closing endpoint");
    // TODO several things
    // end the open loop
    running.set(false);

    // remove message listeners
    messageListeners.clear();

    // stop sender (this implicitly stops the remote and closes the receiver as well)
    sender.close();
  }

  final private void messageReceived(String message) {
    if (isRunning()) {
      if (Sender.STOP_SIGNAL.equals(message)) {
        System.out.println("endpoint received STOP signal");
      } else {
        System.out.println(String.format("endpoint received message %s", message));
      }
      messageListeners.forEach(listener -> listener.onMessage(message));
    }
  }

  final boolean isRunning() {
    return running.get();
  }
}
