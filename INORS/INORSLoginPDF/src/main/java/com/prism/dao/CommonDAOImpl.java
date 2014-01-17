/**
 * 
 */
package com.prism.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

	/**
	 * @param schoolId
	 * @return
	 * @deprecated
	 */
	public Integer getClassCount(Long schoolId) {
		Integer count = jdbcTemplate.queryForObject("select count(*) from org_node_dim where parent_org_nodeid=" + schoolId, Integer.class);
		logger.info("count=" + count);
		return count;
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

	/**
	 * Creates the organization Map where the organization id is the key and the organization name is the value.
	 * 
	 * @param userList
	 * 
	 * @return
	 * @throws Exception
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
	 * Creates a comma separated list of all org_node_ids
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

	/**
	 * Update new user flag for all users in the userList
	 * 
	 * @param userList
	 * @return
	 * @throws Exception
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

	/**
	 * Fetch Education Center information
	 * 
	 * @param eduCenterId
	 * @return
	 * @throws Exception
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
	 * Populates the Role List for all the users
	 * 
	 * @param users
	 * @return
	 * @throws Exception
	 */
	public List<UserTO> getSchoolUsersWithRoles(List<UserTO> users) {
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

	public Map<String, String> getOrgLabelMap() {
		Map<String, String> orgLabelMap = new HashMap<String, String>();
		// TODO
		/*
		 * Connection conn = null; PreparedStatement pstmt = null; ResultSet rs = null; try { conn = driver.connect(DATA_SOURCE, null); String query =
		 * CustomStringUtil.appendString("SELECT TEMP.ORG_LEVEL, LISTAGG(TEMP.ORG_LABEL, '/') WITHIN GROUP(ORDER BY TEMP.ORG_LEVEL) AS ORG_LABEL",
		 * " FROM (SELECT  DISTINCT ORG_LEVEL,ORG_LABEL FROM ORG_TP_STRUCTURE ORDER BY ORG_LEVEL) TEMP", " GROUP BY TEMP.ORG_LEVEL"); pstmt = conn.prepareCall(query); rs = pstmt.executeQuery(); while
		 * (rs.next()) { orgLabelMap.put(rs.getString(1), rs.getString(2)); } } catch (SQLException e) { e.printStackTrace(); } finally { try { rs.close(); } catch (Exception e2) { } try {
		 * pstmt.close(); } catch (Exception e2) { } try { conn.close(); } catch (Exception e2) { } }
		 */
		return orgLabelMap;
	}

	/**
	 * get process id - without any condition
	 * 
	 * @param structElement
	 * @return
	 * @throws Exception
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

	/**
	 * get current admin year
	 * 
	 * @return
	 * @throws Exception
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

	/**
	 * check if new students are added
	 * 
	 * @param jasperOrgId
	 *            , adminId
	 * @return
	 * @throws Exception
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

	/**
	 * This is to update process status
	 * 
	 * 
	 */
	@Override
	public int updateProcessStatus(long processId, String status) {
		return jdbcTemplate.update("UPDATE ORG_PROCESS_STATUS SET PROCESS_STATUS = ?, updated_date_time = SYSDATE WHERE processid = ?", status, processId);
	}

	/**
	 * This is to update mail sending status
	 * 
	 * @param allUsers
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateMailStatus(long processId, String status, String prevStatus) {
		return jdbcTemplate.update("UPDATE ORG_PROCESS_STATUS SET TARGET_EMAIL_STATUS = ?, UPDATED_DATE_TIME = SYSDATE WHERE PROCESSID = ?", status, processId);
	}
}
