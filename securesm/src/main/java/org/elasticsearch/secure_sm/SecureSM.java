/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.secure_sm;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.util.Objects;

/**
 * Extension of SecurityManager that works around a few design flaws in Java Security.
 * <p>
 * There are a few major problems that require custom {@code SecurityManager} logic to fix:
 * <ul>
 *   <li>{@code exitVM} permission is implicitly granted to all code by the default
 *       Policy implementation. For a server app, this is not wanted. </li>
 *   <li>ThreadGroups are not enforced by default, instead only system threads are
 *       protected out of box by {@code modifyThread/modifyThreadGroup}. Applications
 *       are encouraged to override the logic here to implement a stricter policy.
 *   <li>System threads are not even really protected, because if the system uses
 *       ThreadPools, {@code modifyThread} is abused by its {@code shutdown} checks. This means
 *       a thread must have {@code modifyThread} to even terminate its own pool, leaving
 *       system threads unprotected.
 * </ul>
 * This class throws exception on {@code exitVM} calls, and provides a whitelist where calls
 * from exit are allowed.
 * <p>
 * Additionally it enforces threadgroup security with the following rules:
 * <ul>
 *   <li>{@code modifyThread} and {@code modifyThreadGroup} are required for any thread access
 *       checks: with these permissions, access is granted as long as the thread group is
 *       the same or an ancestor ({@code sourceGroup.parentOf(targetGroup) == true}). 
 *   <li>code without these permissions can do very little, except to interrupt itself. It may
 *       not even create new threads.
 *   <li>very special cases (like test runners) that have {@link ThreadPermission} can violate 
 *       threadgroup security rules.
 * </ul>
 * <p>
 * If java security debugging ({@code java.security.debug}) is enabled, and this SecurityManager
 * is installed, it will emit additional debugging information.
 *  
 * @see SecurityManager#checkAccess(Thread)
 * @see SecurityManager#checkAccess(ThreadGroup)
 * @see <a href="http://cs.oswego.edu/pipermail/concurrency-interest/2009-August/006508.html">
 *         http://cs.oswego.edu/pipermail/concurrency-interest/2009-August/006508.html</a>
 */
public class SecureSM extends SecurityManager {

    private final String[] classesThatCanExit;

    /**
     * Creates a new security manager where no packages can exit nor halt the virtual machine.
     */
    public SecureSM() {
        this(new String[0]);
    }

    /**
     * Creates a new security manager with the specified list of regular expressions as the those that class names will be tested against to
     * check whether or not a class can exit or halt the virtual machine.
     *
     * @param classesThatCanExit the list of classes that can exit or halt the virtual machine
     */
    public SecureSM(final String[] classesThatCanExit) {
        this.classesThatCanExit = classesThatCanExit;
    }

    /**
     * Creates a new security manager with a standard set of test packages being the only packages that can exit or halt the virtual
     * machine. The packages that can exit are:
     * <ul>
     *    <li><code>org.apache.maven.surefire.booter.</code></li>
     *    <li><code>com.carrotsearch.ant.tasks.junit4.</code></li>
     *    <li><code>org.eclipse.internal.junit.runner.</code></li>
     *    <li><code>com.intellij.rt.execution.junit.</code></li>
     * </ul>
     *
     * @return an instance of SecureSM where test packages can halt or exit the virtual machine
     */
    public static SecureSM createTestSecureSM() {
        return new SecureSM(TEST_RUNNER_PACKAGES);
    }

    static final String[] TEST_RUNNER_PACKAGES = new String[] {
        // surefire test runner
        "org\\.apache\\.maven\\.surefire\\.booter\\..*",
        // junit4 test runner
        "com\\.carrotsearch\\.ant\\.tasks\\.junit4\\.slave\\..*",
        // eclipse test runner
        "org\\.eclipse.jdt\\.internal\\.junit\\.runner\\..*",
        // intellij test runner
        "com\\.intellij\\.rt\\.execution\\.junit\\..*"
    };

    // java.security.debug support
    private static final boolean DEBUG_ALL = AccessController.doPrivileged((PrivilegedAction<Boolean>) () -> {
        try {
            String v = System.getProperty("java.security.debug");
            return v != null && v.contains("all");
        } catch (SecurityException e) {
            return false;
        }
    });

