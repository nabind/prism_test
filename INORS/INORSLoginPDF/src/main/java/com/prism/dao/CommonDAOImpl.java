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
}
