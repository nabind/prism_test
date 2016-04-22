package com.ctb.prism.web.controller;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ctb.prism.admin.service.IAdminService;
import com.ctb.prism.admin.transferobject.EduCenterTO;
import com.ctb.prism.admin.transferobject.OrgTO;
import com.ctb.prism.admin.transferobject.OrgTreeTO;
import com.ctb.prism.admin.transferobject.RoleTO;
import com.ctb.prism.admin.transferobject.UserDataTO;
import com.ctb.prism.admin.transferobject.UserTO;
import com.ctb.prism.core.Service.IRepositoryService;
import com.ctb.prism.core.Service.IUsabilityService;
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.constant.IApplicationConstants.EXTRACT_CATEGORY;
import com.ctb.prism.core.constant.IApplicationConstants.EXTRACT_FILETYPE;
import com.ctb.prism.core.constant.IApplicationConstants.JOB_STATUS;
import com.ctb.prism.core.constant.IApplicationConstants.REQUEST_TYPE;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.core.transferobject.StudentDataExtractTO;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.EmailSender;
import com.ctb.prism.core.util.FileUtil;
import com.ctb.prism.core.util.PasswordGenerator;
import com.ctb.prism.core.util.SaltedPasswordEncoder;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.inors.constant.InorsDownloadConstants;
import com.ctb.prism.inors.util.InorsDownloadUtil;
import com.ctb.prism.login.Service.ILoginService;
import com.ctb.prism.login.security.filter.RESTAuthenticationFilter;
import com.ctb.prism.parent.service.IParentService;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.StudentTO;
import com.ctb.prism.web.util.JsonUtil;

/**
 * @author TCS
 * 
 */
// @PreAuthorize("hasAuthority('ROLE_ADMIN', ROLE_EDU_ADMIN)")
@Controller
public class AdminController {
	private static final IAppLogger logger = LogFactory.getLoggerInstance(AdminController.class.getName());

	@Autowired
	private IAdminService adminService;
	
	@Autowired
	private IParentService parentService;
	
	@Autowired
	private IUsabilityService usabilityService;
	
	@Autowired 
	private ILoginService loginService;
	
	@Autowired
	private IPropertyLookup propertyLookup;
	
	@Autowired
	private EmailSender emailSender;
	
