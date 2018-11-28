package org.apache.lucene.testframework.test.util;

import com.carrotsearch.randomizedtesting.annotations.TestGroup;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@TestGroup(enabled = false)
public @interface Jira {
  String bug();
}
