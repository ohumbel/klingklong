package st.extreme.klingklong;

public interface MessageListener {

  /**
   * Notify that a message was received.
   * 
   * @param message
   */
  public void onMessage(String message);
}
