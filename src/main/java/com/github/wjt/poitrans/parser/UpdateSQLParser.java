package com.github.wjt.poitrans.parser;

import java.util.Objects;

public class UpdateSQLParser extends AbstractSQLParser {

    private static UpdateSQLParser single;

    private UpdateSQLParser() {

    }

    public static UpdateSQLParser getParser() {
        if (Objects.isNull(single))
            single = new UpdateSQLParser();
        return single;
    }


    @Override
    protected void fillSQLInfo(BaseSQLInfo baseSQLInfo, String sql) {
        baseSQLInfo.setSqlType(BaseSQLInfo.SQLType.UPDATE);
        baseSQLInfo.setSheetName(SQLSeizeUtils.seizeSheetNameFromDelete(sql));
        baseSQLInfo.setSetMap(SQLSeizeUtils.seizeSetMap(sql));
        baseSQLInfo.setCaseMap(SQLSeizeUtils.seizeCaseMap(sql));
    }
}
