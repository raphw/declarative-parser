package com.blogspot.mydailyjava.dp.annotation;

import com.blogspot.mydailyjava.dp.delegation.PropertyDelegate;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResolveMatchWith {
    Class<? extends PropertyDelegate> value();
}