    private static final boolean DEBUG_PERMISSIONS = AccessController.doPrivileged((PrivilegedAction<Boolean>) () -> {
        try {
            String v = System.getProperty("java.security.debug");
            return v != null && v.contains("permissions");
        } catch (SecurityException e) {
            return false;
        }
    });

    private static final boolean DEBUG_ACCESS = AccessController.doPrivileged((PrivilegedAction<Boolean>) () -> {
        try {
            String v = System.getProperty("java.security.debug");
            return v != null && v.contains("access");
        } catch (SecurityException e) {
            return false;
        }
    });

    @Override
    public void checkPermission(Permission permission) {
        if (DEBUG_PERMISSIONS) {
            System.err.println("checkPermission " + permission);
            Thread.currentThread().dumpStack();
        }
        super.checkPermission(permission);
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
        if (DEBUG_PERMISSIONS) {
            System.err.println("checkPermission " + perm + " context = " + context);
            Thread.currentThread().dumpStack();
        }
        super.checkPermission(perm, context);
    }

    @Override
    public void checkAccept (String host, int port) {
        if (DEBUG_ALL) {
            System.err.println("checkAccept " + host + " port = " + port);
            Thread.currentThread().dumpStack();
        }
        super.checkAccept(host, port);
    }

    @Override
    public void checkConnect(String host, int port) {
        if (DEBUG_ALL) {
            System.err.println("checkConnect " + host + " port = " + port);
            Thread.currentThread().dumpStack();
        }
        super.checkConnect(host, port);
    }

    @Override
    public void checkConnect(String host, int port, Object context) {
        if (DEBUG_ALL) {
            System.err.println("checkConnect " + host + " port = " + port + " context = " + context);
            Thread.currentThread().dumpStack();
        }
        super.checkConnect(host, port, context);
    }


    @Override
    public void checkCreateClassLoader () {
        if (DEBUG_ALL) {
            System.err.println("checkCreateClassLoader");
            Thread.currentThread().dumpStack();
        }
        super.checkCreateClassLoader();
    }

    @Override
    public void checkExec(String cmd) {
        if (DEBUG_ALL) {
            System.err.println("checkExec " + cmd);
            Thread.currentThread().dumpStack();
        }
        super.checkExec(cmd);
    }

    @Override
    public void checkLink(String lib) {
        if (DEBUG_ALL) {
            System.err.println("checkLink " + lib);
            Thread.currentThread().dumpStack();
        }
        super.checkLink(lib);
    }

    @Override
    public void checkListen(int port) {
        if (DEBUG_ALL) {
            System.err.println("checkListen port = " + port);
            Thread.currentThread().dumpStack();
        }
        super.checkListen(port);
    }

    public void checkMulticast(InetAddress maddr) {
        if (DEBUG_ALL) {
            System.err.println("checkMulticast " + maddr);
            Thread.currentThread().dumpStack();
        }
        super.checkMulticast(maddr);
    }

    public void checkPropertyAccess(String key) {
        if (DEBUG_ALL) {
            System.err.println("checkPropertyAccess " + key);
            Thread.currentThread().dumpStack();
        }
        super.checkPropertyAccess(key);
    }

    @Override
    public void checkPropertiesAccess() {
        if (DEBUG_ALL) {
            System.err.println("checkPropertiesAccess");
            Thread.currentThread().dumpStack();
        }
        super.checkPropertiesAccess();
    }

    @Override
    public void checkRead(String file) {
        if (DEBUG_ALL) {
            System.err.println("checkRead " + file);
            Thread.currentThread().dumpStack();
        }
        super.checkRead(file);
    }

    @Override
    public void checkRead(String file, Object context) {
        if (DEBUG_ALL) {
            System.err.println("checkRead " + file + " context = " + context);
            Thread.currentThread().dumpStack();
        }
        super.checkRead(file, context);
    }

    @Override
    public void checkRead(FileDescriptor fd) {
        if (DEBUG_ALL) {
            System.err.println("checkRead " + fd);
            Thread.currentThread().dumpStack();
        }
        super.checkRead(fd);
    }

    @Override
    public void checkWrite (String file) {
        if (DEBUG_ALL) {
            System.err.println("checkWrite " + file);
            Thread.currentThread().dumpStack();
        }
        super.checkWrite(file);
    }

    @Override
    public void checkWrite(FileDescriptor fd) {
        if (DEBUG_ALL) {
            System.err.println("checkWrite " + fd);
            Thread.currentThread().dumpStack();
        }
        super.checkWrite(fd);
    }

