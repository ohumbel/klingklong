package st.extreme.klingklong;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static st.extreme.klingklong.util.PropertyLoader.TEMPERATURE_PROPERTY_NAME;
import static st.extreme.klingklong.util.Temperature.HOT;

import java.net.ServerSocket;
import java.util.concurrent.Semaphore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import st.extreme.klingklong.util.HornFormatter;
import st.extreme.klingklong.util.TestFormattingListener;

public class ReceiverImplTest {

  private String originalTemeratureProperty;
  private TestFormattingListener formattingListener;

  @Before
  public void setUp() {
    originalTemeratureProperty = System.getProperty(TEMPERATURE_PROPERTY_NAME);
    System.setProperty(TEMPERATURE_PROPERTY_NAME, HOT.name());
    formattingListener = new TestFormattingListener();
    HornFormatter.addFormattingListener(formattingListener);
  }

  @After
  public void tearDown() {
    if (originalTemeratureProperty == null) {
      System.getProperties().remove(TEMPERATURE_PROPERTY_NAME);
    } else {
      System.setProperty(TEMPERATURE_PROPERTY_NAME, originalTemeratureProperty);
    }
    HornFormatter.removeAllListeners();
  }

  /**
   * By blocking the socket we provoke a {@code BindException} (address already in use) <br>
   * at the very beginning of the {@code run()} method.
   * <p>
   * This allows us to cover the catch clause.
   * 
   * @throws Exception either from the receiver or from the semaphore
   */
  @Test
  public void testSocketException() throws Exception {
    final int doubleUsedPort = 8834;
    ServerSocket doubleUsedSocket = new ServerSocket(doubleUsedPort);
    assertNotNull(doubleUsedSocket);
    Semaphore receiverSemaphore = new Semaphore(0, true);
    Receiver receiver = new ReceiverImpl(doubleUsedPort, this::consumeMessage, receiverSemaphore);
    receiver.start();
    receiverSemaphore.acquire();
    assertTrue(formattingListener.contains("Exception caught when trying to listen"));
  }

  void consumeMessage(String message) {
  }
}
