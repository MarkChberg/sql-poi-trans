package com.github.wjt.poitrans.statement;


public class ExcelMetaData {

    private String[] columnNames;

    public ExcelMetaData(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }
}
