package com.blogspot.mydailyjava.dp.handler;

import com.blogspot.mydailyjava.dp.annotation.MatchBy;
import com.blogspot.mydailyjava.dp.annotation.OptionalMatch;
import com.blogspot.mydailyjava.dp.annotation.ResolveMatchWith;
import com.blogspot.mydailyjava.dp.delegation.IDelegationFactory;
import com.blogspot.mydailyjava.dp.delegation.PropertyDelegate;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

class PropertyResolver {

    private static final String UNESCAPED_GROUP_START_REGEX = "(?<!\\\\)(\\\\\\\\)*\\((?!\\?:)";
    private static final String ESCAPED_GROUP_START = "$1(?:";

    private final IDelegationFactory delegationFactory;
    private final Locale locale;

    public PropertyResolver(IDelegationFactory delegationFactory, Locale locale) {
        this.delegationFactory = delegationFactory;
        this.locale = locale;
    }

    public PropertyDelegate resolve(Field field, StringBuilder stringBuilder) {
        PropertyDelegate propertyDelegate = makePropertyDelegate(field);
        stringBuilder.append("(");
        stringBuilder.append(uncaptureGroups(makePattern(field, propertyDelegate)));
        stringBuilder.append(")");
        if (isOptional(field)) {
            stringBuilder.append("?");
        }
        return propertyDelegate;
    }

    protected static String uncaptureGroups(String expression) {
        return expression.replaceAll(UNESCAPED_GROUP_START_REGEX, ESCAPED_GROUP_START);
    }

    private static boolean isOptional(Field field) {
        return field.getAnnotation(OptionalMatch.class) != null;
    }

    private static String makePattern(Field field, PropertyDelegate propertyDelegate) {
        MatchBy matchExpression = field.getAnnotation(MatchBy.class);
        if (matchExpression == null) {
            return propertyDelegate.getPattern();
        } else {
            return matchExpression.value();
        }
    }

    private PropertyDelegate makePropertyDelegate(Field field) {
        try {
            return tryMakePropertyDelegate(field);
        } catch (InstantiationException e) {
            throw new TransformationException(String.format("Could not instantiate delegate for %s", field), e);
        } catch (IllegalAccessException e) {
            throw new TransformationException(String.format("Could not access delegate for %s", field), e);
        } catch (InvocationTargetException e) {
            throw new TransformationException(String.format("Error on instantiating delegate for %s", field), e.getCause());
        } catch (NoSuchMethodException e) {
            throw new TransformationException(String.format("Declared delegate for %s does not have a default constructor", field), e);
        }
    }

    private PropertyDelegate tryMakePropertyDelegate(Field field) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        ResolveMatchWith resolveMatchWith = field.getAnnotation(ResolveMatchWith.class);
        if (resolveMatchWith == null) {
            return delegationFactory.makeDelegate(field, locale);
        } else {
            Constructor<? extends PropertyDelegate> delegateConstructor = resolveMatchWith.value()
                    .getConstructor(Field.class, Locale.class);
            delegateConstructor.setAccessible(true);
            return delegateConstructor.newInstance(field, locale);
        }
    }
}
