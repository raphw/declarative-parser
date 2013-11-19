package com.blogspot.mydailyjava.dp.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WritePattern {
    String value();
}
