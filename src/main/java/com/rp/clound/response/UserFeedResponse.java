package com.rp.clound.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserFeedResponse {

	private String userId;

	private String subId;

	private List<ResearchDocument> docs;

	private long modifiedDate;

	public UserFeedResponse(String userId, String subId, List<ResearchDocument> docs, long modifiedDate) {
		super();
		this.userId = userId;
		this.subId = subId;
		this.docs = docs;
		this.modifiedDate = modifiedDate;
	}

	@JsonProperty("userId")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@JsonProperty("subId")
	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	@JsonProperty("docs")
	public List<ResearchDocument> getDocs() {
		if (null == docs) {
			docs = new ArrayList<>();
		}
		return docs;
	}

	public void setDocs(List<ResearchDocument> docs) {
		this.docs = docs;
	}

	@JsonProperty("modifiedDate")
	public long getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(long modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
}
