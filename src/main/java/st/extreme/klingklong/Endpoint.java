package st.extreme.klingklong;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of an endpoint.
 */
public class Endpoint implements Klingklong {

  private Receiver receiver;
  private Sender sender;
  private List<MessageListener> messageListeners;

  @Override
  // TODO move into constructor ?
  final public void configure(Configuration configuration) throws ConfigurationException, UnknownHostException {
    // TODO check for invalid configuration
    receiver = new Receiver(configuration.getLocalPort(), this::messageReceived);
    sender = new Sender(configuration.getRemoteHost(), configuration.getRemotePort());
    messageListeners = new ArrayList<>();
  }

  @Override
  final public void connect() throws ConnectionError {
    receiver.start();
    sender.start();
    // TODO handshake (or change javadoc)
    // TODO start open loop
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
    // TODO Auto-generated method stub
    // end the open loop
  }

  final private void messageReceived(String message) {
    messageListeners.forEach(listener -> listener.onMessage(message));
  }

}
