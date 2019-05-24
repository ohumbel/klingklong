package st.extreme.klingklong;

public class Klong extends Endpoint {

  /**
   * Create the default Klong endpoint, using its default configuration
   * 
   * @return the Klong endpoint instance
   * 
   * @throws ConfigurationException in case of a mismatched configuration
   */
  public static Klong create() throws ConfigurationException {
    Klong klong = new Klong();
    klong.configure(Klong.defaultConfiguration());
    return klong;
  }

  private static Configuration defaultConfiguration() {
    return null;
  }

}
