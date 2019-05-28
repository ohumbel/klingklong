package st.extreme.klingklong;

import java.net.InetAddress;

public class ConfigurationBuilder {

  private int localPort;
  private InetAddress remoteHost;
  private int remotePort;

  public static ConfigurationBuilder create() {
    return new ConfigurationBuilder();
  }

  private ConfigurationBuilder() {
    // no direct instantiation
  }

  public ConfigurationBuilder withLocalPort(int localPort) {
    this.localPort = localPort;
    return this;
  }

  public ConfigurationBuilder withRemoteHost(InetAddress remoteHost) {
    this.remoteHost = remoteHost;
    return this;
  }

  public ConfigurationBuilder withRemotePort(int remotePort) {
    this.remotePort = remotePort;
    return this;
  }

  public Configuration build() {
    return new ConfigurationImpl(localPort, remoteHost, remotePort);
  }

}
