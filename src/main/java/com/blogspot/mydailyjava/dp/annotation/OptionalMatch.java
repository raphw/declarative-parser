package com.blogspot.mydailyjava.dp.annotation;

import java.lang.annotation.*;

/**
 * This annotation can be used to make a match optional.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalMatch {
}
