package st.extreme.klingklong.demo;

import st.extreme.klingklong.Type;

public class JVM2 {

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
    JVMWorker worker = new JVMWorker(Type.KLONG);
    try {
      worker.workAndCommunicate();
      return 0;
    } catch (Exception e) {
      e.printStackTrace();
      return 1;
    }
  }

}
