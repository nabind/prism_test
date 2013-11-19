package com.ctb.prism.webservice.transferobject;

public class StudentDetailsTO {

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
