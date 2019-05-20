package st.extreme.klingklong.demo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import st.extreme.klingklong.Kling;
import st.extreme.klingklong.MessageListener;

public class JVM1 implements MessageListener {

  private AtomicBoolean atWork = new AtomicBoolean(true);
  private int workCount = 0;

  public void doWorkAndCommunicate() throws Exception {

    try (Kling kling = Kling.create()) {
      kling.addMessageListener(this);
      kling.connect();
      while (atWork.get()) {
        kling.send("doing some work");
        work();
      }
      kling.send("my work is done soon");
      TimeUnit.SECONDS.sleep(1);
    }
  }

  private void work() throws InterruptedException {
    workCount++;
    TimeUnit.SECONDS.sleep(3);
    if (workCount > 5) {
      stopWork();
    }
  }

  private void stopWork() {
    atWork.set(false);
  }

  @Override
  public void onMessage(String message) {
    System.out.println("got message: '" + message + "'");
    if ("STOP".equals(message)) {
      stopWork();
    }
  }

}
