package com.socialheat.util.nlpir;

/**
 * Created by sl on 16-6-2.
 */
public class WordFrequency {

    public String[] wordFre(String filePath , int keyNum){
        NLPIR nlpir = new NLPIR();
        nlpir.Instance.NLPIR_Init(nlpir.getSystemFolder(),nlpir.getCharsetType() ,nlpir.getSystemCharset());
        String nativeBytes = null;

        System.out.println("filePath: "+filePath);

        nativeBytes = nlpir.Instance.NLPIR_GetFileKeyWords(filePath,keyNum,false);
        System.out.println("result : " + nativeBytes);

        String[] keywords = nativeBytes.split("#");

        String result = null;
        for(String keyword : keywords){
            result = result + " " + keyword;
        }
        return keywords;
    }


}
