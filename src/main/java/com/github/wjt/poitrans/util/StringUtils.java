package com.github.wjt.poitrans.util;

public abstract class StringUtils {


    public static boolean isBlank(String source) {
        return source == null || source.length() == 0;
    }

    public static boolean isNotBlank(String source) {
        return !isBlank(source);
    }
}
