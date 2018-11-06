package com.carrotsearch.randomizedtesting.rules;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

import org.junit.ClassRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.carrotsearch.randomizedtesting.RandomizedContext;

/**
 * A {@link TestRule} that ensures static, reference fields of the suite class
 * (and optionally its superclasses) are cleaned up after a suite is completed.
 * This is helpful in finding out static memory leaks (a class references
 * something huge but is no longer used).
 * 
 * @see ClassRule
 * @see #accept(Field)
 */
public class StaticFieldsInvariantRule implements TestRule {
  public static final long DEFAULT_LEAK_THRESHOLD = 10 * 1024 * 1024;
  
  private final long leakThreshold;
  private final boolean countSuperclasses;
  
  /**
   * By default use {@link #DEFAULT_LEAK_THRESHOLD} as the threshold and count
   * in superclasses.
   */
  public StaticFieldsInvariantRule() {
    this(DEFAULT_LEAK_THRESHOLD, true);
  }
  
  public StaticFieldsInvariantRule(long leakThresholdBytes, boolean countSuperclasses) {
    this.leakThreshold = leakThresholdBytes;
    this.countSuperclasses = countSuperclasses;
  }
  
  static class Entry implements Comparable<Entry> {
    final Field field;
    final Object value;

    public Entry(Field field, Object value) {
      this.field = field;
      this.value = value;
    }
    
    @Override
    public int compareTo(Entry o) {
      return this.field.toString().compareTo(o.field.toString());
    }
  }
  
  @Override
  public Statement apply(final Statement s, final Description d) {
    return new StatementAdapter(s) {
      @Override
      protected void afterAlways(List<Throwable> errors) throws Throwable {
        // Try to get the target class from the context, if available.
        Class<?> testClass = null;
        try {
          testClass = RandomizedContext.current().getTargetClass();
        } catch (Throwable t) {
          // Ignore.
        }

        if (testClass == null) {
          // This is JUnit's ugly way that attempts Class.forName and may use a different
          // classloader... let's use it as a last resort option.
          testClass = d.getTestClass();
        }
        
        // No test class? Weird.
        if (testClass == null) {
          throw new RuntimeException("Test class could not be acquired from the randomized " +
          		"context or the Description object.");
        }

        // Collect all fields first to count references to the same object once.
        ArrayList<Entry> fieldsAndValues = new ArrayList<Entry>();
        ArrayList<Object> values = new ArrayList<Object>();
        for (Class<?> c = testClass; countSuperclasses && c.getSuperclass() != null; c = c.getSuperclass()) {
          final Class<?> target = c;
          Field[] allFields = AccessController.doPrivileged(new PrivilegedAction<Field[]>() {
            @Override
            public Field[] run() {
              return target.getDeclaredFields();
            }
          });
          for (final Field field : allFields) {
            if (Modifier.isStatic(field.getModifiers()) && 
                !field.getType().isPrimitive() &&
                accept(field)) {
              try {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {
                  @Override
                  public Void run() {
                    field.setAccessible(true);
                    return null;
                  }
                });
                Object v = field.get(null);
                if (v != null) {
                  fieldsAndValues.add(new Entry(field, v));
                  values.add(v);
                }
              } catch (SecurityException e) {
                errors.add(new RuntimeException("Could not access field '" + field.getName() + "'.", e));
              }
            }
          }
        }
      }
    };
  }

  /**
   * @return Return <code>false</code> to exclude a given field from being
   *         counted. By default final fields are rejected.
   */
  protected boolean accept(Field field) {
    return !Modifier.isFinal(field.getModifiers());
  }
}
