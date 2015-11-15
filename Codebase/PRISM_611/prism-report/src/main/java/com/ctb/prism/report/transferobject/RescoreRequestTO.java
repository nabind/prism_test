/**
 * 
 */
package com.ctb.prism.report.transferobject;

import java.util.ArrayList;
import java.util.List;
import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author Joy
 * 
 */
public class RescoreRequestTO extends BaseTO {
	private static final long serialVersionUID = 1L;
	private String button;
	private String testAdministrationVal;
	private String testAdministrationText;
	private String testProgram;
	private String district;
	private String school;
	private String grade;
	private String studentFullName = "";
	private long userId = 0;
	private String userName = "";
	private long adminId = 0;
	private long customerId = 0;
	private long orgNodeId= 0;
	private String parentRescoreDate = "";
	private long studentBioId = 0;
	private String requestedDate = "";
	private long subtestId = 0;
	private String sessionId = "";
	private String performanceLevel = "";
	private long itemsetId = 0;
	private String isRequested = "";
	private String subtestCode = "";
	private List<RescoreSubtestTO> rescoreSubtestTOList = new ArrayList<RescoreSubtestTO>();
	private String originalScore = "";
	private long rrfId = 0;
	private long itemNumber = 0;
	private String moduleId = "";
	private String itemPart = "";
	private String itemScore = "";
	private String pointPossible = "";
	private String ipFileName = "";
	
	public String getButton() {
		return button;
	}
	public void setButton(String button) {
		this.button = button;
	}
	public String getTestAdministrationVal() {
		return testAdministrationVal;
	}
	public void setTestAdministrationVal(String testAdministrationVal) {
		this.testAdministrationVal = testAdministrationVal;
	}
	public String getTestAdministrationText() {
		return testAdministrationText;
	}
	public void setTestAdministrationText(String testAdministrationText) {
		this.testAdministrationText = testAdministrationText;
	}
	public String getTestProgram() {
		return testProgram;
	}
	public void setTestProgram(String testProgram) {
		this.testProgram = testProgram;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getStudentFullName() {
		return studentFullName;
	}
	public void setStudentFullName(String studentFullName) {
		this.studentFullName = studentFullName;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public long getAdminId() {
		return adminId;
	}
	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getOrgNodeId() {
		return orgNodeId;
	}
	public void setOrgNodeId(long orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	public String getParentRescoreDate() {
		return parentRescoreDate;
	}
	public void setParentRescoreDate(String parentRescoreDate) {
		this.parentRescoreDate = parentRescoreDate;
	}
	public long getStudentBioId() {
		return studentBioId;
	}
	public void setStudentBioId(long studentBioId) {
		this.studentBioId = studentBioId;
	}
	public String getRequestedDate() {
		return requestedDate;
	}
	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}
	public long getSubtestId() {
		return subtestId;
	}
	public void setSubtestId(long subtestId) {
		this.subtestId = subtestId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getPerformanceLevel() {
		return performanceLevel;
	}
	public void setPerformanceLevel(String performanceLevel) {
		if("A".equals(performanceLevel)){
			this.performanceLevel = "Pass"; 
		}else if("B".equals(performanceLevel)){
			this.performanceLevel = "DNP";
		}else if("U".equals(performanceLevel)){
			this.performanceLevel = "UND";
		}else if("N".equals(performanceLevel)){
			this.performanceLevel = "DNR";
		}else if("P".equals(performanceLevel)){
			this.performanceLevel = "Pass+";
		}else{
			this.performanceLevel = performanceLevel;
		}
	}
	public long getItemsetId() {
		return itemsetId;
	}
	public void setItemsetId(long itemsetId) {
		this.itemsetId = itemsetId;
	}
	public String getIsRequested() {
		return isRequested;
	}
	public void setIsRequested(String isRequested) {
		this.isRequested = isRequested;
	}
	public String getSubtestCode() {
		return subtestCode;
	}
	public void setSubtestCode(String subtestCode) {
		this.subtestCode = subtestCode;
	}
	public List<RescoreSubtestTO> getRescoreSubtestTOList() {
		return rescoreSubtestTOList;
	}
	public void setRescoreSubtestTOList(List<RescoreSubtestTO> rescoreSubtestTOList) {
		this.rescoreSubtestTOList = rescoreSubtestTOList;
	}
	public String getOriginalScore() {
		return originalScore;
	}
	public void setOriginalScore(String originalScore) {
		this.originalScore = originalScore;
	}
	public long getRrfId() {
		return rrfId;
	}
	public void setRrfId(long rrfId) {
		this.rrfId = rrfId;
	}
	public long getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(long itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getItemPart() {
		return itemPart;
	}
	public void setItemPart(String itemPart) {
		this.itemPart = itemPart;
	}
	public String getItemScore() {
		return itemScore;
	}
	public void setItemScore(String itemScore) {
		this.itemScore = itemScore;
	}
	public String getPointPossible() {
		return pointPossible;
	}
	public void setPointPossible(String pointPossible) {
		this.pointPossible = pointPossible;
	}
	public String getIpFileName() {
		return ipFileName;
	}
	public void setIpFileName(String ipFileName) {
		this.ipFileName = ipFileName;
	}
	
}
