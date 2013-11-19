package com.blogspot.mydailyjava.dp.handler;

import com.blogspot.mydailyjava.dp.delegation.IDelegationFactory;
import com.blogspot.mydailyjava.dp.delegation.PropertyDelegate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ReadPatternResolver {

    private static final String PROPERTY_EXPRESSION = "(?<!\\\\)(\\\\\\\\)*@[a-zA-Z][a-zA-Z0-9]*@";
    protected static final Pattern PROPERTY_EXPRESSION_PATTERN = Pattern.compile(PROPERTY_EXPRESSION);

    static class ResolvedPattern {

        private final String readPattern;
        private final List<PropertyDelegate> properties;

        public ResolvedPattern(String readPattern, List<PropertyDelegate> properties) {
            this.readPattern = readPattern;
            this.properties = properties;
        }

        public String getReadPattern() {
            return readPattern;
        }

        public List<PropertyDelegate> getPropertyDelegates() {
            return properties;
        }
    }

    private final Class<?> clazz;
    private final PropertyResolver propertyResolver;

    public ReadPatternResolver(Class<?> clazz, IDelegationFactory delegationFactory, Locale locale) {
        this.clazz = clazz;
        this.propertyResolver = new PropertyResolver(delegationFactory, locale);
    }

    public ResolvedPattern resolve(String rawPattern) {
        try {
            return tryResolve(rawPattern);
        } catch (NoSuchFieldException e) {
            throw new TransformationException("A property referenced in the pattern is not referenced by a field", e);
        }
    }

    private ResolvedPattern tryResolve(final String rawPattern) throws NoSuchFieldException {
        StringBuilder readPattern = new StringBuilder();
        Matcher rawPatternMatcher = PROPERTY_EXPRESSION_PATTERN.matcher(rawPattern);
        List<PropertyDelegate> propertiesInPattern = new ArrayList<PropertyDelegate>();
        int lastPropertyVariableIndex = 0;
        while (rawPatternMatcher.find()) {
            String propertyVariableName = rawPatternMatcher.group();
            readPattern.append(rawPattern, lastPropertyVariableIndex, rawPatternMatcher.start());
            int propertyNameStartIndex = propertyVariableName.indexOf('@') + 1;
            readPattern.append(propertyVariableName, 1, propertyNameStartIndex);
            String propertyName = propertyVariableName.substring(propertyNameStartIndex, propertyVariableName.length() - 1);
            Field field = clazz.getDeclaredField(propertyName);
            propertiesInPattern.add(propertyResolver.resolve(field, readPattern));
            lastPropertyVariableIndex = rawPatternMatcher.end();
        }
        readPattern.append(rawPattern, lastPropertyVariableIndex, rawPattern.length());
        return new ResolvedPattern(readPattern.toString(), propertiesInPattern);
    }
}
