package com.rp.cloud.service.impl;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rp.cloud.response.ResearchDocument;
import com.rp.cloud.service.RcsService;

@Service
public class RcsServiceImpl implements RcsService {

	private static  final Logger logger = LoggerFactory.getLogger(RcsServiceImpl.class);
	
	@Override
	public Map<String, ResearchDocument> getDocumentDetails() {
		logger.info("fetching documnet details...") ;
	    List<ResearchDocument> documents = new ArrayList<>();
	    Map<String, ResearchDocument> documentMap = new HashMap<>();
		try { 
		    
		    final ObjectMapper mapper = new ObjectMapper();
		    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		    InputStream inputStream = classloader.getResourceAsStream("response.txt");
		    JSONParser jsonParser = new JSONParser();
		    JSONObject jsonObject = (JSONObject)jsonParser.parse(
		          new InputStreamReader(inputStream, "UTF-8"));
		   
		    if (null != jsonObject) {
		    	 JSONArray documentsarray = (JSONArray)jsonObject.get("docs");
		    	 if (null != documentsarray) {
		    		 for (int i=0; i<documentsarray.size();i++) {
		    		    	JSONObject documentObject = (JSONObject)documentsarray.get(i);
		    		    	ResearchDocument document = mapper.readValue(documentObject.toString(), ResearchDocument.class);
		    		    	documents.add(document);
		    		    }
		    	 }
		    }
		    logger.info("Total number of document are : " +documents.size());
		   documentMap = documents.stream().collect(Collectors.toMap(ResearchDocument::getUuid, doc -> doc, (oldvalue, newvalue)->newvalue));
		} catch (Exception e) {
			logger.info("Exception occur while fetching document details" + e.getMessage());
		}
		logger.info("Final document map size is : " +documentMap.size());
		return documentMap;
	}

}
