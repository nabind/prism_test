package com.ctb.prism.admin.transferobject.studentdata;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ItemResponsesDetailsTO {

	@XStreamImplicit
	private List<ItemResponseTO> itemResponseTO;

	public List<ItemResponseTO> getItemResponseTO() {
		return itemResponseTO;
	}

	public void setItemResponseTO(List<ItemResponseTO> itemResponseTO) {
		this.itemResponseTO = itemResponseTO;
	}

}
