package com.blogspot.mydailyjava.dp.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Skip {

    public static enum Policy {
        NONE,
        EMPTY,
        NON_MATCHING
    }

    Policy value();
}
