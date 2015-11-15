package com.ctb.prism.admin.transferobject.studentdata;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class StudentSurveyBioTO {

	@XStreamAsAttribute
	@XStreamAlias("IsDataChange")
	private boolean dataChanged = false;

	@XStreamImplicit
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
