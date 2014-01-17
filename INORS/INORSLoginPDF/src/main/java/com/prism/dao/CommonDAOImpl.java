package com.prism.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.prism.constant.Constants;
import com.prism.to.OrgTO;
import com.prism.to.UserTO;
import com.prism.util.CustomStringUtil;

/**
 * @author Amitabha Roy
 * 
 */
public class CommonDAOImpl implements CommonDAO {

	private static final Logger logger = Logger.getLogger(CommonDAOImpl.class);

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#getSchoolDetails(java.lang.String)
	 */
	public OrgTO getSchoolDetails(String schoolId) throws Exception {
		logger.info("School Id: " + schoolId);
		OrgTO school = new OrgTO();
		List<Map<String, Object>> dataList = jdbcTemplate.queryForList(Constants.GET_SCHOOL_DETAILS, schoolId);
		if ((dataList != null) && (!dataList.isEmpty())) {
			logger.info("Schools: " + dataList.size());
			for (Map<String, Object> data : dataList) {
				school.setElementName((String) data.get("ORG_NODE_NAME")); // 2
				school.setOrgNodeId(((BigDecimal) data.get("ORG_NODEID")).toString()); // 1
				school.setOrgNodeLevel(((BigDecimal) data.get("ORG_NODE_LEVEL")).toString()); // 4
				school.setJasperOrgId(((BigDecimal) data.get("ORG_NODEID")).toString()); // 1
				school.setEmail((String) data.get("EMAILS")); // 9
				school.setCustomerCode(((BigDecimal) data.get("CUSTOMERID")).toString()); // 10
				school.setParentJasperOrgId(((BigDecimal) data.get("PARENT_ORG_NODEID")).toString()); // 7
				school.setDateStr((String) data.get("DATE_STR")); // 11
				school.setDateStrWtYear((String) data.get("DATE_STR_WT_YEAR")); // 12
				break;
			}
		} else {
			logger.info("No school found");
		}
		if (school != null) {
			List<UserTO> users = getSchoolUsers(school.getJasperOrgId(), false);
			school.setUsers(users);
		}
		return school;
	}

	/**
	 * @param schoolId
	 * @param hierarchical
	 * @return
	 * @throws Exception
	 */
	private List<UserTO> getSchoolUsers(String schoolId, boolean hierarchical) throws Exception {
		List<UserTO> users = new ArrayList<UserTO>();
		String query = Constants.GET_SCHOOL_USERS;
		if (hierarchical) {
			query = Constants.GET_SCHOOL_USERS_HIERARCHICAL;
		}
		List<Map<String, Object>> dataList = jdbcTemplate.queryForList(query, schoolId);
		if (dataList != null && dataList.size() > 0) {
			for (Map<String, Object> data : dataList) {
				UserTO user = new UserTO();
				String userId = ((BigDecimal) data.get("USERID")).toString();
				user.setJasperUserId(userId);
				user.setUserId(userId);
				user.setUserName((String) data.get("USERNAME"));
				user.setFullName((String) data.get("DISPLAY_USERNAME"));
				user.setOrgNodeId(((BigDecimal) data.get("ORG_NODEID")).toString());
				user.setOrgNodeLevel(((BigDecimal) data.get("ORG_NODE_LEVEL")).toString());
				user.setOrgNodeName((String) data.get("ORG_NODE_NAME"));
				// Database values are not correct so a manual call is required
				user.setOrgNodeCodePath(getOrgNodeCodePath(user.getOrgNodeId()));
				users.add(user);
			}
		}
		return getSchoolUsersWithRoles(users);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#getOrgNodeCodePath(java.lang.String)
	 */
	public String getOrgNodeCodePath(String orgNodeId) throws Exception {
		StringBuilder orgNodeCodePath = new StringBuilder();
		orgNodeCodePath.insert(0, orgNodeId);
		String parentOrgNodeId = getParentOrgNodeId(orgNodeId);
		while ((parentOrgNodeId != null) && (!"0".equals(parentOrgNodeId))) {
			orgNodeCodePath.insert(0, "~");
			orgNodeCodePath.insert(0, parentOrgNodeId);
			parentOrgNodeId = getParentOrgNodeId(parentOrgNodeId);
		}
		orgNodeCodePath.insert(0, "0~");
		return orgNodeCodePath.toString();
	}

	/**
	 * @param orgNodeId
	 * @return
	 * @throws Exception
	 */
	private String getParentOrgNodeId(String orgNodeId) throws Exception {
		String parentOrgNodeId = null;
		List<Map<String, Object>> lstData = jdbcTemplate.queryForList(Constants.GET_PARENT_ORG_NODEID, orgNodeId);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				parentOrgNodeId = ((BigDecimal) fieldDetails.get("PARENT_ORG_NODEID")).toString();
				break;
			}
		}
		return parentOrgNodeId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#getOrgMap(java.util.List)
	 */
	public Map<Integer, String> getOrgMap(List<UserTO> userList) {
		Map<Integer, String> orgMap = new HashMap<Integer, String>();
		String ids = getIds(userList);
		List<Map<String, Object>> dataList = jdbcTemplate.queryForList(CustomStringUtil.replaceCharacterInString('?', ids, Constants.GET_ORG_MAP));
		logger.info("Map size: " + dataList.size());
		if (dataList != null && dataList.size() > 0) {
			for (Map<String, Object> data : dataList) {
				orgMap.put(((BigDecimal) data.get("ORG_NODEID")).intValue(), (String) data.get("ORG_NODE_NAME"));
			}
		}
		return orgMap;
	}

