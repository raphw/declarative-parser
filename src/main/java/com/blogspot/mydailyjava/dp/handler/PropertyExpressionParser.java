package com.blogspot.mydailyjava.dp.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PropertyExpressionParser {

    // When writing a pattern, it is easier to implement a sort of look behind escaping handle directly by simply
    // not adding the escaping slashes to the output buffer once they are discovered before a property expression.
    private static final String PROPERTY_EXPRESSION = "@[a-zA-Z][a-zA-Z0-9]*@";
    private static final Pattern PROPERTY_EXPRESSION_PATTERN = Pattern.compile(PROPERTY_EXPRESSION);

    private static final char ESCAPE_SYMBOL = '\\';

    public static interface IPropertyMatch {
        void match(int matchIndex, String propertyName, StringBuilder patternBuilder);
    }

    private final IPropertyMatch propertyMatch;
    private final int reinstatingFactor;

    public PropertyExpressionParser(IPropertyMatch propertyMatch, boolean reinstateEscaping) {
        this.propertyMatch = propertyMatch;
        reinstatingFactor = reinstateEscaping ? 2 : 1;
    }

    public String process(String rawPattern) {
        StringBuilder expressionBuilder = new StringBuilder();
        Matcher matcher = PROPERTY_EXPRESSION_PATTERN.matcher(rawPattern);
        int lastPropertyVariableIndex = 0;
        for (int matchIndex = 0; matcher.find(); ) {
            int slashes = numberOfPrecededSlashes(rawPattern.substring(lastPropertyVariableIndex, matcher.start()));
            expressionBuilder.append(rawPattern, lastPropertyVariableIndex, matcher.start() - slashes);
            for (int escapedAdded = 0; escapedAdded < (slashes / 2) * reinstatingFactor; escapedAdded++) {
                expressionBuilder.append(ESCAPE_SYMBOL);
            }
            if (slashes % 2 == 1) { // uneven number of slashes: expression is escaped
                expressionBuilder.append(rawPattern, matcher.start(), matcher.end());
            } else { // even number of slashes: expression is not escaped
                String propertyName = rawPattern.substring(matcher.start() + 1, matcher.end() - 1);
                propertyMatch.match(matchIndex++, propertyName, expressionBuilder);
            }
            lastPropertyVariableIndex = matcher.end();
        }
        expressionBuilder.append(rawPattern, lastPropertyVariableIndex, rawPattern.length());
        return expressionBuilder.toString();
    }

    private static int numberOfPrecededSlashes(String patternPrefix) {
        int index = patternPrefix.length(), slashes = 0;
        while (--index != -1 && patternPrefix.charAt(index) == ESCAPE_SYMBOL) {
            slashes++;
        }
        return slashes;
    }
}
