package st.extreme.klingklong.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HornTest {

  private String originalTemeratureProperty;

  @Before
  public void setUp() {
    originalTemeratureProperty = System.getProperty(Horn.TEMPERATURE_PROPERTY_NAME, Temperature.FROZEN.name());
  }

  @After
  public void tearDown() {
    System.setProperty(Horn.TEMPERATURE_PROPERTY_NAME, originalTemeratureProperty);
  }

  @Test
  public void testLoadProperties() throws IOException {
    Horn.loadProperties();
    assertEquals(Temperature.COSY.name(), System.getProperty(Horn.TEMPERATURE_PROPERTY_NAME));
  }

  @Test
  public void testSystemTemperature() {
    System.setProperty(Horn.TEMPERATURE_PROPERTY_NAME, Temperature.FROZEN.name());
    assertEquals(Temperature.FROZEN, Horn.systemTemperature());

    System.setProperty(Horn.TEMPERATURE_PROPERTY_NAME, Temperature.COSY.name());
    assertEquals(Temperature.COSY, Horn.systemTemperature());

    System.setProperty(Horn.TEMPERATURE_PROPERTY_NAME, Temperature.HOT.name());
    assertEquals(Temperature.HOT, Horn.systemTemperature());
  }

}
