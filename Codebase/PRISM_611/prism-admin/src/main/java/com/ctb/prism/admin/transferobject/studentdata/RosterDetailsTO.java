package com.ctb.prism.admin.transferobject.studentdata;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Roster_Details")
public class RosterDetailsTO {

	@XStreamAsAttribute
	@XStreamAlias("roster_id")
	private String rosterId;

	@XStreamAlias("Cust_Hierarchy_Details")
	private CustHierarchyDetailsTO custHierarchyDetailsTO;

	@XStreamAlias("Student_Details")
	private StudentDetailsTO studentDetailsTO;

	@XStreamImplicit
	private List<ContentDetailsTO> collContentDetailsTO;

	public String getRosterId() {
		return rosterId;
	}

	public void setRosterId(String rosterId) {
		this.rosterId = rosterId;
	}

	public CustHierarchyDetailsTO getCustHierarchyDetailsTO() {
		return custHierarchyDetailsTO;
	}

	public void setCustHierarchyDetailsTO(
			CustHierarchyDetailsTO custHierarchyDetailsTO) {
		this.custHierarchyDetailsTO = custHierarchyDetailsTO;
	}

	public StudentDetailsTO getStudentDetailsTO() {
		return studentDetailsTO;
	}

	public void setStudentDetailsTO(StudentDetailsTO studentDetailsTO) {
		this.studentDetailsTO = studentDetailsTO;
	}

	public List<ContentDetailsTO> getCollContentDetailsTO() {
		return collContentDetailsTO;
	}

	public void setCollContentDetailsTO(
			List<ContentDetailsTO> collContentDetailsTO) {
		this.collContentDetailsTO = collContentDetailsTO;
	}

}
