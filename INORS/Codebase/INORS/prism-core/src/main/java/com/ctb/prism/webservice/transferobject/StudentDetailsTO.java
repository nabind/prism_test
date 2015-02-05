package com.ctb.prism.webservice.transferobject;

import java.io.Serializable;

public class StudentDetailsTO implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4477773094826787095L;

	private StudentBioTO studentBioTO;
	private StudentDemoTO studentDemoTO;
	private StudentSurveyBioTO studentSurveyBioTO;
	
	public StudentSurveyBioTO getStudentSurveyBioTO() {
		return studentSurveyBioTO;
	}
	public void setStudentSurveyBioTO(StudentSurveyBioTO studentSurveyBioTO) {
		this.studentSurveyBioTO = studentSurveyBioTO;
	}
	public StudentBioTO getStudentBioTO() {
		return studentBioTO;
	}
	public void setStudentBioTO(StudentBioTO studentBioTO) {
		this.studentBioTO = studentBioTO;
	}
	public StudentDemoTO getStudentDemoTO() {
		return studentDemoTO;
	}
	public void setStudentDemoTO(StudentDemoTO studentDemoTO) {
		this.studentDemoTO = studentDemoTO;
	}
	
}
