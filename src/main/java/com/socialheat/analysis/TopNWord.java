package com.socialheat.analysis;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.socialheat.bean.Word;
import com.socialheat.dao.StopWordDao;


public class TopNWord {
	
	// 统计分词好的结果中每次词语出现的次数
	private	Map<String, Word> wordMap;
	// 记录当前的句子数
	private int index;
	// 停用词集合
	private Set<String> stopWordSet;
	// 所有词语总数(含重复词语)
	private long wordSum = 0L;
	// 用于统计 CPMI
	private WordCPMI wordCPMI;
	
	// 当前取出来所有句子数
	// 每次都是新的
	private int newSentenceCount;
	// 当前取出来所有词语数
	// 每次都是新的
	private int newWordCount;
	// 统计当前遍历取出来的所有词语出现的次数
	// 每次都是新的
	private Map<String,Integer[]> newWordCountMap;
	
	
	
	public TopNWord(int topNum) {
		wordCPMI = new WordCPMI(topNum);
		wordMap = new HashMap<String, Word>();
		index = 0;
	}
	
    /**
     * 
     * @param wordList 已分好的词语，尚未进行停词表处理和统计
     * @param sentenceList 所有的句子列表
     * @param topNum
     * @return
     * @throws IOException
     */
    public List<Word> getWordTopN(List<String[]> splitSencenceList, int topNum) {
    	newSentenceCount = splitSencenceList.size();
    	
    	if (index == 0) {
    		// 获取停用词
    		StopWordDao stopWordDao = new StopWordDao();
    		stopWordSet = stopWordDao.getStopWord();
    		stopWordSet.add("逾");
    		stopWordSet.add("跌");
    		stopWordSet.add("股");
    		stopWordSet.add("🇨");
    		stopWordSet.add("🇳");
    		stopWordSet.add("388");
    	}
    	
    	// 统计所有词语总数(含重复词语)
//		System.out.println("\n开始统计所有词语并去重！ 开始时间为：" + TimeUtil.currentTime());
		statWords(splitSencenceList);
//		System.out.println("共有：" + wordSum + "个词语（含重复词语）！");
//		System.out.println("共有：" + wordMap.size() + "个词语（无重复词语）！");
//		System.out.println("统计所有词语并去重结束！ 结束时间为：" + TimeUtil.currentTime());
//		System.out.println();
		
		// 计算 TF * IDF * length
//		System.out.println("开始计算 TF-IDF！ 开始时间为：" + TimeUtil.currentTime());
		List<Word> allWordList = getTF_IDF_Length(wordMap);
//		System.out.println("计算 TF-IDF结束！ 结束时间为：" + TimeUtil.currentTime());
		
		// 得到前 topNum 个热词
		List<Word> topNWordList = getTopN(topNum, allWordList);
		
		// 第一次计算 CPMI 先初始化
//		System.out.println("初始化 CPMI 开始！ 开始时间为：" + TimeUtil.currentTime());
		wordCPMI.initCPMI(topNWordList, newSentenceCount);
//		System.out.println("初始化 CPMI 结束！ 结束时间为：" + TimeUtil.currentTime() + "\n");
		
		// 得到 CPMI 和权重
		topNWordList = getCPMIAndWeight(topNWordList);
     	
		// 使用删词算法进行删词
		List<Word> hotWordList = deleteHotWord(topNWordList);
		
     	// 得到前 topNum 的热词的权重
	    return hotWordList;
	}
    
    public int getNewSentenceCount() {
		return newSentenceCount;
	}
    
	public int getNewWordCount() {
		return newWordCount;
	}

	public void setNewWordCount(int newWordCount) {
		this.newWordCount = newWordCount;
	}

	public Map<String, Integer[]> getNewWordCountMap() {
		return newWordCountMap;
	}
    
	
	
	
	
	/**
	 * 得到 CPMI 和权重 
	 * @param topNWordList
	 * @return
	 */
    private List<Word> getCPMIAndWeight(List<Word> topNWordList) {
    	List<Word> resultList = new ArrayList<Word>();
    	// 得到前 topNum 的热词的 CPMI
     	topNWordList = wordCPMI.getCPMI(topNWordList);
     	// 重新排序
    	Collections.sort(topNWordList,new Comparator<Word>(){
            public int compare(Word a, Word b) {
            	BigDecimal data1 = new BigDecimal(b.getTf_idf_length()*b.getCpmi()); 
            	BigDecimal data2 = new BigDecimal(a.getTf_idf_length()*a.getCpmi()); 
                return data1.compareTo(data2);
            }
        });
    	
    	for (Word word : topNWordList) {
    		word.setWeight(word.getTf_idf_length() * word.getCpmi());
    			resultList.add(word);
		}
    	return resultList;
	}
	
