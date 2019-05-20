package st.extreme.klingklong;
public class Kling implements Klingklong {

  /**
   * Create the default kling endpoint, using its default configuration
   * 
   * @throws ConfigurationException
   */
  public static Kling create() throws ConfigurationException {
    Kling kling = new Kling();
    kling.configure(Kling.defaultConfiguration());
    return kling;
  }

  private static Configuration defaultConfiguration() {
    return null;
  }

  @Override
  public void close() throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void configure(Configuration configuration) throws ConfigurationException {
    // TODO Auto-generated method stub

  }

  @Override
  public void connect() throws ConnectionError {
    // TODO Auto-generated method stub

  }

  @Override
  public void send(String message) {
    // TODO Auto-generated method stub

  }

  @Override
  public void addMessageListener(MessageListener messageListener) {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeMessageListener(MessageListener messageListener) {
    // TODO Auto-generated method stub

  }

}
