package com.ctb.prism.webservice.transferobject;

import java.io.Serializable;
import java.util.List;

public class SubtestAccommodationsTO implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4477773094826787095L;

	private List<SubtestAccommodationTO> collSubtestAccommodationTO;

	public List<SubtestAccommodationTO> getCollSubtestAccommodationTO() {
		return collSubtestAccommodationTO;
	}

	public void setCollSubtestAccommodationTO(
			List<SubtestAccommodationTO> collSubtestAccommodationTO) {
		this.collSubtestAccommodationTO = collSubtestAccommodationTO;
	}

	
	
}
