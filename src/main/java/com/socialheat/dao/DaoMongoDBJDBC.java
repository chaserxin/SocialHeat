package com.socialheat.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DaoMongoDBJDBC {
	
	MongoClient mongoClient = null;
	
	public MongoDatabase getConnection() {
		
		// 连接到 mongodb 服务
		mongoClient = new MongoClient( "localhost" , 27017 );
	    // 连接到数据库
	    MongoDatabase mongoDatabase = mongoClient.getDatabase("socialheat");  
	    
	    return mongoDatabase;
	}
	
	public void close() {
		mongoClient.close();
	}
	
	
	public static void main(String[] args) {
//		MongoDBJDBC mongoDBJDBC = new MongoDBJDBC();
//		MongoDatabase mongoDatabase = mongoDBJDBC.getConnection();
//		MongoCollection<Document> collection = mongoDatabase.getCollection("pokemon_query");
//		MongoCursor<Document> cursor = collection.find().sort(new BasicDBObject("create_time",-1)).iterator();  
//		System.out.println(collection.count());
//		try {  
//		    while (cursor.hasNext()) {  
//		    	
//		    }  
//		} finally {  
//		    cursor.close();  
//		}  
	}
}
