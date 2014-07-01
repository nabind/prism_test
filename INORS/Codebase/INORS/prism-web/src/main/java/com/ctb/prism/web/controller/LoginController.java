package com.ctb.prism.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.transferobject.ObjectValueTO;
import com.ctb.prism.login.Service.ILoginService;
import com.ctb.prism.login.security.provider.AuthenticatedUser;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.service.IParentService;
import com.ctb.prism.parent.transferobject.ManageContentTO;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.QuestionTO;
import com.ctb.prism.report.service.IReportService;
import com.ctb.prism.report.transferobject.ReportTO;
import com.ctb.prism.web.util.JsonUtil;
import com.google.gson.Gson;

@Controller
public class LoginController {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(LoginController.class.getName());

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

		UserTO user = null;
		try {
			user = loginService.getUserDetails(req.getParameter("loginAsUsername"));

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
	 * This method is to display landing page
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/landing", method = RequestMethod.GET)
	public ModelAndView loadLandingPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.log(IAppLogger.INFO, "Enter: loadLandingPage()");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("REPORT_NAME", IApplicationConstants.GENERIC_REPORT_NAME);
		paramMap.put("MESSAGE_TYPE", IApplicationConstants.GENERIC_MESSAGE_TYPE);
		paramMap.put("MESSAGE_NAME", IApplicationConstants.COMMON_LOG_IN);
		String commonLogInInfoMessage = loginService.getSystemConfigurationMessage(paramMap);
		ModelAndView modelAndView = new ModelAndView("common/landing");
		modelAndView.addObject("commonLogInInfoMessage", commonLogInInfoMessage);
		logger.log(IAppLogger.INFO, "Exit: loadLandingPage()");
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
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView loadLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.log(IAppLogger.INFO, "Enter: loadLoginPage()");
		String parent = request.getParameter(IApplicationConstants.PARENT_LOGIN);
		String mess_login_error = (String) request.getParameter("login_error");
		String message = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("REPORT_NAME", IApplicationConstants.GENERIC_REPORT_NAME);
		paramMap.put("MESSAGE_TYPE", IApplicationConstants.GENERIC_MESSAGE_TYPE);
		paramMap.put("MESSAGE_NAME", IApplicationConstants.COMMON_LOG_IN);
		String logInInfoMessage = loginService.getSystemConfigurationMessage(paramMap);
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
		logger.log(IAppLogger.INFO, "Enter: userlogin()");
		logger.log(IAppLogger.INFO, "theme -------------> "+themeResolver.resolveThemeName(request));
		String mess_login_error = (String) request.getParameter("login_error");
		String message = null;
		if ("1".equalsIgnoreCase(mess_login_error)) {
			logger.log(IAppLogger.ERROR, "Invalid Login");
			message = "error.login.invalidlogin";
		} else if ("2".equalsIgnoreCase(mess_login_error)) {
			logger.log(IAppLogger.ERROR, "Session Expired");
			message = "error.login.sessionexpired";
		} else {
			// this is proper login
		}
		ModelAndView modelAndView = new ModelAndView("user/userlogin");
		modelAndView.addObject("message", message);
		logger.log(IAppLogger.INFO, "Exit: userlogin()");
		return modelAndView;
	}
	
