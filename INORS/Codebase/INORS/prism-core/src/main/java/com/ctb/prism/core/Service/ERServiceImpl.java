
package com.ctb.prism.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ctb.prism.core.business.IERBusiness;
import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.webservice.erTransferobject.StudentList;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;


@Service
public class ERServiceImpl implements IERService {

	
	@Autowired
	IERBusiness erBuisness;
	
	//@Async
	//@Transactional (propagation = Propagation.REQUIRES_NEW)
	public StudentDataLoadTO updateStagingData(StudentList studentList, StudentDataLoadTO studentDataLoadTO) throws Exception {
		
		return erBuisness.updateStagingData(studentList, studentDataLoadTO);
	}
	
	public JobTrackingTO getStudent(JobTrackingTO jobTrackingTO) throws Exception {
		return erBuisness.getStudent(jobTrackingTO);
	}
	
}
