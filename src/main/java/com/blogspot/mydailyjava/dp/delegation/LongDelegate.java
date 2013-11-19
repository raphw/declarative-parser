package com.blogspot.mydailyjava.dp.delegation;

import java.lang.reflect.Field;
import java.util.Locale;

public class LongDelegate extends PropertyDelegate {

    public LongDelegate(Field field, Locale locale) {
        super(field, locale);
    }

    @Override
    public void setValue(Object bean, String raw) throws IllegalAccessException {
        getField().setLong(bean, Long.parseLong(raw));
    }

    @Override
    public String getValue(Object bean) throws IllegalAccessException {
        return String.valueOf(getField().getLong(bean));
    }

    @Override
    public String getPattern() {
        return IntDelegate.INTEGER_NUMBER_REGEX;
    }
}
