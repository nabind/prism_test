package com.ctb.prism.core.business;

import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.webservice.erTransferobject.StudentList;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;

public interface IERBusiness {

	public StudentDataLoadTO updateStagingData(StudentList studentList, StudentDataLoadTO studentDataLoadTO) throws Exception;
	
	public JobTrackingTO getStudent(JobTrackingTO jobTrackingTO) throws Exception;
}
