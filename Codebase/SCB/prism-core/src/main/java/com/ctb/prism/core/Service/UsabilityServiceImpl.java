/**
 * 
 */
package com.ctb.prism.core.Service;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ctb.prism.core.business.IUsabilityBusiness;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.core.transferobject.ProcessTO;
import com.ctb.prism.core.transferobject.StudentDataExtractTO;
import com.ctb.prism.core.transferobject.UsabilityTO;
import com.ctb.prism.webservice.erTransferobject.StudentDetails;
import com.ctb.prism.webservice.erTransferobject.StudentList;
import com.ctb.prism.webservice.transferobject.RosterDetailsTO;
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
			@CacheEvict(value = "tascUserCache",  condition = "T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'  and #p2 != null", key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1) ) )", beforeInvocation = true),
			@CacheEvict(value = "usmoUserCache",  condition = "T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'  and #p2 != null", key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1) ) )", beforeInvocation = true)
		  } , cacheable = { 
			@Cacheable(value = "inorsUserCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1) ) )"),
			@Cacheable(value = "tascUserCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1) ) )"),
			@Cacheable(value = "usmoUserCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1) ) )")
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
			@CacheEvict(value = "tascUserCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1) )"),
			@CacheEvict(value = "usmoUserCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1) )")
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
	
	@Autowired private IDynamoDBService dynamoDBService;
	@Autowired private IPropertyLookup propertyLookup;
	public void storeERWSObject(StudentList studentList, long processId, boolean requestObj, String source) {
		try {
    		JAXBContext jc = JAXBContext.newInstance( StudentList.class );
    		Marshaller mc = jc.createMarshaller();
    		mc.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    		StreamResult result=new StreamResult(new StringWriter());
    		mc.marshal(studentList, result);
    		storeWsObject(result.getWriter().toString(), processId, requestObj, source, null);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public void storeOASWSObject(StudentListTO studentListTO, long processId, boolean requestObj, String source) {
		storeOASWSObject(studentListTO, null, processId, requestObj, source);
	}

	public void storeOASWSObject(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO, long processId, boolean requestObj, String source) {
		try {
			String rosterId = getRosterId(studentListTO);
    		JAXBContext jc = JAXBContext.newInstance( StudentListTO.class );
    		Marshaller mc = jc.createMarshaller();
    		mc.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    		StreamResult result=new StreamResult(new StringWriter());
    		mc.marshal(studentListTO, result);
    		storeWsObject(result.getWriter().toString(), processId, requestObj, source, rosterId);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public void storeOASWSLog(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) {
		try {
			if(studentDataLoadTO != null) usabilityBuisness.storeWebserviceLog(studentListTO, studentDataLoadTO);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void storeWSResponse(StudentDataLoadTO studentDataLoadTO, long processId, boolean requestObj, String source) {
		storeWSResponse(null, studentDataLoadTO, processId, requestObj, source);
	}
	
	public void storeWSResponse(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO, long processId, boolean requestObj, String source) {
		try {
			String rosterId = getRosterId(studentListTO);
			JAXBContext jc = JAXBContext.newInstance( StudentDataLoadTO.class );
    		Marshaller mc = jc.createMarshaller();
    		mc.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    		StreamResult result=new StreamResult(new StringWriter());
    		mc.marshal(studentDataLoadTO, result);
    		storeWsObject(result.getWriter().toString(), processId, requestObj, source, rosterId);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	private void storeWsObject(String obj, long processId, boolean requestObj, String source, String rosterId) {
		dynamoDBService.storeWsObject(propertyLookup.get("environment.postfix"), obj, processId, requestObj, source, rosterId);
	}
	@Async
	public void generateStudentXMLExtract(Map<String, Object> paramMap){
		usabilityBuisness.generateStudentXMLExtract(paramMap);
	}
	
	/**
	 * @param paramMap
	 * @throws Exception
	 */
	public StudentDataExtractTO getClobXMLFile(Map<String, Object> paramMap) {
		return usabilityBuisness.getClobXMLFile(paramMap);
	}
	
	public boolean createRoster(StudentListTO studentListTO) throws Exception {
		return usabilityBuisness.createRoster(studentListTO);
	}
	
	public void removeRoster(StudentListTO studentListTO) throws Exception {
		usabilityBuisness.removeRoster(studentListTO);
	}
	
	private String getRosterId(StudentListTO studentListTO) {
		String rosterId = "";
		try {
			if(studentListTO != null) {
				List<RosterDetailsTO> rosterDetailsList = studentListTO.getRosterDetailsTO();
				for(RosterDetailsTO rosterDetailsTO : rosterDetailsList) {
					rosterId = rosterId + rosterDetailsTO.getRosterId() + "-";
				}
				if(rosterId.length() > 0) rosterId = rosterId.substring(0, rosterId.length()-1);
			}
		} catch (Exception e) {}
		return rosterId;
	}
	
	private String getUuid(StudentList studentList) {
		String uuid = "";
		try {
			if(studentList != null && studentList.getStudentDetails() != null) {
				List<StudentDetails> studentDetailsList = studentList.getStudentDetails();
				for(StudentDetails studentDetails : studentDetailsList) {
					uuid = uuid + studentDetails.getUUID() + "-";
				}
				if(uuid.length() > 0) uuid = uuid.substring(0, uuid.length()-1);
			}
		} catch (Exception e) {}
		return uuid;
	}
}
