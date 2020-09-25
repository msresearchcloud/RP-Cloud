package com.rp.cloud.controller;

import com.rp.cloud.client.ResearchKafkaClient;
import com.rp.cloud.kafka.config.AuthHelper;
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

    @RequestMapping(value = {"/execute"}, produces = {"application/json"},
            method = {RequestMethod.GET})
    @ResponseBody
    public String execute(@RequestParam("action") String action, @RequestParam(name = "topicName", required = false) String topicName)  {
        return processAction(action, topicName);
    }

    @RequestMapping(value = {"/authenticate"}, produces = {"application/json"},
            method = {RequestMethod.GET})
    @ResponseBody
    public String authenticate(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Throwable {
        return authHelper.getAccessToken(httpRequest, httpResponse);
    }



    /**
     * Re <producer|consumer|describe|create|delete> <topicName> brokerhosts
     *
     * @param action    (producer|consumer|describe|create|delete)
     * @param topicName
     * @throws IOException
     */
    private String processAction(String action, String topicName) {

        String response = null;
        try {
            // Get the brokers
            switch (action.toLowerCase()) {
                case "producer":
                    response = adminClientWrapper.produceMessage(topicName);
                    break;
                case "consumer":
                    response = adminClientWrapper.consumeMessage(topicName);
                    break;
                case "describe":
                    response = adminClientWrapper.describeTopics(topicName);
                    break;
                case "create":
                    response = adminClientWrapper.createTopics(topicName);
                    break;
                case "delete":
                    adminClientWrapper.deleteTopics(topicName);
                    response = "DeleteTopics - Topic : " + topicName;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            response = "Error occurred while processing";
        }
        return response;
    }
}
