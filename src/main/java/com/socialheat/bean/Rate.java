package com.socialheat.bean;

import java.util.List;

/**
 * Created by sl on 16-10-31.
 * 保存时间间隔内的热度的相关信息
 */
public class Rate {

	// 时间间隔内的热度
    private double rate;
    // 时间间隔内的弹幕数
    private List<String> strings;
    // 开始时间
    private long startTime;
    // 结束时间
    private long endTime;

    public Rate(List<String> strings, long startTime, long endTime) {
        this.strings = strings;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
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
}
