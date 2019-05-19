package st.extreme.klingklong.socket;

/**
 * Listens on klong port, sends on kling port
 */
public class DemoSocketKling {

  protected static final int DEFAUT_KLING_PORT = 8533;

  private static final String KLING = "Kling/" + DEFAUT_KLING_PORT;

  public static void main(String[] args) {
    Receiver receiver = new Receiver(KLING, DemoSocketKlong.DEFAULT_KLONG_PORT);
    receiver.start();
    Sender sender = new Sender(KLING, "localhost", DEFAUT_KLING_PORT);
    sender.start();
  }

}
