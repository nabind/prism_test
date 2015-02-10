package com.ctb.prism.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.MTOM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.ctb.prism.admin.service.IAdminService;
import com.ctb.prism.core.Service.IUsabilityService;
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.transferobject.ProcessTO;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;
import com.ctb.prism.webservice.transferobject.StudentListTO;

@Component(value="StudentDataload")
@MTOM
@WebService(serviceName="StudentDataloadService")
public class SampleWebservice extends SpringBeanAutowiringSupport {
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(SampleWebservice.class.getName());	
	
	@Autowired
	IAdminService adminService;
	
	@Autowired
	IUsabilityService usabilityService;
	
	@Autowired
	private IPropertyLookup propertyLookup;
	
	/**
	 * This parameter is used to get HMac secret key
	 */
	private String hmacsecret= "WPZguVF49hXaRuZfe9L29ItsC2I";
	
	@WebMethod(exclude = true)
	public String getHmacsecret() {
		return hmacsecret;
	}
	@WebMethod(exclude = true)
	public void setHmacsecret(String hmacsecret){
		// this property is set from ws-context.xml (<!-- Set hmacsecret key-->) through setter injection
		this.hmacsecret = hmacsecret;
	}
	
	
    private WebServiceContext wsctx;
    
    @Resource
    @WebMethod(exclude = true)
	public void setWsctx(WebServiceContext context) {
        this.wsctx = context;
    }
    
