/**
 * 
 */
package com.ctb.prism.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ctb.prism.core.business.IUsabilityBusiness;
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
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(UsabilityServiceImpl.class.getName());
	
	@CacheEvict(value="userCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1) )", condition="#p2 != null", beforeInvocation = true)
	@Cacheable(value="userCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1) )")
	public Object getSetCache(String username, String sessionName, Object sessionParam) {
		logger.log(IAppLogger.INFO, "********* ********** ************ putting into cache ****** "+sessionName);
		return sessionParam;
	}
	
	@CacheEvict(value="userCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1) )")
	public Object removeFromCache(String username, String sessionName) {
		return null;
	}

}
