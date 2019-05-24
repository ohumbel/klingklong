package st.extreme.klingklong;

import java.net.InetAddress;

/**
 * The configuration of an endpoint.
 */
public interface Configuration {

  /**
   * Get the local port for the listening socket.
   * 
   * @return the local port
   */
  public int getLocalPort();

  /**
   * Get the remote host.
   * 
   * @return the remote host
   */
  public InetAddress getRemoteHost();

  /**
   * Get the remote port for the sending socket.
   * 
   * @return the remote port
   */
  public int getRemotePort();
}
