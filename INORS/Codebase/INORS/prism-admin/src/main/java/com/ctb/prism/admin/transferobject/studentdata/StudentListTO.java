package com.ctb.prism.admin.transferobject.studentdata;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Student_List")
public class StudentListTO {

	@XStreamImplicit
	private List<RosterDetailsTO> rosterDetailsTO;

	public List<RosterDetailsTO> getRosterDetailsTO() {
		return rosterDetailsTO;
	}

	public void setRosterDetailsTO(List<RosterDetailsTO> rosterDetailsTO) {
		this.rosterDetailsTO = rosterDetailsTO;
	}

}
