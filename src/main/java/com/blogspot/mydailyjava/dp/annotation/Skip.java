package com.blogspot.mydailyjava.dp.annotation;

import java.lang.annotation.*;

/**
 * Defines a policy for non-matching lines.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Skip {

    public static enum Policy {

        /**
         * An exception is thrown for any non-matching line.
         */
        NONE,

        /**
         * An exception is thrown for any non-matching line unless the line does not contain characters.
         */
        EMPTY,

        /**
         * All non-matching lines are ignored.
         */
        NON_MATCHING
    }

    /**
     * The policy definition.
     * @return The valid policy.
     */
    Policy value();
}
