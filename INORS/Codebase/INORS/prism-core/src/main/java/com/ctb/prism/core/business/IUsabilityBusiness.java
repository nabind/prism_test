/**
 * 
 */
package com.ctb.prism.core.business;

import com.ctb.prism.core.transferobject.UsabilityTO;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;
import com.ctb.prism.webservice.transferobject.StudentListTO;

/**
 * Business to report usability
 * 
 * @author d-abir_dutta
 */
public interface IUsabilityBusiness {

	/**
	 * @param usability
	 * @return
	 * @throws Exception
	 */
	public boolean saveUsabilityData(UsabilityTO usability) throws Exception;

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
}
