package com.blogspot.mydailyjava.dp.handler;

import com.blogspot.mydailyjava.dp.annotation.Skip;
import com.blogspot.mydailyjava.dp.delegation.IDelegationFactory;
import com.blogspot.mydailyjava.dp.delegation.SimpleDelegationFactory;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class BeanTransformer<T> {

    public static class Builder {

        private IDelegationFactory delegationFactory = new SimpleDelegationFactory();
        private String readPattern = null, writePattern = null;
        private Locale locale = Locale.getDefault();
        private Skip.Policy skipPolicy = null;

        public Builder delegationFactory(IDelegationFactory delegationFactory) {
            checkNotNull(delegationFactory);
            this.delegationFactory = delegationFactory;
            return this;
        }

        public Builder readPattern(String readPattern) {
            checkNotNull(readPattern);
            this.readPattern = readPattern;
            return this;
        }

        public Builder writePattern(String writePattern) {
            checkNotNull(writePattern);
            this.writePattern = writePattern;
            return this;
        }

        public Builder locale(Locale locale) {
            checkNotNull(locale);
            this.locale = locale;
            return this;
        }

        public Builder skipPolicy(Skip.Policy skipPolicy) {
            checkNotNull(skipPolicy);
            this.skipPolicy = skipPolicy;
            return this;
        }

        public <U> BeanTransformer<U> build(Class<? extends U> type) {
            checkNotNull(type);
            checkNonAbstract(type);
            return new BeanTransformer<U>(type, delegationFactory, readPattern, writePattern, skipPolicy, locale);
        }

        private static void checkNotNull(Object value) {
            if (value == null) {
                throw new NullPointerException();
            }
        }

        private static <U> Class<? extends U> checkNonAbstract(Class<? extends U> type) {
            int modifiers = type.getModifiers();
            if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
                throw new IllegalArgumentException(String.format("%s is abstract", type));
            }
            return type;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static <U> BeanTransformer<U> make(Class<? extends U> type) {
        return builder().build(type);
    }

    private final InputHandler inputHandler;
    private final Constructor<? extends T> beanConstructor;

    private BeanTransformer(Class<? extends T> type, IDelegationFactory delegationFactory,
                            String readPattern, String writePattern,
                            Skip.Policy skipPolicy, Locale locale) {
        this.inputHandler = new InputHandler(type, delegationFactory, readPattern, writePattern, skipPolicy, locale);
        beanConstructor = findConstructor(type);
    }

    private static <U> Constructor<U> findConstructor(Class<U> type) {
        try {
            Constructor<U> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("No default constructor found for %s", type), e);
        }
    }

    public T readSingle(String value) {
        List<T> result = read(value);
        if (result.size() == 1) {
            return result.get(0);
        } else if (result.isEmpty()) {
            return null;
        } else {
            throw new TransformationException(String.format("Input yielded %d results instead of one", result.size()));
        }
    }

    public List<T> read(String value) {
        try {
            return read(new StringReader(value));
        } catch (IOException e) {
            throw new TransformationException("Error on string to reader composition", e);
        }
    }

    public List<T> read(Reader reader) throws IOException {
        List<T> result = new LinkedList<T>();
        LineNumberReader bufferedReader = new LineNumberReader(reader);
        try {
            for (String chunk = bufferedReader.readLine(); chunk != null; chunk = bufferedReader.readLine()) {
                addIfNotNull(result, inputHandler.read(chunk, makeBean()));
            }
        } catch (TransformationException e) {
            throw new TransformationException(String.format("Error on reading line %d: %s",
                    bufferedReader.getLineNumber(), e.getMessage()), e.getCause());
        } finally {
            bufferedReader.close();
        }
        return result;
    }

    private static <U> void addIfNotNull(List<U> list, U bean) {
        if (bean != null) {
            list.add(bean);
        }
    }

    private T makeBean() {
        try {
            return beanConstructor.newInstance();
        } catch (InstantiationException e) {
            throw new TransformationException(String.format("Could not instantiate %s", beanConstructor), e);
        } catch (IllegalAccessException e) {
            throw new TransformationException(String.format("Could not access %s", beanConstructor), e);
        } catch (InvocationTargetException e) {
            throw new TransformationException(String.format("Error on instantiating %s", beanConstructor), e.getCause());
        }
    }

    public String writeSingle(T bean) {
        return write(Collections.singletonList(bean)).replaceFirst("(\r)?\n", "");
    }

    public String write(List<? extends T> beans) {
        StringWriter stringWriter = new StringWriter();
        try {
            write(stringWriter, beans);
        } catch (IOException e) {
            throw new TransformationException("Error on string to writer composition", e);
        }
        return stringWriter.toString();
    }

    public void write(Writer writer, List<? extends T> beans) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        try {
            for (T bean : beans) {
                bufferedWriter.write(inputHandler.write(bean));
                bufferedWriter.newLine();
            }
        } finally {
            bufferedWriter.close();
        }
    }
}
