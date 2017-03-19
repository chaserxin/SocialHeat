package com.socialheat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.socialheat.wordsplit.WordSplit;

public class DfzxWeiboDao implements DaoInterface {

	public DfzxWeiboDao() {
	}
	
	/**
	 * 得到微博的评论
	 */
	public List<String> getSentenceList() {
		List<String> result = new ArrayList<String>();

        // 获取数据库连接
        Connection conn = DaoHandler.getConnection();

        String sql = "SELECT text FROM dfzx_weibo ORDER BY create_time";
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
        return result;
	}

	public String getName() {
		return "DFZX_Weibo";
	}

	public List<String> getSentenceListByStream(int start) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<List<String>> getSplitSentenceList(WordSplit wordSplit) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getStartTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<String[]> getSplitSentenceListByStream(int span) {
		// TODO Auto-generated method stub
		return null;
	}

}
