package com.carrotsearch.ant.tasks.junit4.test.it;

import java.net.URL;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runners.model.Statement;

/**
 * A base class for tests contained in <code>junit4.xml</code>
 * file.
 */
public class JUnit4XmlTestBase extends AntBuildFileTestBase {
  @Rule
  public TestRule dumpLogOnError = (base, description) -> new Statement() {
    @Override
    public void evaluate() throws Throwable {
      try {
        base.evaluate();
      } catch (Throwable e) {
        System.out.println("Ant log: " + getLog());
        throw e;
      }
    }
  };
  
  @Before
  public void setUp() {
    URL resource = getClass().getClassLoader().getResource("junit4.xml");
    super.setupProject(resource);
  }
  
  protected static int countPattern(String output, String substr) {
    int count = 0;
    for (int i = 0; i < output.length();) {
      int index = output.indexOf(substr, i);
      if (index < 0) {
        break;
      }
      count++;
      i = index + 1;
    }
    return count;
  }  
}
