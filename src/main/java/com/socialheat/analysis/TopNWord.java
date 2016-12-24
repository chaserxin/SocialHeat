package com.socialheat.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.socialheat.bean.Word;
import com.socialheat.dao.StopWordDao;


public class TopNWord {
	
    /**
     * 
     * @param wordList 已分好的词语，尚未进行停词表处理和统计
     * @param sentenceList 所有的句子列表
     * @param topNum
     * @return
     * @throws IOException
     */
    public List<Word> getWordTopN(List<String> wordList, List<String> sentenceList, int topNum) {
    	List<Word> topNWordList = new ArrayList<Word>();
    	
		// 获取停用词
		StopWordDao stopWordDao = new StopWordDao();
		Set<String> stopWordSet = stopWordDao.getStopWord();
		
		// 统计分词好的结果中每次词语出现的次数
		Map<String,Integer> wordCountMap = new HashMap<String, Integer>();
		for(String word : wordList){
			// 过滤掉停用词和空格
			if (!stopWordSet.contains(word) && !word.equals(" ")) {
		        if (!wordCountMap.containsKey(word)) {
		            wordCountMap.put(word, 1);
		        } else {
		            wordCountMap.put(word, wordCountMap.get(word) + 1);
		        }
			}
		}
		
		// 统计 TF (词频)
		List<Word> TFWordList = new ArrayList<Word>();
		TFWordList = TopNWord.getTF(wordCountMap);

		// 统计 IDF 并且 计算 TF * IDF * length 的值
		List<Word> allWordList = new ArrayList<Word>();
		allWordList = TopNWord.getTF_IDF_length(TFWordList, sentenceList);
		
		// 得到 topN
		topNWordList = TopNWord.getTopN(topNum, allWordList, "TIL");
	    return topNWordList;
	}

    /**
     * 得到每次词的 TF
     * @param wordCountMap
     * @return
     */
    public static List<Word> getTF(Map<String,Integer> wordCountMap) {
    	List<Word> TFWordList = new ArrayList<Word>();
    	
		// 获取所有词语个数,包含重复词语
    	double sum = 0;
		for(Map.Entry<String,Integer> it : wordCountMap.entrySet()){
		    sum += it.getValue();
		}
		System.out.println();
		System.out.println("分词结束！共分出"+(long)sum+"个词语（含重复词语）！");
		
		System.out.println("开始进行词频统计...");
		
		List<Map.Entry<String, Integer>> temp =
		        new ArrayList<Map.Entry<String, Integer>>(wordCountMap.entrySet());
		
		// List 中所有词语按照次数从大到小排序
		Collections.sort(temp, new Comparator<Map.Entry<String, Integer>>() {
		    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
		        return (o2.getValue() - o1.getValue());
		    }
		});
		
		List<String> result = new ArrayList<String>();
		
		for (int i=0; i<temp.size(); i++) {
		    String outString = temp.get(i).getKey() + "  " + String.format("%.6f", temp.get(i).getValue()/sum);
		    result.add(outString);
		
		    // 初始化 word 的词语、次数、长度
		    Word word = new Word(temp.get(i).getKey(), temp.get(i).getValue(), temp.get(i).getKey().length());
		    // 计算词频 TF
		    double tf = temp.get(i).getValue() / sum;
		    word.setTf(tf);
		    TFWordList.add(word);
		}
		System.out.println("统计结束！共统计词语" + temp.size() + "个（无重复词语）！");
		
		return TFWordList;
    }

    /**
     * 得到每次词的 TF * IDF * log(len) 的结果
     * @param TFWordList
     * @return
     */
    public static List<Word> getTF_IDF_length(List<Word> TFWordList, List<String> sentenceList) {
    	List<Word> topNWordList = new ArrayList<Word>();
    	
        // cnt_tot 为所有弹幕总数,即所有文档总数
        int cnt_tot = sentenceList.size(); 
        
        int wordEndNum = 0;
        for(Word word : TFWordList){
            if(wordEndNum > 1000)
                break;
            wordEndNum ++;
            
            // 包含改热词的弹幕个数,即包含该此的文档个数,默认值为1,则在计算 IDF 时不用再加1
            int cnt = 1;
            String wordName = word.getName();
            for(String queryWord : sentenceList){
                if(queryWord.contains(wordName)){
                    cnt ++;
                }
            }
            // 保存包含该热词的文档数 +1
            word.setTimes_sentence(cnt);

            // 计算 IDF 
            double idf = Math.log(cnt_tot/cnt);
            // 计算长度的 log
            double len = Math.log(word.getLength()) / Math.log(2);

            word.setIdf(idf);
            // 保存单独的 TF * IDF
            word.setTf_idf(word.getTf()*idf);
            // 保存 TF * IDF * log(len)
            word.setTf_idf_length(word.getTf()*idf*len);
            topNWordList.add(word);
        }
        return topNWordList;
    }

    /**
     * 得到前 TopNum 的热词
     * @param topNum
     * @param wordList
     * @param flag 如果 flag = "TIL" 得到 TF*IDF*log(len) 的前 TopNum 的热词
     * @return
     */
    public static List<Word> getTopN(int topNum , List<Word> wordList, String flag) {
        List<Word> wordListTopN = new ArrayList<Word>();

        System.out.println();
        if (flag.equals("TIL")) {
        	// 重新排序
        	Collections.sort(wordList,new Comparator<Word>(){
                public int compare(Word a, Word b) {
                    return (int)((b.getTf_idf_length() - a.getTf_idf_length()) * 1000000);
                }
            });
        } else if (flag.equals("TILC")) {
        	// 重新排序
     		Collections.sort(wordList,new Comparator<Word>(){
                 public int compare(Word a, Word b) {
                     return (int)((b.getTf_idf_length_cpmi() - a.getTf_idf_length_cpmi()) * 1000000);
                 }
             });
        } else {
        	System.out.println("=====================");
        	System.out.println("ERROR");
        	System.out.println("=====================");
        }
        
 		 for(int i=0 ; i<topNum ; i++){
 			 if (flag.equals("TIL")) {
 				System.out.println(wordList.get(i).getName()+" "+wordList.get(i).getTf_idf_length());
 			 } else if (flag.equals("TILC")) {
 				System.out.println(wordList.get(i).getName()+" "+wordList.get(i).getTf_idf_length_cpmi());
 			 } else {
 				System.out.println("=====================");
 	        	System.out.println("ERROR");
 	        	System.out.println("=====================");
 			 }
             wordListTopN.add(wordList.get(i));
         }

 		 System.out.println();
        return wordListTopN;
    }
}
