package com.ctb.prism.login.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.login.dao.ILoginDAO;
import com.ctb.prism.login.transferobject.UserTO;



@Component
public class LoginBusinessImpl implements ILoginBusiness{

	private static final IAppLogger logger = LogFactory.getLoggerInstance(LoginBusinessImpl.class.getName());
	
	@Autowired
	private ILoginDAO loginDAO;
	
	public boolean selectTest(){
		return loginDAO.selectTest();
	}
	
	public UserTO getUserByEmail(String userEmail) throws SystemException{
		return loginDAO.getUserByEmail(userEmail);
	}
	public UserTO getUserByEmail(String userEmail, String contractName) throws SystemException{
		return loginDAO.getUserByEmail(userEmail, contractName);
	}
	
	public List<GrantedAuthority> getGrantedAuthorities(String username) {
		return loginDAO.getGrantedAuthorities(username);
	}
	
	public String getTenant(String username) {
		return loginDAO.getTenantId(username);
	}

	public Map<String,Object> getSystemConfigurationMessage(Map<String,Object> paramMap) {
		Map<String, Object> messageMap = new HashMap<String, Object>();
		String systemMessage = "";
		if(IApplicationConstants.PURPOSE_LANDING_PAGE.equals((String)paramMap.get("purpose"))){
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.COMMON_LOG_IN);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("commonLoginMessage", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.COMMON_HEADER);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("commonHeaderMessage", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.COMMON_FOOTER);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("commonFooterMessage", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.LANDING_PAGE_CONTENT);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("landingPageContent", systemMessage);
			
		} else if(IApplicationConstants.PURPOSE_TEACHER_LOGIN_PAGE.equals((String)paramMap.get("purpose"))){
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.COMMON_HEADER);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("commonHeaderMessage", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.TEACHER_FOOTER);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("teacherFooterMessage", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.TEACHER_LOGIN_PAGE_CONTENT);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("teacherPageContent", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.TEACHER_LOGIN_OUTAGE_CONTENT);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("teacherOutageContent", systemMessage);
			
		} else if(IApplicationConstants.PURPOSE_PARENT_LOGIN_PAGE.equals((String)paramMap.get("purpose"))){
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.COMMON_HEADER);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("commonHeaderMessage", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.PARENT_FOOTER);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("parentFooterMessage", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.PARENT_LOGIN_PAGE_CONTENT);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("parentPageContent", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.PARENT_LOGIN_OUTAGE_CONTENT);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("parentOutageContent", systemMessage);
			
		}else{
			
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("systemMessage", systemMessage);
			
		}
		return messageMap;
	}
	

	public boolean checkFirstTimeLogin(String username) {
		if(IApplicationConstants.FLAG_Y.equals( loginDAO.checkFirstTimeLogin(username) )) {
			return true;
		} else {
			return false;
		}
	}

	public UserTO getUserDetails(String username) {
		return loginDAO.getUserDetails(username);
	}
	
	public UserTO getUsersForSSO(UserTO userTO) throws Exception {
		return loginDAO.getUsersForSSO(userTO);
	}
	
	public UserTO getOrgLevel(UserTO userTO) {
		return loginDAO.getOrgLevel(userTO);
	}
	
	public boolean checkUserAvailability(String username) {
		return loginDAO.checkUserAvailability(username);
	}
	
	public String getUserOrgNode(String username) {
		return loginDAO.getUserOrgNode(username);
	}
	
	public void updateUserOrg(String username, String OrgNodeId, String oldOrgNodeid) {
		loginDAO.updateUserOrg(username, OrgNodeId, oldOrgNodeid);
	}
	
	public void addNewUser(Map<String,Object> paramMap) throws Exception {
		loginDAO.addNewUser(paramMap);
	}
	
	public String getRootPath(String customerId, String testAdmin) {
		return loginDAO.getRootPath(customerId, testAdmin);
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.login.business.ILoginBusiness#getMenuMap(java.util.Map)
	 */
	public Map<String, String> getMenuMap(Map<String, Object> paramMap) {
		return loginDAO.getMenuMap(paramMap);
	}
}
