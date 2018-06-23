package com.github.wjt.poitrans.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class PropertiesReadUtils {


    public static String getProperties(String key) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = PropertiesReadUtils.class.getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }
}
