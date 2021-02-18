package com.rp.cloud.controller;

import com.azure.messaging.servicebus.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rp.cloud.client.ResearchMongoClient;
import com.rp.cloud.client.ServiceBusClient;
import com.rp.cloud.response.UserSubscriptionDetails;
import com.rp.cloud.service.RedisService;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("servicebus")
public class ServiceBusController {

    static final String connectionString = "Endpoint=sb://researchportal-feed.servicebus.windows.net/;" +
            "SharedAccessKeyName=ServiceBusPolicy;" +
            "SharedAccesskey=LJk2MU7LNwYKDSB1eysGx0QyP2TBR3WoPYRhpxNpKN8=";

    static String queueName = "feedqueue";
    static String topicName = "feedtopic";
    static String subNameA = "feedtopicsubA";
    static String subNameB = "feedtopicsubB";
    static String subNameC = "feedtopicsubC";

    @Autowired
    private ServiceBusClient serviceBusClient;

    @Autowired
    private ResearchMongoClient mongoClient;

    private static  final Logger logger = LoggerFactory.getLogger(ServiceBusController.class);

    @RequestMapping(value = {"/usersub-db"}, produces = {"application/json"},method = {RequestMethod.POST})
    @ResponseBody
    public String addUserSubscriptionToDB(@RequestBody UserSubscriptionDetails userSubscriptionDetails){
        return mongoClient.updateFeedDetails(userSubscriptionDetails);
    }

    @RequestMapping(value = {"/usersub-db"}, produces = {"application/json"},method = {RequestMethod.GET})
    @ResponseBody
    public String addUserSubscriptionToDB(@RequestParam(name = "userId") String userId,
                                      @RequestParam(name = "subId") String subId,
                                      @RequestParam(name = "modifiedDate") String modifiedDate,
                                      @RequestParam(name = "docs") List<String> docs){

        UserSubscriptionDetails userSubscriptionDetails = new UserSubscriptionDetails();
        userSubscriptionDetails.setUserId(userId);
        userSubscriptionDetails.setSubId(subId);
        userSubscriptionDetails.setModifiedDate(Long.parseLong(modifiedDate));
        userSubscriptionDetails.setDocs(docs);

        return mongoClient.updateFeedDetails(userSubscriptionDetails);
    }


    @RequestMapping(value = {"/publish-usersub-to-topic"}, produces = {"application/json"},method = {RequestMethod.POST})
    @ResponseBody
    public String publishUserSubscriptionToTopic(@RequestBody UserSubscriptionDetails userSubscriptionDetails){
        return serviceBusClient.publishUserSubscriptionToTopic(userSubscriptionDetails);
    }

    @RequestMapping(value = {"/publish-usersub-to-topic"}, produces = {"application/json"},method = {RequestMethod.GET})
    @ResponseBody
    public String publishUserSubscriptionToTopic(@RequestParam(name = "userId") String userId,
                                                 @RequestParam(name = "subId") String subId,
                                                 @RequestParam(name = "modifiedDate") String modifiedDate,
                                                 @RequestParam(name = "docs") List<String> docs){

        UserSubscriptionDetails userSubscriptionDetails = new UserSubscriptionDetails();
        userSubscriptionDetails.setUserId(userId);
        userSubscriptionDetails.setSubId(subId);
        userSubscriptionDetails.setModifiedDate(Long.parseLong(modifiedDate));
        userSubscriptionDetails.setDocs(docs);

        return serviceBusClient.publishUserSubscriptionToTopic(userSubscriptionDetails);
    }

    @RequestMapping(value = {"/start-feedtopicsubA-listener"}, produces = {"application/json"},method = {RequestMethod.GET})
    @ResponseBody
    public String startSubAListener(){
        return serviceBusClient.startSubAListener();
    }

}
