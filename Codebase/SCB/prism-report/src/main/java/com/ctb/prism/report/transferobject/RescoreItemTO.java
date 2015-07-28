/**
 * 
 */
package com.ctb.prism.report.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.core.util.CustomStringUtil;

/**
 * @author Joy
 * 
 */
public class RescoreItemTO extends BaseTO {
	
	private static final long serialVersionUID = 1L;
	private long itemsetId = 0;
	private String originalScore = "";
	private long rrfId = 0;
	private String isRequested = "";
	private String requestedDate = "";
	private long userId = 0;
	private String userName = "";
	private long itemNumber = 0;
	private long studentBioId = 0;
	private String itemPart = "";
	private String itemScore = "";
	private String pointPossible = "";
	private String performanceLevel = "";
	
	public long getItemsetId() {
		return itemsetId;
	}
	public void setItemsetId(long itemsetId) {
		this.itemsetId = itemsetId;
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
	public String getIsRequested() {
		return isRequested;
	}
	public void setIsRequested(String isRequested) {
		this.isRequested = isRequested;
	}
	public String getRequestedDate() {
		return requestedDate;
	}
	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
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
	public long getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(long itemNumber) {
		this.itemNumber = itemNumber;
	}
	public long getStudentBioId() {
		return studentBioId;
	}
	public void setStudentBioId(long studentBioId) {
		this.studentBioId = studentBioId;
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
	public String getPerformanceLevel() {
		return performanceLevel;
	}
	public void setPerformanceLevel(String performanceLevel) {
		this.performanceLevel = performanceLevel;
	}
	
}