    @WebMethod(exclude = true)
    public synchronized String getPartitionName() throws Exception {
    	String partitionName = null;
    	try{
    		long start = System.currentTimeMillis();
	    	partitionName = checkPartition();
	    	long end = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "checkPartition() - " + CustomStringUtil.getHMSTimeFormat(end - start));
	    	int retryCount = 0;
	    	while(partitionName == null && retryCount < 3) {
	    		retryCount++;
	    		System.out.println(" ... All partitions are busy ... waiting ... ");
				Thread.sleep(5000);
				partitionName = checkPartition();
			}
	    	return partitionName;
    	} finally {
			/*try {
				usabilityService.updatePartition(partitionName);
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		}
    }
    
    @WebMethod(exclude = true)
    public String checkPartition() throws Exception {
    	return usabilityService.checkPartition();	
    }
    
    /**
     * This web service method is used for OAS student dataload
     * @param studentListTO
     * @return
     */
    @WebMethod
	public StudentDataLoadTO loadStudentDataDev(@WebParam(name = "StudentListTO") StudentListTO studentListTO) {
    	StudentDataLoadTO studentDataLoadTO = new StudentDataLoadTO();
    	studentDataLoadTO.setStatus("SUCCESS");
		studentDataLoadTO.setStatusCode(1);
		studentDataLoadTO.setSummary("Student loaded successfully");
		
		return studentDataLoadTO; 
    }
    
    /**
     * This web service method is used for OAS student dataload
     * @param studentListTO
     * @return
     */
    @WebMethod
	public StudentDataLoadTO loadStudentData(@WebParam(name = "StudentListTO") StudentListTO studentListTO) {
    	StudentDataLoadTO studentDataLoadTO = new StudentDataLoadTO();
    	String partitionName = "";
    	long start = System.currentTimeMillis();
    	long processId = -99;
    	// to print the output xml
    	try {
    		if("true".equals(propertyLookup.get("print.ws.log"))) {
	    		JAXBContext jc = JAXBContext.newInstance( StudentListTO.class );
	    		Marshaller mc = jc.createMarshaller();
	    		mc.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    		mc.marshal(studentListTO, System.out);
	    		mc.marshal(studentListTO, new File("/mnt/ACSIREPORTS/Temp/"+Utils.getDateTime()+"_oas.xml"));
    		}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
    	try {
    		partitionName = getPartitionName();
    		logger.log(logger.INFO, "    >> partitionName : " + partitionName);
    		System.out.println("    >> partitionName : " + partitionName);
    		if(partitionName != null && partitionName.length() > 0) {
    			studentDataLoadTO.setPartitionName(partitionName);
    			// create process
    			studentDataLoadTO = usabilityService.createProces(studentListTO, studentDataLoadTO);
    			processId = studentDataLoadTO.getProcessId();
    			usabilityService.storeOASWSObject(studentListTO, processId, true);
    			logger.log(logger.INFO, "    >> process id : " + studentDataLoadTO.getProcessId());
    			System.out.println("    >> process id : " + studentDataLoadTO.getProcessId());
    			// load student data into staging tables
				studentDataLoadTO = usabilityService.updateStagingData(studentListTO, studentDataLoadTO);
				if(studentDataLoadTO != null) {
					studentDataLoadTO.setStatus("SUCCESS");
					studentDataLoadTO.setStatusCode(1);
					studentDataLoadTO.setSummary("Student loaded successfully");
				}
				logger.log(logger.INFO, "    >> Staging load is completed");
				/* Staging load is completed */
				
				// Call ETL to start workflow
				BufferedReader read = null;
				logger.log(logger.INFO, "    >> Before Proc Call");
				
				File file = new File(propertyLookup.get("ETL.file.loc"));
				String[] commands = new String[]{
						propertyLookup.get("ETL.shell.command.1"),
						propertyLookup.get("ETL.shell.command.2"),
						studentDataLoadTO.getPartitionName()
						};
				Process proc =  Runtime.getRuntime().exec(commands,null,file);
								
				logger.log(logger.INFO, "    >> After Proc Call");
				read = new BufferedReader(new InputStreamReader(
	                    proc.getInputStream()));
				try {
	                proc.waitFor();
	                logger.log(logger.INFO, "    >> After wait for");	               
	            } catch (InterruptedException e) {
	            	logger.log(logger.ERROR, e.getMessage());
	                proc.getErrorStream();
	            }    
	            while (read.ready()) {
	            	logger.log(logger.INFO, read.readLine());
	            	logger.log(logger.INFO, "    >> After read ready");	                	
	            }
	            
	            // Read status from org_process_status table and update studentDataLoadTO.StatusCode and return
	            logger.log(logger.INFO, "    >> ETL data validation is completed. Checking status ... ");
	            // Get validation status
	            ProcessTO processTO = new ProcessTO();
	            processTO.setProcessId(""+studentDataLoadTO.getProcessId());
	            processTO = usabilityService.getProces(processTO);
	            
	            logger.log(logger.INFO, "    >> processTO : ");
	            logger.log(logger.INFO, processTO.toString()); 
	            if(!IApplicationConstants.COMPLETED_FLAG.equals(processTO.getErValidation())) {
	            	logger.log(logger.INFO, "    >> ER VALIDATION FAILED !!! ");
	            	studentDataLoadTO.setStatus("ER_ERROR");
	    			studentDataLoadTO.setStatusCode(-99);
	    			List<String> errorMessage = new ArrayList<String>();
	    			errorMessage.add(processTO.getProcessLog());
	    			studentDataLoadTO.setErrorMessages(errorMessage);
	    			studentDataLoadTO.setSummary("ER data validation failed");
	            } else {
		            if(!IApplicationConstants.COMPLETED_FLAG.equals(processTO.getHierValidation())
		            		|| !IApplicationConstants.COMPLETED_FLAG.equals(processTO.getBioValidation())
		            		|| !IApplicationConstants.COMPLETED_FLAG.equals(processTO.getDemoValidation())
		            		|| !IApplicationConstants.COMPLETED_FLAG.equals(processTO.getContentValidation())
		            		|| !IApplicationConstants.COMPLETED_FLAG.equals(processTO.getObjectiveValidation())
		            		|| !IApplicationConstants.COMPLETED_FLAG.equals(processTO.getItemValidation())
		            	) {
		            	logger.log(logger.INFO, "Data validation failed");
		            	studentDataLoadTO.setStatus("ERROR");
		    			studentDataLoadTO.setStatusCode(0);
		    			List<String> errorMessage = new ArrayList<String>();
		    			errorMessage.add(processTO.getProcessLog());
		    			studentDataLoadTO.setErrorMessages(errorMessage);
		    			studentDataLoadTO.setSummary("Data validation failed");
		            }
	            }
	            //return studentDataLoadTO; 
    		} else {
    			studentDataLoadTO.setStatus("ERROR");
    			studentDataLoadTO.setStatusCode(0);
    			List<String> errorMessage = new ArrayList<String>();
    			errorMessage.add("All processes are busy at this time. Please try later.");
    			studentDataLoadTO.setErrorMessages(errorMessage);
    			studentDataLoadTO.setSummary("All processes are busy");
    		}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "SampleWebservice - loadStudentData :: " + e.getMessage());
			// TODO update process with ERROR LOG and change all validation status to error
			
			if(studentDataLoadTO == null)  studentDataLoadTO = new StudentDataLoadTO();
			studentDataLoadTO.setStatus("ERROR");
			studentDataLoadTO.setStatusCode(0);
			List<String> errorMessage = new ArrayList<String>();
			errorMessage.add("Error invoking web service. Please try later.");
			studentDataLoadTO.setErrorMessages(errorMessage);
			studentDataLoadTO.setSummary("Student dataload failed");
			e.printStackTrace();
		} finally {
			try {
				usabilityService.updatePartition(partitionName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		logger.log(IAppLogger.INFO, "-----------------------------------x----------------------------------- "+processId);
		logger.log(IAppLogger.INFO, "END-to-END web service call - time taken : " + CustomStringUtil.getHMSTimeFormat(end - start));
    	logger.log(IAppLogger.INFO, "Exit: SampleWebservice - loadStudentData");
    	try {
    		if("true".equals(propertyLookup.get("print.ws.log"))) {
	    		System.out.println(processId + " << Process Id | END-to-END web service call - time taken : " + CustomStringUtil.getHMSTimeFormat(end - start));
	    		JAXBContext jc = JAXBContext.newInstance( StudentDataLoadTO.class );
	    		Marshaller mc = jc.createMarshaller();
	    		mc.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    		mc.marshal(studentDataLoadTO, System.out);
    		}
    		usabilityService.storeWSResponse(studentDataLoadTO, studentDataLoadTO.getProcessId(), false);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return studentDataLoadTO; 
    }
    
	
}
