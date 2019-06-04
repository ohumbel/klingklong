package st.extreme.klingklong.demo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {

  private static Semaphore semaphore = new Semaphore(0, true);

  public static void main(String[] args) {
    ChildThread childThread = new ChildThread(semaphore);
    childThread.start();
    try {
      TimeUnit.SECONDS.sleep(1);
      log("parent thread blocking on semaphore");
      semaphore.acquire();
      log("parent thread continuing");
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      log("parent thread stopping");
    }
  }

  private static void log(String msg) {
    System.out.println(String.format("[%d] %s", System.currentTimeMillis(), msg));
  }

  private static class ChildThread extends Thread {
    private final Semaphore outerSemaphore;

    ChildThread(Semaphore outerSemaphore) {
      this.outerSemaphore = outerSemaphore;
    }

    @Override
    public void run() {
      try {
        log("child thread starting");
        TimeUnit.SECONDS.sleep(4);
        log("child thread releasing semaphore");
        outerSemaphore.release();
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        log("child thread stopping");
      }
    }
  }
}
