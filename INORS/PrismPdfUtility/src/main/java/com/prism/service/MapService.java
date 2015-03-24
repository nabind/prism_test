package com.prism.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.prism.dao.MapDao;
import com.prism.mail.EmailSender;
import com.prism.to.OrgTO;
import com.prism.to.StudentTO;
import com.prism.util.Constants;
import com.prism.util.CustomStringUtil;
import com.prism.util.PropertyFile;
import com.prism.util.ReportPDF;
 



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
	
	public static void main(String[] args) {
		MapService map = new MapService();
		map.mainMethod(new String[]{"5050", "1036", "576"});
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
		
		try {
			if (dao == null) {
				dao = new MapDao(configProperties);
			}
			for(String schoolOrgNodeId : schoolIds) {
				OrgTO orgTo = dao.getParentOrgNodeId(schoolOrgNodeId);
				String districtOrgNodeId = orgTo.getParentJasperOrgId();
				String customerId = orgTo.getCustomerCode();
				List<StudentTO> students = dao.getStudents(schoolOrgNodeId);
				String userId = dao.getDummyUser();
				StringBuffer studentIds = new StringBuffer();
				String oldGrade = "", newGrade = "";
				Map<String, String> map = new HashMap<String, String>();
				Set<String> subtest = new HashSet<String>();
				
				// divide students based on grade
				for(StudentTO studentTO : students) {
					// TODO delete existing pdf for this student from s3
					
					
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
					if(allStudents != null) allStudents = allStudents.substring(0, allStudents.length()-1);
					
					
					String[] studentArr = allStudents.split(",");
					List<String> chunks = new  ArrayList<String>();
					int lengthToSplit = 5;
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
					
					for(String chunkStud : chunks) {
						// now iterate on subtests
						for(String subtestId : subtest) {
							String urlParameters = CustomStringUtil.appendString("p_test_administration=", custProdId, "&p_school=", schoolOrgNodeId, "&p_district_Id=", districtOrgNodeId,
									"&p_grade=", gradeid, "&studentId=", chunkStud, "&p_subtest=", subtestId, "&fileName=", "BULK&j_contract=usmo&theme=usmo&mode=SP", "&email=BULK",
									"&customerid=", customerId, "&username=dummyssouser", "", "&userid=", userId);
							
							sendPost(mapProperties, urlParameters);
							
							break;
						}
						break;
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Completed!! ");
	}
	
	// HTTP POST request
	private void sendPost(Properties mapProperties, String urlParameters) throws Exception {
 
		String targetUrl = mapProperties.getProperty("prism.map.url");
		
		HttpURLConnection connection = (HttpURLConnection) new URL(targetUrl+"?"+urlParameters).openConnection();
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		connection.connect();
		
		System.out.println(connection.getResponseCode() + " <<-------- x ----------- PDF generation completed for one set");
 
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

	
}
