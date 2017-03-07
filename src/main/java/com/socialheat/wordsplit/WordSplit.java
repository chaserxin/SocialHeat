package com.socialheat.wordsplit;

import java.util.List;

/**
 * 分词
 */
public interface WordSplit {


    public List<String> run(List<String> sentenceList);
    
    /*
     * 将每个句子单独切词,每个句子的切词结果存放在一个 sentences 列表中
     * 然后将每个句子切词后得到的 sentences 存放在一个 result 列表中
     */
    public List<List<String>> splitSencenceList(List<String> sentenceList);
    
    public List<String> splitSencence(String sentence);
}
