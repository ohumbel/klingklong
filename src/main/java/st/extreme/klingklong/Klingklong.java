package st.extreme.klingklong;

/**
 * TODO: add description
 *
 */
public interface Klingklong extends AutoCloseable {

  /**
   * Configure with remote and local host names and port numbers.
   * 
   * @param configuration
   * @throws ConfigurationException
   */
  public void configure(Configuration configuration) throws ConfigurationException;

  /**
   * Establish the connection.
   * <p>
   * This method blocks until the remote accepts the connection and an initial handshake was successful.
   */
  public void connect() throws ConnectionError;

  /**
   * Send a message.
   * 
   * @param message
   */
  public void send(String message);

  /**
   * Add a message listener.
   * 
   * @param messageListener
   */
  public void addMessageListener(MessageListener messageListener);

  /**
   * remove a message listener.
   * 
   * @param messageListener
   */
  public void removeMessageListener(MessageListener messageListener);

}
