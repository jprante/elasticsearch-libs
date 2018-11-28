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
package org.apache.lucene.test.analysis.tokenattributes;

import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttributeImpl;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttributeImpl;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;
import org.apache.lucene.analysis.tokenattributes.FlagsAttributeImpl;
import org.apache.lucene.analysis.tokenattributes.FlagsAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttributeImpl;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.tokenattributes.PayloadAttributeImpl;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.analysis.tokenattributes.KeywordAttributeImpl;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttributeImpl;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.testframework.util.TestUtil;
import org.apache.lucene.testframework.util.LuceneTestCase;

import java.util.Collections;
import java.util.HashMap;

public class TestSimpleAttributeImpl extends LuceneTestCase {

  // this checks using reflection API if the defaults are correct
  public void testAttributes() {
    TestUtil.assertAttributeReflection(new PositionIncrementAttributeImpl(),
        Collections.singletonMap(PositionIncrementAttribute.class.getName() + "#positionIncrement", 1));
    TestUtil.assertAttributeReflection(new PositionLengthAttributeImpl(),
        Collections.singletonMap(PositionLengthAttribute.class.getName() + "#positionLength", 1));
    TestUtil.assertAttributeReflection(new FlagsAttributeImpl(),
        Collections.singletonMap(FlagsAttribute.class.getName() + "#flags", 0));
    TestUtil.assertAttributeReflection(new TypeAttributeImpl(),
        Collections.singletonMap(TypeAttribute.class.getName() + "#type", TypeAttribute.DEFAULT_TYPE));
    TestUtil.assertAttributeReflection(new PayloadAttributeImpl(),
        Collections.singletonMap(PayloadAttribute.class.getName() + "#payload", null));
    TestUtil.assertAttributeReflection(new KeywordAttributeImpl(),
        Collections.singletonMap(KeywordAttribute.class.getName() + "#keyword", false));
    TestUtil.assertAttributeReflection(new OffsetAttributeImpl(), new HashMap<String, Object>() {{
      put(OffsetAttribute.class.getName() + "#startOffset", 0);
      put(OffsetAttribute.class.getName() + "#endOffset", 0);
    }});
  }

}
