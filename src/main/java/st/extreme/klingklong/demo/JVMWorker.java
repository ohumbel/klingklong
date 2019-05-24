package st.extreme.klingklong.demo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import st.extreme.klingklong.ConfigurationException;
import st.extreme.klingklong.ConfigurationException.Reason;
import st.extreme.klingklong.Kling;
import st.extreme.klingklong.Klingklong;
import st.extreme.klingklong.Klong;
import st.extreme.klingklong.MessageListener;
import st.extreme.klingklong.Type;

public class JVMWorker implements MessageListener {

  private final Type type;
  private final AtomicBoolean atWork;
  private final AtomicInteger workCount;

  public JVMWorker(Type type) {
    this.type = type;
    this.atWork = new AtomicBoolean(true);
    this.workCount = new AtomicInteger(0);
  }

  @Override
  public void onMessage(String message) {
    System.out.println("got message: '" + message + "'");
    if ("STOP".equals(message)) { // TODO: check how to stop really
      stopWork();
    }
  }

  void workAndCommunicate() throws Exception {
    System.out.println("creating klingklong");
    try (Klingklong klingklong = createKlingKlong()) {
      klingklong.addMessageListener(this);
      klingklong.connect();
      while (atWork.get()) {
        klingklong.send(String.format("doing some work (%d)", workCount.get()));
        work();
      }
      klingklong.send("my work is done soon");
      TimeUnit.SECONDS.sleep(1);
      klingklong.send("bye");
    }
  }

  private Klingklong createKlingKlong() throws ConfigurationException {
    final Klingklong klingklong;
    switch (type) {
    case KLING:
      System.out.println("creating kling");
      klingklong = Kling.create();
      break;
    case KLONG:
      System.out.println("creating kling");
      klingklong = Klong.create();
      break;
    default:
      throw new ConfigurationException(Reason.UNKOWN_TYPE);
    }
    return klingklong;
  }

  private void work() throws InterruptedException {
    int actualWorkCount = workCount.incrementAndGet();
    TimeUnit.SECONDS.sleep(3);
    if (actualWorkCount > 5) {
      stopWork();
    }
  }

  private void stopWork() {
    atWork.set(false);
  }

}
