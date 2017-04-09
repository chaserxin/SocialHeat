package com.socialheat.bean;

import java.util.ArrayList;
import java.util.List;

/**
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
    
    // TF 乘以 IDF 的结果
    private double tf_idf;
    // TF 乘以 IDF 乘以 log(Len) 的最终结果
    private double tf_idf_length;
    // TF 乘以 IDF 乘以 log(Len) 乘以 cpmi 的最终结果
    private double weight;
    // 记录该热词的排名,从0开始
    private int index;
    
    // 包含该热词的文档编号 List
    public List<Integer> sentenceList = new ArrayList<Integer>();
    // 记录该热词存在的最后一个文档的index值
    public int lastSentenceIndex = 0;
    
    public Word(String name, int times) {
        this.name = name;
        this.times = times;
    }

    public Word(){
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

	public double getCpmi() {
		return cpmi;
	}

	public void setCpmi(double cpmi) {
		this.cpmi = cpmi;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
    
	
}
