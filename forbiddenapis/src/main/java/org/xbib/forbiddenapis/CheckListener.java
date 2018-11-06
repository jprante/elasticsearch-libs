package org.xbib.forbiddenapis;

public interface CheckListener {

    void missing(String message);

    void violation(String message);
}
