/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.lucene.testframework.test.search;

import org.apache.lucene.index.Term;

import junit.framework.AssertionFailedError;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.testframework.search.BaseExplanationTestCase;
import org.apache.lucene.testframework.search.CheckHits;

/** 
 * Tests that the {@link BaseExplanationTestCase} helper code, as well as 
 * {@link CheckHits#checkNoMatchExplanations} are checking what they are suppose to.
 */
public class TestBaseExplanationTestCase extends BaseExplanationTestCase {

  public void testQueryNoMatchWhenExpected() throws Exception {
    expectThrows(AssertionFailedError.class, () -> {
        qtest(new TermQuery(new Term(FIELD, "BOGUS")), new int[] { 3 /* none */ });
      });
  }
  public void testQueryMatchWhenNotExpected() throws Exception {
    expectThrows(AssertionFailedError.class, () -> {
        qtest(new TermQuery(new Term(FIELD, "w1")), new int[] { 0, 1 /*, 2, 3 */ });
      });
  }

  public void testIncorrectExplainScores() throws Exception {
    // sanity check what a real TermQuery matches
    qtest(new TermQuery(new Term(FIELD, "zz")), new int[] { 1, 3 });

    // ensure when the Explanations are broken, we get an error about those matches
    expectThrows(AssertionFailedError.class, () -> {
        qtest(new BrokenExplainTermQuery(new Term(FIELD, "zz"), false, true), new int[] { 1, 3 });
              
      });
  }

  public void testIncorrectExplainMatches() throws Exception {
    // sanity check what a real TermQuery matches
    qtest(new TermQuery(new Term(FIELD, "zz")), new int[] { 1, 3 });
    
    // ensure when the Explanations are broken, we get an error about the non matches
    expectThrows(AssertionFailedError.class, () -> {
        CheckHits.checkNoMatchExplanations(new BrokenExplainTermQuery(new Term(FIELD, "zz"), true, false),
                                           FIELD, searcher, new int[] { 1, 3 });
      });
  }
}
