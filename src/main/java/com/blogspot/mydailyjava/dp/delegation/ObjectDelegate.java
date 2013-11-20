package com.blogspot.mydailyjava.dp.delegation;

import java.lang.reflect.Field;
import java.util.Locale;

public abstract class ObjectDelegate<T> extends PropertyDelegate {

    public static final String NON_GREEDY_ALL_MATCH_PATTERN = ".+?";
    public static final String GREEDY_ALL_MATCH_PATTERN = ".+";

    public ObjectDelegate(Field field, Locale locale) {
        super(field, locale);
    }

    @Override
    public void setValue(Object bean, String raw) throws IllegalAccessException {
        getField().set(bean, decode(raw));
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getValue(Object bean) throws IllegalAccessException {
        return encode((T) getField().get(bean));
    }

    /**
     * Will never receive null as its argument. Can return null.
     */
    protected abstract T decode(String raw) throws IllegalAccessException;

    /**
     * Must not return null. Rather return empty string. Might receive null as its argument.
     */
    protected abstract String encode(T bean) throws IllegalAccessException;
}
