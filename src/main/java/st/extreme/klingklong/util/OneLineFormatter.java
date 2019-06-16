package st.extreme.klingklong.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

class OneLineFormatter extends Formatter {

  private static final List<FormattingListener> formattingListeners = new ArrayList<>();

  @Override
  public String format(LogRecord record) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(String.format("[klingklong] %s", record.getMessage()));
    Throwable throwable = record.getThrown();
    if (throwable != null) {
      stringBuilder.append(":\n");
      stringBuilder.append("[klingklong] ");
      try (StringWriter stringWriter = new StringWriter(); PrintWriter printWriter = new PrintWriter(stringWriter)) {
        throwable.printStackTrace(printWriter);
        stringBuilder.append(stringWriter.toString());
      } catch (IOException e) { // ignore
      }
    } else {
      stringBuilder.append('\n');
    }
    final String formatted = stringBuilder.toString();
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