package com.ctb.prism.webservice.transferobject;

import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ContentDetailsTO implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4477773094826787095L;
	
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
		try {
			if (dateTestTaken != null) {
				// format date from MMDDYY --> MMDDYYYY
				Date date = new SimpleDateFormat("MMddyy", Locale.ENGLISH).parse(dateTestTaken);
				SimpleDateFormat dateformatter = new SimpleDateFormat("MMddyyyy");
				dateTestTaken = dateformatter.format(date);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
