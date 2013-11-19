package com.ctb.prism.webservice.transferobject;

import java.util.List;

public class StudentSurveyBioTO {

	private boolean dataChanged = false;
	private List<SurveyBioTO> collSurveyBioTO;
	
	public boolean isDataChanged() {
		return dataChanged;
	}
	public void setDataChanged(boolean dataChanged) {
		this.dataChanged = dataChanged;
	}
	public List<SurveyBioTO> getCollSurveyBioTO() {
		return collSurveyBioTO;
	}
	public void setCollSurveyBioTO(List<SurveyBioTO> collSurveyBioTO) {
		this.collSurveyBioTO = collSurveyBioTO;
	}
	
	
}
