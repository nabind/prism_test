package com.ctb.prism.webservice.transferobject;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
@XStreamAlias("StudentListTO")
public class StudentListTO {

	private List<RosterDetailsTO> rosterDetailsTO;

	public List<RosterDetailsTO> getRosterDetailsTO() {
		return rosterDetailsTO;
	}

	public void setRosterDetailsTO(List<RosterDetailsTO> rosterDetailsTO) {
		this.rosterDetailsTO = rosterDetailsTO;
	}

	
}
