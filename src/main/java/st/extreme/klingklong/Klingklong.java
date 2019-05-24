package st.extreme.klingklong;

/**
 * The interface for an endpoint
 */
public interface Klingklong extends AutoCloseable {

  /**
   * Configure with remote and local host names and port numbers.
   * 
   * @param configuration the configuration
   * 
   * @throws ConfigurationException in case of a mismatched configuration.
   */
  public void configure(Configuration configuration) throws ConfigurationException;

  /**
   * Establish the connection.
   * <p>
   * This method blocks until the remote accepts the connection and an initial handshake was successful.
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
