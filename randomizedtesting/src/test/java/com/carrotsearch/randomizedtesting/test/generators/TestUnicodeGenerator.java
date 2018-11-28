package com.carrotsearch.randomizedtesting.test.generators;

import com.carrotsearch.randomizedtesting.generators.UnicodeGenerator;

public class TestUnicodeGenerator extends StringGeneratorTestBase {
  public TestUnicodeGenerator() {
    super(new UnicodeGenerator());
  }
}
