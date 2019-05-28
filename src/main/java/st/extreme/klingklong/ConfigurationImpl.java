package st.extreme.klingklong;

import java.net.InetAddress;

final class ConfigurationImpl implements Configuration {

  private final int localPort;
  private final InetAddress remoteHost;
  private final int remotePort;

  public ConfigurationImpl(int localPort, InetAddress remoteHost, int remotePort) {
    this.localPort = localPort;
    this.remoteHost = remoteHost;
    this.remotePort = remotePort;
  }

  @Override
  public int getLocalPort() {
    return localPort;
  }

  @Override
  public InetAddress getRemoteHost() {
    return remoteHost;
  }

  @Override
  public int getRemotePort() {
    return remotePort;
  }

}
