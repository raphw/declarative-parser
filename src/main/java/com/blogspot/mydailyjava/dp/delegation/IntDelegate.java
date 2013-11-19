package com.blogspot.mydailyjava.dp.delegation;

import java.lang.reflect.Field;
import java.util.Locale;

public class IntDelegate extends PropertyDelegate {

    public static final String INTEGER_NUMBER_REGEX = "(-|\\+)?[0-9]+";

    public IntDelegate(Field field, Locale locale) {
        super(field, locale);
    }

    @Override
    public void setValue(Object bean, String raw) throws IllegalAccessException {
        getField().setInt(bean, Integer.parseInt(raw));
    }

    @Override
    public String getValue(Object bean) throws IllegalAccessException {
        return String.valueOf(getField().getInt(bean));
    }

    @Override
    public String getPattern() {
        return INTEGER_NUMBER_REGEX;
    }
}
