package com.prism.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.prism.jdcc.JDCConnectionDriver;
import com.prism.to.OrgTO;
import com.prism.to.UserTO;
import com.prism.util.BasicFormatterImpl;
import com.prism.util.CustomStringUtil;

public class InorsGrowthDao extends CommonDao {

	private final Logger logger = Logger.getLogger(InorsGrowthDao.class);
	JDCConnectionDriver driver = null;
	String DATA_SOURCE = "jdbc:jdc:acsi";
	String ACSI = "ROLE_ACSI";
	String SCHOOL = "ROLE_SCHOOL";
	String TEACHER = "ROLE_TEACHER";
	String USER = "ROLE_USER";
	String ADMIN = "ROLE_ADMINISTRATOR";
	String CTB = "ROLE_ADMIN";

	/**
	 * Constructor. It initializes the JDCConnectionDriver instance.
	 * 
	 * @param prop
	 * @throws Exception
	 */
	public InorsGrowthDao(Properties prop) throws Exception {
		String dbURL = prop.getProperty("dbURL");
		String dbUserName = prop.getProperty("dbUserName");
		String dbPassword = prop.getProperty("dbPassword");
		driver = new JDCConnectionDriver("oracle.jdbc.driver.OracleDriver", dbURL, dbUserName, dbPassword);
	}

