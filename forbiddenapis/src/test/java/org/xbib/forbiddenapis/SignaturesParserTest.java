package org.xbib.forbiddenapis;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public final class SignaturesParserTest {

    @Test
    public void testTargetVersionFix() throws Exception {
        assertEquals("jdk-dummy-1.7", Signatures.fixTargetVersion("jdk-dummy-1.7"));
        assertEquals("jdk-dummy-1.7", Signatures.fixTargetVersion("jdk-dummy-7"));
        assertEquals("jdk-dummy-1.7", Signatures.fixTargetVersion("jdk-dummy-7.0"));

        assertEquals("jdk-dummy-1.1", Signatures.fixTargetVersion("jdk-dummy-1.1"));

        assertEquals("jdk-dummy-9", Signatures.fixTargetVersion("jdk-dummy-9"));
        assertEquals("jdk-dummy-9", Signatures.fixTargetVersion("jdk-dummy-9.0"));

        assertEquals("jdk-dummy-18", Signatures.fixTargetVersion("jdk-dummy-18"));
        assertEquals("jdk-dummy-18.3", Signatures.fixTargetVersion("jdk-dummy-18.3"));
        assertEquals("jdk-dummy-18.3", Signatures.fixTargetVersion("jdk-dummy-18.03"));

        assertFails("jdk-dummy-0");
        assertFails("jdk-dummy-1");

        assertFails("jdk-dummy-1.7.1");
        assertFails("jdk-dummy-1.7.1.1");
        assertFails("jdk-dummy-1.7.0.1");

        assertFails("jdk-dummy-7.1");
        assertFails("jdk-dummy-7.1.1");
        assertFails("jdk-dummy-7.0.1");

        assertFails("jdk-dummy-1.9");
        assertFails("jdk-dummy-9.0.1");
        assertFails("jdk-dummy-9.0.0.1");
    }

    private void assertFails(String name) {
        try {
            Signatures.fixTargetVersion(name);
            fail("Should fail for: " + name);
        } catch (ParseException pe) {
            // pass
        }
    }

}
