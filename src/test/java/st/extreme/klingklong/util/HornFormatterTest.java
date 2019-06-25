package st.extreme.klingklong.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Test;

public class HornFormatterTest {

  @Test
  public void testAddFormattingListener() throws Exception {
    HornFormatter.addFormattingListener(new TestFormattingListener());
    HornFormatter.addFormattingListener(new TestFormattingListener());
    assertEquals(2, getFormattingListeners().size());
  }

  @Test
  public void testRemoveAllListeners() throws Exception {
    HornFormatter.addFormattingListener(new TestFormattingListener());
    HornFormatter.addFormattingListener(new TestFormattingListener());
    assertEquals(2, getFormattingListeners().size());
    HornFormatter.removeAllListeners();
    assertEquals(0, getFormattingListeners().size());
  }

  @Test
  public void testFormat_simple() {
    HornFormatter formatter = new HornFormatter();
    LogRecord logRecord = new TestLogRecord("the message");
    assertEquals("[klingklong] the message\n", formatter.format(logRecord));
  }

  @Test
  public void testFormat_exception() {
    HornFormatter formatter = new HornFormatter();
    LogRecord logRecord = new TestLogRecord("the message", new Exception("the exception"));
    String expectedStart = "[klingklong] the message:\n" + //
        "[klingklong] java.lang.Exception: the exception\n" + //
        "\tat st.extreme.klingklong.util.HornFormatterTest.testFormat_exception(HornFormatterTest.java:";
    assertTrue(formatter.format(logRecord).startsWith(expectedStart));
  }

  @Test
  public void testFormat_null_exception() {
    HornFormatter formatter = new HornFormatter();
    LogRecord logRecord = new TestLogRecord("the message", null);
    assertEquals("[klingklong] the message\n", formatter.format(logRecord));
  }

  private List<FormattingListener> getFormattingListeners() throws Exception {
    Field field = HornFormatter.class.getDeclaredField("formattingListeners");
    assertNotNull(field);
    field.setAccessible(true);
    Object result = field.get(HornFormatter.class);
    assertTrue(result instanceof List<?>);
    @SuppressWarnings("unchecked")
    List<FormattingListener> listeners = (List<FormattingListener>) result;
    return listeners;
  }

  private static final class TestLogRecord extends LogRecord {
    private static final long serialVersionUID = 1L;

    public TestLogRecord(String message) {
      super(Level.WARNING, message);
    }

    public TestLogRecord(String message, Throwable throwable) {
      this(message);
      setThrown(throwable);
    }
  }

}
