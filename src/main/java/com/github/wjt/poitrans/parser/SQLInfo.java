package com.github.wjt.poitrans.parser;

// info about the sql

public abstract class SQLInfo {

    private String sheetName;

    public String getSheetName() {
        return sheetName == null ? "Sheet1" : sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
}
