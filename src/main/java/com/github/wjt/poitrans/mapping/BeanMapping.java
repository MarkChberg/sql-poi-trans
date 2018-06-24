package com.github.wjt.poitrans.mapping;

import com.github.wjt.poitrans.parser.BaseSQLInfo;
import com.github.wjt.poitrans.statement.ResultMap;


// 將resultMap轉化為Bean
public interface BeanMapping {

    // 泛型方法，傳入一個泛型，返回一個規定類型
    <T> T getBean(ResultMap<String, String> resultMap, BaseSQLInfo baseSQLInfo, Class<T> tClass) throws IllegalAccessException, InstantiationException;
}
