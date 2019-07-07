package st.extreme.klingklong.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static st.extreme.klingklong.util.PropertyLoader.TEMPERATURE_PROPERTY_NAME;
import static st.extreme.klingklong.util.Temperature.COSY;
import static st.extreme.klingklong.util.Temperature.FROZEN;
import static st.extreme.klingklong.util.Temperature.HOT;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HornTest {
  private TestFormattingListener formattingListener;

  @Before
  public void setUp() throws Exception {
    formattingListener = new TestFormattingListener();
    HornFormatter.addFormattingListener(formattingListener);
  }

  @After
  public void tearDown() throws Exception {
    HornFormatter.removeAllListeners();
  }

  @Test
  public void testSystemTemperature() {
    System.setProperty(TEMPERATURE_PROPERTY_NAME, FROZEN.name());
    assertEquals(FROZEN, Horn.systemTemperature());

    System.setProperty(TEMPERATURE_PROPERTY_NAME, COSY.name());
    assertEquals(COSY, Horn.systemTemperature());

    System.setProperty(TEMPERATURE_PROPERTY_NAME, HOT.name());
    assertEquals(HOT, Horn.systemTemperature());
  }

  @Test
  public void testHonk_accept_HOT() {
    System.setProperty(TEMPERATURE_PROPERTY_NAME, HOT.name());
    Horn.honk(HOT, "hot message");
    assertTrue(formattingListener.contains("hot message"));
    Horn.honk(COSY, "cosy message");
    assertTrue(formattingListener.contains("cosy message"));
    Horn.honk(FROZEN, "frozen message");
    assertFalse(formattingListener.contains("frozen message"));
  }

  @Test
  public void testHonk_accept_COSY() {
    System.setProperty(TEMPERATURE_PROPERTY_NAME, COSY.name());
    Horn.honk(HOT, "hot message");
    assertFalse(formattingListener.contains("hot message"));
    Horn.honk(COSY, "cosy message");
    assertTrue(formattingListener.contains("cosy message"));
    Horn.honk(FROZEN, "frozen message");
    assertFalse(formattingListener.contains("frozen message"));
  }

  @Test
  public void testHonk_accept_FROZEN() {
    System.setProperty(TEMPERATURE_PROPERTY_NAME, FROZEN.name());
    Horn.honk(HOT, "hot message");
    assertFalse(formattingListener.contains("hot message"));
    Horn.honk(COSY, "cosy message");
    assertFalse(formattingListener.contains("cosy message"));
    Horn.honk(FROZEN, "frozen message");
    assertFalse(formattingListener.contains("frozen message"));
  }

  @Test
  public void testHonkThrowable() {
    System.setProperty(TEMPERATURE_PROPERTY_NAME, COSY.name());
    Horn.honk(COSY, "a message with an exception", new Exception("the exception"));
    String expectedStart = "[klingklong] a message with an exception:\n" + //
        "[klingklong] java.lang.Exception: the exception\n" + //
        "\tat st.extreme.klingklong.util.HornTest.testHonkThrowable(";
    String start = formattingListener.getFormattedMessages().get(0).substring(0, expectedStart.length());
    assertEquals(expectedStart, start);
    assertTrue(start.startsWith(expectedStart));
  }

  @Test
  public void testCreation() {
    assertNotNull(new Horn()); // coverage, we salute you
  }

}
