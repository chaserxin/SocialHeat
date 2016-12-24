package com.socialheat.analysis;

import java.util.List;

import com.socialheat.bean.Word;

public class WordCPMI {

	// 得到 N12,结果存在数组 N12 中
	// N12 为每个热词在出现在多少个文档中
	private static int[] N12 = null;
	
	// 得到 N3,结果放在二维数组 N3 中
	// N3 为每个热词与其他热词的 PMI
	private static int[][] N3 = null;
	
	// 句子的总数
	private static int sentenceCount = 0;
	
	/*
     * 获得每个热词与其他热词的互信息:CPMI 
     */
    public static List<Word> getCPMI(List<Word> topNWordList) {
    	int wordCount = topNWordList.size();
    	
    	// 得到给个词语的 CPMI 
    	// 计算公式为: log((N*(n3+1) / ((N1+1)*(N2+1)))
    	// 结果放在 result 这个 map 中
    	double[] CPMI = new double[wordCount];
    	double Max_CPMI = -100.0;
    	for (int i=0; i<wordCount; i++) {
    		CPMI[i] = 0.0;
    		for (int j=0; j<wordCount; j++) {
    			if (i == j) {
    				continue;
    			}
    			double temp1 = (double)sentenceCount * (double)(N3[i][j] + 1);
    			double temp2 = (double)(N12[i] + 1) * (double)(N12[j] + 1);
    			if (i == 0) {
    				System.out.println(sentenceCount + " * " + N3[i][j] + " / " + N12[i] + " * " + N12[j] + " = " + Math.log(temp1 / temp2) + " ");
    			}
//    			CPMI[i] += Math.log(temp1 / temp2);
    			CPMI[i] += temp1 / temp2;
    		}
    		if (i == 0) {
    			System.out.println(CPMI[i]);
    		}
    		if (CPMI[i] > Max_CPMI) {
				Max_CPMI = CPMI[i];
			}
    	}
    	for (int i=0; i<wordCount; i++) {
    		double cmpi = CPMI[i] / Max_CPMI;
    		topNWordList.get(i).setCpmi(cmpi);
    		topNWordList.get(i).setTf_idf_length_cpmi(topNWordList.get(i).getTf_idf_length() * cmpi);
    		System.out.println(topNWordList.get(i).getName() + " : " + cmpi);
    	}
		return topNWordList;
    }
    
    /*
     * 初始化获取 CPMI所需的信息
     * 主要有 sentenceCount、每个热词在出现在多少个文档中、每个热词与其他热词的 PMI
     */
	public static void initCPMI(List<Word> topNWordList, List<List<String>> sentenceList, int windowSize) {
    	int wordCount = topNWordList.size();
    	sentenceCount = sentenceList.size();
    	
    	// 得到所有每次热词在每个句子中的存在情况
    	// 结果存在二维数组 N 中,如果存在则为1
    	int[][] N = new int[wordCount][sentenceCount];
    	
    	N12 = new int[wordCount];
    	N3 = new int[wordCount][wordCount];
    	
    	for (int i=0; i<wordCount; i++) {
    		Word topNword = topNWordList.get(i);
    		int n12 = 0;
			for (int j=0; j<sentenceCount; j++) {
				List<String> sentence = sentenceList.get(j);
				if(sentence.contains(topNword.getName())) {
					N[i][j] = 1;
				}
				n12 += N[i][j];
				if(i > 0) {
					for (int z=i; z>0; z--) {
						if ((N[z-1][j] + N[i][j]) == 2) {
							N3[z-1][i] += 1; 
							N3[i][z-1] += 1;
						}
					}
				}
			}
			N12[i] = n12;
		}
    	
//    	// 输出 N3
//    	for (int i=0; i<wordCount; i++) {
//    		for (int j=0; j<wordCount; j++) {
//				System.out.print(N3[i][j] + " ");
//    		}
//    		System.out.println();
//    	}
//    	System.out.println();
//
    	// 输出 N12
    	for (int i=0; i<wordCount; i++) {
    		System.out.print(N12[i] + " ");
    	}
    	System.out.println();
    	System.out.println();
    	
    	return;
    }
}
