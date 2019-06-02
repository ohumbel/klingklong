package st.extreme.klingklong.demo;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import st.extreme.klingklong.ConfigurationException;
import st.extreme.klingklong.ConfigurationException.Reason;
import st.extreme.klingklong.Endpoint;
import st.extreme.klingklong.Kling;
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
    // TODO: check how to stop really
    System.out.println(String.format("worker got a message '%s'", message));
    if ("STOP".equals(message)) {
      stopWork();
    }
  }

  void workAndCommunicate() throws Exception {
    System.out.println("worker is creating the endpoint");
    try (Endpoint endpoint = createEndpoint()) {
      endpoint.addMessageListener(this);
      endpoint.connect();
      System.out.println("worker's endpoint is now connected\n---------------------------");
      while (atWork.get()) {
        endpoint.send(String.format("I am doing some work (%d)", workCount.get()));
        work();
      }
      endpoint.send("My work is done soon");
      TimeUnit.SECONDS.sleep(1);
      endpoint.send("bye");
    }
    System.out.println("worker finally closed the endpoint");
  }

  private Endpoint createEndpoint() throws ConfigurationException, UnknownHostException {
    final Endpoint endpoint;
    switch (type) {
    case KLING:
      System.out.println("creating kling");
      endpoint = Kling.create();
      break;
    case KLONG:
      System.out.println("creating klong");
      endpoint = Klong.create();
      break;
    default:
      throw new ConfigurationException(Reason.UNKOWN_TYPE);
    }
    return endpoint;
  }

  private void work() throws InterruptedException {
    int actualWorkCount = workCount.incrementAndGet();
    // for testing ?
    double rand = Math.random();
    if (rand <= 0.5) {
      TimeUnit.SECONDS.sleep(1);
    } else {
      TimeUnit.SECONDS.sleep(2);
    }
    if (actualWorkCount > 2) {
      stopWork();
    }
  }

  private void stopWork() {
    System.out.println("worker: stopping");
    atWork.set(false);
  }

}
