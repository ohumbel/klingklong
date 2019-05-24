package st.extreme.klingklong.socket;

import java.net.InetAddress;
import java.net.UnknownHostException;

import st.extreme.klingklong.Receiver;
import st.extreme.klingklong.Sender;

/**
 * Listens on klong port, sends on kling port
 */
public class DemoSocketKling {

  protected static final int DEFAUT_KLING_PORT = 8533;

  public static void main(String[] args) {
    try {
      Receiver receiver = new Receiver(DemoSocketKlong.DEFAULT_KLONG_PORT);
      receiver.start();
      Sender sender = new Sender(InetAddress.getLocalHost(), DEFAUT_KLING_PORT);
      sender.start();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

}
