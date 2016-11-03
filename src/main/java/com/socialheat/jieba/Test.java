package com.socialheat.jieba;
import com.socialheat.bean.DataBase;
import com.socialheat.bean.Word;
import com.socialheat.util.SaveTxtFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by sl on 16-10-20.
 */
public class Test {

    public static void main(String args[]) throws IOException {

        WordSplit run = new WordSplit();
        List<String> fenci_results = run.run();
        WordCount wordCount = new WordCount();
        List<Word> results = wordCount.count(fenci_results, 50);
        SaveTxtFile.write("/home/sl/SocialHeat/1/" + DataBase.tableName + ".txt",results);

    }


}
