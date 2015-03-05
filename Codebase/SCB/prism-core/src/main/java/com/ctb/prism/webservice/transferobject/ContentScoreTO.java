package com.ctb.prism.webservice.transferobject;

import java.io.Serializable;

public class ContentScoreTO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4477773094826787095L;

	private String scoreType;
	private String scoreValue;
	public String getScoreType() {
		return scoreType;
	}
	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}
	public String getScoreValue() {
		return scoreValue;
	}
	public void setScoreValue(String scoreValue) {
		this.scoreValue = scoreValue;
	}
	
	
}
