package com.github.wjt.poitrans.parser;


import java.util.Objects;

public class SelectSQLParser extends AbstractSQLParser {

    private static SelectSQLParser single;

    private SelectSQLParser() {}

    public static SelectSQLParser getParser() {
        if (Objects.isNull(single))
            single = new SelectSQLParser();
        return single;
    }

    @Override
    protected void fillSQLInfo(BaseSQLInfo baseSQLInfo, String sql) {
        baseSQLInfo.setSqlType(BaseSQLInfo.SQLType.SELECT);
        baseSQLInfo.setSheetName(SQLSeizeUtils.seizeSheetNameFromSelect(sql));
        baseSQLInfo.setCaseMap(SQLSeizeUtils.seizeCaseMap(sql));
        baseSQLInfo.setFields(SQLSeizeUtils.seizeFields(sql));
    }
}
