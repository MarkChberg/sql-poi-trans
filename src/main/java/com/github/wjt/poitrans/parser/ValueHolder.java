package com.github.wjt.poitrans.parser;

public class ValueHolder<T> {

    private T value;

    private Class type;

    public ValueHolder(T value, Class type) {
        this.value = value;
        this.type = type;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
