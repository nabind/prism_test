package com.ctb.prism.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.service.IParentService;
import com.ctb.prism.parent.transferobject.ManageContentTO;
import com.ctb.prism.web.util.JsonUtil;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.google.gson.Gson;

@Controller
public class ManageContentController {
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(ManageContentController.class.getName());
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private IParentService parentService;
	
	@Autowired
	private IPropertyLookup propertyLookup;
	
	@RequestMapping(value = "/manageContent", method = RequestMethod.GET)
	public ModelAndView getManageContentFilter(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException,BusinessException{
		
		logger.log(IAppLogger.INFO, "Enter: ManageContentController - getReportMessageFilter()");
		long t1 = System.currentTimeMillis();
		Map<String,Object> serviceMapManageContentFilter = null;
		ModelAndView modelAndView = new ModelAndView("parent/manageContent");
		UserTO loggedinUserTO = (UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("loggedinUserTO", loggedinUserTO);
		try{
			if(loggedinUserTO != null){
				serviceMapManageContentFilter = parentService.getManageContentFilter(paramMap);
				modelAndView.addObject("serviceMapManageContentFilter",serviceMapManageContentFilter);
			}
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ManageContentController - getReportMessageFilter() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return modelAndView;
	}
	
	@RequestMapping(value = "/populateGrade", method = RequestMethod.GET)
	public void populateGrade(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException,BusinessException {
		
		logger.log(IAppLogger.INFO, "Enter: ManageContentController - populateGrade()");
		long t1 = System.currentTimeMillis();
		response.setContentType("text/plain");
		long custProdId = Long.parseLong(request.getParameter("custProdId"));
		List<com.ctb.prism.core.transferobject.ObjectValueTO> gradeList = null;
		String jsonString = "";
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("custProdId", custProdId);
		try{
			gradeList =  parentService.populateGrade(paramMap);
			jsonString = JsonUtil.convertToJsonAdmin(gradeList);
			logger.log(IAppLogger.INFO, "jsonString for custProdId: "+custProdId);
			logger.log(IAppLogger.INFO, jsonString);
			response.getWriter().write(jsonString);
	    }catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ManageContentController - populateGrade() took time: "+String.valueOf(t2 - t1)+"ms");
		}
    }
	
	@RequestMapping(value = "/populateSubtest", method = RequestMethod.GET)
	public void populateSubtest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException,BusinessException {
		
		logger.log(IAppLogger.INFO, "Enter: ManageContentController - populateSubtest()");
		long t1 = System.currentTimeMillis();
		response.setContentType("text/plain");
		long custProdId = Long.parseLong(request.getParameter("custProdId"));
		long gradeId = Long.parseLong(request.getParameter("gradeId"));
		List<com.ctb.prism.core.transferobject.ObjectValueTO> subtestList = null;
		String jsonString = "";
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("custProdId", custProdId);
		paramMap.put("gradeId", gradeId);
		try{
			subtestList =  parentService.populateSubtest(paramMap);
			jsonString = JsonUtil.convertToJsonAdmin(subtestList);
			logger.log(IAppLogger.INFO, "jsonString for gradeId: "+gradeId);
			logger.log(IAppLogger.INFO, jsonString);
			response.getWriter().write(jsonString);
	    }catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ManageContentController - populateSubtest() took time: "+String.valueOf(t2 - t1)+"ms");
		}
    }
	
	@RequestMapping(value = "/populateObjective", method = RequestMethod.GET)
	public void populateObjective(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException,BusinessException {
		
		logger.log(IAppLogger.INFO, "Enter: ManageContentController - populateObjective()");
		long t1 = System.currentTimeMillis();
		response.setContentType("text/plain");
		long subtestId = Long.parseLong(request.getParameter("subtestId"));
		long gradeId = Long.parseLong(request.getParameter("gradeId"));
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectiveList = null;
		String jsonString = "";
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("subtestId", subtestId);
		paramMap.put("gradeId", gradeId);
		try{
			objectiveList =  parentService.populateObjective(paramMap);
			jsonString = JsonUtil.convertToJsonAdmin(objectiveList);
			logger.log(IAppLogger.INFO, "jsonString for subtestId: "+subtestId+" and gradeId:"+gradeId);
			logger.log(IAppLogger.INFO, jsonString);
			response.getWriter().write(jsonString);
	    }catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ManageContentController - populateObjective() took time: "+String.valueOf(t2 - t1)+"ms");
		}
    }
	
	/**
	 * To add a content
	 * @return
	 */
	@RequestMapping(value = "/addNewContent", method = RequestMethod.POST)
    public void addNewContent(@ModelAttribute("manageContentTO") ManageContentTO manageContentTO,
    		HttpServletRequest request, HttpServletResponse response) throws BusinessException, IOException {
		logger.log(IAppLogger.INFO, "Enter: ManageContentController - addNewContent()");
		long t1 = System.currentTimeMillis();
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("manageContentTO", manageContentTO);
		com.ctb.prism.core.transferobject.ObjectValueTO statusTO = null;
		Gson gson = new Gson();
		String jsonString = "";
		try{
			statusTO = parentService.addNewContent(paramMap); 
			jsonString = gson.toJson(statusTO);
			logger.log(IAppLogger.INFO, "jsonString of status:");
			logger.log(IAppLogger.INFO, jsonString);
			response.getWriter().write(jsonString);
	    }catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ManageContentController - addNewContent() took time: "+String.valueOf(t2 - t1)+"ms");
		}
    }
	
	
	/**
	 * @author TCS
	 * Controller method for manage content datatable population 
	 * @return
	 */
	@RequestMapping(value = "/loadManageContent", method = RequestMethod.GET)
	public void loadManageContent(HttpServletRequest req,
			HttpServletResponse res) throws ServletException, IOException,BusinessException {
		
		logger.log(IAppLogger.INFO, "Enter: ManageContentController - loadManageContent");
		long t1 = System.currentTimeMillis();
		UserTO loggedinUserTO = (UserTO) req.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("loggedinUserTO", loggedinUserTO);
		paramMap.put("custProdId",req.getParameter("custProdId"));
		paramMap.put("subtestId",req.getParameter("subtestId"));
		paramMap.put("objectiveId",req.getParameter("objectiveId"));
		paramMap.put("contentTypeId",req.getParameter("contentTypeId"));
		paramMap.put("lastid",req.getParameter("lastid"));
		paramMap.put("checkFirstLoad",req.getParameter("checkFirstLoad"));
		
		List<ManageContentTO> loadManageContentList = null;
		String loadManageContentJson = "";
		try{
			loadManageContentList = parentService.loadManageContent(paramMap);
			loadManageContentJson = JsonUtil.convertToJsonAdmin(loadManageContentList);
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			logger.log(IAppLogger.INFO, "jsonString of loadManageContent:");
			logger.log(IAppLogger.INFO, loadManageContentJson);
			res.setContentType("application/json");
			res.getWriter().write(loadManageContentJson);
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ManageContentController - loadManageContent() took time: "+String.valueOf(t2 - t1)+"ms");
		}
	}
	
	/**
	 * Get content details for edit depending upon article_metedata id
	 */
	@RequestMapping(value = "/getContentForEdit", method = RequestMethod.GET)
    public void getContentForEdit(HttpServletRequest request, HttpServletResponse response) throws BusinessException, IOException {
		logger.log(IAppLogger.INFO, "Enter: ManageContentController - getContentForEdit()");
		long t1 = System.currentTimeMillis();
		long contentId = Long.parseLong(request.getParameter("contentId"));
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("contentId", contentId);
		ManageContentTO manageContentTO = null;
		Gson gson = new Gson();
		String jsonString = "";
		try{
			manageContentTO = parentService.getContentForEdit(paramMap); 
			jsonString = gson.toJson(manageContentTO);
			logger.log(IAppLogger.INFO, "jsonString of status:");
			logger.log(IAppLogger.INFO, jsonString);
			response.getWriter().write(jsonString);
	    }catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ManageContentController - getContentForEdit() took time: "+String.valueOf(t2 - t1)+"ms");
		}
    }
	
