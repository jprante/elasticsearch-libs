package org.xbib.forbiddenapis;

import java.util.regex.Pattern;

public final class ClassPatternRule {

    private final Pattern pattern;

    private final String glob, message;

    /**
     * Create new rule for class glob and given printout.
     * @param glob glob
     * @param message message
     */
    public ClassPatternRule(String glob, String message) {
        if (glob == null) {
            throw new NullPointerException("glob");
        }
        this.glob = glob;
        this.pattern = Utils.glob2Pattern(glob);
        this.message = message;
    }

    /**
     * Returns true, if the given class (binary name, dotted) matches this rule.
     * @param className class name
     * @return true if class matches this rule
     */
    public boolean matches(String className) {
        return pattern.matcher(className).matches();
    }

    /**
     * Returns the printout using the message and the given class name.
     * @param className class name
     * @return print out
     */
    public String getPrintout(String className) {
        return message == null ? className : (className + " [" + message + "]");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + glob.hashCode();
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ClassPatternRule other = (ClassPatternRule) obj;
        if (!glob.equals(other.glob)) {
            return false;
        }
        if (message == null) {
            return other.message == null;
        } else {
            return message.equals(other.message);
        }
    }

    @Override
    public String toString() {
        return "ClassPatternRule [glob=" + glob + ", message=" + message + "]";
    }

}
