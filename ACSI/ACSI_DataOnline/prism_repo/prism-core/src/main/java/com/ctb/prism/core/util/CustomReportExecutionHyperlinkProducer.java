package com.ctb.prism.core.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.web.servlets.ReportServlet;

public class CustomReportExecutionHyperlinkProducer implements JRHyperlinkProducer {

	
private HttpServletRequest request;
	
	/**
	 *
	 */
	private CustomReportExecutionHyperlinkProducer (HttpServletRequest request)
	{
		this.request = request;
	}

	/**
	 *
	 */
	public static CustomReportExecutionHyperlinkProducer getInstance(HttpServletRequest request)
	{
		return new CustomReportExecutionHyperlinkProducer(request);
	}


	/**
	 *
	 */
	public String getHyperlink(JRPrintHyperlink hyperlink) 
	{
		/*String appContext = request.getContextPath();
		String servletPath = request.getServletPath();
		String reportUri = request.getParameter(ReportServlet.REQUEST_PARAMETER_REPORT_URI);
		String reportAction = null;//request.getParameter(FillServlet.REPORT_ACTION);
		String reportActionData = null;//request.getParameter(FillServlet.REPORT_ACTION_DATA);
		
		StringBuffer allParams = new StringBuffer();
		
		if (hyperlink.getHyperlinkParameters() != null)
		{
			List parameters = hyperlink.getHyperlinkParameters().getParameters();
			if (parameters != null)
			{
				for (int i = 0; i < parameters.size(); i++)
				{
					JRPrintHyperlinkParameter parameter = (JRPrintHyperlinkParameter)parameters.get(i);
					if (ReportServlet.REQUEST_PARAMETER_REPORT_URI.equals(parameter.getName()))
					{
						reportUri = (String)parameter.getValue();
					}
//					else if (FillServlet.REPORT_ACTION.equals(parameter.getName()))
//					{
//						reportAction = (String)parameter.getValue();
//					}
//					else if (FillServlet.REPORT_ACTION_DATA.equals(parameter.getName()))
//					{
//						reportActionData = (String)parameter.getValue();
//					}
					else if (parameter.getValue() != null)
					{
						allParams.append("&").append(parameter.getName()).append("=").append(parameter.getValue());
					}
				}
			}
		}*/
		
		return "";
			/*appContext + (servletPath != null ? servletPath : "")
				+ "?" + ReportServlet.REQUEST_PARAMETER_REPORT_URI + "=" + reportUri 
//				+ (reportAction == null ? "" : "&" + FillServlet.REPORT_ACTION + "=" + reportAction) 
//				+ (reportActionData == null ? "" : "&" + FillServlet.REPORT_ACTION_DATA + "=" + reportActionData)
				+ allParams.toString();*/
	}

}
