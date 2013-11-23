package com.blogspot.mydailyjava.dp.annotation;

import java.lang.annotation.*;

/**
 * Defines a regular expression pattern for matching a bean or a bean property. If this annotation annotates a
 * bean, property expressions in the form of {@code @name@} can be used to reference a property in addition to
 * regular expressions.
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchBy {

    /**
     * A regular expression for matching a bean to content.
     * @return The regular expression pattern.
     */
    String value();
}
