package com.ctb.prism.admin.transferobject.studentdata;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Content_Details")
public class ContentDetailsTO {

	@XStreamAsAttribute
	@XStreamAlias("Content_Code")
	private String contentCode;

	@XStreamAsAttribute
	@XStreamAlias("Scrng_Method")
	private String scoringMethod;

	@XStreamAsAttribute
	@XStreamAlias("Status_code")
	private String statusCode;

	@XStreamAsAttribute
	@XStreamAlias("Dt_Tst_Taken")
	private String dateTestTaken;

	@XStreamAsAttribute
	@XStreamAlias("IsDataChange")
	private boolean dataChanged = false;

	@XStreamAlias("Subtest_Accommodations")
	private SubtestAccommodationsTO subtestAccommodationsTO;

	@XStreamAlias("Item_Responses_Details")
	private ItemResponsesDetailsTO itemResponsesDetailsTO;

	@XStreamAlias("Content_Score_Details")
	private ContentScoreDetailsTO contentScoreDetailsTO;

	@XStreamImplicit
	@XStreamAlias("IsDataChange")
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

	public void setContentScoreDetailsTO(
			ContentScoreDetailsTO contentScoreDetailsTO) {
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
