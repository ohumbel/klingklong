package st.extreme.klingklong;

public class Klong implements Klingklong {

  /**
   * Create the default klong endpoint, using its default configuration
   * 
   * @throws ConfigurationException
   */
  public static Klong create() throws ConfigurationException {
    Klong klong = new Klong();
    klong.configure(Klong.defaultConfiguration());
    return klong;
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
