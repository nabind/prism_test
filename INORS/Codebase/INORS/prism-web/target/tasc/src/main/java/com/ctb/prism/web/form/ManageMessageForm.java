package com.ctb.prism.web.form;

import java.io.Serializable;
import java.util.List;

import com.ctb.prism.report.transferobject.ManageMessageTO;

public class ManageMessageForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<ManageMessageTO> manageMessageTOList;

	public List<ManageMessageTO> getManageMessageTOList() {
		return manageMessageTOList;
	}

	public void setManageMessageTOList(List<ManageMessageTO> manageMessageTOList) {
		this.manageMessageTOList = manageMessageTOList;
	}
	
}
