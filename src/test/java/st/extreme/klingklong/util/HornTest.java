package st.extreme.klingklong.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HornTest {

  private Path originalPropertiesPath;
  private List<String> originalLines;
  private String originalTemeratureProperty;
  private TestFormattingListener formattingListener;

  @Before
  public void setUp() throws Exception {
    URL originalPropertiesURL = Horn.class.getResource("/klingklong.properties");
    assertNotNull(originalPropertiesURL);
    originalPropertiesPath = Paths.get(originalPropertiesURL.toURI());
    originalLines = Files.readAllLines(originalPropertiesPath);
    originalTemeratureProperty = System.getProperty(Horn.TEMPERATURE_PROPERTY_NAME);
    formattingListener = new TestFormattingListener();
    HornFormatter.addFormattingListener(formattingListener);
  }

  @After
  public void tearDown() throws Exception {
    if (originalTemeratureProperty == null) {
      System.getProperties().remove(Horn.TEMPERATURE_PROPERTY_NAME);
    } else {
      System.setProperty(Horn.TEMPERATURE_PROPERTY_NAME, originalTemeratureProperty);
    }
    HornFormatter.removeAllListeners();
    Files.write(originalPropertiesPath, originalLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
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

  @Test
  public void testLoadProperties() throws IOException {
    Horn.loadProperties();
    assertEquals(Temperature.COSY.name(), System.getProperty(Horn.TEMPERATURE_PROPERTY_NAME));
  }

  @Test
  public void testLoadProperties_HOT() throws IOException {
    List<String> newLines = new ArrayList<>();
    originalLines.forEach(line -> {
      if (line.startsWith(Horn.TEMPERATURE_PROPERTY_NAME)) {
        line = Horn.TEMPERATURE_PROPERTY_NAME.concat("=").concat(Temperature.HOT.name());
      }
      newLines.add(line);
    });
    assertEquals(originalLines.size(), newLines.size());
    Files.write(originalPropertiesPath, newLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    Horn.loadProperties();
    assertEquals(Temperature.HOT.name(), System.getProperty(Horn.TEMPERATURE_PROPERTY_NAME));
  }

  @Test
  public void testLoadProperties_System_before_builtin() {
    System.setProperty(Horn.TEMPERATURE_PROPERTY_NAME, Temperature.FROZEN.name());
    Horn.loadProperties();
    assertEquals(Temperature.FROZEN.name(), System.getProperty(Horn.TEMPERATURE_PROPERTY_NAME));
  }

  @Test
  public void testLoadProperties_Exception() throws Exception {
    System.setProperty(Horn.TEMPERATURE_PROPERTY_NAME, Temperature.HOT.name());
    List<String> newLines = new ArrayList<>();
    originalLines.forEach(line -> newLines.add(line));
    assertEquals(originalLines.size(), newLines.size());
    newLines.add("test.breaking.property=\\u2");
    Files.write(originalPropertiesPath, newLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    Horn.loadProperties();
    assertTrue(formattingListener.contains("unable to load klinklong.properties"));
  }

  @Test
  public void testSetSystemProperty() {
    final String testSystemProperetyName = "st.extreme.klingklong.test.property";
    try {
      Horn.setSystemProperty(testSystemProperetyName, "initial");
      assertEquals("initial", System.getProperty(testSystemProperetyName));
      Horn.setSystemProperty(testSystemProperetyName, "overwrite");
      assertEquals("initial", System.getProperty(testSystemProperetyName));
    } finally {
      System.getProperties().remove(testSystemProperetyName);
    }
  }

}
