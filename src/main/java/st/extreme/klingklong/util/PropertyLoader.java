package st.extreme.klingklong.util;

import static st.extreme.klingklong.util.Horn.honk;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public final class PropertyLoader {

  public static final String TEMPERATURE_PROPERTY_NAME = "st.extreme.klingklong.horn.temperature";


  static {
    loadProperties();
  }

  // TODO: handle properties in user.home

  public static void loadProperties() {
    Properties properties = new Properties();
    try (InputStream propertiesStream = Horn.class.getResourceAsStream("/klingklong.properties")) {
      properties.load(propertiesStream);
    } catch (Exception e) {
      honk(Temperature.HOT, "unable to load klinklong.properties", e);
    }

    Set<Object> keys = properties.keySet();
    for (Object object : keys) {
      String key = (String) object;
      setSystemProperty(key, properties.getProperty(key));
    }
  }

  static void setSystemProperty(String propertyName, String propertyValue) {
    // if the system property is already set, do not overwrite it
    String actualValue = System.getProperty(propertyName);
    if (actualValue == null) {
      System.setProperty(propertyName, propertyValue);
    }
  }

}
