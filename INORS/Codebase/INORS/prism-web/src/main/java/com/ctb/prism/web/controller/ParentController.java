package com.ctb.prism.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ctb.prism.admin.service.IAdminService;
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.LdapManager;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.Service.ILoginService;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.service.IParentService;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.QuestionTO;
import com.ctb.prism.parent.transferobject.StudentTO;
import com.ctb.prism.web.util.JsonUtil;

@Controller
public class ParentController {

	private static final IAppLogger logger = LogFactory
			.getLoggerInstance(ParentController.class.getName());

	@Autowired
	private IParentService parentService;
	
	@Autowired
	private LdapManager ldapManager;
	
	@Autowired
	private IPropertyLookup propertyLookup;
	
	
	@Autowired
	private IAdminService adminService;
	@Autowired
	private ILoginService loginService;

	
	/**
	 * Opens user registration screen for IC code registration
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView parentRegistration(HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		logger.log(IAppLogger.INFO, "Open user registration screen");
		//fetch security questions
		List<QuestionTO> questionList = parentService.getSecretQuestions();
		logger.log(IAppLogger.DEBUG, ""+questionList.size());
		ModelAndView mv = new ModelAndView("parent/registration");
		mv.addObject("secretQuestionList", null);
		mv.addObject("masterQuestionList", questionList);
		logger.log(IAppLogger.INFO, "Exit: Open user registration screen");
		return mv;
	}
	
	/**
	 * Add new invitation code with existing parent account
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */
	@Secured({"ROLE_PARENT"})
	@RequestMapping(value = "/claimInvitation", method = RequestMethod.GET)
	public ModelAndView addNewInvitation(HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		logger.log(IAppLogger.INFO, "Enter: Add new invitation code with existing parent account");
		String status = "Fail";
		String curruser = (String) req.getSession().getAttribute(IApplicationConstants.CURRUSER);
		if(IApplicationConstants.PARENT_LOGIN.equals(req.getSession().getAttribute("PARENT_LOGIN"))) {
			String invitationCode = (String)req.getParameter("invitationCode");
			boolean success= parentService.addInvitationToAccount(curruser,invitationCode);
			res.setContentType("text/plain");
			if(success) {
				status = "Success";
			} 
		} else {
			status = "INVALID_USER";
		}
		res.getWriter().write( "{\"status\":\""+status+"\"}" );
		logger.log(IAppLogger.INFO, "Exit: Add new invitation code with existing parent account");
		return null;
	}
	//End
	/**
	 * Save user registration 
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)

	public ModelAndView register(HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		logger.log(IAppLogger.INFO, "Save user registration screen");
		ParentTO parentTO = new ParentTO();
		QuestionTO questionTO = null;
		List<QuestionTO> questionToList = new ArrayList<QuestionTO>();	
		
		ModelAndView mv = null;
		
		// save user details
		String userName = (String)req.getParameter("username");
		String password = (String)req.getParameter("password"); 
		/*if (password != null) {
			if (password.equals(userName)) {
				mv = new ModelAndView("parent/registration");
				mv.addObject("errorMesage", "Password should not match with Username");
				return mv;
			}
			if(!Utils.validatePassword(password)) {
				mv = new ModelAndView("parent/registration");
				mv.addObject("errorMesage", "Please provide some valid password");
				return mv;
			}
		}*/
		
		String firstName = (String)req.getParameter("firstName");
		String lastName = (String)req.getParameter("lastName");
		String mailId = (String)req.getParameter("mail");
		String mobile = (String)req.getParameter("mobile");
		String zip_code = (String)req.getParameter("zip_code");
		String street = (String)req.getParameter("street");
		String city = (String)req.getParameter("city");
		String state = (String)req.getParameter("state");
		String country = (String)req.getParameter("country");
		String invitationCode = (String)req.getParameter("invitationCode");
		String displayName = req.getParameter("displayName");
		
		parentTO.setUserName(userName);
		parentTO.setPassword(password);
		parentTO.setFirstName(firstName);
		parentTO.setLastName(lastName);
		parentTO.setMail(mailId);
		parentTO.setMobile(mobile);
		parentTO.setCountry(country);
		parentTO.setZipCode(zip_code);
		parentTO.setStreet(street);
		parentTO.setCity(city);
		parentTO.setState(state);
		parentTO.setCountry(country);
		parentTO.setInvitationCode(invitationCode);		
		parentTO.setDisplayName(displayName);
		
