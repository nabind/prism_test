package com.vaannila.TO;

public class StudentScoresTO {
	
	private long contentId;
	private long gradeSeq;
	private String gradeName;
	private long ppCutScore;
	private long pCutScore;
	private long aCutScore;
	private String studentScore;
	private String growthScore;
	private String achvLevel;
	private String achvPercentile;
	private String dateAttended;
	private String schoolAttended;
	private long studentBioId;
	
	private String step0;
	private String step1;
	private String step2;
	private String step3;
	private String contentName;
	private String studentName;
	private String currentGrade;
	private String currentDistrict;
	private String currentSchool;
	private String graphText;
	private String tableText;
	
	private String assessmentFlag;
	
	public String getAssessmentFlag() {
		return assessmentFlag;
	}
	public void setAssessmentFlag(String assessmentFlag) {
		this.assessmentFlag = assessmentFlag;
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
	public String getCurrentDistrict() {
		return currentDistrict;
	}
	public void setCurrentDistrict(String currentDistrict) {
		this.currentDistrict = currentDistrict;
	}
	public String getCurrentSchool() {
		return currentSchool;
	}
	public void setCurrentSchool(String currentSchool) {
		this.currentSchool = currentSchool;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getCurrentGrade() {
		return currentGrade;
	}
	public void setCurrentGrade(String currentGrade) {
		this.currentGrade = currentGrade;
	}
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public String getStep0() {
		return step0;
	}
	public void setStep0(String step0) {
		this.step0 = step0;
	}
	public String getStep1() {
		return step1;
	}
	public void setStep1(String step1) {
		this.step1 = step1;
	}
	public String getStep2() {
		return step2;
	}
	public void setStep2(String step2) {
		this.step2 = step2;
	}
	public String getStep3() {
		return step3;
	}
	public void setStep3(String step3) {
		this.step3 = step3;
	}
	public long getStudentBioId() {
		return studentBioId;
	}
	public void setStudentBioId(long studentBioId) {
		this.studentBioId = studentBioId;
	}
	public long getContentId() {
		return contentId;
	}
	public void setContentId(long contentId) {
		this.contentId = contentId;
	}
	public long getGradeSeq() {
		return gradeSeq;
	}
	public void setGradeSeq(long gradeSeq) {
		this.gradeSeq = gradeSeq;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public long getPpCutScore() {
		return ppCutScore;
	}
	public void setPpCutScore(long ppCutScore) {
		this.ppCutScore = ppCutScore;
	}
	public long getpCutScore() {
		return pCutScore;
	}
	public void setpCutScore(long pCutScore) {
		this.pCutScore = pCutScore;
	}
	public long getaCutScore() {
		return aCutScore;
	}
	public void setaCutScore(long aCutScore) {
		this.aCutScore = aCutScore;
	}
	public String getStudentScore() {
		return (studentScore == null) ? "": studentScore;
	}
	public void setStudentScore(String studentScore) {
		this.studentScore = studentScore;
	}
	public String getGrowthScore() {
		return (growthScore == null) ? "": growthScore;
	}
	public void setGrowthScore(String growthScore) {
		this.growthScore = growthScore;
	}
	public String getAchvLevel() {
		return (achvLevel == null) ? "": achvLevel;
	}
	public void setAchvLevel(String achvLevel) {
		this.achvLevel = achvLevel;
	}
	public String getAchvPercentile() {
		return (achvPercentile == null) ? "": achvPercentile;
	}
	public void setAchvPercentile(String achvPercentile) {
		this.achvPercentile = achvPercentile;
	}
	public String getDateAttended() {
		return (dateAttended == null) ? "": dateAttended;
	}
	public void setDateAttended(String dateAttended) {
		this.dateAttended = dateAttended;
	}
	public String getSchoolAttended() {
		return (schoolAttended == null) ? "": schoolAttended;
	}
	public void setSchoolAttended(String schoolAttended) {
		this.schoolAttended = schoolAttended;
	}
	
}
