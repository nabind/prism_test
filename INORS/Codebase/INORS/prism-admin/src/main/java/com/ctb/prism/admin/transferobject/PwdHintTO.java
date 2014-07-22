package com.ctb.prism.admin.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author TCS
 * @version 1.1
 */
public class PwdHintTO extends BaseTO {

	private static final long serialVersionUID = 1L;

	private Long userId;

	private Long questionId;
	private String questionValue;
	private Long questionSequence;
	private String questionActivationStatus;

	private Long answerId;
	private String answerValue;

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the questionId
	 */
	public Long getQuestionId() {
		return questionId;
	}

	/**
	 * @param questionId
	 *            the questionId to set
	 */
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	/**
	 * @return the questionValue
	 */
	public String getQuestionValue() {
		return questionValue;
	}

	/**
	 * @param questionValue
	 *            the questionValue to set
	 */
	public void setQuestionValue(String questionValue) {
		this.questionValue = questionValue;
	}

	/**
	 * @return the questionSequence
	 */
	public Long getQuestionSequence() {
		return questionSequence;
	}

	/**
	 * @param questionSequence
	 *            the questionSequence to set
	 */
	public void setQuestionSequence(Long questionSequence) {
		this.questionSequence = questionSequence;
	}

	/**
	 * @return the questionActivationStatus
	 */
	public String getQuestionActivationStatus() {
		return questionActivationStatus;
	}

	/**
	 * @param questionActivationStatus
	 *            the questionActivationStatus to set
	 */
	public void setQuestionActivationStatus(String questionActivationStatus) {
		this.questionActivationStatus = questionActivationStatus;
	}

	/**
	 * @return the answerId
	 */
	public Long getAnswerId() {
		return answerId;
	}

	/**
	 * @param answerId
	 *            the answerId to set
	 */
	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
	}

	/**
	 * @return the answerValue
	 */
	public String getAnswerValue() {
		return answerValue;
	}

	/**
	 * @param answerValue
	 *            the answerValue to set
	 */
	public void setAnswerValue(String answerValue) {
		this.answerValue = answerValue;
	}

}
