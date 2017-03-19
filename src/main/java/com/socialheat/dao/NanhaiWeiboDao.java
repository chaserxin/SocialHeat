package com.socialheat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.IndexAnalysis;

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
	
	public static void main(String[] args) {
		
		StopWordDao stopWordDao = new StopWordDao();
		Set<String> stopWordSet = stopWordDao.getStopWord();
		
//		long startTime = 0L;
//		long endTime = 0L;
//		try {
//			startTime = Long.parseLong(TimeUtil.date2Timestamp("2016-7-13 10:00:01"));
//			endTime = Long.parseLong(TimeUtil.date2Timestamp("2016-7-19 23:59:59"));
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		} 
		
		// 获取数据库连接
		Connection conn = DaoHandler.getConnection();
		String sql1 = "SELECT text, create_time FROM nanhai_weibo WHERE create_time LIMIT 3000000, 6000000";
		String sql2 = "insert into sw_nanhai_weibo(word,create_time) values(?,?)";
        PreparedStatement pstmt1;
        PreparedStatement pstmt2;
        int i = 0;
        int j = 0;
        
        List<List<Object>> wordList = new ArrayList<List<Object>>();
        try {
            pstmt1 = conn.prepareStatement(sql1);
            ResultSet rs = pstmt1.executeQuery();
        	pstmt2 = conn.prepareStatement(sql2);
        	//优化插入第一步       设置手动提交  
            conn.setAutoCommit(false); 
            while (rs.next()) {
            	String text = rs.getString(1);
            	long create_time = rs.getLong(2);
	            List<Term> wordSplitResult = IndexAnalysis.parse(text);
	            List<Object> list = new ArrayList<Object>();
	            list.add(create_time);
	            for (Term term : wordSplitResult) {
	                String name = term.getName();
	                // 过滤掉停用词和空格
					if (!stopWordSet.contains(name)) {
						list.add(name);
					}
	            }
	            if (list.size() > 1) {
	            	 wordList.add(list);
	            } else {
	            	System.out.println(text);
	            }
	           
		    	i ++;
		    	System.out.println(i);
            }

            for (List<Object> list : wordList) {
            	StringBuffer sb = new StringBuffer();
            	sb.append((String) list.get(1));
            	for (int n=2; n<list.size()-1; n++) {
            		sb.append("," + (String) list.get(n));
            	}
            	pstmt2.setString(1, sb.toString());
            	pstmt2.setLong(2, (Long) list.get(0));
    	        //优化插入第二步       插入代码打包，等一定量后再一起插入。
    	        pstmt2.addBatch(); 
    	        j ++;
    	        //每10000次提交一次 
                if((j!=0 && j%10000==0 || j == wordList.size()-1)){//可以设置不同的大小；如50，100，200，500，1000等等  
                	System.out.println(j + " -------------- " + wordList.size());
                	pstmt2.executeBatch();  
                    //优化插入第三步       提交，批量插入数据库中。
                    conn.commit(); 
                    //提交后，Batch清空。 
                    pstmt2.clearBatch();        
                }
			}
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	DaoHandler.close(conn);
		}
	}

}
