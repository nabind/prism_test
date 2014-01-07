package com.ctb.prism.web.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.lingala.zip4j.core.ZipFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ctb.prism.admin.service.IAdminService;
import com.ctb.prism.admin.transferobject.EduCenterTO;
import com.ctb.prism.admin.transferobject.ObjectValueTO;
import com.ctb.prism.admin.transferobject.OrgTO;
import com.ctb.prism.admin.transferobject.OrgTreeTO;
import com.ctb.prism.admin.transferobject.RoleTO;
import com.ctb.prism.admin.transferobject.StudentDataTO;
import com.ctb.prism.admin.transferobject.UserDataTO;
import com.ctb.prism.admin.transferobject.UserTO;
import com.ctb.prism.admin.util.StudentDataUtil;
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.FileUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.inors.constant.InorsDownloadConstants;
import com.ctb.prism.inors.util.InorsDownloadUtil;
import com.ctb.prism.parent.service.IParentService;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.StudentTO;
import com.ctb.prism.web.util.JsonUtil;

/**
 * @author TCS
 * 
 */
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Controller
public class AdminController {
	private static final IAppLogger logger = LogFactory
			.getLoggerInstance(AdminController.class.getName());

	@Autowired
	private IAdminService adminService;
	
	@Autowired
	private IParentService parentService;
	
	@Autowired
	private IPropertyLookup propertyLookup;
	
	boolean isTree =true;//set it to false for slide menu

