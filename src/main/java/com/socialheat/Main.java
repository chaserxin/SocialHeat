package com.socialheat;

import com.socialheat.analysis.DataStat;
import com.socialheat.dao.DaoInterface;
import com.socialheat.dao.DfzxBaiduDao;
import com.socialheat.dao.GsddBaiduDao;
import com.socialheat.util.TimeUtil;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println("统计开始！ 开始时间为：" + TimeUtil.currentTime() + "\n");
		DataStat dataStat = new DataStat();
		
//		// USee
//		DaoInterface dao = new USeeDao();
//		int topNum = 60;
//		int span = 10;
		
//		// DFZX_Baidu
//		DaoInterface dao = new DfzxBaiduDao();
//		int topNum = 200;
//		int span = 60 * 24;
//		int loopNum = 30;
		
//		// DFZX_Weibo
//		DaoInterface dao = new DfzxWeiboDao();
//		int topNum = 200;
//		int span = 10;
		
		// DFZX_Baidu
		DaoInterface dao = new GsddBaiduDao();
		int topNum = 200;
		int span = 60 * 24;
		int loopNum = 28;
		
//		// Nanhai_Weibo
//		DaoInterface dao = new NanhaiWeiboDao();
//		int topNum = 200; 
//		int span = 60 * 24;
//		int loopNum = 9;
		
//		// Pokemon_Weibo
//		DaoInterface dao = new PokemonWeiboDao();
//		int topNum = 200;
//		int span = 60 * 24;
		
//		// Pokemon_Weibo
//		DaoInterface dao = new NanhaiWeiboDao();
//		int topNum = 200;
//		int span = 60 * 24;
		
        dataStat.analysis(dao, topNum, span, loopNum);
        
        System.out.println("统计结束！ 结束时间为：" + TimeUtil.currentTime());
	}
}
