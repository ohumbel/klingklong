package st.extreme.klingklong.util;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class Horn {

  static final String TEMPERATURE_PROPERTY_NAME = "klingklong.horn.temperature";
  private static final Logger LOGGER;

  static {
    LOGGER = Logger.getLogger(Horn.class.getName());
    // set our formatter on the console handler of the root logger
    Logger.getLogger("").getHandlers()[0].setFormatter(new OneLineFormatter());
  }

  public static void honk(Temperature temperature, String message) {
    honk(temperature, message, null);
  }

  public static void honk(Temperature requestedTemperature, String message, Throwable throwable) {
    final Temperature currentTemperature;
    if (requestedTemperature.isHotterThanOrEqualTo(systemTemperature())) {
      currentTemperature = requestedTemperature;
    } else {
      currentTemperature = Temperature.FROZEN;
    }
    final Level level;
    switch (currentTemperature) {
    case COSY:
      level = Level.INFO;
      break;
    case HOT:
      level = Level.ALL;
      break;
    default:
      level = Level.OFF;
    }
    if (throwable != null) {
      LOGGER.log(level, message, throwable);
    } else {
      LOGGER.log(level, message);
    }
  }

  static void loadProperties() {
    Properties properties = new Properties();
    try (InputStream s = Horn.class.getResourceAsStream("klingklong.properties")) {
      properties.load(s);
    } catch (Exception e) {
      honk(Temperature.HOT, "unable to load klinklong.properties", e);
    }

    Set<Object> keys = properties.keySet();
    for (Object object : keys) {
      String key = (String) object;
      System.setProperty(key, properties.getProperty(key));
    }
  }

  static Temperature systemTemperature() {
    return Temperature.valueOf(System.getProperty(TEMPERATURE_PROPERTY_NAME, Temperature.FROZEN.name()));
  }

  private static class OneLineFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
      return String.format("[klingklong] %s\n", record.getMessage());
    }
  }

}
