package com.ctb.prism.webservice.transferobject;

import java.io.Serializable;
import java.util.List;

public class ObjectiveScoreDetailsTO implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4477773094826787095L;

	private String objectiveName;
	private String objectiveCode;
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
	public void setCollObjectiveScoreTO(List<ObjectiveScoreTO> collObjectiveScoreTO) {
		this.collObjectiveScoreTO = collObjectiveScoreTO;
	}
	
	
}
