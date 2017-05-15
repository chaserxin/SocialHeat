package com.socialheat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.socialheat.analysis.TopNWord;
import com.socialheat.bean.Word;
import com.socialheat.wordsplit.WordSplit;
import com.socialheat.wordsplit.WordSplitAnsj_seg;

/**
 * 得到哔哩哔哩视频 弹幕和评论的关键词
 * 得到直播弹幕和QQ群数据的关键词
 * 
 * 分析每个视频的平均弹幕长度和平均评论长度
 * @author lhx
 *
 */
public class Analyze0 {
	
	public static WordSplit wordSplit = new WordSplitAnsj_seg();
	
	public static void main(String[] args) throws Exception {
		
		/**
		 * 获取关键词
		 */
		TopNWord topNWord = new TopNWord(41);
		// 得到此段时间内的所有句子
    	List<String[]> splitSencenceList = getSplitWordList();
	    // 获取 TopN 热词
	    List<Word> wordList = topNWord.getWordTopN(splitSencenceList, 41);
	    System.out.println();
	    for (Word word : wordList) {
			System.out.println(word.getName());
		}
		
		
		/**
		 * 平均长度
		 */
//		List<String> textList = getQQCommmontList();
//		int totalLength = 0;
//		for (String string : textList) {
//			totalLength += string.length();
//		}
//		System.out.println(totalLength / (double)textList.size());
		
	}
	
	/**
	 * 将得到的数据进行分词
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> getSplitWordList() throws Exception{
		
		List<String> textList = getBilibiliDanmuList();
		
		List<String[]> splitSencenceList = new ArrayList<String[]>();
		for (String strings : textList) {
			List<String> wordList = wordSplit.splitSencence(strings);
			String[] tempArr = new String[wordList.size()];
		    for (int i = 0; i < tempArr.length; i++) {
			    tempArr[i] = wordList.get(i);
		    }
		    splitSencenceList.add(tempArr);
		}
        return splitSencenceList;
	}
	
	public static List<String> getBilibiliDanmuList() throws Exception{
		List<String> danmuList = new ArrayList<String>();
		String pattern = ">(.*)<";
		Pattern p = Pattern.compile(pattern);
		
		BufferedReader reader = new BufferedReader(new FileReader("D:\\社交热度数据\\弹幕与评论的社交热度对比\\bilibili视频与评论\\barrage\\舌尖上的中国第一集.txt"));
        String line = reader.readLine();
        while(line != null) {
        	Matcher matcher = p.matcher(line);
        	matcher.find();
        	String str = matcher.group().replace(">", "").replace("<", "");
        	danmuList.add(str);
        	line = reader.readLine();
        }
        reader.close();
        return danmuList;
	}
	
	
	public static List<String> getBilibiliCommentList() throws Exception{
		List<String> commentList = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader("D:\\社交热度数据\\弹幕与评论的社交热度对比\\bilibili视频与评论\\comment\\9870707.txt"));
        String line = reader.readLine();
        while(line != null) {
        	commentList.add(line);
		    line = reader.readLine();
        }
        reader.close();
        return commentList;
	}
	
	public static List<String> getZhiboDanmuList() throws Exception{
		List<String> danmuList = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader("D:\\社交热度数据\\弹幕与评论的社交热度对比\\直播弹幕与qq群\\直播数据过滤赠送\\德云色直播.txt"));
        String line = reader.readLine();
        while(line != null) {
        	if(line.contains(": ")) {
        		String text = line.split(": ")[1];
        		danmuList.add(text);
        	}
		    line = reader.readLine();
        }
        reader.close();
        return danmuList;
	}
	
	public static List<String> getQQCommmontList() throws Exception{
		List<String> commentList = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader("D:\\社交热度数据\\弹幕与评论的社交热度对比\\直播弹幕与qq群\\直播数据过滤赠送\\55开直播.txt"));
        String line = reader.readLine();
        while(line != null) {
        	line = reader.readLine();
        	commentList.add(line);
		    line = reader.readLine();
		    line = reader.readLine();
        }
        reader.close();
        return commentList;
	}
}
