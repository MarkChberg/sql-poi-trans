package com.github.wjt.poitrans;

import com.github.wjt.poitrans.connection.Connection;
import com.github.wjt.poitrans.connection.ExcelConnection;
import com.github.wjt.poitrans.parser.DefaultParserDelegate;
import com.github.wjt.poitrans.parser.SQLParser;
import com.github.wjt.poitrans.statement.ExcelStatement;
import com.github.wjt.poitrans.statement.ResultMap;
import com.github.wjt.poitrans.statement.Statement;

import java.io.IOException;

// main entrance
public class ExcelCrawler implements Runnable{

    private Connection connection;

    private Statement statement;

    private SQLParser sqlParser = DefaultParserDelegate.getParser();

    // private BeanMapping beanMapping;

    private ExcelCrawler() {

    }

    public static ExcelCrawler build() {
        return new ExcelCrawler();
    }

    public ExcelCrawler connection(String fileName) {
        return addConnection(fileName);
    }

    private ExcelCrawler addConnection(String fileName) {
        connection = new ExcelConnection(fileName);
        return this;
    }

    public ExcelCrawler statement(SQLHolder sqlHolder) {
        return addStatement(sqlHolder);
    }

    private ExcelCrawler addStatement(SQLHolder sqlHolder) {
        this.statement = new ExcelStatement(sqlHolder, connection);
        return this;
    }

    public static void main(String[] args) {
        ExcelCrawler.build().statement(new SQLHolder("select * from Sheet1")).run();
    }

    @Override
    public void run() {
        try {
            statement.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
