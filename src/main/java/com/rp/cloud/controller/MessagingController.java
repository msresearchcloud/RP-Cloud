package com.rp.cloud.controller;

import com.rp.cloud.client.ResearchKafkaClient;
import com.rp.cloud.kafka.config.AuthHelper;
import com.rp.cloud.response.UserSubscriptionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("messagingservice")
public class MessagingController {

    @Autowired
    AuthHelper authHelper;

    @Autowired
    private ResearchKafkaClient adminClientWrapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping(value = {"/authenticate"}, produces = {"application/json"},
            method = {RequestMethod.GET})
    @ResponseBody
    public String authenticate(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Throwable {
        return authHelper.getAccessToken(httpRequest, httpResponse);
    }

    @RequestMapping(value = {"/create-topic"}, produces = {"application/json"},
            method = {RequestMethod.GET})
    @ResponseBody
    public String createTopic(@RequestParam(name = "topicName") String topicName) {
        try {
            return adminClientWrapper.createTopics(topicName);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    @RequestMapping(value = {"/describe-topics"}, produces = {"application/json"},
            method = {RequestMethod.GET})
    @ResponseBody
    public String describeTopics() {
        return adminClientWrapper.describeTopics();
    }

    @RequestMapping(value = {"/produce-message"}, produces = {"application/json"},
            method = {RequestMethod.POST})
    @ResponseBody
    public String produceMessage(@RequestParam(name = "topicName") String topicName, @RequestBody UserSubscriptionDetails userSubscriptionDetails) {
        return adminClientWrapper.produceMessage(topicName, userSubscriptionDetails);
    }

    @RequestMapping(value = {"/consume-message"}, produces = {"application/json"},
            method = {RequestMethod.GET})
    @ResponseBody
    public String consumeMessage(@RequestParam(name = "topicName") String topicName) {
        return adminClientWrapper.consumeMessage(topicName);
    }

}