	/**
	 * get current admin year
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getCurrentAdminYear() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String adminid = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = "SELECT ADMINID FROM ADMIN_DIM WHERE IS_CURRENT_ADMIN = 'Y'";
			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				adminid = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return adminid;
	}

	/**
	 * Fetch school information
	 * 
	 * @param jasperOrgId
	 * @return
	 * @throws Exception
	 */
	public OrgTO getSchoolDetails(String jasperOrgId, boolean state, Properties prop) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrgTO school = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = CustomStringUtil.appendString("SELECT SCHOOL.ORG_NODE_NAME AS SCHOOL_NAME,", " SCHOOL.ORG_NODE_CODE AS SCHOOL_CODE,",
					" DISTRICT.ORG_NODE_NAME AS DISTRICT_NAME,", " DISTRICT.ORG_NODE_CODE AS DISTRICT_CODE,", " DISTRICT.ORG_MODE AS ORG_MODE,",
					" SCHOOL.ORG_NODEID,", " SCHOOL.ORG_NODE_LEVEL,", " SCHOOL.STRC_ELEMENT,", " SCHOOL.SPECIAL_CODES,", " SCHOOL.PARENT_ORG_NODEID,",
					" SCHOOL.ORG_NODE_CODE_PATH,", " SCHOOL.EMAILS,", " SCHOOL.CUSTOMERID,", " TO_CHAR(SYSDATE, 'DDMM') AS DATE_STR,",
					" TO_CHAR(SYSDATE, 'DDMMYYHH24MISS') AS DATE_STR_WT_YEAR", " FROM ORG_NODE_DIM SCHOOL, ORG_NODE_DIM DISTRICT",
					" WHERE SCHOOL.PARENT_ORG_NODEID = DISTRICT.ORG_NODEID", " AND SCHOOL.ORG_NODE_LEVEL = 3", " AND DISTRICT.ORG_NODE_LEVEL = 2",
					" AND SCHOOL.ORG_NODEID = ?");
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, jasperOrgId);
			String pQuery = CustomStringUtil.setQueryString(query, 1, jasperOrgId);
			pQuery = new BasicFormatterImpl().format(pQuery);
			logger.debug(CustomStringUtil.getQueryString(pQuery));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				school = new OrgTO();
				school.setElementName(rs.getString("SCHOOL_NAME"));
				school.setOrgNode(rs.getString("ORG_NODEID"));
				school.setOrgNodeCode(rs.getString("SCHOOL_CODE"));
				school.setParentOrgNodeCode(rs.getString("DISTRICT_CODE"));
				school.setJasperOrgId(rs.getString("ORG_NODEID"));
				school.setEmail(rs.getString("EMAILS"));
				school.setCustomerCode(rs.getString("CUSTOMERID"));
				school.setParentJasperOrgId(rs.getString("PARENT_ORG_NODEID"));
				school.setDateStr(rs.getString("DATE_STR"));
				school.setDateStrWtYear(rs.getString("DATE_STR_WT_YEAR"));
				school.setOrgNodeLevel(rs.getString("ORG_NODE_LEVEL"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		if (school != null) {
			List<UserTO> users = getUsers(school.getJasperOrgId(), prop);
			school.setUsers(users);
		}
		return school;
	}

	/**
	 * Fetch all users under the org node id.
	 * 
	 * @param jasperOrgId
	 * @return
	 * @throws Exception
	 */
	public List<UserTO> getUsers(String jasperOrgId, Properties prop) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<UserTO> allUsers = new ArrayList<UserTO>();
		UserTO user = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = CustomStringUtil.appendString("SELECT U.USERID,", " U.USERNAME,", " U.DISPLAY_USERNAME,", " O.ORG_NODEID,", " O.ORG_NODE_LEVEL,",
					" O.ORG_NODE_NAME,", " O.ORG_NODE_CODE_PATH,", " U.USER_TYPE", " FROM ORG_USERS OU,", " USERS U,",
					" (SELECT ORG_NODEID, ORG_NODE_LEVEL, ORG_NODE_NAME, ORG_NODE_CODE_PATH", " FROM ORG_NODE_DIM", " WHERE ORG_NODEID = ?) O",
					" WHERE OU.USERID = U.USERID", " AND OU.ORG_NODEID = O.ORG_NODEID", " AND U.ACTIVATION_STATUS = 'SS'", " AND U.IS_NEW_USER = 'Y'",
					" AND U.USER_TYPE LIKE 'GRW%'     ", " ORDER BY USER_TYPE DESC, USERNAME ASC");
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, jasperOrgId);
			String pQuery = CustomStringUtil.setQueryString(query, 1, jasperOrgId);
			pQuery = new BasicFormatterImpl().format(pQuery);
			logger.debug(CustomStringUtil.getQueryString(pQuery));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				user = new UserTO();
				String userId = rs.getString("USERID");
				user.setJasperUserId(userId);
				user.setUserId(userId);
				user.setUserName(rs.getString("USERNAME"));
				user.setFullName(rs.getString("DISPLAY_USERNAME"));
				user.setOrgNodeId(rs.getString("ORG_NODEID"));
				user.setUserType(rs.getString("USER_TYPE"), prop);
				user.setOrgLevel(rs.getString("ORG_NODE_LEVEL"));
				allUsers.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return fetchAdminColumn(allUsers);
	}

	/**
	 * Check if user is admin user or not
	 * 
	 * @param users
	 * @return
	 * @throws Exception
	 */
	public List<UserTO> fetchAdminColumn(List<UserTO> users) throws Exception {
		StringBuffer allUserIds = new StringBuffer();
		boolean firstRec = false;
		for (UserTO u : users) {
			if (firstRec) {
				allUserIds.append(",");
			}
			firstRec = true;
			allUserIds.append(u.getUserId());
		}
		List<UserTO> allUsers = null;
		if (allUserIds.toString() != null && allUserIds.toString().length() > 0) {
			allUsers = checkAdmin(allUserIds.toString());
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
	public List<UserTO> checkAdmin(String allUserIds) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<UserTO> allUsers = new ArrayList<UserTO>();
		UserTO user = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = "SELECT RLE.ROLE_NAME, URLE.USERID FROM ROLE RLE, USER_ROLE URLE "
					+ "WHERE URLE.ROLEID = RLE.ROLEID AND RLE.ROLE_NAME = 'ROLE_GRW' " + "AND URLE.USERID IN ( " + allUserIds + " )";
			pstmt = conn.prepareCall(query);
			logger.debug(query);
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
			releaseResources(conn, pstmt, rs);
		}
		return allUsers;
	}

	/**
	 * Update new user flag for school users
	 * 
	 * @param UserTO
	 * @return
	 * @throws Exception
	 */
	public int updateNewuserFlag(UserTO user) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int updateCount = 0;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = "UPDATE USERS SET IS_NEW_USER = 'N', PASSWORD = ?, SALT = ?, UPDATED_DATE_TIME=SYSDATE WHERE IS_NEW_USER = 'Y' AND USERNAME = ? ";
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, user.getEncPassword());
			pstmt.setString(2, user.getSalt());
			pstmt.setString(3, user.getUserName());
			logger.debug(CustomStringUtil.getQueryString(query));
			logger.info("Updating Users. Please wait...");
			updateCount = pstmt.executeUpdate();
			logger.info("Update count = " + updateCount);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt);
		}
		return updateCount;
	}

	/**
	 * Updates the IS_NEW_USER flag.
	 * 
	 * @param userList
	 */
	public void updateNewuserFlag(List<UserTO> userList) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int updateCount[] = null;
		try {
			logger.info("Updating " + userList.size() + " users. Please wait...");
			conn = driver.connect(DATA_SOURCE, null);
			String query = "UPDATE USERS SET IS_NEW_USER = 'N', PASSWORD = ?, SALT = ?, UPDATED_DATE_TIME=SYSDATE WHERE IS_NEW_USER = 'Y' AND USERNAME = ? ";
			pstmt = conn.prepareStatement(query);
			for (UserTO user : userList) {
				pstmt.setString(1, user.getEncPassword());
				pstmt.setString(2, user.getSalt());
				pstmt.setString(3, user.getUserName());
				pstmt.addBatch();
			}
			updateCount = pstmt.executeBatch();
			int count = 0;
			for (int i = 0; i < updateCount.length; i++) {
				count = count + updateCount[i];
			}
			if (conn.getAutoCommit() == false) {
				conn.commit();
			}
			logger.debug("Update count = " + count);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseResources(conn, pstmt);
		}
	}

	/**
	 * Updates the IS_NEW_USER for all teachers.
	 * 
	 * @param teachers
	 * @return
	 * @throws Exception
	 */

	public int updateTeacherNewuserFlag(List<OrgTO> teachers) throws Exception {
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
			String query = "UPDATE users SET new_user = 'N', updated_date_time=sysdate  WHERE new_user = 'Y' AND org_id in ( " + nodes + " ) ";

			pstmt = conn.prepareCall(query);
			updateCount = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt);
		}
		return updateCount;
	}

	/**
	 * get all users for each teacher
	 * 
	 * @param teachers
	 * @return
	 * @throws Exception
	 */
	public List<OrgTO> getTeacherUsers(List<OrgTO> teachers, Properties prop) throws Exception {
		for (OrgTO t : teachers) {
			List<UserTO> users = getUsers(t.getJasperOrgId(), prop);
			t.setUsers(users);
		}
		return teachers;
	}

	/**
	 * Fetch grade for each teacher
	 * 
	 * @param teachers
	 * @return
	 * @throws Exception
	 */
	public List<OrgTO> getGradeForTeacherUsers(List<OrgTO> teachers) throws Exception {
		StringBuffer allNodes = new StringBuffer();
		StringBuffer allJasperOrgIds = new StringBuffer();
		boolean firstRec = false;
		for (OrgTO t : teachers) {
			if (firstRec) {
				allNodes.append(",");
				allJasperOrgIds.append(",");
			}
			firstRec = true;
			allNodes.append(t.getOrgNode());
			allJasperOrgIds.append(t.getJasperOrgId());
		}
		List<OrgTO> allGrades = null;
		List<UserTO> allStudents = null;
		List<UserTO> students = null;
		if (allNodes.toString() != null && allNodes.toString().length() > 0) {
			logger.info("Fetching grades for teachers. Please wait...");
			allGrades = getGrades(allJasperOrgIds.toString());
			logger.info("Fetching students for teachers. Please wait...");
			allStudents = getStudents(allNodes.toString());
			for (OrgTO tech : teachers) {
				for (OrgTO grade : allGrades) {
					if (tech.getJasperOrgId() != null && tech.getJasperOrgId().equals(grade.getJasperOrgId())) {
						tech.setGrade(grade.getGrade());
						tech.setGradeSeq(grade.getGradeSeq());
						break;
					}
				}
				students = new ArrayList<UserTO>();
				for (UserTO student : allStudents) {
					if (tech.getOrgNode() != null && tech.getOrgNode().equals(student.getOrgNodeId())) {
						students.add(student);
					}
				}
				tech.setUsers(students);
			}
		}
		return teachers;
	}

	/**
	 * fetching top 5 students alphabetically
	 * 
	 * @param nodeId
	 * @return
	 * @throws Exception
	 */
	public List<UserTO> getStudents(String nodeId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<UserTO> students = new ArrayList<UserTO>();
		UserTO student = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = " SELECT * FROM (SELECT stu.org_nodeid, " + "stu.last_name || ', ' || stu.first_name || ' ' || stu.middle_name "
					+ "FROM student_bio_dim stu WHERE stu.org_nodeid = ? ORDER BY stu.last_name, stu.first_name, stu.middle_name " + "  ) WHERE ROWNUM < 6 ";
			String[] nodearr = nodeId.split(",");
			int count = 0;
			StringBuffer queryBuf = new StringBuffer();
			for (@SuppressWarnings("unused")
			String node : nodearr) {
				if (count > 0) {
					queryBuf.append("UNION ALL");
				}
				count++;
				queryBuf.append(query);
			}
			pstmt = conn.prepareCall(queryBuf.toString());
			logger.info(CustomStringUtil.getQueryString(queryBuf.toString()));
			count = 0;
			for (String node : nodearr) {
				pstmt.setString(++count, node);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				student = new UserTO();
				student.setOrgNodeId(rs.getString(1));
				student.setStudentName(rs.getString(2));
				students.add(student);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return students;
	}

	/**
	 * get grade for teacher
	 * 
	 * @param jasperOrgId
	 * @return
	 * @throws Exception
	 */
	public List<OrgTO> getGrades(String jasperOrgIds) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<OrgTO> allGrades = new ArrayList<OrgTO>();
		OrgTO grades = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = "SELECT DISTINCT GRADE.GRADE_NAME, gsl.jasper_orgid, GRADE.GRADE_SEQ  "
					+ "FROM GRADE_SELECTION_LOOKUP GSL, ADMIN_DIM ADM, GRADE_DIM GRADE " + "WHERE ADM.ADMINID = GSL.ADMINID "
					+ "AND grade.gradeid = gsl.gradeid " + "AND ADM.CURRENT_ADMIN = 'Y' " + "AND gsl.jasper_orgid in ( " + jasperOrgIds + " ) ";

			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
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
			releaseResources(conn, pstmt, rs);
		}
		return allGrades;
	}

	/**
	 * Fetch all teacher org with student count and login user
	 * 
	 * @param jasperOrgId
	 * @return
	 * @throws Exception
	 */
	public List<OrgTO> getAllTeachers(String jasperOrgId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<OrgTO> allTeachers = new ArrayList<OrgTO>();
		OrgTO teacher = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = "SELECT COUNT(STU.STUDENT_BIO_ID), " + "ORG.LEVEL4_ELEMENT_NAME, " + "ORG.LEVEL4_JASPER_ORGID, " + "ORG.ORG_NODEID, "
					+ "U.USER_ID, " + "U.USERNAME, " + "U.ORG_ID " + "FROM ORG_NODE_DIM ORG, USERS U, STUDENT_BIO_DIM STU "
					+ "WHERE ORG.LEVEL3_JASPER_ORGID = ? " + "AND U.ORG_ID = ORG.LEVEL4_JASPER_ORGID " + "AND STU.ORG_NODEID = ORG.ORG_NODEID "
					+ "AND u.new_user = 'Y' and u.activation_status = 'AC' " + " and u.adminid = (select adminid from admin_dim where current_admin = 'Y') "
					+ "GROUP BY ORG.LEVEL4_ELEMENT_NAME, " + "ORG.LEVEL4_JASPER_ORGID, " + "ORG.ORG_NODEID, " + "U.USER_ID, " + "U.USERNAME, U.ORG_ID "
					+ "ORDER BY ORG.ORG_NODEID";

			pstmt = conn.prepareCall(query);
			pstmt.setString(1, jasperOrgId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				teacher = new OrgTO();
				teacher.setStudentCount(rs.getString(1));
				String elementName = rs.getString(2);
				teacher.setElementName(elementName);
				teacher.setFullName(elementName);
				teacher.setJasperOrgId(rs.getString(3));
				teacher.setOrgNode(rs.getString(4));
				teacher.setUserId(rs.getString(5));
				teacher.setUserName(rs.getString(6));
				teacher.setTenantId(rs.getString(7));
				allTeachers.add(teacher);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return getGradeForTeacherUsers(allTeachers);
	}

	/**
	 * check if new students are added.
	 * 
	 * @param jasperOrgId
	 * @return
	 * @throws Exception
	 */
	public boolean newStudentsLoaded(String jasperOrgId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int newStudCount = 0;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = " SELECT count(*) as new_student " + " FROM STUDENT_BIO_DIM STD, INVITATION_CODE IC, "
					+ "    ORG_NODE_DIM ORG,ADMIN_DIM ADM,grade_dim grd " + " WHERE  ORG.LEVEL3_JASPER_ORGID=? "
					+ "     AND IC.STUDENT_STRUC_ELEMENT = STD.STRUCTURE_ELEMENT " + "     AND STD.ORG_NODEID = ORG.ORG_NODEID "
					+ "     AND  STD.ADMINID = ADM.ADMINID " + "     and std.gradeid = grd.gradeid " + "     AND ic.activation_status = 'AC' "
					+ "     and ic.new_code = 'Y' " + "     AND ic.adminid = std.adminid "
					+ "     and ic.adminid = (select adminid from admin_dim where current_admin = 'Y') ";
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, jasperOrgId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				newStudCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return (newStudCount > 0) ? true : false;
	}

	/**
	 * Updates new code flag for all students
	 * 
	 * @param teachers
	 * @return
	 * @throws Exception
	 */
	public int updateNewActivationFlag(String jasperOrgId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int updateCount = 0;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = "UPDATE invitation_code SET new_code = 'N', updated_date_time=sysdate " + " WHERE new_code = 'Y' AND activation_status = 'AC' "
					+ " AND org_nodeid in (select org_nodeid from org_node_dim where level3_jasper_orgid = ?) ";
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, jasperOrgId);
			updateCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt);
		}
		return updateCount;
	}

	/**
	 * Checks if new school
	 * 
	 * @param jasperOrgId
	 *            , adminId
	 * @return
	 * @throws Exception
	 */
	public boolean newSchool(String jasperOrgId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int newOrgCount = 0;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = " select count(*) as ORG_COUNT "
					+ "  from org_structure_element curr_ose "
					+ " where curr_ose.org_level = 3 "
					+ "   and curr_ose.adminid = (select adminid from admin_dim where current_admin = 'Y') "
					+ "   and curr_ose.jasper_orgid = ? "
					+ "   and not exists (select jasper_orgid from org_structure_element prev_ose where curr_ose.jasper_orgid = prev_ose.jasper_orgid and curr_ose.adminid <> prev_ose.adminid) ";

			pstmt = conn.prepareCall(query);
			pstmt.setString(1, jasperOrgId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				newOrgCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return (newOrgCount > 0) ? true : false;
	}

	/**
	 * Insert into users table
	 * 
	 * @param allUsers
	 * @return
	 * @throws Exception
	 */

	public void migrateUserRedefined(List<UserTO> allUsers) {
		try {
			deleteUsers(allUsers);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			conn.setAutoCommit(false);
			String query = "insert into users (user_id, username, display_username, last_name, first_name, email_address, "
					+ "org_id, org_level, adminid, is_firsttime_login, activation_status, new_user, created_date_time) "
					+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, 'Y', ?, 'Y', sysdate) ";
			String query1 = "insert into user_role (user_id, role_id, created_date_time) values(?, ?, sysdate) ";
			pstmt = conn.prepareStatement(query);
			pstmt1 = conn.prepareStatement(query1);
			long count = 1;
			for (UserTO user : allUsers) {
				logger.info(count++ + " of " + allUsers.size() + " ... Adding users into batch ---- " + user.getUserName());
				migrateUsers(user, pstmt, pstmt1);
			}
			logger.info("------------------------------------------------");
			logger.info("Executing batch for users count :  " + allUsers.size());
			pstmt.executeBatch();
			logger.info("Executing batch for role count :  " + allUsers.size());
			pstmt1.executeBatch();
			conn.commit();
			logger.info("All users inserted into operational");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			releaseResources(conn, pstmt, pstmt1);
		}
	}

	/**
	 * @param user
	 * @param pstmt
	 * @param pstmt1
	 * @throws Exception
	 */
	public void migrateUsers(UserTO user, PreparedStatement pstmt, PreparedStatement pstmt1) throws Exception {
		String userSeq = null;
		try {
			String orgLevel = user.getOrgLevel();
			try {
				if (orgLevel != null) {
					Integer.parseInt(orgLevel);
				} else {
					throw new Exception("Not a valid organization user." + user.getUserName());
				}
			} catch (Exception ex) {
				throw new Exception("Not a valid organization user: " + user.getUserName());
			}
			userSeq = getUserSequence();
			pstmt.setString(1, userSeq);
			pstmt.setString(2, user.getUserName());
			pstmt.setString(3, user.getUserName().length() > 10 ? user.getUserName().substring(0, 10) : user.getUserName());
			pstmt.setString(4, "");
			pstmt.setString(5, "");
			pstmt.setString(6, user.getEmail());
			pstmt.setString(7, user.getTenantId());
			pstmt.setString(8, user.getOrgLevel());
			pstmt.setString(9, user.getAdminId());
			pstmt.setString(10, user.getEnabled());
			pstmt.addBatch();
			for (String role : user.getRoles()) {
				pstmt1.setString(1, userSeq);
				pstmt1.setString(2,
						(role.equals(ACSI)) ? "2" : (role.equals(SCHOOL)) ? "3" : (role.equals(TEACHER)) ? "4" : (role.equals(CTB)) ? "5"
								: (role.equals(ADMIN)) ? "7" : (role.equals(USER)) ? "1" : "8");
				pstmt1.addBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
		}
	}

	/**
	 * Deletes a user
	 * 
	 * @param jasperOrgId
	 * @return
	 * @throws Exception
	 */
	public void deleteUsers(List<UserTO> allUsers) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			conn.setAutoCommit(false);
			String query = "delete from user_role where user_id in (select user_id from users where username = ? ) ";
			String query1 = "delete from password_hint_answers where user_id in (select user_id from users where username = ? ) ";
			String query2 = "delete from users where username = ? ";
			pstmt = conn.prepareStatement(query);
			pstmt1 = conn.prepareStatement(query1);
			pstmt2 = conn.prepareStatement(query2);
			long count = 1;
			for (UserTO user : allUsers) {
				logger.info(count++ + " of " + allUsers.size() + " ... Adding users into batch (for deletion - if any) ---- " + user.getUserName());
				deleteUser(user, pstmt, pstmt1, pstmt2);
			}
			logger.info("------------------------------------------------");
			logger.info("Executing batch for users count :  " + allUsers.size());
			pstmt.executeBatch();
			pstmt1.executeBatch();
			pstmt2.executeBatch();
			conn.commit();
			logger.info("All entries (if any) deleted from users");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			releaseResources(conn, pstmt, pstmt1, pstmt2);
		}
	}

	/**
	 * @param user
	 * @param pstmt
	 * @param pstmt1
	 * @param pstmt2
	 * @throws Exception
	 */
	public void deleteUser(UserTO user, PreparedStatement pstmt, PreparedStatement pstmt1, PreparedStatement pstmt2) throws Exception {
		try {
			pstmt.setString(1, user.getUserName());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
		}
		try {
			pstmt1.setString(1, user.getUserName());
			pstmt1.addBatch();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
		}
		try {
			pstmt2.setString(1, user.getUserName());
			pstmt2.addBatch();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
		}
	}

	/**
	 * Insert into user_role table
	 * 
	 * @param jasperOrgId
	 * @return
	 * @throws Exception
	 */
	public void addPermission(UserTO user, String userSeq) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String query = "";
		try {
			conn = driver.connect(DATA_SOURCE, null);
			query = "insert into user_role (user_id, role_id, created_date_time) values(?, ?, sysdate) ";
			for (String role : user.getRoles()) {
				pstmt = conn.prepareCall(query);
				pstmt.setString(1, userSeq);
				pstmt.setString(2,
						(role.equals(ACSI)) ? "2" : (role.equals(SCHOOL)) ? "3" : (role.equals(TEACHER)) ? "4" : (role.equals(CTB)) ? "5"
								: (role.equals(ADMIN)) ? "7" : (role.equals(USER)) ? "1" : "8");
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt);
		}
	}

	/**
	 * Fetch all school information
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getUserSequence() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String userSeq = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = "select user_id_seq.nextval from dual ";
			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				userSeq = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return userSeq;
	}

	/**
	 * Gets district id for a given school id.
	 * 
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public String getDistrictId(String orgId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String districtId = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = "SELECT PARENT_ORG_NODEID FROM ORG_NODE_DIM WHERE ORG_NODEID = ?";
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, orgId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				districtId = rs.getString("PARENT_ORG_NODEID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return districtId;
	}

	/**
	 * Gets the district details.
	 * 
	 * @param jasperOrgId
	 * @param state
	 * @return
	 * @throws Exception
	 */
	public OrgTO getDistrictDetails(String jasperOrgId, boolean state, Properties prop) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrgTO district = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = CustomStringUtil.appendString("SELECT DISTRICT.ORG_NODE_NAME AS DISTRICT_NAME,", " DISTRICT.ORG_NODE_CODE AS DISTRICT_CODE,",
					" DISTRICT.ORG_MODE AS ORG_MODE,  DISTRICT.ORG_NODEID,", " DISTRICT.ORG_NODE_LEVEL,  DISTRICT.STRC_ELEMENT,",
					" DISTRICT.SPECIAL_CODES,  DISTRICT.PARENT_ORG_NODEID,", " DISTRICT.ORG_NODE_CODE_PATH,  DISTRICT.EMAILS,", " DISTRICT.CUSTOMERID,",
					" TO_CHAR(SYSDATE, 'DDMM') AS DATE_STR,", " TO_CHAR(SYSDATE, 'DDMMYYHH24MISS') AS DATE_STR_WT_YEAR", " FROM  ORG_NODE_DIM DISTRICT",
					" WHERE DISTRICT.ORG_NODE_LEVEL = 2", " AND DISTRICT.ORG_NODEID = ?");
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, jasperOrgId);
			String pQuery = CustomStringUtil.setQueryString(query, 1, jasperOrgId);
			pQuery = new BasicFormatterImpl().format(pQuery);
			logger.debug(CustomStringUtil.getQueryString(pQuery));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				district = new OrgTO();
				district.setElementName(rs.getString("DISTRICT_NAME"));
				district.setOrgNode(rs.getString("ORG_NODEID"));
				district.setOrgNodeCode(rs.getString("DISTRICT_CODE"));
				district.setJasperOrgId(rs.getString("ORG_NODEID"));
				district.setEmail(rs.getString("EMAILS"));
				district.setCustomerCode(rs.getString("CUSTOMERID"));
				district.setDateStr(rs.getString("DATE_STR"));
				district.setDateStrWtYear(rs.getString("DATE_STR_WT_YEAR"));
				district.setOrgNodeLevel(rs.getString("ORG_NODE_LEVEL"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		if (district != null) {
			List<UserTO> users = getUsers(district.getJasperOrgId(), prop);
			district.setUsers(users);
		}
		return district;
	}
}