package com.socialheat.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.socialheat.bean.Word;

/**
 * 得到事件热度
 * @author huaxin
 *
 */
public class EventPopularity {
	
	/**
	 * 
	 * @param topNWordList 热词列表
	 * @param wordSplit_results 此段时间内所有句子的分词
	 * @param sentenceList 此段时间内的所有句子
	 * @return
	 */
	public double getRate_v2(List<Word> topNWordList , List<String> wordSplit_results , List<String> sentenceList) {
        double rate = 0;
        
        // 此段时间内所有的句子
        int sentenceNum = sentenceList.size();
        
 		// 统计分词好的结果中每次词语出现的次数
 		Map<String,Integer> wordCountMap = new HashMap<String, Integer>();
 		for(String word : wordSplit_results){
	        if (!wordCountMap.containsKey(word)) {
	            wordCountMap.put(word, 1);
	        } else {
	            wordCountMap.put(word, wordCountMap.get(word) + 1);
	        }
 		}
        
        for (Word word : topNWordList){
        	// 此时间段内此热词出现次数
            int wordNums = 0;
            // 此时间段内包含热词的句子数
            int sentenceNum_contain = 0;

            if (wordCountMap.containsKey(word.getName())) {
            	wordNums = wordCountMap.get(word.getName());
            } else {
            	continue;
            }

            for (String sentence : sentenceList){
                if(sentence.contains(word.getName()))
                    sentenceNum_contain++;
            }

            if(sentenceNum_contain==0 || wordNums==0){
                rate += 0;
            }else{
            	double idf = Math.log(sentenceNum / (double)sentenceNum_contain);
            	double len = Math.log(word.getLength()) / Math.log(2);
                rate += wordNums * idf * len * word.getCpmi();
            }
        }
        return rate;
    }
	
	public double getRate(List<Word> topNWords , List<String> wordSplit_words , List<String> sentences) {
        double rate = 0;
        
        // 此段时间内所有的词语
        int wordNum = wordSplit_words.size();
        // 此段时间内所有的句子
        int sentenceNum = sentences.size();

        System.out.println("wordNum = " + wordNum);
        System.out.println("sentenceNum = " + sentenceNum);
        for (Word word : topNWords){
        	// 此时间段内包含的热词数量
            int wordNum_contain = 0;
            // 此时间段内包含热词的句子数
            int sentenceNum_contain = 0;

            for (String wordSplit : wordSplit_words){
                if(wordSplit.equals(word.getName()))
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
            	double tf = wordNum_contain / (double)wordNum;
            	double idf = Math.log(sentenceNum / (double)sentenceNum_contain);
            	double len = Math.log(word.getLength()) / Math.log(2);
                rate += tf * idf * len;
            }
        }
        return rate;
    }
}
