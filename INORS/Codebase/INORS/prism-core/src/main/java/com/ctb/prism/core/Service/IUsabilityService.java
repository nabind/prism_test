/**
 * 
 */
package com.ctb.prism.core.Service;

import com.ctb.prism.core.transferobject.UsabilityTO;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;
import com.ctb.prism.webservice.transferobject.StudentListTO;

/**
 * @author d-abir_dutta
 * Service to report usability
 */
public interface IUsabilityService {

	public boolean saveUsabilityData(UsabilityTO usability) throws Exception;
	
	public StudentDataLoadTO updateStagingData(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception;
	
	public void updatePartition(String partitionName) throws Exception;
	public String checkPartition() throws Exception;
	public StudentDataLoadTO createProces(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception;
	
	public Object getSetCache(String username, String sessionName, Object sessionParam);
	public Object removeFromCache(String username, String sessionName);
}
