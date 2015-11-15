
package com.ctb.prism.core.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.dao.IERDAO;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.webservice.erTransferobject.BioDetails;
import com.ctb.prism.webservice.erTransferobject.ScheduleDetails;
import com.ctb.prism.webservice.erTransferobject.ScheduleList;
import com.ctb.prism.webservice.erTransferobject.StudentDetails;
import com.ctb.prism.webservice.erTransferobject.StudentList;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;


@Component
public class ERBusinessImpl implements IERBusiness {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(ERBusinessImpl.class.getName());
	
	@Autowired
	IERDAO erDAO;

	/*
	 * (non-Javadoc)
	 * @see com.ctb.prism.core.business.IERBusiness#updateStagingData(com.ctb.prism.webservice.erTransferobject.StudentList, com.ctb.prism.webservice.transferobject.StudentDataLoadTO)
	 */
	public StudentDataLoadTO updateStagingData(StudentList studentList,
			StudentDataLoadTO studentDataLoadTO) throws Exception {
		List<String> errorMsg = new ArrayList<String>();
		StringBuffer scheduleErrorMsg = null;
		StringBuffer bioErrorMsg = null;
		boolean bioError, scheduleError = false;
		// validate required values
		if(studentList != null && studentList.getStudentDetails() != null) {
			List<StudentDetails> studentDetailsList = studentList.getStudentDetails();
			for(StudentDetails studentDetails : studentDetailsList) {
				Map<String, String> history = new HashMap<String, String>();
				ScheduleList scheduleList = studentDetails.getScheduleList();
				/** SAVE into history */
				if(scheduleList != null && scheduleList.getScheduleDetails() != null) {
					List<ScheduleDetails> scheduleDetailsList = scheduleList.getScheduleDetails();
					for(ScheduleDetails scheduleDetails : scheduleDetailsList) {
						if(studentDataLoadTO == null) studentDataLoadTO = new StudentDataLoadTO();
						studentDataLoadTO.setSuccess(true);
						studentDataLoadTO = erDAO.saveERHistory(studentDetails, studentDataLoadTO, scheduleDetails);
						if(studentDataLoadTO.isSuccess()) {
							history.put(scheduleDetails.getScheduleID(), ""+studentDataLoadTO.getObjectiveDetailsId());
						} else {
							errorMsg.addAll(formatErrorMessages(studentDataLoadTO.getSummary()));
							studentDataLoadTO.setSummary("");
						}
					}
				}
				
				/** SAVE student BIO */
				if(studentDataLoadTO == null) studentDataLoadTO = new StudentDataLoadTO();
				studentDataLoadTO.setSuccess(true);
				studentDataLoadTO = erDAO.saveStudentBio(studentDetails, studentDataLoadTO);
				if(studentDataLoadTO.isSuccess()) {
				// schedule validation
					if(scheduleList != null && scheduleList.getScheduleDetails() != null) {
						List<ScheduleDetails> scheduleDetailsList = scheduleList.getScheduleDetails();
						Set<String> duplicate = new HashSet<String>();
						for(ScheduleDetails scheduleDetails : scheduleDetailsList) {
							// check for duplicate contentAreaCode 
							if(!duplicate.add(scheduleDetails.getContentAreaCode())) {
								// duplicate schedule present - raise error
								String historyId = history.get(scheduleDetails.getScheduleID());
								String err = scheduleError("ERR-153: Duplicate schedule present for same content area code", studentDetails.getUUID(), scheduleDetails);
								errorMsg.add(err);
								List<String> errLst = new ArrayList<String>();
								errLst.add(err);
								erDAO.exceptionHistoryUpdate(studentDetails, historyId, errLst);
								scheduleError = true;
								break;
							}
						}
						if(!scheduleError) {
							for(ScheduleDetails scheduleDetails : scheduleDetailsList) {
								/** SAVE schedule */
								studentDataLoadTO.setSuccess(true);
								studentDataLoadTO = erDAO.saveSchedule(studentDetails, studentDataLoadTO, scheduleDetails);
								if(!studentDataLoadTO.isSuccess()) {
									/** Add to error*/
									String historyId = history.get(scheduleDetails.getScheduleID());
									erDAO.exceptionHistoryUpdate(studentDetails, historyId, 
											formatErrorMessages(studentDataLoadTO.getSummary()));
									errorMsg.addAll(formatErrorMessages(studentDataLoadTO.getSummary()));
									studentDataLoadTO.setSummary("");
								}
							}
						}
					}
				} else {
					/** Add to error*/
					Iterator it = history.entrySet().iterator();
				    while (it.hasNext()) {
				        Map.Entry hist = (Map.Entry)it.next();
				        erDAO.exceptionHistoryUpdate(studentDetails, (String) hist.getValue(), 
				        		formatErrorMessages(studentDataLoadTO.getSummary()));
				    }
				    errorMsg.addAll(formatErrorMessages(studentDataLoadTO.getSummary()));
					studentDataLoadTO.setSummary("");
				}
			}
		}
		if(studentDataLoadTO == null) studentDataLoadTO = new StudentDataLoadTO();
		if(studentDataLoadTO.getErrorMessages() != null) studentDataLoadTO.getErrorMessages().addAll(errorMsg);
		else studentDataLoadTO.setErrorMessages(errorMsg);
		
		return studentDataLoadTO;
	}
	
	private List<String> formatErrorMessages(String message) {
		if(message == null) return new ArrayList<String>();
		List<String> err = new ArrayList<String>();
		String[] errors = message.split("#");
		if(errors != null) {
			for(String errMsg : errors) {
				if(errMsg != null) {
					errMsg = errMsg.replaceAll("ORA", "ERR");
				}
				err.add(errMsg);
			}
		}
		//Collections.addAll(err, errors);
		return err;
	}
	
	private String prepareError(String message, String UUID, BioDetails bio) {
		UUID = (UUID == null)? "[No UUID]" : UUID;
		String name = (bio != null)? CustomStringUtil.appendString(" (", bio.getLastName(), ", ", bio.getFirstName(), ")") : "";
		String errorMsg = CustomStringUtil.appendString(message, " for student ", UUID, name);
		return errorMsg;
	}
	
	private String scheduleError(String message, String UUID, ScheduleDetails scheduleDetails) {
		String errorMsg = CustomStringUtil.appendString(message, " for student UUID:", UUID, 
				"| Content Area Code:", scheduleDetails.getContentAreaCode(), " Schedule ID: ", scheduleDetails.getScheduleID());
		return errorMsg;
	}
	
	public JobTrackingTO getStudent(JobTrackingTO jobTrackingTO) throws Exception {
		return erDAO.getStudent(jobTrackingTO);
	}
	

}
