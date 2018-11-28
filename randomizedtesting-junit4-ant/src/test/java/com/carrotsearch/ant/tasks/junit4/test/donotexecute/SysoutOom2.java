package com.carrotsearch.ant.tasks.junit4.test.donotexecute;

import org.junit.Test;

public class SysoutOom2 extends SysoutOom1 {
  @Override
  @Test
  public void writealot() {
    write("abcdefghjqiuqiugqwpicvaisuvcipsvcuipqyfpuixv".toCharArray());
  }
}
