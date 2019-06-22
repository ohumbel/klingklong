package st.extreme.klingklong;

import java.net.InetAddress;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class SenderImplTest {

  @Test
  public void testInterruptedException() throws Exception {
    Sender sender = new SenderImpl(InetAddress.getLocalHost(), 8543, 8654, new Semaphore(0, true));
    sender.start();
    TimeUnit.MILLISECONDS.sleep(5500); // wait > 5 seconds to cover the honk condition
    ((SenderImpl) sender).interrupt(); // provoke an interrupted exception during sleep
  }

}
