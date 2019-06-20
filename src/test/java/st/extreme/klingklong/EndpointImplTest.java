package st.extreme.klingklong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class EndpointImplTest {

  @Test
  @Ignore
  public void testEndpointImpl() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testConfigure() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testConnect() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testSend() {
    fail("Not yet implemented");
  }

  @Test
  public void testAddMessageListener() throws Exception {
    TestMessageListener messageListener1 = new TestMessageListener();
    TestMessageListener messageListener2 = new TestMessageListener();
    try (EndpointImpl endpoint = new EndpointImpl()) {
      endpoint.addMessageListener(messageListener1);
      endpoint.addMessageListener(messageListener2);
      endpoint.addMessageListener(messageListener2);
      endpoint.messageReceived("message1");
      endpoint.messageReceived("message2");
      assertMessages(messageListener1);
      assertMessages(messageListener2);
    }
  }

  private void assertMessages(TestMessageListener messageListener) {
    List<String> messages = messageListener.getMessages();
    assertEquals(2, messages.size());
    assertEquals("message1", messages.get(0));
    assertEquals("message2", messages.get(1));
  }

  @Test
  public void testRemoveMessageListener() throws Exception {
    TestMessageListener messageListener1 = new TestMessageListener();
    TestMessageListener messageListener2 = new TestMessageListener();
    TestMessageListener messageListener3 = new TestMessageListener();
    try (EndpointImpl endpoint = new EndpointImpl()) {
      endpoint.addMessageListener(messageListener1);
      endpoint.addMessageListener(messageListener2);
      endpoint.removeMessageListener(messageListener3);
      endpoint.removeMessageListener(messageListener2);
      endpoint.removeMessageListener(messageListener1);
      endpoint.messageReceived("message");
      assertEquals(0, messageListener1.getMessages().size());
      assertEquals(0, messageListener2.getMessages().size());
      assertEquals(0, messageListener3.getMessages().size());
    }
  }

  @Test
  @Ignore
  public void testClose() {
    fail("Not yet implemented");
  }

  private static final class TestMessageListener implements MessageListener {
    private final List<String> messages;

    TestMessageListener() {
      messages = new ArrayList<>();
    }

    @Override
    public void onMessage(String message) {
      messages.add(message);
    }

    List<String> getMessages() {
      return messages;
    }
  }
}
