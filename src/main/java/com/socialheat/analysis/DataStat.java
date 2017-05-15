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
	 * 
	 * @param dao 需要分析的项目
	 * @param wordSplit 分词方式
	 * @param span 分析的时间间隔
	 * @throws IOException
	 */
	public void analysis(ProjectDao dao, int topNum, int span) throws IOException {
		TopNWord topNWord = new TopNWord(topNum);
		List<Rate> rateList = new ArrayList<Rate>();
		int loopNum = dao.getLoopNum(span);

		for (int i = 1; i <= loopNum; i++) {
			System.out.println(
					"****************************************************************************************");
			System.out.println("*************************************** 第 " + i + "次循环 "
					+ "*****************************************");
			System.out.println(
					"****************************************************************************************");

			// 得到此段时间内的所有句子
			List<String[]> splitSencenceList = dao.getSplitSentenceListByStream(span);
			// 获取 TopN 热词
			List<Word> hotWordList = topNWord.getWordTopN(splitSencenceList, topNum);

			// 获取以 span 分钟为间隔的并且在每个 span 分钟内的所有弹幕的 List
			Rate tempRate = new Rate(dao.getStartTime() - span * 60, dao.getStartTime());
			// 统计此段时间内的所有词语出现的次数
			tempRate.setWordCountMap(topNWord.getNewWordCountMap());
			tempRate.setSentenceCount(topNWord.getNewSentenceCount());
			tempRate.setWordCount(topNWord.getNewWordCount());
			rateList.add(tempRate);
			// 此循环为计算热度循环
			for (Rate rate : rateList) {
				EventPopularity eventPopularity = new EventPopularity();
				rate.setRate(eventPopularity.getRate(hotWordList, rate.getWordCountMap(), rate.getSentenceCount(),
						rate.getWordCount()));
			}
			SaveFileUtil.writeWord("E:/SocialHeatPaper/SocialHeat/result/" + dao.getName() + "_Top_Word.txt",
					hotWordList);
			SaveFileUtil.writeRate("E:/SocialHeatPaper/SocialHeat/result/" + dao.getName() + "_Top_Rate.txt", rateList);
		}

		return;
	}
}
