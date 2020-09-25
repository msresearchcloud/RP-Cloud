package com.rp.cloud.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Commodity {

	private List<String> commodity;
	
	public Commodity() {}

	public Commodity(List<String> commodity) {
		super();
		this.commodity = commodity;
	}

	@JsonProperty("com")
	public List<String> getCommodity() {
		return commodity;
	}

	public void setCommodity(List<String> commodity) {
		this.commodity = commodity;
	}
	
}