	@Autowired
	private IRepositoryService repositoryService;

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
		//long currCustomer = (customer == null)? 0 : Long.parseLong(customer);
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		//paramMap.put("loggedinUserTO", loggedinUserTO);
		paramMap.put("loggedInCustomer",loggedinUserTO.getCustomerId());
		paramMap.put("loggedInOrgId",loggedinUserTO.getOrgId());
		
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
				}else{
					request.getSession().setAttribute(IApplicationConstants.ADMIN_YEAR, adminYear);
				}
				
				paramMap.put("nodeid", currentOrg);
				paramMap.put("currOrg", currentOrg);
				paramMap.put("isFirstLoad", true);
				paramMap.put("adminYear", adminYear);
				paramMap.put("customerId", customer);
				paramMap.put("orgMode", orgMode);
				
				OrgTreeTOs =adminService.getOrgTree(paramMap);
				//organizationList= adminService.getOrganizationList(currentOrg,adminYear);
				modelAndView.addObject("organizationTreeList", OrgTreeTOs);
				modelAndView.addObject("organizationList", organizationList);
				modelAndView.addObject("treeSturcture", "Yes");
				//orgJsonString = JsonUtil.convertToJsonAdmin(OrgTreeTOs);
				modelAndView.addObject("orgName", OrgTreeTOs.get(0).getData());
				modelAndView.addObject("rootOrgId", OrgTreeTOs.get(0).getMetadata().getParentTenantId());
				modelAndView.addObject("rootOrgLevel", OrgTreeTOs.get(0).getMetadata().getOrgLevel());
				
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
		String jsonString = "";
		String nodeid= request.getParameter("tenantId");
		String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
		String searchParam= request.getParameter("searchParam");
		if("Search".equals(searchParam)) searchParam = "";
		
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		//long currCustomer = (customer == null)? 0 : Long.parseLong(customer);
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
				String moreCount = propertyLookup.get("count.results.button.more");
					children = adminService.getOrganizationChildren(nodeid, adminYear, searchParam, customer,orgMode,moreCount);
					if ( children != null )	{
						jsonString = JsonUtil.convertToJsonAdmin(children);
					}
					response.setContentType("application/json");
					response.getWriter().write(jsonString);
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
	 * Method retrieves the number of users for an organization when clicked on the view users button. This method is called through AJAX
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getUserCount", method = RequestMethod.GET)
	public String getUserCount(HttpServletRequest request, HttpServletResponse response) {
		logger.log(IAppLogger.INFO, "Enter: getUserCount()");
		OrgTO orguser = new OrgTO();
		List<OrgTO> orgUserCount = new ArrayList<OrgTO>();
		String jsonString = null;
		String tenantId = request.getParameter("tenantId");
		String adminYear = request.getParameter("adminYear");
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		//long currCustomer = (customer == null) ? 0 : Long.parseLong(customer);
		String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
		try {
			if (tenantId != null && adminYear != null) {
				orguser = adminService.getTotalUserCount(tenantId, adminYear, customer, orgMode);
				if (orguser != null) {
					orgUserCount.add(orguser);
					jsonString = JsonUtil.convertToJsonAdmin(orgUserCount);
					response.setContentType("application/json");
					response.getWriter().write(jsonString);
				}
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: getUserCount()");
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
			String custProdId = (String) req.getParameter("AdminYear");
			String customer = (String) req.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			//long currCustomer = (customer == null)? 0 : Long.parseLong(customer);
			String orgMode = (String) req.getSession().getAttribute(IApplicationConstants.ORG_MODE);
			String selectedOrg = (String) req.getParameter("selectedOrg");
			Map<String,Object> paramMap = new HashMap<String,Object>(); 
			
			List<OrgTO> orgs = null;
			
			if (selectedOrg != null && !selectedOrg.equals("unspecified")) {
				paramMap.put("orgName", req.getParameter("searchString"));
				paramMap.put("tenantId",selectedOrg);
				paramMap.put("custProdId",custProdId);
				paramMap.put("customerId",customer);
				paramMap.put("orgMode",orgMode);
				
				orgs = adminService.searchOrganization(paramMap);
			} else {
				logger.log(IAppLogger.INFO, "No Org selected");
			}
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
			String custProdId = (String) req.getParameter("AdminYear");
			String customer = (String) req.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			//long currCustomer = (customer == null)? 0 : Long.parseLong(customer);
			String orgMode = (String) req.getSession().getAttribute(IApplicationConstants.ORG_MODE);
			String selectedOrg = (String) req.getParameter("selectedOrg");
			String orgs = null;
			Map<String,Object> paramMap = new HashMap<String,Object>(); 
			
			if (selectedOrg !=null && !selectedOrg.equals("unspecified")) {
				paramMap.put("orgName", req.getParameter("term"));
				paramMap.put("tenantId",selectedOrg);
				paramMap.put("custProdId",custProdId);
				paramMap.put("customerId",customer);
				paramMap.put("orgMode",orgMode);
				orgs = adminService.searchOrgAutoComplete(paramMap);
			} else {
				logger.log(IAppLogger.INFO, "No Org selected");
			}
			
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
	public ModelAndView userModule(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/users");
		List<OrgTreeTO> OrgTreeTOs = null;
		try {
			String currentOrg = (String) request.getSession().getAttribute(IApplicationConstants.CURRORG);
			String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
			
			com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
			Map<String,Object> paramMap = new HashMap<String,Object>(); 
			paramMap.put("loggedInCustomer",loggedinUserTO.getCustomerId());
			paramMap.put("loggedInOrgId",loggedinUserTO.getOrgId());
			paramMap.put("project",loggedinUserTO.getProject());
			paramMap.put("contractName", Utils.getContractName());
			
			// get list of test admin for the customer for which user is logged in
			List<com.ctb.prism.core.transferobject.ObjectValueTO> customerProductList = adminService.getCustomerProduct(paramMap);
			modelAndView.addObject("adminList", customerProductList);
			
			String adminYear = (String) request.getParameter("AdminYear");
			if(adminYear == null) {
				adminYear = (String) request.getSession().getAttribute(IApplicationConstants.ADMIN_YEAR);
				if(adminYear == null) {
					for(com.ctb.prism.core.transferobject.ObjectValueTO object : customerProductList) {
						adminYear = object.getValue();
						break;
					}
				}
			} else{
				request.getSession().setAttribute(IApplicationConstants.ADMIN_YEAR, adminYear);
			}
			
			if (currentOrg != null) {
				OrgTreeTOs = new ArrayList<OrgTreeTO>();
				Map<String,Object> paramMapA = new HashMap<String,Object>(); 
				paramMapA.put("nodeid", currentOrg);
				paramMapA.put("currOrg", currentOrg);
				paramMapA.put("isFirstLoad", true);
				paramMapA.put("adminYear", adminYear);
				paramMapA.put("customerId", customer);
				paramMapA.put("orgMode", orgMode);
				
				// get org hierarchy
				OrgTreeTOs = adminService.getOrganizationTree(paramMapA);
				modelAndView.addObject("organizationTreeList", OrgTreeTOs);
				modelAndView.addObject("treeSturcture", "Yes");
				modelAndView.addObject("orgName", OrgTreeTOs.get(0).getData());
				modelAndView.addObject("rootOrgId", OrgTreeTOs.get(0).getMetadata().getParentTenantId());
				modelAndView.addObject("rootOrgLevel", OrgTreeTOs.get(0).getMetadata().getOrgLevel());
			}
		
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} 
		
		return modelAndView;
	}
	
	/**
	 * This is for old oracle method 
	 * TODO - remove after replacing with mongo*/
	@Deprecated
	public ModelAndView manageUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		request.getSession().setAttribute(IApplicationConstants.LOGIN_AS, IApplicationConstants.ACTIVE_FLAG);
		
		List<OrgTO> OrgTOs = new ArrayList<OrgTO>();
		List<UserTO> UserTOs = new ArrayList<UserTO>();
		List<OrgTreeTO> OrgTreeTOs = new ArrayList<OrgTreeTO>();
		String orgJsonString;
		ModelAndView modelAndView = new ModelAndView("admin/users");
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		long currCustomer = (customer == null)? 0 : Long.parseLong(customer);
		String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		//paramMap.put("loggedinUserTO", loggedinUserTO);
		paramMap.put("loggedInCustomer",loggedinUserTO.getCustomerId());
		paramMap.put("loggedInOrgId",loggedinUserTO.getOrgId());

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
			}else{
				request.getSession().setAttribute(IApplicationConstants.ADMIN_YEAR, adminYear);
			}
			
			if (currentOrg != null) {
				if(isTree)
				{	Map<String,Object> paramMapA = new HashMap<String,Object>(); 
					paramMapA.put("nodeid", currentOrg);
					paramMapA.put("currOrg", currentOrg);
					paramMapA.put("isFirstLoad", true);
					paramMapA.put("adminYear", adminYear);
					paramMapA.put("customerId", customer);
					paramMapA.put("orgMode", orgMode);
					OrgTreeTOs = adminService.getOrganizationTree(paramMapA);
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
				{	Map<String,Object> paramMapB = new HashMap<String,Object>(); 
					paramMapB.put("nodeid", currentOrg);
					paramMapB.put("customerId", customer);
					OrgTOs = adminService.getOrganizationDetailsOnFirstLoad(paramMapB);
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
		//long currCustomer = (customer == null)? 0 : Long.parseLong(customer);
		String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
		
		try {
			logger.log(IAppLogger.INFO,
					"Enter: AdminController - tenantHierarchy");
			String nodeid = (String) request.getParameter("tenantId");
			String adminYear = (String) request.getParameter("AdminYear");
			
			Map<String,Object> paramMap = new HashMap<String,Object>(); 
			paramMap.put("nodeid", nodeid.equals("0_") ? "0_1" : nodeid);
			paramMap.put("currOrg", currentOrg);
			paramMap.put("isFirstLoad", false);
			paramMap.put("adminYear", adminYear);
			paramMap.put("customerId", customer);
			paramMap.put("orgMode", orgMode);
			
			if (nodeid != null) {
				OrgTreeTOs = adminService.getOrganizationTree(paramMap);
			}
			 orgJsonString = JsonUtil.convertToJsonAdmin(OrgTreeTOs);
			
			/*if(isTree)
			{
				if (nodeid != null) {
					OrgTreeTOs = adminService.getOrganizationTree(paramMap);
					for (OrgTreeTO to : OrgTreeTOs) {
						String orgNodeLevel = to.getOrgNodeLevel();
						String organizationMode = to.getOrgMode();
						if (orgNodeLevel.equals("2")) {
							request.getSession().setAttribute(IApplicationConstants.ORG_MODE, organizationMode);
						}
					}
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
			*/

			
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
	String userDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<UserTO> UserTOs = new ArrayList<UserTO>();
		String currentOrg = (String) request.getSession().getAttribute(IApplicationConstants.CURRORG);
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		Map<String,Object> paramUserMap = new HashMap<String,Object>(); ; 
		//paramMap.put("loggedinUserTO", loggedinUserTO);
		paramMap.put("loggedInCustomer",loggedinUserTO.getCustomerId());
		paramMap.put("loggedInOrgId",loggedinUserTO.getOrgId());
		paramMap.put("project",loggedinUserTO.getProject());
		paramMap.put("contractName", Utils.getContractName());
		paramMap.put("loggedInOrgId",loggedinUserTO.getOrgId());
		
		String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
		String moreCount = propertyLookup.get("count.results.button.more");
		logger.log(IAppLogger.INFO, "moreCount = " + moreCount);
		
		//List<ObjectValueTO> adminList = null;
		try {
			logger.log(IAppLogger.INFO, "Enter: AdminController - userDetails");
			String customerid = (String)request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			String nodeId = (String) request.getParameter("tenantId");
			String adminYear = (String) request.getParameter("AdminYear");
			
			if(nodeId != null && (adminYear == null || "undefined".equals(adminYear))){				
			   	adminYear = (String) request.getSession().getAttribute(IApplicationConstants.ADMIN_YEAR);
				if(adminYear == null) {
					//adminList = adminService.getAllAdmin();	
					List<com.ctb.prism.core.transferobject.ObjectValueTO> customerProductList = adminService.getCustomerProduct(paramMap);
					for(com.ctb.prism.core.transferobject.ObjectValueTO object : customerProductList) {
						adminYear = object.getValue();
						break;
					}
				}
			}else{
				request.getSession().setAttribute(IApplicationConstants.ADMIN_YEAR, adminYear);
			}
			
			
			String searchParam = (String) request.getParameter("searchParam");
			if("Search".equals(searchParam)) searchParam = "";
			logger.log(IAppLogger.INFO, "Node ID: " + nodeId);
			if (nodeId != null) {
				paramUserMap.put("NODEID", nodeId);
				
				if (currentOrg.equals(nodeId)) {					
					paramUserMap.put("CURRENTORG", currentOrg);	
				} else if (nodeId.indexOf("_") > 0) {			
					paramUserMap.put("CURRENTORG", nodeId.substring(0, nodeId.indexOf("_")));
				} else {
					paramUserMap.put("CURRENTORG", nodeId);	
				}
				paramUserMap.put("ADMINYEAR", adminYear);
				paramUserMap.put("SEARCHPARAM", searchParam);
				paramUserMap.put("CUSTOMERID", customerid);
				paramUserMap.put("ORGMODE", orgMode);
				paramUserMap.put("moreCount", moreCount);
								
				UserTOs = adminService.getUserDetailsOnClick(paramUserMap);
				
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

		UserTO user = new UserTO();
		List<UserTO> userList = new ArrayList<UserTO>();
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		try {
			logger.log(IAppLogger.INFO, "Enter: AdminController - getEditUserData");
			String userId = (String) request.getParameter("userId");
			String purpose = (String) request.getParameter("purpose");
			String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
			paramMap.put("project",loggedinUserTO.getProject());
			paramMap.put("userId", userId);
			paramMap.put("customer", customer);
			paramMap.put("purpose", purpose);
			
			if (userId != null) {
				user = adminService.getEditUserData(paramMap);			
				
				if (IApplicationConstants.PURPOSE.equals(purpose)) {
					user.setAvailableRoleList(getEducationCenterAvailableRoleList());
					user.setMasterRoleList(getEducationCenterMasterRoleList(userId)); // nodeId having value of userId
				}
			}
			
			userList.add(user);

			String userJsonString = JsonUtil.convertToJsonAdmin(userList);
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
			String salt = null;//PasswordGenerator.getNextSalt();
			com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) req.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
			
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
				} else {
					// check password history
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("contractName", Utils.getContractName());
					paramMap.put("userName", userName);
					paramMap.put("project",loggedinUserTO.getProject());
					List<String> pwdList = loginService.getPasswordHistory(paramMap);
					if(pwdList != null && !pwdList.isEmpty()) {
						salt = pwdList.get(0);					
						String encPass = SaltedPasswordEncoder.encryptPassword(password, Utils.getSaltWithUser(userName, salt));
						if(pwdList.contains(encPass)) {
							res.setContentType("text/plain");
							status="invalidPwdHistory";
							res.getWriter().write("{\"status\":\"" + status + "\"}");
							return null;
						}
					}
				}
			}	

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("Id", Id);
			paramMap.put("userId", userId);
			paramMap.put("userName", userName);
			paramMap.put("emailId", emailId);
			paramMap.put("password", password);
			paramMap.put("userStatus", userStatus);
			paramMap.put("userRoles", userRoles);
			paramMap.put("salt", salt);
			paramMap.put("contractName", Utils.getContractName());
			paramMap.put("project",loggedinUserTO.getProject());
			
			boolean isSaved = adminService.updateUser(paramMap);
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
	 * @return
	 */
	private List<RoleTO> getEducationCenterAvailableRoleList() {
		List<RoleTO> roleList = new ArrayList<RoleTO>();
		RoleTO to = new RoleTO();
		to.setRoleName(IApplicationConstants.ROLE_TYPE.ROLE_USER.toString());
		to.setRoleDescription("User");
		to.setRoleId(IApplicationConstants.ROLE_USER_ID);
		to.setDefaultSelection("selected");
		roleList.add(to);
		to = new RoleTO();
		to.setRoleName(IApplicationConstants.ROLE_TYPE.ROLE_EDU_ADMIN.toString());
		to.setRoleDescription("Education Center Admin User");
		to.setRoleId(IApplicationConstants.ROLE_EDU_ADMIN_ID);
		roleList.add(to);
		return roleList;
	}

	/**
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
	private List<RoleTO> getEducationCenterMasterRoleList(String userId) throws Exception {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userId", userId);
		return adminService.getEduUserRoleList(paramMap);
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

		List<RoleTO> roleList = new ArrayList<RoleTO>();
		try {
			logger.log(IAppLogger.INFO,
					"Enter: AdminController - getRoleOnAddUser");
			
			String orgLevel = (String) request.getParameter("orgLevel");
			String purpose = (String) request.getParameter("purpose");
			
			if(IApplicationConstants.PURPOSE.equals(purpose)){
				/*RoleTO masterRoleTO = new RoleTO();
				masterRoleTO.setRoleName(IApplicationConstants.ROLE_TYPE.ROLE_USER.toString());
				masterRoleTO.setRoleDescription("User");
				masterRoleTO.setRoleId(IApplicationConstants.ROLE_USER_ID);
				masterRoleTO.setDefaultSelection("selected");
				RoleTOs.add(masterRoleTO);
				
				masterRoleTO = new RoleTO();
				masterRoleTO.setRoleName(IApplicationConstants.ROLE_TYPE.ROLE_ADMIN.toString());
				masterRoleTO.setRoleDescription("Education Center Admin User");
				masterRoleTO.setRoleId(IApplicationConstants.ROLE_EDU_ADMIN);
				RoleTOs.add(masterRoleTO);*/
				roleList = getEducationCenterAvailableRoleList();
			}else{
				if (orgLevel != null) {
					String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
					roleList = adminService.getRoleOnAddUser(orgLevel, customer);
				}
			}
			
			String userJsonString = JsonUtil.convertToJsonAdmin(roleList);
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
			
			com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) req.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
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
			paramMap.put("project",loggedinUserTO.getProject());
			paramMap.put("orgMode",loggedinUserTO.getOrgMode());
			paramMap.put("orgName",loggedinUserTO.getOrgName());
			
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
			
			// Check if password contains part of user name
			if (password.equalsIgnoreCase(userId) || password.toLowerCase().indexOf(userId.toLowerCase()) != -1 || userId.toLowerCase().indexOf(password.toLowerCase()) != -1) {
				throw new BusinessException(propertyLookup.get("script.user.passwordPartUsername"));
			}
			
			/*userTo = adminService.addNewUser(userId, tenantId, userName,
							emailId, password, userStatus, userRoles,orgLevel, adminYear, customer);*/
			paramMap.put("contractName", Utils.getContractName());
			
			//Second parameter is needed to build cache key to implement cachePut
			Map<String,Object> searchParamMap = new HashMap<String,Object>(); 
			searchParamMap.put("NODEID", tenantId);
			searchParamMap.put("CURRENTORG", tenantId);				
			searchParamMap.put("ADMINYEAR", adminYear);
			searchParamMap.put("SEARCHPARAM", null);
			searchParamMap.put("CUSTOMERID", customer);
			searchParamMap.put("ORGMODE", req.getSession().getAttribute(IApplicationConstants.ORG_MODE));
			searchParamMap.put("moreCount", propertyLookup.get("count.results.button.more"));
			
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
			res.setContentType("text/plain");
			res.getWriter().write("{\"status\":\"Faliure\", \"message\":\"Error in user creation. Please try later.\"}");
		}
		return null;
	}
	/**
	 * delete User from Ldap and database
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException 
	 */

	@RequestMapping(value="/deleteUser", method=RequestMethod.POST )
	public ModelAndView deleteUser(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			logger.log(IAppLogger.INFO, "Enter: AdminController - deleteUser");


			String Id = (String)req.getParameter("Id");
			logger.log(IAppLogger.INFO, "Id of the user to delete is ........................."+Id);
			String userName = (String)req.getParameter("userName");
			String password = (String)req.getParameter("password");
			String purpose = (String)req.getParameter("purpose");		
			
			com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) req.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
			
			Map<String,Object> paramMap = new HashMap<String,Object>(); 
			paramMap.put("userName", userName);
			paramMap.put("password", password);
			paramMap.put("Id", Id);
			paramMap.put("purpose", purpose);
			paramMap.put("project",loggedinUserTO.getProject());
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
			res.getWriter().write( "{\"status\":\"Fail\"}" );
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
			String orgMode = (String) req.getSession().getAttribute(IApplicationConstants.ORG_MODE);

			
				
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
				String tenantId = (String)req.getParameter("selectedOrg");
				List<UserTO> users = new ArrayList<UserTO>();
				
				if(tenantId != null &&  !tenantId.equals("undefined")) {
					 users = adminService.searchUser(req.getParameter("username"), 
							 tenantId, adminYear,isExactSearch,orgMode);
				} else {
					logger.log(IAppLogger.INFO, "No org selected");
					return null;
				}				    
				
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
	 * This method is called through AJAX.
	 * 
	 * @param req
	 * @param res
	 * @return Returns nothing, instead writes search data in response as a JSON string
	 */
	@RequestMapping(value = "/searchUserAutoComplete", method = RequestMethod.GET)
	public String searchUserAutoComplete(HttpServletRequest req, HttpServletResponse res) {
		logger.log(IAppLogger.INFO, "Enter: AdminController - searchUserAutoComplete");
		try {
			String adminYear = (String) req.getParameter("AdminYear");
			String term = (String) req.getParameter("term");
			String selectedOrg = (String) req.getParameter("selectedOrg");
			String purpose = (String) req.getParameter("purpose");
			String orgMode = (String) req.getSession().getAttribute(IApplicationConstants.ORG_MODE);
			String moreCount = propertyLookup.get("count.results.button.more");
			logger.log(IAppLogger.INFO, "adminYear = " + adminYear);
			logger.log(IAppLogger.INFO, "term = " + term);
			logger.log(IAppLogger.INFO, "selectedOrg = " + selectedOrg);
			logger.log(IAppLogger.INFO, "purpose = " + purpose);
			logger.log(IAppLogger.INFO, "orgMode = " + orgMode);
			logger.log(IAppLogger.INFO, "moreCount = " + moreCount);
			if (IApplicationConstants.PURPOSE.equals(purpose)) {
				selectedOrg = (String) req.getParameter("eduCenterId");
			}
			if (selectedOrg == null) {
				return null;
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("adminYear", adminYear);
			paramMap.put("term", term);
			paramMap.put("selectedOrg", selectedOrg);
			paramMap.put("purpose", purpose);
			paramMap.put("orgMode", orgMode);
			paramMap.put("moreCount", moreCount);
			String users = adminService.searchUserAutoComplete(paramMap);
			logger.log(IAppLogger.INFO, "CURRORG = " + (String) req.getSession().getAttribute(IApplicationConstants.CURRORG));
			if (users != null) {
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
		String roleId = (String)request.getParameter("roleId");
		String lastUserId = (String)request.getParameter("lastUserId");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		
		try {
			String moreRole = (String)request.getParameter("moreRole");
			if(null==moreRole)
			{
				moreRole="false";
			}
		
			paramMap.put("roleId", roleId);
			paramMap.put("currentOrg", currentOrg);
			paramMap.put("customer", customer);
			paramMap.put("moreRole", moreRole);
			paramMap.put("lastUserId", lastUserId);
			paramMap.put("contractName", Utils.getContractName());

			if (roleId != null) {
				roleTo = adminService.getRoleDetailsById(paramMap);
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
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("roleId", roleId);
			paramMap.put("currentOrg", currentOrg);
			paramMap.put("customer", customer);
			paramMap.put("username", userName);
			
			if(!parentService.checkUserAvailability(paramMap)) {
				logger.log(IAppLogger.INFO, "****Username matched with database****");	
				if (!parentService.isRoleAlreadyTagged(roleId, userName)){
					logger.log(IAppLogger.INFO, "****Role Already Tagged****");
					if (roleId != null && userName != null) {
						boolean isSaved = adminService.associateUserToRole(roleId, userName);
						if (isSaved) {
							roleTo = adminService.getRoleDetailsById(paramMap);
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
		Map<String,Object> paramMap = new HashMap<String,Object>();
		try {
			String roleId = (String) request.getParameter("roleId");
			String userId = (String) request.getParameter("userId");

			paramMap.put("roleId", roleId);
			paramMap.put("currentOrg", currentOrg);
			paramMap.put("customer", customer);
			
			if (roleId != null && userId != null) {
				boolean isDeleted = adminService.deleteUserFromRole(roleId, userId);
				if (isDeleted) {
					roleTo = adminService.getRoleDetailsById(paramMap);
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
			long currCustomer = (customer == null)? 0 : Long.parseLong(customer);
			String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
			com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
			Map<String,Object> paramMap = new HashMap<String,Object>(); 
			//paramMap.put("loggedinUserTO", loggedinUserTO);
			paramMap.put("loggedInCustomer",loggedinUserTO.getCustomerId());
			paramMap.put("loggedInOrgId",loggedinUserTO.getOrgId());
			
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
				}else{
					request.getSession().setAttribute(IApplicationConstants.ADMIN_YEAR, adminYear);
				}
				
				
				modelAndView.addObject("adminList", customerProductList);
				if(isTree) {
					Map<String,Object> paramMapA = new HashMap<String,Object>(); 
					paramMapA.put("nodeid", currentOrg);
					paramMapA.put("currOrg", currentOrg);
					paramMapA.put("isFirstLoad", true);
					paramMapA.put("adminYear", adminYear);
					paramMapA.put("customerId", currCustomer);
					paramMapA.put("orgMode", orgMode);
					orgTreeTOs = adminService.getOrganizationTree(paramMapA);
					/*orgTreeTOs =adminService.getOrganizationTree(currentOrg,currentOrg,true, adminYear,currCustomer, "");*/
					paramMapA.put("nodeid", nodeId);
					//paramMapA.put("userId", userId);
					paramMapA.put("isRedirected", true);
					hierarchialOrgIds =adminService.getOrganizationTreeOnRedirect(paramMapA);
					modelAndView.addObject("hierarchialOrgIds", hierarchialOrgIds);
					//modelAndView.addObject("organizationTreeList", orgTreeTOs);
					modelAndView.addObject("treeSturcture", "Yes");
					//orgJsonString = JsonUtil.convertToJsonAdmin(orgTreeTOs);
					//modelAndView.addObject("orgName", orgTreeTOs.get(0).getData());
					modelAndView.addObject("rootOrgId", orgTreeTOs.get(0).getMetadata().getParentTenantId());
				}
				else
				{
					orgTOs = adminService.getOrganizationDetailsOnFirstLoad(paramMap);
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
		long currCustomer = (customer == null)? 0 : Long.parseLong(customer);
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		//paramMap.put("loggedinUserTO", loggedinUserTO);
		paramMap.put("loggedInCustomer",loggedinUserTO.getCustomerId());
		paramMap.put("loggedInOrgId",loggedinUserTO.getOrgId());

		try {
			logger.log(IAppLogger.INFO, "Enter: manageParent");
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
				}else{
					request.getSession().setAttribute(IApplicationConstants.ADMIN_YEAR, adminYear);
				}
				
				
				modelAndView.addObject("adminList", customerProductList);
				if(isTree)
				{	
					Map<String,Object> paramMapA = new HashMap<String,Object>(); 
					paramMapA.put("nodeid", currentOrg);
					paramMapA.put("currOrg", currentOrg);
					paramMapA.put("isFirstLoad", true);
					paramMapA.put("adminYear", adminYear);
					paramMapA.put("customerId", currCustomer);
					paramMapA.put("orgMode", orgMode);
					OrgTreeTOs =adminService.getOrganizationTree(paramMapA);
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
			logger.log(IAppLogger.INFO, "Exit: manageParent");
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
		long currCustomer = (customer == null)? 0 : Long.parseLong(customer);
		String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		//paramMap.put("loggedinUserTO", loggedinUserTO);
		paramMap.put("loggedInCustomer",loggedinUserTO.getCustomerId());
		paramMap.put("loggedInOrgId",loggedinUserTO.getOrgId());
		
		try {
			logger.log(IAppLogger.INFO, "Enter: manageStudent");
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
				}else{
					request.getSession().setAttribute(IApplicationConstants.ADMIN_YEAR, adminYear);
				}
				
				modelAndView.addObject("adminList", customerProductList);
				if(isTree)
				{	
					Map<String,Object> paramMapA = new HashMap<String,Object>(); 
					paramMapA.put("nodeid", currentOrg);
					paramMapA.put("currOrg", currentOrg);
					paramMapA.put("isFirstLoad", true);
					paramMapA.put("adminYear", adminYear);
					paramMapA.put("customerId", currCustomer);
					paramMapA.put("orgMode", orgMode);
					OrgTreeTOs =adminService.getOrganizationTree(paramMapA);
					modelAndView.addObject("organizationTreeList", OrgTreeTOs);
					modelAndView.addObject("treeSturcture", "Yes");
					orgJsonString = JsonUtil.convertToJsonAdmin(OrgTreeTOs);
					modelAndView.addObject("orgName", OrgTreeTOs.get(0).getData());
					modelAndView.addObject("rootOrgId", OrgTreeTOs.get(0).getMetadata().getParentTenantId());
					modelAndView.addObject("rootOrgLevel", OrgTreeTOs.get(0).getMetadata().getOrgLevel());
					logger.log(IAppLogger.DEBUG, "TREE STRUCTURE ON LOAD FOR STUDENT...................");
					logger.log(IAppLogger.DEBUG, orgJsonString);
				}
				
				if(IApplicationConstants.CONTRACT_NAME_TASC.equals(Utils.getContractName())){
					modelAndView.addObject("schoolOrgLevel",IApplicationConstants.TESTING_SITE);
				}else{
					modelAndView.addObject("schoolOrgLevel",IApplicationConstants.SCHOOL);
				}
			}
			//modelAndView.addObject("studentList", studentTOs);
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: manageStudent");
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
			logger.log(IAppLogger.INFO, "Enter: getParentDetailsOnScroll");
			String adminYear = (String) request.getParameter("AdminYear");
			String currentOrg = (String) request.getSession().getAttribute(IApplicationConstants.CURRORG);
			String tenantId = (String) request.getParameter("tenantId");
			String searchParam= request.getParameter("searchParam");
			//Added for TD 77154
			String orgMode = (String)request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
			if("Search".equals(searchParam)) searchParam = "";
			
			//Fix to implement cust_prod_id properly - By Joy
			com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
			Map<String,Object> paramMap = new HashMap<String,Object>(); 
			//paramMap.put("loggedinUserTO", loggedinUserTO);
			paramMap.put("loggedInCustomer",loggedinUserTO.getCustomerId());
			paramMap.put("loggedInOrgId",loggedinUserTO.getOrgId());
			
			if(adminYear == null){
				adminYear = (String) request.getSession().getAttribute(IApplicationConstants.ADMIN_YEAR);
				if(adminYear == null) {
					List<com.ctb.prism.core.transferobject.ObjectValueTO> customerProductList = adminService.getCustomerProduct(paramMap);
					for(com.ctb.prism.core.transferobject.ObjectValueTO object : customerProductList) {
						adminYear = object.getValue();
						break;
					}
				}
			}else{
				request.getSession().setAttribute(IApplicationConstants.ADMIN_YEAR, adminYear);
			}
			
			String moreCount = propertyLookup.get("count.results.button.more");
			logger.log(IAppLogger.INFO, "moreCount = " + moreCount);
			if (tenantId != null ) {
				parentTOs = parentService.getParentList(tenantId, adminYear, searchParam, orgMode, moreCount);
			}
			

			String parentJsonString = JsonUtil.convertToJsonAdmin(parentTOs);

			logger.log(IAppLogger.INFO, parentJsonString);
			response.setContentType("application/json");
			response.getWriter().write(parentJsonString);
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: getParentDetailsOnScroll");
		}

		return null;

	}	
	
	
	
	/**
	 * Fetching all users for selected organization.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getStudentDetailsOnScroll", method = RequestMethod.GET)
	public @ResponseBody
	String getStudentDetailsOnScroll(HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<StudentTO> studentTOs = new ArrayList<StudentTO>();
		try {
			logger.log(IAppLogger.INFO, "Enter: getStudentDetailsOnScroll");
			String moreCount = propertyLookup.get("count.results.button.more");
			logger.log(IAppLogger.INFO, "moreCount = " + moreCount);
			String adminYear = (String) request.getParameter("AdminYear");
			String scrollId = (String) request.getParameter("scrollId");
			String studentBioId = (String) request.getParameter("studentBioId");
			String isRedirectedTree = (String) request.getParameter("isRedirectedTree");
			String searchParam = request.getParameter("searchParam");
			String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			long currCustomer = (customer == null) ? 0 : Long.parseLong(customer);

			if ("Search".equals(searchParam))
				searchParam = "";
			if (scrollId != null && scrollId.trim().length() > 0) {
				String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);
				if (("Yes".equals(isRedirectedTree)) && ((studentBioId != null) && (studentBioId.trim().length() > 0))) {
					if (scrollId.lastIndexOf("|") > 0) {
						scrollId = scrollId.substring((scrollId.lastIndexOf("|") + 1), scrollId.length());
					}
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("studentBioId", request.getParameter("studentBioId"));
					paramMap.put("scrollId", scrollId);
					paramMap.put("customer", customer);
					paramMap.put("orgMode", orgMode);
					paramMap.put("adminYear", adminYear);
					paramMap.put("moreCount", moreCount);
					// studentTOs =  parentService.searchStudentOnRedirect(request.getParameter("studentBioId"), scrollId, Long.parseLong(customer));
					logger.log(IAppLogger.INFO, "Invoking: parentService.searchStudentOnRedirect(" + paramMap + ")");
					studentTOs = parentService.searchStudentOnRedirect(paramMap);
					// (String)request.getSession().getAttribute(IApplicationConstants.CURRORG)
				} else {
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("scrollId", scrollId);
					paramMap.put("adminYear", adminYear);
					paramMap.put("searchParam", searchParam);
					paramMap.put("currCustomer", currCustomer);
					paramMap.put("orgMode", orgMode);
					paramMap.put("moreCount", moreCount);
					// studentTOs = parentService.getStudentList(scrollId, adminYear, searchParam, currCustomer);
					studentTOs = parentService.getStudentList(paramMap);
				}
			}
			String studentJsonString = JsonUtil.convertToJsonAdmin(studentTOs);
			if(studentTOs != null) {
				logger.log(IAppLogger.INFO, "studentTOs.size() = "+ " " +studentTOs.size());
			} else {
				logger.log(IAppLogger.INFO, "studentTOs.size() = 0");
			}
			logger.log(IAppLogger.INFO, studentJsonString);
			response.setContentType("application/json");
			response.getWriter().write(studentJsonString);
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: getStudentDetailsOnScroll");
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
		logger.log(IAppLogger.INFO, "Enter: searchParentAutoComplete");
		try {
			String adminYear = (String) req.getParameter("AdminYear");
			//changed for TD 77154
			String orgMode = (String) req.getSession().getAttribute(IApplicationConstants.ORG_MODE);
			String parents = parentService.searchParentAutoComplete(req.getParameter("term"), 
					req.getParameter("selectedOrg"), adminYear, orgMode);
			logger.log(IAppLogger.INFO, "Selected organisation............."+req.getParameter("selectedOrg"));
			logger.log(IAppLogger.INFO, (String)req.getSession().getAttribute(IApplicationConstants.CURRORG));
			if ( parents != null ) {
				res.setContentType("application/json");
				res.getWriter().write(parents);
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: searchParentAutoComplete");
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
		logger.log(IAppLogger.INFO, "Enter: searchParent");
		try {
			ArrayList<ParentTO> parentsList=null;
			String adminYear = (String) req.getParameter("AdminYear");
			String parentName=req.getParameter("parentName");
			String selectedOrg=req.getParameter("selectedOrg");
			String isExactSeacrh=req.getParameter("isExactSeacrh");
			String orgMode = (String)req.getSession().getAttribute(IApplicationConstants.ORG_MODE);
			
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
						selectedOrg, adminYear,isExactSeacrh,orgMode);
			}else{
				parentsList = parentService.searchParent(parentName, 
						selectedOrg, adminYear,isExactSeacrh,orgMode);
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
			logger.log(IAppLogger.INFO, "Exit: searchParent");
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
			String testElementId = (String) req.getParameter("testElementId");
			//List<StudentTO> childrenList = parentService.getChildrenList((String) req.getSession().getAttribute(IApplicationConstants.CURRUSER));
			if(testElementId!= null)
			{
				List<StudentTO> studentAssessmentList = parentService.getAssessmentList(testElementId);
				if ( studentAssessmentList != null && studentAssessmentList.size() > 0){
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
		logger.log(IAppLogger.INFO, "Enter: searchStudentAutoComplete");
		String customer = (String) req.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		long currCustomer = (customer == null)? 0 : Long.parseLong(customer);
		
		try {
			String adminYear = (String) req.getParameter("AdminYear");
			String orgMode = (String) req.getSession().getAttribute(IApplicationConstants.ORG_MODE);
			
			String selectedOrg = req.getParameter("selectedOrg");
			String students = null;
			
			if(selectedOrg != null && !selectedOrg.equals("unspecified")) {
				students = parentService.searchStudentAutoComplete(req.getParameter("term"), 
						req.getParameter("selectedOrg"), adminYear,  currCustomer, orgMode);
				logger.log(IAppLogger.INFO, "searchStudentAutoComplete.....................................");
			} else {
				logger.log(IAppLogger.INFO, "No Org selected");
			}
			
			if ( students != null ) {
				res.setContentType("application/json");
				res.getWriter().write(students);
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: searchStudentAutoComplete");
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
		logger.log(IAppLogger.INFO, "Enter: searchStudent");
		String customer = (String) req.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		long currCustomer = (customer == null)? 0 : Long.parseLong(customer);
		
		try {
			ArrayList<StudentTO> studentsList = null;
			String adminYear = (String) req.getParameter("AdminYear");
			String studentName = req.getParameter("studentName");
			if(studentName != null && studentName.trim().length() == 0) {
				studentName = "%%";
			}
			String browser = req.getParameter("browser");
			String selectedOrg = req.getParameter("selectedOrg");
			String orgMode = (String) req.getSession().getAttribute(IApplicationConstants.ORG_MODE);
			if (selectedOrg != null && !selectedOrg.equals("unspecified")) {
				
				if(("ie7".equals(browser)) && ("Search".equals(studentName))) {
					studentName = "";
					studentsList = parentService.searchStudent(studentName, 
							selectedOrg, adminYear,currCustomer, orgMode);				
				}
				else {
					studentsList = parentService.searchStudent(studentName, 
							selectedOrg, adminYear, currCustomer, orgMode);				
				}
				
			} else {
				logger.log(IAppLogger.INFO, "No Org selected");
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
			logger.log(IAppLogger.INFO, "Exit: searchStudent");
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
	public ModelAndView redirectToStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<StudentTO> studentTOs = new ArrayList<StudentTO>();
		List<OrgTreeTO> orgTreeTOs = new ArrayList<OrgTreeTO>();
		ModelAndView modelAndView = new ModelAndView("parent/manageStudent");
		String hierarchialOrgIds = null;
		String adminYear = (String) request.getParameter("AdminYear");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> customerProductList = null;
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		long currCustomer = (customer == null) ? 0 : Long.parseLong(customer);
		String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);

		try {
			logger.log(IAppLogger.INFO, "Enter: redirectToStudent");
			logger.log(IAppLogger.INFO, "STUDENT BIO ID.................." + request.getParameter("studentBioId"));
			String nodeId = request.getParameter("nodeId");
			String currentOrg = (String) request.getSession().getAttribute(IApplicationConstants.CURRORG);
			String userId = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSERID);
			// String userName=(String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
			com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			//paramMap.put("loggedinUserTO", loggedinUserTO);
			paramMap.put("loggedInCustomer",loggedinUserTO.getCustomerId());
			paramMap.put("loggedInOrgId",loggedinUserTO.getOrgId());
			
			if (request.getParameter("studentBioId") != null) {
				if (nodeId != null) {
					customerProductList = adminService.getCustomerProduct(paramMap);

					if (adminYear == null) {
						adminYear = (String) request.getSession().getAttribute(IApplicationConstants.ADMIN_YEAR);
						if (adminYear == null) {
							for (com.ctb.prism.core.transferobject.ObjectValueTO object : customerProductList) {
								adminYear = object.getValue();
								break;
							}
						}
					} else {
						request.getSession().setAttribute(IApplicationConstants.ADMIN_YEAR, adminYear);
					}
				}
				modelAndView.addObject("adminList", customerProductList);
				Map<String,Object> paramMapA = new HashMap<String,Object>(); 
				if (isTree) {					
					paramMapA.put("nodeid", currentOrg);
					paramMapA.put("currOrg", currentOrg);
					paramMapA.put("isFirstLoad", true);
					paramMapA.put("adminYear", adminYear);
					paramMapA.put("customerId", currCustomer+"");
					paramMapA.put("orgMode", orgMode);
					orgTreeTOs = adminService.getOrganizationTree(paramMapA);
					paramMapA.put("nodeid", nodeId);
					//paramMapA.put("userId", userId);
					paramMapA.put("isRedirected", true);
					hierarchialOrgIds = adminService.getOrganizationTreeOnRedirect(paramMapA);
					modelAndView.addObject("hierarchialOrgIds", hierarchialOrgIds);
					logger.log(IAppLogger.DEBUG, "hierarchialOrgIds................" + hierarchialOrgIds);
					// modelAndView.addObject("organizationTreeList", orgTreeTOs);
					modelAndView.addObject("treeSturcture", "Yes");
					modelAndView.addObject("isRedirected", "Yes");
					modelAndView.addObject("studentBioId", request.getParameter("studentBioId"));
					// orgJsonString = JsonUtil.convertToJsonAdmin(orgTreeTOs);
					// modelAndView.addObject("orgName", orgTreeTOs.get(0).getData());
					modelAndView.addObject("rootOrgId", orgTreeTOs.get(0).getMetadata().getParentTenantId());
				}
				// studentTOs = parentService.searchStudentOnRedirect(request.getParameter("studentBioId"), (String) request.getSession().getAttribute(IApplicationConstants.CURRORG), currCustomer);
				Map<String, Object> parameterMap = new HashMap<String, Object>();
				parameterMap.put("studentBioId", request.getParameter("studentBioId"));
				parameterMap.put("scrollId", request.getSession().getAttribute(IApplicationConstants.CURRORG));
				parameterMap.put("customer", currCustomer);
				parameterMap.put("orgMode", orgMode);
				parameterMap.put("adminYear", adminYear);
				logger.log(IAppLogger.INFO, "Invoking: parentService.searchStudentOnRedirect(" + paramMapA + ")");
				studentTOs = parentService.searchStudentOnRedirect(parameterMap);
				logger.log(IAppLogger.INFO, "LOADING STUDENT STUDENT PAGE ON CLICK..................");
			}
			String studentJsonString = JsonUtil.convertToJsonAdmin(studentTOs);
			logger.log(IAppLogger.INFO, studentJsonString);

			modelAndView.addObject("studentList", studentTOs);

		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		} finally {
			logger.log(IAppLogger.INFO, "Exit: redirectToStudent");
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
			logger.log(IAppLogger.INFO, "Enter: updateAssessmentDetails");

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

			logger.log(IAppLogger.INFO, "Exit: updateAssessmentDetails");

		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error Saving File", e);
		}
		return null;
	}
	
	/**
	 * Method performs reset password operation. This method is called through AJAX.
	 * 
	 * @param req
	 * @param res
	 * @return Returns nothing, instead writes password in response as JSON
	 */
	@RequestMapping(value="/resetPassword", method=RequestMethod.GET)
	public String resetPassword(HttpServletRequest req, HttpServletResponse res ) {
		logger.log(IAppLogger.INFO, "Enter: AdminController - resetPassword");
		String sendEmailFlag = "0";
		Map<String, Object> paramMap = new HashMap<String, Object>();	
		String pwd = "";
		try {
			String userName= (String) req.getParameter("userName");
			if ( userName != null ) {
			//	String newPassword = adminService.resetPassword(userName);
				paramMap.put("username", userName);
				paramMap.put("contractName", Utils.getContractName());
				com.ctb.prism.login.transferobject.UserTO userTO = adminService.resetPassword(paramMap);
				if(userTO != null) pwd = userTO.getPassword();
				if (userTO.getUserEmail() != null && userTO.getPassword() != null) {
					try{
						emailSender.sendUserPasswordEmail(userTO.getUserEmail(),null,userTO.getPassword());
						sendEmailFlag = "1";
					} catch (Exception e) {
						sendEmailFlag = "0";
						logger.log(IAppLogger.ERROR, e.getMessage(), e);
					}										
				}
			}
			
			res.getWriter().write("{\"sendEmailFlag\":\"" + sendEmailFlag + "\",\"password\":\"" + pwd + "\"}");
			
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
			
			//To retrieve the actions for a particular user and a cust_prod_id - By Joy
			//If no. of parameters are changed, please update validateUser() of LoginController
			String userId = (String)req.getSession().getAttribute(IApplicationConstants.CURRUSERID);
			Map<String,Object> actionParamMap = new HashMap<String,Object>();
			actionParamMap.put("userId", userId);
			actionParamMap.put("custProdId", adminYear);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} 
		logger.log(IAppLogger.INFO, "Exit: AdminController - updateAdminYear");
		return null;
	}	
	
	@RequestMapping(value="/resetPrismActions", method=RequestMethod.GET)
	@ResponseBody
	public String resetPrismActions(HttpServletRequest req, HttpServletResponse res ) {
		String actionJson = "";
		logger.log(IAppLogger.INFO, "Enter: resetPrismActions()");
		try {
			//String userId = (String)req.getSession().getAttribute(IApplicationConstants.CURRUSERID);
			String roles = (String)req.getSession().getAttribute(IApplicationConstants.CURR_USER_ROLES);
			String orgNodeLevel = String.valueOf((Long)req.getSession().getAttribute(IApplicationConstants.CURRORGLVL));
			String custProdId = req.getParameter("AdminYear").equals("0")
					?String.valueOf((Long)req.getSession().getAttribute(IApplicationConstants.DEFAULT_CUST_PROD_ID))
					:req.getParameter("AdminYear");
					
			Map<String,Object> actionParamMap = new HashMap<String,Object>();
			actionParamMap.put("roles", roles.toString());
			/*if (IApplicationConstants.EDU_USER_FLAG.equals(user.getUserType())) {
				actionParamMap.put("orgNodeLevel", IApplicationConstants.DEFAULT_LEVELID_VALUE);
			} else {
				actionParamMap.put("orgNodeLevel", orgNodeLevel);
			}*/
			actionParamMap.put("orgNodeLevel", orgNodeLevel);
			actionParamMap.put("custProdId", custProdId);
			
			
			Map<String, String> actionMap= loginService.getActionMap(actionParamMap);
			req.getSession().setAttribute(IApplicationConstants.ACTION_MAP_SESSION, actionMap);
			actionJson = Utils.objectToJson(actionMap);
			logger.log(IAppLogger.INFO, "actionJson = " + actionJson);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		} 
		logger.log(IAppLogger.INFO, "Exit: AdminController - resetPrismActions()");
		return actionJson;
	}
	
	/**
	 * Method regenerate the invitation code for a student. 
	 * This method is called through AJAX
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/regenerateActivationCode", method = RequestMethod.GET)
	public String regenerateActivationCode(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		boolean success = Boolean.FALSE;
		String status = "";
		try {
			String studentBioId = request.getParameter("studentBioId");
			String adminYear = request.getParameter("adminYear");
			String invitationCode = request.getParameter("invitationCode");
			String testElementId = request.getParameter("testElementId");
			
			// URI to generate modified IC Letter
			StringBuffer uriBuff = new StringBuffer();
			/*uriBuff.append(request.getScheme());
			uriBuff.append("://");
			uriBuff.append(request.getServerName());
			uriBuff.append(":");
			uriBuff.append(request.getServerPort());
			uriBuff.append(request.getContextPath());*/
			uriBuff.append(propertyLookup.get("icDownloadUrl"));
			uriBuff.append("icDownload.do");
			uriBuff.append("?reportUrl=/public/PN/Report/Invitation_pdf_files");
			uriBuff.append("&type=pdf&token=0&filter=true&p_L3_Jasper_Org_Id=-1");
			uriBuff.append("&drillDown=true&assessmentId=105_InvLetter&p_AdminYear=-1");
			uriBuff.append("&p_Student_Bio_Id=").append(testElementId);
			uriBuff.append("&contractName=").append(Utils.getContractName());
			logger.log(IAppLogger.INFO, uriBuff.toString());
		
			StudentTO student = new StudentTO();
			student.setStudentBioId((studentBioId==null) ? 0 : Long.parseLong(studentBioId));
			student.setAdminid(adminYear);
			student.setInvitationcode(invitationCode);
			student.setTestElementId(testElementId);
			student.setIcLetterUri(uriBuff.toString());
			
			success = parentService.regenerateActivationCode(student);
		} catch (Exception e) {
			success = Boolean.FALSE;
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		}finally{
			status = (success)? "Success" : "Failed";
			response.setContentType("text/plain");
			response.getWriter().write("{\"status\":\"" + status + "\"}");
		}

		return null;
	}
	
	@RequestMapping(value = "/studentFileDownload", method = RequestMethod.GET)
	public ModelAndView studentFileDownload(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("admin/studentFileDownload");
		Map<String, Object> propertyMap = (Map<String,Object>) request.getSession().getAttribute("propertyMap");
		if(propertyMap == null){
			Map<String, Object> tileParamMap = new HashMap<String, Object>();
			tileParamMap.put("contractName", Utils.getContractName());
			propertyMap = loginService.getContractProerty(tileParamMap);
			request.getSession().setAttribute("propertyMap", propertyMap);
		}
		String tascStudentDataFileLayoutHref = propertyMap.get(IApplicationConstants.STATIC_PDF_LOCATION) + File.separator +propertyLookup.get("S3KEY_LAYOUTS") + propertyLookup.get(IApplicationConstants.TASC_STUDENT_DATA_FILE_LAYOUT);
		logger.log(IAppLogger.INFO, "tascStudentDataFileLayoutHref = " + tascStudentDataFileLayoutHref);
		modelAndView.addObject("tascStudentDataFileLayoutHref", tascStudentDataFileLayoutHref);
		return modelAndView;
	}
	
	@RequestMapping(value = "/downloadStudentFile", method = RequestMethod.GET)
	public @ResponseBody String downloadStudentFile(HttpServletRequest request, HttpServletResponse response) {
		logger.log(IAppLogger.INFO, "Enter: downloadStudentFile");
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String userId = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSERID);
			String currentUser = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSER);
			String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			String startDate = (String) request.getParameter("startDate");
			String endDate = (String) request.getParameter("endDate");
			String fileType = (String) request.getParameter("type");
			String dateType = request.getParameter("dateType");
			paramMap.put("userId", userId);
			paramMap.put("startDate", startDate);
			paramMap.put("endDate", endDate);
			paramMap.put("dateType", dateType);
			
			/* For On demand Student XML file generation we don't need to insert data in Job tracking from here as 
			 * this will be handel in the PKG_DATA_EXTRACT.SP_CUSTOMER_STD_EXTRACT_ONLINE procedure*/
			if(fileType.equals("XML")){
				paramMap.put("customerId", customer);
				paramMap.put("contractName", Utils.getContractName()); // needed as this will be async
				usabilityService.generateStudentXMLExtract(paramMap);
			} else {
				JobTrackingTO jobTrackingTO = new JobTrackingTO(); 
				String docName = CustomStringUtil.appendString(currentUser, "_" ,Utils.getDateTime(), "_Querysheet.pdf");
	            jobTrackingTO.setJobName(docName);
	            jobTrackingTO.setUserId((String) request.getSession().getAttribute(
	            		IApplicationConstants.CURRUSERID));
	            String username = currentUser;
	            if(username != null && username.indexOf(RESTAuthenticationFilter.RANDOM_STRING) != -1) {
	            	// remove random character for SSO users
	            	username = username.substring(0, username.indexOf(RESTAuthenticationFilter.RANDOM_STRING));
	            }
	            jobTrackingTO.setRequestFilename(CustomStringUtil.appendString(
	            		username, "_", customer, "_StudentDataFile_", Utils.getDateTime("yyyyMMdd.HHmmss.SSS"), ".", fileType)
	                    );
	            //jobTrackingTO.setAdminId(request.getParameter("p_Admin_Name"));
	            jobTrackingTO.setExtractCategory(EXTRACT_CATEGORY.valueOf(dateType).toString());
	            jobTrackingTO.setRequestType(REQUEST_TYPE.SDF.toString());  
	            jobTrackingTO.setExtractFiletype(EXTRACT_FILETYPE.valueOf(fileType).toString());
	            String type = ("TTD".equals(dateType))? "Test Taken Date" : "Last Updated Date";
	            jobTrackingTO.setRequestSummary(CustomStringUtil.appendString(
	            		"Download student data file in ", fileType, " format.",
	            		//"\n\nRequest parameters:",
	            		"\n\nDate Type: ", type,
	            		"\n\nStart Date: ", startDate,
	            		"\n\nEnd Date: ", endDate));
	            jobTrackingTO.setJobStatus(JOB_STATUS.SU.toString()); 
	            jobTrackingTO.setCustomerId(customer);
	            jobTrackingTO.setExtractStartdate(startDate);
	            jobTrackingTO.setExtractEnddate(endDate);
	             
				jobTrackingTO = usabilityService.insertIntoJobTracking(jobTrackingTO); 
			}
						
			
			response.setContentType("application/json");
			response.getWriter().write( "{\"status\":\"Success\"}" );
			/*
			 * Student data file generation is moved to ETL 
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
			*/
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "", e);
			e.printStackTrace();
		} 
		logger.log(IAppLogger.INFO, "Exit: downloadStudentFile");
		return null;
	}
	
	/**
	 * Retrive File size
	 * 
	 * @param bulkDownloadTO
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getFileSize", method = RequestMethod.GET)
	public void getFileSize(HttpServletRequest request, HttpServletResponse response) {
		logger.log(IAppLogger.INFO, "Start: getFileSize()");
		JobTrackingTO jobTrackingTO = new JobTrackingTO();
		List<JobTrackingTO> fileDetails = new ArrayList<JobTrackingTO>();
		String jsonString = null;
		String jobId = (String) request.getParameter("jobId");
		String filePath = (String) request.getParameter("filePath");
		String fileName = (String) request.getParameter("fileName");
		String envString =  null;
		String FOLDER_SUFFIX = "/";
		String customer = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		
		try {
			if(jobId!=null && jobId.trim().length() > 0 ){
				jobTrackingTO = usabilityService.getFileSize(jobId);
			}
			
			if (jobTrackingTO.getFileSize() != null) {
				fileDetails.add(jobTrackingTO);
			} else {
				logger.log(IAppLogger.INFO, "retrieving FileSize() of " + filePath);
				
				if(fileName.endsWith(".xml")) {
					
					@SuppressWarnings("unchecked")
					Map<String, Object> propertyMap = (Map<String,Object>)request.getSession().getAttribute("propertyMap");
					if(propertyMap == null){
						Map<String, Object> tileParamMap = new HashMap<String, Object>();
						tileParamMap.put("contractName", Utils.getContractName());
						propertyMap = loginService.getContractProerty(tileParamMap);
						request.getSession().setAttribute("propertyMap", propertyMap);
					}
					
					envString = (String) propertyMap.get(IApplicationConstants.STATIC_PDF_LOCATION) + IApplicationConstants.SDF_XML_S3_LOCATION;
					filePath = envString + fileName;
				}
				try{
					byte[] fileBytes =repositoryService.getAssetBytes(filePath);
					if(fileBytes.length > 0){
						jobTrackingTO.setFileSize(FileUtil.humanReadableByteCount(fileBytes.length));
						jobTrackingTO.setJobId(Long.parseLong(jobId));
						jobTrackingTO.setFilePath(filePath);
						jobTrackingTO.setRequestFilename(fileName);
						fileDetails.add(jobTrackingTO);
						jobTrackingTO = usabilityService.updateFileSize(jobTrackingTO);
					}	
				} catch(Exception e) {
					/*File need to be created from studentdata_extract clob and upload into s3*/
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("customer", customer);
					paramMap.put("jobId",jobId);
					StudentDataExtractTO studentDataExtractTO = usabilityService.getClobXMLFile(paramMap);
					String studentXML = studentDataExtractTO.getStudentDataXML();

					String fileLocation = CustomStringUtil.appendString(propertyLookup.get("xmlGenPath"), fileName);
					
					//fileLocation = fileLocation.replace("//", "/");
					String tempDirectory = FileUtil.getDirFromFilePath(fileLocation);
					File tempJobDirectory = new File(tempDirectory);
					
					if (!tempJobDirectory.exists()) {
						logger.log(IAppLogger.INFO, "Creating directory ... " + tempJobDirectory);
						boolean status = tempJobDirectory.mkdir();
						logger.log(IAppLogger.INFO, tempJobDirectory + " : " + status);
					} else {
						logger.log(IAppLogger.INFO, "Directory exists: " + tempJobDirectory);
					}
					File tempFile = new File(fileLocation);
					FileUtil.createFile(fileLocation, studentXML.getBytes());
					repositoryService.uploadAsset(envString, tempFile);
					
					jobTrackingTO = new JobTrackingTO();
					jobTrackingTO.setJobId(Long.valueOf(jobId));
					jobTrackingTO.setFileSize(FileUtil.fileSize(studentXML.getBytes()));
					jobTrackingTO.setFilePath(filePath);
					jobTrackingTO.setRequestFilename(fileName);
					fileDetails.add(jobTrackingTO);
					jobTrackingTO = usabilityService.updateFileSize(jobTrackingTO);					
					
					//Time to delete the temporary file
					boolean fileDeleteFlag = FileUtils.deleteQuietly(tempFile); //delete temp file
					//tempJobDirectory.delete(); //delete temp directory
					if(fileDeleteFlag){
						logger.log(IAppLogger.INFO, "Temp file has been deleted successfully: " + fileName);
					}else{
						logger.log(IAppLogger.INFO, "Unable to delete Temp file: " + fileName);
					}
					
				}
			}
			if (fileDetails.size() != 0) {
				jsonString = JsonUtil.convertToJsonAdmin(fileDetails);
				response.setContentType("application/json");
				response.getWriter().write(jsonString);
			} else {
				logger.log(IAppLogger.INFO, "File does not exists!");
			}
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
		}
		logger.log(IAppLogger.INFO, "Exit: getFileSize()");
	}
	
	/**
	 * Show list of all education center
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@Secured({"ROLE_EDU_ADMIN","ROLE_SUPER", "ROLE_CTB"})
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
	 * Show list of all education center users.
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws Exception
	 */
	//@Secured({"ROLE_EDU_ADMIN","ROLE_SUPER", "ROLE_CTB"}) - Not Required
	@RequestMapping(value = "/loadEduCenterUsers", method = RequestMethod.GET)
	public @ResponseBody void loadEduCenterUsers(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: AdminController - loadEduCenterUsers()");
		String moreCount = propertyLookup.get("count.results.button.more");
		logger.log(IAppLogger.INFO, "moreCount = " + moreCount);
		String userJsonString = "";
		long eduCenterId = Long.parseLong(request.getParameter("eduCenterId"));
		String searchParam = (String) request.getParameter("searchParam");
		if ("Search".equals(searchParam)) {
			searchParam = "";
		}
		String lastEduCenterId_username = (String) request.getParameter("lastEduCenterId_username");
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) request.getSession().getAttribute(
				IApplicationConstants.LOGGEDIN_USER_DETAILS);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loggedinUserTO", loggedinUserTO);
		paramMap.put("eduCenterId", eduCenterId);
		paramMap.put("searchParam", searchParam);
		paramMap.put("lastEduCenterId_username", lastEduCenterId_username);
		paramMap.put("moreCount", moreCount);
		List<EduCenterTO> eduCenterTOList = null;
		try {
			eduCenterTOList = adminService.loadEduCenterUsers(paramMap);
			List<UserTO> userTOList = convertEduToUserList(eduCenterTOList);
			userJsonString = JsonUtil.convertToJsonAdmin(userTOList);
			logger.log(IAppLogger.INFO, "loadEduCenterUsers():" + userJsonString);
			response.setContentType("application/json");
			response.getWriter().write(userJsonString);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}
		logger.log(IAppLogger.INFO, "Exit: AdminController - loadEduCenterUsers()");
	}

	/**
	 * @param eduCenterTOList
	 * @return
	 */
	private List<UserTO> convertEduToUserList(List<EduCenterTO> eduCenterTOList) {
		List<UserTO> userTOList = new ArrayList<UserTO>();
		UserTO userTO = null;
		for (EduCenterTO eduCenterTO : eduCenterTOList) {
			userTO = new UserTO();
			userTO.setUserId(eduCenterTO.getUserId());
			userTO.setAvailableRoleList(eduCenterTO.getRoleList());
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
		String orgMode = (String) request.getSession().getAttribute(IApplicationConstants.ORG_MODE);

		paramMap.put("tenantId", tenantId);
		paramMap.put("adminYear", adminYear);
		paramMap.put("userId", userId);
		paramMap.put("orgMode", orgMode);

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

	/**
	 * Display the Reset Password form.
	 * 
	 * @return
	 */
	@Secured({"ROLE_SUPER", "ROLE_CTB"})
	@RequestMapping(value = "/resetUserPasswordForm", method = RequestMethod.GET)
	public ModelAndView resetUserPasswordForm() {
		logger.log(IAppLogger.INFO, "Enter: resetUserPasswordForm()");
		ModelAndView modelAndView = new ModelAndView("admin/resetUserPasswordForm");
		logger.log(IAppLogger.INFO, "Exit: resetUserPasswordForm()");
		return modelAndView;
	}

	/**
	 * Gets the user for Reset Password.
	 * 
	 * @param request
	 * @return
	 */
	@Secured({"ROLE_SUPER", "ROLE_CTB"})
	@RequestMapping(value = "/getUserForResetPassword", method = RequestMethod.GET)
	@ResponseBody
	public String getUserForResetPassword(HttpServletRequest request) {
		logger.log(IAppLogger.INFO, "Enter: getUserForResetPassword()");
		String username = (String) request.getParameter("username");
		String currentOrg = (String) request.getSession().getAttribute(IApplicationConstants.CURRORG);
		Object currOrgLvl = request.getSession().getAttribute(IApplicationConstants.CURRORGLVL);
		String currentOrgLvl = ((Long) currOrgLvl == null ? "0" : currOrgLvl).toString();
		logger.log(IAppLogger.INFO, "currentOrgLvl = " + currentOrgLvl);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("username", username);
		paramMap.put("currentOrg", currentOrg);
		paramMap.put("currentOrgLvl", currentOrgLvl);
		UserTO user = new UserTO();
		try {
			user = adminService.getUserForResetPassword(paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String jsonString = Utils.objectToJson(user);
		logger.log(IAppLogger.INFO, jsonString);
		logger.log(IAppLogger.INFO, "Exit: getUserForResetPassword()");
		return jsonString;
	}

	/**
	 * Reset password for the user.
	 * 
	 * @param request
	 * @return
	 */
	@Secured({"ROLE_SUPER", "ROLE_CTB"})
	@RequestMapping(value = "/resetUserPassword", method = RequestMethod.GET)
	@ResponseBody
	public String resetUserPassword(HttpServletRequest request) {
		logger.log(IAppLogger.INFO, "Enter: resetUserPassword()");
		String username = (String) request.getParameter("username");
		String email = (String) request.getParameter("email");
	//	String password = "";
		String resetPwdFlag = "0";
		String sendEmailFlag = "0";
		Map<String, Object> paramMap = new HashMap<String, Object>();		
		try {
			if (username != null) {
				//password = adminService.resetPassword(username);
				paramMap.put("username", username);
				paramMap.put("contractName", Utils.getContractName());
				com.ctb.prism.login.transferobject.UserTO userTO = adminService.resetPassword(paramMap);
				if (userTO.getPassword() != null) {
					resetPwdFlag = "1";
					try {
						emailSender.sendUserPasswordEmail(email, username, userTO.getPassword());
						sendEmailFlag = "1";
					} catch (Exception e) {
						sendEmailFlag = "0";
						logger.log(IAppLogger.ERROR, e.getMessage(), e);
					}
				}
			}
		} catch (Exception e) {
			resetPwdFlag = "0";
			logger.log(IAppLogger.ERROR, e.getMessage(), e);
		}
	
		String jsonString = "{\"username\" : \"" + username
				+ "\", \"resetPwdFlag\" : \"" + resetPwdFlag
				+ "\", \"sendEmailFlag\" : \"" + sendEmailFlag + "\"}";
		logger.log(IAppLogger.INFO, jsonString);
		logger.log(IAppLogger.INFO, "Exit: resetUserPasswordForm()");
		return jsonString;
	}
}
