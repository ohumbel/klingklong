package st.extreme.klingklong;

import java.nio.charset.Charset;
import java.util.Base64;

final class Message {

  private static final Charset UTF_8 = Charset.forName("UTF-8");

  public static String forSending(String msg) {
    return Base64.getEncoder().encodeToString(msg.getBytes(UTF_8));
  }

  public static String afterReceiving(String msg) {
    return new String(Base64.getDecoder().decode(msg), UTF_8);
  }
}
