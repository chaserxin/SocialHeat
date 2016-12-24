package com.socialheat.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class StopWordDao {
	private DaoHandler daoHandler;
	
	public StopWordDao() {
		daoHandler = new DaoHandler();
	}
	
	public Set<String> getStopWord(){
		Set<String> set = new HashSet<String>();
		
		 // 获取数据库连接
        Connection conn = daoHandler.getConnection();
        
        // StopWord
        String sql = "SELECT word FROM stopword";
        Statement st;
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            System.out.println("============================");
            while (rs.next()) {
            	set.add(rs.getString(1));
            }

            System.out.println("读入了"+set.size()+"条数据（停用词）");
            System.out.println("============================");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	daoHandler.close(conn);
		}
		return set;
	}
	
	public static void main(String[] args) {
		StopWordDao stopWordDao = new StopWordDao();
		Set<String> set = stopWordDao.getStopWord();
		for (String string : set) {
			System.out.println(string);
		}
	}
}
