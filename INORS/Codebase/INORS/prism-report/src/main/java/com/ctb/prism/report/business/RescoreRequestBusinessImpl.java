package com.ctb.prism.report.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	private static final IAppLogger logger = LogFactory
			.getLoggerInstance(RescoreRequestBusinessImpl.class.getName());

	@Autowired
	private IRescoreRequestDAO rescoreRequestDAO;

	public List<RescoreRequestTO> getDnpStudentList(Map<String, Object> paramMap)
			throws BusinessException {
		return arrangeRescoreData(rescoreRequestDAO.getDnpStudentList(paramMap));
	}

	private List<RescoreRequestTO> arrangeRescoreData(List<RescoreRequestTO> studentList) {
		
		logger.log(IAppLogger.INFO, "Enter: RescoreRequestBusinessImpl - arrangeRescoreData()");
		long t1 = System.currentTimeMillis();
		
		List<RescoreRequestTO> modifiedStudentList = new ArrayList<RescoreRequestTO>();

		RescoreRequestTO modifiedRescoreRequestTO = null;

		RescoreSubtestTO rescoreSubtestTO = null;
		RescoreSessionTO rescoreSessionTO = null;
		RescoreItemTO rescoreItemTO = null;

		long curStudentBioId = 0;
		long curSubtestId = 0;
		long curSessionId = 0;
		boolean addToList = false;
		int index = 0;
		
		for (RescoreRequestTO rescoreRequestTOStud : studentList) {
		/*while( index < studentList.size() ){*/
			if(index == studentList.size()){
				break;
			}
			rescoreRequestTOStud = studentList.get(index);
			if(curStudentBioId != rescoreRequestTOStud.getStudentBioId()){
				modifiedRescoreRequestTO = new RescoreRequestTO();
				curStudentBioId = rescoreRequestTOStud.getStudentBioId();
				modifiedRescoreRequestTO.setStudentBioId(rescoreRequestTOStud.getStudentBioId());
				modifiedRescoreRequestTO.setStudentFullName(rescoreRequestTOStud.getStudentFullName());
				modifiedRescoreRequestTO.setRequestedDate(rescoreRequestTOStud.getRequestedDate());
				curSubtestId = 0;
				addToList = true;
			}

			for (RescoreRequestTO rescoreRequestTOSubt : studentList) {
			/*while( index < studentList.size() ){*/
				if(index == studentList.size()){
					break;
				}
				rescoreRequestTOSubt = studentList.get(index);
				if(modifiedRescoreRequestTO.getStudentBioId() == rescoreRequestTOSubt.getStudentBioId()){
					if(curSubtestId != rescoreRequestTOSubt.getSubtestId()){
						rescoreSubtestTO = new RescoreSubtestTO();
						curSubtestId = rescoreRequestTOSubt.getSubtestId();
						rescoreSubtestTO.setSubtestId(rescoreRequestTOSubt.getSubtestId());
						rescoreSubtestTO.setSubtestCode(rescoreRequestTOSubt.getSubtestCode());
						rescoreSubtestTO.setPerformanceLevel(rescoreRequestTOSubt.getPerformanceLevel());
						curSessionId = 0;
					}
					
					for (RescoreRequestTO rescoreRequestTOSession : studentList) {
					/*while( index < studentList.size() ){*/
						if(index == studentList.size()){
							break;
						}
						rescoreRequestTOSession = studentList.get(index);
						if((modifiedRescoreRequestTO.getStudentBioId() == rescoreRequestTOSession.getStudentBioId())
								&& (rescoreSubtestTO.getSubtestId() == rescoreRequestTOSession.getSubtestId())){
							if(curSessionId != rescoreRequestTOSession.getSessionId()){
								rescoreSessionTO = new RescoreSessionTO();
								rescoreSessionTO.setSessionId(rescoreRequestTOSession.getSessionId());
								curSessionId = rescoreRequestTOSession.getSessionId();
							}

							for (RescoreRequestTO rescoreRequestTOItem : studentList) {
							/*while( index < studentList.size() ){*/
								if(index == studentList.size()){
									break;
								}
								rescoreRequestTOItem = studentList.get(index);
								if((modifiedRescoreRequestTO.getStudentBioId() == rescoreRequestTOItem.getStudentBioId())
										&& (rescoreSubtestTO.getSubtestId() == rescoreRequestTOItem.getSubtestId())
										&& (rescoreRequestTOSession.getSessionId() == rescoreRequestTOItem.getSessionId())){
									rescoreItemTO =  new RescoreItemTO();
									rescoreItemTO.setItemsetId(rescoreRequestTOItem.getItemsetId());
									rescoreItemTO.setIsRequested(rescoreRequestTOItem.getIsRequested());
									rescoreItemTO.setOriginalScore(rescoreRequestTOItem.getOriginalScore());
									rescoreItemTO.setRrfId(rescoreRequestTOItem.getRrfId());
									rescoreItemTO.setRequestedDate(rescoreRequestTOItem.getRequestedDate());
									rescoreItemTO.setUserId(rescoreRequestTOItem.getUserId());
									rescoreItemTO.setUserName(rescoreRequestTOItem.getUserName());
									rescoreSessionTO.getRescoreItemTOList().add(rescoreItemTO);
									addToList = true;
									//studentList.remove(index);
									index++;
								}
							}
							rescoreSubtestTO.getRescoreSessionTOList().add(rescoreSessionTO);	
						}
					}
					modifiedRescoreRequestTO.getRescoreSubtestTOList().add(rescoreSubtestTO);
				}
			}
			
			if(addToList){
				modifiedStudentList.add(modifiedRescoreRequestTO);
				addToList = false;
			}
		}
		
		long t2 = System.currentTimeMillis();
		logger.log(IAppLogger.INFO, "Exit: RescoreRequestBusinessImpl - arrangeRescoreData() took time: " + String.valueOf(t2 - t1) + "ms");
		return modifiedStudentList;
	}
	
	public com.ctb.prism.core.transferobject.ObjectValueTO submitRescoreRequest(final Map<String, Object> paramMap) 
			throws BusinessException{
		return rescoreRequestDAO.submitRescoreRequest(paramMap);
	}
}
