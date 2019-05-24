package st.extreme.klingklong;

public class Kling extends Endpoint {

  /**
   * Create the default Kling endpoint, using its default configuration
   * 
   * @return the Kling endpoint instance
   * 
   * @throws ConfigurationException in case of a mismatched configuration
   */
  public static Kling create() throws ConfigurationException {
    Kling kling = new Kling();
    kling.configure(Kling.defaultConfiguration());
    return kling;
  }

  private static Configuration defaultConfiguration() {

    return null;
  }

}
