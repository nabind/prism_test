package com.vaannila.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vaannila.TO.AdminTO;
import com.vaannila.TO.OrgTO;
import com.vaannila.util.JDCConnectionDriver;


public class CommonDAOImpl {
	static JDCConnectionDriver driver = null;
	static String DATA_SOURCE = "jdbc:jdc:acsi";
	
	/**
	 * Get all admin year list
	 * @throws Exception
	 */
	public List<AdminTO> getAllAdminYear() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AdminTO admin = null;
		List<AdminTO> adminList = new ArrayList<AdminTO>();
		String query = "select adminid, admin_name, current_admin from admin_dim order by admin_seq desc";
		
		try {
			driver = LoadConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);

			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				admin = new AdminTO();
				admin.setAdminId(rs.getString(1));
				admin.setAdminName(rs.getString(2));
				admin.setCurrentAdmin(rs.getString(3));
				adminList.add(admin);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		return adminList;
	}
	
	/**
	 * This method is used to get school details
	 * @param structureElement
	 * @return
	 * @throws Exception
	 */
	public OrgTO getSchoolDetails(String structureElement, String adminid) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrgTO school = null;
		try {
			driver = LoadConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			
			String query = "SELECT ORG.LEVEL3_ELEMENT_NAME, " +
						       "ORG.ORG_NODEID, " +
						       "ORG.LEVEL3_JASPER_ORGID, " +
						       "STR.EMAIL, " +
						       "STR.STRUCTURE_ELEMENT, " +
						       "ORG.LEVEL3_CUSTOMER_CODE, " +
						       "(SELECT COUNT(1) FROM users usr WHERE usr.org_id IN(org.level4_jasper_orgid, STR.JASPER_ORGID)) user_count, " +
						       "(SELECT COUNT(org.level4_jasper_orgid) FROM ORG_NODE_DIM org WHERE Org.level3_jasper_orgid = STR.JASPER_ORGID) class_count, " +
						       "ORG.LEVEL2_ELEMENT_NAME " +
						   "FROM ORG_NODE_DIM ORG, ORG_STRUCTURE_ELEMENT STR, users usr " +
						 "WHERE STR.JASPER_ORGID = ORG.LEVEL3_JASPER_ORGID " +
						   "AND ROWNUM = 1 " +
						   "AND STR.STRUCTURE_ELEMENT = ? and str.adminid = ? ";
			
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, structureElement);
			pstmt.setString(2, adminid);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				school = new OrgTO();
				school.setElementName(rs.getString(1));
				school.setOrgNode(rs.getString(2));
				school.setJasperOrgId(rs.getString(3));
				school.setEmail(rs.getString(4));
				school.setStructureElement(rs.getString(5));
				school.setCustomerCode(rs.getString(6));
				school.setUserCount(rs.getLong(7));
				school.setClassCount(rs.getLong(8));
				school.setRegionName(rs.getString(9));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		if(school != null && school.getJasperOrgId() != null) {
			school.setStudCount(getStudentCount(school.getJasperOrgId()));
		}
		return school;
	}
	
	/**
	 * This method is to fetch student count
	 * @param jasperOrgId
	 * @return
	 * @throws Exception
	 */
	public long getStudentCount(String jasperOrgId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long studCount = 0;
		try {
			conn = driver.connect(DATA_SOURCE, null);

			String query = "SELECT sum(COUNT(STU.STUDENT_BIO_ID)) " +
						  "FROM ORG_NODE_DIM ORG, STUDENT_BIO_DIM STU " +  
						 "WHERE ORG.LEVEL3_JASPER_ORGID = ? " +
						   "AND STU.ORG_NODEID = ORG.ORG_NODEID   " +
						 "GROUP BY ORG.ORG_NODEID "; 
			
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, jasperOrgId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				studCount = rs.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		return studCount;
	}
	
	/**
	 * This method is to update email id of a school
	 * @param email
	 * @param structureElement
	 * @return
	 * @throws Exception
	 */
	public long updateEmail(String email, String structureElement, String adminid) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		long updateCount = 0;
		try {
			conn = driver.connect(DATA_SOURCE, null);

			String query = "update org_structure_element set email = ? ,updated_date_time = SYSDATE " +
					" where structure_element = ? and adminid = ? "; 
			
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, email);
			pstmt.setString(2, structureElement);
			pstmt.setString(3, adminid);
			updateCount = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		return updateCount;
	}
	
}
