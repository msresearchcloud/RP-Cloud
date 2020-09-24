package com.rp.clound.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IndustryViewChangeInfo {

	private String id;
	private String industry;
	private String industryViewPrior;
	private String industryViewCurrent;
	
	@JsonProperty("id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@JsonProperty("industry")
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	@JsonProperty("ivp")
	public String getIndustryViewPrior() {
		return industryViewPrior;
	}
	public void setIndustryViewPrior(String industryViewPrior) {
		this.industryViewPrior = industryViewPrior;
	}
	@JsonProperty("ivc")
	public String getIndustryViewCurrent() {
		return industryViewCurrent;
	}
	public void setIndustryViewCurrent(String industryViewCurrent) {
		this.industryViewCurrent = industryViewCurrent;
	}
	
	
}
