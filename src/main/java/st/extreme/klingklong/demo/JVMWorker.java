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

  private static final String STOP_MESSAGE = "STOP";
  private static final int MINIMUM_WORK = 3;

  private final Type type;
  private final AtomicInteger workCount;
  private final AtomicBoolean isRemoteListening;

  public JVMWorker(Type type) {
    this.type = type;
    workCount = new AtomicInteger(0);
    isRemoteListening = new AtomicBoolean(true);
  }

  @Override
  public void onMessage(String message) {
    System.out.println(String.format("got a message: '%s'", message));
    if (STOP_MESSAGE.equals(message)) {
      isRemoteListening.set(false);
    }
  }

  void workAndCommunicate() throws Exception {
    try (Endpoint endpoint = createEndpoint()) {
      endpoint.addMessageListener(this);
      endpoint.connect();
      System.out.println("endpoint is now connected");
      while (workCount.get() <= MINIMUM_WORK) {
        endpoint.send(String.format("I am doing some work (%d)", workCount.get()));
        workOneUnit();
      }
      if (isRemoteListening.get()) {
        endpoint.send("My work is done soon");
      }
      TimeUnit.SECONDS.sleep(1);
      if (isRemoteListening.get()) {
        endpoint.send(STOP_MESSAGE);
      }
    }
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

  private void workOneUnit() throws InterruptedException {
    workCount.incrementAndGet();
    double rand = Math.random();
    if (rand <= 0.5) {
      TimeUnit.SECONDS.sleep(1);
    } else {
      TimeUnit.SECONDS.sleep(2);
    }
  }

}
