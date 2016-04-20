/**
 * 
 */
package com.ctb.prism.core.business;

import java.util.Map;

import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.core.transferobject.MUsabilityTO;
import com.ctb.prism.core.transferobject.ProcessTO;
import com.ctb.prism.core.transferobject.StudentDataExtractTO;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;
import com.ctb.prism.webservice.transferobject.StudentListTO;

/**
 * @author d-abir_dutta Business to report usability
 */
public interface IUsabilityBusiness {

	/**
	 * @param usability
	 * @return
	 * @throws Exception
	 */
	public boolean saveUsabilityData(MUsabilityTO usability) throws Exception;

	/**
	 * Update webservice data to staging.
	 * 
	 * @param studentListTO
	 * @param studentDataLoadTO
	 * @return
	 * @throws Exception
	 */
	public StudentDataLoadTO updateStagingData(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception;

	/**
	 * @param partitionName
	 * @throws Exception
	 */
	public void updatePartition(String partitionName) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	public String checkPartition() throws Exception;

	/**
	 * @param studentListTO
	 * @param studentDataLoadTO
	 * @return
	 * @throws Exception
	 */
	public StudentDataLoadTO createProces(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception;

	/**
	 * @param jobTrackingTO
	 * @return
	 * @throws Exception
	 */
	public JobTrackingTO insertIntoJobTracking(JobTrackingTO jobTrackingTO) throws Exception;

	/**
	 * @param processTO
	 * @return
	 * @throws Exception
	 */
	public ProcessTO getProces(ProcessTO processTO) throws Exception;

	/**
	 * @return
	 */
	public Map<Long, StudentListTO> getStudentListTO();

	/**
	 * @param studentDataLoadTO
	 * @param wsRosterDataId
	 * @return
	 */
	public int updateWSRosterData(StudentDataLoadTO studentDataLoadTO, long wsRosterDataId);

	/**
	 * @param jobId
	 * @return
	 */
	public JobTrackingTO getFileSize(String jobId);

	/**
	 * @param jobTrackingTO
	 * @return
	 */
	public JobTrackingTO updateFileSize(JobTrackingTO jobTrackingTO);
	
	/**
	 * @param paramMap
	 * @throws Exception
	 */
	public void generateStudentXMLExtract(Map<String, Object> paramMap);
	
	/**
	 * @param paramMap
	 * @throws Exception
	 */
	public StudentDataExtractTO getClobXMLFile(Map<String, Object> paramMap);
	
	public boolean createRoster(StudentListTO studentListTO) throws Exception;
	
	public void removeRoster(StudentListTO studentListTO) throws Exception;
	
	public void storeWebserviceLog(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO);

}
