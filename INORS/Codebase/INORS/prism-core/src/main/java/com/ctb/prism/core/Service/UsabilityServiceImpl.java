/**
 * 
 */
package com.ctb.prism.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ctb.prism.core.business.IUsabilityBusiness;
import com.ctb.prism.core.transferobject.UsabilityTO;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;
import com.ctb.prism.webservice.transferobject.StudentListTO;

/**
 * @author d-abir_dutta
 * Service to report usability
 *
 */
@Service
public class UsabilityServiceImpl implements IUsabilityService {

	/* (non-Javadoc)
	 * @see com.ctb.prism.core.Service.IUsabilityService#saveUsabilityData(com.ctb.prism.core.transferobject.UsabilityTO)
	 */
	@Autowired
	IUsabilityBusiness usabilityBuisness;
	
	@Async
	public boolean saveUsabilityData(UsabilityTO usability) throws Exception {
		// TODO Auto-generated method stub
		return usabilityBuisness.saveUsabilityData(usability);
	}
	
	//@Async
	public StudentDataLoadTO updateStagingData(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception {
		
		return usabilityBuisness.updateStagingData(studentListTO, studentDataLoadTO);
	}
	
	public void updatePartition(String partitionName) throws Exception {
		usabilityBuisness.updatePartition(partitionName);
	}
	
	public String checkPartition() throws Exception {
		return usabilityBuisness.checkPartition();
	}
	
	public StudentDataLoadTO createProces(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception {
		return usabilityBuisness.createProces(studentListTO, studentDataLoadTO);
	}

}