	/**
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws BusinessException
	 * Get login message for particular user(teacher/parent).
	 */
	@RequestMapping(value="/getLoginMessage", method=RequestMethod.GET)
	public @ResponseBody 
	String getLoginMessage(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException,BusinessException{
		logger.log(IAppLogger.INFO, "Enter: LoginController - getLoginMessage()");
		long t1 = System.currentTimeMillis();
		String parent = "";
		Map<String, Object> paramMapLoginMessage = null;
		String loginMessage = "";
		String jsonString = "";
		try{
			parent = themeResolver.resolveThemeName(request); 
			paramMapLoginMessage = new HashMap<String, Object>();
			paramMapLoginMessage.put("REPORT_NAME", IApplicationConstants.GENERIC_REPORT_NAME);
			paramMapLoginMessage.put("MESSAGE_TYPE", IApplicationConstants.GENERIC_MESSAGE_TYPE);
			
			if (IApplicationConstants.PARENT_LOGIN.equals(parent)) {
				paramMapLoginMessage.put("MESSAGE_NAME", IApplicationConstants.PARENT_LOG_IN);
			} else {
				paramMapLoginMessage.put("MESSAGE_NAME", IApplicationConstants.TEACHER_LOG_IN);
			}
			
			loginMessage = loginService.getSystemConfigurationMessage(paramMapLoginMessage);
			com.ctb.prism.core.transferobject.ObjectValueTO loginMessageObject = new com.ctb.prism.core.transferobject.ObjectValueTO();
			loginMessageObject.setValue(loginMessage);
			jsonString = new Gson().toJson(loginMessageObject);
			
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			logger.log(IAppLogger.INFO, "Login Message: "+jsonString);
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: LoginController - getLoginMessage() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return jsonString;
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
	@RequestMapping(value = "/validateUser", method = RequestMethod.GET)
	public ModelAndView validateUser(HttpServletRequest req, HttpServletResponse res) throws IOException {
		logger.log(IAppLogger.INFO, "Enter: validateUser()");
		ModelAndView modelAndView = null;
		String orgLvl = null;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				ReportTO homeReport = null;
				String username = null;
				Collection<GrantedAuthority> authorities = null;
				String userDisplay = (String) req.getSession().getAttribute(IApplicationConstants.CURR_USER_DISPLAY);
				UserTO user = null;
				if (auth.getPrincipal() instanceof LdapUserDetailsImpl) {
					LdapUserDetailsImpl userDetails = (LdapUserDetailsImpl) auth.getPrincipal();
					req.getSession().setAttribute(IApplicationConstants.CURRUSER, userDetails.getUsername());
					req.getSession().setAttribute(IApplicationConstants.AUTHORITIES, userDetails.getAuthorities());
					user = loginService.getUserDetails(userDetails.getUsername());
					req.getSession().setAttribute(IApplicationConstants.CURRORG, user.getOrgId());
					req.getSession().setAttribute(IApplicationConstants.CURRORGLVL, user.getOrgNodeLevel());

					authorities = userDetails.getAuthorities();
					username = userDetails.getUsername();
				} else if (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
					org.springframework.security.core.userdetails.User userDetails = (User) auth.getPrincipal();
					req.getSession().setAttribute(IApplicationConstants.CURRUSER, userDetails.getUsername());
					req.getSession().setAttribute(IApplicationConstants.AUTHORITIES, userDetails.getAuthorities());
					user = loginService.getUserDetails(userDetails.getUsername());
					req.getSession().setAttribute(IApplicationConstants.CURRORG, user.getOrgId());
					req.getSession().setAttribute(IApplicationConstants.CURRORGLVL, user.getOrgNodeLevel());

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
						// system reloading user after first-time change password
						user = loginService.getUserDetails(user.getUserName());
					}
					req.getSession().setAttribute(IApplicationConstants.CURRORG, user.getOrgId());
					req.getSession().setAttribute(IApplicationConstants.CURRORGLVL, user.getOrgNodeLevel());

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
				req.getSession().setAttribute(IApplicationConstants.CURRUSERID, user.getUserId());
				req.getSession().setAttribute(IApplicationConstants.CURR_USER_DISPLAY, user.getDisplayName());
				req.getSession().setAttribute(IApplicationConstants.CUSTOMER, user.getCustomerId());
				req.getSession().setAttribute(IApplicationConstants.EMAIL, user.getUserEmail());
				req.getSession().setAttribute(IApplicationConstants.PRODUCT_NAME, user.getProduct());
				String switchUser = (String) req.getSession().getAttribute(IApplicationConstants.PREV_ADMIN);
				
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
				if (homeReport.isRegularUser() || ((null != req.getSession().getAttribute("PARENT_LOGIN")) && ("parent".equals(req.getSession().getAttribute("PARENT_LOGIN"))))) {
					if ((null != req.getSession().getAttribute("PARENT_LOGIN")) && ("parent".equals(req.getSession().getAttribute("PARENT_LOGIN")))) {
						modelAndView = new ModelAndView("parent/parentWelcome");
						themeResolver.setThemeName(req, res, "parent");
					} else {
						homeReport.setProductName("TerraNova 3 : ");
						modelAndView = new ModelAndView("user/welcome");
						modelAndView.addObject("homeReport", homeReport);
						themeResolver.setThemeName(req, res, "acsi");
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
			}
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage());
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

		String username = (String) req.getParameter("username");
		// fetch security questions
		List<QuestionTO> questionList = parentService.getSecretQuestions();
		// logger.log(IAppLogger.DEBUG, questionList.size());
		// logger.log(IAppLogger.DEBUG, "into changePassword......................");
		// logger.log(IAppLogger.DEBUG, "username in  changePassword......................"+username);
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
		/*
		 * by deepak for fetching the user registration response from changePassword page
		 * 
		 * This code is similar to parent registration code so copied from Parent-controller object
		 */

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
		questionTO.setQuestionId(Long.valueOf(qsn1));
		questionTO.setAnswer(ans1);
		questionTO.setAnswerId(Long.valueOf(ansId1));
		questionToList.add(questionTO);
		String qsn2 = (String) req.getParameter("qsn2");
		String ans2 = (String) req.getParameter("ans2");
		String ansId2 = (String) req.getParameter("ansId2");
		questionTO = new QuestionTO();
		questionTO.setQuestionId(Long.valueOf(qsn2));
		questionTO.setAnswer(ans2);
		questionTO.setAnswerId(Long.valueOf(ansId2));
		questionToList.add(questionTO);

		String qsn3 = (String) req.getParameter("qsn3");
		String ans3 = (String) req.getParameter("ans3");
		String ansId3 = (String) req.getParameter("ansId3");
		questionTO = new QuestionTO();
		questionTO.setQuestionId(Long.valueOf(qsn3));
		questionTO.setAnswer(ans3);
		questionTO.setAnswerId(Long.valueOf(ansId3));
		questionToList.add(questionTO);
		parentTO.setQuestionToList(questionToList);
		boolean success = false;
		try {
			success = parentService.firstTimeUserLogin(parentTO);
		} catch (Exception ex) {
			success = false;
		}
		ModelAndView mv = null;
		if (success) {
			// setting this attribute so that application reloads user data during login
			req.setAttribute(IApplicationConstants.RELOAD_USER, IApplicationConstants.TRUE);
			req.getSession().setAttribute(IApplicationConstants.RELOAD_USER, IApplicationConstants.TRUE);
			return validateUser(req, res);
		} else {
			// fetch security questions
			List<QuestionTO> questionList = parentService.getSecretQuestions();
			// logger.log(IAppLogger.DEBUG, questionList.size());
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
	@RequestMapping(value = "/regn/securityQuestionForUser", method = RequestMethod.GET)
	public @ResponseBody
	String securityQuestionForUser(HttpServletRequest req, HttpServletResponse res) throws IOException {
		logger.log(IAppLogger.INFO, "Fectching the User Security Questions");
		try {
			List<QuestionTO> questionToList = new ArrayList<QuestionTO>();
			String username = req.getParameter("username");
			questionToList = parentService.getSecurityQuestionForUser(username);
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
			logger.log(IAppLogger.INFO, "Enter: checkAnswers()");
			String userName = (String) req.getParameter("username");
			String ans1 = (String) req.getParameter("ans1");
			String ans2 = (String) req.getParameter("ans2");
			String ans3 = (String) req.getParameter("ans3");
			String questionId1 = (String) req.getParameter("questionId1");
			String questionId2 = (String) req.getParameter("questionId2");
			String questionId3 = (String) req.getParameter("questionId3");

			boolean isValid = parentService.validateAnswers(userName, ans1, ans2, ans3, questionId1, questionId2, questionId3);
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
		logger.log(IAppLogger.INFO, "Enter: generateTempPwd()");
		try {
			String userName = (String) req.getParameter("username");
			if (userName != null) {
				String newPassword = adminService.resetPassword(userName);
				if (newPassword != null) {
					res.getWriter().write("{\"password\":\"" + newPassword + "\"}");
				}
			}

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
	@RequestMapping(value = "/regn/getUserNames", method = RequestMethod.GET)
	public @ResponseBody
	String getUserNames(HttpServletRequest req, HttpServletResponse res) throws IOException {
		logger.log(IAppLogger.INFO, "Fectching the User Names");
		try {
			List<UserTO> userToList = new ArrayList<UserTO>();
			String emailId = req.getParameter("emailId");
			userToList = parentService.getUserNamesByEmail(emailId);
			if (userToList != null) {
				String jsonString = JsonUtil.convertToJsonAdmin(userToList);
				res.setContentType("application/json");
				res.getWriter().write(jsonString);
				// res.getWriter().write( "{\"status\":\"Success\"}" );
				logger.log(IAppLogger.DEBUG, jsonString);
			}

		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Fectching the User Names");
		}
		return null;
	}

	/**
	 * Check if entered username is available and the user is enabled
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/regn/checkActiveUser", method = RequestMethod.GET)
	public @ResponseBody
	String checkActiveUserAvailability(HttpServletRequest req, HttpServletResponse res) throws IOException {
		logger.log(IAppLogger.INFO, "Validating Username");

		try {
			String username = req.getParameter("username");
			// check username is available and the user is enabled
			if (parentService.checkActiveUserAvailability(username)) {
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
	 * Changed by Abir Load user specific dynamic login page message
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/loadHomePageMsg", method = RequestMethod.GET)
	@ResponseBody
	public String loadHomePageMsg(HttpServletRequest req, HttpServletResponse res) throws IOException,BusinessException {
		logger.log(IAppLogger.INFO, "Enter: LoginController - loadHomePageMsg()");
		long t1 = System.currentTimeMillis();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("REPORT_NAME", IApplicationConstants.GENERIC_REPORT_NAME);
		paramMap.put("MESSAGE_TYPE", IApplicationConstants.GENERIC_MESSAGE_TYPE);
		paramMap.put("MESSAGE_NAME", IApplicationConstants.INORS_HOME_PAGE);
		String inorsHomePageInfoMessage = "";
		String jsonString = "";
		try {
			inorsHomePageInfoMessage = loginService.getSystemConfigurationMessage(paramMap);
			//Fixed for TD 77263 - By Joy
			com.ctb.prism.core.transferobject.ObjectValueTO homePageMsgObj = new com.ctb.prism.core.transferobject.ObjectValueTO();
			homePageMsgObj.setValue(inorsHomePageInfoMessage);
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

}
