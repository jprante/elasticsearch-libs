package org.xbib.forbiddenapis;

import java.io.IOException;

@SuppressWarnings("serial")
final class RelatedClassLoadingException extends RuntimeException {

    private final String className;

    public RelatedClassLoadingException(ClassNotFoundException e, String className) {
        super(e);
        this.className = className;
    }

    public RelatedClassLoadingException(IOException e, String className) {
        super(e);
        this.className = className;
    }

    public Exception getException() {
        return (Exception) getCause();
    }

    public String getClassName() {
        return className;
    }

}
