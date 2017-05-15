package com.socialheat;

import com.socialheat.analysis.DataStat;
import com.socialheat.dao.ProjectDao;

public class Main {

	public static void main(String[] args) throws Exception {
//		  System.out.println("统计开始！ 开始时间为：" + TimeUtil.currentTime() + "\n");
		
		DataStat dataStat = new DataStat();
		
		ProjectDao dao = new ProjectDao("gsdd_weibo", true);
		int topNum = 1000;
		int span = 60 * 24;
		
        dataStat.analysis(dao, topNum, span);
        
//        System.out.println("统计结束！ 结束时间为：" + TimeUtil.currentTime());
	}
}
