package st.extreme.klingklong.util;

import static st.extreme.klingklong.util.Horn.honk;
import static st.extreme.klingklong.util.Temperature.HOT;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public final class PropertyLoader {

  // property names
  public static final String TEMPERATURE_PROPERTY_NAME = "st.extreme.klingklong.horn.temperature";

  // property file names
  public static final String BUILTIN_PROPERTY_FILE_NAME = "klingklong.properties";
  private static final String HOME_PROPERTY_FiLE_NAME = ".st.extreme.klingklong.properties";

  private static AtomicBoolean loaded = new AtomicBoolean(false);

  public static void loadProperties() {
    if (!loaded.get()) {
      loadHomeProperties();
      loadBuiltinProperties();
      loaded.set(true);
    }
  }

  static void setSystemProperty(String propertyName, String propertyValue) {
    // if the system property is already set, do not overwrite it
    String actualValue = System.getProperty(propertyName);
    if (actualValue == null) {
      System.setProperty(propertyName, propertyValue);
    }
  }

  static Path getUserHomePropertiesPath() {
    return Paths.get(System.getProperty("user.home")).resolve(HOME_PROPERTY_FiLE_NAME);
  }

  static void allowReloading() {
    loaded.set(false); // for testing purposes
  }

  private static void loadHomeProperties() {
    File homeProperties = getUserHomePropertiesPath().toFile();
    if (homeProperties.exists()) {
      Properties properties = new Properties();
      try (InputStream propertiesStream = new FileInputStream(homeProperties)) {
        properties.load(propertiesStream);
      } catch (Exception e) {
        honk(HOT, "unable to load " + homeProperties.getAbsolutePath(), e);
      }
      applyProperties(properties);
    }
  }

  private static void loadBuiltinProperties() {
    Properties properties = new Properties();
    try (InputStream propertiesStream = Horn.class.getResourceAsStream("/".concat(BUILTIN_PROPERTY_FILE_NAME))) {
      properties.load(propertiesStream);
    } catch (Exception e) {
      honk(HOT, "unable to load builtin " + BUILTIN_PROPERTY_FILE_NAME, e);
    }
    applyProperties(properties);
  }

  private static void applyProperties(Properties properties) {
    Set<Object> keys = properties.keySet();
    for (Object object : keys) {
      String key = (String) object;
      setSystemProperty(key, properties.getProperty(key));
    }
  }

}
