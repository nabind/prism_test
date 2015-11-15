package com.ctb.prism.report.transferobject;

import java.util.ArrayList;
import java.util.List;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * TO class holds the information about assessment
 */
public class AssessmentTO extends BaseTO {

	private static final long serialVersionUID = 1L;
	
	private long assessmentId;
	private String assessmentName;
	private List<ReportTO> reports = new ArrayList<ReportTO>();
	
	private int reportCount;
	
	public long getAssessmentId() {
		return assessmentId;
	}
	public void setAssessmentId(long assessmentId) {
		this.assessmentId = assessmentId;
	}
	public String getAssessmentName() {
		return assessmentName;
	}
	public void setAssessmentName(String assessmentName) {
		this.assessmentName = assessmentName;
	}
	public List<ReportTO> getReports() {
		if ( reports.size() == 0)
		{
			return null;
		}
		return reports;
	}
	public void setReports(List<ReportTO> reports) {
		this.reports = reports;
	}
	public void addReport(ReportTO to)
	{
		this.reports.add(to);
	}
	public int getReportCount() {
		reportCount = reports.size();
		return reportCount;
	}
	public void setReportCount(int reportCount) {
		this.reportCount = reportCount;
	}
	
	
}
