package com.prism.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.prism.jdcc.JDCConnectionDriver;
import com.prism.to.OrgTO;
import com.prism.to.StudentTO;
import com.prism.to.UserTO;
import com.prism.util.CustomStringUtil;

public class MapDao extends CommonDao {

	private static final Logger logger = Logger.getLogger(MapDao.class);
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
	public MapDao(Properties prop) throws Exception {
		String dbURL = prop.getProperty("dbURL");
		String dbUserName = prop.getProperty("dbUserName");
		String dbPassword = prop.getProperty("dbPassword");
		driver = new JDCConnectionDriver("oracle.jdbc.driver.OracleDriver", dbURL, dbUserName, dbPassword);

	}

	/**
	 * Fetch all students for a school
	 * 
	 * @param schoolOrgNodeId
	 * @return
	 * @throws Exception
	 */
	public List<StudentTO> getStudents(String schoolOrgNodeId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StudentTO student = null;
		List<StudentTO> students = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = CustomStringUtil.appendString("select student_bio_id, gradeid, subtestid  from subtest_score_fact ",
					" where org_nodeid = ? order by gradeid");
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, schoolOrgNodeId);
			rs = pstmt.executeQuery();
			students = new LinkedList<StudentTO>();
			while (rs.next()) {
				student = new StudentTO();
				student.setStudentBioId(rs.getString(1));
				student.setGradeId(rs.getString(2));
				student.setSubtest(rs.getString(3));
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
			String query = CustomStringUtil.appendString("SELECT SUPPORT_EMAIL, SEND_LOGIN_PDF FROM CUSTOMER_INFO WHERE CUSTOMERID = ?");
			pstmt = conn.prepareCall(query);
			pstmt.setLong(1, Long.valueOf(customerId));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				orgTO.setEmail(rs.getString(1));
				orgTO.setSendLoginPdf(rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return orgTO;
	}


	public OrgTO getParentOrgNodeId(String orgNodeId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String parentOrgNodeId = null;
		OrgTO orgTO = new OrgTO();
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = "SELECT PARENT_ORG_NODEID, customerid FROM ORG_NODE_DIM WHERE ORG_NODEID = ?";
			pstmt = conn.prepareCall(query);
			pstmt.setLong(1, Long.valueOf(orgNodeId));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				orgTO.setParentJasperOrgId(rs.getString(1));
				orgTO.setCustomerCode(rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return orgTO;
	}
	
	public String getDummyUser() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String userid = "";
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = "SELECT userid FROM users where username = 'dummyssouser'";
			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				userid = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return userid;
	}
	
	
	/**
	 * Fetch current school of a students based on subtestId and custProdId and 
	 * 
	 * @param bioId
	 * @param subtestId
	 * @param custProdId
	 * @return
	 * @throws Exception
	 */
	public String getStudentCurrentSchool(String bioId,String subtestId, String custProdId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StudentTO student = null;
		String studentSchool = null;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = CustomStringUtil.appendString(" select org_nodeid from subtest_score_fact where student_bio_id = ? ",
					" and subtestid = ?  and  cust_prod_id = ?");
			pstmt = conn.prepareCall(query);
			pstmt.setLong(1, Long.valueOf(bioId));
			pstmt.setLong(2, Long.valueOf(subtestId));
			pstmt.setLong(3, Long.valueOf(custProdId));
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
			  studentSchool = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			releaseResources(conn, pstmt, rs);
		}
		return studentSchool;
	}

}