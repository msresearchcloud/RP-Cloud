package com.rp.clound.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Periodical {
	
	private List<String> periodical;

	@JsonProperty("pl")
	public List<String> getPeriodical() {
		return periodical;
	}

	public void setPeriodical(List<String> periodical) {
		this.periodical = periodical;
	}

}
