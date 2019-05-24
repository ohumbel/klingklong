package st.extreme.klingklong.socket;

import java.net.InetAddress;
import java.net.UnknownHostException;

import st.extreme.klingklong.Receiver;
import st.extreme.klingklong.Sender;

/**
 * Listens on kling port, sends on klong port.
 */
public class DemoSocketKlong {

  protected static final int DEFAULT_KLONG_PORT = 8539;

  public static void main(String[] args) {
    try {
      Receiver receiver = new Receiver(DemoSocketKling.DEFAUT_KLING_PORT);
      receiver.start();
      Sender sender = new Sender(InetAddress.getLocalHost(), DEFAULT_KLONG_PORT);
      sender.start();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

}
