package com.socialheat.wordsplit;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class WordSplitIKAnalyzer implements WordSplit {
	
	public List<String> run(List<String> sentenceList) {
    	List<String> result = new ArrayList<String>();
    	
        System.out.println("开始进行分词...");

        for(String sentence : sentenceList){
            StringReader sr = new StringReader(sentence);
            IKSegmenter ik = new IKSegmenter(sr, true);
            Lexeme lex = null;
            try {
				while((lex=ik.next()) != null){
				    String it = lex.getLexemeText();
				    result.add(it);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return result;
    }

	public List<List<String>> splitSencence(List<String> sentenceList) {
		return null;
	}
}
