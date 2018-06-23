package com.github.wjt.poitrans.parser;

public class ValueHolder<T> {

    private T value;

    private Class type;

    public ValueHolder(T value, Class type) {
        this.value = value;
        this.type = type;
    }
}
