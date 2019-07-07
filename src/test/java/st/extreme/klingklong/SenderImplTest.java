package st.extreme.klingklong;

import static org.junit.Assert.assertTrue;
import static st.extreme.klingklong.util.PropertyLoader.TEMPERATURE_PROPERTY_NAME;
import static st.extreme.klingklong.util.Temperature.HOT;

import java.net.InetAddress;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import st.extreme.klingklong.util.HornFormatter;
import st.extreme.klingklong.util.TestFormattingListener;

public class SenderImplTest {

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

  @Test
  public void testInterruptedException() throws Exception {
    Sender sender = new SenderImpl(InetAddress.getLocalHost(), 8543, 8654, new Semaphore(0, true));
    sender.start();
    TimeUnit.MILLISECONDS.sleep(5500); // wait > 5 seconds to cover the honk condition
    ((SenderImpl) sender).interrupt(); // provoke an interrupted exception during sleep
    TimeUnit.MICROSECONDS.sleep(1300);
    assertTrue(formattingListener.contains("waiting for remote"));
    assertTrue(formattingListener.contains("connection error with remote host"));
  }

}
