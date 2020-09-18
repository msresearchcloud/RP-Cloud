package com.rp.clound.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rp.clound.client.ResearchMongoClient;
import com.rp.clound.service.FeedService;

@Service
public class FeedServiceImpl implements FeedService {

	@Autowired
	private ResearchMongoClient client;
	
	@Override
	public String getFeed() {
		return client.getFeedDetails();
	}

}
