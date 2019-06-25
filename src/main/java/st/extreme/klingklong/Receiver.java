package st.extreme.klingklong;

/**
 * The interface for the receiver of the endpoint
 */
public interface Receiver {

  /**
   * Start the receiver
   */
  void start();

  /**
   * Check if the receiver is still alive
   * 
   * @return {@code true} if the receiver is alive, {@code false} otherwise.
   */
  boolean isAlive();

}
