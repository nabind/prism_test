package com.prism.dao;

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
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.prism.jdcc.JDCConnectionDriver;
import com.prism.to.OrgTO;
import com.prism.to.UserTO;
import com.prism.util.Constants;
import com.prism.util.CustomStringUtil;

public class TascDao extends CommonDao {

	private static final Logger logger = Logger.getLogger(TascDao.class);
	static JDCConnectionDriver driver = null;
	static String DATA_SOURCE = "jdbc:jdc:acsi";

	static String ACSI = "ROLE_ACSI";
	static String SCHOOL = "ROLE_SCHOOL";
	static String TEACHER = "ROLE_TEACHER";
	static String USER = "ROLE_USER";
	static String ADMIN = "ROLE_ADMINISTRATOR";
	static String CTB = "ROLE_ADMIN";

	/**
	 * Constructor. It initializes the JDCConnectionDriver instance.
	 * 
	 * @param prop
	 * @throws Exception
	 */
	public TascDao(Properties prop) throws Exception {
		String dbURL = prop.getProperty("dbURL");
		String dbUserName = prop.getProperty("dbUserName");
		String dbPassword = prop.getProperty("dbPassword");
		driver = new JDCConnectionDriver("oracle.jdbc.driver.OracleDriver", dbURL, dbUserName, dbPassword);

	}

