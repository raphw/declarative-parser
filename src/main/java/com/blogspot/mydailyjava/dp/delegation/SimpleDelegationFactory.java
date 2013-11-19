package com.blogspot.mydailyjava.dp.delegation;

import java.lang.reflect.Field;
import java.util.Locale;

public class SimpleDelegationFactory implements IDelegationFactory {

    @Override
    public PropertyDelegate makeDelegate(Field field, Locale locale) {
        Class<?> type = field.getType();
        if (type.isPrimitive()) {
            if (type == int.class) {
                return new IntDelegate(field, locale);
            } else if (type == double.class) {
                return new DoubleDelegate(field, locale);
            } else if (type == long.class) {
                return new LongDelegate(field, locale);
            } else if (type == boolean.class) {
                return new BooleanDelegate(field, locale);
            } else if (type == float.class) {
                return new FloatDelegate(field, locale);
            } else if (type == byte.class) {
                return new ByteDelegate(field, locale);
            } else if (type == char.class) {
                return new CharDelegate(field, locale);
            } else if (type == short.class) {
                return new ShortDelegate(field, locale);
            } else {
                throw new AssertionError();
            }
        } else if (type == String.class) {
            return new StringDelegate(field, locale);
        } else {
            return new DefaultObjectDelegate(field, locale);
        }
    }
}
