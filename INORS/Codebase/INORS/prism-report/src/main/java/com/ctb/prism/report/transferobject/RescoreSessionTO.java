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
	private long sessionId = 0;
	private List<RescoreItemTO> rescoreItemTOList = new ArrayList<RescoreItemTO>();
	
	public long getSessionId() {
		return sessionId;
	}
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
	public List<RescoreItemTO> getRescoreItemTOList() {
		return rescoreItemTOList;
	}
	public void setRescoreItemTOList(List<RescoreItemTO> rescoreItemTOList) {
		this.rescoreItemTOList = rescoreItemTOList;
	}
}
