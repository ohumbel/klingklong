package st.extreme.klingklong.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static st.extreme.klingklong.util.Temperature.COSY;
import static st.extreme.klingklong.util.Temperature.FROZEN;
import static st.extreme.klingklong.util.Temperature.HOT;

import org.junit.Test;

public class TemperatureTest {

  @Test
  public void testTemperatureValueOf() {
    assertEquals(FROZEN, Temperature.valueOf("FROZEN"));
    assertEquals(COSY, Temperature.valueOf("COSY"));
    assertEquals(HOT, Temperature.valueOf("HOT"));
  }

  @Test
  public void testBelowOrEqualTo() {
    assertTrue(FROZEN.isBelowOrEqualTo(FROZEN));
    assertTrue(FROZEN.isBelowOrEqualTo(COSY));
    assertTrue(FROZEN.isBelowOrEqualTo(HOT));

    assertFalse(COSY.isBelowOrEqualTo(FROZEN));
    assertTrue(COSY.isBelowOrEqualTo(COSY));
    assertTrue(COSY.isBelowOrEqualTo(HOT));

    assertFalse(HOT.isBelowOrEqualTo(FROZEN));
    assertFalse(HOT.isBelowOrEqualTo(COSY));
    assertTrue(HOT.isBelowOrEqualTo(HOT));

    assertFalse(FROZEN.isBelowOrEqualTo(null));
    assertFalse(COSY.isBelowOrEqualTo(null));
    assertFalse(HOT.isBelowOrEqualTo(null));
  }

}
