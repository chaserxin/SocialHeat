package com.socialheat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import com.socialheat.bean.Rate;

public class USeeDao implements DaoInterface {
	
	private DaoHandler daoHandler;
	
	public USeeDao(){
		daoHandler = new DaoHandler();
	}


    /**
     * 得到弹幕message
     * @return
     */
    public List<String> getSentenceList(){
        List<String> result = new ArrayList<String>();

        // 获取数据库连接
        Connection conn = daoHandler.getConnection();

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
        	daoHandler.close(conn);
		}

        return result;
    }


    /**
     * 按照时间间隔获取弹幕
     * @param span：时间间隔，以分钟为单位
     * @return Rate
     */
    public List<Rate> getSentenceListByTime(int span){

        int ms = span*60;
        long startTime,endTime;
        List<Rate> result = new ArrayList<Rate>();
        // 获取数据库连接
        Connection conn = daoHandler.getConnection();

        //USee
        String sql = "SELECT * FROM danmu WHERE topicID=295 ORDER BY create_time";
        Statement st = null;
        try {
            st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(sql);
            // 获取开始时间和结束时间
            rs.next();
            startTime = Long.valueOf(rs.getString(12));
            rs.last();
            endTime = Long.valueOf(rs.getString(12));
            // 以十分钟为间隔,将所有弹幕分成不同组
            rs.beforeFirst();
            long i = startTime;
            while (endTime-i >= ms){
                List<String> resStr = new ArrayList<String>();
                while (rs.next()) {
                    if(Long.valueOf(rs.getString(12)) >= i+ms) {
                    	rs.previous();
                        break;
                    }
                    resStr.add(rs.getString(16));
                }
                result.add(new Rate(resStr, i, i+ms));
                i = i + ms;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	daoHandler.close(conn);
		}
        return result;
    }
    
    public String getName() {
    	return "USee";
    }
}
