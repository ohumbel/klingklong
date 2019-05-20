package st.extreme.klingklong;

public class ConnectionError extends Exception {
  private static final long serialVersionUID = 1L;

  public ConnectionError(String message, Throwable cause) {
    super(message, cause);
  }
}
