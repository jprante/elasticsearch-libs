package org.xbib.forbiddenapis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;

/**
 * Forbidden APIs checker class.
 */
public final class Checker implements RelatedClassLookup, Constants {

    private static final Logger logger = Logger.getLogger(Checker.class.getName());

    public enum Option {
        FAIL_ON_MISSING_CLASSES,
        FAIL_ON_VIOLATION,
        FAIL_ON_UNRESOLVABLE_SIGNATURES,
        DISABLE_CLASSLOADING_CACHE
    }

    public final boolean isSupportedJDK;

    private final CheckListener checkListener;

    private final long start;

    private final NavigableSet<String> runtimePaths;

    private final ClassLoader loader;

    final Method methodClassGetModule;

    final Method methodModuleGetName;

    final EnumSet<Option> options;

    /** Classes to check: key is the binary name (dotted) */
    private final Map<String, ClassSignature> classesToCheck = new HashMap<>();

    /** Cache of loaded classes: key is the binary name (dotted) */
    private final Map<String,ClassSignature> classpathClassCache = new HashMap<>();

    final Signatures forbiddenSignatures;

    /** descriptors (not internal names) of all annotations that suppress */
    private final Set<String> suppressAnnotations = new LinkedHashSet<String>();

    public Checker(ClassLoader loader) {
        this(loader, EnumSet.noneOf(Option.class), null);
    }

    public Checker(ClassLoader loader, EnumSet<Option> options, CheckListener checkListener) {
        this.loader = loader;
        this.options = options;
        this.checkListener = checkListener;
        this.start = System.currentTimeMillis();

        // default (always available)
        addSuppressAnnotation(SuppressForbidden.class);

        boolean isSupportedJDK = false;

        // Try to figure out entry points to Java module system
        Method methodClassGetModule;
        Method methodModuleGetName;
        try {
            methodClassGetModule = Class.class.getMethod("getModule");
            methodModuleGetName = methodClassGetModule.getReturnType().getMethod("getName");
            isSupportedJDK = true;
        } catch (NoSuchMethodException e) {
            methodClassGetModule = methodModuleGetName = null;
        }
        this.methodClassGetModule = methodClassGetModule;
        this.methodModuleGetName = methodModuleGetName;

        NavigableSet<String> runtimePaths = new TreeSet<>();

        // fall back to legacy behavior:
        if (!isSupportedJDK) {
            try {
                URL objectClassURL = loader.getResource(Utils.getClassResourceName(Object.class.getName()));
                if (objectClassURL != null && "jrt".equalsIgnoreCase(objectClassURL.getProtocol())) {
                    // this is Java 9+ allowing direct access to .class file resources - we do not need to deal with modules!
                    isSupportedJDK = true;
                } else {
                    String javaHome = System.getProperty("java.home");
                    if (javaHome != null) {
                        javaHome = new File(javaHome).getCanonicalPath();
                        if (!javaHome.endsWith(File.separator)) {
                            javaHome += File.separator;
                        }
                        runtimePaths.add(javaHome);
                    }
                    isSupportedJDK = !runtimePaths.isEmpty();
                    if (!isSupportedJDK) {
                        logger.log(Level.WARNING, "Boot classpath appears to be empty or ${java.home} not defined; marking runtime as not suppported");
                    }
                }
            } catch (IOException ioe) {
                logger.log(Level.WARNING, "Cannot scan boot classpath and ${java.home} due to IO exception; marking runtime as not suppported: " + ioe);
                isSupportedJDK = false;
                runtimePaths.clear();
            }
        }
        this.runtimePaths = runtimePaths;
        if (isSupportedJDK) {
            try {
                isSupportedJDK = getClassFromClassLoader(Object.class.getName()).isRuntimeClass;
                if (!isSupportedJDK) {
                    logger.log(Level.WARNING,"Bytecode of java.lang.Object does not seem to come from runtime library; marking runtime as not suppported");
                }
            } catch (IllegalArgumentException iae) {
                logger.log(Level.WARNING, "Bundled version of ASM cannot parse bytecode of java.lang.Object class; marking runtime as not suppported");
                isSupportedJDK = false;
            } catch (ClassNotFoundException cnfe) {
                logger.log(Level.WARNING, "Bytecode or Class<?> instance of java.lang.Object not found; marking runtime as not suppported");
                isSupportedJDK = false;
            } catch (IOException ioe) {
                logger.log(Level.WARNING, "IOException while loading java.lang.Object class from classloader; marking runtime as not suppported: " + ioe);
                isSupportedJDK = false;
            }
        }
        this.isSupportedJDK = isSupportedJDK;
        // make signatures ready for parse:
        this.forbiddenSignatures = new Signatures(this);
    }

