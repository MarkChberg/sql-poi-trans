package com.github.wjt.poitrans.statement;

import java.io.IOException;

public interface Statement {

    ResultMap<String, String> execute() throws IOException;

    ResultMap<String, String> execute(String sql);
}
