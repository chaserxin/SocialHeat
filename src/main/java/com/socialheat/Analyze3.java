package com.socialheat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.socialheat.dao.DaoHandler;

public class Analyze3 {

	public static void main(String[] args) throws Exception {
		getText("C:\\Users\\97404\\Desktop\\百度数据5.7.txt");

	}

	public static void getText(String filePath) throws Exception {
		Connection con = DaoHandler.getConnection();
		String sql = "insert into testdata_baidu(create_time, user, text) value(?,?,?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
		String line = reader.readLine();
		DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		while(line != null) {
			String[] arr = line.split(" ");
			String a1 = arr[0].substring(0, 11);
			String a2 = arr[0].substring(11) + ":00";
			Date date = sdf.parse(a1 + " " + a2); 
			String create_time = date.getTime()/1000 + "";
			String user = arr[1];
			StringBuffer sb = new StringBuffer();
			for (int i = 2; i < arr.length; i++) {
				sb.append(arr[i] + " ");
			}
			String text = sb.toString();
			System.out.println(text);
			pstmt.setString(1, create_time);
			pstmt.setString(2, user);
			pstmt.setString(3, text);
			pstmt.execute();
			
			line = reader.readLine();
		}
		reader.close();
	}
}
