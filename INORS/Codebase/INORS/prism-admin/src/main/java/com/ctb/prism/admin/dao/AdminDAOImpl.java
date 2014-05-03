package com.ctb.prism.admin.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.stereotype.Repository;

import com.ctb.prism.admin.transferobject.EduCenterTO;
import com.ctb.prism.admin.transferobject.EduCenterTOMapper;
import com.ctb.prism.admin.transferobject.ObjectValueTO;
import com.ctb.prism.admin.transferobject.OrgTO;
import com.ctb.prism.admin.transferobject.OrgTreeTO;
import com.ctb.prism.admin.transferobject.RoleTO;
import com.ctb.prism.admin.transferobject.StgOrgTO;
import com.ctb.prism.admin.transferobject.StudentDataTO;
import com.ctb.prism.admin.transferobject.UserDataTO;
import com.ctb.prism.admin.transferobject.UserTO;
import com.ctb.prism.admin.util.StudentDataConstants;
import com.ctb.prism.admin.util.StudentDataUtil;
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
//import com.googlecode.ehcache.annotations.Cacheable;
//import com.googlecode.ehcache.annotations.TriggersRemove;

@Repository("adminDAO")
public class AdminDAOImpl extends BaseDAO implements IAdminDAO {

	@Autowired
	private LdapManager ldapManager;

	@Autowired
	private IPropertyLookup propertyLookup;

	private static final IAppLogger logger = LogFactory.getLoggerInstance(AdminDAOImpl.class.getName());

