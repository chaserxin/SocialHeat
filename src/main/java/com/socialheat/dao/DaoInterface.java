package com.socialheat.dao;

import java.util.List;

import com.socialheat.wordsplit.WordSplit;

public interface DaoInterface {
	public List<String> getSentenceList();
	
	public List<String> getSentenceListByStream(int start);
	
	public String getName();
	
	public List<List<String>> getSplitSentenceList(WordSplit wordSplit);
	
	public long getStartTime();
}
