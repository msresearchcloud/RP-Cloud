package com.rp.clound.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PriceTargetChangeInfo {

	private String ticker;
	private String priceTargetPrior;
	private String priceTargetCurrent;
	private String currency;
	
	@JsonProperty("ticker")
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	@JsonProperty("ptp")
	public String getPriceTargetPrior() {
		return priceTargetPrior;
	}
	public void setPriceTargetPrior(String priceTargetPrior) {
		this.priceTargetPrior = priceTargetPrior;
	}
	@JsonProperty("ptc")
	public String getPriceTargetCurrent() {
		return priceTargetCurrent;
	}
	public void setPriceTargetCurrent(String priceTargetCurrent) {
		this.priceTargetCurrent = priceTargetCurrent;
	}
	@JsonProperty("cy")
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
}
