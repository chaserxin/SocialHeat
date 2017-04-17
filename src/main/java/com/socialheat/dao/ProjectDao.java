package com.socialheat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.socialheat.util.ProjectInfo;
import com.socialheat.util.TimeUtil;
import com.socialheat.wordsplit.WordSplit;
import com.socialheat.wordsplit.WordSplitAnsj_seg;

public class ProjectDao {
	private String name;
	private long startTime;
	private long endTime;
	private Boolean isSpiltWord;
	private WordSplit wordSplit;
	
	public ProjectDao(String projectName, Boolean isSpiltWord) {
		String[] nameArr = projectName.split("_");
		ProjectInfo projectInfo = new ProjectInfo(nameArr[0]);
		name = projectInfo.getName() + "_" + nameArr[1];
		try {
			startTime = Long.parseLong(TimeUtil.date2Timestamp(projectInfo.getStartTime()));
			endTime = Long.parseLong(TimeUtil.date2Timestamp(projectInfo.getEndTime()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		this.isSpiltWord = isSpiltWord;
		if(!isSpiltWord){
			wordSplit = new WordSplitAnsj_seg();
		}
	}

	public List<String[]> getSplitSentenceListByStream(int span) {
		List<String[]> result = new ArrayList<String[]>();
		
		 // 获取数据库连接
        Connection conn = DaoHandler.getConnection();
		long endTime = startTime + span * 60;
		// 判断此项目是否已经分词
		if(isSpiltWord) {
			String sql = "SELECT word FROM sw_" + name + " WHERE create_time BETWEEN " + startTime + " and " + endTime;
	        System.out.println(sql);
	        PreparedStatement pstmt;
	        try {
	            pstmt = conn.prepareStatement(sql);
	            ResultSet rs = pstmt.executeQuery();
	            System.out.println("============================");
	            while (rs.next()) {
	                result.add(rs.getString(1).split(","));
	            }
	            System.out.println("读入了"+result.size()+"条数据");
	            System.out.println("============================");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	        	DaoHandler.close(conn);
			}
		} else {
			String sql = "SELECT text FROM " + name + " WHERE create_time BETWEEN " + startTime + " and " + endTime;
	        System.out.println(sql);
	        PreparedStatement pstmt;
	        try {
	            pstmt = conn.prepareStatement(sql);
	            ResultSet rs = pstmt.executeQuery();
	            System.out.println("============================");
	            while (rs.next()) {
	         	   List<String> wordList = wordSplit.splitSencence(rs.getString(1));
	         	   String[] tempArr = new String[wordList.size()];
	         	   for (int i = 0; i < tempArr.length; i++) {
	         		   tempArr[i] = wordList.get(i);
	         	   }
	                result.add(tempArr);
	            }
	            System.out.println("读入了"+result.size()+"条数据");
	            System.out.println("============================");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	        	DaoHandler.close(conn);
			}
		}
        startTime = endTime;
		return result;
	}

	public String getName() {
		return name;
	}

	public long getStartTime() {
		return startTime;
	}
	
	/**
	 * 获得循环次数,计算规则为：
	 * 1.事件发生的总秒数 = 事件结束时间 - 事件开始时间
	 * 2.loopNum = 事件发生的总秒数 / (span * 60)
	 * @param span
	 * @return
	 */
	public int getLoopNum(int span){
		return (int) ((endTime - startTime) / (span * 60));
	}
}