	/**
	 * Fetch org information
	 * 
	 * @param jasperOrgId
	 * @param state
	 * @param hierarchical
	 * @return
	 * @throws Exception
	 */
	public OrgTO getSchoolDetails(String jasperOrgId, boolean state, boolean hierarchical) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrgTO school = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = CustomStringUtil.appendString("SELECT ORG_NODEID,", // 1
					"   ORG_NODE_NAME,", // 2
					"   ORG_NODE_CODE,", // 3
					"   ORG_NODE_LEVEL,", // 4
					"   STRC_ELEMENT,", // 5 // Unused
					"   SPECIAL_CODES,", // 6 // Unused
					"   PARENT_ORG_NODEID,", // 7
					"   ORG_NODE_CODE_PATH,",// 8 // Unused
					"   EMAILS,", // 9
					"   CUSTOMERID,", // 10
					"   TO_CHAR(SYSDATE, 'DDMM') AS DATE_STR,", // 11
					"   TO_CHAR(SYSDATE, 'DDMMYYHH24MISS') AS DATE_STR_WT_YEAR,", // 12
					"   substr(ORG_NODE_CODE_PATH,3,3) STATE_CODE  ", //13
					" FROM ORG_NODE_DIM", " WHERE ORG_NODEID = ?");
			
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, jasperOrgId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				school = new OrgTO();
				school.setElementName(rs.getString(2));
				school.setOrgNodeId(rs.getString(1));
				school.setOrgNodeCode(rs.getString(3));
				school.setOrgNodeLevel(rs.getString(4));
				school.setJasperOrgId(rs.getString(1));
				school.setEmail(rs.getString(9));
				school.setCustomerCode(rs.getString(10));
				school.setParentJasperOrgId(rs.getString(7));
				school.setDateStr(rs.getString(11));
				school.setDateStrWtYear(rs.getString(12));
				school.setLvlOneOrgCode(rs.getString(13));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		if (school != null) {
			List<UserTO> users = getSchoolUsers(school.getJasperOrgId(), hierarchical);
			school.setUsers(users);
		}
		return school;
	}

	/**
	 * Fetch Education Center information
	 * 
	 * @param eduCenterId
	 * @return
	 * @throws Exception
	 */
	public OrgTO getEducationCenterDetails(String eduCenterId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrgTO school = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = CustomStringUtil.appendString("SELECT EDU_CENTERID,", // 1
					"   EDU_CENTER_NAME,", // 2
					"   EDU_CENTER_CODE,", // 3
					"   '-99' AS ORG_NODE_LEVEL,", // 4
					"   EMAILS,", // 5
					"   CUSTOMERID,", // 6
					"   TO_CHAR(SYSDATE, 'DDMM')           AS DATE_STR,", // 7
					"   TO_CHAR(SYSDATE, 'DDMMYYHH24MISS') AS DATE_STR_WT_YEAR", // 8
					" FROM EDU_CENTER_DETAILS ECD", " WHERE ECD.EDU_CENTERID = ?");
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, eduCenterId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				school = new OrgTO();
				school.setElementName(rs.getString(2));
				school.setOrgNodeId(rs.getString(1));
				school.setOrgNodeCode(rs.getString(3));
				school.setOrgNodeLevel(rs.getString(4));
				school.setJasperOrgId(rs.getString(1));
				school.setEmail(rs.getString(5));
				school.setCustomerCode(rs.getString(6));
				school.setDateStr(rs.getString(7));
				school.setDateStrWtYear(rs.getString(8));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		if (school != null) {
			List<UserTO> users = getEducationCenterUsers(school.getJasperOrgId());
			school.setUsers(users);
		}
		return school;
	}

	/**
	 * Fetch all org users
	 * 
	 * @param jasperOrgId
	 * @return
	 * @throws Exception
	 */
	public List<UserTO> getSchoolUsers(String jasperOrgId, boolean hierarchical) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<UserTO> allUsers = new ArrayList<UserTO>();
		UserTO user = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = CustomStringUtil.appendString("SELECT U.USERID,", "   U.USERNAME,", "   U.DISPLAY_USERNAME,", "   O.ORG_NODEID,",
					"   O.ORG_NODE_LEVEL,", "   O.ORG_NODE_NAME,", "   O.ORG_NODE_CODE_PATH", " FROM ORG_USERS OU,", "   USERS U,",
					"   (SELECT ORG_NODEID, ORG_NODE_LEVEL, ORG_NODE_NAME, ORG_NODE_CODE_PATH ", "   FROM ORG_NODE_DIM", "     WHERE ORG_NODEID       = ?",
					"   ) O", " WHERE OU.USERID         = U.USERID", " AND OU.ORG_NODEID       =O.ORG_NODEID", " AND U.ACTIVATION_STATUS = 'AC'",
					" AND U.ACTIVATION_STATUS not in ('SS')", " AND U.IS_NEW_USER       = 'Y'");
			if (hierarchical == true) {
				query = CustomStringUtil.appendString("SELECT U.USERID,", "   U.USERNAME,", "   U.DISPLAY_USERNAME,", "   O.ORG_NODEID,",
						"   O.ORG_NODE_LEVEL,", "   O.ORG_NODE_NAME,", "   O.ORG_NODE_CODE_PATH", " FROM ORG_USERS OU,", "   USERS U,", "   (SELECT *",
						"   FROM ORG_NODE_DIM", "     START WITH ORG_NODEID       = ?", "     CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID", "   ) O",
						" WHERE OU.USERID         = U.USERID", " AND OU.ORG_NODEID       =O.ORG_NODEID", " AND U.ACTIVATION_STATUS = 'AC'",
						" AND U.ACTIVATION_STATUS not in ('SS')", " AND U.IS_NEW_USER       = 'Y'");
			}
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, jasperOrgId);
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
				allUsers.add(user);
			}
			logger.debug("Returning org users");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return getSchoolUsersWithRoles(allUsers);
	}

	/**
	 * Fetch all Education Center users
	 * 
	 * @param jasperOrgId
	 * @return
	 * @throws Exception
	 */
	public List<UserTO> getEducationCenterUsers(String jasperOrgId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<UserTO> allUsers = new ArrayList<UserTO>();
		UserTO user = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = CustomStringUtil.appendString(
					"SELECT U.USERID,", // 1
					"   U.USERNAME,", // 2
					"   U.DISPLAY_USERNAME,", // 3
					"   ECD.EDU_CENTERID,", // 4
					"   '-99' AS ORG_NODE_LEVEL,", // 5
					"   ECD.EDU_CENTER_NAME", // 6
					" FROM EDU_CENTER_DETAILS ECD, EDU_CENTER_USER_LINK ECUL, USERS U WHERE ECD.EDU_CENTERID = ?",
					" AND ECD.EDU_CENTERID = ECUL.EDU_CENTERID", " AND ECUL.USERID = U.USERID AND U.USERNAME NOT IN ('ctbadmin')");
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, jasperOrgId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				user = new UserTO();
				String userId = rs.getString(1);
				user.setJasperUserId(userId);
				user.setUserId(userId);
				user.setUserName(rs.getString(2));
				user.setFullName(rs.getString(3));
				user.setOrgNodeId(rs.getString(4));
				user.setOrgNodeLevel(rs.getString(5));
				user.setOrgNodeName(rs.getString(6));
				allUsers.add(user);
			}
			logger.debug("Returning education center users");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return getSchoolUsersWithRoles(allUsers);
	}

	/**
	 * Update new user flag for all users in the userList
	 * 
	 * @param userList
	 * @return
	 * @throws Exception
	 */
	public int[] updateNewuserFlag(List<UserTO> userList) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int updateCount[];
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = "UPDATE USERS SET IS_NEW_USER = 'N', PASSWORD = ?, SALT = ?, UPDATED_DATE_TIME = SYSDATE WHERE IS_NEW_USER = 'Y' AND USERNAME = ? ";
			pstmt = conn.prepareStatement(query);
			for (UserTO user : userList) {
				pstmt.setString(1, user.getEncPassword());
				pstmt.setString(2, user.getSalt());
				pstmt.setString(3, user.getUserName());
				pstmt.addBatch();
			}
			updateCount = pstmt.executeBatch();
			logger.info("Records updated: " + updateCount);
			conn.commit();
			logger.debug("commit successful");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt);
		}
		return updateCount;
	}

	/**
	 * Update new user flag for all teachers
	 * 
	 * @param teachers
	 * @return
	 * @throws Exception
	 */
	public int updateTeacherNewUserFlag(List<OrgTO> teachers) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int updateCount = 0;
		boolean firstRec = false;
		StringBuffer allNodes = new StringBuffer();
		try {
			for (OrgTO t : teachers) {
				if (firstRec) {
					allNodes.append(",");
				}
				firstRec = true;
				allNodes.append(t.getJasperOrgId());
			}
			conn = driver.connect(DATA_SOURCE, null);
			String nodes = allNodes.toString();
			if (nodes != null && nodes.trim().length() == 0) {
				nodes = "0";
			}
			String query = "UPDATE USERS SET NEW_USER = 'N', UPDATED_DATE_TIME = SYSDATE WHERE NEW_USER = 'Y' AND ORG_ID IN ( " + nodes + " ) ";
			pstmt = conn.prepareCall(query);
			updateCount = pstmt.executeUpdate();
			logger.debug("Returning update count");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt);
		}
		return updateCount;
	}

	/**
	 * Populates the Role List for all the users
	 * 
	 * @param users
	 * @return
	 * @throws Exception
	 */
	public List<UserTO> getSchoolUsersWithRoles(List<UserTO> users) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = "SELECT R.DESCRIPTION FROM USER_ROLE UR, ROLE R WHERE UR.USERID = ? AND UR.ROLEID = R.ROLEID ORDER BY DESCRIPTION";
			pstmt = conn.prepareCall(query);
			for (UserTO user : users) {
				pstmt.setString(1, user.getUserId());
				rs = pstmt.executeQuery();
				List<String> roleList = new ArrayList<String>();
				while (rs.next()) {
					roleList.add(rs.getString(1));
				}
				user.setRoles(roleList);
			}
			logger.debug("Returning roles");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return users;
	}

	/**
	 * Creates the organization Map where the organization id is the key and the
	 * organization name is the value.
	 * 
	 * @param userList
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<Integer, String> getOrgMap(List<UserTO> userList) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<Integer, String> orgMap = new HashMap<Integer, String>();
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String ids = getIds(userList);
			String query = CustomStringUtil.appendString(" SELECT ORG_NODEID, ORG_NODE_NAME ", " FROM ORG_NODE_DIM", " WHERE ORG_NODEID IN (", ids, ")");
			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				orgMap.put(rs.getInt(1), rs.getString(2));
			}
			logger.debug("Returning org Map");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
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
	 * Fetch Education Center information
	 * 
	 * @param eduCenterId
	 * @return
	 * @throws Exception
	 */
	public OrgTO getSupportEmailForCustomer(String customerId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrgTO orgTO = new OrgTO();
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = CustomStringUtil.appendString("SELECT SUPPORT_EMAIL, SEND_LOGIN_PDF, CUSTOMER_NAME FROM CUSTOMER_INFO WHERE CUSTOMERID = ?");
			pstmt = conn.prepareCall(query);
			pstmt.setLong(1, Long.valueOf(customerId));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				orgTO.setEmail(rs.getString(1));
				orgTO.setSendLoginPdf(rs.getString(2));
				orgTO.setCustomerName(rs.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return orgTO;
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
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String parentOrgNodeId = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = "SELECT PARENT_ORG_NODEID FROM ORG_NODE_DIM WHERE ORG_NODEID = ?";
			pstmt = conn.prepareCall(query);
			pstmt.setLong(1, Long.valueOf(orgNodeId));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				parentOrgNodeId = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return parentOrgNodeId;
	}

	public Map<String, String> getOrgLabelMap() {
		Map<String, String> orgLabelMap = new HashMap<String, String>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = CustomStringUtil.appendString(
					"SELECT TEMP.ORG_LEVEL, LISTAGG(TEMP.ORG_LABEL, '/') WITHIN GROUP(ORDER BY TEMP.ORG_LEVEL) AS ORG_LABEL",
					" FROM (SELECT  DISTINCT ORG_LEVEL,ORG_LABEL FROM ORG_TP_STRUCTURE ORDER BY ORG_LEVEL) TEMP", " GROUP BY TEMP.ORG_LEVEL");
			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				orgLabelMap.put(rs.getString(1), rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return orgLabelMap;
	}

	public String getCustomerCode(String customerId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String ustomerCode = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = "SELECT CUSTOMER_CODE FROM CUSTOMER_INFO WHERE CUSTOMERID = ?";
			pstmt = conn.prepareCall(query);
			pstmt.setLong(1, Long.valueOf(customerId));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				ustomerCode = rs.getString("CUSTOMER_CODE");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return ustomerCode;
	}
	
	public String getRootPathForLoginPdf(String customerId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String rootPath = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			pstmt = conn.prepareCall(Constants.GET_LOGINPDF_PATH);
			pstmt.setLong(1, Long.valueOf(customerId));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				rootPath = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		logger.debug("customerId=" + customerId + ", Returning Root Path = " + rootPath);
		return rootPath;
	}
	
	public String getHierarchyFileName() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String hierarchyFile = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			pstmt = conn.prepareCall(Constants.GET_HIERARCHY_FILE);
			
			rs = pstmt.executeQuery();
			if (rs.next()) {
				hierarchyFile = rs.getString(1) ;
			}
			
			hierarchyFile = hierarchyFile != null ? hierarchyFile: "";
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		logger.debug("HierarchyFile=" + hierarchyFile);
		return hierarchyFile;
	}
	
}