    /** Loads the class from Java's module system and uses reflection to get methods and fields. */
    private ClassSignature loadClassFromModulePath(String classname) {
        if (methodClassGetModule == null || methodModuleGetName == null) {
            return null; // no Module System
        }
        Class<?> clazz;
        String moduleName;
        try {
            clazz = Class.forName(classname, false, loader);
            Object module = methodClassGetModule.invoke(clazz);
            moduleName = (String) methodModuleGetName.invoke(module);
        } catch (Exception e) {
            return null; // not found
        }
        return new ClassSignature(clazz, Utils.isRuntimeModule(moduleName));
    }

    private boolean isRuntimePath(URL url) throws IOException {
        if (!"file".equalsIgnoreCase(url.getProtocol())) {
            return false;
        }
        try {
            String path = new File(url.toURI()).getCanonicalPath();
            String lookup = runtimePaths.floor(path);
            return lookup != null && path.startsWith(lookup);
        } catch (URISyntaxException e) {
            // should not happen, but if it's happening, it's definitely not a below our paths
            return false;
        }
    }

    private boolean isRuntimeClass(URLConnection conn) throws IOException {
        URL url = conn.getURL();
        if (isRuntimePath(url)) {
            return true;
        } else if ("jar".equalsIgnoreCase(url.getProtocol()) && conn instanceof JarURLConnection) {
            URL jarUrl = ((JarURLConnection) conn).getJarFileURL();
            return isRuntimePath(jarUrl);
        } else if ("jrt".equalsIgnoreCase(url.getProtocol())) {
            // all 'jrt:' URLs refer to a module in the Java  runtime (see http://openjdk.java.net/jeps/220)
            return Utils.isRuntimeModule(Utils.getModuleName(url));
        }
        return false;
    }

    /** Reads a class (binary name) from the given {@link ClassLoader}. If not found there, falls back to the list of classes to be checked. */
    @Override
    public ClassSignature getClassFromClassLoader(String clazz) throws ClassNotFoundException,IOException {
        if (classpathClassCache.containsKey(clazz)) {
            ClassSignature c = classpathClassCache.get(clazz);
            if (c == null) {
                throw new ClassNotFoundException(clazz);
            }
            return c;
        } else {
            URL url = loader.getResource(Utils.getClassResourceName(clazz));
            if (url != null) {
                URLConnection conn = url.openConnection();
                boolean isRuntimeClass = isRuntimeClass(conn);
                if (!isRuntimeClass && options.contains(Option.DISABLE_CLASSLOADING_CACHE)) {
                    conn.setUseCaches(false);
                }
                ClassReader cr;
                try (InputStream in = conn.getInputStream()) {
                    cr = Utils.readAndPatchClass(in);
                } catch (IllegalArgumentException iae) {
                    if (isRuntimeClass) {
                        ClassSignature c = loadClassFromModulePath(clazz);
                        if (c != null) {
                            classpathClassCache.put(clazz, c);
                            return c;
                        }
                    }
                    // unfortunately the ASM IAE has no message, so add good info!
                    throw new IllegalArgumentException(String.format(Locale.ROOT,
                            "The class file format of '%s' (loaded from location '%s') is too recent to be parsed by ASM",
                            clazz, url.toExternalForm()));
                }
                ClassSignature c = new ClassSignature(cr, isRuntimeClass, false);
                classpathClassCache.put(clazz, c);
                return c;
            } else {
                ClassSignature c = loadClassFromModulePath(clazz);
                if (c != null) {
                    classpathClassCache.put(clazz, c);
                    return c;
                }
            }
            // try to get class from our list of classes we are checking:
            ClassSignature c = classesToCheck.get(clazz);
            if (c != null) {
                classpathClassCache.put(clazz, c);
                return c;
            }
            // all failed => the class does not exist!
            classpathClassCache.put(clazz, null);
            throw new ClassNotFoundException(clazz);
        }
    }

