package org.xbib.forbiddenapis;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Default annotation to suppress forbidden-apis errors inside a whole class, a method, or a field.
 * You can define your own annotation and pass a list of suppressing annotation types to the checker,
 * this allows to use the feature without compile-time dependencies to forbidden-apis.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
public @interface SuppressForbidden {}
