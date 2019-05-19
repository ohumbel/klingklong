package st.extreme.klingklong.socket;

/**
 * Listens on kling port, sends on klong port.
 */
public class DemoSocketKlong {

  protected static final int DEFAULT_KLONG_PORT = 8539;

  private static final String KLONG = "Klong/" + DEFAULT_KLONG_PORT;

  public static void main(String[] args) {
    Receiver receiver = new Receiver(KLONG, DemoSocketKling.DEFAUT_KLING_PORT);
    receiver.start();
    Sender sender = new Sender(KLONG, "localhost", DEFAULT_KLONG_PORT);
    sender.start();
  }

}
