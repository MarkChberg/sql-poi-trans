package com.github.wjt.poitrans.statement;

import java.io.IOException;
import java.util.Set;

public interface Statement {

    Set<ResultMap<String, String>> queryExecute() throws IOException;

    Boolean execute() throws IOException;

}
