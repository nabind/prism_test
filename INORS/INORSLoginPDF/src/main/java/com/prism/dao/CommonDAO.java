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
	public int[] updateNewuserFlag(final List<UserTO> userList);

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

}