    @Override
    public ClassSignature lookupRelatedClass(String internalName, String internalNameOrig) {
        Type type = Type.getObjectType(internalName);
        if (type.getSort() != Type.OBJECT) {
            return null;
        }
        try {
            // use binary name, so we need to convert:
            return getClassFromClassLoader(type.getClassName());
        } catch (ClassNotFoundException cnfe) {
            String origClassName = Type.getObjectType(internalNameOrig).getClassName();
            if (options.contains(Option.FAIL_ON_MISSING_CLASSES)) {
                throw new RelatedClassLoadingException(cnfe, origClassName);
            } else {
                logger.log(Level.WARNING, String.format(Locale.ROOT,
                        "class '%s' cannot be loaded (while looking up details about referenced class '%s')",
                        type.getClassName(), origClassName
                ));
                if (checkListener != null) {
                    checkListener.missing(type.getClassName());
                }
                return null;
            }
        } catch (IOException ioe) {
            throw new RelatedClassLoadingException(ioe, Type.getObjectType(internalNameOrig).getClassName());
        }
    }

    /**
     * Reads a list of bundled API signatures from classpath.
     * @param name name
     * @param jdkTargetVersion JDK target version
     * @throws IOException if add fails
     * @throws ParseException if parse fails
     */
    public void addBundledSignatures(String name, String jdkTargetVersion) throws IOException, ParseException {
        forbiddenSignatures.addBundledSignatures(name, jdkTargetVersion);
    }

    /**
     * Reads a list of API signatures. Closes the Reader when done (on Exception, too).
     * @param in input stream
     * @param name name
     * @throws IOException if read fails
     * @throws ParseException if parse fails
     */
    public void parseSignaturesFile(InputStream in, String name) throws IOException, ParseException {
        forbiddenSignatures.parseSignaturesStream(in, name);
    }

    /**
     * Reads a list of API signatures from the given URL.
     * @param url URL
     * @throws IOException if read fails
     * @throws ParseException if parse fails
     */
    public void parseSignaturesFile(URL url) throws IOException, ParseException {
        parseSignaturesFile(url.openStream(), url.toString());
    }

    /**
     * Reads a list of API signatures from the given file.
     * @param f file
     * @throws IOException if read fails
     * @throws ParseException if parse fails
     */
    public void parseSignaturesFile(File f) throws IOException, ParseException {
        parseSignaturesFile(new FileInputStream(f), f.toString());
    }

    /**
     * Reads a list of API signatures from a String.
     * @param signatures signatures
     * @throws IOException if read fails
     * @throws ParseException if parse fails
     */
    public void parseSignaturesString(String signatures) throws IOException, ParseException {
        forbiddenSignatures.parseSignaturesString(signatures);
    }

    /**
     * Returns if there are any signatures.
     * @return true if there are any signatures
     */
    public boolean hasNoSignatures() {
        return forbiddenSignatures.hasNoSignatures();
    }

    /**
     * Parses and adds a class from the given stream to the list of classes to check.
     * Closes the stream when parsed (on Exception, too)! Does not log anything.
     * @param in input stream
     * @param name name
     * @throws IOException if read fails
     */
    public void addClassToCheck(InputStream in, String name) throws IOException {
        ClassReader reader;
        try (in) {
            reader = Utils.readAndPatchClass(in);
        } catch (IllegalArgumentException iae) {
            // unfortunately the ASM IAE has no message, so add good info
            throw new IllegalArgumentException(String.format(Locale.ROOT,
                    "The class file format of '%s' is too recent to be parsed by ASM", name));
        }
        String binaryName = Type.getObjectType(reader.getClassName()).getClassName();
        classesToCheck.put(binaryName, new ClassSignature(reader, false, true));
    }

    /**
     * Parses and adds a class from the given file to the list of classes to check. Does not log anything.
     * @param f file
     * @throws IOException if file read fails
     */
    public void addClassToCheck(File f) throws IOException {
        addClassToCheck(new FileInputStream(f), f.toString());
    }

