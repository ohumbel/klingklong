package st.extreme.klingklong.demo;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Listens on klong port, sends on kling port
 */
public class DemoSocketKling {

  protected static final int DEFAUT_KLING_PORT = 8533;

  public static void main(String[] args) {
    try {
      DemoReceiver receiver = new DemoReceiver(DemoSocketKlong.DEFAULT_KLONG_PORT);
      receiver.start();
      DemoSender sender = new DemoSender(InetAddress.getLocalHost(), DEFAUT_KLING_PORT);
      sender.start();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

}
