package com.socialheat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.socialheat.util.TimeUtil;

public class GsddBaiduDao implements DaoInterface {

	private long startTime;

	public GsddBaiduDao() {
		try {
			startTime = Long.parseLong(TimeUtil.date2Timestamp("2015-6-16 00:00:00"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 得到百度评论
	 * 
	 * @return
	 */
	public List<String> getSentenceList() {
		return null;
	}

	public List<String> getSentenceListByStream(int start) {
		return null;
	}

	public List<String[]> getSplitSentenceListByStream(int span) {
		List<String[]> result = new ArrayList<String[]>();

		// 获取数据库连接
		Connection conn = DaoHandler.getConnection();
		long endTime = startTime + span * 60;
		String sql = "SELECT word FROM sw_gsdd_baidu WHERE create_time BETWEEN " + startTime + " and " + endTime;
		System.out.println(sql);
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("============================");
			while (rs.next()) {
				result.add(rs.getString(1).split(","));
			}
			System.out.println("读入了" + result.size() + "条数据（百度评论）");
			System.out.println("============================");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DaoHandler.close(conn);
		}

		startTime = endTime;
		return result;
	}

	public long getStartTime() {
		return startTime;
	}

	public String getName() {
		return "GSDD_Baidu";
	}

}
