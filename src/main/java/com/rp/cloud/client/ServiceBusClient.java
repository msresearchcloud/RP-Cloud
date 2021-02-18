package com.rp.cloud.client;

import com.azure.messaging.servicebus.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rp.cloud.response.UserSubscriptionDetails;
import com.rp.cloud.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class ServiceBusClient {

    @Autowired
    private ResearchMongoClient mongoClient;

    @Autowired
    private RedisService redisService;

    static final String connectionString = "Endpoint=sb://researchportal-feed.servicebus.windows.net/;" +
            "SharedAccessKeyName=ServiceBusPolicy;" +
            "SharedAccesskey=LJk2MU7LNwYKDSB1eysGx0QyP2TBR3WoPYRhpxNpKN8=";

    static String queueName = "feedqueue";
    static String topicName = "feedtopic";
    static String subNameA = "feedtopicsubA";
    static String subNameB = "feedtopicsubB";
    static String subNameC = "feedtopicsubC";

    public String publishUserSubscriptionToTopic(UserSubscriptionDetails userSubscriptionDetails){

        String response = "";
        ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .topicName(topicName)
                .buildClient();

        try {
            senderClient.sendMessage(new ServiceBusMessage(new ObjectMapper().writeValueAsString(userSubscriptionDetails)));
            response = "Sent a single User Subscription to the topic " + topicName;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            response = "Error:"+e.getMessage();
        }

        return response;
    }

    public String startSubAListener(){

        CountDownLatch countdownLatch = new CountDownLatch(1);
        ServiceBusProcessorClient processorClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .processor()
                .topicName(topicName)
                .subscriptionName(subNameA)
                .processMessage(this::processMessage)
                .processError(context -> processError(context, countdownLatch))
                .buildProcessorClient();

        processorClient.start();
        return "Listener on feedtopicsubA started.";
    }

    private void processMessage(ServiceBusReceivedMessageContext context) {

        ServiceBusReceivedMessage message = context.getMessage();
        System.out.printf("Processing message. Session: %s, Sequence #: %s. Contents: %s%n", message.getMessageId(),
                message.getSequenceNumber(), message.getBody());
        ObjectMapper mapper = new ObjectMapper();
        UserSubscriptionDetails userSubscriptionDetails = null;
        try {
            userSubscriptionDetails = mapper.readValue(message.getBody().toString(), UserSubscriptionDetails.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        mongoClient.updateFeedDetails(userSubscriptionDetails);
        redisService.put(userSubscriptionDetails.getUserId(),userSubscriptionDetails.getDocs().size());
    }

    private void processError(ServiceBusErrorContext context, CountDownLatch countdownLatch) {
        System.out.printf("Error when receiving messages from namespace: '%s'. Entity: '%s'%n",
                context.getFullyQualifiedNamespace(), context.getEntityPath());

        if (!(context.getException() instanceof ServiceBusException)) {
            System.out.printf("Non-ServiceBusException occurred: %s%n", context.getException());
            return;
        }

        ServiceBusException exception = (ServiceBusException) context.getException();
        ServiceBusFailureReason reason = exception.getReason();

        if (reason == ServiceBusFailureReason.MESSAGING_ENTITY_DISABLED
                || reason == ServiceBusFailureReason.MESSAGING_ENTITY_NOT_FOUND
                || reason == ServiceBusFailureReason.UNAUTHORIZED) {
            System.out.printf("An unrecoverable error occurred. Stopping processing with reason %s: %s%n",
                    reason, exception.getMessage());

            countdownLatch.countDown();
        } else if (reason == ServiceBusFailureReason.MESSAGE_LOCK_LOST) {
            System.out.printf("Message lock lost for message: %s%n", context.getException());
        } else if (reason == ServiceBusFailureReason.SERVICE_BUSY) {
            try {
                // Choosing an arbitrary amount of time to wait until trying again.
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.err.println("Unable to sleep for period of time");
            }
        } else {
            System.out.printf("Error source %s, reason %s, message: %s%n", context.getErrorSource(),
                    reason, context.getException());
        }
    }

}
