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

  public static void honk(String message) {
    LOGGER.log(Level.INFO, message);
  }

  private static class OneLineFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
      return String.format("%s\n", record.getMessage());
    }
  }

}
