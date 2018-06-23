package com.github.wjt.poitrans.parser;

import java.util.Objects;

public class InsertSQLParser extends AbstractSQLParser {

    private static InsertSQLParser single;

    private InsertSQLParser() {}

    public static InsertSQLParser getParser() {
        if (Objects.isNull(single))
            single = new InsertSQLParser();
        return single;
    }

    @Override
    protected void fillSQLInfo(BaseSQLInfo baseSQLInfo, String sql) {
        baseSQLInfo.setSqlType(BaseSQLInfo.SQLType.INSERT);
        baseSQLInfo.setSheetName(SQLSeizeUtils.seizeSheetNameFromInsert(sql));
        baseSQLInfo.setInsertMap(SQLSeizeUtils.seizeInsertMap(sql));
    }
}
