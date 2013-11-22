package com.blogspot.mydailyjava.dp.handler;

import com.blogspot.mydailyjava.dp.delegation.IDelegationFactory;
import com.blogspot.mydailyjava.dp.delegation.PropertyDelegate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class ReadPatternResolver {

    private class ReadPatternPropertyMatcher implements PropertyExpressionParser.IPropertyMatch {

        private final List<PropertyDelegate> propertyDelegates;

        private ReadPatternPropertyMatcher(List<PropertyDelegate> propertyDelegates) {
            this.propertyDelegates = propertyDelegates;
        }

        @Override
        public void match(int matchIndex, String propertyName, StringBuilder patternBuilder) {
            try {
                Field field = clazz.getDeclaredField(propertyName);
                propertyDelegates.add(propertyResolver.resolve(field, patternBuilder));
            } catch (Exception e) {
                throw new TransformationException(String.format("Could not retrieve value for property %d", matchIndex), e);
            }
        }
    }

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
        List<PropertyDelegate> propertyDelegates = new ArrayList<PropertyDelegate>();
        String readPattern = new PropertyExpressionParser(new ReadPatternPropertyMatcher(propertyDelegates), true)
                .process(rawPattern);
        return new ResolvedPattern(readPattern, propertyDelegates);
    }
}
