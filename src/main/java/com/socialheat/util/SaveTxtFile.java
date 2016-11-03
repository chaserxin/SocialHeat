package com.socialheat.util;

import com.socialheat.bean.Rate;
import com.socialheat.bean.Word;
import java.sql.Timestamp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by sl on 16-7-26.
 */
public class SaveTxtFile {

    public static void write(String fileName , List<Word> results) throws IOException {
        File file=new File(fileName);
        if(!file.exists())
            file.createNewFile();
        FileOutputStream out=new FileOutputStream(file,true);


        StringBuffer sb1=new StringBuffer();
        sb1.append("序号    词语          加入长度         TF-IDF          TF           IDF"+"\n");
        out.write(sb1.toString().getBytes("utf-8"));

        int cnt_out=1;
        for (Word word : results) {
            StringBuffer sb=new StringBuffer();
            sb.append(cnt_out+"  "+word.getName()+"  "+word.getTf_idf_length()+"  "+word.getTf_idf()+"  "+word.getTf()+"  "+word.getIdf()
                    +"\n");
            out.write(sb.toString().getBytes("utf-8"));
            cnt_out++;
        }
        out.close();
    }

    public static void writeForUsee(String fileName , List<Rate> results) throws IOException {
        File file=new File(fileName);
        if(!file.exists())
            file.createNewFile();
        FileOutputStream out=new FileOutputStream(file,true);


        StringBuffer sb1=new StringBuffer();
        sb1.append("序号        开始时间      --      结束时间         Rate"+"\n");
        out.write(sb1.toString().getBytes("utf-8"));

        int cnt_out=1;
        for (Rate rate : results) {
            StringBuffer sb=new StringBuffer();
            sb.append(cnt_out+"  "+new Timestamp(rate.getStartTime()*1000)+" -- "+new Timestamp(rate.getEndTime()*1000)+"  "+rate.getRate()+"\n");
            out.write(sb.toString().getBytes("utf-8"));
            cnt_out++;
        }
        out.close();
    }


}
