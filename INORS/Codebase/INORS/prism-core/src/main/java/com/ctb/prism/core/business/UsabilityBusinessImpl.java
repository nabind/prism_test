/**
 * 
 */
package com.ctb.prism.core.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.dao.IUsabilityDAO;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.transferobject.UsabilityTO;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;
import com.ctb.prism.webservice.transferobject.StudentListTO;

/**
 * @author d-abir_dutta
 * Service to report usability
 *
 */

@Component
public class UsabilityBusinessImpl implements IUsabilityBusiness {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(UsabilityBusinessImpl.class.getName());
	
	@Autowired
	IUsabilityDAO usabilityDAO;
	/* (non-Javadoc)
	 * @see com.ctb.prism.core.business.IUsabilityBusiness#saveUsabilityData(com.ctb.prism.core.transferobject.UsabilityTO)
	 */
	public boolean saveUsabilityData(UsabilityTO usability) throws Exception {
		// TODO Auto-generated method stub
		logger.log(IAppLogger.INFO, "UsabilityBusinessImpl:saveUsabilityData");
		return usabilityDAO.saveUsabilityData(usability);
	}
	
	/**
	 * Update webservice data to staging
	 * @param studentListTO
	 */
	public StudentDataLoadTO updateStagingData(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception {
		studentDataLoadTO.setPartitionName(studentDataLoadTO.getPartitionName());
		// create process id with this partition - moved to controller
		// studentDataLoadTO = usabilityDAO.createProces(studentListTO, studentDataLoadTO);
		if(studentDataLoadTO.getProcessId() != 0) {
			usabilityDAO.updateStagingData(studentListTO, studentDataLoadTO);
		}
	
		return studentDataLoadTO;
	}
	
	public void updatePartition(String partitionName) throws Exception {
		usabilityDAO.updatePartition(partitionName);
	}
	
	public String checkPartition() throws Exception {
		return usabilityDAO.checkPartition();
	}
	
	public StudentDataLoadTO createProces(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception {
		return usabilityDAO.createProces(studentListTO, studentDataLoadTO);
	}

}