    /**
     * 统计此次数据流过来的词语
     * @param splitSencenceList
     */
    private void statWords(List<String[]> splitSencenceList) {
    	newWordCountMap = new HashMap<String, Integer[]>();
    	newWordCount = 0;
		for (int i=0; i<splitSencenceList.size(); i++) {
			String[] words = splitSencenceList.get(i);
			for(String wordstring : words){
				// 过滤掉停用词和空格
				if (!stopWordSet.contains(wordstring) && !wordstring.equals(" ")) {
			        if (!wordMap.containsKey(wordstring)) {
			        	Word word = new Word(wordstring, 1);
			        	// 初始化 sentenceList
			        	word.lastSentenceIndex = i + index;
			            word.sentenceList.add(i + index);
			            wordMap.put(wordstring, word);
			        } else {
			        	Word word = wordMap.get(wordstring);
			        	word.setTimes(word.getTimes() + 1);
			        	if (i + index != word.lastSentenceIndex) {
			        		word.sentenceList.add(i + index);
			        	}
			        	wordMap.put(wordstring, word);
			        }
			        
			        if (!newWordCountMap.containsKey(wordstring)) {
			        	Integer[] intArr = new Integer[3];
			        	intArr[0] = 1;
			        	intArr[1] = 1;
			        	intArr[2] = i;
			        	newWordCountMap.put(wordstring, intArr);
			        } else {
			        	Integer[] intArr = newWordCountMap.get(wordstring);
			        	if(intArr[2] == i) {
			        		intArr[0] ++;
			        	} else {
			        		intArr[0] ++;
			        		intArr[1] ++;
			        		intArr[2] = i;
			        	}
			        	newWordCountMap.put(wordstring, intArr);
			        }
			        newWordCount++;
			        wordSum ++;
				} else {
					continue;
				}
			}
		}
		// index 叠加
		index += splitSencenceList.size();
    }
    

    /**
     * 修改接口，一次性计算 TF_IDF_Length
     * @param wordMap
     * @return
     */
    private List<Word> getTF_IDF_Length(Map<String, Word> wordMap) {
    	List<Word> WordList = new ArrayList<Word>();
    	
    	Iterator<Entry<String, Word>> iter = wordMap.entrySet().iterator();
    	while (iter.hasNext()) {
    		Map.Entry<String, Word> entry = iter.next();
    		Word word = entry.getValue();
    		// docCount 为包含该热词的文档总数
    		int docCount = word.sentenceList.size();
    		
    		// 计算总文档数的开方,用于保证所选择的热词出现在的文档数大于等于总文档数的开方
    		if(docCount >= (int) Math.sqrt(index)) {
    			// 计算词频 TF
    		    double tf = word.getTimes() / (double)wordSum;
    		    // 计算 IDF 
                double idf = Math.log((double)index / (double)(docCount+1));
                // 计算长度的 log
                double len = Math.log(word.getName().length()) / Math.log(2);
    		    
                // 保存 TF
                word.setTf(tf);
                // 保存 IDF
                word.setIdf(idf);
                // 保存单独的 TF * IDF
                word.setTf_idf(tf * idf);
                // 保存 TF * IDF * log(len)
                word.setTf_idf_length(tf * idf * len);
                
                WordList.add(word);
    		}
    		
    	}
    	return WordList;
    }

    /**
     * 得到前 TopN 的热词
     * @param topNum
     * @param wordList
     * @return
     */
    private List<Word> getTopN(int topNum, List<Word> wordList) {
        List<Word> wordListTopN = new ArrayList<Word>();

    	// 重新排序
    	Collections.sort(wordList,new Comparator<Word>(){
            public int compare(Word a, Word b) {
                return (int)((b.getTf_idf_length() - a.getTf_idf_length()) * 1000000);
            }
        });

        int topNum1 = 0;
        for (Word word : wordList) {
        	if (word.getTf_idf_length() > 0) {
        		word.setIndex(topNum1);
        		wordListTopN.add(word);
        		topNum1 ++;
			}
			if (topNum1 == topNum) {
				break;
			}
        }
 		
        return wordListTopN;
    }
    
