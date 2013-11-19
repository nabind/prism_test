package com.ctb.prism.report.transferobject;

import java.util.ArrayList;
import java.util.List;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.transferobject.BaseTO;

public class ManageMessageTO extends BaseTO {
	
	private static final long serialVersionUID = 1L;
	
	private long reportId = 0;
	private long messageTypeId = 0;
	private String messageTypeName = "";
	private String messageTypeDesc = "";
	private String messageType = "";
	private long dbMessageId = 0;
	private String message = "";
	private String activationStatus = IApplicationConstants.DEFAULT_VALUE_DRM_CHECKBOX;
	private long customerId = 0;
	private long msgTypeCustId = 0;
	private long gradeId = 0;
	private String gradeName = "";
	private List<ObjectValueTO> gradeTOList = new ArrayList<ObjectValueTO>();
	private long[] gradeIdArr;
	private String[] gradeNameArr;
	private long msgTypeCustSeq = 0;
	private long adminIdHidden = 0;
	private long custProdIdHidden = 0;
	private long orgNodeLevelIdHidden = 0;
	private long roleIdHidden = 0;
	
	
	public long getReportId() {
		return reportId;
	}
	public void setReportId(long reportId) {
		this.reportId = reportId;
	}
	public long getMessageTypeId() {
		return messageTypeId;
	}
	public void setMessageTypeId(long messageTypeId) {
		this.messageTypeId = messageTypeId;
	}
	public String getMessageTypeName() {
		return messageTypeName;
	}
	public void setMessageTypeName(String messageTypeName) {
		this.messageTypeName = messageTypeName;
	}
	public String getMessageTypeDesc() {
		return messageTypeDesc;
	}
	public void setMessageTypeDesc(String messageTypeDesc) {
		this.messageTypeDesc = messageTypeDesc;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getActivationStatus() {
		return activationStatus;
	}
	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public long getDbMessageId() {
		return dbMessageId;
	}
	public void setDbMessageId(long dbMessageId) {
		this.dbMessageId = dbMessageId;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getMsgTypeCustId() {
		return msgTypeCustId;
	}
	public void setMsgTypeCustId(long msgTypeCustId) {
		this.msgTypeCustId = msgTypeCustId;
	}
	public long getMsgTypeCustSeq() {
		return msgTypeCustSeq;
	}
	public void setMsgTypeCustSeq(long msgTypeCustSeq) {
		this.msgTypeCustSeq = msgTypeCustSeq;
	}
	public long getGradeId() {
		return gradeId;
	}
	public void setGradeId(long gradeId) {
		this.gradeId = gradeId;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public long[] getGradeIdArr() {
		return gradeIdArr;
	}
	public void setGradeIdArr(long[] gradeIdArr) {
		this.gradeIdArr = gradeIdArr;
	}
	public String[] getGradeNameArr() {
		return gradeNameArr;
	}
	public void setGradeNameArr(String[] gradeNameArr) {
		this.gradeNameArr = gradeNameArr;
	}
	public List<ObjectValueTO> getGradeTOList() {
		return gradeTOList;
	}
	public void setGradeTOList(List<ObjectValueTO> gradeTOList) {
		this.gradeTOList = gradeTOList;
	}
	public long getAdminIdHidden() {
		return adminIdHidden;
	}
	public void setAdminIdHidden(long adminIdHidden) {
		this.adminIdHidden = adminIdHidden;
	}
	public long getOrgNodeLevelIdHidden() {
		return orgNodeLevelIdHidden;
	}
	public void setOrgNodeLevelIdHidden(long orgNodeLevelIdHidden) {
		this.orgNodeLevelIdHidden = orgNodeLevelIdHidden;
	}
	public long getRoleIdHidden() {
		return roleIdHidden;
	}
	public void setRoleIdHidden(long roleIdHidden) {
		this.roleIdHidden = roleIdHidden;
	}
	public long getCustProdIdHidden() {
		return custProdIdHidden;
	}
	public void setCustProdIdHidden(long custProdIdHidden) {
		this.custProdIdHidden = custProdIdHidden;
	}
}



