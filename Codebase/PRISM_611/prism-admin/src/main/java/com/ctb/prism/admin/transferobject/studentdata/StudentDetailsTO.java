package com.ctb.prism.admin.transferobject.studentdata;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class StudentDetailsTO {

	@XStreamAlias("Student_Bio")
	private StudentBioTO studentBioTO;

	@XStreamAlias("Student_Demo")
	private StudentDemoTO studentDemoTO;

	@XStreamAlias("Student_Survey_Bio")
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
