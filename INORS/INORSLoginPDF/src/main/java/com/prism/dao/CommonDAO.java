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
	public OrgTO getSchoolDetails(String schoolId) throws Exception;

	/**
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
	 * @param userList
	 * @return
	 */
	public int[] updateNewuserFlag(final List<UserTO> userList);

	/**
	 * @param customerId
	 * @return
	 */
	public String getSupportEmailForCustomer(String customerId);

	/**
	 * @return
	 */
	public Map<String, String> getOrgLabelMap();

	/**
	 * @param structureElement
	 * @return
	 */
	public long getProcessIdNoCondition(String structureElement);

	/**
	 * @return
	 */
	public String getCurrentAdminYear();

	/**
	 * @param schoolId
	 * @return
	 */
	public boolean newStudentsLoaded(String schoolId);

	public int updateProcessStatus(long processId, String cOMPLETE_STATUS);

	public int updateMailStatus(long processId, String sUCCESS_STATUS, String iNPROGRESS_STATUS);

}
