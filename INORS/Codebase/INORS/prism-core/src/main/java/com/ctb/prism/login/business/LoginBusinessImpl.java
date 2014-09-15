package com.ctb.prism.login.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.login.dao.ILoginDAO;
import com.ctb.prism.login.transferobject.MenuTO;
import com.ctb.prism.login.transferobject.UserTO;



@Component
public class LoginBusinessImpl implements ILoginBusiness{

	private static final IAppLogger logger = LogFactory.getLoggerInstance(LoginBusinessImpl.class.getName());
	
	@Autowired
	private ILoginDAO loginDAO;
	
	public boolean selectTest(){
		return loginDAO.selectTest();
	}
	
/*	public UserTO getUserByEmail(String userEmail) throws SystemException{
		return loginDAO.getUserByEmail(userEmail);
	}*/
	public UserTO getUserByEmail(Map<String, Object> paramMap) throws SystemException{
		return loginDAO.getUserByEmail(paramMap);
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
		if(IApplicationConstants.PURPOSE_LANDING_PAGE
				.equals((String)paramMap.get(IApplicationConstants.PURPOSE_PRISM))){
			
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
			
		} else if(IApplicationConstants.PURPOSE_TEACHER_LOGIN_PAGE
				.equals((String)paramMap.get(IApplicationConstants.PURPOSE_PRISM))){
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.COMMON_HEADER);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("commonHeaderMessage", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.TEACHER_LOGIN_FOOTER);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("teacherFooterMessage", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.TEACHER_LOGIN_PAGE_CONTENT);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("teacherPageContent", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.TEACHER_LOGIN_OUTAGE_CONTENT);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("teacherOutageContent", systemMessage);
			
		} else if(IApplicationConstants.PURPOSE_PARENT_LOGIN_PAGE
				.equals((String)paramMap.get(IApplicationConstants.PURPOSE_PRISM))){
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.COMMON_HEADER);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("commonHeaderMessage", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.PARENT_LOGIN_FOOTER);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("parentFooterMessage", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.PARENT_LOGIN_PAGE_CONTENT);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("parentPageContent", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.PARENT_LOGIN_OUTAGE_CONTENT);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("parentOutageContent", systemMessage);
			
		}else if(IApplicationConstants.PURPOSE_TEACHER_HOME_PAGE
				.equals((String)paramMap.get(IApplicationConstants.PURPOSE_PRISM))){
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.COMMON_HEADER);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("commonHeaderMessage", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.TEACHER_HOME_FOOTER);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("teacherFooterMessage", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.TEACHER_HOME_PAGE);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("systemMessage", systemMessage);
			
			paramMap.put("REPORT_NAME", IApplicationConstants.PRODUCT_SPECIFIC_REPORT_NAME);
			paramMap.put("MESSAGE_TYPE", IApplicationConstants.PRODUCT_SPECIFIC_MESSAGE_TYPE);
			paramMap.put("MESSAGE_NAME", IApplicationConstants.MENU_MESSAGE);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("menuMessage", systemMessage);
			
		}else if(IApplicationConstants.PURPOSE_PARENT_HOME_PAGE
				.equals((String)paramMap.get(IApplicationConstants.PURPOSE_PRISM))){
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.COMMON_HEADER);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("commonHeaderMessage", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.PARENT_HOME_FOOTER);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("parentFooterMessage", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.PARENT_HOME_PAGE);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("systemMessage", systemMessage);
			
			paramMap.put("REPORT_NAME", IApplicationConstants.PRODUCT_SPECIFIC_REPORT_NAME);
			paramMap.put("MESSAGE_TYPE", IApplicationConstants.PRODUCT_SPECIFIC_MESSAGE_TYPE);
			paramMap.put("MESSAGE_NAME", IApplicationConstants.MENU_MESSAGE);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("menuMessage", systemMessage);
			
		}else if(IApplicationConstants.PURPOSE_GROWTH_HOME_PAGE
				.equals((String)paramMap.get(IApplicationConstants.PURPOSE_PRISM))){
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.COMMON_HEADER);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("commonHeaderMessage", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.TEACHER_HOME_FOOTER);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("parentFooterMessage", systemMessage);
			
			paramMap.put("MESSAGE_NAME", IApplicationConstants.GROWTH_HOME_PAGE);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("systemMessage", systemMessage);
			
			paramMap.put("REPORT_NAME", IApplicationConstants.PRODUCT_SPECIFIC_REPORT_NAME);
			paramMap.put("MESSAGE_TYPE", IApplicationConstants.PRODUCT_SPECIFIC_MESSAGE_TYPE);
			paramMap.put("MESSAGE_NAME", IApplicationConstants.MENU_MESSAGE);
			systemMessage = loginDAO.getSystemConfigurationMessage(paramMap);
			messageMap.put("menuMessage", systemMessage);
			
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

/*	public UserTO getUserDetails(String username) {
		return loginDAO.getUserDetails(username);
	}*/
	
	public UserTO getUserDetails(Map<String, Object> paramMap) {
		return loginDAO.getUserDetails(paramMap);
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
	
	public String getUserOrgNode(String username, String contractName) {
		return loginDAO.getUserOrgNode(username,contractName);
	}
	
	public void updateUserOrg(String username, String OrgNodeId, String oldOrgNodeid, String contractName) {
		loginDAO.updateUserOrg(username, OrgNodeId, oldOrgNodeid, contractName);
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
	public Set<MenuTO> getMenuMap(Map<String, Object> paramMap) {
		return loginDAO.getMenuMap(paramMap);
	}
	
	public Map<String,String> getActionMap(Map<String, Object> paramMap) {
		return loginDAO.getActionMap(paramMap);
	}
	
	public Map<String, Object>  getContractProerty (Map<String, Object> paramMap) {
		return loginDAO.getContractProerty(paramMap);
	}
	
}
