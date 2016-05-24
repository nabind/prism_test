package com.vaannila.TO;

import java.io.Serializable;
import java.util.List;


public class ContentTO implements Serializable{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long contentId;
	private String contentName;
	private List<StudentScoresTO> studentScores;
	private String graphText;
	private String tableText;
	
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
	public List<StudentScoresTO> getStudentScores() {
		return studentScores;
	}
	public void setStudentScores(List<StudentScoresTO> studentScores) {
		this.studentScores = studentScores;
	}
	public String getGraphText() {
		return (graphText == null) ? "": graphText;
	}
	public void setGraphText(String graphText) {
		this.graphText = graphText;
	}
	public String getTableText() {
		return (tableText == null) ? "": tableText;
	}
	public void setTableText(String tableText) {
		this.tableText = tableText;
	}
	
	/*public String getGradeFixedScore(int scoreTypeVal) {
		StringBuffer grdScrBuf = new StringBuffer();
		int count = 0;
		if(getStudentScores() != null) {
			grdScrBuf.append("[");
			for(StudentScoresTO studentScoresTO : getStudentScores()) {
				if(count != 0) {
					count++;
					grdScrBuf.append(",");
				} else {
					count++;
				}
				//grdScrBuf.append("[");
				//grdScrBuf.append(studentScoresTO.getGradeName());
				//grdScrBuf.append(",");
				if(scoreTypeVal == 1) grdScrBuf.append(studentScoresTO.getPpCutScore());
				if(scoreTypeVal == 2) grdScrBuf.append(studentScoresTO.getpCutScore());
				if(scoreTypeVal == 3) grdScrBuf.append(studentScoresTO.getaCutScore());
				//grdScrBuf.append("]");
			}
			grdScrBuf.append("]");
		}
		return grdScrBuf.toString();
	}
	
	public String getStudentScore() {
		StringBuffer grdScrBuf = new StringBuffer();
		int count = 0;
		if(getStudentScores() != null) {
			grdScrBuf.append("[");
			for(StudentScoresTO studentScoresTO : getStudentScores()) {
				if(studentScoresTO.getStudentScore() != null 
						&& studentScoresTO.getStudentScore().trim().length() > 0) {
					if(count != 0) {
						count++;
						grdScrBuf.append(",");
					} else {
						count++;
					}
					//grdScrBuf.append("[");
					//grdScrBuf.append(studentScoresTO.getGradeName());
					//grdScrBuf.append(",");
					if(studentScoresTO.getStudentScore().trim().length() > 0) {
						grdScrBuf.append(studentScoresTO.getStudentScore());
					} else {
						grdScrBuf.append("null");
					}
					//grdScrBuf.append("]");
				} else {
					if(count != 0) {
						count++;
						grdScrBuf.append(",");
					} else {
						count++;
					}
					grdScrBuf.append("null");
				}
			}
			grdScrBuf.append("]");
		}
		return grdScrBuf.toString();
	}
	
	public String getGrowthScore() {
		StringBuffer grdScrBuf = new StringBuffer();
		int count = 0;
		if(getStudentScores() != null) {
			grdScrBuf.append("[");
			for(StudentScoresTO studentScoresTO : getStudentScores()) {
				if(studentScoresTO.getGrowthScore() != null 
						&& !studentScoresTO.getGrowthScore().equals("N/A")) {
					if(count != 0) {
						count++;
						grdScrBuf.append(",");
					} else {
						count++;
					}
					//grdScrBuf.append("[");
					//grdScrBuf.append(studentScoresTO.getGradeName());
					//grdScrBuf.append(",");
					if(studentScoresTO.getGrowthScore().trim().length() > 0) {
						grdScrBuf.append(studentScoresTO.getGrowthScore());
					} else {
						grdScrBuf.append("null");
					}
					//grdScrBuf.append("]");
				} else {
					if(count != 0) {
						count++;
						grdScrBuf.append(",");
					} else {
						count++;
					}
					grdScrBuf.append("null");
				}
			}
			grdScrBuf.append("]");
		}
		return grdScrBuf.toString();
	}*/
	
	public String getGradeFixedScore(int scoreTypeVal) {
		StringBuffer grdScrBuf = new StringBuffer();
		int count = 0;
		if(getStudentScores() != null) {
			grdScrBuf.append("[");
			for(StudentScoresTO studentScoresTO : getStudentScores()) {
				if(count != 0) {
					count++;
					grdScrBuf.append(",");
				} else {
					count++;
				}
				grdScrBuf.append("[");
				grdScrBuf.append(studentScoresTO.getGradeName());
				grdScrBuf.append(",");
				if(scoreTypeVal == 1) grdScrBuf.append(studentScoresTO.getPpCutScore());
				if(scoreTypeVal == 2) grdScrBuf.append(studentScoresTO.getpCutScore());
				if(scoreTypeVal == 3) grdScrBuf.append(studentScoresTO.getaCutScore());
				grdScrBuf.append("]");
			}
			grdScrBuf.append("]");
		}
		return grdScrBuf.toString();
	}
	
	public String getStudentScore() {
		StringBuffer grdScrBuf = new StringBuffer();
		int count = 0;
		if(getStudentScores() != null) {
			grdScrBuf.append("[");
			for(StudentScoresTO studentScoresTO : getStudentScores()) {
				if(studentScoresTO.getStudentScore() != null 
						&& studentScoresTO.getStudentScore().trim().length() > 0) {
					if(count != 0) {
						count++;
						grdScrBuf.append(",");
					} else {
						count++;
					}
					grdScrBuf.append("[");
					grdScrBuf.append(studentScoresTO.getGradeName());
					grdScrBuf.append(",");
					grdScrBuf.append(studentScoresTO.getStudentScore());
					grdScrBuf.append("]");
				} /*else {  // Uncomment this else section if you want to show student line in graph only for valid scores
					if(!"S".equals(studentScoresTO.getAssessmentFlag())) {
						if(count != 0) {
							count++;
							grdScrBuf.append(",");
						} else {
							count++;
						}
						grdScrBuf.append("[");
						grdScrBuf.append(studentScoresTO.getGradeName());
						grdScrBuf.append(",");
						grdScrBuf.append("null");
						grdScrBuf.append("]");
					}
				}*/
			}
			grdScrBuf.append("]");
		}
		//System.out.println(grdScrBuf.toString());
		return grdScrBuf.toString();
	}
	
	public String getGrowthScore() {
		StringBuffer grdScrBuf = new StringBuffer();
		int count = 0;
		if(getStudentScores() != null) {
			grdScrBuf.append("[");
			for(StudentScoresTO studentScoresTO : getStudentScores()) {
				if(studentScoresTO.getGrowthScore() != null 
						&& !studentScoresTO.getGrowthScore().equals("N/A")) {
					
					if(studentScoresTO.getGrowthScore().trim().length() > 0) {
						if(count != 0) {
							count++;
							grdScrBuf.append(",");
						} else {
							count++;
						}
						grdScrBuf.append("[");
						grdScrBuf.append(studentScoresTO.getGradeName());
						grdScrBuf.append(",");
						grdScrBuf.append(studentScoresTO.getGrowthScore());
						grdScrBuf.append("]");
					}
				}
			}
			grdScrBuf.append("]");
		}
		return grdScrBuf.toString();
	}
	
	
	
}
