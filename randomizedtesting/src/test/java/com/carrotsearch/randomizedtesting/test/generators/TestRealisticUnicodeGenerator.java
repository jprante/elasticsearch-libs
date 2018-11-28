package com.carrotsearch.randomizedtesting.test.generators;

import com.carrotsearch.randomizedtesting.generators.UnicodeGenerator;

public class TestRealisticUnicodeGenerator extends StringGeneratorTestBase {
  public TestRealisticUnicodeGenerator() {
    super(new UnicodeGenerator());
  }
}
