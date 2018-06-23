package com.github.wjt.poitrans.parser;

// select id,name,age from Sheet1 where id = 3
// update Sheet1 set name='wjtwjt' where id = 4
// delete from Sheet1 where id = 1
// insert into Sheet1(id,name,age) values (2, 'jack', 34)

import com.github.wjt.poitrans.SQLHolder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.*;

public abstract class SQLSeizeUtils {

    private final static String FROM = "FROM";
    private final static String WHERE = "WHERE";
    private final static String VALUES = "VALUES";

    private final static Set<String> KEY_WORDS = ImmutableSet.of("SELECT", "UPDATE", "DELETE",
            "INSERT", "FROM", "INTO", "WHERE", "VALUES", "AND", "SET");
    private final static String SET = "SET";
    private static final String INTO = "INTO";

    public static String seizeSheetNameFromSelect(String sql) {
        String[] froms = sql.split(FROM);
        String fromAfter = getSecond(froms);
        if (fromAfter.contains(WHERE)) {
            String[] wheres = fromAfter.split(WHERE);
            String sheetName = getFirst(wheres);
            return sheetName.trim();
        }
        return fromAfter.trim();
    }

    public static String getFirst(String[] strings) {
        return strings[0];
    }

    public static String getSecond(String[] strings) {
        return strings[1];
    }

    public static Map<String, ValueHolder> seizeCaseMap(String sql) {
        Map<String, ValueHolder> map = null;
        if (sql.contains(WHERE)) {
            map = new HashMap<>();
            String[] wheres = sql.split(WHERE);
            String casesString = getSecond(wheres);
            String[] caseAndValue = casesString.split("AND");
            for (String cs : caseAndValue) {
                String[] kAndV = cs.split("=");
                String key = getFirst(kAndV).trim();
                String value = getSecond(kAndV).trim();
                if (value.contains("'")) {
                    map.put(key, new ValueHolder(clearApostrophe(value), String.class));
                } else {
                    map.put(key, new ValueHolder(value, Number.class));
                }
            }
        }
        return map;
    }

    private static String clearApostrophe(String word) {
        return word.substring(1, word.length() - 1);
    }

    public static Map<String, ValueHolder> seizeInsertMap(String sql) {
        Map<String, ValueHolder> map = null;
        if (sql.contains(VALUES)) {
            String[] values = sql.split(VALUES);
            String variables = getSecond(values).trim();
            String columns = getFirst(values).trim();
            columns = columns.substring(columns.indexOf("("), columns.length() - 1);
            String[] vars = variables.split(",");
            String[] columnVars = columns.split(",");
            if (vars.length != columnVars.length) {
                throw new IllegalArgumentException("語法格式錯誤");
            }
            for(int i = 0; i <= vars.length; i++) {
                if (vars[i].contains("'")) {
                    map.put(columnVars[i].trim(), new ValueHolder(clearApostrophe(vars[i].trim()), String.class));
                } else {
                    map.put(columnVars[i].trim(), new ValueHolder(vars[i].trim(), Number.class));
                }
            }
        }
        return map;
    }

    public static List<String> seizeFields(String sql) {
        String[] froms = sql.split(FROM);
        String fromBefore = getFirst(froms).trim();
        String fieldString = fromBefore.substring(6, fromBefore.length());
        if (Objects.equals(fieldString.trim(), "*")) {
            return ImmutableList.of("*");
        }
        String[] fields = fieldString.split(",");
        return Arrays.asList(fields);
    }

    public static void main(String[] args) {
        SQLHolder sql = new SQLHolder("select * from test1 where id = 1 and name = 'wjt'");
        String s = seizeSheetNameFromSelect(sql.getHandledSql());
        Map<String, ValueHolder> stringValueHolderMap = seizeCaseMap(sql.getHandledSql());
        Map<String, ValueHolder> stringValueHolderMap1 = seizeInsertMap(sql.getHandledSql());
        List<String> strings = seizeFields(sql.getHandledSql());
        System.out.println("finish");
    }

    public static String modifySql(String originSql) {
        String[] words = originSql.split(" ");
        for(int i = 0; i < words.length; i++) {
            words[i] = KEY_WORDS.contains(words[i].toUpperCase()) ? words[i].toUpperCase() : words[i];
        }
        return String.join(" ", words);
    }

    public static String seizeSheetNameFromUpdate(String sql) {
        String[] sets = sql.split(SET);
        String setBefore = getFirst(sets).trim();
        return setBefore.substring(6, setBefore.length());
    }

    public static Map<String, ValueHolder> seizeSetMap(String sql) {
        Map<String, ValueHolder> map = null;
        String[] sets = sql.split(SET);
        String setAfter = getSecond(sets);
        String[] wheres = setAfter.split("WHERE");
        String setString = getFirst(wheres).trim();
        String[] settings = setString.split(",");
        for (String setting : settings) {
            String[] kv = setting.split("=");
            String key = getFirst(kv);
            String value = getSecond(kv);
            if (value.contains("'")) {
                map.put(key.trim(), new ValueHolder(clearApostrophe(value.trim()), String.class));
            } else {
                map.put(key.trim(), new ValueHolder(value.trim(), Number.class));
            }
        }
        return map;
    }

    public static String seizeSheetNameFromDelete(String sql) {
        return seizeSheetNameFromSelect(sql);
    }

    public static String seizeSheetNameFromInsert(String sql) {
        String[] froms = sql.split(INTO);
        String intoAfter = getSecond(froms);
        String[] wheres = intoAfter.split(VALUES);
        String containSheetName = getFirst(wheres).trim();
        return containSheetName.substring(0, containSheetName.indexOf("("));
    }
}
