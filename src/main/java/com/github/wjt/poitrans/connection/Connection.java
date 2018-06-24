package com.github.wjt.poitrans.connection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface Connection {

    void close() throws IOException;

    boolean isOpen();

    boolean isClosed();

    InputStream getInputStream() throws FileNotFoundException;

    String getFileName();
}
