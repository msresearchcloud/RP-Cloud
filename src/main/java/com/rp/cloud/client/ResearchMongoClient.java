package com.rp.cloud.client;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

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
		MongoClientURI uri = new MongoClientURI("mongodb://researchportal-db:rWNfcYwatkA5pkpLikqM9tNIO3yx78Lv8ru7FfcUc69sxDg6OeSalsZFrTje1ICLw0qbBVniNOSpG88aiJwW2A==@researchportal-db.mongo.cosmos.azure.com:10255/?ssl=true&replicaSet=globaldb&retrywrites=false&maxIdleTimeMS=120000&appName=@researchportal-db@");
		mongoClient = new MongoClient(uri);  
		database = mongoClient.getDatabase("researchportal-db");
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


	public String updateFeedDetails(UserSubscriptionDetails userSubscriptionDetails) {
		try {
			logger.info("Hitting DB to update feed details for user : " + userSubscriptionDetails.getUserId());
			String message = null;
			MongoCollection<Document> collection = database.getCollection("research-user-event");
			List<UserSubscriptionDetails> existingSubs = getFeedDetails(userSubscriptionDetails.getUserId());
			if (CollectionUtils.isNotEmpty(existingSubs)) {
				List<String> existingDocs = existingSubs.stream().flatMap(e -> e.getDocs().stream()).collect(Collectors.toList());
				userSubscriptionDetails.getDocs().addAll(existingDocs);
				BasicDBObject query = new BasicDBObject();
				query.append("userId", userSubscriptionDetails.getUserId());
				ObjectMapper mapper = new ObjectMapper();
				Document dbObject = Document.parse(mapper.writeValueAsString(userSubscriptionDetails));
				collection.findOneAndReplace(query, dbObject);
				message = String.format("Record updated for userId : %s", userSubscriptionDetails.getUserId());
			} else {
				//Insert the data
				BasicDBObject query = new BasicDBObject();
				query.append("_id", userSubscriptionDetails.getUserId());
				ObjectMapper mapper = new ObjectMapper();
				Document dbObject = Document.parse(mapper.writeValueAsString(userSubscriptionDetails));
				collection.insertOne(dbObject);
				message = String.format("Record inserted for userId : %s", userSubscriptionDetails.getUserId());
			}
			return message;
		} catch (Exception e) {
			String message = String.format("Exception occur while inserting detail in db for user : %s", userSubscriptionDetails.getUserId());
			logger.info(message);
			logger.info(e.getStackTrace().toString());
			return message;
		}
	}

	private List<UserSubscriptionDetails> parseData(FindIterable<Document> queryResult) throws Exception {
		List<UserSubscriptionDetails> userSubscriptionDetails = new ArrayList<>();
		final ObjectMapper mapper = new ObjectMapper();
		if (queryResult != null) {
			for (Document doc : queryResult) {
				UserSubscriptionDetails userSubsricption = mapper.readValue(doc.toJson(), UserSubscriptionDetails.class);
				userSubscriptionDetails.add(userSubsricption);
			}
		}
		return userSubscriptionDetails;
	}
}
