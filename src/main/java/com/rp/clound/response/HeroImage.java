package com.rp.clound.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class HeroImage {

	private String wideDestop;

	private String destop;

	private String tablet;

	private String mobile;

	private String tile;

	@JsonProperty("wd")
	public String getWideDestop() {
		return wideDestop;
	}

	public void setWideDestop(String wideDestop) {
		this.wideDestop = wideDestop;
	}

	@JsonProperty("d")
	public String getDestop() {
		return destop;
	}

	public void setDestop(String destop) {
		this.destop = destop;
	}

	@JsonProperty("t")
	public String getTablet() {
		return tablet;
	}

	public void setTablet(String tablet) {
		this.tablet = tablet;
	}

	@JsonProperty("m")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@JsonProperty("tile")
	public String getTile() {
		return tile;
	}

	public void setTile(String tile) {
		this.tile = tile;
	}


}
