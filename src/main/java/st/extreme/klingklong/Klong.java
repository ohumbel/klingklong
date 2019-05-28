package st.extreme.klingklong;

import java.net.UnknownHostException;

public class Klong extends EndpointImpl {

  /**
   * Create the default Klong endpoint, using its default configuration
   * 
   * @return the Klong endpoint instance
   * 
   * @throws ConfigurationException in case of a mismatched configuration.
   * @throws UnknownHostException in case the remote host is not known.
   */
  public static Klong create() throws ConfigurationException, UnknownHostException {
    Klong klong = new Klong();
    klong.configure(Klong.defaultConfiguration());
    return klong;
  }

  private static Configuration defaultConfiguration() {
    return null;
  }

}
