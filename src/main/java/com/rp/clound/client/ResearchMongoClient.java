package com.rp.clound.client;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Component
public class ResearchMongoClient {

	private static MongoClient mongoClient = null;
	private static MongoDatabase database = null;
	private static  final Logger logger = LoggerFactory.getLogger(ResearchMongoClient.class);
	
	public ResearchMongoClient() {
		MongoClientURI uri = new MongoClientURI("mongodb://researchportal:P1m2u4ugPCZ7EnpT84KzhFnyHahV6TmDn3hDHbrBOsfwlq0bfgDeJH9xeDPVUl17zYa9ybTW0dH53Fwhp58TiQ==@researchportal.mongo.cosmos.azure.com:10255/?ssl=true&replicaSet=globaldb&retrywrites=false&maxIdleTimeMS=120000&appName=@researchportal@");
		mongoClient = new MongoClient(uri);  
	    database = mongoClient.getDatabase("researchportal");
	}
	
	public String getFeedDetails() {
		 
		 MongoCollection<Document> collection = database.getCollection("research-user-event");
		 Document queryResult = collection.find().first();
		 logger.info(queryResult.toJson()); 
		 return queryResult.toJson();
	}
}
