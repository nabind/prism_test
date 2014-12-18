package com.ctb.prism.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.theme.CookieThemeResolver;

import com.ctb.prism.admin.service.IAdminService;
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.constant.IEmailConstants;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.EmailSender;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.Service.ILoginService;
import com.ctb.prism.login.security.provider.AuthenticatedUser;
import com.ctb.prism.login.transferobject.MenuTO;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.service.IParentService;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.QuestionTO;
import com.ctb.prism.report.transferobject.ReportTO;
import com.ctb.prism.web.util.JsonUtil;
import com.google.gson.Gson;

@Controller
public class LoginController {

	private static IAppLogger logger = LogFactory.getLoggerInstance(LoginController.class.getName());

	private List<String> prismUserHomePage;
	private List<ReportTO> homePageByRoleEntries;

	@Autowired private IPropertyLookup propertyLookup;

	@Autowired private ILoginService loginService;

	@Autowired private IParentService parentService;

	@Autowired private IAdminService adminService;
	
	@Autowired private CookieThemeResolver themeResolver;

	@RequestMapping(value = "/error", method = RequestMethod.POST)
	public ModelAndView error(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ModelAndView modelAndView = new ModelAndView("error/error");
		logger.log(IAppLogger.INFO, "Exit: loadErrorPage()");
		return modelAndView;
	}

