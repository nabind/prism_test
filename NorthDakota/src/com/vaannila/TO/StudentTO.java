package com.vaannila.TO;

import java.util.List;

public class StudentTO {
	private long studentBioId;
	private String studentName;
	private String birthDate;
	private String gender;
	private String grade;
	private String currentSchool;
	private String currentDistrict;
	private long contentId;
	private String contentName;
	private String graphText;
	private String tableText;
	private long orgNodeId;
	
	private List<ContentTO> content;

	public List<ContentTO> getContent() {
		return content;
	}
	public void setContent(List<ContentTO> content) {
		this.content = content;
	}
	public long getOrgNodeId() {
		return orgNodeId;
	}
	public void setOrgNodeId(long orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	private ScoreTO scoreTO;
	
	public ScoreTO getScoreTO() {
		return scoreTO;
	}
	public void setScoreTO(ScoreTO scoreTO) {
		this.scoreTO = scoreTO;
	}
	public long getStudentBioId() {
		return studentBioId;
	}
	public void setStudentBioId(long studentBioId) {
		this.studentBioId = studentBioId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getCurrentSchool() {
		return currentSchool;
	}
	public void setCurrentSchool(String currentSchool) {
		this.currentSchool = currentSchool;
	}
	public String getCurrentDistrict() {
		return currentDistrict;
	}
	public void setCurrentDistrict(String currentDistrict) {
		this.currentDistrict = currentDistrict;
	}
	public long getContentId() {
		return contentId;
	}
	public void setContentId(long contentId) {
		this.contentId = contentId;
	}
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public String getGraphText() {
		return graphText;
	}
	public void setGraphText(String graphText) {
		this.graphText = graphText;
	}
	public String getTableText() {
		return tableText;
	}
	public void setTableText(String tableText) {
		this.tableText = tableText;
	}
	
	
}
