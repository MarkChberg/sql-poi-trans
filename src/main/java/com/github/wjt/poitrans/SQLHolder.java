package com.github.wjt.poitrans;

import com.github.wjt.poitrans.parser.SQLSeizeUtils;

import java.util.Objects;

public class SQLHolder {

    private String originSql;

    private String handledSql;

    private String type;

    public SQLHolder(String originSql) {
        this.originSql = originSql;

    }

    public String getHandledSql() {
        if (Objects.isNull(handledSql)) {
            handledSql = SQLSeizeUtils.modifySql(originSql.trim());
            type = handledSql.substring(0, 6);
        }

        return handledSql;
    }

    public String getType() {
        if (Objects.isNull(type)) {
            getHandledSql();
        }
        return type;
    }
}
