package com.socialheat.util;

/**
 * Created by sl on 16-7-26.
 */

import com.socialheat.bean.DataBase;
import com.socialheat.bean.Rate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DataQuery {
    //数据库用户名
    private static final String USERNAME = "root";
    //数据库密码
    private static final String PASSWORD = "111111";
    //驱动信息
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    //数据库地址
    private static final String URL = "jdbc:mysql://localhost:3306/baidu";
    private Connection connection;

    public DataQuery() {
        try{
            Class.forName(DRIVER);
            System.out.println("数据库连接成功！");

        }catch(Exception e){

        }
    }

    /**
     * 获得数据库的连接
     * @return
     */
    public Connection getConnection(){
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


    /**
     * 查询
     * @param tableName 待查询的表名
     * @param columnName 待查询的列名
     */
    public List<String> query(String tableName , String columnName){

        List<String> result = new ArrayList<String>();

        Connection conn = getConnection();
        String sql = "select "+columnName+" from "+tableName;
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("============================");
            while (rs.next()) {
                result.add(rs.getString(1));
            }

            System.out.println("读入了"+result.size()+"条数据");
            System.out.println("============================");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * 查询两列
     * @return
     */
    public List<String> queryDoubleColumn(){
        List<String> result = new ArrayList<String>();

        Connection conn = getConnection();

        String column = DataBase.columnName;
        if(DataBase.columnName2!=null)
            column += ","+DataBase.columnName2;

        //Usee
//        System.out.println(column);
        String sql = "SELECT "+column+" FROM "+DataBase.tableName+" WHERE topicID=273";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("============================");
            while (rs.next()) {
                result.add(rs.getString(1));
            }

            System.out.println("读入了"+result.size()+"条数据");
            System.out.println("============================");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 按照时间间隔获取弹幕
     * @param span：时间间隔，以分钟为单位
     * @return Rate
     */
    public List<Rate> queryByTime(int span){

        int ms = span*60;

        long startTime,endTime;

        List<Rate> result = new ArrayList<Rate>();

        Connection conn = getConnection();

        //Usee
        String sql = "SELECT * FROM "+DataBase.tableName+" WHERE topicID=273 ORDER BY create_time";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            rs.next();
            startTime = Long.valueOf(rs.getString(12));

            String tmp="0";
            while (rs.next()) {
                tmp = rs.getString(12);
            }
            endTime = Long.valueOf(tmp);

            long i=startTime;
            while (endTime - i >= ms){
                List<String> resStr = new ArrayList<String>();
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    if(Long.valueOf(rs.getString(12))>=i+ms)
                        break;
                    if(Long.valueOf(rs.getString(12))<i)
                        continue;
                    resStr.add(rs.getString(16));
                }
                result.add(new Rate(resStr,i,i+ms));

                i = i+ms;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        System.out.println(startTime +"   "+endTime);

        return result;


    }




}