package com.socialheat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.socialheat.dao.DaoHandler;

/**
 * 统计共有每一项数据中，参与人数
 * 注意需要用户去重
 * @author lhx
 *
 */
public class Analyze1 {

	public static void main(String[] args) throws Exception {
		
		
		/**
		 * 统计每一项数据中，参与人数
		 */
//		Map<String, Integer> userMap = comment();
//		System.out.println(userMap.size());
		
		
		
		/**
		 * 统计每个弹幕数区间内共有多少个人
		 */
		Map<String, Integer> userMap = usee("jxy");
		
		Set<Entry<String, Integer>> set = userMap.entrySet();
		Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
		for (Entry<String, Integer> entry : set) {
			int value = entry.getValue();
			if(countMap.containsKey(value)) {
				countMap.put(value, countMap.get(value) + 1);
			} else {
				countMap.put(value, 1);
			}
		}
		
		// 排序
		List<Map.Entry<Integer, Integer>> list = new ArrayList<Map.Entry<Integer, Integer>>(countMap.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<Integer, Integer>>() {
            //升序排序
            public int compare(Entry<Integer, Integer> o1,
                    Entry<Integer, Integer> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        
        System.out.print("[");
        for (int i = 0; i < list.size(); i++) {
        	Entry<Integer, Integer> mapping = list.get(i);
        	if(mapping.getKey() > 10) {
        		break;
        	}
        	System.out.print("'" + mapping.getKey() + "'");
        	if(i < list.size()-1) {
        		System.out.print(", ");
        	}
		}
        System.out.print("]");
        
        System.out.println();
        
        int _10 = 0;
        int _15 = 0;
        int _20 = 0;
        int _30 = 0;
        int _50 = 0;
        int _100 = 0;
        System.out.print("[");
        for (int i = 0; i < list.size(); i++) {
        	Entry<Integer, Integer> mapping = list.get(i);
        	if(mapping.getKey() <= 10) {
        		System.out.print(mapping.getValue());
        		if(i < list.size()-1) {
            		System.out.print(", ");
            	}
        	} else if (mapping.getKey() <= 15) {
        		_10 = _10 + mapping.getValue();
        	} else if (mapping.getKey() <= 20) {
        		_15 = _15 + mapping.getValue();
        	} else if (mapping.getKey() <= 30) {
        		_20 = _20 + mapping.getValue();
        	} else if (mapping.getKey() <= 50) {
        		_30 = _30 + mapping.getValue();
        	} else if (mapping.getKey() <= 100){
        		_50 = _50 + mapping.getValue();
        	} else {
        		_100 = _100 + mapping.getValue();
        	}
		}
        System.out.print(_10 + ", " + _15 + ", " +_20 + ", " + _30 + ", " + _50 + ", " + _100);
        System.out.print("]");
        
	}
	
	
	public static Map<String, Integer> usee(String name) throws Exception{
		Map<String, Integer> map = new HashMap<String, Integer>();
		Connection connection = DaoHandler.getConnection();		
		String sql = "SELECT userID FROM " + name + "_danmu";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			String userId = rs.getString(1);
			if(map.containsKey(userId)) {
        		map.put(userId, map.get(userId) + 1);
        	} else {
        		map.put(userId, 1);
        	}
		}
		DaoHandler.close(connection);
		
		return map;
	}
	
	public static Map<String, Integer> danmu() throws Exception{
		Map<String, Integer> map = new HashMap<String, Integer>();
		BufferedReader reader = new BufferedReader(new FileReader("D:\\社交热度数据\\弹幕与评论的社交热度对比\\bilibili对比分析（4个）\\barrage\\Ted演讲.txt"));
        String line = reader.readLine();
        while(line != null) {
        	String userId = line.split(",")[6];
        	if(map.containsKey(userId)) {
        		map.put(userId, map.get(userId) + 1);
        	} else {
        		map.put(userId, 1);
        	}
        	line = reader.readLine();
        }
        reader.close();
        return map;
	}
	
	public static Map<String, Integer> comment() throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		BufferedReader reader = new BufferedReader(new FileReader("D:\\社交热度数据\\弹幕与评论的社交热度对比\\bilibili对比分析（4个）\\comment\\comment\\Ted演讲.txt"));
        String line = reader.readLine();
        while(line != null) {
        	if(line.contains("用户名：")) {
        		String userId = line.replace("用户名：", "");
        		if(map.containsKey(userId)) {
            		map.put(userId, map.get(userId) + 1);
            	} else {
            		map.put(userId, 1);
            	}
        	} else if (line.contains("----  ")) {
        		String userId = line.replace("----  ", "").split("  : ")[0];
        		if(map.containsKey(userId)) {
            		map.put(userId, map.get(userId) + 1);
            	} else {
            		map.put(userId, 1);
            	}
        	}
        	line = reader.readLine();
        }
        reader.close();
        return map;
	}
	
	public static Map<String, Integer> zhibo() throws Exception{
		Map<String, Integer> map = new HashMap<String, Integer>();
		BufferedReader reader = new BufferedReader(new FileReader("D:\\社交热度数据\\弹幕与评论的社交热度对比\\直播弹幕与qq群\\直播数据过滤赠送\\黑白直播.txt"));
        String line = reader.readLine();
        while(line != null) {
        	String userId = line.split("] ")[1].split(": ")[0];
        	if(map.containsKey(userId)) {
        		map.put(userId, map.get(userId) + 1);
        	} else {
        		map.put(userId, 1);
        	}
        	line = reader.readLine();
        }
        reader.close();
        return map;
	}
	
	public static Map<String, Integer> qq() throws Exception{
		Map<String, Integer> map = new HashMap<String, Integer>();
		BufferedReader reader = new BufferedReader(new FileReader("D:\\社交热度数据\\弹幕与评论的社交热度对比\\直播弹幕与qq群\\55开粉丝1群(40401113).txt"));
        String line = reader.readLine();
        while(line != null) {
        	String userId = line.substring(21);
        	if(map.containsKey(userId)) {
        		map.put(userId, map.get(userId) + 1);
        	} else {
        		map.put(userId, 1);
        	}
        	line = reader.readLine();
        	line = reader.readLine();
        	line = reader.readLine();
        }
        reader.close();
        return map;
	}
	
}
