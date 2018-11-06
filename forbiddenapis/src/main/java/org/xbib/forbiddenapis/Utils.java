package org.xbib.forbiddenapis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

/**
 * Some static utilities for analyzing with ASM, also constants.
 */
public final class Utils {

    private static final String REGEX_META_CHARS = ".^$+{}[]|()\\";

    /** Package prefixes of documented Java API (extracted from Javadocs of Java 8). */
    private static final Pattern PORTABLE_RUNTIME_PACKAGE_PATTERN =
            makePkgPrefixPattern("java", "javax", "org.ietf.jgss", "org.omg", "org.w3c.dom", "org.xml.sax");

    /** Pattern that matches all module names, which are shipped by default in Java.
     * (see: {@code http://openjdk.java.net/projects/jigsaw/spec/sotms/}):
     * The remaining platform modules will share the 'java.' name prefix and are likely to include,
     * e.g., java.sql for database connectivity, java.xml for XML processing, and java.logging for
     * logging. Modules that are not defined in the Java SE 9 Platform Specification but instead
     * specific to the JDK will, by convention, share the 'jdk.' name prefix.
     */
    private static final Pattern RUNTIME_MODULES_PATTERN = makePkgPrefixPattern("java", "jdk");

    private Utils() {}

    /**
     * Returns true, if the given binary class name (dotted) is part of the documented and portable Java APIs.
     * @param className class name
     * @return true if class is portable runtime class
     */
    public static boolean isPortableRuntimeClass(String className) {
        return PORTABLE_RUNTIME_PACKAGE_PATTERN.matcher(className).matches();
    }

    /** Returns true, if the given Java module name is part of the runtime (no custom 3rd party module).
     * @param module the module name or {@code null}, if in unnamed module
     * @return true if runtime module
     */
    public static boolean isRuntimeModule(String module) {
        return module != null && RUNTIME_MODULES_PATTERN.matcher(module).matches();
    }

    /**
     * Converts a binary class name (dotted) to the JVM internal one (slashed). Only accepts valid class names, no arrays.
     * @param clazz class name
     * @return internal name
     */
    public static String binaryToInternal(String clazz) {
        if (clazz.indexOf('/') >= 0 || clazz.indexOf('[') >= 0) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "'%s' is not a valid binary class name", clazz));
        }
        return clazz.replace('.', '/');
    }

    /**
     * Converts a binary class name to a &quot;{@code .class}&quot; file resource name.
     * @param clazz class name
     * @return resource name
     */
    public static String getClassResourceName(String clazz) {
        return binaryToInternal(clazz).concat(".class");
    }

    /**
     * Returns true is a string is a glob pattern.
     * @param s string
     * @return true if string is glob pattern
     */
    public static boolean isGlob(String s) {
        return s.indexOf('*') >= 0 || s.indexOf('?') >= 0;
    }

    /**
     * Returns a regex pattern that matches on any of the globs on class names (e.g., "sun.misc.**") .
     *
     * @param globs globs
     * @return pattern
     */
    public static Pattern glob2Pattern(String... globs) {
        final StringBuilder regex = new StringBuilder();
        boolean needOr = false;
        for (String glob : globs) {
            if (needOr) {
                regex.append('|');
            }
            int i = 0, len = glob.length();
            while (i < len) {
                char c = glob.charAt(i++);
                switch (c) {
                    case '*':
                        if (i < len && glob.charAt(i) == '*') {
                            // crosses package boundaries
                            regex.append(".*");
                            i++;
                        } else {
                            // do not cross package boundaries
                            regex.append("[^.]*");
                        }
                        break;

                    case '?':
                        // do not cross package boundaries
                        regex.append("[^.]");
                        break;

                    default:
                        if (isRegexMeta(c)) {
                            regex.append('\\');
                        }
                        regex.append(c);
                }
            }
            needOr = true;
        }
        return Pattern.compile(regex.toString(), 0);
    }

    /**
     * Returns the module name from a {@code jrt:/} URL; returns null if no module given or wrong URL type.
     * @param jrtUrl URL
     * @return module name
     */
    public static String getModuleName(URL jrtUrl) {
        if (!"jrt".equalsIgnoreCase(jrtUrl.getProtocol())) {
            return null;
        }
        try {
            // use URI class to also decode path and remove escapes:
            String mod = jrtUrl.toURI().getPath();
            if (mod != null && mod.length() >= 1) {
                mod = mod.substring(1);
                int p = mod.indexOf('/');
                if (p >= 0) {
                    mod = mod.substring(0, p);
                }
                return mod.isEmpty() ? null : mod;
            }
            return null;
        } catch (URISyntaxException use) {
            return null;
        }
    }

    /** Utility method to load class files of later Java versions by patching them, so ASM can read them.
     * Does nothing at the moment.
     * @param in input stream
     * @return class reader
     * @throws IOException if load fails
     */
    @SuppressForbidden
    @SuppressWarnings("unused")
    public static ClassReader readAndPatchClass(InputStream in) throws IOException {
        byte[] bytecode = readStream(in);
        if (false) {
            patchClassMajorVersion(bytecode, Opcodes.V11 + 1, Opcodes.V11);
        }
        return new ClassReader(bytecode);
    }

    private static void patchClassMajorVersion(byte[] bytecode, int versionFrom, int versionTo) {
        final ByteBuffer buf = ByteBuffer.wrap(bytecode).order(ByteOrder.BIG_ENDIAN);
        if (buf.getShort(6) == versionFrom) {
            buf.putShort(6, (short) versionTo);
        }
    }

    /**
     * This method is used to read the whole stream into byte array. This allows patching.
     * It also works around a bug in ASM 6.1 (https://gitlab.ow2.org/asm/asm/issues/317816).
     */
    private static byte[] readStream(final InputStream in) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final byte[] data = new byte[4096];
        int bytesRead;
        while ((bytesRead = in.read(data, 0, data.length)) != -1) {
            bos.write(data, 0, bytesRead);
        }
        return bos.toByteArray();
    }

    private static Pattern makePkgPrefixPattern(String... prefixes) {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (final String p : prefixes) {
            sb.append(first ? '(' : '|').append(Pattern.quote(p));
            first = false;
        }
        sb.append(")").append(Pattern.quote(".")).append(".*");
        return Pattern.compile(sb.toString());
    }

    private static boolean isRegexMeta(char c) {
        return REGEX_META_CHARS.indexOf(c) != -1;
    }
}
