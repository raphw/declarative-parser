package com.blogspot.mydailyjava.dp.delegation;

import java.lang.reflect.Field;
import java.util.Locale;

public class ShortDelegate extends PropertyDelegate {

    public ShortDelegate(Field field, Locale locale) {
        super(field, locale);
    }

    @Override
    public void setValue(Object bean, String raw) throws IllegalAccessException {
        getField().setShort(bean, Short.parseShort(raw));
    }

    @Override
    public String getValue(Object bean) throws IllegalAccessException {
        return String.valueOf(getField().getShort(bean));
    }

    @Override
    public String getPattern() {
        return IntDelegate.INTEGER_NUMBER_REGEX;
    }
}
