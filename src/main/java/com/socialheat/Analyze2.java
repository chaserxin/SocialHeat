package com.socialheat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.socialheat.dao.StopWordDao;
import com.socialheat.wordsplit.WordSplit;
import com.socialheat.wordsplit.WordSplitAnsj_seg;

/**
 * 统计几个权重高的主观词在每个文档中占的比重
 * @author lhx
 *
 */
public class Analyze2 {

	private static Set<String> stopWordSet = new StopWordDao().getStopWord();
	private static WordSplit wordSplit = new WordSplitAnsj_seg();

	public static void main(String[] args) throws Exception {
		getWordTopN(getSplitDanmuList("D:\\社交热度数据\\弹幕与评论的社交热度对比\\bilibili对比分析（4个）\\barrage\\6461838.txt"));
		getWordTopN(getSplitDanmuList("D:\\社交热度数据\\弹幕与评论的社交热度对比\\bilibili对比分析（4个）\\barrage\\9826480.txt"));
		getWordTopN(getSplitDanmuList("D:\\社交热度数据\\弹幕与评论的社交热度对比\\bilibili对比分析（4个）\\barrage\\9850350.txt"));
		getWordTopN(getSplitDanmuList("D:\\社交热度数据\\弹幕与评论的社交热度对比\\bilibili对比分析（4个）\\barrage\\9870707.txt"));
		
		getWordTopN(getSplitCoomentList("D:\\社交热度数据\\弹幕与评论的社交热度对比\\bilibili对比分析（4个）\\comment\\6461838.txt"));
		getWordTopN(getSplitCoomentList("D:\\社交热度数据\\弹幕与评论的社交热度对比\\bilibili对比分析（4个）\\comment\\9826480.txt"));
		getWordTopN(getSplitCoomentList("D:\\社交热度数据\\弹幕与评论的社交热度对比\\bilibili对比分析（4个）\\comment\\9850350.txt"));
		getWordTopN(getSplitCoomentList("D:\\社交热度数据\\弹幕与评论的社交热度对比\\bilibili对比分析（4个）\\comment\\9870707.txt"));
	}

	
	
	
	
	private static List<String[]> getSplitDanmuList(String filePath) throws Exception{
		List<String[]> splitSencenceList = new ArrayList<String[]>();
		String pattern = ">(.*)<";
		Pattern p = Pattern.compile(pattern);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = reader.readLine();
        while(line != null) {
        	Matcher matcher = p.matcher(line);
        	matcher.find();
        	String str = matcher.group().replace(">", "").replace("<", "");
        	List<String> wordList = wordSplit.splitSencence(str);
		    String[] tempArr = new String[wordList.size()];
		    for (int i = 0; i < tempArr.length; i++) {
			    tempArr[i] = wordList.get(i);
		    }
		    splitSencenceList.add(tempArr);
        	line = reader.readLine();
        }
        reader.close();
        return splitSencenceList;
	}
	
	private static List<String[]> getSplitCoomentList(String filePath) throws Exception{
		List<String[]> splitSencenceList = new ArrayList<String[]>();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = reader.readLine();
        while(line != null) {
        	List<String> wordList = wordSplit.splitSencence(line);
		    String[] tempArr = new String[wordList.size()];
		    for (int i = 0; i < tempArr.length; i++) {
			    tempArr[i] = wordList.get(i);
		    }
		    splitSencenceList.add(tempArr);
		    line = reader.readLine();
        }
        
        reader.close();
        return splitSencenceList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void getWordTopN(List<String[]> splitSencenceList) {
		System.out.println("-------------------------------------");
		Map<String, Integer> wordMap = new HashMap<String, Integer>();
		wordMap.put("2333", 0);
		wordMap.put("hhh", 0);
		wordMap.put("哈哈哈", 0);
		wordMap.put("666", 0);
		wordMap.put("bgm", 0);

		int wordNum = 0;
		for (int i = 0; i < splitSencenceList.size(); i++) {
			String[] words = splitSencenceList.get(i);
			for (String wordstring : words) {
				// 过滤掉停用词和空格
				if (!stopWordSet.contains(wordstring) && !wordstring.equals(" ")) {
					if (wordMap.containsKey(wordstring)) {
						wordMap.put(wordstring, wordMap.get(wordstring) + 1);
					}
					wordNum++;
				} else {
					continue;
				}
			}
		}
		System.out.println("2333的频率为：" + wordMap.get("2333") + " / " + wordNum + " = " +  (double)wordMap.get("2333")/wordNum);
		System.out.println("hhh的频率为：" + wordMap.get("hhh") + " / " + wordNum + " = " + (double)wordMap.get("hhh")/wordNum);
		System.out.println("哈哈哈的频率为：" + wordMap.get("哈哈哈") + " / " + wordNum + " = " + (double)wordMap.get("哈哈哈")/wordNum);
		System.out.println("666的频率为：" + wordMap.get("666") + " / " + wordNum + " = " + (double)wordMap.get("666")/wordNum);
		System.out.println("bgm的频率为：" + wordMap.get("bgm") + " / " + wordNum + " = " + (double)wordMap.get("bgm")/wordNum);
		System.out.println("-------------------------------------");
		return;
	}
}
