package com.rp.cloud.service;

import java.util.Map;

import com.rp.cloud.response.ResearchDocument;

public interface RcsService {

	Map<String, ResearchDocument> getDocumentDetails();
}
