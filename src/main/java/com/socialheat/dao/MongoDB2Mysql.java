package com.socialheat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoDB2Mysql {

	private MongoDBJDBC mongoDBJDBC;
	
	public MongoDB2Mysql() {
		mongoDBJDBC = new MongoDBJDBC();
	}
	
	public static void main(String[] args) throws Exception {
		MongoDB2Mysql test = new MongoDB2Mysql();
		
		List<List<String>> dbList = new ArrayList<List<String>>();
		
        // 获取数据库连接
		MongoDatabase mongoDatabase = test.mongoDBJDBC.getConnection();
		MongoCollection<Document> collection = mongoDatabase.getCollection("dfzx_baidu");
		MongoCursor<Document> cursor = collection.find().iterator();  
		long i = 0;
		try {  
		    while (cursor.hasNext()) {  
		    	i ++;
		    	System.out.println(i);
		    	Document document = cursor.next();
		    	List<String> tempList = new ArrayList<String>();
		    	tempList.add(document.getString("text"));
		    	tempList.add(document.getString("create_time"));
		    	dbList.add(tempList);
		    }  
		} finally {  
		    cursor.close();  
		}  
		
		System.out.println("============================");
		System.out.println("读入了"+ i +"条数据");
        System.out.println("============================");
        
        
        // 获取数据库连接
		Connection conn = DaoHandler.getConnection();
		PreparedStatement pstmt = null;
		
		long j = 0;
        try { 
        	String sql = "insert into dfzx_baidu(text,create_time) values(?,?)";
        	pstmt = conn.prepareStatement(sql);
        	//优化插入第一步       设置手动提交  
            conn.setAutoCommit(false); 
            
        	for (List<String> list : dbList) {
        		j ++;
    	        pstmt.setString(1, list.get(0));
    	        pstmt.setString(2, list.get(1));
    	        
    	        //优化插入第二步       插入代码打包，等一定量后再一起插入。
    	        pstmt.addBatch(); 
    	        
    	        //每10000次提交一次 
                if((j!=0 && j%10000==0 || j==dbList.size()-1)){//可以设置不同的大小；如50，100，200，500，1000等等  
                	System.out.println(j);
                	pstmt.executeBatch();  
                    //优化插入第三步       提交，批量插入数据库中。
                    conn.commit(); 
                    //提交后，Batch清空。 
                    pstmt.clearBatch();        
                }
    		}
        } catch (SQLException e) {
			 e.printStackTrace();
        } finally {  
		     DaoHandler.close(conn);
		}  
        
        System.out.println("============================");
		System.out.println("存入了"+ j +"条数据");
        System.out.println("============================");
        
        return ;
	}

}
