package com.ctb.prism.admin.transferobject;

import java.util.ArrayList;
import java.util.List;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author TCS
 * version 1.1
 */

public class EduCenterTO extends BaseTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 53818111201133053L;
	
	private long eduCenterId = 0;
	private String eduCenterCode = "";
	private String eduCenterName = "";
	private long userId = 0;
	private String userName = "";
	private String fullName = "";
	private String status = "";
	private List<com.ctb.prism.core.transferobject.ObjectValueTO> roleList = new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
	
	public long getEduCenterId() {
		return eduCenterId;
	}
	public void setEduCenterId(long eduCenterId) {
		this.eduCenterId = eduCenterId;
	}
	public String getEduCenterCode() {
		return eduCenterCode;
	}
	public void setEduCenterCode(String eduCenterCode) {
		this.eduCenterCode = eduCenterCode;
	}
	public String getEduCenterName() {
		return eduCenterName;
	}
	public void setEduCenterName(String eduCenterName) {
		this.eduCenterName = eduCenterName;
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
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getRoleList() {
		return roleList;
	}
	public void setRoleList(
			List<com.ctb.prism.core.transferobject.ObjectValueTO> roleList) {
		this.roleList = roleList;
	}
}

