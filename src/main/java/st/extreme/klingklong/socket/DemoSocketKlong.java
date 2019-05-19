package st.extreme.klingklong.socket;

/**
 * Sends on klong port, listens on kling port
 */
public class DemoSocketKlong {

  protected static final int DEFAULT_KLONG_PORT = 8539;

  private static final String KLONG = "Klong";

  public static void main(String[] args) {
    Receiver receiver = new Receiver(KLONG, DemoSocketKling.DEFAUT_KLING_PORT);
    receiver.run();
  }

}
