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
 * Service to report usability
 * 
 * @author d-abir_dutta
 * 
 */

@Component
public class UsabilityBusinessImpl implements IUsabilityBusiness {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(UsabilityBusinessImpl.class.getName());

	@Autowired
	IUsabilityDAO usabilityDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.business.IUsabilityBusiness#saveUsabilityData(com.ctb.prism.core.transferobject.UsabilityTO)
	 */
	public boolean saveUsabilityData(UsabilityTO usability) throws Exception {
		// TODO Auto-generated method stub
		logger.log(IAppLogger.INFO, "UsabilityBusinessImpl:saveUsabilityData");
		return usabilityDAO.saveUsabilityData(usability);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.business.IUsabilityBusiness#updateStagingData(com.ctb.prism.webservice.transferobject.StudentListTO, com.ctb.prism.webservice.transferobject.StudentDataLoadTO)
	 */
	public StudentDataLoadTO updateStagingData(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception {
		studentDataLoadTO.setPartitionName(studentDataLoadTO.getPartitionName());
		// create process id with this partition - moved to controller
		// studentDataLoadTO = usabilityDAO.createProces(studentListTO, studentDataLoadTO);
		if (studentDataLoadTO.getProcessId() != 0) {
			usabilityDAO.updateStagingData(studentListTO, studentDataLoadTO);
		}

		return studentDataLoadTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.business.IUsabilityBusiness#updatePartition(java.lang.String)
	 */
	public void updatePartition(String partitionName) throws Exception {
		usabilityDAO.updatePartition(partitionName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.business.IUsabilityBusiness#checkPartition()
	 */
	public String checkPartition() throws Exception {
		return usabilityDAO.checkPartition();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.business.IUsabilityBusiness#createProces(com.ctb.prism.webservice.transferobject.StudentListTO, com.ctb.prism.webservice.transferobject.StudentDataLoadTO)
	 */
	public StudentDataLoadTO createProces(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception {
		return usabilityDAO.createProces(studentListTO, studentDataLoadTO);
	}

}
