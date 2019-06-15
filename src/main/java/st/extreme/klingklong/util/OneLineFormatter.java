package st.extreme.klingklong.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

class OneLineFormatter extends Formatter {

  private static final List<FormattingListener> formattingListeners = new ArrayList<>();

  @Override
  public String format(LogRecord record) {
    String formatted = String.format("[klingklong] %s\n", record.getMessage());
    formattingListeners.forEach(listener -> listener.onFormatting(formatted));
    return formatted;
  }

  static void addFormattingListener(FormattingListener formattingListener) {
    formattingListeners.add(formattingListener);
  }

  static void removeAllListeners() {
    formattingListeners.clear();
  }

}