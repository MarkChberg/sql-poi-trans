package com.github.wjt.poitrans.parser;

import java.util.Objects;

public class DeleteSQLParser extends AbstractSQLParser {

    private static DeleteSQLParser single;

    private DeleteSQLParser() {

    }

    public static DeleteSQLParser getParser() {
        if (Objects.isNull(single))
            single = new DeleteSQLParser();
        return single;
    }


    @Override
    protected void fillSQLInfo(BaseSQLInfo baseSQLInfo, String sql) {
        baseSQLInfo.setSqlType(BaseSQLInfo.SQLType.DELETE);
        baseSQLInfo.setSheetName(SQLSeizeUtils.seizeSheetNameFromUpdate(sql));
        baseSQLInfo.setCaseMap(SQLSeizeUtils.seizeCaseMap(sql));
    }
}
