package st.extreme.klingklong;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation of an endpoint.
 */
public class EndpointImpl implements Endpoint {

  private final List<MessageListener> messageListeners;

  // TODO this should be global - receiver should not accept until everybody is up and running...
  private final AtomicBoolean running;

  private Receiver receiver;
  private Sender sender;

  public EndpointImpl() {
    messageListeners = new ArrayList<>();
    running = new AtomicBoolean(false);
  }

  @Override
  final public void configure(Configuration configuration) throws ConfigurationException, UnknownHostException {
    System.out.println("endpoint configures");
    // TODO check for invalid configuration
    receiver = new Receiver(configuration.getLocalPort(), this::messageReceived);
    sender = new Sender(configuration.getRemoteHost(), configuration.getRemotePort(), configuration.getLocalPort(), this::setRunning);
  }

  @Override
  final public void connect() throws ConnectionError {
    System.out.println("endpoint connecting ...");
    if (!isRunning()) {
      receiver.start();
      sender.start();
      // TODO use a semaphore here, otherwise messages are coming in before this method is terminated
      while (!isRunning()) {
        // wait for the sender callback to set running to true
        try {
          TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
          throw new ConnectionError("Error while waiting for sender", e);
        }
      }
      System.out.println("endpoint is now connected");
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
    // terminate the running loop
    running.set(false);
    // remove message listeners
    messageListeners.clear();
    // stop sender (this implicitly stops the remote and closes the receiver if necessary)
    sender.close();
  }

  final private void messageReceived(String message) {
    if (isRunning()) {
      System.out.println(String.format("endpoint received message '%s'", message));
      messageListeners.forEach(listener -> listener.onMessage(message));
    }
  }

  final private boolean isRunning() {
    return running.get();
  }

  final private void setRunning(Boolean newRunning) {
    System.out.println(String.format("endpoint sets running to %s", newRunning.toString()));
    running.set(newRunning.booleanValue());
  }

}
