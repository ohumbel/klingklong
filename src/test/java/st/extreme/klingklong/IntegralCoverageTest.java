package st.extreme.klingklong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import st.extreme.klingklong.demo.JVM1;
import st.extreme.klingklong.demo.JVM2;
import st.extreme.klingklong.util.Horn;
import st.extreme.klingklong.util.HornFormatter;
import st.extreme.klingklong.util.Temperature;
import st.extreme.klingklong.util.TestFormattingListener;

/**
 * This tests two endpoints in the same vm.
 * <p>
 * The output will most of the time be in different order, but coverage should be pretty good.
 */
public class IntegralCoverageTest {

  private String originalTemeratureProperty;
  private TestFormattingListener formattingListener;

  @Before
  public void setUp() {
    originalTemeratureProperty = System.getProperty(Horn.TEMPERATURE_PROPERTY_NAME);
    formattingListener = new TestFormattingListener();
    HornFormatter.addFormattingListener(formattingListener);
  }

  @After
  public void tearDown() {
    if (originalTemeratureProperty == null) {
      System.getProperties().remove(Horn.TEMPERATURE_PROPERTY_NAME);
    } else {
      System.setProperty(Horn.TEMPERATURE_PROPERTY_NAME, originalTemeratureProperty);
    }
    HornFormatter.removeAllListeners();
  }

  @Test
  public void testTowEndpoints() throws InterruptedException {
    System.setProperty(Horn.TEMPERATURE_PROPERTY_NAME, Temperature.HOT.name());
    WorkerThread1 workerThread1 = new WorkerThread1();
    WorkerThread2 workerThread2 = new WorkerThread2();
    workerThread1.start();
    workerThread2.start();
    do {
      TimeUnit.MILLISECONDS.sleep(500);
    } while (workerThread1.isAlive() || workerThread2.isAlive());

    List<String> formattedMessages = formattingListener.getFormattedMessages();
    assertTrue(formattedMessages.size() >= 24);
    assertEquals(2, formattingListener.occurrences("starting worker on JVM"));
    assertTrue(formattingListener.contains("creating kling"));
    assertTrue(formattingListener.contains("creating klong"));
    assertEquals(2, formattingListener.occurrences("endpoint is connecting ..."));
    assertEquals(2, formattingListener.occurrences("endpoint is now connected"));
    assertEquals(2, formattingListener.occurrences("got a message: 'I am doing some work (0)'"));
    assertEquals(2, formattingListener.occurrences("got a message: 'I am doing some work (1)'"));
    assertEquals(2, formattingListener.occurrences("got a message: 'I am doing some work (2)'"));
    assertEquals(2, formattingListener.occurrences("got a message: 'I am doing some work (3)'"));
    assertTrue(formattingListener.contains("got a message: 'My work is done soon'"));
    assertTrue(formattingListener.contains("got a message: 'STOP'"));
    assertEquals(2, formattingListener.occurrences("receiver thread is terminating now"));
    assertEquals(2, formattingListener.occurrences("sender thread is terminating now"));
    assertEquals(2, formattingListener.occurrences("endpoint is now closed"));
  }

  private static final class WorkerThread1 extends Thread {
    @Override
    public void run() {
      assertEquals(0, JVM1.work());
    }
  }

  private static final class WorkerThread2 extends Thread {
    @Override
    public void run() {
      assertEquals(0, JVM2.work());
    }
  }
}
