package st.extreme.klingklong;

public class ConfigurationException extends Exception {
  private static final long serialVersionUID = 3397987269929507504L;

  public ConfigurationException(Reason reason) {
    this(reason.getErrorMessage());
  }

  private ConfigurationException(String message) {
    super(message);
  }

  private static enum Reason {
    SAME_PORT("The same port on the same host ist not allowed."), //
    DUPLICATE("The exact same configuration is already present."), //
    ALREADY("This endpoint is already configured");

    private final String errorMessage;

    Reason(String errorMessage) {
      this.errorMessage = errorMessage;
    }

    String getErrorMessage() {
      return errorMessage;
    }
  }
}
