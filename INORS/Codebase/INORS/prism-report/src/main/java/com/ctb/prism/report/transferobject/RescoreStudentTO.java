/**
 * 
 */
package com.ctb.prism.report.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author 796763
 * 
 */
public class RescoreStudentTO extends BaseTO {
	private static final long serialVersionUID = 1L;
	private String name;

	public String getFirstName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
