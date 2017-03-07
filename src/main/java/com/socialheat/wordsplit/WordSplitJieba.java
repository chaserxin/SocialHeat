package com.socialheat.wordsplit;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;

public class WordSplitJieba implements WordSplit {

	private static WordDictionary wordDict = WordDictionary.getInstance();
	private JiebaSegmenter segmenter;
	
	public WordSplitJieba() {
		Path path = Paths.get("E:/SocialHeatPaper/SocialHeat/library/userdict.dic");
		wordDict.loadUserDict(path);
		segmenter = new JiebaSegmenter();
	}

	public List<String> run(List<String> sentenceList) {
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
	public List<List<String>> splitSencenceList(List<String> sentenceList) {
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
	
	public static void main(String[] args) {
		WordSplit wordSplit = new WordSplitJieba();
		List<String> sentenceList = new ArrayList<String>();
		sentenceList.add("经过了很久很久的东方之星.如大家所看.最后一次事件纪要是一年半以前.没什么好说的了.之前因为我的不负责任让这个项目停滞了好久.这次做了很多改进.断断续续,大致如下");
		List<String> result = wordSplit.run(sentenceList);
		for (String string : result) {
			System.out.println(string);
		}
	}

	public List<String> splitSencence(String sentence) {
		List<String> result = new ArrayList<String>();

		List<SegToken> segTokens = segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);
		for (SegToken segToken : segTokens) {
			result.add(segToken.word);
		}
		return result;
	}
}
