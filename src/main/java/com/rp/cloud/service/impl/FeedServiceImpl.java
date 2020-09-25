package com.rp.cloud.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rp.cloud.client.ResearchMongoClient;
import com.rp.cloud.response.FeedResponse;
import com.rp.cloud.response.ResearchDocument;
import com.rp.cloud.response.UserFeedResponse;
import com.rp.cloud.response.UserSubscriptionDetails;
import com.rp.cloud.service.FeedService;

@Service
public class FeedServiceImpl implements FeedService {

	private static  final Logger logger = LoggerFactory.getLogger(FeedServiceImpl.class);
	@Autowired
	private ResearchMongoClient client;
	
	@Autowired
	private RcsServiceImpl rcsService;
	
	@Override
	public FeedResponse getFeed(String userId) {
		logger.info("Fetching feed details for user : "+userId);
		FeedResponse response = new FeedResponse();
		List<UserFeedResponse> userFeedDetails = new ArrayList<>();
		List<UserSubscriptionDetails> userSubscriptionDetails = client.getFeedDetails(userId);
		if (null == userSubscriptionDetails || userSubscriptionDetails.size()==0) {
			response.setStatus("FAILED");
			response.setErrorList(Arrays.asList("No Feed details found for user "+userId +" in DB"));
			return response;
		}
		Map<String, ResearchDocument> documentMap = rcsService.getDocumentDetails();
		userSubscriptionDetails.stream().forEach(userSub -> {
			UserFeedResponse userFeed = new UserFeedResponse(userSub.getUserId(),userSub.getSubId(), processDocDetails(userSub.getDocs(),documentMap ), userSub.getModifiedDate());
			userFeedDetails.add(userFeed);
		});
		response.setStatus("SUCCESS");
		response.setUserFeedDetails(userFeedDetails);
	    return response;
	}
	
	private List<ResearchDocument> processDocDetails(List<String> docIds, Map<String, ResearchDocument> documentMap) {
		return docIds.stream().map(id -> documentMap.get(id)).collect(Collectors.toList());
	}

}
