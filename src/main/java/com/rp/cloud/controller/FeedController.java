package com.rp.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rp.cloud.response.FeedResponse;
import com.rp.cloud.service.FeedService;

@RestController
@RequestMapping("feedservice")
public class FeedController {

	@Autowired
	private FeedService service;
	
	@RequestMapping(value = {"/getfeed"}, produces = {"application/json"},method = {RequestMethod.GET})
    @ResponseBody
    public FeedResponse getFeed(@RequestParam("userId") String userId) {
        return service.getFeed(userId);
    }
}
