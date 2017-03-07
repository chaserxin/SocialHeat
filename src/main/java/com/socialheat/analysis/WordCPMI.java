package com.socialheat.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.socialheat.bean.Word;
import com.socialheat.util.TimeUtil;

public class WordCPMI {

	// 得到 N12,结果存在数组 N12 中
	// N12 为每个热词在出现在多少个文档中
	private int[] N12;
	
	// 得到 N3,结果放在二维数组 N3 中
	// N3 为每个热词与其他热词的 PMI
	private int[][] N3;
	
	// 上一次初始化 CPMI 时所有的热词 Map,键为热词的 Name 值为热词在 Word 对象;
	Map<String, Integer[]> topNWordMap;
	
	// 句子的总数
	private int sentenceCount;
	// 热词总数
	private int topNum;
	
	public WordCPMI(int topNum) {
		this.topNum = topNum;
		sentenceCount = 0;
		topNWordMap = new HashMap<String, Integer[]>();
		N12 = new int[topNum];
		N3 = new int[topNum][topNum];
	}
	
	 /**
     * 初始化获取 CPMI所需的信息
     * 主要有 sentenceCount、每个热词在出现在多少个文档中、每个热词与其他热词的 PMI
     */
	public void initCPMI(List<Word> topNWordList, int newSentenceCount) {
		System.out.println("初始化 CPMI 开始！ 开始时间为：" + TimeUtil.currentTime() + "\n");
		
		sentenceCount += newSentenceCount;
    	int wordCount = topNWordList.size();
    	
    	int[] _N12 = new int[topNum];
    	int[][] _N3 = new int[topNum][topNum];
    	
    	for (int i=0; i<wordCount-1; i++) {
    		Word word1 = topNWordList.get(i);
    		Integer[] sentenceArr1 = word1.sentenceList.toArray(new Integer[word1.sentenceList.size()]);
    		// 如果此热词在之前的计算中出现过,读出之前保存的信息
    		int oldIndex1 = -1;
    		int sentencecount1 = 0;
    		if (topNWordMap.containsKey(word1.getName())) {
    			Integer[] temp = topNWordMap.get(word1.getName());
    			oldIndex1 = temp[0];
    			sentencecount1 = temp[1];
    		}
    		
    		for (int j=i+1; j<wordCount; j++) {
    			Word word2 = topNWordList.get(j);
    			Integer[] sentenceArr2 = word2.sentenceList.toArray(new Integer[word2.sentenceList.size()]);
    			// 如果此热词在之前的计算中出现过,读出之前保存的信息
    			int oldIndex2 = -1;
    			int sentencecount2 = 0;
        		if (topNWordMap.containsKey(word2.getName())) {
        			Integer[] temp = topNWordMap.get(word2.getName());
        			oldIndex2 = temp[0];
        			sentencecount2 = temp[1];
        		}
        		if (oldIndex1 == -1 || oldIndex2 == -1) {
        			// 两个热词中至少有一个是第一次出现
        			int startIndex = 0;
        			for (int m=0; m<sentenceArr2.length; m++) {
        				int sentence2 = sentenceArr2[m];
        				if (sentence2 > sentenceArr1[sentenceArr1.length-1]){
    						break;
    					}
        				// 二分查找法
        				int index = searchLoop(sentenceArr1, sentence2, startIndex);
        				if (index != -1) {
        					startIndex = index;
        					_N3[i][j] ++; 
        				}
        			}
        		} else {
        			// 两个热词都不是第一次出现
        			int startIndex = sentencecount1 - 1;
        			for (int m=0; m<sentencecount2; m++) {
        				// 先用 sentenceArr2 中之前的数据和 sentenceArr1 中新的数据作对比
        				int sentence2 = sentenceArr2[m];
        				if (sentence2 < sentenceArr1[startIndex]) {
    						continue;
    					} else if (sentence2 > sentenceArr1[sentenceArr1.length-1]){
    						break;
    					}
        				// 二分查找法
        				int index = searchLoop(sentenceArr1, sentence2, startIndex);
        				if (index != -1) {
        					startIndex = index;
        					_N3[i][j] ++; 
        				}
        			}
        			startIndex = 0;
        			for (int m=sentencecount2; m<sentenceArr2.length; m++) {
        				// 再用 sentenceArr2 中新的数据和 sentenceArr1 中所有数据作对比
        				int sentence2 = sentenceArr2[m];
        				if (sentence2 > sentenceArr1[sentenceArr1.length-1]){
    						break;
    					}
        				// 二分查找法
        				int index = searchLoop(sentenceArr1, sentence2, startIndex);
        				if (index != -1) {
        					startIndex = index;
        					_N3[i][j] ++; 
        				}
        			}
        			// 加上之前的数据
					if(oldIndex1 > oldIndex2) {
						_N3[i][j] += N3[oldIndex2][oldIndex1];
					} else {
						_N3[i][j] += N3[oldIndex1][oldIndex2];
					}
        		}
    		}
    	}
    	
    	// 将原来的信息清理
    	topNWordMap.clear();
    	for (int i=0; i<wordCount; i++) {
    		Word word = topNWordList.get(i);
    		_N12[i] = word.sentenceList.size();
    		Integer[] num = new Integer[2];
    		num[0] = word.getIndex();
    		num[1] = word.sentenceList.size();
    		topNWordMap.put(word.getName(), num);
    	}
    	
    	// 保存必要的信息
    	N12 = _N12;
    	N3 = _N3;
    	
    	System.out.println("初始化 CPMI 结束！ 结束时间为：" + TimeUtil.currentTime() + "\n");
    	return ;
    }
	
