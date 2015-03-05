package com.ctb.prism.webservice.transferobject;

import java.io.Serializable;
import java.util.List;

public class ItemResponsesDetailsTO implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4477773094826787095L;

	private List<ItemResponseTO> itemResponseTO;

	public List<ItemResponseTO> getItemResponseTO() {
		return itemResponseTO;
	}

	public void setItemResponseTO(List<ItemResponseTO> itemResponseTO) {
		this.itemResponseTO = itemResponseTO;
	}

	
}
