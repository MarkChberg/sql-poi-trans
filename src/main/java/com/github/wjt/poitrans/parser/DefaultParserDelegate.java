package com.github.wjt.poitrans.parser;

import com.github.wjt.poitrans.SQLHolder;

import java.util.Objects;

// 繼承SQLParser，雖然這是個委託類，但對外同樣有和SQLParser一樣的功能
public class DefaultParserDelegate implements SQLParser{

    private static DefaultParserDelegate single;

    private SelectSQLParser selectSQLParser = SelectSQLParser.getParser();

    private UpdateSQLParser updateSQLParser = UpdateSQLParser.getParser();

    private DeleteSQLParser deleteSQLParser = DeleteSQLParser.getParser();

    private InsertSQLParser insertSQLParser = InsertSQLParser.getParser();

    private DefaultParserDelegate() {

    }

    /*以單例模式，向外界提供一個默認的轉化器實現*/
    public static DefaultParserDelegate getParser() {
        if (Objects.isNull(single)) {
            single = new DefaultParserDelegate();
        }
        return single;
    }

    @Override
    public BaseSQLInfo parse(SQLHolder sqlHolder) {
        BaseSQLInfo retInfo = null;
        switch (sqlHolder.getType()) {
            case "SELECT" :
                retInfo = selectSQLParser.parse(sqlHolder);
                break;
            case "UPDATE":
                retInfo = updateSQLParser.parse(sqlHolder);
                break;
            case "INSERT":
                retInfo = insertSQLParser.parse(sqlHolder);
                break;
            case "DELETE":
                retInfo = deleteSQLParser.parse(sqlHolder);
                break;
            default:
                break;
        }
        return retInfo;
    }

    /*以下四個setter可以實現不同的轉化器定制*/
    public void setSelectSQLParser(SelectSQLParser selectSQLParser) {
        this.selectSQLParser = selectSQLParser;
    }

    public void setUpdateSQLParser(UpdateSQLParser updateSQLParser) {
        this.updateSQLParser = updateSQLParser;
    }


    public void setDeleteSQLParser(DeleteSQLParser deleteSQLParser) {
        this.deleteSQLParser = deleteSQLParser;
    }

    public void setInsertSQLParser(InsertSQLParser insertSQLParser) {
        this.insertSQLParser = insertSQLParser;
    }


    public static void main(String[] args) {
        SQLParser sqlParser = DefaultParserDelegate.getParser();
        BaseSQLInfo parse = sqlParser.parse(new SQLHolder("select * from sheet1"));
        System.out.println(parse);
    }
}