	/**
     * 获得每个热词与其他热词的互信息:CPMI 
     */
    public List<Word> getCPMI(List<Word> topNWordList) {
    	int wordCount = topNWordList.size();
    	
//    	// 输出 N12
//    	for (int i=0; i<wordCount; i++) {
//    		System.out.print(N12[i] + " ");
//    	}
//    	System.out.println();
//    	System.out.println();
    	
    	
    	
    	// 得到给个词语的 CPMI 
    	// 计算公式为: log((N*(N3+1) / ((N1+1)*(N2+1)))
    	// 结果放在 result 这个 map 中
    	double[] CPMI = new double[wordCount];
    	double Max_CPMI = 0.0 - sentenceCount;
    	for (int i=0; i<wordCount; i++) {
    		CPMI[i] = 0.0;
    		for (int j=0; j<wordCount; j++) {
    			if (i == j) {
    				continue;
    			}
    			double temp1 = 0.0;
    			if(i < j) {
    				temp1 = (double)sentenceCount * (double)(N3[i][j] + 1);
    			} else {
    				temp1 = (double)sentenceCount * (double)(N3[j][i] + 1);
    			}
    			
    			double temp2 = (double)(N12[i] + 1) * (double)(N12[j] + 1);
//    			if (i == 0) {
//    				System.out.println(sentenceCount + " * " + N3[i][j] + " / " + N12[i] + " * " + N12[j] + " = " + Math.log(temp1 / temp2 + 1) + " ");
//    			}
    			CPMI[i] += Math.log(temp1 / temp2 + 1);
    		}
//    		if (i == 0) {
//    			System.out.println(CPMI[i]);
//    		}
    		if (CPMI[i] > Max_CPMI) {
				Max_CPMI = CPMI[i];
			}
    	}
    	for (int i=0; i<wordCount; i++) {
    		double cmpi = CPMI[i] / Max_CPMI;
    		topNWordList.get(i).setCpmi(cmpi);
//    		System.out.println(topNWordList.get(i).getName() + " : " + cmpi);
    	}
		return topNWordList;
    }
    
    
    /** 
     * 循环二分查找，返回第一次出现该值的位置 
     * @param sortedData    已排序的数组 
     * @param findValue     需要找的值 
     * @return              值在数组中的位置，从0开始。找不到返回-1 
     */  
    private int searchLoop(Integer[] sortedData, int findValue, int startIndex) {  
        int start = startIndex;  
        int end = sortedData.length - 1;  
          
        while(start <= end) {  
            //中间位置  
            int middle = (start + end) / 2;
            //中值  
            int middleValue = sortedData[middle];  
              
            if (findValue == middleValue) {  
                //等于中值直接返回  
                return middle;  
            } else if (findValue < middleValue) {  
                //小于中值时在中值前面找  
                end = middle - 1;  
            } else {  
                //大于中值在中值后面找  
                start = middle + 1;  
            }  
        }  
        //找不到  
        return -1;  
    }  
    
//    /**
//     * 初始化获取 CPMI所需的信息
//     * 主要有 sentenceCount、每个热词在出现在多少个文档中、每个热词与其他热词的 PMI
//     */
//	public void initCPMI(List<Word> topNWordList, List<List<String>> sentenceList, int topNum) {
//    	int wordCount = topNWordList.size();
//    	sentenceCount = sentenceList.size();
//    	
//    	// 得到所有每次热词在每个句子中的存在情况
//    	// 结果存在二维数组 N 中,如果存在则为1
//    	int[][] N = new int[wordCount][sentenceCount];
//    	
//    	N12 = new int[wordCount];
//    	N3 = new int[wordCount][wordCount];
//    	
//    	
//    	for (int i=0; i<wordCount; i++) {
//    		Word topNword = topNWordList.get(i);
//    		int n12 = 0;
//			for (int j=0; j<sentenceCount; j++) {
//				List<String> sentence = sentenceList.get(j);
//				if(sentence.contains(topNword.getName())) {
//					N[i][j] = 1;
//				}
//				n12 += N[i][j];
//				if(i > 0) {
//					for (int z=i; z>0; z--) {
//						if ((N[z-1][j] + N[i][j]) == 2) {
//							N3[z-1][i] += 1; 
//							N3[i][z-1] += 1;
//						}
//					}
//				}
//			}
//			N12[i] = n12;
//		}
//    	
//    	// 输出 N3
//    	for (int i=0; i<wordCount; i++) {
//    		for (int j=0; j<wordCount; j++) {
//				System.out.print(N3[i][j] + " ");
//    		}
//    		System.out.println();
//    	}
//    	System.out.println();
//
//    	// 输出 N12
//    	for (int i=0; i<wordCount; i++) {
//    		System.out.print(N12[i] + " ");
//    	}
//    	System.out.println();
//    	System.out.println();
//    	
//    	return;
//    }
}
