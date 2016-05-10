package com.ctb.prism.parent.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
import com.ctb.prism.login.dao.ILoginDAO;
import com.ctb.prism.login.transferobject.Address;
import com.ctb.prism.login.transferobject.HintAnswers;
import com.ctb.prism.login.transferobject.HintQuestions;
import com.ctb.prism.login.transferobject.MCustProdAdminTO;
import com.ctb.prism.login.transferobject.MUserTO;
import com.ctb.prism.login.transferobject.PwdHistory;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.transferobject.ManageContentTO;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.QuestionTO;
import com.ctb.prism.parent.transferobject.StudentTO;

@Repository("parentDAO")
public class MParentDAOImpl extends BaseDAO implements IParentDAO {

	@Autowired
	private LdapManager ldapManager;
	@Autowired
	private IPropertyLookup propertyLookup;
	@Autowired
	private ILoginDAO loginDAO; 

	private static final IAppLogger logger = LogFactory.getLoggerInstance(ParentDAOImpl.class.getName());

	/**Moved to store proc - By Joy
	 *  Add paramMap and caching mechanism changed - By Joy
	 * (non-Javadoc)
	 * @see com.ctb.prism.parent.dao.IParentDAO#getSecretQuestions(Map<String,Object> paramMap)
	 */
	@SuppressWarnings("unchecked")
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getSecretQuestions') )"),
			@Cacheable(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getSecretQuestions') )"),
			@Cacheable(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getSecretQuestions') )")
	} )
	public List<QuestionTO> getSecretQuestions(final Map<String,Object> paramMap) {
		
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getSecretQuestions()");
		long t1 = System.currentTimeMillis();
		
		String contractName = (String) paramMap.get("contractName"); 
		if(contractName == null) {
			contractName = Utils.getContractName();
		}
		logger.log(IAppLogger.INFO, "Contract Name: "+contractName);
		List<QuestionTO> questionList = new ArrayList<QuestionTO>();
		
		Query searchQuestionQuery = new Query(Criteria.where("_id").is(Utils.getProject().toUpperCase()));
		searchQuestionQuery.fields().include("hintQuestions");
		
		List<MCustProdAdminTO> custProdAdminTO = getMongoTemplatePrism("global").find(searchQuestionQuery, MCustProdAdminTO.class);
		
		if(custProdAdminTO != null){
			HintQuestions[] pwdHintQuestionList =custProdAdminTO.get(0).getHintQuestions();
			if(pwdHintQuestionList != null && pwdHintQuestionList.length > 0) {
				QuestionTO questionTO = null;
				for (HintQuestions question : pwdHintQuestionList) {
					questionTO = new QuestionTO();
					questionTO.setQuestionId(Long.valueOf(question.getQid()));
					questionTO.setQuestion(question.getQues());
					questionTO.setSno(Long.valueOf(question.getSeq()));
					questionList.add(questionTO);
				}	
			}			
		}
			
		long t2 = System.currentTimeMillis();
		logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getSecretQuestions() took time: " + String.valueOf(t2 - t1) + "ms");
		return questionList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#checkUserAvailability(java.lang.String)
	 */
	public boolean checkUserAvailability(final Map<String,Object> paramMap) {
		String username = (String)paramMap.get("username");
		String contractName = (String) paramMap.get("contractName");
		String project = (String) paramMap.get("project");
		if(contractName == null) {
			contractName = Utils.getContractName();
		}
		logger.log(IAppLogger.INFO, "Contract Name: "+contractName);
		
		Query searchUserQuery = new Query(Criteria.where("_id").is(username).and("project_id").is(project));
		long searchUserCount = getMongoTemplatePrism(contractName).count(searchUserQuery, MUserTO.class);

		if(searchUserCount > 0) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#checkActiveUserAvailability(java.lang.String)
	 */
	public boolean checkActiveUserAvailability(Map<String, Object> paramMap) {
		String contractName = (String) paramMap.get("contractName"); 
		String username = (String) paramMap.get("username"); 
		
		Query searchUserQuery = new Query(Criteria.where("_id").is(username).and("status").in("AC","SS"));
		long searchUserCount = getMongoTemplatePrism(contractName).count(searchUserQuery, MUserTO.class);
		
		
		if (searchUserCount == 0) {
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

	
	/**
	 * Moved to package and reduce DB call - By Joy 
	 * (non-Javadoc)
	 * Fix for TD 78161 - By Joy
	 * @see com.ctb.prism.parent.dao.IParentDAO#validateIC(java.lang.String)
	 */
	public ParentTO validateIC(final Map<String, Object> paramMap) {

		logger.log(IAppLogger.INFO, "Enter: validateIC()");
		long t1 = System.currentTimeMillis();
		final String invitationCode = (String)paramMap.get("invitationCode");
		String contractName = (String) paramMap.get("contractName");
		if(contractName == null) {
			contractName = Utils.getContractName();
		}
		logger.log(IAppLogger.INFO, "Contract Name: "+contractName);

		ParentTO parentTO = null;
		try{
			parentTO = (ParentTO) getJdbcTemplatePrism(contractName).execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				        	CallableStatement cs = con.prepareCall("{call " + IQueryConstants.VALIDATE_INVITATION_CODE + "}");
				            if(paramMap.get("loggedinUserTO") != null){
				    			UserTO loggedinUserTO = (UserTO) paramMap.get("loggedinUserTO");
				    			cs.setString(1, loggedinUserTO.getUserName());
				    		}else{
				    			cs.setString(1, "");
				    		}
				            cs.setString(2, invitationCode);
				            cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR); 
				            cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
				            return cs;				      			            
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rsIc = null;
			        			ParentTO parentTOResult = null;
			        			try {
									cs.execute();
									rsIc = (ResultSet) cs.getObject(3);
									if(rsIc.next()){
										parentTOResult = new ParentTO();
										parentTOResult.setTotalAttemptedCalim(rsIc.getLong("TOTAL_ATTEMPT"));
										parentTOResult.setTotalAvailableCalim(rsIc.getLong("TOTAL_AVAILABLE"));
										parentTOResult.setIcExpirationStatus(rsIc.getString("EXPIRATION_STATUS"));
										parentTOResult.setIcActivationStatus(rsIc.getString("ACTIVATION_STATUS"));
										parentTOResult.setIsAlreadyClaimed(rsIc.getLong("ALREADY_CLAIMED"));
									}
									
			        			} catch (SQLException e) {
			        				e.printStackTrace();
			        			}
			        			return parentTOResult;
				        }
				    });
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: validateIC() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return parentTO;
	}

	/**
	 * Moved to package - By Joy 
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getStudentForIC(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public ParentTO getStudentForIC(final Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: getStudentForIC()");
		long t1 = System.currentTimeMillis();
		String contractName = (String) paramMap.get("contractName");
		if(contractName == null) {
			contractName = Utils.getContractName();
		}
		logger.log(IAppLogger.INFO, "Contract Name: "+contractName);

		ParentTO parentTO = new ParentTO();
		List<StudentTO> studentToList = new ArrayList<StudentTO>();
		try{
			studentToList = (List<StudentTO>) getJdbcTemplatePrism(contractName).execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				        	CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_STUDENT_FOR_INVITATION_CODE + "}");
				            cs.setString(1, (String)paramMap.get("invitationCode"));
				            cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR); 
				            cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
				            return cs;				      			            
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rsStudent = null;
			        			List<StudentTO> studentToResult = new ArrayList<StudentTO>();
			        			try {
									cs.execute();
									rsStudent = (ResultSet) cs.getObject(2);
									StudentTO studentTO = null;
									while(rsStudent.next()){
										studentTO = new StudentTO();
										studentTO.setStudentName(rsStudent.getString("STUDENT_NAME"));
										studentTO.setGrade(rsStudent.getString("GRADE"));
										studentTO.setAdministration(rsStudent.getString("ADMINISTRATION"));
										studentTO.setSchoolName(rsStudent.getString("ORG_NODE_NAME"));
										studentToResult.add(studentTO);
									}
									
			        			} catch (SQLException e) {
			        				e.printStackTrace();
			        			}
			        			return studentToResult;
				        }
				    });
			parentTO.setStudentToList(studentToList);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: getStudentForIC() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return parentTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#registerUser(com.ctb.prism.parent.transferobject.ParentTO)
	 */
	@Caching( evict = { 
			@CacheEvict(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", allEntries = true),
			@CacheEvict(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  allEntries = true),
			@CacheEvict(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  allEntries = true)
	} )
	public boolean registerUser(final ParentTO parentTO) throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: registerUser()");
		long t1 = System.currentTimeMillis();
		final String userName = parentTO.getUserName();
		final String userDisplayName =  parentTO.getDisplayName();
		final String emailId =  parentTO.getMail();
		final String isFirstTimeLogin =  parentTO.isFirstTimeUser() ? IApplicationConstants.FLAG_Y : IApplicationConstants.FLAG_N;
		final String salt = PasswordGenerator.getNextSalt();
		final String password = SaltedPasswordEncoder.encryptPassword(parentTO.getPassword(), Utils.getSaltWithUser(parentTO.getUserName(), salt));
		final String invitaionCode = parentTO.getInvitationCode();
		final String mobileNo = parentTO.getMobile();
		final String country = parentTO.getCountry();
		final String zip = parentTO.getZipCode();
		final String street = parentTO.getStreet();
		final String city = parentTO.getCity();
		final String state = parentTO.getState();
		final String lastName = parentTO.getLastName();
		final String firstName = parentTO.getFirstName();
		
		String contractName = parentTO.getContractName();
		if ("".equals(contractName)) {
			contractName = Utils.getContractName();
		}
		logger.log(IAppLogger.INFO, "Contract Name: "+contractName);
		
		final String[] questionIdArr = new String[parentTO.getQuestionToList().size()];
		final String[] ansValArr = new String[parentTO.getQuestionToList().size()];
		int index = 0;
		for (QuestionTO questionTo : parentTO.getQuestionToList()) {
			questionIdArr[index] = String.valueOf(questionTo.getQuestionId());
			ansValArr[index] = String.valueOf(questionTo.getAnswer());
			index++;
		}
		
		String userId = "";
		try{
			
			userId = (String) getJdbcTemplatePrism(contractName).execute(new CallableStatementCreator() {
				int count =1;
				//String salt = PasswordGenerator.getNextSalt();
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " +IQueryConstants.CREATE_PARENT + "}");
					cs.setString(count++, userName);
					cs.setString(count++, userDisplayName);
					cs.setString(count++, emailId);
					cs.setString(count++, IApplicationConstants.ACTIVE_FLAG);
					cs.setString(count++, isFirstTimeLogin);
					cs.setString(count++, password);
					cs.setString(count++, salt);
					cs.setString(count++, IApplicationConstants.FLAG_N);
					cs.setString(count++, invitaionCode);
					cs.setString(count++, mobileNo);
					cs.setString(count++, country);
					cs.setString(count++, zip);
					cs.setString(count++, street);
					cs.setString(count++, city);
					cs.setString(count++, state);
					cs.setString(count++, lastName);
					cs.setString(count++, firstName);
					cs.setString(count++, Utils.arrayToSeparatedString(questionIdArr, '~'));
					cs.setString(count++, Utils.arrayToSeparatedString(ansValArr, '~'));
					cs.registerOutParameter(count++, oracle.jdbc.OracleTypes.NUMBER);
					cs.registerOutParameter(count++, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					String strUserId = null; 
					try {
						cs.execute();
						strUserId =  cs.getString(20);
						if( cs.getString(21) != null &&  cs.getString(21).trim().length() > 0) {
							logger.log(IAppLogger.DEBUG,"Parent Not added due to " + cs.getString(21));
							return null;
						} else if(strUserId!=null && strUserId.equals("0")){ 
							logger.log(IAppLogger.DEBUG, "User already exists");
					    } else {
							logger.log(IAppLogger.DEBUG, "INSERT_USER_DATA DONE");
							logger.log(IAppLogger.INFO, "User created with userid: " + strUserId);
						}
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return strUserId;
				}
			});
					
					
			if (userId != null && userId.trim().length() > 0 && !userId.equals("0")) {
				return Boolean.TRUE;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: registerUser() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return Boolean.FALSE;
	}

	/**
	 * @deprecated - By Joy
	 * Don't use this method to save Password Hint Answer use PKG_ADMIN_MODULE.SP_SAVE_PH_ANSWER() from create/edit proc or call separately.
	 * Need to remove this method after removing all the dependencies.
	 * Save Password hint answer.
	 * @param userid
	 * @param questionToList
	 * @return
	 */
	private boolean savePasswordHistAnswer(long userid, List<QuestionTO> questionToList) {
		int count = 0;
		long answerCount = getJdbcTemplatePrism().queryForObject(IQueryConstants.CHECK_FOR_EXISTING_ANSWER, Long.class, userid);
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
	public List<StudentTO> getChildrenList(final Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getChildrenList()");
		long t1 = System.currentTimeMillis();
		
		final String isPN = (String)paramMap.get("isPN");
		final String parentName = (String)paramMap.get("parentName");
		final String clickedTreeNode = (String)paramMap.get("clickedTreeNode");
		final String adminYear = (String)paramMap.get("adminYear");
		final String orgMode = (String)paramMap.get("orgMode");
		
		List<StudentTO> studentList = null;

		try {
			if(IApplicationConstants.FLAG_N.equals(isPN)){
				//Admin Module(Manage Parent -> View Children flow) flow
				studentList = (List<StudentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_STUDENT_DETAILS_ADMIN + "}");
						cs.setString(1, parentName);
						cs.setString(2, clickedTreeNode);
						cs.setString(3, orgMode);
						cs.setString(4, adminYear);
						cs.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(6, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						ResultSet rs = null;
						List<StudentTO> studentResult = new ArrayList<StudentTO>();
						try {
							cs.execute();
							rs = (ResultSet) cs.getObject(5);
							StudentTO studentTO = null;
							while (rs.next()) {
								studentTO = new StudentTO();
								studentTO.setStudentName(rs.getString("STUDENT_NAME"));
								studentTO.setTestElementId(rs.getString("TEST_ELEMENT_ID"));
								studentTO.setAdministration(rs.getString("ADMIN_SEASON_YEAR"));
								studentTO.setGrade(rs.getString("STUDENT_GRADE"));
								studentTO.setStudentGradeId(rs.getLong("STUDENT_GRADEID"));
								studentTO.setAdminid(rs.getString("ADMINID"));
								studentTO.setBioExists(rs.getLong("BIO_EXISTS"));
								studentTO.setStudentBioId(rs.getLong("STUDENT_BIO_ID"));
								studentTO.setClikedOrgId(Long.valueOf(clickedTreeNode));
								studentResult.add(studentTO);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return studentResult;
					}
				});
			}else{
				//Parent Network flow
				studentList = (List<StudentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_STUDENT_DETAILS + "}");
						cs.setString(1, parentName);
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
								studentTO.setTestElementId(rs.getString("TEST_ELEMENT_ID"));
								studentTO.setAdministration(rs.getString("ADMIN_SEASON_YEAR"));
								studentTO.setGrade(rs.getString("STUDENT_GRADE"));
								studentTO.setStudentGradeId(rs.getLong("STUDENT_GRADEID"));
								studentTO.setAdminid(rs.getString("ADMINID"));
								studentTO.setBioExists(rs.getLong("BIO_EXISTS"));
								studentTO.setStudentBioId(rs.getLong("STUDENT_BIO_ID"));
								studentTO.setClikedOrgId(Long.valueOf(clickedTreeNode));
								studentResult.add(studentTO);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return studentResult;
					}
				});
			}
			
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
	/* (non-Javadoc)
	 * @see com.ctb.prism.parent.dao.IParentDAO#getParentList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Caching( cacheable = {
			@Cacheable(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #root.method.name )"),
			@Cacheable(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #root.method.name )"),
			@Cacheable(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #root.method.name )")
	} )
	public ArrayList<ParentTO> getParentList(final String orgId, final String adminYear, final String searchParam, final String orgMode, final String moreCount) {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getStudentList()");
		long t1 = System.currentTimeMillis();
		
		ArrayList<ParentTO> parentTOs = new ArrayList<ParentTO>();
		try {
			parentTOs = (ArrayList<ParentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = null;
					cs = con.prepareCall(IQueryConstants.GET_PARENT_DETAILS_MANAGE_PARENT);
					if (orgId.indexOf("_") > 0) {
						String userName = orgId.substring((orgId.indexOf("_") + 1), orgId.length());
						String tenantId = orgId.substring(0, orgId.indexOf("_"));
						if (searchParam != null && searchParam.trim().length() > 0) {
							String searchParamNew = CustomStringUtil.appendString("%", searchParam, "%");
							logger.log(IAppLogger.INFO, "GET_PARENT_DETAILS_ON_SCROLL_WITH_SRCH_PARAM");
							logger.log(IAppLogger.INFO, "tenantId = " + tenantId);
							logger.log(IAppLogger.INFO, "userName = " + userName);
							logger.log(IAppLogger.INFO, "searchParam = " + searchParam);
							cs.setString(1, adminYear);
							cs.setString(2, orgMode);
							cs.setLong(3, Long.parseLong(tenantId));
							cs.setString(4, userName);
							cs.setString(5, searchParamNew);
							cs.setLong(6, IApplicationConstants.ROLE_PARENT_ID);
							cs.setLong(7, Long.parseLong(moreCount));
							cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);							
							cs.registerOutParameter(9, oracle.jdbc.OracleTypes.VARCHAR);
						} else {
							logger.log(IAppLogger.INFO, "GET_PARENT_DETAILS_ON_SCROLL");
							logger.log(IAppLogger.INFO, "tenantId = " + tenantId);
							logger.log(IAppLogger.INFO, "userName = " + userName);
							cs.setString(1, adminYear);
							cs.setString(2, orgMode);
							cs.setLong(3, Long.parseLong(tenantId));
							cs.setString(4, userName);
							cs.setString(5, "-99");
							cs.setLong(6, IApplicationConstants.ROLE_PARENT_ID);
							cs.setLong(7, Long.parseLong(moreCount));
							cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);							
							cs.registerOutParameter(9, oracle.jdbc.OracleTypes.VARCHAR);
							return cs;
						}
					} else {
						String tenantId = orgId;
						logger.log(IAppLogger.INFO, "GET_PARENT_DETAILS_ON_FIRST_LOAD");
						logger.log(IAppLogger.INFO, "tenantId = " + tenantId);
						cs.setString(1, adminYear);
						cs.setString(2, orgMode);
						cs.setLong(3, Long.parseLong(tenantId));
						cs.setString(4, "-99");
						cs.setString(5, "-99");
						cs.setLong(6, IApplicationConstants.ROLE_PARENT_ID);
						cs.setLong(7, Long.parseLong(moreCount));
						cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);							
						cs.registerOutParameter(9, oracle.jdbc.OracleTypes.VARCHAR);
					}
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ArrayList<ParentTO> parentTOResult = new ArrayList<ParentTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(8);
						ParentTO parentTO = null;
						while (rs.next()){
							parentTO = new ParentTO();
							parentTO.setUserId(rs.getLong("USERID"));
							parentTO.setUserName(rs.getString("USERNAME"));
							parentTO.setDisplayName(rs.getString("FULLNAME"));
							parentTO.setStatus(rs.getString("STATUS"));
							parentTO.setOrgId(rs.getLong("ORG_NODEID"));
							parentTO.setOrgName(rs.getString("ORG_NODE_NAME"));
							parentTO.setClikedOrgId(rs.getLong("TENANTID"));
							parentTO.setLastLoginAttempt(rs.getString("LAST_LOGIN_ATTEMPT"));
							parentTOResult.add(parentTO);
						}
						Utils.logError(cs.getString(9));
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return parentTOResult;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getParentList() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return parentTOs;
	}
	

	/*
	 * (non-Javadoc)
	 * Moved to PKG_MANAGE_PARENT by Joy
	 * @see com.ctb.prism.parent.dao.IParentDAO#searchParent(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ParentTO> searchParent(final String parentName, final String tenantId, final String adminYear, final String isExactSeacrh, final String orgMode) {

		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - searchParent()");
		long t1 = System.currentTimeMillis();

		ArrayList<ParentTO> parentTOs = new ArrayList<ParentTO>();
		final long rowNum = 15;
		try {
			parentTOs = (ArrayList<ParentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = null;
					cs = con.prepareCall("{call " + IQueryConstants.SEARCH_PARENT + "}");
					if (IApplicationConstants.FLAG_N.equalsIgnoreCase(isExactSeacrh)) {
						String searchParam = CustomStringUtil.appendString("%", parentName, "%");	
						logger.log(IAppLogger.INFO, "SEARCH_PARENT");
						logger.log(IAppLogger.INFO, "tenantId = " + tenantId);
						logger.log(IAppLogger.INFO, "parentName = " + parentName);
						cs.setString(1, adminYear);
						cs.setLong(2,IApplicationConstants.ROLE_PARENT_ID);
						cs.setString(3, orgMode);
						cs.setLong(4, Long.parseLong(tenantId));
						cs.setString(5, searchParam);
						cs.setLong(6,rowNum);
						cs.setString(7, isExactSeacrh);
						cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(9, oracle.jdbc.OracleTypes.VARCHAR);
					}else{
						logger.log(IAppLogger.INFO, "SEARCH_PARENT_EXACT");
						logger.log(IAppLogger.INFO, "tenantId = " + tenantId);
						logger.log(IAppLogger.INFO, "parentName = " + parentName);
						cs.setString(1, adminYear);
						cs.setLong(2,IApplicationConstants.ROLE_PARENT_ID);
						cs.setString(3, orgMode);
						cs.setLong(4, Long.parseLong(tenantId));
						cs.setString(5, parentName);
						cs.setLong(6,rowNum);
						cs.setString(7, isExactSeacrh);
						cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(9, oracle.jdbc.OracleTypes.VARCHAR);
					}
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ArrayList<ParentTO> parentTOResult = new ArrayList<ParentTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(8);
						ParentTO parentTO = null;
						while (rs.next()){
							parentTO = new ParentTO();
							parentTO.setUserId(rs.getLong("USERID"));
							parentTO.setUserName(rs.getString("USERNAME"));
							parentTO.setDisplayName(rs.getString("FULLNAME"));
							parentTO.setStatus(rs.getString("STATUS"));
							parentTO.setOrgId(rs.getLong("ORG_NODEID"));
							parentTO.setOrgName(rs.getString("ORG_NODE_NAME"));
							parentTO.setClikedOrgId(rs.getLong("TENANTID"));
							parentTO.setLastLoginAttempt(rs.getString("LAST_LOGIN_ATTEMPT"));
							parentTOResult.add(parentTO);
						}
						Utils.logError(cs.getString(9));
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return parentTOResult;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - searchParent() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return parentTOs;
	}

	/*
	 * (non-Javadoc)
	 * Moved to PKG_MANAGE_PARENT by Joy
	 * @see com.ctb.prism.parent.dao.IParentDAO#searchParentAutoComplete(java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public String searchParentAutoComplete(final String parentName,final String tenantId,final String adminYear,final String orgMode) {

		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - searchParentAutoComplete()");
		long t1 = System.currentTimeMillis();
		
		String parentListJsonString = null;
		final String searchParam = CustomStringUtil.appendString("%", parentName, "%");
		final long rowNum = 100;
		ArrayList<ParentTO> parentTOs = new ArrayList<ParentTO>();
		try {
			parentTOs = (ArrayList<ParentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = null;
					cs = con.prepareCall("{call " + IQueryConstants.SEARCH_PARENT + "}");
					cs.setString(1, adminYear);
					cs.setLong(2,IApplicationConstants.ROLE_PARENT_ID);
					cs.setString(3, orgMode);
					cs.setLong(4, Long.parseLong(tenantId));
					cs.setString(5, searchParam);
					cs.setLong(6,rowNum);
					cs.setString(7, IApplicationConstants.FLAG_N);
					cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(9, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ArrayList<ParentTO> parentTOResult = new ArrayList<ParentTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(8);
						ParentTO parentTO = null;
						while (rs.next()){
							parentTO = new ParentTO();
							parentTO.setUserName(rs.getString("USERNAME"));
							parentTO.setFullName(rs.getString("FULLNAME"));
							parentTOResult.add(parentTO);
						}
						Utils.logError(cs.getString(9));
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return parentTOResult;
				}
			});
			
			if(parentTOs.size() > 0){
				parentListJsonString = "[";
				for (ParentTO parentTO : parentTOs) {
					parentListJsonString = CustomStringUtil.appendString(parentListJsonString, "\"", parentTO.getUserName(), "<br/>", parentTO.getFullName(), "\",");
				}
				parentListJsonString = CustomStringUtil.appendString(parentListJsonString.substring(0, parentListJsonString.length() - 1), "]");
			}
			logger.log(IAppLogger.INFO, "parentListJsonString: "+parentListJsonString);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - searchParentAutoComplete() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return parentListJsonString;
	}

	/*
	 * (non-Javadoc)
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 * @see com.ctb.prism.parent.dao.IParentDAO#getStudentList(Map<String, Object> paramMap)
	 */
	@SuppressWarnings("unchecked")
	@Caching( cacheable = {
			@Cacheable(value="inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getStudentList') )"),
			@Cacheable(value="tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getStudentList') )"),
			@Cacheable(value="usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getStudentList') )")
	} )
	public ArrayList<StudentTO> getStudentList(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getStudentList()");
		long t1 = System.currentTimeMillis();
		
		final String orgId = (String) paramMap.get("scrollId");
		final String adminYear = (String) paramMap.get("adminYear");
		final String searchParam = (String) paramMap.get("searchParam");
		final long customerId = (Long) paramMap.get("currCustomer");
		final String orgMode = (String) paramMap.get("orgMode");
		final String moreCount = (String) paramMap.get("moreCount");

		ArrayList<StudentTO> studentTOs = null;
		try {
			studentTOs = (ArrayList<StudentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = null;
					if (orgId.lastIndexOf("|") > 0) {
						String tenantId = orgId.substring((orgId.lastIndexOf("|") + 1), orgId.length());
						String studentNameAndId = orgId.substring(0, orgId.lastIndexOf("|"));
						if (searchParam != null && searchParam.trim().length() > 0) {
							String searchParamNew = CustomStringUtil.appendString("%", searchParam, "%");
							cs = con.prepareCall(IQueryConstants.GET_STUDENT_DETAILS_MANAGE_STUDENT);
							cs.setString(1, adminYear);
							cs.setLong(2,customerId);
							cs.setString(3, orgMode);
							cs.setLong(4, Long.parseLong(tenantId));
							cs.setString(5, studentNameAndId);
							cs.setString(6, searchParamNew);
							cs.setLong(7, Long.valueOf(moreCount));
							cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);							
							cs.registerOutParameter(9, oracle.jdbc.OracleTypes.VARCHAR);
							return cs;
						} else {
							cs = con.prepareCall(IQueryConstants.GET_STUDENT_DETAILS_MANAGE_STUDENT);
							cs.setString(1, adminYear);
							cs.setLong(2,customerId);
							cs.setString(3, orgMode);
							cs.setLong(4, Long.parseLong(tenantId));
							cs.setString(5, studentNameAndId);
							cs.setString(6, String.valueOf(IApplicationConstants.DEFAULT_PRISM_VALUE));
							cs.setLong(7, Long.valueOf(moreCount));
							cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);							
							cs.registerOutParameter(9, oracle.jdbc.OracleTypes.VARCHAR);
							return cs;
						}
					} else {
						String tenantId = orgId;
						cs = con.prepareCall(IQueryConstants.GET_STUDENT_DETAILS_MANAGE_STUDENT);
						cs.setString(1, adminYear);
						cs.setLong(2,customerId);
						cs.setString(3, orgMode);
						cs.setLong(4, Long.parseLong(tenantId));
						cs.setString(5, String.valueOf(IApplicationConstants.DEFAULT_PRISM_VALUE));
						cs.setString(6, String.valueOf(IApplicationConstants.DEFAULT_PRISM_VALUE));
						cs.setLong(7, Long.valueOf(moreCount));
						cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);							
						cs.registerOutParameter(9, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ArrayList<StudentTO> studentTOResult = new ArrayList<StudentTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(8);
						StudentTO studentTO = null;
						while (rs.next()){
							studentTO = new StudentTO();
							studentTO.setStudentBioId(rs.getLong("STUDENT_BIO_ID"));
							//Fix for TD 78028 - By Joy
							studentTO.setTestElementId(rs.getString("TESTELEMENTID"));
							if(!IApplicationConstants.CONTRACT_NAME_TASC.equals(Utils.getContractName())){
								studentTO.setParentAccount(getParentAccountDetailsByTestElementId(studentTO.getTestElementId(), customerId));
							}
							studentTO.setStudentName(rs.getString("STUDENTNAME"));
							studentTO.setGrade(rs.getString("STUDENTGRADE"));
							studentTO.setStudentMode(rs.getString("STUDENT_MODE"));
							studentTO.setRowIndentifier(rs.getString("ROWIDENTIFIER"));
							studentTO.setOrgName(rs.getString("SCHOOL"));
							studentTO.setClikedOrgId(rs.getLong("TENANTID"));
							studentTOResult.add(studentTO);
						}
						Utils.logError(cs.getString(9));
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return studentTOResult;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getStudentList() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return studentTOs;
	}

	//Moved to PKG_MANAGE_STUDENT by Joy
	//Fix for TD 78028 - By Joy
	@SuppressWarnings("unchecked")
	private ArrayList<ParentTO> getParentAccountDetailsByTestElementId(final String testElementId,final long customerId) {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getParentAccountDetailsByTestElementId()");
		long t1 = System.currentTimeMillis();
		
		//If no parent found for associated student the the method will return empty object of ArrayList
		ArrayList<ParentTO> parentTOs = new ArrayList<ParentTO>();
		if (testElementId != null) {
			try {
				parentTOs = (ArrayList<ParentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = null;
						cs = con.prepareCall("{call " + IQueryConstants.GET_PARENT_DETAILS_FOR_STUDENT + "}");
						cs.setString(1, testElementId);
						cs.setLong(2,customerId);
						cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						ResultSet rs = null;
						ArrayList<ParentTO> parentTOResult = new ArrayList<ParentTO>();
						try {
							cs.execute();
							rs = (ResultSet) cs.getObject(3);
							ParentTO parentTO = null;
							while (rs.next()){
								parentTO = new ParentTO();
								parentTO.setUserName(rs.getString("USERNAME"));
								parentTO.setStatus(rs.getString("STATUS"));
								parentTOResult.add(parentTO);
							}
							Utils.logError(cs.getString(4));
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
						return parentTOResult;
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				long t2 = System.currentTimeMillis();
				logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getParentAccountDetailsByTestElementId() took time: " + String.valueOf(t2 - t1) + "ms");
			}
		}
		return parentTOs;
		
	}

	/*
	 * (non-Javadoc)
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 * @see com.ctb.prism.parent.dao.IParentDAO#searchStudentAutoComplete(java.lang.String, java.lang.String, java.lang.String, long)
	 */
	@SuppressWarnings("unchecked")
	public String searchStudentAutoComplete(final String studentName,final String tenantId, final String adminyear, final long customerId, final String orgMode) {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - searchStudentAutoComplete()");
		long t1 = System.currentTimeMillis();
		
		String studentListJsonString = null;
		final String searchParam = CustomStringUtil.appendString("%", studentName, "%");
		final long rowNum = 100;
		ArrayList<StudentTO> studentTOs = new ArrayList<StudentTO>();
		try {
			studentTOs = (ArrayList<StudentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = null;
					cs = con.prepareCall("{call " + IQueryConstants.SEARCH_STUDENT + "}");
					cs.setString(1, adminyear);
					cs.setLong(2,customerId);
					cs.setString(3, orgMode);
					cs.setLong(4, Long.parseLong(tenantId));
					cs.setString(5, searchParam);
					cs.setLong(6,rowNum);
					cs.registerOutParameter(7, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(8, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ArrayList<StudentTO> studentTOResult = new ArrayList<StudentTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(7);
						StudentTO studentTO = null;
						while (rs.next()){
							studentTO = new StudentTO();
							studentTO.setStudentBioId(rs.getLong("STUDENT_BIO_ID"));
							//Fix for TD 78028 - By Joy
							studentTO.setTestElementId(rs.getString("TESTELEMENTID"));
							studentTO.setParentAccount(getParentAccountDetailsByTestElementId(studentTO.getTestElementId(), customerId));
							studentTO.setStudentName(rs.getString("STUDENTNAME"));
							studentTO.setGrade(rs.getString("STUDENTGRADE"));
							studentTO.setRowIndentifier(rs.getString("ROWIDENTIFIER"));
							studentTO.setOrgName(rs.getString("SCHOOL"));
							studentTO.setClikedOrgId(rs.getLong("TENANTID"));
							studentTOResult.add(studentTO);
						}
						Utils.logError(cs.getString(8));
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return studentTOResult;
				}
			});
			
			if(studentTOs.size() > 0){
				studentListJsonString = "[";
				for (StudentTO studentTO : studentTOs) {
					studentListJsonString = CustomStringUtil.appendString(studentListJsonString, "\"", studentTO.getStudentName(), "\",");
				}
				studentListJsonString = CustomStringUtil.appendString(studentListJsonString.substring(0, studentListJsonString.length() - 1), "]");
			}
			logger.log(IAppLogger.INFO, studentListJsonString);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - searchStudentAutoComplete() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return studentListJsonString;
	}

	/*
	 * (non-Javadoc)
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 * @see com.ctb.prism.parent.dao.IParentDAO#searchStudent(java.lang.String, java.lang.String, java.lang.String, long)
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<StudentTO> searchStudent(final String studentName,final String tenantId,final String adminyear,final long customerId,final String orgMode) {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - searchStudent()");
		long t1 = System.currentTimeMillis();

		ArrayList<StudentTO> studentTOs = new ArrayList<StudentTO>();
		final long rowNum = 15;
		final String searchParam = CustomStringUtil.appendString("%", studentName, "%");	
		try {
			studentTOs = (ArrayList<StudentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = null;
					cs = con.prepareCall("{call " + IQueryConstants.SEARCH_STUDENT + "}");
					cs.setString(1, adminyear);
					cs.setLong(2,customerId);
					cs.setString(3, orgMode);
					cs.setLong(4, Long.parseLong(tenantId));
					cs.setString(5, searchParam);
					cs.setLong(6,rowNum);
					cs.registerOutParameter(7, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(8, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ArrayList<StudentTO> studentTOResult = new ArrayList<StudentTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(7);
						StudentTO studentTO = null;
						while (rs.next()){
							studentTO = new StudentTO();
							studentTO.setStudentBioId(rs.getLong("STUDENT_BIO_ID"));
							//Fix for TD 78028 - By Joy
							studentTO.setTestElementId(rs.getString("TESTELEMENTID"));
							studentTO.setParentAccount(getParentAccountDetailsByTestElementId(studentTO.getTestElementId(), customerId));
							studentTO.setStudentName(rs.getString("STUDENTNAME"));
							studentTO.setGrade(rs.getString("STUDENTGRADE"));
							studentTO.setRowIndentifier(rs.getString("ROWIDENTIFIER"));
							studentTO.setOrgName(rs.getString("SCHOOL"));
							studentTO.setClikedOrgId(rs.getLong("TENANTID"));
							studentTOResult.add(studentTO);
						}
						Utils.logError(cs.getString(8));
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return studentTOResult;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - searchStudent() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return studentTOs;
	}

	/*
	 * (non-Javadoc)
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 * @see com.ctb.prism.parent.dao.IParentDAO#searchStudentOnRedirect(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<StudentTO> searchStudentOnRedirect(Map<String, Object> paramMap) {
		
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - searchStudentOnRedirect()");
		long t1 = System.currentTimeMillis();
		
		final String tesElementId = (String) paramMap.get("studentBioId");
		final String tenantId = (String) paramMap.get("scrollId");
		//Fix for java.lang.ClassCastException
		String customerIdString = paramMap.get("customer").toString();
		final long customerId = Long.parseLong(customerIdString);
		String orgMode = (String) paramMap.get("orgMode");
		final String adminYear =(String) paramMap.get("adminYear");
		ArrayList<StudentTO> studentTOs = null;
		
		try {
			studentTOs = (ArrayList<StudentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = null;
					cs = con.prepareCall("{call " + IQueryConstants.SEARCH_STUDENT_ON_REDIRECT + "}");
					cs.setString(1, adminYear);
					cs.setString(2, tesElementId);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ArrayList<StudentTO> studentTOResult = new ArrayList<StudentTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(3);
						StudentTO studentTO = null;
						while (rs.next()){
							studentTO = new StudentTO();
							studentTO.setTestElementId(rs.getString("TEST_ELEMENT_ID"));
							studentTO.setOrgId(rs.getLong("ORG_NODEID"));
							studentTO.setOrgName(rs.getString("ORG_NODE_NAME"));
							studentTO.setParentAccount(getParentAccountDetailsByTestElementId(studentTO.getTestElementId(), customerId));
							studentTO.setStudentName(rs.getString("STUDENTNAME"));
							studentTO.setRowIndentifier(rs.getString("ROWIDENTIFIER"));
							studentTO.setGrade(rs.getString("STUDENTGRADE"));
							studentTO.setClikedOrgId(Long.parseLong(tenantId));
							studentTOResult.add(studentTO);
						}
						Utils.logError(cs.getString(4));
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return studentTOResult;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - searchStudentOnRedirect() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return studentTOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getAssessmentList(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<StudentTO> getAssessmentList(final String testElementId) {

		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getAssessmentList()");
		long t1 = System.currentTimeMillis();

		ArrayList<StudentTO> studentAssessmentList = null;
		try {
			studentAssessmentList = (ArrayList<StudentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = null;
					cs = con.prepareCall("{call " + IQueryConstants.GET_ASSESSMENT_LIST + "}");
					cs.setString(1, testElementId);
					cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ArrayList<StudentTO> studentTOResult = new ArrayList<StudentTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(2);
						StudentTO studentTO = null;
						while (rs.next()){
							studentTO = new StudentTO();
							studentTO.setStudentBioId(rs.getLong("STUDENT_BIO_ID"));
							studentTO.setTestElementId(rs.getString("TESTELEMENTID"));
							studentTO.setAdministration(rs.getString("ASSESSMENT_YEAR"));
							studentTO.setInvitationcode(rs.getString("INVITATION_CODE"));
							studentTO.setExpirationDate(rs.getString("EXPIRATION_DATE"));
							studentTO.setIcExpirationStatus(rs.getString("EXPIRATION_STATUS"));
							studentTO.setTotalAvailableClaim(rs.getLong("TOTAL_AVAILABLE"));
							studentTOResult.add(studentTO);
						}
						Utils.logError(cs.getString(3));
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return studentTOResult;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getAssessmentList() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return studentAssessmentList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#updateAssessmentDetails(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean updateAssessmentDetails(final String studentBioId, final String administration, final String invitationcode,
			final String icExpirationStatus, final String totalAvailableClaim, final String expirationDate)
			throws Exception {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - updateAssessmentDetails()");
		com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
		long t1 = System.currentTimeMillis();
		boolean returnFlag = false;
		
		try {
			objectValueTO = (com.ctb.prism.core.transferobject.ObjectValueTO) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.UPDATE_ASSESSMENT + "}");
					cs.setString(1, totalAvailableClaim);
					cs.setString(2, expirationDate);
					cs.setString(3, invitationcode);
					cs.registerOutParameter(4, oracle.jdbc.OracleTypes.NUMBER);
					cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					long executionStatus = 0;
					com.ctb.prism.core.transferobject.ObjectValueTO statusTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
					try {
						cs.execute();
						executionStatus = cs.getLong(4);
						statusTO.setValue(Long.toString(executionStatus));
						statusTO.setErrorMsg(cs.getString(5));
						Utils.logError(cs.getString(5));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return statusTO;
				}
			});
			
			if(Long.parseLong(objectValueTO.getValue()) > 0){
				returnFlag = true;
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.ERROR, "ParentDAOImpl - updateAssessmentDetails() with error: " + objectValueTO.getErrorMsg());
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - updateAssessmentDetails() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return returnFlag;
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
			if (IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
				ldapStatus = ldapManager.updateUser(parentTO.getUserName(), parentTO.getUserName(), parentTO.getUserName(), parentTO.getPassword());
			} else {
				final String userName = parentTO.getUserName();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("username", userName);
				String contractName = Utils.getContractName();
				paramMap.put("contractName",contractName);
				UserTO userTO = loginDAO.getUserEmail(paramMap);
				
				final String salt =  userTO.getSalt() != null ? userTO.getSalt() : PasswordGenerator.getNextSalt();				
				final String encryptedPass = SaltedPasswordEncoder.encryptPassword(parentTO.getPassword(), Utils.getSaltWithUser(userName, salt));

				// get the user which needs modification
				Query searchUserQuery = new Query(Criteria.where("_id").is(userName).and("project_id").is(Utils.getProject(contractName)));
				MUserTO savedUser = getMongoTemplatePrism().findOne(searchUserQuery, MUserTO.class);
				
				if(savedUser != null) {
					savedUser.setPassword(encryptedPass);
					savedUser.setSalt(salt);
					savedUser.setIsFirstTimeLogin(IApplicationConstants.FLAG_N);
					savedUser.setLastName(parentTO.getLastName());
					savedUser.setFirstName(parentTO.getFirstName());
					savedUser.setEmail(parentTO.getMail());
					savedUser.setPhoneNum(parentTO.getMobile());
					
					Address address = new Address();
					address.setCounty(parentTO.getCountry());
					address.setZipCode(parentTO.getZipCode());
					address.setState(parentTO.getState());
					address.setStreet(parentTO.getStreet());
					address.setCity(parentTO.getCity());
					
					savedUser.setAddress(address);
					
					PwdHistory[] pwdHistories =	savedUser.getPwdHistory();
					
					int currentLength = pwdHistories != null ? pwdHistories.length : 0;
					PwdHistory pwdHistory = new PwdHistory();
					pwdHistory.setPwd(encryptedPass);
					pwdHistory.setDate(new Date());
					if(pwdHistories == null) {
						pwdHistories = new PwdHistory[1];
					}
					pwdHistories[currentLength]	= pwdHistory;
					
					savedUser.setPwdHistory(pwdHistories);
					
					List<QuestionTO> questionTOs = parentTO.getQuestionToList();
					if(questionTOs!= null && questionTOs.size() > 0) {
						HintAnswers[] hintAnswerArr = new HintAnswers[questionTOs.size()];
						HintAnswers hintAnswer = null;
						for (int i=0; i<questionTOs.size(); i++) {
							hintAnswer = new HintAnswers();
							hintAnswer.setQID(String.valueOf(questionTOs.get(i).getQuestionId()));
							hintAnswer.setAnsValue(questionTOs.get(i).getAnswer());
							hintAnswerArr[i] = hintAnswer;
						}
						savedUser.setHintAnswers(hintAnswerArr);
					}
					
					
					//save
					getMongoTemplatePrism().save(savedUser);
					logger.log(IAppLogger.DEBUG, "INSERT_USER_DATA Done");
					status = true;
				}
				
			//	boolean isSavedPasswordHistAnswer = savePasswordHistAnswer(userID, parentTO.getQuestionToList());
		

			}
		
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while updating frist time user details.", e);
			throw new BusinessException(e.getMessage());
		}
		return status;
	}

	/**
	 * Moved to store proc and reduce redundant DB call - By Joy
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#manageParentAccountDetails(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public ParentTO manageParentAccountDetails(final String username) {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - manageParentAccountDetails()");
		long t1 = System.currentTimeMillis();
		ParentTO parentTO = null;
		Map<String,Object> resultMap = null;
		List<QuestionTO> questionTOs = new ArrayList<QuestionTO>();
		
		Query searchUserQuery = new Query(Criteria.where("project_id").is(Utils.getProject())
				.and("_id").is(username));
		
		MUserTO user = getMongoTemplatePrism().findOne(searchUserQuery, MUserTO.class);
		
		if(user != null){
			parentTO = new ParentTO();
			parentTO.setUserName(user.get_id());
			parentTO.setDisplayName(user.getDisplayName());
			parentTO.setLastName(user.getLastName());
			parentTO.setFirstName(user.getFirstName());
			parentTO.setMail(user.getEmail());
			parentTO.setMobile(user.getPhoneNum());
			if(user.getAddress() != null) {
				parentTO.setCountry(user.getAddress().getCounty());
				parentTO.setZipCode(user.getAddress().getZipCode());
				parentTO.setCity(user.getAddress().getCity());
				parentTO.setState(user.getAddress().getState());
				parentTO.setStreet(user.getAddress().getStreet());
			}			
			parentTO.setSalt(user.getSalt());
		
			if(user.getHintAnswers() != null) {
				for (int count =0; count < user.getHintAnswers().length ; count++) {
					Query searchQuestionQuery = new Query(Criteria.where("_id").is(Utils.getProject().toUpperCase())
							.and("hintQuestions.qid").is(user.getHintAnswers()[count].getQID())
							);
					searchQuestionQuery.fields().include("hintQuestions.$");
					
					MCustProdAdminTO custProdAdmin = getMongoTemplatePrism("global").findOne(searchQuestionQuery, MCustProdAdminTO.class);
					
					HintQuestions pwdHintQuestion = new HintQuestions();
					 					
					if(custProdAdmin != null) {
						pwdHintQuestion = custProdAdmin.getHintQuestions()[0];
						QuestionTO questionTo = null;
						questionTo = new QuestionTO();
						questionTo.setSno(Long.valueOf(pwdHintQuestion.getSeq()));
						questionTo.setQuestionId(Long.valueOf(pwdHintQuestion.getQid()));
						questionTo.setQuestion(pwdHintQuestion.getQues());
						//questionTo.setAnswerId(rsQuestion.getLong("ANSWER_ID")); --Blocked
						questionTo.setAnswer(user.getHintAnswers()[count].getAnsValue());
						questionTOs.add(questionTo);
						parentTO.setQuestionToList(questionTOs);
					}				
				}
			}			
		}		
		return parentTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getSecurityQuestionForUser(Map<String, Object>)
	 */
	//TODO - Need to move in store proc. Use PKG_MY_ACCOUNT.SP_GET_SECURITY_QUESTIONS() then delete GET_PARENT_SECURITY_QUESTION
	public ArrayList<QuestionTO> getSecurityQuestionForUser(Map<String, Object> paramMap) {

		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - getSecurityQuestionForUser()");
		long t1 = System.currentTimeMillis();
		
		String contractName = (String) paramMap.get("contractName"); 
		String username = (String) paramMap.get("username"); 
		
		List<Map<String, Object>> lstData = null;
		ArrayList<QuestionTO> questionTOs = new ArrayList<QuestionTO>();
		
		Query searchUserQuery = new Query(Criteria.where("project_id").is(contractName.toUpperCase())
				.and("_id").is(username));
		
		MUserTO user = getMongoTemplatePrism(contractName).findOne(searchUserQuery, MUserTO.class);
		
		if(user != null){
			if(user.getHintAnswers() != null) {
				for (int count =0; count < user.getHintAnswers().length ; count++) {
					// populate parent security question and answer detail
					Query searchQuestionQuery = new Query(Criteria.where("_id").is(Utils.getProject(contractName).toUpperCase())
							.and("hintQuestions.qid").is(user.getHintAnswers()[count].getQID())
					);
					searchQuestionQuery.fields().include("hintQuestions.$");

					MCustProdAdminTO custProdAdmin = getMongoTemplatePrism("global").findOne(searchQuestionQuery, MCustProdAdminTO.class);

					HintQuestions pwdHintQuestion = new HintQuestions();

					if(custProdAdmin != null) {
						pwdHintQuestion = custProdAdmin.getHintQuestions()[0];
						QuestionTO questionTo = null;
						questionTo = new QuestionTO();
						questionTo.setSno(Long.valueOf(pwdHintQuestion.getSeq()));
						questionTo.setQuestionId(Long.valueOf(pwdHintQuestion.getQid()));
						questionTo.setQuestion(pwdHintQuestion.getQues());
						//questionTo.setAnswerId(rsQuestion.getLong("ANSWER_ID")); --Blocked
						questionTo.setAnswer(user.getHintAnswers()[count].getAnsValue());
						questionTOs.add(questionTo);

					}				
				}
			}
		}
		long t2 = System.currentTimeMillis();
		logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getSecurityQuestionForUser() took time: " + String.valueOf(t2 - t1) + "ms");
		return questionTOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#validateAnswers(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public boolean validateAnswers(Map<String, Object> paramMap) {
		long validUser = 0;
		String contractName = (String) paramMap.get("contractName"); 
		String username = (String)paramMap.get("username");
		List<QuestionTO> questionToList = new ArrayList<QuestionTO>();
		questionToList = (List<QuestionTO>)paramMap.get("questionToList");
		
		/*for(QuestionTO questionTOs: questionToList){
			questionTOs.getQuestionId();
		}*/
		try {
			validUser = getJdbcTemplatePrism(contractName).queryForObject(
					IQueryConstants.VALIDATE_SECURITY_ANSWERS, Long.class,
					username, questionToList.get(0).getQuestionId(), questionToList.get(0).getAnswer(),
					username, questionToList.get(1).getQuestionId(), questionToList.get(1).getAnswer(), 
					questionToList.get(2).getQuestionId(), questionToList.get(2).getAnswer());
			
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
	public List<UserTO> getUserNamesByEmail(Map<String, Object> paramMap) {
		
		String contractName = (String) paramMap.get("contractName"); 
		String emailId = (String) paramMap.get("emailId"); 
		
		ArrayList<UserTO> UserTOs = new ArrayList<UserTO>();
		List<Map<String, Object>> userslist = null;
		
		Query searchUserQuery = new Query(Criteria.where("email").is(emailId)
				.and("status").in(IApplicationConstants.ACTIVE_FLAG,IApplicationConstants.SS_FLAG));
		
		List<MUserTO> serchUsers = getMongoTemplatePrism(contractName).find(searchUserQuery, MUserTO.class);
		
		if(serchUsers != null && serchUsers.size() > 0) {
			for(MUserTO mUserTO : serchUsers) {
				UserTO to = new UserTO();
				to.setUserName(mUserTO.get_id());
				to.setFirstName(mUserTO.getFirstName());
				to.setLastName(mUserTO.getLastName());
				UserTOs.add(to);
			}
		}
		return UserTOs;
	}

	/**
	 * Remove multiple DB call to single DB call and moved to store proc - By Joy
	 * @see com.ctb.prism.parent.dao.IParentDAO#updateUserProfile(com.ctb.prism.parent.transferobject.ParentTO)
	 */
	@Caching( evict = { 
			@CacheEvict(value = {"inorsConfigCache","inorsAdminCache"}, condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", allEntries = true),
			@CacheEvict(value = {"tascAdminCache","tascAdminCache"},    condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  allEntries = true),
			@CacheEvict(value = {"usmoAdminCache","usmoAdminCache"},    condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  allEntries = true)
	} )
	public boolean updateUserProfile(final ParentTO parentTO) throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - updateUserProfile()");
		long t1 = System.currentTimeMillis();
		
		boolean ldapFlag = Boolean.TRUE;
		parentTO.setLdapFlag(ldapFlag);
		com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
		boolean returnFlag = Boolean.FALSE;
		boolean passwordChanged = Boolean.FALSE;
		try {
			if (parentTO.getPassword() != null && !"".equals(parentTO.getPassword())) {
				// calling ldapManager for updating password for a username in LDAP
				if (IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
					ldapFlag = ldapManager.updateUser(parentTO.getUserName(), parentTO.getUserName(), parentTO.getUserName(), parentTO.getPassword());
					parentTO.setLdapFlag(ldapFlag);
				}else{
					if(parentTO.getSalt() == null){
						parentTO.setSalt(PasswordGenerator.getNextSalt());
					}
					parentTO.setPassword(SaltedPasswordEncoder.encryptPassword(parentTO.getPassword(), Utils.getSaltWithUser(parentTO.getUserName(), parentTO.getSalt())));
				}
				passwordChanged = Boolean.TRUE;
			}/*else{
				parentTO.setPassword(String.valueOf(IApplicationConstants.DEFAULT_PRISM_VALUE));
			}*/
			
			// get the user which needs modification
			Query searchUserQuery = new Query(Criteria.where("_id").is(parentTO.getUserName()).and("project_id").is(Utils.getProject()));
			MUserTO savedUser = getMongoTemplatePrism().findOne(searchUserQuery, MUserTO.class);
			
			if(savedUser != null) {
				if(passwordChanged) {
					
					//savedUser.setIsFirstTimeLogin("N");
					savedUser.setPassword(parentTO.getPassword());
					savedUser.setSalt(parentTO.getSalt());
					
					PwdHistory[] pwdHistoryArr = savedUser.getPwdHistory();
					
					PwdHistory pwdHistory = new PwdHistory();
					pwdHistory.setPwd(parentTO.getPassword());
					pwdHistory.setDate(new Date());
					if(pwdHistoryArr!=null){
						List<PwdHistory> pwdHistoryList = new LinkedList<PwdHistory>( Arrays.asList(pwdHistoryArr) );
						pwdHistoryList.add(pwdHistory);
						PwdHistory[] pwdHistoryArrTemp = new PwdHistory[pwdHistoryList.size()];
						savedUser.setPwdHistory(pwdHistoryList.toArray(pwdHistoryArrTemp));
					} else {
						pwdHistoryArr = new PwdHistory[]{pwdHistory};
						savedUser.setPwdHistory(pwdHistoryArr);
					}
				}
				
				savedUser.setLastName(parentTO.getLastName());
				savedUser.setFirstName(parentTO.getFirstName());
				savedUser.setEmail(parentTO.getMail());
				savedUser.setPhoneNum(parentTO.getMobile());
				savedUser.setDisplayName(parentTO.getDisplayName());
				
				Address address = new Address();
				address.setCounty(parentTO.getCountry());
				address.setZipCode(parentTO.getZipCode());
				address.setState(parentTO.getState());
				address.setStreet(parentTO.getStreet());
				address.setCity(parentTO.getCity());
				
				savedUser.setAddress(address);
				savedUser.setUpdatedDate(new Date());
				
				
				HintAnswers[]  hintAnswersArr = new HintAnswers[parentTO.getQuestionToList().size()];
				HintAnswers hintAnswers = null;
				
				int index = 0;
				for (QuestionTO questionTo : parentTO.getQuestionToList()) {
					hintAnswers = new HintAnswers();
					hintAnswers.setQID(String.valueOf(questionTo.getQuestionId()));
					hintAnswers.setAnsValue(questionTo.getAnswer());
					hintAnswersArr[index] = hintAnswers;
					index++;
				}
				
				savedUser.setHintAnswers(hintAnswersArr);
				
				//save
				getMongoTemplatePrism().save(savedUser);
				
				long t2 = System.currentTimeMillis();
				logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - updateUserProfile() took time: " + String.valueOf(t2 - t1) + "ms");
				
				return true;
			} else {
				return false;
			}
			
		} catch(Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while updating user details.", e);
			
		}
		return false;
	}

	
	/** @author Joy
	 * Modified the method to reduce multiple query hit from DAO, implement store proc
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#addInvitationToAccount(java.lang.String, java.lang.String)
	 */
	@Caching( evict = { 
			@CacheEvict(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", allEntries = true),
			@CacheEvict(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  allEntries = true),
			@CacheEvict(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  allEntries = true)
	} )
	public boolean addInvitationToAccount(final Map<String,Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - addInvitationToAccount()");
		long t1 = System.currentTimeMillis();
		com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
		boolean returnFlag = Boolean.FALSE;
		
		try {
			//Giving patch as some times it is trying to access tasc package
			objectValueTO = (com.ctb.prism.core.transferobject.ObjectValueTO) getJdbcTemplatePrism(Utils.getContractName()).execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.ADD_INVITATION_CODE_TO_ACCOUNT + "}");
					cs.setString(1, (String)paramMap.get("curruser"));
					cs.setString(2, (String)paramMap.get("invitationCode"));
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.NUMBER);
					cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					long executionStatus = 0;
					com.ctb.prism.core.transferobject.ObjectValueTO statusTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
					try {
						cs.execute();
						executionStatus = cs.getLong(3);
						statusTO.setValue(Long.toString(executionStatus));
						statusTO.setErrorMsg(cs.getString(4));
						Utils.logError(cs.getString(4));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return statusTO;
				}
			});
			
			if(Long.parseLong(objectValueTO.getValue()) > 0){
				returnFlag = Boolean.TRUE;
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnFlag = Boolean.FALSE;
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.ERROR, "ParentDAOImpl - addInvitationToAccount() with error: " + objectValueTO.getErrorMsg());
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - addInvitationToAccount() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return returnFlag;
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
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 * Generate a new invitation code and disable the old activation code - By Joy
	 */
	@Caching( evict = { 
			@CacheEvict(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", allEntries = true),
			@CacheEvict(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  allEntries = true),
			@CacheEvict(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  allEntries = true)
	} )
	public boolean regenerateActivationCode(final StudentTO student) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - regenerateActivationCode()");
		com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
		long t1 = System.currentTimeMillis();
		boolean returnFlag = Boolean.FALSE;
		// String envPostFix = propertyLookup.get(IApplicationConstants.ENV_POSTFIX).toUpperCase();
		
		try {
			objectValueTO = (com.ctb.prism.core.transferobject.ObjectValueTO) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.REGENERATE_ACTIVATION_CODE + "}");
					cs.setString(1, student.getInvitationcode());
					cs.setString(2, student.getTestElementId());
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
					cs.registerOutParameter(4, oracle.jdbc.OracleTypes.NUMBER);
					cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					long executionStatus = 0;
					com.ctb.prism.core.transferobject.ObjectValueTO statusTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
					try {
						cs.execute();
						statusTO.setOther(cs.getString(3));
						executionStatus = cs.getLong(4);
						statusTO.setValue(Long.toString(executionStatus));
						statusTO.setErrorMsg(cs.getString(5));
						Utils.logError(cs.getString(5));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return statusTO;
				}
			});
			
			student.setIcLetterPath(objectValueTO.getOther());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		} finally {
			if(Long.parseLong(objectValueTO.getValue()) > 0){
				returnFlag = Boolean.TRUE;
			}
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.ERROR, "ParentDAOImpl - regenerateActivationCode() with error: " + objectValueTO.getErrorMsg());
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - regenerateActivationCode() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return returnFlag;
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
							objectValueTO.setName(Utils.convertSpecialCharToHtmlChar(rs.getString("NAME")));
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
	 * @see com.ctb.prism.parent.dao.IParentDAO#populatePerformanceLevel(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populatePerformanceLevel(final Map<String, Object> paramMap) throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - populatePerformanceLevel()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		long t1 = System.currentTimeMillis();
		final long custProdId = ((Long) paramMap.get("custProdId")).longValue();
		final String contentTypeId = (String) paramMap.get("contentTypeId");
		try {
			objectValueTOList = (List<com.ctb.prism.core.transferobject.ObjectValueTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_PERFORMANCE_LEVEL + "}");
					cs.setLong(1, custProdId);
					cs.setString(2, contentTypeId);
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
							objectValueTO.setName(Utils.convertSpecialCharToHtmlChar(rs.getString("NAME")));
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
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - populatePerformanceLevel() took time: " + String.valueOf(t2 - t1) + "ms");
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
					cs.setString(11, manageContentTO.getStatusCode());
					cs.setString(12, manageContentTO.getObjectiveDesc());
					cs.registerOutParameter(13, oracle.jdbc.OracleTypes.NUMBER);
					cs.registerOutParameter(14, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					long executionStatus = 0;
					com.ctb.prism.core.transferobject.ObjectValueTO statusTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
					try {
						cs.execute();
						executionStatus = cs.getLong(13);
						statusTO.setValue(Long.toString(executionStatus));
						statusTO.setName("");
						Utils.logError(cs.getString(14));
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
		final String moreCount = (String) paramMap.get("moreCount");

		try {
			manageContentList = (List<ManageContentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					if (checkFirstLoad.equals("true")) {
						CallableStatement cs = con.prepareCall(IQueryConstants.GET_MANAGE_CONTENT_LIST);
						cs.setLong(1, custProdId);
						cs.setLong(2, subtestId);
						cs.setLong(3, objectiveId);
						cs.setString(4, contentTypeId);
						cs.setLong(5, Long.valueOf(moreCount));
						cs.registerOutParameter(6, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(7, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					} else {
						final long lastid = Long.parseLong((String) paramMap.get("lastid"));
						CallableStatement cs = con.prepareCall(IQueryConstants.GET_MANAGE_CONTENT_LIST_MORE);
						cs.setLong(1, custProdId);
						cs.setLong(2, subtestId);
						cs.setLong(3, objectiveId);
						cs.setLong(4, lastid);
						cs.setString(5, contentTypeId);
						cs.setLong(6, Long.valueOf(moreCount));
						cs.registerOutParameter(7, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(8, oracle.jdbc.OracleTypes.VARCHAR);
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
							rs = (ResultSet) cs.getObject(6);
							Utils.logError(cs.getString(7));
						} else {
							rs = (ResultSet) cs.getObject(7);
							Utils.logError(cs.getString(8));
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
						Utils.logError(cs.getString(7));
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
						Utils.logError(cs.getString(3));
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
		final String performanceLevelId = (String) paramMap.get("performanceLevelId");
		final String statusCodeId = (String) paramMap.get("statusCodeId");

		try {
			manageContentTO = (ManageContentTO) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_GENERIC_DETAILS_FOR_EDIT + "}");
					cs.setLong(1, custProdId);
					cs.setLong(2, gradeId);
					cs.setLong(3, subtestId);
					cs.setLong(4, objectiveId);
					cs.setString(5, type);
					cs.setString(6, performanceLevelId);
					cs.setString(7, statusCodeId);
					cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ManageContentTO manageContentTOResult = null;
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(8);
						if (rs.next()) {
							manageContentTOResult = new ManageContentTO();
							if(IApplicationConstants.CONTENT_TYPE_STD.equals(type)){
								manageContentTOResult.setContentId(rs.getLong("METADATA_ID"));
								manageContentTOResult.setContentDescription(Utils.convertClobToString((Clob) rs.getClob("CONTENT_DESCRIPTION")));
								manageContentTOResult.setObjectiveDesc(rs.getString("OBJECTIVE_DESC"));
							}else{
								manageContentTOResult.setContentId(rs.getLong("METADATA_ID"));
								manageContentTOResult.setContentDescription(Utils.convertClobToString((Clob) rs.getClob("CONTENT_DESCRIPTION")));
							}
							
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
		final String testElementId = (String) paramMap.get("testElementId");

		try {
			objectValueTOList = (List<com.ctb.prism.core.transferobject.ObjectValueTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_STUDENT_SUBTEST + "}");
					cs.setString(1, testElementId);
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
							objectValueTO.setOther(rs.getString("OTHER")); // cust prod id
							objectValueTO.setRowIndentifier(rs.getString("CODE")); // product code
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
		final long custProdId = ((Long) paramMap.get("custProdId")).longValue();

		try {
			articleTypeDetailsList = (List<ManageContentTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_ARTICLE_TYPE_DETAILS + "}");
					cs.setLong(1, studentBioId);
					cs.setLong(2, subtestId);
					cs.setLong(3, studentGradeId);
					cs.setString(4, contentType);
					cs.setLong(5, custProdId);
					cs.registerOutParameter(6, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(7, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<ManageContentTO> articleTypeDetailsResult = new ArrayList<ManageContentTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(6);
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
