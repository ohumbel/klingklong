package st.extreme.klingklong;

/**
 * The interface for the sender of the endpoint
 */
public interface Sender {

  /**
   * Signal an endpoint sends if it wants to quit the conversation
   */
  static final String STOP_SIGNAL = "__^ 3B@?)8Eu6.x6u2t?7?cw#2e+2W8)P?R3 ^__";

  /**
   * Start the sender
   */
  void start();

  /**
   * Send a message to the other endpoint
   * 
   * @param message the message.
   */
  void send(String message);

  /**
   * Close the sender
   */
  void close();

}
