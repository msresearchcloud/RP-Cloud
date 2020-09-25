package com.rp.cloud.client;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rp.cloud.response.UserSubscriptionDetails;

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

	public List<UserSubscriptionDetails> getFeedDetails(String userId) {
		try { 
			logger.info("Hitting DB to fetch feed details for user : "+userId);
			MongoCollection<Document> collection = database.getCollection("research-user-event");
			BasicDBObject whereQuery = new BasicDBObject();
		    whereQuery.put("userId", userId);
			FindIterable<Document> queryResult = collection.find(whereQuery);
			List<UserSubscriptionDetails> userSubscriptionDetails =  parseData(queryResult);
			logger.info("Total record fetch from DB is : "+userSubscriptionDetails.size());
			return userSubscriptionDetails;
		} catch(Exception e) {
			logger.info("Exception occur while fetching detail from db for user : "+userId);
			e.printStackTrace();
			return null;
		}
	}

	private List<UserSubscriptionDetails> parseData(FindIterable<Document> queryResult) throws Exception {
		List<UserSubscriptionDetails> userSubscriptionDetails = new ArrayList<>();
		final ObjectMapper mapper = new ObjectMapper();
		for (Document doc : queryResult) {
			UserSubscriptionDetails userSubsricption =  mapper.readValue(doc.toJson(), UserSubscriptionDetails.class);
			userSubscriptionDetails.add(userSubsricption);
		}
		return userSubscriptionDetails;
	}
}