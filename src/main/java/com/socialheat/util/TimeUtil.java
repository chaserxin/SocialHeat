package com.socialheat.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//currentTime:格式为yyyy-MM-dd HH:mm:ss的当前时间
	public static String currentTime() {
		return df.format(new Date()); 
	}
	
	//currentTimeStamp:UNIX时间戳，定义为从格林威治时间 1970 年 01 月 01 日 00 时 00 分 00 秒起至现在的总秒数。
	public static String currentTimeStamp() {
		return System.currentTimeMillis()/1000 + "";
	}
    
	/**
	 * 时间戳转成常见的yyyy-MM-dd HH:mm:ss型
	 * @param s:String类型unix时间戳，一般从数据库取出来的时候就是这种类型
	 * @return
	 */
    public static String timestamp2Date(String s) {
    	Long timestamp = Long.parseLong(s) * 1000;  
    	return df.format(new Date(timestamp));  
    }
    
    public static String timestamp2Date(long s) {
    	Long timestamp = s * 1000;  
    	return df.format(new Date(timestamp));  
    }
    
    /**
     * 常见的yyyy-MM-dd HH:mm:ss型转成unix时间戳，存入数据库的时候用到
     * @param s:常见yyyy-MM-dd HH:mm:ss格式的实践
     * @return
     * @throws ParseException
     */
    public static String date2Timestamp(String s) throws ParseException{
		long l = df.parse(s).getTime();
		return String.valueOf(l).substring(0, 10);
    }
}
