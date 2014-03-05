package com.ctb.prism.web.util;

import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.web.controller.ParentController;
//import com.googlecode.ehcache.annotations.Cacheable;


public class TestDAOImpl implements TestDAO {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(TestDAOImpl.class.getName());
	
	//@Cacheable(cacheName = "msgCache")
	public String getMessage(int id) {
		logger.log(IAppLogger.DEBUG, "------ not from cache !!");
		return "hi there ! " + id;
	}
	
	public static void main(String[] args) {
		try {
			/*ReportFilterTO reportFilterTO = new ReportFilterTO();
			//reportFilterTO.setAdminYear("101");
			//reportFilterTO.setAssessmentId("101");
			//Class<?> c = Class.forName("com.ctb.prism.report.transferobject.ReportFilterTO");
			Class<?> c = Class.forName(ReportFilterTO.class.getName());
			Method[] allMethods = c.getDeclaredMethods();
			for (Method m : allMethods) {
				String mname = m.getName();
				if(mname.startsWith("getAdminYear")) {
					String s = (String) m.invoke(reportFilterTO);
					//logger.log(IAppLogger.DEBUG, s);
				}
			}
			String someString = "adminYear";
			//someString = freemarker.template.utility.StringUtil.capitalize(someString);
			someString = CustomStringUtil.capitalizeFirstCharacter(someString);
			//someString = someString.substring(0,1).toUpperCase() + someString.substring(1);
			logger.log(IAppLogger.DEBUG, someString);
			Method m = c.getMethod("getAdminYear");
			String s = (String) m.invoke(reportFilterTO);
			logger.log(IAppLogger.DEBUG, s);*/
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
