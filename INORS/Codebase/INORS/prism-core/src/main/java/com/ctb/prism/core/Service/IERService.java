package com.ctb.prism.core.Service;

import org.springframework.scheduling.annotation.Async;

import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.webservice.erTransferobject.StudentList;
import com.ctb.prism.webservice.transferobject.StudentBioTO;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;

public interface IERService {

	
	public StudentDataLoadTO updateStagingData(StudentList studentList, StudentDataLoadTO studentDataLoadTO) throws Exception;
	
	public JobTrackingTO getStudent(JobTrackingTO jobTrackingTO) throws Exception;
	
	
	
}
