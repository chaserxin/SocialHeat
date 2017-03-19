package com.socialheat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.socialheat.wordsplit.WordSplit;

public class USeeDao implements DaoInterface {
	
	
	public USeeDao(){
	}


    /**
     * 得到弹幕message
     * @return
     */
    public List<String> getSentenceList(){
        List<String> result = new ArrayList<String>();

        // 获取数据库连接
        Connection conn = DaoHandler.getConnection();

        // USee
        String sql = "SELECT messages FROM danmu WHERE topicID = '295'";
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("============================");
            while (rs.next()) {
                result.add(rs.getString(1));
            }

            System.out.println("读入了"+result.size()+"条数据（弹幕）");
            System.out.println("============================");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	DaoHandler.close(conn);
		}

        return result;
    }
    
    public String getName() {
    	return "USee";
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