	/**
	 * This method is return display name for given username
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getDisplayName", method = RequestMethod.GET)
	public @ResponseBody
	String getDisplayName(HttpServletRequest req, HttpServletResponse res) throws IOException {
		logger.log(IAppLogger.INFO, "Enter: ReportController - checkpagination");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		UserTO user = null;
		try {
			paramMap.put("username", req.getParameter("loginAsUsername"));
			user = loginService.getUserDetails(paramMap);

			res.setContentType("plain/text");
			res.getWriter().write(user.getDisplayName());
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: ReportController - checkpagination");
		}
		return null;
	}

	/**
	 * Changed by Joy for getSystemConfigurationMessage() architecture change
	 * This method is to display landing page
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/landing", method = RequestMethod.GET)
	public ModelAndView loadLandingPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contractName = Utils.getContractNameNoLogin(themeResolver.resolveThemeName(request));
		logger = LogFactory.getLoggerInstance(LoginController.class.getName(),contractName);
		
		logger.log(IAppLogger.INFO, "Enter: loadLandingPage()");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("REPORT_NAME", IApplicationConstants.GENERIC_REPORT_NAME);
		paramMap.put("MESSAGE_TYPE", IApplicationConstants.GENERIC_MESSAGE_TYPE);
		paramMap.put(IApplicationConstants.PURPOSE_PRISM, IApplicationConstants.PURPOSE_LANDING_PAGE);
		
		//As the landing page is applicable only for inors
		contractName = IApplicationConstants.CONTRACT_NAME_INORS;
		
		paramMap.put("contractName", contractName);
		paramMap.put("custProdId", 0L);
		Map<String, Object> messageMap = loginService.getSystemConfigurationMessage(paramMap);
		
		//to optimally use cache need to create new paramMap
		Map<String, Object> tileParamMap = new HashMap<String, Object>();
		tileParamMap.put("contractName", contractName);
		
		Map<String, Object> propertyMap = loginService.getContractProerty(tileParamMap);
		String contractTitle = propertyMap.get(IApplicationConstants.CONTRACT_TITLE)!= null ? (String)propertyMap.get(IApplicationConstants.CONTRACT_TITLE): "Prism";
		
		request.getSession().setAttribute("propertyMap", propertyMap);
		
		ModelAndView modelAndView = new ModelAndView("common/landing");
		modelAndView.addObject("messageMap", messageMap);
		modelAndView.addObject("contractTitle", contractTitle);
		logger.log(IAppLogger.INFO, "Exit: loadLandingPage()");
		return modelAndView;
	}

	/**
	 * Changed by Joy for getSystemConfigurationMessage() architecture change
	 * This method is to display login page
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView loadLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contractName = Utils.getContractNameNoLogin(themeResolver.resolveThemeName(request));
		logger = LogFactory.getLoggerInstance(LoginController.class.getName(),contractName);
		
		logger.log(IAppLogger.INFO, "Enter: loadLoginPage()");
		String parent = request.getParameter(IApplicationConstants.PARENT_LOGIN);
		String mess_login_error = (String) request.getParameter("login_error");
		String message = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("REPORT_NAME", IApplicationConstants.GENERIC_REPORT_NAME);
		paramMap.put("MESSAGE_TYPE", IApplicationConstants.GENERIC_MESSAGE_TYPE);
		paramMap.put("MESSAGE_NAME", IApplicationConstants.COMMON_LOG_IN);
		paramMap.put("contractName", contractName);
		paramMap.put("custProdId", 0L);
		Map<String, Object> messageMap = loginService.getSystemConfigurationMessage(paramMap);
		String logInInfoMessage = (String)messageMap.get("systemMessage");
		if ("1".equalsIgnoreCase(mess_login_error)) {
			logger.log(IAppLogger.ERROR, "Invalid Login");
			message = "error.login.invalidlogin";
		} else if ("2".equalsIgnoreCase(mess_login_error)) {
			logger.log(IAppLogger.ERROR, "Session Expired");
			message = "error.login.sessionexpired";
		} else {
			// this is proper login
		}
		ModelAndView modelAndView = null;
		if (IApplicationConstants.TRUE.equals(parent)) {
			modelAndView = new ModelAndView("parent/login");
			modelAndView.addObject("logInInfoMessage", logInInfoMessage);

		} else {
			modelAndView = new ModelAndView("user/login");
			modelAndView.addObject("logInInfoMessage", logInInfoMessage);
		}
		modelAndView.addObject("message", message);
		logger.log(IAppLogger.INFO, "Exit: loadLoginPage()");
		return modelAndView;
	}

	/**
	 * This method is to display login page
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/userlogin", method = RequestMethod.GET)
	public ModelAndView userlogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String contractName = Utils.getContractNameNoLogin(themeResolver.resolveThemeName(request));
		logger = LogFactory.getLoggerInstance(LoginController.class.getName(),contractName);
		
		logger.log(IAppLogger.INFO, "Enter: userlogin()");
		logger.log(IAppLogger.INFO, "theme -------------> "+themeResolver.resolveThemeName(request));
		String mess_login_error = (String) request.getParameter("login_error");
		String message = null;
		Map<String, Object> messageMap = new HashMap<String, Object>();		
		Map<String, Object> propertyMap = new HashMap<String, Object>();		
		Map<String,Object> paramMap = new HashMap<String, Object>();
		
		paramMap.put("contractName", contractName);
		propertyMap = loginService.getContractProerty(paramMap);
		request.getSession().setAttribute("propertyMap", propertyMap);
	
		String contractTitle = propertyMap.get(IApplicationConstants.CONTRACT_TITLE)!= null ? (String)propertyMap.get(IApplicationConstants.CONTRACT_TITLE): "Prism";

		if ("1".equalsIgnoreCase(mess_login_error)) {
			logger.log(IAppLogger.ERROR, "Invalid Login");
			message = "error.login.invalidlogin";
			
			paramMap.put("theme", themeResolver.resolveThemeName(request));
			paramMap.put("action","login");
			paramMap.put("custProdId", 0L);
			messageMap = getMessageMap(paramMap);
		} else if ("2".equalsIgnoreCase(mess_login_error)) {
			logger.log(IAppLogger.ERROR, "Session Expired");
			message = "error.login.sessionexpired";
		} else {
			// this is proper login
			paramMap.put("theme", themeResolver.resolveThemeName(request));
			paramMap.put("action","login");
			paramMap.put("custProdId", 0L);
			messageMap = getMessageMap(paramMap);
		}
				
		ModelAndView modelAndView = new ModelAndView("user/userlogin");
		modelAndView.addObject("contractTitle", contractTitle);
		modelAndView.addObject("message", message);
		modelAndView.addObject("messageMap", messageMap);
		logger.log(IAppLogger.INFO, "Exit: userlogin()");
		return modelAndView;
	}
	
	private Map<String, Object> getMessageMap(Map<String,Object> paramMap){
		paramMap.put("REPORT_NAME", IApplicationConstants.GENERIC_REPORT_NAME);
		paramMap.put("MESSAGE_TYPE", IApplicationConstants.GENERIC_MESSAGE_TYPE);
		
		if(paramMap.get("theme") != null){
			String theme = (String)paramMap.get("theme");
			if("login".equals(paramMap.get("action"))){
				paramMap.put("contractName", Utils.getContractNameNoLogin(theme));
				if (theme.indexOf(IApplicationConstants.PARENT_LOGIN) != -1) {
					paramMap.put(IApplicationConstants.PURPOSE_PRISM, IApplicationConstants.PURPOSE_PARENT_LOGIN_PAGE);
				}else{
					paramMap.put(IApplicationConstants.PURPOSE_PRISM, IApplicationConstants.PURPOSE_TEACHER_LOGIN_PAGE);
				}
			}else if("home".equals(paramMap.get("action"))){
				if (theme.indexOf(IApplicationConstants.PARENT_LOGIN) != -1) {
					paramMap.put(IApplicationConstants.PURPOSE_PRISM, IApplicationConstants.PURPOSE_PARENT_HOME_PAGE);
				}else{
					paramMap.put(IApplicationConstants.PURPOSE_PRISM, IApplicationConstants.PURPOSE_TEACHER_HOME_PAGE);
				}
			}else if("homeGrowth".equals(paramMap.get("action"))){
				if (theme.indexOf(IApplicationConstants.PARENT_LOGIN) != -1) {
					paramMap.put(IApplicationConstants.PURPOSE_PRISM, IApplicationConstants.PURPOSE_GROWTH_HOME_PAGE);
				}else{
					paramMap.put(IApplicationConstants.PURPOSE_PRISM, IApplicationConstants.PURPOSE_GROWTH_HOME_PAGE);
				}
			}
		}
		
		Map<String, Object> messageMap = loginService.getSystemConfigurationMessage(paramMap);
		return messageMap;
	}
	
	/**
	 * Opens dashboards
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/dashboards", method = RequestMethod.GET)
	public ModelAndView dashboards(HttpServletRequest req, HttpServletResponse res) throws IOException {
		req.getSession().setAttribute(IApplicationConstants.PASSWORD_WARNING,"FALSE");
		return validateUser(req, res);
	}

	@RequestMapping(value = "/reports", method = RequestMethod.GET)
	public ModelAndView reports(HttpServletRequest req, HttpServletResponse res) throws IOException {
		return validateUser(req, res);
	}

	/**
	 * This method is invoked after successful authentication
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/validateUser", method = RequestMethod.GET)
	public ModelAndView validateUser(HttpServletRequest req, HttpServletResponse res) throws IOException {
		logger.log(IAppLogger.INFO, "Enter: validateUser()");
		ModelAndView modelAndView = null;
		String orgLvl = null;
		Map<String,Object> paramMap = new HashMap<String, Object>();
		StringBuilder roles =new StringBuilder();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				ReportTO homeReport = null;
				String username = null;
				Collection<GrantedAuthority> authorities = null;
				//String userDisplay = (String) req.getSession().getAttribute(IApplicationConstants.CURR_USER_DISPLAY);
				UserTO user = null;
				if (auth.getPrincipal() instanceof LdapUserDetailsImpl) {
					LdapUserDetailsImpl userDetails = (LdapUserDetailsImpl) auth.getPrincipal();
					req.getSession().setAttribute(IApplicationConstants.CURRUSER, userDetails.getUsername());
					req.getSession().setAttribute(IApplicationConstants.AUTHORITIES, userDetails.getAuthorities());
					paramMap.put("username", userDetails.getUsername());
					user = loginService.getUserDetails(paramMap);
					req.getSession().setAttribute(IApplicationConstants.CURRORG, user.getOrgId());
					req.getSession().setAttribute(IApplicationConstants.CURRORGLVL, user.getOrgNodeLevel());
					req.getSession().setAttribute(IApplicationConstants.DEFAULT_CUST_PROD_ID, user.getDefultCustProdId());
					authorities = userDetails.getAuthorities();
					username = userDetails.getUsername();
				} else if (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
					org.springframework.security.core.userdetails.User userDetails = (User) auth.getPrincipal();
					req.getSession().setAttribute(IApplicationConstants.CURRUSER, userDetails.getUsername());
					req.getSession().setAttribute(IApplicationConstants.AUTHORITIES, userDetails.getAuthorities());
					paramMap.put("username", userDetails.getUsername());
					user = loginService.getUserDetails(paramMap);
					req.getSession().setAttribute(IApplicationConstants.CURRORG, user.getOrgId());
					req.getSession().setAttribute(IApplicationConstants.CURRORGLVL, user.getOrgNodeLevel());
					req.getSession().setAttribute(IApplicationConstants.DEFAULT_CUST_PROD_ID, user.getDefultCustProdId());
					authorities = userDetails.getAuthorities();
					username = userDetails.getUsername();
				} else {
					// this section is only for DAO authentication - used during development
					AuthenticatedUser authenticatedUser = (AuthenticatedUser) auth.getPrincipal();

					user = authenticatedUser.getUserTO();
					// if (user.getUserName() != null && user.getUserName().indexOf("//") != -1){
					// String[] userParameter=user.getUserName().split("//");
					// req.getSession().setAttribute(IApplicationConstants.CURRUSER,userParameter[0]);
					// } else {
					req.getSession().setAttribute(IApplicationConstants.CURRUSER, user.getUserName());
					// }
					req.getSession().setAttribute(IApplicationConstants.AUTHORITIES, authenticatedUser.getAuthorities());
					String reloadUserRequestScope = (String) req.getAttribute(IApplicationConstants.RELOAD_USER);
					String reloadUserSessionScope = (String) req.getSession().getAttribute(IApplicationConstants.RELOAD_USER);
					if ((IApplicationConstants.TRUE.equals(reloadUserRequestScope)) || (IApplicationConstants.TRUE.equals(reloadUserSessionScope))) {
						paramMap.put("username", user.getUserName());
						// system reloading user after first-time change password
						user = loginService.getUserDetails(paramMap);
					}
					req.getSession().setAttribute(IApplicationConstants.CURRORG, user.getOrgId());
					req.getSession().setAttribute(IApplicationConstants.CURRORGLVL, user.getOrgNodeLevel());
					req.getSession().setAttribute(IApplicationConstants.DEFAULT_CUST_PROD_ID, user.getDefultCustProdId());

					authorities = authenticatedUser.getAuthorities();

					// add admin role to user if it an SSO user and admin flag
					if (req.getSession().getAttribute(IApplicationConstants.SSO_ADMIN) != null) {

						Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
						List<GrantedAuthority> extraAuths = new ArrayList<GrantedAuthority>(auth.getAuthorities());
						extraAuths.addAll(authorities);
						if((Boolean) req.getSession().getAttribute(IApplicationConstants.SSO_ADMIN)) {
							extraAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
						}
						extraAuths.add(new SimpleGrantedAuthority("ROLE_SSO"));
						Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), extraAuths);
						SecurityContextHolder.getContext().setAuthentication(newAuth);
					}

					String ssoOrg = (String) req.getSession().getAttribute(IApplicationConstants.SSO_ORG);
					if (ssoOrg != null && ssoOrg.length() > 0 && req.getSession().getAttribute(IApplicationConstants.PREV_ADMIN)== null) { //Changed by Abir to solve login as after sso
						user.setOrgId(ssoOrg);
						req.getSession().setAttribute(IApplicationConstants.CURRORG, user.getOrgId());
						req.getSession().setAttribute(IApplicationConstants.CURRORGLVL, req.getSession().getAttribute(IApplicationConstants.SSO_ORG_LEVEL));
					}
					username = user.getUserName();
				}
				
				/*
				 * Block parent to login in teacher login page
				 *  */
				if(themeResolver.resolveThemeName(req).indexOf(IApplicationConstants.PARENT_LOGIN) == -1 
						&& user.getUserType().equals(IApplicationConstants.PARENT_USER_FLAG)){
					// parent is login through teacher page
					res.sendRedirect("userlogin.do?login_error=1");
				} else if (themeResolver.resolveThemeName(req).indexOf(IApplicationConstants.PARENT_LOGIN) != -1 
						&& !user.getUserType().equals(IApplicationConstants.PARENT_USER_FLAG)){
					// teacher is login through parent page
					res.sendRedirect("userlogin.do?login_error=1");
				}
				
