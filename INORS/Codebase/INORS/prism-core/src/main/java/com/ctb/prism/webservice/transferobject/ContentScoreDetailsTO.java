package com.ctb.prism.webservice.transferobject;

import java.io.Serializable;
import java.util.List;

public class ContentScoreDetailsTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4477773094826787095L;
	
	private List<ContentScoreTO> collContentScoreTO;

	public List<ContentScoreTO> getCollContentScoreTO() {
		return collContentScoreTO;
	}

	public void setCollContentScoreTO(List<ContentScoreTO> collContentScoreTO) {
		this.collContentScoreTO = collContentScoreTO;
	}

	
}
