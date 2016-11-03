package com.socialheat.bean;

/**
 * Created by sl on 16-10-24.
 */
public class Word {
    private String name;
    private int times;
    private double tf;
    private double idf;
    private int times_sentence;
    private int length;
    private double tf_idf;
    private double tf_idf_length;

    public Word(String name, int times, double tf , int length) {
        this.name = name;
        this.times = times;
        this.tf = tf;
        this.length = length;
    }

    public Word(String name, int times) {
        this.name = name;
        this.times = times;
    }

    public Word(){

    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getTf_idf_length() {
        return tf_idf_length;
    }

    public void setTf_idf_length(double tf_idf_length) {
        this.tf_idf_length = tf_idf_length;
    }


    public double getTf_idf() {
        return tf_idf;
    }

    public void setTf_idf(double tf_idf) {
        this.tf_idf = tf_idf;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public double getTf() {
        return tf;
    }

    public void setTf(double tf) {
        this.tf = tf;
    }

    public double getIdf() {
        return idf;
    }

    public void setIdf(double idf) {
        this.idf = idf;
    }

    public int getTimes_sentence() {
        return times_sentence;
    }

    public void setTimes_sentence(int times_sentence) {
        this.times_sentence = times_sentence;
    }
}
