package com.github.wjt.poitrans.config;

import com.github.wjt.poitrans.util.PropertiesReadUtils;
import com.github.wjt.poitrans.util.StringUtils;

public class ConfigReader {

    private static ConfigReader instance;

    private String fileName; // to link the excel file

    private String location;

    private ConfigReader() {

    }

    // thread unsafe todo
    public static ConfigReader getInstance() {
        if(instance == null) {
            return new ConfigReader();
        }
        return instance;
    }

    public String getFileName() {
        if (StringUtils.isBlank(fileName)) {
            fileName = PropertiesReadUtils.getProperties("file-name");
        }
        return fileName;
    }

    public String getLocation() {
        if (StringUtils.isBlank(location)) {
            location = PropertiesReadUtils.getProperties("location");
        }
        return location;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static void main(String[] args) {
        ConfigReader instance = ConfigReader.getInstance();
        System.out.println(instance.getFileName());
    }
}
