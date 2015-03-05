package com.ctb.prism.admin.transferobject.studentdata;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Survey_Bio")
public class SurveyBioTO {

	@XStreamAsAttribute
	@XStreamAlias("Survey_name")
	private String surveyName;

	@XStreamAsAttribute
	@XStreamAlias("survey_value")
	private String surveyValue;

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public String getSurveyValue() {
		return surveyValue;
	}

	public void setSurveyValue(String surveyValue) {
		this.surveyValue = surveyValue;
	}

}
