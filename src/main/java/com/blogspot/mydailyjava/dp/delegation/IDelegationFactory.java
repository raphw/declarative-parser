package com.blogspot.mydailyjava.dp.delegation;

import java.lang.reflect.Field;
import java.util.Locale;

public interface IDelegationFactory {

    PropertyDelegate makeDelegate(Field field, Locale locale);
}
