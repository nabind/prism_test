package com.ctb.prism.parent.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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
import java.sql.Clob;


@Repository("parentDAO")
public class ParentDAOImpl extends BaseDAO implements IParentDAO {

	@Autowired
	private LdapManager ldapManager;
	@Autowired
	private IPropertyLookup propertyLookup;
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(ParentDAOImpl.class.getName());

	/**
	 * Fetch all questions
	 * 
	 * @return Map
	 */
	@Cacheable(cacheName = "securityQuestions")
	public List<QuestionTO> getSecretQuestions() {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism()
				.queryForList(IQueryConstants.GET_SECRET_QUESTION);
		List<QuestionTO> questionList = new ArrayList<QuestionTO>();
		QuestionTO quesTo = null;
		for (Map<String, Object> questiondetails : lstData) {
			quesTo = new QuestionTO();
			quesTo.setQuestionId(((BigDecimal) questiondetails
					.get("QUESTION_ID")).longValue());
			quesTo.setQuestion((String) (questiondetails.get("QUESTION")));
			quesTo.setSno(((BigDecimal) questiondetails.get("SNO")).longValue());
			questionList.add(quesTo);
		}

		return questionList;
	}

	/**
	 * check provided user availability
	 * 
	 * @return boolean
	 */
	public boolean checkUserAvailability(String username) {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism()
				.queryForList(IQueryConstants.VALIDATE_USER_NAME, username);
		if (lstData == null || lstData.isEmpty()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	/**
	 * check provided user and activation status for forgot password
	 * 
	 * @return boolean
	 */
	public boolean checkActiveUserAvailability(String username) {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism()
				.queryForList(IQueryConstants.VALIDATE_ACTIVE_USER_NAME, username);
		if (lstData == null || lstData.isEmpty()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	/**
	 * check check whether role is already tagged to this user
	 * 
	 * @return boolean
	 */
	public boolean isRoleAlreadyTagged(String roleId, String userName){
		List<Map<String, Object>> lstData = getJdbcTemplatePrism()
		.queryForList(IQueryConstants.IS_ROLE_TAGGED, roleId,userName);
					if (lstData == null || lstData.isEmpty()) {
						return Boolean.FALSE;
					}
		return Boolean.TRUE;
	}

	/**
	 * check provided invitation code availability
	 * 
	 * @return boolean
	 */
	public ParentTO validateIC(String invitationCode) {

		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.VALIDATE_INVITATION_CODE,invitationCode);
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

	/**
	 * Fetch all Students for invitation code
	 * 
	 * @return ParentTO
	 */
	public ParentTO getStudentForIC(String invitationCode) {

		StudentTO studentTO = null;
		ParentTO parentTO = new ParentTO();
		List<StudentTO> studentToList = new ArrayList<StudentTO>();
		List<Map<String, Object>> lstData = getJdbcTemplatePrism()
				.queryForList(IQueryConstants.GET_STUDENT_FOR_INVITATION_CODE,
						invitationCode);
			for (Map<String, Object> fieldDetails : lstData) {
				studentTO = new StudentTO();
				studentTO.setStudentName((String) (fieldDetails
						.get("STUDENT_NAME")));
				studentTO
						.setGrade((String) (fieldDetails.get("ADMINISTRATION")));
				studentTO
						.setAdministration((String) (fieldDetails.get("GRADE")));
				studentToList.add(studentTO);
			}
			parentTO.setStudentToList(studentToList);

		return parentTO;
	}

	/**
	 * Save parent user information
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean registerUser(ParentTO parentTO) throws BusinessException {
		try {
			// incase the user exists into LDAP
			if(IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
				ldapManager.deleteUser(parentTO.getUserName(), parentTO.getUserName(), parentTO.getUserName());
			}
		} catch (Exception ex){}
		/*String displayName=(CustomStringUtil.appendString(parentTO.getLastName(),  parentTO.getFirstName())).trim();
			if (displayName.length()>10) displayName=displayName.substring(0, 10);*/
		boolean addToLdapStatus = false;
		if(IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
			addToLdapStatus = ldapManager.addUser(parentTO.getUserName(), parentTO.getUserName(),
				parentTO.getUserName(), parentTO.getPassword());
		} else {
			addToLdapStatus = true;
		}
		
		if( addToLdapStatus ) {
			long user_seq_id = getJdbcTemplatePrism().queryForLong(
					IQueryConstants.USER_SEQ_ID);
			long orgUserSeqId = getJdbcTemplatePrism().queryForLong(
					IQueryConstants.USER_SEQ_ID);
			int count = 0;
			if(IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
				count = getJdbcTemplatePrism().update(
								IQueryConstants.INSERT_USER_DATA, user_seq_id,
								parentTO.getUserName(),parentTO.getDisplayName(),
								parentTO.getLastName(), parentTO.getFirstName(),
								parentTO.getMail(), parentTO.getMobile(),
								parentTO.getCountry(), parentTO.getZipCode(), 
								parentTO.getStreet(),parentTO.getCity(), parentTO.getState(), 
								parentTO.getInvitationCode(), 
								parentTO.isFirstTimeUser() ? IApplicationConstants.FLAG_Y : IApplicationConstants.FLAG_N);
			} else {
				// insert user with password into DAO
				String salt = PasswordGenerator.getNextSalt();
				count = getJdbcTemplatePrism().update(
								IQueryConstants.INSERT_USER_DATA_WITH_PASSWD, user_seq_id,
								parentTO.getUserName(),parentTO.getDisplayName(),
								parentTO.getLastName(), parentTO.getFirstName(),
								parentTO.getMail(), parentTO.getMobile(),
								parentTO.getCountry(), parentTO.getZipCode(), 
								parentTO.getStreet(),parentTO.getCity(), parentTO.getState(), 
								parentTO.getInvitationCode(), 
								parentTO.isFirstTimeUser() ? IApplicationConstants.FLAG_Y : IApplicationConstants.FLAG_N,
								SaltedPasswordEncoder.encryptPassword(parentTO.getPassword(), Utils.getSaltWithUser(parentTO.getUserName(), salt)),
								salt);
				
				getJdbcTemplatePrism()
				.update(IQueryConstants.INSERT_ORG_USER_PARENT,
						orgUserSeqId, user_seq_id, parentTO.getInvitationCode(),parentTO.getInvitationCode(), IApplicationConstants.ACTIVE_FLAG);
			}
			logger.log(IAppLogger.DEBUG, "INSERT_USER_DATA DONE");

			if (count > 0) {
				getJdbcTemplatePrism().update(IQueryConstants.ADD_ROLE_TO_REGISTERED_USER,parentTO.getUserName(),"ROLE_USER");
				getJdbcTemplatePrism().update(IQueryConstants.ADD_ROLE_TO_REGISTERED_USER,parentTO.getUserName(),"ROLE_PARENT");
				logger.log(IAppLogger.DEBUG, "ADD_ROLE_TO_REGISTERED_USER DONE");
				System.out.println("user_seq_id ::::" +user_seq_id);	
				boolean isSavedInvitationCodeClaim = saveInvitationCodeClaim(orgUserSeqId, parentTO);
					
				if (isSavedInvitationCodeClaim) {
					boolean isUpdatedInvitationCodeClaimCount = updateInvitationCodeClaimCount(parentTO.getInvitationCode());
					if (isUpdatedInvitationCodeClaimCount) {
						boolean isSavedPasswordHistAnswer = savePasswordHistAnswer(
								user_seq_id, parentTO.getQuestionToList());
		
						if (isSavedPasswordHistAnswer) {
							/*ldapManager.addUser(parentTO.getUserName(), parentTO.getUserName(),
									parentTO.getUserName(), parentTO.getPassword());*/
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
	 * Save Invitation code claim
	 * 
	 * @return boolean
	 */
	public boolean saveInvitationCodeClaim(long orgUserSeqId, ParentTO parentTO) {

		int count = getJdbcTemplatePrism().update(
				IQueryConstants.INSERT_INVITATION_CODE_CLAIM_DATA,
				parentTO.getInvitationCode(), orgUserSeqId);
		logger.log(IAppLogger.DEBUG, "INSERT_INVITATION_CODE_CLAIM_DATA Done");
		if (count > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Update Invitation code claim count
	 * 
	 * @return boolean
	 */
	public boolean updateInvitationCodeClaimCount(String invitationCode) {
		int availableCliamCount= getJdbcTemplatePrism().queryForInt(IQueryConstants.CHECK_AVAILABLE_INVITATION_CODE_CLAIM_COUNT,invitationCode);
		if (availableCliamCount >0){
			int count1 = getJdbcTemplatePrism().update(
					IQueryConstants.UPDATE_INVITATION_CODE_CLAIM_COUNT, invitationCode, invitationCode);
			int count2 = getJdbcTemplatePrism().update(
					IQueryConstants.UPDATE_AVAILABLE_INVITATION_CODE_CLAIM_COUNT, invitationCode, invitationCode);
			logger.log(IAppLogger.DEBUG, "UPDATE_INVITATION_CODE_CLAIM_COUNT Done");
			if ((count1 > 0) && (count2 > 0)) {
				return Boolean.TRUE;
			}
		}
		
		return Boolean.FALSE;
	}
	

	/**
	 * Save Password hint answer
	 * 
	 * @return boolean
	 */
	public boolean savePasswordHistAnswer(long userid,
			List<QuestionTO> questionToList) {
		int count = 0;
		long answerCount= getJdbcTemplatePrism().queryForLong(IQueryConstants.CHECK_FOR_EXISTING_ANSWER,userid);	
		logger.log(IAppLogger.DEBUG, "answerCount................"+answerCount);
		if(answerCount==0){
			//Inserting answers if there no existing answers
			for (QuestionTO questionTo : questionToList) {
				getJdbcTemplatePrism().update(IQueryConstants.INSERT_ANSWER_DATA,
						userid, questionTo.getQuestionId(), questionTo.getAnswer());
				logger.log(IAppLogger.DEBUG, "INSERT_ANSWER_DATA Done");
				count++;
			}
		}else{	
				//Updating answer if there is existing answer and updating the uadated_date_time
					for (QuestionTO questionTo : questionToList) {
						getJdbcTemplatePrism().update(IQueryConstants.UPDATE_ANSWER_DATA,
								questionTo.getQuestionId(), questionTo.getAnswer(),userid,questionTo.getAnswerId() );
						logger.log(IAppLogger.DEBUG, "UPDATED EXISTING USER ANSWERS");
						count++;
				}
			}
		
		if (count > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	
	/**
	 * Retrieves the list of the children of the logged in parent.
	 * This information is displayed in the home page of the parent login.
	 * @param userName User Name of the logged in parent 
	 * Updated by Joy on 23-DEC-2013
	 */
	@SuppressWarnings("unchecked")
	public List<StudentTO> getChildrenList(final String userName,String clickedTreeNode, String adminYear ) {
		/*List<StudentTO> children = null;
		List<Map<String,Object>> list = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_CHILDREN, userName);
		logger.log(IAppLogger.DEBUG, "outside...... list");
		if ( list != null && list.size() > 0 )
		{
			logger.log(IAppLogger.DEBUG, "Inside...... list");
			children = new ArrayList<StudentTO>();
			for (Map<String, Object> data : list) {
				StudentTO studentTO = new StudentTO();
				studentTO.setStudentName((String) data.get("STUDENT_NAME"));
				studentTO.setStudentBioId(((BigDecimal) data.get("STUDENT_BIO_ID"))
						.longValue());
				studentTO.setAdministration((String) data.get("ADMIN_SEASON_YEAR"));
				studentTO.setGrade((String) data.get("STUDENTGRADE"));
				studentTO.setAdminid(((BigDecimal) data.get("ADMINID")).toString());
				children.add(studentTO);
			}
		}
		return children;*/
		
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getChildrenList()");
		long t1 = System.currentTimeMillis();
		List<StudentTO> studentList = null;
		
		try{
			studentList = (List<StudentTO>) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				        	CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_STUDENT_DETAILS + "}");
					            cs.setString(1, userName);
					            cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR); 
					            cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
					            return cs;
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rs = null;
			        			List<StudentTO> studentResult = new ArrayList<StudentTO>();
			        			try {
									cs.execute();
									rs = (ResultSet) cs.getObject(2);
									StudentTO studentTO = null;
									while(rs.next()){
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
		}catch(Exception e){
			//Unable to throw the exception from this method - Need code change
			//throw new BusinessException(e.getMessage());
			e.printStackTrace();
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getChildrenList() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return studentList;
	}
	
	/**
	 * Returns the parentList on load.
	 * 
	 * @param orgId
	 * @return
	 */
	public ArrayList<ParentTO> getParentList(String orgId, String adminYear, String searchParam) {

		ArrayList<ParentTO> parentTOs = new ArrayList<ParentTO>();
		String userName = "";
		String tenantId = "";
		List<Map<String, Object>> lstData = null;
		if (orgId.indexOf("_") > 0) {
			userName = orgId.substring((orgId.indexOf("_") + 1),orgId.length());
			tenantId = orgId.substring(0, orgId.indexOf("_"));
			if(searchParam != null && searchParam.trim().length() > 0) {
				searchParam = CustomStringUtil.appendString("%", searchParam, "%");
				lstData = getJdbcTemplatePrism().queryForList(
						IQueryConstants.GET_PARENT_DETAILS_ON_SCROLL_WITH_SRCH_PARAM, tenantId, userName, 
						searchParam, searchParam, searchParam);
			} else {
				lstData = getJdbcTemplatePrism().queryForList(
						IQueryConstants.GET_PARENT_DETAILS_ON_SCROLL, tenantId, userName);
			}
		} else {
			tenantId = orgId;
			lstData = getJdbcTemplatePrism().queryForList(
					IQueryConstants.GET_PARENT_DETAILS_ON_FIRST_LOAD, tenantId);
			logger.log(IAppLogger.DEBUG, lstData.size()+"");
		}

		if (lstData.size() > 0) {
			parentTOs = new ArrayList<ParentTO>();
			for (Map<String, Object> fieldDetails : lstData) {
				ParentTO to = new ParentTO();
				to.setUserId(((BigDecimal) fieldDetails.get("USERID"))
						.longValue());
				to.setUserName((String) (fieldDetails.get("USERNAME")));
				to.setDisplayName((String) (fieldDetails.get("FULLNAME")));
				to.setStatus((String) (fieldDetails.get("STATUS")));				to.setOrgId(((BigDecimal) fieldDetails.get("ORG_NODEID")).longValue());
				to.setOrgName((String) (fieldDetails.get("ORG_NODE_NAME")));

				try{to.setClikedOrgId(Long.parseLong(tenantId));} catch(Exception ex){}
				to.setLastLoginAttempt((String) (fieldDetails.get("LAST_LOGIN_ATTEMPT")));
				parentTOs.add(to);
			}
		}

		return parentTOs;
	}	
	
	/**
	 * Searches and returns the parent names(use like operator) as a JSON
	 * string. Performs case insensitive searching. This method is used to
	 * perform auto complete in search box.
	 * 
	 * @param parentName
	 *            Search String treated as parent name
	 * @param tenantId
	 *            parentId of the logged in user
	 */
public ArrayList <ParentTO> searchParent(String parentName, String tenantId, String adminYear,String isExactSeacrh){
		
		ArrayList<ParentTO> parentTOs = new ArrayList<ParentTO>();
		List<Map<String, Object>> parentlist = null;
		
		if (IApplicationConstants.FLAG_N.equalsIgnoreCase(isExactSeacrh)){
			parentName = CustomStringUtil.appendString("%", parentName, "%");
			parentlist = getJdbcTemplatePrism().queryForList(
					IQueryConstants.SEARCH_PARENT, tenantId, parentName,parentName,parentName, "15");
		}	else{
			parentlist = getJdbcTemplatePrism().queryForList(
					IQueryConstants.SEARCH_PARENT_EXACT, tenantId, parentName, "15");
		}
		
		 
		
		if (parentlist.size() > 0) {
			parentTOs = new ArrayList<ParentTO>();
			for (Map<String, Object> fieldDetails : parentlist) {
				ParentTO to = new ParentTO();
				to.setUserId(((BigDecimal) fieldDetails.get("USERID")).longValue());
				to.setUserName((String) (fieldDetails.get("USERNAME")));
				to.setDisplayName((String) (fieldDetails.get("FULLNAME")));
				to.setStatus((String) (fieldDetails.get("STATUS")));
				try{to.setClikedOrgId(Long.parseLong(tenantId));} catch(Exception ex){}
				to.setLastLoginAttempt((String) (fieldDetails.get("LAST_LOGIN_ATTEMPT")));
				parentTOs.add(to);
			}
		}
		return parentTOs;
	}
	
	/**
	 * Searches and returns the parent(s) with given name (like operator).
	 * Performs case insensitive searching.
	 * 
	 * @param parentName
	 *            Search String treated as parent name
	 * @param tenantId
	 *            parentId of the logged in user
	 * @return
	 */
	public String searchParentAutoComplete( String parentName, String tenantId, String adminYear ) {
		parentName = CustomStringUtil.appendString("%", parentName, "%");
		String parentListJsonString = null;
		List<Map<String, Object>> listOfParents = getJdbcTemplatePrism().queryForList(
				IQueryConstants.SEARCH_PARENT, tenantId, parentName,parentName,parentName, "100");
		if (listOfParents != null && listOfParents.size() > 0) {
			parentListJsonString = "[";
			for (Map<String, Object> data : listOfParents) {
				//String parentNameStr = (String) data.get("USERNAME");
				parentListJsonString = CustomStringUtil.appendString(
						parentListJsonString, "\"", (String) data.get("USERNAME"), "<br/>"
						, (String) data.get("FULLNAME"), "\",");
			}
			parentListJsonString = CustomStringUtil.appendString(parentListJsonString
					.substring(0, parentListJsonString.length() - 1), "]");
		}
		logger.log(IAppLogger.DEBUG, parentListJsonString);
		return parentListJsonString;
	}
	
	/**
	 * 
	 */
	public ArrayList<StudentTO> getStudentList(String orgId, String adminYear, String searchParam , long customerId) {

		ArrayList<StudentTO> studentTOs = new ArrayList<StudentTO>();
		String studentNameAndId = "";
		String tenantId = "";
		List<Map<String, Object>> lstData = null;
		if (orgId.lastIndexOf("|") > 0) {
			tenantId = orgId.substring((orgId.lastIndexOf("|") + 1),orgId.length());
			studentNameAndId = orgId.substring(0, orgId.lastIndexOf("|"));
			if(searchParam != null && searchParam.trim().length() > 0) {
				searchParam = CustomStringUtil.appendString("%", searchParam, "%");
				lstData = getJdbcTemplatePrism().queryForList(
						IQueryConstants.GET_STUDENT_DETAILS_ON_SCROLL_WITH_SRCH_PARAM,adminYear,customerId,customerId,tenantId,studentNameAndId, searchParam);
			} else {
				lstData = getJdbcTemplatePrism().queryForList(
						IQueryConstants.GET_STUDENT_DETAILS_ON_SCROLL,adminYear,customerId,customerId,tenantId,studentNameAndId);
			}
			
		} else {
			tenantId = orgId;
			lstData = getJdbcTemplatePrism().queryForList(
					IQueryConstants.GET_STUDENT_DETAILS_ON_FIRST_LOAD,adminYear,tenantId,customerId);
			logger.log(IAppLogger.DEBUG, lstData.size()+"");
		}

		if (lstData.size() > 0) {
			studentTOs = new ArrayList<StudentTO>();
			for (Map<String, Object> fieldDetails : lstData) {
				StudentTO to = new StudentTO();
				long studentBioId = ((BigDecimal) fieldDetails.get("STUDENT_BIO_ID")).longValue();
				
				to.setStudentBioId(studentBioId);
				
				if(!checkTASCProduct(customerId) && getParentAccountDetails(String.valueOf(studentBioId), customerId)!=null)
				{
					to.setParentAccount(getParentAccountDetails(String.valueOf(studentBioId),customerId));
				}
				else
				{
					to.setParentAccount(Collections.<ParentTO>emptyList());
				}
				to.setStructureElement((String)(fieldDetails.get("TESTELEMENTID")));
				to.setStudentName((String) (fieldDetails.get("STUDENTNAME")));
				to.setGrade((String) (fieldDetails.get("STUDENTGRADE")));
				to.setStudentMode((String) (fieldDetails.get("STUDENT_MODE")));
				to.setRowIndentifier((String) (fieldDetails.get("ROWIDENTIFIER")));
				to.setOrgName((String) (fieldDetails.get("SCHOOL")));
				tenantId = (tenantId == null)? tenantId = "0" : tenantId;
				to.setClikedOrgId(Long.parseLong(tenantId));
				studentTOs.add(to);
			}
		}

		return studentTOs;
	}
	
	/**
	 * 
	 * @param studentBioId
	 * @return
	 */	
	private ArrayList<ParentTO> getParentAccountDetails(String studentBioId, long customerId) {
		
		ArrayList<ParentTO> parentTOs = null;
		List<Map<String, Object>> parentAccountData = null;
		if (studentBioId!=null)
		{
			parentAccountData = getJdbcTemplatePrism().queryForList(
				IQueryConstants.GET_PARENT_DETAILS_FOR_CHILDREN, studentBioId, customerId);
		
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
	
	
	
	
	
	/**
	 * Searches and returns the students(s) with given name (like operator).
	 * Performs case insensitive searching.
	 * 
	 * @param parentName
	 *            Search String treated as student name
	 * @param tenantId
	 *            parentId of the logged in user
	 * @return
	 */
	public String searchStudentAutoComplete( String studentName, String tenantId, String adminyear, long customerId) {
		studentName = CustomStringUtil.appendString("%", studentName, "%");
		String studentListJsonString = null;
		List<Map<String, Object>> listOfStudents = getJdbcTemplatePrism().queryForList(
				IQueryConstants.SEARCH_STUDENT,adminyear,tenantId,studentName,customerId,"100");
		if (listOfStudents != null && listOfStudents.size() > 0) {
			studentListJsonString = "[";
			for (Map<String, Object> data : listOfStudents) {
				String studentNameStr = (String) data.get("STUDENTNAME");
				studentListJsonString = CustomStringUtil.appendString(
						studentListJsonString, "\"", studentNameStr, "\",");
			}
			studentListJsonString = CustomStringUtil.appendString(studentListJsonString
					.substring(0, studentListJsonString.length() - 1), "]");
		}
		logger.log(IAppLogger.DEBUG, studentListJsonString);
		return studentListJsonString;
	}
	
	/**
	 * Searches and returns the student names(use like operator) as a JSON
	 * string. Performs case insensitive searching. This method is used to
	 * perform auto complete in search box.
	 * 
	 * @param studentName
	 *            Search String treated as student name
	 * @param tenantId
	 *            parentId of the logged in user
	 */
	public ArrayList <StudentTO> searchStudent(String studentName, String tenantId, String adminyear, long customerId){
		
		ArrayList<StudentTO> studentTOs = new ArrayList<StudentTO>();
		List<Map<String, Object>> studentlist = null;
		
		studentName = CustomStringUtil.appendString("%", studentName, "%");
		
		studentlist = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_STUDENT, adminyear,tenantId, studentName,customerId, "15");
		
			if (studentlist != null && studentlist.size() > 0) {
				studentTOs = new ArrayList<StudentTO>();
				for (Map<String, Object> fieldDetails : studentlist) {
					StudentTO to = new StudentTO();
					long studentBioId = ((BigDecimal) fieldDetails.get("STUDENT_BIO_ID")).longValue();
					to.setStudentBioId(studentBioId);
					if(!checkTASCProduct(customerId) && getParentAccountDetails(String.valueOf(studentBioId), customerId)!=null)
					{
						to.setParentAccount(getParentAccountDetails(String.valueOf(studentBioId),customerId));
					}
					else
					{
						to.setParentAccount(Collections.<ParentTO>emptyList());
					}
					to.setStructureElement((String) (fieldDetails.get("TESTELEMENT")));
					to.setStudentName((String) (fieldDetails.get("STUDENTNAME")));
					to.setRowIndentifier((String) (fieldDetails.get("ROWIDENTIFIER")));
					to.setGrade((String) (fieldDetails.get("STUDENTGRADE")));
					tenantId = (tenantId == null)? tenantId = "0" : tenantId;
					to.setClikedOrgId(Long.parseLong(tenantId));
					//to.setInvitationcode((String) (fieldDetails.get("INVITATIONCODE")));
					//to.setOrgId(((BigDecimal) fieldDetails.get("ORG_ID")).longValue());
					//to. setActivationStatus((String) (fieldDetails.get("ACTIVATIONSTATUS")));
					to.setOrgName((String) (fieldDetails.get("SCHOOL")));
					studentTOs.add(to);
				}
			}



		return studentTOs;
	}
	
	
	public ArrayList <StudentTO> searchStudentOnRedirect(String studentBioId, String tenantId, long customerId)
	{
		ArrayList<StudentTO> studentTOs = new ArrayList<StudentTO>();
		List<Map<String, Object>> studentlist = null;
				
		studentlist = getJdbcTemplatePrism().queryForList(
				IQueryConstants.SEARCH_STUDENT_ON_REDIRECT, tenantId, studentBioId);
		
			if (studentlist != null && studentlist.size() > 0) {
				studentTOs = new ArrayList<StudentTO>();
				for (Map<String, Object> fieldDetails : studentlist) {
					StudentTO to = new StudentTO();
					to.setStudentBioId(((BigDecimal) fieldDetails.get("STUDENT_BIO_ID"))
							.longValue());
					to.setParentAccount(getParentAccountDetails(((Long)(to.getStudentBioId())).toString(),customerId));
					to.setStructureElement((String) (fieldDetails.get("STUDENT_STRUC_ELEMENT")));
					to.setStudentName((String) (fieldDetails.get("STUDENTNAME")));
					to.setRowIndentifier((String) (fieldDetails.get("ROWIDENTIFIER")));
					to.setGrade((String) (fieldDetails.get("STUDENTGRADE")));
					to.setInvitationcode((String) (fieldDetails.get("INVITATIONCODE")));
					try{to.setClikedOrgId(Long.parseLong(tenantId));} catch(Exception ex){}
					to. setActivationStatus((String) (fieldDetails.get("ACTIVATIONSTATUS")));
					studentTOs.add(to);
				}
			}



		return studentTOs;
	}
	
	/**
	 * Retrieves the list of the children of the logged in parent.
	 * This information is displayed in the home page of the parent login.
	 * @param userName User Name of the logged in parent 
	 */
	public List<StudentTO> getAssessmentList( String studentBioId ) {
		List<StudentTO> assessmentList = null;
		List<Map<String,Object>> list = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ASSESSMENT_LIST, studentBioId);
		if ( list != null && list.size() > 0 )
		{
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
	
	public boolean updateAssessmentDetails(String studentBioId, String administration, String invitationcode,
			String icExpirationStatus, String totalAvailableClaim,String expirationDate) throws Exception {

		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - updateUser");
		try {		
				// update invitation_code table
				getJdbcTemplatePrism().update(IQueryConstants.UPDATE_ASSESSMENT,
						totalAvailableClaim, expirationDate, invitationcode, studentBioId);
		
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while updating assessment details.", e);
			return false;
		}
		logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - updateUser");
		return true;
	}
	
	/**
	 * by Deepak Save First Time user profile &(ChangePAssword)
	 * 
	 * @return boolean
	 * @throws BusinessException 
	 * @throws Exception
	 */

	public boolean firstTimeUserLogin(ParentTO parentTO) throws BusinessException {
		Boolean status = false;
		Boolean ldapStatus = false;
		try {
			Long userID = getJdbcTemplatePrism()
					.queryForLong(IQueryConstants.CHECK_EXISTING_USER,
							parentTO.getUserName());
			if(IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
				ldapStatus = ldapManager.updateUser(
					parentTO.getUserName(), parentTO.getUserName(), parentTO.getUserName(),
					parentTO.getPassword());
			} else {
				String salt = PasswordGenerator.getNextSalt();
				getJdbcTemplatePrism().update(IQueryConstants.UPDATE_PASSWORD_DATA,IApplicationConstants.FLAG_Y,
						SaltedPasswordEncoder.encryptPassword(parentTO.getPassword(), Utils.getSaltWithUser(parentTO.getUserName(), salt)), 
						salt, parentTO.getUserName());
				ldapStatus = true;
			}
			if (ldapStatus) {
				int count = getJdbcTemplatePrism().update(
						IQueryConstants.UPDATE_FIRSTTIMEUSERLOGIN_DATA,
						parentTO.getLastName(), parentTO.getFirstName(),
						parentTO.getMail(), parentTO.getMobile(),
						parentTO.getCountry(), parentTO.getZipCode(),parentTO.getState(),parentTO.getStreet(), parentTO.getCity(),userID);
				
				logger.log(IAppLogger.DEBUG, "INSERT_USER_DATA Done");
				if (count > 0) {
					boolean isSavedPasswordHistAnswer = savePasswordHistAnswer(
							userID, parentTO.getQuestionToList());
					if (isSavedPasswordHistAnswer) {
						int count1 = getJdbcTemplatePrism().update(
								IQueryConstants.UPDATE_FIRSTTIMEUSERFLAG_DATA,
								IApplicationConstants.FLAG_N,parentTO.getUserName());
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
			logger.log(IAppLogger.ERROR, 
					"Error occurred while updating frist time user details.", e);
			return false;
		}
		return status;
	}
	
	
	/**
	 * Get parent account details details for manage
	 * @param username
	 * @return ParentTO
	 */
	//Added by Ravi for Manage Profile
	public ParentTO manageParentAccountDetails(String username) {

		ParentTO parentTO = new ParentTO();
		List<Map<String, Object>> lstData = null;
		try {
		//populate parent account details
		lstData = getJdbcTemplatePrism().queryForList(
				IQueryConstants.GET_PARENT_DETAILS_BY_USERNAME, username);
		
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				parentTO.setUserId(((BigDecimal) fieldDetails.get("USERID")).longValue());
				parentTO.setUserName((String) (fieldDetails.get("USERNAME")));
				parentTO.setDisplayName((String) (fieldDetails.get("DISPLAY_USERNAME")));
				parentTO.setLastName((String) (fieldDetails.get("LAST_NAME")));
				parentTO.setFirstName((String) (fieldDetails.get("FIRST_NAME")));
				//parentTO.setMiddleName((String) (fieldDetails.get("MIDDLE_NAME")));
				parentTO.setMail((String) (fieldDetails.get("EMAIL_ADDRESS")));
				parentTO.setMobile((String) (fieldDetails.get("PHONE_NO")));
				parentTO.setCountry((String) (fieldDetails.get("COUNTRY")));
				parentTO.setZipCode((String) (fieldDetails.get("ZIPCODE")));
				parentTO.setCity((String) (fieldDetails.get("CITY")));
				parentTO.setState((String) (fieldDetails.get("STATE")));
				parentTO.setStreet((String) (fieldDetails.get("STREET")));
			}
		}
		//setting secret question list by calling method getParentSecretQuestionDetails()
		parentTO.setQuestionToList(getParentSecretQuestionDetails(username));
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while retrieving parent details for manage.", e);
		}
		return parentTO;
	}
	
	/**
	 * Get parent secret questions details for manage
	 * @param username
	 * @return QuestionTOs
	 */
	//Added by Ravi for Manage Profile
	public ArrayList<QuestionTO> getParentSecretQuestionDetails(String username) {
		
		List<Map<String, Object>> lstData = null;
		ArrayList<QuestionTO> questionTOs = new ArrayList<QuestionTO>();
		
		// populate parent security question and answer detail
		lstData = getJdbcTemplatePrism().queryForList(
				IQueryConstants.GET_PARENT_SECURITY_QUESTION, username);
		
		if (lstData.size() > 0) {

			for (Map<String, Object> fieldDetails : lstData) {
				QuestionTO questionTo = new QuestionTO();
				questionTo.setSno(((BigDecimal) fieldDetails.get("SNO")).longValue());
				questionTo.setQuestionId(((BigDecimal) fieldDetails.get("QUESTION_ID")).longValue());
				questionTo.setQuestion((String)fieldDetails.get("QUESTION"));
				questionTo.setAnswerId(((BigDecimal) fieldDetails.get("ANSWER_ID")).longValue());
				questionTo.setAnswer((String)fieldDetails.get("ANSWER"));
				
				questionTOs.add(questionTo);
			}
		}
		return questionTOs;
	}
	
	
	
	/**
	 * Get parent secret questions details for Forget password
	 * @param username
	 * @return QuestionTOs
	 */
	
	public ArrayList<QuestionTO> getSecurityQuestionForUser(String username) {
		
		List<Map<String, Object>> lstData = null;
		ArrayList<QuestionTO> questionTOs = new ArrayList<QuestionTO>();
		
		// populate parent security question and answer detail
		lstData = getJdbcTemplatePrism().queryForList(
				IQueryConstants.GET_PARENT_SECURITY_QUESTION, username);
		
		if (lstData.size() > 0) {

			for (Map<String, Object> fieldDetails : lstData) {
				QuestionTO questionTo = new QuestionTO();
				questionTo.setSno(((BigDecimal) fieldDetails.get("SNO")).longValue());
				questionTo.setQuestionId(((BigDecimal) fieldDetails.get("QUESTION_ID")).longValue());
				questionTo.setQuestion((String)fieldDetails.get("QUESTION"));
				//questionTo.setAnswerId(((BigDecimal) fieldDetails.get("ANSWER_ID")).longValue());
				//questionTo.setAnswer((String)fieldDetails.get("ANSWER"));
				
				questionTOs.add(questionTo);
			}
		}
		return questionTOs;
	}
	
	/**
	 * This function validate the user secret answers
	 * @param username,ans1, ans2,ans3,questionId1,questionId2,questionId3
	 * @return boolean flag
	 */
	public boolean validateAnswers(String userName,String ans1, String ans2,String ans3,String questionId1,String questionId2,String questionId3){
		long validUser=0;
		try {
			validUser= getJdbcTemplatePrism().queryForLong(IQueryConstants.VALIDATE_SECURITY_ANSWERS, userName,questionId1,ans1,userName,questionId2,ans2,questionId3,ans3);
		if (validUser != 0 && validUser != -1 && validUser==1){
			return true;
		}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while validating answers.", e);
			return false;
		}
		return false;
	}
	
	/**
	 * This function retrieves the user names for a particular email id
	 * @param emailId
	 * @return UsersList
	 */
	
	public List<UserTO> getUserNamesByEmail(String emailId)
	{	
		ArrayList<UserTO> UserTOs = new ArrayList<UserTO>();
		List<Map<String, Object>> userslist = null;
		userslist = getJdbcTemplatePrism().queryForList(
				IQueryConstants.GET_ALL_USERS_BY_EMAIL, emailId);
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
	
	
	/**
	 * Update user profile information on save
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	//Added by Ravi for Manage Profile
	@TriggersRemove(cacheName="orgUsers", removeAll=true)
	public boolean updateUserProfile(ParentTO parentTO) throws BusinessException {

		long user_id = parentTO.getUserId();
		String password = parentTO.getPassword();
		try{
		boolean ldapFlag = true;
		// calling ldapManager for updating password for a username in LDAP
		if (password != null && !"".equals(password)) {
			if(IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
				ldapFlag = 	ldapManager.updateUser(parentTO.getUserName(), parentTO.getUserName(), parentTO.getUserName(), password);
			} else {
				String salt = PasswordGenerator.getNextSalt();
				getJdbcTemplatePrism().update(IQueryConstants.UPDATE_PASSWORD_DATA,
						IApplicationConstants.FLAG_N,
						SaltedPasswordEncoder.encryptPassword(password, Utils.getSaltWithUser(parentTO.getUserName(), salt)), 
						salt, parentTO.getUserName());
				ldapFlag = true;
			}
		}
		if(ldapFlag){
			// updating user details in users table
			int count = getJdbcTemplatePrism().update(
					IQueryConstants.UPDATE_USER_DATA, parentTO.getLastName(), parentTO.getFirstName(), parentTO.getMail(), 
					parentTO.getMobile(), parentTO.getCountry(), parentTO.getZipCode(), parentTO.getState(), 
					parentTO.getStreet(),parentTO.getCity(), parentTO.getDisplayName(), user_id);
			
			if (count > 0) {
				// delete security answers for that user and then insert as fresh
				//boolean isDeleted= deletePasswordHistAnswer(user_id);
				//if(isDeleted){
					savePasswordHistAnswer(user_id, parentTO.getQuestionToList());	
				//}
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
	 * Delete Password hint answers for a userid
	 * 
	 * @return boolean
	 */
	//Added by Ravi for Manage Profile
	public boolean deletePasswordHistAnswer(long userid) {
		getJdbcTemplatePrism().update(IQueryConstants.DELETE_ANSWER_DATA, userid);
		return Boolean.TRUE;
	}
	
	/**
	 * check invitation code claim to existing parent account
	 * 
	 * @return boolean
	 */
	public boolean checkInvitationCodeClaim(String userName, String invitationCode) {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(
				IQueryConstants.CHECK_INVITATION_CODE_CLAIM, userName,
				invitationCode);
		if (lstData == null || lstData.isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	/**
	 * Add invitation code to existing parent account
	 * 
	 * @return boolean
	 */
	public boolean addInvitationToAccount(String userName, String invitationCode) {
		int count = getJdbcTemplatePrism().update(
				IQueryConstants.ADD_INVITATION_CODE_TO_ACCOUNT, userName,
				invitationCode);
		if (count > 0) {
			boolean isUpdatedInvitationCodeClaimCount=updateInvitationCodeClaimCount(invitationCode);
			if (isUpdatedInvitationCodeClaimCount) {
				return updateOrgIdForParent(userName, invitationCode);
			}
		}
		return Boolean.FALSE;
	}
	
	/**
	 * Update Invitation code claim count
	 * 
	 * @return boolean
	 */
	public boolean updateOrgIdForParent(String userName, String invitationCode) {
	
		int count = getJdbcTemplatePrism().update(
				IQueryConstants.UPDATE_PARENT_USER_ORG, invitationCode, userName);
		
		logger.log(IAppLogger.DEBUG, "UPDATE ORG ID FOR PARENT Done");
		if (count > 0) {
			return Boolean.TRUE;
		}
	
		
		return Boolean.FALSE;
	}
	
	/**
	 * Retrieves and returns school id (leve3_jasper_orgid) for given student bio id
	 * @param studentBioId
	 * @return leve3_jasper_orgid
	 */
	public String getSchoolOrgId( String studentBioId ) {
		return getJdbcTemplatePrism().queryForObject(IQueryConstants.FETCH_SCHOOLID_FOR_STUDENT, new Object[]{ studentBioId }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int col) throws SQLException {
				return ((BigDecimal) rs.getObject(1)).toString();
			}
		});
	}
	
	/**
	 * Insert new Invitation code
	 * 
	 * @return boolean
	 */
	public boolean generateActivationCode(StudentTO student) {
		int count = getJdbcTemplatePrism().update(
				IQueryConstants.ADD_NEW_INVITATION_CODE, student.getInvitationcode(), student.getStudentBioId());
		if (count > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * disable Invitation code
	 * 
	 * @return boolean
	 */
	public boolean disableActivationCode(StudentTO student) {
		int count = getJdbcTemplatePrism().update(
				IQueryConstants.UPDATE_ACTIVATION_CODE, student.getInvitationcode(), student.getStudentBioId());
		if (count > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public boolean checkTASCProduct(long customerId) {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(
				IQueryConstants.GET_PRODUCT_NAME, customerId);
		if (lstData == null || lstData.isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	//Manage Content - Parent Network - Start  - By Joy
	
	/**
	 * @author Joy
	 * To populate grade
	 */
	@SuppressWarnings("unchecked")
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateGrade(final Map<String,Object> paramMap) 
			throws BusinessException{
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - populateGrade()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		long t1 = System.currentTimeMillis();
		final long custProdId = ((Long) paramMap.get("custProdId")).longValue();
		try{
			objectValueTOList = (List<com.ctb.prism.core.transferobject.ObjectValueTO>) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				            CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_GRADE + "}");
				            cs.setLong(1, custProdId);				            
				            cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR); 
				            cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
				            return cs;
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rs = null;
			        			List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOResult 
			        							= new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
			        			try {
									cs.execute();
									rs = (ResultSet) cs.getObject(2);
									com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
									
									while(rs.next()){
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
		}catch(Exception e){
			throw new BusinessException(e.getMessage());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - populateGrade() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return objectValueTOList;
	}
	
	/**
	 * @author Joy
	 * To populate subtest
	 */
	@SuppressWarnings("unchecked")
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateSubtest(final Map<String,Object> paramMap) 
			throws BusinessException{
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - populateSubtest()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		long t1 = System.currentTimeMillis();
		final long custProdId = ((Long) paramMap.get("custProdId")).longValue();
		final long gradeId = ((Long) paramMap.get("gradeId")).longValue();
		try{
			objectValueTOList = (List<com.ctb.prism.core.transferobject.ObjectValueTO>) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				            CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_SUBTEST + "}");
				            cs.setLong(1, custProdId);	
				            cs.setLong(2, gradeId);				            
				            cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR); 
				            cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
				            return cs;
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rs = null;
			        			List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOResult 
			        							= new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
			        			try {
									cs.execute();
									rs = (ResultSet) cs.getObject(3);
									com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
									
									while(rs.next()){
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
		}catch(Exception e){
			throw new BusinessException(e.getMessage());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - populateSubtest() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return objectValueTOList;
	}
	
	/**
	 * @author Joy
	 * To populate Objective
	 */
	@SuppressWarnings("unchecked")
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateObjective(final Map<String,Object> paramMap) 
			throws BusinessException{
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - populateObjective()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		long t1 = System.currentTimeMillis();
		final long subtestId = ((Long) paramMap.get("subtestId")).longValue();
		final long gradeId = ((Long) paramMap.get("gradeId")).longValue();
		try{
			objectValueTOList = (List<com.ctb.prism.core.transferobject.ObjectValueTO>) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				            CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_OBJECTIVE + "}");
				            cs.setLong(1, subtestId);		
				            cs.setLong(2, gradeId);	
				            cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR); 
				            cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
				            return cs;
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rs = null;
			        			List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOResult 
			        							= new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
			        			try {
									cs.execute();
									rs = (ResultSet) cs.getObject(3);
									com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
									
									while(rs.next()){
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
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - populateObjective() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return objectValueTOList;
	}
	
	/**
	 * @author Joy
	 * Insert content/article along with metadata - By Joy
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO addNewContent(final Map<String,Object> paramMap) 
			throws BusinessException{
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - addNewContent()");
		com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
		long t1 = System.currentTimeMillis();
		final ManageContentTO manageContentTO =  (ManageContentTO) paramMap.get("manageContentTO");

		try{
			objectValueTO = (com.ctb.prism.core.transferobject.ObjectValueTO) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
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
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			long executionStatus = 0;
			        			com.ctb.prism.core.transferobject.ObjectValueTO statusTO = 
										new com.ctb.prism.core.transferobject.ObjectValueTO();
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
		}catch(Exception e){
			throw new BusinessException(e.getMessage());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - addNewContent() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return objectValueTO;
	}
	
	/**
	 * @author Joy
	 * returns content list respective of the search field selected - By Joy
	 */
	@SuppressWarnings("unchecked")
	public List<ManageContentTO> loadManageContent(final Map<String,Object> paramMap) throws BusinessException{
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - loadManageContent()");
		long t1 = System.currentTimeMillis();
		List<ManageContentTO> manageContentList = null;
		ManageContentTO manageContentTO = new ManageContentTO();
		final long custProdId = Long.parseLong((String) paramMap.get("custProdId"));
		final long subtestId = Long.parseLong((String) paramMap.get("subtestId"));
		final long objectiveId = Long.parseLong((String) paramMap.get("objectiveId"));
		final String contentTypeId = (String) paramMap.get("contentTypeId");
		final String checkFirstLoad = (String) paramMap.get("checkFirstLoad");
		
		try{
			manageContentList = (List<ManageContentTO>) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				        	if(checkFirstLoad.equals("true")){
				        		CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_MANAGE_CONTENT_LIST + "}");
					            cs.setLong(1, custProdId);
					            cs.setLong(2, subtestId);
					            cs.setLong(3, objectiveId);
					            cs.setString(4, contentTypeId);
					            cs.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR); 
					            cs.registerOutParameter(6, oracle.jdbc.OracleTypes.VARCHAR);
					            return cs;
				        	}else{
			        			final long lastid = Long.parseLong((String) paramMap.get("lastid"));
				        		CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_MANAGE_CONTENT_LIST_MORE + "}");
					            cs.setLong(1, custProdId);
					            cs.setLong(2, subtestId);
					            cs.setLong(3, objectiveId);
					            cs.setLong(4,lastid);
					            cs.setString(5, contentTypeId);
					            cs.registerOutParameter(6, oracle.jdbc.OracleTypes.CURSOR); 
					            cs.registerOutParameter(7, oracle.jdbc.OracleTypes.VARCHAR);
					            return cs;	
				        	}
				        	
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rs = null;
			        			List<ManageContentTO> manageContentListResult = new ArrayList<ManageContentTO>();
			        			try {
									cs.execute();
									if(checkFirstLoad.equals("true")) {
										rs = (ResultSet) cs.getObject(5);
									} else {
										rs = (ResultSet) cs.getObject(6);
									}
									com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
									
									while(rs.next()){
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
		}catch(Exception e){
			throw new BusinessException(e.getMessage());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - loadManageContent() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return manageContentList;
	}
	
	/**
	 * @author Joy
	 * Get content details for edit depending upon article_metedata id  - By Joy
	 */
	@SuppressWarnings("unchecked")
	public ManageContentTO getContentForEdit(final Map<String,Object> paramMap) throws BusinessException{
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getContentForEdit()");
		long t1 = System.currentTimeMillis();
		ManageContentTO manageContentTO = null;
		final long contentId = ((Long) paramMap.get("contentId")).longValue();
		
		try{
			manageContentTO = (ManageContentTO) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
			        		CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_MANAGE_CONTENT_FOR_EDIT + "}");
				            cs.setLong(1, contentId);
				            cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR); 
				            cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
				            return cs;
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rs = null;
			        			ManageContentTO manageContentTOResult = null;
			        			try {
									cs.execute();
									rs = (ResultSet) cs.getObject(2);
									if(rs.next()){
										manageContentTOResult = new ManageContentTO();
										manageContentTOResult.setContentId(rs.getLong("METADATA_ID"));
										manageContentTOResult.setContentName(rs.getString("NAME"));	
										manageContentTOResult.setSubHeader(rs.getString("SUB_HEADER"));
										manageContentTOResult.setContentDescription(Utils.convertClobToString((Clob)rs.getClob("CONTENT_DESCRIPTION")));
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
		}catch(Exception e){
			throw new BusinessException(e.getMessage());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getContentForEdit() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return manageContentTO;
	}
	
	/**
	 * @author Joy
	 * Update content/article along with metadata  - By Joy
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO updateContent(final Map<String,Object> paramMap) 
			throws BusinessException{
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - updateContent()");
		com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
		long t1 = System.currentTimeMillis();
		final ManageContentTO manageContentTO =  (ManageContentTO) paramMap.get("manageContentTO");

		try{
			objectValueTO = (com.ctb.prism.core.transferobject.ObjectValueTO) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
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
				    } ,  new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			long executionStatus = 0;
			        			com.ctb.prism.core.transferobject.ObjectValueTO statusTO = 
										new com.ctb.prism.core.transferobject.ObjectValueTO();
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
		}catch(Exception e){
			throw new BusinessException(e.getMessage());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - updateContent() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return objectValueTO;
	}
	
	/**
	 * @author Joy
	 * Delete content/article's meta data and delete content/article if no association present with another mete data.  - By Joy
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO deleteContent(final Map<String,Object> paramMap) 
			throws BusinessException{
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - deleteContent()");
		com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
		long t1 = System.currentTimeMillis();
		final long contentId = ((Long) paramMap.get("contentId")).longValue();

		try{
			objectValueTO = (com.ctb.prism.core.transferobject.ObjectValueTO) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				            CallableStatement cs = con.prepareCall("{call " + IQueryConstants.DELETE_CONTENT + "}");
				            cs.setLong(1, contentId);	
				            cs.registerOutParameter(2, oracle.jdbc.OracleTypes.NUMBER); 
				            cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
				            return cs;
				        }
				    } ,  new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			long executionStatus = 0;
			        			com.ctb.prism.core.transferobject.ObjectValueTO statusTO = 
										new com.ctb.prism.core.transferobject.ObjectValueTO();
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
		}catch(Exception e){
			throw new BusinessException(e.getMessage());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - deleteContent() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return objectValueTO;
	}
	
	/**
	 * @author Joy
	 * Get content details for edit depending upon article_metedata id  - By Joy
	 */
	@SuppressWarnings("unchecked")
	public ManageContentTO modifyStandardForEdit(final Map<String,Object> paramMap) throws BusinessException{
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - modifyStandardForEdit()");
		long t1 = System.currentTimeMillis();
		ManageContentTO manageContentTO = null;
		final long objectiveId = ((Long) paramMap.get("objectiveId")).longValue();
		
		try{
			manageContentTO = (ManageContentTO) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
			        		CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_OBJECTIVE_DETAILS_FOR_EDIT + "}");
				            cs.setLong(1, objectiveId);
				            cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR); 
				            return cs;
				        }
				    } , new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rs = null;
			        			ManageContentTO manageContentTOResult = null;
			        			try {
									cs.execute();
									rs = (ResultSet) cs.getObject(2);
									if(rs.next()){
										manageContentTOResult = new ManageContentTO();
										manageContentTOResult.setObjectiveId(rs.getLong("OBJECTIVEID"));
										manageContentTOResult.setContentDescription(Utils.convertClobToString((Clob)rs.getClob("CONTENT_DESCRIPTION")));
									}
			        			} catch (SQLException e) {
			        				e.printStackTrace();
			        			} catch (Exception e) {
			        				e.printStackTrace();
			        			}
			        			return manageContentTOResult;
					        }
					    });
		}catch(Exception e){
			throw new BusinessException(e.getMessage());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - modifyStandardForEdit() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return manageContentTO;
	}
	
	/**
	 * @author Joy
	 * Get Description of Resource, Everyday Activity and About the Test  - By Joy
	 */
	@SuppressWarnings("unchecked")
	public ManageContentTO modifyGenericForEdit(final Map<String,Object> paramMap) throws BusinessException{
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - modifyGenericForEdit()");
		long t1 = System.currentTimeMillis();
		ManageContentTO manageContentTO = null;
		final long gradeId = ((Long) paramMap.get("gradeId")).longValue();
		final long subtestId = ((Long) paramMap.get("subtestId")).longValue();
		final String type = (String) paramMap.get("type");
		
		try{
			manageContentTO = (ManageContentTO) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
			        		CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_GENERIC_DETAILS_FOR_EDIT + "}");
				            cs.setLong(1, gradeId);
				            cs.setLong(2, subtestId);
				            cs.setString(3, type);
				            cs.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR); 
				            return cs;
				        }
				    } , new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rs = null;
			        			ManageContentTO manageContentTOResult = null;
			        			try {
									cs.execute();
									rs = (ResultSet) cs.getObject(4);
									if(rs.next()){
										manageContentTOResult = new ManageContentTO();
										manageContentTOResult.setContentId(rs.getLong("METADATA_ID"));
										manageContentTOResult.setContentDescription(Utils.convertClobToString((Clob)rs.getClob("CONTENT_DESCRIPTION")));
									}
			        			} catch (SQLException e) {
			        				e.printStackTrace();
			        			} catch (Exception e) {
			        				e.printStackTrace();
			        			}
			        			return manageContentTOResult;
					        }
					    });
		}catch(Exception e){
			throw new BusinessException(e.getMessage());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - modifyGenericForEdit() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return manageContentTO;
	}
	
	//Manage Content - Parent Network - End
	
	
	//Parent Network - Start
	
	/*
	 * @author Joy
	 * Get Student's sub test - By Joy
	 */
	@SuppressWarnings("unchecked")
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getStudentSubtest(final Map<String,Object> paramMap) throws BusinessException{
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getStudentSubtest()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		long t1 = System.currentTimeMillis();
		final long studentBioId = Long.parseLong((String) paramMap.get("studentBioId")); 
		
		try{
			objectValueTOList = (List<com.ctb.prism.core.transferobject.ObjectValueTO>) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				            CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_STUDENT_SUBTEST + "}");
				            cs.setLong(1, studentBioId);		
				            cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR); 
				            cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
				            return cs;
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rs = null;
			        			List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOResult 
			        							= new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
			        			try {
									cs.execute();
									rs = (ResultSet) cs.getObject(2);
									com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
									
									while(rs.next()){
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
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getStudentSubtest() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return objectValueTOList;
	}
	
	/*
	 * @author Joy
	 * Get Standard and associated activity/indicator.
	 * It may or may not depends upon Student   - By Joy
	 */
	@SuppressWarnings("unchecked")
	public List<ManageContentTO> getArticleTypeDetails(final Map<String,Object> paramMap) throws BusinessException{
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getArticleTypeDetails()");
		List<ManageContentTO> articleTypeDetailsList = null;
		long t1 = System.currentTimeMillis();
		final long studentBioId = ((Long) paramMap.get("studentBioId")).longValue(); 
		final long subtestId = ((Long) paramMap.get("subtestId")); 
		final long studentGradeId = ((Long)paramMap.get("studentGradeId")); 
		final String contentType = (String) paramMap.get("contentType");
		
		try{
			articleTypeDetailsList = (List<ManageContentTO>) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
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
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rs = null;
			        			List<ManageContentTO> articleTypeDetailsResult 
			        							= new ArrayList<ManageContentTO>();
			        			try {
									cs.execute();
									rs = (ResultSet) cs.getObject(5);
									ManageContentTO articleTypeDetailsTO = null;
									
									while(rs.next()){
										articleTypeDetailsTO = new ManageContentTO();
										articleTypeDetailsTO.setObjectiveId(rs.getLong("STANDARD_ID"));
										articleTypeDetailsTO.setObjectiveName(rs.getString("STANDARD_NAME"));
										articleTypeDetailsTO.setContentId(rs.getLong("ARTICLEID"));
										articleTypeDetailsTO.setContentName(rs.getString("ARTICLE_NAME"));
										articleTypeDetailsTO.setSubHeader(rs.getString("ARTICLE_SUB_HEADER"));
										articleTypeDetailsTO.setProficiencyLevel(rs.getString("PROFICENCY_LEVEL"));
										articleTypeDetailsResult.add(articleTypeDetailsTO);
									}
			        			} catch (SQLException e) {
			        				e.printStackTrace();
			        			}
			        			return articleTypeDetailsResult;
				        }
				    });
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getArticleTypeDetails() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return articleTypeDetailsList;
	}
	
	/**
	 * @author Joy
	 * Get Article description depending upon article id and type/category  - By Joy
	 */
	@SuppressWarnings("unchecked")
	public ManageContentTO getArticleDescription(final Map<String,Object> paramMap) throws BusinessException{
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getArticleDescription()");
		long t1 = System.currentTimeMillis();
		ManageContentTO manageContentTO = null;
		final long articleId = ((Long) paramMap.get("articleId")).longValue(); 
		final String contentType = (String) paramMap.get("contentType");
		
		try{
			manageContentTO = (ManageContentTO) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				        	CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_ARTICLE_DESCRIPTION + "}");
				            cs.setLong(1, articleId);
				            cs.setString(2, contentType);
				            cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR); 
				            cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
				            return cs;
				        }
				    } , new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rs = null;
			        			ManageContentTO manageContentTOResult = null;
			        			try {
			        				
			        				cs.execute();
									rs = (ResultSet) cs.getObject(3);
									if(rs.next()){
										manageContentTOResult = new ManageContentTO();
										manageContentTOResult.setContentId(rs.getLong("ARTICLEID"));
										manageContentTOResult.setContentName(rs.getString("ARTICLE_NAME"));
										manageContentTOResult.setContentDescription(Utils.convertClobToString((Clob)rs.getClob("CONTENT_DESCRIPTION")));
									}
			        			} catch (SQLException e) {
			        				e.printStackTrace();
			        			} catch (Exception e) {
			        				e.printStackTrace();
			        			}
			        			return manageContentTOResult;
					        }
					    });
		}catch(Exception e){
			throw new BusinessException(e.getMessage());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getArticleDescription() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return manageContentTO;
	}
	
	
	/**
	 * @author Joy
	 * Get grade and associated subtest depends upon logged in customerId for current admin year
	 */
	@SuppressWarnings("unchecked")
	public List<ManageContentTO> getGradeSubtestInfo(final Map<String,Object> paramMap) throws BusinessException{
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getGradeSubtestInfo()");
		List<ManageContentTO> gradeSubtestList = null;
		long t1 = System.currentTimeMillis();
		final UserTO loggedinUserTO = (UserTO) paramMap.get("loggedinUserTO");
		
		try{
			gradeSubtestList = (List<ManageContentTO>) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				            CallableStatement cs = null;
				            cs = con.prepareCall("{call " + IQueryConstants.GET_GRADE_SUBTEST_INFO + "}");
				            cs.setLong(1, Long.valueOf(loggedinUserTO.getCustomerId()));
				            cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR); 
				            cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
				            return cs;
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rs = null;
			        			List<ManageContentTO> gradeSubtestResult 
			        							= new ArrayList<ManageContentTO>();
			        			try {
									cs.execute();
									rs = (ResultSet) cs.getObject(2);
									ManageContentTO gradeSubtestTO = null;
									
									while(rs.next()){
										gradeSubtestTO = new ManageContentTO();
										gradeSubtestTO.setGradeId(rs.getLong("GRADE_ID"));
										gradeSubtestTO.setGradeName(rs.getString("GRADE_NAME"));
										gradeSubtestTO.setSubtestId(rs.getLong("SUBTEST_ID"));
										gradeSubtestTO.setSubtestName(rs.getString("SUBTEST_NAME"));
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
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getGradeSubtestInfo() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return gradeSubtestList;
	}
	
	//Parent Network - End
}