    /**
     * 此循环为删词循环:
     * 1.将所有热词分为20等份
     * 2.得到20个坐标点，每个点的横坐标X为热词个数，纵坐标为这X个热词的总权重之和，画出一条曲线
     * 3.将该曲线的起点A与终点B相连，旋转曲线，使该曲线最终以直线AB为X轴
     * 4.得到中间18个点中距离AB最远的点，该点的横坐标即为热词个数
     * 
     * @param wordList
     * @return
     */
    private List<Word> deleteHotWord(List<Word> topNWordList) {
    	// 将所有热词分为20等份
		if (topNWordList.size() >= 20) {
			// 每份的词语个数
			int spanWord = topNWordList.size() / 20;
			// 分成20份后还剩下的额外的词语数
			// 这些词语将被加到前面的extraWord份中，每份加一个
			int extraWord = topNWordList.size() % 20;
			// 前N个词语的总权重列表
			List<Double> allWeightList = new ArrayList<Double>();
			// 词语数列表
			List<Integer> wordCountList = new ArrayList<Integer>();
			// 总权重的和
			double allWeight = 0.0;

			System.out.println(
					"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println(
					"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

			// 求出 1.前N个词语的总权重列表allWeightList 2.词语数列表wordCountList
			for (int j = 1; j <= topNWordList.size(); j++) {

				Word word = topNWordList.get(j - 1);
				System.out.println(word.getName() + " --------- 出现次数: " + word.getTimes()
						+ " --------- TF-IDF-Len: " + word.getTf_idf_length() + " --------- CMPI: " + word.getCpmi()
						+ " --------- 权重为: " + word.getWeight());

				allWeight += topNWordList.get(j - 1).getWeight();
				if (extraWord > 0) {
					if (j % (spanWord + 1) == 0) {

						System.out.println();
						System.out.println("总权重为：" + allWeight + " 总词语数为：" + j);
						System.out.println();

						allWeightList.add(allWeight);
						wordCountList.add(j);
						extraWord--;
					}
				} else {
					if ((j - topNWordList.size() % 20) % spanWord == 0) {

						System.out.println();
						System.out.println("总权重为：" + allWeight + " 总词语数为：" + j);
						System.out.println();

						allWeightList.add(allWeight);
						wordCountList.add(j);
					}
				}

			}

			System.out.println(
					"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println(
					"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

			System.out.println();
			System.out.print("原始的Y坐标：[");
			for (int j = 0; j < allWeightList.size(); j++) {
				System.out.print(allWeightList.get(j));
				if (j < allWeightList.size() - 1) {
					System.out.print(", ");
				}
			}
			System.out.print("]");
			
			System.out.println();
			System.out.print("X坐标：[");
			for (int j = 0; j < wordCountList.size(); j++) {
				System.out.print(wordCountList.get(j));
				if (j < wordCountList.size() - 1) {
					System.out.print(", ");
				}
			}
			System.out.print("]");
			
			// 已第一个点P1和最后一个点P19的连线为X轴建立坐标系，得到中间的18个点中，距离新的X轴的最大距离的点
			// 计算方法：根据 Px、P1、P19 三个点为三角形，用海伦公式计算出三角形的面积Sx，最后用Sx除以P1到P19两点间的距离得到最大距离
			double x1 = wordCountList.get(0);
			double y1 = allWeightList.get(0);
			double x2 = wordCountList.get(19);
			double y2 = allWeightList.get(19);
			double maxDistance = 0.0;
			int maxDistanceWordcount = 0;
			List<Double> distanceList = new ArrayList<Double>();
			distanceList.add(0.0);
			for (int index = 1; index < allWeightList.size()-1; index++) {
				double distance = pointToLine(x1, y1, x2, y2, wordCountList.get(index), allWeightList.get(index));
				distanceList.add(distance);
				if(distance > maxDistance) {
					maxDistance = distance;
					maxDistanceWordcount = wordCountList.get(index);
				}
			}
			distanceList.add(0.0);
			
			System.out.println();
			System.out.print("函数变换后的Y坐标：[");
			for (int j = 0; j < distanceList.size(); j++) {
				System.out.print(distanceList.get(j));
				if (j < distanceList.size() - 1) {
					System.out.print(", ");
				}
			}
			System.out.print("]");
			System.out.println();
			
			System.out.println("最好的情况是：" + maxDistanceWordcount + " 距离：" + maxDistance);
			
			return topNWordList.subList(0, maxDistanceWordcount);
		} else {
			return topNWordList;
		}
    }
    
    /**
     * 点到线段的最短距离, 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x
     * @param y
     * @return
     */
 	private double pointToLine(double x1, double y1, double x2, double y2, double x, double y) {
 		double ans = 0;
 		double a, b, c;
 		a = Math.hypot(x1 - x2, y1 - y2);
 		b = Math.hypot(x1 - x, y1 - y);
 		c = Math.hypot(x2 - x, y2 - y);
 		// 点在线段上
 		if (c + b == a) {
 			ans = 0;
 			return ans;
 		}
 		// 不是线段，是一个点
 		if (a <= 0.00001) {
 			ans = b;
 			return ans;
 		}
 		// 组成直角三角形或钝角三角形，p1为直角或钝角
 		if (c * c >= a * a + b * b) { 
 			ans = b;
 			return ans;
 		}
 		// 组成直角三角形或钝角三角形，p2为直角或钝角
 		if (b * b >= a * a + c * c) {
 			ans = c;
 			return ans;
 		}
 		// 组成锐角三角形，则求三角形的高
 		// 半周长
 		double p0 = (a + b + c) / 2;
 		// 海伦公式求面积
 		double s = Math.sqrt(p0 * (p0 - a) * (p0 - b) * (p0 - c));
 		// 返回点到线的距离（利用三角形面积公式求高）
 		ans = 2 * s / a;
 		return ans;
 	}
}
