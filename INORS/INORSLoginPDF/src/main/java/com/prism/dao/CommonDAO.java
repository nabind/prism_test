package com.prism.dao;

import java.util.List;
import java.util.Map;

import com.prism.to.OrgTO;
import com.prism.to.UserTO;

public interface CommonDAO {
	
		
	/**
	 * @param schoolId
	 * @return
	 * @throws Exception
	 */
	public OrgTO getSchoolDetails(String schoolId, boolean cascade) throws Exception;

	/**
	 * Creates the organization Map where the organization id is the key and the organization name is the value.
	 * 
	 * @param users
	 * @return
	 */
	public Map<Integer, String> getOrgMap(List<UserTO> users);

	/**
	 * @param orgNodeId
	 * @return
	 * @throws Exception
	 */
	public String getOrgNodeCodePath(String orgNodeId) throws Exception;

	/**
	 * Update new user flag for all users in the userList.
	 * 
	 * @param userList
	 * @return
	 */
	public int[] updateNewUserFlag(final List<UserTO> userList);

	/**
	 * Fetch Education Center information.
	 * 
	 * @param customerId
	 * @return
	 */
	public String getSupportEmailForCustomer(String customerId);

	/**
	 * @return
	 */
	public Map<String, String> getOrgLabelMap();

	/**
	 * @param schoolId
	 * @return
	 */
	public List<String> getIcLetterPathList(String schoolId);

	/**
	 * Get current admin year.
	 * 
	 * @return
	 */
	public String getCurrentAdminYear();

	/**
	 * @param schoolId
	 * @return
	 */
	public List<String> getStudentIdList(String schoolId);

	/**
	 * @param pdfPathList
	 * @return
	 */
	public int[] updateStudentsPDFloc(Map<String, String> pdfPathList);

	/**
	 * @param schoolId
	 * @return
	 */
	public List<String> getStudentIdListFromExtractTable(String schoolId);

	/**
	 * @return
	 */
	public String getSubjectPrefix(String schoolId);
	
	public OrgTO getSchoolDetailsAcsi(String jasperOrgId, boolean state) throws Exception;
	public List<UserTO> getSchoolUsersAcsi(String jasperOrgId) throws Exception;
	public List<UserTO> fetchAdminColumnAcsi(List<UserTO> users) throws Exception;
	public List<UserTO> checkAdminAcsi(String allRoleIds) throws Exception;
	public String getCurrentAdminYearAcsi() throws Exception;
	public List<OrgTO> getAllTeachersAcsi(String jasperOrgId) throws Exception;
	public List<OrgTO> getGradeForTeacherUsersAcsi(List<OrgTO> teachers) throws Exception;
	public List<OrgTO> getGradesAcsi(String jasperOrgIds) throws Exception;
	public List<UserTO> getStudentsAcsi(String nodeId) throws Exception;

}
