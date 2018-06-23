package com.github.wjt.poitrans.connection;

import com.github.wjt.poitrans.config.ConfigReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


// a connection to a file
public class ExcelConnection implements Connection {

    private InputStream inputStream;

    private String LOCATION = ConfigReader.getInstance().getLocation();

    private String fileName = ConfigReader.getInstance().getFileName();

    public ExcelConnection(String fileName) {
        this.fileName = fileName;
    }

    public ExcelConnection() {

    }

    public void close() throws IOException {
        inputStream.close();
    }

    public boolean isOpen() {
        return Objects.nonNull(inputStream);
    }

    public boolean isClosed() {
        return false;
    }

    public InputStream getInputStream() throws FileNotFoundException {
        if (Objects.isNull(inputStream)) {
            inputStream = new FileInputStream(LOCATION + fileName);
        }
        return inputStream;
    }

    public static void main(String[] args) {
        Connection connection = new ExcelConnection();
        try {
            InputStream inputStream = connection.getInputStream();
            System.out.println(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
