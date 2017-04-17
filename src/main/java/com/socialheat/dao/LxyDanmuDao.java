//package com.socialheat.dao;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.socialheat.util.TimeUtil;
//import com.socialheat.wordsplit.WordSplit;
//import com.socialheat.wordsplit.WordSplitAnsj_seg;
//
//public class LxyDanmuDao implements ProjectDao {
//
//	long startTime;
//	String name = "lxy_danmu";
//	
//	WordSplit wordSplit;
//	public LxyDanmuDao() {
//		wordSplit = new WordSplitAnsj_seg();
//		try {
//			startTime = Long.parseLong(TimeUtil.date2Timestamp("2016-9-27 18:00:00"));
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//	}
//	
//	public List<String> getSentenceList() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public List<String> getSentenceListByStream(int span) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public long getStartTime() {
//		return startTime;
//	}
//
//	public List<String[]> getSplitSentenceListByStream(int span) {
//		List<String[]> result = new ArrayList<String[]>();
//		
//		 // 获取数据库连接
//       Connection conn = DaoHandler.getConnection();
//		long endTime = startTime + span * 60;
//       String sql = "SELECT messages FROM " + name + " WHERE create_time BETWEEN " + startTime + " and " + endTime;
//       System.out.println(sql);
//       PreparedStatement pstmt;
//       try {
//           pstmt = conn.prepareStatement(sql);
//           ResultSet rs = pstmt.executeQuery();
//           System.out.println("============================");
//           while (rs.next()) {
//        	   List<String> wordList = wordSplit.splitSencence(rs.getString(1));
//        	   String[] tempArr = new String[wordList.size()];
//        	   for (int i = 0; i < tempArr.length; i++) {
//        		   tempArr[i] = wordList.get(i);
//        	   }
//               result.add(tempArr);
//           }
//           System.out.println("读入了"+result.size()+"条数据");
//           System.out.println("============================");
//       } catch (SQLException e) {
//           e.printStackTrace();
//       } finally {
//       	DaoHandler.close(conn);
//		}
//       
//       startTime = endTime;
//		return result;
//	}
//
//	public static void main(String[] args) throws Exception {
//		// 获取数据库连接
//        Connection conn = DaoHandler.getConnection();
//        String sql = "INSERT INTO jxy_comment VALUE(?,?,?,?,?,?)";
//        PreparedStatement pstmt = conn.prepareStatement(sql);
//        //优化插入第一步       设置手动提交  
//        conn.setAutoCommit(false);
//        
//        BufferedReader reader = new BufferedReader(new FileReader("F:\\UseeDanmuData\\comment2.txt"));
//        String line = reader.readLine();
//        while(line != null) {
//        	String[] tampArr = line.split(",");
//        	for (int i = 1; i <= tampArr.length; i++) {
//        		pstmt.setString(i, tampArr[i-1]);
//        		System.out.println(tampArr[i-1]);
//			}
//        	System.out.println("_________________________________");
//        	//优化插入第二步       插入代码打包，等一定量后再一起插入。
//	        pstmt.addBatch(); 
//        	line = reader.readLine();
//        }
//        
//        pstmt.executeBatch();  
//        //优化插入第三步       提交，批量插入数据库中。
//        conn.commit(); 
//        reader.close();
//        DaoHandler.close(conn);
//	}
//}