    @Override
    public void checkDelete(String file) {
        if (DEBUG_ALL) {
            System.err.println("checkDelete " + file);
            Thread.currentThread().dumpStack();
        }
        super.checkDelete(file);
    }

    @Override
    @SuppressForbidden(reason = "java.security.debug messages go to standard error")
    public void checkAccess(Thread t) {
        try {
            checkThreadAccess(t);
        } catch (SecurityException e) {
            if (DEBUG_ACCESS) {
                System.err.println("checkAccess: caller thread = " + Thread.currentThread() +
                                " caller group = " + Thread.currentThread().getThreadGroup() +
                        " target thread = " + t +
                        " target thread group = " + t.getThreadGroup());
            }
            throw e;
        }
    }
    
    @Override
    @SuppressForbidden(reason = "java.security.debug messages go to standard error")
    public void checkAccess(ThreadGroup g) {
        try {
            checkThreadGroupAccess(g);
        } catch (SecurityException e) {
            if (DEBUG_ACCESS) {
                System.err.println("checkAccess (threadgroup): caller thread = " + Thread.currentThread() +
                        " caller group = " + Thread.currentThread().getThreadGroup() +
                        " target thread group = " + g);
            }
            throw e;
        }
    }
    

    private static final Permission MODIFY_THREAD_PERMISSION = new RuntimePermission("modifyThread");
    private static final Permission MODIFY_ARBITRARY_THREAD_PERMISSION = new ThreadPermission("modifyArbitraryThread");

    protected void checkThreadAccess(Thread t) {
        Objects.requireNonNull(t);
        checkPermission(MODIFY_THREAD_PERMISSION);
        final ThreadGroup source = Thread.currentThread().getThreadGroup();
        final ThreadGroup target = t.getThreadGroup();
        if (target != null && !source.parentOf(target)) {
            checkPermission(MODIFY_ARBITRARY_THREAD_PERMISSION);
        }
    }
    
    private static final Permission MODIFY_THREADGROUP_PERMISSION = new RuntimePermission("modifyThreadGroup");
    private static final Permission MODIFY_ARBITRARY_THREADGROUP_PERMISSION = new ThreadPermission("modifyArbitraryThreadGroup");
    
    protected void checkThreadGroupAccess(ThreadGroup g) {
        Objects.requireNonNull(g);
        checkPermission(MODIFY_THREADGROUP_PERMISSION);
        final ThreadGroup source = Thread.currentThread().getThreadGroup();
        if (source != null && !source.parentOf(g)) {
            checkPermission(MODIFY_ARBITRARY_THREADGROUP_PERMISSION);
        }
    }

    // exit permission logic
    @Override
    public void checkExit(int status) {
        innerCheckExit(status);
    }
    
    /**
     * The "Uwe Schindler" algorithm.
     *
     * @param status the exit status
     */
    protected void innerCheckExit(final int status) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            @Override
            public Void run() {
                final String systemClassName = System.class.getName(),
                        runtimeClassName = Runtime.class.getName();
                String exitMethodHit = null;
                for (final StackTraceElement se : Thread.currentThread().getStackTrace()) {
                    final String className = se.getClassName(), methodName = se.getMethodName();
                    if (
                        ("exit".equals(methodName) || "halt".equals(methodName)) &&
                        (systemClassName.equals(className) || runtimeClassName.equals(className))
                    ) {
                        exitMethodHit = className + '#' + methodName + '(' + status + ')';
                        continue;
                    }
                    
                    if (exitMethodHit != null) {
                        if (classesThatCanExit == null) {
                            break;
                        }
                        if (classCanExit(className, classesThatCanExit)) {
                            // this exit point is allowed, we return normally from closure:
                            return null;
                        }
                        // anything else in stack trace is not allowed, break and throw SecurityException below:
                        break;
                    }
                }
                
                if (exitMethodHit == null) {
                    // should never happen, only if JVM hides stack trace - replace by generic:
                    exitMethodHit = "JVM exit method";
                }
                throw new SecurityException(exitMethodHit + " calls are not allowed");
            }
        });
        
        // we passed the stack check, delegate to super, so default policy can still deny permission:
        super.checkExit(status);
    }

    static boolean classCanExit(final String className, final String[] classesThatCanExit) {
        for (final String classThatCanExit : classesThatCanExit) {
            if (className.matches(classThatCanExit)) {
                return true;
            }
        }
        return false;
    }

}