	/**
	 * Entry method for manage organizations screen
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/manageOrganizations", method = RequestMethod.GET)
	public ModelAndView getOrganizations(HttpServletRequest request,
			HttpServletResponse response) {
		
		
		logger.log(IAppLogger.INFO, "Enter: AdminController - getOrganizations");
		
		List<OrgTreeTO> OrgTreeTOs = new ArrayList<OrgTreeTO>();
		List<OrgTO> organizationList = new ArrayList<OrgTO>();
		ModelAndView modelAndView = new ModelAndView("admin/organizations");
		String currentOrg =(String) request.getSession().getAttribute(IApplicationConstants.CURRORG);
		String orgJsonString;
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		long currCustomer = (customer == null)? 0 : Long.valueOf(customer);
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("loggedinUserTO", loggedinUserTO);
		try {
			String adminYear = (String) request.getParameter("AdminYear");
			if (currentOrg != null) {
				List<com.ctb.prism.core.transferobject.ObjectValueTO> customerProductList = adminService.getCustomerProduct(paramMap);
				if(adminYear == null) {
					adminYear = (String) request.getSession().getAttribute(IApplicationConstants.ADMIN_YEAR);
					if(adminYear == null) {
						for(com.ctb.prism.core.transferobject.ObjectValueTO object : customerProductList) {
							adminYear = object.getValue();
							break;
						}
					}
				}
				paramMap.put("nodeid", currentOrg);
				paramMap.put("currOrg", currentOrg);
				paramMap.put("isFirstLoad", true);
				paramMap.put("adminYear", adminYear);
				paramMap.put("customerId", currCustomer);
				paramMap.put("orgMode", orgMode);
				if(isTree){
					OrgTreeTOs =adminService.getOrgTree(paramMap);
					//organizationList= adminService.getOrganizationList(currentOrg,adminYear);
					modelAndView.addObject("organizationTreeList", OrgTreeTOs);
					modelAndView.addObject("organizationList", organizationList);
					modelAndView.addObject("treeSturcture", "Yes");
					orgJsonString = JsonUtil.convertToJsonAdmin(OrgTreeTOs);
					modelAndView.addObject("orgName", OrgTreeTOs.get(0).getData());
					modelAndView.addObject("rootOrgId", OrgTreeTOs.get(0).getMetadata().getParentTenantId());
					modelAndView.addObject("rootOrgLevel", OrgTreeTOs.get(0).getMetadata().getOrgLevel());
					logger.log(IAppLogger.DEBUG, "MANAGE ORGANIZATION TREE STRUCTURE ON LOAD...................");
					logger.log(IAppLogger.DEBUG, orgJsonString);
				}
				else{
						organizationList= adminService.getOrganizationList(currentOrg, adminYear, currCustomer);
						modelAndView.addObject("organizationList", organizationList);
						modelAndView.addObject("orgName", organizationList.get(0).getTenantName());	
					
				}
				modelAndView.addObject("adminList", customerProductList);
			}
					
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO,
					"Exit: AdminController - getOrganizations");
		}
		modelAndView.addObject("PDCT_NAME",propertyLookup.get("PDCT_NAME"));
		return modelAndView;
	}

	/**
	 * Method retrieves the organization children when clicked on a organization
	 * node. This method is called through AJAX
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/orgChildren", method = RequestMethod.GET)
	public String getOrgChildren(HttpServletRequest request,
			HttpServletResponse response) {
		logger.log(IAppLogger.INFO, "Enter: AdminController - getOrgChildren");
		List<OrgTreeTO> OrgTreeTOs = new ArrayList<OrgTreeTO>();
		List<OrgTO> children= new ArrayList<OrgTO>();
		String jsonString = null;
		String nodeid= request.getParameter("tenantId");
		String searchParam= request.getParameter("searchParam");
		if("Search".equals(searchParam)) searchParam = "";
		
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		long currCustomer = (customer == null)? 0 : Long.valueOf(customer);
		try {
			if (nodeid != null)	{
				String adminYear = (String) request.getParameter("AdminYear");
		
				/*if(isTree){
					OrgTreeTOs = adminService.getOrgTree(nodeid,false);
					logger.log(IAppLogger.DEBUG, "MANAGE ORGANIZATION TREE STURCTURE.................");
					jsonString = JsonUtil.convertToJsonAdmin(OrgTreeTOs);
					logger.log(IAppLogger.DEBUG, jsonString);
					}
				else{*/
					children = adminService.getOrganizationChildren(nodeid, adminYear, searchParam, currCustomer);
					if ( children != null )	{
						jsonString = JsonUtil.convertToJsonAdmin(children);
						/*}*/

						response.setContentType("application/json");
						response.getWriter().write(jsonString);
					}
			}
			
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO,
					"Exit: AdminController - getOrgChildren");
		}

		return null;
	}
	/**
	 * Method retrieves the number of users for an organization when clicked on the view users button.
	 *  This method is called through AJAX
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getUserCount", method = RequestMethod.GET)
	public String getUserCount(HttpServletRequest request,
			HttpServletResponse response) {
		logger.log(IAppLogger.INFO, "Enter: AdminController - getUserCount");
		OrgTO orguser= new OrgTO();
		List<OrgTO> orgUserCount= new ArrayList<OrgTO>();
		String jsonString = null;
		String tenantId= request.getParameter("tenantId");
		String adminYear= request.getParameter("adminYear");
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		long currCustomer = (customer == null)? 0 : Long.valueOf(customer);
		try {
			if (tenantId != null && adminYear != null)	{
								
				orguser = adminService.getTotalUserCount(tenantId, adminYear,currCustomer);
					if ( orguser != null )	{
						orgUserCount.add(orguser);
						jsonString = JsonUtil.convertToJsonAdmin(orgUserCount);
						
						response.setContentType("application/json");
						response.getWriter().write(jsonString);
					}
			}
			
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO,
					"Exit: AdminController - getUserCount");
		}
		
		return null;
	}

	/**
	 * Method performs search organization operation. This method is called
	 * through AJAX
	 * 
	 * @param req
	 * @param res
	 * @return Returns nothing, instead writes search data in response as JSON
	 */
	@RequestMapping(value = "/searchOrganization", method = RequestMethod.GET)
	public String searchOrganization(HttpServletRequest req,
			HttpServletResponse res) {
		logger.log(IAppLogger.INFO,
				"Enter: AdminController - searchOrganization");
		try {
			String adminYear = (String) req.getParameter("AdminYear");
			String customer = (String) req.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			long currCustomer = (customer == null)? 0 : Long.valueOf(customer);
			/*List<OrgTO> orgs = adminService.searchOrganization(req
					.getParameter("searchString"), (String) req.getSession()
					.getAttribute(IApplicationConstants.CURRORG));*/
			List<OrgTO> orgs = adminService.searchOrganization(req.getParameter("searchString")
					, req.getParameter("selectedOrg"), adminYear,currCustomer);
			if (orgs != null) {
				String jsonString = JsonUtil.convertToJsonAdmin(orgs);

				res.setContentType("application/json");
				res.getWriter().write(jsonString);
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO,
					"Exit: AdminController - searchOrganization");
		}
		return null;
	}

	/**
	 * Performs search organization operation for auto complete feature in the
	 * search box. This method is called through AJAX
	 * 
	 * @param req
	 * @param res
	 * @return Returns nothing, instead writes search data in response as a JSON
	 *         string
	 */
	@RequestMapping(value = "/searchOrgAutoComplete", method = RequestMethod.GET)
	public String searchOrgAutoComplete(HttpServletRequest req,
			HttpServletResponse res) {
		logger.log(IAppLogger.INFO,
				"Enter: AdminController - searchOrgAutoComplete");
		try {
			String adminYear = (String) req.getParameter("AdminYear");
			String customer = (String) req.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			long currCustomer = (customer == null)? 0 : Long.valueOf(customer);
			/*String orgs = adminService.searchOrgAutoComplete(req
					.getParameter("term"), (String) req.getSession()
					.getAttribute(IApplicationConstants.CURRORG));*/
			String orgs = adminService.searchOrgAutoComplete(req.getParameter("term")
					, req.getParameter("selectedOrg"), adminYear, currCustomer);
			if (orgs != null) {
				res.setContentType("application/json");
				res.getWriter().write(orgs);
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO,
					"Exit: AdminController - searchOrgAutoComplete");
		}
		return null;
	}

	/**
	 * Show list of all users
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userModule", method = RequestMethod.GET)
	public ModelAndView manageUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		request.getSession().setAttribute(IApplicationConstants.LOGIN_AS, IApplicationConstants.ACTIVE_FLAG);
		
		List<OrgTO> OrgTOs = new ArrayList<OrgTO>();
		List<UserTO> UserTOs = new ArrayList<UserTO>();
		List<OrgTreeTO> OrgTreeTOs = new ArrayList<OrgTreeTO>();
		String orgJsonString;
		ModelAndView modelAndView = new ModelAndView("admin/users");
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		long currCustomer = (customer == null)? 0 : Long.valueOf(customer);
		String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("loggedinUserTO", loggedinUserTO);

		try {
			logger.log(IAppLogger.INFO, "Enter: AdminController - manageUser");
			String currentOrg = (String) request.getSession().getAttribute(
					IApplicationConstants.CURRORG);
			String adminYear = (String) request.getParameter("AdminYear");
			//List<ObjectValueTO> adminList = adminService.getAllAdmin();
			List<com.ctb.prism.core.transferobject.ObjectValueTO> customerProductList = adminService.getCustomerProduct(paramMap);
			if(adminYear == null) {
				adminYear = (String) request.getSession().getAttribute(IApplicationConstants.ADMIN_YEAR);
				if(adminYear == null) {
					for(com.ctb.prism.core.transferobject.ObjectValueTO object : customerProductList) {
						adminYear = object.getValue();
						break;
					}
				}
			}
			if (currentOrg != null) {
				if(isTree)
				{
					paramMap.put("nodeid", currentOrg);
					paramMap.put("currOrg", currentOrg);
					paramMap.put("isFirstLoad", true);
					paramMap.put("adminYear", adminYear);
					paramMap.put("customerId", currCustomer);
					paramMap.put("orgMode", orgMode);
					OrgTreeTOs = adminService.getOrganizationTree(paramMap);
					/*OrgTreeTOs =adminService.getOrganizationTree(currentOrg,currentOrg,true,adminYear,currCustomer, "");*/
					modelAndView.addObject("organizationTreeList", OrgTreeTOs);
					modelAndView.addObject("treeSturcture", "Yes");
					orgJsonString = JsonUtil.convertToJsonAdmin(OrgTreeTOs);
					modelAndView.addObject("orgName", OrgTreeTOs.get(0).getData());
					modelAndView.addObject("rootOrgId", OrgTreeTOs.get(0).getMetadata().getParentTenantId());
					modelAndView.addObject("rootOrgLevel", OrgTreeTOs.get(0).getMetadata().getOrgLevel());
					logger.log(IAppLogger.DEBUG, "TREE STRUCTURE ON LOAD...................");
					logger.log(IAppLogger.DEBUG, orgJsonString);
				}
				else
				{
					OrgTOs = adminService.getOrganizationDetailsOnFirstLoad(currentOrg);
					modelAndView.addObject("organizationList", OrgTOs);
					modelAndView.addObject("treeSturcture", "No");
					modelAndView.addObject("orgName", OrgTOs.get(0).getTenantName());
				}
				/*UserTOs = adminService.getUserDetailsOnClick(currentOrg,currentOrg,adminYear);
				modelAndView.addObject("userList", UserTOs);*/
				modelAndView.addObject("adminList", customerProductList);
			}
		
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: AdminController - manageUser");
		}
		modelAndView.addObject("PDCT_NAME",propertyLookup.get("PDCT_NAME"));
		return modelAndView;

	}

	/**
	 * Fetch all organizations for left menu
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/tenantHierarchy", method = RequestMethod.GET)
	public @ResponseBody
	String getTenantHierarchy(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		List<OrgTO> OrgTOs = new ArrayList<OrgTO>();
		List<OrgTreeTO> OrgTreeTOs = new ArrayList<OrgTreeTO>();
		String orgJsonString;
		String currentOrg = (String) request.getSession().getAttribute(
				IApplicationConstants.CURRORG);
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		long currCustomer = (customer == null)? 0 : Long.valueOf(customer);
		String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
		
		try {
			logger.log(IAppLogger.INFO,
					"Enter: AdminController - tenantHierarchy");
			String nodeid = (String) request.getParameter("tenantId");
			String adminYear = (String) request.getParameter("AdminYear");
			
			Map<String,Object> paramMap = new HashMap<String,Object>(); 
			paramMap.put("nodeid", nodeid);
			paramMap.put("currOrg", currentOrg);
			paramMap.put("isFirstLoad", false);
			paramMap.put("adminYear", adminYear);
			paramMap.put("customerId", currCustomer);
			paramMap.put("orgMode", orgMode);
			
			if(isTree)
			{
				if (nodeid != null) {
					OrgTreeTOs = adminService.getOrganizationTree(paramMap);
				}
				 orgJsonString = JsonUtil.convertToJsonAdmin(OrgTreeTOs);
			}
			else
			{
				if (nodeid != null) {
					OrgTOs = adminService.getOrganizationDetailsOnClick(paramMap);
				}
				 orgJsonString = JsonUtil.convertToJsonAdmin(OrgTOs);
			}
			

			
			logger.log(IAppLogger.DEBUG, "TREE STURCTURE.................");
			logger.log(IAppLogger.DEBUG, orgJsonString);
			response.setContentType("application/json");
			response.getWriter().write(orgJsonString);
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO,
					"Exit: AdminController - tenantHierarchy");
		}

		return null;

	}

	/**
	 * Fetching all users for selected organization
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getUserDetails", method = RequestMethod.GET)
	public @ResponseBody
	String userDetails(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<UserTO> UserTOs = new ArrayList<UserTO>();
		String currentOrg = (String) request.getSession().getAttribute(
				IApplicationConstants.CURRORG);
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("loggedinUserTO", loggedinUserTO);
		
		//List<ObjectValueTO> adminList = null;
		try {
			logger.log(IAppLogger.INFO, "Enter: AdminController - userDetails");
			String customerid = (String)request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			String nodeId = (String) request.getParameter("tenantId");
			String adminYear = (String) request.getParameter("AdminYear");
			
			if(nodeId != null && adminYear == null){				
				   	adminYear = (String) request.getSession().getAttribute(IApplicationConstants.ADMIN_YEAR);
					if(adminYear == null) {
						//adminList = adminService.getAllAdmin();	
						List<com.ctb.prism.core.transferobject.ObjectValueTO> customerProductList = adminService.getCustomerProduct(paramMap);
						for(com.ctb.prism.core.transferobject.ObjectValueTO object : customerProductList) {
							adminYear = object.getValue();
							break;
						}
					}
			}
			String searchParam = (String) request.getParameter("searchParam");
			if("Search".equals(searchParam)) searchParam = "";
			logger.log(IAppLogger.INFO, "Node ID: " + nodeId);
			if (nodeId != null) {
				if(currentOrg.equals(nodeId) ){
					UserTOs = adminService.getUserDetailsOnClick(nodeId, currentOrg, adminYear, searchParam ,customerid);
				} else if (nodeId.indexOf("_") > 0){
					UserTOs = adminService.getUserDetailsOnClick(nodeId,  nodeId.substring(0, nodeId.indexOf("_")), adminYear, searchParam 
							,customerid);
				} else{
					UserTOs = adminService.getUserDetailsOnClick(nodeId, nodeId, adminYear, searchParam ,customerid);
				}
			}
			logger.log(IAppLogger.INFO, "Users: " + UserTOs.size());
			String userJsonString = JsonUtil.convertToJsonAdmin(UserTOs);
			
			logger.log(IAppLogger.INFO, userJsonString);
			response.setContentType("application/json");

			// response.getWriter().write( orgJsonString + userJsonString );
			response.getWriter().write(userJsonString);
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: AdminController - userDetails");
		}

		return null;

	}

	/**
	 * Get selected user info with associated roles
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getEditUserData", method = RequestMethod.GET)
	public @ResponseBody
	String editUserData(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		UserTO userTo = new UserTO();
		List<UserTO> UserTOs = new ArrayList<UserTO>();
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		try {
			logger.log(IAppLogger.INFO, "Enter: AdminController - getEditUserData");
			String nodeId = (String) request.getParameter("tenantId");
			String purpose = (String) request.getParameter("purpose");
			String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			
			paramMap.put("nodeId", nodeId);
			paramMap.put("customer", customer);
			paramMap.put("purpose", purpose);
			
			if (nodeId != null) {
				userTo = adminService.getEditUserData(paramMap);
			}
			UserTOs.add(userTo);

			String userJsonString = JsonUtil.convertToJsonAdmin(UserTOs);
			logger.log(IAppLogger.INFO, userJsonString);
			response.setContentType("application/json");

			// response.getWriter().write( orgJsonString + userJsonString );
			response.getWriter().write(userJsonString);
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: AdminController - getEditUserData");
		}

		return null;

	}

	/**
	 * Update selected user
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public ModelAndView updateUser(HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		try {
			logger.log(IAppLogger.INFO, "Enter: AdminController - updateUser");

			String Id = (String) req.getParameter("Id");
			logger.log(IAppLogger.INFO, "Id................................." + Id);
			String userName = (String) req.getParameter("userName");
			String userId = (String) req.getParameter("userId");
			String emailId = (String) req.getParameter("emailId") != null ? (String) req
					.getParameter("emailId") : "";
			String password = (String) req.getParameter("password");
			String userStatus = (String) req.getParameter("userStatus");
			if ("on".equals(userStatus)) {
				userStatus = IApplicationConstants.ACTIVE_FLAG;
			} else {
				userStatus = IApplicationConstants.INACTIVE_FLAG;
			}
			String[] userRoles = req.getParameterValues("userRole");
			String status = "Fail";
			
			if(password!=null && password.trim().length() > 0) {
				if (password.equals(userId)) {
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
	
					boolean isSaved = adminService.updateUser(Id, userId, userName,
							emailId, password, userStatus, userRoles);
					res.setContentType("text/plain");
					
					if (isSaved) {
						status = "Success";
					}
					res.getWriter().write("{\"status\":\"" + status + "\"}");

			logger.log(IAppLogger.INFO, "Exit: AdminController - updateUser");

		} catch (BusinessException bex) {
			logger.log(IAppLogger.ERROR, "Error Saving User", bex);
			res.setContentType("text/plain");
			res.getWriter().write("{\"status\":\"LDAP_ERROR\", \"message\":\""+bex.getCustomExceptionMessage()+"\"}");
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error Saving File", e);
		}
		return null;
	}
	
	/**
	 * Get role based on org_level
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getRoleOnAddUser", method = RequestMethod.GET)
	public @ResponseBody
	String getRoleOnAddUser(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		List<RoleTO> RoleTOs = new ArrayList<RoleTO>();
		try {
			logger.log(IAppLogger.INFO,
					"Enter: AdminController - getRoleOnAddUser");
			
			String orgLevel = (String) request.getParameter("orgLevel");
			String purpose = (String) request.getParameter("purpose");
			
			if(IApplicationConstants.PURPOSE.equals(purpose)){
				RoleTO masterRoleTO = new RoleTO();
				masterRoleTO.setRoleName(IApplicationConstants.ROLE_TYPE.ROLE_USER.toString());
				masterRoleTO.setRoleId(IApplicationConstants.ROLE_USER_ID);
				masterRoleTO.setDefaultSelection("selected");
				RoleTOs.add(masterRoleTO);
				
				masterRoleTO = new RoleTO();
				masterRoleTO.setRoleName(IApplicationConstants.ROLE_TYPE.ROLE_ADMIN.toString());
				masterRoleTO.setRoleId(IApplicationConstants.ROLE_ADMIN_ID);
				RoleTOs.add(masterRoleTO);
			}else{
				if (orgLevel != null) {
					String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
					RoleTOs = adminService.getRoleOnAddUser(orgLevel, customer);
				}
			}
			
			String userJsonString = JsonUtil.convertToJsonAdmin(RoleTOs);
			logger.log(IAppLogger.INFO, userJsonString);
			response.setContentType("application/json");

			response.getWriter().write(userJsonString);
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO,
					"Exit: AdminController - getRoleOnAddUser");
		}

		return null;
	}

	/**
	 * Add new user to selected org
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/addNewUser", method = RequestMethod.POST)
	public ModelAndView addNewUser(HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		try {
			logger.log(IAppLogger.INFO, "Enter: AdminController - addNewUser");
			UserTO userTo = null;
			List<UserTO> UserTOs = new ArrayList<UserTO>();
			String adminYear = (String) req.getParameter("AdminYear");
			String tenantId = (String) req.getParameter("tenantId");
			String userName = (String) req.getParameter("userName");
			String userId = (String) req.getParameter("userId");
			String emailId = (String) req.getParameter("emailId") != null ? (String) req.getParameter("emailId") : "";
			String password = (String) req.getParameter("password");
			String userStatus = (String) req.getParameter("userStatus");
			String orgLevel = (String) req.getParameter("orgLevel");
			String purpose = (String) req.getParameter("purpose")!= null ? (String) req.getParameter("purpose") : "";
			String eduCenterId = (String) req.getParameter("eduCenterId")!= null ? (String) req.getParameter("eduCenterId") : "";
			
			//userId stands for username in DB and userName stands for display_username in DB
			Map<String,Object> paramMap = new HashMap<String,Object>(); 
			paramMap.put("adminYear", adminYear);
			paramMap.put("tenantId", tenantId);
			paramMap.put("userName", userId);
			paramMap.put("userDisplayName", userName);
			paramMap.put("emailId", emailId);
			paramMap.put("password", password);
			paramMap.put("orgLevel", orgLevel);
			paramMap.put("purpose", purpose);
			paramMap.put("eduCenterId", eduCenterId);
			
			if (IApplicationConstants.CHECKED_CHECKBOX_VALUE.equals(userStatus)) {
				userStatus = IApplicationConstants.ACTIVE_FLAG;
			} else {
				userStatus = IApplicationConstants.INACTIVE_FLAG;
			}
			
			String[] userRoles = req.getParameterValues("userRole");
			String customer = (String) req.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			paramMap.put("userStatus", userStatus);
			paramMap.put("userRoles", userRoles);
			paramMap.put("customer", customer);
			
			String status = "Fail";
						
			if(password!=null && password.trim().length() > 0)
			{
				if (password.equals(userId)) {
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
			
			/*userTo = adminService.addNewUser(userId, tenantId, userName,
							emailId, password, userStatus, userRoles,orgLevel, adminYear, customer);*/
			userTo = adminService.addNewUser(paramMap);
					res.setContentType("text/plain");
			
			if (userTo != null) {
				UserTOs.add(userTo);
				String userJsonString = JsonUtil.convertToJsonAdmin(UserTOs);
				res.getWriter().write(userJsonString);
			} else {
				status = "Faliure";
				res.getWriter().write("{\"status\":\"" + status + "\"}");
			}
					
						
			logger.log(IAppLogger.INFO, "Exit: AdminController - addNewUser");

		} catch (BusinessException bex) {
			logger.log(IAppLogger.ERROR, "Error Saving User", bex);
			res.setContentType("text/plain");
			res.getWriter().write("{\"status\":\"LDAP_ERROR\", \"message\":\""+bex.getCustomExceptionMessage()+"\"}");
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error Saving User", e);
		}
		return null;
	}
	/**
	 * delete User from Ldap and database
	 * @param req
	 * @param res
	 * @return
	 */

	@RequestMapping(value="/deleteUser", method=RequestMethod.GET )
	public ModelAndView deleteUser(HttpServletRequest req, HttpServletResponse res) {
		try {
			logger.log(IAppLogger.INFO, "Enter: AdminController - deleteUser");


			String Id = (String)req.getParameter("Id");
			logger.log(IAppLogger.INFO, "Id of the user to delete is ........................."+Id);
			String userName = (String)req.getParameter("userName");
			String password = (String)req.getParameter("password");
			String purpose = (String)req.getParameter("purpose");		
			
			Map<String,Object> paramMap = new HashMap<String,Object>(); 
			paramMap.put("userName", userName);
			paramMap.put("password", password);
			paramMap.put("Id", Id);
			paramMap.put("purpose", purpose);
			boolean isDeleted = adminService.deleteUser(paramMap);
			//Arunava
			res.setContentType("text/plain");
			String status = "Fail";
			if(isDeleted) {
				status = "Success";
			}
			res.getWriter().write( "{\"status\":\""+status+"\"}" );

			logger.log(IAppLogger.INFO, "Exit: AdminController - deleteUser");

		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error Saving File", e);
		}
		return null;
	}
	
	/**
	 * Method performs search user operation. This method is called through AJAX
	 * @param req
	 * @param res
	 * @return Returns nothing, instead writes search data in response as JSON
	 */
	@RequestMapping(value="/searchUser", method=RequestMethod.GET)
	public String searchUser(HttpServletRequest req, HttpServletResponse res ) {
		logger.log(IAppLogger.INFO, "Enter: AdminController - searchUser");
		try {
			String adminYear = (String) req.getParameter("AdminYear");
			String isExactSearch = (String) req.getParameter("isExactSearch");
			if (isExactSearch == null || IApplicationConstants.FLAG_N.equalsIgnoreCase(isExactSearch)){
				isExactSearch=IApplicationConstants.FLAG_N;
			}else{
				isExactSearch=IApplicationConstants.FLAG_Y;
			}
			
			
				
		    String purpose = (String) req.getParameter("purpose");
			
			if(IApplicationConstants.PURPOSE.equals(purpose)){
				
				Map<String,Object> paramMap = new HashMap<String,Object>(); 
				paramMap.put("userName", req.getParameter("username"));
				paramMap.put("tenantId", req.getParameter("eduCenterId"));
				paramMap.put("isExactSearch",isExactSearch);
				
				List<EduCenterTO> users = adminService.searchEduUser(paramMap);
				List<UserTO> userTOList = convertEduToUserList(users);
				
				if(users != null) {
					String userJsonString = JsonUtil.convertToJsonAdmin(userTOList);
					logger.log(IAppLogger.INFO, "For Searh Education USER Json.......................");
					logger.log(IAppLogger.INFO, userJsonString);
					res.setContentType("application/json");
					res.getWriter().write( userJsonString );
				}
			}else {
				List<UserTO> users = adminService.searchUser(req.getParameter("username"), 
					req.getParameter("selectedOrg"), adminYear,isExactSearch);
				
				if ( users != null ) {
					String userJsonString = JsonUtil.convertToJsonAdmin(users);
					logger.log(IAppLogger.INFO, "For Searh USER Json.......................");
					logger.log(IAppLogger.INFO, userJsonString);
					res.setContentType("application/json");
					res.getWriter().write( userJsonString  );
				}
			}

		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: AdminController - searchUser");
		}
		return null;
			
	}	
	
	/**
	 * Performs search user operation for auto complete feature in the search box.
	 * This method is called through AJAX
	 * @param req
	 * @param res
	 * @return Returns nothing, instead writes search data in response as a JSON string
	 */
	@RequestMapping(value="/searchUserAutoComplete", method=RequestMethod.GET)
	public String searchUserAutoComplete( HttpServletRequest req, HttpServletResponse res) {
		logger.log(IAppLogger.INFO, "Enter: AdminController - searchUserAutoComplete");
		try {
			String adminYear = (String) req.getParameter("AdminYear");
			String term = (String) req.getParameter("term");
			String selectedOrg = (String) req.getParameter("selectedOrg");
			String purpose = (String) req.getParameter("purpose");
			
			if(IApplicationConstants.PURPOSE.equals(purpose)){
				selectedOrg = (String) req.getParameter("eduCenterId");
			}
			
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("adminYear",adminYear);
			paramMap.put("term",term);
			paramMap.put("selectedOrg",selectedOrg);
			paramMap.put("purpose",purpose);
			
			String users = adminService.searchUserAutoComplete(paramMap);
			logger.log(IAppLogger.INFO, "CURRUSER.............");
			logger.log(IAppLogger.INFO, (String)req.getSession().getAttribute(IApplicationConstants.CURRORG));
			if ( users != null ) {
				res.setContentType("application/json");
				res.getWriter().write(users);
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: AdminController - searchUserAutoComplete");
		}
		return null;
	}
		
	/**
	 * Open screen with list of roles
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ROLE_CTB')")
	@RequestMapping(value = "/manageRole", method = RequestMethod.GET)
	public ModelAndView manageRole(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		
		List<RoleTO> roleList = new ArrayList<RoleTO>();
		ModelAndView modelAndView = null;
		//List<BaseTO> OrgTOs = new ArrayList<BaseTO>();
		try {
		logger.log(IAppLogger.INFO, "Enter: AdminController - manageRole");
	
			roleList = adminService.getRoleDetails();
	
		
		modelAndView = new ModelAndView("admin/manageRoles");
		modelAndView.addObject("roleList", roleList);
		modelAndView.addObject("test", "Test");
		modelAndView.addObject("message", "Hi");
		modelAndView.addObject("PDCT_NAME",propertyLookup.get("PDCT_NAME"));
		}
		catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: AdminController - manageRole");
		}

		return modelAndView;
		
	}
	
	/**
	 * Fetch role and associated user details for selected role
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ROLE_CTB')")
	@RequestMapping(value = "/editRole", method = RequestMethod.GET)
	public @ResponseBody String editRole(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		
		logger.log(IAppLogger.INFO, "Enter: AdminController - editRole");
		List<RoleTO> roleList = new ArrayList<RoleTO>();
		RoleTO roleTo = new RoleTO();
		
		String currentOrg = (String) request.getSession().getAttribute(
				IApplicationConstants.CURRORG);
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
	
		try {
		String roleId = (String)request.getParameter("roleId");
		//String roleId = "2000005";
		if (roleId != null) {
			roleTo = adminService.getRoleDetailsById(roleId, currentOrg,  customer);
			roleList.add(roleTo);
		}
		
		String roleJsonString = JsonUtil.convertToJsonAdmin(roleList);
		response.setContentType("application/json");
		response.getWriter().write( roleJsonString  );
		//response.getWriter().write( "{\"status\":\"Success\"\"}" );
				}
		catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: AdminController - editRole");
		}
		return null;
	}

	/**
	 * Method for updating a role
	 * @param request
	 * @param response
	 * @return
	 */
	@PreAuthorize("hasAuthority('ROLE_CTB')")
	@RequestMapping(value="/updateRole", method=RequestMethod.POST )
	public ModelAndView updateRole(HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.log(IAppLogger.INFO, "Enter: AdminController - updateRole");

			
			boolean isSaved = false;

			String roleId = (String) request.getParameter("roleId");
			String roleName = (String) request.getParameter("roleName");
			String roleDescription = (String) request.getParameter("roleDescription");
			String status= "Fail";
			logger.log(IAppLogger.INFO, "roleId---roleName---roleDescription"+roleId+"---"+roleName+"---"+roleDescription
					);
			
			isSaved= adminService.saveRole(roleId, roleName, roleDescription);
			response.setContentType("text/plain");
			if (isSaved) {
				status="Success";
				
			}
			response.getWriter().write("{\"status\":\"" + status + "\"}");
			
			logger.log(IAppLogger.INFO, "Exit: AdminController - updateRole");
			
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error updating role details", e);
		}
		return null;
	}
	
	/**
	 * Delete selected role
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ROLE_CTB')")
	@RequestMapping(value = "/deleteRole", method = RequestMethod.GET)
	public ModelAndView deleteRole(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		
		List<RoleTO> roleList = new ArrayList<RoleTO>();
		ModelAndView modelAndView = null;
		String roleId = request.getParameter("roleId"); 
		//List<BaseTO> OrgTOs = new ArrayList<BaseTO>();
		try {
		logger.log(IAppLogger.INFO, "Enter: AdminController - deleteRole");
		String status = "Fail";
		boolean isDeleted=false;
		if (roleId != null) {
			isDeleted= adminService.deleteRole(roleId);
		}
		if(isDeleted) {
			//roleList = adminService.getRoleDetails();
			status = "Success";
		}
		response.getWriter().write( "{\"status\":\""+status+"\"}" ); 
		//modelAndView = new ModelAndView("admin/manageRoles");
		//modelAndView.addObject("roleList", roleList);
		//modelAndView.addObject("test", "Test");
		//modelAndView.addObject("status", status);
		
		}
		catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: AdminController - deleteRole");
		}
		
		return null;
		
	}
	
	/**
	 * Associate user for a role through associate button from edit role screen
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ROLE_CTB')")
	@RequestMapping(value = "/associateUser", method = RequestMethod.GET)
	public @ResponseBody String associateUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		logger.log(IAppLogger.INFO, "Enter: AdminController - associateUser");
		List<RoleTO> roleList = new ArrayList<RoleTO>();
		RoleTO roleTo = new RoleTO();
		String currentOrg = (String) request.getSession().getAttribute(
				IApplicationConstants.CURRORG);
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);

		try {
			String roleId = (String) request.getParameter("roleId");
			//String userName = (String) request.getParameter("userId");
			String userName = (String) request.getParameter("userName");
			
			if(!parentService.checkUserAvailability(userName)) {
				logger.log(IAppLogger.INFO, "****Username matched with database****");	
				if (!parentService.isRoleAlreadyTagged(roleId, userName)){
					logger.log(IAppLogger.INFO, "****Role Already Tagged****");
					if (roleId != null && userName != null) {
						boolean isSaved = adminService.associateUserToRole(roleId, userName);
						if (isSaved) {
							roleTo = adminService.getRoleDetailsById(roleId,currentOrg,customer);
							roleList.add(roleTo);
						}
					}
					String roleJsonString = JsonUtil.convertToJsonAdmin(roleList);
					response.setContentType("application/json");
					response.getWriter().write(roleJsonString);
					//response.getWriter().write( "{\"status\":\"Success\"}" );
				}
				else {
					response.setContentType("application/json");
					response.getWriter().write( "{\"status\":\"RoleAlreadyTagged\"}" );	
				}
				
			}
			else {
				response.setContentType("application/json");
				response.getWriter().write( "{\"status\":\"Fail\"}" );					
			}
			
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: AdminController - associateUser");
		}
		return null;
	}

	/**
	 * Dissociate user from a role through delete button from edit role screen
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ROLE_CTB')")
	@RequestMapping(value = "/dissociateUser", method = RequestMethod.GET)
	public @ResponseBody String dissociateUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		logger.log(IAppLogger.INFO, "Enter: AdminController - dissociateUser");
		List<RoleTO> roleList = new ArrayList<RoleTO>();
		RoleTO roleTo = new RoleTO();
		
		String currentOrg = (String) request.getSession().getAttribute(
				IApplicationConstants.CURRORG);
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);

		try {
			String roleId = (String) request.getParameter("roleId");
			String userId = (String) request.getParameter("userId");

			if (roleId != null && userId != null) {
				boolean isDeleted = adminService.deleteUserFromRole(roleId, userId);
				if (isDeleted) {
					roleTo = adminService.getRoleDetailsById(roleId,currentOrg,customer);
					roleList.add(roleTo);
				}
			}
			String roleJsonString = JsonUtil.convertToJsonAdmin(roleList);
			response.setContentType("application/json");
			response.getWriter().write(roleJsonString);
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO,
					"Exit: AdminController - dissociateUser");
		}
		return null;
	}
	
	/**
	 * Show list of all users when redirected from the manageOrganization page
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/redirectToUser", method = RequestMethod.GET)
	public ModelAndView redirectToUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		List<OrgTO> orgTOs = new ArrayList<OrgTO>();
		List<UserTO> userTOs = new ArrayList<UserTO>();
		ModelAndView modelAndView = null;
		List<OrgTreeTO> orgTreeTOs = new ArrayList<OrgTreeTO>();
		String hierarchialOrgIds = null;

		try {
			logger.log(IAppLogger.INFO, "Enter: AdminController - redirectToUser");
			String nodeId = request.getParameter("nodeId");
			String adminYear = (String) request.getParameter("AdminYear");
			String currentOrg = (String) request.getSession().getAttribute(IApplicationConstants.CURRORG);
			String userId=(String) request.getSession().getAttribute(IApplicationConstants.CURRUSERID);
			String userName=(String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
			String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			long currCustomer = (customer == null)? 0 : Long.valueOf(customer);
			String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
			com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
			Map<String,Object> paramMap = new HashMap<String,Object>(); 
			paramMap.put("loggedinUserTO", loggedinUserTO);
			if (nodeId != null) {
				modelAndView = new ModelAndView("admin/users");
				/*List<ObjectValueTO> adminList = adminService.getAllAdmin();*/
				List<com.ctb.prism.core.transferobject.ObjectValueTO> customerProductList = adminService.getCustomerProduct(paramMap);
				if(adminYear == null) {
					adminYear = (String) request.getSession().getAttribute(IApplicationConstants.ADMIN_YEAR);
					if(adminYear == null) {
						for(com.ctb.prism.core.transferobject.ObjectValueTO object : customerProductList) {
							adminYear = object.getValue();
							break;
						}
					}
				}
				modelAndView.addObject("adminList", customerProductList);
				if(isTree) {
					paramMap.put("nodeid", currentOrg);
					paramMap.put("currOrg", currentOrg);
					paramMap.put("isFirstLoad", true);
					paramMap.put("adminYear", adminYear);
					paramMap.put("customerId", currCustomer);
					paramMap.put("orgMode", orgMode);
					orgTreeTOs = adminService.getOrganizationTree(paramMap);
					/*orgTreeTOs =adminService.getOrganizationTree(currentOrg,currentOrg,true, adminYear,currCustomer, "");*/
					hierarchialOrgIds =adminService.getOrganizationTreeOnRedirect(nodeId,currentOrg,userId,currCustomer,true);
					modelAndView.addObject("hierarchialOrgIds", hierarchialOrgIds);
					//modelAndView.addObject("organizationTreeList", orgTreeTOs);
					modelAndView.addObject("treeSturcture", "Yes");
					//orgJsonString = JsonUtil.convertToJsonAdmin(orgTreeTOs);
					//modelAndView.addObject("orgName", orgTreeTOs.get(0).getData());
					modelAndView.addObject("rootOrgId", orgTreeTOs.get(0).getMetadata().getParentTenantId());
				}
				else
				{
					orgTOs = adminService.getOrganizationDetailsOnFirstLoad(currentOrg);
					modelAndView.addObject("organizationList", orgTOs);
					modelAndView.addObject("treeSturcture", "No");
					modelAndView.addObject("orgName", orgTOs.get(0).getTenantName());
				}
				
				//userTOs = adminService.getUserDetailsOnClick(nodeId);
				//modelAndView.addObject("userList", userTOs);
				//OrgTOs = adminService.getOrganizationDetailsOnClick(parentOrgId);
				//UserTOs = adminService.getUserDetailsOnClick(nodeId);
			}
			
			//modelAndView.addObject("organizationList", orgTOs);
			//modelAndView.addObject("userList", userTOs);
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: AdminController - redirectToUser");
		}

		return modelAndView;

	}
	
	/**
	 * Show list of all users
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/manageParent", method = RequestMethod.GET)
	public ModelAndView manageParent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		//List<ParentTO> parentTOs = new ArrayList<ParentTO>();
		ModelAndView modelAndView = new ModelAndView("parent/manageParent");
		List<OrgTreeTO> OrgTreeTOs = new ArrayList<OrgTreeTO>();
		String orgJsonString;
		String adminYear = (String) request.getParameter("AdminYear");
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
		long currCustomer = (customer == null)? 0 : Long.valueOf(customer);
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("loggedinUserTO", loggedinUserTO);

		try {
			logger.log(IAppLogger.INFO, "Enter: ParentController - manageParent");
			String currentOrg = (String) request.getSession().getAttribute(IApplicationConstants.CURRORG);
			if (currentOrg != null) {
				/*List<ObjectValueTO> adminList = adminService.getAllAdmin();*/
				List<com.ctb.prism.core.transferobject.ObjectValueTO> customerProductList = adminService.getCustomerProduct(paramMap);
				if(adminYear == null) {
					adminYear = (String) request.getSession().getAttribute(IApplicationConstants.ADMIN_YEAR);
					if(adminYear == null) {
						for(com.ctb.prism.core.transferobject.ObjectValueTO object : customerProductList) {
							adminYear = object.getValue();
							break;
						}
					}
				}
				modelAndView.addObject("adminList", customerProductList);
				if(isTree)
				{	
					paramMap.put("nodeid", currentOrg);
					paramMap.put("currOrg", currentOrg);
					paramMap.put("isFirstLoad", true);
					paramMap.put("adminYear", adminYear);
					paramMap.put("customerId", currCustomer);
					paramMap.put("orgMode", orgMode);
					OrgTreeTOs =adminService.getOrganizationTree(paramMap);
					modelAndView.addObject("organizationTreeList", OrgTreeTOs);
					modelAndView.addObject("treeSturcture", "Yes");
					orgJsonString = JsonUtil.convertToJsonAdmin(OrgTreeTOs);
					modelAndView.addObject("orgName", OrgTreeTOs.get(0).getData());
					modelAndView.addObject("rootOrgId", OrgTreeTOs.get(0).getMetadata().getParentTenantId());
					modelAndView.addObject("rootOrgLevel", OrgTreeTOs.get(0).getMetadata().getOrgLevel());
					logger.log(IAppLogger.DEBUG, "TREE STRUCTURE ON LOAD...................");
					logger.log(IAppLogger.DEBUG, orgJsonString);
				}
			  //parentTOs = parentService.getParentList(currentOrg, adminYear, null);
			}
			
			//modelAndView.addObject("parentList", parentTOs);
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: ParentController - manageParent");
		}
		modelAndView.addObject("PDCT_NAME",propertyLookup.get("PDCT_NAME"));
		return modelAndView;

	}
	
	/**
	 * Show list of all students
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/manageStudent", method = RequestMethod.GET)
	public ModelAndView manageStudent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		//List<StudentTO> studentTOs = new ArrayList<StudentTO>();
		List<OrgTreeTO> OrgTreeTOs = new ArrayList<OrgTreeTO>();
		String orgJsonString;
		ModelAndView modelAndView = new ModelAndView("parent/manageStudent");
		String adminYear = (String) request.getParameter("AdminYear");
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		String product = (String) request.getSession().getAttribute(IApplicationConstants.PRODUCT_NAME);
		long currCustomer = (customer == null)? 0 : Long.valueOf(customer);
		String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("loggedinUserTO", loggedinUserTO);
		
		try {
			logger.log(IAppLogger.INFO, "Enter: ParentController - manageStudent");
			String currentOrg = (String) request.getSession().getAttribute(IApplicationConstants.CURRORG);
			
			if (currentOrg != null) {
				/*List<ObjectValueTO> adminList = adminService.getAllAdmin();*/
				List<com.ctb.prism.core.transferobject.ObjectValueTO> customerProductList = adminService.getCustomerProduct(paramMap);
				if(adminYear == null) {
					adminYear = (String) request.getSession().getAttribute(IApplicationConstants.ADMIN_YEAR);
					if(adminYear == null) {
						for(com.ctb.prism.core.transferobject.ObjectValueTO object : customerProductList) {
							adminYear = object.getValue();
							break;
						}
					}
				}
				modelAndView.addObject("adminList", customerProductList);
				if(isTree)
				{
					paramMap.put("nodeid", currentOrg);
					paramMap.put("currOrg", currentOrg);
					paramMap.put("isFirstLoad", true);
					paramMap.put("adminYear", adminYear);
					paramMap.put("customerId", currCustomer);
					paramMap.put("orgMode", orgMode);
					OrgTreeTOs =adminService.getOrganizationTree(paramMap);
					modelAndView.addObject("organizationTreeList", OrgTreeTOs);
					modelAndView.addObject("treeSturcture", "Yes");
					orgJsonString = JsonUtil.convertToJsonAdmin(OrgTreeTOs);
					modelAndView.addObject("orgName", OrgTreeTOs.get(0).getData());
					modelAndView.addObject("rootOrgId", OrgTreeTOs.get(0).getMetadata().getParentTenantId());
					modelAndView.addObject("rootOrgLevel", OrgTreeTOs.get(0).getMetadata().getOrgLevel());
					logger.log(IAppLogger.DEBUG, "TREE STRUCTURE ON LOAD FOR STUDENT...................");
					logger.log(IAppLogger.DEBUG, orgJsonString);
				}
				
				if (product.equals(IApplicationConstants.TASC_PRODUCT)){
					modelAndView.addObject("schoolOrgLevel",IApplicationConstants.TESTING_SITE);
					modelAndView.addObject("isAdultEducation","Yes");
				} else {
					modelAndView.addObject("schoolOrgLevel",IApplicationConstants.SCHOOL);
					modelAndView.addObject("isAdultEducation","No");
				}
				/*studentTOs = parentService.getStudentList(currentOrg, adminYear, null);
				String studentJsonString = JsonUtil.convertToJsonAdmin(studentTOs);
				logger.log(IAppLogger.INFO, "STUDENT DETAILS ON FIRST LOAD..................");
				logger.log(IAppLogger.INFO, studentJsonString);*/
			}
			//modelAndView.addObject("studentList", studentTOs);
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: ParentController - manageStudent");
		}
		modelAndView.addObject("PDCT_NAME",propertyLookup.get("PDCT_NAME"));
		return modelAndView;

	}
	
	/**
	 * Fetching all users for selected organization
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getParentDetailsOnScroll", method = RequestMethod.GET)
	public @ResponseBody
	String getParentDetailsOnScroll(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		List<ParentTO> parentTOs = new ArrayList<ParentTO>();
		try {
			logger.log(IAppLogger.INFO, "Enter: ParentController - getParentDetailsOnScroll");
			String adminYear = (String) request.getParameter("AdminYear");
			String currentOrg = (String) request.getSession().getAttribute(IApplicationConstants.CURRORG);
			String tenantId = (String) request.getParameter("tenantId");
			String searchParam= request.getParameter("searchParam");
			if("Search".equals(searchParam)) searchParam = "";
			
			if (tenantId != null ) {
				parentTOs = parentService.getParentList(tenantId, adminYear, searchParam);
			}
			

			String parentJsonString = JsonUtil.convertToJsonAdmin(parentTOs);

			logger.log(IAppLogger.INFO, parentJsonString);
			response.setContentType("application/json");
			response.getWriter().write(parentJsonString);
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: ParentController - getParentDetailsOnScroll");
		}

		return null;

	}	
	
	
	/**
	 * Fetching all users for selected organization
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getStudentDetailsOnScroll", method = RequestMethod.GET)
	public @ResponseBody
	String getStudentDetailsOnScroll(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		List<StudentTO> studentTOs = new ArrayList<StudentTO>();
		try {
			logger.log(IAppLogger.INFO, "Enter: ParentController - getStudentDetailsOnScroll");
			String adminYear = (String) request.getParameter("AdminYear");
			String scrollId = (String) request.getParameter("scrollId");
			String studentBioId = (String) request.getParameter("studentBioId");
			String isRedirectedTree = (String) request.getParameter("isRedirectedTree");
			String searchParam= request.getParameter("searchParam");
			String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			long currCustomer = (customer == null)? 0 : Long.valueOf(customer);
			
			if("Search".equals(searchParam)) searchParam = "";
			if (scrollId != null && scrollId.trim().length()>0){
				if(("Yes".equals(isRedirectedTree)) && ((studentBioId != null) && (studentBioId.trim().length()>0)) ){
					if (scrollId.lastIndexOf("|") > 0){
						scrollId=scrollId.substring((scrollId.lastIndexOf("|") + 1),scrollId.length());
					}
				studentTOs =  parentService.searchStudentOnRedirect(request.getParameter("studentBioId"), 
						scrollId, Long.valueOf(customer).longValue());
				//(String)request.getSession().getAttribute(IApplicationConstants.CURRORG)
				}
				else {
					studentTOs = parentService.getStudentList(scrollId, adminYear, searchParam, currCustomer);
				}
				}
			String studentJsonString = JsonUtil.convertToJsonAdmin(studentTOs);
			logger.log(IAppLogger.INFO, "LOADING STUDENT DETAILS ..................");
			logger.log(IAppLogger.INFO, studentJsonString);
			response.setContentType("application/json");
			response.getWriter().write(studentJsonString);
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: ParentController - getStudentDetailsOnScroll");
		}

		return null;

	}
	
	/**
	 * Performs search parent user operation for auto complete feature in the search box.
	 * This method is called through AJAX
	 * @param req
	 * @param res
	 * @return Returns nothing, instead writes search data in response as a JSON string
	 */
	@RequestMapping(value="/searchParentAutoComplete", method=RequestMethod.GET)
	public String searchParentAutoComplete( HttpServletRequest req, HttpServletResponse res) {
		logger.log(IAppLogger.INFO, "Enter: ParentController - searchParentAutoComplete");
		try {
			String adminYear = (String) req.getParameter("AdminYear");
			String parents = parentService.searchParentAutoComplete(req.getParameter("term"), 
					req.getParameter("selectedOrg"), adminYear);
			logger.log(IAppLogger.INFO, "Selected organisation............."+req.getParameter("selectedOrg"));
			logger.log(IAppLogger.INFO, (String)req.getSession().getAttribute(IApplicationConstants.CURRORG));
			if ( parents != null ) {
				res.setContentType("application/json");
				res.getWriter().write(parents);
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: ParentController - searchParentAutoComplete");
		}
		return null;
	}
	
	
	/**
	 * Method performs search parent user operation. This method is called through AJAX
	 * @param req
	 * @param res
	 * @return Returns nothing, instead writes search data in response as JSON
	 */
	@RequestMapping(value="/searchParent", method=RequestMethod.GET)
	public String searchParent(HttpServletRequest req, HttpServletResponse res ) {
		logger.log(IAppLogger.INFO, "Enter: ParentController - searchParent");
		try {
			ArrayList<ParentTO> parentsList=null;
			String adminYear = (String) req.getParameter("AdminYear");
			String parentName=req.getParameter("parentName");
			String selectedOrg=req.getParameter("selectedOrg");
			String isExactSeacrh=req.getParameter("isExactSeacrh");
			
			if (isExactSeacrh == null || IApplicationConstants.FLAG_N.equalsIgnoreCase(isExactSeacrh)){
				isExactSeacrh=IApplicationConstants.FLAG_N;
			}else{
				isExactSeacrh=IApplicationConstants.FLAG_Y;
			}
				
			if(parentName != null && parentName.trim().length() == 0) {
				parentName = "%%";
			}
			String browser= req.getParameter("browser");
			if(("ie7".equals(browser))&& ("Search".equals(parentName))){
				parentName="";
				parentsList = parentService.searchParent(parentName, 
						selectedOrg, adminYear,isExactSeacrh);
			}else{
				parentsList = parentService.searchParent(parentName, 
						selectedOrg, adminYear,isExactSeacrh);
			}
			 
			if ( parentsList != null ) {
				String userJsonString = JsonUtil.convertToJsonAdmin(parentsList);
				logger.log(IAppLogger.INFO, "For Searh Parent Name Json.......................");
				logger.log(IAppLogger.INFO, userJsonString);
				res.setContentType("application/json");
				res.getWriter().write( userJsonString  );
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: ParentController - searchParent");
		}
		return null;
			
	}

	/**
	 * Retrieves the Assessment details of the selected Student in order to display it in the home page
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value="/getStudentAssessmentList", method=RequestMethod.GET)
	public String getStudentAssessmentList(HttpServletRequest req, HttpServletResponse res ) {
		logger.log(IAppLogger.INFO, "Enter: getStudentAssessmentList");
		String jsonString = null;
		try {
			String studentBioId = (String) req.getParameter("studentBioId");
			//List<StudentTO> childrenList = parentService.getChildrenList((String) req.getSession().getAttribute(IApplicationConstants.CURRUSER));
			if(studentBioId!= null)
			{
				List<StudentTO> studentAssessmentList = parentService.getAssessmentList(studentBioId);
				if ( studentAssessmentList != null )
				{
					jsonString = JsonUtil.convertToJsonAdmin(studentAssessmentList);
					logger.log(IAppLogger.INFO, jsonString);
					res.setContentType("application/json");
					res.getWriter().write(jsonString);
				} else {
					res.setContentType("application/json");
					res.getWriter().write("{\"status\":\"Blank\"}");
				}
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO,	"Exit: getStudentAssessmentList");
		}
		return null;
	}	
	
	
	/**
	 * Performs search student operation for auto complete feature in the search box.
	 * This method is called through AJAX
	 * @param req
	 * @param res
	 * @return Returns nothing, instead writes search data in response as a JSON string
	 */
	@RequestMapping(value="/searchStudentAutoComplete", method=RequestMethod.GET)
	public String searchStudentAutoComplete( HttpServletRequest req, HttpServletResponse res) {
		logger.log(IAppLogger.INFO, "Enter: ParentController - searchStudentAutoComplete");
		String customer = (String) req.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		long currCustomer = (customer == null)? 0 : Long.valueOf(customer);
		
		try {
			String adminYear = (String) req.getParameter("AdminYear");
			String students = parentService.searchStudentAutoComplete(req.getParameter("term"), 
					req.getParameter("selectedOrg"), adminYear,  currCustomer);
			logger.log(IAppLogger.INFO, "searchStudentAutoComplete.....................................");
			if ( students != null ) {
				res.setContentType("application/json");
				res.getWriter().write(students);
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: ParentController - searchStudentAutoComplete");
		}
		return null;
	}
	
	
	/**
	 * Method performs search student operation. This method is called through AJAX
	 * @param req
	 * @param res
	 * @return Returns nothing, instead writes search data in response as JSON
	 */
	@RequestMapping(value="/searchStudent", method=RequestMethod.GET)
	public String searchStudent(HttpServletRequest req, HttpServletResponse res ) {
		logger.log(IAppLogger.INFO, "Enter: ParentController - searchStudent");
		String customer = (String) req.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		long currCustomer = (customer == null)? 0 : Long.valueOf(customer);
		
		try {
			ArrayList<StudentTO> studentsList = null;
			String adminYear = (String) req.getParameter("AdminYear");
			String studentName = req.getParameter("studentName");
			if(studentName != null && studentName.trim().length() == 0) {
				studentName = "%%";
			}
			String browser = req.getParameter("browser");
			String selectedOrg = req.getParameter("selectedOrg");
			if(("ie7".equals(browser)) && ("Search".equals(studentName))) {
				studentName = "";
				studentsList = parentService.searchStudent(studentName, 
						selectedOrg, adminYear,currCustomer);				
			}
			else {
				studentsList = parentService.searchStudent(studentName, 
						selectedOrg, adminYear, currCustomer);				
			}

			if ( studentsList != null ) {
				String studentJsonString = JsonUtil.convertToJsonAdmin(studentsList);
				logger.log(IAppLogger.INFO, "For Searh Student Name Json.......................");
				logger.log(IAppLogger.INFO, studentJsonString);
				res.setContentType("application/json");
				res.getWriter().write( studentJsonString  );
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: ParentController - searchStudent");
		}
		return null;
			
	}
	

	/**
	 * Show list of all students on redirect 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/redirectToStudent", method = RequestMethod.GET)
	public ModelAndView redirectToStudent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		List<StudentTO> studentTOs = new ArrayList<StudentTO>();
		List<OrgTreeTO> orgTreeTOs = new ArrayList<OrgTreeTO>();
		ModelAndView modelAndView = new ModelAndView("parent/manageStudent");
		String hierarchialOrgIds = null;
		String adminYear = (String) request.getParameter("AdminYear");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> customerProductList =null;	
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		long currCustomer = (customer == null)? 0 : Long.valueOf(customer);
		String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);

		try {
				logger.log(IAppLogger.INFO, "Enter: ParentController - redirectToStudent");
				logger.log(IAppLogger.INFO, "STUDENT BIO ID.................."+request.getParameter("studentBioId"));
				String nodeId = request.getParameter("nodeId");
				String currentOrg = (String) request.getSession().getAttribute(IApplicationConstants.CURRORG);
				String userId=(String) request.getSession().getAttribute(IApplicationConstants.CURRUSERID);
				//String userName=(String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
				com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
				Map<String,Object> paramMap = new HashMap<String,Object>(); 
				paramMap.put("loggedinUserTO", loggedinUserTO);
				if (request.getParameter("studentBioId")!= null)
					{	
						if (nodeId != null){
							customerProductList = adminService.getCustomerProduct(paramMap);
							 
							if(adminYear == null) {
								adminYear = (String) request.getSession().getAttribute(IApplicationConstants.ADMIN_YEAR);
								if(adminYear == null) {
									for(com.ctb.prism.core.transferobject.ObjectValueTO object : customerProductList) {
										adminYear = object.getValue();
										break;
									}
								}
							} else {
								request.getSession().setAttribute(IApplicationConstants.ADMIN_YEAR, adminYear);
							}
						}
					modelAndView.addObject("adminList", customerProductList);
					if(isTree) {
						paramMap.put("nodeid", currentOrg);
						paramMap.put("currOrg", currentOrg);
						paramMap.put("isFirstLoad", true);
						paramMap.put("adminYear", adminYear);
						paramMap.put("customerId", currCustomer);
						paramMap.put("orgMode", orgMode);
						orgTreeTOs =adminService.getOrganizationTree(paramMap);
						hierarchialOrgIds =adminService.getOrganizationTreeOnRedirect(nodeId,currentOrg,userId,currCustomer,true);
						modelAndView.addObject("hierarchialOrgIds", hierarchialOrgIds);
						logger.log(IAppLogger.DEBUG, "hierarchialOrgIds................"+hierarchialOrgIds);
						//modelAndView.addObject("organizationTreeList", orgTreeTOs);
						modelAndView.addObject("treeSturcture", "Yes");
						modelAndView.addObject("isRedirected", "Yes");
						modelAndView.addObject("studentBioId", request.getParameter("studentBioId"));
						//orgJsonString = JsonUtil.convertToJsonAdmin(orgTreeTOs);
						//modelAndView.addObject("orgName", orgTreeTOs.get(0).getData());
						modelAndView.addObject("rootOrgId", orgTreeTOs.get(0).getMetadata().getParentTenantId());
					}
						studentTOs =  parentService.searchStudentOnRedirect(request.getParameter("studentBioId"), 
								(String)request.getSession().getAttribute(IApplicationConstants.CURRORG),currCustomer);
						logger.log(IAppLogger.INFO, "LOADING STUDENT STUDENT PAGE ON CLICK..................");
					}
				String studentJsonString = JsonUtil.convertToJsonAdmin(studentTOs);
				logger.log(IAppLogger.INFO, studentJsonString);
				
				
				modelAndView.addObject("studentList", studentTOs);
		
		
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: ParentController - redirectToStudent");
		}

		return modelAndView;

	}
	/**
	 * Update Assessment Details
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "/updateAssessmentDetails", method = RequestMethod.GET)
	public ModelAndView updateAssessmentDetails(HttpServletRequest req,
			HttpServletResponse res) {
		try {
			logger.log(IAppLogger.INFO, "Enter: ParentController - updateAssessmentDetails");

			String studentBioId= (String) req.getParameter("studentBioId");
			logger.log(IAppLogger.INFO, "studentBioId................................." + studentBioId);
			String administration = (String) req.getParameter("administration");
			String invitationcode = (String) req.getParameter("invitationcode");
			String icExpirationStatus = (String) req.getParameter("icExpirationStatus");
			String totalAvailableClaim = (String) req.getParameter("totalAvailableClaim");
			String expirationDate = (String) req.getParameter("expirationDate");
			if ("Active".equals(icExpirationStatus)) {
				icExpirationStatus = IApplicationConstants.ACTIVE_FLAG;
			} else {
				icExpirationStatus = IApplicationConstants.INACTIVE_FLAG;
			}
			
			boolean isSaved = parentService.updateAssessmentDetails(studentBioId, administration, invitationcode,
					icExpirationStatus, totalAvailableClaim, expirationDate);
			res.setContentType("text/plain");
			String status = "Fail";
			if (isSaved) {
				status = "Success";
			}
			res.getWriter().write("{\"status\":\"" + status + "\"}");

			logger.log(IAppLogger.INFO, "Exit: ParentController - updateAssessmentDetails");

		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error Saving File", e);
		}
		return null;
	}
	
	/**
	 * Method performs reset password operation. This method is called through AJAX
	 * @param req
	 * @param res
	 * @return Returns nothing, instead writes password in response as JSON
	 */
	@RequestMapping(value="/resetPassword", method=RequestMethod.GET)
	public String resetPassword(HttpServletRequest req, HttpServletResponse res ) {
		logger.log(IAppLogger.INFO, "Enter: AdminController - resetPassword");
		try {
			String userName= (String) req.getParameter("userName");
			if ( userName != null ) {
				String newPassword = adminService.resetPassword(userName);
				if (newPassword != null) {
					res.getWriter().write("{\"password\":\"" + newPassword + "\"}");
				}
			}
			
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: AdminController - resetPassword");
		}
		return null;
			
	}	
	
	/**
	 * Method to update admin year into session
	 * @param req
	 * @param res
	 * @return Returns nothing, instead writes password in response as JSON
	 */
	@RequestMapping(value="/updateAdminYear", method=RequestMethod.GET)
	public String updateAdminYear(HttpServletRequest req, HttpServletResponse res ) {
		logger.log(IAppLogger.INFO, "Enter: AdminController - updateAdminYear");
		try {
			String adminYear= req.getParameter("AdminYear");
			String orgMode= req.getParameter("orgMode");
			req.getSession().setAttribute(IApplicationConstants.ADMIN_YEAR, adminYear);
			req.getSession().setAttribute(IApplicationConstants.ORG_MODE, orgMode);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} 
		logger.log(IAppLogger.INFO, "Exit: AdminController - updateAdminYear");
		return null;
			
	}	
	
	/**
	 * Method retrieves the organization children when clicked on a organization
	 * node. This method is called through AJAX
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/regenerateActivationCode", method = RequestMethod.GET)
	public String regenerateActivationCode(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String studentBioId = request.getParameter("studentBioId");
			String adminYear = request.getParameter("adminYear");
			String invitationCode = request.getParameter("invitationCode");
		
			StudentTO student = new StudentTO();
			student.setStudentBioId((studentBioId==null) ? 0 : Long.valueOf(studentBioId));
			student.setAdminid(adminYear);
			student.setInvitationcode(invitationCode);
			
			boolean success = parentService.regenerateActivationCode(student);
			String status = (success)? "Success" : "Failed";
			response.setContentType("text/plain");
			response.getWriter().write("{\"status\":\"" + status + "\"}");

			
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} 

		return null;
	}
	
	@RequestMapping(value = "/studentFileDownload", method = RequestMethod.GET)
	public ModelAndView studentFileDownload() {
		ModelAndView modelAndView = new ModelAndView("admin/studentFileDownload");
		return modelAndView;
	}
	
	@RequestMapping(value = "/downloadStudentFile", method = RequestMethod.GET)
	public void downloadStudentFile(HttpServletRequest request, HttpServletResponse response) {
		logger.log(IAppLogger.INFO, "Enter: Controller - downloadStudentFile");
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String userId = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSERID);
			String startDate = (String) request.getParameter("startDate");
			String endDate = (String) request.getParameter("endDate");
			String fileType = (String) request.getParameter("type");
			paramMap.put("userId", userId);
			paramMap.put("startDate", startDate);
			paramMap.put("endDate", endDate);
			List<StudentDataTO> studentDataList = adminService.downloadStudentFile(paramMap);

			Long orgNodeLevel = ((Long) request.getSession().getAttribute(IApplicationConstants.CURRORGLVL));
			char[] pwd = propertyLookup.get("STUDENT_FILE_PWD").toCharArray();
			ZipFile zipFile = StudentDataUtil.createPasswordProtectedZipFile(orgNodeLevel, studentDataList, fileType, pwd);

			response.setContentType("application/zip");
			response.setContentLength((int) zipFile.getFile().length());
			response.setHeader("Content-Disposition", "attachment; filename=StudentDataFile.zip");

			InputStream is = new FileInputStream(zipFile.getFile());
			FileCopyUtils.copy(is, response.getOutputStream());

			is.close();
			StudentDataUtil.cleanup();
		} catch (IOException e) {
			logger.log(IAppLogger.ERROR, "", e);
			e.printStackTrace();
		} catch (SystemException e) {
			logger.log(IAppLogger.ERROR, "", e);
			e.printStackTrace();
		}
		logger.log(IAppLogger.INFO, "Exit: Controller - downloadStudentFile");
	}
	
	/**
	 * Show list of all education center
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/manageEducationCenter", method = RequestMethod.GET)
	public ModelAndView manageEducationCenter(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		
		request.getSession().setAttribute(IApplicationConstants.LOGIN_AS, IApplicationConstants.ACTIVE_FLAG);

		logger.log(IAppLogger.INFO, "Entre: AdminController - manageEducationCenter()");
		ModelAndView modelAndView = new ModelAndView("admin/eduCenterUsers");
		Map<String,Object> serviceMapEduCentreFilter = null;
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request
				.getSession().getAttribute(
						IApplicationConstants.LOGGEDIN_USER_DETAILS);
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("loggedinUserTO", loggedinUserTO);
		
		try{
			serviceMapEduCentreFilter = adminService.getEducationCenter(paramMap);
			modelAndView.addObject("serviceMapEduCentreFilter",serviceMapEduCentreFilter);
		}catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: AdminController - manageEducationCenter()");
		}
		modelAndView.addObject("PDCT_NAME",propertyLookup.get("PDCT_NAME"));
		return modelAndView;

	}
	
	/**
	 * Show list of all education center users
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loadEduCenterUsers", method = RequestMethod.GET)
	public @ResponseBody 
	void loadEduCenterUsers(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		logger.log(IAppLogger.INFO, "Enter: AdminController - loadEduCenterUsers()");
		String userJsonString = "";
		long eduCenterId = Long.parseLong(request.getParameter("eduCenterId"));
		String eduCenterName = request.getParameter("eduCenterName");
		String searchParam = (String) request.getParameter("searchParam");
		if("Search".equals(searchParam)){
			searchParam = "";
		}
		String lastEduCenterId_username = (String) request.getParameter("lastEduCenterId_username");
		
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request
				.getSession().getAttribute(
						IApplicationConstants.LOGGEDIN_USER_DETAILS);
		
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("loggedinUserTO", loggedinUserTO);
		paramMap.put("eduCenterId", eduCenterId);
		paramMap.put("searchParam", searchParam);
		paramMap.put("lastEduCenterId_username", lastEduCenterId_username);
		
		List<EduCenterTO> eduCenterTOList = null;
		try{
			eduCenterTOList = adminService.loadEduCenterUsers(paramMap);
			List<UserTO> userTOList = convertEduToUserList(eduCenterTOList);
			userJsonString = JsonUtil.convertToJsonAdmin(userTOList);
			
			logger.log(IAppLogger.INFO,"userJsonString:"+userJsonString);
			response.setContentType("application/json");
			response.getWriter().write(userJsonString);

		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}
		logger.log(IAppLogger.INFO, "Exit: AdminController - loadEduCenterUsers()");
	}
	
	private List<UserTO> convertEduToUserList(List<EduCenterTO> eduCenterTOList){
		List<UserTO> userTOList = new ArrayList<UserTO>();
		UserTO userTO = null;
		for (EduCenterTO eduCenterTO : eduCenterTOList) {
			userTO = new UserTO();
			userTO.setUserId(eduCenterTO.getUserId());
			
			RoleTO roleTO = null;
			List<RoleTO> roleTOList = new ArrayList<RoleTO>();
			for (com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO : eduCenterTO.getRoleList()) {
				roleTO = new RoleTO();
				roleTO.setRoleId(Long.parseLong(objectValueTO.getValue()));
				roleTO.setRoleName(objectValueTO.getName());
				roleTOList.add(roleTO);
			}
			userTO.setAvailableRoleList(roleTOList);
			userTO.setUserName(eduCenterTO.getUserName());
			userTO.setUserDisplayName(eduCenterTO.getFullName());
			userTO.setStatus(eduCenterTO.getStatus());
			userTO.setTenantId(eduCenterTO.getEduCenterId());
			userTO.setParentId(eduCenterTO.getEduCenterId());
			userTO.setTenantName(eduCenterTO.getEduCenterName());
			userTO.setLoggedInOrgId(eduCenterTO.getEduCenterId());
			userTOList.add(userTO);
		}
		return userTOList;
	}
	
	@RequestMapping(value = "/downloadUsers", method = RequestMethod.GET)
	public void downloadUsers(HttpServletRequest request, HttpServletResponse response) {
		logger.log(IAppLogger.INFO, "Enter: AdminController.downloadUsers");
		Map<String, String> paramMap = new HashMap<String, String>();
		
		String tenantId = (String) request.getParameter("tenantId");
		String adminYear = (String) request.getParameter("adminYear");
		String userId = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSERID);

		paramMap.put("tenantId", tenantId);
		paramMap.put("adminYear", adminYear);
		paramMap.put("userId", userId);

		logger.log(IAppLogger.INFO, "tenantId=" + tenantId + ", adminYear=" + adminYear + ", userId=" + userId);

		byte[] data = null;
		List<UserDataTO> userList = (List<UserDataTO>) adminService.getUserData(paramMap);
		data = InorsDownloadUtil.getUserDataBytes(userList, "\t");
		try {
			data = FileUtil.xlsxBytes(data, "\t", true);
			data = FileUtil.zipBytes(InorsDownloadConstants.USERS_FILE_PATH, data);
		} catch (IOException e) {
			logger.log(IAppLogger.ERROR, "zipBytes - ", e);
			e.printStackTrace();
		}
		FileUtil.browserDownload(response, data, InorsDownloadConstants.USERS_ZIP_FILE_PATH);
		logger.log(IAppLogger.INFO, "Exit: AdminController.downloadUsers");
	}
}
