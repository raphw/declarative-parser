package com.blogspot.mydailyjava.dp.delegation;

import java.lang.reflect.Field;
import java.util.Locale;

public class BooleanDelegate extends PropertyDelegate {

    public static final String BOOLEAN_NAME_REGEX = "(true)|(false)";

    public BooleanDelegate(Field field, Locale locale) {
        super(field, locale);
    }

    @Override
    public void setValue(Object bean, String raw) throws IllegalAccessException {
        getField().setBoolean(bean, Boolean.valueOf(raw));
    }

    @Override
    public String getValue(Object bean) throws IllegalAccessException {
        return String.valueOf(getField().getBoolean(bean));
    }

    @Override
    public String getPattern() {
        return BOOLEAN_NAME_REGEX;
    }
}
