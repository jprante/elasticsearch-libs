package com.carrotsearch.randomizedtesting.test;

import java.util.Random;

import com.carrotsearch.randomizedtesting.RandomizedContext;
import com.carrotsearch.randomizedtesting.RandomizedTest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Check out of scope {@link Random} use.
 */
public class TestRandomInStaticInitializer extends RandomizedTest {
  static boolean wasOutOfScope;

  static {
    try {
      RandomizedContext.current();
    } catch (IllegalStateException e) {
      wasOutOfScope = true;
    }
  }

  @Test
  public void testStaticInitializerOutOfScope() {
    assertTrue(wasOutOfScope);
  }
}
