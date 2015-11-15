package com.ctb.prism.admin.transferobject.studentdata;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Objective_Score")
public class ObjectiveScoreTO {

	@XStreamAsAttribute
	@XStreamAlias("Score_Type")
	private String scoreType;

	@XStreamAsAttribute
	private String value;

	public String getScoreType() {
		return scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
