package st.extreme.klingklong.socket;

/**
 * Sends on kling port, listens on klong port
 */
public class DemoSocketKling {

  protected static final int DEFAUT_KLING_PORT = 8533;

  private static final String KLING = "Kling";

  public static void main(String[] args) {
    Sender sender = new Sender(KLING, "localhost", DEFAUT_KLING_PORT);
    sender.run();
  }

}
