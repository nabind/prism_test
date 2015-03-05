/**
 * 
 */
package com.ctb.prism.report.transferobject;

import java.util.ArrayList;
import java.util.List;

import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.core.util.CustomStringUtil;

/**
 * @author Joy
 * 
 */
public class RescoreSubtestTO extends BaseTO {
	private static final long serialVersionUID = 1L;
	private long subtestId = 0;
	private String subtestCode = "";
	private String performanceLevel = "";
	private List<RescoreSessionTO> rescoreSessionTOList = new ArrayList<RescoreSessionTO>();
	
	public long getSubtestId() {
		return subtestId;
	}
	public void setSubtestId(long subtestId) {
		this.subtestId = subtestId;
	}
	public String getSubtestCode() {
		return subtestCode;
	}
	public void setSubtestCode(String subtestCode) {
		this.subtestCode = subtestCode;
	}
	public String getPerformanceLevel() {
		return performanceLevel;
	}
	public void setPerformanceLevel(String performanceLevel) {
		this.performanceLevel = performanceLevel;
	}
	public List<RescoreSessionTO> getRescoreSessionTOList() {
		return rescoreSessionTOList;
	}
	public void setRescoreSessionTOList(List<RescoreSessionTO> rescoreSessionTOList) {
		this.rescoreSessionTOList = rescoreSessionTOList;
	}
}
