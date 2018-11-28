/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.test.mockitousage.matchers;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.test.mockitousage.IMethods;
import org.mockito.test.mockitoutil.TestBase;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MoreMatchersTest extends TestBase {

    @Mock private IMethods mock;

    @Test
    public void should_help_out_with_unnecessary_casting() {
        when(mock.objectArgMethod(any(String.class))).thenReturn("string");

        assertEquals("string", mock.objectArgMethod("foo"));
    }

    @Test
    public void any_should_be_actual_alias_to_any() {
        mock.simpleMethod((Object) null);

        verify(mock).simpleMethod((String) any());
    }

    @Test
    public void any_class_should_be_actual_alias_to_isA() {
        mock.simpleMethod(new ArrayList());

        verify(mock).simpleMethod(isA(List.class));
        verify(mock).simpleMethod(any(List.class));


        mock.simpleMethod((String) null);
        try {
            verify(mock).simpleMethod(isA(String.class));
            fail();
        } catch (AssertionError ignored) { }
        try {
            verify(mock).simpleMethod(any(String.class));
            fail();
        } catch (AssertionError ignored) { }
    }

    @Test
    public void should_help_out_with_unnecessary_casting_of_lists() {
        when(mock.listArgMethod(anyList())).thenReturn("list");

        assertEquals("list", mock.listArgMethod(new LinkedList<String>()));
        assertEquals("list", mock.listArgMethod(Collections.<String>emptyList()));
    }

    @Test
    public void should_help_out_with_unnecessary_casting_of_sets() {
        when(mock.setArgMethod(anySet())).thenReturn("set");

        assertEquals("set", mock.setArgMethod(new HashSet<String>()));
        assertEquals("set", mock.setArgMethod(Collections.<String>emptySet()));
    }

    @Test
    public void should_help_out_with_unnecessary_casting_of_maps() {
        when(mock.setArgMethod(anySet())).thenReturn("set");

        assertEquals("map", mock.forMap(new HashMap<String, String>()));
        assertEquals("map", mock.forMap(Collections.<String, String>emptyMap()));
    }

    @Test
    public void should_help_out_with_unnecessary_casting_of_collections() {
        when(mock.setArgMethod(anySet())).thenReturn("set");
        when(mock.collectionArgMethod(anyCollection())).thenReturn("collection");

        assertEquals("collection", mock.collectionArgMethod(new ArrayList<String>()));
        assertEquals("collection", mock.collectionArgMethod(Collections.<String>emptyList()));
    }

    @Test
    public void should_help_out_with_unnecessary_casting_of_iterables() {
        when(mock.setArgMethod(anySet())).thenReturn("set");
        when(mock.iterableArgMethod(anyIterable())).thenReturn("iterable");

        assertEquals("iterable", mock.iterableArgMethod(new ArrayList<String>()));
        assertEquals("iterable", mock.iterableArgMethod(Collections.<String>emptyList()));
    }

    @Test
    public void should_help_out_with_unnecessary_casting_of_nullity_checks() {
        when(mock.objectArgMethod(isNull())).thenReturn("string");
        when(mock.objectArgMethod(notNull())).thenReturn("string");
        when(mock.objectArgMethod(isNotNull())).thenReturn("string");

        assertEquals("string", mock.objectArgMethod(null));
        assertEquals("string", mock.objectArgMethod("foo"));
        assertEquals("string", mock.objectArgMethod("foo"));
    }

}
