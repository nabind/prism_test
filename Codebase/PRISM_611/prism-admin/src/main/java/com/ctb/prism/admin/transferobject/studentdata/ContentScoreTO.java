package com.ctb.prism.admin.transferobject.studentdata;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Content_Score")
public class ContentScoreTO {

	@XStreamAsAttribute
	@XStreamAlias("Score_Type")
	private String scoreType;

	@XStreamAsAttribute
	@XStreamAlias("score_value")
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
