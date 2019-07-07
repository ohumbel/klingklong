package st.extreme.klingklong.util;

import static org.junit.Assert.assertEquals;
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

public class PropertyLoaderTest {

  private Path originalPropertiesPath;
  private List<String> originalLines;
  private String originalTemeratureProperty;
  private TestFormattingListener formattingListener;

  @Before
  public void setUp() throws Exception {
    URL originalPropertiesURL = PropertyLoader.class.getResource("/klingklong.properties");
    assertNotNull(originalPropertiesURL);
    originalPropertiesPath = Paths.get(originalPropertiesURL.toURI());
    originalLines = Files.readAllLines(originalPropertiesPath);
    originalTemeratureProperty = System.getProperty(PropertyLoader.TEMPERATURE_PROPERTY_NAME);
    formattingListener = new TestFormattingListener();
    HornFormatter.addFormattingListener(formattingListener);
  }

  @After
  public void tearDown() throws Exception {
    if (originalTemeratureProperty == null) {
      System.getProperties().remove(PropertyLoader.TEMPERATURE_PROPERTY_NAME);
    } else {
      System.setProperty(PropertyLoader.TEMPERATURE_PROPERTY_NAME, originalTemeratureProperty);
    }
    Files.write(originalPropertiesPath, originalLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    HornFormatter.removeAllListeners();
  }

  @Test
  public void testCreation() {
    assertNotNull(new PropertyLoader()); // coverage, we salute you
  }

  @Test
  public void testLoadProperties() throws IOException {
    PropertyLoader.loadProperties();
    assertEquals(Temperature.COSY.name(), System.getProperty(PropertyLoader.TEMPERATURE_PROPERTY_NAME));
  }

  @Test
  public void testLoadProperties_HOT() throws IOException {
    List<String> newLines = new ArrayList<>();
    originalLines.forEach(line -> {
      if (line.startsWith(PropertyLoader.TEMPERATURE_PROPERTY_NAME)) {
        line = PropertyLoader.TEMPERATURE_PROPERTY_NAME.concat("=").concat(Temperature.HOT.name());
      }
      newLines.add(line);
    });
    assertEquals(originalLines.size(), newLines.size());
    Files.write(originalPropertiesPath, newLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    PropertyLoader.loadProperties();
    assertEquals(Temperature.HOT.name(), System.getProperty(PropertyLoader.TEMPERATURE_PROPERTY_NAME));
  }

  @Test
  public void testLoadProperties_System_before_builtin() {
    System.setProperty(PropertyLoader.TEMPERATURE_PROPERTY_NAME, Temperature.FROZEN.name());
    PropertyLoader.loadProperties();
    assertEquals(Temperature.FROZEN.name(), System.getProperty(PropertyLoader.TEMPERATURE_PROPERTY_NAME));
  }

  @Test
  public void testLoadProperties_Exception() throws Exception {
    System.setProperty(PropertyLoader.TEMPERATURE_PROPERTY_NAME, Temperature.HOT.name());
    List<String> newLines = new ArrayList<>();
    originalLines.forEach(line -> newLines.add(line));
    assertEquals(originalLines.size(), newLines.size());
    newLines.add("test.breaking.property=\\u2");
    Files.write(originalPropertiesPath, newLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    PropertyLoader.loadProperties();
    assertTrue(formattingListener.contains("unable to load klinklong.properties"));
  }

  @Test
  public void testSetSystemProperty() {
    final String testSystemProperetyName = "st.extreme.klingklong.test.property";
    try {
      PropertyLoader.setSystemProperty(testSystemProperetyName, "initial");
      assertEquals("initial", System.getProperty(testSystemProperetyName));
      PropertyLoader.setSystemProperty(testSystemProperetyName, "overwrite");
      assertEquals("initial", System.getProperty(testSystemProperetyName));
    } finally {
      System.getProperties().remove(testSystemProperetyName);
    }
  }

}
