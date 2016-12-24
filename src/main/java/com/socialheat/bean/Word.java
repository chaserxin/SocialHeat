package com.socialheat.bean;

/**
 * Created by sl on 16-10-24.
 * 词语对象
 * 保存词语相关的信息
 */
public class Word {
	// 词语
    private String name;
    // 出现次数
    private int times;
    // TF 词频
    private double tf;
    // IDF
    private double idf;
    // CPMI
    private double cpmi;
    
    // 包含该热词的文档数 +1
    private int times_sentence;
    // 词语的长度
    private int length;
    // TF 乘以 IDF 的结果
    private double tf_idf;
    // TF 乘以 IDF 乘以 Length 的最终结果
    private double tf_idf_length;
    // TF 乘以 IDF 乘以 Length 乘以 CPMI 的最终结果
    private double tf_idf_length_cpmi;
    
    public Word(String name, int times, int length) {
        this.name = name;
        this.times = times;
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

	public double getTf_idf_length_cpmi() {
		return tf_idf_length_cpmi;
	}

	public void setTf_idf_length_cpmi(double tf_idf_length_cpmi) {
		this.tf_idf_length_cpmi = tf_idf_length_cpmi;
	}

	public double getCpmi() {
		return cpmi;
	}

	public void setCpmi(double cpmi) {
		this.cpmi = cpmi;
	}
    
	
}
