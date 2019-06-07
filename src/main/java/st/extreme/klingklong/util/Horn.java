package st.extreme.klingklong.util;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class Horn {

  private static final Logger LOGGER;

  static {
    LOGGER = Logger.getLogger(Horn.class.getName());
    // set our formatter on the console handler of the root logger
    Logger.getLogger("").getHandlers()[0].setFormatter(new OneLineFormatter());
  }

  public static void honk(Temperature temperature, String message) {
    switch (temperature) {
    case FROZEN:
      break;
    case COSY:
      LOGGER.log(Level.INFO, message);
      break;
    case HOT:
      LOGGER.log(Level.ALL, message);
      break;
    }
  }

  public enum Temperature {
    /**
     * The temperature is so frozen that there is no output at all.
     */
    FROZEN,

    /**
     * The temperature is cosy, so a moderate amount of output is issued.
     */
    COSY,

    /**
     * The temperature is hot, because of a lot of output.
     */
    HOT;
  }

  private static class OneLineFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
      return String.format("[klingklong] %s\n", record.getMessage());
    }
  }

}
