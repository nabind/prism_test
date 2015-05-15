package com.prism.service;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.prism.dao.MapDao;
import com.prism.mail.EmailSender;
import com.prism.to.OrgTO;
import com.prism.to.StudentTO;
import com.prism.util.AWSStorageUtil;
import com.prism.util.Constants;
import com.prism.util.CustomStringUtil;
import com.prism.util.PropertyFile;
 



//import javax.net.ssl.HttpsURLConnection;

public class MapService implements PrismPdfService {

	private final Logger logger = Logger.getLogger(MapService.class);
	private MapDao dao = null;
	private Calendar cal = null;
	private StringBuffer processLog = null;
	private long lStartTime; // start time
	private long lEndTime; // end time
	private boolean encryptionNeeded = false;
	private boolean archiveNeeded = false;
	private String DDMMYY = "";
	private Map<Integer, String> orgMap = null;
	
	private final String USER_AGENT = "Mozilla/5.0";
	
	int lengthToSplit = 50;
	
	public static void main(String[] args) {
		MapService map = new MapService();
		map.mainMethod(new String[]{"5035", /*"576"*/ "7391"});
	}

	/**
	 * Main method that creates the PDF.
	 * 
	 */
	public void mainMethod(String[] args) {
		logger.info("Starting MapService..");
		String custProdId = args[0];
		String[] schoolIds = CustomStringUtil.getAllButFirstArg(args);
		Properties configProperties = PropertyFile.loadProperties(Constants.MAP_JDBC_PROPERTIES_FILE);
		Properties mapProperties = PropertyFile.loadProperties(Constants.MAP_PROPERTIES_FILE);
		String curStudentSchoolId = null;
		
		try {
			if (dao == null) {
				dao = new MapDao(configProperties);
			}
			for(String schoolOrgNodeId : schoolIds) {
				OrgTO orgTo = dao.getParentOrgNodeId(schoolOrgNodeId);
				String districtOrgNodeId = orgTo.getParentJasperOrgId();
				String customerId = orgTo.getCustomerCode();
				String orgNodePath = orgTo.getOrgNodeCodePath();
				
				String orgNodeCodes[] = orgNodePath.split("~");
				String districtCode = orgNodeCodes[2];
				String schoolCode =  orgNodeCodes[3];
				
				List<StudentTO> students = dao.getStudents(schoolOrgNodeId);
				if(students != null && students.size() == 0) {
					logger.info("There is no student present for school " + schoolOrgNodeId);
					continue;
				}
				String userId = dao.getDummyUser();
				StringBuffer studentIds = new StringBuffer();
				String oldGrade = "", newGrade = "";
				Map<String, String> map = new HashMap<String, String>();
				Set<String> subtest = new HashSet<String>();
				
				// divide students based on grade
				for(StudentTO studentTO : students) {
					newGrade = studentTO.getGradeId();
					subtest.add(studentTO.getSubtest());
					if(oldGrade.equals(newGrade)) {
						studentIds.append(studentTO.getStudentBioId()).append(",");
					} else {
						if("".equals(oldGrade)) {
							oldGrade = newGrade;
							studentIds.append(studentTO.getStudentBioId()).append(",");
						} else {
							//studentIds.delete(studentIds.length()-1, studentIds.length());
							map.put(oldGrade, studentIds.toString());
							studentIds = new StringBuffer();
							studentIds.append(studentTO.getStudentBioId()).append(",");
							oldGrade = newGrade;
						}
					}
					
				}
				// for the last loop
				map.put(oldGrade, studentIds.toString());
				
				// iterate for each grade 
				Iterator itr = map.entrySet().iterator();
				while (itr.hasNext()) {
					Map.Entry mapEntry = (Map.Entry) itr.next();
					String gradeid = (String) mapEntry.getKey();
					String allStudents = (String) mapEntry.getValue();
					if(allStudents != null && allStudents.length() > 1) allStudents = allStudents.substring(0, allStudents.length()-1);
					
					
					String[] studentArr = allStudents.split(",");
					List<String> chunks = new  ArrayList<String>();
					if(studentArr.length > lengthToSplit) {
						int arrayLength = studentArr.length;
						for (int i = 0; i < arrayLength; i = i + lengthToSplit) {
							String[] val = new String[lengthToSplit];
							if (arrayLength < i + lengthToSplit) {
								lengthToSplit = arrayLength - i;
							}
							System.arraycopy(studentArr, i, val, 0, lengthToSplit);

							String idString = StringUtils.arrayToCommaDelimitedString(val);
							idString = idString.replace(",null", "");
							chunks.add(idString);
						}
						
						System.out.println();
					} else {
						chunks.add(allStudents);
					}
					
					String rootPath = dao.getRootFilePath(customerId,custProdId);
					String rootLocForS3 = CustomStringUtil.appendString(rootPath, File.separator, 
							"ISR", File.separator,districtCode,File.separator,
							schoolCode,File.separator);
					
					//Removing entire directory in S3 for the current school
					List<KeyVersion> keys = new ArrayList<KeyVersion>();
					
					
					String[] StudentIdArr = allStudents.split(",");
					
					for (int i= 0; i < StudentIdArr.length; i++) {
						for(String subtestId : subtest) {
							keys.add(new KeyVersion(CustomStringUtil.appendString(rootLocForS3,  
									"MAP_ISR_",custProdId,"_",districtCode,"_",schoolCode,"_",gradeid,"_",subtestId,"_",StudentIdArr[i],".pdf")));
						}
					}					
					
					removeFilesFromS3(keys);
					
					for(String chunkStud : chunks) {
						// now iterate on subtests
						for(String subtestId : subtest) {
							//Need to open the following  checking if we  want to put pdf if the student based on subtest with exact school folder in s3 
							/*curStudentSchoolId = dao.getStudentCurrentSchool(chunkStud, subtestId, custProdId);
							if(curStudentSchoolId != null && curStudentSchoolId.equals(schoolOrgNodeId)) {*/
								String urlParameters = CustomStringUtil.appendString("p_test_administration=", custProdId, "&p_school=", schoolOrgNodeId, "&p_district_Id=", districtOrgNodeId,
										"&p_grade=", gradeid, "&studentId=", chunkStud, "&p_subtest=", subtestId, "&fileName=", "BULK&j_contract=usmo&theme=usmo&mode=SP", "&email=BULK",
										"&customerid=", customerId, "&username=dummyssouser", "", "&userid=", userId);
								
								sendPost(mapProperties, urlParameters);
							/*}*/
							//break; // for debug
						}
						//break; // for debug
					}
					//break; // for debug
				}
			}
			EmailSender.sendMailInors(mapProperties, mapProperties.getProperty("supportEmail"), mapProperties.getProperty("mailSubject"),  mapProperties.getProperty("messageBody"), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Completed!! ");
	}
	
	// HTTP POST request
	private void sendPost(Properties mapProperties, String urlParameters) throws Exception {
		long startTime = System.currentTimeMillis();
		String targetUrl = mapProperties.getProperty("prism.map.url");
		
		logger.info("calling next thread ... ");
		
		/** CODE TO IGNORE SSL CERTIFICATE ERROR **/
		// Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType) throws CertificateException {
					// TODO Auto-generated method stub
				}
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType) throws CertificateException {
					// TODO Auto-generated method stub
				}
            }
        };
 
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
 
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
 
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        /** END: CODE TO IGNORE SSL CERTIFICATE ERROR **/
        
		
		HttpURLConnection connection = (HttpURLConnection) new URL(targetUrl+"?"+urlParameters).openConnection();
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		connection.connect();
		
		logger.info(connection.getResponseCode() + " <<-------- x ----------- PDF generation requested for " + lengthToSplit + " students");
		
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		String timeTaken = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(timeDiff), TimeUnit.MILLISECONDS.toSeconds(timeDiff)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeDiff)));
		logger.info("Time Taken = " + timeTaken);
 
	}
	
	/**
	 * 
	 * @param message
	 * @param id
	 */
	private void updateLog(String message, String id) {
		processLog.append("\n");
		processLog.append(getTime()).append(message);
		if (id != null) {
			processLog.append(id);
		}
	}

	/**
	 * Overloaded updateLog() method where id is null by default.
	 * 
	 * @param message
	 */
	private void updateLog(String message) {
		updateLog(message, null);
	}

	/**
	 * Returns time string
	 * 
	 * @return
	 */
	private String getTime() {
		cal = Calendar.getInstance();
		SimpleDateFormat dateformatter = new SimpleDateFormat("hh:mm:ss a");
		StringBuffer time = new StringBuffer();
		time.append("[");
		time.append(dateformatter.format(cal.getTime()));
		time.append("] [UserAccountPdf] ");
		return time.toString();
	}

	/**
	 * Logs Elapsed Time
	 * 
	 * @param message
	 */
	private void logElapsedTime(String message) {
		long difference = lEndTime - lStartTime; // check different
		StringBuffer timeDiff = new StringBuffer();
		if (difference > 1000) {
			timeDiff.append(message).append(" Elapsed : ");
			timeDiff.append(difference / 1000).append(" second(s) ");
			timeDiff.append(difference % 1000).append(" milliseconds");
		} else {
			timeDiff.append(message).append(" Elapsed : ");
			timeDiff.append(difference).append(" milliseconds");
		}
		logger.info(timeDiff.toString());
	}


	/**
	 * This calls mail sending method to send send PDF to school users
	 * 
	 * @param level3OrgId
	 * @param isInitialLoad
	 * @param migration
	 * @param prop
	 * @param mailSubject
	 * @param toMailAddr
	 * @param attachment
	 * @param attachmentTwo
	 * @param processLog
	 * @param schoolUserPresent
	 * @param letterMail
	 * @param supportEmail
	 * @return
	 */
	private boolean sendMail(String level3OrgId, boolean isInitialLoad, boolean migration, Properties prop, String mailSubject, String toMailAddr,
			String attachment, String attachmentTwo, StringBuffer processLog, boolean schoolUserPresent, boolean letterMail, String supportEmail) {
		logger.info("sending mail... ");
		lStartTime = new Date().getTime();
		boolean mailSent = false;
		// String mailSubject = "";
		String mailBody = "";
		try {
			// mailSubject = prop.getProperty("mailSubject");
			mailBody = prop.getProperty("messageBody") + prop.getProperty("messageFooter");
			EmailSender.sendMailTasc(prop, toMailAddr, attachment, attachmentTwo, mailSubject, mailBody, supportEmail);
			mailSent = true;
		} catch (Exception e) {
			logger.info("Mail sending failed ..." + e.getMessage());
			updateLog("Mail sending failed", e.getMessage());
		}
		lEndTime = new Date().getTime();
		logElapsedTime("Mail sending : ");
		return mailSent;
	}

	
	private void removeFilesFromS3(List<KeyVersion> keys) {
		AWSStorageUtil aWSStorageUtil = AWSStorageUtil.getInstance();
		logger.debug("Calling aWSStorageUtil.removeFilesFromS3 ");
		aWSStorageUtil.removeAsset(keys);
		logger.info("File Successfully removed from S3");
	}
	
}
