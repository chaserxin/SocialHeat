package com.socialheat.dao;

import java.util.List;

import com.socialheat.bean.Rate;

public interface DaoInterface {
	public List<String> getSentenceList();
	
	public List<Rate> getSentenceListByTime(int span);
	
	public String getName();
}
