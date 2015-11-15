package com.ctb.prism.admin.transferobject.studentdata;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ContentScoreDetailsTO {

	@XStreamImplicit
	private List<ContentScoreTO> collContentScoreTO;

	public List<ContentScoreTO> getCollContentScoreTO() {
		return collContentScoreTO;
	}

	public void setCollContentScoreTO(List<ContentScoreTO> collContentScoreTO) {
		this.collContentScoreTO = collContentScoreTO;
	}

}
