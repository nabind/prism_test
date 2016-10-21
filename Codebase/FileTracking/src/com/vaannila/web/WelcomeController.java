package com.vaannila.web;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.vaannila.DAO.StageDAOImpl;
import com.vaannila.TO.OrgProcess;
import com.vaannila.util.PropertyFile;

@Controller
public class WelcomeController {
	private static final Logger logger = Logger.getLogger(WelcomeController.class);

	JSONArray jsonArray = new JSONArray();
	String jsonStr = "";
	@RequestMapping("/welcome.htm")
	public ModelAndView redirect(HttpServletRequest request,
			HttpServletResponse response)
	{
		Properties prop = PropertyFile.loadProperties("acsi.properties");
		StageDAOImpl stageDao = new StageDAOImpl();
		
		
		try {
			List<OrgProcess> processes = stageDao.getProcessDetails((String) request.getSession().getAttribute("adminid"));
			convertToJson(processes);
			
			logger.info(jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("welcome", "message", jsonStr);
	}
	
	public void convertToJson(List<OrgProcess> processes) {
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("product", OrgProcess.class);
		for(Iterator<OrgProcess> itr = processes.iterator(); itr.hasNext();) {
			jsonStr = xstream.toXML(itr.next());
			//logger.info(jsonStr);
			jsonArray.put(xstream.toXML(jsonStr));
		}
	}
}
