/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.test.mockitousage.annotation;

import org.junit.After;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.configuration.DefaultMockitoConfiguration;
import org.mockito.test.configuration.ConfigurationAccess;
import org.mockito.internal.configuration.IndependentAnnotationEngine;
import org.mockito.test.mockitoutil.TestBase;

import static org.junit.Assert.*;

public class DeprecatedAnnotationEngineApiTest extends TestBase {

    @After
    public void goBackToDefaultConfiguration() {
        ConfigurationAccess.getConfig().overrideAnnotationEngine(null);
    }

    public class SimpleTestCase {
        @InjectMocks Tested tested = new Tested();
        @Mock Dependency mock;
    }

    public class Tested {
        Dependency dependency;
        public void setDependency(Dependency dependency) {
            this.dependency = dependency;
        }
    }

    public class Dependency {}

    @Test
    public void shouldInjectMocksIfThereIsNoUserDefinedEngine() throws Exception {
        //given
        AnnotationEngine defaultEngine = new DefaultMockitoConfiguration().getAnnotationEngine();
        ConfigurationAccess.getConfig().overrideAnnotationEngine(defaultEngine);
        SimpleTestCase test = new SimpleTestCase();

        //when
        MockitoAnnotations.initMocks(test);

        //then
        assertNotNull(test.mock);
        assertNotNull(test.tested.dependency);
        assertSame(test.mock, test.tested.dependency);
    }

    @Test
    public void shouldRespectUsersEngine() throws Exception {
        //given
        AnnotationEngine customizedEngine = new IndependentAnnotationEngine() { /**/ };
        ConfigurationAccess.getConfig().overrideAnnotationEngine(customizedEngine);
        SimpleTestCase test = new SimpleTestCase();

        //when
        MockitoAnnotations.initMocks(test);

        //then
        assertNotNull(test.mock);
        assertNull(test.tested.dependency);
    }
}
