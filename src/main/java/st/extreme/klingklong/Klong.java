package st.extreme.klingklong;

import java.net.InetAddress;
import java.net.UnknownHostException;

final public class Klong extends EndpointImpl {

  static final int DEFAULT_KLONG_PORT = 8539;

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

  private static Configuration defaultConfiguration() throws UnknownHostException {
    return ConfigurationBuilder.create().withLocalPort(DEFAULT_KLONG_PORT).withRemoteHost(InetAddress.getLocalHost())
        .withRemotePort(Kling.DEFAUT_KLING_PORT).build();
  }

}
