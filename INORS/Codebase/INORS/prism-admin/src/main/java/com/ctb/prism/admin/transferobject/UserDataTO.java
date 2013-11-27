/**
 * 
 */
package com.ctb.prism.admin.transferobject;

import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author 233208
 * 
 */
@Record(minOccurs = 0)
public class UserDataTO extends BaseTO {
	private static final long serialVersionUID = 1L;
	
	@Field(at = 0)
	private String userId;
	
	@Field(at = 1)
	private String fullName;
	
	@Field(at = 2)
	private String status;
	
	@Field(at = 3)
	private String orgName;
	
	@Field(at = 4)
	private String userRoles;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(String userRoles) {
		this.userRoles = userRoles;
	}

}
