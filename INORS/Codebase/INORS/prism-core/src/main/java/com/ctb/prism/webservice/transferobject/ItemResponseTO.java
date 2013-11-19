package com.ctb.prism.webservice.transferobject;

public class ItemResponseTO {

	private String itemSetType;
	private String scoreValue;
	private String itemCode;
	private String responseStatus;
	private String editedResponse;
	
	public String getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	public String getEditedResponse() {
		return editedResponse;
	}
	public void setEditedResponse(String editedResponse) {
		this.editedResponse = editedResponse;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemSetType() {
		return itemSetType;
	}
	public void setItemSetType(String itemSetType) {
		this.itemSetType = itemSetType;
	}
	public String getScoreValue() {
		return scoreValue;
	}
	public void setScoreValue(String scoreValue) {
		this.scoreValue = scoreValue;
	}

}
