package com.socialheat.util;


import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
/**
 * Created by sl on 16-7-27.
 */
public class WordAnalyzer {

    public List<String> count(List<String> words , int topNum) throws IOException {
        Map<String,Integer> wordCount = new HashMap<String, Integer>();

        System.out.println("开始进行分词...");

        for(String word : words){
            StringReader sr=new StringReader(word);
            IKSegmenter ik=new IKSegmenter(sr, true);
            Lexeme lex=null;
            while((lex=ik.next())!=null){
                String it = lex.getLexemeText();
                if(!wordCount.containsKey(it)){
                    wordCount.put(it,1);
                }else{
                    int it_count = wordCount.get(it);
                    wordCount.remove(it);
                    wordCount.put(it,it_count+1);
                }
                //System.out.print(it+"|");
            }
        }

        //统计词频for(Map.Entry<String, String> entry:map.entrySet()){
        double sum=0;
        for(Map.Entry<String,Integer> it : wordCount.entrySet()){
            sum += it.getValue();
        }
        System.out.println("分词结束！共分出"+(long)sum+"个词语（含重复词语）！");


        System.out.println("开始进行词频统计...");

        List<Map.Entry<String, Integer>> temp =
                new ArrayList<Map.Entry<String, Integer>>(wordCount.entrySet());

        Collections.sort(temp, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue() - o1.getValue());
               // return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });

//        for (int i=0; i<topNum; i++) {
//            System.out.println(i+1+" "+temp.get(i).getKey()+" "+
//                    temp.get(i).getValue()/sum);
//        }

        List<String> result = new ArrayList<String>();

        for (int i=0; i<temp.size(); i++) {
            String outString = temp.get(i).getKey()+"  "+
                    String.format("%.6f", temp.get(i).getValue()/sum);
            result.add(outString);
        }

        System.out.println("统计结束！共统计词语"+temp.size()+"个（无重复词语）！");
        return result;

    }

}