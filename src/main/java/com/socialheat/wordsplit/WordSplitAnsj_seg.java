package com.socialheat.wordsplit;

import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

public class WordSplitAnsj_seg implements WordSplit {
	
	public WordSplitAnsj_seg() {
//		// 添加自定义字典 1
//		for (int i = 0; i < DictionaryUtil.dictionary.length; i++) {
//            System.out.println(DictionaryUtil.dictionary[i]);
//            UserDefineLibrary.insertWord(DictionaryUtil.dictionary[i], "userDefine", 1000);
//        }
//		// 添加自定义字典 2
//		MyStaticValue.userLibrary = "E:/SocialHeatPaper/SocialHeat/library/userLibrary.dic";
	}

    public List<String> run(List<String> sentenceList) {
        List<String> result = new ArrayList<String>();

        for(String sentence : sentenceList){
            List<Term> wordSplitResult = ToAnalysis.parse(sentence);
            for (Term term : wordSplitResult) {
                String name = term.getName();
                result.add(name);
            }
        }
        return result;
    }
    
    /*
     * 将每个句子单独切词,每个句子的切词结果存放在一个 sentences 列表中
     * 然后将每个句子切词后得到的 sentences 存放在一个 result 列表中
     */
	public List<List<String>> splitSencence(List<String> sentenceList) {
		List<List<String>> result = new ArrayList<List<String>>();
		 
		for (String sentence : sentenceList) {
				List<String> sentences = new ArrayList<String>();
				List<Term> wordSplitResult = ToAnalysis.parse(sentence);
				for (Term term : wordSplitResult) {
					String name = term.getName();
					sentences.add(name);
				}
				result.add(sentences);
		}
		return result;
	}
}
