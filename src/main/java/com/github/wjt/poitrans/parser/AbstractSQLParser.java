package com.github.wjt.poitrans.parser;

import com.github.wjt.poitrans.SQLHolder;

public abstract class AbstractSQLParser implements SQLParser {


    @Override
    public BaseSQLInfo parse(SQLHolder sqlHolder) {
        String sql = sqlHolder.getHandledSql();
        return this.handleParse(sql);
    }

    private BaseSQLInfo handleParse(String sql) {
        BaseSQLInfo baseSQLInfo = new BaseSQLInfo();
        fillSQLInfo(baseSQLInfo, sql);
        return baseSQLInfo;
    }

    protected abstract void fillSQLInfo(BaseSQLInfo baseSQLInfo, String sql);

}
