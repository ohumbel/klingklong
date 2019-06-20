package st.extreme.klingklong;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConnectionExceptionTest {

  @Test
  public void testConnectionException() {
    ConnectionException ce;
    Exception cause = new Exception("the cause");

    ce = new ConnectionException("unable to connect", cause);
    assertEquals("unable to connect", ce.getMessage());
    ce = new ConnectionException("unable to connect", null);
    assertEquals("unable to connect", ce.getMessage());
  }

}
