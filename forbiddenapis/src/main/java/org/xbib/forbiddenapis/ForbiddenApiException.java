package org.xbib.forbiddenapis;

@SuppressWarnings("serial")
public final class ForbiddenApiException extends Exception {

    public ForbiddenApiException(String msg) {
        super(msg);
    }

    public ForbiddenApiException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
