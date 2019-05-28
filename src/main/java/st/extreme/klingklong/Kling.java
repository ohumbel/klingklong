package st.extreme.klingklong;

import java.net.UnknownHostException;

public class Kling extends EndpointImpl {

  /**
   * Create the default Kling endpoint, using its default configuration
   * 
   * @return the Kling endpoint instance
   * 
   * @throws ConfigurationException in case of a mismatched configuration
   * @throws UnknownHostException in case the remote host is not known
   */
  public static Kling create() throws ConfigurationException, UnknownHostException {
    Kling kling = new Kling();
    kling.configure(Kling.defaultConfiguration());
    return kling;
  }

  private static Configuration defaultConfiguration() {

    return null;
  }

}
