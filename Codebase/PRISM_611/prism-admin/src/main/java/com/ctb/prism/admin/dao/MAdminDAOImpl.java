package com.ctb.prism.admin.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.stereotype.Repository;

import com.ctb.prism.admin.transferobject.EduCenterTO;
import com.ctb.prism.admin.transferobject.EduCenterTOMapper;
import com.ctb.prism.admin.transferobject.ObjectValueTO;
import com.ctb.prism.admin.transferobject.OrgTO;
import com.ctb.prism.admin.transferobject.OrgTreeTO;
import com.ctb.prism.admin.transferobject.PwdHintTO;
import com.ctb.prism.admin.transferobject.RoleTO;
import com.ctb.prism.admin.transferobject.StgOrgTO;
import com.ctb.prism.admin.transferobject.StudentDataTO;
import com.ctb.prism.admin.transferobject.UserDataTO;
import com.ctb.prism.admin.transferobject.UserTO;
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.constant.IOrgQuery;
import com.ctb.prism.core.constant.IQueryConstants;
import com.ctb.prism.core.dao.BaseDAO;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.transferobject.ObjectValueTOMapper;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.LdapManager;
import com.ctb.prism.core.util.PasswordGenerator;
import com.ctb.prism.core.util.SaltedPasswordEncoder;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.dao.ILoginDAO;
import com.ctb.prism.login.transferobject.MCustProdAdminTO;
import com.ctb.prism.login.transferobject.MOrgTO;
import com.ctb.prism.login.transferobject.MUserTO;
import com.ctb.prism.login.transferobject.OrgCategory;
import com.ctb.prism.login.transferobject.OrgUser;
import com.ctb.prism.login.transferobject.Roles;

//@Repository("adminDAO")
@SuppressWarnings("unchecked")
public class MAdminDAOImpl extends BaseDAO /*implements IAdminDAO*/ {

	@Autowired
	private LdapManager ldapManager;

	@Autowired
	private IPropertyLookup propertyLookup;
	
	@Autowired
	private ILoginDAO loginDAO; 

	private static final IAppLogger logger = LogFactory.getLoggerInstance(AdminDAOImpl.class.getName());

	/**
	 * Returns the organizationList on load.
	 * 
	 * @param nodeid
	 * @return
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrganizationDetailsOnFirstLoad') )"),
			@Cacheable(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrganizationDetailsOnFirstLoad') )"),
			@Cacheable(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrganizationDetailsOnFirstLoad') )")
	} )
	public ArrayList<OrgTO> getOrganizationDetailsOnFirstLoad(Map<String, Object> paramMap) throws Exception{
		
		logger.log(IAppLogger.INFO, "Enter: getOrganizationDetailsOnFirstLoad()");
		final long nodeid = Long.valueOf(paramMap.get("nodeid").toString());
		final long customerId = Long.valueOf(paramMap.get("customerId").toString());
		
		return (ArrayList<OrgTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_CURR_TENANT_DETAILS);
				cs.setLong(1, nodeid);
				cs.setLong(2, customerId);
				cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
				cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				ResultSet rs = null;
				List<OrgTO> OrgTOList = new ArrayList<OrgTO>();
				try {
					cs.execute();
					rs = (ResultSet) cs.getObject(3);
					Utils.logError(cs.getString(4));
					if(cs.getString(4) != null && cs.getString(4).length() > 0) {
						throw new BusinessException("Exception occured while getting Organization " + cs.getString(4));
					}
					while(rs.next()){
						OrgTO to = new OrgTO();
						to.setTenantId(rs.getLong("ORG_NODEID"));
						to.setTenantName(rs.getString("ORG_NODE_NAME"));
						to.setParentTenantId(rs.getLong("PARENT_ORG_NODEID"));
						to.setOrgLevel(rs.getLong("ORG_NODE_LEVEL"));
						OrgTOList.add(to);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				logger.log(IAppLogger.INFO, "getOrganizationDetailsOnFirstLoad().OrgTOList.size()=" + OrgTOList.size());
				logger.log(IAppLogger.INFO, "Exit: getOrganizationDetailsOnFirstLoad()");
				return OrgTOList;
			}
		});
	}

	/**
	 * Returns the organizationList on load.
	 * 
	 * @param nodeid
	 * @return
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrganizationDetailsOnClick') )"),
			@Cacheable(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrganizationDetailsOnClick') )"),
			@Cacheable(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrganizationDetailsOnClick') )")
	} )
	public ArrayList<OrgTO> getOrganizationDetailsOnClick(Map<String, Object> paramMap) throws Exception{
		logger.log(IAppLogger.INFO, "Enter: getOrganizationDetailsOnClick()");
		final long nodeid = Long.valueOf(paramMap.get("nodeid").toString());
		final long custProdId = Long.valueOf(paramMap.get("adminYear").toString());
		final long customerId = Long.valueOf(paramMap.get("customerId").toString());

		logger.log(IAppLogger.INFO, "nodeid=" + nodeid);
		logger.log(IAppLogger.INFO, "custProdId=" + custProdId);
		logger.log(IAppLogger.INFO, "customerId=" + customerId);
	
		return (ArrayList<OrgTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_TENANT_DETAILS);
				cs.setLong(1, nodeid);
				cs.setLong(2, customerId);
				cs.setLong(3, custProdId);
				cs.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
				cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				ResultSet rs = null;
				List<OrgTO> OrgTOList = new ArrayList<OrgTO>();
				try {
					cs.execute();
					rs = (ResultSet) cs.getObject(4);
					Utils.logError(cs.getString(5));
					if(cs.getString(5) != null && cs.getString(5).length() > 0) {
						throw new BusinessException("Exception occured while getting Organization " + cs.getString(5));
					}
					while(rs.next()){
						OrgTO to = new OrgTO();
						to.setTenantId(rs.getLong("ORG_NODEID"));
						to.setTenantName(rs.getString("ORG_NODE_NAME"));
						to.setParentTenantId(rs.getLong("PARENT_ORG_NODEID"));
						to.setOrgLevel(rs.getLong("ORG_NODE_LEVEL"));
						OrgTOList.add(to);
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				logger.log(IAppLogger.INFO, "getOrganizationDetailsOnClick().OrgTOList.size()=" + OrgTOList.size());
				logger.log(IAppLogger.INFO, "Exit: getOrganizationDetailsOnClick()");
				return OrgTOList;
			}
		});
	}

	/**
	 * Returns the organizationList to create a tree structure.
	 * 
	 * @param nodeid
	 * @return
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrganizationTree') )"),
			@Cacheable(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrganizationTree') )"),
			@Cacheable(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrganizationTree') )")
	} )
	public ArrayList<OrgTreeTO> getOrganizationTree(Map<String, Object> paramMap) throws Exception {
		final boolean isFirstLoad = (Boolean) paramMap.get("isFirstLoad");
		final String currOrg = (String) paramMap.get("currOrg");
		final String nodeId = (String) paramMap.get("nodeid");
		String adminYear = (String) paramMap.get("adminYear");
		
		ArrayList<OrgTreeTO> orgTreeTOs = new ArrayList<OrgTreeTO>();
		OrgTO orgTO = null;
		OrgTreeTO treeTO = null;
		
		if (isFirstLoad || nodeId.indexOf(".0") != -1 || nodeId.indexOf("0_1") != -1) {
			Query searchOrgQuery = new Query(Criteria.where("_id").is(currOrg));
			MOrgTO orgList = getMongoTemplatePrism().findOne(searchOrgQuery, MOrgTO.class);
			if(orgList != null) {
				orgTO = new OrgTO();
				treeTO = new OrgTreeTO();
				orgTO.setId(orgList.get_id());
				orgTO.setParentTenantId(orgList.getParent_id());
				orgTO.setOrgLevel(orgList.getLevel());
				treeTO.setState("closed");
				treeTO.setOrgTreeId(orgList.get_id());
				treeTO.setData(orgList.getName());
				treeTO.setMetadata(orgTO);
				treeTO.setAttr(orgTO);
				orgTreeTOs.add(treeTO);		
			}
		} else {
			Query searchOrgQuery = new Query(Criteria.where("parent_id").is(nodeId).and("adminCodes").in(adminYear));
			List<MOrgTO> orgList = getMongoTemplatePrism().find(searchOrgQuery, MOrgTO.class);
			if(orgList != null) {
				for(MOrgTO org : orgList) {
					orgTO = new OrgTO();
					treeTO = new OrgTreeTO();
					orgTO.setId(org.get_id());
					orgTO.setParentTenantId(org.getParent_id());
					orgTO.setOrgLevel(org.getLevel());
					treeTO.setState("closed");
					treeTO.setOrgTreeId(org.get_id());
					treeTO.setData(org.getName());
					treeTO.setMetadata(orgTO);
					treeTO.setAttr(orgTO);
					orgTreeTOs.add(treeTO);		
				}
			}
		}
		
		return orgTreeTOs;
	}
	
	@Deprecated
	// TODo remove this method after mongo conversion is done
	public ArrayList<OrgTreeTO> getOrganizationTreeOld(Map<String, Object> paramMap) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: getOrganizationTree()");
		final String nodeId = (String) paramMap.get("nodeid");
		final String currOrg = (String) paramMap.get("currOrg");
		final boolean isFirstLoad = (Boolean) paramMap.get("isFirstLoad");
		final String custProdId = (String) paramMap.get("adminYear");
		final long customerId = Long.valueOf(paramMap.get("customerId").toString());
		final String orgMode = (String) paramMap.get("orgMode");

		logger.log(IAppLogger.INFO, "nodeid=" + nodeId);
		logger.log(IAppLogger.INFO, "currOrg=" + currOrg);
		logger.log(IAppLogger.INFO, "isFirstLoad=" + isFirstLoad);
		logger.log(IAppLogger.INFO, "custProdId=" + custProdId);
		logger.log(IAppLogger.INFO, "customerId=" + customerId);
		logger.log(IAppLogger.INFO, "orgMode=" + orgMode);
	
	//	ArrayList<OrgTreeTO> lstData = new ArrayList<OrgTreeTO>();
			
		return (ArrayList<OrgTreeTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					if (isFirstLoad) {
						CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_CURR_TENANT_DETAILS);
						cs.setLong(1, Long.valueOf(nodeId));
						cs.setLong(2, customerId);
						cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					} else {
						if (nodeId.indexOf("_") > 0) {
							String orgParentId = nodeId.substring(0, nodeId.indexOf("_"));
							String orgLevel = nodeId.substring((nodeId.indexOf("_") + 1), nodeId.length());
							if (!("1".equals(orgLevel))) {
								//GET_TENANT_DETAILS_NON_ACSI
								CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_TENANT_DETAILS_NON_ACSI);
								cs.setLong(1, Long.valueOf(currOrg));
								cs.setLong(2, customerId);
								cs.setLong(3, Long.valueOf(orgParentId));
								cs.setLong(4, Long.valueOf(custProdId));
								cs.setString(5, orgMode);
								cs.registerOutParameter(6, oracle.jdbc.OracleTypes.CURSOR);
								cs.registerOutParameter(7, oracle.jdbc.OracleTypes.VARCHAR);
								return cs;
							} else {
								if(Integer.parseInt(orgParentId) == 0){
									//GET_TENANT_DETAILS
									CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_TENANT_DETAILS);
									cs.setLong(1, Long.valueOf(orgParentId));
									cs.setLong(2, customerId);
									cs.setLong(3, Long.valueOf(custProdId));
									cs.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
									cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
									return cs;
								}else{
									//GET_TENANT_DETAILS_NON_ROOT
									CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_TENANT_DETAILS_NON_ROOT);
									cs.setLong(1, Long.valueOf(orgParentId));
									cs.setLong(2, customerId);
									cs.setLong(3, Long.valueOf(custProdId));
									cs.setString(4, orgMode);
									cs.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
									cs.registerOutParameter(6, oracle.jdbc.OracleTypes.VARCHAR);
									return cs;
								}
							}
						} else {
							if(Integer.parseInt(nodeId) == 0){
								//GET_TENANT_DETAILS
								CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_TENANT_DETAILS);
								cs.setLong(1, Long.valueOf(nodeId));
								cs.setLong(2, customerId);
								cs.setLong(3, Long.valueOf(custProdId));
								cs.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
								cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
								return cs;
							} else {
								//GET_TENANT_DETAILS_NON_ROOT
								CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_TENANT_DETAILS_NON_ROOT);
								cs.setLong(1, Long.valueOf(nodeId));
								cs.setLong(2, customerId);
								cs.setLong(3, Long.valueOf(custProdId));
								cs.setString(4, orgMode);
								cs.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
								cs.registerOutParameter(6, oracle.jdbc.OracleTypes.VARCHAR);
								return cs;
							}
						}
					}
					
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<OrgTreeTO> orgTreeTOs = new ArrayList<OrgTreeTO>();
					OrgTO orgTO = null;
					OrgTreeTO treeTO = null;
					try {
						cs.execute();
						if (isFirstLoad){
							rs = (ResultSet) cs.getObject(3);
							Utils.logError(cs.getString(4));
							if(cs.getString(4) != null && cs.getString(4).length() > 0) {
								throw new BusinessException("Exception occured while getting Organization " + cs.getString(4));
							}
						} else {
							if (nodeId.indexOf("_") > 0) {
								if (!("1".equals(nodeId.substring((nodeId.indexOf("_") + 1), nodeId.length())))) {
									rs = (ResultSet) cs.getObject(6);
									Utils.logError(cs.getString(7));
									if(cs.getString(7) != null && cs.getString(7).length() > 0) {
										throw new SQLException();
									}
								} else {
									if(Integer.parseInt(nodeId.substring(0, nodeId.indexOf("_"))) == 0){
										rs = (ResultSet) cs.getObject(4);
										Utils.logError(cs.getString(5));
										if(cs.getString(5) != null && cs.getString(5).length() > 0) {
											throw new SQLException();
										}
									} else {
										rs = (ResultSet) cs.getObject(5);
										Utils.logError(cs.getString(6));
										if(cs.getString(6) != null && cs.getString(6).length() > 0) {
											throw new SQLException();
										}
									}
								}
							} else {
								if(Integer.parseInt(nodeId) == 0){
									rs = (ResultSet) cs.getObject(4);
									Utils.logError(cs.getString(5));
									if(cs.getString(5) != null && cs.getString(5).length() > 0) {
										throw new SQLException();
									}
								} else {
									rs = (ResultSet) cs.getObject(5);
									Utils.logError(cs.getString(6));
									if(cs.getString(6) != null && cs.getString(6).length() > 0) {
										throw new SQLException();
									}
								}
							}
							
						}
						
						while (rs.next()) {
							orgTO = new OrgTO();
							treeTO = new OrgTreeTO();
							orgTO.setId(rs.getLong("ORG_NODEID"));
							orgTO.setParentTenantId(rs.getLong("PARENT_ORG_NODEID"));
							orgTO.setOrgLevel(rs.getLong("ORG_NODE_LEVEL"));
							treeTO.setState("closed");
							treeTO.setOrgTreeId(rs.getLong("ORG_NODEID"));
							treeTO.setData(rs.getString("ORG_NODE_NAME"));
							treeTO.setMetadata(orgTO);
							treeTO.setAttr(orgTO);
							orgTreeTOs.add(treeTO);							
						}
						
						
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					logger.log(IAppLogger.INFO, "actionMap.size = " + orgTreeTOs.size());
					return orgTreeTOs;
				}
			});
	}

	/**
	 * Returns the organizationList to create a tree structure on redirecting from manage organizations page while clicked on the number of users .
	 * 
	 * 
	 * @param nodeid
	 * @return
	 */
	
