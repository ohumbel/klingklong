package st.extreme.klingklong;

import java.net.UnknownHostException;

/**
 * Implementation of an endpoint.
 */
public class Endpoint implements Klingklong {

  private Receiver receiver;
  private Sender sender;

  @Override
  final public void configure(Configuration configuration) throws ConfigurationException, UnknownHostException {
    // TODO check for invalid configuration
    receiver = new Receiver(configuration.getLocalPort());
    sender = new Sender(configuration.getRemoteHost(), configuration.getRemotePort());
  }

  @Override
  final public void connect() throws ConnectionError {
    receiver.start();
    sender.start();
    // TODO handshake
  }

  @Override
  final public void send(String message) {
    // TODO Auto-generated method stub

  }

  @Override
  final public void addMessageListener(MessageListener messageListener) {
    // TODO Auto-generated method stub

  }

  @Override
  final public void removeMessageListener(MessageListener messageListener) {
    // TODO Auto-generated method stub

  }

  @Override
  final public void close() throws Exception {
    // TODO Auto-generated method stub

  }

}
