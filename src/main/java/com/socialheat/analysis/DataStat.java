package com.socialheat.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.socialheat.bean.Rate;
import com.socialheat.bean.Word;
import com.socialheat.dao.ProjectDao;
import com.socialheat.util.SaveFileUtil;


public class DataStat {
	
	/**
	 * 此类为项目分析起始类,按照流的形式分析数据
	 * @param dao 			需要分析的项目
	 * @param wordSplit		分词方式
	 * @param span			分析的时间间隔
	 * @throws IOException
	 */
    public void analysis(ProjectDao dao, int topNum, int span) throws IOException {
        TopNWord topNWord = new TopNWord(topNum);
        List<Rate> rateList = new ArrayList<Rate>();
        int loopNum = dao.getLoopNum(span);
        
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
		    
		    // 此循环为删词循环
		    // 存热词增加的速率 = 此次循环的热词数 / 上次循环的热次数
		    List<Double> wordAddRateList = new ArrayList<Double>();
		    // 保存总速率
		    double totalWordAddRate = 0.0;
		    // 保存平均速率
		    double averageWordAddRate = 0.0;
		    // 保存上次迭代的热词数
		    int lastWordNum = -1;
		    // 保存第一次迭代热词数超过20迭代次数
		    int firstTimes = 1;
		    // 保存最优的迭代次数
		    int bestTimes = 0;
		    // 本次迭代计算每次循环新加热词的速率
		    for (double weightPercentage=0.1; weightPercentage<=1; weightPercentage=weightPercentage+0.1) {
		         List<Word> tempTopNWordList = topNWord.getWeightTopNWord(topNWordList, weightPercentage);
		         if(lastWordNum != -1) {
	        		 double tempWordAddRate = tempTopNWordList.size() / (double)lastWordNum;
	        		 wordAddRateList.add(tempWordAddRate);
	        		 totalWordAddRate += tempWordAddRate;
		         }
	        	 lastWordNum = tempTopNWordList.size();
	        	 if(lastWordNum < 20) {
	        		 firstTimes ++;
	        	 }
		    }
		    averageWordAddRate = totalWordAddRate / wordAddRateList.size();
		    
		    
		    
		    
		    for (int j = 0; j < wordAddRateList.size(); j++) {
				System.out.print(wordAddRateList.get(j) + "---");
			}
		    System.out.print(averageWordAddRate);
		    System.out.println();
		    
		    
		    
		    
		    
		    // 本次迭代找出最优的一次迭代,规则为
		    // 1.热词总数大于20
		    // 2.在条件1满足的情况下,连续两次循环所加热词的速率大于平均速率
		    int aboveTimes = 0;
		    for (int j = 0; j < wordAddRateList.size(); j++) {
				if(wordAddRateList.get(j) > averageWordAddRate){
					aboveTimes ++;
				} else {
					aboveTimes = 0;
				}
				if(aboveTimes >= 2 && j >= firstTimes-1) {
					bestTimes = j + 1;
					break;
				}
			}
		    if(bestTimes == 0) {
		    	bestTimes = 10;
		    }
		    // 得到最优的一次迭代的热词
		    List<Word> tempTopNWordList = topNWord.getWeightTopNWord(topNWordList, bestTimes*0.1);
		    
		    
		    // 此循环为计算热度循环
		    for(Rate rate : rateList){
	     		EventPopularity eventPopularity = new EventPopularity();
	            rate.setRate(eventPopularity.getRate(tempTopNWordList, rate.getWordCountMap(), rate.getSentenceCount(), rate.getWordCount()));
	        }
     		SaveFileUtil.writeWord("E:/SocialHeatPaper/SocialHeat/result/" + dao.getName() + "_Top_Word.txt", tempTopNWordList);
	     	SaveFileUtil.writeRate("E:/SocialHeatPaper/SocialHeat/result/" + dao.getName() + "_Top_Rate.txt", rateList);
	    }
        
       return ;
    }
}
