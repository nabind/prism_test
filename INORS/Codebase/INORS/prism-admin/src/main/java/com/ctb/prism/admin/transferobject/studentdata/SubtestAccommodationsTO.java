package com.ctb.prism.admin.transferobject.studentdata;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class SubtestAccommodationsTO {

	@XStreamImplicit
	private List<SubtestAccommodationTO> collSubtestAccommodationTO;

	public List<SubtestAccommodationTO> getCollSubtestAccommodationTO() {
		return collSubtestAccommodationTO;
	}

	public void setCollSubtestAccommodationTO(
			List<SubtestAccommodationTO> collSubtestAccommodationTO) {
		this.collSubtestAccommodationTO = collSubtestAccommodationTO;
	}

}
