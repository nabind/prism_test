package com.ctb.prism.inors.business;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctb.prism.admin.dao.IAdminDAO;
import com.ctb.prism.admin.transferobject.ObjectValueTO;
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.inors.dao.IInorsDAO;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.util.ConcatPdf;
import com.ctb.prism.inors.util.PdfGenerator;

/**
 * @author TCS
 * 
 */
@Component("inorBusiness")
public class InorsBusinessImpl implements IInorsBusiness {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(InorsBusinessImpl.class.getName());

	@Autowired
	private IInorsDAO inorsDAO;

	@Autowired
	private IAdminDAO adminDAO;

	@Autowired
	private IPropertyLookup propertyLookup;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#createJob(com.ctb.prism.inors.transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO createJob(BulkDownloadTO bulkDownloadTO) {
		String[] allNodes = (bulkDownloadTO.getSelectedNodes() != null) ? bulkDownloadTO.getSelectedNodes().split(",") : null;
		;
		// if(allNodes == null) return bulkDownloadTO;
		List<ObjectValueTO> students = null;
		long studentCount = 0, schoolCount = 0, classCount = 0;
		StringBuilder studentIds = new StringBuilder();
		Set<String> school = new HashSet<String>();
		Set<String> orgClass = new HashSet<String>();
		Set<String> schoolName = new HashSet<String>();
		Set<String> orgClassName = new HashSet<String>();
		BulkDownloadTO schoolClassNode = null;
		if (allNodes != null) {
			for (String node : allNodes) {

				if (node.indexOf("|") != -1) {
					String nodeId = node.substring(0, node.indexOf("|"));
					if ("-1".equals(node.substring(node.indexOf("|") + 1, node.length()))) {
						studentCount++;
						studentIds.append(nodeId).append(",");
						schoolClassNode = inorsDAO.getSchoolClass(nodeId);
						school.add(schoolClassNode.getSchoolNodes());
						schoolName.add(schoolClassNode.getSchool());
						orgClass.add(schoolClassNode.getClassNodes());
						orgClassName.add(schoolClassNode.getOrgClass());
					} else {

						if ("3".equals(node.substring(node.indexOf("|") + 1, node.length()))) {
							// schoolCount++;
							// bulkDownloadTO.setSchoolNodes(bulkDownloadTO.getSchoolNodes() + "," + nodeId);
							school.add(nodeId);
							schoolClassNode = inorsDAO.getNodeName(nodeId);
							schoolName.add(schoolClassNode.getOrgClass());
						}
						if ("4".equals(node.substring(node.indexOf("|") + 1, node.length()))) {
							// classCount++;
							// bulkDownloadTO.setClassNodes(bulkDownloadTO.getClassNodes() + "," + nodeId);
							orgClass.add(nodeId);
							schoolClassNode = inorsDAO.getNodeName(nodeId);
							schoolName.add(schoolClassNode.getOrgClass());
						}
						// get all students
						students = adminDAO.getAllStudents(bulkDownloadTO.getTestAdministration(), nodeId, bulkDownloadTO.getCustomerId(), bulkDownloadTO.getGrade());
						studentCount += (students == null) ? 0 : students.size();
						if (students != null) {
							for (ObjectValueTO obj : students) {
								studentIds.append(obj.getValue()).append(",");
							}
						}
					}
				}
			}
			if (studentIds.length() > 1) {
				// remove last comma
				studentIds.deleteCharAt(studentIds.length() - 1);
			}
			// all school names
			bulkDownloadTO.setSchool(setToString(schoolName));
			// all school node ids
			bulkDownloadTO.setSchoolNodes(setToString(school));
			// all class names
			bulkDownloadTO.setOrgClass(setToString(orgClassName));
			// all class node ids
			bulkDownloadTO.setSchoolNodes(setToString(orgClass));

			bulkDownloadTO.setSchoolCount(school.size());
			bulkDownloadTO.setClassCount(orgClass.size());
			bulkDownloadTO.setStudentCount(studentCount);
			bulkDownloadTO.setStudentBioIds(studentIds.toString());
		}
		return inorsDAO.createJob(bulkDownloadTO);
	}

	/**
	 * Converts Set<String> to comma separated String.
	 * 
	 * @param node
	 * @return
	 */
	private String setToString(Set<String> node) {
		String[] nodes = node.toArray(new String[0]);
		StringBuilder str = new StringBuilder();
		for (String sch : nodes) {
			str.append(sch).append(",");
		}
		if (str.length() > 1) {
			// remove last comma
			str.deleteCharAt(str.length() - 1);
		}
		return str.toString();
	}

	/**
	 * Get all students.
	 * 
	 * @param jobTO
	 * @return
	 */
	public String getAllStudents(BulkDownloadTO jobTO) {
		String[] allNodes = (jobTO != null && jobTO.getSelectedNodes() != null) ? jobTO.getSelectedNodes().split(",") : null;
		;
		if (allNodes == null)
			return "0";
		List<ObjectValueTO> students = null;
		long studentCount = 0, schoolCount = 0, classCount = 0;
		StringBuilder studentIds = new StringBuilder();
		for (String node : allNodes) {

			if (node.indexOf("|") != -1) {
				String nodeId = node.substring(0, node.indexOf("|"));
				if ("-1".equals(node.substring(node.indexOf("|") + 1, node.length()))) {
					studentCount++;
					studentIds.append(nodeId).append(",");
				} else {

					if ("3".equals(node.substring(node.indexOf("|") + 1, node.length()))) {
						schoolCount++;
					}
					if ("4".equals(node.substring(node.indexOf("|") + 1, node.length()))) {
						classCount++;
					}
					// get all students
					students = adminDAO.getAllStudents(jobTO.getTestAdministration(), nodeId, jobTO.getCustomerId(), jobTO.getGrade());
					studentCount += (students == null) ? 0 : students.size();
					if (students != null) {
						for (ObjectValueTO obj : students) {
							studentIds.append(obj.getValue()).append(",");
						}
					}
				}
			}
		}
		if (studentIds.length() > 1) {
			// remove last comma
			studentIds.deleteCharAt(studentIds.length() - 1);
		}
		return studentIds.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#getJob(java.lang.String)
	 */
	public BulkDownloadTO getJob(String jobId) {
		return inorsDAO.getJob(jobId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#batchPDFDownload(java.lang.String)
	 */
	public void batchPDFDownload(String jobId) {
		logger.log(IAppLogger.INFO, "START ================== f r o m  async method --------------- ");
		BulkDownloadTO jobTO = null;
		jobTO = getJob(jobId);
		if ("CR".equals(jobTO.getDownloadMode())) {
			batchCRPDFDownload(jobId, jobTO);
		} else {
			batchPDFDownload(jobId, jobTO);
		}
	}

	/**
	 * Process candidate report download.
	 * 
	 * @param jobId
	 * @param jobTO
	 */
	private void batchCRPDFDownload(String jobId, BulkDownloadTO jobTO) {
		OutputStream fos = null;
		InputStream is = null;
		StringBuffer log = new StringBuffer();

		// set status to inprogress
		try {
			jobTO.setStatus(IApplicationConstants.INPROGRESS_FLAG);
			updateStatus(jobTO);

			String folderLoc = CustomStringUtil.appendString(propertyLookup.get("pdfGenPath"), File.separator);
			jobTO.setFileLocation(CustomStringUtil.appendString(folderLoc, jobTO.getFileName(), ".pdf"));

			String reportUrl = jobTO.getReportUrl();
			StringBuffer URLStringBuf = new StringBuffer();
			URLStringBuf.append(propertyLookup.get("bulkDownloadUrl"));
			URLStringBuf.append("icDownload.do?reportUrl=").append(reportUrl);
			URLStringBuf.append("&assessmentId=0&type=pdf&token=0&filter=true");
			URLStringBuf.append(jobTO.getSelectedNodes());

			URL url1 = new URL(URLStringBuf.toString());
			fos = new FileOutputStream(jobTO.getFileLocation());

			// Contacting the URL
			logger.log(IAppLogger.INFO, "\nConnecting to " + url1.toString() + " ... ");
			URLConnection urlConn = url1.openConnection();

			// Checking whether the URL contains a PDF
			if (!urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
				logger.log(IAppLogger.ERROR, " : FAILED.\n[Sorry. This is not a PDF.]");
				log.append("Error getting candidate report for job # " + jobId);
			} else {
				// Read the PDF from the URL and save to a local file
				is = url1.openStream();
				IOUtils.copy(is, fos);
			}

			// create archive
			List<String> isrFiles = new ArrayList<String>();
			List<String> studentfiles = new LinkedList<String>();
			List<String> archieveFileNames = new LinkedList<String>();

			isrFiles.add(CustomStringUtil.appendString(folderLoc, jobTO.getQuerysheetFile()));
			archieveFileNames.add(CustomStringUtil.appendString("0-", jobTO.getQuerysheetFile()));
			isrFiles.add(jobTO.getFileLocation());
			archieveFileNames.add(CustomStringUtil.appendString("1-", jobTO.getFileLocation()));

			String archiveFileName = CustomStringUtil.appendString(folderLoc, jobTO.getFileName(), Utils.getDateTime(), ".zip");
			PdfGenerator.zipit(studentfiles, archieveFileNames, archiveFileName);

			File file = new File(archiveFileName);
			double size = 0;
			DecimalFormat f = new DecimalFormat("##.00");
			if (file.exists()) {
				size = file.length() / 1024 / 1024;
				if (size == 0) {
					size = file.length() / 1024;
					jobTO.setFileSize(f.format(size) + "K");
				} else {
					jobTO.setFileSize(f.format(size) + "M");
				}
			}

			// set status to completed
			jobTO.setStatus(IApplicationConstants.COMPLETED_FLAG);
			jobTO.setLog(log.toString());
			updateJob(jobTO);

		} catch (Exception e) {
			e.printStackTrace();
			logger.log(IAppLogger.ERROR, " : FAILED.\n[" + e.getMessage() + "]\n");
			log.append("Error Creating CR files : " + e.getMessage());
			// set status to error
			jobTO.setStatus(IApplicationConstants.ERROR_FLAG);
			jobTO.setLog(log.toString());
			updateJobStatusAnsLog(jobTO);
			System.exit(0);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
		}
	}

	/**
	 * Process inors file download
	 * 
	 * @param jobId
	 * @param jobTO
	 */
	private void batchPDFDownload(String jobId, BulkDownloadTO jobTO) {
		String folderLoc = "";
		OutputStream fos = null;
		InputStream is = null;
		StringBuffer log = new StringBuffer();

		boolean icDownload = false;
		if (IApplicationConstants.DOWNLOAD_MODE_IC.equals(jobTO.getDownloadMode())) {
			icDownload = true;
		}

		// set status to inprogress
		jobTO.setStatus(IApplicationConstants.INPROGRESS_FLAG);
		updateStatus(jobTO);
		folderLoc = CustomStringUtil.appendString(propertyLookup.get("pdfGenPath"), File.separator);
		jobTO.setFileLocation(CustomStringUtil.appendString(folderLoc, jobTO.getFileName(), ".pdf"));
		String studentIds = getAllStudents(jobTO);
		String reportUrl = jobTO.getReportUrl(); // "/public/ISTEP/Reports/Dummy_Student_Report_files";

		// split pdfs into multiple files
		String[] students = studentIds.split(",");
		StringBuffer URLStringBuf = new StringBuffer();
		URLStringBuf.append(propertyLookup.get("bulkDownloadUrl"));
		URLStringBuf.append("icDownload.do?reportUrl=").append(reportUrl);
		URLStringBuf.append("&assessmentId=0&type=pdf&token=0&filter=true&p_studentIds=");

		// get ISR files from jasper
		Map<String, String> isrFileMap = new HashMap<String, String>();
		try {
			for (String studentId : students) {
				// TODO provide proper naming convention
				String studentFileName = CustomStringUtil.appendString(folderLoc, jobTO.getFileName(), studentId, Utils.getDateTime(), ".pdf");
				isrFileMap.put(studentId, studentFileName);

				URL url1 = new URL(URLStringBuf.toString() + studentId);
				fos = new FileOutputStream(studentFileName);

				// Contacting the URL
				logger.log(IAppLogger.INFO, "\nConnecting to " + url1.toString() + " ... ");
				URLConnection urlConn = url1.openConnection();

				// Checking whether the URL contains a PDF
				if (!urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
					logger.log(IAppLogger.ERROR, studentId + " : FAILED.\n[Sorry. This is not a PDF.]");
					log.append("Error getting ISR for " + studentId);
				} else {
					// Read the PDF from the URL and save to a local file
					is = url1.openStream();
					IOUtils.copy(is, fos);
				}
			}
		} catch (Exception npe) {
			logger.log(IAppLogger.ERROR, studentIds + " : FAILED.\n[" + npe.getMessage() + "]\n");
			log.append("Error ISR files : " + npe.getMessage());
			// set status to error
			jobTO.setStatus(IApplicationConstants.ERROR_FLAG);
			jobTO.setLog(log.toString());
			updateJobStatusAnsLog(jobTO);
			System.exit(0);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
		}

		if (icDownload) {
			procesICFiles(jobTO, folderLoc, students, isrFileMap, log);
		} else {
			// archive files based on parameters
			procesFiles(jobTO, folderLoc, students, isrFileMap, log);
		}

		// email notification
		// TODO code for email notification

		logger.log(IAppLogger.INFO, "END   ================== f r o m  async method --------------- ");
	}

	/**
	 * Process ic files
	 * 
	 * @param jobTO
	 * @param folderLoc
	 * @param students
	 * @param isrFileMap
	 * @param log
	 */
	private void procesICFiles(BulkDownloadTO jobTO, String folderLoc, String[] students, Map<String, String> isrFileMap, StringBuffer log) {
		OutputStream fos = null;
		InputStream is = null;
		try {
			List<String> isrFiles = new ArrayList<String>();
			List<String> studentfiles = new LinkedList<String>();
			List<String> archieveFileNames = new LinkedList<String>();
			if (IApplicationConstants.DOWNLOAD_TYPE_MERGED.equals(jobTO.getRequestType())) {
				// add querysheet file
				isrFiles.add(CustomStringUtil.appendString(folderLoc, jobTO.getQuerysheetFile()));
				// add student IC files
				for (String studentId : students) {
					isrFiles.add(isrFileMap.get(studentId));
				}
				String fileName = CustomStringUtil.appendString(folderLoc, jobTO.getFileName(), "merged.pdf");
				fos = new FileOutputStream(fileName);
				ConcatPdf.concatPDFs(isrFiles, fos, false, folderLoc);
				studentfiles.add(fileName);
				archieveFileNames.add(jobTO.getFileName() + "merged.pdf");
			} else {
				// add querysheet file
				studentfiles.add(CustomStringUtil.appendString(folderLoc, jobTO.getQuerysheetFile()));
				archieveFileNames.add(CustomStringUtil.appendString("0-", jobTO.getQuerysheetFile()));
				for (String studentId : students) {
					// add student IC files
					String isrFile = isrFileMap.get(studentId);
					studentfiles.add(isrFile);
					archieveFileNames.add(CustomStringUtil.appendString("1-", isrFile.substring(isrFile.lastIndexOf(File.separator) + 1)));
				}
			}

			// create archive
			String archiveFileName = CustomStringUtil.appendString(folderLoc, jobTO.getFileName(), Utils.getDateTime(), "IC.zip");
			PdfGenerator.zipit(studentfiles, archieveFileNames, archiveFileName);

			File file = new File(archiveFileName);
			double size = 0;
			DecimalFormat f = new DecimalFormat("##.00");
			if (file.exists()) {
				size = file.length() / 1024 / 1024;
				if (size == 0) {
					size = file.length() / 1024;
					jobTO.setFileSize(f.format(size) + "K");
				} else {
					jobTO.setFileSize(f.format(size) + "M");
				}
			}

			// set status to completed
			jobTO.setStatus(IApplicationConstants.COMPLETED_FLAG);
			jobTO.setLog(log.toString());
			updateJob(jobTO);

		} catch (Exception npe) {
			logger.log(IAppLogger.ERROR, " : FAILED.\n[" + npe.getMessage() + "]\n");
			log.append("Error merging/archiving files : " + npe.getMessage());
			// set status to error
			jobTO.setStatus(IApplicationConstants.ERROR_FLAG);
			jobTO.setLog(log.toString());
			updateStatus(jobTO);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
		}
	}

	/**
	 * Process Group File Download
	 * 
	 * @param jobTO
	 * @param folderLoc
	 * @param students
	 * @param isrFileMap
	 * @param log
	 */
	private void procesFiles(BulkDownloadTO jobTO, String folderLoc, String[] students, Map<String, String> isrFileMap, StringBuffer log) {
		OutputStream fos = null;
		InputStream is = null;
		try {
			List<String> isrFiles = new ArrayList<String>();
			List<String> studentfiles = new LinkedList<String>();
			List<String> archieveFileNames = new LinkedList<String>();
			if (IApplicationConstants.DOWNLOAD_TYPE_MERGED.equals(jobTO.getRequestType())) {
				// add querysheet file
				isrFiles.add(CustomStringUtil.appendString(folderLoc, jobTO.getQuerysheetFile()));
				// add student ISR files
				for (String studentId : students) {
					isrFiles.add(isrFileMap.get(studentId));
					// add image print files
					if ("3".equals(jobTO.getGroupFile())) {
						// TODO
						/** GET CODE for IMAGE PRINT */
					}
				}
				// merge querysheet file + isr + image print
				String fileName = CustomStringUtil.appendString(folderLoc, jobTO.getFileName(), "merged.pdf");
				fos = new FileOutputStream(fileName);
				ConcatPdf.concatPDFs(isrFiles, fos, false, folderLoc);
				studentfiles.add(fileName);
				archieveFileNames.add(jobTO.getFileName() + "merged.pdf");
			} else {
				// add querysheet file
				studentfiles.add(CustomStringUtil.appendString(folderLoc, jobTO.getQuerysheetFile()));
				archieveFileNames.add(CustomStringUtil.appendString("0-", jobTO.getQuerysheetFile()));
				for (String studentId : students) {
					// add student ISR files
					String isrFile = isrFileMap.get(studentId);
					studentfiles.add(isrFile);
					// add image print files
					if ("3".equals(jobTO.getGroupFile())) {
						archieveFileNames.add(CustomStringUtil.appendString("1a-", isrFile.substring(isrFile.lastIndexOf(File.separator) + 1)));
						// TODO
						/** GET CODE for IMAGE PRINT */
						// studentfiles.add( "imageprint file name with loc" );
						// archieveFileNames.add(CustomStringUtil.appendString("1b-", "image print file name"));
					} else {
						archieveFileNames.add(CustomStringUtil.appendString("1-", isrFile.substring(isrFile.lastIndexOf(File.separator) + 1)));
					}

				}
			}

			// create archive
			String archiveFileName = CustomStringUtil.appendString(folderLoc, jobTO.getFileName(), Utils.getDateTime(), ".zip");
			PdfGenerator.zipit(studentfiles, archieveFileNames, archiveFileName);

			File file = new File(archiveFileName);
			double size = 0;
			DecimalFormat f = new DecimalFormat("##.00");
			if (file.exists()) {
				size = file.length() / 1024 / 1024;
				if (size == 0) {
					size = file.length() / 1024;
					jobTO.setFileSize(f.format(size) + "K");
				} else {
					jobTO.setFileSize(f.format(size) + "M");
				}
			}

			// set status to completed
			jobTO.setStatus(IApplicationConstants.COMPLETED_FLAG);
			jobTO.setLog(log.toString());
			updateJob(jobTO);

		} catch (Exception npe) {
			logger.log(IAppLogger.ERROR, " : FAILED.\n[" + npe.getMessage() + "]\n");
			log.append("Error merging/archiving files : " + npe.getMessage());
			// set status to error
			jobTO.setStatus(IApplicationConstants.ERROR_FLAG);
			jobTO.setLog(log.toString());
			updateStatus(jobTO);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#updateStatus(com.ctb.prism.inors.transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO updateStatus(BulkDownloadTO bulkDownloadTO) {
		return inorsDAO.updateStatus(bulkDownloadTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#updateJobLog(com.ctb.prism.inors.transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO updateJobLog(BulkDownloadTO bulkDownloadTO) {
		return inorsDAO.updateJobLog(bulkDownloadTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#updateJob(com.ctb.prism.inors.transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO updateJob(BulkDownloadTO bulkDownloadTO) {
		return inorsDAO.updateJob(bulkDownloadTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#updateJobStatusAnsLog(com.ctb.prism.inors.transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO updateJobStatusAnsLog(BulkDownloadTO bulkDownloadTO) {
		return inorsDAO.updateJobStatusAnsLog(bulkDownloadTO);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			URL url1 = new URL("http://localhost:8080/tasc/icDownload.do");

			fos = new FileOutputStream("C:\\temp\\GroupDownload\\testFile.pdf");

			// Contacting the URL
			logger.log(IAppLogger.INFO, "\nConnecting to " + url1.toString() + " ... ");
			HttpURLConnection urlConn = (HttpURLConnection) url1.openConnection();
			urlConn.setRequestMethod("POST");
			urlConn.setRequestProperty("User-Agent", "Mozilla/5.0");
			urlConn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			// Send post request
			urlConn.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(urlConn.getOutputStream());
			wr.writeBytes("reportUrl=/public/ISTEP/Reports/Dummy_Student_Report_files&assessmentId=0&type=pdf&token=0&filter=true&p_studentIds=23740625");
			wr.flush();
			wr.close();

			int responseCode = urlConn.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url1.toString());
			System.out.println("Post parameters : " + "reportUrl=/public/ISTEP/Reports/Dummy_Student_Report_files&assessmentId=0&type=pdf&token=0&filter=true&p_studentIds=23740625");
			System.out.println("Response Code : " + responseCode);

			if (!urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
				logger.log(IAppLogger.ERROR, " : FAILED.\n[Sorry. This is not a PDF.]");
				throw new Exception();
			} else {
				BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
				IOUtils.copy(is, fos);
			}

		} catch (Exception npe) {
			logger.log(IAppLogger.ERROR, " : FAILED.\n[" + npe.getMessage() + "]\n");
			npe.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#getDownloadData(java.util.Map)
	 */
	public List<? extends BaseTO> getDownloadData(Map<String, String> paramMap) {
		return inorsDAO.getDownloadData(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#getDistricts()
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateDistrictGrt(Map<String, String> paramMap) {
		return inorsDAO.populateDistrictGrt(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#populateSchool(java.lang.Long)
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateSchoolGrt(Map<String, String> paramMap) {
		return inorsDAO.populateSchoolGrt(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.business.IInorsBusiness#getProductNameById(java.lang.Long)
	 */
	public String getProductNameById(Long productId) {
		return inorsDAO.getProductNameById(productId);
	}
}
