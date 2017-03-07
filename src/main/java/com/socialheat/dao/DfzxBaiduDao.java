package com.socialheat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.socialheat.util.TimeUtil;
import com.socialheat.wordsplit.WordSplit;

public class DfzxBaiduDao implements DaoInterface {

	private DaoHandler daoHandler;
	
	private long startTime;
	
	public DfzxBaiduDao() {
		daoHandler = new DaoHandler();
		try {
			startTime = Long.parseLong(TimeUtil.date2Timestamp("2015-06-01 00:00:00"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
     * 得到百度评论
     * @return
     */
    public List<String> getSentenceList(){
        List<String> result = new ArrayList<String>();

        // 获取数据库连接
        Connection conn = daoHandler.getConnection();

        //  LIMIT 0, 300000
        String sql = "SELECT text FROM dfzx_query ORDER BY create_time";
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("============================");
            while (rs.next()) {
                result.add(rs.getString(1));
            }

            System.out.println("读入了"+result.size()+"条数据（百度评论）");
            System.out.println("============================");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	daoHandler.close(conn);
		}

        return result;
    }

	public String getName() {
		return "DFZX_Baidu";
	}

	public long getStartTime() {
		return startTime;
	}
	
	/**
	 * 一天一天的获取评论
	 */
	public List<String> getSentenceListByStream(int span) {
		List<String> result = new ArrayList<String>();

        // 获取数据库连接
        Connection conn = daoHandler.getConnection();

        long endTime = startTime + span * 60;
        String sql = "SELECT text FROM dfzx_query WHERE create_time > '" + TimeUtil.timestamp2Date(startTime) + "' AND create_time < '" + TimeUtil.timestamp2Date(endTime) + "' ORDER BY create_time";
        System.out.println(sql);
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("============================");
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            System.out.println("读入了"+result.size()+"条数据（百度评论）");
            System.out.println("============================");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	daoHandler.close(conn);
		}
        
        startTime = endTime;
        return result;
	}

	public List<List<String>> getSplitSentenceList(WordSplit wordSplit) {
		System.out.println("开始获取所有数据以及分词！ 开始时间为：" + TimeUtil.currentTime());
		
		List<List<String>> result = new ArrayList<List<String>>();

        // 获取数据库连接
        Connection conn = daoHandler.getConnection();

        //  LIMIT 0, 300000
        String sql = "SELECT text FROM dfzx_query ORDER BY create_time";
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("============================");
            while (rs.next()) {
            	List<String> wordList = wordSplit.splitSencence(rs.getString(1));
		    	result.add(wordList);
            }

            System.out.println("读入了"+result.size()+"条数据（百度评论）");
            System.out.println("============================");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	daoHandler.close(conn);
		}

        System.out.println("所有数据获取以及分词完成！ 结束时间为：" + TimeUtil.currentTime() + "\n");
        return result;
	}
}
