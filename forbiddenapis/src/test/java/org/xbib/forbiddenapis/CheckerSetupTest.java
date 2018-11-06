package org.xbib.forbiddenapis;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.commons.Method;

import java.util.Collections;
import java.util.EnumSet;

import static org.xbib.forbiddenapis.Checker.Option.FAIL_ON_MISSING_CLASSES;
import static org.xbib.forbiddenapis.Checker.Option.FAIL_ON_UNRESOLVABLE_SIGNATURES;
import static org.xbib.forbiddenapis.Checker.Option.FAIL_ON_VIOLATION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeTrue;

public final class CheckerSetupTest {

    private Checker checker;

    private Signatures forbiddenSignatures;

    @Before
    public void setUp() {
        checker = new Checker(ClassLoader.getSystemClassLoader(),
                EnumSet.of(FAIL_ON_MISSING_CLASSES, FAIL_ON_VIOLATION, FAIL_ON_UNRESOLVABLE_SIGNATURES), null);
        assumeTrue("This test only works with a supported JDK (see docs)", checker.isSupportedJDK);
        assertEquals(EnumSet.of(FAIL_ON_MISSING_CLASSES, FAIL_ON_VIOLATION, FAIL_ON_UNRESOLVABLE_SIGNATURES), checker.options);
        forbiddenSignatures = checker.forbiddenSignatures;
    }

    @Test
    public void testEmpty() {
        assertEquals(Collections.emptyMap(), forbiddenSignatures.signatures);
        assertEquals(Collections.emptySet(), forbiddenSignatures.classPatterns);
        assertTrue(checker.hasNoSignatures());
    }

    @Test
    public void testClassSignature() throws Exception {
        checker.parseSignaturesString("java.lang.Object @ Foobar");
        assertEquals(Collections.singletonMap(Signatures.getKey("java/lang/Object"), "java.lang.Object [Foobar]"), forbiddenSignatures.signatures);
        assertEquals(Collections.emptySet(), forbiddenSignatures.classPatterns);
    }

    @Test
    public void testClassPatternSignature() throws Exception {
        checker.parseSignaturesString("java.lang.** @ Foobar");
        assertEquals(Collections.emptyMap(), forbiddenSignatures.signatures);
        assertEquals(Collections.singleton(new ClassPatternRule("java.lang.**", "Foobar")),
                forbiddenSignatures.classPatterns);
    }

    @Test
    public void testFieldSignature() throws Exception {
        checker.parseSignaturesString("java.lang.String#CASE_INSENSITIVE_ORDER @ Foobar");
        assertEquals(Collections.singletonMap(Signatures.getKey("java/lang/String", "CASE_INSENSITIVE_ORDER"), "java.lang.String#CASE_INSENSITIVE_ORDER [Foobar]"),
                forbiddenSignatures.signatures);
        assertEquals(Collections.emptySet(), forbiddenSignatures.classPatterns);
    }

    @Test
    public void testMethodSignature() throws Exception {
        checker.parseSignaturesString("java.lang.Object#toString() @ Foobar");
        assertEquals(Collections.singletonMap(Signatures.getKey("java/lang/Object", new Method("toString", "()Ljava/lang/String;")), "java.lang.Object#toString() [Foobar]"),
                forbiddenSignatures.signatures);
        assertEquals(Collections.emptySet(), forbiddenSignatures.classPatterns);
    }

    @Test
    public void testEmptyCtor() throws Exception {
        Checker chk = new Checker(ClassLoader.getSystemClassLoader());
        assertEquals(EnumSet.noneOf(Checker.Option.class), chk.options);
    }

    @Test
    public void testRuntimeClassSignatures(){
        String internalName = "java/lang/String";
        ClassSignature cs = checker.lookupRelatedClass(internalName, internalName);
        assertTrue(cs.isRuntimeClass);
        assertTrue(cs.signaturePolymorphicMethods.isEmpty());
    }

    @Test
    public void testSignaturePolymorphic() {
        try {
            String internalName = "java/lang/invoke/MethodHandle";
            ClassSignature cs = checker.lookupRelatedClass(internalName, internalName);
            assertTrue(cs.signaturePolymorphicMethods.contains("invoke"));
            assertTrue(cs.signaturePolymorphicMethods.contains("invokeExact"));
        } catch (RelatedClassLoadingException we) {
            assertTrue(we.getCause() instanceof ClassNotFoundException);
            assumeNoException("This test only works with Java 7+", we);
        }
    }

    @Test
    public void testJava9ModuleSystemFallback() {
        final Class<?> moduleClass;
        try {
            moduleClass = Class.forName("java.lang.Module");
        } catch (ClassNotFoundException cfe) {
            assumeNoException("This test only works with Java 9+", cfe);
            return;
        }
        assertNotNull(checker.methodClassGetModule);
        assertSame(moduleClass, checker.methodClassGetModule.getReturnType());
        assertNotNull(checker.methodModuleGetName);
        assertSame(moduleClass, checker.methodModuleGetName.getDeclaringClass());
    }

}
