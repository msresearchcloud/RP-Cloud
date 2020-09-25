package com.rp.cloud.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserSubscriptionDetails {

	private String userId;
	
	private String subId;
	
	private List<String> docs;
	
	private long modifiedDate;

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
	public List<String> getDocs() {
		if (null == docs) {
			docs = new ArrayList<>();
		}
		return docs;
	}

	public void setDocs(List<String> docs) {
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
