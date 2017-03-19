package com.socialheat.dao;

import java.util.List;

public interface DaoInterface {
	public List<String> getSentenceList();
	
	public List<String> getSentenceListByStream(int span);
	
	public String getName();
	
	public long getStartTime();
	
	public List<String[]> getSplitSentenceListByStream(int span);
}
