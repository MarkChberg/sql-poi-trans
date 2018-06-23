package com.github.wjt.poitrans.statement;

import com.github.wjt.poitrans.SQLHolder;
import com.github.wjt.poitrans.connection.Connection;
import com.github.wjt.poitrans.connection.ExcelConnection;
import com.github.wjt.poitrans.parser.DefaultParserDelegate;
import com.github.wjt.poitrans.parser.SQLInfo;
import com.github.wjt.poitrans.parser.SQLParser;
import com.github.wjt.poitrans.parser.SelectSQLParser;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.Objects;

// a statement,can
public class ExcelStatement implements Statement {

    private SQLHolder sqlHolder;

    private SQLInfo sqlInfo;

    private ResultMap<String, String> returnMap;

    private Connection connection;

    private Workbook workbook;

    private Sheet sheet;

    private ExcelMetaData excelMetaData;

    private Integer columnNumber;

    private SQLParser sqlParser = DefaultParserDelegate.getParser(); // init by single-instance

    public ExcelStatement(SQLHolder sqlHolder, Connection connection) {
        this.sqlHolder = sqlHolder;
        if (Objects.isNull(connection))
            this.connection = new ExcelConnection();
        this.connection = connection;
    }

    public SQLHolder getSqlHolder() {
        return sqlHolder;
    }

    public void setSqlHolder(SQLHolder sqlHolder) {
        this.sqlHolder = sqlHolder;
    }

    public SQLInfo getSqlInfo() {
        if (Objects.isNull(sqlInfo)) {
            sqlInfo = sqlParser.parse(sqlHolder);
        }
        return sqlInfo;
    }

    public void setSqlInfo(SQLInfo sqlInfo) {
        this.sqlInfo = sqlInfo;
    }

    public ResultMap<String, String> getReturnMap() {
        return returnMap;
    }

    public void setReturnMap(ResultMap<String, String> returnMap) {
        this.returnMap = returnMap;
    }

    public Connection getConnection() {
        if (Objects.isNull(connection))
            connection = new ExcelConnection();
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Workbook getWorkbook() throws IOException {
        if (Objects.isNull(workbook)) {
            workbook = new HSSFWorkbook(getConnection().getInputStream());
        }
        return workbook;
    }


    public Sheet getSheet() throws IOException {
        if (Objects.isNull(sheet)) {
            sheet = getWorkbook().getSheet(sqlInfo.getSheetName());
        }
        return sheet;
    }

    public ExcelMetaData getExcelMetaData() throws IOException {
        if (Objects.isNull(excelMetaData))
            excelMetaData = new ExcelMetaData(getColumnNames());
        return excelMetaData;
    }

    public Integer getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(Integer columnNumber) {
        this.columnNumber = columnNumber;
    }

    public String[] getColumnNames() throws IOException {
        Sheet sheet = getSheet();
        Row metaRow = sheet.getRow(0);
        setColumnNumber(metaRow.getPhysicalNumberOfCells());
        String[] columnNames = new String[columnNumber];
        for(int i = 0; i < columnNumber; i++) {
            columnNames[i] = metaRow.getCell(i).getStringCellValue();
        }
        return columnNames;
    }

    @Override
    public ResultMap<String, String> execute() throws IOException {
        Sheet sheet = getSheet();
        System.out.println(sheet);
        connection.close();
        return null;
    }

    @Override
    public ResultMap<String, String> execute(String sql) {
        return null;
    }
}
