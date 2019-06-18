package st.extreme.klingklong.util;

import java.util.ArrayList;
import java.util.List;

public final class TestFormattingListener implements FormattingListener {
  private List<String> formattedMessages;

  public TestFormattingListener() {
    formattedMessages = new ArrayList<>();
  }

  @Override
  public void onFormatting(String formatted) {
    formattedMessages.add(formatted);
  }

  public boolean contains(String message) {
    return formattedMessages.stream().filter(msg -> msg.contains(message)).findFirst().isPresent();
  }

  public long occurrences(String message) {
    return formattedMessages.stream().filter(msg -> msg.contains(message)).count();
  }

  public List<String> getFormattedMessages() {
    return formattedMessages;
  }
}
