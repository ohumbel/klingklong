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
    honk(COSY, "starting worker on JVM 1");
    JVMWorker worker = new JVMWorker(Type.KLING);
    try {
      worker.workAndCommunicate();
      System.exit(0);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
