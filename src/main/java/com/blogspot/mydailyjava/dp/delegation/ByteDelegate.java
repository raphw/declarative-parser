package com.blogspot.mydailyjava.dp.delegation;

import java.lang.reflect.Field;
import java.util.Locale;

public class ByteDelegate extends PropertyDelegate {

    public ByteDelegate(Field field, Locale locale) {
        super(field, locale);
    }

    @Override
    public void setValue(Object bean, String raw) throws IllegalAccessException {
        getField().setByte(bean, Byte.parseByte(raw));
    }

    @Override
    public String getValue(Object bean) throws IllegalAccessException {
        return String.valueOf(getField().getByte(bean));
    }

    @Override
    public String getPattern() {
        return IntDelegate.INTEGER_NUMBER_REGEX;
    }
}