	/**
	 * Creates a comma separated list of all org_node_ids.
	 * 
	 * @param userList
	 * @return
	 */
	private String getIds(List<UserTO> userList) {
		Set<String> idSet = new HashSet<String>();
		for (UserTO user : userList) {
			idSet.add(user.getOrgNodeId());
			if (null != user.getOrgNodeCodePath()) {
				String[] orgHierarchy = user.getOrgNodeCodePath().split("~");
				Set<String> orgHierarchySet = new HashSet<String>(Arrays.asList(orgHierarchy));
				idSet.addAll(orgHierarchySet);
			}
		}
		String ids = idSet.toString();
		ids = ids.substring(1, ids.length() - 1);
		return ids;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#updateNewuserFlag(java.util.List)
	 */
	public int[] updateNewuserFlag(final List<UserTO> userList) {
		int[] updateCount = jdbcTemplate.batchUpdate(Constants.UPDATE_NEW_USER_FLAG, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement pstmt, int i) throws SQLException {
				UserTO user = userList.get(i);
				pstmt.setString(1, user.getEncPassword());
				pstmt.setString(2, user.getSalt());
				pstmt.setString(3, user.getUserName());
			}

			public int getBatchSize() {
				return userList.size();
			}
		});
		return updateCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#getSupportEmailForCustomer(java.lang.String)
	 */
	public String getSupportEmailForCustomer(String customerId) {
		String supportEmail = null;
		List<Map<String, Object>> lstData = jdbcTemplate.queryForList(Constants.GET_SUPPORT_EMAIL_FOR_CUSTOMER, Long.valueOf(customerId));
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				supportEmail = (String) fieldDetails.get("SUPPORT_EMAIL");
				break;
			}
		}
		return supportEmail;
	}

	/**
	 * Populates the Role List for all the users.
	 * 
	 * @param users
	 * @return
	 * @throws Exception
	 */
	private List<UserTO> getSchoolUsersWithRoles(List<UserTO> users) {
		for (UserTO user : users) {
			List<Map<String, Object>> dataList = jdbcTemplate.queryForList(Constants.GET_SCHOOL_USERS_WITH_ROLES, user.getUserId());
			if (dataList != null && dataList.size() > 0) {
				List<String> roleList = new ArrayList<String>();
				for (Map<String, Object> data : dataList) {
					roleList.add((String) data.get("DESCRIPTION"));
				}
				user.setRoles(roleList);
			}
		}
		return users;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#getOrgLabelMap()
	 */
	public Map<String, String> getOrgLabelMap() {
		Map<String, String> orgLabelMap = new HashMap<String, String>();
		List<Map<String, Object>> dataList = jdbcTemplate.queryForList(Constants.GET_ORG_LABEL_MAP);
		logger.info("Map size: " + dataList.size());
		if (dataList != null && dataList.size() > 0) {
			for (Map<String, Object> data : dataList) {
				orgLabelMap.put(((BigDecimal) data.get("ORG_LEVEL")).toString(), (String) data.get("ORG_LABEL"));
			}
		}
		return orgLabelMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#getProcessIdNoCondition(java.lang.String)
	 */
	public long getProcessIdNoCondition(String structElement) {
		long processId = 0;
		List<Map<String, Object>> lstData = jdbcTemplate.queryForList(Constants.GET_PROCESS_ID_NO_CONDITION, structElement);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				processId = ((BigDecimal) fieldDetails.get("PROCESSID")).longValue();
				break;
			}
		}
		return processId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#getCurrentAdminYear()
	 */
	public String getCurrentAdminYear() {
		String adminid = null;
		List<Map<String, Object>> lstData = jdbcTemplate.queryForList(Constants.GET_CURRENT_ADMIN_YEAR);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				adminid = (String) fieldDetails.get("ADMINID");
				break;
			}
		}
		return adminid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#newStudentsLoaded(java.lang.String)
	 */
	public boolean newStudentsLoaded(String jasperOrgId) {
		int newStudCount = 0;
		List<Map<String, Object>> lstData = jdbcTemplate.queryForList(Constants.NEW_STUDENTS_LOADED, jasperOrgId);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				newStudCount = ((BigDecimal) fieldDetails.get("PROCESSID")).intValue();
				break;
			}
		}
		return (newStudCount > 0) ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#updateProcessStatus(long, java.lang.String)
	 */
	public int updateProcessStatus(long processId, String status) {
		return jdbcTemplate.update(Constants.UPDATE_PROCESS_STATUS, status, processId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#updateMailStatus(long, java.lang.String, java.lang.String)
	 */
	public int updateMailStatus(long processId, String status, String prevStatus) {
		return jdbcTemplate.update(Constants.UPDATE_MAIL_STATUS, status, processId);
	}
}
