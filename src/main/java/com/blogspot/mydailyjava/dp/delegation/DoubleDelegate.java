package com.blogspot.mydailyjava.dp.delegation;

import java.lang.reflect.Field;
import java.util.Locale;

public class DoubleDelegate extends PropertyDelegate {

    public static final String DECIMAL_NUMBER_REGEX = IntDelegate.INTEGER_NUMBER_REGEX + "([\\.|,][0-9]*)?";

    public DoubleDelegate(Field field, Locale locale) {
        super(field, locale);
    }

    @Override
    public void setValue(Object bean, String raw) throws IllegalAccessException {
        getField().setDouble(bean, Double.parseDouble(raw));
    }

    @Override
    public String getValue(Object bean) throws IllegalAccessException {
        return String.valueOf(getField().getDouble(bean));
    }

    @Override
    public String getPattern() {
        return DECIMAL_NUMBER_REGEX;
    }
}
