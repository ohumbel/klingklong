package st.extreme.klingklong.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HornTest {

  private String originalTemeratureProperty;
  private TestFormattingListener formattingListener;

  @Before
  public void setUp() {
    originalTemeratureProperty = System.getProperty(Horn.TEMPERATURE_PROPERTY_NAME, Temperature.FROZEN.name());
    formattingListener = new TestFormattingListener();
    HornFormatter.addFormattingListener(formattingListener);
  }

  @After
  public void tearDown() {
    System.setProperty(Horn.TEMPERATURE_PROPERTY_NAME, originalTemeratureProperty);
    HornFormatter.removeAllListeners();
  }

  @Test
  public void testLoadProperties() throws IOException {
    Horn.loadProperties();
    assertEquals(Temperature.COSY.name(), System.getProperty(Horn.TEMPERATURE_PROPERTY_NAME));
  }

  @Test
  public void testSystemTemperature() {
    System.setProperty(Horn.TEMPERATURE_PROPERTY_NAME, Temperature.FROZEN.name());
    assertEquals(Temperature.FROZEN, Horn.systemTemperature());

    System.setProperty(Horn.TEMPERATURE_PROPERTY_NAME, Temperature.COSY.name());
    assertEquals(Temperature.COSY, Horn.systemTemperature());

    System.setProperty(Horn.TEMPERATURE_PROPERTY_NAME, Temperature.HOT.name());
    assertEquals(Temperature.HOT, Horn.systemTemperature());
  }

  @Test
  public void testHonk_accept_HOT() {
    System.setProperty(Horn.TEMPERATURE_PROPERTY_NAME, Temperature.HOT.name());
    Horn.honk(Temperature.HOT, "hot message");
    assertTrue(formattingListener.contains("hot message"));
    Horn.honk(Temperature.COSY, "cosy message");
    assertTrue(formattingListener.contains("cosy message"));
    Horn.honk(Temperature.FROZEN, "frozen message");
    assertFalse(formattingListener.contains("frozen message"));
  }

  @Test
  public void testHonk_accept_COSY() {
    System.setProperty(Horn.TEMPERATURE_PROPERTY_NAME, Temperature.COSY.name());
    Horn.honk(Temperature.HOT, "hot message");
    assertFalse(formattingListener.contains("hot message"));
    Horn.honk(Temperature.COSY, "cosy message");
    assertTrue(formattingListener.contains("cosy message"));
    Horn.honk(Temperature.FROZEN, "frozen message");
    assertFalse(formattingListener.contains("frozen message"));
  }

  @Test
  public void testHonk_accept_FROZEN() {
    System.setProperty(Horn.TEMPERATURE_PROPERTY_NAME, Temperature.FROZEN.name());
    Horn.honk(Temperature.HOT, "hot message");
    assertFalse(formattingListener.contains("hot message"));
    Horn.honk(Temperature.COSY, "cosy message");
    assertFalse(formattingListener.contains("cosy message"));
    Horn.honk(Temperature.FROZEN, "frozen message");
    assertFalse(formattingListener.contains("frozen message"));
  }

  @Test
  public void testHonkThrowable() {
    System.setProperty(Horn.TEMPERATURE_PROPERTY_NAME, Temperature.COSY.name());
    Horn.honk(Temperature.COSY, "a message with an exception", new Exception("the exception"));
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