		String qsn1 = (String)req.getParameter("qsn1");
		String ans1 = (String)req.getParameter("ans1");
		questionTO = new QuestionTO();
		questionTO.setQuestionId(Long.valueOf(qsn1));
		questionTO.setAnswer(ans1);
		questionToList.add(questionTO);
		String qsn2 = (String)req.getParameter("qsn2");
		String ans2 = (String)req.getParameter("ans2");
		questionTO = new QuestionTO();
		questionTO.setQuestionId(Long.valueOf(qsn2));
		questionTO.setAnswer(ans2);	
		questionToList.add(questionTO);		
		String qsn3 = (String)req.getParameter("qsn3");
		String ans3 = (String)req.getParameter("ans3");
		questionTO = new QuestionTO();
		questionTO.setQuestionId(Long.valueOf(qsn3));
		questionTO.setAnswer(ans3);		
		questionToList.add(questionTO);
		parentTO.setQuestionToList(questionToList);
		parentTO.setFirstTimeUser(false);
		logger.log(IAppLogger.DEBUG,qsn1+ans1);		

		boolean success = false;
		try {
			success = parentService.registerUser(parentTO);
		} catch (BusinessException e) {
			logger.log(IAppLogger.ERROR, "Error Saving User", e);
			res.setContentType("text/plain");
			res.getWriter().write("{\"status\":\"LDAP_ERROR\", \"message\":\""+e.getCustomExceptionMessage()+"\"}");
		}
		if(success) {
			mv = new ModelAndView("parent/regSuccess");
		} else {
			mv = new ModelAndView("parent/reqError");
			mv.addObject("errorMesage", "message");
		}
		logger.log(IAppLogger.INFO, "Exit: Save user registration screen");
		return mv;
	}
	
	
	/**
	 * This method is used to display user profile
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	//Added by Ravi for Manage Profile
	@RequestMapping(value = "/myAccount", method = RequestMethod.GET)
	public ModelAndView myAccount(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ParentTO parentTO = new ParentTO();
		String loggedinUser = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
		parentTO = parentService.manageParentAccountDetails(loggedinUser);		
		List<QuestionTO> questionList = parentService.getSecretQuestions();
		ModelAndView modelAndView = null;
		modelAndView = new ModelAndView("user/profile");
		modelAndView.addObject("parentAccountDetail", parentTO);
		modelAndView.addObject("secretQuestionList", parentTO.getQuestionToList());
		modelAndView.addObject("masterQuestionList", questionList);
		modelAndView.addObject("PDCT_NAME",propertyLookup.get("PDCT_NAME"));
		return modelAndView;
	}

	/**
	 * Save profile details 
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */
	//Added by Ravi for Manage Profile
	@RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
	public ModelAndView updateProfile(HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		try{
		logger.log(IAppLogger.INFO, "Update user profile details on save");
		ParentTO parentTO = new ParentTO();
		QuestionTO questionTO = null;
		List<QuestionTO> questionToList = new ArrayList<QuestionTO>();		
		
		// save user details
		String userId = req.getParameter("userId");
		String userName = req.getParameter("username");
		String password = req.getParameter("password"); 
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String mailId = req.getParameter("verify_mail");
		String mobile = req.getParameter("mobile");
		String zip_code = req.getParameter("zip_code");
		String country = req.getParameter("userCountry");
		// checking whether the country has been selected form the drop down, if not then taking the default value of the dropdown
		if(country.trim().length()<=0){
			 country = req.getParameter("country");
		}
		String street = req.getParameter("street");
		String city = req.getParameter("city");
		String state = req.getParameter("state");
		String displayName = req.getParameter("displayName");
		
		// update session with new display name
		req.getSession().setAttribute(IApplicationConstants.CURR_USER_DISPLAY, displayName);
		req.getSession().setAttribute(IApplicationConstants.RELOAD_USER, IApplicationConstants.TRUE);
		
		String status = "Fail";
		
	//checking for valid password
		if(password!=null && password.trim().length() > 0)
		{
			if (password.equals(userName)) {
				res.setContentType("text/plain");
				status="equalsUserName";
				res.getWriter().write("{\"status\":\"" + status + "\"}");
				return null;
			} else if(!Utils.validatePassword(password)) {
				res.setContentType("text/plain");
				status="invalidPwd";
				res.getWriter().write("{\"status\":\"" + status + "\"}");
				return null;
			}
		}
		
		parentTO.setUserId(Long.valueOf(userId));
		parentTO.setUserName(userName);
		parentTO.setPassword(password);
		parentTO.setFirstName(firstName);
		parentTO.setLastName(lastName);
		parentTO.setMail(mailId);
		parentTO.setMobile(mobile);
		parentTO.setCountry(country);
		parentTO.setZipCode(zip_code);
		parentTO.setCountry(country);	
		parentTO.setStreet(street);
		parentTO.setCity(city);
		parentTO.setState(state);
		parentTO.setDisplayName(displayName);
	
		String qsn1 = (String)req.getParameter("qsn1");
		String ans1 = (String)req.getParameter("ans1");
		String ansId1 = (String)req.getParameter("ansId1");
		questionTO = new QuestionTO();
		questionTO.setQuestionId(Long.valueOf(qsn1));
		questionTO.setAnswer(ans1);
		questionTO.setAnswerId(Long.valueOf(ansId1));
		questionToList.add(questionTO);
		String qsn2 = (String)req.getParameter("qsn2");
		String ans2 = (String)req.getParameter("ans2");
		String ansId2 = (String)req.getParameter("ansId2");
		questionTO = new QuestionTO();
		questionTO.setQuestionId(Long.valueOf(qsn2));
		questionTO.setAnswer(ans2);
		questionTO.setAnswerId(Long.valueOf(ansId2));
		questionToList.add(questionTO);		
		String qsn3 = (String)req.getParameter("qsn3");
		String ans3 = (String)req.getParameter("ans3");
		String ansId3 = (String)req.getParameter("ansId3");
		questionTO = new QuestionTO();
		questionTO.setQuestionId(Long.valueOf(qsn3));
		questionTO.setAnswer(ans3);		
		questionTO.setAnswerId(Long.valueOf(ansId3));	
		questionToList.add(questionTO);
		parentTO.setQuestionToList(questionToList);	

		boolean success = parentService.updateUserProfile(parentTO);
		
		res.setContentType("text/plain");
		if(success) {
			status = "Success";
		} 
		res.getWriter().write( "{\"status\":\""+status+"\"}" );
		logger.log(IAppLogger.INFO, "Exit: update user profile details on save");
		} catch (BusinessException bex) {
			logger.log(IAppLogger.ERROR, "Error Saving User", bex);
			res.setContentType("text/plain");
			res.getWriter().write("{\"status\":\"LDAP_ERROR\", \"message\":\""+bex.getCustomExceptionMessage()+"\"}");
		} catch (Exception e) {
				logger.log(IAppLogger.ERROR, "Error Updating Profile", e);
		}
		return null;
	}
	
	/**
	 * Check if the entered Invitation code is valid. For valid IC returns student details
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/regn/validateCode", method = RequestMethod.GET)
	public @ResponseBody String validateCode(HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		logger.log(IAppLogger.INFO, "Validating Invitation Code");

		try {
			List<ParentTO> parentToList = new ArrayList<ParentTO>();
			String invitationCode = req.getParameter("invitationCode");
			String fromLogin = req.getParameter("fromLogin");
			
			//Fix for TD 78161 - By Joy
			UserTO loggedinUserTO = null;
			if(req.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS)!= null && fromLogin.equals("N")){ //formLogin patch is required for browser shared session issue
				loggedinUserTO = (UserTO) req.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
			}
			
			Map<String,Object> paramMap = new HashMap<String,Object>(); 
			paramMap.put("loggedinUserTO", loggedinUserTO);
			paramMap.put("invitationCode", invitationCode);
			
			// validate ic
			ParentTO parentTO = parentService.validateIC(paramMap);
			
			if(parentTO != null) {
				parentToList.add(parentTO);
				String jsonString = JsonUtil.convertToJsonAdmin(parentToList);
				res.setContentType("application/json");
				res.getWriter().write(jsonString);
				//res.getWriter().write( "{\"status\":\"Success\"}" );
				logger.log(IAppLogger.DEBUG,jsonString);
			}
			
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Validating Invitation Code");
		}
		return null;
	}
	
	/**
	 * Check if entered username is available
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/regn/checkusername", method = RequestMethod.GET)
	public @ResponseBody String checkAvailability(HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		logger.log(IAppLogger.INFO, "Validating Invitation Code");

		try {
			String username = req.getParameter("username");
			// check username availability
			if(parentService.checkUserAvailability(username)) {
				res.setContentType("application/json");
				res.getWriter().write( "{\"status\":\"Success\", \"available\":\""+"true"+"\"}" );				
			}
			else {
				res.setContentType("application/json");
				res.getWriter().write( "{\"status\":\"Failure\", \"available\":\""+"false"+"\"}" );					
			}
	
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Validating Invitation Code");
		}
		return null;
	}
	
	
	
	
	/**
	 * Retrieves the children details of the logged in parent in order to display it in the home page
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value="/getChildrenList", method=RequestMethod.GET)
	public String getChildrenList(HttpServletRequest req, HttpServletResponse res ) {
		logger.log(IAppLogger.INFO, "Enter: Retrieve children details");
		String jsonString = null;
		String parentName=null;
		String clickedTreeNode=null;
		String orgId = null;
		Map<String,Object> paramChildMap = new HashMap<String,Object>();
		
		try {
			String orgMode = (String)req.getSession().getAttribute(IApplicationConstants.ORG_MODE);
			String adminYear = (String) req.getParameter("AdminYear");
			String isPN = (String) req.getParameter("isPN");
			
			if(adminYear == null){
				com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) 
						req.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
				Map<String,Object> paramMap = new HashMap<String,Object>(); 
				paramMap.put("loggedinUserTO", loggedinUserTO);
				
				adminYear = (String) req.getSession().getAttribute(IApplicationConstants.ADMIN_YEAR);
				if(adminYear == null) {
					List<com.ctb.prism.core.transferobject.ObjectValueTO> customerProductList = adminService.getCustomerProduct(paramMap);
					for(com.ctb.prism.core.transferobject.ObjectValueTO object : customerProductList) {
						adminYear = object.getValue();
						break;
					}
				}
			}else{
				req.getSession().setAttribute(IApplicationConstants.ADMIN_YEAR, adminYear);
			}
			
			 parentName = (String)req.getParameter("parentName");
			 orgId=(String)req.getParameter("orgId");
			//this check is to determine whether the parentName is retrieved from manage parent (view children)
			// or from the PN login page
			if (parentName == null || parentName.trim().length()<0){
				parentName =(String) req.getSession().getAttribute(IApplicationConstants.CURRUSER);
			}
			
			clickedTreeNode=(String)req.getParameter("clickedTreeNode");
			if (clickedTreeNode == null || clickedTreeNode.trim().length()<0){
				clickedTreeNode =(String) req.getSession().getAttribute(IApplicationConstants.CURRORG);
			}else{
				req.getSession().setAttribute(IApplicationConstants.CURRORG, clickedTreeNode);
			}
			if (orgId == null || orgId.trim().length()<0) {
				orgId = clickedTreeNode;
			}
			
			if(parentName!= null){	 
				paramChildMap.put("parentName", parentName);
				paramChildMap.put("clickedTreeNode", orgId);
				paramChildMap.put("adminYear", adminYear);
				paramChildMap.put("orgMode", orgMode);
				paramChildMap.put("isPN", isPN);
				
				List<StudentTO> childrenList = parentService.getChildrenList(paramChildMap);
				if ( childrenList != null ){
					jsonString = JsonUtil.convertToJsonAdmin(childrenList);
					logger.log(IAppLogger.DEBUG,"View Studnet in Parent Search.............");
					logger.log(IAppLogger.DEBUG,jsonString);
					res.setContentType("application/json");
					res.getWriter().write(jsonString);
				}
				
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO,	"Exit: Retrieve children details");
		}
		return null;
	}
	
	/**
	 * Validating the password
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "/validatePwd", method = RequestMethod.GET)
	public ModelAndView validatePwd(HttpServletRequest req,
			HttpServletResponse res) {
		try {
			logger.log(IAppLogger.INFO, "Enter: ParentController - validatePwd");
			String password = (String) req.getParameter("password");
			String username = (String) req.getParameter("username");
			String status = "Success";
			
			if(password!=null) {
				if (password.equals(username)) {
					res.setContentType("text/plain");
					status="equalsUserName";
					res.getWriter().write("{\"status\":\"" + status + "\"}");
					return null;
				} else if(!Utils.validatePassword(password)) {
					res.setContentType("text/plain");
					status="invalidPwd";
					res.getWriter().write("{\"status\":\"" + status + "\"}");
					return null;
				} else {
					Random randomGenerator = new Random();
					int randomInt = randomGenerator.nextInt(100);
					String tempUsername = username + randomInt;
					try {
						if(IApplicationConstants.APP_LDAP.equals(propertyLookup.get("app.auth"))) {
							// check if the password is accepted by LDAP
							ldapManager.addUser(tempUsername, tempUsername, tempUsername, password);
							
							// delete user - if insert was successful
							ldapManager.deleteUser(tempUsername, tempUsername, tempUsername);
						}
					} catch (BusinessException bex) {
						logger.log(IAppLogger.ERROR, "Password is not accepted by LDAP", bex);
						res.setContentType("text/plain");
						res.getWriter().write("{\"status\":\"LDAP_ERROR\", \"message\":\""+bex.getCustomExceptionMessage()+"\"}");
						return null;
					} 
				}
			}	
	
			res.getWriter().write("{\"status\":\"" + status + "\"}");

			logger.log(IAppLogger.INFO, "Exit: ParentController - validatePwd");

		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error performing validation on password", e);
		}
		return null;
	}
		
}
