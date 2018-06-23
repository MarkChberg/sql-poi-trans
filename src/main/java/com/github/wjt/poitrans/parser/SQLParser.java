package com.github.wjt.poitrans.parser;


import com.github.wjt.poitrans.SQLHolder;

// parse the sql info for sql String
public interface SQLParser {

    BaseSQLInfo parse(SQLHolder sqlHolder);
}
