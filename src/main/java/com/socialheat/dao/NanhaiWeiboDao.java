package com.socialheat.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.socialheat.wordsplit.WordSplit;

public class NanhaiWeiboDao implements DaoInterface {

	private MongoDBJDBC mongoDBJDBC;
	
	public NanhaiWeiboDao() {
		mongoDBJDBC = new MongoDBJDBC();
	}
	
	/**
	 * 得到微博的评论
	 */
	public List<List<String>> getSplitSentenceList(WordSplit wordSplit) {
		List<List<String>> result = new ArrayList<List<String>>();

        // 获取数据库连接
		MongoDatabase mongoDatabase = mongoDBJDBC.getConnection();

		MongoCollection<Document> collection = mongoDatabase.getCollection("nanhai_weibo");
		
		long count = collection.count();
		
//		MongoCursor<Document> cursor = collection.find().sort(new BasicDBObject("create_time",-1)).limit(936378).iterator();  

		MongoCursor<Document> cursor = collection.find().iterator();  
		long i = 0;
		try {  
		    while (cursor.hasNext()) {  
		    	i++;
//		    	if (i == 3000000)
//		    		break;
		    	System.out.println(i);
		    	List<String> wordList = wordSplit.splitSencence(cursor.next().getString("text"));
		    	result.add(wordList);
		    }  
		} finally {  
		    cursor.close();  
		}  
		
		System.out.println("============================");
		System.out.println("读入了"+ i +"条数据（微博评论）");
        System.out.println("============================");
        return result;
	}

	public String getName() {
		return "Nanhai_Weibo";
	}

	public List<String> getSentenceListByStream(int start) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getSentenceList() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getStartTime() {
		// TODO Auto-generated method stub
		return 0;
	}

}
