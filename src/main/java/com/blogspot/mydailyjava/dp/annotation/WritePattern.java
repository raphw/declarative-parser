package com.blogspot.mydailyjava.dp.annotation;

import java.lang.annotation.*;

/**
 * Defines a pattern for writing beans to a {@link java.io.Writer}. Other than the {@link MatchBy} annotation,
 * this pattern does not use regular expression what allows writing <i>clean</i> output when the matcher pattern
 * uses regular expressions which normally cannot be written. If this annotation is not set on a bean, the {@link MatchBy}
 * pattern will be used for writing where regular expressions are printet <i>as they are</i>.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WritePattern {
    /**
     * Defines a pattern for writing a bean to a content consumer.
     *
     * @return The write pattern.
     */
    String value();
}
