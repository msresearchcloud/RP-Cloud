package com.rp.cloud.service;

import com.rp.cloud.response.FeedResponse;

public interface FeedService {

	FeedResponse getFeed(String userId);
}
