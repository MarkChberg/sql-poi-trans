package com.github.wjt.poitrans.util;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public abstract class SQLUtils {
    public static Set<String> KEY_WORDS = ImmutableSet.of("SELECT", "UPDATE", "DELETE", "INSERT", "FROM", "WHERE", "INTO", "VALUES", "SET");

    public static String modifySqlFormat(String source) {
        String[] words = source.split(" ");
        for(int i = 0; i < words.length; i++) {
            String modifyWord = words[i].toUpperCase();
            if (KEY_WORDS.contains(modifyWord)) {
                words[i] = modifyWord;
            }
        }
        return String.join(" ", words);
    }
}
