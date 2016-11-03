package com.socialheat.jieba;

import com.socialheat.bean.Word;
import com.socialheat.util.*;

import java.io.IOException;
import java.util.*;

/**
 * Created by sl on 16-10-20.
 */
public class WordCount {

    /**
     *
     * @param words 已分好的词语，尚未进行停词表处理和统计
     * @param topNum
     * @return
     * @throws IOException
     */
    public List<Word> count(List<String> words , int topNum) throws IOException {

            List<Word> wordList = new ArrayList<Word>();

            Map<String,Integer> wordCount = new HashMap<String, Integer>();

            Set<String> stopWords = StopWord.getStopWord();

            for(String word : words){
                if (!stopWords.contains(word) && !word.equals(" ")) {

                    if (!wordCount.containsKey(word)) {
                        wordCount.put(word, 1);
                    } else {
                        int it_count = wordCount.get(word);
                        wordCount.remove(word);
                        wordCount.put(word, it_count + 1);
                    }
                }

            }

            //统计词频
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
                }
            });



            List<String> result = new ArrayList<String>();

            for (int i=0; i<temp.size(); i++) {
                String outString = temp.get(i).getKey()+"  "+
                        String.format("%.6f", temp.get(i).getValue()/sum);
                result.add(outString);

                Word word = new Word(temp.get(i).getKey(),temp.get(i).getValue(),
                        temp.get(i).getValue()/sum,temp.get(i).getKey().length());
                wordList.add(word);

            }


            System.out.println("统计结束！共统计词语"+temp.size()+"个（无重复词语）！");

            return WordCount.getTopN(topNum,WordCount.getTF_IDF_length(wordList));

        }


    public static List<Word> getTF_IDF_length(List<Word> wordList){

        DataQuery query = new DataQuery();

        List<String> queryResult = query.queryDoubleColumn();

        String wordName;
        int cnt_tot = queryResult.size();
        System.out.println("cnt_tot = "+cnt_tot);
        int wordEndNum = 0;
        for(Word word : wordList){
            if(wordEndNum>1000)
                break;
            wordEndNum++;
            int cnt=1;
            wordName = word.getName();
            for(String queryWord : queryResult){
                if(queryWord.contains(wordName)){
                    cnt++;
                }
            }

            word.setTimes_sentence(cnt);

            double idf = Math.log(cnt_tot/cnt);
            double len = Math.log(word.getLength())/Math.log(2);

            word.setIdf(idf);
            word.setTf_idf(word.getTf()*idf);
            word.setTf_idf_length(word.getTf()*idf*len);

        }
        return wordList;
    }


    public static List<Word> getTopN(int topNum , List<Word> wordList){
        List<Word> wordListTopN = new ArrayList<Word>();

        Collections.sort(wordList,new Comparator<Word>(){

            public int compare(Word a, Word b) {
                return (int)((b.getTf_idf_length()-a.getTf_idf_length())*1000000);
            }

        });

        for(int i=0 ; i<topNum ; i++){
            System.out.println(wordList.get(i).getName()+" "+wordList.get(i).getTf_idf());
            wordListTopN.add(wordList.get(i));

        }

        return wordListTopN;
    }


    public double countForDataStat(List<Word> topNWords , List<String> fenci_words , List<String> sentences){

        double rate=0;
        int wordNum_contain,sentenceNum_contain;
        int wordNum=fenci_words.size() , sentenceNum=sentences.size();

        System.out.println("wordNum = "+wordNum);
        System.out.println("sentenceNum = "+sentenceNum);
        for (Word word : topNWords){
            //rate = 0;
            wordNum_contain = 0;
            sentenceNum_contain = 0;

//            System.out.println("word : "+word.getName());
            for (String fenci : fenci_words){
//                System.out.println(fenci);
                if(fenci.equals(word.getName()))
                    wordNum_contain++;
            }

            for (String sentence : sentences){
                if(sentence.contains(word.getName()))
                    sentenceNum_contain++;
            }

//            System.out.println("sentenceNum_contain = "+sentenceNum_contain);
//            System.out.println("wordNum = "+wordNum);

            if(sentenceNum_contain==0 || wordNum==0){
                rate += 0;
            }else{
                rate += wordNum_contain/(double)wordNum * Math.log(sentenceNum/(double)sentenceNum_contain)
                        * Math.log(word.getLength())/Math.log(2);
            }

        }

        return rate;

    }



}
