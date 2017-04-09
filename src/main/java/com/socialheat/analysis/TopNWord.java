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
	// 记录当前的句子数
	private int index;
	// 停用词集合
	private Set<String> stopWordSet;
	// 所有词语总数(含重复词语)
	private long wordSum = 0L;
	// 用于统计 CPMI
	private WordCPMI wordCPMI;
	
	// topN热词的 CPIM 总和
	@SuppressWarnings("unused")
	private double totalCPMI;
	// topN热词的 weight 总和
	@SuppressWarnings("unused")
	private double totalWeight;
	
	// 当前取出来所有句子数
	// 每次都是新的
	private int newSentenceCount;
	// 当前取出来所有词语数
	// 每次都是新的
	private int newWordCount;
	// 统计当前遍历取出来的所有词语出现的次数
	// 每次都是新的
	private Map<String,Integer[]> newWordCountMap;
	
	
	
	public TopNWord(int topNum) {
		wordCPMI = new WordCPMI(topNum);
		wordMap = new HashMap<String, Word>();
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
    public List<Word> getWordTopN(List<String[]> splitSencenceList, int topNum) {
    	newSentenceCount = splitSencenceList.size();
    	
    	if (index == 0) {
    		// 获取停用词
    		StopWordDao stopWordDao = new StopWordDao();
    		stopWordSet = stopWordDao.getStopWord();
    		stopWordSet.add("逾");
    		stopWordSet.add("跌");
    		stopWordSet.add("股");
    		stopWordSet.add("🇨");
    		stopWordSet.add("🇳");
    	}
    	
    	// 统计所有词语总数(含重复词语)
		System.out.println("\n开始统计所有词语并去重！ 开始时间为：" + TimeUtil.currentTime());
		statWords(splitSencenceList);
		System.out.println("共有：" + wordSum + "个词语（含重复词语）！");
		System.out.println("共有：" + wordMap.size() + "个词语（无重复词语）！");
		System.out.println("统计所有词语并去重结束！ 结束时间为：" + TimeUtil.currentTime());
		System.out.println();
		
		// 计算 TF * IDF * length
		System.out.println("开始计算 TF-IDF！ 开始时间为：" + TimeUtil.currentTime());
		List<Word> allWordList = getTF_IDF_Length(wordMap);
		System.out.println("计算 TF-IDF结束！ 结束时间为：" + TimeUtil.currentTime());
		
		// 得到 topN
		List<Word> topNWordList = getTopN(topNum, allWordList);
		
		// 第一次计算 CPMI 先初始化
		System.out.println("初始化 CPMI 开始！ 开始时间为：" + TimeUtil.currentTime());
		wordCPMI.initCPMI(topNWordList, newSentenceCount);
		System.out.println("初始化 CPMI 结束！ 结束时间为：" + TimeUtil.currentTime() + "\n");
		
		// 得到 CPMI 和权重
		topNWordList = getCPMIAndWeight(topNWordList);
     	
     	// 得到前 topNum 的热词的权重
	    return topNWordList;
	}
    
	/**
     * 得到权重为 TopN 的热词
     * @param topNWordList
     * @return
     */
    public List<Word> getWeightTopNWord(List<Word> topNWordList, int weightPercentage) {
    	List<Word> tempList = new ArrayList<Word>();
		for (Word word : topNWordList) {
			if(word.getWeight() >= topNWordList.get(0).getWeight() * weightPercentage / 100) {
				tempList.add(word);
			}
		}
    	// 得到前 topNum 的热词的 CPMI
     	topNWordList = wordCPMI.getCPMI(tempList);
     	// 重新排序
    	Collections.sort(tempList,new Comparator<Word>(){
            public int compare(Word a, Word b) {
                return (int)((b.getTf_idf_length()*b.getCpmi() - a.getTf_idf_length()*a.getCpmi()) * 1000000);
            }
        });
    	
    	System.out.println();
    	
    	System.out.println("\n选取权重大于等于总权重的百分之 " + weightPercentage + " 的热词，总共有：" + tempList.size() + " 个热词！");
    	System.out.println("热词排序为：");
    	System.out.println("===========================================================================");
    	for (Word word : tempList) {
    		word.setWeight(word.getTf_idf_length() * word.getCpmi());
			System.out.println(word.getName() + " --------- CMPI: " + word.getCpmi() + " --------- 权重为: " + word.getWeight());
		}
    	System.out.println("===========================================================================");
    	return tempList;
	}
    
    public int getNewSentenceCount() {
		return newSentenceCount;
	}
    
	public int getNewWordCount() {
		return newWordCount;
	}

	public void setNewWordCount(int newWordCount) {
		this.newWordCount = newWordCount;
	}

	public Map<String, Integer[]> getNewWordCountMap() {
		return newWordCountMap;
	}
    
	
	/**
	 * 得到 CPMI 和权重 
	 * @param topNWordList
	 * @return
	 */
    private List<Word> getCPMIAndWeight(List<Word> topNWordList) {
    	// 得到前 topNum 的热词的 CPMI
     	topNWordList = wordCPMI.getCPMI(topNWordList);
     	// 重新排序
    	Collections.sort(topNWordList,new Comparator<Word>(){
            public int compare(Word a, Word b) {
                return (int)((b.getTf_idf_length()*b.getCpmi() - a.getTf_idf_length()*a.getCpmi()) * 1000000);
            }
        });
    	
    	totalCPMI = 0.0;
    	totalWeight = 0.0;
    	double min = 0.0;
    	for (Word word : topNWordList) {
    		word.setWeight(word.getTf_idf_length() * word.getCpmi());
    		System.out.println(word.getName() + " --------- 出现次数: " + word.getTimes() + " --------- TF-IDF-Len: " + word.getTf_idf_length() + " --------- CMPI: " + word.getCpmi() + " --------- 权重为: " + word.getWeight());
			totalCPMI += word.getCpmi();
			totalWeight += word.getWeight();
			min = word.getWeight();
		}
    	if(topNWordList.size() > 0)
    		System.out.println(topNWordList.get(0).getWeight() + " / " + min + " = " + topNWordList.get(0).getWeight() / min);
    	return topNWordList;
	}
	
    /**
     * 统计此次数据流过来的词语
     * @param splitSencenceList
     */
    private void statWords(List<String[]> splitSencenceList) {
    	newWordCountMap = new HashMap<String, Integer[]>();
    	newWordCount = 0;
		for (int i=0; i<splitSencenceList.size(); i++) {
			String[] words = splitSencenceList.get(i);
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
			        
			        if (!newWordCountMap.containsKey(wordstring)) {
			        	Integer[] intArr = new Integer[3];
			        	intArr[0] = 1;
			        	intArr[1] = 1;
			        	intArr[2] = i;
			        	newWordCountMap.put(wordstring, intArr);
			        } else {
			        	Integer[] intArr = newWordCountMap.get(wordstring);
			        	if(intArr[2] == i) {
			        		intArr[0] ++;
			        	} else {
			        		intArr[0] ++;
			        		intArr[1] ++;
			        		intArr[2] = i;
			        	}
			        	newWordCountMap.put(wordstring, intArr);
			        }
			        newWordCount++;
			        wordSum ++;
				} else {
					continue;
				}
			}
		}
		// index 叠加
		index += splitSencenceList.size();
    }
    

    /**
     * 修改接口，一次性计算 TF_IDF_Length
     * @param wordMap
     * @return
     */
    private List<Word> getTF_IDF_Length(Map<String, Word> wordMap) {
    	List<Word> WordList = new ArrayList<Word>();
    	
    	Iterator<Entry<String, Word>> iter = wordMap.entrySet().iterator();
    	while (iter.hasNext()) {
    		Map.Entry<String, Word> entry = iter.next();
    		Word word = entry.getValue();
    		// docCount 为包含该热词的文档总数
    		int docCount = word.sentenceList.size();
    		
    		// 计算总文档数的开方,用于保证所选择的热词出现在的文档数大于等于总文档数的开方
    		if(docCount >= (int) Math.sqrt(index)) {
    			// 计算词频 TF
    		    double tf = word.getTimes() / (double)wordSum;
    		    // 计算 IDF 
                double idf = Math.log((double)index / (double)(docCount+1));
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
     * 得到前 TopN 的热词
     * @param topNum
     * @param wordList
     * @return
     */
    private List<Word> getTopN(int topNum, List<Word> wordList) {
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
        		topNum1 ++;
			}
			if (topNum1 == topNum) {
				break;
			}
        }
 		System.out.println("共选出: " + topNum1 + "个热词！\n");
 		
        return wordListTopN;
    }
}
