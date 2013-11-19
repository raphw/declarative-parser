package com.blogspot.mydailyjava.dp.delegation;

import java.lang.reflect.Field;
import java.util.Locale;

public class StringDelegate extends PropertyDelegate {

    protected StringDelegate(Field field, Locale locale) {
        super(field, locale);
    }

    @Override
    public void setValue(Object bean, String raw) throws IllegalAccessException {
        getField().set(bean, raw);
    }

    @Override
    public String getValue(Object bean) throws IllegalAccessException {
        Object value = getField().get(bean);
        return value == null ? "" : value.toString();
    }

    @Override
    public String getPattern() {
        return ObjectDelegate.NON_GREEDY_ALL_MATCH_PATTERN;
    }
}
