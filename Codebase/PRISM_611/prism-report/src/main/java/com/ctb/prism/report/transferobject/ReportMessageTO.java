package com.ctb.prism.report.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

public class ReportMessageTO extends BaseTO {
	private static final long serialVersionUID = 1L;

	private String messageType;
	private String messageName;
	private String message;
	private String displayFlag;

	/**
	 * @return the messageType
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType
	 *            the messageType to set
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	/**
	 * @return the messageName
	 */
	public String getMessageName() {
		return messageName;
	}

	/**
	 * @param messageName
	 *            the messageName to set
	 */
	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the displayFlag
	 */
	public String getDisplayFlag() {
		return displayFlag;
	}

	/**
	 * @param displayFlag
	 *            the displayFlag to set
	 */
	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}

}
