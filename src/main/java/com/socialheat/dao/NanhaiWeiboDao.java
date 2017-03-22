package com.socialheat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.socialheat.util.TimeUtil;

public class NanhaiWeiboDao implements DaoInterface {

	private long startTime;
	
	public NanhaiWeiboDao() {
		try {
			startTime = Long.parseLong(TimeUtil.date2Timestamp("2016-7-10 00:00:00"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public List<String> getSentenceList() {
		return null;
	} 

	public List<String> getSentenceListByStream(int span) {
		List<String> result = new ArrayList<String>();

        // 获取数据库连接
        Connection conn = DaoHandler.getConnection();

        long endTime = startTime + span * 60;
        
        String sql = "SELECT text FROM nanhai_weibo WHERE create_time BETWEEN " + startTime + " and " + endTime;
        System.out.println(sql);
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("============================");
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            System.out.println("读入了"+result.size()+"条数据（微博评论）");
            System.out.println("============================");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	DaoHandler.close(conn);
		}
        
        startTime = endTime;
        return result;
	}

	public List<String[]> getSplitSentenceListByStream(int span) {
		List<String[]> result = new ArrayList<String[]>();
		
		 // 获取数据库连接
        Connection conn = DaoHandler.getConnection();
		long endTime = startTime + span * 60;
        String sql = "SELECT word FROM sw_nanhai_weibo WHERE create_time BETWEEN " + startTime + " and " + endTime;
        System.out.println(sql);
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("============================");
            while (rs.next()) {
                result.add(rs.getString(1).split(","));
            }
            System.out.println("读入了"+result.size()+"条数据（微博评论）");
            System.out.println("============================");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	DaoHandler.close(conn);
		}
        
        startTime = endTime;
		return result;
	}

	public String getName() {
		return "nanhai_weibo";
	}

	public long getStartTime() {
		return startTime;
	}

}
