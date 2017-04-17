package com.socialheat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.socialheat.analysis.TopNWord;
import com.socialheat.wordsplit.WordSplit;
import com.socialheat.wordsplit.WordSplitAnsj_seg;

public class AnalyzeBilibili {
	
	private static WordSplit wordSplit = new WordSplitAnsj_seg();
	
	public static void main(String[] args) throws Exception {
		TopNWord topNWord = new TopNWord(20);
		// 得到此段时间内的所有句子
    	List<String[]> splitSencenceList = getSplitCoomentList();
	    // 获取 TopN 热词
	    topNWord.getWordTopN(splitSencenceList, 20);
	}
	
	private static List<String[]> getSplitDanmuList() throws Exception{
		List<String[]> splitSencenceList = new ArrayList<String[]>();
		
		String pattern = ">(.*)<";
		Pattern p = Pattern.compile(pattern);
		
		BufferedReader reader = new BufferedReader(new FileReader("F:\\bilibili弹幕与评论对比\\danmu\\bilibili这是我听过最刺激的《痒》了！.txt"));
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
	
	private static List<String[]> getSplitCoomentList() throws Exception{
		List<String[]> splitSencenceList = new ArrayList<String[]>();
		BufferedReader reader = new BufferedReader(new FileReader("F:\\bilibili弹幕与评论对比\\comment\\bilibili这是我听过最刺激的《痒》了！.txt"));
        String line = reader.readLine();
        line = reader.readLine();
        line = reader.readLine();
        while(line != null) {
        	String str = line.replace("评论：", "");
        	List<String> wordList = wordSplit.splitSencence(str);
		    String[] tempArr = new String[wordList.size()];
		    for (int i = 0; i < tempArr.length; i++) {
			    tempArr[i] = wordList.get(i);
		    }
		    splitSencenceList.add(tempArr);
        	line = reader.readLine();
        	line = reader.readLine();
        	line = reader.readLine();
        }
        
        reader.close();
        
        return splitSencenceList;
	}
}
