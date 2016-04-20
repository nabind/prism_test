/**
 * 
 */
package com.ctb.prism.core.dao;

import java.util.Map;

import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.core.transferobject.MUsabilityTO;
import com.ctb.prism.core.transferobject.ProcessTO;
import com.ctb.prism.core.transferobject.StudentDataExtractTO;
import com.ctb.prism.core.transferobject.UsabilityTO;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;
import com.ctb.prism.webservice.transferobject.StudentListTO;

/**
 * Dao for storing usability
 * 
 * @author d-abir_dutta
 * @author d-abir_dutta
 */
/**
 * @author 233208
 * 
 */
public interface IUsabilityDAO {

	/**
	 * @param usability
	 * @return
	 * @throws Exception
	 */
	public boolean saveUsabilityData(MUsabilityTO usability) throws Exception;
	
	public boolean saveUsabilityData(UsabilityTO usability) throws Exception;

	/**
	 * @param studentListTO
	 * @param studentDataLoadTO
	 * @return
	 * @throws Exception
	 */
	public StudentDataLoadTO updateStagingData(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception;

	
	/**
	 * Get available partition name from informatica for further processing.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkPartition() throws Exception;

	/**
	 * Create process for Online Staging data load
	 * 
	 * @param studentListTO
	 *            - all web service data
	 * @param studentDataLoadTO
	 *            - contains partition name
	 * @return studentDataLoadTO - contains process name
	 * @throws Exception
	 */
	public StudentDataLoadTO createProces(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception;

	/**
	 * Insert org hierarchy and lst node
	 * 
	 * @param studentListTO
	 *            - all web service data
	 * @param studentDataLoadTO
	 *            - contains partition name and process id
	 * @return studentDataLoadTO - with success indicator
	 * @throws Exception
	 */
	public StudentDataLoadTO insertOrgHierarchy(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception;

	/**
	 * 
	 * @param studentListTO
	 * @param studentDataLoadTO
	 * @return
	 * @throws Exception
	 */
	public StudentDataLoadTO insertStudentBio(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception;

	/**
	 * Create subtest accomodation and item response
	 * 
	 * @param studentListTO
	 * @param studentDataLoadTO
	 * @return
	 * @throws Exception
	 */
	public StudentDataLoadTO insertContent(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception;

	/**
	 * 
	 * @param studentListTO
	 * @param studentDataLoadTO
	 * @return
	 * @throws Exception
	 */
	public StudentDataLoadTO insertItems(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception;

	/**
	 * reset partition
	 * 
	 * @param partitionName
	 * @throws Exception
	 */
	public void updatePartition(String partitionName) throws Exception;

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
	
	public String getRoster(final String rosterId) throws Exception;
			
	public boolean createRoster(String rosterId) throws Exception;
	
	public void removeRoster(String rosterId) throws Exception;
	
	public void storeWebserviceLog(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO);
}
