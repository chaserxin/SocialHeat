package com.socialheat.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.socialheat.wordsplit.WordSplit;
import com.socialheat.wordsplit.WordSplitAnsj_seg;

public class Lagou implements DaoInterface {
	


    public List<String> getSentenceList(){
        List<String> result = new ArrayList<String>();


        return result;
    }
    
    public String getName() {
    	return "Lagou";
    }


	public List<String> getSentenceListByStream(int start) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<List<String>> getSplitSentenceList(WordSplit wordSplit) {
		List<List<String>> resultList = new ArrayList<List<String>>();
		
		File file = new File("F://拉勾网爬虫//spider.txt");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 0;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
            	resultList.add(wordSplit.splitSencence(tempString));
                line++;
            }
            System.out.println(line);
            reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	public static void main(String[] args) {
		WordSplit wordSplit = new WordSplitAnsj_seg();
		Lagou lagou = new Lagou();
		List<List<String>> splitSencenceList =lagou.getSplitSentenceList(wordSplit);
    	
		// 获取停用词
		StopWordDao stopWordDao = new StopWordDao();
		Set<String> stopWordSet = stopWordDao.getStopWord();
		stopWordSet.add("股");
		stopWordSet.add(" ");
		stopWordSet.add("1.");
		stopWordSet.add("2.");
		stopWordSet.add("4.");
		stopWordSet.add("5.");
		stopWordSet.add("6.");
		stopWordSet.add("7.");
		stopWordSet.add("3.");
		stopWordSet.add("描述");
		stopWordSet.add("职位");
		stopWordSet.add("工作");
		stopWordSet.add("相关");
		stopWordSet.add("职责");
		stopWordSet.add("岗位");
		stopWordSet.add("优先");
		
		Map<String, Integer> wordMap = new HashMap<String, Integer>();
		int index = 0;
		for (int i=0; i<splitSencenceList.size(); i++) {
			List<String> words = splitSencenceList.get(i);
			
//			List<String> words_  = new ArrayList<String>();
//			for(String wordstring : words){
//				// 过滤掉停用词和空格
//				if (!stopWordSet.contains(wordstring) && !wordstring.equals(" ")) {
//			        if (!wordMap.containsKey(wordstring)) {
//			            wordMap.put(wordstring, 1);
//			        } else if (!words_.contains(wordstring)) {
//			        	wordMap.put(wordstring, wordMap.get(wordstring)+1);
//			        } else {
//			        	continue;
//			        }
//			        words_.add(wordstring);
//				} else {
//					continue;
//				}
//			}
			
			for(String wordstring : words){
				wordstring = wordstring.toLowerCase();
				// 过滤掉停用词和空格
				if (!stopWordSet.contains(wordstring) && !wordstring.equals(" ")) {
			        if (!wordMap.containsKey(wordstring)) {
			            wordMap.put(wordstring, 1);
			        } else {
			        	wordMap.put(wordstring, wordMap.get(wordstring)+1);
			        }
				} else {
					continue;
				}
			}
			
		}
		
		List<Map.Entry<String, Integer>> temp =
		        new ArrayList<Map.Entry<String, Integer>>(wordMap.entrySet());
		// List 中所有词语按照次数从大到小排序
		Collections.sort(temp, new Comparator<Map.Entry<String, Integer>>() {
		    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
		        return (o2.getValue() - o1.getValue());
		    }
		});
		System.out.println("共分出 " + temp.size() + "个词语！");
		for (int i=0; i<temp.size(); i++) {
			String word = temp.get(i).getKey();
			int count = temp.get(i).getValue();
			if (count >= 1000) {
				System.out.println(word + " : " + count);
			}
		}
		
	}


	public long getStartTime() {
		// TODO Auto-generated method stub
		return 0;
	}
}
