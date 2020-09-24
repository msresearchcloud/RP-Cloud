package com.rp.clound.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FeedResponse {

	private String status ;
	
	private List<String> errorList;
	
	private List<UserFeedResponse> userFeedDetails;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

	public List<UserFeedResponse> getUserFeedDetails() {
		return userFeedDetails;
	}

	public void setUserFeedDetails(List<UserFeedResponse> userFeedDetails) {
		this.userFeedDetails = userFeedDetails;
	}
	
	
}
