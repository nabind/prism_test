package com.ctb.prism.parent.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author Joy
 * version 1.1
 */
public class ManageContentTO extends BaseTO {

	private static final long serialVersionUID = 1L;
	private long contentId = 0;
	private String contentName = "";
	private String subHeader = "";
	private String gradeName = "";
	private String performanceLevel = "";
	private String statusCode = "";
	private String contentDescription = "";
	private long subObjMapId = 0;
	private long custProdId = 0;
	private String contentTypeName = "";
	private String contentType = "";
	private long gradeId = 0; 
	private long subtestId = 0; 
	private long objectiveId = 0;
	private String objectiveName = "";
	private String subtestName = "";
	private String proficiencyLevel = "";
	private long objContentId = 0;
	private String objectiveDesc = "";
	
	public long getContentId() {
		return contentId;
	}
	public void setContentId(long contentId) {
		this.contentId = contentId;
	}
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public String getSubHeader() {
		return subHeader;
	}
	public void setSubHeader(String subHeader) {
		this.subHeader = subHeader;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getContentDescription() {
		if(contentDescription != null) {
			contentDescription = contentDescription.replaceAll("<strong>", "<b>").replaceAll("</strong>", "</b>");
			contentDescription = contentDescription.replaceAll("<em>", "<i>").replaceAll("</em>", "</i>");
			contentDescription = contentDescription.replaceAll("Click here to add text","");
		}
		return contentDescription;
	}
	public void setContentDescription(String contentDescription) {
		this.contentDescription = contentDescription;
	}
	public long getSubObjMapId() {
		return subObjMapId;
	}
	public void setSubObjMapId(long subObjMapId) {
		this.subObjMapId = subObjMapId;
	}
	public long getCustProdId() {
		return custProdId;
	}
	public void setCustProdId(long custProdId) {
		this.custProdId = custProdId;
	}
	public String getContentTypeName() {
		return contentTypeName;
	}
	public void setContentTypeName(String contentTypeName) {
		this.contentTypeName = contentTypeName;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public long getGradeId() {
		return gradeId;
	}
	public void setGradeId(long gradeId) {
		this.gradeId = gradeId;
	}
	public long getSubtestId() {
		return subtestId;
	}
	public void setSubtestId(long subtestId) {
		this.subtestId = subtestId;
	}
	public long getObjectiveId() {
		return objectiveId;
	}
	public void setObjectiveId(long objectiveId) {
		this.objectiveId = objectiveId;
	}
	public String getObjectiveName() {
		return objectiveName;
	}
	public void setObjectiveName(String objectiveName) {
		this.objectiveName = objectiveName;
	}
	public String getSubtestName() {
		return subtestName;
	}
	public void setSubtestName(String subtestName) {
		this.subtestName = subtestName;
	}
	public String getPerformanceLevel() {
		return performanceLevel;
	}
	public void setPerformanceLevel(String performanceLevel) {
		this.performanceLevel = performanceLevel;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}	
	public String getProficiencyLevel() {
		return proficiencyLevel;
	}
	public void setProficiencyLevel(String proficiencyLevel) {
		this.proficiencyLevel = proficiencyLevel;
	}
	public long getObjContentId() {
		return objContentId;
	}
	public void setObjContentId(long objContentId) {
		this.objContentId = objContentId;
	}
	public String getObjectiveDesc() {
		return objectiveDesc;
	}
	public void setObjectiveDesc(String objectiveDesc) {
		this.objectiveDesc = objectiveDesc;
	}
}

