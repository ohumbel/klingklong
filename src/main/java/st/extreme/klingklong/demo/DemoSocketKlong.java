package st.extreme.klingklong.demo;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Listens on kling port, sends on klong port.
 */
public class DemoSocketKlong {

  protected static final int DEFAULT_KLONG_PORT = 8539;

  public static void main(String[] args) {
    try {
      DemoReceiver receiver = new DemoReceiver(DemoSocketKling.DEFAUT_KLING_PORT);
      receiver.start();
      DemoSender sender = new DemoSender(InetAddress.getLocalHost(), DEFAULT_KLONG_PORT);
      sender.start();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

}
