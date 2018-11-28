/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.test.mockitousage.matchers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.test.mockitoutil.TestBase;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GenericMatchersTest extends TestBase {

    private interface Foo {
        List<String> sort(List<String> otherList);
        String convertDate(Date date);
    }

    @Mock Foo sorter;

    @SuppressWarnings("unchecked")
    @Test
    public void shouldCompile() {
        when(sorter.convertDate(new Date())).thenReturn("one");
        when(sorter.convertDate(any())).thenReturn("two");

        //following requires warning suppression but allows setting anyList()
        when(sorter.sort(ArgumentMatchers.<String>anyList())).thenReturn(null);
    }
}
