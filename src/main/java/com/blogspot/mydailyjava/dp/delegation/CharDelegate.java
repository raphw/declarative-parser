package com.blogspot.mydailyjava.dp.delegation;

import com.blogspot.mydailyjava.dp.handler.TransformationException;

import java.lang.reflect.Field;
import java.util.Locale;

public class CharDelegate extends PropertyDelegate {

    public static final String SINGLE_CHARACTER_REGEX = ".{1}";

    public CharDelegate(Field field, Locale locale) {
        super(field, locale);
    }

    @Override
    public void setValue(Object bean, String raw) throws IllegalAccessException {
        if (raw.length() != 1) {
            throw new TransformationException(String.format("Could not set %s as a single char property for %s", raw, getField()));
        }
        getField().setChar(bean, raw.charAt(0));
    }

    @Override
    public String getValue(Object bean) throws IllegalAccessException {
        return String.valueOf(getField().getChar(bean));
    }

    @Override
    public String getPattern() {
        return SINGLE_CHARACTER_REGEX;
    }
}