				req.getSession().setAttribute(IApplicationConstants.CURRUSERID, user.getUserId());
				req.getSession().setAttribute(IApplicationConstants.CURR_USER_DISPLAY, user.getDisplayName());
				req.getSession().setAttribute(IApplicationConstants.CUSTOMER, user.getCustomerId());
				req.getSession().setAttribute(IApplicationConstants.EMAIL, user.getUserEmail());
				req.getSession().setAttribute(IApplicationConstants.PRODUCT_NAME, user.getProduct());
				
				String switchUser = (String) req.getSession().getAttribute(IApplicationConstants.PREV_ADMIN);
				
				boolean isSwitchUser = false;
				// check if user logs-in first time
				if (switchUser != null && switchUser.trim().length() > 0) {
					isSwitchUser = true;
				} else if (IApplicationConstants.INACTIVE_FLAG.equals(user.getUserStatus())) {
					if (req.getSession().getAttribute(IApplicationConstants.PREV_ADMIN) == null) {
						// user is disabled - redirect to access denied page
						req.getSession().invalidate();
						return new ModelAndView("redirect:denied.jsp");
					} else {
						// user is disabled - but the Admin is using Login As feature
						logger.log(IAppLogger.INFO, "Login As feature is used Admin");
					}
				}
				
				if (!isSwitchUser && user.getIsPasswordExpired().equals("TRUE")) {
					// logger.log(IAppLogger.DEBUG, "calling  changePassword.do...............................");
					return new ModelAndView("redirect:changePassword.do?username=" + username);
				}
				if(req.getSession().getAttribute(IApplicationConstants.PASSWORD_WARNING) == null)
					req.getSession().setAttribute(IApplicationConstants.PASSWORD_WARNING, user.getIsPasswordWarning());
				
				paramMap.clear();
				if (IApplicationConstants.EDU_USER_FLAG.equals(user.getUserType())) {
					//orgLvl = String.valueOf(IApplicationConstants.DEFAULT_LEVELID_VALUE);
					paramMap.put("orgNodeLevel", IApplicationConstants.DEFAULT_LEVELID_VALUE);
				} else {
					paramMap.put("orgNodeLevel", user.getOrgNodeLevel());
				}
				paramMap.put("custProdId", user.getDefultCustProdId());
				
				List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
				authList = user.getRoles();
					
				for (int i = 0; i < authList.size(); i++) {
					roles.append(authList.get(i).getAuthority()).append(",");
				}
				roles.replace(roles.lastIndexOf(","), roles.lastIndexOf(",")+1, "");
				if(req.getSession().getAttribute(IApplicationConstants.SSO_ADMIN) != null && 
						(Boolean) req.getSession().getAttribute(IApplicationConstants.SSO_ADMIN)) {
					roles.append(",").append("ROLE_ADMIN");
				}
				logger.log(IAppLogger.INFO, "Roles = " + roles.toString());
				paramMap.put("roles", roles.toString());
				req.getSession().setAttribute(IApplicationConstants.CURR_USER_ROLES, roles.toString());
				
				Set<MenuTO> menuSet = loginService.getMenuMap(paramMap);
				menuSet = Utils.attachCSSClassToMenuSet(menuSet, propertyLookup);
				logger.log(IAppLogger.INFO, "menuSet : " + Utils.objectToJson(menuSet));
				req.getSession().setAttribute(IApplicationConstants.MENU_SET, menuSet);
				
