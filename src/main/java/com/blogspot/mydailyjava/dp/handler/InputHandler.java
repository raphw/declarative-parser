package com.blogspot.mydailyjava.dp.handler;

import com.blogspot.mydailyjava.dp.annotation.MatchBy;
import com.blogspot.mydailyjava.dp.annotation.Skip;
import com.blogspot.mydailyjava.dp.annotation.WritePattern;
import com.blogspot.mydailyjava.dp.delegation.IDelegationFactory;
import com.blogspot.mydailyjava.dp.delegation.PropertyDelegate;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class InputHandler {

    private final Pattern actualReadPattern;
    private final String actualWritePattern;
    private final List<PropertyDelegate> matchedProperties;
    private final Skip.Policy skipPolicy;

    public InputHandler(Class<?> type, IDelegationFactory delegationFactory,
                        String patternOverride, String writePatternOverride,
                        Skip.Policy skipPolicy, Locale locale) {
        String actualReadPattern = PropertyResolver.uncaptureGroups(findActualReadPattern(type, patternOverride));
        ReadPatternResolver.ResolvedPattern resolvedPattern = new ReadPatternResolver(type, delegationFactory, locale)
                .resolve(actualReadPattern);
        this.actualReadPattern = Pattern.compile(resolvedPattern.getReadPattern());
        this.matchedProperties = resolvedPattern.getPropertyDelegates();
        this.actualWritePattern = findActualWritePattern(type, actualReadPattern, writePatternOverride);
        this.skipPolicy = findActualSkipPolicy(type, skipPolicy);
    }

    private static String findActualReadPattern(Class<?> type, String override) {
        if (override != null) {
            return override;
        }
        MatchBy customPattern = type.getAnnotation(MatchBy.class);
        if (customPattern == null || customPattern.value() == null) {
            throw new TransformationException(String.format("%s does not define a default pattern", type));
        }
        return customPattern.value();
    }

    private static String findActualWritePattern(Class<?> type, String actualReadPattern, String override) {
        if (override != null) {
            return override;
        }
        WritePattern writePattern = type.getAnnotation(WritePattern.class);
        if (writePattern != null && writePattern.value() != null) {
            return writePattern.value();
        }
        return actualReadPattern;
    }

    private static Skip.Policy findActualSkipPolicy(Class<?> type, Skip.Policy override) {
        if (override != null) {
            return override;
        }
        Skip skip = type.getAnnotation(Skip.class);
        if (skip != null && skip.value() != null) {
            return skip.value();
        } else {
            return Skip.Policy.EMPTY;
        }
    }

    public <T> T read(String input, T bean) {
        try {
            return tryRead(input, bean);
        } catch (IllegalAccessException e) {
            throw new TransformationException(String.format("Could not access property for %s", bean.getClass()), e);
        }
    }

    private <T> T tryRead(String input, T bean) throws IllegalAccessException {
        Matcher matcher = actualReadPattern.matcher(input);
        if (!matcher.find()) {
            if (skipPolicy == Skip.Policy.EMPTY && input.trim().length() == 0 || skipPolicy == Skip.Policy.NON_MATCHING) {
                return null;
            } else {
                throw new TransformationException(String.format("Could not match pattern %s to value %s",
                        actualReadPattern.pattern(), input));
            }
        }
        for (int i = 0; i < matcher.groupCount(); i++) {
            String matchedExpression = matcher.group(i + 1);
            if (matchedExpression == null) {
                continue;
            }
            try {
                matchedProperties.get(i).setValue(bean, matchedExpression);
            } catch (RuntimeException e) {
                throw new TransformationException(String.format("Illegal value found for property %d: %s", i, matchedExpression), e);
            }
        }
        return bean;
    }

    public String write(Object bean) {
        try {
            return tryWrite(bean);
        } catch (IllegalAccessException e) {
            throw new TransformationException(String.format("Could not access property for %s", bean.getClass()), e);
        }
    }

    private String tryWrite(Object bean) throws IllegalAccessException {
        StringBuilder expressionBuilder = new StringBuilder();
        Matcher patternMatcher = ReadPatternResolver.PROPERTY_EXPRESSION_PATTERN.matcher(actualWritePattern);
        int lastPropertyVariableIndex = 0;
        for (int i = 0; patternMatcher.find(); i++) {
            expressionBuilder.append(actualWritePattern.substring(lastPropertyVariableIndex, patternMatcher.start()));
            try {
                expressionBuilder.append(matchedProperties.get(i).getValue(bean));
            } catch (RuntimeException e) {
                throw new TransformationException(String.format("Could not retrieve value for property %d", i), e);
            }
            lastPropertyVariableIndex = patternMatcher.end();
        }
        expressionBuilder.append(actualWritePattern.substring(lastPropertyVariableIndex, actualWritePattern.length()));
        return expressionBuilder.toString();
    }
}
