package com.socialheat.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.socialheat.bean.Rate;
import com.socialheat.bean.Word;
import com.socialheat.dao.DaoInterface;
import com.socialheat.util.SaveFileUtil;
import com.socialheat.util.TimeUtil;


public class DataStat {
	
	/**
	 * 此类为项目分析起始类,按照流的形式分析数据
	 * @param dao 			需要分析的项目
	 * @param wordSplit		分词方式
	 * @param span			分析的时间间隔
	 * @throws IOException
	 */
    public void analysis(DaoInterface dao, int topNum, int span, int loopNum) throws IOException {
        TopNWord topNWord = new TopNWord(topNum);
        List<Rate> rateList = new ArrayList<Rate>();
        
        for (int i=1; i<=loopNum; i++) {
        	System.out.println("****************************************************************************************");
        	System.out.println("*************************************** 第 " + i + "次循环 " + "*****************************************");
        	System.out.println("****************************************************************************************");
        	
        	// 得到此段时间内的所有句子
        	List<String[]> splitSencenceList =  dao.getSplitSentenceListByStream(span);
		    // 获取 TopN 热词
		    List<Word> topNWordList = topNWord.getWordTopN(splitSencenceList, topNum);
		    
	    	// 获取以 span 分钟为间隔的并且在每个 span 分钟内的所有弹幕的 List
		    Rate tempRate = new Rate(dao.getStartTime() - span * 60, dao.getStartTime());
		    // 统计此段时间内的所有词语出现的次数
		    tempRate.setWordCountMap(topNWord.getNewWordCountMap());
		    tempRate.setSentenceCount(topNWord.getNewSentenceCount());
		    tempRate.setWordCount(topNWord.getNewWordCount());
		    rateList.add(tempRate);
		    
		    System.out.println("开始Rate循环！ 开始时间为：" + TimeUtil.currentTime());
		    // 此循环为筛选
		    for (int weightPercentage=1; weightPercentage<=10; weightPercentage=weightPercentage+3) {
		        List<Word> tempTopNWordList = new ArrayList<Word>();
		        tempTopNWordList = topNWord.getWeightTopNWord(topNWordList, weightPercentage);
		        
		     	for(Rate rate : rateList){
		     		EventPopularity eventPopularity = new EventPopularity();
		            rate.setRate(eventPopularity.getRate(tempTopNWordList, rate.getWordCountMap(), rate.getSentenceCount(), rate.getWordCount()));
		        }
	     		SaveFileUtil.writeWord("E:/SocialHeatPaper/SocialHeat/result/" + dao.getName() + "_Top_Word.txt", tempTopNWordList);
		     	SaveFileUtil.writeRate("E:/SocialHeatPaper/SocialHeat/result/" + dao.getName() + "_Top_Rate.txt", rateList);
		    }
		    System.out.println("Rate循环结束！ 结束时间为：" + TimeUtil.currentTime());
		    System.out.println();
	    }
        
       return ;
    }
}
