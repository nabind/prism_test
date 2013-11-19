package com.ctb.prism.webservice.transferobject;

import java.util.List;

public class ContentDetailsTO {

	private String contentCode;
	private String scoringMethod;
	private String statusCode;
	private String dateTestTaken;
	private boolean dataChanged = false;
	private SubtestAccommodationsTO subtestAccommodationsTO; 
	private ItemResponsesDetailsTO itemResponsesDetailsTO;
	private ContentScoreDetailsTO contentScoreDetailsTO;
	private List<ObjectiveScoreDetailsTO> collObjectiveScoreDetailsTO;
	public String getContentCode() {
		return contentCode;
	}
	public void setContentCode(String contentCode) {
		this.contentCode = contentCode;
	}
	public String getScoringMethod() {
		return scoringMethod;
	}
	public void setScoringMethod(String scoringMethod) {
		this.scoringMethod = scoringMethod;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getDateTestTaken() {
		return dateTestTaken;
	}
	public void setDateTestTaken(String dateTestTaken) {
		this.dateTestTaken = dateTestTaken;
	}
	public boolean isDataChanged() {
		return dataChanged;
	}
	public void setDataChanged(boolean dataChanged) {
		this.dataChanged = dataChanged;
	}
	public SubtestAccommodationsTO getSubtestAccommodationsTO() {
		return subtestAccommodationsTO;
	}
	public void setSubtestAccommodationsTO(
			SubtestAccommodationsTO subtestAccommodationsTO) {
		this.subtestAccommodationsTO = subtestAccommodationsTO;
	}
	public ItemResponsesDetailsTO getItemResponsesDetailsTO() {
		return itemResponsesDetailsTO;
	}
	public void setItemResponsesDetailsTO(
			ItemResponsesDetailsTO itemResponsesDetailsTO) {
		this.itemResponsesDetailsTO = itemResponsesDetailsTO;
	}
	public ContentScoreDetailsTO getContentScoreDetailsTO() {
		return contentScoreDetailsTO;
	}
	public void setContentScoreDetailsTO(ContentScoreDetailsTO contentScoreDetailsTO) {
		this.contentScoreDetailsTO = contentScoreDetailsTO;
	}
	public List<ObjectiveScoreDetailsTO> getCollObjectiveScoreDetailsTO() {
		return collObjectiveScoreDetailsTO;
	}
	public void setCollObjectiveScoreDetailsTO(
			List<ObjectiveScoreDetailsTO> collObjectiveScoreDetailsTO) {
		this.collObjectiveScoreDetailsTO = collObjectiveScoreDetailsTO;
	}
	
	
	
	
}
