package com.socialheat.jieba;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;
import com.socialheat.util.DataQuery;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sl on 16-10-20.
 */
public class WordSplit {

    private static WordDictionary wordDict = WordDictionary.getInstance();

    public List<String> run() throws IOException {

        Path path = Paths.get("/home/sl/SocialHeat/1.dict");
        wordDict.loadUserDict(path);

        JiebaSegmenter segmenter = new JiebaSegmenter();

        DataQuery query = new DataQuery();

        List<String> queryResult = query.queryDoubleColumn();

        List<String> result = new ArrayList<String>();

        for (String sentence : queryResult) {
            List<SegToken> segTokens = segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);
            for(SegToken segToken : segTokens){
                result.add(segToken.word);
            }
        }

        return result;

    }


    public List<String> runByWords(List<String> words) throws IOException {

        Path path = Paths.get("/home/sl/SocialHeat/1.dict");
        wordDict.loadUserDict(path);

        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<String> result = new ArrayList<String>();

        for (String sentence : words) {
            List<SegToken> segTokens = segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);
            for(SegToken segToken : segTokens){
                result.add(segToken.word);
            }
        }

        return result;

    }

}
