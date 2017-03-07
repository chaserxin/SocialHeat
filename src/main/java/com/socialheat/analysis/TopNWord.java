package com.socialheat.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.socialheat.bean.Word;
import com.socialheat.dao.StopWordDao;
import com.socialheat.util.TimeUtil;


public class TopNWord {
	
	// 统计分词好的结果中每次词语出现的次数
	private	Map<String, Word> wordMap;
	// 保存分词好的所有句子
	List<List<String>> splitSencenceList;
	// 记录当前的句子数
	private int index = 0;
	// 停用词集合
	private Set<String> stopWordSet;
	// 所有词语总数(含重复词语)
	long wordSum = 0L;
	
	public TopNWord() {
		wordMap = new HashMap<String, Word>();
		splitSencenceList = new ArrayList<List<String>>();
		index = 0;
	}
	
    /**
     * 
     * @param wordList 已分好的词语，尚未进行停词表处理和统计
     * @param sentenceList 所有的句子列表
     * @param topNum
     * @return
     * @throws IOException
     */
    public List<Word> getWordTopN(List<List<String>> new_splitSencenceList, int topNum) {
    	List<Word> topNWordList = new ArrayList<Word>();
    	
    	if (index == 0) {
    		// 获取停用词
    		StopWordDao stopWordDao = new StopWordDao();
    		stopWordSet = stopWordDao.getStopWord();
    		stopWordSet.add("逾");
    		stopWordSet.add("跌");
    		stopWordSet.add("股");
    	}
    	
		System.out.println("\n开始统计所有词语并去重！ 开始时间为：" + TimeUtil.currentTime());
		// 统计所有词语总数(含重复词语)
		
		for (int i=0; i<new_splitSencenceList.size(); i++) {
			List<String> words = new_splitSencenceList.get(i);
			for(String wordstring : words){
				// 过滤掉停用词和空格
				if (!stopWordSet.contains(wordstring) && !wordstring.equals(" ")) {
			        if (!wordMap.containsKey(wordstring)) {
			        	Word word = new Word(wordstring, 1);
			        	// 初始化 sentenceList
			        	word.lastSentenceIndex = i + index;
			            word.sentenceList.add(i + index);
			            wordMap.put(wordstring, word);
			        } else {
			        	Word word = wordMap.get(wordstring);
			        	word.setTimes(word.getTimes() + 1);
			        	if (i + index != word.lastSentenceIndex) {
			        		word.sentenceList.add(i + index);
			        	}
			        	wordMap.put(wordstring, word);
			        }
			        wordSum ++;
				} else {
					continue;
				}
			}
		}
		// index 叠加
		index += new_splitSencenceList.size();
    	splitSencenceList.addAll(new_splitSencenceList);
    	
		System.out.println("共有：" + wordSum + "个词语（含重复词语）！");
		System.out.println("共有：" + wordMap.size() + "个词语（无重复词语）！");
		System.out.println("统计所有词语并去重结束！ 结束时间为：" + TimeUtil.currentTime());
		System.out.println();
		
		System.out.println("开始计算 TF-IDF！ 开始时间为：" + TimeUtil.currentTime());
		// 计算 TF * IDF * length
		List<Word> allWordList = getTF_IDF_Length(wordMap);
		System.out.println("计算 TF-IDF结束！ 结束时间为：" + TimeUtil.currentTime());
		
		// 得到 topN
		topNWordList = getTopN(topNum, allWordList);
		
	    return topNWordList;
	}

    /**
     * 修改接口，一次性计算 TF_IDF_Length
     * @param wordMap
     * @return
     */
    public List<Word> getTF_IDF_Length(Map<String, Word> wordMap) {
    	List<Word> WordList = new ArrayList<Word>();
    	
    	// allDocCount 为所有文档总数
        int allDocCount = splitSencenceList.size(); 
    	
    	Iterator<Entry<String, Word>> iter = wordMap.entrySet().iterator();
    	while (iter.hasNext()) {
    		Map.Entry<String, Word> entry = iter.next();
    		Word word = entry.getValue();
    		// docCount 为包含该热词的文档总数
    		int docCount = word.sentenceList.size();
    		
    		// 计算总文档数的开方,用于保证所选择的热词出现在的文档数大于等于总文档数的开方
    		if(docCount >= (int) Math.sqrt(splitSencenceList.size())) {
    			// 计算词频 TF
    		    double tf = word.getTimes() / (double)wordSum;
    		    // 计算 IDF 
                double idf = Math.log((double)allDocCount / (double)(docCount+1));
                // 计算长度的 log
                double len = Math.log(word.getName().length()) / Math.log(2);
    		    
                // 保存 TF
                word.setTf(tf);
                // 保存 IDF
                word.setIdf(idf);
                // 保存单独的 TF * IDF
                word.setTf_idf(tf * idf);
                // 保存 TF * IDF * log(len)
                word.setTf_idf_length(tf * idf * len);
                
                WordList.add(word);
    		}
    		
    	}
    	return WordList;
    }

    /**
     * 得到前 TopNum 的热词
     * @param topNum
     * @param wordList
     * @return
     */
    public List<Word> getTopN(int topNum, List<Word> wordList) {
        List<Word> wordListTopN = new ArrayList<Word>();

        System.out.println();
       
    	// 重新排序
    	Collections.sort(wordList,new Comparator<Word>(){
            public int compare(Word a, Word b) {
                return (int)((b.getTf_idf_length() - a.getTf_idf_length()) * 1000000);
            }
        });

        int topNum1 = 0;
        for (Word word : wordList) {
        	if (word.getTf_idf_length() > 0) {
        		word.setIndex(topNum1);
        		wordListTopN.add(word);
        		System.out.println(word.getName() + " : " + word.getTimes() + " ==== " + word.getTf_idf_length());
        		topNum1 ++;
			}
			if (topNum1 == topNum) {
				break;
			}
        }
 		System.out.println("共选出: " + topNum1 + "个热词！\n");
 		
        return wordListTopN;
    }
    
    public Map<String, Integer[]> getWordInfoMap(List<List<String>> splitSencenceList) {
    	Map<String,Integer[]> wordCountMap = new HashMap<String, Integer[]>();
    	
    	for (int i=0; i<splitSencenceList.size(); i++) {
			List<String> wordList = splitSencenceList.get(i);
    		for(String wordstring : wordList){
				// 过滤掉停用词和空格
				if (!stopWordSet.contains(wordstring) && !wordstring.equals(" ")) {
			        if (!wordCountMap.containsKey(wordstring)) {
			        	Integer[] intArr = new Integer[3];
			        	intArr[0] = 1;
			        	intArr[1] = 1;
			        	intArr[2] = i;
			        	wordCountMap.put(wordstring, intArr);
			        } else {
			        	Integer[] intArr = wordCountMap.get(wordstring);
			        	if(intArr[2] == i) {
			        		intArr[0] ++;
			        	} else {
			        		intArr[0] ++;
			        		intArr[1] ++;
			        		intArr[2] = i;
			        	}
			        	wordCountMap.put(wordstring, intArr);
			        }
				} else {
					continue;
				}
			}
		}
    	return wordCountMap;
    }
}
