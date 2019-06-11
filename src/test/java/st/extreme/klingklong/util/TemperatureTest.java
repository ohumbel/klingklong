package st.extreme.klingklong.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TemperatureTest {

  @Test
  public void testTemperatureValueOf() {
    assertEquals(Temperature.FROZEN, Temperature.valueOf("FROZEN"));
    assertEquals(Temperature.COSY, Temperature.valueOf("COSY"));
    assertEquals(Temperature.HOT, Temperature.valueOf("HOT"));
  }

  @Test
  public void testIsHotterThan() {
    assertTrue(Temperature.FROZEN.isHotterThanOrEqualTo(Temperature.FROZEN));
    assertFalse(Temperature.FROZEN.isHotterThanOrEqualTo(Temperature.COSY));
    assertFalse(Temperature.FROZEN.isHotterThanOrEqualTo(Temperature.HOT));

    assertTrue(Temperature.COSY.isHotterThanOrEqualTo(Temperature.FROZEN));
    assertTrue(Temperature.COSY.isHotterThanOrEqualTo(Temperature.COSY));
    assertFalse(Temperature.COSY.isHotterThanOrEqualTo(Temperature.HOT));

    assertTrue(Temperature.HOT.isHotterThanOrEqualTo(Temperature.FROZEN));
    assertTrue(Temperature.HOT.isHotterThanOrEqualTo(Temperature.COSY));
    assertTrue(Temperature.HOT.isHotterThanOrEqualTo(Temperature.HOT));
  }

}
