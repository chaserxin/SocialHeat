package com.socialheat.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.socialheat.bean.Rate;
import com.socialheat.bean.Word;

/**
 * Created by sl on 16-7-26.
 */
public class SaveFileUtil {

    public static void writeWord(String fileName , List<Word> results) throws IOException {
        File file=new File(fileName);
        if(!file.exists()) {
        	file.createNewFile();
        } else {
        	file.delete();
        	file.createNewFile();
        }
        FileOutputStream out=new FileOutputStream(file,true);

        StringBuffer sb1=new StringBuffer();
        sb1.append("序号    词语         权重"+"\n");
        out.write(sb1.toString().getBytes("utf-8"));

        int cnt_out=1;
        for (Word word : results) {
            StringBuffer sb=new StringBuffer();
            sb.append(cnt_out+"  "+word.getName()+"  "+word.getTf_idf_length()*word.getCpmi()+"\n");
            out.write(sb.toString().getBytes("utf-8"));
            cnt_out++;
        }
        out.close();
    }

    public static void writeRate(String fileName , List<Rate> results) throws IOException {
        File file=new File(fileName);
        if(!file.exists()) {
        	file.createNewFile();
        } else {
        	file.delete();
        	file.createNewFile();
        }
        FileOutputStream out=new FileOutputStream(file,true);

        StringBuffer sb1=new StringBuffer();
        sb1.append("序号        开始时间      --      结束时间         Rate"+"\n");
        out.write(sb1.toString().getBytes("utf-8"));

        StringBuffer time = new StringBuffer();
        StringBuffer eventPopularity = new StringBuffer();
        time.append("[");
        eventPopularity.append("[");
        
        int cnt_out=1;
        for (Rate rate : results) {
        	if(cnt_out != 1) {
        		time.append(",");
        		eventPopularity.append(",");
        	}
        	time.append("'" + TimeUtil.timestamp2Date(rate.getStartTime()) + "-" + TimeUtil.timestamp2Date(rate.getEndTime()) + "'");
            eventPopularity.append(rate.getRate());
            
            StringBuffer sb = new StringBuffer();
            sb.append(cnt_out+"  "+TimeUtil.timestamp2Date(rate.getStartTime())+" -- "+TimeUtil.timestamp2Date(rate.getEndTime())+"  "+rate.getRate()+"\n");
            out.write(sb.toString().getBytes("utf-8"));
            cnt_out++;
        }
        time.append("]");
        eventPopularity.append("]");
        System.out.println("=============================");
        System.out.println("time: " + time.toString());
        System.out.println("eventPopularity: " + eventPopularity.toString());
        System.out.println("=============================");
        out.close();
        
        
    }


}
