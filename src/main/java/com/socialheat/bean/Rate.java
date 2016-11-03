package com.socialheat.bean;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by sl on 16-10-31.
 */
public class Rate {

    private double rate;
    private List<String> strings;
    private long startTime;
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
