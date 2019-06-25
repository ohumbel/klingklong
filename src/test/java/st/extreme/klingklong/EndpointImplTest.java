package st.extreme.klingklong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Ignore;
import org.junit.Test;

public class EndpointImplTest {

  @Test
  @Ignore
  public void testConfigure() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testSend() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testClose() {
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
  public void testConnect_InterruptedException() throws Exception {
    EndpointImpl endpoint = new EndpointImpl();
    injectTestSenderAndReceiver(endpoint);
    final AtomicBoolean connectionException = new AtomicBoolean(false);
    Thread connectorThread = new Thread() {
      @Override
      public void run() {
        try {
          endpoint.connect();
          fail("ConnectionException expected");
        } catch (ConnectionException e) {
          connectionException.set(true);
          ;
          assertEquals("Error while waiting for sender", e.getMessage());
        }
      }
    };
    connectorThread.start();
    TimeUnit.MILLISECONDS.sleep(500);
    connectorThread.interrupt();
    TimeUnit.MILLISECONDS.sleep(500);
    assertTrue(connectionException.get());
  }

  private void injectTestSenderAndReceiver(EndpointImpl endpoint) throws Exception {
    Field senderField = EndpointImpl.class.getDeclaredField("sender");
    Field receiverField = EndpointImpl.class.getDeclaredField("receiver");
    assertNotNull(senderField);
    assertNotNull(receiverField);
    senderField.setAccessible(true);
    receiverField.setAccessible(true);
    senderField.set(endpoint, new TestSender());
    receiverField.set(endpoint, new TestReceiver());
  }

  private static final class TestSender implements Sender {
    @Override
    public void start() {
    }

    @Override
    public void send(String message) {
    }

    @Override
    public void close() {
    }
  }

  private static final class TestReceiver implements Receiver {
    @Override
    public void start() {
    }

    @Override
    public boolean isAlive() {
      return false;
    }
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
