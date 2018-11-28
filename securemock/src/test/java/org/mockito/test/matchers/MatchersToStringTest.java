/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.test.matchers;

import java.util.regex.Pattern;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.And;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.Contains;
import org.mockito.internal.matchers.EndsWith;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.Find;
import org.mockito.internal.matchers.Matches;
import org.mockito.internal.matchers.Not;
import org.mockito.internal.matchers.NotNull;
import org.mockito.internal.matchers.Null;
import org.mockito.internal.matchers.Or;
import org.mockito.internal.matchers.Same;
import org.mockito.internal.matchers.StartsWith;
import org.mockito.test.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;

public class MatchersToStringTest extends TestBase {

    @Test
    public void sameToStringWithString() {
        assertEquals("same(\"X\")", new Same("X").toString());
    }

    @Test
    public void nullToString() {
        assertEquals("isNull()", Null.NULL.toString());
    }

    @Test
    public void notNullToString() {
        assertEquals("notNull()", NotNull.NOT_NULL.toString());
    }

    @Test
    public void anyToString() {
        assertEquals("<any>", Any.ANY.toString());
    }

    @Test
    public void sameToStringWithChar() {
        assertEquals("same('x')", new Same('x').toString());
    }

    @Test
    public void sameToStringWithObject() {
        Object o = new Object() {
            @Override
            public String toString() {
                return "X";
            }
        };
        assertEquals("same(X)", new Same(o).toString());
    }

    @Test
    public void equalsToStringWithString() {
        assertEquals("\"X\"", new Equals("X").toString());

    }

    @Test
    public void equalsToStringWithChar() {
        assertEquals("'x'", new Equals('x').toString());
    }

    @Test
    public void equalsToStringWithObject() {
        Object o = new Object() {
            @Override
            public String toString() {
                return "X";
            }
        };
        assertEquals("X", new Equals(o).toString());
    }

    @Test
    public void orToString() {
        ArgumentMatcher<?> m1=new Equals(1);
        ArgumentMatcher<?> m2=new Equals(2);
        assertEquals("or(1, 2)", new Or(m1,m2).toString());
    }

    @Test
    public void notToString() {
        assertEquals("not(1)", new Not(new Equals(1)).toString());
    }

    @Test
    public void andToString() {
        ArgumentMatcher<?> m1=new Equals(1);
        ArgumentMatcher<?> m2=new Equals(2);
        assertEquals("and(1, 2)", new And(m1,m2).toString());
    }

    @Test
    public void startsWithToString() {
        assertEquals("startsWith(\"AB\")", new StartsWith("AB").toString());
    }

    @Test
    public void endsWithToString() {
        assertEquals("endsWith(\"AB\")", new EndsWith("AB").toString());
    }

    @Test
    public void containsToString() {
        assertEquals("contains(\"AB\")", new Contains("AB").toString());
    }

    @Test
    public void findToString() {
        assertEquals("find(\"\\\\s+\")", new Find("\\s+").toString());
    }

    @Test
    public void matchesToString() {
        assertEquals("matches(\"\\\\s+\")", new Matches("\\s+").toString());
        assertEquals("matches(\"\\\\s+\")", new Matches(Pattern.compile("\\s+")).toString());
    }

}
