package com.socialheat.analysis;

import com.socialheat.bean.DataBase;
import com.socialheat.bean.Rate;
import com.socialheat.bean.Word;
import com.socialheat.jieba.WordCount;
import com.socialheat.jieba.WordSplit;
import com.socialheat.util.DataQuery;
import com.socialheat.util.SaveTxtFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sl on 16-10-31.
 */
public class DataStat {

    public static void main(String args[]) throws IOException {
        DataStat ds = new DataStat();
        ds.analysis();
    }

    public void analysis() throws IOException {

        DataQuery dq = new DataQuery();
        WordSplit run = new WordSplit();
        WordCount wordCount = new WordCount();

        //整体分词
        List<String> fenciResults = run.run();
        List<Word> topN_words = wordCount.count(fenciResults, 10);

        List<Rate> rates = dq.queryByTime(10);
        System.out.println("\n准备进入Rate循环。。。。。。。。。");

        for(Rate rate : rates){

            List<String> fenci_results = run.runByWords(rate.getStrings());
            rate.setRate(wordCount.countForDataStat(topN_words,fenci_results,rate.getStrings()));

            System.out.println(rate.getStartTime()+" -- "+rate.getEndTime()+"  : "+rate.getRate());
        }


        SaveTxtFile.writeForUsee("/home/sl/SocialHeat/1/" + DataBase.tableName + "_Rate.txt",rates);
//        SaveTxtFile.write("/home/sl/SocialHeat/1/" + DataBase.tableName + ".txt", results);

    }


}
