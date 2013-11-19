package com.ctb.prism.admin.transferobject.studentdata;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Objective_Score_Details")
public class ObjectiveScoreDetailsTO {

	@XStreamAsAttribute
	@XStreamAlias("Obj_Name")
	private String objectiveName;

	@XStreamAsAttribute
	@XStreamAlias("Obj_Code")
	private String objectiveCode;

	@XStreamImplicit
	private List<ObjectiveScoreTO> collObjectiveScoreTO;

	public String getObjectiveName() {
		return objectiveName;
	}

	public void setObjectiveName(String objectiveName) {
		this.objectiveName = objectiveName;
	}

	public String getObjectiveCode() {
		return objectiveCode;
	}

	public void setObjectiveCode(String objectiveCode) {
		this.objectiveCode = objectiveCode;
	}

	public List<ObjectiveScoreTO> getCollObjectiveScoreTO() {
		return collObjectiveScoreTO;
	}

	public void setCollObjectiveScoreTO(
			List<ObjectiveScoreTO> collObjectiveScoreTO) {
		this.collObjectiveScoreTO = collObjectiveScoreTO;
	}

}
