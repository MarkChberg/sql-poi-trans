package com.github.wjt.poitrans;

import java.util.Objects;

public class SQLHolder {

    private String originSql;

    private String handledSql;

    public SQLHolder(String originSql) {
        this.originSql = originSql;
    }

    public String getHandledSql() {
        if (Objects.isNull(handledSql))
            handledSql = originSql;
        return handledSql;
    }
}
