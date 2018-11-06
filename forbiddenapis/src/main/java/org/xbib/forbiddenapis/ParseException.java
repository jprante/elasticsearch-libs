package org.xbib.forbiddenapis;

@SuppressWarnings("serial")
public final class ParseException extends Exception {

    public ParseException(String msg) {
        super(msg);
    }

    public ParseException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
