package com.rp.cloud.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(Include.NON_EMPTY)
public class ResearchDocument {
	
	private String publishDate;
	
	private String docAbstract;
	
	private String uuid;
	
	private String headline;
	
	private Personnel author;
	
	private List<Personnel> contributers;
	
	private String docType;
	
	private String primaryTicker;
	
	private String productVersion;
	
	private String productName;
	
	private String freeTextSummary;
	
	private String htmlSummary;
	
	private String pageCount;
	
	private HeroImage heroImage;
	
	private Periodical periodicals;
	
	private List<Audio> audios = null;
	
	private List<Video> videos = null;
	
	private String lang;
	
	private String fullSynopsis;
	
	private ChangeInfo changeInfo;
	
	private String productSubType;
	
	private String productSubTypeCode;
	
	private String productCategroy1;
	
	private String productCategory2;
	
	private Commodity commodities;
	
	private Currency currencies;

	@JsonProperty("pd")
	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	@JsonProperty("ab")
	public String getDocAbstract() {
		return docAbstract;
	}

	public void setDocAbstract(String docAbstract) {
		this.docAbstract = docAbstract;
	}
	@JsonProperty("id")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@JsonProperty("hl")
	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}
	@JsonProperty("a")
	public Personnel getAuthor() {
		return author;
	}

	public void setAuthor(Personnel author) {
		this.author = author;
	}
	@JsonProperty("co")
	public List<Personnel> getContributers() {
		return contributers;
	}

	public void setContributers(List<Personnel> contributers) {
		this.contributers = contributers;
	}
	@JsonProperty("dt")
	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}
	@JsonProperty("pt")
	public String getPrimaryTicker() {
		return primaryTicker;
	}

	public void setPrimaryTicker(String primaryTicker) {
		this.primaryTicker = primaryTicker;
	}
	@JsonProperty("pv")
	public String getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}
	@JsonProperty("pn")
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
    @JsonIgnore
	public String getFreeTextSummary() {
		return freeTextSummary;
	}

	public void setFreeTextSummary(String freeTextSummary) {
		this.freeTextSummary = freeTextSummary;
	}
	@JsonProperty("hs")
	public String getHtmlSummary() {
		return htmlSummary;
	}

	public void setHtmlSummary(String htmlSummary) {
		this.htmlSummary = htmlSummary;
	}
	@JsonProperty("pc")
	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}
	@JsonProperty("hi")
	public HeroImage getHeroImage() {
		return heroImage;
	}

	public void setHeroImage(HeroImage heroImage) {
		this.heroImage = heroImage;
	}
	@JsonProperty("pls")
	public Periodical getPeriodicals() {
		return periodicals;
	}

	public void setPeriodicals(Periodical periodicals) {
		this.periodicals = periodicals;
	}
	@JsonProperty("au")
	public List<Audio> getAudios() {
		return audios;
	}

	public void setAudios(List<Audio> audios) {
		this.audios = audios;
	}
	@JsonProperty("vi")
	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}
	@JsonProperty("lang")
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	@JsonProperty("fs")
	public String getFullSynopsis() {
		return fullSynopsis;
	}

	public void setFullSynopsis(String fullSynopsis) {
		this.fullSynopsis = fullSynopsis;
	}
	@JsonProperty("cinfo")
	public ChangeInfo getChangeInfo() {
		return changeInfo;
	}

	public void setChangeInfo(ChangeInfo changeInfo) {
		this.changeInfo = changeInfo;
	}
	@JsonProperty("pst")
	public String getProductSubType() {
		return productSubType;
	}

	public void setProductSubType(String productSubType) {
		this.productSubType = productSubType;
	}
	@JsonProperty("pstc")
	public String getProductSubTypeCode() {
		return productSubTypeCode;
	}

	public void setProductSubTypeCode(String productSubTypeCode) {
		this.productSubTypeCode = productSubTypeCode;
	}
	@JsonProperty("pcat1")
	public String getProductCategroy1() {
		return productCategroy1;
	}

	public void setProductCategroy1(String productCategroy1) {
		this.productCategroy1 = productCategroy1;
	}
	@JsonProperty("pcat2")
	public String getProductCategory2() {
		return productCategory2;
	}

	public void setProductCategory2(String productCategory2) {
		this.productCategory2 = productCategory2;
	}
	@JsonProperty("coms")
	public Commodity getCommodities() {
		return commodities;
	}

	public void setCommodities(Commodity commodities) {
		this.commodities = commodities;
	}
	@JsonProperty("curs")
	public Currency getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Currency currencies) {
		this.currencies = currencies;
	}	

}
