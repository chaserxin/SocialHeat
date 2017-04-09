package com.socialheat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class AnalyzeHotWord {
	
	static String startLine = "序号    词语         权重";
	
	public static void main(String[] args) throws Exception {
//		getNewHotWord();
		getHotWord();
	}

	public static void getNewHotWord() throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader("E:/SocialHeatPaper/SocialHeat/result/DFZX_Baidu_Top_Word.txt"));
		reader.readLine();
		String line = reader.readLine();
		
		Set<String> hotWordSet = new HashSet<String>();
		for(int i=0; i<30; i++) {
			if(i == 0) {
				while(!line.equals(startLine)) {
					String[] lineArr = line.split("  ");
					if(Integer.parseInt(lineArr[0]) > 50) {
						line = reader.readLine();
						continue;
					}
					hotWordSet.add(lineArr[1]);
					line = reader.readLine();
				}
			} else {
				System.out.println("第" + (i+1) + "天增加的热词：");
				Set<String> hotWordSet1 = new HashSet<String>();
				line = reader.readLine();
				while(line != null && !line.equals(startLine)) {
					String[] lineArr = line.split("  ");
					if(Integer.parseInt(lineArr[0]) > 50) {
						line = reader.readLine();
						continue;
					}
					if(!hotWordSet.contains(lineArr[1])){
						System.out.println(lineArr[1] + "  " + lineArr[2]);
					}
					hotWordSet1.add(lineArr[1]);
					line = reader.readLine();
				}
				hotWordSet = hotWordSet1;
				System.out.println("--------------------------------------");
			}
		}
		reader.close();
	}
	
	public static void getHotWord() throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader("E:/SocialHeatPaper/SocialHeat/result/DFZX_Baidu_Top_Word.txt"));
		reader.readLine();
		String line = reader.readLine();
		for(int i=0; i<30; i++) {
			System.out.println("第" + (i+1) + "天大于平均权重的热词：");
			Map<String, Double> hotWordmap = new HashMap<String, Double>();
			double totalRate = 0.0;
			while(line != null && !line.equals(startLine)) {
				String[] lineArr = line.split("  ");
				hotWordmap.put(lineArr[1], Double.parseDouble(lineArr[2]));
				totalRate += Double.parseDouble(lineArr[2]);
				line = reader.readLine();
			}
			double averageRate = totalRate / hotWordmap.size();
			Set<Entry<String, Double>> entry = hotWordmap.entrySet();
			for (Entry<String, Double> entry2 : entry) {
				if(entry2.getValue() >= averageRate) {
					System.out.println(entry2.getKey() + "  " + entry2.getValue());
				}
			}
			System.out.println("--------------------------------------");
			line = reader.readLine();
		}
		reader.close();
	}
}
