package com.ctb.prism.admin.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;

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
	@Cacheable(cacheName = "orgTreeChildren")
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
					lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_TENANT_DETAILS_NON_ACSI, adminYear, orgMode, currOrg, customerId, orgLevel);
					logger.log(IAppLogger.DEBUG, "Tree for non TASC Users...Currorg=" + currOrg);
				} else {
					lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_TENANT_DETAILS, adminYear, orgMode, orgParentId, customerId);
				}
			} else {
				lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_TENANT_DETAILS, adminYear, orgMode, nodeId, customerId);
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
	// @Cacheable(cacheName = "orgTreeChildren")
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

	/**
	 * Returns the organizationList to create a tree structure. for manage organizations
	 * 
	 * @param nodeid
	 * @return
	 */
	public ArrayList<OrgTreeTO> getOrgTree(Map<String, Object> paramMap) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: getOrgTree()");
		ArrayList<OrgTreeTO> OrgTreeTOs = new ArrayList<OrgTreeTO>();

		String nodeId = (String) paramMap.get("nodeid");
		String currOrg = (String) paramMap.get("currOrg");
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
	@Cacheable(cacheName = "orgUsers")
	public ArrayList<UserTO> getUserDetailsOnClick(String nodeId, String currorg, String adminYear, String searchParam, String customerid) {
		logger.log(IAppLogger.INFO, "Enter: getUserDetailsOnClick()");
		logger.log(IAppLogger.INFO, "nodeId=" + nodeId);
		logger.log(IAppLogger.INFO, "currorg=" + currorg);
		logger.log(IAppLogger.INFO, "adminYear=" + adminYear);
		logger.log(IAppLogger.INFO, "searchParam=" + searchParam);
		logger.log(IAppLogger.INFO, "customerid=" + customerid);
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
				lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_DETAILS_ON_SCROLL_WITH_SRCH_PARAM, customerid, tenantId, adminYear, userName, searchParam, searchParam,
						searchParam);
			} else {
				logger.log(IAppLogger.DEBUG, "GET_USER_DETAILS_ON_SCROLL");
				lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_DETAILS_ON_SCROLL, customerid, tenantId, adminYear, userName);
			}
		} else {
			logger.log(IAppLogger.DEBUG, "GET_USER_DETAILS_ON_FIRST_LOAD");
			tenantId = nodeId;
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_DETAILS_ON_FIRST_LOAD, customerid, tenantId, adminYear);
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
		List<Map<String, Object>> lstRoleData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_ROLE_ON_EDIT, nodeId, nodeId, IApplicationConstants.ROLE_TYPE.ROLE_CTB.toString());
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
			lstMasterRoleData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ROLE_ADD, IApplicationConstants.ROLE_TYPE.ROLE_CTB.toString());
		}else{
			lstMasterRoleData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ROLE, userid, userid, userid, IApplicationConstants.ROLE_TYPE.ROLE_CTB.toString());
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
	@TriggersRemove(cacheName = "orgUsers", removeAll = true)
	public boolean updateUser(String Id, String userId, String userName, String emailId, String password, String userStatus, String[] userRoles) throws BusinessException, Exception {
		logger.log(IAppLogger.INFO, "Enter: AdminDAOImpl - updateUser");
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

	@TriggersRemove(cacheName = "orgUsers", removeAll = true)
	public boolean deleteUser(Map<String, Object> paramMap) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: AdminDAOImpl - deleteUser");
		try {
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
			if (IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
				ldapManager.deleteUser(userName, userName, userName);
			}
			// } else {
			// return false;
			// }
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while deleting user details.", e);
			throw new Exception(e);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#addNewUser(java.util.Map)
	 */
	@TriggersRemove(cacheName = "orgUsers", removeAll = true)
	public UserTO addNewUser(Map<String, Object> paramMap) throws BusinessException, Exception {
		logger.log(IAppLogger.INFO, "Enter: AdminDAOImpl - addNewUser");
		UserTO to = null;
		String userName = (String) paramMap.get("userName");
		String password = (String) paramMap.get("password");
		String userDisplayName = (String) paramMap.get("userDisplayName");
		String emailId = (String) paramMap.get("emailId");
		String userStatus = (String) paramMap.get("userStatus");
		String customerId = (String) paramMap.get("customer");
		String tenantId = (String) paramMap.get("tenantId");
		String orgLevel = (String) paramMap.get("orgLevel");
		String adminYear = (String) paramMap.get("adminYear");
		String[] userRoles = (String[]) paramMap.get("userRoles");
		String purpose = (String) paramMap.get("purpose");
		try {
			if (IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
				// As discussed,no need to handle edu_center_user for
				// ldapManager
				if (!ldapManager.searchUser(userName)) {

					List<Map<String, Object>> userMap = getJdbcTemplatePrism().queryForList(IQueryConstants.CHECK_EXISTING_USER, userName);
					if (userMap == null || userMap.isEmpty()) {
						if (ldapManager.addUser(userName, userName, userName, password)) {
							long user_seq_id = getJdbcTemplatePrism().queryForLong(IQueryConstants.USER_SEQ_ID);
							if (user_seq_id != 0) {
								getJdbcTemplatePrism().update(IQueryConstants.INSERT_USER, user_seq_id, userName, userDisplayName, emailId, userStatus, IApplicationConstants.FLAG_Y,
										IApplicationConstants.FLAG_Y, customerId);
								long orgUserSeqId = getJdbcTemplatePrism().queryForLong(IQueryConstants.USER_SEQ_ID);
								getJdbcTemplatePrism().update(IQueryConstants.INSERT_ORG_USER, orgUserSeqId, user_seq_id, tenantId, orgLevel, adminYear, IApplicationConstants.ACTIVE_FLAG);
								if (userRoles != null) {
									getJdbcTemplatePrism().update(IQueryConstants.INSERT_USER_ROLE, IApplicationConstants.DEFAULT_USER_ROLE, user_seq_id);
									for (String role : userRoles) {
										getJdbcTemplatePrism().update(IQueryConstants.INSERT_USER_ROLE, role, user_seq_id);
									}
								}
							}
							String nodeId = String.valueOf(user_seq_id);
							paramMap.put("nodeId", nodeId);
							to = getEditUserData(paramMap);
						}
						// ldapManager.addUser(userName, userName, userName,
						// password);
					}
				}
			} else {
				// code to insert user in DAO only
				List<Map<String, Object>> userMap = getJdbcTemplatePrism().queryForList(IQueryConstants.CHECK_EXISTING_USER, userName);
				if (userMap == null || userMap.isEmpty()) { // user not present
															// in DB so insert
															// new

					// Check if password contains part of user name
					if (password.equalsIgnoreCase(userName) || password.toLowerCase().indexOf(userName.toLowerCase()) != -1 || userName.toLowerCase().indexOf(password.toLowerCase()) != -1) {
						throw new BusinessException(propertyLookup.get("script.user.passwordPartUsername"));
					}

					long user_seq_id = getJdbcTemplatePrism().queryForLong(IQueryConstants.USER_SEQ_ID);
					if (user_seq_id != 0) {
						String salt = PasswordGenerator.getNextSalt();
						getJdbcTemplatePrism().update(IQueryConstants.INSERT_USER_WITH_PASSWORD, user_seq_id, userName, userDisplayName, emailId, userStatus, IApplicationConstants.FLAG_Y,
								SaltedPasswordEncoder.encryptPassword(password, Utils.getSaltWithUser(userName, salt)), salt, IApplicationConstants.FLAG_Y, customerId);
						if (IApplicationConstants.PURPOSE.equals(purpose)) {
							// Insert into edu_center_user_link
							String eduCenterId = (String) paramMap.get("eduCenterId");
							getJdbcTemplatePrism().update(IQueryConstants.INSERT_EDU_CENTER_USER, eduCenterId, user_seq_id);
						} else {
							// insert into org_users
							long orgUserSeqId = getJdbcTemplatePrism().queryForLong(IQueryConstants.USER_SEQ_ID);
							getJdbcTemplatePrism().update(IQueryConstants.INSERT_ORG_USER, orgUserSeqId, user_seq_id, tenantId, orgLevel, adminYear, IApplicationConstants.ACTIVE_FLAG);
						}

						if (userRoles != null) {
							/*
							 * getJdbcTemplatePrism().update( IQueryConstants.INSERT_USER_ROLE, IApplicationConstants.DEFAULT_USER_ROLE, user_seq_id);
							 */
							for (String role : userRoles) {
								getJdbcTemplatePrism().update(IQueryConstants.INSERT_USER_ROLE, role, user_seq_id);
							}
						}
					}
					String nodeId = String.valueOf(user_seq_id);
					paramMap.put("nodeId", nodeId);
					to = getEditUserData(paramMap);
				}
			}
		} catch (BusinessException bex) {
			throw new BusinessException(bex.getCustomExceptionMessage());
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while adding user details.", e);
			throw new Exception(e);
		}
		logger.log(IAppLogger.INFO, "Exit: AdminDAOImpl - addNewUser");
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
	public ArrayList<UserTO> searchUser(String userName, String tenantId, String adminYear, String isExactSearch) {
		ArrayList<UserTO> UserTOs = new ArrayList<UserTO>();
		ArrayList<RoleTO> RoleTOs = new ArrayList<RoleTO>();
		List<Map<String, Object>> userslist = null;
		if (IApplicationConstants.FLAG_N.equalsIgnoreCase(isExactSearch)) {
			userName = CustomStringUtil.appendString("%", userName, "%");
			// List<OrgTO> orgList = null;
			userslist = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_USER, tenantId, userName, userName, userName, adminYear, "15");
		} else {
			userslist = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_USER_EXACT, tenantId, userName, adminYear, "15");
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
		String purpose = (String) paramMap.get("purpose");
		userName = CustomStringUtil.appendString("%", userName, "%");
		String userListJsonString = null;
		List<Map<String, Object>> listOfUser = null;
		if (IApplicationConstants.PURPOSE.equals(purpose)) {
			listOfUser = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_EDU_USER, userName, userName, userName, tenantId, "100");
		} else {
			listOfUser = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_USER, tenantId, userName, userName, userName, adminYear, "100");
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
		logger.log(IAppLogger.INFO, "Enter: AdminDAOImpl - getOrganizationList");
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ORGANIZATION_LIST, tenantId, tenantId, adminYear, customerId, tenantId, adminYear, customerId);
		List<OrgTO> orgList = getOrgList(lstData, adminYear);
		logger.log(IAppLogger.INFO, "Exit: AdminDAOImpl - getOrganizationList");
		return orgList;
	}

	/**
	 * Returns all the children of an organization.
	 * 
	 * @param String
	 *            tenantId
	 * @return list of orgTO
	 */
	@Cacheable(cacheName = "orgChildren")
	public List<OrgTO> getOrganizationChildren(String nodeId, String adminYear, String searchParam, long customerId) {
		logger.log(IAppLogger.INFO, "Enter: AdminDAOImpl - getOrganizationChildren");
		String parentTenantId = "";
		String orgId = "";
		List<OrgTO> orgList = null;
		List<Map<String, Object>> lstData = null;
		if (nodeId.indexOf("_") > 0) {
			orgId = nodeId.substring((nodeId.indexOf("_") + 1), nodeId.length());
			parentTenantId = nodeId.substring(0, nodeId.indexOf("_"));
			logger.log(IAppLogger.DEBUG, "orgId=" + orgId);
			if (searchParam != null && searchParam.trim().length() > 0) {
				searchParam = CustomStringUtil.appendString("%", searchParam, "%");
				lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ORGANIZATION_CHILDREN_LIST_ON_SCROLL_WITH_SRCH_PARAM, parentTenantId, parentTenantId, parentTenantId, orgId,
						searchParam);
			} else {
				lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ORGANIZATION_CHILDREN_LIST_ON_SCROLL, parentTenantId, customerId, parentTenantId, orgId, customerId, parentTenantId);
			}
			orgList = getOrgList(lstData, adminYear);
		} else {
			parentTenantId = nodeId;
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ORGANIZATION_CHILDREN_LIST, parentTenantId, customerId, parentTenantId, customerId, parentTenantId);
			orgList = getOrgList(lstData, adminYear);
			logger.log(IAppLogger.DEBUG, lstData.size() + "");
		}
		logger.log(IAppLogger.INFO, "Exit: AdminDAOImpl - getOrganizationChildren");
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
		List<OrgTO> orgList = null;
		if (queryData != null && queryData.size() > 0) {
			orgList = new ArrayList<OrgTO>();
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

	public OrgTO getTotalUserCount(String tenantId, String adminYear, long customerId) {
		OrgTO orgTO = null;
		Map<String, Object> userCount = getJdbcTemplatePrism().queryForMap(IQueryConstants.GET_USER_COUNT, adminYear, tenantId, adminYear, customerId, adminYear);
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
	public List<OrgTO> searchOrganization(String orgName, String tenantId, String adminYear, long customerId) {
		orgName = CustomStringUtil.appendString("%", orgName, "%");
		List<OrgTO> orgList = null;
		List<Map<String, Object>> list = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_ORGANNIZATION, customerId, orgName, tenantId, tenantId);
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
	public String searchOrgAutoComplete(String orgName, String tenantId, String adminYear, long customerId) {
		orgName = CustomStringUtil.appendString("%", orgName, "%");
		// List<OrgTO> orgList = null;
		String orgListJsonString = null;
		List<Map<String, Object>> list = getJdbcTemplatePrism().queryForList(IQueryConstants.SEARCH_ORG_AUTO_COMPLETE, customerId, tenantId, orgName);
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
		logger.log(IAppLogger.INFO, "Enter: AdminDAOImpl - updateRole");
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
		logger.log(IAppLogger.INFO, "Exit: AdminDAOImpl - updateRole");
		return true;
	}

	/**
	 * Delete selected role
	 * 
	 * @param
	 * @return
	 */
	public boolean deleteRole(String roleid) {
		logger.log(IAppLogger.INFO, "Enter: AdminDAOImpl - deleteRole");
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
		logger.log(IAppLogger.INFO, "Exit: AdminDAOImpl - deleteRole");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#associateUserToRole(java.lang.String, java.lang.String)
	 */
	public boolean associateUserToRole(String roleId, String userName) {
		logger.log(IAppLogger.INFO, "Enter: AdminDAOImpl - associateUserToRole");
		try {
			// update role table
			getJdbcTemplatePrism().update(IQueryConstants.INSERT_INTO_USER_ROLE, userName, roleId);

		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while associating user to role.", e);
			return false;
		}
		logger.log(IAppLogger.INFO, "Exit: AdminDAOImpl - associateUserToRole");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#deleteUserFromRole(java.lang.String, java.lang.String)
	 */
	public boolean deleteUserFromRole(String roleId, String userId) {
		logger.log(IAppLogger.INFO, "Enter: AdminDAOImpl - deleteUserFromRole");
		try {
			// update role table
			getJdbcTemplatePrism().update(IQueryConstants.DELETE_USER_FROM_USER_ROLE_TABLE, roleId, userId);

		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while deleting user for role.", e);
			return false;
		}
		logger.log(IAppLogger.INFO, "Exit: AdminDAOImpl - deleteUserFromRole");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#saveRole(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean saveRole(String roleId, String roleName, String roleDescription) {
		logger.log(IAppLogger.INFO, "Enter: AdminDAOImpl - updateRole");
		try {
			// update role table
			getJdbcTemplatePrism().update(IQueryConstants.UPDATE_ROLE, roleName, roleDescription, roleId);

		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while updating role details.", e);
			return false;
		}
		logger.log(IAppLogger.INFO, "Exit: AdminDAOImpl - updateRole");
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
	@Cacheable(cacheName = "allAdminYear")
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
	@Cacheable(cacheName = "studentsForDownload")
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
	@Cacheable(cacheName = "hierarchyForDownload")
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
	public List<StudentDataTO> downloadStudentFile(final Map<String, Object> paramMap) throws SystemException {
		logger.log(IAppLogger.INFO, "Enter: AdminDAOImpl - downloadStudentFile()");
		String userId = (String) paramMap.get("userId");
		String startDate = (String) paramMap.get("startDate");
		String endDate = (String) paramMap.get("endDate");
		SimpleDateFormat sdf = new SimpleDateFormat(StudentDataConstants.DATE_FORMAT);
		Date today = new Date();
		if ((startDate == null) || (startDate.equals("null")) || (startDate.length() < 1)) {
			startDate = StudentDataConstants.MINUS_INFINITY_DATE;
		}
		if ((endDate == null) || (endDate.equals("null")) || (endDate.length() < 1)) {
			endDate = sdf.format(today);
		}
		logger.log(IAppLogger.INFO, "userId=" + userId + ",startString=" + startDate + ",endString=" + endDate);
		List<StudentDataTO> studentDataList = new ArrayList<StudentDataTO>();
		List<Map<String, Object>> studentSSFDataList = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_STUDENT_DATA, userId, startDate, endDate);
		if (!studentSSFDataList.isEmpty()) {
			for (Map<String, Object> fieldDetails : studentSSFDataList) {
				StudentDataTO s = new StudentDataTO();
				s.setCustID(StudentDataUtil.paddedWrap(StudentDataConstants.COL001, fieldDetails.get("PRISM_CUSTOMER_ID")));
				s.setRosterID(StudentDataUtil.paddedWrap(StudentDataConstants.COL002, fieldDetails.get("ROSTER_ID")));
				String orgNodeLevel = fieldDetails.get("ORG_NODE_LEVEL").toString();
				if ("1".equals(orgNodeLevel)) {
					s.setHierarchyA_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, fieldDetails.get("ELEMENT_HIERARCHY_NAME")));
					s.setHierarchyA_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, fieldDetails.get("ELEMENT_ELEMENT_NUMBER"), '0', StudentDataConstants.ALIGN_RIGHT));
					s.setHierarchyA_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, fieldDetails.get("ELEMENT_SPECIAL_CODES")));
					s.setHierarchyB_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyB_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyB_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyC_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyC_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyC_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyD_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyD_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyD_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyE_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyE_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyE_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyF_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyF_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyF_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyG_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyG_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyG_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
				} else if ("2".equals(orgNodeLevel)) {
					s.setHierarchyB_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, fieldDetails.get("ELEMENT_HIERARCHY_NAME")));
					s.setHierarchyB_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, fieldDetails.get("ELEMENT_ELEMENT_NUMBER"), '0', StudentDataConstants.ALIGN_RIGHT));
					s.setHierarchyB_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, fieldDetails.get("ELEMENT_SPECIAL_CODES")));
					s.setHierarchyA_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyA_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyA_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyC_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyC_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyC_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyD_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyD_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyD_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyE_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyE_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyE_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyF_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyF_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyF_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyG_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyG_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyG_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
				} else if ("3".equals(orgNodeLevel)) {
					s.setHierarchyC_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, fieldDetails.get("ELEMENT_HIERARCHY_NAME")));
					s.setHierarchyC_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, fieldDetails.get("ELEMENT_ELEMENT_NUMBER"), '0', StudentDataConstants.ALIGN_RIGHT));
					s.setHierarchyC_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, fieldDetails.get("ELEMENT_SPECIAL_CODES")));
					s.setHierarchyA_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyA_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyA_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyB_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyB_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyB_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyD_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyD_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyD_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyE_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyE_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyE_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyF_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyF_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyF_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyG_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyG_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyG_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
				} else if ("4".equals(orgNodeLevel)) {
					s.setHierarchyD_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, fieldDetails.get("ELEMENT_HIERARCHY_NAME")));
					s.setHierarchyD_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, fieldDetails.get("ELEMENT_ELEMENT_NUMBER"), '0', StudentDataConstants.ALIGN_RIGHT));
					s.setHierarchyD_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, fieldDetails.get("ELEMENT_SPECIAL_CODES")));
					s.setHierarchyA_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyA_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyA_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyB_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyB_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyB_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyC_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyC_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyC_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyE_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyE_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyE_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyF_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyF_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyF_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyG_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyG_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyG_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
				} else if ("5".equals(orgNodeLevel)) {
					s.setHierarchyE_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, fieldDetails.get("ELEMENT_HIERARCHY_NAME")));
					s.setHierarchyE_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, fieldDetails.get("ELEMENT_ELEMENT_NUMBER"), '0', StudentDataConstants.ALIGN_RIGHT));
					s.setHierarchyE_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, fieldDetails.get("ELEMENT_SPECIAL_CODES")));
					s.setHierarchyA_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyA_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyA_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyB_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyB_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyB_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyC_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyC_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyC_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyD_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyD_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyD_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyF_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyF_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyF_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyG_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyG_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyG_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
				} else if ("6".equals(orgNodeLevel)) {
					s.setHierarchyF_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, fieldDetails.get("ELEMENT_HIERARCHY_NAME")));
					s.setHierarchyF_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, fieldDetails.get("ELEMENT_ELEMENT_NUMBER"), '0', StudentDataConstants.ALIGN_RIGHT));
					s.setHierarchyF_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, fieldDetails.get("ELEMENT_SPECIAL_CODES")));
					s.setHierarchyA_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyA_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyA_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyB_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyB_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyB_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyC_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyC_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyC_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyD_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyD_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyD_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyE_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyE_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyE_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyG_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyG_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyG_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
				} else if ("7".equals(orgNodeLevel)) {
					s.setHierarchyG_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, fieldDetails.get("ELEMENT_HIERARCHY_NAME")));
					s.setHierarchyG_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, fieldDetails.get("ELEMENT_ELEMENT_NUMBER"), '0', StudentDataConstants.ALIGN_RIGHT));
					s.setHierarchyG_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, fieldDetails.get("ELEMENT_SPECIAL_CODES")));
					s.setHierarchyA_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyA_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyA_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyB_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyB_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyB_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyC_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyC_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyC_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyD_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyD_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyD_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyE_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyE_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyE_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
					s.setHierarchyF_Name(StudentDataUtil.paddedWrap(StudentDataConstants.COL003, ""));
					s.setHierarchyF_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL004, ""));
					s.setHierarchyF_SpCodes(StudentDataUtil.paddedWrap(StudentDataConstants.COL005, ""));
				}
				s.setLastName(StudentDataUtil.paddedWrap(StudentDataConstants.COL024, fieldDetails.get("LAST_NAME")));
				s.setFirstName(StudentDataUtil.paddedWrap(StudentDataConstants.COL025, fieldDetails.get("FIRST_NAME")));
				s.setMiddleInitial(StudentDataUtil.paddedWrap(StudentDataConstants.COL026, fieldDetails.get("MIDDLE_NAME")));
				s.setDateOfBirth(StudentDataUtil.paddedWrap(StudentDataConstants.COL027, fieldDetails.get("BIRTHDATE")));
				s.setForm(StudentDataUtil.paddedWrap(StudentDataConstants.COL028, fieldDetails.get("TEST_FORM")));
				s.setChangedFormFlag(StudentDataUtil.paddedWrap(StudentDataConstants.COL029, fieldDetails.get("FORM_CHANGE")));
				s.setTestLang(StudentDataUtil.paddedWrap(StudentDataConstants.COL030, fieldDetails.get("TEST_LANGUAGE")));
				s.setTestMode(StudentDataUtil.paddedWrap(StudentDataConstants.COL031, fieldDetails.get("TEST_MODE")));
				s.setGender(StudentDataUtil.paddedWrap(StudentDataConstants.COL032, fieldDetails.get("GENDER")));
				s.setResolvedEthnicity(StudentDataUtil.paddedWrap(StudentDataConstants.COL033, fieldDetails.get("RESOLVED_ETHNICITY")));
				s.setStudentID((String) fieldDetails.get("EXAMINEE_ID"));
				s.setPhoneNumber(StudentDataUtil.paddedWrap(StudentDataConstants.COL035, fieldDetails.get("EXAMINEE_PHONE")));
				s.setLithocode(StudentDataUtil.paddedWrap(StudentDataConstants.COL036, fieldDetails.get("LITHOCODE")));
				s.setImagingID(StudentDataUtil.paddedWrap(StudentDataConstants.COL037, fieldDetails.get("IMAGING_ID")));
				s.setStreetAddress(StudentDataUtil.paddedWrap(StudentDataConstants.COL038, fieldDetails.get("ADDRESS")));
				s.setAptNo(StudentDataUtil.paddedWrap(StudentDataConstants.COL039, fieldDetails.get("APT")));
				s.setCity(StudentDataUtil.paddedWrap(StudentDataConstants.COL040, fieldDetails.get("CITY")));
				s.setState(StudentDataUtil.paddedWrap(StudentDataConstants.COL041, fieldDetails.get("STATE")));
				s.setZipCode(StudentDataUtil.paddedWrap(StudentDataConstants.COL042, fieldDetails.get("ZIP")));
				s.setCounty_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL043, fieldDetails.get("COUNTY_CODE")));
				s.setEduc_Center_Code(StudentDataUtil.paddedWrap(StudentDataConstants.COL044, fieldDetails.get("EDUCATION_CENTER_CODE")));
				s.setAcc_Audio_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL045, fieldDetails.get("RD_AUDIO_ALT_PRESN")));
				s.setAcc_Audio_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL046, fieldDetails.get("WR_AUDIO_ALT_PRESN")));
				s.setAcc_Audio_Math1(StudentDataUtil.paddedWrap(StudentDataConstants.COL047, fieldDetails.get("MATH_PART1_AUDIO_ALT_PRESN")));
				s.setAcc_Audio_Math2(StudentDataUtil.paddedWrap(StudentDataConstants.COL048, fieldDetails.get("MATH_PART2_AUDIO_ALT_PRESN")));
				s.setAcc_Audio_Sc(StudentDataUtil.paddedWrap(StudentDataConstants.COL049, fieldDetails.get("SCI_AUDIO_ALT_PRESN")));
				s.setAcc_Audio_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL050, fieldDetails.get("SS_AUDIO_ALT_PRESN")));
				s.setAcc_Breaks_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL051, fieldDetails.get("RD_BREAKS")));
				s.setAcc_Breaks_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL052, fieldDetails.get("WR_BREAKS")));
				s.setAcc_Breaks_Math1(StudentDataUtil.paddedWrap(StudentDataConstants.COL053, fieldDetails.get("MATH_PART1_BREAKS")));
				s.setAcc_Breaks_Math2(StudentDataUtil.paddedWrap(StudentDataConstants.COL054, fieldDetails.get("MATH_PART2_BREAKS")));
				s.setAcc_Breaks_SC(StudentDataUtil.paddedWrap(StudentDataConstants.COL055, fieldDetails.get("SCI_BREAKS")));
				s.setAcc_Breaks_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL056, fieldDetails.get("SS_BREAKS")));
				s.setAcc_Calculator_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL057, fieldDetails.get("RD_CALCULATOR")));
				s.setAcc_Calculator_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL058, fieldDetails.get("WR_CALCULATOR")));
				s.setAcc_Calculator_Math1(StudentDataUtil.paddedWrap(StudentDataConstants.COL059, fieldDetails.get("MATH_PART1_CALCULATOR")));
				s.setAcc_Calculator_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL060, fieldDetails.get("SS_CALCULATOR")));
				s.setAcc_Duration1_25_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL061, fieldDetails.get("RD_DUR_1_25T")));
				s.setAcc_Duration1_25_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL062, fieldDetails.get("WR_DUR_1_25T")));
				s.setAcc_Duration1_25_Math1(StudentDataUtil.paddedWrap(StudentDataConstants.COL063, fieldDetails.get("MATH_PART1_DUR_1_25T")));
				s.setAcc_Duration1_25_Math2(StudentDataUtil.paddedWrap(StudentDataConstants.COL064, fieldDetails.get("MATH_PART2_DUR_1_25T")));
				s.setAcc_Duration1_25_Sc(StudentDataUtil.paddedWrap(StudentDataConstants.COL065, fieldDetails.get("SCI_DUR_1_25T")));
				s.setAcc_Duration1_25_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL066, fieldDetails.get("SS_DUR_1_25T")));
				s.setAcc_Duration1_5_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL067, fieldDetails.get("RD_DUR_1_5T")));
				s.setAcc_Duration1_5_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL068, fieldDetails.get("WR_DUR_1_5T")));
				s.setAcc_Duration1_5_Math1(StudentDataUtil.paddedWrap(StudentDataConstants.COL069, fieldDetails.get("MATH_PART1_DUR_1_5T")));
				s.setAcc_Duration1_5_Math2(StudentDataUtil.paddedWrap(StudentDataConstants.COL070, fieldDetails.get("MATH_PART2_DUR_1_5T")));
				s.setAcc_Duration1_5_Sc(StudentDataUtil.paddedWrap(StudentDataConstants.COL071, fieldDetails.get("SCI_DUR_1_5T")));
				s.setAcc_Duration1_5_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL072, fieldDetails.get("SS_DUR_1_5T")));
				s.setAcc_Duration2_0_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL073, fieldDetails.get("RD_DUR_2T")));
				s.setAcc_Duration2_0_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL074, fieldDetails.get("WR_DUR_2T")));
				s.setAcc_Duration2_0_Math1(StudentDataUtil.paddedWrap(StudentDataConstants.COL075, fieldDetails.get("MATH_PART1_DUR_2T")));
				s.setAcc_Duration2_0_Math2(StudentDataUtil.paddedWrap(StudentDataConstants.COL076, fieldDetails.get("MATH_PART2_DUR_2T")));
				s.setAcc_Duration2_0_Sc(StudentDataUtil.paddedWrap(StudentDataConstants.COL077, fieldDetails.get("SCI_DUR_2T")));
				s.setAcc_Duration2_0_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL078, fieldDetails.get("SS_DUR_2T")));
				s.setAcc_PhysicalSupport_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL079, fieldDetails.get("RD_PHYSICAL_SUPP")));
				s.setAcc_PhysicalSupport_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL080, fieldDetails.get("WR_PHYSICAL_SUPP")));
				s.setAcc_PhysicalSupport_Math1(StudentDataUtil.paddedWrap(StudentDataConstants.COL081, fieldDetails.get("MATH_PART1_PHYSICAL_SUPP")));
				s.setAcc_PhysicalSupport_Math2(StudentDataUtil.paddedWrap(StudentDataConstants.COL082, fieldDetails.get("MATH_PART2_PHYSICAL_SUPP")));
				s.setAcc_PhysicalSupport_Sc(StudentDataUtil.paddedWrap(StudentDataConstants.COL083, fieldDetails.get("SCI_PHYSICAL_SUPP")));
				s.setAcc_PhysicalSupport_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL084, fieldDetails.get("SS_PHYSICAL_SUPP")));
				s.setAcc_Scribe_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL085, fieldDetails.get("RD_SCRIBE")));
				s.setAcc_Scribe_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL086, fieldDetails.get("WR_SCRIBE")));
				s.setAcc_Scribe_Math1(StudentDataUtil.paddedWrap(StudentDataConstants.COL087, fieldDetails.get("MATH_PART1_SCRIBE")));
				s.setAcc_Scribe_Math2(StudentDataUtil.paddedWrap(StudentDataConstants.COL088, fieldDetails.get("MATH_PART2_SCRIBE")));
				s.setAcc_Scribe_Sc(StudentDataUtil.paddedWrap(StudentDataConstants.COL089, fieldDetails.get("SCI_SCRIBE")));
				s.setAcc_Scribe_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL090, fieldDetails.get("SS_SCRIBE")));
				s.setAcc_TechDevice_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL091, fieldDetails.get("RD_TECNOLOGY_DEVICE")));
				s.setAcc_TechDevice_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL092, fieldDetails.get("WR_TECHNOLOGY_DEVICE")));
				s.setAcc_TechDevice_Math1(StudentDataUtil.paddedWrap(StudentDataConstants.COL093, fieldDetails.get("MATH_PART1_TECNOLOGY_DEVICE")));
				s.setAcc_TechDevice_Math2(StudentDataUtil.paddedWrap(StudentDataConstants.COL094, fieldDetails.get("MATH_PART2_TECNOLOGY_DEVICE")));
				s.setAcc_TechDevice_Sc(StudentDataUtil.paddedWrap(StudentDataConstants.COL095, fieldDetails.get("SCI_TECHNOLOGY_DEVICE")));
				s.setAcc_TechDevice_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL096, fieldDetails.get("SS_TECHNOLOGY_DEVICE")));
				s.setAcc_SepRoom_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL097, fieldDetails.get("RD_SEPARATE_ROOM")));
				s.setAcc_SepRoom_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL098, fieldDetails.get("WR_SEPARATE_ROOM")));
				s.setAcc_SepRoom_Math1(StudentDataUtil.paddedWrap(StudentDataConstants.COL099, fieldDetails.get("MATH_PART1_SEPARATE_ROOM")));
				s.setAcc_SepRoom_Math2(StudentDataUtil.paddedWrap(StudentDataConstants.COL100, fieldDetails.get("MATH_PART2_SEPARATE_ROOM")));
				s.setAcc_SepRoom_Sc(StudentDataUtil.paddedWrap(StudentDataConstants.COL101, fieldDetails.get("SCI_SEPARATE_ROOM")));
				s.setAcc_SepRoom_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL102, fieldDetails.get("SS_SEPARATE_ROOM")));
				s.setAcc_SmGroup_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL103, fieldDetails.get("RD_SMALL_GRP_SETTING")));
				s.setAcc_SmGroup_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL104, fieldDetails.get("WR_SMALL_GRP_SETTING")));
				s.setAcc_SmGroup_Math1(StudentDataUtil.paddedWrap(StudentDataConstants.COL105, fieldDetails.get("MATH_PART1_SMALL_GRP_SETTING")));
				s.setAcc_SmGroup_Math2(StudentDataUtil.paddedWrap(StudentDataConstants.COL106, fieldDetails.get("MATH_PART2_SMALL_GRP_SETTING")));
				s.setAcc_SmGroup_Sc(StudentDataUtil.paddedWrap(StudentDataConstants.COL107, fieldDetails.get("SCI_SMALL_GRP_SETTING")));
				s.setAcc_SmGroup_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL108, fieldDetails.get("SS_SMALL_GRP_SETTING")));
				s.setAcc_Other_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL109, fieldDetails.get("RD_OTHER")));
				s.setAcc_Other_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL110, fieldDetails.get("WR_OTHER")));
				s.setAcc_Other_Math1(StudentDataUtil.paddedWrap(StudentDataConstants.COL111, fieldDetails.get("MATH_PART1_OTHER")));
				s.setAcc_Other_Math2(StudentDataUtil.paddedWrap(StudentDataConstants.COL112, fieldDetails.get("MATH_PART2_OTHER")));
				s.setAcc_Other_Sc(StudentDataUtil.paddedWrap(StudentDataConstants.COL113, fieldDetails.get("SCI_OTHER")));
				s.setAcc_Other_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL114, fieldDetails.get("SS_OTHER")));
				s.setExaminer_Ack_Accomm(StudentDataUtil.paddedWrap(StudentDataConstants.COL115, fieldDetails.get("WAIVER")));
				s.setHomeLang(StudentDataUtil.paddedWrap(StudentDataConstants.COL116, fieldDetails.get("HOME_LANGUAGE")));
				s.setK_12_Educ_Completed(StudentDataUtil.paddedWrap(StudentDataConstants.COL117, fieldDetails.get("K_12_EDUC_COMPLETED")));
				s.setSubj_Taken_Passed_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL118, fieldDetails.get("RD_PASSED")));
				s.setSubj_Taken_Passed_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL119, fieldDetails.get("WR_PASSED")));
				s.setSubj_Taken_Passed_Math(StudentDataUtil.paddedWrap(StudentDataConstants.COL120, fieldDetails.get("MATH_PASSED")));
				s.setSubj_Taken_Passed_Sc(StudentDataUtil.paddedWrap(StudentDataConstants.COL121, fieldDetails.get("SCI_PASSED")));
				s.setSubj_Taken_Passed_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL122, fieldDetails.get("SS_PASSED")));
				s.setSubj_Taken_Passed_None(StudentDataUtil.paddedWrap(StudentDataConstants.COL123, fieldDetails.get("NONE_PASSED")));
				s.setNo_Times_Taken_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL124, fieldDetails.get("RD_T_TAKEN")));
				s.setNo_Times_Taken_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL125, fieldDetails.get("WR_T_TAKEN")));
				s.setNo_Times_Taken_Math(StudentDataUtil.paddedWrap(StudentDataConstants.COL126, fieldDetails.get("MATH_T_TAKEN")));
				s.setNo_Times_Taken_Sc(StudentDataUtil.paddedWrap(StudentDataConstants.COL127, fieldDetails.get("SCI_T_TAKEN")));
				s.setNo_Times_Taken_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL128, fieldDetails.get("SS_T_TAKEN")));
				s.setRetake_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL129, fieldDetails.get("RD_RETAKE")));
				s.setRetake_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL130, fieldDetails.get("WR_RETAKE")));
				s.setRetake_Math(StudentDataUtil.paddedWrap(StudentDataConstants.COL131, fieldDetails.get("MATH_RETAKE")));
				s.setRetake_Sc(StudentDataUtil.paddedWrap(StudentDataConstants.COL132, fieldDetails.get("SCI_RETAKE")));
				s.setRetake_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL133, fieldDetails.get("SS_RETAKE")));
				s.setRetake_None(StudentDataUtil.paddedWrap(StudentDataConstants.COL134, fieldDetails.get("NONE_RETAKE")));
				s.setTake_Readiness_Assessment(StudentDataUtil.paddedWrap(StudentDataConstants.COL135, fieldDetails.get("READINESS_ASSESSMENT")));
				s.setPrepare_County_Prog(StudentDataUtil.paddedWrap(StudentDataConstants.COL136, fieldDetails.get("COUNTY_PROGRAM")));
				s.setPrepare_Sch_Dist_Prog(StudentDataUtil.paddedWrap(StudentDataConstants.COL137, fieldDetails.get("DISTRICT_PROGRAM")));
				s.setPrepare_Military_Prog(StudentDataUtil.paddedWrap(StudentDataConstants.COL138, fieldDetails.get("MILITARY_PROGRAM")));
				s.setPrepare_Religious_Prog(StudentDataUtil.paddedWrap(StudentDataConstants.COL139, fieldDetails.get("RELIGIOUS_PROGRAM")));
				s.setPrepare_Purchased_My_Own_Study_Books(StudentDataUtil.paddedWrap(StudentDataConstants.COL140, fieldDetails.get("PURCHASED_BOOKS")));
				s.setPrepare_Online_Study_Prog(StudentDataUtil.paddedWrap(StudentDataConstants.COL141, fieldDetails.get("ONLINE_STUDY_PROGRAM")));
				s.setPrepare_Homeschool(StudentDataUtil.paddedWrap(StudentDataConstants.COL142, fieldDetails.get("HOMESCHOOL")));
				s.setPrepare_Tutor(StudentDataUtil.paddedWrap(StudentDataConstants.COL143, fieldDetails.get("TUTOR")));
				s.setPrepare_Self_Taught(StudentDataUtil.paddedWrap(StudentDataConstants.COL144, fieldDetails.get("SELF_TAUGHT")));
				s.setRecent_Class_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL145, fieldDetails.get("RD_PAST_3MONTHS")));
				s.setRecent_Class_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL146, fieldDetails.get("WR_PAST_3MONTHS")));
				s.setRecent_Class_Math(StudentDataUtil.paddedWrap(StudentDataConstants.COL147, fieldDetails.get("MATH_PAST_3MONTHS")));
				s.setRecent_Class_Sc(StudentDataUtil.paddedWrap(StudentDataConstants.COL148, fieldDetails.get("SCI_PAST_3MONTHS")));
				s.setRecent_Class_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL149, fieldDetails.get("SS_PAST_3MONTHS")));
				s.setMonths_Studied_Subj_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL150, fieldDetails.get("RD_HOW_MANY_MONTHS")));
				s.setMonths_Studied_Subj_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL151, fieldDetails.get("WR_HOW_MANY_MONTHS")));
				s.setMonths_Studied_Subj_Math(StudentDataUtil.paddedWrap(StudentDataConstants.COL152, fieldDetails.get("MATH_HOW_MANY_MONTHS")));
				s.setMonths_Studied_Subj_Sc(StudentDataUtil.paddedWrap(StudentDataConstants.COL153, fieldDetails.get("SCI_HOW_MANY_MONTHS")));
				s.setMonths_Studied_Subj_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL154, fieldDetails.get("SS_HOW_MANY_MONTHS")));
				s.setGrade_in_Subj_Rd(StudentDataUtil.paddedWrap(StudentDataConstants.COL155, fieldDetails.get("RD_WHAT_GRADE")));
				s.setGrade_in_Subj_Wr(StudentDataUtil.paddedWrap(StudentDataConstants.COL156, fieldDetails.get("WR_WHAT_GRADE")));
				s.setGrade_in_Subj_Math(StudentDataUtil.paddedWrap(StudentDataConstants.COL157, fieldDetails.get("MATH_WHAT_GRADE")));
				s.setGrade_in_Subj_Sc(StudentDataUtil.paddedWrap(StudentDataConstants.COL158, fieldDetails.get("SCI_WHAT_GRADE")));
				s.setGrade_in_Subj_SS(StudentDataUtil.paddedWrap(StudentDataConstants.COL159, fieldDetails.get("SS_WHAT_GRADE")));
				s.setTestFormat_Braille(StudentDataUtil.paddedWrap(StudentDataConstants.COL160, fieldDetails.get("BRAILLE")));
				s.setTestFormat_LP(StudentDataUtil.paddedWrap(StudentDataConstants.COL161, fieldDetails.get("LARGE_PRINT")));
				s.setLocal_Field_1(StudentDataUtil.paddedWrap(StudentDataConstants.COL162, fieldDetails.get("LOCAL_USE_1")));
				s.setLocal_Field_2(StudentDataUtil.paddedWrap(StudentDataConstants.COL163, fieldDetails.get("LOCAL_USE_2")));
				s.setLocal_Field_3(StudentDataUtil.paddedWrap(StudentDataConstants.COL164, fieldDetails.get("LOCAL_USE_3")));
				s.setLocal_Field_4(StudentDataUtil.paddedWrap(StudentDataConstants.COL165, fieldDetails.get("LOCAL_USE_4")));
				s.setLocal_Field_5(StudentDataUtil.paddedWrap(StudentDataConstants.COL166, fieldDetails.get("LOCAL_USE_5")));
				s.setLocal_Field_6(StudentDataUtil.paddedWrap(StudentDataConstants.COL167, fieldDetails.get("LOCAL_USE_6")));
				s.setLocal_Field_7(StudentDataUtil.paddedWrap(StudentDataConstants.COL168, fieldDetails.get("LOCAL_USE_7")));
				s.setLocal_Field_8(StudentDataUtil.paddedWrap(StudentDataConstants.COL169, fieldDetails.get("LOCAL_USE_8")));
				s.setLocal_Field_9(StudentDataUtil.paddedWrap(StudentDataConstants.COL170, fieldDetails.get("LOCAL_USE_9")));
				s.setLocal_Field_10(StudentDataUtil.paddedWrap(StudentDataConstants.COL171, fieldDetails.get("LOCAL_USE_10")));
				s.setLocal_Field_11(StudentDataUtil.paddedWrap(StudentDataConstants.COL172, fieldDetails.get("LOCAL_USE_11")));
				s.setLocal_Field_12(StudentDataUtil.paddedWrap(StudentDataConstants.COL173, fieldDetails.get("LOCAL_USE_12")));
				s.setLocal_Field_13(StudentDataUtil.paddedWrap(StudentDataConstants.COL174, fieldDetails.get("LOCAL_USE_13")));
				s.setLocal_Field_14(StudentDataUtil.paddedWrap(StudentDataConstants.COL175, fieldDetails.get("LOCAL_USE_14")));
				s.setLocal_Field_15(StudentDataUtil.paddedWrap(StudentDataConstants.COL176, fieldDetails.get("LOCAL_USE_15")));
				s.setLocal_Field_16(StudentDataUtil.paddedWrap(StudentDataConstants.COL177, fieldDetails.get("LOCAL_USE_16")));
				s.setLocal_Field_17(StudentDataUtil.paddedWrap(StudentDataConstants.COL178, fieldDetails.get("LOCAL_USE_17")));
				s.setLocal_Field_18(StudentDataUtil.paddedWrap(StudentDataConstants.COL179, fieldDetails.get("LOCAL_USE_18")));
				s.setLocal_Field_19(StudentDataUtil.paddedWrap(StudentDataConstants.COL180, fieldDetails.get("LOCAL_USE_19")));
				s.setLocal_Field_20(StudentDataUtil.paddedWrap(StudentDataConstants.COL181, fieldDetails.get("LOCAL_USE_20")));
				s.setCandidate_Acknowledgement(StudentDataUtil.paddedWrap(StudentDataConstants.COL182, fieldDetails.get("ACKNOWLEDGEMENT")));
				s.setReading_dateTestTaken(StudentDataUtil.paddedWrap(StudentDataConstants.COL183, fieldDetails.get("DATE_TEST_TAKEN_RD_SUBTEST")));
				s.setReading_numberCorrect(StudentDataUtil.paddedWrap(StudentDataConstants.COL184, fieldDetails.get("RD_NUMBER_CORRECT")));
				s.setReading_scaleScore(StudentDataUtil.paddedWrap(StudentDataConstants.COL185, fieldDetails.get("RD_SCALE_SCORE")));
				s.setReading_hSE_Score(StudentDataUtil.paddedWrap(StudentDataConstants.COL186, fieldDetails.get("RD_HIGH_SC_EQ")));
				s.setReading_percentileRank(StudentDataUtil.paddedWrap(StudentDataConstants.COL187, fieldDetails.get("RD_PERCENTILE_RANK")));
				s.setReading_nCE(StudentDataUtil.paddedWrap(StudentDataConstants.COL188, fieldDetails.get("RD_NORMAL_CURVE_EQ")));
				s.setReading_std_Obj_Mstry_Scr_1(StudentDataUtil.paddedWrap(StudentDataConstants.COL189, fieldDetails.get("RD_OBJ_1_MASTERY")));
				s.setReading_not_All_items_atmpt_1(StudentDataUtil.paddedWrap(StudentDataConstants.COL190, fieldDetails.get("RD_OBJ_1_NOT_ALL_ATMPT_FLAG")));
				s.setReading_std_Obj_Mstry_Scr_2(StudentDataUtil.paddedWrap(StudentDataConstants.COL191, fieldDetails.get("RD_OBJ_2_MASTERY")));
				s.setReading_not_All_items_atmpt_2(StudentDataUtil.paddedWrap(StudentDataConstants.COL192, fieldDetails.get("RD_OBJ_2_NOT_ALL_ATMPT_FLAG")));
				s.setReading_std_Obj_Mstry_Scr_3(StudentDataUtil.paddedWrap(StudentDataConstants.COL193, fieldDetails.get("RD_OBJ_3_MASTERY")));
				s.setReading_not_All_items_atmpt_3(StudentDataUtil.paddedWrap(StudentDataConstants.COL194, fieldDetails.get("RD_OBJ_3_NOT_ALL_ATMPT_FLAG")));
				s.setReading_std_Obj_Mstry_Scr_4(StudentDataUtil.paddedWrap(StudentDataConstants.COL195, fieldDetails.get("RD_OBJ_4_MASTERY")));
				s.setReading_not_All_items_atmpt_4(StudentDataUtil.paddedWrap(StudentDataConstants.COL196, fieldDetails.get("RD_OBJ_4_NOT_ALL_ATMPT_FLAG")));
				s.setReading_std_Obj_Mstry_Scr_5(StudentDataUtil.paddedWrap(StudentDataConstants.COL197, fieldDetails.get("RD_OBJ_5_MASTERY")));
				s.setReading_not_All_items_atmpt_5(StudentDataUtil.paddedWrap(StudentDataConstants.COL198, fieldDetails.get("RD_OBJ_5_NOT_ALL_ATMPT_FLAG")));
				s.setReading_std_Obj_Mstry_Scr_6(StudentDataUtil.paddedWrap(StudentDataConstants.COL199, fieldDetails.get("RD_OBJ_6_MASTERY")));
				s.setReading_not_All_items_atmpt_6(StudentDataUtil.paddedWrap(StudentDataConstants.COL200, fieldDetails.get("RD_OBJ_6_NOT_ALL_ATMPT_FLAG")));
				s.setReading_std_Obj_Mstry_Scr_7(StudentDataUtil.paddedWrap(StudentDataConstants.COL201, fieldDetails.get("RD_OBJ_7_MASTERY")));
				s.setReading_not_All_items_atmpt_7(StudentDataUtil.paddedWrap(StudentDataConstants.COL202, fieldDetails.get("RD_OBJ_7_NOT_ALL_ATMPT_FLAG")));
				s.setReading_std_Obj_Mstry_Scr_8(StudentDataUtil.paddedWrap(StudentDataConstants.COL203, fieldDetails.get("RD_OBJ_8_MASTERY")));
				s.setReading_not_All_items_atmpt_8(StudentDataUtil.paddedWrap(StudentDataConstants.COL204, fieldDetails.get("RD_OBJ_8_NOT_ALL_ATMPT_FLAG")));
				s.setWriting_dateTestTaken(StudentDataUtil.paddedWrap(StudentDataConstants.COL205, fieldDetails.get("DATE_TEST_TAKEN_WR_SUBTEST")));
				s.setWriting_numberCorrect(StudentDataUtil.paddedWrap(StudentDataConstants.COL206, fieldDetails.get("WR_NUMBER_CORRECT")));
				s.setWriting_scaleScore(StudentDataUtil.paddedWrap(StudentDataConstants.COL207, fieldDetails.get("WR_SCALE_SCORE")));
				s.setWriting_hSE_Score(StudentDataUtil.paddedWrap(StudentDataConstants.COL208, fieldDetails.get("WR_HIGH_SC_EQ")));
				s.setWriting_percentileRank(StudentDataUtil.paddedWrap(StudentDataConstants.COL209, fieldDetails.get("WR_PERCENTILE_RANK")));
				s.setWriting_nCE(StudentDataUtil.paddedWrap(StudentDataConstants.COL210, fieldDetails.get("WR_NORMAL_CURVE_EQ")));
				s.setWriting_std_Obj_Mstry_Scr_1(StudentDataUtil.paddedWrap(StudentDataConstants.COL211, fieldDetails.get("WR_OBJ_1_MASTERY")));
				s.setWriting_not_All_items_atmpt_1(StudentDataUtil.paddedWrap(StudentDataConstants.COL212, fieldDetails.get("WR_OBJ_1_NOT_ALL_ATMPT_FLAG")));
				s.setWriting_std_Obj_Mstry_Scr_2(StudentDataUtil.paddedWrap(StudentDataConstants.COL213, fieldDetails.get("WR_OBJ_2_MASTERY")));
				s.setWriting_not_All_items_atmpt_2(StudentDataUtil.paddedWrap(StudentDataConstants.COL214, fieldDetails.get("WR_OBJ_2_NOT_ALL_ATMPT_FLAG")));
				s.setWriting_std_Obj_Mstry_Scr_3(StudentDataUtil.paddedWrap(StudentDataConstants.COL215, fieldDetails.get("WR_OBJ_3_MASTERY")));
				s.setWriting_not_All_items_atmpt_3(StudentDataUtil.paddedWrap(StudentDataConstants.COL216, fieldDetails.get("WR_OBJ_3_NOT_ALL_ATMPT_FLAG")));
				s.setWriting_std_Obj_Mstry_Scr_4(StudentDataUtil.paddedWrap(StudentDataConstants.COL217, fieldDetails.get("WR_OBJ_4_MASTERY")));
				s.setWriting_not_All_items_atmpt_4(StudentDataUtil.paddedWrap(StudentDataConstants.COL218, fieldDetails.get("WR_OBJ_4_NOT_ALL_ATMPT_FLAG")));
				s.setEla_scaleScore(StudentDataUtil.paddedWrap(StudentDataConstants.COL219, fieldDetails.get("ELA_SCALE_SCORE")));
				s.setEla_hSE_Score(StudentDataUtil.paddedWrap(StudentDataConstants.COL220, fieldDetails.get("ELA_HIGH_SC_EQ")));
				s.setEla_percentileRank(StudentDataUtil.paddedWrap(StudentDataConstants.COL221, fieldDetails.get("ELA_PERCENTILE_RANK")));
				s.setEla_nCE(StudentDataUtil.paddedWrap(StudentDataConstants.COL222, fieldDetails.get("ELA_NORMAL_CURVE_EQ")));
				s.setMath_dateTestTaken(StudentDataUtil.paddedWrap(StudentDataConstants.COL223, fieldDetails.get("DATE_TEST_TAKEN_MATH_SUBTEST")));
				s.setMath_numberCorrect(StudentDataUtil.paddedWrap(StudentDataConstants.COL224, fieldDetails.get("MATH_NUMBER_CORRECT")));
				s.setMath_scaleScore(StudentDataUtil.paddedWrap(StudentDataConstants.COL225, fieldDetails.get("MATH_SCALE_SCORE")));
				s.setMath_hSE_Score(StudentDataUtil.paddedWrap(StudentDataConstants.COL226, fieldDetails.get("MATH_HIGH_SC_EQ")));
				s.setMath_percentileRank(StudentDataUtil.paddedWrap(StudentDataConstants.COL227, fieldDetails.get("MATH_PERCENTILE_RANK")));
				s.setMath_nCE(StudentDataUtil.paddedWrap(StudentDataConstants.COL228, fieldDetails.get("MATH_NORMAL_CURVE_EQ")));
				s.setMath_std_Obj_Mstry_Scr_1(StudentDataUtil.paddedWrap(StudentDataConstants.COL229, fieldDetails.get("MATH_OBJ_1_MASTERY")));
				s.setMath_not_All_items_atmpt_1(StudentDataUtil.paddedWrap(StudentDataConstants.COL230, fieldDetails.get("MATH_OBJ_1_NOT_ALL_ATMPT_FLAG")));
				s.setMath_std_Obj_Mstry_Scr_2(StudentDataUtil.paddedWrap(StudentDataConstants.COL231, fieldDetails.get("MATH_OBJ_2_MASTERY")));
				s.setMath_not_All_items_atmpt_2(StudentDataUtil.paddedWrap(StudentDataConstants.COL232, fieldDetails.get("MATH_OBJ_2_NOT_ALL_ATMPT_FLAG")));
				s.setMath_std_Obj_Mstry_Scr_3(StudentDataUtil.paddedWrap(StudentDataConstants.COL233, fieldDetails.get("MATH_OBJ_3_MASTERY")));
				s.setMath_not_All_items_atmpt_3(StudentDataUtil.paddedWrap(StudentDataConstants.COL234, fieldDetails.get("MATH_OBJ_3_NOT_ALL_ATMPT_FLAG")));
				s.setMath_std_Obj_Mstry_Scr_4(StudentDataUtil.paddedWrap(StudentDataConstants.COL235, fieldDetails.get("MATH_OBJ_4_MASTERY")));
				s.setMath_not_All_items_atmpt_4(StudentDataUtil.paddedWrap(StudentDataConstants.COL236, fieldDetails.get("MATH_OBJ_4_NOT_ALL_ATMPT_FLAG")));
				s.setMath_std_Obj_Mstry_Scr_5(StudentDataUtil.paddedWrap(StudentDataConstants.COL237, fieldDetails.get("MATH_OBJ_5_MASTERY")));
				s.setMath_not_All_items_atmpt_5(StudentDataUtil.paddedWrap(StudentDataConstants.COL238, fieldDetails.get("MATH_OBJ_5_NOT_ALL_ATMPT_FLAG")));
				s.setMath_std_Obj_Mstry_Scr_6(StudentDataUtil.paddedWrap(StudentDataConstants.COL239, fieldDetails.get("MATH_OBJ_6_MASTERY")));
				s.setMath_not_All_items_atmpt_6(StudentDataUtil.paddedWrap(StudentDataConstants.COL240, fieldDetails.get("MATH_OBJ_6_NOT_ALL_ATMPT_FLAG")));
				s.setMath_std_Obj_Mstry_Scr_7(StudentDataUtil.paddedWrap(StudentDataConstants.COL241, fieldDetails.get("MATH_OBJ_7_MASTERY")));
				s.setMath_not_All_items_atmpt_7(StudentDataUtil.paddedWrap(StudentDataConstants.COL242, fieldDetails.get("MATH_OBJ_7_NOT_ALL_ATMPT_FLAG")));
				s.setScience_dateTestTaken(StudentDataUtil.paddedWrap(StudentDataConstants.COL243, fieldDetails.get("DATE_TEST_TAKEN_SCI_SUBTEST")));
				s.setScience_numberCorrect(StudentDataUtil.paddedWrap(StudentDataConstants.COL244, fieldDetails.get("SCI_NUMBER_CORRECT")));
				s.setScience_scaleScore(StudentDataUtil.paddedWrap(StudentDataConstants.COL245, fieldDetails.get("SCI_SCALE_SCORE")));
				s.setScience_hSE_Score(StudentDataUtil.paddedWrap(StudentDataConstants.COL246, fieldDetails.get("SCI_HIGH_SC_EQ")));
				s.setScience_percentileRank(StudentDataUtil.paddedWrap(StudentDataConstants.COL247, fieldDetails.get("SCI_PERCENTILE_RANK")));
				s.setScience_nCE(StudentDataUtil.paddedWrap(StudentDataConstants.COL248, fieldDetails.get("SCI_NORMAL_CURVE_EQ")));
				s.setScience_std_Obj_Mstry_Scr_1(StudentDataUtil.paddedWrap(StudentDataConstants.COL249, fieldDetails.get("SCI_OBJ_1_MASTERY")));
				s.setScience_not_All_items_atmpt_1(StudentDataUtil.paddedWrap(StudentDataConstants.COL250, fieldDetails.get("SCI_OBJ_1_NOT_ALL_ATMPT_FLAG")));
				s.setScience_std_Obj_Mstry_Scr_2(StudentDataUtil.paddedWrap(StudentDataConstants.COL251, fieldDetails.get("SCI_OBJ_2_MASTERY")));
				s.setScience_not_All_items_atmpt_2(StudentDataUtil.paddedWrap(StudentDataConstants.COL252, fieldDetails.get("SCI_OBJ_2_NOT_ALL_ATMPT_FLAG")));
				s.setScience_std_Obj_Mstry_Scr_3(StudentDataUtil.paddedWrap(StudentDataConstants.COL253, fieldDetails.get("SCI_OBJ_3_MASTERY")));
				s.setScience_not_All_items_atmpt_3(StudentDataUtil.paddedWrap(StudentDataConstants.COL254, fieldDetails.get("SCI_OBJ_3_NOT_ALL_ATMPT_FLAG")));
				s.setScience_std_Obj_Mstry_Scr_4(StudentDataUtil.paddedWrap(StudentDataConstants.COL255, fieldDetails.get("SCI_OBJ_4_MASTERY")));
				s.setScience_not_All_items_atmpt_4(StudentDataUtil.paddedWrap(StudentDataConstants.COL256, fieldDetails.get("SCI_OBJ_4_NOT_ALL_ATMPT_FLAG")));
				s.setScience_std_Obj_Mstry_Scr_5(StudentDataUtil.paddedWrap(StudentDataConstants.COL257, fieldDetails.get("SCI_OBJ_5_MASTERY")));
				s.setScience_not_All_items_atmpt_5(StudentDataUtil.paddedWrap(StudentDataConstants.COL258, fieldDetails.get("SCI_OBJ_5_NOT_ALL_ATMPT_FLAG")));
				s.setSocial_dateTestTaken(StudentDataUtil.paddedWrap(StudentDataConstants.COL259, fieldDetails.get("DATE_TEST_TAKEN_SS_SUBTEST")));
				s.setSocial_numberCorrect(StudentDataUtil.paddedWrap(StudentDataConstants.COL260, fieldDetails.get("SOC_NUMBER_CORRECT")));
				s.setSocial_scaleScore(StudentDataUtil.paddedWrap(StudentDataConstants.COL261, fieldDetails.get("SOC_SCALE_SCORE")));
				s.setSocial_hSE_Score(StudentDataUtil.paddedWrap(StudentDataConstants.COL262, fieldDetails.get("SOC_HIGH_SC_EQ")));
				s.setSocial_percentileRank(StudentDataUtil.paddedWrap(StudentDataConstants.COL263, fieldDetails.get("SOC_PERCENTILE_RANK")));
				s.setSocial_nCE(StudentDataUtil.paddedWrap(StudentDataConstants.COL264, fieldDetails.get("SOC_NORMAL_CURVE_EQ")));
				s.setSocial_std_Obj_Mstry_Scr_1(StudentDataUtil.paddedWrap(StudentDataConstants.COL265, fieldDetails.get("SOC_OBJ_1_MASTERY")));
				s.setSocial_not_All_items_atmpt_1(StudentDataUtil.paddedWrap(StudentDataConstants.COL266, fieldDetails.get("SOC_OBJ_1_NOT_ALL_ATMPT_FLAG")));
				s.setSocial_std_Obj_Mstry_Scr_2(StudentDataUtil.paddedWrap(StudentDataConstants.COL267, fieldDetails.get("SOC_OBJ_2_MASTERY")));
				s.setSocial_not_All_items_atmpt_2(StudentDataUtil.paddedWrap(StudentDataConstants.COL268, fieldDetails.get("SOC_OBJ_2_NOT_ALL_ATMPT_FLAG")));
				s.setSocial_std_Obj_Mstry_Scr_3(StudentDataUtil.paddedWrap(StudentDataConstants.COL269, fieldDetails.get("SOC_OBJ_3_MASTERY")));
				s.setSocial_not_All_items_atmpt_3(StudentDataUtil.paddedWrap(StudentDataConstants.COL270, fieldDetails.get("SOC_OBJ_3_NOT_ALL_ATMPT_FLAG")));
				s.setSocial_std_Obj_Mstry_Scr_4(StudentDataUtil.paddedWrap(StudentDataConstants.COL271, fieldDetails.get("SOC_OBJ_4_MASTERY")));
				s.setSocial_not_All_items_atmpt_4(StudentDataUtil.paddedWrap(StudentDataConstants.COL272, fieldDetails.get("SOC_OBJ_4_NOT_ALL_ATMPT_FLAG")));
				s.setSocial_std_Obj_Mstry_Scr_5(StudentDataUtil.paddedWrap(StudentDataConstants.COL273, fieldDetails.get("SOC_OBJ_5_MASTERY")));
				s.setSocial_not_All_items_atmpt_5(StudentDataUtil.paddedWrap(StudentDataConstants.COL274, fieldDetails.get("SOC_OBJ_5_NOT_ALL_ATMPT_FLAG")));
				s.setSocial_std_Obj_Mstry_Scr_6(StudentDataUtil.paddedWrap(StudentDataConstants.COL275, fieldDetails.get("SOC_OBJ_6_MASTERY")));
				s.setSocial_not_All_items_atmpt_6(StudentDataUtil.paddedWrap(StudentDataConstants.COL276, fieldDetails.get("SOC_OBJ_6_NOT_ALL_ATMPT_FLAG")));
				s.setSocial_std_Obj_Mstry_Scr_7(StudentDataUtil.paddedWrap(StudentDataConstants.COL277, fieldDetails.get("SOC_OBJ_7_MASTERY")));
				s.setSocial_not_All_items_atmpt_7(StudentDataUtil.paddedWrap(StudentDataConstants.COL278, fieldDetails.get("SOC_OBJ_7_NOT_ALL_ATMPT_FLAG")));
				s.setOverall_scaleScore(StudentDataUtil.paddedWrap(StudentDataConstants.COL279, fieldDetails.get("OVR_COMP_SCORE_SCALE_SCORE")));
				s.setOverall_hSE_Score(StudentDataUtil.paddedWrap(StudentDataConstants.COL280, fieldDetails.get("OVR_COMP_SCORE_HIGH_SC_EQ")));
				s.setOverall_percentileRank(StudentDataUtil.paddedWrap(StudentDataConstants.COL281, fieldDetails.get("OVR_COMP_SCORE_PERCENTILE_RANK")));
				s.setOverall_nCE(StudentDataUtil.paddedWrap(StudentDataConstants.COL282, fieldDetails.get("OVR_COMP_SCORE_NORMAL_CURVE_EQ")));
				s.setOasReading_screenReader(StudentDataUtil.paddedWrap(StudentDataConstants.COL283, fieldDetails.get("RD_SCREEN_READER")));
				s.setOasReading_onlineCalc(StudentDataUtil.paddedWrap(StudentDataConstants.COL284, fieldDetails.get("RD_ONLINE_CALC")));
				s.setOasReading_testPause(StudentDataUtil.paddedWrap(StudentDataConstants.COL285, fieldDetails.get("RD_TEST_PAUSE")));
				s.setOasReading_highlighter(StudentDataUtil.paddedWrap(StudentDataConstants.COL286, fieldDetails.get("RD_HIGHLIGHTER")));
				s.setOasReading_blockingRuler(StudentDataUtil.paddedWrap(StudentDataConstants.COL287, fieldDetails.get("RD_BLOCKING_RULER")));
				s.setOasReading_magnifyingGlass(StudentDataUtil.paddedWrap(StudentDataConstants.COL288, fieldDetails.get("RD_MAGNIFYING_GLASS")));
				s.setOasReading_fontAndBkgndClr(StudentDataUtil.paddedWrap(StudentDataConstants.COL289, fieldDetails.get("RD_FONT_AND_BKGND_CLR")));
				s.setOasReading_largeFont(StudentDataUtil.paddedWrap(StudentDataConstants.COL290, fieldDetails.get("RD_LARGE_FONT")));
				s.setOasReading_musicPlayer(StudentDataUtil.paddedWrap(StudentDataConstants.COL291, fieldDetails.get("RD_MUSIC_PLAYER")));
				s.setOasReading_extendedTime(StudentDataUtil.paddedWrap(StudentDataConstants.COL292, fieldDetails.get("RD_EXTENDED_TIME")));
				s.setOasReading_maskingTool(StudentDataUtil.paddedWrap(StudentDataConstants.COL293, fieldDetails.get("RD_MASKING_TOOL")));
				s.setOasWriting_screenReader(StudentDataUtil.paddedWrap(StudentDataConstants.COL294, fieldDetails.get("WR_SCREEN_READER")));
				s.setOasWriting_onlineCalc(StudentDataUtil.paddedWrap(StudentDataConstants.COL295, fieldDetails.get("WR_ONLINE_CALC")));
				s.setOasWriting_testPause(StudentDataUtil.paddedWrap(StudentDataConstants.COL296, fieldDetails.get("WR_TEST_PAUSE")));
				s.setOasWriting_highlighter(StudentDataUtil.paddedWrap(StudentDataConstants.COL297, fieldDetails.get("WR_HIGHLIGHTER")));
				s.setOasWriting_blockingRuler(StudentDataUtil.paddedWrap(StudentDataConstants.COL298, fieldDetails.get("WR_BLOCKING_RULER")));
				s.setOasWriting_magnifyingGlass(StudentDataUtil.paddedWrap(StudentDataConstants.COL299, fieldDetails.get("WR_MAGNIFYING_GLASS")));
				s.setOasWriting_fontAndBkgndClr(StudentDataUtil.paddedWrap(StudentDataConstants.COL300, fieldDetails.get("WR_FONT_AND_BKGND_CLR")));
				s.setOasWriting_largeFont(StudentDataUtil.paddedWrap(StudentDataConstants.COL301, fieldDetails.get("WR_LARGE_FONT")));
				s.setOasWriting_musicPlayer(StudentDataUtil.paddedWrap(StudentDataConstants.COL302, fieldDetails.get("WR_MUSIC_PLAYER")));
				s.setOasWriting_extendedTime(StudentDataUtil.paddedWrap(StudentDataConstants.COL303, fieldDetails.get("WR_EXTENDED_TIME")));
				s.setOasWriting_maskingTool(StudentDataUtil.paddedWrap(StudentDataConstants.COL304, fieldDetails.get("WR_MASKING_TOOL")));
				s.setOasMath_screenReader(StudentDataUtil.paddedWrap(StudentDataConstants.COL305, fieldDetails.get("MATH_SCREEN_READER")));
				s.setOasMath_onlineCalc(StudentDataUtil.paddedWrap(StudentDataConstants.COL306, fieldDetails.get("MATH_ONLINE_CALC")));
				s.setOasMath_testPause(StudentDataUtil.paddedWrap(StudentDataConstants.COL307, fieldDetails.get("MATH_TEST_PAUSE")));
				s.setOasMath_highlighter(StudentDataUtil.paddedWrap(StudentDataConstants.COL308, fieldDetails.get("MATH_HIGHLIGHTER")));
				s.setOasMath_blockingRuler(StudentDataUtil.paddedWrap(StudentDataConstants.COL309, fieldDetails.get("MATH_BLOCKING_RULER")));
				s.setOasMath_magnifyingGlass(StudentDataUtil.paddedWrap(StudentDataConstants.COL310, fieldDetails.get("MATH_MAGNIFYING_GLASS")));
				s.setOasMath_fontAndBkgndClr(StudentDataUtil.paddedWrap(StudentDataConstants.COL311, fieldDetails.get("MATH_FONT_AND_BKGND_CLR")));
				s.setOasMath_largeFont(StudentDataUtil.paddedWrap(StudentDataConstants.COL312, fieldDetails.get("MATH_LARGE_FONT")));
				s.setOasMath_musicPlayer(StudentDataUtil.paddedWrap(StudentDataConstants.COL313, fieldDetails.get("MATH_MUSIC_PLAYER")));
				s.setOasMath_extendedTime(StudentDataUtil.paddedWrap(StudentDataConstants.COL314, fieldDetails.get("MATH_EXTENDED_TIME")));
				s.setOasMath_maskingTool(StudentDataUtil.paddedWrap(StudentDataConstants.COL315, fieldDetails.get("MATH_MASKING_TOOL")));
				s.setOasScience_screenReader(StudentDataUtil.paddedWrap(StudentDataConstants.COL316, fieldDetails.get("SCI_SCREEN_READER")));
				s.setOasScience_onlineCalc(StudentDataUtil.paddedWrap(StudentDataConstants.COL317, fieldDetails.get("SCI_ONLINE_CALC")));
				s.setOasScience_testPause(StudentDataUtil.paddedWrap(StudentDataConstants.COL318, fieldDetails.get("SCI_TEST_PAUSE")));
				s.setOasScience_highlighter(StudentDataUtil.paddedWrap(StudentDataConstants.COL319, fieldDetails.get("SCI_HIGHLIGHTER")));
				s.setOasScience_blockingRuler(StudentDataUtil.paddedWrap(StudentDataConstants.COL320, fieldDetails.get("SCI_BLOCKING_RULER")));
				s.setOasScience_magnifyingGlass(StudentDataUtil.paddedWrap(StudentDataConstants.COL321, fieldDetails.get("SCI_MAGNIFYING_GLASS")));
				s.setOasScience_fontAndBkgndClr(StudentDataUtil.paddedWrap(StudentDataConstants.COL322, fieldDetails.get("SCI_FONT_AND_BKGND_CLR")));
				s.setOasScience_largeFont(StudentDataUtil.paddedWrap(StudentDataConstants.COL323, fieldDetails.get("SCI_LARGE_FONT")));
				s.setOasScience_musicPlayer(StudentDataUtil.paddedWrap(StudentDataConstants.COL324, fieldDetails.get("SCI_MUSIC_PLAYER")));
				s.setOasScience_extendedTime(StudentDataUtil.paddedWrap(StudentDataConstants.COL325, fieldDetails.get("SCI_EXTENDED_TIME")));
				s.setOasScience_maskingTool(StudentDataUtil.paddedWrap(StudentDataConstants.COL326, fieldDetails.get("SCI_MASKING_TOOL")));
				s.setOasSocial_screenReader(StudentDataUtil.paddedWrap(StudentDataConstants.COL327, fieldDetails.get("SS_SCREEN_READER")));
				s.setOasSocial_onlineCalc(StudentDataUtil.paddedWrap(StudentDataConstants.COL328, fieldDetails.get("SS_ONLINE_CALC")));
				s.setOasSocial_testPause(StudentDataUtil.paddedWrap(StudentDataConstants.COL329, fieldDetails.get("SS_TEST_PAUSE")));
				s.setOasSocial_highlighter(StudentDataUtil.paddedWrap(StudentDataConstants.COL330, fieldDetails.get("SS_HIGHLIGHTER")));
				s.setOasSocial_blockingRuler(StudentDataUtil.paddedWrap(StudentDataConstants.COL331, fieldDetails.get("SS_BLOCKING_RULER")));
				s.setOasSocial_magnifyingGlass(StudentDataUtil.paddedWrap(StudentDataConstants.COL332, fieldDetails.get("SS_MAGNIFYING_GLASS")));
				s.setOasSocial_fontAndBkgndClr(StudentDataUtil.paddedWrap(StudentDataConstants.COL333, fieldDetails.get("SS_FONT_AND_BKGND_CLR")));
				s.setOasSocial_largeFont(StudentDataUtil.paddedWrap(StudentDataConstants.COL334, fieldDetails.get("SS_LARGE_FONT")));
				s.setOasSocial_musicPlayer(StudentDataUtil.paddedWrap(StudentDataConstants.COL335, fieldDetails.get("SS_MUSIC_PLAYER")));
				s.setOasSocial_extendedTime(StudentDataUtil.paddedWrap(StudentDataConstants.COL336, fieldDetails.get("SS_EXTENDED_TIME")));
				s.setOasSocial_maskingTool(StudentDataUtil.paddedWrap(StudentDataConstants.COL337, fieldDetails.get("SS_MASKING_TOOL")));
				s.setReadingItems_SR(StudentDataUtil.paddedWrap(StudentDataConstants.COL338, fieldDetails.get("RD_SR_RESPONSE")));
				s.setReadingItems_FU(StudentDataUtil.paddedWrap(StudentDataConstants.COL339, fieldDetails.get("RD_ITEMS_FU")));
				s.setWritingItems_SR(StudentDataUtil.paddedWrap(StudentDataConstants.COL340, fieldDetails.get("WR_SR_RESPONSE")));
				s.setWritingItems_CR(StudentDataUtil.paddedWrap(StudentDataConstants.COL341, fieldDetails.get("WR_ITEM_CODE_CR")));
				s.setWritingItems_FU(StudentDataUtil.paddedWrap(StudentDataConstants.COL342, fieldDetails.get("WR_ITEMS_FU")));
				s.setMathItems_SR(StudentDataUtil.paddedWrap(StudentDataConstants.COL343, fieldDetails.get("MATH_ITEM_CODE_SR")));
				s.setMathItems_GR_Status(StudentDataUtil.paddedWrap(StudentDataConstants.COL344, fieldDetails.get("MATH_ITEM_CODE_GR_STATUS")));
				s.setMathItems_GR_Edited(StudentDataUtil.paddedWrap(StudentDataConstants.COL345, fieldDetails.get("MATH_ITEM_CODE_GR_EDITED")));
				s.setMathItems_FU(StudentDataUtil.paddedWrap(StudentDataConstants.COL346, fieldDetails.get("MATH_ITEMS_FU")));
				s.setScienceItems_SR(StudentDataUtil.paddedWrap(StudentDataConstants.COL347, fieldDetails.get("SCI_SR_RESPONSE")));
				s.setScienceItems_FU(StudentDataUtil.paddedWrap(StudentDataConstants.COL348, fieldDetails.get("SCI_ITEMS_FU")));
				s.setSocialStudies_SR(StudentDataUtil.paddedWrap(StudentDataConstants.COL349, fieldDetails.get("SOC_SR_RESPONSE")));
				s.setSocialStudies_FU(StudentDataUtil.paddedWrap(StudentDataConstants.COL350, fieldDetails.get("SS_FU")));
				s.setcTBUseField(StudentDataUtil.paddedWrap(StudentDataConstants.COL351, fieldDetails.get("CTB_USE_FIELD")));
				studentDataList.add(s);
			}
		}
		logger.log(IAppLogger.INFO, "Exit: AdminDAOImpl - downloadStudentFile()");
		return studentDataList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#getEducationCenter(java.util.Map)
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getEducationCenter(final Map<String, Object> paramMap) throws SystemException {
		logger.log(IAppLogger.INFO, "Enter: AdminDAOImpl - getEducationCenter()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) paramMap.get("loggedinUserTO");
		List placeHolderValueList = new ArrayList();
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
		logger.log(IAppLogger.INFO, "Exit: AdminDAOImpl - getEducationCenter()");
		return objectValueTOList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.dao.IAdminDAO#loadEduCenterUsers(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public List<EduCenterTO> loadEduCenterUsers(final Map<String, Object> paramMap) throws SystemException {

		logger.log(IAppLogger.INFO, "Enter: AdminDAOImpl - loadEduCenterUsers()");
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
			logger.log(IAppLogger.INFO, "Exit: AdminDAOImpl - loadEduCenterUsers()");
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
		logger.log(IAppLogger.INFO, "Enter: AdminDAOImpl - getUserData()");
		final String orgNodeId = (String) paramMap.get("tenantId");
		String adminYear = (String) paramMap.get("adminYear");
		String userId = (String) paramMap.get("userId");
		logger.log(IAppLogger.INFO, "orgNodeId=" + orgNodeId + ", adminYear=" + adminYear + ", userId=" + userId);

		ArrayList<UserDataTO> userDataList = new ArrayList<UserDataTO>();
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_DATA, orgNodeId);
		if ((lstData != null) && (!lstData.isEmpty())) {
			for (Map<String, Object> fieldDetails : lstData) {
				UserDataTO to = new UserDataTO();
				to.setUserId(fieldDetails.get("USERNAME").toString());
				to.setFullName(fieldDetails.get("FULLNAME").toString());
				to.setStatus(fieldDetails.get("STATUS").toString());
				to.setOrgName(fieldDetails.get("ORG_NODE_NAME").toString());
				to.setUserRoles(getRolesWithLabel(fieldDetails.get("ORG_LABEL").toString(), fieldDetails.get("DESCRIPTION").toString()));
				userDataList.add(to);
			}
		}
		long end = System.currentTimeMillis();
		logger.log(IAppLogger.INFO, "Exit: AdminDAOImpl - getUserData() : " + CustomStringUtil.getHMSTimeFormat(end - start));
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
		StringBuilder buff = new StringBuilder();
		String[] roles = userRoles.split(",");
		for (String role : roles) {
			String[] roleTokens = role.split(" ");
			for (String token : roleTokens) {
				if ("ADMIN".equalsIgnoreCase(token)) {
					return role;
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
