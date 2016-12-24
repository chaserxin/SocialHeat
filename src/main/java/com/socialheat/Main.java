package com.socialheat;

import com.socialheat.analysis.DataStat;
import com.socialheat.dao.DaoInterface;
import com.socialheat.dao.DfzxBaiduDao;
import com.socialheat.util.TimeUtil;
import com.socialheat.wordsplit.WordSplit;
import com.socialheat.wordsplit.WordSplitAnsj_seg;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println("统计开始！ 开始时间为：" + TimeUtil.currentTime() + "\n");
		
//		// USee
//		DaoInterface dao = new USeeDao();
//		WordSplit wordSplit = new WordSplitAnsj_seg();
//		int topNum = 200;
//		int span = 10;
		
		// DFZX_Baidu
		DaoInterface dao = new DfzxBaiduDao();
		WordSplit wordSplit = new WordSplitAnsj_seg();
		int topNum = 100;
		int span = 60 * 24;
		
        DataStat.analysis(dao, wordSplit, topNum, span);
        
        System.out.println("统计结束！ 结束时间为：" + TimeUtil.currentTime());
	}
}
