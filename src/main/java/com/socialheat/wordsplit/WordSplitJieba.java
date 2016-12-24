package com.socialheat.wordsplit;

import java.util.ArrayList;
import java.util.List;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;

public class WordSplitJieba implements WordSplit {

//	private static WordDictionary wordDict = WordDictionary.getInstance();

	public List<String> run(List<String> sentenceList) {

//		Path path = Paths.get("E:/SocialHeatPaper/SocialHeat/library/userLibrary.dic");
//		wordDict.loadUserDict(path);
		JiebaSegmenter segmenter = new JiebaSegmenter();

		List<String> result = new ArrayList<String>();

		for (String sentence : sentenceList) {
			List<SegToken> segTokens = segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);
			for (SegToken segToken : segTokens) {
				result.add(segToken.word);
			}
		}

		return result;
	}

	/*
	 * 将每个句子单独切词,每个句子的切词结果存放在一个 sentences 列表中 然后将每个句子切词后得到的 sentences 存放在一个
	 * result 列表中
	 */
	public List<List<String>> splitSencence(List<String> sentenceList) {

		JiebaSegmenter segmenter = new JiebaSegmenter();

		List<List<String>> result = new ArrayList<List<String>>();

		for (String sentence : sentenceList) {
			List<String> sentences = new ArrayList<String>();
			List<SegToken> segTokens = segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);
			for (SegToken segToken : segTokens) {
				sentences.add(segToken.word);
			}
			result.add(sentences);
		}

		return result;
	}
}
