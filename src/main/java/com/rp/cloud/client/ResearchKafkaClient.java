package com.rp.cloud.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rp.cloud.kafka.config.AuthHelper;
import com.rp.cloud.response.ResearchDocument;
import com.rp.cloud.response.UserFeedResponse;
import com.rp.cloud.response.UserSubscriptionDetails;
import com.rp.cloud.service.impl.RcsServiceImpl;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;

import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.IOException;

@Component
public class ResearchKafkaClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    AuthHelper authHelper;

    @Autowired
    private RcsServiceImpl rcsService;

    @Autowired
    private ResearchMongoClient mongoClient;

    // Kafka Rest APIs
    private String api_version = "v1";
    private static String get_topics_api = "/v1/metadata/topics";
    private static String createTopicAPI = "/v1/topics/";
    private static String produceMsgAPI = "/v1/producer/topics/";
    private static String consumeMsgAPI = "/v1/consumer/topics/";


    public Properties getProperties(String brokers) {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);

        // Set how to serialize key/value pairs
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		// specify the protocol for Domain Joined clusters
        //properties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
		
        return properties;
    }

    public String describeTopics() {
        // Set properties used to configure admin client
        //Properties properties = getProperties(brokers);

        try {
            String getTopicsURL = authHelper.getKafkaRestEndpoint() + get_topics_api;
            ResponseEntity<String> topicsResponse = executeRequest(getTopicsURL, null, HttpMethod.GET);
            HttpStatus statusCodeT = topicsResponse.getStatusCode();
            if(HttpStatus.OK == statusCodeT){
                return topicsResponse.getBody();
            }
        } catch (Exception e) {
            System.out.print("Describe denied\n");
            System.out.print(e.getMessage());
        }
        return "Error occured in getting topic details";
    }

    public void deleteTopics(String topicName) throws IOException {
        // Set properties used to configure admin client
        Properties properties = getProperties(null);

        try (final AdminClient adminClient = KafkaAdminClient.create(properties)) {
            final DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Collections.singleton(topicName));
            deleteTopicsResult.values().get(topicName).get();
            setTopicName(null);
            System.out.print("Topic " + topicName + " deleted");
        } catch (Exception e) {
            System.out.print("Delete Topics denied\n");
            System.out.print(e.getMessage());
        }
    }

    public String createTopics(String topicName) throws IOException {
        // Set properties used to configure admin client
        //Properties properties = getProperties(brokers);
        try  {
            String uri = authHelper.getKafkaRestEndpoint() + createTopicAPI + topicName;

            ObjectMapper mapper = new ObjectMapper();
            File file = ResourceUtils.getFile("classpath:topic_config.json");
            FileReader reader = new FileReader(file);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            String createTopicJson = mapper.writeValueAsString(jsonObject);

            ResponseEntity<String> response = executeRequest(uri, createTopicJson, HttpMethod.PUT);
            HttpStatus statusCode = response.getStatusCode();
            if(HttpStatus.OK == statusCode){
                return response.getBody();
            }
            System.out.print("Topic " + topicName + " created");
        } catch (Exception e) {
            System.out.print("Create Topics denied\n");
            System.out.print(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return null;
    }

    public String produceMessage(String topicName, UserSubscriptionDetails userSubscriptionDetails) {
        try  {
            List<String> successMsg = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();

                    String jsonValue = "{\"records\" : [ {\"key\" : \"" + userSubscriptionDetails.getUserId() + "\", \"value\" : \"" +  JSONValue.escape(mapper.writeValueAsString(userSubscriptionDetails)) + "\"} ]}";
                    String result = sendMessage(userSubscriptionDetails.getUserId(), jsonValue, topicName);
                    if(result != null){
                        successMsg.add(result);
                    }


            System.out.print("Producer produces message for topic : " + topicName);
            return "Message Sent for keys : " + successMsg.toString();
        } catch (Exception e) {
            System.out.print("Error occurred while producing the message\n");
            System.out.print(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /* private String ecapse(String jsString) {
        jsString = jsString.replace("\\", "\\\\");
        jsString = jsString.replace("\"", "\\\"");
        jsString = jsString.replace("\b", "\\b");
        jsString = jsString.replace("\f", "\\f");
        jsString = jsString.replace("\n", "\\n");
        jsString = jsString.replace("\r", "\\r");
        jsString = jsString.replace("\t", "\\t");
        jsString = jsString.replace("/", "\\/");
        return jsString;
    } */

    private String sendMessage(String key, String payload, String topicName){
        try{
            String uri = authHelper.getKafkaRestEndpoint() + produceMsgAPI + topicName;
            ResponseEntity<String> response = executeRequest(uri, payload, HttpMethod.POST);
            HttpStatus statusCode = response.getStatusCode();
            if(HttpStatus.OK == statusCode){
                return key;
            }
        } catch (Exception ex){
            System.out.print("Error occurred while producing the message\n");
            System.out.print(ex.getMessage());
        }
        return null;
    }

    //@Scheduled(fixedRate = 30000)
    public String consumeMessage(){
        return consumeMessage("research-feed");
    }

    public String consumeMessage(String topicName) {
        try  {
            int partition_id = 0;
            int offset = 0;
            String uri = authHelper.getKafkaRestEndpoint() + consumeMsgAPI + topicName + "/partitions/" + partition_id + "/offsets/" + offset + "?count=1";
            ResponseEntity<String> response = executeRequest(uri, null, HttpMethod.GET);
            HttpStatus statusCode = response.getStatusCode();
            if(HttpStatus.OK == statusCode){
                String responseMsg = null;
                ObjectMapper mapper = new ObjectMapper();
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
                JSONArray jsonArray = (JSONArray) jsonObject.get("records");

                Iterator<JSONObject> iterator = jsonArray.iterator();
                while(iterator.hasNext()) {
                    JSONObject parsedObj = iterator.next();
                    String value = parsedObj.get("value").toString();
                    UserSubscriptionDetails subsDetails = mapper.readValue(value, UserSubscriptionDetails.class);
                     responseMsg = mongoClient.updateFeedDetails(subsDetails);
                }

                return responseMsg;
            }
            System.out.print("Consumer consumes message from topic :  " + topicName);
        } catch (Exception e) {
            System.out.print("Error occurred while consuming the message\n");
            System.out.print(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return null;
    }

    private  void setTopicName(String topicName) {
        topicName = topicName;
    }

    private ResponseEntity executeRequest(String uri, String requestBody, HttpMethod httpMethod)
    {
        String accessToken = authHelper.getAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Accept", "application/json");
        headers.set("Content-type", "application/json");

        HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
        ResponseEntity response = restTemplate.exchange(uri, httpMethod, entity, String.class);

        return response;
    }

}
