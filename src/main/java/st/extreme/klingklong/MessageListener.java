package st.extreme.klingklong;

public interface MessageListener {

  /**
   * Notify that a message was received.
   * 
   * @param message the message in plain text
   */
  public void onMessage(String message);
}
