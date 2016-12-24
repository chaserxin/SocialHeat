package com.socialheat.analysis;

import java.io.IOException;
import java.util.List;

import com.socialheat.bean.Rate;
import com.socialheat.bean.Word;
import com.socialheat.dao.DaoInterface;
import com.socialheat.util.SaveFileUtil;
import com.socialheat.wordsplit.WordSplit;


public class DataStat {

	/**
	 * 此类为项目分析起始类
	 * @param dao 			需要分析的项目
	 * @param wordSplit		分词方式
	 * @param span			分析的时间间隔
	 * @throws IOException
	 */
    public static void analysis(DaoInterface dao, WordSplit wordSplit, int topNum, int span) throws IOException {
        TopNWord topNWord = new TopNWord();
        EventPopularity eventPopularity = new EventPopularity();
        
        // 获取所有句子
        List<String> SentenceList = dao.getSentenceList();
        // 将所有句子整体分词
        List<String> wordSplitResults = wordSplit.run(SentenceList);
        // 获取 TopN 热词
        List<Word> topNWordList = topNWord.getWordTopN(wordSplitResults, SentenceList, topNum);

        // 第一次计算 CPMI 先初始化
		List<List<String>> splitSencenceList = wordSplit.splitSencence(SentenceList);
		WordCPMI.initCPMI(topNWordList, splitSencenceList, 0);
		
        // 获取以 10 分钟为间隔的并且在每个 10 分钟内的所有弹幕的 List
        List<Rate> rateList = dao.getSentenceListByTime(span);
        
        System.out.println("准备进入Rate循环...");
        int span_ = topNum / 5;
        for (int n=topNum; n>=topNum*6/10; n=n-span_) {
        	System.out.println("++++++++++++++++++++++++++++++++++++");
            System.out.println("选取前 " + n + " 个热词！");
            
        	 // 得到前 topNum 的热词加上 CPMI 的结果
        	List<Word> tempList = topNWordList.subList(0, n);
         	List<Word> topN_CPMI_WordList = WordCPMI.getCPMI(tempList);
         	for(Rate rate : rateList){
                List<String> wordSplit_results = wordSplit.run(rate.getStrings());
                rate.setRate(eventPopularity.getRate_v2(topN_CPMI_WordList, wordSplit_results, rate.getStrings()));
            }
         	if (n == topNum) {
         		SaveFileUtil.writeWord("E:/SocialHeatPaper/SocialHeat/result/" + dao.getName() + "_Top" + n + "_Word.txt", topN_CPMI_WordList);
         	}
         	SaveFileUtil.writeRate("E:/SocialHeatPaper/SocialHeat/result/" + dao.getName() + "_Top" + n + "_Rate.txt", rateList);
        }
        System.out.println("++++++++++++++++++++++++++++++++++++");
        System.out.println();
       return ;
    }


}
