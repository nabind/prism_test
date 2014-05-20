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
	
}
