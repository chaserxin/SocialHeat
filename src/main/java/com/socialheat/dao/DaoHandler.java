package com.socialheat.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoHandler {
	//数据库用户名
    private static final String USERNAME = "root";
    //数据库密码
    private static final String PASSWORD = "";
    //驱动信息
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    //数据库地址
    private static final String URL = "jdbc:mysql://localhost:3306/socialheat";
    private Connection connection;
  
    public DaoHandler() {
        try{
            Class.forName(DRIVER);
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    
    public Connection getConnection(){
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
        	System.out.println("获取连接失败...");
        }
        return connection;
    }
    
	public void close(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("关闭连接失败...");
		}
	}
}
