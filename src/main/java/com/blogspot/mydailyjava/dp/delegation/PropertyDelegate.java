package com.blogspot.mydailyjava.dp.delegation;

import java.lang.reflect.Field;
import java.util.Locale;

public abstract class PropertyDelegate {

    private final Field field;
    private final Locale locale;

    protected PropertyDelegate(Field field, Locale locale) {
        this.field = field;
        field.setAccessible(true);
        this.locale = locale;
    }

    protected Field getField() {
        return field;
    }

    protected Locale getLocale() {
        return locale;
    }

    public abstract void setValue(Object bean, String raw) throws IllegalAccessException;

    public abstract String getValue(Object bean) throws IllegalAccessException;

    public abstract String getPattern();
}
