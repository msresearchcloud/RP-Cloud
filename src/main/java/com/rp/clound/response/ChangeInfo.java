package com.rp.clound.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ChangeInfo {
	
	private List<StockRatingChangeInfo> stockRatingChangeInfo;

	private List<PriceTargetChangeInfo> priceTargetChangeInfo;
	
	private List<IndustryViewChangeInfo> industryViewChangeInfo;

	@JsonProperty("srcinfo")
	public List<StockRatingChangeInfo> getStockRatingChangeInfo() {
		return stockRatingChangeInfo;
	}

	public void setStockRatingChangeInfo(List<StockRatingChangeInfo> stockRatingChangeInfo) {
		this.stockRatingChangeInfo = stockRatingChangeInfo;
	}

	@JsonProperty("ptcinfo")
	public List<PriceTargetChangeInfo> getPriceTargetChangeInfo() {
		return priceTargetChangeInfo;
	}

	public void setPriceTargetChangeInfo(List<PriceTargetChangeInfo> priceTargetChangeInfo) {
		this.priceTargetChangeInfo = priceTargetChangeInfo;
	}

	@JsonProperty("ivcinfo")
	public List<IndustryViewChangeInfo> getIndustryViewChangeInfo() {
		return industryViewChangeInfo;
	}

	public void setIndustryViewChangeInfo(List<IndustryViewChangeInfo> industryViewChangeInfo) {
		this.industryViewChangeInfo = industryViewChangeInfo;
	}
	
	
}
