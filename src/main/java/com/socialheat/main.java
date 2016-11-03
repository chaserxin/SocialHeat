package com.socialheat;

import com.socialheat.util.DataQuery;
import com.socialheat.util.SaveTxtFile;
import com.socialheat.util.WordAnalyzer2;
import com.socialheat.util.nlpir.WordFrequency;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sl on 16-7-26.
 */
public class main {

    public static void main(String args[]) throws IOException {

        String tableName = "dfzx_weibo";
        String columnName = "text";
        String columnName2 = "rt_text";

        DataQuery query = new DataQuery();

        List<String> queryResult = query.queryDoubleColumn();

        WordAnalyzer2 analyzer = new WordAnalyzer2();
        List<String> result = analyzer.count(queryResult,1);

//        SaveTxtFile.write("/home/sl/SocialHeat/1/"+tableName+".txt",result);
    }

}
