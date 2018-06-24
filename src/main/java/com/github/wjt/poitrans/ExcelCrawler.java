package com.github.wjt.poitrans;

import com.github.wjt.poitrans.connection.Connection;
import com.github.wjt.poitrans.connection.ExcelConnection;
import com.github.wjt.poitrans.mapping.BeanMapping;
import com.github.wjt.poitrans.mapping.DefaultBeanMapping;
import com.github.wjt.poitrans.parser.BaseSQLInfo;
import com.github.wjt.poitrans.parser.DefaultParserDelegate;
import com.github.wjt.poitrans.parser.SQLParser;
import com.github.wjt.poitrans.statement.ExcelStatement;
import com.github.wjt.poitrans.statement.ResultMap;
import com.github.wjt.poitrans.statement.Statement;

import java.io.IOException;
import java.util.Set;

// main entrance
public class ExcelCrawler {

    private Connection connection;

    private Statement statement;

    private SQLParser sqlParser = DefaultParserDelegate.getParser();

    private BeanMapping beanMapping = DefaultBeanMapping.getMapping();

    private BaseSQLInfo baseSQLInfo;

    private Class retClass;

    private ExcelCrawler() {

    }

    public static ExcelCrawler build() {
        return new ExcelCrawler();
    }

    public ExcelCrawler beanMapping(BeanMapping beanMapping) {
        return addBeanMapping(beanMapping);
    }

    private ExcelCrawler addBeanMapping(BeanMapping beanMapping) {
        this.beanMapping = beanMapping;
        return this;
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
        baseSQLInfo = sqlParser.parse(sqlHolder);
        return this;
    }

    public <T> ExcelCrawler setMappingBean(Class<T> clazz) {
        return addMappingBean(clazz);
    }

    private <T> ExcelCrawler addMappingBean(Class<T> clazz) {
        this.retClass = clazz;
        return this;
    }

    public static void main(String[] args) {
        try {
            ExcelCrawler.build().statement(new SQLHolder("select * from test1")).setMappingBean(TestDemo.class).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public <T> T execute() throws Exception {
        T bean = null;
        try {
            Set<ResultMap<String, String>> resultMaps = statement.queryExecute();
            for (ResultMap<String, String> resultMap : resultMaps) {
                try {
                    bean = beanMapping.getBean(resultMap, baseSQLInfo, (Class<T>) retClass);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bean;
    }
}
