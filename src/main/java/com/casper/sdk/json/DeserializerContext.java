package com.casper.sdk.json;

import java.util.Stack;

public class DeserializerContext {

    private static final ThreadLocal<Stack<String>> fieldNameStack = new ThreadLocal<>();

    public static void clear() {
        getFieldNameStack().clear();
    }

    public static void pushFieldName(final String name) {
        getFieldNameStack().push(name);
    }

    public static String peekFieldName() {
        final Stack<String> fieldNameStack = getFieldNameStack();
        return fieldNameStack.isEmpty() ? null : fieldNameStack.peek();
    }

    public static String popFieldName() {
        return getFieldNameStack().pop();
    }

    private static Stack<String> getFieldNameStack() {
        Stack<String> fieldNames = fieldNameStack.get();
        if (fieldNames == null) {
            fieldNames = new Stack<>();
            fieldNameStack.set(fieldNames);
        }
        return fieldNames;
    }
}
