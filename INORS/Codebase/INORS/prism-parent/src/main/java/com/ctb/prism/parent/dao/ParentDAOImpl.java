package com.ctb.prism.parent.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.constant.IQueryConstants;
import com.ctb.prism.core.dao.BaseDAO;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.LdapManager;
import com.ctb.prism.core.util.PasswordGenerator;
import com.ctb.prism.core.util.SaltedPasswordEncoder;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.transferobject.ManageContentTO;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.QuestionTO;
import com.ctb.prism.parent.transferobject.StudentTO;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;

@Repository("parentDAO")
public class ParentDAOImpl extends BaseDAO implements IParentDAO {

	@Autowired
	private LdapManager ldapManager;
	@Autowired
	private IPropertyLookup propertyLookup;

	private static final IAppLogger logger = LogFactory.getLoggerInstance(ParentDAOImpl.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getSecretQuestions()
	 */
	@Cacheable(cacheName = "securityQuestions")
	public List<QuestionTO> getSecretQuestions() {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_SECRET_QUESTION);
		List<QuestionTO> questionList = new ArrayList<QuestionTO>();
		QuestionTO quesTo = null;
		for (Map<String, Object> questiondetails : lstData) {
			quesTo = new QuestionTO();
			quesTo.setQuestionId(((BigDecimal) questiondetails.get("QUESTION_ID")).longValue());
			quesTo.setQuestion((String) (questiondetails.get("QUESTION")));
			quesTo.setSno(((BigDecimal) questiondetails.get("SNO")).longValue());
			questionList.add(quesTo);
		}

		return questionList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#checkUserAvailability(java.lang.String)
	 */
	public boolean checkUserAvailability(String username) {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.VALIDATE_USER_NAME, username);
		if (lstData == null || lstData.isEmpty()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#checkActiveUserAvailability(java.lang.String)
	 */
	public boolean checkActiveUserAvailability(String username) {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.VALIDATE_ACTIVE_USER_NAME, username);
		if (lstData == null || lstData.isEmpty()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#isRoleAlreadyTagged(java.lang.String, java.lang.String)
	 */
	public boolean isRoleAlreadyTagged(String roleId, String userName) {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.IS_ROLE_TAGGED, roleId, userName);
		if (lstData == null || lstData.isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#validateIC(java.lang.String)
	 */
	public ParentTO validateIC(String invitationCode) {

		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.VALIDATE_INVITATION_CODE, invitationCode);
		ParentTO parentTO = null;
		if (lstData.size() > 0) {
			parentTO = new ParentTO();
			for (Map<String, Object> fieldDetails : lstData) {
				parentTO.setTotalAttemptedCalim(((BigDecimal) fieldDetails.get("TOTAL_ATTEMPT")).longValue());
				parentTO.setTotalAvailableCalim(((BigDecimal) fieldDetails.get("TOTAL_AVAILABLE")).longValue());
				parentTO.setIcExpirationStatus((String) (fieldDetails.get("EXPIRATION_STATUS")));
				parentTO.setIcActivationStatus((String) (fieldDetails.get("ACTIVATION_STATUS")));
			}
		}
		return parentTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getStudentForIC(java.lang.String)
	 */
	public ParentTO getStudentForIC(String invitationCode) {

		StudentTO studentTO = null;
		ParentTO parentTO = new ParentTO();
		List<StudentTO> studentToList = new ArrayList<StudentTO>();
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_STUDENT_FOR_INVITATION_CODE, invitationCode);
		for (Map<String, Object> fieldDetails : lstData) {
			studentTO = new StudentTO();
			studentTO.setStudentName((String) (fieldDetails.get("STUDENT_NAME")));
			studentTO.setGrade((String) (fieldDetails.get("ADMINISTRATION")));
			studentTO.setAdministration((String) (fieldDetails.get("GRADE")));
			studentToList.add(studentTO);
		}
		parentTO.setStudentToList(studentToList);

		return parentTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#registerUser(com.ctb.prism.parent.transferobject.ParentTO)
	 */
	public boolean registerUser(ParentTO parentTO) throws BusinessException {
		try {
			// incase the user exists into LDAP
			if (IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
				ldapManager.deleteUser(parentTO.getUserName(), parentTO.getUserName(), parentTO.getUserName());
			}
		} catch (Exception ex) {
		}
		/*
		 * String displayName=(CustomStringUtil.appendString(parentTO.getLastName(), parentTO.getFirstName())).trim(); if (displayName.length()>10) displayName=displayName.substring(0, 10);
		 */
		boolean addToLdapStatus = false;
		if (IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
			addToLdapStatus = ldapManager.addUser(parentTO.getUserName(), parentTO.getUserName(), parentTO.getUserName(), parentTO.getPassword());
		} else {
			addToLdapStatus = true;
		}

		if (addToLdapStatus) {
			long user_seq_id = getJdbcTemplatePrism().queryForLong(IQueryConstants.USER_SEQ_ID);
			long orgUserSeqId = getJdbcTemplatePrism().queryForLong(IQueryConstants.USER_SEQ_ID);
			int count = 0;
			if (IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
				count = getJdbcTemplatePrism().update(IQueryConstants.INSERT_USER_DATA, user_seq_id, parentTO.getUserName(), parentTO.getDisplayName(), parentTO.getLastName(),
						parentTO.getFirstName(), parentTO.getMail(), parentTO.getMobile(), parentTO.getCountry(), parentTO.getZipCode(), parentTO.getStreet(), parentTO.getCity(), parentTO.getState(),
						parentTO.getInvitationCode(), parentTO.isFirstTimeUser() ? IApplicationConstants.FLAG_Y : IApplicationConstants.FLAG_N);
			} else {
				// insert user with password into DAO
				String salt = PasswordGenerator.getNextSalt();
				count = getJdbcTemplatePrism().update(IQueryConstants.INSERT_USER_DATA_WITH_PASSWD, user_seq_id, parentTO.getUserName(), parentTO.getDisplayName(), parentTO.getLastName(),
						parentTO.getFirstName(), parentTO.getMail(), parentTO.getMobile(), parentTO.getCountry(), parentTO.getZipCode(), parentTO.getStreet(), parentTO.getCity(), parentTO.getState(),
						parentTO.getInvitationCode(), parentTO.isFirstTimeUser() ? IApplicationConstants.FLAG_Y : IApplicationConstants.FLAG_N,
						SaltedPasswordEncoder.encryptPassword(parentTO.getPassword(), Utils.getSaltWithUser(parentTO.getUserName(), salt)), salt);

				getJdbcTemplatePrism().update(IQueryConstants.INSERT_ORG_USER_PARENT, orgUserSeqId, user_seq_id, parentTO.getInvitationCode(), parentTO.getInvitationCode(),
						IApplicationConstants.ACTIVE_FLAG);
			}
			logger.log(IAppLogger.DEBUG, "INSERT_USER_DATA DONE");

			if (count > 0) {
				getJdbcTemplatePrism().update(IQueryConstants.ADD_ROLE_TO_REGISTERED_USER, parentTO.getUserName(), "ROLE_USER");
				getJdbcTemplatePrism().update(IQueryConstants.ADD_ROLE_TO_REGISTERED_USER, parentTO.getUserName(), "ROLE_PARENT");
				logger.log(IAppLogger.DEBUG, "ADD_ROLE_TO_REGISTERED_USER DONE");
				System.out.println("user_seq_id ::::" + user_seq_id);
				boolean isSavedInvitationCodeClaim = saveInvitationCodeClaim(orgUserSeqId, parentTO);

				if (isSavedInvitationCodeClaim) {
					boolean isUpdatedInvitationCodeClaimCount = updateInvitationCodeClaimCount(parentTO.getInvitationCode());
					if (isUpdatedInvitationCodeClaimCount) {
						boolean isSavedPasswordHistAnswer = savePasswordHistAnswer(user_seq_id, parentTO.getQuestionToList());

						if (isSavedPasswordHistAnswer) {
							/*
							 * ldapManager.addUser(parentTO.getUserName(), parentTO.getUserName(), parentTO.getUserName(), parentTO.getPassword());
							 */
							return Boolean.TRUE;
						}
					}
				}
			}
			return Boolean.FALSE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * Save Invitation code claim.
	 * 
	 * @param orgUserSeqId
	 * @param parentTO
	 * @return
	 */
	public boolean saveInvitationCodeClaim(long orgUserSeqId, ParentTO parentTO) {
		// TODO : Code Review : Not an Interface method but has public method access
		int count = getJdbcTemplatePrism().update(IQueryConstants.INSERT_INVITATION_CODE_CLAIM_DATA, parentTO.getInvitationCode(), orgUserSeqId);
		logger.log(IAppLogger.DEBUG, "INSERT_INVITATION_CODE_CLAIM_DATA Done");
		if (count > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Update Invitation code claim count.
	 * 
	 * @param invitationCode
	 * @return
	 */
	public boolean updateInvitationCodeClaimCount(String invitationCode) {
		// TODO : Code Review : Not an Interface method but has public method access
		int availableCliamCount = getJdbcTemplatePrism().queryForInt(IQueryConstants.CHECK_AVAILABLE_INVITATION_CODE_CLAIM_COUNT, invitationCode);
		if (availableCliamCount > 0) {
			int count1 = getJdbcTemplatePrism().update(IQueryConstants.UPDATE_INVITATION_CODE_CLAIM_COUNT, invitationCode, invitationCode);
			int count2 = getJdbcTemplatePrism().update(IQueryConstants.UPDATE_AVAILABLE_INVITATION_CODE_CLAIM_COUNT, invitationCode, invitationCode);
			logger.log(IAppLogger.DEBUG, "UPDATE_INVITATION_CODE_CLAIM_COUNT Done");
			if ((count1 > 0) && (count2 > 0)) {
				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;
	}

	/**
	 * Save Password hint answer.
	 * 
	 * @param userid
	 * @param questionToList
	 * @return
	 */
	public boolean savePasswordHistAnswer(long userid, List<QuestionTO> questionToList) {
		// TODO : Code Review : Not an Interface method but has public method access
		int count = 0;
		long answerCount = getJdbcTemplatePrism().queryForLong(IQueryConstants.CHECK_FOR_EXISTING_ANSWER, userid);
		logger.log(IAppLogger.DEBUG, "answerCount................" + answerCount);
		if (answerCount == 0) {
			// Inserting answers if there no existing answers
			for (QuestionTO questionTo : questionToList) {
				getJdbcTemplatePrism().update(IQueryConstants.INSERT_ANSWER_DATA, userid, questionTo.getQuestionId(), questionTo.getAnswer());
				logger.log(IAppLogger.DEBUG, "INSERT_ANSWER_DATA Done");
				count++;
			}
		} else {
			// Updating answer if there is existing answer and updating the uadated_date_time
			for (QuestionTO questionTo : questionToList) {
				getJdbcTemplatePrism().update(IQueryConstants.UPDATE_ANSWER_DATA, questionTo.getQuestionId(), questionTo.getAnswer(), userid, questionTo.getAnswerId());
				logger.log(IAppLogger.DEBUG, "UPDATED EXISTING USER ANSWERS");
				count++;
			}
		}

		if (count > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getChildrenList(java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<StudentTO> getChildrenList(final String userName, String clickedTreeNode, String adminYear) {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getChildrenList()");
		long t1 = System.currentTimeMillis();
		List<StudentTO> studentList = null;

		try {
			studentList = (List<StudentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_STUDENT_DETAILS + "}");
					cs.setString(1, userName);
					cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<StudentTO> studentResult = new ArrayList<StudentTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(2);
						StudentTO studentTO = null;
						while (rs.next()) {
							studentTO = new StudentTO();
							studentTO.setStudentName(rs.getString("STUDENT_NAME"));
							studentTO.setStudentBioId(rs.getLong("STUDENT_BIO_ID"));
							studentTO.setAdministration(rs.getString("ADMIN_SEASON_YEAR"));
							studentTO.setGrade(rs.getString("STUDENT_GRADE"));
							studentTO.setStudentGradeId(rs.getLong("STUDENT_GRADEID"));
							studentTO.setAdminid(rs.getString("ADMINID"));
							studentResult.add(studentTO);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return studentResult;
				}
			});
		} catch (Exception e) {
			// Unable to throw the exception from this method - Need code change
			// throw new BusinessException(e.getMessage());
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getChildrenList() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return studentList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getParentList(java.lang.String, java.lang.String, java.lang.String)
	 */
	public ArrayList<ParentTO> getParentList(String orgId, String adminYear, String searchParam, String orgMode) {
		logger.log(IAppLogger.INFO, "Enter: getParentList()");
		ArrayList<ParentTO> parentTOs = new ArrayList<ParentTO>();
		String userName = "";
		String tenantId = "";
		List<Map<String, Object>> lstData = null;
		if (orgId.indexOf("_") > 0) {
			userName = orgId.substring((orgId.indexOf("_") + 1), orgId.length());
			tenantId = orgId.substring(0, orgId.indexOf("_"));
			if (searchParam != null && searchParam.trim().length() > 0) {
				searchParam = CustomStringUtil.appendString("%", searchParam, "%");
				logger.log(IAppLogger.INFO, "GET_PARENT_DETAILS_ON_SCROLL_WITH_SRCH_PARAM");
				logger.log(IAppLogger.INFO, "tenantId = " + tenantId);
				logger.log(IAppLogger.INFO, "userName = " + userName);
				logger.log(IAppLogger.INFO, "searchParam = " + searchParam);
				lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_PARENT_DETAILS_ON_SCROLL_WITH_SRCH_PARAM, orgMode, tenantId, userName, searchParam, searchParam, searchParam);
			} else {
				logger.log(IAppLogger.INFO, "GET_PARENT_DETAILS_ON_SCROLL");
				logger.log(IAppLogger.INFO, "tenantId = " + tenantId);
				logger.log(IAppLogger.INFO, "userName = " + userName);
				lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_PARENT_DETAILS_ON_SCROLL, orgMode, tenantId, userName);
			}
		} else {
			tenantId = orgId;
			logger.log(IAppLogger.INFO, "GET_PARENT_DETAILS_ON_FIRST_LOAD");
			logger.log(IAppLogger.INFO, "tenantId = " + tenantId);
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_PARENT_DETAILS_ON_FIRST_LOAD, orgMode, tenantId);
			logger.log(IAppLogger.DEBUG, "lstData.size() = " + lstData.size());
		}
		if (lstData.size() > 0) {
			parentTOs = new ArrayList<ParentTO>();
			for (Map<String, Object> fieldDetails : lstData) {
				ParentTO to = new ParentTO();
				to.setUserId(((BigDecimal) fieldDetails.get("USERID")).longValue());
				to.setUserName((String) (fieldDetails.get("USERNAME")));
				to.setDisplayName((String) (fieldDetails.get("FULLNAME")));
				to.setStatus((String) (fieldDetails.get("STATUS")));
				to.setOrgId(((BigDecimal) fieldDetails.get("ORG_NODEID")).longValue());
				to.setOrgName((String) (fieldDetails.get("ORG_NODE_NAME")));

				try {
					to.setClikedOrgId(Long.parseLong(tenantId));
				} catch (Exception ex) {
				}
				to.setLastLoginAttempt((String) (fieldDetails.get("LAST_LOGIN_ATTEMPT")));
				parentTOs.add(to);
			}
		}
		logger.log(IAppLogger.DEBUG, "parentTOs.size() = " + parentTOs.size());
		logger.log(IAppLogger.INFO, "Exit: getParentList()");
		return parentTOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#searchParent(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ArrayList<ParentTO> searchParent(String parentName, String tenantId, String adminYear, String isExactSeacrh, String orgMode) {
		logger.log(IAppLogger.INFO, "Enter: searchParent()");
		ArrayList<ParentTO> parentTOs = new ArrayList<ParentTO>();
		List<Map<String, Object>> parentlist = null;
		if (IApplicationConstants.FLAG_N.equalsIgnoreCase(isExactSeacrh)) {
			parentName = CustomStringUtil.appendString("%", parentName, "%");
			logger.log(IAppLogger.INFO, "SEARCH_PARENT");
			logger.log(IAppLogger.INFO, "tenantId = " + tenantId);
			logger.log(IAppLogger.INFO, "parentName = " + parentName);
			parentlist = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_PARENT, orgMode, tenantId, parentName, parentName, parentName, "15");
		} else {
			logger.log(IAppLogger.INFO, "SEARCH_PARENT_EXACT");
			logger.log(IAppLogger.INFO, "tenantId = " + tenantId);
			logger.log(IAppLogger.INFO, "parentName = " + parentName);
			parentlist = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_PARENT_EXACT, orgMode, tenantId, parentName, "15");
		}
		if (parentlist.size() > 0) {
			parentTOs = new ArrayList<ParentTO>();
			for (Map<String, Object> fieldDetails : parentlist) {
				ParentTO to = new ParentTO();
				to.setUserId(((BigDecimal) fieldDetails.get("USERID")).longValue());
				to.setUserName((String) (fieldDetails.get("USERNAME")));
				to.setDisplayName((String) (fieldDetails.get("FULLNAME")));
				to.setStatus((String) (fieldDetails.get("STATUS")));
				to.setOrgName((String) (fieldDetails.get("ORG_NODE_NAME")));
				try {
					to.setClikedOrgId(Long.parseLong(tenantId));
				} catch (Exception ex) {
					logger.log(IAppLogger.WARN, "Skipping Cliked OrgId");
				}
				to.setLastLoginAttempt((String) (fieldDetails.get("LAST_LOGIN_ATTEMPT")));
				parentTOs.add(to);
			}
		}
		logger.log(IAppLogger.DEBUG, "parentTOs.size() = " + parentTOs.size());
		logger.log(IAppLogger.INFO, "Exit: searchParent()");
		return parentTOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#searchParentAutoComplete(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String searchParentAutoComplete(String parentName, String tenantId, String adminYear, String orgMode) {
		parentName = CustomStringUtil.appendString("%", parentName, "%");
		String parentListJsonString = null;
		List<Map<String, Object>> listOfParents = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_PARENT, orgMode, tenantId, parentName, parentName, parentName, "100");
		if (listOfParents != null && listOfParents.size() > 0) {
			parentListJsonString = "[";
			for (Map<String, Object> data : listOfParents) {
				// String parentNameStr = (String) data.get("USERNAME");
				parentListJsonString = CustomStringUtil.appendString(parentListJsonString, "\"", (String) data.get("USERNAME"), "<br/>", (String) data.get("FULLNAME"), "\",");
			}
			parentListJsonString = CustomStringUtil.appendString(parentListJsonString.substring(0, parentListJsonString.length() - 1), "]");
		}
		logger.log(IAppLogger.DEBUG, parentListJsonString);
		return parentListJsonString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getStudentList(Map<String, Object> paramMap)
	 */
	public ArrayList<StudentTO> getStudentList(Map<String, Object> paramMap) {
		String orgId = (String) paramMap.get("scrollId");
		String adminYear = (String) paramMap.get("adminYear");
		String searchParam = (String) paramMap.get("searchParam");
		long customerId = (Long) paramMap.get("currCustomer");
		String orgMode = (String) paramMap.get("orgMode");

		ArrayList<StudentTO> studentTOs = new ArrayList<StudentTO>();
		String studentNameAndId = "";
		String tenantId = "";
		List<Map<String, Object>> lstData = null;
		if (orgId.lastIndexOf("|") > 0) {
			tenantId = orgId.substring((orgId.lastIndexOf("|") + 1), orgId.length());
			studentNameAndId = orgId.substring(0, orgId.lastIndexOf("|"));
			if (searchParam != null && searchParam.trim().length() > 0) {
				searchParam = CustomStringUtil.appendString("%", searchParam, "%");
				lstData = getJdbcTemplatePrism()
						.queryForList(IQueryConstants.GET_STUDENT_DETAILS_ON_SCROLL_WITH_SRCH_PARAM, adminYear, customerId, orgMode, customerId, tenantId, studentNameAndId, searchParam);
			} else {
				lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_STUDENT_DETAILS_ON_SCROLL, adminYear, customerId, orgMode, customerId, tenantId, studentNameAndId);
			}

		} else {
			tenantId = orgId;
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_STUDENT_DETAILS_ON_FIRST_LOAD, orgMode, adminYear, tenantId, customerId);
			logger.log(IAppLogger.DEBUG, lstData.size() + "");
		}

		if (lstData.size() > 0) {
			studentTOs = new ArrayList<StudentTO>();
			for (Map<String, Object> fieldDetails : lstData) {
				StudentTO to = new StudentTO();
				long studentBioId = ((BigDecimal) fieldDetails.get("STUDENT_BIO_ID")).longValue();

				to.setStudentBioId(studentBioId);

				if (getParentAccountDetails(String.valueOf(studentBioId), customerId) != null) {
					to.setParentAccount(getParentAccountDetails(String.valueOf(studentBioId), customerId));
				} else {
					to.setParentAccount(Collections.<ParentTO> emptyList());
				}
				//As data type of TESTELEMENTID 
				//to.setStructureElement(String.valueOf((BigDecimal) (fieldDetails.get("TESTELEMENTID"))));
				to.setStudentName((String) (fieldDetails.get("STUDENTNAME")));
				to.setGrade((String) (fieldDetails.get("STUDENTGRADE")));
				to.setStudentMode((String) (fieldDetails.get("STUDENT_MODE")));
				to.setRowIndentifier((String) (fieldDetails.get("ROWIDENTIFIER")));
				to.setOrgName((String) (fieldDetails.get("SCHOOL")));
				tenantId = (tenantId == null) ? tenantId = "0" : tenantId;
				to.setClikedOrgId(Long.parseLong(tenantId));
				studentTOs.add(to);
			}
		}

		return studentTOs;
	}

	/**
	 * @param studentBioId
	 * @param customerId
	 * @return
	 */
	private ArrayList<ParentTO> getParentAccountDetails(String studentBioId, long customerId) {

		ArrayList<ParentTO> parentTOs = null;
		List<Map<String, Object>> parentAccountData = null;
		if (studentBioId != null) {
			parentAccountData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_PARENT_DETAILS_FOR_CHILDREN, studentBioId, customerId);

			if (parentAccountData.size() > 0) {
				parentTOs = new ArrayList<ParentTO>();
				for (Map<String, Object> fieldDetails : parentAccountData) {
					ParentTO to = new ParentTO();
					to.setUserName((String) (fieldDetails.get("USERNAME")));
					to.setStatus((String) (fieldDetails.get("STATUS")));
					parentTOs.add(to);
				}
			}
		}
		return parentTOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#searchStudentAutoComplete(java.lang.String, java.lang.String, java.lang.String, long)
	 */
	public String searchStudentAutoComplete(String studentName, String tenantId, String adminyear, long customerId, String orgMode) {
		studentName = CustomStringUtil.appendString("%", studentName, "%");
		String studentListJsonString = null;
		List<Map<String, Object>> listOfStudents = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_STUDENT, adminyear, orgMode, tenantId, studentName, customerId, "100");
		if (listOfStudents != null && listOfStudents.size() > 0) {
			studentListJsonString = "[";
			for (Map<String, Object> data : listOfStudents) {
				String studentNameStr = (String) data.get("STUDENTNAME");
				studentListJsonString = CustomStringUtil.appendString(studentListJsonString, "\"", studentNameStr, "\",");
			}
			studentListJsonString = CustomStringUtil.appendString(studentListJsonString.substring(0, studentListJsonString.length() - 1), "]");
		}
		logger.log(IAppLogger.DEBUG, studentListJsonString);
		return studentListJsonString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#searchStudent(java.lang.String, java.lang.String, java.lang.String, long)
	 */
	public ArrayList<StudentTO> searchStudent(String studentName, String tenantId, String adminyear, long customerId, String orgMode) {

		ArrayList<StudentTO> studentTOs = new ArrayList<StudentTO>();
		List<Map<String, Object>> studentlist = null;

		studentName = CustomStringUtil.appendString("%", studentName, "%");

		studentlist = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_STUDENT, adminyear, orgMode, tenantId, studentName, customerId, "15");

		if (studentlist != null && studentlist.size() > 0) {
			studentTOs = new ArrayList<StudentTO>();
			for (Map<String, Object> fieldDetails : studentlist) {
				StudentTO to = new StudentTO();
				long studentBioId = ((BigDecimal) fieldDetails.get("STUDENT_BIO_ID")).longValue();
				to.setStudentBioId(studentBioId);
				if (getParentAccountDetails(String.valueOf(studentBioId), customerId) != null) {
					to.setParentAccount(getParentAccountDetails(String.valueOf(studentBioId), customerId));
				} else {
					to.setParentAccount(Collections.<ParentTO> emptyList());
				}
				//As data type of TESTELEMENTID 
				//to.setStructureElement(String.valueOf((BigDecimal) fieldDetails.get("TESTELEMENT")));
				to.setStudentName((String) (fieldDetails.get("STUDENTNAME")));
				to.setRowIndentifier((String) (fieldDetails.get("ROWIDENTIFIER")));
				to.setGrade((String) (fieldDetails.get("STUDENTGRADE")));
				tenantId = (tenantId == null) ? tenantId = "0" : tenantId;
				to.setClikedOrgId(Long.parseLong(tenantId));
				// to.setInvitationcode((String) (fieldDetails.get("INVITATIONCODE")));
				// to.setOrgId(((BigDecimal) fieldDetails.get("ORG_ID")).longValue());
				// to. setActivationStatus((String) (fieldDetails.get("ACTIVATIONSTATUS")));
				to.setOrgName((String) (fieldDetails.get("SCHOOL")));
				studentTOs.add(to);
			}
		}

		return studentTOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#searchStudentOnRedirect(java.util.Map)
	 */
	public ArrayList<StudentTO> searchStudentOnRedirect(Map<String, Object> paramMap) {
		String studentBioId = (String) paramMap.get("studentBioId");
		String tenantId = (String) paramMap.get("scrollId");
		long customerId = (Long) paramMap.get("customer");
		String orgMode = (String) paramMap.get("orgMode");
		ArrayList<StudentTO> studentTOs = new ArrayList<StudentTO>();
		List<Map<String, Object>> studentlist = null;
		studentlist = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_STUDENT_ON_REDIRECT, orgMode, tenantId, studentBioId);
		if (studentlist != null && studentlist.size() > 0) {
			studentTOs = new ArrayList<StudentTO>();
			for (Map<String, Object> fieldDetails : studentlist) {
				StudentTO to = new StudentTO();
				to.setStudentBioId(((BigDecimal) fieldDetails.get("STUDENT_BIO_ID")).longValue());
				to.setParentAccount(getParentAccountDetails(((Long) (to.getStudentBioId())).toString(), customerId));
				//As data type of TESTELEMENTID 
				//to.setStructureElement((String) (fieldDetails.get("STUDENT_STRUC_ELEMENT")));
				to.setStudentName((String) (fieldDetails.get("STUDENTNAME")));
				to.setRowIndentifier((String) (fieldDetails.get("ROWIDENTIFIER")));
				to.setGrade((String) (fieldDetails.get("STUDENTGRADE")));
				to.setInvitationcode((String) (fieldDetails.get("INVITATIONCODE")));
				try {
					to.setClikedOrgId(Long.parseLong(tenantId));
				} catch (Exception ex) {
				}
				to.setActivationStatus((String) (fieldDetails.get("ACTIVATIONSTATUS")));
				studentTOs.add(to);
			}
		}

		return studentTOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getAssessmentList(java.lang.String)
	 */
	public List<StudentTO> getAssessmentList(String studentBioId) {
		List<StudentTO> assessmentList = null;
		List<Map<String, Object>> list = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ASSESSMENT_LIST, studentBioId);
		if (list != null && list.size() > 0) {
			assessmentList = new ArrayList<StudentTO>();
			for (Map<String, Object> fieldDetails : list) {
				StudentTO studentTO = new StudentTO();
				studentTO.setStudentBioId(((BigDecimal) fieldDetails.get("STUDENT_BIO_ID")).longValue());
				studentTO.setAdministration((String) fieldDetails.get("ASSESSMENT_YEAR"));
				studentTO.setInvitationcode((String) fieldDetails.get("INVITATION_CODE"));
				studentTO.setExpirationDate((String) fieldDetails.get("EXPIRATION_DATE"));
				studentTO.setIcExpirationStatus((String) fieldDetails.get("EXPIRATION_STATUS"));
				studentTO.setTotalAvailableClaim(((BigDecimal) fieldDetails.get("TOTAL_AVAILABLE")).longValue());
				assessmentList.add(studentTO);
			}
		}
		return assessmentList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#updateAssessmentDetails(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean updateAssessmentDetails(String studentBioId, String administration, String invitationcode, String icExpirationStatus, String totalAvailableClaim, String expirationDate)
			throws Exception {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - updateUser");
		try {
			// update invitation_code table
			getJdbcTemplatePrism().update(IQueryConstants.UPDATE_ASSESSMENT, totalAvailableClaim, expirationDate, invitationcode, studentBioId);

		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while updating assessment details.", e);
			return false;
		}
		logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - updateUser");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#firstTimeUserLogin(com.ctb.prism.parent.transferobject.ParentTO)
	 */

	public boolean firstTimeUserLogin(ParentTO parentTO) throws BusinessException {
		Boolean status = false;
		Boolean ldapStatus = false;
		try {
			Long userID = getJdbcTemplatePrism().queryForLong(IQueryConstants.CHECK_EXISTING_USER, parentTO.getUserName());
			if (IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
				ldapStatus = ldapManager.updateUser(parentTO.getUserName(), parentTO.getUserName(), parentTO.getUserName(), parentTO.getPassword());
			} else {
				String salt = PasswordGenerator.getNextSalt();
				getJdbcTemplatePrism().update(IQueryConstants.UPDATE_PASSWORD_DATA, IApplicationConstants.FLAG_Y,
						SaltedPasswordEncoder.encryptPassword(parentTO.getPassword(), Utils.getSaltWithUser(parentTO.getUserName(), salt)), salt, parentTO.getUserName());
				ldapStatus = true;
			}
			if (ldapStatus) {
				int count = getJdbcTemplatePrism().update(IQueryConstants.UPDATE_FIRSTTIMEUSERLOGIN_DATA, parentTO.getLastName(), parentTO.getFirstName(), parentTO.getMail(), parentTO.getMobile(),
						parentTO.getCountry(), parentTO.getZipCode(), parentTO.getState(), parentTO.getStreet(), parentTO.getCity(), userID);

				logger.log(IAppLogger.DEBUG, "INSERT_USER_DATA Done");
				if (count > 0) {
					boolean isSavedPasswordHistAnswer = savePasswordHistAnswer(userID, parentTO.getQuestionToList());
					if (isSavedPasswordHistAnswer) {
						int count1 = getJdbcTemplatePrism().update(IQueryConstants.UPDATE_FIRSTTIMEUSERFLAG_DATA, IApplicationConstants.FLAG_N, parentTO.getUserName());
						// logger.log(IAppLogger.DEBUG, "INSERT_USER_DATA Done");
						if (count1 > 0) {
							status = true;
						}
					}
				}
			}
		} catch (BusinessException bex) {
			throw new BusinessException(bex.getCustomExceptionMessage());
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while updating frist time user details.", e);
			return false;
		}
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#manageParentAccountDetails(java.lang.String)
	 */
	public ParentTO manageParentAccountDetails(String username) {

		ParentTO parentTO = new ParentTO();
		List<Map<String, Object>> lstData = null;
		try {
			// populate parent account details
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_PARENT_DETAILS_BY_USERNAME, username);

			if (lstData.size() > 0) {
				for (Map<String, Object> fieldDetails : lstData) {
					parentTO.setUserId(((BigDecimal) fieldDetails.get("USERID")).longValue());
					parentTO.setUserName((String) (fieldDetails.get("USERNAME")));
					parentTO.setDisplayName((String) (fieldDetails.get("DISPLAY_USERNAME")));
					parentTO.setLastName((String) (fieldDetails.get("LAST_NAME")));
					parentTO.setFirstName((String) (fieldDetails.get("FIRST_NAME")));
					// parentTO.setMiddleName((String) (fieldDetails.get("MIDDLE_NAME")));
					parentTO.setMail((String) (fieldDetails.get("EMAIL_ADDRESS")));
					parentTO.setMobile((String) (fieldDetails.get("PHONE_NO")));
					parentTO.setCountry((String) (fieldDetails.get("COUNTRY")));
					parentTO.setZipCode((String) (fieldDetails.get("ZIPCODE")));
					parentTO.setCity((String) (fieldDetails.get("CITY")));
					parentTO.setState((String) (fieldDetails.get("STATE")));
					parentTO.setStreet((String) (fieldDetails.get("STREET")));
				}
			}
			// setting secret question list by calling method getParentSecretQuestionDetails()
			parentTO.setQuestionToList(getParentSecretQuestionDetails(username));
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while retrieving parent details for manage.", e);
		}
		return parentTO;
	}

	/**
	 * Get parent secret questions details for manage.
	 * 
	 * @param username
	 * @return
	 */
	public ArrayList<QuestionTO> getParentSecretQuestionDetails(String username) {
		// TODO : Code Review : Not an Interface method but has public method access
		List<Map<String, Object>> lstData = null;
		ArrayList<QuestionTO> questionTOs = new ArrayList<QuestionTO>();

		// populate parent security question and answer detail
		lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_PARENT_SECURITY_QUESTION, username);
		if (lstData.size() > 0) {

			for (Map<String, Object> fieldDetails : lstData) {
				QuestionTO questionTo = new QuestionTO();
				questionTo.setSno(((BigDecimal) fieldDetails.get("SNO")).longValue());
				questionTo.setQuestionId(((BigDecimal) fieldDetails.get("QUESTION_ID")).longValue());
				questionTo.setQuestion((String) fieldDetails.get("QUESTION"));
				questionTo.setAnswerId(((BigDecimal) fieldDetails.get("ANSWER_ID")).longValue());
				questionTo.setAnswer((String) fieldDetails.get("ANSWER"));

				questionTOs.add(questionTo);
			}
		}
		return questionTOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getSecurityQuestionForUser(java.lang.String)
	 */

	public ArrayList<QuestionTO> getSecurityQuestionForUser(String username) {

		List<Map<String, Object>> lstData = null;
		ArrayList<QuestionTO> questionTOs = new ArrayList<QuestionTO>();

		// populate parent security question and answer detail
		lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_PARENT_SECURITY_QUESTION, username);
		if (lstData.size() > 0) {

			for (Map<String, Object> fieldDetails : lstData) {
				QuestionTO questionTo = new QuestionTO();
				questionTo.setSno(((BigDecimal) fieldDetails.get("SNO")).longValue());
				questionTo.setQuestionId(((BigDecimal) fieldDetails.get("QUESTION_ID")).longValue());
				questionTo.setQuestion((String) fieldDetails.get("QUESTION"));
				// questionTo.setAnswerId(((BigDecimal) fieldDetails.get("ANSWER_ID")).longValue());
				// questionTo.setAnswer((String)fieldDetails.get("ANSWER"));

				questionTOs.add(questionTo);
			}
		}
		return questionTOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#validateAnswers(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean validateAnswers(String userName, String ans1, String ans2, String ans3, String questionId1, String questionId2, String questionId3) {
		long validUser = 0;
		try {
			validUser = getJdbcTemplatePrism().queryForLong(IQueryConstants.VALIDATE_SECURITY_ANSWERS, userName, questionId1, ans1, userName, questionId2, ans2, questionId3, ans3);
			if (validUser != 0 && validUser != -1 && validUser == 1) {
				return true;
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while validating answers.", e);
			return false;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getUserNamesByEmail(java.lang.String)
	 */
	public List<UserTO> getUserNamesByEmail(String emailId) {
		ArrayList<UserTO> UserTOs = new ArrayList<UserTO>();
		List<Map<String, Object>> userslist = null;
		userslist = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ALL_USERS_BY_EMAIL, emailId);
		if (userslist.size() > 0) {

			for (Map<String, Object> fieldDetails : userslist) {
				UserTO to = new UserTO();
				to.setUserName((String) (fieldDetails.get("USERNAME")));
				to.setFirstName((String) (fieldDetails.get("FIRST_NAME")));
				to.setLastName((String) (fieldDetails.get("LAST_NAME")));
				UserTOs.add(to);

			}

		}

		return UserTOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#updateUserProfile(com.ctb.prism.parent.transferobject.ParentTO)
	 */
	@TriggersRemove(cacheName = "orgUsers", removeAll = true)
	public boolean updateUserProfile(ParentTO parentTO) throws BusinessException {

		long user_id = parentTO.getUserId();
		String password = parentTO.getPassword();
		try {
			boolean ldapFlag = true;
			// calling ldapManager for updating password for a username in LDAP
			if (password != null && !"".equals(password)) {
				if (IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
					ldapFlag = ldapManager.updateUser(parentTO.getUserName(), parentTO.getUserName(), parentTO.getUserName(), password);
				} else {
					String salt = PasswordGenerator.getNextSalt();
					getJdbcTemplatePrism().update(IQueryConstants.UPDATE_PASSWORD_DATA, IApplicationConstants.FLAG_N,
							SaltedPasswordEncoder.encryptPassword(password, Utils.getSaltWithUser(parentTO.getUserName(), salt)), salt, parentTO.getUserName());
					ldapFlag = true;
				}
			}
			if (ldapFlag) {
				// updating user details in users table
				int count = getJdbcTemplatePrism().update(IQueryConstants.UPDATE_USER_DATA, parentTO.getLastName(), parentTO.getFirstName(), parentTO.getMail(), parentTO.getMobile(),
						parentTO.getCountry(), parentTO.getZipCode(), parentTO.getState(), parentTO.getStreet(), parentTO.getCity(), parentTO.getDisplayName(), user_id);
				if (count > 0) {
					// delete security answers for that user and then insert as fresh
					// boolean isDeleted= deletePasswordHistAnswer(user_id);
					// if(isDeleted){
					savePasswordHistAnswer(user_id, parentTO.getQuestionToList());
					// }
				}
			}
		} catch (BusinessException bex) {
			throw new BusinessException(bex.getCustomExceptionMessage());
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while updating user profile details.", e);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * Delete Password hint answers for a userid.
	 * 
	 * @param userid
	 * @return
	 */
	public boolean deletePasswordHistAnswer(long userid) {
		// TODO : Code Review : Not an Interface method but has public method access
		getJdbcTemplatePrism().update(IQueryConstants.DELETE_ANSWER_DATA, userid);
		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#checkInvitationCodeClaim(java.lang.String, java.lang.String)
	 */
	public boolean checkInvitationCodeClaim(String userName, String invitationCode) {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.CHECK_INVITATION_CODE_CLAIM, userName, invitationCode);
		if (lstData == null || lstData.isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#addInvitationToAccount(java.lang.String, java.lang.String)
	 */
	public boolean addInvitationToAccount(String userName, String invitationCode) {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - addInvitationToAccount()");
		long t1 = System.currentTimeMillis();

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userName", userName);
		paramMap.put("invitationCode", invitationCode);

		try {
			long orgUserid = getOrgUserId(paramMap);
			int count = getJdbcTemplatePrism().update(IQueryConstants.ADD_INVITATION_CODE_TO_ACCOUNT, orgUserid, invitationCode);
			if (count > 0) {
				boolean isUpdatedInvitationCodeClaimCount = updateInvitationCodeClaimCount(invitationCode);
				return isUpdatedInvitationCodeClaimCount;
			}
		} catch (Exception e) {
			return Boolean.FALSE;
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - addInvitationToAccount() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return Boolean.FALSE;
	}

	/**
	 * Get OrgUserId depending upon student's school and parent userid. Add a record if the data does not exists.
	 * 
	 * @author Joy
	 * @param paramMap
	 * @return
	 */
	private long getOrgUserId(final Map<String, Object> paramMap) {

		String userName = (String) paramMap.get("userName");
		String invitationCode = (String) paramMap.get("invitationCode");
		long orgUserid = 0;
		long userid = getJdbcTemplatePrism().queryForLong(IQueryConstants.GET_USERID_PARENT, userName);
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.CHECK_ORG_USER_PARENT, userid, invitationCode);
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				orgUserid = ((BigDecimal) fieldDetails.get("ORG_USER_ID")).longValue();
			}
		} else {
			orgUserid = getJdbcTemplatePrism().queryForLong(IQueryConstants.USER_SEQ_ID);
			// Insert data in ORG_USERS
			getJdbcTemplatePrism().update(IQueryConstants.INSERT_ORG_USER_PARENT, orgUserid, userid, invitationCode, invitationCode, IApplicationConstants.ACTIVE_FLAG);
		}
		return orgUserid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getSchoolOrgId(java.lang.String)
	 */
	public String getSchoolOrgId(String studentBioId) {
		return getJdbcTemplatePrism().queryForObject(IQueryConstants.FETCH_SCHOOLID_FOR_STUDENT, new Object[] { studentBioId }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int col) throws SQLException {
				return ((BigDecimal) rs.getObject(1)).toString();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#generateActivationCode(com.ctb.prism.parent.transferobject.StudentTO)
	 */
	public boolean generateActivationCode(StudentTO student) {
		int count = getJdbcTemplatePrism().update(IQueryConstants.ADD_NEW_INVITATION_CODE, student.getInvitationcode(), student.getStudentBioId());
		if (count > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#disableActivationCode(com.ctb.prism.parent.transferobject.StudentTO)
	 */
	public boolean disableActivationCode(StudentTO student) {
		int count = getJdbcTemplatePrism().update(IQueryConstants.UPDATE_ACTIVATION_CODE, student.getInvitationcode(), student.getStudentBioId());
		if (count > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#populateGrade(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateGrade(final Map<String, Object> paramMap) throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - populateGrade()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		long t1 = System.currentTimeMillis();
		final long custProdId = ((Long) paramMap.get("custProdId")).longValue();
		try {
			objectValueTOList = (List<com.ctb.prism.core.transferobject.ObjectValueTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_GRADE + "}");
					cs.setLong(1, custProdId);
					cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOResult = new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(2);
						com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;

						while (rs.next()) {
							objectValueTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
							objectValueTO.setValue(rs.getString("VALUE"));
							objectValueTO.setName(rs.getString("NAME"));
							objectValueTOResult.add(objectValueTO);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return objectValueTOResult;
				}
			});
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - populateGrade() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return objectValueTOList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#populateSubtest(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateSubtest(final Map<String, Object> paramMap) throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - populateSubtest()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		long t1 = System.currentTimeMillis();
		final long custProdId = ((Long) paramMap.get("custProdId")).longValue();
		final long gradeId = ((Long) paramMap.get("gradeId")).longValue();
		try {
			objectValueTOList = (List<com.ctb.prism.core.transferobject.ObjectValueTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_SUBTEST + "}");
					cs.setLong(1, custProdId);
					cs.setLong(2, gradeId);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOResult = new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(3);
						com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;

						while (rs.next()) {
							objectValueTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
							objectValueTO.setValue(rs.getString("VALUE"));
							objectValueTO.setName(rs.getString("NAME"));
							objectValueTOResult.add(objectValueTO);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return objectValueTOResult;
				}
			});
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - populateSubtest() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return objectValueTOList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#populateObjective(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateObjective(final Map<String, Object> paramMap) throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - populateObjective()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		long t1 = System.currentTimeMillis();
		final long subtestId = ((Long) paramMap.get("subtestId")).longValue();
		final long gradeId = ((Long) paramMap.get("gradeId")).longValue();
		try {
			objectValueTOList = (List<com.ctb.prism.core.transferobject.ObjectValueTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_OBJECTIVE + "}");
					cs.setLong(1, subtestId);
					cs.setLong(2, gradeId);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOResult = new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(3);
						com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;

						while (rs.next()) {
							objectValueTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
							objectValueTO.setValue(rs.getString("VALUE"));
							objectValueTO.setName(rs.getString("NAME"));
							objectValueTOResult.add(objectValueTO);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return objectValueTOResult;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - populateObjective() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return objectValueTOList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#addNewContent(java.util.Map)
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO addNewContent(final Map<String, Object> paramMap) throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - addNewContent()");
		com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
		long t1 = System.currentTimeMillis();
		final ManageContentTO manageContentTO = (ManageContentTO) paramMap.get("manageContentTO");

		try {
			objectValueTO = (com.ctb.prism.core.transferobject.ObjectValueTO) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.ADD_NEW_CONTENT + "}");
					cs.setString(1, manageContentTO.getContentDescription());
					cs.setString(2, manageContentTO.getContentName());
					cs.setLong(3, manageContentTO.getCustProdId());
					cs.setLong(4, manageContentTO.getSubtestId());
					cs.setLong(5, manageContentTO.getObjectiveId());
					cs.setString(6, manageContentTO.getContentTypeName());
					cs.setString(7, manageContentTO.getContentType());
					cs.setString(8, manageContentTO.getSubHeader());
					cs.setLong(9, manageContentTO.getGradeId());
					cs.setString(10, manageContentTO.getPerformanceLevel());
					cs.registerOutParameter(11, oracle.jdbc.OracleTypes.NUMBER);
					cs.registerOutParameter(12, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					long executionStatus = 0;
					com.ctb.prism.core.transferobject.ObjectValueTO statusTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
					try {
						cs.execute();
						executionStatus = cs.getLong(11);
						statusTO.setValue(Long.toString(executionStatus));
						statusTO.setName("");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return statusTO;
				}
			});
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - addNewContent() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return objectValueTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#loadManageContent(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public List<ManageContentTO> loadManageContent(final Map<String, Object> paramMap) throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - loadManageContent()");
		long t1 = System.currentTimeMillis();
		List<ManageContentTO> manageContentList = null;
		final long custProdId = Long.parseLong((String) paramMap.get("custProdId"));
		final long subtestId = Long.parseLong((String) paramMap.get("subtestId"));
		final long objectiveId = Long.parseLong((String) paramMap.get("objectiveId"));
		final String contentTypeId = (String) paramMap.get("contentTypeId");
		final String checkFirstLoad = (String) paramMap.get("checkFirstLoad");

		try {
			manageContentList = (List<ManageContentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					if (checkFirstLoad.equals("true")) {
						CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_MANAGE_CONTENT_LIST + "}");
						cs.setLong(1, custProdId);
						cs.setLong(2, subtestId);
						cs.setLong(3, objectiveId);
						cs.setString(4, contentTypeId);
						cs.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(6, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					} else {
						final long lastid = Long.parseLong((String) paramMap.get("lastid"));
						CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_MANAGE_CONTENT_LIST_MORE + "}");
						cs.setLong(1, custProdId);
						cs.setLong(2, subtestId);
						cs.setLong(3, objectiveId);
						cs.setLong(4, lastid);
						cs.setString(5, contentTypeId);
						cs.registerOutParameter(6, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(7, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<ManageContentTO> manageContentListResult = new ArrayList<ManageContentTO>();
					try {
						cs.execute();
						if (checkFirstLoad.equals("true")) {
							rs = (ResultSet) cs.getObject(5);
						} else {
							rs = (ResultSet) cs.getObject(6);
						}

						while (rs.next()) {
							ManageContentTO manageContentTO = new ManageContentTO();
							manageContentTO.setContentId(rs.getLong("METADATA_ID"));
							manageContentTO.setContentName(rs.getString("NAME"));
							manageContentTO.setSubHeader(rs.getString("SUB_HEADER"));
							manageContentTO.setGradeName(rs.getString("GRADE"));
							manageContentTO.setPerformanceLevel(rs.getString("PROFICIENCY_LEVEL"));
							manageContentListResult.add(manageContentTO);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return manageContentListResult;
				}
			});
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - loadManageContent() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return manageContentList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getContentForEdit(java.util.Map)
	 */
	public ManageContentTO getContentForEdit(final Map<String, Object> paramMap) throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getContentForEdit()");
		long t1 = System.currentTimeMillis();
		ManageContentTO manageContentTO = null;
		final long contentId = ((Long) paramMap.get("contentId")).longValue();

		try {
			manageContentTO = (ManageContentTO) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_MANAGE_CONTENT_FOR_EDIT + "}");
					cs.setLong(1, contentId);
					cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ManageContentTO manageContentTOResult = null;
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(2);
						if (rs.next()) {
							manageContentTOResult = new ManageContentTO();
							manageContentTOResult.setContentId(rs.getLong("METADATA_ID"));
							manageContentTOResult.setContentName(rs.getString("NAME"));
							manageContentTOResult.setSubHeader(rs.getString("SUB_HEADER"));
							manageContentTOResult.setContentDescription(Utils.convertClobToString((Clob) rs.getClob("CONTENT_DESCRIPTION")));
							manageContentTOResult.setPerformanceLevel(rs.getString("PROFICIENCY_LEVEL"));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return manageContentTOResult;
				}
			});
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getContentForEdit() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return manageContentTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#updateContent(java.util.Map)
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO updateContent(final Map<String, Object> paramMap) throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - updateContent()");
		com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
		long t1 = System.currentTimeMillis();
		final ManageContentTO manageContentTO = (ManageContentTO) paramMap.get("manageContentTO");

		try {
			objectValueTO = (com.ctb.prism.core.transferobject.ObjectValueTO) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.UPDATE_CONTENT + "}");
					cs.setLong(1, manageContentTO.getContentId());
					cs.setString(2, manageContentTO.getContentName());
					cs.setString(3, manageContentTO.getSubHeader());
					cs.setString(4, manageContentTO.getContentDescription());
					cs.setString(5, manageContentTO.getPerformanceLevel());
					cs.registerOutParameter(6, oracle.jdbc.OracleTypes.NUMBER);
					cs.registerOutParameter(7, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					long executionStatus = 0;
					com.ctb.prism.core.transferobject.ObjectValueTO statusTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
					try {
						cs.execute();
						executionStatus = cs.getLong(6);
						statusTO.setValue(Long.toString(executionStatus));
						statusTO.setName("");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return statusTO;
				}
			});
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - updateContent() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return objectValueTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#deleteContent(java.util.Map)
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO deleteContent(final Map<String, Object> paramMap) throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - deleteContent()");
		com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
		long t1 = System.currentTimeMillis();
		final long contentId = ((Long) paramMap.get("contentId")).longValue();

		try {
			objectValueTO = (com.ctb.prism.core.transferobject.ObjectValueTO) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.DELETE_CONTENT + "}");
					cs.setLong(1, contentId);
					cs.registerOutParameter(2, oracle.jdbc.OracleTypes.NUMBER);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					long executionStatus = 0;
					com.ctb.prism.core.transferobject.ObjectValueTO statusTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
					try {
						cs.execute();
						executionStatus = cs.getLong(2);
						statusTO.setValue(Long.toString(executionStatus));
						statusTO.setName("");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return statusTO;
				}
			});
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - deleteContent() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return objectValueTO;
	}

	/**
	 * @author Joy Get content details for edit depending upon article_metedata id Not usable now Remove the code after testing
	 */
	/*
	 * public ManageContentTO modifyStandardForEdit(final Map<String,Object> paramMap) throws BusinessException{ logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - modifyStandardForEdit()"); long t1
	 * = System.currentTimeMillis(); ManageContentTO manageContentTO = null; final long objectiveId = ((Long) paramMap.get("objectiveId")).longValue();
	 * 
	 * try{ manageContentTO = (ManageContentTO) getJdbcTemplatePrism().execute( new CallableStatementCreator() { public CallableStatement createCallableStatement(Connection con) throws SQLException {
	 * CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_OBJECTIVE_DETAILS_FOR_EDIT + "}"); cs.setLong(1, objectiveId); cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
	 * return cs; } } , new CallableStatementCallback<Object>() { public Object doInCallableStatement(CallableStatement cs) { ResultSet rs = null; ManageContentTO manageContentTOResult = null; try {
	 * cs.execute(); rs = (ResultSet) cs.getObject(2); if(rs.next()){ manageContentTOResult = new ManageContentTO(); manageContentTOResult.setObjectiveId(rs.getLong("OBJECTIVEID"));
	 * manageContentTOResult.setContentDescription(Utils.convertClobToString((Clob)rs.getClob("CONTENT_DESCRIPTION"))); } } catch (SQLException e) { e.printStackTrace(); } catch (Exception e) {
	 * e.printStackTrace(); } return manageContentTOResult; } }); }catch(Exception e){ throw new BusinessException(e.getMessage()); }finally{ long t2 = System.currentTimeMillis();
	 * logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - modifyStandardForEdit() took time: "+String.valueOf(t2 - t1)+"ms"); } return manageContentTO; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#modifyGenericForEdit(java.util.Map)
	 */
	public ManageContentTO modifyGenericForEdit(final Map<String, Object> paramMap) throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - modifyGenericForEdit()");
		long t1 = System.currentTimeMillis();
		ManageContentTO manageContentTO = null;
		final long custProdId = ((Long) paramMap.get("custProdId")).longValue();
		final long gradeId = ((Long) paramMap.get("gradeId")).longValue();
		final long subtestId = ((Long) paramMap.get("subtestId")).longValue();
		final long objectiveId = ((Long) paramMap.get("objectiveId")).longValue();
		final String type = (String) paramMap.get("type");

		try {
			manageContentTO = (ManageContentTO) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_GENERIC_DETAILS_FOR_EDIT + "}");
					cs.setLong(1, custProdId);
					cs.setLong(2, gradeId);
					cs.setLong(3, subtestId);
					cs.setLong(4, objectiveId);
					cs.setString(5, type);
					cs.registerOutParameter(6, oracle.jdbc.OracleTypes.CURSOR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ManageContentTO manageContentTOResult = null;
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(6);
						if (rs.next()) {
							manageContentTOResult = new ManageContentTO();
							manageContentTOResult.setContentId(rs.getLong("METADATA_ID"));
							manageContentTOResult.setContentDescription(Utils.convertClobToString((Clob) rs.getClob("CONTENT_DESCRIPTION")));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return manageContentTOResult;
				}
			});
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - modifyGenericForEdit() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return manageContentTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getStudentSubtest(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getStudentSubtest(final Map<String, Object> paramMap) throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getStudentSubtest()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		long t1 = System.currentTimeMillis();
		final long studentBioId = Long.parseLong((String) paramMap.get("studentBioId"));

		try {
			objectValueTOList = (List<com.ctb.prism.core.transferobject.ObjectValueTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_STUDENT_SUBTEST + "}");
					cs.setLong(1, studentBioId);
					cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOResult = new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(2);
						com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;

						while (rs.next()) {
							objectValueTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
							objectValueTO.setValue(rs.getString("VALUE"));
							objectValueTO.setName(rs.getString("NAME"));
							objectValueTOResult.add(objectValueTO);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return objectValueTOResult;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getStudentSubtest() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return objectValueTOList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getArticleTypeDetails(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public List<ManageContentTO> getArticleTypeDetails(final Map<String, Object> paramMap) throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getArticleTypeDetails()");
		List<ManageContentTO> articleTypeDetailsList = null;
		long t1 = System.currentTimeMillis();
		final long studentBioId = ((Long) paramMap.get("studentBioId")).longValue();
		final long subtestId = ((Long) paramMap.get("subtestId"));
		final long studentGradeId = ((Long) paramMap.get("studentGradeId"));
		final String contentType = (String) paramMap.get("contentType");

		try {
			articleTypeDetailsList = (List<ManageContentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_ARTICLE_TYPE_DETAILS + "}");
					cs.setLong(1, studentBioId);
					cs.setLong(2, subtestId);
					cs.setLong(3, studentGradeId);
					cs.setString(4, contentType);
					cs.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(6, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<ManageContentTO> articleTypeDetailsResult = new ArrayList<ManageContentTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(5);
						ManageContentTO articleTypeDetailsTO = null;

						while (rs.next()) {
							articleTypeDetailsTO = new ManageContentTO();
							articleTypeDetailsTO.setObjectiveId(rs.getLong("STANDARD_ID"));
							articleTypeDetailsTO.setObjectiveName(rs.getString("STANDARD_NAME"));
							articleTypeDetailsTO.setContentId(rs.getLong("ARTICLEID"));
							articleTypeDetailsTO.setContentName(rs.getString("ARTICLE_NAME"));
							articleTypeDetailsTO.setSubHeader(rs.getString("ARTICLE_SUB_HEADER"));
							articleTypeDetailsTO.setProficiencyLevel(rs.getString("PROFICENCY_LEVEL"));
							articleTypeDetailsTO.setObjContentId(rs.getLong("STD_ARTICLEID"));
							articleTypeDetailsTO.setCustProdId(rs.getLong("CUST_PROD_ID"));
							articleTypeDetailsResult.add(articleTypeDetailsTO);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return articleTypeDetailsResult;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getArticleTypeDetails() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return articleTypeDetailsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getArticleDescription(java.util.Map)
	 */
	public ManageContentTO getArticleDescription(final Map<String, Object> paramMap) throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getArticleDescription()");
		long t1 = System.currentTimeMillis();
		ManageContentTO manageContentTO = null;
		final long custProdId = ((Long) paramMap.get("custProdId")).longValue();
		final long studentBioId = ((Long) paramMap.get("studentBioId")).longValue();
		final long articleId = ((Long) paramMap.get("articleId")).longValue();
		final String contentType = (String) paramMap.get("contentType");
		final long subtestId = ((Long) paramMap.get("subtestId")).longValue();
		final long studentGradeId = ((Long) paramMap.get("studentGradeId")).longValue();

		try {
			manageContentTO = (ManageContentTO) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_ARTICLE_DESCRIPTION + "}");
					cs.setLong(1, custProdId);
					cs.setLong(2, studentBioId);
					cs.setLong(3, articleId);
					cs.setLong(4, studentGradeId);
					cs.setLong(5, subtestId);
					cs.setString(6, contentType);
					cs.registerOutParameter(7, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(8, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ManageContentTO manageContentTOResult = null;
					try {

						cs.execute();
						rs = (ResultSet) cs.getObject(7);
						if (rs.next()) {
							manageContentTOResult = new ManageContentTO();
							manageContentTOResult.setContentId(rs.getLong("ARTICLEID"));
							manageContentTOResult.setContentName(rs.getString("ARTICLE_NAME"));
							manageContentTOResult.setContentDescription(Utils.convertClobToString((Clob) rs.getClob("CONTENT_DESCRIPTION")));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return manageContentTOResult;
				}
			});
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getArticleDescription() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return manageContentTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getGradeSubtestInfo(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public List<ManageContentTO> getGradeSubtestInfo(final Map<String, Object> paramMap) throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getGradeSubtestInfo()");
		List<ManageContentTO> gradeSubtestList = null;
		long t1 = System.currentTimeMillis();
		final UserTO loggedinUserTO = (UserTO) paramMap.get("loggedinUserTO");

		try {
			gradeSubtestList = (List<ManageContentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = null;
					cs = con.prepareCall("{call " + IQueryConstants.GET_GRADE_SUBTEST_INFO + "}");
					cs.setLong(1, Long.valueOf(loggedinUserTO.getCustomerId()));
					cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<ManageContentTO> gradeSubtestResult = new ArrayList<ManageContentTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(2);
						ManageContentTO gradeSubtestTO = null;

						while (rs.next()) {
							gradeSubtestTO = new ManageContentTO();
							gradeSubtestTO.setGradeId(rs.getLong("GRADE_ID"));
							gradeSubtestTO.setGradeName(rs.getString("GRADE_NAME"));
							gradeSubtestTO.setSubtestId(rs.getLong("SUBTEST_ID"));
							gradeSubtestTO.setSubtestName(rs.getString("SUBTEST_NAME"));
							gradeSubtestTO.setCustProdId(rs.getLong("CUST_PROD_ID"));
							gradeSubtestResult.add(gradeSubtestTO);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return gradeSubtestResult;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getGradeSubtestInfo() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return gradeSubtestList;
	}
}
