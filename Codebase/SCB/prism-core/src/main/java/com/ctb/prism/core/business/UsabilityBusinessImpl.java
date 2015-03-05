/**
 * 
 */
package com.ctb.prism.core.business;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.dao.IUsabilityDAO;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.core.transferobject.ProcessTO;
import com.ctb.prism.core.transferobject.UsabilityTO;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;
import com.ctb.prism.webservice.transferobject.StudentListTO;

/**
 * @author d-abir_dutta Service to report usability
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.business.IUsabilityBusiness#insertIntoJobTracking(com.ctb.prism.core.transferobject.JobTrackingTO)
	 */
	public JobTrackingTO insertIntoJobTracking(JobTrackingTO jobTrackingTO) throws Exception {
		return usabilityDAO.insertIntoJobTracking(jobTrackingTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.business.IUsabilityBusiness#getProces(com.ctb.prism.core.transferobject.ProcessTO)
	 */
	public ProcessTO getProces(ProcessTO processTO) throws Exception {
		return usabilityDAO.getProces(processTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.business.IUsabilityBusiness#getStudentListTO()
	 */
	public Map<Long, StudentListTO> getStudentListTO() {
		return usabilityDAO.getStudentListTO();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.business.IUsabilityBusiness#updateWSRosterData(com.ctb.prism.webservice.transferobject.StudentDataLoadTO, long)
	 */
	public int updateWSRosterData(StudentDataLoadTO studentDataLoadTO, long wsRosterDataId) {
		return usabilityDAO.updateWSRosterData(studentDataLoadTO, wsRosterDataId);
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.core.business.IUsabilityBusiness#getFileSize(java.lang.String)
	 */
	public JobTrackingTO getFileSize(String jobId) {
		return usabilityDAO.getFileSize(jobId);
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.core.business.IUsabilityBusiness#updateFileSize(com.ctb.prism.core.transferobject.JobTrackingTO)
	 */
	public JobTrackingTO updateFileSize(JobTrackingTO jobTrackingTO) {
		return usabilityDAO.updateFileSize(jobTrackingTO);
	}

}
