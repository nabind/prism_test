/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package com.ctb.prism.report.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.data.cache.ColumnDataCacheHandler;
import net.sf.jasperreports.data.cache.DataCacheHandler;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.fill.AsynchronousFillHandle;
/** PRISM **/
//import net.sf.jasperreports.repo.RepositoryUtil;
/** end PRISM **/
import net.sf.jasperreports.web.JRInteractiveException;
import net.sf.jasperreports.web.servlets.AsyncJasperPrintAccessor;
/** PRISM **/
//import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.servlets.JasperPrintAccessor;
import net.sf.jasperreports.web.servlets.SimpleJasperPrintAccessor;
/** end PRISM **/
import net.sf.jasperreports.web.actions.AbstractAction;
import net.sf.jasperreports.web.actions.Action;
import net.sf.jasperreports.web.commands.CommandStack;
import net.sf.jasperreports.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.theme.CookieThemeResolver;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.util.ApplicationContextProvider;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.report.service.IReportService;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class Controller
{
	private static final Log log = LogFactory.getLog(Controller.class);
	public static final String EXCEPTION_MESSAGE_KEY_REPORT_NOT_FOUND = "web.servlets.controller.report.not.found";

	/** PRISM **/
	@Autowired 
	private CookieThemeResolver themeResolver;
	/** end PRISM **/
	/**
	 *
	 */
	private JasperReportsContext jasperReportsContext;

	
	/**
	 *
	 */
	public Controller(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
	}
	
	
	/**
	 * @throws JRInteractiveException 
	 *
	 */
	public void runReport(
		WebReportContext webReportContext,
		Action action /** PRISM **/ , HttpServletRequest request, HttpServletResponse response /** end PRISM **/
		) throws JRException, JRInteractiveException
	{
		String reportUri = (String)webReportContext.getParameterValue(WebUtil.REQUEST_PARAMETER_REPORT_URI);
		int initialStackSize = 0;
		CommandStack commandStack = (CommandStack)webReportContext.getParameterValue(AbstractAction.PARAM_COMMAND_STACK);
		if (commandStack != null) {
			initialStackSize = commandStack.getExecutionStackSize();
		}

		setDataCache(webReportContext);
		
		JasperReport jasperReport = null;
		
		
		if (reportUri != null && reportUri.trim().length() > 0)
		{
			reportUri = reportUri.trim();

			if (action != null) 
			{
				action.run();
				if (log.isDebugEnabled()) {
					log.debug("action requires refill: " + action.requiresRefill());
				}
				if (!action.requiresRefill()) { // stop here because this action does not modify jasperDesign
					return;
				}
			}

			jasperReport = RepositoryUtil.getInstance(jasperReportsContext).getReport(webReportContext, reportUri /** PRISM **/ , request /** end PRISM **/);
		}


		if (jasperReport == null)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_REPORT_NOT_FOUND,
					new Object[]{reportUri});
		}
		/** PRISM **/
		//String asyncParamName = propUtil.getProperty(WebUtil.REQUEST_PARAMETER_ASYNC_REPORT);
		//Boolean async = (Boolean)webReportContext.getParameterValue(asyncParamName);
		//String asyncParamName = WebUtil.REQUEST_PARAMETER_ASYNC_REPORT;
		//Boolean async = (Boolean)webReportContext.getParameterValue(WebUtil.REQUEST_PARAMETER_ASYNC_REPORT);
		/** end-PRISM **/
		Boolean async = (Boolean)webReportContext.getParameterValue(WebUtil.REQUEST_PARAMETER_ASYNC_REPORT);
		if (async == null)
		{
			async = Boolean.FALSE;
		}
		webReportContext.setParameterValue(WebUtil.REQUEST_PARAMETER_ASYNC_REPORT, async);
		
		try {
			/** PRISM **/
			//runReport(webReportContext, jasperReport, async.booleanValue());
			runReport(webReportContext, jasperReport, async.booleanValue(), request, response, reportUri);
			/** end PRISM **/
		} catch (JRException e) {
			undoAction(webReportContext, initialStackSize);
			throw e;
		} catch (JRRuntimeException e) {
			undoAction(webReportContext, initialStackSize);
			throw e;
		}
	}
	
	private void undoAction(WebReportContext webReportContext, int initialStackSize) {
		CommandStack commandStack = (CommandStack)webReportContext.getParameterValue(AbstractAction.PARAM_COMMAND_STACK);
		if (commandStack != null) {
			for (int i = 0; i < (commandStack.getExecutionStackSize() - initialStackSize); i++) {
				commandStack.undo();
			}
		}
	}


	protected void setDataCache(WebReportContext webReportContext)
	{
		DataCacheHandler dataCacheHandler = (DataCacheHandler) webReportContext.getParameterValue(
				DataCacheHandler.PARAMETER_DATA_CACHE_HANDLER);
		if (dataCacheHandler != null && !dataCacheHandler.isSnapshotPopulated())
		{
			// if we have an old cache handler which is not yet final, create a new one
			// TODO lucianc also check for final but disabled caches
			
			if (log.isDebugEnabled())
			{
				log.debug("Data cache handler not final " + dataCacheHandler);
			}
			
			dataCacheHandler = null;
		}
		
		if (dataCacheHandler == null)
		{
			//initialize the data cache handler
			dataCacheHandler = new ColumnDataCacheHandler();
			
			if (log.isDebugEnabled())
			{
				log.debug("Created data cache handler " + dataCacheHandler);
			}
			
			webReportContext.setParameterValue(
					DataCacheHandler.PARAMETER_DATA_CACHE_HANDLER, dataCacheHandler);
		}
	}


	/**
	 *
	 */
	protected void runReport(
		WebReportContext webReportContext,
		JasperReport jasperReport, 
		boolean async /** PRISM **/, HttpServletRequest request, HttpServletResponse response, String reportUri /** end PRISM **/
		) throws JRException
	{
		JasperPrintAccessor accessor;
		/** PRISM */
		Map<String, Object> parameters = (Map<String, Object>) request.getSession().getAttribute("apiJasperParameters"+reportUri);
		parameters.put("p_Is3D", IApplicationConstants.FLAG_Y);
		Iterator it = parameters.entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry pairs = (Map.Entry)it.next();
		    webReportContext.setParameterValue((String) pairs.getKey(), pairs.getValue());
		}
		
		String contractName = request.getParameter("contractName") != null 
							? request.getParameter("contractName") : Utils.getContractName();
		
		if(contractName != null && contractName.trim().length() == 0 && themeResolver != null) {
			contractName = Utils.getContractName(themeResolver.resolveThemeName(request));
		}
		webReportContext.setParameterValue("contractName", contractName);
		
		
		/** End PRISM */
		if (async)
		{
			AsynchronousFillHandle fillHandle = 
				AsynchronousFillHandle.createHandle(
					jasperReportsContext,
					jasperReport, 
					webReportContext.getParameterValues()
					);
			AsyncJasperPrintAccessor asyncAccessor = new AsyncJasperPrintAccessor(fillHandle);
			
			fillHandle.startFill();
			
			accessor = asyncAccessor;
		}
		else
		{
			/** PRISM **/
			/*JasperPrint jasperPrint = 
					JasperFillManager.getInstance(jasperReportsContext).fill(
						jasperReport, 
						webReportContext.getParameterValues()
						);*/
			JasperPrint jasperPrint = null;
			try {
				//jasperPrint = JasperFillManager.fillReport(jasperReport, webReportContext.getParameterValues(), conn);
				ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
				IReportService reportService = (IReportService) appContext.getBean("reportService");
				jasperPrint = reportService.getFilledReport(jasperReport, webReportContext.getParameterValues());
				
				parameters.put("contractName", contractName);
				// Async call for PDF
				reportService.getFilledReportForPDF(jasperReport, parameters, true, 
						(String) request.getSession().getAttribute(IApplicationConstants.CURRUSER), reportUri);
				
				//IFillManager fillManager = new FillManagerImpl();
				//jasperPrint = fillManager.fillReport(jasperReport, webReportContext.getParameterValues());
				
				Map<String, Object> sessionObj = new HashMap<String, Object>();
				sessionObj.put("jasperReport", jasperReport);
				sessionObj.put("parameterValues", webReportContext.getParameterValues());
				request.getSession().setAttribute("apiJasperPrint"+reportUri, sessionObj);
				
				
				//IUsabilityService usabilityService = (IUsabilityService) appContext.getBean("usabilityService");
				/** session to cache **/
				/*usabilityService.getSetCache((String) request.getSession().getAttribute(IApplicationConstants.CURRUSER),
						reportUri, jasperPrint);*/
				// moving the following section from reportController (API_TABLE report) to here
				/*
				int noOfPages = jasperPrint.getPages().size();	
				
				if (noOfPages > 1) {
					request.getSession().setAttribute(reportUri, IApplicationConstants.TRUE);
					request.setAttribute("totalPages", noOfPages);
					request.getSession().setAttribute(IApplicationConstants.TOTAL_PAGES, noOfPages);
				} else {
					request.getSession().setAttribute(reportUri, IApplicationConstants.FALSE);
				}
				
				if(noOfPages == 0) {
					request.getSession().setAttribute("dataPresent_"+reportUri, IApplicationConstants.FALSE);
					response.sendRedirect(CustomStringUtil.appendString(request.getContextPath(), "/loadEmptyReport.do?reportName=",
							jasperReport.getName(), "&reportMsg=", 
							request.getParameter("msg") != null ? request.getParameter("msg") : null));
				} else {
					request.getSession().setAttribute("dataPresent_"+reportUri, IApplicationConstants.TRUE);
				}
				*/
			} catch (Exception e) {
				e.printStackTrace();
			} 
			System.out.println("jasperPrint object created : " + jasperPrint != null? jasperPrint.getName() : "jasperPrint from SESSION null");
			/** End PRISM */
			
			accessor = new SimpleJasperPrintAccessor(jasperPrint);
		}
		
		webReportContext.setParameterValue(WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_PRINT_ACCESSOR, accessor);
	}
}
