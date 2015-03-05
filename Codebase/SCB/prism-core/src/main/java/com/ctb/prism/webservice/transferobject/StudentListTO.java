package com.ctb.prism.webservice.transferobject;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
@XStreamAlias("StudentListTO")
public class StudentListTO implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4477773094826787095L;

	private List<RosterDetailsTO> rosterDetailsTO;

	public List<RosterDetailsTO> getRosterDetailsTO() {
		return rosterDetailsTO;
	}

	public void setRosterDetailsTO(List<RosterDetailsTO> rosterDetailsTO) {
		this.rosterDetailsTO = rosterDetailsTO;
	}

	
}
