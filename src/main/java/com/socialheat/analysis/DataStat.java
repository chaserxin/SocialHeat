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
        TopNWord topNWord = new TopNWord();
        WordCPMI wordCPMI = new WordCPMI(topNum);
        List<Rate> rateList = new ArrayList<Rate>();
        
        for (int i=1; i<=loopNum; i++) {
        	System.out.println("****************************************************************************************");
        	System.out.println("*************************************** 第 " + i + "次循环 " + "*****************************************");
        	System.out.println("****************************************************************************************");
        	
        	int tempTopN = topNum;
        	// 得到此段时间内的所有句子
        	List<String[]> splitSencenceList =  dao.getSplitSentenceListByStream(span);
		    // 获取 TopN 热词
		    List<Word> topNWordList = topNWord.getWordTopN(splitSencenceList, tempTopN);
		    // 第一次计算 CPMI 先初始化
		    wordCPMI.initCPMI(topNWordList, topNWord.sentenceCount);
			
	    	// 获取以 span 分钟为间隔的并且在每个 span 分钟内的所有弹幕的 List
		    Rate tempRate = new Rate(dao.getStartTime() - span * 60, dao.getStartTime());
		    // 统计此段时间内的所有词语出现的次数
		    tempRate.setWordCountMap(topNWord.wordCountMap);
		    tempRate.setSentenceCount(topNWord.sentenceCount);
		    rateList.add(tempRate);
		    
		    System.out.println("开始Rate循环！ 开始时间为：" + TimeUtil.currentTime());
		    if (tempTopN > topNWordList.size()) {
		    	tempTopN = topNWordList.size();
	        }
		    // 此循环为筛选
		    for (int n=tempTopN; n>=tempTopN*80/100; n=n-20) {
		        System.out.println("\n选取前 " + n + " 个热词！");
		    	// 得到前 tempTopN 的热词的 CPMI
		     	topNWordList = wordCPMI.getCPMI(topNWordList.subList(0, n));
		     	for(Rate rate : rateList){
		     		EventPopularity eventPopularity = new EventPopularity();
		            rate.setRate(eventPopularity.getRate(topNWordList, rate.getWordCountMap(), rate.getSentenceCount()));
		        }
		     	if (n == tempTopN || n == topNWordList.size()) {
		     		SaveFileUtil.writeWord("E:/SocialHeatPaper/SocialHeat/result/" + dao.getName() + "_Top" + n + "_Word.txt", topNWordList);
		     	}
		     	SaveFileUtil.writeRate("E:/SocialHeatPaper/SocialHeat/result/" + dao.getName() + "_Top" + n + "_Rate.txt", rateList);
		    }
		    System.out.println("Rate循环结束！ 结束时间为：" + TimeUtil.currentTime());
		    System.out.println();
	    }
        
       return ;
    }
}
