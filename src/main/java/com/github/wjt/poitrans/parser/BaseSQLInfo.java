package com.github.wjt.poitrans.parser;

import java.util.List;
import java.util.Map;
// select id,name,age from Sheet1 where id = 3
// update Sheet1 set name='wjtwjt' where id = 4
// delete from Sheet1 where id = 1
// insert into Shee1(id,name,age) values (2, 'jack', 34)
public class BaseSQLInfo extends SQLInfo {

    private Map<String, ValueHolder> caseMap;

    private List<String> fields;

    private Map<String, ValueHolder> setMap;

    private Map<String, ValueHolder> insertMap;

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    private SQLType sqlType;

    public Map<String, ValueHolder> getCaseMap() {
        return caseMap;
    }

    public void setCaseMap(Map<String, ValueHolder> caseMap) {
        this.caseMap = caseMap;
    }

    public Map<String, ValueHolder> getInsertMap() {
        return insertMap;
    }

    public void setInsertMap(Map<String, ValueHolder> insertMap) {
        this.insertMap = insertMap;
    }

    public SQLType getSqlType() {
        return sqlType;
    }

    public void setSqlType(SQLType sqlType) {
        this.sqlType = sqlType;
    }

    public Map<String, ValueHolder> getSetMap() {

        return setMap;
    }

    public void setSetMap(Map<String, ValueHolder> setMap) {
        this.setMap = setMap;
    }

    public static enum SQLType {
        INSERT,
        UPDATE,
        DELETE,
        SELECT
    }
}
