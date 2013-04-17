package com.ctb.prism.core.transferobject;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * This class is the base Transfer Object. Other Transfer Objects should
 * extend this class
 *
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
@SuppressWarnings("serial")
public class BaseTO implements Serializable {
	private long createdBy;
	private Timestamp createdAt;
	private long udatedBy;
	private long clikedOrgId;
	private Timestamp updatedAt;
	private String rowIndentifier;
	private String errorMsg;
	
	public long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public long getUdatedBy() {
		return udatedBy;
	}
	public void setUdatedBy(long udatedBy) {
		this.udatedBy = udatedBy;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getRowIndentifier() {
		return rowIndentifier;
	}
	public void setRowIndentifier(String rowIndentifier ) {
		this.rowIndentifier = rowIndentifier;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}	
	public long getClikedOrgId() {
		return clikedOrgId;
	}
	public void setClikedOrgId(long clikedOrgId) {
		this.clikedOrgId = clikedOrgId;
	}
}
