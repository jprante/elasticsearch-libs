package org.xbib.forbiddenapis;

import java.io.IOException;

public interface RelatedClassLookup {

    ClassSignature lookupRelatedClass(String internalName, String internalNameOrig);

    ClassSignature getClassFromClassLoader(String clazz) throws ClassNotFoundException,IOException;
}
