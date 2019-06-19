package st.extreme.klingklong;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import st.extreme.klingklong.ConfigurationException.Reason;

public class ConfigurationExceptionTest {

  @Test
  public void testConfigurationException() {
    ConfigurationException ce;

    ce = new ConfigurationException(Reason.ALREADY);
    assertEquals("This endpoint is already configured", ce.getMessage());
    ce = new ConfigurationException(Reason.DUPLICATE);
    assertEquals("The exact same configuration is already present.", ce.getMessage());
    ce = new ConfigurationException(Reason.SAME_PORT);
    assertEquals("The same port on the same host ist not allowed.", ce.getMessage());
    ce = new ConfigurationException(Reason.UNKOWN_TYPE);
    assertEquals("The specified type is unknown", ce.getMessage());
  }

}