	/**
	 * Returns the organizationList on load.
	 * 
	 * @param nodeid
	 * @return
	 */
	public ArrayList<OrgTO> getOrganizationDetailsOnFirstLoad(String nodeid) {
		logger.log(IAppLogger.INFO, "Enter: getOrganizationDetailsOnFirstLoad()");
		ArrayList<OrgTO> OrgTOs = new ArrayList<OrgTO>();
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_CURR_TENANT_DETAILS, nodeid);
		if (lstData.size() > 0) {
			OrgTOs = new ArrayList<OrgTO>();
			for (Map<String, Object> fieldDetails : lstData) {
				OrgTO to = new OrgTO();
				to.setTenantId(((BigDecimal) fieldDetails.get("ID")).longValue());
				to.setTenantName((fieldDetails.get("ORG_NAME")).toString());
				to.setParentTenantId(((BigDecimal) fieldDetails.get("PARENTID")).longValue());
				to.setOrgLevel(((BigDecimal) fieldDetails.get("ORG_LEVEL")).longValue());
				OrgTOs.add(to);
			}
		}
		logger.log(IAppLogger.INFO, "Exit: getOrganizationDetailsOnFirstLoad()");
		return OrgTOs;
	}

	/**
	 * Returns the organizationList on load.
	 * 
	 * @param nodeid
	 * @return
	 */
	public ArrayList<OrgTO> getOrganizationDetailsOnClick(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: getOrganizationDetailsOnClick()");
		String nodeid = (String) paramMap.get("nodeid");
		String currOrg = (String) paramMap.get("currOrg");
		boolean isFirstLoad = (Boolean) paramMap.get("isFirstLoad");
		String adminYear = (String) paramMap.get("adminYear");
		long customerId = Long.valueOf(paramMap.get("customerId").toString());
		String orgMode = (String) paramMap.get("orgMode");

		logger.log(IAppLogger.INFO, "nodeid=" + nodeid);
		logger.log(IAppLogger.INFO, "currOrg=" + currOrg);
		logger.log(IAppLogger.INFO, "isFirstLoad=" + isFirstLoad);
		logger.log(IAppLogger.INFO, "adminYear=" + adminYear);
		logger.log(IAppLogger.INFO, "customerId=" + customerId);
		logger.log(IAppLogger.INFO, "orgMode=" + orgMode);

		ArrayList<OrgTO> OrgTOs = new ArrayList<OrgTO>();
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_TENANT_DETAILS, adminYear, orgMode, nodeid, customerId);
		if (lstData.size() > 0) {
			OrgTOs = new ArrayList<OrgTO>();
			for (Map<String, Object> fieldDetails : lstData) {
				OrgTO to = new OrgTO();
				to.setTenantId(((BigDecimal) fieldDetails.get("ID")).longValue());
				to.setTenantName((fieldDetails.get("ORG_NAME")).toString());
				to.setParentTenantId(((BigDecimal) fieldDetails.get("PARENTID")).longValue());
				to.setOrgLevel(((BigDecimal) fieldDetails.get("ORG_LEVEL")).longValue());
				OrgTOs.add(to);
			}
		}
		logger.log(IAppLogger.INFO, "Exit: getOrganizationDetailsOnClick()");
		return OrgTOs;
	}

	/**
	 * Returns the organizationList to create a tree structure.
	 * 
	 * @param nodeid
	 * @return
	 */
	// @Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getOrganizationTree') )")
	public ArrayList<OrgTreeTO> getOrganizationTree(Map<String, Object> paramMap) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: getOrganizationTree()");
		String nodeId = (String) paramMap.get("nodeid");
		String currOrg = (String) paramMap.get("currOrg");
		boolean isFirstLoad = (Boolean) paramMap.get("isFirstLoad");
		String adminYear = (String) paramMap.get("adminYear");
		long customerId = Long.valueOf(paramMap.get("customerId").toString());
		String orgMode = (String) paramMap.get("orgMode");

		logger.log(IAppLogger.INFO, "nodeid=" + nodeId);
		logger.log(IAppLogger.INFO, "currOrg=" + currOrg);
		logger.log(IAppLogger.INFO, "isFirstLoad=" + isFirstLoad);
		logger.log(IAppLogger.INFO, "adminYear=" + adminYear);
		logger.log(IAppLogger.INFO, "customerId=" + customerId);
		logger.log(IAppLogger.INFO, "orgMode=" + orgMode);

		ArrayList<OrgTreeTO> OrgTreeTOs = new ArrayList<OrgTreeTO>();
		List<Map<String, Object>> lstData = null;
		if (isFirstLoad) {
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_CURR_TENANT_DETAILS, nodeId, customerId);
		} else {
			if (nodeId.indexOf("_") > 0) {
				String orgParentId = nodeId.substring(0, nodeId.indexOf("_"));
				String orgLevel = nodeId.substring((nodeId.indexOf("_") + 1), nodeId.length());
				if (!("1".equals(orgLevel))) {
					lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_TENANT_DETAILS_NON_ACSI, adminYear, orgMode, orgParentId, currOrg, customerId, currOrg);
					logger.log(IAppLogger.DEBUG, "Tree for non TASC Users...Currorg=" + currOrg);
				} else {
					if(Integer.parseInt(orgParentId) == 0){
						lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_TENANT_DETAILS, adminYear, orgParentId, customerId);
					}else{
						lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_TENANT_DETAILS_NON_ROOT, adminYear, orgParentId, customerId, orgMode);
					}
				}
			} else {
				if(Integer.parseInt(nodeId) == 0){
					lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_TENANT_DETAILS, adminYear, nodeId, customerId);
				}else{
					lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_TENANT_DETAILS_NON_ROOT, adminYear, nodeId, customerId, orgMode);
				}
			}
		}
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				OrgTO to = new OrgTO();
				OrgTreeTO treeTo = new OrgTreeTO();
				to.setId(Long.valueOf(fieldDetails.get("ORG_NODEID").toString()));
				to.setParentTenantId(Long.valueOf(fieldDetails.get("PARENT_ORG_NODEID").toString()));
				to.setOrgLevel(Long.valueOf(fieldDetails.get("ORG_NODE_LEVEL").toString()));
				treeTo.setState("closed");
				treeTo.setOrgTreeId(Long.valueOf(fieldDetails.get("ORG_NODEID").toString()));
				treeTo.setData((String) (fieldDetails.get("ORG_NODE_NAME")));
				//treeTo.setOrgMode((String) (fieldDetails.get("ORG_MODE")));
				//treeTo.setOrgNodeLevel(((BigDecimal) (fieldDetails.get("ORG_NODE_LEVEL"))).toString());
				treeTo.setMetadata(to);
				treeTo.setAttr(to);
				OrgTreeTOs.add(treeTo);
			}
		}
		logger.log(IAppLogger.INFO, "Exit: getOrganizationTree()");
		return OrgTreeTOs;
	}

	/**
	 * Returns the organizationList to create a tree structure on redirecting from manage organozations page while clicked on the number of usres .
	 * 
	 * 
	 * @param nodeid
	 * @return
	 */
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #root.method.name )")
	public String getOrganizationTreeOnRedirect(String selectedOrgId, String parentOrgId, String userId, long customerId, boolean isRedirected) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: getOrganizationTreeOnRedirect()");
		StringBuffer hierarcialOrgIds = new StringBuffer();
		String cummsSeperatedId = "";
		List<Map<String, Object>> hierarcialOrgIdList = null;
		if (isRedirected) {
			hierarcialOrgIdList = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ORG_HIERARCHY_ON_REDIRECT, customerId, selectedOrgId, parentOrgId, userId);
		}

		if ((hierarcialOrgIdList != null) && (hierarcialOrgIdList.size() > 0)) {
			for (Map<String, Object> orgId : hierarcialOrgIdList) {
				String hierarcialOrgId = (String) orgId.get("ORG_ID");
				hierarcialOrgIds.append(hierarcialOrgId).append(",");
			}
			cummsSeperatedId = hierarcialOrgIds.toString();
			cummsSeperatedId = cummsSeperatedId.substring(0, hierarcialOrgIds.length() - 1);
		}
		logger.log(IAppLogger.INFO, "Exit: getOrganizationTreeOnRedirect()");
		return cummsSeperatedId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#getOrgTree(java.util.Map)
	 */
	public ArrayList<OrgTreeTO> getOrgTree(Map<String, Object> paramMap) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: getOrgTree()");
		ArrayList<OrgTreeTO> OrgTreeTOs = new ArrayList<OrgTreeTO>();

		String nodeId = (String) paramMap.get("nodeid");
		// String currOrg = (String) paramMap.get("currOrg");
		boolean isFirstLoad = (Boolean) paramMap.get("isFirstLoad");
		String adminYear = (String) paramMap.get("adminYear");
		long customerId = Long.valueOf(paramMap.get("customerId").toString());
		String orgMode = (String) paramMap.get("orgMode");

		List<Map<String, Object>> lstData;
		if (isFirstLoad) {
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ORGANIZATION_LIST, nodeId, nodeId, customerId, orgMode, nodeId, customerId);
		} else {
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ORG_CHILDREN_LIST, nodeId, adminYear, orgMode, customerId, nodeId, adminYear, customerId);
		}

		if (lstData.size() > 0) {

			for (Map<String, Object> fieldDetails : lstData) {
				OrgTO to = new OrgTO();
				OrgTreeTO treeTo = new OrgTreeTO();

				to.setId(Long.valueOf(fieldDetails.get("SELECTED_ORG_ID").toString()));

				to.setParentTenantId(Long.valueOf(fieldDetails.get("Parent_Org_Nodeid").toString()));

				to.setOrgLevel(Long.valueOf(fieldDetails.get("Org_Node_Level").toString()));

				treeTo.setState("closed");
				treeTo.setOrgTreeId(Long.valueOf(fieldDetails.get("SELECTED_ORG_ID").toString()));
				treeTo.setData((fieldDetails.get("Org_Node_Name").toString()));
				treeTo.setMetadata(to);
				treeTo.setAttr(to);
				OrgTreeTOs.add(treeTo);
			}
		}
		logger.log(IAppLogger.INFO, "Exit: getOrgTree()");
		return OrgTreeTOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#getUserDetailsOnClick(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Cacheable(value = "adminCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #p5, #root.method.name )")
	public ArrayList<UserTO> getUserDetailsOnClick(String nodeId, String currorg, String adminYear, String searchParam, String customerid, String orgMode) {
		logger.log(IAppLogger.INFO, "Enter: getUserDetailsOnClick()");
		logger.log(IAppLogger.INFO, "nodeId=" + nodeId);
		logger.log(IAppLogger.INFO, "currorg=" + currorg);
		logger.log(IAppLogger.INFO, "adminYear=" + adminYear);
		logger.log(IAppLogger.INFO, "searchParam=" + searchParam);
		logger.log(IAppLogger.INFO, "customerid=" + customerid);
		logger.log(IAppLogger.INFO, "orgMode=" + orgMode);
		ArrayList<UserTO> UserTOs = new ArrayList<UserTO>();
		ArrayList<RoleTO> RoleTOs = new ArrayList<RoleTO>();
		String userName = "";
		String tenantId = "";
		List<Map<String, Object>> lstData = new ArrayList<Map<String, Object>>();
		if (nodeId.indexOf("_") > 0) {
			userName = nodeId.substring((nodeId.indexOf("_") + 1), nodeId.length());
			tenantId = nodeId.substring(0, nodeId.indexOf("_"));
			logger.log(IAppLogger.INFO, "userName=" + userName);
			logger.log(IAppLogger.INFO, "tenantId=" + tenantId);
			if (searchParam != null && searchParam.trim().length() > 0) {
				searchParam = CustomStringUtil.appendString("%", searchParam, "%");
				logger.log(IAppLogger.INFO, "searchParam=" + searchParam);
				logger.log(IAppLogger.DEBUG, "GET_USER_DETAILS_ON_SCROLL_WITH_SRCH_PARAM");
				lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_DETAILS_ON_SCROLL_WITH_SRCH_PARAM, customerid, orgMode, tenantId, customerid, tenantId, IApplicationConstants.ROLE_PARENT_ID, adminYear, userName, searchParam, searchParam, searchParam);
			} else {
				logger.log(IAppLogger.DEBUG, "GET_USER_DETAILS_ON_SCROLL");
				lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_DETAILS_ON_SCROLL, customerid, orgMode, tenantId, customerid, tenantId,IApplicationConstants.ROLE_PARENT_ID, adminYear, userName);
			}
		} else {
			logger.log(IAppLogger.DEBUG, "GET_USER_DETAILS_ON_FIRST_LOAD");
			tenantId = nodeId;
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_DETAILS_ON_FIRST_LOAD, customerid, orgMode, tenantId, customerid, tenantId, adminYear, IApplicationConstants.ROLE_PARENT_ID);
		}
		logger.log(IAppLogger.DEBUG, lstData.size() + "");
		if (lstData.size() > 0) {
			UserTOs = new ArrayList<UserTO>();
			for (Map<String, Object> fieldDetails : lstData) {
				UserTO to = new UserTO();
				long userId = ((BigDecimal) fieldDetails.get("USER_ID")).longValue();
				to.setUserId(userId);
				// fetching role for each users
				if ((String.valueOf(userId) != null) && ((String) (fieldDetails.get("USERNAME")) != null)) {
					List<Map<String, Object>> roleList = null;
					roleList = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_ROLE, userId);
					if (roleList.size() > 0) {
						RoleTOs = new ArrayList<RoleTO>();
						for (Map<String, Object> roleDetails : roleList) {

							RoleTO rleTo = new RoleTO();
							rleTo.setRoleId(((BigDecimal) roleDetails.get("ROLEID")).longValue());
							rleTo.setRoleName((String) (roleDetails.get("ROLE_NAME")));
							rleTo.setRoleDescription((String) (roleDetails.get("DESCRIPTION")));
							rleTo.setLabel((String) (roleDetails.get("ORG_LABEL")));
							RoleTOs.add(rleTo);
						}
						to.setAvailableRoleList(RoleTOs);
					}

				}
				to.setUserName((String) (fieldDetails.get("USERNAME")));
				if ((String) (fieldDetails.get("FULLNAME")) != null) {
					to.setUserDisplayName((String) (fieldDetails.get("FULLNAME")));
				} else {
					to.setUserDisplayName("");
				}

				to.setStatus((String) (fieldDetails.get("STATUS")));
				to.setTenantId(((BigDecimal) fieldDetails.get("ORG_PARENT_ID")).longValue());
				to.setParentId(((BigDecimal) fieldDetails.get("ORG_PARENT_ID")).longValue());
				try {
					to.setLoggedInOrgId(Long.parseLong(currorg));
				} catch (NumberFormatException e) {
				}
				to.setTenantName((String) (fieldDetails.get("ORG_NAME")));
				// to.setUserType((String) (fieldDetails.get("USER_TYPE")));
				UserTOs.add(to);
			}
		}
		logger.log(IAppLogger.INFO, "Users: " + UserTOs.size());
		logger.log(IAppLogger.INFO, "Exit: getUserDetailsOnClick()");
		return UserTOs;
	}

	/**
	 * Returns the userTO on Edit.
	 * 
	 * @param nodeid
	 * @return
	 */
	public UserTO getEditUserData(Map<String, Object> paramMap) {
		UserTO to = new UserTO();
		List<RoleTO> availableRoleList = new ArrayList<RoleTO>();
		RoleTO roleTO = null;
		String nodeId = (String) paramMap.get("nodeId");
		String customerId = (String) paramMap.get("customer");

		List<RoleTO> masterRoleList = null;
		String purpose = "edit";
		masterRoleList = getMasterRoleList("user", nodeId, customerId,purpose);

		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_DETAILS_ON_EDIT, nodeId);
		List<Map<String, Object>> lstRoleData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_ROLE_ON_EDIT, nodeId, nodeId,
				IApplicationConstants.ROLE_TYPE.ROLE_CTB.toString(),
				IApplicationConstants.ROLE_TYPE.ROLE_PARENT.toString(),
				IApplicationConstants.ROLE_TYPE.ROLE_SUPER.toString());
		
		for (Map<String, Object> fieldDetails : lstRoleData) {
			roleTO = new RoleTO();
			String roleName = ((String) (fieldDetails.get("ROLENAME")));
			String roleDescription = ((String) (fieldDetails.get("DESCRIPTION")));
			// if (!(IApplicationConstants.DEFAULT_USER_ROLE.equals(roleName))){
			roleTO.setRoleName(roleName);
			roleTO.setRoleDescription(roleDescription);
			availableRoleList.add(roleTO);
			// }
		}
		to.setAvailableRoleList(availableRoleList);
		to.setMasterRoleList(masterRoleList);
		for (Map<String, Object> fieldDetails : lstData) {
			to.setUserId(((BigDecimal) fieldDetails.get("ID")).longValue());
			to.setUserName((String) (fieldDetails.get("USERID")));
			to.setUserDisplayName((String) (fieldDetails.get("USERNAME")));
			to.setEmailId((String) (fieldDetails.get("EMAIL")));
			to.setStatus((String) (fieldDetails.get("STATUS")));
		}
		return to;
	}

	/**
	 * Returns the RoleTO on Add.
	 * 
	 * @param userId
	 * @return
	 */
	public List<RoleTO> getRoleOnAddUser(String orgLevel, String customerId) {
		String purpose = "add";
		return getMasterRoleList("org", orgLevel, customerId,purpose);
	}

	/**
	 * Returns the masterRoleTO on Edit/Add.
	 * 
	 * @param argType
	 *            ,userId
	 * @return
	 */
	@Cacheable(value = "configCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #root.method.name )")
	private List<RoleTO> getMasterRoleList(String argType, String userid, String customerId,String purpose) {
		logger.log(IAppLogger.INFO, "argType=" + argType);
		logger.log(IAppLogger.INFO, "userid=" + userid);
		logger.log(IAppLogger.INFO, "customerId=" + customerId);
		RoleTO masterRoleTO = null;
		List<RoleTO> masterRoleList = new ArrayList<RoleTO>();
		List<Map<String, Object>> lstMasterRoleData = null;
		long user_org_level = -1;
		ResourceBundle b = ResourceBundle.getBundle("messages");
		if ("user".equals(argType)) {
			try {
				user_org_level = getJdbcTemplatePrism().queryForLong(IQueryConstants.GET_USER_ORG_LEVEL, userid);
			} catch (DataAccessException e) {
				logger.log(IAppLogger.INFO, "No data found for userid: " + userid);
			}
			logger.log(IAppLogger.DEBUG, "user_org_level" + user_org_level);
		} else if ("org".equals(argType)) {
			user_org_level = Long.valueOf(userid);
		}

		if("add".equals(purpose)){
			lstMasterRoleData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ROLE_ADD, 
					IApplicationConstants.ROLE_TYPE.ROLE_CTB.toString(),
					IApplicationConstants.ROLE_TYPE.ROLE_PARENT.toString(),
					IApplicationConstants.ROLE_TYPE.ROLE_SUPER.toString());
		}else{
			lstMasterRoleData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ROLE, userid, userid, userid, 
					IApplicationConstants.ROLE_TYPE.ROLE_CTB.toString(),
					IApplicationConstants.ROLE_TYPE.ROLE_PARENT.toString(),
					IApplicationConstants.ROLE_TYPE.ROLE_SUPER.toString());
		}
		

		/*
		 * if (user_org_level == 1) { lstMasterRoleData =
		 * getJdbcTemplatePrism().queryForList( IQueryConstants.GET_ROLE_ACSI);
		 * } else if (user_org_level == 3) { lstMasterRoleData =
		 * getJdbcTemplatePrism().queryForList(
		 * IQueryConstants.GET_ROLE_SCHOOL); } else if (user_org_level == 4) {
		 * lstMasterRoleData = getJdbcTemplatePrism().queryForList(
		 * IQueryConstants.GET_ROLE_TEACHER); } else { lstMasterRoleData =
		 * getJdbcTemplatePrism().queryForList(
		 * IQueryConstants.GET_ROLE_DEFAULT); }
		 */
		for (Map<String, Object> fieldDetails : lstMasterRoleData) {
			masterRoleTO = new RoleTO();
			String roleName = (String) (fieldDetails.get("ROLE_NAME"));
			masterRoleTO.setRoleName((String) (fieldDetails.get("ROLE_NAME")));
			masterRoleTO.setRoleId(((BigDecimal) fieldDetails.get("ROLE_ID")).longValue());
			masterRoleTO.setRoleDescription(fieldDetails.get("DESCRIPTION").toString());

			/*
			 * if (user_org_level == 1) { if
			 * (IApplicationConstants.ROLE_TYPE.ROLE_ACSI
			 * .toString().equals(roleName)) {
			 * masterRoleTO.setDefaultSelection("selected"); } else if
			 * (IApplicationConstants
			 * .ROLE_TYPE.ROLE_USER.toString().equals(roleName)) {
			 * masterRoleTO.setDefaultSelection("selected"); } else {
			 * masterRoleTO.setDefaultSelection(""); } } else if (user_org_level
			 * == 3) { if
			 * (IApplicationConstants.ROLE_TYPE.ROLE_SCHOOL.toString()
			 * .equals(roleName)) {
			 * masterRoleTO.setDefaultSelection("selected"); } else if
			 * (IApplicationConstants
			 * .ROLE_TYPE.ROLE_USER.toString().equals(roleName)) {
			 * masterRoleTO.setDefaultSelection("selected"); } else {
			 * masterRoleTO.setDefaultSelection(""); } } else if (user_org_level
			 * == 4) { if
			 * (IApplicationConstants.ROLE_TYPE.ROLE_CLASS.toString().
			 * equals(roleName)) { masterRoleTO.setDefaultSelection("selected");
			 * } else if
			 * (IApplicationConstants.ROLE_TYPE.ROLE_USER.toString().equals
			 * (roleName)) { masterRoleTO.setDefaultSelection("selected"); }
			 * else { masterRoleTO.setDefaultSelection(""); } }
			 */
			if (user_org_level != Long.valueOf(b.getString("user.not.added"))) {
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
	@CacheEvict(value = "adminCache", allEntries = true)
	public boolean updateUser(String Id, String userId, String userName, String emailId, String password, String userStatus, String[] userRoles) throws BusinessException, Exception {
		logger.log(IAppLogger.INFO, "Enter: updateUser()");
		try {
			boolean ldapFlag = true;
			if (password != null && !"".equals(password)) {

				// Check if password contains part of user name
				if (password.equalsIgnoreCase(userName) || password.toLowerCase().indexOf(userName.toLowerCase()) != -1 || userName.toLowerCase().indexOf(password.toLowerCase()) != -1) {
					throw new BusinessException(propertyLookup.get("script.user.passwordPartUsername"));
				}

				if (IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
					ldapFlag = ldapManager.updateUser(userId, userId, userId, password);
				} else {
					String salt = PasswordGenerator.getNextSalt();
					getJdbcTemplatePrism().update(IQueryConstants.UPDATE_PASSWORD_DATA, IApplicationConstants.FLAG_N,
							SaltedPasswordEncoder.encryptPassword(password, Utils.getSaltWithUser(userId, salt)), salt, userId);
					ldapFlag = true;
				}
			}
			if (ldapFlag) {
				// update user table
				getJdbcTemplatePrism().update(IQueryConstants.UPDATE_USER, userId, userName, emailId, userStatus, Id);
				// delete from userRole table
				getJdbcTemplatePrism().update(IQueryConstants.DELETE_USER_ROLE, Id);

				if (userRoles != null) {
					logger.log(IAppLogger.DEBUG, IApplicationConstants.DEFAULT_USER_ROLE);
					// getJdbcTemplatePrism().update(IQueryConstants.INSERT_USER_ROLE,
					// IApplicationConstants.DEFAULT_USER_ROLE, Id);
					for (String role : userRoles) {
						logger.log(IAppLogger.DEBUG, role);
						getJdbcTemplatePrism().update(IQueryConstants.INSERT_USER_ROLE, role, Id);
					}
				}
			} else {
				return false;
			}

		} catch (BusinessException bex) {
			throw new BusinessException(bex.getCustomExceptionMessage());
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while updating user details.", e);
			throw new Exception(e);
		}
		return true;
	}

	/**
	 * Returns boolean.
	 * 
	 * @param Id
	 *            userId, userName, password
	 * @return
	 */

	@CacheEvict(value = "adminCache", allEntries = true)
	public boolean deleteUser(Map<String, Object> paramMap) /*throws Exception*/ {
		logger.log(IAppLogger.INFO, "Enter: deleteUser()");
		//try {
			// if (ldapManager.deleteUser(userName, userName, userName)) {
			// delete the security answers from pwd_hint_answers table
			String Id = (String) paramMap.get("Id");
			String userName = (String) paramMap.get("userName");
			String purpose = (String) paramMap.get("purpose");
			getJdbcTemplatePrism().update(IQueryConstants.DELETE_ANSWER_DATA, Id);

			// delete the roles assigned to the user from user_role table
			getJdbcTemplatePrism().update(IQueryConstants.DELETE_USER_ROLE, Id);

			if (IApplicationConstants.PURPOSE.equals(purpose)) {
				// delete from edu_center_user_link
				getJdbcTemplatePrism().update(IQueryConstants.DELETE_EDU_USER, Id);
			} else {
				// delete from org_users table
				getJdbcTemplatePrism().update(IQueryConstants.DELETE_ORG_USER, Id);
			}

			// delete the user from users table
			getJdbcTemplatePrism().update(IQueryConstants.DELETE_USER, Id);

			// delete user from LDAP
			/*if (IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
				ldapManager.deleteUser(userName, userName, userName);
			}*/
			// } else {
			// return false;
			// }
		/*} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while deleting user details.", e);
			throw new Exception(e);
		}*/
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#addNewUser(java.util.Map)
	 */
	@CacheEvict(value = "adminCache", allEntries = true)
	public UserTO addNewUser(Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: addNewUser()");
		UserTO to = null;
		final String userName = (String) paramMap.get("userName");
		final String password = (String) paramMap.get("password");
		final String userDisplayName = (String) paramMap.get("userDisplayName");
		final String emailId = (String) paramMap.get("emailId");
		final String userStatus = (String) paramMap.get("userStatus");
		final String customerId = (String) paramMap.get("customer");
		final String tenantId = (String) paramMap.get("tenantId");
		final String orgLevel = (String) paramMap.get("orgLevel");
		final String adminYear = (String) paramMap.get("adminYear");
		String[] userRoles = (String[]) paramMap.get("userRoles");
		final StringBuilder roles = new StringBuilder();
		if (userRoles != null) {
			/*
			 * getJdbcTemplatePrism().update( IQueryConstants.INSERT_USER_ROLE, IApplicationConstants.DEFAULT_USER_ROLE, user_seq_id);
			 */
			for (String role : userRoles) {
				roles.append(role).append(",");
			}
			
			roles.replace(roles.lastIndexOf(","), roles.lastIndexOf(",")+1, "");
			
		}
		 
		
		
		//String purpose = (String) paramMap.get("purpose");
		
		try {
		logger.log(IAppLogger.INFO, "Add User");
		
		String nodeId = (String) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
			String salt = PasswordGenerator.getNextSalt();
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(IQueryConstants.CREATE_USER);
				cs.setString(1, userName);
				cs.setString(2, userDisplayName);
				cs.setString(3, emailId);
				cs.setString(4, userStatus);
				cs.setString(5, IApplicationConstants.FLAG_Y);
				cs.setString(6, SaltedPasswordEncoder.encryptPassword(password, Utils.getSaltWithUser(userName, salt)));
				cs.setString(7, salt);
				cs.setString(8, IApplicationConstants.FLAG_N);
				cs.setLong(9, Long.valueOf(customerId));
				cs.setLong(10, Long.valueOf(tenantId));
				cs.setLong(11, Long.valueOf(orgLevel));
				cs.setLong(12, Long.valueOf(adminYear));
				cs.setString(13, IApplicationConstants.ACTIVE_FLAG);
				cs.setString(14, roles.toString());
				cs.registerOutParameter(15, oracle.jdbc.OracleTypes.NUMBER);
				cs.registerOutParameter(16, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				String strNodeId = null; 
				try {
					cs.execute();
					strNodeId =  cs.getString(15);
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return strNodeId;
			}
		});
		logger.log(IAppLogger.INFO, "User added in org : " + nodeId);
		
		if (nodeId != null) {
			paramMap.put("nodeId", nodeId);
			to = getEditUserData(paramMap);
		}
		
		}finally {
			logger.log(IAppLogger.INFO, "Exit: addNewUser()");
		}		
		return to;
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
	public ArrayList<UserTO> searchUser(String userName, String tenantId, String adminYear, String isExactSearch, String orgMode) {
		ArrayList<UserTO> UserTOs = new ArrayList<UserTO>();
		ArrayList<RoleTO> RoleTOs = new ArrayList<RoleTO>();
		List<Map<String, Object>> userslist = null;
		if (IApplicationConstants.FLAG_N.equalsIgnoreCase(isExactSearch)) {
			userName = CustomStringUtil.appendString("%", userName, "%");
			// List<OrgTO> orgList = null;
			userslist = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_USER,orgMode, tenantId, tenantId, userName, userName, userName, IApplicationConstants.ROLE_PARENT_ID, adminYear, "15");
		} else {
			userslist = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_USER_EXACT,orgMode, tenantId, tenantId, userName, IApplicationConstants.ROLE_PARENT_ID, adminYear, "15");
		}
		if (userslist.size() > 0) {
			UserTOs = new ArrayList<UserTO>();
			for (Map<String, Object> fieldDetails : userslist) {

				UserTO to = new UserTO();
				long userId = ((BigDecimal) fieldDetails.get("USER_ID")).longValue();
				to.setUserId(userId);
				/*
				 * to.setUserId(((BigDecimal) fieldDetails.get("USER_ID")) .longValue());
				 */
				to.setUserName((String) (fieldDetails.get("USERNAME")));
				to.setUserDisplayName((String) (fieldDetails.get("FULLNAME")));
				to.setStatus((String) (fieldDetails.get("STATUS")));
				to.setTenantId(((BigDecimal) fieldDetails.get("ORG_ID")).longValue());
				to.setParentId(((BigDecimal) fieldDetails.get("ORG_PARENT_ID")).longValue());
				to.setTenantName((String) (fieldDetails.get("ORG_NAME")));
				// to.setUserType((String) (fieldDetails.get("USER_TYPE")));
				try {
					to.setLoggedInOrgId(Long.parseLong(tenantId));
				} catch (NumberFormatException e) {
					// TODO : ??
				}
				// fetching role for each users
				if ((String.valueOf(userId) != null) && ((String) (fieldDetails.get("USERNAME")) != null)) {
					List<Map<String, Object>> roleList = null;
					roleList = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_ROLE, userId);
					if (roleList.size() > 0) {
						RoleTOs = new ArrayList<RoleTO>();
						for (Map<String, Object> roleDetails : roleList) {

							RoleTO rleTo = new RoleTO();
							rleTo.setRoleId(((BigDecimal) roleDetails.get("ROLEID")).longValue());
							rleTo.setRoleName((String) (roleDetails.get("ROLE_NAME")));
							rleTo.setRoleDescription((String) (roleDetails.get("DESCRIPTION")));
							rleTo.setLabel((String) (roleDetails.get("ORG_LABEL")));
							RoleTOs.add(rleTo);
						}
						to.setAvailableRoleList(RoleTOs);
					}
				}
				UserTOs.add(to);
			}
		}
		return UserTOs;
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
	@SuppressWarnings("unchecked")
	public List<EduCenterTO> searchEduUser(Map<String, Object> paramMap) {
		String userName = (String) paramMap.get("userName");
		String tenantId = (String) paramMap.get("tenantId");
		String isExactSearch = (String) paramMap.get("isExactSearch");
		List<EduCenterTO> eduCenterTOList = null;
		@SuppressWarnings("rawtypes")
		List placeHolderValueList = new ArrayList();
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
		String userName = (String) paramMap.get("term");
		String tenantId = (String) paramMap.get("selectedOrg");
		String adminYear = (String) paramMap.get("adminYear");
		String orgMode = (String) paramMap.get("orgMode");
		
		userName = CustomStringUtil.appendString("%", userName, "%");
		String userListJsonString = null;
		List<Map<String, Object>> listOfUser = null;
		if (tenantId != null) {
			listOfUser = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_USER, orgMode, tenantId, tenantId, userName, userName, userName, IApplicationConstants.ROLE_PARENT_ID, adminYear, "100");
		}
		if (listOfUser != null && listOfUser.size() > 0) {
			userListJsonString = "[";
			for (Map<String, Object> data : listOfUser) {
				userListJsonString = CustomStringUtil.appendString(userListJsonString, "\"", (String) data.get("USERNAME"), "<br/>", (String) data.get("FULLNAME"), "\",");
			}
			userListJsonString = CustomStringUtil.appendString(userListJsonString.substring(0, userListJsonString.length() - 1), "]");
		}
		logger.log(IAppLogger.DEBUG, userListJsonString);
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
	// @Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #p4, #root.method.name )")
	public List<OrgTO> getOrganizationChildren(String nodeId, String adminYear, String searchParam, long customerId, String orgMode) {
		logger.log(IAppLogger.INFO, "Enter: getOrganizationChildren()");
		logger.log(IAppLogger.DEBUG, "orgMode=" + orgMode);
		String parentTenantId = "";
		String orgId = "";
		List<OrgTO> orgList = new ArrayList<OrgTO>();
		List<Map<String, Object>> lstData = null;
		if (nodeId.indexOf("_") > 0) {
			orgId = nodeId.substring((nodeId.indexOf("_") + 1), nodeId.length());
			parentTenantId = nodeId.substring(0, nodeId.indexOf("_"));
			logger.log(IAppLogger.DEBUG, "orgId=" + orgId);
			if (searchParam != null && searchParam.trim().length() > 0) {
				searchParam = CustomStringUtil.appendString("%", searchParam, "%");
				lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ORGANIZATION_CHILDREN_LIST_ON_SCROLL_WITH_SRCH_PARAM, parentTenantId, parentTenantId, parentTenantId, orgMode,searchParam,customerId, adminYear);
			} else {
				lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ORGANIZATION_CHILDREN_LIST_ON_SCROLL, parentTenantId, customerId, parentTenantId, orgId, orgMode, customerId, adminYear, parentTenantId);
			}
			orgList = getOrgList(lstData, adminYear);
		} else {
			parentTenantId = nodeId;
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ORGANIZATION_CHILDREN_LIST, parentTenantId, customerId, adminYear, parentTenantId, orgMode, customerId,adminYear, parentTenantId);
			orgList = getOrgList(lstData, adminYear);
			logger.log(IAppLogger.DEBUG, lstData.size() + "");
		}
		logger.log(IAppLogger.INFO, "Exit: getOrganizationChildren()");
		return orgList;
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

	public OrgTO getTotalUserCount(String tenantId, String adminYear, long customerId, String orgMode) {
		OrgTO orgTO = null;
		Map<String, Object> userCount = getJdbcTemplatePrism().queryForMap(IQueryConstants.GET_USER_COUNT, customerId, orgMode, adminYear, tenantId, customerId, tenantId, adminYear, adminYear);
		if (userCount != null && userCount.size() > 0) {
			orgTO = new OrgTO();
			orgTO.setNoOfUsers(((BigDecimal) userCount.get("USER_NO")).longValue());
			orgTO.setAdminName((String) userCount.get("ADMIN_NAME"));
		}
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
	public List<OrgTO> searchOrganization(String orgName, String tenantId, String adminYear, long customerId, String orgMode) {
		orgName = CustomStringUtil.appendString("%", orgName, "%");
		List<OrgTO> orgList = null;
		List<Map<String, Object>> list = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_ORGANNIZATION, customerId, adminYear, orgName, orgMode, adminYear, tenantId, tenantId);
		if (list != null && list.size() > 0) {
			orgList = new ArrayList<OrgTO>();
			for (Map<String, Object> data : list) {
				OrgTO orgTO = new OrgTO();
				orgTO.setTenantId(Long.valueOf(data.get("ORG_NODEID").toString()));
				orgTO.setTenantName((String) data.get("ORG_NODE_NAME"));
				orgTO.setNoOfChildOrgs(Long.valueOf(data.get("CHILD_ORG_NO").toString()));
				// orgTO.setNoOfUsers(((BigDecimal)
				// data.get("USER_NO")).longValue());
				orgList.add(orgTO);
			}
		}
		return orgList;
	}

	/**
	 * Searches and returns the organization names(use like operator) as a JSON string. Performs case insensitive searching. This method is used to perform auto complete in search box.
	 * 
	 * @param orgName
	 *            Search String treated as organization name
	 * @param tenantId
	 *            tenantID of the logged in user
	 */
	public String searchOrgAutoComplete(String orgName, String tenantId, String adminYear, long customerId, String orgMode) {
		orgName = CustomStringUtil.appendString("%", orgName, "%");
		// List<OrgTO> orgList = null;
		String orgListJsonString = null;
		List<Map<String, Object>> list = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_ORG_AUTO_COMPLETE, customerId, orgMode, adminYear, tenantId, orgName);
		if (list != null && list.size() > 0) {
			orgListJsonString = "[";
			for (Map<String, Object> data : list) {
				String orgNameStr = (String) data.get("ORG_NODE_NAME");
				orgListJsonString = CustomStringUtil.appendString(orgListJsonString, "\"", orgNameStr, "\",");
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
	public ArrayList<RoleTO> getRoleDetails() {
		ArrayList<RoleTO> RoleTOs = new ArrayList<RoleTO>();
		List<Map<String, Object>> lstData = null;
		lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ROLE_DETAILS);
		if (lstData.size() > 0) {
			RoleTOs = new ArrayList<RoleTO>();
			for (Map<String, Object> fieldDetails : lstData) {
				RoleTO to = new RoleTO();
				to.setRoleId(((BigDecimal) fieldDetails.get("ROLE_ID")).longValue());
				to.setRoleName((String) (fieldDetails.get("ROLE_NAME")));
				to.setRoleDescription((String) (fieldDetails.get("DESCRIPTION")));
				RoleTOs.add(to);
			}
		}
		return RoleTOs;
	}

	/**
	 * Get role details
	 * 
	 * @param role
	 * @return RoleTO
	 */
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
					long userId = userTo.getUserId();
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
	public String resetPassword(String userName) throws Exception {
		String password = PasswordGenerator.getNext();
		if (IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
			boolean isUpdated = ldapManager.updateUser(userName, userName, userName, password);
			if (isUpdated) {
				getJdbcTemplatePrism().update(IQueryConstants.UPDATE_FIRSTTIMEUSERFLAG_DATA, IApplicationConstants.FLAG_Y, userName);
				return password;
			} else {
				return null;
			}
		} else {
			String salt = PasswordGenerator.getNextSalt();
			getJdbcTemplatePrism().update(IQueryConstants.UPDATE_PASSWORD_DATA, IApplicationConstants.FLAG_Y, SaltedPasswordEncoder.encryptPassword(password, Utils.getSaltWithUser(userName, salt)),
					salt, userName);
			return password;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#getAllAdmin()
	 */
	@Cacheable(value = "configCache", key="'getAllAdmin'.concat(#root.method.name)")
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
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey( #p0, #p1, #p2, #p3, #root.method.name )")
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
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getHierarchy') )")
	public ArrayList<OrgTreeTO> getHierarchy(Map<String, Object> paramMap) throws Exception {
		String nodeId = (String) paramMap.get("nodeid");
		String currOrg = (String) paramMap.get("currOrg");
		boolean isFirstLoad = (Boolean) paramMap.get("isFirstLoad");
		String adminYear = (String) paramMap.get("adminYear");
		long customerId = Long.valueOf(paramMap.get("customerId").toString());
		String orgMode = (String) paramMap.get("orgMode");
		String selectedLevelOrgId = (String) paramMap.get("selectedLevelOrgId");

		logger.log(IAppLogger.INFO, "nodeid=" + nodeId);
		logger.log(IAppLogger.INFO, "currOrg=" + currOrg);
		logger.log(IAppLogger.INFO, "isFirstLoad=" + isFirstLoad);
		logger.log(IAppLogger.INFO, "adminYear=" + adminYear);
		logger.log(IAppLogger.INFO, "customerId=" + customerId);
		logger.log(IAppLogger.INFO, "orgMode=" + orgMode);

		ArrayList<OrgTreeTO> OrgTreeTOs = new ArrayList<OrgTreeTO>();
		List<Map<String, Object>> lstData = null;
		if ("-1".equals(selectedLevelOrgId)) {
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_TENANT_DETAILS, adminYear, orgMode, nodeId, customerId);
		} else {
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_TENANT_DETAILS_NON_ACSI, nodeId, selectedLevelOrgId, customerId);
		}
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				OrgTO to = new OrgTO();
				OrgTreeTO treeTo = new OrgTreeTO();
				to.setId(Long.valueOf(fieldDetails.get("ORG_NODEID").toString()));
				to.setParentTenantId(Long.valueOf(fieldDetails.get("PARENT_ORG_NODEID").toString()));
				to.setOrgLevel(Long.valueOf(fieldDetails.get("ORG_NODE_LEVEL").toString()));
				to.setTenantName(fieldDetails.get("ORG_NODE_NAME").toString());

				treeTo.setState("closed");
				treeTo.setOrgTreeId(Long.valueOf(fieldDetails.get("ORG_NODEID").toString()));
				treeTo.setData((String) (fieldDetails.get("ORG_NODE_NAME")));
				treeTo.setMetadata(to);
				treeTo.setAttr(to);
				OrgTreeTOs.add(treeTo);
			}
		}
		return OrgTreeTOs;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#loadEduCenterUsers(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public List<EduCenterTO> loadEduCenterUsers(final Map<String, Object> paramMap) throws SystemException {
		logger.log(IAppLogger.INFO, "Enter: loadEduCenterUsers()");
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) paramMap.get("loggedinUserTO");
		long eduCenterId = ((Long) paramMap.get("eduCenterId")).longValue();
		String searchParam = (String) paramMap.get("searchParam");
		String lastEduCenterId_username = (String) paramMap.get("lastEduCenterId_username");
		String userName = "";

		@SuppressWarnings("rawtypes")
		List placeHolderValueList = new ArrayList();
		List<EduCenterTO> eduCenterTOList = null;
		try {
			if (lastEduCenterId_username.indexOf("_") > 0) {
				userName = lastEduCenterId_username.substring((lastEduCenterId_username.indexOf("_") + 1), lastEduCenterId_username.length());
				logger.log(IAppLogger.DEBUG, "userName=" + userName);
				if (searchParam != null && searchParam.trim().length() > 0) {
					searchParam = CustomStringUtil.appendString("%", searchParam, "%");
					placeHolderValueList.add(loggedinUserTO.getCustomerId());
					placeHolderValueList.add(eduCenterId);
					placeHolderValueList.add(userName);
					placeHolderValueList.add(userName);
					placeHolderValueList.add(userName);
					placeHolderValueList.add(searchParam);
					placeHolderValueList.add(IApplicationConstants.RECORD_PER_PAGE);
					eduCenterTOList = getJdbcTemplatePrism()
							.query(IQueryConstants.GET_EDU_CENTER_USERS_ON_SCROLL_SEARCH, placeHolderValueList.toArray(), new EduCenterTOMapper(getJdbcTemplatePrism()));
				} else {
					placeHolderValueList.add(loggedinUserTO.getCustomerId());
					placeHolderValueList.add(eduCenterId);
					placeHolderValueList.add(userName);
					placeHolderValueList.add(IApplicationConstants.RECORD_PER_PAGE);
					eduCenterTOList = getJdbcTemplatePrism().query(IQueryConstants.GET_EDU_CENTER_USERS_ON_SCROLL, placeHolderValueList.toArray(), new EduCenterTOMapper(getJdbcTemplatePrism()));
				}
			} else {
				placeHolderValueList.add(loggedinUserTO.getCustomerId());
				placeHolderValueList.add(eduCenterId);
				placeHolderValueList.add(IApplicationConstants.RECORD_PER_PAGE);
				eduCenterTOList = getJdbcTemplatePrism().query(IQueryConstants.GET_EDU_CENTER_USERS_FIRST_LOAD, placeHolderValueList.toArray(), new EduCenterTOMapper(getJdbcTemplatePrism()));
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred in loadEduCenterUsers():", e);
			throw new SystemException(e);
		} finally {
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
}
