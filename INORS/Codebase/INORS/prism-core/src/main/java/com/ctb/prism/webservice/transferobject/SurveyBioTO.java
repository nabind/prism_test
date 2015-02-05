package com.ctb.prism.webservice.transferobject;

import java.io.Serializable;

public class SurveyBioTO implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4477773094826787095L;

	private String surveyName;
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
