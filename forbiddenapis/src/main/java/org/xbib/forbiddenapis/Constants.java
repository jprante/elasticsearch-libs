package org.xbib.forbiddenapis;

import java.util.Locale;
import java.util.regex.Pattern;

import org.objectweb.asm.Type;

public interface Constants {

    String BS_JDK_NONPORTABLE = "jdk-non-portable";

    Pattern JDK_SIG_PATTERN = Pattern.compile("(jdk\\-.*?\\-)(\\d+)(\\.\\d+)?(\\.\\d+)*");

    String DEPRECATED_WARN_INTERNALRUNTIME = String.format(Locale.ENGLISH,
            "The setting 'internalRuntimeForbidden' was deprecated and will be removed in next version. For backwards compatibility task/mojo is using '%s' bundled signatures instead.", BS_JDK_NONPORTABLE);

    Type DEPRECATED_TYPE = Type.getType(Deprecated.class);

    String DEPRECATED_DESCRIPTOR = DEPRECATED_TYPE.getDescriptor();

    String LAMBDA_META_FACTORY_INTERNALNAME = "java/lang/invoke/LambdaMetafactory";

    String LAMBDA_METHOD_NAME_PREFIX = "lambda$";

    String SIGNATURE_POLYMORPHIC_PKG_INTERNALNAME = "java/lang/invoke/";

    String SIGNATURE_POLYMORPHIC_DESCRIPTOR = Type.getMethodDescriptor(Type.getType(Object.class), Type.getType(Object[].class));

    String CLASS_CONSTRUCTOR_METHOD_NAME = "<clinit>";

    String CONSTRUCTOR_METHOD_NAME = "<init>";
}
