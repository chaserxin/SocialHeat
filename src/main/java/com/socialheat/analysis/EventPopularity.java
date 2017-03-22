package com.socialheat.analysis;

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
	public double getRate(List<Word> topNWordList, Map<String,Integer[]> wordCountMap, int sentenceCount) {
 		
 		// 此段时间内所有热词出现的总次数
 		int hotWord_contain = 0;
 		for (Word word : topNWordList){
 			if (wordCountMap.containsKey(word.getName())) {
 				hotWord_contain = hotWord_contain + wordCountMap.get(word.getName())[0];
            } else {
            	continue;
            }
 		}
 		
 		// 此段时间内的事件热度
 		double eventPopularity = 0;
 		
 		// 此段时间内的所有热词
        for (Word word : topNWordList){
        	String wordString = word.getName();
        	// 此时间段内此热词出现次数
            int wordNums = 0;
            // 此时间段内包含此热词的句子数
            int sentenceCount_contain = 0;

            if (wordCountMap.containsKey(wordString)) {
            	wordNums = wordCountMap.get(wordString)[0];
            } else {
            	continue;
            }

            if (wordCountMap.containsKey(wordString)) {
            	sentenceCount_contain = wordCountMap.get(wordString)[1];
			}

            if (sentenceCount_contain==0 || wordNums==0){
            	eventPopularity += 0;
            } else {
            	double idf = Math.log(sentenceCount / (double)sentenceCount_contain);
            	double len = Math.log(word.getName().length()) / Math.log(2);
            	double tf = (double)wordNums / (double)hotWord_contain;
            	double p_wi = tf * idf * len * word.getCpmi();
            	eventPopularity += wordNums * p_wi;
            }
        }
		
        return eventPopularity;
    }
	
	
	public double getRate1(List<Word> topNWords , List<String> wordSplit_words , List<String> sentences) {
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

            if(sentenceNum_contain==0 || wordNum==0){
                rate += 0;
            }else{
            	double tf = wordNum_contain / (double)wordNum;
            	double idf = Math.log(sentenceNum / (double)sentenceNum_contain);
            	double len = Math.log(word.getName().length()) / Math.log(2);
                rate += tf * idf * len;
            }
        }
        return rate;
    }

}
