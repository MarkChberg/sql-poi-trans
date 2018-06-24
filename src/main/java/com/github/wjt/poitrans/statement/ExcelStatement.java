package com.github.wjt.poitrans.statement;

import com.github.wjt.poitrans.SQLHolder;
import com.github.wjt.poitrans.connection.Connection;
import com.github.wjt.poitrans.connection.ExcelConnection;
import com.github.wjt.poitrans.parser.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

// a statement, 對傳入的sqlHolder進行處理轉化，返回一個Map
public class ExcelStatement implements Statement {

    private SQLHolder sqlHolder;

    private BaseSQLInfo sqlInfo;

    private Set<ResultMap<String, String>> retSet;

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

    public BaseSQLInfo getSqlInfo() {
        if (Objects.isNull(sqlInfo)) {
            sqlInfo = sqlParser.parse(sqlHolder);
        }
        return sqlInfo;
    }

    public void setSqlInfo(BaseSQLInfo sqlInfo) {
        this.sqlInfo = sqlInfo;
    }

    public Set<ResultMap<String, String>> getRetSet() {
        return retSet;
    }

    public void setRetSet(Set<ResultMap<String, String>> retSet) {
        this.retSet = retSet;
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
            sheet = getWorkbook().getSheet(getSqlInfo().getSheetName());
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
    public Set<ResultMap<String, String>> queryExecute() throws IOException {
        Map<String, ValueHolder> caseMap = getSqlInfo().getCaseMap();
        Set<Integer> collect = handleCaseMap(caseMap); // 處理條件查詢，獲取行號結果集合
        connection.close();
        return getResult(collect);
    }

    @Override
    public Boolean execute() throws IOException {
        BaseSQLInfo sqlInfo = getSqlInfo();
        try {
            switch (sqlInfo.getSqlType()) {
                case INSERT:
                    insertExecute(sqlInfo);
                    break;
                case DELETE:
                    deleteExecute(sqlInfo);
                    break;
                case UPDATE:
                    updateExecute(sqlInfo);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            return false;
        }
        connection.close();
        return true;
    }

    private Set<Integer> handleCaseMap(Map<String, ValueHolder> caseMap) throws IOException {
        Sheet sheet = getSheet();
        int rowNumber = sheet.getLastRowNum();
        String[] columnNames = getExcelMetaData().getColumnNames();
        if (Objects.nonNull(caseMap)) {
            Set<Map.Entry<String, ValueHolder>> entries = caseMap.entrySet();
            Set<Set<Integer>> caseSetCollection = entries.stream().map(entry -> {
                Set<Integer> result = new HashSet<>();
                String key = entry.getKey();
                ValueHolder value = entry.getValue();
                for (int i = 0; i < columnNumber; i++) {
                    if (Objects.equals(columnNames[i], key)) {
                        for (int j = 1; j <= rowNumber; j++) {
                            Row row = sheet.getRow(j);
                            Cell cell = row.getCell(i);
                            if (value.getType().equals(String.class)) {
                                String val = (String) value.getValue();
                                if (Objects.equals(val, cell.getStringCellValue())) {
                                    result.add(cell.getRowIndex());
                                }
                            }
                            if (value.getType().equals(Number.class)) {
                                Double numVal = new Double((String) value.getValue());
                                if (numVal.compareTo(cell.getNumericCellValue()) == 0) {
                                    result.add(cell.getRowIndex());
                                }
                            }
                        }
                    }
                }
                return result;
            }).collect(Collectors.toSet());
            Set<Integer> collect = caseSetCollection.stream()
                    .flatMap(integers -> integers.stream())
                    .filter(integer -> integer != -1)
                    .collect(Collectors.toSet());
            return collect;
        } else {
            Set<Integer> collect = new HashSet<>();
            for(int i = 1; i <= rowNumber; i++) {
                collect.add(i);
            }
            return collect;
        }
    }

    private Set<ResultMap<String, String>> getResult(Set<Integer> collect) throws IOException {
        if (CollectionUtils.isEmpty(collect))
            return null;
        String[] columnNames = getColumnNames();
        Sheet sheet = getSheet();
        retSet = collect.stream().map(c -> {
            ResultMap<String, String> resultMap = new ResultMap<>();
            Row row = sheet.getRow(c);
            for (int i = 0; i < columnNumber; i++) {
                String value = null;
                try {
                    value = row.getCell(i).getStringCellValue();
                } catch (Exception e) {
                    value = row.getCell(i).getNumericCellValue() + "";
                }
                resultMap.put(columnNames[i], value);
            }
            return resultMap;
        }).collect(Collectors.toSet());
        return retSet;
    }

    private void deleteExecute(BaseSQLInfo sqlInfo) throws IOException {
        Map<String, ValueHolder> caseMap = sqlInfo.getCaseMap();
        Set<Integer> integers = handleCaseMap(caseMap);
        Sheet sheet = getSheet();
        integers.forEach(i -> sheet.removeRow(sheet.getRow(i)));
        removeBlankRow();
        OutputStream fos = new FileOutputStream(getConnection().getFileName());
        getWorkbook().write(fos);
        fos.close();
    }

    private void removeBlankRow() throws IOException {
        int i = getSheet().getLastRowNum();
        while(i > 0){
            i--;
            Row tempRow = getSheet().getRow(i);
            if(tempRow == null){
                sheet.shiftRows(i+1, sheet.getLastRowNum(), -1);
            }
        }
    }

    private void updateExecute(BaseSQLInfo sqlInfo) throws IOException {
        Map<String, ValueHolder> caseMap = sqlInfo.getCaseMap();
        Map<String, ValueHolder> setMap = sqlInfo.getSetMap();
        Set<Integer> integers = handleCaseMap(caseMap);
        if (CollectionUtils.isNotEmpty(integers) && integers.size() == 1) {
            Integer updateNumber = integers.toArray(new Integer[0])[0];
            Row row = getSheet().getRow(updateNumber);
            setCell(row, setMap);
            writeToWorkBook();
        }
    }

    private void insertExecute(BaseSQLInfo sqlInfo) throws IOException {
        Sheet sheet = getSheet();
        int insertRowNum = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(insertRowNum);
        Map<String, ValueHolder> insertMap = sqlInfo.getInsertMap();
        setCell(row, insertMap);
        writeToWorkBook();
    }

    private void writeToWorkBook() throws IOException {
        OutputStream fos = new FileOutputStream(getConnection().getFileName());
        getWorkbook().write(fos);
        fos.close();
    }

    private void setCell(Row row, Map<String, ValueHolder> map) throws IOException {
        String[] columnNames = getColumnNames();
        map.entrySet().forEach(set -> {
            String key = set.getKey();
            ValueHolder value = set.getValue();
            for(int i = 0; i < getColumnNumber(); i++) {
                if (Objects.equals(columnNames[i], key)) {
                    Cell cell = row.getCell(i);
                    if (value.getType().equals(String.class)) {
                        String val = (String) value.getValue();
                        cell.setCellValue(val);
                    }
                    if (value.getType().equals(Number.class)) {
                        Object num = value.getValue();
                        cell.setCellValue((Double) num);
                    }
                }
            }
        });
    }



    public static void main(String[] args) {
        Statement statement = new ExcelStatement(new SQLHolder("select * from test1 where id = 1"), new ExcelConnection("hello-world.xls"));
        try {
            Set<ResultMap<String, String>> resultMaps = statement.queryExecute();
            System.out.println(resultMaps);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
