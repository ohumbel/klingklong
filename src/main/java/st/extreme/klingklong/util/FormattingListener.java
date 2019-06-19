package st.extreme.klingklong.util;

public interface FormattingListener {

  /**
   * This method is called at the time a message is being formatted.
   * 
   * @param formatted The already formatted message
   */
  void onFormatting(String formatted);

}
