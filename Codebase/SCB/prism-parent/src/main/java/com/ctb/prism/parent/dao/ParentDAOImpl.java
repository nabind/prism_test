package com.ctb.prism.parent.dao;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.transferobject.ManageContentTO;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.QuestionTO;
import com.ctb.prism.parent.transferobject.StudentTO;

@Repository("parentDAO")
public class ParentDAOImpl extends BaseDAO implements IParentDAO {

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
			@Cacheable(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getSecretQuestions'),'inors' )"),
			@Cacheable(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getSecretQuestions'),'tasc' )"),
			@Cacheable(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getSecretQuestions'),'usmo' )"),
			@Cacheable(value = "wiscConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'wisc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getSecretQuestions'),'wisc' )")
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
		try {
			questionList = (List<QuestionTO>) getJdbcTemplatePrism(contractName).execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_SECRET_QUESTION + "}");
					cs.setString(1, "0");
					cs.registerOutParameter(2, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rsQuestion = null;
					List<QuestionTO> questionList = new ArrayList<QuestionTO>();
					try {
						cs.execute();
						rsQuestion = cs.getResultSet();
						QuestionTO questionTO = null;
						while(rsQuestion.next()){
							questionTO = new QuestionTO();
							questionTO.setQuestionId(rsQuestion.getLong("QUESTION_ID"));
							questionTO.setQuestion(rsQuestion.getString("QUESTION"));
							questionTO.setSno(rsQuestion.getLong("SNO"));
							questionList.add(questionTO);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return questionList;
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getSecretQuestions() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return questionList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#checkUserAvailability(java.lang.String)
	 */
	public boolean checkUserAvailability(final Map<String,Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: checkUserAvailability()");
		long t1 = System.currentTimeMillis();
		final String username = (String)paramMap.get("username");
		String contractName = (String) paramMap.get("contractName");
		if(contractName == null) {
			contractName = Utils.getContractName();
		}
		logger.log(IAppLogger.INFO, "Contract Name: "+contractName);

		String tempUsername = "";
		try{
			tempUsername = (String) getJdbcTemplatePrism(contractName).execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				        	CallableStatement cs = con.prepareCall("{call " + IQueryConstants.VALIDATE_USER_NAME + "}");
				            cs.setString(1, username);
				            cs.registerOutParameter(2, java.sql.Types.VARCHAR);
				            return cs;				      			            
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rsUsername = null;
			        			String usernameResult = "";
			        			try {
									cs.execute();
									rsUsername = cs.getResultSet();
									if(rsUsername.next()){
										usernameResult = rsUsername.getString("USERNAME");
									}
									
			        			} catch (SQLException e) {
			        				e.printStackTrace();
			        			}
			        			return usernameResult;
				        }
				    });
			if("".equals(tempUsername)){
				return Boolean.TRUE;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: checkUserAvailability() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return Boolean.FALSE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#checkActiveUserAvailability(java.lang.String)
	 */
	public boolean checkActiveUserAvailability(Map<String, Object> paramMap) {
		String contractName = (String) paramMap.get("contractName"); 
		String username = (String) paramMap.get("username"); 
		List<Map<String, Object>> lstData = getJdbcTemplatePrism(contractName).queryForList(IQueryConstants.VALIDATE_ACTIVE_USER_NAME, username);
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
				            //cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR); 
				            cs.registerOutParameter(3, java.sql.Types.VARCHAR);
				            return cs;				      			            
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rsIc = null;
			        			ParentTO parentTOResult = null;
			        			try {
									cs.execute();
									//rsIc = (ResultSet) cs.getObject(3);
									rsIc = cs.getResultSet();
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
				            //cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR); 
				            cs.registerOutParameter(2, java.sql.Types.VARCHAR);
				            return cs;				      			            
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rsStudent = null;
			        			List<StudentTO> studentToResult = new ArrayList<StudentTO>();
			        			try {
									cs.execute();
									//rsStudent = (ResultSet) cs.getObject(2);
									rsStudent = cs.getResultSet();
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
			@CacheEvict(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  allEntries = true),
			@CacheEvict(value = "wiscAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'wisc'",  allEntries = true)
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
					cs.registerOutParameter(count++, java.sql.Types.BIGINT); // index 20
					cs.registerOutParameter(count++, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					String strUserId = null; 
					try {
						cs.execute();
						strUserId = Long.toString(cs.getLong(20));
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
						//cs.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(5, java.sql.Types.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						ResultSet rs = null;
						List<StudentTO> studentResult = new ArrayList<StudentTO>();
						try {
							cs.execute();
							//rs = (ResultSet) cs.getObject(5);
							rs = cs.getResultSet();
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
						//cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(2, java.sql.Types.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						ResultSet rs = null;
						List<StudentTO> studentResult = new ArrayList<StudentTO>();
						try {
							cs.execute();
							//rs = (ResultSet) cs.getObject(2);
							rs = cs.getResultSet();
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
			@Cacheable(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('inors', #p0, #p1, #p2, #p3, #p4, #root.method.name )"),
			@Cacheable(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('tasc', #p0, #p1, #p2, #p3, #p4, #root.method.name )"),
			@Cacheable(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('usmo', #p0, #p1, #p2, #p3, #p4, #root.method.name )"),
			@Cacheable(value = "wiscAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'wisc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('wisc', #p0, #p1, #p2, #p3, #p4, #root.method.name )")
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
							//cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);							
							cs.registerOutParameter(8, java.sql.Types.VARCHAR);
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
							//cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);							
							cs.registerOutParameter(8, java.sql.Types.VARCHAR);
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
						//cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);							
						cs.registerOutParameter(8, java.sql.Types.VARCHAR);
					}
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ArrayList<ParentTO> parentTOResult = new ArrayList<ParentTO>();
					try {
						cs.execute();
						//rs = (ResultSet) cs.getObject(8);
						rs = cs.getResultSet();
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
						Utils.logError(cs.getString(8));
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
						//cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(8, java.sql.Types.VARCHAR);
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
						//cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(8, java.sql.Types.VARCHAR);
					}
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ArrayList<ParentTO> parentTOResult = new ArrayList<ParentTO>();
					try {
						cs.execute();
						//rs = (ResultSet) cs.getObject(8);
						rs = cs.getResultSet();
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
						Utils.logError(cs.getString(8));
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
					//cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(8, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ArrayList<ParentTO> parentTOResult = new ArrayList<ParentTO>();
					try {
						cs.execute();
						//rs = (ResultSet) cs.getObject(8);
						rs = cs.getResultSet();
						ParentTO parentTO = null;
						while (rs.next()){
							parentTO = new ParentTO();
							parentTO.setUserName(rs.getString("USERNAME"));
							parentTO.setFullName(rs.getString("FULLNAME"));
							parentTOResult.add(parentTO);
						}
						Utils.logError(cs.getString(8));
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
			@Cacheable(value="inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getStudentList'),'inors' )"),
			@Cacheable(value="tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getStudentList'),'tasc' )"),
			@Cacheable(value="usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getStudentList'),'usmo' )"),
			@Cacheable(value="wiscAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'wisc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getStudentList'),'wisc' )")
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
							//cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);							
							cs.registerOutParameter(8, java.sql.Types.VARCHAR);
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
							//cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);							
							cs.registerOutParameter(8, java.sql.Types.VARCHAR);
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
						//cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);							
						cs.registerOutParameter(8, java.sql.Types.VARCHAR);
						return cs;
					}
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ArrayList<StudentTO> studentTOResult = new ArrayList<StudentTO>();
					try {
						cs.execute();
						//rs = (ResultSet) cs.getObject(8);
						rs = cs.getResultSet();
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
						//cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(3, java.sql.Types.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						ResultSet rs = null;
						ArrayList<ParentTO> parentTOResult = new ArrayList<ParentTO>();
						try {
							cs.execute();
							//rs = (ResultSet) cs.getObject(3);
							rs = cs.getResultSet();
							ParentTO parentTO = null;
							while (rs.next()){
								parentTO = new ParentTO();
								parentTO.setUserName(rs.getString("USERNAME"));
								parentTO.setStatus(rs.getString("STATUS"));
								parentTOResult.add(parentTO);
							}
							Utils.logError(cs.getString(3));
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
					//cs.registerOutParameter(7, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(7, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ArrayList<StudentTO> studentTOResult = new ArrayList<StudentTO>();
					try {
						cs.execute();
						//rs = (ResultSet) cs.getObject(7);
						rs = cs.getResultSet();
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
						Utils.logError(cs.getString(7));
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
					//cs.registerOutParameter(7, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(7, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ArrayList<StudentTO> studentTOResult = new ArrayList<StudentTO>();
					try {
						cs.execute();
						//rs = (ResultSet) cs.getObject(7);
						rs = cs.getResultSet();
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
						Utils.logError(cs.getString(7));
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
					//cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(3, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ArrayList<StudentTO> studentTOResult = new ArrayList<StudentTO>();
					try {
						cs.execute();
						//rs = (ResultSet) cs.getObject(3);
						rs = cs.getResultSet();
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
					//cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(2, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ArrayList<StudentTO> studentTOResult = new ArrayList<StudentTO>();
					try {
						cs.execute();
						//rs = (ResultSet) cs.getObject(2);
						rs = cs.getResultSet();
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
						Utils.logError(cs.getString(2));
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
					cs.registerOutParameter(4, java.sql.Types.BIGINT);
					cs.registerOutParameter(5, java.sql.Types.VARCHAR);
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
			Long userID = getJdbcTemplatePrism().queryForLong(IQueryConstants.CHECK_EXISTING_USER, parentTO.getUserName());
			if (IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
				ldapStatus = ldapManager.updateUser(parentTO.getUserName(), parentTO.getUserName(), parentTO.getUserName(), parentTO.getPassword());
			} else {
				final String userName = parentTO.getUserName();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("username", userName);
				paramMap.put("contractName", Utils.getContractName());
				UserTO userTO = loginDAO.getUserEmail(paramMap);
				
				final String salt =  userTO.getSalt() != null ? userTO.getSalt() : PasswordGenerator.getNextSalt();				
				final String encryptedPass = SaltedPasswordEncoder.encryptPassword(parentTO.getPassword(), Utils.getSaltWithUser(userName, salt));
				
				/*getJdbcTemplatePrism().update(IQueryConstants.UPDATE_PASSWORD_DATA, IApplicationConstants.FLAG_Y,
						SaltedPasswordEncoder.encryptPassword(parentTO.getPassword(), Utils.getSaltWithUser(parentTO.getUserName(), salt)), salt, parentTO.getUserName());
				 */				
				ldapStatus  = (Boolean)getJdbcTemplatePrism().execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = con.prepareCall(IQueryConstants.SP_RESET_PASSWORD);
						cs.setString(1, userName);
						cs.setString(2, encryptedPass);
						cs.setString(3, salt);
						cs.setString(4, IApplicationConstants.FLAG_Y);
						cs.registerOutParameter(5, java.sql.Types.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						try {
							cs.execute();
							if(cs.getString(5) == null) {
								return true;
							}
							Utils.logError(cs.getString(5));
						} catch (SQLException e) {
							e.printStackTrace();
						}
						logger.log(IAppLogger.INFO, "resetPassword()");
						return false;
					}
				});
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
		try {
			resultMap = (HashMap<String,Object>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_ACCOUNT_DETAILS + "}");
					cs.setString(1, username);
					cs.registerOutParameter(2, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					Map<String,Object> resultMap = null;
					ResultSet rsUser = null;
					ResultSet rsQuestion = null;
					try {
						cs.execute();
						rsUser = cs.getResultSet();						
						ParentTO parentTO = null;
						while (rsUser.next()) {
							parentTO = new ParentTO();
							parentTO.setUserId(rsUser.getLong("USERID"));
							parentTO.setUserName(rsUser.getString("USERNAME"));
							parentTO.setDisplayName(rsUser.getString("DISPLAY_USERNAME"));
							parentTO.setLastName(rsUser.getString("LAST_NAME"));
							parentTO.setFirstName(rsUser.getString("FIRST_NAME"));
							parentTO.setMail(rsUser.getString("EMAIL_ADDRESS"));
							parentTO.setMobile(rsUser.getString("PHONE_NO"));
							parentTO.setCountry(rsUser.getString("COUNTRY"));
							parentTO.setZipCode(rsUser.getString("ZIPCODE"));
							parentTO.setCity(rsUser.getString("CITY"));
							parentTO.setState(rsUser.getString("STATE"));
							parentTO.setStreet(rsUser.getString("STREET"));
							parentTO.setSalt(rsUser.getString("SALT"));
						}
						
						if (cs.getMoreResults()) {
							rsQuestion = cs.getResultSet();
						}
						List<QuestionTO> questionTOs = new ArrayList<QuestionTO>();
						QuestionTO questionTo = null;
						while (rsQuestion.next()) {
							questionTo = new QuestionTO();
							questionTo.setSno(rsQuestion.getLong("SNO"));
							questionTo.setQuestionId(rsQuestion.getLong("QUESTION_ID"));
							questionTo.setQuestion(rsQuestion.getString("QUESTION"));
							questionTo.setAnswerId(rsQuestion.getLong("ANSWER_ID"));
							questionTo.setAnswer(rsQuestion.getString("ANSWER"));
							questionTOs.add(questionTo);
						}
						
						resultMap = new HashMap<String,Object>();
						resultMap.put("parentTO", parentTO);
						resultMap.put("questionTOs", questionTOs);
					} catch (SQLException e) {
						e.printStackTrace();
						logger.log(IAppLogger.ERROR, "Error occurred while retrieving user details for manage.", e);
					}
					return resultMap;
				}
			});
			
			parentTO = (ParentTO)resultMap.get("parentTO");
			parentTO.setQuestionToList((List<QuestionTO>)resultMap.get("questionTOs"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(IAppLogger.ERROR, "Error occurred while retrieving user details for manage.", e);
		}finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - manageParentAccountDetails() took time: " + String.valueOf(t2 - t1) + "ms");
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

		String contractName = (String) paramMap.get("contractName"); 
		String username = (String) paramMap.get("username"); 
		
		List<Map<String, Object>> lstData = null;
		ArrayList<QuestionTO> questionTOs = new ArrayList<QuestionTO>();

		// populate parent security question and answer detail
		lstData = getJdbcTemplatePrism(contractName).queryForList(IQueryConstants.GET_PARENT_SECURITY_QUESTION, username);
		if (lstData.size() > 0) {

			for (Map<String, Object> fieldDetails : lstData) {
				QuestionTO questionTo = new QuestionTO();
				questionTo.setSno(((Long) fieldDetails.get("SNO")).longValue());
				questionTo.setQuestionId(((Long) fieldDetails.get("QUESTION_ID")).longValue());
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
			validUser = getJdbcTemplatePrism(contractName).queryForLong(
					IQueryConstants.VALIDATE_SECURITY_ANSWERS, 
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
		userslist = getJdbcTemplatePrism(contractName).queryForList(IQueryConstants.GET_ALL_USERS_BY_EMAIL, emailId);
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
	 * Remove multiple DB call to single DB call and moved to store proc - By Joy
	 * @see com.ctb.prism.parent.dao.IParentDAO#updateUserProfile(com.ctb.prism.parent.transferobject.ParentTO)
	 */
	@Caching( evict = { 
			@CacheEvict(value = {"inorsConfigCache","inorsAdminCache"}, condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", allEntries = true),
			@CacheEvict(value = {"tascAdminCache","tascAdminCache"},    condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  allEntries = true),
			@CacheEvict(value = {"usmoAdminCache","usmoAdminCache"},    condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  allEntries = true),
			@CacheEvict(value = {"wiscAdminCache","wiscAdminCache"},    condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'wisc'",  allEntries = true)
	} )
	public boolean updateUserProfile(final ParentTO parentTO) throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: ParentDAOImpl - updateUserProfile()");
		long t1 = System.currentTimeMillis();
		
		boolean ldapFlag = Boolean.TRUE;
		parentTO.setLdapFlag(ldapFlag);
		com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
		boolean returnFlag = Boolean.FALSE;
		
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
			}else{
				parentTO.setPassword(String.valueOf(IApplicationConstants.DEFAULT_PRISM_VALUE));
			}
			final String[] questionIdArr = new String[parentTO.getQuestionToList().size()];
			final String[] answerIdArr = new String[parentTO.getQuestionToList().size()];
			final String[] ansValArr = new String[parentTO.getQuestionToList().size()];
			int index = 0;
			for (QuestionTO questionTo : parentTO.getQuestionToList()) {
				questionIdArr[index] = String.valueOf(questionTo.getQuestionId());
				answerIdArr[index] = String.valueOf(questionTo.getAnswerId());
				ansValArr[index] = String.valueOf(questionTo.getAnswer());
				index++;
			}
			
			objectValueTO = (com.ctb.prism.core.transferobject.ObjectValueTO) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					int count =1;
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.UPDATE_USER_DATA + "}");
					cs.setLong(count++, parentTO.getUserId());
					cs.setString(count++, parentTO.getPassword());
					cs.setString(count++, parentTO.getSalt());
					cs.setString(count++, parentTO.getFirstName());
					cs.setString(count++, parentTO.getLastName());
					cs.setString(count++, parentTO.getMail());
					cs.setString(count++, parentTO.getMobile());
					cs.setString(count++, parentTO.getCountry());
					cs.setString(count++, parentTO.getZipCode());
					cs.setString(count++, parentTO.getState());
					cs.setString(count++, parentTO.getStreet());
					cs.setString(count++, parentTO.getCity());
					cs.setString(count++, parentTO.getDisplayName());
					cs.setString(count++, Utils.arrayToSeparatedString(questionIdArr, '~'));
					cs.setString(count++, Utils.arrayToSeparatedString(answerIdArr, '~'));
					cs.setString(count++, Utils.arrayToSeparatedString(ansValArr, '~'));
					cs.registerOutParameter(count++, java.sql.Types.BIGINT);
					cs.registerOutParameter(count++, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					long executionStatus = 0;
					com.ctb.prism.core.transferobject.ObjectValueTO statusTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
					try {
						cs.execute();
						executionStatus = cs.getLong(17);
						statusTO.setValue(Long.toString(executionStatus));
						statusTO.setErrorMsg(cs.getString(18));
						Utils.logError(statusTO.getErrorMsg());
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return statusTO;
				}
			});
			
			if(Long.parseLong(objectValueTO.getValue()) > 0){
				returnFlag = Boolean.TRUE;
			}
			
		} catch (BusinessException bex) {
			throw new BusinessException(bex.getCustomExceptionMessage());
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while updating user profile details.", e);
			return Boolean.FALSE;
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.ERROR, "ParentDAOImpl - updateUserProfile() with error: " + objectValueTO.getErrorMsg());
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - updateUserProfile() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return returnFlag;
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
			@CacheEvict(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  allEntries = true),
			@CacheEvict(value = "wiscAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'wisc'",  allEntries = true)
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
					cs.registerOutParameter(3, java.sql.Types.BIGINT);
					cs.registerOutParameter(4, java.sql.Types.VARCHAR);
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
				return (Long.toString(rs.getLong(1)));
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
			@CacheEvict(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  allEntries = true),
			@CacheEvict(value = "wiscAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'wisc'",  allEntries = true)
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
					cs.registerOutParameter(3, java.sql.Types.VARCHAR);
					cs.registerOutParameter(4, java.sql.Types.BIGINT);
					cs.registerOutParameter(5, java.sql.Types.VARCHAR);
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
					//cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(2, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOResult = new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
					try {
						cs.execute();
						//rs = (ResultSet) cs.getObject(2);
						rs = cs.getResultSet();
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
					//cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(3, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOResult = new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
					try {
						cs.execute();
						//rs = (ResultSet) cs.getObject(3);
						rs = cs.getResultSet();
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
					//cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(3, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOResult = new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
					try {
						cs.execute();
						//rs = (ResultSet) cs.getObject(3);
						rs = cs.getResultSet();
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
					//cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(3, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOResult = new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
					try {
						cs.execute();
						//rs = (ResultSet) cs.getObject(3);
						rs = cs.getResultSet();
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
					cs.registerOutParameter(12, java.sql.Types.BIGINT);
					cs.registerOutParameter(13, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					long executionStatus = 0;
					com.ctb.prism.core.transferobject.ObjectValueTO statusTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
					try {
						cs.execute();
						executionStatus = cs.getLong(12);
						statusTO.setValue(Long.toString(executionStatus));
						statusTO.setName("");
						Utils.logError(cs.getString(13));
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
						//cs.registerOutParameter(6, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(6, java.sql.Types.VARCHAR);
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
						//cs.registerOutParameter(7, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(7, java.sql.Types.VARCHAR);
						return cs;
					}
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<ManageContentTO> manageContentListResult = new ArrayList<ManageContentTO>();
					try {
						cs.execute();
						rs = cs.getResultSet();
						while (rs.next()) {
							ManageContentTO manageContentTO = new ManageContentTO();
							manageContentTO.setContentId(rs.getLong("METADATA_ID"));
							manageContentTO.setContentName(rs.getString("NAME"));
							manageContentTO.setSubHeader(rs.getString("SUB_HEADER"));
							manageContentTO.setGradeName(rs.getString("GRADE"));
							manageContentTO.setPerformanceLevel(rs.getString("PROFICIENCY_LEVEL"));
							manageContentListResult.add(manageContentTO);
						}
						if (checkFirstLoad.equals("true")) {
							//rs = (ResultSet) cs.getObject(6);
							Utils.logError(cs.getString(7));
						} else {
							//rs = (ResultSet) cs.getObject(7);
							Utils.logError(cs.getString(8));
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
					//cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(2, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ManageContentTO manageContentTOResult = null;
					try {
						cs.execute();
						//rs = (ResultSet) cs.getObject(2);
						rs = cs.getResultSet();
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
					cs.registerOutParameter(6, java.sql.Types.BIGINT);
					cs.registerOutParameter(7, java.sql.Types.VARCHAR);
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
					cs.registerOutParameter(2, java.sql.Types.BIGINT);
					cs.registerOutParameter(3, java.sql.Types.VARCHAR);
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
					//cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ManageContentTO manageContentTOResult = null;
					try {
						cs.execute();
						//rs = (ResultSet) cs.getObject(8);
						rs = cs.getResultSet();
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
					//cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(2, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOResult = new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
					try {
						cs.execute();
						//rs = (ResultSet) cs.getObject(2);
						rs = cs.getResultSet();
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
					//cs.registerOutParameter(6, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(6, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<ManageContentTO> articleTypeDetailsResult = new ArrayList<ManageContentTO>();
					try {
						cs.execute();
						//rs = (ResultSet) cs.getObject(6);
						rs = cs.getResultSet();
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
					//cs.registerOutParameter(7, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(7, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					ManageContentTO manageContentTOResult = null;
					try {

						cs.execute();
						//rs = (ResultSet) cs.getObject(7);
						rs = cs.getResultSet();
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
					//cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(2, java.sql.Types.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<ManageContentTO> gradeSubtestResult = new ArrayList<ManageContentTO>();
					try {
						cs.execute();
						//rs = (ResultSet) cs.getObject(2);
						rs = cs.getResultSet();
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
