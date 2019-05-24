package st.extreme.klingklong.demo;

import st.extreme.klingklong.Type;

public class JVM1 {

  /**
   * Main method for testing
   * 
   * @param args
   */
  public static void main(String[] args) {
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
