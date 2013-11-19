/**
 * 
 */
package com.ctb.prism.core.business;
import com.ctb.prism.core.transferobject.UsabilityTO;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;
import com.ctb.prism.webservice.transferobject.StudentListTO;

/**
 * @author d-abir_dutta
 *  Business to report usability
 */
public interface IUsabilityBusiness {

	public boolean saveUsabilityData(UsabilityTO usability) throws Exception;
	public StudentDataLoadTO updateStagingData(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception;
	public void updatePartition(String partitionName) throws Exception;
	public String checkPartition() throws Exception;
	public StudentDataLoadTO createProces(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception;
}