	/**
	 * Update content/article along with metadata
	 */
	@RequestMapping(value = "/updateContent", method = RequestMethod.POST)
    public void updateContent(@ModelAttribute("manageContentTO") ManageContentTO manageContentTO,
    		HttpServletRequest request, HttpServletResponse response) throws BusinessException, IOException {
		logger.log(IAppLogger.INFO, "Enter: ManageContentController - updateContent()");
		long t1 = System.currentTimeMillis();
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("manageContentTO", manageContentTO);
		com.ctb.prism.core.transferobject.ObjectValueTO statusTO = null;
		Gson gson = new Gson();
		String jsonString = "";
		try{
			statusTO = parentService.updateContent(paramMap); 
			jsonString = gson.toJson(statusTO);
			logger.log(IAppLogger.INFO, "jsonString of status:");
			logger.log(IAppLogger.INFO, jsonString);
			response.getWriter().write(jsonString);
	    }catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ManageContentController - updateContent() took time: "+String.valueOf(t2 - t1)+"ms");
		}
    }
	
	/**
	 * Delete content/article's meta data and delete content/article if no association present with another mete data.
	 */
	@RequestMapping(value = "/deleteContent", method = RequestMethod.GET)
    public void deleteContent(HttpServletRequest request, HttpServletResponse response) throws BusinessException, IOException {
		logger.log(IAppLogger.INFO, "Enter: ManageContentController - deleteContent()");
		long t1 = System.currentTimeMillis();
		long contentId = Long.parseLong(request.getParameter("contentId"));
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("contentId", contentId);
		com.ctb.prism.core.transferobject.ObjectValueTO statusTO = null;
		Gson gson = new Gson();
		String jsonString = "";
		try{
			statusTO = parentService.deleteContent(paramMap); 
			jsonString = gson.toJson(statusTO);
			logger.log(IAppLogger.INFO, "jsonString of status:");
			logger.log(IAppLogger.INFO, jsonString);
			response.getWriter().write(jsonString);
	    }catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ManageContentController - deleteContent() took time: "+String.valueOf(t2 - t1)+"ms");
		}
    }
}