				//Only two parameters are being sent for proper caching - By Joy
				//If no. of parameters are changed, please update updateAdminYear() of AdminController
				Map<String,Object> actionParamMap = new HashMap<String,Object>();
				//actionParamMap.put("userId", user.getUserId());
				actionParamMap.put("roles", roles.toString());
				if (IApplicationConstants.EDU_USER_FLAG.equals(user.getUserType())) {
					//orgLvl = String.valueOf(IApplicationConstants.DEFAULT_LEVELID_VALUE);
					actionParamMap.put("orgNodeLevel", IApplicationConstants.DEFAULT_LEVELID_VALUE);
				} else {
					actionParamMap.put("orgNodeLevel", user.getOrgNodeLevel());
				}
				actionParamMap.put("custProdId", String.valueOf(user.getDefultCustProdId()));
				
				try {
					//this Map is now need to add in Modelview
					Map<String, String> actionMap= loginService.getActionMap(actionParamMap);
					req.getSession().setAttribute(IApplicationConstants.ACTION_MAP_SESSION, actionMap);					
				} catch (Exception e) {
					logger.log(IAppLogger.ERROR, "actionMap : " + e.getMessage());
				}
				
				Map<String,Object> paramMapMessage = new HashMap<String, Object>();
				paramMapMessage.put("theme", themeResolver.resolveThemeName(req));
				paramMapMessage.put("action","home");
				paramMapMessage.put("custProdId", user.getDefultCustProdId());
				Map<String, Object> messageMap = getMessageMap(paramMapMessage);
				req.getSession().setAttribute(IApplicationConstants.MESSAGE_MAP_SESSION, messageMap);
				
				
				if (switchUser != null && switchUser.trim().length() > 0) {
					req.getSession().setAttribute(IApplicationConstants.CURR_USER_DISPLAY, user.getDisplayName());
				}

				// Due to introduction of Org Mode - By Joy
				// req.getSession().setAttribute(IApplicationConstants.ORG_MODE, propertyLookup.get("orgMode.val.public"));
				logger.log(IAppLogger.INFO, "ORG_MODE = " + user.getOrgMode());
				req.getSession().setAttribute(IApplicationConstants.ORG_MODE, user.getOrgMode());

