package com.socialheat;

import com.socialheat.analysis.DataStat;
import com.socialheat.dao.DaoInterface;
import com.socialheat.dao.NanhaiWeiboDao;
import com.socialheat.util.TimeUtil;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println("统计开始！ 开始时间为：" + TimeUtil.currentTime() + "\n");
		DataStat dataStat = new DataStat();
		
//		// DFZX_Baidu
//		DaoInterface dao = new DfzxBaiduDao();
//		int topNum = 200;
//		int span = 60;
//		int loopNum = 30 * 24;
		
//		// DFZX_Weibo
//		DaoInterface dao = new DfzxWeiboDao();
//		int topNum = 200;
//		int span = 60;
//		int loopNum = 30 * 24;
		
//		// GSDD_Baidu
//		DaoInterface dao = new GsddBaiduDao();
//		int topNum = 200;
//		int span = 60;
//		int loopNum = 28 * 24;
		
//		// GSDD_Weibo
//		DaoInterface dao = new GsddWeiboDao();
//		int topNum = 200;
//		int span = 60;
//		int loopNum = 28 * 24;
		
//		// Nanhai_Baidu
//		DaoInterface dao = new NanhaiBaiduDao();
//		int topNum = 200; 
//		int span = 60;
//		int loopNum = 9 * 24;
		
		// Nanhai_Weibo
		DaoInterface dao = new NanhaiWeiboDao();
		int topNum = 200; 
		int span = 60;
		int loopNum = 9 * 24;
		
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