    /**
     * Parses and adds a multiple class files.
     * @param files files
     * @throws IOException if file read fails
     */
    public void addClassesToCheck(Iterable<File> files) throws IOException {
        logger.log(Level.INFO, "Loading classes to check");
        for (File f : files) {
            addClassToCheck(f);
        }
    }

    /**
     * Parses and adds a multiple class files.
     * @param files files
     * @throws IOException if file read fails
     */
    public void addClassesToCheck(File... files) throws IOException {
        addClassesToCheck(Arrays.asList(files));
    }

    /**
     * Parses and adds a multiple class files.
     * @param basedir base dir
     * @param relativeNames relative names
     * @throws IOException if file read fails
     */
    public void addClassesToCheck(File basedir, Iterable<String> relativeNames) throws IOException {
        logger.log(Level.INFO, "Loading classes to check");
        for (String f : relativeNames) {
            addClassToCheck(new File(basedir, f));
        }
    }

    /**
     * Parses and adds a multiple class files.
     * @param basedir base dir
     * @param relativeNames relative names
     * @throws IOException if parse fails
     */
    public void addClassesToCheck(File basedir, String... relativeNames) throws IOException {
        addClassesToCheck(basedir, Arrays.asList(relativeNames));
    }

    /**
     * Adds the given annotation class for suppressing errors.
     * @param anno annotation
     */
    public void addSuppressAnnotation(Class<? extends Annotation> anno) {
        suppressAnnotations.add(anno.getName());
    }

    /**
     * Adds suppressing annotation name in binary form (dotted). It may also be a glob pattern.
     * The class name is not checked for existence.
     * @param annoName annotation name
     */
    public void addSuppressAnnotation(String annoName) {
        suppressAnnotations.add(annoName);
    }

    /**
     * Parses a class and checks for valid method invocations.
     * @param reader reader
     * @param suppressAnnotationsPattern pattern
     * @return size of violations
     */
    private int checkClass(ClassReader reader, Pattern suppressAnnotationsPattern) throws ForbiddenApiException {
        String className = Type.getObjectType(reader.getClassName()).getClassName();
        ClassScanner scanner = new ClassScanner(this, forbiddenSignatures, suppressAnnotationsPattern);
        try {
            reader.accept(scanner, ClassReader.SKIP_FRAMES);
        } catch (RelatedClassLoadingException rcle) {
            Exception cause = rcle.getException();
            StringBuilder msg = new StringBuilder()
                    .append("Check for forbidden API calls failed while scanning class '")
                    .append(className)
                    .append('\'');
            String source = scanner.getSourceFile();
            if (source != null) {
                msg.append(" (").append(source).append(')');
            }
            msg.append(": ").append(cause);
            msg.append(" (while looking up details about referenced class '").append(rcle.getClassName()).append("')");
            throw new ForbiddenApiException(msg.toString(), cause);
        }
        List<ForbiddenViolation> violations = scanner.getSortedViolations();
        Pattern splitter = Pattern.compile(Pattern.quote(ForbiddenViolation.SEPARATOR));
        for (ForbiddenViolation v : violations) {
            for (String line : splitter.split(v.format(className, scanner.getSourceFile()))) {
                if (checkListener != null) {
                    checkListener.violation(line);
                }
                logger.log(Level.WARNING, line);
            }
        }
        return violations.size();
    }

    public void run() throws ForbiddenApiException {
        logger.log(Level.INFO, "Scanning classes for violations");
        int errors = 0;
        Pattern suppressAnnotationsPattern = Utils.glob2Pattern(suppressAnnotations.toArray(new String[0]));
        for (ClassSignature c : classesToCheck.values()) {
            errors += checkClass(c.getReader(), suppressAnnotationsPattern);
        }
        String message = String.format(Locale.ROOT,
                "Scanned %d class file(s) for forbidden API invocations (in %.2fs), %d error(s)",
                classesToCheck.size(), (System.currentTimeMillis() - start) / 1000.0, errors);
        if (options.contains(Option.FAIL_ON_VIOLATION) && errors > 0) {
            logger.log(Level.SEVERE, message);
            throw new ForbiddenApiException("Check for forbidden API calls failed, see log");
        } else {
            logger.log(Level.INFO, message);
        }
    }

}
