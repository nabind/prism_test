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
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.prism.constant.Constants;
import com.prism.to.ObjectValueTO;
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
		logger.info("Fetching School Data ...");
		List<Map<String, Object>> dataList = jdbcTemplate.queryForList(Constants.GET_SCHOOL_DETAILS, schoolId);
		if ((dataList != null) && (!dataList.isEmpty())) {
			// logger.info("Schools: " + dataList.size());
			for (Map<String, Object> data : dataList) {
				school.setElementName(((String) data.get("ORG_NODE_NAME")).trim()); // 2
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
		logger.info("Fetching School User Data ...");
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
	 * @see com.prism.dao.CommonDAO#updateNewUserFlag(java.util.List)
	 */
	public int[] updateNewUserFlag(final List<UserTO> userList) {
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
		logger.info("Fetching School User Role Data ...");
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
	 * @see com.prism.dao.CommonDAO#getIcLetterPathList(java.lang.String)
	 */
	public List<String> getIcLetterPathList(String schoolId) {
		List<String> pdfList = new ArrayList<String>();
		List<Map<String, Object>> lstData = jdbcTemplate.queryForList(Constants.GET_IC_LETTER_PATH_LIST, schoolId);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				String pdfPath = (String) fieldDetails.get("IC_FILE_LOC");
				if (pdfPath != null && !"null".equalsIgnoreCase(pdfPath) && !pdfPath.isEmpty()) {
					pdfList.add(pdfPath);
				}
			}
		}
		logger.info("pdfList.size(): " + pdfList.size());
		return pdfList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#getCurrentAdminYear()
	 */
	public String getCurrentAdminYear() {
		String currentAdminYear = null;
		List<Map<String, Object>> lstData = jdbcTemplate.queryForList(Constants.GET_CURRENT_ADMIN_YEAR);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				currentAdminYear = ((BigDecimal) fieldDetails.get("ADMINID")).toString();
				break;
			}
		}
		return currentAdminYear;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#getStudentIdList(java.lang.String)
	 */
	public List<String> getStudentIdList(String schoolId) {
		List<String> studentIdList = new ArrayList<String>();
		List<Map<String, Object>> lstData = jdbcTemplate.queryForList(Constants.GET_STUDENT_ID_LIST, schoolId);
		if (!lstData.isEmpty()) {
			for (Map<String, Object> fieldDetails : lstData) {
				String studentBioId = ((BigDecimal) fieldDetails.get("STUDENT_BIO_ID")).toString();
				studentIdList.add(studentBioId);
			}
		}
		logger.info("studentIdList.size(): " + studentIdList.size());
		return studentIdList;
	}

	/*
	 * (non-Javadoc)
	 * @see com.prism.dao.CommonDAO#updateStudentsPDFloc(Map<String,String> pdfPathList)
	 */
	public int[] updateStudentsPDFloc(Map<String,String> pdfPathList) {
		
		final List<ObjectValueTO> studentList = new ArrayList<ObjectValueTO>();
		ObjectValueTO studentTo = null;
		for (Entry entry: pdfPathList.entrySet()){
			studentTo = new ObjectValueTO();
			studentTo.setName(entry.getKey().toString());
			studentTo.setValue(entry.getValue().toString());
			studentList.add(studentTo);
		}
		
		int[] updateCount = jdbcTemplate.batchUpdate(Constants.UPDATE_STUDENT_PDF_LOC, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement pstmt, int i) throws SQLException {
				ObjectValueTO student = studentList.get(i);
				pstmt.setString(1, student.getValue()); // IC_FILE_LOC
				pstmt.setString(2, student.getName()); // STUDENT_BIO_ID
				
				
			}

			public int getBatchSize() {
				return studentList.size();
			}
		});
		return updateCount;
		
	}
}
