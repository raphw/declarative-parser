package com.blogspot.mydailyjava.dp.delegation;

import com.blogspot.mydailyjava.dp.handler.TransformationException;

import java.lang.reflect.*;
import java.util.Locale;

public class DefaultObjectDelegate extends ObjectDelegate<Object> {

    private static final String VALUE_OF_METHOD_NAME = "valueOf";

    private static interface Decoder {
        Object decode(String raw) throws IllegalAccessException;
    }

    private static class MethodDecoder implements Decoder {

        private final Method method;

        private MethodDecoder(Method method) throws NoSuchMethodException {
            int modifiers = method.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                throw new NoSuchMethodException();
            }
            this.method = method;
            method.setAccessible(true);
        }

        @Override
        public Object decode(String raw) throws IllegalAccessException {
            try {
                return method.invoke(raw);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class ConstructorDecoder implements Decoder {

        private final Constructor constructor;

        private ConstructorDecoder(Constructor constructor) throws NoSuchMethodException {
            int modifiers = constructor.getDeclaringClass().getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                throw new NoSuchMethodException();
            }
            this.constructor = constructor;
            constructor.setAccessible(true);
        }

        @Override
        public Object decode(String raw) throws IllegalAccessException {
            try {
                return constructor.newInstance(raw);
            } catch (InstantiationException e) {
                throw new TransformationException(String.format("Could not instantiate %s", constructor), e);
            } catch (InvocationTargetException e) {
                throw new TransformationException(String.format("Error on instantiating %s with %s", constructor, raw), e.getCause());
            }
        }
    }

    private static Decoder findDecoder(Class<?> type) {
        try {
            return new MethodDecoder(type.getDeclaredMethod(VALUE_OF_METHOD_NAME, String.class));
        } catch (NoSuchMethodException ignore) {
            try {
                return new ConstructorDecoder(type.getDeclaredConstructor(String.class));
            } catch (NoSuchMethodException e) {
                throw new TransformationException(String.format("%s is not a non-abstract type with a single String " +
                        "constructor or has a static valueOf(String) method", type));
            }
        }
    }

    private static String findPattern(Class<?> type) {
        if (type == Integer.class || type == Short.class || type == Byte.class || type == Long.class) {
            return IntDelegate.INTEGER_NUMBER_REGEX;
        } else if (type == Double.class || type == Float.class) {
            return DoubleDelegate.DECIMAL_NUMBER_REGEX;
        } else if (type == Character.class) {
            return CharDelegate.SINGLE_CHARACTER_REGEX;
        } else if (type == Boolean.class) {
            return BooleanDelegate.BOOLEAN_NAME_REGEX;
        } else {
            return NON_GREEDY_ALL_MATCH_PATTERN;
        }
    }

    private final Decoder decoder;
    private final String pattern;

    public DefaultObjectDelegate(Field field, Locale locale) {
        super(field, locale);
        decoder = findDecoder(field.getType());
        pattern = findPattern(field.getType());
    }

    @Override
    protected Object decode(String raw) throws IllegalAccessException {
        return raw == null ? null : decoder.decode(raw);
    }

    @Override
    protected String encode(Object bean) {
        return bean != null ? bean.toString() : "";
    }

    @Override
    public String getPattern() {
        return pattern;
    }
}
