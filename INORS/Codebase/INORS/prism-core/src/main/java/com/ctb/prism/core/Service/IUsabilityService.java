/**
 * 
 */
package com.ctb.prism.core.Service;

import java.util.Map;

import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.core.transferobject.ProcessTO;
import com.ctb.prism.core.transferobject.UsabilityTO;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;
import com.ctb.prism.webservice.transferobject.StudentListTO;

/**
 * @author d-abir_dutta Service to report usability
 */
public interface IUsabilityService {

	/**
	 * @param usability
	 * @return
	 * @throws Exception
	 */
	public boolean saveUsabilityData(UsabilityTO usability) throws Exception;

	/**
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
	 * @param username
	 * @param sessionName
	 * @param sessionParam
	 * @return
	 */
	public Object getSetCache(String username, String sessionName, Object sessionParam);

	/**
	 * @param username
	 * @param sessionName
	 * @return
	 */
	public Object removeFromCache(String username, String sessionName);
}
