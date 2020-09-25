package com.rp.cloud.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Currency {

	private List<String> currency;
	
	public Currency() {}

	public Currency(List<String> currency) {
		super();
		this.currency = currency;
	}

	@JsonProperty("cur")
	public List<String> getCurrency() {
		return currency;
	}

	public void setCurrency(List<String> currency) {
		this.currency = currency;
	}
	
	
	
}
