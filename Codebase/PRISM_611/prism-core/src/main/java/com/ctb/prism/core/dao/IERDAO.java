package com.ctb.prism.core.dao;

import java.util.List;

import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.webservice.erTransferobject.ScheduleDetails;
import com.ctb.prism.webservice.erTransferobject.StudentDetails;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;

public interface IERDAO {

	public StudentDataLoadTO saveERHistory(final StudentDetails studentDetails, StudentDataLoadTO studentDataLoadTO, final ScheduleDetails scheduleDetails);
	public String exceptionHistoryUpdate(final StudentDetails studentDetails, final String historyId, final List<String> errorMessage);
	public StudentDataLoadTO saveStudentBio(final StudentDetails studentDetails, StudentDataLoadTO studentDataLoadTO);
	public StudentDataLoadTO saveSchedule(final StudentDetails studentDetails, final StudentDataLoadTO studentDataLoadTO, final ScheduleDetails scheduleDetails);
	
	public JobTrackingTO getStudent(JobTrackingTO jobTrackingTO) throws Exception;
}
