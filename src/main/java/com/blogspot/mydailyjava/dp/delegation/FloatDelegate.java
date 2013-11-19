package com.blogspot.mydailyjava.dp.delegation;

import java.lang.reflect.Field;
import java.util.Locale;

public class FloatDelegate extends PropertyDelegate {

    public FloatDelegate(Field field, Locale locale) {
        super(field, locale);
    }

    @Override
    public void setValue(Object bean, String raw) throws IllegalAccessException {
        getField().setFloat(bean, Float.parseFloat(raw));
    }

    @Override
    public String getValue(Object bean) throws IllegalAccessException {
        return String.valueOf(getField().getFloat(bean));
    }

    @Override
    public String getPattern() {
        return DoubleDelegate.DECIMAL_NUMBER_REGEX;
    }
}
