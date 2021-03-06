package com.ctb.prism.report.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.report.dao.IRescoreRequestDAO;
import com.ctb.prism.report.transferobject.RescoreItemTO;
import com.ctb.prism.report.transferobject.RescoreRequestTO;
import com.ctb.prism.report.transferobject.RescoreSessionTO;
import com.ctb.prism.report.transferobject.RescoreSubtestTO;

@Component("rescoreRequestBusiness")
public class RescoreRequestBusinessImpl implements IRescoreRequestBusiness {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(RescoreRequestBusinessImpl.class.getName());

	@Autowired
	private IRescoreRequestDAO rescoreRequestDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ctb.prism.report.business.IRescoreRequestBusiness#getRescoreRequestForm
	 * (java.util.Map)
	 */
	public Map<String, Object> getRescoreRequestForm(Map<String, Object> paramMap) throws BusinessException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("dnpStudentList", arrangeRescoreData(rescoreRequestDAO.getDnpStudentList(paramMap), paramMap));
		returnMap.put("notDnpStudents", rescoreRequestDAO.getNotDnpStudents(paramMap));
		returnMap.put("notDnpStudentList", arrangeRescoreData(rescoreRequestDAO.getNotDnpStudentList(paramMap), paramMap));
		return returnMap;
	}

	/**
	 * @param studentList
	 * @param paramMap
	 * @return
	 */
	private List<RescoreRequestTO> arrangeRescoreData(List<RescoreRequestTO> studentList, Map<String, Object> paramMap) {

		logger.log(IAppLogger.INFO, "Enter: RescoreRequestBusinessImpl - arrangeRescoreData()");
		long t1 = System.currentTimeMillis();

		List<RescoreRequestTO> modifiedStudentList = new ArrayList<RescoreRequestTO>();

		RescoreRequestTO modifiedRescoreRequestTO = null;

		RescoreSubtestTO rescoreSubtestTO = null;
		RescoreSessionTO rescoreSessionTO = null;
		RescoreItemTO rescoreItemTO = null;

		long curStudentBioId = 0;
		long curSubtestId = 0;
		String curSessionId = "";
		boolean addToList = false;
		int index = 0;

		for (RescoreRequestTO rescoreRequestTOStud : studentList) {
			/* while( index < studentList.size() ){ */
			if (index == studentList.size()) {
				break;
			}
			rescoreRequestTOStud = studentList.get(index);
			if (curStudentBioId != rescoreRequestTOStud.getStudentBioId()) {
				modifiedRescoreRequestTO = new RescoreRequestTO();
				curStudentBioId = rescoreRequestTOStud.getStudentBioId();
				modifiedRescoreRequestTO.setStudentBioId(rescoreRequestTOStud.getStudentBioId());
				modifiedRescoreRequestTO.setStudentFullName(rescoreRequestTOStud.getStudentFullName());
				modifiedRescoreRequestTO.setRequestedDate(rescoreRequestTOStud.getRequestedDate());
				curSubtestId = 0;
				addToList = true;
			}

			for (RescoreRequestTO rescoreRequestTOSubt : studentList) {
				/* while( index < studentList.size() ){ */
				if (index == studentList.size()) {
					break;
				}
				rescoreRequestTOSubt = studentList.get(index);
				if (modifiedRescoreRequestTO.getStudentBioId() == rescoreRequestTOSubt.getStudentBioId()) {
					if (curSubtestId != rescoreRequestTOSubt.getSubtestId()) {
						rescoreSubtestTO = new RescoreSubtestTO();
						curSubtestId = rescoreRequestTOSubt.getSubtestId();
						rescoreSubtestTO.setSubtestId(rescoreRequestTOSubt.getSubtestId());
						rescoreSubtestTO.setSubtestCode(rescoreRequestTOSubt.getSubtestCode());
						rescoreSubtestTO.setPerformanceLevel(rescoreRequestTOSubt.getPerformanceLevel());
						curSessionId = "";
					}

					for (RescoreRequestTO rescoreRequestTOSession : studentList) {
						/* while( index < studentList.size() ){ */
						if (index == studentList.size()) {
							break;
						}
						rescoreRequestTOSession = studentList.get(index);
						if ((modifiedRescoreRequestTO.getStudentBioId() == rescoreRequestTOSession.getStudentBioId())
								&& (rescoreSubtestTO.getSubtestId() == rescoreRequestTOSession.getSubtestId())) {
							if (!curSessionId.equals(rescoreRequestTOSession.getSessionId())) {
								rescoreSessionTO = new RescoreSessionTO();
								rescoreSessionTO.setModuleId(rescoreRequestTOSession.getModuleId());
								rescoreSessionTO.setSessionId(rescoreRequestTOSession.getSessionId());
								curSessionId = rescoreRequestTOSession.getSessionId();
							}

							for (RescoreRequestTO rescoreRequestTOItem : studentList) {
								/* while( index < studentList.size() ){ */
								if (index == studentList.size()) {
									break;
								}
								rescoreRequestTOItem = studentList.get(index);
								if ((modifiedRescoreRequestTO.getStudentBioId() == rescoreRequestTOItem.getStudentBioId())
										&& (rescoreSubtestTO.getSubtestId() == rescoreRequestTOItem.getSubtestId())
										&& (rescoreRequestTOSession.getSessionId().equals(rescoreRequestTOItem.getSessionId()))) {
									rescoreItemTO = new RescoreItemTO();
									rescoreItemTO.setStudentBioId(rescoreRequestTOItem.getStudentBioId());
									rescoreItemTO.setItemsetId(rescoreRequestTOItem.getItemsetId());
									rescoreItemTO.setItemNumber(rescoreRequestTOItem.getItemNumber());
									rescoreItemTO.setIsRequested(rescoreRequestTOItem.getIsRequested());
									rescoreItemTO.setOriginalScore(rescoreRequestTOItem.getOriginalScore());
									rescoreItemTO.setRrfId(rescoreRequestTOItem.getRrfId());
									rescoreItemTO.setRequestedDate(rescoreRequestTOItem.getRequestedDate());
									rescoreItemTO.setUserId(rescoreRequestTOItem.getUserId());
									rescoreItemTO.setUserName(rescoreRequestTOItem.getUserName());
									rescoreSessionTO.getRescoreItemTOList().add(rescoreItemTO);
									addToList = true;
									// studentList.remove(index);
									index++;
								}
							}
							rescoreSubtestTO.getRescoreSessionTOList().add(rescoreSessionTO);
						}
					}
					modifiedRescoreRequestTO.getRescoreSubtestTOList().add(rescoreSubtestTO);
				}
			}

			if (addToList) {
				modifiedStudentList.add(modifiedRescoreRequestTO);
				addToList = false;
			}
		}

		long t2 = System.currentTimeMillis();
		logger.log(IAppLogger.INFO, "Exit: RescoreRequestBusinessImpl - arrangeRescoreData() took time: " + String.valueOf(t2 - t1) + "ms");
		refreshRescoreData(modifiedStudentList, paramMap);
		return modifiedStudentList;
	}

	/**
	 * @param modifiedStudentList
	 * @param paramMap
	 */
	private void refreshRescoreData(List<RescoreRequestTO> modifiedStudentList, Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: RescoreRequestBusinessImpl - refreshRescoreData()");
		long t1 = System.currentTimeMillis();

		long grade = Long.valueOf((String) paramMap.get("grade"));
		RescoreSubtestTO rescoreSubtestTO = null;
		RescoreSessionTO rescoreSessionTO = null;
		RescoreItemTO rescoreItemTO = null;

		RescoreSubtestTO rescoreSubtestTempTO = null;

		for (RescoreRequestTO rescoreRequestTOStud : modifiedStudentList) {
			// Checking session format
			for (RescoreSubtestTO rescoreRequestTOSubt : rescoreRequestTOStud.getRescoreSubtestTOList()) {
				if ("ELA".equals(rescoreRequestTOSubt.getSubtestCode())) {
					if (rescoreRequestTOSubt.getRescoreSessionTOList().size() == 1) {
						// For ELA Session
						rescoreItemTO = new RescoreItemTO();
						rescoreSessionTO = new RescoreSessionTO();
						rescoreSessionTO.getRescoreItemTOList().add(rescoreItemTO);
						if (rescoreRequestTOSubt.getRescoreSessionTOList().get(0).getSessionId().equals("2")) {
							// For Session 3
							rescoreRequestTOSubt.getRescoreSessionTOList().add(rescoreSessionTO);
						} else {
							// For Session 2
							rescoreRequestTOSubt.getRescoreSessionTOList().add(0, rescoreSessionTO);
						}
					}
				}
			}

			// Checking subtest format
			if (grade == 10001 || grade == 10006) {
				if (rescoreRequestTOStud.getRescoreSubtestTOList().size() < 2) {
					if ("ELA".equals(rescoreRequestTOStud.getRescoreSubtestTOList().get(0).getSubtestCode())) {
						rescoreSubtestTO = new RescoreSubtestTO();
						// For Math Session1
						rescoreItemTO = new RescoreItemTO();
						rescoreSessionTO = new RescoreSessionTO();
						rescoreSessionTO.getRescoreItemTOList().add(rescoreItemTO);
						rescoreSubtestTO.getRescoreSessionTOList().add(rescoreSessionTO);

						rescoreRequestTOStud.getRescoreSubtestTOList().add(rescoreSubtestTO);
					} else {
						rescoreSubtestTO = new RescoreSubtestTO();
						// For ELA Session2
						rescoreItemTO = new RescoreItemTO();
						rescoreSessionTO = new RescoreSessionTO();
						rescoreSessionTO.getRescoreItemTOList().add(rescoreItemTO);
						rescoreSubtestTO.getRescoreSessionTOList().add(rescoreSessionTO);

						// For ELA Session3
						rescoreItemTO = new RescoreItemTO();
						rescoreSessionTO = new RescoreSessionTO();
						rescoreSessionTO.getRescoreItemTOList().add(rescoreItemTO);
						rescoreSubtestTO.getRescoreSessionTOList().add(rescoreSessionTO);

						rescoreRequestTOStud.getRescoreSubtestTOList().add(0, rescoreSubtestTO);
					}
				}
			} else {
				if (rescoreRequestTOStud.getRescoreSubtestTOList().size() == 2) {
					// if ELS exist then it will be the first object
					String subt1Code = rescoreRequestTOStud.getRescoreSubtestTOList().get(0).getSubtestCode();
					String subt2Code = rescoreRequestTOStud.getRescoreSubtestTOList().get(1).getSubtestCode();
					if ("ELA".equals(subt1Code)) {
						rescoreSubtestTO = new RescoreSubtestTO();
						// For one subtest
						rescoreItemTO = new RescoreItemTO();
						rescoreSessionTO = new RescoreSessionTO();
						rescoreSessionTO.getRescoreItemTOList().add(rescoreItemTO);
						rescoreSubtestTO.getRescoreSessionTOList().add(rescoreSessionTO);
						if ("MATH".equals(subt2Code)) {
							// For SCI/SS
							rescoreRequestTOStud.getRescoreSubtestTOList().add(rescoreSubtestTO);
						} else {
							// For MATH
							rescoreSubtestTempTO = rescoreRequestTOStud.getRescoreSubtestTOList().get(1);
							rescoreRequestTOStud.getRescoreSubtestTOList().remove(1);
							rescoreRequestTOStud.getRescoreSubtestTOList().add(1, rescoreSubtestTO);
							rescoreRequestTOStud.getRescoreSubtestTOList().add(2, rescoreSubtestTempTO);
						}

					} else {
						// For ELA
						rescoreSubtestTO = new RescoreSubtestTO();
						// For Session2
						rescoreItemTO = new RescoreItemTO();
						rescoreSessionTO = new RescoreSessionTO();
						rescoreSessionTO.getRescoreItemTOList().add(rescoreItemTO);
						rescoreSubtestTO.getRescoreSessionTOList().add(rescoreSessionTO);

						// For Session3
						rescoreItemTO = new RescoreItemTO();
						rescoreSessionTO = new RescoreSessionTO();
						rescoreSessionTO.getRescoreItemTOList().add(rescoreItemTO);
						rescoreSubtestTO.getRescoreSessionTOList().add(rescoreSessionTO);

						rescoreRequestTOStud.getRescoreSubtestTOList().add(0, rescoreSubtestTO);
					}
				} else if (rescoreRequestTOStud.getRescoreSubtestTOList().size() == 1) {
					String subtCode = rescoreRequestTOStud.getRescoreSubtestTOList().get(0).getSubtestCode();
					if ("ELA".equals(subtCode)) {
						// For one subtest
						rescoreSubtestTO = new RescoreSubtestTO();
						rescoreItemTO = new RescoreItemTO();
						rescoreSessionTO = new RescoreSessionTO();
						rescoreSessionTO.getRescoreItemTOList().add(rescoreItemTO);
						rescoreSubtestTO.getRescoreSessionTOList().add(rescoreSessionTO);
						rescoreRequestTOStud.getRescoreSubtestTOList().add(rescoreSubtestTO);

						// For another subtest
						rescoreSubtestTO = new RescoreSubtestTO();
						rescoreItemTO = new RescoreItemTO();
						rescoreSessionTO = new RescoreSessionTO();
						rescoreSessionTO.getRescoreItemTOList().add(rescoreItemTO);
						rescoreSubtestTO.getRescoreSessionTOList().add(rescoreSessionTO);
						rescoreRequestTOStud.getRescoreSubtestTOList().add(rescoreSubtestTO);
					} else {
						// For ELA
						rescoreSubtestTO = new RescoreSubtestTO();
						// For Session2
						rescoreItemTO = new RescoreItemTO();
						rescoreSessionTO = new RescoreSessionTO();
						rescoreSessionTO.getRescoreItemTOList().add(rescoreItemTO);
						rescoreSubtestTO.getRescoreSessionTOList().add(rescoreSessionTO);

						// For Session3
						rescoreItemTO = new RescoreItemTO();
						rescoreSessionTO = new RescoreSessionTO();
						rescoreSessionTO.getRescoreItemTOList().add(rescoreItemTO);
						rescoreSubtestTO.getRescoreSessionTOList().add(rescoreSessionTO);

						rescoreRequestTOStud.getRescoreSubtestTOList().add(0, rescoreSubtestTO);

						// For one subtest
						rescoreSubtestTO = new RescoreSubtestTO();
						rescoreItemTO = new RescoreItemTO();
						rescoreSessionTO = new RescoreSessionTO();
						rescoreSessionTO.getRescoreItemTOList().add(rescoreItemTO);
						rescoreSubtestTO.getRescoreSessionTOList().add(rescoreSessionTO);

						if ("MATH".equals(subtCode)) {
							// For SCI/SS
							rescoreRequestTOStud.getRescoreSubtestTOList().add(rescoreSubtestTO);
						} else {
							// For MATH
							rescoreRequestTOStud.getRescoreSubtestTOList().add(1, rescoreSubtestTO);
						}
					}
				}
			}
		}

		long t2 = System.currentTimeMillis();
		logger.log(IAppLogger.INFO, "Exit: RescoreRequestBusinessImpl - refreshRescoreData() took time: " + String.valueOf(t2 - t1) + "ms");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ctb.prism.report.business.IRescoreRequestBusiness#submitRescoreRequest
	 * (java.util.Map)
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO submitRescoreRequest(final Map<String, Object> paramMap) throws BusinessException {
		return rescoreRequestDAO.submitRescoreRequest(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ctb.prism.report.business.IRescoreRequestBusiness#resetItemState(
	 * java.util.Map)
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO resetItemState(final Map<String, Object> paramMap) throws BusinessException {
		return rescoreRequestDAO.resetItemState(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ctb.prism.report.business.IRescoreRequestBusiness#resetItemDate(java
	 * .util.Map)
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO resetItemDate(final Map<String, Object> paramMap) throws BusinessException {
		return rescoreRequestDAO.resetItemDate(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ctb.prism.report.business.IRescoreRequestBusiness#getNotDnpStudentDetails
	 * (java.util.Map)
	 */
	public RescoreRequestTO getNotDnpStudentDetails(Map<String, Object> paramMap) throws BusinessException {
		List<RescoreRequestTO> notDnpStudentDetails = arrangeRescoreData(rescoreRequestDAO.getNotDnpStudentDetails(paramMap), paramMap);
		RescoreRequestTO rescoreStudentTO = notDnpStudentDetails.get(0);
		return rescoreStudentTO;
	}
	
	/**
	 * @author Joy 
	 * (non-Javadoc)
	 * @see
	 * com.ctb.prism.report.business.IRescoreRequestBusiness#getRescoreRequestForm
	 * (java.util.Map)
	 */
	public Map<String, Object> getRescoreRequestForm2015(Map<String, Object> paramMap) throws BusinessException {
		Map<String, Object> returnMap = arrangeRescoreData2015(rescoreRequestDAO.getSubtestDetails(paramMap));
		return returnMap;
	}
	
	private Map<String, Object> arrangeRescoreData2015(List<RescoreRequestTO> subtestList){
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if(subtestList.isEmpty()){
			return returnMap;
		}
		String studentFullName = (String)subtestList.get(0).getStudentFullName();
		String requestedDate = (String)subtestList.get(0).getRequestedDate();
		String ipFileName = (String)subtestList.get(0).getIpFileName();
		returnMap.put("studentFullName", studentFullName);
		returnMap.put("requestedDate", requestedDate);
		returnMap.put("ipFileName", ipFileName);
		
		
		RescoreSubtestTO rescoreElaTO = null;
		RescoreSubtestTO rescoreMathTO = null;
		RescoreSubtestTO rescoreScienceTO = null;
		
		RescoreSessionTO rescoreSessionTO = null;
		RescoreItemTO rescoreItemTO = null;
		
		//ELA
		List<RescoreSessionTO> rescoreSessionTOList = new ArrayList<RescoreSessionTO>();
		List<RescoreItemTO> rescoreItemTOList = new ArrayList<RescoreItemTO>();
		RescoreSessionTO rescoreSessionTOLastAdded = null;
		rescoreElaTO = new RescoreSubtestTO();
		for (RescoreRequestTO rescoreRequestTO : subtestList) {
			if("ELA".equals(rescoreRequestTO.getSubtestCode())){
				rescoreItemTO = new RescoreItemTO();
				rescoreItemTO.setItemNumber(rescoreRequestTO.getItemNumber());
				rescoreItemTO.setIsRequested(rescoreRequestTO.getIsRequested());
				rescoreItemTO.setItemPart(rescoreRequestTO.getItemPart());
				rescoreItemTO.setItemScore(rescoreRequestTO.getItemScore());
				rescoreItemTO.setItemsetId(rescoreRequestTO.getItemsetId());
				rescoreItemTO.setPointPossible(rescoreRequestTO.getPointPossible());
				rescoreItemTO.setPerformanceLevel(rescoreRequestTO.getPerformanceLevel());
				rescoreItemTO.setStudentBioId(rescoreRequestTO.getStudentBioId());
				rescoreItemTO.setRrfId(rescoreRequestTO.getRrfId());
				rescoreItemTO.setRequestedDate(rescoreRequestTO.getRequestedDate());
				
				//Last added object
				if(!rescoreSessionTOList.isEmpty()){
					rescoreSessionTOLastAdded = rescoreSessionTOList.get(rescoreSessionTOList.size()-1);
					if(rescoreSessionTOLastAdded.getSessionId().equals(rescoreRequestTO.getSessionId())){
						rescoreItemTOList.add(rescoreItemTO);
					}else{
						rescoreSessionTOLastAdded.setRescoreItemTOList(rescoreItemTOList);
						rescoreItemTOList = new ArrayList<RescoreItemTO>();
						rescoreItemTOList.add(rescoreItemTO);
						
						//Populate session
						rescoreSessionTO = new RescoreSessionTO();
						rescoreSessionTO.setSessionId(rescoreRequestTO.getSessionId());
						rescoreSessionTO.setModuleId(rescoreRequestTO.getModuleId());
						rescoreSessionTOList.add(rescoreSessionTO);
					}
				}else{
					rescoreItemTOList = new ArrayList<RescoreItemTO>();
					rescoreItemTOList.add(rescoreItemTO);
					
					//Populate session
					rescoreSessionTO = new RescoreSessionTO();
					rescoreSessionTO.setSessionId(rescoreRequestTO.getSessionId());
					rescoreSessionTO.setModuleId(rescoreRequestTO.getModuleId());
					rescoreSessionTOList.add(rescoreSessionTO);
				}
				
				rescoreElaTO.setSubtestId(rescoreRequestTO.getSubtestId());
				rescoreElaTO.setSubtestCode(rescoreRequestTO.getSubtestCode());
			}
		}
		if(!rescoreItemTOList.isEmpty()){
			rescoreSessionTOLastAdded = rescoreSessionTOList.get(rescoreSessionTOList.size()-1);
			rescoreSessionTOLastAdded.setRescoreItemTOList(rescoreItemTOList);
			rescoreElaTO.setRescoreSessionTOList(rescoreSessionTOList);
		}
		
		
		
		//MATH
		rescoreSessionTOList = new ArrayList<RescoreSessionTO>();
		rescoreItemTOList = new ArrayList<RescoreItemTO>();
		rescoreSessionTOLastAdded = null;
		rescoreMathTO = new RescoreSubtestTO();
		for (RescoreRequestTO rescoreRequestTO : subtestList) {
			if("MATH".equals(rescoreRequestTO.getSubtestCode())){
				rescoreItemTO = new RescoreItemTO();
				rescoreItemTO.setItemNumber(rescoreRequestTO.getItemNumber());
				rescoreItemTO.setIsRequested(rescoreRequestTO.getIsRequested());
				rescoreItemTO.setItemPart(rescoreRequestTO.getItemPart());
				rescoreItemTO.setItemScore(rescoreRequestTO.getItemScore());
				rescoreItemTO.setItemsetId(rescoreRequestTO.getItemsetId());
				rescoreItemTO.setPointPossible(rescoreRequestTO.getPointPossible());
				rescoreItemTO.setPerformanceLevel(rescoreRequestTO.getPerformanceLevel());
				rescoreItemTO.setStudentBioId(rescoreRequestTO.getStudentBioId());
				rescoreItemTO.setRrfId(rescoreRequestTO.getRrfId());
				rescoreItemTO.setRequestedDate(rescoreRequestTO.getRequestedDate());
				
				//Last added object
				if(!rescoreSessionTOList.isEmpty()){
					rescoreSessionTOLastAdded = rescoreSessionTOList.get(rescoreSessionTOList.size()-1);
					if(rescoreSessionTOLastAdded.getSessionId().equals(rescoreRequestTO.getSessionId())){
						rescoreItemTOList.add(rescoreItemTO);
					}else{
						rescoreSessionTOLastAdded.setRescoreItemTOList(rescoreItemTOList);
						rescoreItemTOList = new ArrayList<RescoreItemTO>();
						rescoreItemTOList.add(rescoreItemTO);
						
						//Populate session
						rescoreSessionTO = new RescoreSessionTO();
						rescoreSessionTO.setSessionId(rescoreRequestTO.getSessionId());
						rescoreSessionTO.setModuleId(rescoreRequestTO.getModuleId());
						rescoreSessionTOList.add(rescoreSessionTO);
					}
				}else{
					rescoreItemTOList = new ArrayList<RescoreItemTO>();
					rescoreItemTOList.add(rescoreItemTO);
					
					//Populate session
					rescoreSessionTO = new RescoreSessionTO();
					rescoreSessionTO.setSessionId(rescoreRequestTO.getSessionId());
					rescoreSessionTO.setModuleId(rescoreRequestTO.getModuleId());
					rescoreSessionTOList.add(rescoreSessionTO);
				}
				
				rescoreMathTO.setSubtestId(rescoreRequestTO.getSubtestId());
				rescoreMathTO.setSubtestCode(rescoreRequestTO.getSubtestCode());
			}
		}
		if(!rescoreItemTOList.isEmpty()){
			rescoreSessionTOLastAdded = rescoreSessionTOList.get(rescoreSessionTOList.size()-1);
			rescoreSessionTOLastAdded.setRescoreItemTOList(rescoreItemTOList);
			rescoreMathTO.setRescoreSessionTOList(rescoreSessionTOList);
		}
		
		//SCI
		rescoreSessionTOList = new ArrayList<RescoreSessionTO>();
		rescoreItemTOList = new ArrayList<RescoreItemTO>();
		rescoreSessionTOLastAdded = null;
		rescoreScienceTO = new RescoreSubtestTO();
		for (RescoreRequestTO rescoreRequestTO : subtestList) {
			if("SCI".equals(rescoreRequestTO.getSubtestCode())){
				rescoreItemTO = new RescoreItemTO();
				rescoreItemTO.setItemNumber(rescoreRequestTO.getItemNumber());
				rescoreItemTO.setIsRequested(rescoreRequestTO.getIsRequested());
				rescoreItemTO.setItemPart(rescoreRequestTO.getItemPart());
				rescoreItemTO.setItemScore(rescoreRequestTO.getItemScore());
				rescoreItemTO.setItemsetId(rescoreRequestTO.getItemsetId());
				rescoreItemTO.setPointPossible(rescoreRequestTO.getPointPossible());
				rescoreItemTO.setPerformanceLevel(rescoreRequestTO.getPerformanceLevel());
				rescoreItemTO.setStudentBioId(rescoreRequestTO.getStudentBioId());
				rescoreItemTO.setRrfId(rescoreRequestTO.getRrfId());
				rescoreItemTO.setRequestedDate(rescoreRequestTO.getRequestedDate());
				
				//Last added object
				if(!rescoreSessionTOList.isEmpty()){
					rescoreSessionTOLastAdded = rescoreSessionTOList.get(rescoreSessionTOList.size()-1);
					if(rescoreSessionTOLastAdded.getSessionId().equals(rescoreRequestTO.getSessionId())){
						rescoreItemTOList.add(rescoreItemTO);
					}else{
						rescoreSessionTOLastAdded.setRescoreItemTOList(rescoreItemTOList);
						rescoreItemTOList = new ArrayList<RescoreItemTO>();
						rescoreItemTOList.add(rescoreItemTO);
						
						//Populate session
						rescoreSessionTO = new RescoreSessionTO();
						rescoreSessionTO.setSessionId(rescoreRequestTO.getSessionId());
						rescoreSessionTO.setModuleId(rescoreRequestTO.getModuleId());
						rescoreSessionTOList.add(rescoreSessionTO);
					}
				}else{
					rescoreItemTOList = new ArrayList<RescoreItemTO>();
					rescoreItemTOList.add(rescoreItemTO);
					
					//Populate session
					rescoreSessionTO = new RescoreSessionTO();
					rescoreSessionTO.setSessionId(rescoreRequestTO.getSessionId());
					rescoreSessionTO.setModuleId(rescoreRequestTO.getModuleId());
					rescoreSessionTOList.add(rescoreSessionTO);
				}
				
				rescoreScienceTO.setSubtestId(rescoreRequestTO.getSubtestId());
				rescoreScienceTO.setSubtestCode(rescoreRequestTO.getSubtestCode());
			}
		}
		if(!rescoreItemTOList.isEmpty()){
			rescoreSessionTOLastAdded = rescoreSessionTOList.get(rescoreSessionTOList.size()-1);
			rescoreSessionTOLastAdded.setRescoreItemTOList(rescoreItemTOList);
			rescoreScienceTO.setRescoreSessionTOList(rescoreSessionTOList);
		}
		
		returnMap.put("rescoreElaTO", rescoreElaTO);
		returnMap.put("rescoreMathTO", rescoreMathTO);
		returnMap.put("rescoreScienceTO", rescoreScienceTO);
		
		return returnMap;
	}
}
