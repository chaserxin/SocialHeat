package com.socialheat.analysis;

import com.socialheat.bean.BeanFor_tumu;
import com.socialheat.bean.DataBase;
import com.socialheat.bean.Rate;
import com.socialheat.bean.Word;
import com.socialheat.jieba.WordCount;
import com.socialheat.jieba.WordSplit;
import com.socialheat.util.SaveTxtFile;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sl on 16-11-2.
 */
public class tumuyuan {


    public static void main(String argv[]) throws IOException {

        WordSplit run = new WordSplit();
        WordCount wordCount = new WordCount();

        //整体分词

        String filePath = "/home/sl/SocialHeat/data.txt";
        List<String> sentences = readTxtFile(filePath);
        List<String> fenciResults = run.runByWords(sentences);
        List<Word> topN_words = wordCount.count(fenciResults, 10);

        //TODO:2016年11月2号写到这里

        List<Rate> rates = readTxtFile(filePath,10);

        for(Rate rate : rates){

            List<String> fenci_results = run.runByWords(rate.getStrings());
            rate.setRate(wordCount.countForDataStat(topN_words,fenci_results,rate.getStrings()));

            System.out.println(rate.getStartTime()+" -- "+rate.getEndTime()+"  : "+rate.getRate());
        }


        SaveTxtFile.writeForUsee("/home/sl/SocialHeat/1/tumu_10.txt", rates);
    }


    public static List<String> readTxtFile(String filePath){

        List<String> sentences = new ArrayList<String>();
        try {
            String encoding="utf-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    String ss[] = lineTxt.split(" ");
                    sentences.add(ss[4]);
                    System.out.println(ss[4]);
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return sentences;
    }


    public static List<Rate> readTxtFile(String filePath , int span){

        List<BeanFor_tumu> sentences = new ArrayList<BeanFor_tumu>();
        try {
            String encoding="utf-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;

                while((lineTxt = bufferedReader.readLine()) != null){
                    String ss[] = lineTxt.split(" ");
                    Timestamp time = Timestamp.valueOf(ss[1]+" "+ss[2]);
                    sentences.add(new BeanFor_tumu(time.getTime()/1000,ss[4]));
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }



        int ms = span*60;
        int size = sentences.size();
        long startTime=sentences.get(0).getTime() , endTime=sentences.get(size-1).getTime();
        List<Rate> result = new ArrayList<Rate>();

        long i=startTime;
        while (endTime - i >= ms){
            List<String> resStr = new ArrayList<String>();
           for(BeanFor_tumu rs : sentences) {
                if(Long.valueOf(rs.getTime())>=i+ms)
                    break;
                if(Long.valueOf(rs.getTime())<i)
                    continue;
                resStr.add(rs.getMessage());
            }
            result.add(new Rate(resStr,i,i+ms));

            i = i+ms;
        }



        return result;

    }



}
