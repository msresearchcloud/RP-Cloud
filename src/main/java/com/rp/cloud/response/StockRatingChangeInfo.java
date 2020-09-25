package com.rp.cloud.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class StockRatingChangeInfo {

	private String stockRatingPrior;
	
	private String stockRatingCurrent;
	
	private String ticker;

	@JsonProperty("srp")
	public String getStockRatingPrior() {
		return stockRatingPrior;
	}

	public void setStockRatingPrior(String stockRatingPrior) {
		this.stockRatingPrior = stockRatingPrior;
	}

	@JsonProperty("src")
	public String getStockRatingCurrent() {
		return stockRatingCurrent;
	}

	public void setStockRatingCurrent(String stockRatingCurrent) {
		this.stockRatingCurrent = stockRatingCurrent;
	}

	@JsonProperty("ticker")
	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
}
