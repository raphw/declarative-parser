package com.blogspot.mydailyjava.dp.annotation;

import com.blogspot.mydailyjava.dp.delegation.PropertyDelegate;

import java.lang.annotation.*;

/**
 * Defines a custom {@link PropertyDelegate} for a property.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResolveMatchWith {

    /**
     * A custom property delegate.
     *
     * @return A property delegate for handling this field.
     */
    Class<? extends PropertyDelegate> value();
}
