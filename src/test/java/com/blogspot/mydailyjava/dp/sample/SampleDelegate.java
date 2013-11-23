package com.blogspot.mydailyjava.dp.sample;

import com.blogspot.mydailyjava.dp.delegation.PropertyDelegate;

import java.lang.reflect.Field;
import java.util.Locale;

public class SampleDelegate extends PropertyDelegate {

    public static final String FIXED_VALUE = "USER_RESOLVER";

    public SampleDelegate(Field field, Locale locale) {
        super(field, locale);
    }

    @Override
    public void setValue(Object bean, String raw) throws IllegalAccessException {
        getField().set(bean, FIXED_VALUE);
    }

    @Override
    public String getValue(Object bean) throws IllegalAccessException {
        return FIXED_VALUE;
    }

    @Override
    public String getPattern() {
        return "USER_RESOLVER";
    }
}
