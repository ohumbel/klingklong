package st.extreme.klingklong.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Horn {

  private static final Logger LOGGER;

  static {
    LOGGER = Logger.getLogger(Horn.class.getName());
    // set our formatter on the console handler of the root logger
    Logger.getLogger("").getHandlers()[0].setFormatter(new HornFormatter());
  }

  public static void honk(Temperature temperature, String message) {
    honk(temperature, message, null);
  }

  public static void honk(Temperature temperature, String message, Throwable throwable) {
    if (temperature.isBelowOrEqualTo(systemTemperature())) {
      switch (temperature) {
      case COSY:
        internalLog(Level.INFO, message, throwable);
        break;
      case HOT:
        internalLog(Level.SEVERE, message, throwable);
        break;
      default:
        // no logging at all if frozen
        break;
      }
    }
  }

  static Temperature systemTemperature() {
    return Temperature.valueOf(System.getProperty(PropertyLoader.TEMPERATURE_PROPERTY_NAME, Temperature.FROZEN.name()));
  }

  private static void internalLog(Level level, String message, Throwable throwable) {
    if (throwable != null) {
      LOGGER.log(level, message, throwable);
    } else {
      LOGGER.log(level, message);
    }
  }

}
