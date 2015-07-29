/**
 * 
 */
package com.ctb.prism.report.transferobject;

import java.util.*;

import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.core.util.CustomStringUtil;

/**
 * @author Joy
 * 
 */
public class RescoreSessionTO extends BaseTO {
	
	private static final long serialVersionUID = 1L;
	private String sessionId = "";
	private String moduleId = "";
	private List<RescoreItemTO> rescoreItemTOList = new ArrayList<RescoreItemTO>();
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public List<RescoreItemTO> getRescoreItemTOList() {
		return rescoreItemTOList;
	}
	public void setRescoreItemTOList(List<RescoreItemTO> rescoreItemTOList) {
		this.rescoreItemTOList = rescoreItemTOList;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
}
