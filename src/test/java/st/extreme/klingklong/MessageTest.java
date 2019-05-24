package st.extreme.klingklong;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import st.extreme.klingklong.Message;

public class MessageTest {

  @Test
  public void testForSending() {
    assertEquals("cw==", Message.forSending("s"));
    String msg = "a message\n\ton two lines";
    assertEquals("YSBtZXNzYWdlCglvbiB0d28gbGluZXM=", Message.forSending(msg));
  }

  @Test
  public void testAfterReceiving() {
    assertEquals("s", Message.afterReceiving("cw=="));
    String expected = "a message\n\ton two lines";
    assertEquals(expected, Message.afterReceiving("YSBtZXNzYWdlCglvbiB0d28gbGluZXM="));
  }

}
