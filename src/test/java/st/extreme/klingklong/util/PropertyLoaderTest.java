package st.extreme.klingklong.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static st.extreme.klingklong.util.PropertyLoader.BUILTIN_PROPERTY_FILE_NAME;
import static st.extreme.klingklong.util.PropertyLoader.TEMPERATURE_PROPERTY_NAME;
import static st.extreme.klingklong.util.Temperature.COSY;
import static st.extreme.klingklong.util.Temperature.FROZEN;
import static st.extreme.klingklong.util.Temperature.HOT;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PropertyLoaderTest {

  private Path originalBuiltinPropertiesPath;
  private List<String> originalBuiltinLines;
  private List<String> originalHomeLines;
  private String originalTemeratureProperty;
  private TestFormattingListener formattingListener;

  @Before
  public void setUp() throws Exception {
    originalTemeratureProperty = System.getProperty(TEMPERATURE_PROPERTY_NAME);
    URL originalPropertiesURL = PropertyLoader.class.getResource("/".concat(BUILTIN_PROPERTY_FILE_NAME));
    assertNotNull(originalPropertiesURL);
    originalBuiltinPropertiesPath = Paths.get(originalPropertiesURL.toURI());
    originalBuiltinLines = Files.readAllLines(originalBuiltinPropertiesPath);
    try {
      originalHomeLines = Files.readAllLines(PropertyLoader.getUserHomePropertiesPath());
    } catch (NoSuchFileException e) {
      originalHomeLines = new ArrayList<>();
    }
    formattingListener = new TestFormattingListener();
    HornFormatter.addFormattingListener(formattingListener);
    // start with no system properties at all
    if (!"unset".equals(System.getProperty(TEMPERATURE_PROPERTY_NAME, "unset"))) {
      System.getProperties().remove(TEMPERATURE_PROPERTY_NAME);
    }
    PropertyLoader.allowReloading();
  }

  @After
  public void tearDown() throws Exception {
    if (originalTemeratureProperty == null) {
      System.getProperties().remove(TEMPERATURE_PROPERTY_NAME);
    } else {
      System.setProperty(TEMPERATURE_PROPERTY_NAME, originalTemeratureProperty);
    }
    Files.write(originalBuiltinPropertiesPath, originalBuiltinLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    if (originalHomeLines.size() > 0) {
      Files.write(PropertyLoader.getUserHomePropertiesPath(), originalHomeLines, StandardOpenOption.WRITE,
          StandardOpenOption.TRUNCATE_EXISTING);
    } else {
      Files.deleteIfExists(PropertyLoader.getUserHomePropertiesPath());
    }
    HornFormatter.removeAllListeners();
  }

  @Test
  public void testLoadProperties() throws IOException {
    PropertyLoader.loadProperties();
    assertEquals(COSY.name(), System.getProperty(TEMPERATURE_PROPERTY_NAME));
  }

  @Test
  public void testLoadProperties_HOT() throws IOException {
    List<String> newLines = new ArrayList<>();
    originalBuiltinLines.forEach(line -> {
      if (line.startsWith(TEMPERATURE_PROPERTY_NAME)) {
        line = TEMPERATURE_PROPERTY_NAME.concat("=").concat(HOT.name());
      }
      newLines.add(line);
    });
    assertEquals(originalBuiltinLines.size(), newLines.size());
    Files.write(originalBuiltinPropertiesPath, newLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    PropertyLoader.loadProperties();
    assertEquals(HOT.name(), System.getProperty(TEMPERATURE_PROPERTY_NAME));
  }

  @Test
  public void testLoadProperties_System_before_Builtin() {
    System.setProperty(TEMPERATURE_PROPERTY_NAME, FROZEN.name());
    PropertyLoader.loadProperties();
    assertEquals(FROZEN.name(), System.getProperty(TEMPERATURE_PROPERTY_NAME));
  }

  @Test
  public void testLoadProperties_Builtin_Exception() throws Exception {
    System.setProperty(TEMPERATURE_PROPERTY_NAME, HOT.name());
    List<String> newLines = new ArrayList<>();
    originalBuiltinLines.forEach(line -> newLines.add(line));
    assertEquals(originalBuiltinLines.size(), newLines.size());
    newLines.add("test.breaking.property=\\u2");
    Files.write(originalBuiltinPropertiesPath, newLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    PropertyLoader.loadProperties();
    assertTrue(formattingListener.contains("unable to load builtin klingklong.properties:"));
  }

  @Test
  public void testLoadProperties_Home_Exception() throws Exception {
    System.setProperty(TEMPERATURE_PROPERTY_NAME, HOT.name());
    List<String> newLines = new ArrayList<>();
    newLines.add("test.breaking.property=\\u2");
    Files.write(PropertyLoader.getUserHomePropertiesPath(), newLines, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    PropertyLoader.loadProperties();
    assertTrue(formattingListener.contains("unable to load " + PropertyLoader.getUserHomePropertiesPath().toString()));
  }

  @Test
  public void testLoadProperties_Home_before_Builtin() throws Exception {
    List<String> homeLines = new ArrayList<>();
    homeLines.add(TEMPERATURE_PROPERTY_NAME.concat("=").concat(HOT.name()));
    Files.write(PropertyLoader.getUserHomePropertiesPath(), homeLines, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    PropertyLoader.loadProperties();
    assertEquals(HOT.name(), System.getProperty(TEMPERATURE_PROPERTY_NAME));
  }

  @Test
  public void testLoadProperties_System_before_Home() throws Exception {
    System.setProperty(TEMPERATURE_PROPERTY_NAME, FROZEN.name());
    List<String> homeLines = new ArrayList<>();
    homeLines.add(TEMPERATURE_PROPERTY_NAME.concat("=").concat(HOT.name()));
    Files.write(PropertyLoader.getUserHomePropertiesPath(), homeLines, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    PropertyLoader.loadProperties();
    assertEquals(FROZEN.name(), System.getProperty(TEMPERATURE_PROPERTY_NAME));
  }

  @Test
  public void testSetSystemProperty() {
    final String testSystemPropertyName = "st.extreme.klingklong.test.property";
    try {
      PropertyLoader.setSystemProperty(testSystemPropertyName, "initial");
      assertEquals("initial", System.getProperty(testSystemPropertyName));
      PropertyLoader.setSystemProperty(testSystemPropertyName, "overwrite");
      assertEquals("initial", System.getProperty(testSystemPropertyName));
    } finally {
      System.getProperties().remove(testSystemPropertyName);
    }
  }

  @Test
  public void testCreation() {
    assertNotNull(new PropertyLoader()); // coverage, we salute you
  }

}
