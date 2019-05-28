package st.extreme.klingklong;

import java.net.InetAddress;
import java.net.UnknownHostException;

final public class Kling extends EndpointImpl {

  static final int DEFAUT_KLING_PORT = 8533;

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

  private static Configuration defaultConfiguration() throws UnknownHostException {
    return ConfigurationBuilder.create().withLocalPort(DEFAUT_KLING_PORT).withRemoteHost(InetAddress.getLocalHost())
        .withRemotePort(Klong.DEFAULT_KLONG_PORT).build();
  }

}