				if (user.getUserType().equals(IApplicationConstants.PARENT_USER_FLAG)) {
					req.getSession().setAttribute("PARENT_LOGIN", IApplicationConstants.PARENT_LOGIN);
				} else {
					req.getSession().removeAttribute("PARENT_LOGIN");
				}

				
				String loginAs = (String) req.getSession().getAttribute(IApplicationConstants.LOGIN_AS);
				if (loginAs != null && IApplicationConstants.ACTIVE_FLAG.equals(loginAs)) {
					isSwitchUser = true;
				}
				if (!isSwitchUser && IApplicationConstants.FLAG_Y.equals(user.getFirstTimeLogin())) {
					req.getSession().setAttribute(IApplicationConstants.FIRST_TIME_LOGIN, IApplicationConstants.TRUE);
					// res.sendRedirect("changePassword.do");
					// logger.log(IAppLogger.DEBUG, "calling  changePassword.do...............................");
					return new ModelAndView("redirect:changePassword.do?username=" + username);
				} else {
					req.getSession().setAttribute(IApplicationConstants.FIRST_TIME_LOGIN, IApplicationConstants.FALSE);
					// ADDED FOR SSO (and then use LOGIN AS)
					String ssoPrevAdmin = (String) req.getSession().getAttribute(IApplicationConstants.SSO_PREV_ADMIN);
					if (ssoPrevAdmin != null && ssoPrevAdmin.length() > 0) {
						Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
						List<GrantedAuthority> extraAuths = new ArrayList<GrantedAuthority>(auth.getAuthorities());
						extraAuths.addAll(authorities);
						extraAuths.add(new SimpleGrantedAuthority("ROLE_SSO"));
						Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), extraAuths);
						SecurityContextHolder.getContext().setAuthentication(newAuth);
					}
					if (IApplicationConstants.EDU_USER_FLAG.equals(user.getUserType())) {
						orgLvl = String.valueOf(IApplicationConstants.DEFAULT_LEVELID_VALUE);
						logger.log(IAppLogger.INFO, "user.getRoles() = " + user.getRoles());
						List<GrantedAuthority> roleList = user.getRoles();
						for (GrantedAuthority grantedAuthority : roleList) {
							String s = grantedAuthority.getAuthority();
							if ("ROLE_ADMIN".equals(s)) {
								Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
								List<GrantedAuthority> extraAuths = new ArrayList<GrantedAuthority>(auth.getAuthorities());
								extraAuths.addAll(authorities);
								extraAuths.add(new SimpleGrantedAuthority("ROLE_EDU_ADMIN"));
								Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), extraAuths);
								SecurityContextHolder.getContext().setAuthentication(newAuth);
								break;
							}
						}
					}
					homeReport = getBestHomeURLForUser(authorities, orgLvl);
				}
				// open home page based on user role
				if (homeReport.isRegularUser() || ((null != req.getSession().getAttribute("PARENT_LOGIN")) 
						&& (IApplicationConstants.PARENT.equals(req.getSession().getAttribute("PARENT_LOGIN"))))) {
					if ((null != req.getSession().getAttribute("PARENT_LOGIN")) 
							&& (IApplicationConstants.PARENT.equals(req.getSession().getAttribute("PARENT_LOGIN")))) {
						modelAndView = new ModelAndView("parent/parentWelcome");
					} else {
						homeReport.setProductName("TerraNova 3 : ");
						modelAndView = new ModelAndView("user/welcome");
						modelAndView.addObject("homeReport", homeReport);
						themeResolver.setThemeName(req, res, user.getContractName());
					}
				} else if (homeReport.isAccessDenied()) {
					modelAndView = new ModelAndView("error/accessDenied");
				} else {
					// custom url - without report filter
					modelAndView = new ModelAndView("user/welcome");
					homeReport.setHideFilter("hide");
					homeReport.setReportApiUrl(homeReport.getOtherUrl());
					homeReport.setReportName("Home");
					modelAndView.addObject("homeReport", homeReport);
				}
				req.getSession().setAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS, user);
				
				Map<String, Object> propertyMap = new HashMap<String, Object>();
				
				propertyMap=(Map<String,Object>)req.getSession().getAttribute("propertyMap");
				if(propertyMap == null) {
					Map<String, Object> tileParamMap = new HashMap<String, Object>();
					tileParamMap.put("contractName", user.getContractName());
					propertyMap = loginService.getContractProerty(tileParamMap);
				}				
				String contractTitle = propertyMap.get(IApplicationConstants.CONTRACT_HOME_TITLE)!= null ? (String)propertyMap.get(IApplicationConstants.CONTRACT_HOME_TITLE): "Prism";
				req.getSession().setAttribute("contractTitle", contractTitle);			
			}
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, "validateUser(): " + exception.getMessage());
			return modelAndView.addObject("app_error", exception.getMessage());
		} finally {
			logger.log(IAppLogger.INFO, "Exit: validateUser()");
		}
		modelAndView.addObject("PDCT_NAME", propertyLookup.get("PDCT_NAME"));
		return modelAndView;
	}

	/**
	 * Opens home page after saving user profile for firsttime loggedin user
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public ModelAndView changePassword(HttpServletRequest req, HttpServletResponse res) throws IOException {

		String contractName = Utils.getContractNameNoLogin(themeResolver.resolveThemeName(req));
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", contractName);
		// fetch security questions
		List<QuestionTO> questionList = parentService.getSecretQuestions(paramMap);
				
		String username = (String) req.getParameter("username");
		ParentTO parentDetails = parentService.manageParentAccountDetails(username);
		List<QuestionTO> secretQuestionList = parentDetails.getQuestionToList();
		ModelAndView modelAndView = new ModelAndView("user/firstTimeUser");
		modelAndView.addObject("parentDetails", parentDetails);
		modelAndView.addObject("secretQuestionList", secretQuestionList);
		modelAndView.addObject("masterQuestionList", questionList);
		return modelAndView;
	}


	/**
	 * Opens home page after saving user profile for firsttime loggedin user
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/welcome", method = RequestMethod.POST)
	public ModelAndView welcome(HttpServletRequest req, HttpServletResponse res) throws IOException {

		String contractName = Utils.getContractNameNoLogin(themeResolver.resolveThemeName(req));
		logger = LogFactory.getLoggerInstance(LoginController.class.getName(),contractName);
		
		logger.log(IAppLogger.INFO, "Save user change Password screen");
		ParentTO parentTO = new ParentTO();
		QuestionTO questionTO = null;
		List<QuestionTO> questionToList = new ArrayList<QuestionTO>();
		// save user details
		String userName = (String) req.getSession().getAttribute(IApplicationConstants.CURRUSER);
		String password = (String) req.getParameter("password");
		String firstName = (String) req.getParameter("firstName");
		String lastName = (String) req.getParameter("lastName");
		String mailId = (String) req.getParameter("mail");
		String mobile = (String) req.getParameter("mobile");
		String street = (String) req.getParameter("street");
		String city = (String) req.getParameter("city");
		String state = (String) req.getParameter("state");
		String zip_code = (String) req.getParameter("zip_code");
		String country = (String) req.getParameter("userCountry");
		// checking whether the country has been selected form the drop down, if not then taking the default value of the dropdown
		if (country.trim().length() <= 0) {
			country = req.getParameter("country");
		}

		// String invitationCode = (String)req.getParameter("invitationCode");

		parentTO.setUserName(userName);
		parentTO.setPassword(password);
		parentTO.setFirstName(firstName);
		parentTO.setLastName(lastName);
		parentTO.setMail(mailId);
		parentTO.setMobile(mobile);
		parentTO.setStreet(street);
		parentTO.setCity(city);
		parentTO.setState(state);
		parentTO.setZipCode(zip_code);
		parentTO.setCountry(country);

		// parentTO.setInvitationCode(invitationCode);

		String qsn1 = (String) req.getParameter("qsn1");
		String ans1 = (String) req.getParameter("ans1");
		String ansId1 = (String) req.getParameter("ansId1");
		questionTO = new QuestionTO();
		questionTO.setQuestionId(Long.parseLong(qsn1));
		questionTO.setAnswer(ans1);
		questionTO.setAnswerId(Long.parseLong(ansId1));
		questionToList.add(questionTO);
		String qsn2 = (String) req.getParameter("qsn2");
		String ans2 = (String) req.getParameter("ans2");
		String ansId2 = (String) req.getParameter("ansId2");
		questionTO = new QuestionTO();
		questionTO.setQuestionId(Long.parseLong(qsn2));
		questionTO.setAnswer(ans2);
		questionTO.setAnswerId(Long.parseLong(ansId2));
		questionToList.add(questionTO);

		String qsn3 = (String) req.getParameter("qsn3");
		String ans3 = (String) req.getParameter("ans3");
		String ansId3 = (String) req.getParameter("ansId3");
		questionTO = new QuestionTO();
		questionTO.setQuestionId(Long.parseLong(qsn3));
		questionTO.setAnswer(ans3);
		questionTO.setAnswerId(Long.parseLong(ansId3));
		questionToList.add(questionTO);
		parentTO.setQuestionToList(questionToList);
		boolean success = false;
		try {
			success = parentService.firstTimeUserLogin(parentTO);
		} catch (Exception ex) {
			success = false;
		}

		if (success) {
			// setting this attribute so that application reloads user data during login
			req.setAttribute(IApplicationConstants.RELOAD_USER, IApplicationConstants.TRUE);
			req.getSession().setAttribute(IApplicationConstants.RELOAD_USER, IApplicationConstants.TRUE);
			return validateUser(req, res);
		} else {
			
			Map<String,Object> paramMap = new HashMap<String, Object>();
			paramMap.put("contractName", contractName);
			// fetch security questions
			List<QuestionTO> questionList = parentService.getSecretQuestions(paramMap);
			
			ModelAndView modelAndView = new ModelAndView("user/firstTimeUser");
			modelAndView.addObject("secretQuestionList", questionList);
			modelAndView.addObject("parentTO", parentTO);
			modelAndView.addObject("Error message", "System is experiencing some problem while saving. Please try later.");
			req.getSession().removeAttribute(IApplicationConstants.RELOAD_USER);
			return modelAndView;
		}
	}

	/**
	 * Given a user, find their home page based on their role
	 * 
	 * @param User
	 *            user
	 * @return Home page URL for user
	 */
	private ReportTO getBestHomeURLForUser(Collection<GrantedAuthority> u, String orgLvl) {
		List<ReportTO> roleEntries = getHomePageByRoleEntries();
		if (roleEntries != null) {
			Iterator<ReportTO> it = roleEntries.iterator();
			while (it.hasNext()) {
				ReportTO entry = it.next();
				if (orgLvl != null) {
					if (orgLvl.equals(entry.getOrgLevel()) && entry.getAllRoles().equals("ROLE_EDUCENTER_USER")) {
						entry.setAllRoles("ROLE_USER");
						if (hasRole(u, entry.getAllRoles(), orgLvl)) {
							return entry;
						}
					}
				} else {
					if (hasRole(u, entry.getAllRoles())) {
						return entry;
					}
				}

			}
		}
		return null;
	}

	/**
	 * Does this user have a role of this name?
	 * 
	 * @param User
	 *            u
	 * @param String
	 *            roleName
	 * @return true if user has a role with the given name
	 */
	private boolean hasRole(Collection<GrantedAuthority> u, String roleName) {
		for (GrantedAuthority auth : u) {
			if (auth.getAuthority().equalsIgnoreCase(roleName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Does this user have a role of this name?
	 * 
	 * @param User
	 *            u
	 * @param String
	 *            roleName
	 * @return true if user has a role with the given name
	 */
	private boolean hasRole(Collection<GrantedAuthority> u, String roleName, String orgLvl) {
		for (GrantedAuthority auth : u) {
			if (auth.getAuthority().equalsIgnoreCase(roleName) && orgLvl.equals(String.valueOf(IApplicationConstants.DEFAULT_LEVELID_VALUE))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return Returns the homePageByRole.
	 */
	public List getPrismUserHomePage() {
		return prismUserHomePage;
	}

	/**
	 * Converts basic Spring list to Map.Entry
	 * 
	 * @param homePageByRole
	 *            The homePageByRole to set.
	 */
	public void setPrismUserHomePage(List<String> homePageByRole) {
		this.prismUserHomePage = homePageByRole;
		if (homePageByRole == null) {
			setHomePageByRoleEntries(null);
			return;
		}
		List<ReportTO> entriesList = new ArrayList<ReportTO>(homePageByRole.size());
		Iterator<String> it = homePageByRole.iterator();

		while (it.hasNext()) {
			String str = it.next();
			int pos = str.indexOf('|');
			if (pos == -1) {
				throw new RuntimeException("Invalid home page entry (needs | to separate role and URL:" + str);
			}
			String[] urls = str.split("|");
			if (urls.length == 5) {
				ReportTO entry = new ReportTO();
				entry.setAllRoles(urls[0]);
				entry.setReportUrl(urls[1]);
				entry.setReportName(urls[2].replace("_", " "));
				entry.setReportId(Integer.parseInt(urls[3]));
				entry.setAssessmentName(urls[4]);
				entriesList.add(entry);
			} else if (urls.length == 6) {
				ReportTO entry = new ReportTO();
				entry.setAllRoles(urls[0]);
				entry.setReportUrl(urls[1]);
				entry.setReportName(urls[2].replace("_", " "));
				entry.setReportId(Integer.parseInt(urls[3]));
				entry.setAssessmentName(urls[4]);
				if (urls[5] != null && urls[5].endsWith(IApplicationConstants.REPORT_TYPE_TABLE)) {
					entry.setReportApiUrl(IApplicationConstants.REPORT_TABLE_URL);
				}
				entriesList.add(entry);
			}
		}
		setHomePageByRoleEntries(entriesList);
	}

	/**
	 * Get url entries
	 * 
	 * @param homePageByRole
	 */
	public void populateRoleEntries() {
		List<String> homePageByRole = this.prismUserHomePage;
		if (homePageByRole == null) {
			setHomePageByRoleEntries(null);
			return;
		}
		List<ReportTO> entriesList = new ArrayList<ReportTO>(homePageByRole.size());
		Iterator<String> it = homePageByRole.iterator();

		while (it.hasNext()) {
			String str = it.next();
			int pos = str.indexOf('|');
			if (pos == -1) {
				throw new RuntimeException("Invalid home page entry (needs | to separate role and URL:" + str);
			}
			String[] urls = str.split("\\|");
			if (urls.length == 5) {
				ReportTO entry = new ReportTO();
				entry.setRegularUser(true);
				entry.setAllRoles(urls[0]);
				entry.setReportUrl(urls[1]);
				entry.setReportName(urls[2].replace("_", " "));
				entry.setReportId(Integer.parseInt(urls[3]));
				entry.setAssessmentName(urls[4]);
				entriesList.add(entry);
			} else if (urls.length == 6) {
				ReportTO entry = new ReportTO();
				entry.setRegularUser(true);
				entry.setAllRoles(urls[0]);
				entry.setReportUrl(urls[1]);
				entry.setReportName(urls[2].replace("_", " "));
				entry.setReportId(Integer.parseInt(urls[3]));
				entry.setAssessmentName(urls[4]);
				if (urls[5] != null && urls[5].endsWith(IApplicationConstants.REPORT_TYPE_TABLE)) {
					entry.setReportApiUrl(IApplicationConstants.REPORT_TABLE_URL);
				} else if (urls[5] != null && urls[5].equals(String.valueOf(IApplicationConstants.DEFAULT_LEVELID_VALUE))) {
					entry.setOrgLevel(String.valueOf(IApplicationConstants.DEFAULT_LEVELID_VALUE));
				}
				entriesList.add(entry);
			} else if (urls.length == 2) {
				// might be parent/ctb support login
				ReportTO entry = new ReportTO();
				entry.setAllRoles(urls[0]);
				entry.setOtherUrl(urls[1]);
				entriesList.add(entry);
			} else {
				// invalid settings
				ReportTO entry = new ReportTO();
				entry.setAccessDenied(true);
				entriesList.add(entry);
			}
		}
		setHomePageByRoleEntries(entriesList);
	}

	/**
	 * @return Returns the homePageByRoleEntries.
	 */
	public List<ReportTO> getHomePageByRoleEntries() {
		populateRoleEntries();
		return homePageByRoleEntries;
	}

	/**
	 * @param homePageByRoleEntries
	 *            The homePageByRoleEntries to set.
	 */
	public void setHomePageByRoleEntries(List<ReportTO> homePageByRoleEntries) {
		this.homePageByRoleEntries = homePageByRoleEntries;
	}

	/**
	 * Check if the entered Invitation code is valid. For valid IC returns student details
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/regn/securityQuestionForUser", method = RequestMethod.POST)
	public @ResponseBody
	String securityQuestionForUser(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String contractName = Utils.getContractNameNoLogin(themeResolver.resolveThemeName(req));
		logger = LogFactory.getLoggerInstance(LoginController.class.getName(),contractName);
		logger.log(IAppLogger.INFO, "Fectching the User Security Questions");
		try {
			List<QuestionTO> questionToList = new ArrayList<QuestionTO>();
			//String username = req.getParameter("username");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("username",req.getParameter("username"));
			paramMap.put("contractName", contractName);
			
			questionToList = parentService.getSecurityQuestionForUser(paramMap);
			if (questionToList != null) {
				String jsonString = JsonUtil.convertToJsonAdmin(questionToList);
				res.setContentType("application/json");
				res.getWriter().write(jsonString);
				// res.getWriter().write( "{\"status\":\"Success\"}" );
				logger.log(IAppLogger.DEBUG, jsonString);
			}

		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Fectching the User Security Questions");
		}
		return null;
	}

	/**
	 * This function validate the user secret answers
	 * 
	 * @param req
	 * @param res
	 * @return
	 */

	@RequestMapping(value = "/regn/checkAnswers", method = RequestMethod.GET)
	public ModelAndView checkAnswers(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			String contractName = Utils.getContractNameNoLogin(themeResolver.resolveThemeName(req));
			logger = LogFactory.getLoggerInstance(LoginController.class.getName(),contractName);
			
			logger.log(IAppLogger.INFO, "Enter: checkAnswers()");
			/*String userName = (String) req.getParameter("username");
			String ans1 = (String) req.getParameter("ans1");
			String ans2 = (String) req.getParameter("ans2");
			String ans3 = (String) req.getParameter("ans3");
			String questionId1 = (String) req.getParameter("questionId1");
			String questionId2 = (String) req.getParameter("questionId2");
			String questionId3 = (String) req.getParameter("questionId3");*/
			
			List<QuestionTO> questionToList = new ArrayList<QuestionTO>();
			QuestionTO questionTo = new QuestionTO();
			questionTo.setQuestionId(Long.parseLong(req.getParameter("questionId1")));
			questionTo.setAnswer((String) req.getParameter("ans1"));
			questionToList.add(questionTo);
			
			questionTo = new QuestionTO();
			questionTo.setQuestionId(Long.parseLong(req.getParameter("questionId2")));
			questionTo.setAnswer((String) req.getParameter("ans2"));
			questionToList.add(questionTo);
			
			questionTo = new QuestionTO();
			questionTo.setQuestionId(Long.parseLong(req.getParameter("questionId3")));
			questionTo.setAnswer((String) req.getParameter("ans3"));
			questionToList.add(questionTo);
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("username", req.getParameter("username"));
			paramMap.put("questionToList", questionToList);
			paramMap.put("contractName", contractName);

			boolean isValid = parentService.validateAnswers(paramMap);
			res.setContentType("text/plain");
			String status = "Fail";
			if (isValid) {
				status = "Success";
			}
			res.getWriter().write("{\"status\":\"" + status + "\"}");

			logger.log(IAppLogger.INFO, "Exit: checkAnswers()");

		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error validating answers", e);
		}
		return null;
	}

	/**
	 * Method performs reset password operation. This method is called through AJAX
	 * 
	 * @param req
	 * @param res
	 * @return Returns nothing, instead writes password in response as JSON
	 */
	@RequestMapping(value = "/regn/generateTempPwd", method = RequestMethod.GET)
	public String generateTempPwd(HttpServletRequest req, HttpServletResponse res) {
		String contractName = Utils.getContractNameNoLogin(themeResolver.resolveThemeName(req));
		logger = LogFactory.getLoggerInstance(LoginController.class.getName(),contractName);
		
		logger.log(IAppLogger.INFO, "Enter: generateTempPwd()");
		String sendEmailFlag = "0";
		try {
			String userName = (String) req.getParameter("username");
			Map<String, Object> paramMap = new HashMap<String, Object>();			
			if (userName != null) {
				paramMap.put("username", userName);
				paramMap.put("contractName", contractName);
				UserTO userTO = adminService.resetPassword(paramMap);
				if (userTO.getUserEmail() != null && userTO.getPassword() != null) {
					try{
						sendUserPasswordEmail(userTO.getUserEmail(),null,userTO.getPassword());
						sendEmailFlag = "1";						
					} catch (Exception e) {
						sendEmailFlag = "0";
						logger.log(IAppLogger.ERROR, e.getMessage(), e);
					}					
				}
			}
				res.getWriter().write("{\"sendEmailFlag\":\"" + sendEmailFlag + "\"}");

		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: generateTempPwd()");
		}
		return null;

	}

	/**
	 * retrieve users
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/regn/getUserNames", method = RequestMethod.POST)
	public @ResponseBody
	String getUserNames(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String contractName = Utils.getContractNameNoLogin(themeResolver.resolveThemeName(req));
		logger = LogFactory.getLoggerInstance(LoginController.class.getName(),contractName);
		
		logger.log(IAppLogger.INFO, "Fectching the User Names");
		String sendEmailFlag = "0";
		String jsonString = null;
		try {
			List<UserTO> userToList = new ArrayList<UserTO>();
			String emailId = req.getParameter("emailId");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("emailId",emailId);
			paramMap.put("contractName", contractName);
			
			userToList = parentService.getUserNamesByEmail(paramMap);
			if (userToList != null && userToList.size()> 0) {
				/*String jsonString = JsonUtil.convertToJsonAdmin(userToList);
				res.setContentType("application/json");
				res.getWriter().write(jsonString);
				// res.getWriter().write( "{\"status\":\"Success\"}" );
				logger.log(IAppLogger.DEBUG, jsonString);*/
				try {
					//SESMailService.sendUserPasswordEmail(emailId,userToList,null);
					sendUserPasswordEmail(emailId,userToList,null);
					sendEmailFlag = "1";
				} catch (Exception e) {
					sendEmailFlag = "0";
					logger.log(IAppLogger.ERROR, e.getMessage(), e);
				}
				//return jsonString;
				
			} else {
				sendEmailFlag = "0";
				logger.log(IAppLogger.INFO, "No user found");
			}
			
			jsonString = "{\"sendEmailFlag\" : \"" + sendEmailFlag + "\"}";
			logger.log(IAppLogger.INFO, jsonString);
			logger.log(IAppLogger.INFO, "Exit: Forgot username");

		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Fectching the User Names");
		}
		return jsonString;
	}

	/**
	 * Check if entered username is available and the user is enabled
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/regn/checkActiveUser", method = RequestMethod.POST)
	public @ResponseBody
	String checkActiveUserAvailability(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String contractName = Utils.getContractNameNoLogin(themeResolver.resolveThemeName(req));
		logger = LogFactory.getLoggerInstance(LoginController.class.getName(),contractName);
		
		logger.log(IAppLogger.INFO, "Validating Username");

		try {
			//String username = req.getParameter("username");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("username",req.getParameter("username"));
			paramMap.put("contractName", contractName);
			// check username is available and the user is enabled
			if (parentService.checkActiveUserAvailability(paramMap)) {
				res.setContentType("application/json");
				res.getWriter().write("{\"status\":\"Success\", \"available\":\"" + "true" + "\"}");
			} else {
				res.setContentType("application/json");
				res.getWriter().write("{\"status\":\"Failure\", \"available\":\"" + "false" + "\"}");
			}

		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Validating Username");
		}
		return null;
	}

/*	*//**
	 * Inors home page
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 *//*
	@RequestMapping(value = "/inorsHome", method = RequestMethod.GET)
	public ModelAndView inorsHome(HttpServletRequest req, HttpServletResponse res) throws IOException {

		ModelAndView modelAndView = new ModelAndView("user/inorsHome");
		return modelAndView;
	}*/

	/**
	 * Changed by Arunava Datta Load user specific dynamic login page message
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/loadHomePage", method = RequestMethod.GET)
	public ModelAndView loadHomePage(HttpServletRequest req, HttpServletResponse res) throws IOException {
		//Fetching Home page Content moved to ajax call: loadHomePageMsg()
		ModelAndView modelAndView = new ModelAndView("user/inorsHome");
		return modelAndView;
	}
	
	@RequestMapping(value = "/loadGrowthHome", method = RequestMethod.GET)
	public ModelAndView loadGrowthHome(HttpServletRequest req, HttpServletResponse res) throws IOException {
		//Fetching Home page Content moved to ajax call: loadHomePageMsg()
		ModelAndView modelAndView = new ModelAndView("user/growthHome");
		return modelAndView;
	}
	
	
	/**
	 * Changed by Joy for getSystemConfigurationMessage() architecture change
	 * Changed by Abir Load user specific dynamic login page message
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/loadHomePageMsg", method = RequestMethod.GET)
	@ResponseBody
	public String loadHomePageMsg(HttpServletRequest req, HttpServletResponse res) throws IOException,BusinessException {
		String contractName = Utils.getContractNameNoLogin(themeResolver.resolveThemeName(req));
		logger = LogFactory.getLoggerInstance(LoginController.class.getName(),contractName);
		
		logger.log(IAppLogger.INFO, "Enter: LoginController - loadHomePageMsg()");
		String homePage = req.getParameter("homeMessage");
		long t1 = System.currentTimeMillis();
		
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("theme", themeResolver.resolveThemeName(req));
		paramMap.put("custProdId", req.getSession().getAttribute(IApplicationConstants.DEFAULT_CUST_PROD_ID));
		if("growth".equals(homePage)) {
			paramMap.put("action","homeGrowth");
		}else{
			paramMap.put("action","home");
		}
		
		String homePageInfoMessage = "";
		String jsonString = "";
		try {
			Map<String, Object> messageMap = getMessageMap(paramMap);
			homePageInfoMessage = (String)messageMap.get("systemMessage");
			//Fixed for TD 77263 - By Joy
			com.ctb.prism.core.transferobject.ObjectValueTO homePageMsgObj = new com.ctb.prism.core.transferobject.ObjectValueTO();
			homePageMsgObj.setValue(homePageInfoMessage);
			jsonString = new Gson().toJson(homePageMsgObj);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		} finally {
			logger.log(IAppLogger.INFO, "HomePage Message: "+jsonString);
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: LoginController - loadHomePageMsg() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return  jsonString;
	}
	
	@Autowired
	private EmailSender emailSender;
	
	private void sendUserPasswordEmail(String email, List<UserTO> userToList, String password) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: Forgot username/password");
		Properties prop = new Properties();
		prop.setProperty(IEmailConstants.SMTP_HOST, propertyLookup.get(IEmailConstants.SMTP_HOST));
		prop.setProperty(IEmailConstants.SMTP_PORT, propertyLookup.get(IEmailConstants.SMTP_PORT));
		prop.setProperty("senderMail", propertyLookup.get("senderMail"));
		prop.setProperty("supportEmail", propertyLookup.get("supportEmail"));
		String mailBody = null;
		String subject = null;

		/**for forgot username **/
		if(userToList != null) {
			subject = propertyLookup.get("mail.fu.subject");
			if(userToList.size() == 1) {
				mailBody = propertyLookup.get("mail.fu.body");
				mailBody = CustomStringUtil.appendString(mailBody, propertyLookup.get("mail.fu.details"));
				mailBody = CustomStringUtil.replaceCharacterInString('?', userToList.get(0).getUserName()!=null?userToList.get(0).getUserName():"", mailBody);
				mailBody = CustomStringUtil.replaceCharacterInString('#', userToList.get(0).getFirstName()!=null?userToList.get(0).getFirstName():"", mailBody);
				mailBody = CustomStringUtil.replaceCharacterInString('^', userToList.get(0).getLastName()!=null?userToList.get(0).getLastName():"", mailBody);
				
			} else if(userToList.size() > 1) {
				mailBody = propertyLookup.get("mail.fu.multi.body");
				Iterator<UserTO>  it = userToList.iterator();
				while(it.hasNext()){
					UserTO userto = (UserTO)it.next();
					mailBody = CustomStringUtil.appendString(mailBody, propertyLookup.get("mail.fu.details"));
					mailBody = CustomStringUtil.replaceCharacterInString('?', userto.getUserName()!=null?userto.getUserName():"", mailBody);
					mailBody = CustomStringUtil.replaceCharacterInString('#', userto.getFirstName()!=null?userto.getFirstName():"", mailBody);
					mailBody = CustomStringUtil.replaceCharacterInString('^', userto.getLastName()!=null? userto.getLastName():"", mailBody);
				}
			}
		} else { /**for forgot password **/
			subject = propertyLookup.get("mail.fp.subject");
			mailBody = propertyLookup.get("mail.fp.body");
			mailBody = CustomStringUtil.replaceCharacterInString('?', password, mailBody);
		}
						
		logger.log(IAppLogger.INFO, "---------------------------------------------------------------");
		logger.log(IAppLogger.INFO, "SMTP_HOST: " + prop.getProperty(IEmailConstants.SMTP_HOST));
		logger.log(IAppLogger.INFO, "SMTP_PORT: " + prop.getProperty(IEmailConstants.SMTP_PORT));
		logger.log(IAppLogger.INFO, "---------------------------------------------------------------");
		logger.log(IAppLogger.INFO, "Subject: " + subject);
		logger.log(IAppLogger.INFO, "From: " + prop.getProperty("senderMail"));
		logger.log(IAppLogger.INFO, "To: " + email);
		logger.log(IAppLogger.INFO, "Body: " + mailBody);
		logger.log(IAppLogger.INFO, "---------------------------------------------------------------");
		logger.log(IAppLogger.INFO, "Email triggered to: " + email);
		if(IApplicationConstants.ACTIVE_FLAG.equals(propertyLookup.get(IEmailConstants.REALTIME_EMAIL_FLAG))) {
			emailSender.sendMail(prop, email, null, null, subject, mailBody);
		} else if(IApplicationConstants.INACTIVE_FLAG.equals(propertyLookup.get(IEmailConstants.REALTIME_EMAIL_FLAG))) {
			logger.log(IAppLogger.WARN, "Skipping Email Sending.");
		} else {
			logger.log(IAppLogger.ERROR, "Invalid property value. " + IEmailConstants.REALTIME_EMAIL_FLAG + "=" + propertyLookup.get(IEmailConstants.REALTIME_EMAIL_FLAG));
		}
		logger.log(IAppLogger.INFO, "Email sent to: " + email);
		logger.log(IAppLogger.INFO, "Exit: notificationMailGD()");
	}
	
	
}
