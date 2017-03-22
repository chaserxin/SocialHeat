package com.socialheat.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.IndexAnalysis;

import com.socialheat.dao.DaoHandler;
import com.socialheat.dao.StopWordDao;

public class Mysql2SpliteWord {

	public static void main(String[] args) {

		StopWordDao stopWordDao = new StopWordDao();
		Set<String> stopWordSet = stopWordDao.getStopWord();

		// 获取数据库连接
		Connection conn = DaoHandler.getConnection();
		String sql1 = "SELECT text, create_time FROM nanhai_baidu LIMIT 6000000, 3000000";
		String sql2 = "insert into sw_nanhai_baidu(word,create_time) values(?,?)";
		PreparedStatement pstmt1;
		PreparedStatement pstmt2;
		int i = 0;
		int j = 0;

		List<List<String>> wordList = new ArrayList<List<String>>();
		try {
			pstmt1 = conn.prepareStatement(sql1);
			ResultSet rs = pstmt1.executeQuery();
			pstmt2 = conn.prepareStatement(sql2);
			// 优化插入第一步 设置手动提交
			conn.setAutoCommit(false);
			while (rs.next()) {
				String text = rs.getString(1);
				long create_time = rs.getLong(2);
				List<Term> wordSplitResult = IndexAnalysis.parse(text);
				List<String> list = new ArrayList<String>();
				list.add(create_time + "");
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

				i++;
				System.out.println(i);
			}

			for (List<String> list : wordList) {
				StringBuffer sb = new StringBuffer();
				sb.append(list.get(1));
				for (int n = 2; n < list.size() - 1; n++) {
					sb.append("," + list.get(n));
				}
				pstmt2.setString(1, sb.toString());
				pstmt2.setLong(2, Long.parseLong(list.get(0)));
				// 优化插入第二步 插入代码打包，等一定量后再一起插入。
				pstmt2.addBatch();
				j++;
				// 每10000次提交一次
				if ((j != 0 && j % 10000 == 0 || j == wordList.size() - 1)) {// 可以设置不同的大小；如50，100，200，500，1000等等
					System.out.println(j + " -------------- " + wordList.size());
					pstmt2.executeBatch();  
					// 优化插入第三步 提交，批量插入数据库中。
					conn.commit();
					// 提交后，Batch清空。
					pstmt2.clearBatch();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DaoHandler.close(conn);
		}
	}

}
