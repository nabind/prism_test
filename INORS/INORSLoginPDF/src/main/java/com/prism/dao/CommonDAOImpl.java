package com.prism.dao;

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





//import org.springframework.jdbc.core.BatchPreparedStatementSetter;
//import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Properties;







import com.prism.constant.Constants;
import com.prism.to.OrgTO;
import com.prism.to.UserTO;
import com.prism.util.CustomStringUtil;
import com.prism.util.JDCConnectionDriver;

/**
 * @author Amitabha Roy
 * 
 */
public class CommonDAOImpl implements CommonDAO {

	private static final Logger logger = Logger.getLogger(CommonDAOImpl.class);

	static JDCConnectionDriver driver = null;
	static String DATA_SOURCE = "jdbc:jdc:acsi";


	/**
	 * Constructor
	 * 
	 * @param prop
	 * @throws Exception
	 */
	public CommonDAOImpl(Properties prop) throws Exception {
		try {
			String dbURL = prop.getProperty("jdbc.url");
			String dbUserName = prop.getProperty("jdbc.username");
			String dbPassword = prop.getProperty("jdbc.password");

			driver = new JDCConnectionDriver(prop.getProperty("jdbc.driverClassName"),
					dbURL, dbUserName, dbPassword);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#getSchoolDetails(java.lang.String)
	 */
	public OrgTO getSchoolDetails(String schoolId, boolean cascade) throws Exception {
		logger.info("School Id: " + schoolId);
		OrgTO school = new OrgTO();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		logger.info("Fetching School Data ...");
		
		try {
			conn = driver.connect(DATA_SOURCE, null);
			pstmt = conn.prepareCall(Constants.GET_SCHOOL_DETAILS);
			pstmt.setLong(1, Long.valueOf(schoolId));
			
			rs = pstmt.executeQuery();
			if (rs.next()) {
				school = new OrgTO();
				school.setElementName(rs.getString("ORG_NODE_NAME"));
				school.setOrgNodeId(rs.getString("ORG_NODEID"));
				school.setOrgNodeLevel(rs.getString("ORG_NODE_LEVEL"));
				school.setJasperOrgId(rs.getString("ORG_NODEID"));
				school.setEmail(rs.getString("EMAILS"));
				school.setCustomerCode(rs.getString("CUSTOMERID"));
				school.setParentJasperOrgId("PARENT_ORG_NODEID");
				school.setDateStr(rs.getString("DATE_STR"));
				school.setDateStrWtYear(rs.getString("DATE_STR_WT_YEAR"));
			} else {
				logger.info("No school found");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
			}
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
			}
		}
	
		if ((school != null) && (cascade == true)) {
			List<UserTO> users = getSchoolUsers(school.getJasperOrgId(), cascade);
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
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserTO user = null;
		
		String query = Constants.GET_SCHOOL_USERS;
		if (hierarchical) {
			query = Constants.GET_SCHOOL_USERS_HIERARCHICAL;
		}
		try{
			conn = driver.connect(DATA_SOURCE, null);
			pstmt = conn.prepareCall(query);
			pstmt.setLong(1, Long.valueOf(schoolId));
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				user = new UserTO();
				String userId = rs.getString("USERID");
				user.setJasperUserId(userId);
				user.setUserId(userId);
				user.setUserName(rs.getString("USERNAME"));
				user.setFullName(rs.getString("DISPLAY_USERNAME"));
				user.setOrgNodeId(rs.getString("ORG_NODEID"));
				user.setOrgNodeLevel(rs.getString("ORG_NODE_LEVEL"));
				user.setOrgNodeName(rs.getString("ORG_NODE_NAME"));
				// Database values are not correct so a manual call is required
				user.setOrgNodeCodePath(getOrgNodeCodePath(user.getOrgNodeId()));
				users.add(user);
			}
			logger.debug("Returning org users");
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
			}
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
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
	private String getParentOrgNodeId(String schoolId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String parentOrgNodeId = null;		
		try{
			conn = driver.connect(DATA_SOURCE, null);
			pstmt = conn.prepareCall(Constants.GET_PARENT_ORG_NODEID);
			pstmt.setLong(1, Long.valueOf(schoolId));
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				parentOrgNodeId = rs.getString("PARENT_ORG_NODEID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
			}
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
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
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		try{
			conn = driver.connect(DATA_SOURCE, null);
			pstmt = conn.prepareCall(CustomStringUtil.replaceCharacterInString('?', ids, Constants.GET_ORG_MAP));
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				orgMap.put(Integer.valueOf(rs.getString("ORG_NODEID")),rs.getString("ORG_NODE_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
			}
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
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
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		int updateCount[] = null;
		
		try{
			conn = driver.connect(DATA_SOURCE, null);

			pstmt = conn.prepareStatement(Constants.UPDATE_NEW_USER_FLAG);
			
			for (UserTO user : userList) {
				pstmt.setString(1, user.getEncPassword());
				pstmt.setString(2, user.getSalt());
				pstmt.setString(3, user.getUserName());
				pstmt.addBatch();
			}
			updateCount = pstmt.executeBatch();
			logger.debug("Records updated: " + updateCount);
			conn.commit();
			logger.debug("commit successful");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
			}
		}
		
		return updateCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#getSupportEmailForCustomer(java.lang.String)
	 */
	public String getSupportEmailForCustomer(String customerId) {
		String supportEmail = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			pstmt = conn.prepareCall(Constants.GET_SUPPORT_EMAIL_FOR_CUSTOMER);
			pstmt.setLong(1, Long.valueOf(customerId));
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				supportEmail = rs.getString("SUPPORT_EMAIL");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();			
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
			}
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
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
	public List<UserTO> getSchoolUsersWithRoles(List<UserTO> users)	throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = driver.connect(DATA_SOURCE, null);

			pstmt = conn.prepareCall(Constants.GET_SCHOOL_USERS_WITH_ROLES);
			for (UserTO user : users) {
				pstmt.setString(1, user.getUserId());
				
				rs = pstmt.executeQuery();
				List<String> roleList = new ArrayList<String>();
				while (rs.next()) {
					roleList.add(rs.getString("DESCRIPTION"));
				}
				user.setRoles(roleList);
			}
			logger.debug("Returning roles");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
			}
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
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
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;		
		
		try{
			conn = driver.connect(DATA_SOURCE, null);
			pstmt = conn.prepareCall(Constants.GET_ORG_LABEL_MAP);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				orgLabelMap.put(rs.getString("ORG_LEVEL"),rs.getString("ORG_LABEL"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
			}
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
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
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			conn = driver.connect(DATA_SOURCE, null);
			pstmt = conn.prepareCall(Constants.GET_IC_LETTER_PATH_LIST);
			pstmt.setLong(1, Long.valueOf(schoolId));
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String pdfPath = rs.getString("IC_FILE_LOC");
				if (pdfPath != null && !"null".equalsIgnoreCase(pdfPath) && !pdfPath.isEmpty()) {
					pdfList.add(pdfPath);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
			}
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
			}
		
		}
		logger.info("pdfList.size(): " + pdfList!=null ? pdfList.size(): 0);
		return pdfList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#getCurrentAdminYear()
	 */
	public String getCurrentAdminYear() {
		String currentAdminYear = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			conn = driver.connect(DATA_SOURCE, null);
			pstmt = conn.prepareCall(Constants.GET_CURRENT_ADMIN_YEAR);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				currentAdminYear = rs.getString("ADMINID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
			}
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
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
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			conn = driver.connect(DATA_SOURCE, null);
			pstmt = conn.prepareCall(Constants.GET_STUDENT_ID_LIST);
			pstmt.setLong(1, Long.valueOf(schoolId));
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				studentIdList.add(rs.getString("STUDENT_BIO_ID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
			}
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
			}
		
		}
		return studentIdList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#updateStudentsPDFloc(Map<String,String> pdfPathList)
	 */
	public int[] updateStudentsPDFloc(Map<String, String> pdfPathList) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		int updateCount[] = null;
		
		try{
			conn = driver.connect(DATA_SOURCE, null);

			pstmt = conn.prepareStatement(Constants.UPDATE_STUDENT_PDF_LOC);
						
			for (Entry entry : pdfPathList.entrySet()) {	
				pstmt.setString(1, entry.getValue().toString()); // IC_FILE_LOC
				pstmt.setString(2, entry.getKey().toString()); // STUDENT_BIO_ID
				pstmt.addBatch();
			}
			
			updateCount = pstmt.executeBatch();
			logger.debug("Records updated: " + updateCount);
			conn.commit();
			logger.debug("commit successful");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
			}
		}
		return updateCount;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prism.dao.CommonDAO#getStudentIdListFromExtractTable(java.lang.String)
	 */
	public List<String> getStudentIdListFromExtractTable(String schoolId) {
		List<String> studentIdList = new ArrayList<String>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			conn = driver.connect(DATA_SOURCE, null);
			pstmt = conn.prepareCall(Constants.GET_STUDENT_ID_LIST_FROM_EXT);
			pstmt.setLong(1, Long.valueOf(schoolId));
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				studentIdList.add(rs.getString("BIO_EXTRACTID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
			}
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
			}
		
		}
		logger.info("EXT studentIdList.size(): " + studentIdList.size());
		return studentIdList;
	}
	
	/* (non-Javadoc)
	 * @see com.prism.dao.CommonDAO#getSubjectPrefix(java.lang.String)
	 */
	public String getSubjectPrefix(String schoolId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String districtId = "";
		String districtName = "";
		try {
			conn = driver.connect(DATA_SOURCE, null);
			pstmt = conn.prepareCall(Constants.GET_ORG);
			pstmt.setString(1, schoolId);
			pstmt.setString(2, "2");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				districtId = rs.getString("ORG_NODEID");
				districtName = rs.getString("ORG_NODE_NAME");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
			}
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
			}
		}
		return getProductName() + " " + districtName + " " + districtId;
	}

	
	/**
	 * @return
	 */
	private String getProductName() {
		String currentAdminYear = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = driver.connect(DATA_SOURCE, null);
			pstmt = conn.prepareCall(Constants.GET_PRODUCT_NAME);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				currentAdminYear = rs.getString("PRODUCT_NAME");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
			}
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
			}
		}
		return currentAdminYear;
	}

	/**
	 * Fetch school information
	 * 
	 * @param jasperOrgId
	 * @return
	 * @throws Exception
	 */
	public OrgTO getSchoolDetailsAcsi(String jasperOrgId, boolean state) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrgTO school = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			// conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

			/*
			 * String query = "SELECT org.level3_element_name, org.org_nodeid, org.level3_jasper_orgid " + "FROM org_node_dim org WHERE rownum = 1 and org.level3_jasper_orgid = ?";
			 */

			String query = "";
			if (state) {
				query = /*
						 * "SELECT org.level1_element_name, org.org_nodeid, org.level1_jasper_orgid, str.email, " + "str.STRUCTURE_ELEMENT, org.LEVEL1_CUSTOMER_CODE " +
						 * "FROM org_node_dim org, org_structure_element str WHERE str.jasper_orgid = org.level1_jasper_orgid " +
						 * "AND rownum = 1 and org.level1_jasper_orgid = ? and str.adminid = (select adminid from admin_dim where current_admin = 'Y') ";
						 */
				"select org_node_name, org_nodeid, org_nodeid, emails, strc_element, customerid from org_node_dim where org_nodeid = ? and org_node_level=1";
			} else {
				query = /*
						 * "SELECT org.level3_element_name, org.org_nodeid, org.level3_jasper_orgid, str.email, " + "str.STRUCTURE_ELEMENT, org.LEVEL3_CUSTOMER_CODE " +
						 * "FROM org_node_dim org, org_structure_element str WHERE str.jasper_orgid = org.level3_jasper_orgid " +
						 * "AND rownum = 1 and org.level3_jasper_orgid = ? and str.adminid = (select adminid from admin_dim where current_admin = 'Y') ";
						 */
				"select org_node_name, org_nodeid, org_nodeid, emails, strc_element, customerid from org_node_dim where org_nodeid = ? and org_node_level=3";
			}
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, jasperOrgId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				school = new OrgTO();
				school.setElementName(rs.getString(1));
				school.setOrgNodeId(rs.getString(2));
				school.setJasperOrgId(rs.getString(3));
				school.setEmail(rs.getString(4));
				school.setStructureElement(rs.getString(5));
				school.setCustomerCode(rs.getString(6));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
			}
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
			}
		}
		if (school != null) {
			List<UserTO> users = getSchoolUsersAcsi(school.getJasperOrgId());
			school.setUsers(users);
		}
		return school;
	}

	/**
	 * Fetch all school users
	 * 
	 * @param jasperOrgId
	 * @return
	 * @throws Exception
	 */
	public List<UserTO> getSchoolUsersAcsi(String jasperOrgId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<UserTO> allUsers = new ArrayList<UserTO>();
		UserTO user = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			// conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

			/*
			 * String query = "SELECT u.jasper_userid, u.user_name FROM org_users u " + "WHERE u.new_user = 'Y' AND u.jasper_orgid = ?";
			 */
			// changing query for new table in Phase II
			String query = /*
							 * "SELECT u.user_id, u.username FROM users u " + "WHERE u.new_user = 'Y' and u.activation_status = 'AC' AND u.org_id = ? " +
							 * "AND u.adminid = (select adminid from admin_dim where current_admin = 'Y') ";
							 */
			"SELECT U.USERID, U.USERNAME FROM USERS U, ORG_USERS OU WHERE U.USERID=OU.USERID AND U.ACTIVATION_STATUS = 'AC' AND U.IS_NEW_USER='Y' AND OU.ORG_NODEID = ?";
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, jasperOrgId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				user = new UserTO();
				String userId = rs.getString(1);
				user.setJasperUserId(userId);
				user.setUserId(userId);
				user.setUserName(rs.getString(2));
				allUsers.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
			}
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
			}
		}
		return fetchAdminColumnAcsi(allUsers);
	}

	/**
	 * Check if user is admin user or not
	 * 
	 * @param users
	 * @return
	 * @throws Exception
	 */
	public List<UserTO> fetchAdminColumnAcsi(List<UserTO> users) throws Exception {
		StringBuffer allUserIds = new StringBuffer();
		boolean firstRec = false;
		for (UserTO u : users) {
			if (firstRec) {
				allUserIds.append(",");
			}
			firstRec = true;
			allUserIds.append(u.getUserId());
		}
		// System.out.println("all user ids : "+allUserIds);
		List<UserTO> allUsers = null;
		if (allUserIds.toString() != null && allUserIds.toString().length() > 0) {
			allUsers = checkAdminAcsi(allUserIds.toString());

			for (UserTO adUs : allUsers) {
				for (UserTO user : users) {
					if (user.getUserId() != null && user.getUserId().equals(adUs.getUserId())) {
						user.setAdminUser(adUs.isAdminUser());
						break;
					}
				}
			}
		}

		return users;
	}

	/**
	 * Check if the school user is admin or not
	 * 
	 * @param allRoleIds
	 * @return
	 * @throws Exception
	 */
	public List<UserTO> checkAdminAcsi(String allRoleIds) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<UserTO> allUsers = new ArrayList<UserTO>();
		UserTO user = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);

			String query = " SELECT rle.role_name, urle.user_id FROM role rle, user_role urle " + "WHERE urle.role_id = rle.role_ID AND rle.role_name = 'ROLE_ADMIN' " + "AND urle.user_id in ( "
					+ allRoleIds + " )";
			pstmt = conn.prepareCall(query);
			// pstmt.setString(1, allRoleIds);
			// System.out.println(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				user = new UserTO();
				String roleName = rs.getString(1);
				if ("ROLE_ADMIN".equals(roleName)) {
					user.setAdminUser(Boolean.TRUE);
				}
				user.setUserId(rs.getString(2));
				allUsers.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
			}
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
			try {
				conn.close();
			} catch (Exception e2) {
			}
		}
		return allUsers;
	}
	
	/**
	 * get current admin year
	 * @return
	 * @throws Exception
	 */
	public String getCurrentAdminYearAcsi() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String adminid = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = "select adminid from admin_dim where is_current_admin = 'Y'";
			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				adminid = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		return adminid;
	}
	
	/**
	 * Fetch all teacher org with student count and login user
	 * @param jasperOrgId
	 * @return
	 * @throws Exception 
	 */
	public List<OrgTO> getAllTeachersAcsi(String jasperOrgId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<OrgTO> allTeachers = new ArrayList<OrgTO>();
		OrgTO teacher = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);

			/*String query = "SELECT COUNT(STU.STUDENT_BIO_ID), " + 
						       "ORG.LEVEL4_ELEMENT_NAME, " + 
						       "ORG.LEVEL4_JASPER_ORGID, " + 
						       "ORG.ORG_NODEID, " + 
						       "U.JASPER_USERID, " + 
						       "U.USER_NAME " + 
						  "FROM ORG_NODE_DIM ORG, ORG_USERS U, STUDENT_BIO_DIM STU " + 
						 "WHERE ORG.LEVEL3_JASPER_ORGID = ? " + 
						   "AND U.JASPER_ORGID = ORG.LEVEL4_JASPER_ORGID " + 
						   "AND STU.ORG_NODEID = ORG.ORG_NODEID " + 
						   "AND u.new_user = 'Y' " + 
						 "GROUP BY ORG.LEVEL4_ELEMENT_NAME, " + 
						          "ORG.LEVEL4_JASPER_ORGID, " + 
						          "ORG.ORG_NODEID, " + 
						          "U.JASPER_USERID, " + 
						          "U.USER_NAME " + 
						 "ORDER BY ORG.ORG_NODEID";*/
			/** Changing query for Phase II */
			String query = /*"SELECT COUNT(STU.STUDENT_BIO_ID), " + 
						       "ORG.LEVEL4_ELEMENT_NAME, " + 
						       "ORG.LEVEL4_JASPER_ORGID, " + 
						       "ORG.ORG_NODEID, " + 
						       "U.USER_ID, " + 
						       "U.USERNAME, " + 
						       "U.ORG_ID " + 
						  "FROM ORG_NODE_DIM ORG, USERS U, STUDENT_BIO_DIM STU " + 
						 "WHERE ORG.LEVEL3_JASPER_ORGID = ? " + 
						   "AND U.ORG_ID = ORG.LEVEL4_JASPER_ORGID " + 
						   "AND STU.ORG_NODEID = ORG.ORG_NODEID " + 
						   "AND u.new_user = 'Y' and u.activation_status = 'AC' " + 
						   " and u.adminid = (select adminid from admin_dim where current_admin = 'Y') " +
						 "GROUP BY ORG.LEVEL4_ELEMENT_NAME, " + 
						          "ORG.LEVEL4_JASPER_ORGID, " + 
						          "ORG.ORG_NODEID, " + 
						          "U.USER_ID, " + 
						          "U.USERNAME, U.ORG_ID " + 
						 "ORDER BY ORG.ORG_NODEID";*/
			CustomStringUtil.appendString(
					"SELECT COUNT(SBD.STUDENT_BIO_ID),",
					"       OND.ORG_NODE_NAME,",
					"       OND.ORG_NODEID,",
					"       OND.ORG_NODEID,",
					"       U.USERID,",
					"       U.USERNAME,",
					"       OND.ORG_NODEID",
					"  FROM USERS U, ORG_USERS OU, ORG_NODE_DIM OND, STUDENT_BIO_DIM SBD",
					" WHERE U.USERID = OU.USERID",
					"   AND OU.ORG_NODEID = OND.ORG_NODEID",
					"   AND OND.PARENT_ORG_NODEID = ?",
					"   AND SBD.ORG_NODEID = OND.ORG_NODEID",
					" GROUP BY OND.ORG_NODE_NAME, OND.ORG_NODEID, U.USERID, U.USERNAME",
					" ORDER BY OND.ORG_NODEID", "");
			
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, jasperOrgId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				teacher = new OrgTO();
				teacher.setStudentCount(rs.getString(1));
				String elementName = rs.getString(2);
				teacher.setElementName(elementName);
				teacher.setFullName(elementName);
				teacher.setJasperOrgId(rs.getString(3));
				teacher.setOrgNodeId(rs.getString(4));
				teacher.setUserId(rs.getString(5)); // this is jasper user id
				teacher.setUserName(rs.getString(6)); // userid 
				teacher.setTenantId(rs.getString(7));
				allTeachers.add(teacher);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		return getGradeForTeacherUsersAcsi(allTeachers);
	}
	
	/**
	 * Fetch grade for each teacher
	 * @param teachers
	 * @return
	 * @throws Exception 
	 */
	public List<OrgTO> getGradeForTeacherUsersAcsi(List<OrgTO> teachers) throws Exception {
		StringBuffer allNodes = new StringBuffer();
		StringBuffer allJasperOrgIds = new StringBuffer();
		boolean firstRec = false;
		for(OrgTO t : teachers) {
			if(firstRec) {
				allNodes.append(",");
				allJasperOrgIds.append(",");
			}
			firstRec = true;
			allNodes.append(t.getOrgNodeId());
			allJasperOrgIds.append(t.getJasperOrgId());
		}
		//System.out.println("nodes"+allNodes);
		List<OrgTO> allGrades = null;
		List<UserTO> allStudents = null;
		List<UserTO> students = null;
		if(allNodes.toString() != null && allNodes.toString().length() > 0) {
			System.out.println("   Fetching grades for teachers");
			//allGrades = getGrades(allNodes.toString());
			allGrades = getGradesAcsi(allJasperOrgIds.toString());
			
			System.out.println("   Fetching students for teachers");
			allStudents = getStudentsAcsi(allNodes.toString());
		
			for(OrgTO tech : teachers) {
				for(OrgTO grade : allGrades) {
					if(tech.getJasperOrgId() != null && tech.getJasperOrgId().equals(grade.getJasperOrgId())) {
						tech.setGrade(grade.getGrade());
						tech.setGradeSeq(grade.getGradeSeq());
						break;
					}
				}
				students = new ArrayList<UserTO>();
				for(UserTO student : allStudents) {
					if(tech.getOrgNodeId() != null && tech.getOrgNodeId().equals(student.getOrgNodeId())) {
						students.add(student);
					}
				}
				tech.setUsers(students);
			}
		}
		
		return teachers;
	}
	
	/**
	 * get grade for teacher
	 * @param jasperOrgId
	 * @return
	 * @throws Exception 
	 */
	public List<OrgTO> getGradesAcsi(String jasperOrgIds) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<OrgTO> allGrades = new ArrayList<OrgTO>();
		OrgTO grades = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			/*String query = "SELECT DISTINCT grade.grade_name, FACT.ORG_NODEID " + 
							  "FROM SUBTEST_SCORE_FACT FACT, GRADE_DIM GRADE " + 
							 "WHERE FACT.ORG_NODEID in ( "+ nodeId +" )" +
							 "AND grade.gradeid = fact.gradeid";*/
			String query = /*"SELECT DISTINCT GRADE.GRADE_NAME, gsl.jasper_orgid, GRADE.GRADE_SEQ  " +
					"FROM GRADE_SELECTION_LOOKUP GSL, ADMIN_DIM ADM, GRADE_DIM GRADE " +
					"WHERE ADM.ADMINID = GSL.ADMINID " +
					"AND grade.gradeid = gsl.gradeid " +
					"AND ADM.CURRENT_ADMIN = 'Y' " +
					"AND gsl.jasper_orgid in ( "+ jasperOrgIds +" ) ";*/
					CustomStringUtil.appendString(
							"select distinct gd.grade_name, ond.org_nodeid, GD.GRADE_SEQ",
							"  from users           u,",
							"       org_users       ou,",
							"       org_node_dim    ond,",
							"       student_bio_dim sbd,",
							"       grade_dim       gd",
							" where u.userid = ou.userid",
							"   and ou.org_nodeid = ond.org_nodeid",
							"   and ond.parent_org_nodeid in ( "+ jasperOrgIds +" )",
							"   and sbd.org_nodeid = ond.org_nodeid",
							"   and sbd.gradeid = gd.gradeid",
							" order by ond.org_nodeid");
			
			pstmt = conn.prepareCall(query);
			//pstmt.setString(1, nodeId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				grades = new OrgTO();
				grades.setGrade(rs.getString(1));
				grades.setJasperOrgId(rs.getString(2));
				grades.setGradeSeq(rs.getInt(3));
				allGrades.add(grades);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		return allGrades;
	}
	
	/**
	 * fetching top 5 students alphabetically
	 * @param nodeId
	 * @return
	 * @throws Exception 
	 */
	public List<UserTO> getStudentsAcsi(String nodeId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<UserTO> students = new ArrayList<UserTO>();
		UserTO student = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			
			String query = " SELECT * FROM (SELECT stu.org_nodeid, " +
					"stu.last_name || ', ' || stu.first_name || ' ' || stu.middle_name " +
					"FROM student_bio_dim stu WHERE stu.org_nodeid = ? ORDER BY stu.last_name, stu.first_name, stu.middle_name " +
					"  ) WHERE ROWNUM < 6 ";
			
			String[] nodearr = nodeId.split(",");
			int count = 0;
			StringBuffer queryBuf = new StringBuffer();
			for(String node : nodearr) {
				if(count > 0) {
					queryBuf.append("UNION ALL");
				}
				count++;
				queryBuf.append(query);
			}
			
			pstmt = conn.prepareCall(queryBuf.toString());
			//System.out.println(queryBuf.toString());
			count = 0;
			for(String node : nodearr) {
				pstmt.setString(++count, node);
			}
			rs = pstmt.executeQuery();
			while(rs.next()) {
				student = new UserTO();
				student.setOrgNodeId(rs.getString(1));
				student.setStudentName(rs.getString(2));
				students.add(student);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		return students;
	}
}
