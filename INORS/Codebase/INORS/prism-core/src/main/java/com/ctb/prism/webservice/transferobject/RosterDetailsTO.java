package com.ctb.prism.webservice.transferobject;

import java.util.List;

public class RosterDetailsTO {

	private String rosterId;
	private CustHierarchyDetailsTO custHierarchyDetailsTO;
	private StudentDetailsTO studentDetailsTO;
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
	public void setCollContentDetailsTO(List<ContentDetailsTO> collContentDetailsTO) {
		this.collContentDetailsTO = collContentDetailsTO;
	}
	
	
	
}
