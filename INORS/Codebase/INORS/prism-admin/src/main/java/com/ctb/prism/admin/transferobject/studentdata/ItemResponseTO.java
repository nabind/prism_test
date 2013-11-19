package com.ctb.prism.admin.transferobject.studentdata;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Item_Response")
public class ItemResponseTO {

	@XStreamAsAttribute
	@XStreamAlias("item_set_type")
	private String itemSetType;

	@XStreamAsAttribute
	@XStreamAlias("item_code")
	private String scoreValue;

	@XStreamAsAttribute
	@XStreamAlias("score_value")
	private String itemCode;

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
