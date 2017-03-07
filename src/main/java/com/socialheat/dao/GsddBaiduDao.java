package com.socialheat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.socialheat.wordsplit.WordSplit;

public class GsddBaiduDao implements DaoInterface {

	private DaoHandler daoHandler;
	
	public GsddBaiduDao() {
		daoHandler = new DaoHandler();
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
        String sql = "SELECT text FROM gsdd_query ORDER BY create_time";
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
		return "GSDD_Baidu";
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
}
