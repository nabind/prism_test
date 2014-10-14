package com.ctb.prism.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.transform.Source;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.MTOM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.ctb.prism.admin.service.IAdminService;
import com.ctb.prism.admin.transferobject.OrgTO;
import com.ctb.prism.admin.transferobject.StgOrgTO;
import com.ctb.prism.admin.transferobject.UserTO;
import com.ctb.prism.core.Service.IUsabilityService;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.login.security.encoder.DigitalMeasuresHMACQueryStringBuilder;
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
    public String getPartitionName() throws Exception {
    	String partitionName = checkPartition();
    	int retryCount = 0;
    	while(partitionName == null && retryCount < 3) {
    		retryCount++;
    		System.out.println(" ... All partitions are busy ... waiting ... ");
			Thread.sleep(5000);
			partitionName = checkPartition();
		}
    	return partitionName;
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
	public StudentDataLoadTO loadStudentData(@WebParam(name = "StudentListTO") StudentListTO studentListTO) {
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
	public StudentDataLoadTO loadStudentDataDev(@WebParam(name = "StudentListTO") StudentListTO studentListTO) {
    	StudentDataLoadTO studentDataLoadTO = new StudentDataLoadTO();
    	String partitionName = "";
    	
    	try {
    		partitionName = getPartitionName();
    		if(partitionName != null && partitionName.length() > 0) {
    			studentDataLoadTO.setPartitionName(partitionName);
    			studentDataLoadTO = usabilityService.createProces(studentListTO, studentDataLoadTO);
				studentDataLoadTO = usabilityService.updateStagingData(studentListTO, studentDataLoadTO);
				if(studentDataLoadTO != null) {
					studentDataLoadTO.setStatus("SUCCESS");
					studentDataLoadTO.setStatusCode(1);
					studentDataLoadTO.setSummary("Student loaded successfully");
				}
    		} else {
    			if(studentDataLoadTO == null)  studentDataLoadTO = new StudentDataLoadTO();
    			studentDataLoadTO.setStatus("ERROR");
    			studentDataLoadTO.setStatusCode(0);
    			List<String> errorMessage = new ArrayList<String>();
    			errorMessage.add("All process are busy at this time. Please try later.");
    			studentDataLoadTO.setErrorMessages(errorMessage);
    		}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "SampleWebservice - loadStudentData :: " + e.getMessage());
			// TODO update process with ERROR LOG and change all validation status to error
			
			if(studentDataLoadTO == null)  studentDataLoadTO = new StudentDataLoadTO();
			studentDataLoadTO.setStatus("ERROR");
			studentDataLoadTO.setStatusCode(0);
			List<String> errorMessage = new ArrayList<String>();
			errorMessage.add(e.getMessage());
			studentDataLoadTO.setErrorMessages(errorMessage);
			e.printStackTrace();
		} finally {
			try {
				usabilityService.updatePartition(partitionName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	logger.log(IAppLogger.INFO, "Exit: SampleWebservice - loadStudentData");
		return studentDataLoadTO; 
    }
    
    
    
    //================================== S A M P L E ==================================================
    /**
     * All methods below are sample webservice and currently excluded from webservice
     */
    @WebMethod(exclude = true)
	public String testXMLAttachment(@WebParam(name = "xml") Source xmlAtt) {
    	javax.xml.transform.stream.StreamSource ss = (javax.xml.transform.stream.StreamSource) xmlAtt;
    	String result = getStringFromInputStream(ss.getInputStream());
    	System.out.println(result);
		return result;
    }
    
    @WebMethod(exclude = true)
	public String testBean(@WebParam(name = "UserTO") UserTO userTo) {
    	System.out.println(userTo.getFirstName());
		return userTo.getFirstName() + " " + userTo.getLastName();
    }
	
	@WebMethod(exclude = true)
	@RequestMapping(value="/hmac", method=RequestMethod.GET)
	public String sayHello(@WebParam(name = "UserName") String userName) {
		MessageContext mctx = wsctx.getMessageContext();
		//get detail from request headers
        Map http_headers = (Map) mctx.get(MessageContext.HTTP_REQUEST_HEADERS);
        List userList = (List) http_headers.get("Username");
        List passList = (List) http_headers.get("Password");
        
        List ipAddressList = (List) http_headers.get("ipAddress");
        List validUntilDateList = (List) http_headers.get("validUntilDate"); 
        List signatureList = (List) http_headers.get("signature"); 
        
        String username = "";
        String password = ""; 
        
        String ipAddress = "";
        String validUntilDate = "";
        String signature = ""; 
        
          String SECRET_KEY = hmacsecret;//"WPZguVF49hXaRuZfe9L29ItsC2I";
    	  String ENCODING_ALGORITHM = "HmacSHA1";
    	  String URL_ENCODING = "UTF-8";
    	  String timeZone = "PST";
    	  String theme ="inors";
        
        if(userList!=null && ipAddressList!=null && validUntilDateList!=null && signatureList!=null){
        	//get username
        	username = userList.get(0).toString();
        	ipAddress =ipAddressList.get(0).toString();
        	validUntilDate =validUntilDateList.get(0).toString();
        	signature =signatureList.get(0).toString();
        	DigitalMeasuresHMACQueryStringBuilder hMAcSecure = new DigitalMeasuresHMACQueryStringBuilder(SECRET_KEY, 60, ENCODING_ALGORITHM);
        	hMAcSecure.setTimeZone(timeZone);
        	hMAcSecure.setENCODING_ALGORITHM(ENCODING_ALGORITHM);
        	hMAcSecure.setURL_ENCODING(URL_ENCODING);
        	try {
        
				if(hMAcSecure.isValidRequest(URLDecoder.decode(username, URL_ENCODING), 
						URLDecoder.decode(ipAddress, URL_ENCODING), 
						URLDecoder.decode(validUntilDate, URL_ENCODING),
						URLDecoder.decode(signature, URL_ENCODING), null, null, null, null,theme)){
					return "Hello " + userName;
				 } else{
			        	return "Unknown User!";
			        }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } 
        
        return "Insufficient authorization";
        
		
        /*if(passList!=null){
        	//get password
        	password = passList.get(0).toString();
        }*/
		
      //Should validate username and password with database
        /*if (username.equals("Abir") && password.equals("password")){
        	return "Hello " + userName;
        }else{
        	return "Unknown User!";
        }*/
		
	}
	
	@WebMethod(operationName="searchUser", exclude = true)
	public ArrayList<UserTO> searchUser(@WebParam(name = "UserName") String userName,
										@WebParam(name = "OrgId") String parentId,
										@WebParam(name = "AdminYear") String adminYear,
										@WebParam(name = "isExactSearch") String isExactSearch) {
		ArrayList <UserTO> userList = null;
		try {
			userList = adminService.searchUser(userName, parentId, adminYear, isExactSearch,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}
	
	@WebMethod(operationName="getOrganizationList", exclude = true)
	public ArrayList<OrgTO> getOrganizationList(@WebParam(name = "TenantId")String tenantId,
												@WebParam(name = "AdminYear") String adminYear,
												@WebParam(name = "CustomerId") String strCustomerId){
		ArrayList<OrgTO> orgList = null;
		long customerId = 0 ;
		if (strCustomerId == null) {
			customerId = 0;
		} else {
			customerId = Long.parseLong(strCustomerId);
		}
		try{
			orgList = (ArrayList<OrgTO>) adminService.getOrganizationList(tenantId, adminYear, customerId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return orgList;
	}
	
	@WebMethod(operationName="addOrganization", exclude = true)
	public String addOrganization(@WebParam(name = "StgOrgTO") StgOrgTO stgOrgTO){
		
		String acknowledgement = adminService.addOrganization(stgOrgTO);
		return acknowledgement;
	}
	
	
	// convert InputStream to String
	private static String getStringFromInputStream(InputStream is) {
 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
 
		return sb.toString();
 
	}
	
}
