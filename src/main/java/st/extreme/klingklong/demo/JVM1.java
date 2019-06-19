package st.extreme.klingklong.demo;

import static st.extreme.klingklong.util.Horn.honk;
import static st.extreme.klingklong.util.Temperature.COSY;

import st.extreme.klingklong.Type;

public class JVM1 {

  /**
   * Main method for testing
   * 
   * @param args the arguments
   */
  public static void main(String[] args) {
    System.exit(work());
  }

  /**
   * Do all the work and provide an appropriate exit code
   * 
   * @return the exit code (0 = success)
   */
  public static int work() {
    honk(COSY, "starting worker on JVM 1");
    JVMWorker worker = new JVMWorker(Type.KLING);
    try {
      worker.workAndCommunicate();
      return 0;
    } catch (Exception e) {
      e.printStackTrace();
      return 1;
    }
  }
}
