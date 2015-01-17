/**
 * 
 */
package com.ctb.prism.core.Service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ctb.prism.core.business.IUsabilityBusiness;
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
@Service("usabilityService")
public class UsabilityServiceImpl implements IUsabilityService {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(UsabilityServiceImpl.class.getName());
	@Autowired
	IUsabilityBusiness usabilityBuisness;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.Service.IUsabilityService#saveUsabilityData(com.ctb.prism.core.transferobject.UsabilityTO)
	 */
	// @Async // Async was throwing NullPointerException at com.ctb.prism.core.logger.UsabilityLogger.captureActivityLog(UsabilityLogger.java:195)
	public boolean saveUsabilityData(UsabilityTO usability) throws Exception {
		return usabilityBuisness.saveUsabilityData(usability);
	}

	// @Async
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.Service.IUsabilityService#updateStagingData(com.ctb.prism.webservice.transferobject.StudentListTO, com.ctb.prism.webservice.transferobject.StudentDataLoadTO)
	 */
	public StudentDataLoadTO updateStagingData(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception {
		return usabilityBuisness.updateStagingData(studentListTO, studentDataLoadTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.Service.IUsabilityService#updatePartition(java.lang.String)
	 */
	public void updatePartition(String partitionName) throws Exception {
		usabilityBuisness.updatePartition(partitionName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.Service.IUsabilityService#checkPartition()
	 */
	public String checkPartition() throws Exception {
		return usabilityBuisness.checkPartition();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.Service.IUsabilityService#createProces(com.ctb.prism.webservice.transferobject.StudentListTO, com.ctb.prism.webservice.transferobject.StudentDataLoadTO)
	 */
	public StudentDataLoadTO createProces(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) throws Exception {
		return usabilityBuisness.createProces(studentListTO, studentDataLoadTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.Service.IUsabilityService#insertIntoJobTracking(com.ctb.prism.core.transferobject.JobTrackingTO)
	 */
	public JobTrackingTO insertIntoJobTracking(JobTrackingTO jobTrackingTO) throws Exception {
		return usabilityBuisness.insertIntoJobTracking(jobTrackingTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.Service.IUsabilityService#getProces(com.ctb.prism.core.transferobject.ProcessTO)
	 */
	public ProcessTO getProces(ProcessTO processTO) throws Exception {
		return usabilityBuisness.getProces(processTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.Service.IUsabilityService#getStudentListTO()
	 */
	public Map<Long, StudentListTO> getStudentListTO() {
		return usabilityBuisness.getStudentListTO();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.Service.IUsabilityService#updateWSRosterData(com.ctb.prism.webservice.transferobject.StudentDataLoadTO, long)
	 */
	public int updateWSRosterData(StudentDataLoadTO studentDataLoadTO, long wsRosterDataId) {
		return usabilityBuisness.updateWSRosterData(studentDataLoadTO, wsRosterDataId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.Service.IUsabilityService#getSetCache(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Caching( evict = { 
			@CacheEvict(value = "inorsUserCache", condition = "T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors' and #p2 != null", key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1) ) )", beforeInvocation = true),
			@CacheEvict(value = "tascUserCache",  condition = "T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'  and #p2 != null", key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1) ) )", beforeInvocation = true)
		  } , cacheable = { 
			@Cacheable(value = "inorsUserCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1) ) )"),
			@Cacheable(value = "tascUserCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1) ) )")
	} )
	public Object getSetCache(String username, String sessionName, Object sessionParam) {
		logger.log(IAppLogger.INFO, "********* ********** ************ putting into cache ****** " + sessionName);
		return sessionParam;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.core.Service.IUsabilityService#removeFromCache(java.lang.String, java.lang.String)
	 */
	@Caching( evict = {
			@CacheEvict(value = "inorsUserCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1) )"),
			@CacheEvict(value = "tascUserCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1) )")
	} )
	public Object removeFromCache(String username, String sessionName) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.core.Service.IUsabilityService#getFileSize(java.lang.String)
	 */
	public JobTrackingTO getFileSize(String jobId) {
		return usabilityBuisness.getFileSize(jobId);
	}

	public JobTrackingTO updateFileSize(JobTrackingTO jobTrackingTO) {
		return usabilityBuisness.updateFileSize(jobTrackingTO);
	}

}
