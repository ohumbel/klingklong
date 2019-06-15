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
  public void testBelowOrEqualTo() {
    assertTrue(Temperature.FROZEN.isBelowOrEqualTo(Temperature.FROZEN));
    assertTrue(Temperature.FROZEN.isBelowOrEqualTo(Temperature.COSY));
    assertTrue(Temperature.FROZEN.isBelowOrEqualTo(Temperature.HOT));

    assertFalse(Temperature.COSY.isBelowOrEqualTo(Temperature.FROZEN));
    assertTrue(Temperature.COSY.isBelowOrEqualTo(Temperature.COSY));
    assertTrue(Temperature.COSY.isBelowOrEqualTo(Temperature.HOT));

    assertFalse(Temperature.HOT.isBelowOrEqualTo(Temperature.FROZEN));
    assertFalse(Temperature.HOT.isBelowOrEqualTo(Temperature.COSY));
    assertTrue(Temperature.HOT.isBelowOrEqualTo(Temperature.HOT));
  }

}
