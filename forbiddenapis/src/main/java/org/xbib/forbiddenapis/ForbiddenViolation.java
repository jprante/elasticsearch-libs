package org.xbib.forbiddenapis;

import java.util.Formatter;
import java.util.Locale;

import org.objectweb.asm.commons.Method;

public final class ForbiddenViolation implements Comparable<ForbiddenViolation> {

    /** Separator used to allow multiple description lines per violation. */
    static final String SEPARATOR = "\n";

    private int groupId;

    final Method targetMethod;

    public final String description;

    private final String locationInfo;

    private final int lineNo;

    ForbiddenViolation(int groupId, String description, String locationInfo, int lineNo) {
        this(groupId, null, description, locationInfo, lineNo);
    }

    ForbiddenViolation(int groupId, Method targetMethod, String description, String locationInfo, int lineNo) {
        this.groupId = groupId;
        this.targetMethod = targetMethod;
        this.description = description;
        this.locationInfo = locationInfo;
        this.lineNo = lineNo;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getGroupId() {
        return groupId;
    }

    @SuppressWarnings("resource")
    public String format(String className, String source) {
        final StringBuilder sb = new StringBuilder(description);
        sb.append(SEPARATOR).append("  in ").append(className);
        if (source != null) {
            if (lineNo >= 0) {
                new Formatter(sb, Locale.ROOT).format(" (%s:%d)", source, lineNo).flush();
            } else {
                new Formatter(sb, Locale.ROOT).format(" (%s, %s)", source, locationInfo).flush();
            }
        } else {
            new Formatter(sb, Locale.ROOT).format(" (%s)", locationInfo).flush();
        }
        return sb.toString();
    }

    @Override
    public int compareTo(ForbiddenViolation other) {
        if (this.groupId == other.groupId) {
            return Long.signum((long) this.lineNo - (long) other.lineNo);
        } else {
            return Long.signum((long) this.groupId - (long) other.groupId);
        }
    }

}
