package com.socialheat.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.socialheat.bean.Rate;
import com.socialheat.bean.Word;
import com.socialheat.dao.DaoInterface;
import com.socialheat.util.SaveFileUtil;
import com.socialheat.util.TimeUtil;
import com.socialheat.wordsplit.WordSplit;


public class DataStat {
	
	/**
	 * 此类为项目分析起始类,按照流的形式分析数据
	 * @param dao 			需要分析的项目
	 * @param wordSplit		分词方式
	 * @param span			分析的时间间隔
	 * @throws IOException
	 */
    public void analysis(DaoInterface dao, WordSplit wordSplit, int topNum, int span) throws IOException {
        TopNWord topNWord = new TopNWord();
        WordCPMI wordCPMI = new WordCPMI(topNum);
        List<Rate> rateList = new ArrayList<Rate>();
        
        for (int i=0; i<=29; i++) {
        	// 获取此段时间内的所有句子
        	List<String> sentenceList = dao.getSentenceListByStream(span);
        	// 分词
        	List<List<String>> splitSencenceList = wordSplit.splitSencenceList(sentenceList);
        	// 清理 sentenceList
        	sentenceList.clear();
		    // 获取 TopN 热词
		    List<Word> topNWordList = topNWord.getWordTopN(splitSencenceList, topNum);
		    // 第一次计算 CPMI 先初始化
		    wordCPMI.initCPMI(topNWordList, splitSencenceList.size());
			
	    	// 获取以 span 分钟为间隔的并且在每个 span 分钟内的所有弹幕的 List
		    Rate tempRate = new Rate(dao.getStartTime() - span * 60, dao.getStartTime());
		    // 统计此段时间内的所有词语出现的次数
		    tempRate.setWordCountMap(topNWord.getWordInfoMap(splitSencenceList));
		    tempRate.setSentenceCount(splitSencenceList.size());
		    rateList.add(tempRate);
		    
		    System.out.println("开始Rate循环！ 开始时间为：" + TimeUtil.currentTime());
		    // 此循环为筛选
		    for (int n=topNum; n>=topNum*10/10; n=n-40) {
		    	if (n > topNWordList.size()) {
		        	n = topNWordList.size();
		        }
		        System.out.println("\n选取前 " + n + " 个热词！");
		    	// 得到前 topNum 的热词的 CPMI
		     	List<Word> topNWordList_CPMI = wordCPMI.getCPMI(topNWordList.subList(0, n));
		     	for(Rate rate : rateList){
		     		EventPopularity eventPopularity = new EventPopularity();
		            rate.setRate(eventPopularity.getRate(topNWordList_CPMI, rate.getWordCountMap(), rate.getSentenceCount()));
		        }
		     	if (n == topNum || n == topNWordList.size()) {
		     		SaveFileUtil.writeWord("E:/SocialHeatPaper/SocialHeat/result/" + dao.getName() + "_Top" + n + "_Word.txt", topNWordList_CPMI);
		     	}
		     	SaveFileUtil.writeRate("E:/SocialHeatPaper/SocialHeat/result/" + dao.getName() + "_Top" + n + "_Rate.txt", rateList);
		    }
		    System.out.println("Rate循环结束！ 结束时间为：" + TimeUtil.currentTime());
		    System.out.println();
	    }
        
       return ;
    }
    
    
    /**
	 * 此类为项目分析起始类,一次性分析所有数据
	 * @param dao 			需要分析的项目
	 * @param wordSplit		分词方式
	 * @param span			分析的时间间隔
	 * @throws IOException
	 */
    public void analysis_all(DaoInterface dao, WordSplit wordSplit, int topNum, int span) throws IOException {
        TopNWord topNWord = new TopNWord();
        WordCPMI wordCPMI = new WordCPMI(topNum);
        
        // 获取所有句子
        List<List<String>> splitSencenceList = dao.getSplitSentenceList(wordSplit);
	    // 获取 TopN 热词
	    List<Word> topNWordList = topNWord.getWordTopN(splitSencenceList, topNum);
	    // 第一次计算 CPMI 先初始化
	    wordCPMI.initCPMI(topNWordList, splitSencenceList.size());
			
	    List<Rate> rateList = new ArrayList<Rate>();
	    for (int i=0; i<=29; i++) {
//	    	rateList = dao.getSentenceListByTime(span);
	    }
	    
	    System.out.println("准备进入Rate循环...");
	    int span_ = 40;
	    for (int n=topNum; n>=topNum*3/10; n=n-span_) {
	    	if (n > topNWordList.size()) {
	        	n = topNWordList.size();
	        }
	        System.out.println("\n选取前 " + n + " 个热词！");
	    	// 得到前 topNum 的热词的 CPMI
	    	List<Word> tempList = topNWordList.subList(0, n);
	     	List<Word> topN_CPMI_WordList = wordCPMI.getCPMI(tempList);
	     	for(Rate rate : rateList){
	     		EventPopularity eventPopularity = new EventPopularity();
//	            rate.setHotWordList(eventPopularity.getHotWordList(topN_CPMI_WordList, rate.getSplitSentenceList()));
//	            rate.setRate(eventPopularity.getRate_v2(topN_CPMI_WordList, rate.getHotWordList()));
	        }
	     	if (n == topNum || n == topNWordList.size()) {
	     		SaveFileUtil.writeWord("E:/SocialHeatPaper/SocialHeat/result/" + dao.getName() + "_Top" + n + "_Word.txt", topN_CPMI_WordList);
	     	}
	     	SaveFileUtil.writeRate("E:/SocialHeatPaper/SocialHeat/result/" + dao.getName() + "_Top" + n + "_Rate.txt", rateList);
	    }
	    System.out.println();
       return ;
    }
    
}
