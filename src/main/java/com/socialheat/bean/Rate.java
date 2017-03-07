package com.socialheat.bean;

import java.util.Map;

/**
 * 保存时间间隔内的热度的相关信息
 */
public class Rate {

	// 时间间隔内的热度
    private double rate;
    // 开始时间
    private long startTime;
    // 结束时间
    private long endTime;
    // 此段时间内的所句子总数
    private int sentenceCount;
    // 此段时间内所有词语出现的次数统计
    private Map<String,Integer[]> wordCountMap;

    public Rate(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

	public Map<String, Integer[]> getWordCountMap() {
		return wordCountMap;
	}

	public void setWordCountMap(Map<String, Integer[]> wordCountMap) {
		this.wordCountMap = wordCountMap;
	}

	public int getSentenceCount() {
		return sentenceCount;
	}

	public void setSentenceCount(int sentenceCount) {
		this.sentenceCount = sentenceCount;
	}
    

}
