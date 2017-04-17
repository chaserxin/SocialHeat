//package com.socialheat.dao;
//
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
//public class JxyCommentDao implements ProjectDao {
//
//	long startTime;
//	String name = "jxy_comment";
//	
//	WordSplit wordSplit;
//	public JxyCommentDao() {
//		wordSplit = new WordSplitAnsj_seg();
//		try {
//			startTime = Long.parseLong(TimeUtil.date2Timestamp("2016-10-18 18:00:00"));
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
//       String sql = "SELECT content FROM " + name + " WHERE create_time BETWEEN " + startTime + " and " + endTime;
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
//}
