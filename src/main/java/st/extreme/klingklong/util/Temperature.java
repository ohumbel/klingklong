package st.extreme.klingklong.util;

public enum Temperature {
  /**
   * The temperature is so frozen that there is no output at all.
   */
  FROZEN,

  /**
   * The temperature is cosy, so a moderate amount of output is issued.
   */
  COSY,

  /**
   * The temperature is hot, because of a lot of output.
   */
  HOT;

  boolean isBelowOrEqualTo(Temperature other) {
    if (other != null) {
      switch (other) {
      case FROZEN:
        return FROZEN.equals(this);
      case COSY:
        return !HOT.equals(this);
      case HOT:
        return true;
      }
    }
    return false;
  }

}
