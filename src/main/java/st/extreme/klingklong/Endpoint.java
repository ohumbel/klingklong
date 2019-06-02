package st.extreme.klingklong;

import java.net.UnknownHostException;

/**
 * The interface for an endpoint
 * <p>
 * Since an endpoint is {@code AutoCloseable}, it is <strong>mandatory</strong> to use it in a {@code try} with resources block.
 */
public interface Endpoint extends AutoCloseable {

  /**
   * Configure with remote and local host names and port numbers.
   * 
   * @param configuration the configuration
   * 
   * @throws ConfigurationException in case of a mismatched configuration.
   * @throws UnknownHostException in case the remote host is not known.
   */
  public void configure(Configuration configuration) throws ConfigurationException, UnknownHostException;

  /**
   * Establish the connection.
   * <p>
   * This method blocks until the remote accepts the connection.
   * 
   * @throws ConnectionError if the remote endpoint cannot be reached.
   */
  public void connect() throws ConnectionError;

  /**
   * Send a message.
   * 
   * @param message the message in plain text
   */
  public void send(String message);

  /**
   * Add a message listener.
   * 
   * @param messageListener the new message listener to be added
   */
  public void addMessageListener(MessageListener messageListener);

  /**
   * remove a message listener.
   * 
   * @param messageListener the message listener to be removed
   */
  public void removeMessageListener(MessageListener messageListener);

}