	@Caching( cacheable = {
			@Cacheable(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrganizationTreeOnRedirect') )"),
			@Cacheable(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrganizationTreeOnRedirect') )"),
			@Cacheable(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrganizationTreeOnRedirect') )")
	} )
	public String getOrganizationTreeOnRedirect(Map<String, Object> paramMap) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: getOrganizationTreeOnRedirect()");
		final long selectedOrgId = Long.valueOf( paramMap.get("nodeid").toString());
		final long parentOrgId = Long.valueOf( paramMap.get("currOrg").toString());
		//final long userId = Long.valueOf( paramMap.get("userId").toString());
		final long customerId = Long.valueOf(paramMap.get("customerId").toString());
		final boolean isRedirected = (Boolean) paramMap.get("isRedirected");

		logger.log(IAppLogger.INFO, "selectedOrgId=" + selectedOrgId);
		logger.log(IAppLogger.INFO, "parentOrgId=" + parentOrgId);
		//logger.log(IAppLogger.INFO, "userId=" + userId);
		logger.log(IAppLogger.INFO, "customerId=" + customerId);
		logger.log(IAppLogger.INFO, "isRedirected=" + isRedirected);
		
		if (isRedirected) {
			return (String) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_ORG_HIER_ON_REDIRECT);
					cs.setLong(1, customerId);
					cs.setLong(2, selectedOrgId);
					cs.setLong(3, parentOrgId);
					//cs.setLong(4, userId);
					cs.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					StringBuilder hierarcialOrgIds = new StringBuilder();
					String cummsSeperatedId = "";
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(4);
						Utils.logError(cs.getString(5));
						if(cs.getString(5) != null && cs.getString(5).length() > 0) {
							throw new BusinessException("Exception occured while getting Organization " + cs.getString(5));
						}
						while(rs.next()){
							long hierarcialOrgId = rs.getLong("ORG_ID");
							hierarcialOrgIds.append(hierarcialOrgId).append(",");
						}
						cummsSeperatedId = hierarcialOrgIds.toString();
						cummsSeperatedId = cummsSeperatedId.substring(0, hierarcialOrgIds.length() - 1);
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					logger.log(IAppLogger.INFO, "Exit: getOrganizationTreeOnRedirect()");
					return cummsSeperatedId;
				}
			});
		} else {
			return null;
		}

		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#getOrgTree(java.util.Map)
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrgTree') )"),
			@Cacheable(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrgTree') )"),
			@Cacheable(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrgTree') )")
	} )
	public ArrayList<OrgTreeTO> getOrgTree(Map<String, Object> paramMap) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: getOrgTree()");
		
		final String nodeId = paramMap.get("nodeid").toString();
		final boolean isFirstLoad = (Boolean) paramMap.get("isFirstLoad");
		final String adminYear =  paramMap.get("adminYear").toString();
		final String customerId = paramMap.get("customerId").toString();
		final String orgMode = (String) paramMap.get("orgMode");

		Query searchOrgQuery = new Query(Criteria.where("_id").regex("^"+nodeId).and("project_id").is(Utils.getProject()).and("adminCodes").in(adminYear));
		List<MOrgTO> orgList = getMongoTemplatePrism().find(searchOrgQuery, MOrgTO.class);
		
		ArrayList<OrgTreeTO> orgTreeTOs = new ArrayList<OrgTreeTO>();
		OrgTO orgTO = null;
		OrgTreeTO treeTO = null;
		if(orgList != null) {
			for(MOrgTO org : orgList) {
				orgTO = new OrgTO();
				treeTO = new OrgTreeTO();
				orgTO.setId(org.get_id());
				orgTO.setParentTenantId(org.getParent_id());
				orgTO.setOrgLevel(org.getLevel());
				treeTO.setState("closed");
				treeTO.setOrgTreeId(org.get_id());
				treeTO.setData(org.getName());
				treeTO.setMetadata(orgTO);
				treeTO.setAttr(orgTO);
				orgTreeTOs.add(treeTO);		
			}
		}
		return orgTreeTOs;
	}

	/*
	 * Added contract
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#getUserDetailsOnClick(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Caching( cacheable = {
		@Cacheable(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramUserMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramUserMap)).concat(#root.method.name))"),
		@Cacheable(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramUserMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramUserMap)).concat(#root.method.name))"),
		@Cacheable(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramUserMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramUserMap)).concat(#root.method.name))")
	} )
	public ArrayList<UserTO> getUserDetailsOnClick(Map<String,Object> paramUserMap) throws Exception{
		String nodeId = (String)paramUserMap.get("NODEID");
		String currorg = (String)paramUserMap.get("CURRENTORG");
		String adminYear = (String)paramUserMap.get("ADMINYEAR");
		String searchParam = (String)paramUserMap.get("SEARCHPARAM");
		String customerId = (String)paramUserMap.get("CUSTOMERID");
		String orgMode = (String)paramUserMap.get("ORGMODE");
		int moreCount = Integer.valueOf( (String)paramUserMap.get("moreCount") );
		int page = Integer.valueOf( (String)paramUserMap.get("page") );
				
		logger.log(IAppLogger.INFO, "Enter: getUserDetailsOnClick()");
		logger.log(IAppLogger.INFO, "nodeId=" + nodeId );
		logger.log(IAppLogger.INFO, "currorg=" + currorg );
		logger.log(IAppLogger.INFO, "adminYear=" + adminYear);
		logger.log(IAppLogger.INFO, "searchParam=" + searchParam);
		logger.log(IAppLogger.INFO, "customerId=" + customerId);
		logger.log(IAppLogger.INFO, "orgMode=" + orgMode);
				
		List<UserTO> userList = null;
		
		Query searchUserQuery = null;
		if (nodeId.indexOf("0_1.0") != -1) {
			nodeId = currorg;
		}
		if (searchParam != null && searchParam.length() > 0) {
			searchUserQuery = new Query(Criteria.where("orgUser.org_id").regex("^"+nodeId).and("project_id").is(Utils.getProject()).orOperator(
					Criteria.where("_id").regex(searchParam),
					Criteria.where("lastName").regex(searchParam), 
					Criteria.where("firstName").regex(searchParam), 
					Criteria.where("orgUser.org_id").regex(searchParam))
					);
		} else {
			searchUserQuery = new Query(Criteria.where("orgUser.org_id").regex("^"+nodeId).and("project_id").is(Utils.getProject()));
		}
		long userCount = getMongoTemplatePrism().count(searchUserQuery, MUserTO.class);
		List<MUserTO> savedUser = getMongoTemplatePrism().find(searchUserQuery.skip(moreCount*(page-1)).limit(moreCount), MUserTO.class);
		userList = getUserList(savedUser, currorg, userCount);
		
		logger.log(IAppLogger.INFO, "Users: " + userList.size());
		logger.log(IAppLogger.INFO, "Exit: getUserDetailsOnClick()");
		return new ArrayList<UserTO>(userList);
	}
	
	private List<UserTO> getUserList(MUserTO savedUser, String currorg) {
		List<MUserTO> userList = new ArrayList<MUserTO>();
		userList.add(savedUser);
		return getUserList(userList, currorg, 1);
	}
	
	private List<UserTO> getUserList(List<MUserTO> savedUser, String currorg, long userCount) {
		List<UserTO> userList = null;
		if( savedUser != null ) {
			userList = new ArrayList<UserTO>();
			for(MUserTO user : savedUser) {
				UserTO to = new UserTO();
				to.setUserId(user.get_id());
				
				List<RoleTO> roleList = new ArrayList<RoleTO>();
				for(String role : user.getUserRoles()) {
					RoleTO roleTO = new RoleTO();
					roleTO.setRoleName(role);
					if(role.indexOf("ADMIN") != -1) {
						roleTO.setRoleDescription("Admin User");
					} else {
						roleTO.setRoleDescription("User");
					}
					roleTO.setLabel(user.getOrgCategory().getCategory());
					roleList.add(roleTO);
				}
				to.setAvailableRoleList(roleList);
				to.setUserName(user.get_id());
				to.setUserDisplayName(user.getDisplayName());
				to.setStatus("AC");
				to.setTenantId(user.getOrgUser()[0].getOrg_id()); // assuming org user is associated with one org
				//to.setParentId(0);
				to.setLoggedInOrgId(currorg);
				to.setTenantName(user.getOrgUser()[0].getOrgName());
				to.setFirstName(user.getFirstName());
				to.setLastName(user.getLastName());
				to.setMiddleName(user.getMiddleName());
				to.setUserCount(userCount);
				userList.add(to);
			}
		}
		return userList;
	}
	
	private List<RoleTO> getRoleList(final Long userId){
		return (List<RoleTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_USER_ROLE);
				cs.setLong(1, userId);
				cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
				cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				ResultSet rs = null;
				List<RoleTO> roleList = new ArrayList<RoleTO>();
				try {
					cs.execute();
					rs = (ResultSet) cs.getObject(2);
					Utils.logError(cs.getString(3));
					if(cs.getString(3) != null && cs.getString(3).length() > 0) {
						throw new BusinessException("Exception occured while getting Organization " + cs.getString(3));
					}
					while(rs.next()){
						RoleTO to = new RoleTO();
						to.setRoleId(Long.parseLong(rs.getString("ROLEID")));
						to.setRoleName(rs.getString("ROLE_NAME"));
						to.setRoleDescription(rs.getString("DESCRIPTION"));
						to.setLabel(rs.getString("ORG_LABEL"));
						roleList.add(to);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				logger.log(IAppLogger.INFO, "getRoleList().roleList.size()=" + roleList.size());
				return roleList;
			}
		});
	}
	
	private List<UserTO> getUserListFromResultSet(String currorg, ResultSet rs) throws SQLException {
		List<UserTO> userList = new ArrayList<UserTO>();
		while (rs.next()) {
			UserTO to = new UserTO();
			to.setUserId(Long.parseLong(rs.getString("USER_ID")));
			List<RoleTO> roleList = getRoleList(Long.parseLong(rs.getString("USER_ID")));
			if (!roleList.isEmpty()) {
				to.setAvailableRoleList(roleList);
			}
			to.setUserName(rs.getString("USERNAME"));
			if (rs.getString("FULLNAME") != null) {
				to.setUserDisplayName(rs.getString("FULLNAME"));
			} else {
				to.setUserDisplayName("");
			}
			to.setStatus(rs.getString("STATUS"));
			to.setTenantId(Long.parseLong(rs.getString("ORG_PARENT_ID")));
			to.setParentId(Long.parseLong(rs.getString("ORG_PARENT_ID")));
			try {
				to.setLoggedInOrgId(Long.parseLong(currorg));
			} catch (NumberFormatException e) {
				logger.log(IAppLogger.WARN, "Invalid number: " + currorg);
			}
			to.setTenantName(rs.getString("ORG_NAME"));
			userList.add(to);
		}
		return userList;
	} 
	
	private List<UserTO> getUserDetailsOnScrollWithSrchParam(final String currorg, final String customerId, final String orgMode,
			final String tenantId, final Long roleId, final String custProdId, final String userName, final String searchParam,
			final String moreCount) throws Exception {
		
		return (List<UserTO>) getJdbcTemplatePrism().execute(
				new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_USERS_ONSCROLL_WITH_SP);
						cs.setLong(1, Long.parseLong(customerId));
						cs.setString(2, orgMode);
						cs.setLong(3, Long.parseLong(tenantId));
						cs.setLong(4, roleId);
						cs.setLong(5, Long.parseLong(custProdId));
						cs.setString(6, userName);
						cs.setString(7, searchParam);
						cs.setLong(8, Long.parseLong(moreCount));
						cs.registerOutParameter(9, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(10, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						ResultSet rs = null;
						List<UserTO> userList = new ArrayList<UserTO>();
						try {
							cs.execute();
							rs = (ResultSet) cs.getObject(9);
							Utils.logError(cs.getString(10));
							if(cs.getString(10) != null && cs.getString(10).length() > 0) {
								throw new BusinessException("Exception occured while getting Organization " + cs.getString(10));
							}
							userList = getUserListFromResultSet(currorg, rs);
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						logger.log(IAppLogger.INFO, "getUserDetailsOnScrollWithSrchParam().userList.size()=" + userList.size());
						return userList;
					}
				}
			);
	}
	
	private List<UserTO> getUserDetailsOnScroll(final String currorg, final String customerId, final String orgMode,
			final String tenantId, final Long roleId, final String custProdId, final String userName, final String moreCount) throws Exception {
		
		return (List<UserTO>) getJdbcTemplatePrism().execute(
				new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_USERS_ONSCROLL);
						cs.setLong(1, Long.parseLong(customerId));
						cs.setString(2, orgMode);
						cs.setLong(3, Long.parseLong(tenantId));
						cs.setLong(4, roleId);
						cs.setLong(5, Long.parseLong(custProdId));
						cs.setString(6, userName);
						cs.setLong(7, Long.parseLong(moreCount));
						cs.registerOutParameter(8, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(9, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						ResultSet rs = null;
						List<UserTO> userList = new ArrayList<UserTO>();
						try {
							cs.execute();
							rs = (ResultSet) cs.getObject(8);
							Utils.logError(cs.getString(9));
							if(cs.getString(9) != null && cs.getString(9).length() > 0) {
								throw new BusinessException("Exception occured while getting Organization " + cs.getString(9));
							}
							userList = getUserListFromResultSet(currorg, rs);
							Utils.logError(cs.getString(9));
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						logger.log(IAppLogger.INFO, "getUserDetailsOnScroll().userList.size()=" + userList.size());
						return userList;
					}
				}
			);
	}
	
	private List<UserTO> getUserDetailsOnFirstLoad(final String currorg, final String customerId, final String orgMode,
			final String tenantId, final Long roleId, final String custProdId, final String moreCount) throws Exception {
		
		Query searchUserQuery = new Query(Criteria.where("customerCode").is(customerId).and("orgUser.org_id").regex("^"+currorg).and("Project_id").is(Utils.getProject()));
		List<MUserTO> users = getMongoTemplatePrism().find(searchUserQuery, MUserTO.class);
		long userCount = getMongoTemplatePrism().count(searchUserQuery, MUserTO.class);

		List<UserTO> userList = getUserList(users, currorg, userCount);
		
		return userList;
	}
	
	/**
	 * Returns the userTO on Edit.
	 * 
	 * @param userId
	 * @return
	 */
	public UserTO getEditUserData(Map<String, Object> paramMap) throws Exception{
		final String username = (String) paramMap.get("userId");
		final String customerId = (String) paramMap.get("customer");
		final String project = (String) paramMap.get("project");
		final String currorg = (String) paramMap.get("loggedInOrgId");
		final String purpose = "edit";
		
		Query searchUserQuery = new Query(Criteria.where("_id").is(username).and("project_id").is(project));
		MUserTO savedUser = getMongoTemplatePrism().findOne(searchUserQuery, MUserTO.class);
		List<UserTO> userList = getUserList(savedUser, currorg);
		return userList.get(0); // as we are fetching exactly one user
	}

	/**
	 * Returns the RoleTO on Add.
	 * 
	 * @param userId
	 * @return
	 */
	public List<RoleTO> getRoleOnAddUser(String orgLevel, String customerId) throws Exception{
		String purpose = "add";
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("contractName", Utils.getContractName());
		paramMap.put("argType", "org");
		paramMap.put("userid", orgLevel); //why org level?
		paramMap.put("customerId", customerId);
		paramMap.put("purpose", purpose);
		return getMasterRoleList(paramMap);
		//return getMasterRoleList("org", orgLevel, customerId,purpose);
	}

	/**
	 * Returns the masterRoleTO on Edit/Add.
	 * 
	 * @param argType
	 *            ,userId
	 * @return
	 */
	//@Cacheable(value = {"inorsConfigCache","tascConfigCache"}, key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #root.method.name )")
	//private List<RoleTO> getMasterRoleList(String argType, final String userid, String customerId,String purpose) throws SQLException {
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#userParamMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#userParamMap)).concat('getMasterRoleList'))"),
			@Cacheable(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#userParamMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#userParamMap)).concat('getMasterRoleList'))"),
			@Cacheable(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#userParamMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#userParamMap)).concat('getMasterRoleList'))")
	} )
	private List<RoleTO> getMasterRoleList(Map<String,Object> userParamMap) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>(); 
		paramMap.put("contractName", Utils.getContractName());
		//Map<String, Object> propertyMap = loginDAO.getContractProerty(paramMap);
		//String roleNotAdded = (String)propertyMap.get(IApplicationConstants.ROLE_NOT_ADDED);
		
		Query searchUserQuery = new Query(Criteria.where("_id").is(Utils.getProject()));
		MCustProdAdminTO custProdAdmin = getMongoTemplatePrism("global").findOne(searchUserQuery, MCustProdAdminTO.class);
		String[] roleNotAdded = custProdAdmin.getUiConfig().getRoleNotAdded();
		
		List<RoleTO> roleTos = new ArrayList<RoleTO>();
		if(custProdAdmin != null) {
			Roles[] roles = custProdAdmin.getRoles();
			for(Roles role : roles) {
				if(Arrays.asList(roleNotAdded).contains(role.getName())) {
					continue;
				} else {
					RoleTO roleTo = new RoleTO();
					roleTo.setRoleName(role.getName());
					roleTo.setRoleDescription(role.getDescription());
					roleTos.add(roleTo);
				}
			}
		}
		return roleTos;
	}
	
	private List<RoleTO> getMasterRoleListOld(Map<String,Object> userParamMap) throws SQLException {
		final String argType = (String) userParamMap.get("argType");
		final String userid = (String) userParamMap.get("userid");
		final String customerId = (String) userParamMap.get("customerId");
		final String purpose = (String) userParamMap.get("purpose");
		final String contractName = (String) userParamMap.get("contractName");
		logger.log(IAppLogger.INFO, "argType=" + argType);
		logger.log(IAppLogger.INFO, "userid=" + userid);
		logger.log(IAppLogger.INFO, "customerId=" + customerId);
		RoleTO masterRoleTO = null;
		List<RoleTO> masterRoleList = new ArrayList<RoleTO>();
		List<RoleTO> lstMasterRoleData = null;
		long user_org_level = -1;
		ResourceBundle b = ResourceBundle.getBundle("messages");
		if ("user".equals(argType)) {
			try {
				user_org_level = getJdbcTemplatePrism().queryForObject(IQueryConstants.GET_USER_ORG_LEVEL, Long.class, userid);
			} catch (DataAccessException e) {
				logger.log(IAppLogger.INFO, "No data found for userid: " + userid);
			}
			logger.log(IAppLogger.DEBUG, "user_org_level" + user_org_level);
		} else if ("org".equals(argType)) {
			user_org_level = Long.valueOf(userid);
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>(); 
		paramMap.put("contractName", contractName);
		Map<String, Object> propertyMap = loginDAO.getContractProerty(paramMap);
		
		final String roleNotAdded = (String)propertyMap.get(IApplicationConstants.ROLE_NOT_ADDED);
			
		if("add".equals(purpose)){					
			lstMasterRoleData = (List<RoleTO>) getJdbcTemplatePrism().execute(
					new CallableStatementCreator() {
						public CallableStatement createCallableStatement(Connection con) throws SQLException {
							CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_ROLE_ADD);
							cs.setString(1, roleNotAdded);
							cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
							cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
							return cs;
						}
					}, new CallableStatementCallback<Object>() {
						public Object doInCallableStatement(CallableStatement cs) {
							ResultSet rs = null;
							List<RoleTO> roleList = new ArrayList<RoleTO>();
							try {
								cs.execute();
								rs = (ResultSet) cs.getObject(2);
								Utils.logError(cs.getString(3));
								if(cs.getString(3) != null && cs.getString(3).length() > 0) {
									throw new BusinessException("Exception occured while getting Organization " + cs.getString(3));
								}
								while(rs.next()){
									RoleTO to = new RoleTO();
									to.setRoleId(Long.parseLong(rs.getString("ROLEID")));
									to.setRoleName(rs.getString("ROLE_NAME"));
									to.setRoleDescription(rs.getString("DESCRIPTION"));
									roleList.add(to);
								}
								
							} catch (SQLException e) {
								e.printStackTrace();
							} catch (BusinessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							logger.log(IAppLogger.INFO, "getMasterRoleList().roleList.size()=" + roleList.size());
							return roleList;
						}
					}
				);
			
		}else{
			lstMasterRoleData = (List<RoleTO>) getJdbcTemplatePrism().execute(
					new CallableStatementCreator() {
						public CallableStatement createCallableStatement(Connection con) throws SQLException {
							CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_ROLE_USER);
							cs.setString(1, roleNotAdded);
							cs.setLong(2, Long.valueOf(userid));
							cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
							cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
							return cs;
						}
					}, new CallableStatementCallback<Object>() {
						public Object doInCallableStatement(CallableStatement cs) {
							ResultSet rs = null;
							List<RoleTO> roleList = new ArrayList<RoleTO>();
							try {
								cs.execute();
								rs = (ResultSet) cs.getObject(3);
								Utils.logError(cs.getString(4));
								if(cs.getString(4) != null && cs.getString(4).length() > 0) {
									throw new BusinessException("Exception occured while getting Organization " + cs.getString(4));
								}
								while(rs.next()){
									RoleTO to = new RoleTO();
									to.setRoleId(Long.parseLong(rs.getString("ROLEID")));
									to.setRoleName(rs.getString("ROLE_NAME"));
									to.setRoleDescription(rs.getString("DESCRIPTION"));
									roleList.add(to);
								}
							
							} catch (SQLException e) {
								e.printStackTrace();
							} catch (BusinessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							logger.log(IAppLogger.INFO, "getMasterRoleList().roleList.size()=" + roleList.size());
							return roleList;
						}
					}
				);			
		}		
		
		for (RoleTO roles : lstMasterRoleData) {
			masterRoleTO = new RoleTO();
			String roleName = roles.getRoleName();
			masterRoleTO.setRoleName(roles.getRoleName());
			masterRoleTO.setRoleId(roles.getRoleId());
			masterRoleTO.setRoleDescription(roles.getRoleDescription());
			
			if (propertyMap.get(IApplicationConstants.ORGLVL_USER_NOT_ADDED) != null && 
					user_org_level != Long.valueOf((String)propertyMap.get(IApplicationConstants.ORGLVL_USER_NOT_ADDED))) {
				
				if (IApplicationConstants.ROLE_TYPE.ROLE_USER.toString().equals(roleName)) {
					masterRoleTO.setDefaultSelection("selected");
				} else if (IApplicationConstants.ROLE_TYPE.ROLE_ADMIN.toString().equals(roleName) && "user".equals(argType)) {
					masterRoleTO.setDefaultSelection("selected");
				} else {
					masterRoleTO.setDefaultSelection("");
				}
			}
			masterRoleList.add(masterRoleTO);
		}
		return masterRoleList;
	}

	/**
	 * Returns boolean.
	 * 
	 * @param Id
	 *            userId, userName, emailId, password, userStatus,userRoles
	 * @return
	 */
	/*@Caching( evict = { 
			@CacheEvict(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", allEntries = true),
			@CacheEvict(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  allEntries = true),
			@CacheEvict(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  allEntries = true)
	} )*/
	
	/*@Caching( put = {
			@CachePut(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#searchParamMap)).concat('updateUser'))"),
			@CachePut(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#searchParamMap)).concat('updateUser'))"),
			@CachePut(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#searchParamMap)).concat('updateUser'))")
	} )*/
	
	@Caching( put = {
			@CachePut(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'"),
			@CachePut(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'"),
			@CachePut(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'")
	} )
	
	public boolean updateUser(Map<String, Object> paramMap) throws BusinessException, Exception {
		logger.log(IAppLogger.INFO, "Enter: updateUser()");
		
		String Id = (String)paramMap.get("Id");
		String userId = (String)paramMap.get("userId");
		String displayName = (String)paramMap.get("userName");
		String emailId = (String)paramMap.get("emailId");
		String password = (String)paramMap.get("password");
		String userStatus = (String)paramMap.get("userStatus");
		String[] userRoles = (String[])paramMap.get("userRoles");
		String salt = (String)paramMap.get("salt");
		String project = (String)paramMap.get("project");
		// get the user which needs modification
		Query searchUserQuery = new Query(Criteria.where("_id").is(userId).and("project_id").is(project));
		MUserTO savedUser = getMongoTemplatePrism().findOne(searchUserQuery, MUserTO.class);
		
		try {
			if(savedUser != null) {
				if (password != null && !"".equals(password)) {
	
					// Check if password contains part of user name
					if (password.equalsIgnoreCase(userId) || password.toLowerCase().indexOf(userId.toLowerCase()) != -1 || userId.toLowerCase().indexOf(password.toLowerCase()) != -1) {
						throw new BusinessException(propertyLookup.get("script.user.passwordPartUsername"));
					}
					
					// update parameters
					if(salt == null) salt = PasswordGenerator.getNextSalt();
					savedUser.setSalt(salt);
					savedUser.setPassword(password);
				}
				savedUser.setEmail(emailId);
				savedUser.setDisplayName(displayName);
				savedUser.setStatus( userStatus );
				savedUser.setUserRoles(userRoles);
				//save
				getMongoTemplatePrism().save(savedUser);
				
				return true;
			} else {
				return false;
			}

		} catch (BusinessException bex) {
			throw new BusinessException(bex.getCustomExceptionMessage());
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while updating user details.", e);
			throw new Exception(e);
		}
	}

	/**
	 * Returns boolean.
	 * 
	 * @param Id
	 *            userId, userName, password
	 * @return
	 */

	@Caching( evict = { 
			@CacheEvict(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", allEntries = true),
			@CacheEvict(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  allEntries = true),
			@CacheEvict(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  allEntries = true)
	} )
	public boolean deleteUser(Map<String, Object> paramMap) /*throws Exception*/ {
		logger.log(IAppLogger.INFO, "Enter: deleteUser()");
		final String Id = (String) paramMap.get("Id");
		final String userName = (String) paramMap.get("userName");
		final String purpose = (String) paramMap.get("purpose");
		final String project = (String) paramMap.get("project");
		
		Query searchUserQuery = new Query(Criteria.where("_id").is(userName).and("project_id").is(project));
		MUserTO savedUser = getMongoTemplatePrism().findAndRemove(searchUserQuery, MUserTO.class);
		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#addNewUser(java.util.Map)
	 */
	@Caching( evict = { 
			@CacheEvict(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", allEntries = true),
			@CacheEvict(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  allEntries = true),
			@CacheEvict(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  allEntries = true)
	} )
	
	/*@Caching( put = {
			@CachePut(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#searchParamMap)).concat('getUserDetailsOnClick'))"),
			@CachePut(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#searchParamMap)).concat('getUserDetailsOnClick'))"),
			@CachePut(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#searchParamMap)).concat('getUserDetailsOnClick'))")
	} )*/
	
	public UserTO addNewUser(Map<String, Object> paramMap) throws Exception{
		logger.log(IAppLogger.INFO, "Enter: addNewUser(): " + paramMap);
		UserTO userTo = null;
		String userName = (String) paramMap.get("userName");
		String password = (String) paramMap.get("password");
		String userDisplayName = (String) paramMap.get("userDisplayName");
		String emailId = (String) paramMap.get("emailId");
		String userStatus = (String) paramMap.get("userStatus");
		String customerId = (String) paramMap.get("customer");
		String tenantId = (String) paramMap.get("tenantId");
		String orgLevel = (String) paramMap.get("orgLevel");
		String adminYear = (String) paramMap.get("adminYear");
		String purpose = (String) paramMap.get("purpose");
		String eduCenterId = (String) paramMap.get("eduCenterId");
		String contractName = (String) paramMap.get("contractName");
		String project = (String) paramMap.get("project");
		//String orgMode = (String) paramMap.get("orgMode");
		//String orgName = (String) paramMap.get("orgName");
		
		String[] userRoles = (String[]) paramMap.get("userRoles");
		
		
		try {
			Query searchOrgQuery = new Query(Criteria.where("_id").is(tenantId).and("project_id").is(Utils.getProject()));
			MOrgTO orgList = getMongoTemplatePrism().findOne(searchOrgQuery, MOrgTO.class);
			String orgMode = orgList.getCategory();
			String orgName = orgList.getName();
			
			String salt = PasswordGenerator.getNextSalt();
			
			MUserTO user = new MUserTO();
			user.set_id(userName);
			user.setDisplayName(userDisplayName);
			user.setLastName("");
			user.setFirstName("");
			user.setMiddleName("");
			user.setPassword( SaltedPasswordEncoder.encryptPassword(password, Utils.getSaltWithUser(userName, salt)) );
			user.setSalt(salt);
			user.setCustomerCode(customerId);
			user.setProject_id(project);
			user.setUserRoles(userRoles);
				OrgCategory OrgCategory = new OrgCategory();
				OrgCategory.setCategory(orgMode);
				OrgCategory.setLevel( (orgLevel.indexOf(".") != -1) ? orgLevel.substring(0, (orgLevel.indexOf("."))) : orgLevel );
			user.setOrgCategory(OrgCategory);
				OrgUser[] orgUserArr = new OrgUser[1];
				OrgUser orgUser = new OrgUser();
				orgUser.setOrg_id(tenantId);
				orgUser.setIsActive( IApplicationConstants.ACTIVE_FLAG.equals(userStatus) ? IApplicationConstants.FLAG_Y : IApplicationConstants.FLAG_N );
				orgUser.setOrgName(orgName);	
				orgUserArr[0] = orgUser;
			user.setOrgUser(orgUserArr);
			user.setUserType(IApplicationConstants.ORG_USER_FLAG);
			user.setIsFirstTimeLogin(IApplicationConstants.FLAG_Y);
			user.setIsNewUser(IApplicationConstants.FLAG_Y);
			user.setSignedUserAgrmt("");
			user.setIsAutoGeneratedUser(IApplicationConstants.FLAG_N);
			user.setEmail(emailId);
			user.setPhoneNum("");
			user.setStatus(userStatus);
			user.setCreatedDate(new Date());
			
			getMongoTemplatePrism().save(user);
			
			userTo = new UserTO();
			userTo.setUserName(userName);
			logger.log(IAppLogger.INFO, "User added : " + userTo.getUserName());
		
		} catch (Exception bex) {
			throw new Exception(bex);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: addNewUser()");
		}

		return userTo;
	}

	/**
	 * Searches and returns the users(s) with given name (like operator). Performs case insensitive searching.
	 * 
	 * @param userName
	 *            Search String treated as organization name
	 * @param tenantId
	 *            parentId of the logged in user
	 * @return
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #root.method.name )"),
			@Cacheable(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #root.method.name )"),
			@Cacheable(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #root.method.name )")
	} )
	public ArrayList<UserTO> searchUser(Map<String, Object> paramMap) {
		String userName = (String) paramMap.get("userName");
		String tenantId = (String) paramMap.get("tenantId");
		String adminYear = (String) paramMap.get("adminYear");
		String isExactSearch = (String) paramMap.get("isExactSearch");
		String orgMode = (String) paramMap.get("orgMode");
		int moreCount = Integer.parseInt( (String) paramMap.get("moreCount") );
		List<UserTO> userList = null;
		Query searchUserQuery = null;
		if (IApplicationConstants.FLAG_N.equalsIgnoreCase(isExactSearch)) {
			searchUserQuery = new Query(Criteria.where("_id").exists(true).and("orgUser.org_id").regex("^"+tenantId).orOperator(
					Criteria.where("_id").regex(userName),
					Criteria.where("lastName").regex(userName), 
					Criteria.where("firstName").regex(userName), 
					Criteria.where("orgUser.org_id").regex(userName))
					);
		} else {
			searchUserQuery = new Query(Criteria.where("_id").is(userName));
		}
		
		List<MUserTO> savedUser = getMongoTemplatePrism().find(searchUserQuery.limit(moreCount), MUserTO.class);
		long userCount = getMongoTemplatePrism().count(searchUserQuery, MUserTO.class);
		userList = getUserList(savedUser, tenantId, userCount);
		
		return new ArrayList<UserTO>(userList);
	}
	
	public ArrayList<UserTO> searchUser(String userName, String tenantId, String adminYear, String isExactSearch, String orgMode) {
		List<UserTO> userList = null;
		Query searchUserQuery = null;
		if (IApplicationConstants.FLAG_N.equalsIgnoreCase(isExactSearch)) {
			searchUserQuery = new Query(Criteria.where("_id").exists(true).and("orgUser.org_id").regex("^"+tenantId).orOperator(
					Criteria.where("_id").regex(userName),
					Criteria.where("lastName").regex(userName), 
					Criteria.where("firstName").regex(userName), 
					Criteria.where("orgUser.org_id").regex(userName))
					);
		} else {
			searchUserQuery = new Query(Criteria.where("_id").is(userName));
		}
		
		List<MUserTO> savedUser = getMongoTemplatePrism().find(searchUserQuery.limit(1), MUserTO.class);
		long userCount = getMongoTemplatePrism().count(searchUserQuery, MUserTO.class);
		userList = getUserList(savedUser, tenantId, userCount);
		
		return new ArrayList<UserTO>(userList);
	}

	/**
	 * Searches and returns the education users(s) with given name (like operator). Performs case insensitive searching.
	 * 
	 * @param userName
	 *            Search String treated as organization name
	 * @param tenantId
	 *            parentId of the logged in user
	 * @return
	 */
	public List<EduCenterTO> searchEduUser(Map<String, Object> paramMap) {
		String userName = (String) paramMap.get("userName");
		String tenantId = (String) paramMap.get("tenantId");
		String isExactSearch = (String) paramMap.get("isExactSearch");
		List<EduCenterTO> eduCenterTOList = null;
		@SuppressWarnings("rawtypes")
		List placeHolderValueList = new ArrayList();
		if (tenantId != null && !"null".equals(tenantId)) {
			if (IApplicationConstants.FLAG_N.equalsIgnoreCase(isExactSearch)) {
				userName = CustomStringUtil.appendString("%", userName, "%");
				placeHolderValueList.add(userName);
				placeHolderValueList.add(userName);
				placeHolderValueList.add(userName);
				placeHolderValueList.add(tenantId);
				placeHolderValueList.add(15);
				eduCenterTOList = getJdbcTemplatePrism().query(IQueryConstants.SEARCH_EDU_USER, placeHolderValueList.toArray(), new EduCenterTOMapper(getJdbcTemplatePrism()));
			} else {
				placeHolderValueList.add(userName);
				placeHolderValueList.add(userName);
				placeHolderValueList.add(userName);
				placeHolderValueList.add(tenantId);
				placeHolderValueList.add(15);
				eduCenterTOList = getJdbcTemplatePrism().query(IQueryConstants.SEARCH_EDU_USER, placeHolderValueList.toArray(), new EduCenterTOMapper(getJdbcTemplatePrism()));
			}
		}
		return eduCenterTOList;
	}

	/**
	 * Searches and returns the user names(use like operator) as a JSON string. Performs case insensitive searching. This method is used to perform auto complete in search box.
	 * 
	 * @param userName
	 *            Search String treated as organization name
	 * @param parentId
	 *            parentId of the logged in user
	 */
	public String searchUserAutoComplete(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: searchUserAutoComplete()");
		String userName = (String) paramMap.get("term");
		String tenantId = (String) paramMap.get("selectedOrg");
		String adminYear = (String) paramMap.get("adminYear");
		String orgMode = (String) paramMap.get("orgMode");
		String moreCount = (String) paramMap.get("moreCount");
		String purpose = (String)paramMap.get("purpose");
		logger.log(IAppLogger.INFO, "userName = " + userName);
		logger.log(IAppLogger.INFO, "tenantId = " + tenantId);
		logger.log(IAppLogger.INFO, "adminYear = " + adminYear);
		logger.log(IAppLogger.INFO, "orgMode = " + orgMode);
		logger.log(IAppLogger.INFO, "moreCount = " + moreCount);
		logger.log(IAppLogger.INFO, "purpose = " + purpose);
		//userName = CustomStringUtil.appendString("%", userName, "%");
		String userListJsonString = null;
		
		List<UserTO> userList = null;
		Query searchUserQuery = new Query(Criteria.where("_id").exists(true).and("orgUser.org_id").regex("^"+tenantId).orOperator(
				Criteria.where("_id").regex(userName),
				Criteria.where("lastName").regex(userName), 
				Criteria.where("firstName").regex(userName), 
				Criteria.where("displayName").regex(userName), 
				Criteria.where("orgUser.org_id").regex(userName))
				);
		
		List<MUserTO> savedUser = getMongoTemplatePrism().find(searchUserQuery, MUserTO.class);
		long userCount = getMongoTemplatePrism().count(searchUserQuery, MUserTO.class);
		userList = getUserList(savedUser, tenantId, userCount);
		
		if (userList != null && userList.size() > 0) {
			userListJsonString = "[";
			for(UserTO user : userList) {
				userListJsonString = CustomStringUtil.appendString(userListJsonString, "\"", user.getUserId(), "<br/>", 
						(user.getLastName() == null)? "" : (user.getLastName() + ", "), 
						(user.getFirstName() == null)? "" : (user.getFirstName() + " "), 
						(user.getMiddleName() == null)? "" : user.getMiddleName(), 
						"\",");
			}
			userListJsonString = CustomStringUtil.appendString(userListJsonString.substring(0, userListJsonString.length() - 1), "]");
		}
		
		logger.log(IAppLogger.DEBUG, userListJsonString);
		logger.log(IAppLogger.INFO, "Exit: searchUserAutoComplete()");
		return userListJsonString;
	}

	/**
	 * Returns all organization list depending on tenantId
	 * 
	 * @param String
	 *            tenantId
	 * @return list of orgTO
	 */
	public List<OrgTO> getOrganizationList(String tenantId, String adminYear, long customerId) {
		logger.log(IAppLogger.INFO, "Enter: getOrganizationList()");
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ORGANIZATION_LIST, tenantId, tenantId, adminYear, customerId, tenantId, adminYear, customerId);
		List<OrgTO> orgList = getOrgList(lstData, adminYear);
		logger.log(IAppLogger.INFO, "Exit: getOrganizationList()");
		return orgList;
	}

	/**
	 * Returns all the children of an organization.
	 * 
	 * @param String
	 *            tenantId
	 * @return list of orgTO
	 */
	// @Cacheable(value = {"inorsDefaultCache", "tascDefaultCache"}, key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #root.method.name )")
	public List<OrgTO> getOrganizationChildren(String nodeId, String adminYear, String searchParam, String customerId, String orgMode, String moreCount) {
		logger.log(IAppLogger.INFO, "Enter: getOrganizationChildren()");
		logger.log(IAppLogger.INFO, "orgMode=" + orgMode);
		logger.log(IAppLogger.INFO, "searchParam = " + searchParam);
		logger.log(IAppLogger.INFO, "customerId = " + customerId);
		logger.log(IAppLogger.INFO, "adminYear = " + adminYear);
		logger.log(IAppLogger.INFO, "adminYear = " + adminYear);
		logger.log(IAppLogger.INFO, "moreCount = " + moreCount);
		String parentTenantId = "";
		String orgId = "";
		
		
		ArrayList<OrgTO> orgTreeTOs = new ArrayList<OrgTO>();
		OrgTO orgTO = null;
		
		if (nodeId.indexOf("0_1.0") != -1) {
			Query searchOrgQuery = new Query(Criteria.where("_id").is(nodeId).and("project_id").is(Utils.getProject()));
			MOrgTO orgList = getMongoTemplatePrism().findOne(searchOrgQuery, MOrgTO.class);
			if(orgList != null) {
				orgTO = new OrgTO();
				orgTO.setId(orgList.get_id());
				orgTO.setOrgLevel(orgList.getLevel());
				orgTO.setTenantId(orgList.get_id());
				orgTO.setTenantName(orgList.getName());
				orgTO.setNoOfChildOrgs(getChildOrgsCount(orgList.get_id(),adminYear)); // need to correct
				orgTO.setParentTenantId(orgList.getParent_id());
				orgTO.setSelectedOrgId(nodeId);
				
				orgTreeTOs.add(orgTO);		
			}
		} else {
			Query searchOrgQuery = new Query(Criteria.where("parent_id").is(nodeId).and("project_id").is(Utils.getProject()).and("adminCodes").in(adminYear));
			List<MOrgTO> orgList = getMongoTemplatePrism().find(searchOrgQuery, MOrgTO.class);
			if(orgList != null) {
				for(MOrgTO org : orgList) {
					orgTO = new OrgTO();
					orgTO.setId(org.get_id());
					orgTO.setOrgLevel(org.getLevel());
					orgTO.setTenantId(org.get_id());
					orgTO.setTenantName(org.getName());
					orgTO.setNoOfChildOrgs(getChildOrgsCount(org.get_id(),adminYear)); // need to correct
					orgTO.setParentTenantId(org.getParent_id());
					orgTO.setSelectedOrgId(nodeId);
					
					orgTreeTOs.add(orgTO);
				}
			}
		}
		
		return orgTreeTOs;
		
	}

	/**
	 * Get all org list
	 * 
	 * @param query
	 * @param tenantId
	 * @return
	 * 
	 */
	private List<OrgTO> getOrgList(List<Map<String, Object>> queryData, String adminYear) {
		List<OrgTO> orgList = new ArrayList<OrgTO>();
		if (queryData != null && queryData.size() > 0) {
			for (Map<String, Object> data : queryData) {
				OrgTO orgTO = new OrgTO();
				orgTO.setTenantId(Long.valueOf(data.get("ORG_NODEID").toString()));
				orgTO.setTenantName((String) data.get("ORG_NODE_NAME"));
				orgTO.setNoOfChildOrgs(Long.valueOf(data.get("CHILD_ORG_NO").toString()));
				orgTO.setParentTenantId(Long.valueOf(data.get("PARENT_ORG_NODEID").toString()));
				orgTO.setSelectedOrgId((String) data.get("SELECTED_ORG_ID"));
				// orgTO.setNoOfUsers(((BigDecimal)
				// data.get("USER_NO")).longValue());
				// orgTO.setNoOfUsers(getTotalUserCount(orgId, adminYear));
				orgList.add(orgTO);
			}
		}
		return orgList;
	}

	/**
	 * Counts the number of users down the organization hierarchy *
	 * 
	 * @param tenantId
	 *            tenantID of the user returned for a selected organization
	 * @return
	 */

	public OrgTO getTotalUserCount(String tenantId, String adminYear, String customerId, String orgMode) {
		
		Query searchUserQuery = new Query(Criteria.where("orgUser.org_id").is(tenantId)
				.and("customerCode").is(customerId)
				.and("project_id").is(Utils.getProject()));
		long userCount = getMongoTemplatePrism().count(searchUserQuery, MUserTO.class);
		
		OrgTO orgTO = new OrgTO();
		orgTO.setNoOfUsers(userCount);
		orgTO.setAdminName(adminYear);
		
		return orgTO;
	}

	/**
	 * Searches and returns the organization(s) with given name (like operator). Performs case insensitive searching.
	 * 
	 * @param orgName
	 *            Search String treated as organization name
	 * @param tenantId
	 *            tenantID of the logged in user
	 * @return
	 */
	public List<OrgTO> searchOrganization(Map<String, Object> paramMap) throws Exception{
		
		logger.log(IAppLogger.INFO, "Enter: searchOrganization()");
		final String orgName = (String) paramMap.get("orgName");
		final String tenantId = (String) paramMap.get("tenantId");
		final String adminYear = (String) paramMap.get("custProdId");
		final String customerId = (String) paramMap.get("customerId");
		final String orgMode = (String)paramMap.get("orgMode");
		
		Query searchOrgQuery = new Query(Criteria.where("project_id").is(Utils.getProject()).and("adminCodes").in(adminYear).orOperator(
				Criteria.where("_id").regex(orgName),
				Criteria.where("name").regex(orgName))
				);
		List<MOrgTO> orgList = getMongoTemplatePrism().find(searchOrgQuery, MOrgTO.class);
		
		List<OrgTO> orgRetList = new ArrayList<OrgTO>();
		if(orgList != null) {
			for(MOrgTO org : orgList) {
				OrgTO orgTO = new OrgTO();
				orgTO.setTenantId(org.get_id());
				orgTO.setTenantName(org.getName());
				orgTO.setNoOfChildOrgs(getChildOrgsCount(org.get_id(),adminYear)); // need to correct
				orgRetList.add(orgTO);
			}
		}
		return orgRetList;
	}

	
	private long getChildOrgsCount(String tenantId,String adminYear){
		long count = 0;
		long countInner = 0;
		Query searchOrgQuery = new Query(Criteria.where("project_id").is(Utils.getProject()).and("adminCodes").in(adminYear).orOperator(
				Criteria.where("_id").regex(tenantId))
				);
		
		List<MOrgTO> orgList = getMongoTemplatePrism().find(searchOrgQuery, MOrgTO.class);
		
		if(orgList != null) {
			for(MOrgTO org : orgList) {
				searchOrgQuery = new Query(Criteria.where("project_id").is(Utils.getProject()).and("adminCodes").in(adminYear).orOperator(
						Criteria.where("parent_id").is(org.get_id()))
						);
				countInner = getMongoTemplatePrism().count(searchOrgQuery,MOrgTO.class);
				count = count + countInner;
			}
		}
		
		return count;
	}
	
	/**
	 * Searches and returns the organization names(use like operator) as a JSON string. Performs case insensitive searching. This method is used to perform auto complete in search box.
	 * 
	 * @param orgName
	 *            Search String treated as organization name
	 * @param tenantId
	 *            tenantID of the logged in user
	 */
	public String searchOrgAutoComplete(Map<String, Object> paramMap) throws Exception{
		
		logger.log(IAppLogger.INFO, "Enter: searchOrgAutoComplete()");
		final String orgName = (String) paramMap.get("orgName");
		final String tenantId = (String) paramMap.get("tenantId");
		final String custProdId = (String) paramMap.get("custProdId");
		final String customerId = (String) paramMap.get("customerId");
		final String orgMode = (String)paramMap.get("orgMode");
		
		Query searchOrgQuery = new Query(Criteria.where("project_id").is(Utils.getProject()).and("adminCodes").in(custProdId).orOperator(
				Criteria.where("_id").regex(orgName),
				Criteria.where("name").regex(orgName))
				);
		List<MOrgTO> orgList = getMongoTemplatePrism().find(searchOrgQuery, MOrgTO.class);
		
		String orgListJsonString = null;
		if(orgList != null) {
			orgListJsonString = "[";
			for(MOrgTO org : orgList) {
				
				/* Blocked this logic to show org code along with org name as this was not existing behavior
				 * Also based on this auto complete text search functionality should work as well. this appended text is breaking the logic 
				 
				  orgListJsonString = CustomStringUtil.appendString(orgListJsonString,
						"\"", org.getName(), " (", org.get_id().substring(org.get_id().lastIndexOf("~")),")", "\",");*/
				
				orgListJsonString = CustomStringUtil.appendString(orgListJsonString,
						"\"", org.getName(), "\",");
				
			}
			orgListJsonString = CustomStringUtil.appendString(orgListJsonString.substring(0, orgListJsonString.length() - 1), "]");
		}
		return orgListJsonString;
	}

	/**
	 * Get all roles
	 * 
	 * @param
	 * @return list of role
	 */
	public ArrayList<RoleTO> getRoleDetails() throws Exception{
		
		return (ArrayList<RoleTO>) getJdbcTemplatePrism().execute(
				new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_ROLE_DETAILS);
						cs.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(2, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						ResultSet rs = null;
						List<RoleTO> roleTOs = new ArrayList<RoleTO>();
						RoleTO to = new RoleTO();
						try {
							cs.execute();
							rs = (ResultSet) cs.getObject(1);
							Utils.logError(cs.getString(2));
							if(cs.getString(2) != null && cs.getString(2).length() > 0) {
								throw new BusinessException("Exception occured while getting Organization " + cs.getString(2));
							}
							while (rs.next()) {
								to.setRoleId(rs.getLong("ROLE_ID"));	
								to.setRoleName(rs.getString("ROLE_NAME"));
								to.setRoleDescription(rs.getString("DESCRIPTION"));
								roleTOs.add(to);
							}
						
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return roleTOs;
					}
				});
	}

	/**
	 * Get role details
	 * 
	 * @param role
	 * @return RoleTO
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getRoleDetailsById'))"),
			@Cacheable(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getRoleDetailsById'))"),
			@Cacheable(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getRoleDetailsById'))")
	} )
	public RoleTO getRoleDetailsById(Map<String,Object> paramMap) {
		
		String roleid=(String) paramMap.get("roleId");
		String currentOrg=(String) paramMap.get("currentOrg");
		String customer=(String) paramMap.get("customer");
		String moreRole=(String) paramMap.get("moreRole");
		
		RoleTO roleTO = new RoleTO();
		List<Map<String, Object>> lstData = null;
		try {
		lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ROLE_DETAILS_BY_ID, roleid);
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				roleTO.setRoleId(((BigDecimal) fieldDetails.get("ROLE_ID")).longValue());
				roleTO.setRoleName((String) (fieldDetails.get("ROLE_NAME")));
				roleTO.setRoleDescription((String) (fieldDetails.get("DESCRIPTION")));
			}
		}
		
		if(moreRole.equals("true"))
		{
			String lastUserId=(String) paramMap.get("lastUserId");
			roleTO.setUserList(getUsersForSelectedRole(roleid,currentOrg,customer,lastUserId));
		}
		else
		{
			roleTO.setUserList(getUsersForSelectedRole(roleid,currentOrg,customer));
		}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return roleTO;
	}

	/**
	 * get user list for selected role
	 * 
	 * @param roleid
	 * @return List of users
	 */
	public ArrayList<UserTO> getUsersForSelectedRole(String roleid, String currentOrg, String customer) throws Exception {

		ArrayList<UserTO> UserTOs = new ArrayList<UserTO>();

		List<Map<String, Object>> lstData = null;
		lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USERS_FOR_SELECTED_ROLE, roleid, 
				Long.valueOf(customer).longValue(),Long.valueOf(customer).longValue(), Long.valueOf(currentOrg).longValue());
		logger.log(IAppLogger.DEBUG, lstData.size() + "");

		if (lstData.size() > 0) {

			for (Map<String, Object> fieldDetails : lstData) {
				UserTO to = new UserTO();
				to.setUserId(((BigDecimal) fieldDetails.get("USER_ID")).longValue());
				to.setUserName((String) (fieldDetails.get("USERNAME")));
				UserTOs.add(to);
			}
		}
		return UserTOs;
	}
	/**
	 * get user list for selected role
	 * 
	 * @param roleid
	 * @return List of users
	 */
	public ArrayList<UserTO> getUsersForSelectedRole(String roleid, String currentOrg, String customer,String last_user_id) throws Exception {

		ArrayList<UserTO> UserTOs = new ArrayList<UserTO>();

		List<Map<String, Object>> lstData = null;
		lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_MORE_USERS_FOR_SELECTED_ROLE, roleid, 
				Long.valueOf(customer).longValue(),Long.valueOf(last_user_id).longValue(),Long.valueOf(customer).longValue(), Long.valueOf(currentOrg).longValue());
		logger.log(IAppLogger.DEBUG, lstData.size() + "");

		if (lstData.size() > 0) {

			for (Map<String, Object> fieldDetails : lstData) {
				UserTO to = new UserTO();
				to.setUserId(((BigDecimal) fieldDetails.get("USER_ID")).longValue());
				to.setUserName((String) (fieldDetails.get("USERNAME")));
				UserTOs.add(to);
			}
		}
		return UserTOs;
	}

	/**
	 * Update role details
	 * 
	 * @param roleTo
	 * @return
	 */
	public boolean updateRole(RoleTO roleTo) {
		logger.log(IAppLogger.INFO, "Enter: updateRole()");
		try {
			ArrayList<UserTO> userTOs = new ArrayList<UserTO>();
			long roleId = roleTo.getRoleId();
			String roleName = roleTo.getRoleName();
			String roleDescription = roleTo.getRoleDescription();

			// update role table
			getJdbcTemplatePrism().update(IQueryConstants.UPDATE_ROLE, roleName, roleDescription, roleId);

			if (roleTo.getUserList().size() > 0) {
				// delete users from user role table
				getJdbcTemplatePrism().update(IQueryConstants.DELETE_ROLE_FROM_USER_ROLE_TABLE, roleId);
				userTOs = (ArrayList<UserTO>) roleTo.getUserList();
				for (UserTO userTo : userTOs) {
					String userId = userTo.getUserId();
					// insert users into user role table
					getJdbcTemplatePrism().update(IQueryConstants.INSERT_INTO_USER_ROLE, userId, roleId);
				}
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while updating role details.", e);
			return false;
		}
		logger.log(IAppLogger.INFO, "Exit: updateRole()");
		return true;
	}

	/**
	 * Delete selected role
	 * 
	 * @param
	 * @return
	 */
	public boolean deleteRole(String roleid) {
		logger.log(IAppLogger.INFO, "Enter: deleteRole()");
		try {
			// removing the role from the users that is to be deleted
			getJdbcTemplatePrism().update(IQueryConstants.DELETE_ROLE_FROM_USER_ROLE_TABLE, roleid);

			// removing the role from role_customer that is to be deleted
			getJdbcTemplatePrism().update(IQueryConstants.DELETE_ROLE_FROM_ROLE_CUSTOMER_TABLE, roleid);

			// deleting the roles from the roles table
			getJdbcTemplatePrism().update(IQueryConstants.DELETE_ROLE_FROM_ROLES_TABLE, roleid);

		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while deleting role table.", e);
			return false;
		}
		logger.log(IAppLogger.INFO, "Exit: deleteRole()");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#associateUserToRole(java.lang.String, java.lang.String)
	 */
	public boolean associateUserToRole(String roleId, String userName) {
		logger.log(IAppLogger.INFO, "Enter: associateUserToRole()");
		try {
			// update role table
			getJdbcTemplatePrism().update(IQueryConstants.INSERT_INTO_USER_ROLE, userName, roleId);

		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while associating user to role.", e);
			return false;
		}
		logger.log(IAppLogger.INFO, "Exit: associateUserToRole()");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#deleteUserFromRole(java.lang.String, java.lang.String)
	 */
	public boolean deleteUserFromRole(String roleId, String userId) {
		logger.log(IAppLogger.INFO, "Enter: deleteUserFromRole()");
		try {
			// update role table
			getJdbcTemplatePrism().update(IQueryConstants.DELETE_USER_FROM_USER_ROLE_TABLE, roleId, userId);

		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while deleting user for role.", e);
			return false;
		}
		logger.log(IAppLogger.INFO, "Exit: deleteUserFromRole()");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#saveRole(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean saveRole(String roleId, String roleName, String roleDescription) {
		logger.log(IAppLogger.INFO, "Enter: updateRole()");
		try {
			// update role table
			getJdbcTemplatePrism().update(IQueryConstants.UPDATE_ROLE, roleName, roleDescription, roleId);

		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while updating role details.", e);
			return false;
		}
		logger.log(IAppLogger.INFO, "Exit: updateRole()");
		return true;
	}
    
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#resetPassword(java.lang.String)
	 */
	public com.ctb.prism.login.transferobject.UserTO resetPassword(Map<String, Object> paramMap) throws Exception {
		
		com.ctb.prism.login.transferobject.UserTO userTO = new com.ctb.prism.login.transferobject.UserTO();
		userTO =  loginDAO.getUserEmail(paramMap);
		
		String contractName = (String) paramMap.get("contractName"); 
		if(contractName == null) {
			contractName =Utils.getContractName();
		}
		final String userName = (String)paramMap.get("username");
				
		String password = PasswordGenerator.getNext();		
		final String salt = userTO.getSalt() != null ? userTO.getSalt() : PasswordGenerator.getNextSalt();
		final String encryptedPass = SaltedPasswordEncoder.encryptPassword(password, Utils.getSaltWithUser(userName, salt));
		
		boolean isUpdated = false;
		
		if (IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
			isUpdated = ldapManager.updateUser(userName, userName, userName, password);
			if (isUpdated) {
				getJdbcTemplatePrism(contractName).update(IQueryConstants.UPDATE_FIRSTTIMEUSERFLAG_DATA, IApplicationConstants.FLAG_Y, userName);
			//	return password;
			} else {
			//	return null;
			}
		} else {
			isUpdated = (Boolean)getJdbcTemplatePrism(contractName).execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(IQueryConstants.SP_RESET_PASSWORD);
					cs.setString(1, userName);
					cs.setString(2, encryptedPass);
					cs.setString(3, salt);
					cs.setString(4, IApplicationConstants.FLAG_Y);
					cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					try {
						cs.execute();
						Utils.logError(cs.getString(5));
						if(cs.getString(5) == null) {
							return true;
						} else {
							Utils.logError(cs.getString(5));
							throw new BusinessException("Exception occured while getting Organization " + cs.getString(5));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					logger.log(IAppLogger.INFO, "resetPassword()");
					return false;
				}
			});
		}
		
		userTO.setPassword(password);
		return userTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#getAllAdmin()
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getAllAdmin'.concat(#root.method.name) )"),
			@Cacheable(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getAllAdmin'.concat(#root.method.name) )"),
			@Cacheable(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getAllAdmin'.concat(#root.method.name) )")
	} )
	public List<ObjectValueTO> getAllAdmin() throws SystemException {
		List<ObjectValueTO> adminYearList = new ArrayList<ObjectValueTO>();
		List<Map<String, Object>> lstData = null;
		lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.ADMIN_YEAR_LIST);
		logger.log(IAppLogger.DEBUG, lstData.size() + "");
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				ObjectValueTO to = new ObjectValueTO();
				to.setValue(((BigDecimal) (fieldDetails.get("ADMINID"))).toString());
				to.setName((String) (fieldDetails.get("ADMIN_NAME")));
				adminYearList.add(to);
			}
		}
		return adminYearList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#addOrganization(com.ctb.prism.admin.transferobject.StgOrgTO)
	 */
	public String addOrganization(StgOrgTO stgOrgTO) {
		BufferedReader read = null;
		ResourceBundle b = ResourceBundle.getBundle("messages");
		int isInserted = getJdbcTemplatePrism().update(IOrgQuery.INSERT_STG_ORG_NODE, stgOrgTO.getOrgNodeId(), stgOrgTO.getOrgNodeName(), stgOrgTO.getOrgNodeCode(), stgOrgTO.getOrgNodeLevel(),
				stgOrgTO.getStrucElement(), stgOrgTO.getSpecialCodes(), stgOrgTO.getOrgMode(), stgOrgTO.getParentOrgNodeId(), stgOrgTO.getOrgNodeCodePath(), stgOrgTO.getEmail(),
				stgOrgTO.getAdminId(), stgOrgTO.getCustomerId());
		if (isInserted > 0) {
			try {
				System.out.println("Before Proc Call");
				// Process proc =
				// Runtime.getRuntime().exec("sh /home/prism/CallInformatica.sh");
				// Process proc = Runtime.getRuntime().exec(new
				// String[]{"/bin/bash", "/home/prism/CallInformatica.sh"});

				File file = new File("/home/prism");
				String[] commands = new String[] { "/bin/bash", "CallInformatica.sh" };
				Process proc = Runtime.getRuntime().exec(commands, null, file);
				System.out.println("After Proc Call");
				read = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				try {
					proc.waitFor();
					System.out.println("After wait for");
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
					proc.getErrorStream();
				}
				while (read.ready()) {
					System.out.println(read.readLine());
					System.out.println("After read ready");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					read.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return b.getString("soad.acknowledgement.AddOrgSuccess");
		}
		return b.getString("soad.acknowledgement.AddOrgFailure");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#getAllStudents(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #root.method.name )"),
			@Cacheable(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #root.method.name )"),
			@Cacheable(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #root.method.name )")
	} )
	public List<ObjectValueTO> getAllStudents(String adminYear, String nodeId, String customerId, String gradeId) {
		List<ObjectValueTO> studentList = new ArrayList<ObjectValueTO>();
		List<Map<String, Object>> lstData = null;
		lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.STUDENT_LIST_FOR_TREE, nodeId, adminYear, adminYear, customerId, gradeId);
		logger.log(IAppLogger.DEBUG, lstData.size() + "");
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				ObjectValueTO to = new ObjectValueTO();
				to.setName(((String) (fieldDetails.get("NAME"))));
				to.setValue(((BigDecimal) (fieldDetails.get("ID"))).toString());
				studentList.add(to);
			}
		}
		return studentList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#getHierarchy(java.util.Map)
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getHierarchy') )"),
			@Cacheable(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getHierarchy') )"),
			@Cacheable(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getHierarchy') )")
	} )
	public ArrayList<OrgTreeTO> getHierarchy(Map<String, Object> paramMap) throws Exception {
		final String nodeId = (String) paramMap.get("nodeid");
		final long custProdId = Long.valueOf(paramMap.get("adminYear").toString());
		final long customerId = Long.valueOf(paramMap.get("customerId").toString());
		final String orgMode = (String) paramMap.get("orgMode");
		final String selectedLevelOrgId = (String) paramMap.get("selectedLevelOrgId");

		logger.log(IAppLogger.INFO, "nodeid=" + nodeId);
		logger.log(IAppLogger.INFO, "custProdId=" + custProdId);
		logger.log(IAppLogger.INFO, "customerId=" + customerId);
		logger.log(IAppLogger.INFO, "orgMode=" + orgMode);

		
		ArrayList<OrgTreeTO> lstData = new ArrayList<OrgTreeTO>();
		
		return	lstData = (ArrayList<OrgTreeTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				if ("-1".equals(selectedLevelOrgId)) {
					CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_TENANT_DETAILS);
					cs.setLong(1, Long.valueOf(nodeId));
					cs.setLong(2, customerId);
					cs.setLong(3, Long.valueOf(custProdId));
					cs.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				} else {
					CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_TENANT_DETAILS_NON_ACSI);
					cs.setLong(1, Long.valueOf(nodeId));
					cs.setLong(2, customerId);
					cs.setLong(3, Long.valueOf(selectedLevelOrgId));
					cs.setLong(4, Long.valueOf(custProdId));
					cs.setString(5, orgMode);
					cs.registerOutParameter(6, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(7, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
			}
}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				ResultSet rs = null;
				List<OrgTreeTO> orgTreeTOs = new ArrayList<OrgTreeTO>();
				OrgTO orgTO = null;
				OrgTreeTO treeTO = null;
				try {
					cs.execute();
					if ("-1".equals(selectedLevelOrgId)){
						rs = (ResultSet) cs.getObject(4);
						Utils.logError(cs.getString(5));
						if(cs.getString(5) != null && cs.getString(5).length() > 0) {
							throw new BusinessException("Exception occured while getting Organization " + cs.getString(5));
						}
					} else {
						rs = (ResultSet) cs.getObject(6);
						Utils.logError(cs.getString(7));
						if(cs.getString(7) != null && cs.getString(7).length() > 0) {
							throw new BusinessException("Exception occured while getting Organization " + cs.getString(7));
						}
					}
					
					while (rs.next()) {
						orgTO = new OrgTO();
						treeTO = new OrgTreeTO();
						orgTO.setId(rs.getLong("ORG_NODEID"));
						orgTO.setParentTenantId(rs.getLong("PARENT_ORG_NODEID"));
						orgTO.setOrgLevel(rs.getLong("ORG_NODE_LEVEL"));
						orgTO.setTenantName(rs.getString("ORG_NODE_NAME"));
						
						treeTO.setState("closed");
						treeTO.setOrgTreeId(rs.getLong("ORG_NODEID"));
						treeTO.setData(rs.getString("ORG_NODE_NAME"));
						treeTO.setMetadata(orgTO);
						treeTO.setAttr(orgTO);
						orgTreeTOs.add(treeTO);							
					}					
					
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				logger.log(IAppLogger.INFO, "actionMap.size = " + orgTreeTOs.size());
				return orgTreeTOs;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#downloadStudentFile(java.util.Map)
	 */
	@Deprecated
	public List<StudentDataTO> downloadStudentFile(final Map<String, Object> paramMap) throws SystemException {
		logger.log(IAppLogger.INFO, "Enter: downloadStudentFile()");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#getEducationCenter(java.util.Map)
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getEducationCenter(final Map<String, Object> paramMap) throws SystemException {
		logger.log(IAppLogger.INFO, "Enter: getEducationCenter()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) paramMap.get("loggedinUserTO");
		List<String> placeHolderValueList = new ArrayList<String>();
		try {
			if (IApplicationConstants.SS_FLAG.equals(loggedinUserTO.getUserStatus())) {
				logger.log(IAppLogger.INFO, "Fetch Education Center for Customer ID: " + loggedinUserTO.getCustomerId());
				placeHolderValueList.add(loggedinUserTO.getCustomerId());
				objectValueTOList = getJdbcTemplatePrism().query(IQueryConstants.GET_EDUCATION_CENTER_ALL, placeHolderValueList.toArray(), new ObjectValueTOMapper());
			} else {
				logger.log(IAppLogger.INFO, "Fetch Education Center for Customer ID: " + loggedinUserTO.getCustomerId());
				logger.log(IAppLogger.INFO, "Fetch Education Center for User ID: " + loggedinUserTO.getUserId());
				placeHolderValueList.add(loggedinUserTO.getCustomerId());
				placeHolderValueList.add(loggedinUserTO.getUserId());
				objectValueTOList = getJdbcTemplatePrism().query(IQueryConstants.GET_EDUCATION_CENTER, placeHolderValueList.toArray(), new ObjectValueTOMapper());
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred in getEducationCenter():", e);
			throw new SystemException(e);
		}
		logger.log(IAppLogger.INFO, "Exit: getEducationCenter()");
		return objectValueTOList;
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.admin.dao.IAdminDAO#loadEduCenterUsers(java.util.Map)
	 */
	public List<EduCenterTO> loadEduCenterUsers(final Map<String,Object> paramMap) throws SystemException {
		logger.log(IAppLogger.INFO, "Enter: loadEduCenterUsers()");
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) paramMap.get("loggedinUserTO");
		long eduCenterId = ((Long) paramMap.get("eduCenterId")).longValue();
		String searchParam = (String) paramMap.get("searchParam");
		String lastEduCenterId_username = (String) paramMap.get("lastEduCenterId_username");
		String moreCount = (String) paramMap.get("moreCount");
		logger.log(IAppLogger.INFO, "moreCount = "+ moreCount);
		String userName = "";
		List<Object> placeHolderValueList = new ArrayList<Object>();
		List<EduCenterTO> eduCenterTOList = null;
		try{
			if (lastEduCenterId_username.indexOf("_") > 0) {
				userName = lastEduCenterId_username.substring((lastEduCenterId_username.indexOf("_") + 1), lastEduCenterId_username.length());
				logger.log(IAppLogger.INFO, "userName = "+ userName);
				if(searchParam != null && searchParam.trim().length() > 0) {
					searchParam = CustomStringUtil.appendString("%", searchParam, "%");
					placeHolderValueList.add(loggedinUserTO.getCustomerId());
					placeHolderValueList.add(eduCenterId);
					placeHolderValueList.add(userName);
					placeHolderValueList.add(userName);
					placeHolderValueList.add(userName);
					placeHolderValueList.add(searchParam);
					placeHolderValueList.add(moreCount);
					eduCenterTOList = getJdbcTemplatePrism().query(IQueryConstants.GET_EDU_CENTER_USERS_ON_SCROLL_SEARCH, placeHolderValueList.toArray(),new EduCenterTOMapper(getJdbcTemplatePrism()));
				} else {
					placeHolderValueList.add(loggedinUserTO.getCustomerId());
					placeHolderValueList.add(eduCenterId);
					placeHolderValueList.add(userName);
					placeHolderValueList.add(moreCount);
					eduCenterTOList = getJdbcTemplatePrism().query(IQueryConstants.GET_EDU_CENTER_USERS_ON_SCROLL, placeHolderValueList.toArray(),new EduCenterTOMapper(getJdbcTemplatePrism()));
				}
			} else {
				placeHolderValueList.add(loggedinUserTO.getCustomerId());
				placeHolderValueList.add(eduCenterId);
				placeHolderValueList.add(moreCount);
				eduCenterTOList = getJdbcTemplatePrism().query(IQueryConstants.GET_EDU_CENTER_USERS_FIRST_LOAD, placeHolderValueList.toArray(),new EduCenterTOMapper(getJdbcTemplatePrism()));
			}
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "Error occurred in loadEduCenterUsers():",e);
			throw new SystemException(e);
		}finally{
			logger.log(IAppLogger.INFO, "Exit: loadEduCenterUsers()");
		}
		return eduCenterTOList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#getUserData(java.util.Map)
	 */
	public List<UserDataTO> getUserData(Map<String, String> paramMap) {
		long start = System.currentTimeMillis();
		logger.log(IAppLogger.INFO, "Enter: getUserData()");
		String userId = (String) paramMap.get("userId");
		String orgNodeId = (String) paramMap.get("tenantId");
		String adminYear = (String) paramMap.get("adminYear");
		String orgMode = (String) paramMap.get("orgMode");
		logger.log(IAppLogger.INFO, "userId=" + userId);
		logger.log(IAppLogger.INFO, "orgNodeId=" + orgNodeId);
		logger.log(IAppLogger.INFO, "adminYear=" + adminYear);
		logger.log(IAppLogger.INFO, "orgMode=" + orgMode);
		ArrayList<UserDataTO> userDataList = new ArrayList<UserDataTO>();
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_DATA, orgMode, userId, orgNodeId, IApplicationConstants.ROLE_PARENT_ID, adminYear);
		if ((lstData != null) && (!lstData.isEmpty())) {
			for (Map<String, Object> fieldDetails : lstData) {
				UserDataTO to = new UserDataTO();
				to.setUserId(fieldDetails.get("USERNAME").toString());
				to.setFullName(fieldDetails.get("FULLNAME").toString());
				to.setStatus(fieldDetails.get("STATUS").toString());
				to.setOrgName(fieldDetails.get("ORG_NODE_NAME").toString());
				to.setUserRoles(getRolesWithLabel((String) fieldDetails.get("ORG_LABEL"), (String) fieldDetails.get("DESCRIPTION")));
				userDataList.add(to);
			}
		}
		long end = System.currentTimeMillis();
		logger.log(IAppLogger.INFO, "Exit: getUserData() : " + CustomStringUtil.getHMSTimeFormat(end - start));
		return userDataList;
	}

	/**
	 * Appends the label before each role. If the role contains "ADMIN" then only one role is returned.
	 * 
	 * @param label
	 * @param userRoles
	 * @return
	 */
	private static String getRolesWithLabel(String label, String userRoles) {
		if (userRoles == null || "null".equalsIgnoreCase(userRoles)) {
			return "";
		}
		StringBuilder buff = new StringBuilder();
		String[] roles = userRoles.split(",");
		for (String role : roles) {
			String[] roleTokens = role.split(" ");
			for (String token : roleTokens) {
				if ("ADMIN".equalsIgnoreCase(token)) {
					return CustomStringUtil.appendString(label, " ", role);
				}
			}
			buff.append(label);
			buff.insert(buff.length(), " ");
			buff.insert(buff.length(), role);
			buff.insert(buff.length(), ", ");
		}
		buff.delete(buff.length() - 2, buff.length());
		return buff.toString();
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.admin.dao.IAdminDAO#getUserForResetPassword(java.util.Map)
	 */
	public UserTO getUserForResetPassword(Map<String, String> paramMap) throws SystemException{
		final String username = paramMap.get("username");
	//	final Long currentOrg = Long.parseLong(paramMap.get("currentOrg"));
	//	final Long currentOrgLvl = Long.parseLong(paramMap.get("currentOrgLvl"));
		logger.log(IAppLogger.INFO, "username = " + username);
	//	logger.log(IAppLogger.INFO, "currentOrg = " + currentOrg);
	//	logger.log(IAppLogger.INFO, "currentOrgLvl = " + currentOrgLvl);
		return (UserTO) getJdbcTemplatePrism().execute(
			new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_USER_RESET_PASSWORD);
					cs.setString(1, username);
				//	cs.setLong(2, currentOrg);
				//	cs.setLong(3, currentOrgLvl);
					cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					UserTO user = new UserTO();
					try {
						cs.execute();
						Utils.logError(cs.getString(3));
						if(cs.getString(3) != null && cs.getString(3).length() > 0) {
							throw new BusinessException("Exception occured while getting Organization " + cs.getString(3));
						}
						rs = (ResultSet) cs.getObject(2);
						while(rs.next()){
							user.setUserId(Long.parseLong(rs.getString("USERID")));
							user.setUserName(rs.getString("USERNAME"));
							user.setUserDisplayName(rs.getString("DISPLAY_USERNAME"));
							user.setFirstName(rs.getString("FIRST_NAME"));
							user.setMiddleName(rs.getString("MIDDLE_NAME"));
							user.setLastName(rs.getString("LAST_NAME"));
							user.setEmailId(rs.getString("EMAIL_ADDRESS"));
							user.setPhoneNumber(rs.getString("PHONE_NO"));
							user.setStreet(rs.getString("STREET"));
							user.setCountry(rs.getString("COUNTRY"));
							user.setCity(rs.getString("CITY"));
							user.setZip(rs.getString("ZIPCODE"));
							user.setState(rs.getString("STATE"));
							user.setPwdHintList(getUserPwdHintList(rs.getString("USERID")));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return user;
				}
			}
		);
	}

	/**
	 * Gets the password hint list with answers.
	 * 
	 * @param userId
	 * @return
	 */
	public List<PwdHintTO> getUserPwdHintList(final String userId) throws SQLException {
		logger.log(IAppLogger.INFO, "userId = " + userId);
		return (List<PwdHintTO>) getJdbcTemplatePrism().execute(
			new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_USER_PWD_HINT_LIST);
					cs.setString(1, userId);
					cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<PwdHintTO> pwdHintList = new ArrayList<PwdHintTO>();
					try {
						cs.execute();
						Utils.logError(cs.getString(3));
						if(cs.getString(3) != null && cs.getString(3).length() > 0) {
							throw new BusinessException("Exception occured while getting Organization " + cs.getString(3));
						}
						rs = (ResultSet) cs.getObject(2);
						while (rs.next()) {
							PwdHintTO to = new PwdHintTO();
							to.setUserId(Long.parseLong(rs.getString("USERID")));
							to.setQuestionId(Long.parseLong(rs.getString("PH_QUESTIONID")));
							to.setQuestionValue(rs.getString("QUESTION_VALUE"));
							to.setQuestionSequence(Long.parseLong(rs.getString("QUESTION_SEQ")));
							to.setQuestionActivationStatus(rs.getString("ACTIVATION_STATUS"));
							to.setAnswerId(Long.parseLong(rs.getString("PH_ANSWERID")));
							to.setAnswerValue(rs.getString("ANSWER_VALUE"));
							pwdHintList.add(to);
						}
						
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return pwdHintList;
				}
			});
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.admin.dao.IAdminDAO#getEduUserRoleList(java.util.Map)
	 */
	public List<RoleTO> getEduUserRoleList(Map<String, String> paramMap) throws Exception {
		final Long userId = Long.valueOf(paramMap.get("userId"));
		return (List<RoleTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_EDU_USER_ROLE);
				cs.setLong(1, userId);
				cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
				cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				ResultSet rs = null;
				List<RoleTO> roleList = new ArrayList<RoleTO>();
				try {
					cs.execute();
					Utils.logError(cs.getString(3));
					if(cs.getString(3) != null && cs.getString(3).length() > 0) {
						throw new BusinessException("Exception occured while getting Organization " + cs.getString(3));
					}
					rs = (ResultSet) cs.getObject(2);
					while(rs.next()){
						RoleTO to = new RoleTO();
						to.setRoleId(Long.parseLong(rs.getString("ROLEID")));
						to.setRoleName(rs.getString("ROLE_NAME"));
						to.setRoleDescription(rs.getString("DESCRIPTION"));
						roleList.add(to);
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				logger.log(IAppLogger.INFO, "getEduUserRoleList().roleList.size()=" + roleList.size());
				return roleList;
			}
		});
	}
	
}
