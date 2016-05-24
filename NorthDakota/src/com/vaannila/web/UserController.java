package com.vaannila.web;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.mime.content.StringBody;
import org.one2team.highcharts.server.export.ExportType;
import org.one2team.highcharts.server.export.HighchartsExporter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.vaannila.DAO.CommonDAOImpl;
import com.vaannila.TO.AdminTO;
import com.vaannila.TO.ContentTO;
import com.vaannila.TO.OrgTO;
import com.vaannila.TO.ScoreTO;
import com.vaannila.TO.StudentScoresTO;
import com.vaannila.TO.StudentTO;
import com.vaannila.util.PropertyFile;
import com.vaannila.util.ZipUtil;

import com.vaannila.web.SamplesFactory;

@Controller
public class UserController extends MultiActionController {
	
	String jsonStr = "";
	
	/**
	 * Get students with content details
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView welcome(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("welcome method called");
		
		String nodeId = request.getParameter("nodeId");
		List<StudentTO> allStudents = null; //(List<StudentTO>) request.getSession().getAttribute("studentScores");
		if(allStudents == null) {
			CommonDAOImpl commonDao = new CommonDAOImpl();
			allStudents = commonDao.getScores(nodeId);
			int totalContentCount = commonDao.getTotalContentCount();
			request.getSession().setAttribute("totalContentCount", totalContentCount);
		}
		request.getSession().setAttribute("studentScores", allStudents);
		request.getSession().setAttribute("graphCount", 0);
		request.getSession().setAttribute("tableCount", 0);
		
		return new ModelAndView("welcome", "message", "");
	}
	
	/**
	 * Get students with content details
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView webgraph(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("webgraph method called");
		
		String nodeId = request.getParameter("nodeId");
		List<StudentTO> allStudents = (List<StudentTO>) request.getSession().getAttribute("webgraphStudentScores_"+nodeId);
		if(allStudents == null) {
			CommonDAOImpl commonDao = new CommonDAOImpl();
			allStudents = commonDao.getScoresForAllStudents(nodeId);
			//allStudents = commonDao.getScores(nodeId);
			int totalContentCount = commonDao.getTotalContentCount();
			request.getSession().setAttribute("webgraphTotalContentCount", totalContentCount);
		}
		request.setAttribute("nodeId_", nodeId);
		request.getSession().setAttribute("webgraphStudentScores_"+nodeId, allStudents);
		
		request.getSession().setAttribute("graphCount", 0);
		request.getSession().setAttribute("tableCount", 0);
		
		return new ModelAndView("office", "message", "");
	}
	
	/**
	 * This list down all schools
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView process(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("process method called");
		
		CommonDAOImpl commonDao = new CommonDAOImpl();
		int[] projectionPDFCount= commonDao.getAllProjectionPdfNodeCount();
		int[] rosterPDFCount= commonDao.getAllRosterPdfNodeCount();
		List<OrgTO> allNodes = commonDao.getAllNodes();
		
		List<OrgTO> allUngeneratedProjectionPdfNodes = commonDao.getAllUngenaratedProjectionPdfNodes();
		List<OrgTO> allUngeneratedRosterPdfNodes = commonDao.getAllUngenaratedRosterPdfNodes();
		
		List<ContentTO> allContents = commonDao.getAllContents();
		
		AdminTO admin = commonDao.getCurrentAdmin();
		
		request.setAttribute("allNodes", allNodes);
		request.getSession().setAttribute("allNodes", allNodes);
		
		request.setAttribute("allUngeneratedNodes", allUngeneratedProjectionPdfNodes);
		//request.getSession().setAttribute("allUngeneratedNodes", allUngeneratedProjectionPdfNodes);
		request.setAttribute("allUngeneratedRosterPdfNodes", allUngeneratedRosterPdfNodes);
		request.getSession().setAttribute("allContents", allContents);
		
		request.setAttribute("admin", admin);
		request.setAttribute("errorCount", projectionPDFCount[0]);
		request.setAttribute("alreadyGeneratedCount", projectionPDFCount[1]);
		request.setAttribute("allCount", projectionPDFCount[2]);
		request.setAttribute("errorCountRosterPdf", rosterPDFCount[0]);
		request.setAttribute("alreadyGeneratedCountRosterPdf", rosterPDFCount[1]);
		request.setAttribute("allCountRosterPdf", rosterPDFCount[2]);
	
		Properties prop = PropertyFile.loadProperties("nd.properties");
		request.setAttribute("dataSource", prop.getProperty("dbUserName"));
		
		return new ModelAndView("process", "message", "");
	}
	
	/**
	 * This method is to generate PDF based on school
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public @ResponseBody String generatePdf(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		System.out.println("generatePdf method called");
		try {
			String nodeId = request.getParameter("nodeId");
			CommonDAOImpl commonDao = new CommonDAOImpl();
			commonDao.updatePdfLocation("", nodeId, false);
			
			request.getSession().setAttribute("pdfStatus_"+nodeId, 0);
			request.getSession().setAttribute("pdf_totalpage_"+nodeId, 1);
			
			List<StudentTO> allStudents = null;
			//CreatePdf pdfTable = new CreatePdf(request);
			ItextCreatePdf pdfTable = new ItextCreatePdf(request);
			
			allStudents = commonDao.getScores(nodeId);
			
			String fileName = getPdfFileLocation(request, nodeId);
			pdfTable.generateGrowthPdf(allStudents, nodeId, fileName);
			
			commonDao.updatePdfLocation(fileName, nodeId, false);
			
			System.out.println(nodeId + " : -------------- file name : "+fileName);
			//response.setContentType("plain/text");
			//response.getWriter().write( "Success" );
			response.setContentType("application/json");
			response.getWriter().write( "{\"status\":\"Success\", \"pdfFileName\":\"FirstPdf.pdf\"}" );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * This method is to generate Growth PDF based on school
	 * created new in phase II 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public @ResponseBody String getGrowthReport(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("processDetails method called");
		try {
			Properties prop = PropertyFile.loadProperties("nd.properties");
			String exportLocation = prop.getProperty("imageSaveLoc");
			exportLocation = exportLocation + File.separator + request.getParameter("growthFileName");
			String msg = null;
			String nodeId = request.getParameter("nodeId");
			
			request.getSession().setAttribute("pdfStatus_"+nodeId, 0);
			request.getSession().setAttribute("pdf_totalpage_"+nodeId, 10);
			
			CommonDAOImpl commonDao = new CommonDAOImpl();
			commonDao.updatePdfLocation("", nodeId, false);
			
			String fileName = exportLocation + ".pdf";
			request.getSession().setAttribute("pdfStatus_"+nodeId, 1);
			msg = RosterPDF.saveRosterFromJasper(nodeId, "", fileName, false);
			if(msg == null) {
				// success
				request.getSession().setAttribute("growthPdfFile_"+nodeId, fileName);
				request.getSession().setAttribute("pdfStatus_"+nodeId, 9);
				System.out.println(nodeId + " : -------------- file name : "+fileName);
				commonDao.updatePdfLocation(fileName, nodeId, false);
			} 
			
			request.getSession().setAttribute("pdfStatus_"+nodeId, 10);
			response.setContentType("application/json");
			response.getWriter().write( "{\"status\":\"Success\", \"message\":\""+msg+"\"}" );
		} catch (Exception e) {
			e.printStackTrace();
			response.setContentType("application/json");
			response.getWriter().write( "{\"status\":\"Error\", \"message\":\"Error\"}" );
		}
		
		return null;
	}
	
	/**
	 * Returns formatted pdf file name with location
	 * @param request
	 * @param nodeId
	 * @return
	 */
	private String getPdfFileLocation(HttpServletRequest request, String nodeId) {
		String districtNumber = "", schoolNumber = "", schoolName = "";
		java.util.List<OrgTO> allNodes = (java.util.List<OrgTO>) request.getSession().getAttribute("allNodes");
		for(OrgTO node : allNodes) {
			if(nodeId.equals(""+node.getNodeId())) {
				districtNumber = node.getDistrictNumber();
				schoolNumber = node.getSchoolNumber();
				schoolName = node.getSchoolFullName();
				break;
			}
		}
		Properties prop = PropertyFile.loadProperties("nd.properties");
		String exportLocation = prop.getProperty("imageSaveLoc");
		StringBuffer fileLoc = new StringBuffer();
		fileLoc.append(exportLocation).append(File.separator);
		//fileLoc.append("Projection_").append(nodeId).append("_").append(districtNumber).append("_").append(schoolNumber);
		fileLoc.append(schoolName).append("_").append(nodeId).append("_P").append(".pdf");
		//fileLoc.append("_").append(System.currentTimeMillis()).append(".pdf");
		return fileLoc.toString();
	}
	/**
	 * Get current Date 
	 * @return
	 */
	public static String getDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateformatter = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer time = new StringBuffer();
		time.append(dateformatter.format(cal.getTime()));
		return time.toString();
	}
	
	/**
	 * get student data
	 * @param request
	 * @param studentBioId
	 * @return
	 * @throws Exception
	 */
	private ScoreTO getStudentScore(HttpServletRequest request, long studentBioId) throws Exception {
		
		ScoreTO scoreTO = (ScoreTO) request.getSession().getAttribute("score_"+studentBioId);
		
		if(scoreTO == null) {
			CommonDAOImpl commonDao = new CommonDAOImpl();
			scoreTO = commonDao.getStudentScore(studentBioId);
			request.getSession().setAttribute("score_"+studentBioId, scoreTO);
		} else {
			System.out.println("returning scores from session ...");
		}
		return scoreTO;
	}
	
	/**
	 * return student table data in ajax
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public @ResponseBody String getTableScore(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		System.out.println("getTableScore method called");
		
		try {
			String studentBioId = request.getParameter("studentBioId");
			String contentId = request.getParameter("contentId");
			String contentName = request.getParameter("contentName");
			
			long contentid = Long.valueOf(contentId);
			
			ScoreTO scoreTO = getStudentScore(request, Long.valueOf(studentBioId));
			
			StringBuffer studentScoreTable = new StringBuffer();
			
			ContentTO tempContentTO = null;
			if(scoreTO != null) {
				for(ContentTO contentTO : scoreTO.getContent()) {
					if(contentTO.getContentId() == contentid) {
						tempContentTO = contentTO;
						break;
					}
				}
				studentScoreTable.append("	<table class='table' cellspacing='0' width='750px'> " );
				studentScoreTable.append("	<tr class='nobackground'> " );
				studentScoreTable.append("		<td class='borderDown borderRight textAlignCenter'>Grade</td> " );
				studentScoreTable.append("		<td class='borderDown borderRight textAlignCenter' colspan='4'>"+contentName+"</td> " );
				studentScoreTable.append("		<td class='borderDown textAlignCenter' colspan='2'>North Dakota State Assessment Information</td> " );
				studentScoreTable.append("	</tr> " );
				studentScoreTable.append("	<tr class='nobackground'> " );
				studentScoreTable.append("		<td width='5%' class='borderDown borderRight textAlignRight'></td> " );
				studentScoreTable.append("		<td width='8%' class='borderDown borderRight textAlignCenter'>Student<br/>Score</td> " );
				studentScoreTable.append("		<td width='10%' class='borderDown borderRight textAlignCenter'>Achievement<br/>Level</td> " );
				studentScoreTable.append("		<td width='10%' class='borderDown borderRight textAlignCenter'>Achievement<br/>Percentile</td> " );
				studentScoreTable.append("		<td width='8%' class='borderDown borderRight textAlignCenter'>3-Year<br/>Path</td> " );
				studentScoreTable.append("		<td width='9%' class='borderDown'>Date</td> " );
				studentScoreTable.append("		<td width='50%' class='borderDown'>School Attended</td> " );
				studentScoreTable.append("	</tr> " );
				
				for(StudentScoresTO studentScoresTO : tempContentTO.getStudentScores()) { 
					studentScoreTable.append("	<tr> " );
					studentScoreTable.append("		<td class='borderRight textAlignCenter'>"+studentScoresTO.getGradeName() +"</td> " );
					studentScoreTable.append("		<td class='borderRight textAlignCenter'>"+studentScoresTO.getStudentScore() +"</td> " );
					studentScoreTable.append("		<td class='borderRight textAlignCenter'>"+studentScoresTO.getAchvLevel() +"</td> " );
					studentScoreTable.append("		<td class='borderRight textAlignCenter'>"+studentScoresTO.getAchvPercentile() +"</td> " );
					studentScoreTable.append("		<td class='borderRight textAlignCenter'>"+studentScoresTO.getGrowthScore() +"</td> " );
					studentScoreTable.append("		<td>"+studentScoresTO.getDateAttended() +"</td> " );
					studentScoreTable.append("		<td>"+studentScoresTO.getSchoolAttended() +"</td> " );
					studentScoreTable.append("	</tr> " );
				}
				studentScoreTable.append("</table>");
				studentScoreTable.append("<div style='width:750px'>");
				studentScoreTable.append("	<p>"+tempContentTO.getTableText()+"</p>");
				studentScoreTable.append("</div>");
			}
			response.setContentType("plain/text");
			response.getWriter().write( studentScoreTable.toString() );
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			updateTableCount(request);
		}
		
		return null;
	}
	
	/**
	 * updating graph count
	 * @param request
	 * @param type
	 */
	private  synchronized void updateGraphCount(HttpServletRequest request) {
		Integer graphCount = (Integer) request.getSession().getAttribute("graphCount");
		request.getSession().setAttribute("graphCount", ++graphCount);
	}	
	/**
	 * updating table count
	 * @param request
	 * @param type
	 */
	private  synchronized void updateTableCount(HttpServletRequest request) {
		Integer tableCount = (Integer) request.getSession().getAttribute("tableCount");
		request.getSession().setAttribute("tableCount", ++tableCount);
	}
	
	/**
	 * Get getPdfStatus from ajax progressbar
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public @ResponseBody String getPdfStatus(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		System.out.println("getPdfStatus method called");
		try {
			String nodeId = request.getParameter("nodeId");
			Integer pageCount = (Integer) request.getSession().getAttribute("pdfStatus_"+nodeId);
			Integer totalcount = (Integer) request.getSession().getAttribute("pdf_totalpage_"+nodeId);
			
			BigDecimal pdfNum = new BigDecimal( (pageCount==null) ? 0 : pageCount );
			BigDecimal totalNum = new BigDecimal( (totalcount==null) ? 1 : totalcount );
			
			response.setContentType("application/json");
			response.getWriter().write( "{\"progress\":"+ getPercentageChange(pdfNum, totalNum) +"}" );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Downlaod school PDf
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView viewSchoolPdf(HttpServletRequest req, HttpServletResponse res)
		throws Exception {
		OutputStream os = null;
		try {
			String mergedFile = req.getParameter("mergedFile");
			String roster = req.getParameter("roster");
			
			String nodeId = req.getParameter("nodeId");
			String pdfFileName = (String) req.getSession().getAttribute("pdfFileName_"+nodeId);
			
			boolean zipFile = false;
			if(roster != null && "true".equals(roster)) {
				if(mergedFile != null && mergedFile.equals("true")) {
					Properties prop = PropertyFile.loadProperties("nd.properties");
					pdfFileName = prop.getProperty("imageSaveLoc") + File.separator + prop.getProperty("endRosterFileName");
				} else {
					pdfFileName = (String) req.getSession().getAttribute("rosterPdfFile_"+nodeId);
					if(pdfFileName == null) {
						java.util.List<OrgTO> allNodes = (java.util.List<OrgTO>) req.getSession().getAttribute("allNodes");
						for(OrgTO node : allNodes) {
							if(nodeId.equals(""+node.getNodeId())) {
								pdfFileName = node.getPdfFileNameRoster();
								break;
							}
						}
					}
				}
			} else {
				if(mergedFile != null && mergedFile.equals("true")) {
					Properties prop = PropertyFile.loadProperties("nd.properties");
					pdfFileName = prop.getProperty("finalFileLocation") + prop.getProperty("endFileName") + ".zip";
					zipFile = true;
				} else {
					pdfFileName = (String) req.getSession().getAttribute("growthPdfFile_"+nodeId);
					if(pdfFileName == null) {
						java.util.List<OrgTO> allNodes = (java.util.List<OrgTO>) req.getSession().getAttribute("allNodes");
						for(OrgTO node : allNodes) {
							if(nodeId.equals(""+node.getNodeId())) {
								pdfFileName = node.getPdfFileName();
								break;
							}
						}
					}
				}
			}
			
			if(pdfFileName == null) {
				res.setContentType("text/html");
				res.getWriter().write("<span style='color:red;font-size:20px'> Error Getting PDF for this School [Node Id: "+ nodeId + 
						"]. Please re-generate the PDF.</span>");
				return null;
			}
			File file = null;
			file = new File(pdfFileName);
	
			byte[] pdf = getFileData(file);
			
			if(pdf != null) {
				if(zipFile) {
					res.setContentType("application/zip");
				} else {
					res.setContentType("application/pdf");
				}
				res.setHeader("Content-Disposition","attachment; filename=\"" + file.getName() + "\"");
				res.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				res.setHeader("Pragma", "public");
		
				os = res.getOutputStream();
				for (int i = 0; i < pdf.length; i++) {
					os.write(pdf[i]);
				}
			} else {
				System.out.println("PDF file not present is the specified location");
				res.setContentType("text/html");
				res.getWriter().write("<span style='color:red;font-size:20px'> There is no report exists for this School [Node Id: "+ nodeId + 
						"]. Please re-generate the PDF.</span>");
				
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(os != null) try {os.close();}catch (Exception e) {}
		}
		return null;
	}
			
	/**
	 * Get count from ajax progressbar
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public @ResponseBody String getCount(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		//System.out.println("getcount method called");
		try {
			Integer graphCount = (Integer) request.getSession().getAttribute("graphCount");
			Integer tableCount = (Integer) request.getSession().getAttribute("tableCount");
			Integer totalcount = (Integer) request.getSession().getAttribute("totalContentCount");
			
			BigDecimal graphNum = new BigDecimal(graphCount);
			BigDecimal tableNum = new BigDecimal(tableCount);
			BigDecimal totalNum = new BigDecimal(totalcount);
			
			System.out.println("-------------- graph"+getPercentageChange(graphNum, totalNum));
			System.out.println("-------------- table"+getPercentageChange(tableNum, totalNum));
			
			response.setContentType("application/json");
			response.getWriter().write( "{\"graph\":"+getPercentageChange(graphNum, totalNum)
					+ ", \"table\":"+getPercentageChange(tableNum, totalNum)+"}" );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static BigDecimal rounded(BigDecimal aNumber) {
		return aNumber.setScale(0, BigDecimal.ROUND_HALF_EVEN);
	}

	private static BigDecimal getPercentageChange(BigDecimal current, BigDecimal total) {
		BigDecimal fractionalChange = current.divide(total, 5, BigDecimal.ROUND_HALF_EVEN);
		return rounded(fractionalChange.multiply(new BigDecimal(100)));
	}
	
	/**
	 * Get graph data for web render
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public @ResponseBody String getGraphData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("getImage method called");
		File file = null;
		String line1 = "";
 		String line2 = "";
 		String line3 = "";
 		String line4 = "";
 		String line5 = "";
		try {
			String studentBioId = request.getParameter("studentBioId");
			String contentId = request.getParameter("contentId");
			
			long studBioId = Long.valueOf(studentBioId);
			long contentid = Long.valueOf(contentId);
			
			ScoreTO scoreTO = getStudentScore(request, studBioId);
			for(ContentTO contentTO : scoreTO.getContent()) {
				if(contentid == contentTO.getContentId()) {
					line1 = contentTO.getGradeFixedScore(1);
					line2 = contentTO.getGradeFixedScore(2);
					line3 = contentTO.getGradeFixedScore(3);
					line4 = contentTO.getStudentScore();
					line5 = contentTO.getGrowthScore();
				}
			}
			
			response.setContentType("application/json");
			response.getWriter().write( "{\"line1\":\""+line1+"\", \"line2\":\""+line2+"\", \"line3\":\""+line3+"\", \"line4\":\""+line4+"\", \"line5\":\""+line5+"\"}" );
		}
		catch (Exception e) {
		  e.printStackTrace ();
		}
		finally {
			updateGraphCount(request);
			removeFile(file);
		}
		
		return null;
	}
	
	/**
	 * get student graph as image
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public @ResponseBody String getImage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("getImage method called");
		OutputStream os = null;
		File file = null;
		try {
		
			String studentBioId = request.getParameter("studentBioId");
			String contentId = request.getParameter("contentId");
			
			//List<StudentTO> allStudents = (List<StudentTO>) request.getSession().getAttribute("studentScores");
			
			long studBioId = Long.valueOf(studentBioId);
			long contentid = Long.valueOf(contentId);
			
			/*ScoreTO scoreTO = null;
			for(StudentTO student : allStudents) {
				if(studBioId == student.getStudentBioId()) {
					scoreTO = student.getScoreTO();
					break;
				}
			}*/
			
			ScoreTO scoreTO = getStudentScore(request, studBioId);
			
			String graph = createStudentGraph(scoreTO, contentid, studBioId, false);
		
			/******************************/
			file = new File(graph);
			byte[] pdf = getFileData(file);
			
			if(pdf != null) {
				response.setContentType("image/png");
				os = response.getOutputStream();
				for (int i = 0; i < pdf.length; i++) {
					os.write(pdf[i]);
				}
			} 
		}
		catch (Exception e) {
		  e.printStackTrace ();
		}
		finally {
			updateGraphCount(request);
			if(os != null) try {os.close();}catch (Exception e) {}
			removeFile(file);
		}
		
		return null;
	}
	
	/**
	 * This method is to create PNG file for graph
	 * @param scoreTO
	 * @param contentid
	 * @param studBioId
	 * @return
	 * 
	 * This method is moved to NDUtil
	 */
	@Deprecated
	public static String createProjectionGraph(ContentTO contentTO, long contentid, long studBioId, boolean svg) {
		final SamplesFactory highchartsSamples = SamplesFactory.getSingleton ();
		String chartOption = "";
		String line1, line2, line3, line4, line5;
		
		line1 = contentTO.getGradeFixedScore(3);
		line2 = contentTO.getGradeFixedScore(2);
		line3 = contentTO.getGradeFixedScore(1);
		line4 = contentTO.getStudentScore();
		line5 = contentTO.getGrowthScore();
		
		chartOption = highchartsSamples.createJsonColumnBasic();
		if(1002 == contentid) {
			// Reading
			chartOption = chartOption.replace("scale_min", "450");
		} else {
			chartOption = chartOption.replace("scale_min", "350");
		}
		chartOption = chartOption.replace("line1", line1);
		chartOption = chartOption.replace("line2", line2);
		chartOption = chartOption.replace("line3", line3);
		chartOption = chartOption.replace("line4", line4);
		chartOption = chartOption.replace("line5", line5);
				
		
		// export graph as png file
		Properties prop = PropertyFile.loadProperties("nd.properties");
		String exportLocation = prop.getProperty("imageSaveLoc");
		String exportFileName = "";
		HighchartsExporter<String> pngFromJsonExporter = null;
		if(svg) {
			exportFileName = contentid + "_" + studBioId + "_" + System.currentTimeMillis() + ".svg";
			pngFromJsonExporter = ExportType.svg.createJsonExporter ();
		} else {
			exportFileName = contentid + "_" + studBioId + "_" + System.currentTimeMillis() + ".png";
			pngFromJsonExporter = ExportType.png.createJsonExporter ();
		}
		
		File file = new File(exportLocation, exportFileName);
		pngFromJsonExporter.export (chartOption, null, file/*new File (exportLocation, exportFileName)*/);
		return file.getAbsolutePath();
	}
	
	/**
	 * This method is to create PNG file for graph
	 * @param scoreTO
	 * @param contentid
	 * @param studBioId
	 * @deprecated use createProjectionGraph
	 * @return
	 */
	@Deprecated
	public static String createStudentGraph(ScoreTO scoreTO, long contentid, long studBioId, boolean svg) {
		final SamplesFactory highchartsSamples = SamplesFactory.getSingleton ();
		String chartOption = "";
		String line1, line2, line3, line4, line5;
		for(ContentTO contentTO : scoreTO.getContent()) {
			if(contentTO.getContentId() == contentid) {
				line1 = contentTO.getGradeFixedScore(3);
				line2 = contentTO.getGradeFixedScore(2);
				line3 = contentTO.getGradeFixedScore(1);
				line4 = contentTO.getStudentScore();
				line5 = contentTO.getGrowthScore();
				
				chartOption = highchartsSamples.createJsonColumnBasic();
				if(1002 == contentid) {
					// Reading
					chartOption = chartOption.replace("scale_min", "450");
				} else {
					chartOption = chartOption.replace("scale_min", "350");
				}
				chartOption = chartOption.replace("line1", line1);
				chartOption = chartOption.replace("line2", line2);
				chartOption = chartOption.replace("line3", line3);
				chartOption = chartOption.replace("line4", line4);
				chartOption = chartOption.replace("line5", line5);
				
				break;
			}
		}
		
		// export graph as png file
		Properties prop = PropertyFile.loadProperties("nd.properties");
		String exportLocation = prop.getProperty("imageSaveLoc");
		String exportFileName = "";
		HighchartsExporter<String> pngFromJsonExporter = null;
		if(svg) {
			exportFileName = contentid + "_" + studBioId + "_" + System.currentTimeMillis() + ".svg";
			pngFromJsonExporter = ExportType.svg.createJsonExporter ();
		} else {
			exportFileName = contentid + "_" + studBioId + "_" + System.currentTimeMillis() + ".png";
			pngFromJsonExporter = ExportType.png.createJsonExporter ();
		}
		
		File file = new File(exportLocation, exportFileName);
		pngFromJsonExporter.export (chartOption, null, file/*new File (exportLocation, exportFileName)*/);
		return file.getAbsolutePath();
	}
	
	/**
	 * merge all pdfs
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public @ResponseBody String mergePdfs(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String roster = request.getParameter("roster");
		if(roster != null && "true".equals(roster)) {
			return mergeRosterPdfs(request, response);
		} else {
			return mergeProjectionPdfs(request, response);
		}
	}
	
	/**
	 * Merge all projection reports
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private @ResponseBody String mergeProjectionPdfs(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("mergePdfs method called");
		File file = null;
		InputStream is = null;
		StringBuffer msg = null;
		OutputStream output = null;
		try {
			Properties prop = PropertyFile.loadProperties("nd.properties");
			String exportLocation = prop.getProperty("imageSaveLoc");
			exportLocation = exportLocation + File.separator;
			
			List<OrgTO> allNodes = (List<OrgTO>) request.getSession().getAttribute("allNodes");
			java.util.List<InputStream> pdfs = new ArrayList<InputStream>();
			java.util.List<String> pdfFileNames = new ArrayList<String>();
			java.util.List<String> mergedPDFs = new ArrayList<String>();
			int count = 0;
			int finalPDFCount = 0;
			boolean processed = false;
			for(OrgTO node : allNodes) {
				count++;
				processed = false;
				if(node.getPdfFileName() != null && node.getPdfFileName().trim().length() > 0) {
					try{     
						is = new FileInputStream(node.getPdfFileName());
					} catch (Exception ex) {
						if(msg == null) msg = new StringBuffer();
						msg.append("<br/>Node-District-School : ").append(node.getNodeId()).append("-");
						msg.append(node.getDistrictNumber()).append("-").append(node.getSchoolNumber());
						msg.append(" -- Error getting PDF file for school : ").append(node.getSchoolName());
					}
					pdfs.add(is);
					pdfFileNames.add(node.getPdfFileName());
				} else {
					if(msg == null) msg = new StringBuffer();
					msg.append("<br/>Node-District-School : ").append(node.getNodeId()).append("-");
					msg.append(node.getDistrictNumber()).append("-").append(node.getSchoolNumber());
					msg.append(" -- No PDF present for school : ").append(node.getSchoolName());
				}
				
				if(count == 10) {
					processed = true;
					finalPDFCount++;
					count = 0;
					StringBuffer mergedPdfFileName = new StringBuffer();
					mergedPdfFileName.append(prop.getProperty("finalFileLocation")).append(prop.getProperty("endFileName"));
					mergedPdfFileName.append("_").append(finalPDFCount).append(".pdf");
					output = new FileOutputStream(mergedPdfFileName.toString());
					try {
						ConcatPdf.concatPDFs(pdfs, output, false);
						mergedPDFs.add(mergedPdfFileName.toString());
					} catch (Exception e) {
						System.out.println("Error merging the following PDFs");
						for(String name : pdfFileNames) {
							System.out.println("    - " +name);
						}
						e.printStackTrace();
					}
					pdfs = new ArrayList<InputStream>();
					pdfFileNames = new ArrayList<String>();
				}
			}
			if(!processed) {
				// last loop of PDFs
				finalPDFCount++;
				StringBuffer mergedPdfFileName = new StringBuffer();
				mergedPdfFileName.append(exportLocation).append(prop.getProperty("endFileName"));
				mergedPdfFileName.append("_").append(finalPDFCount).append(".pdf");
				output = new FileOutputStream(mergedPdfFileName.toString());
				try {
					ConcatPdf.concatPDFs(pdfs, output, false);
					mergedPDFs.add(mergedPdfFileName.toString());
				} catch (Exception e) {
					System.out.println("Error merging the following PDFs");
					for(String name : pdfFileNames) {
						System.out.println("    - " +name);
					}
					e.printStackTrace();
				}
			}
			
			// create archive
			StringBuffer archiveFileName = new StringBuffer();
			archiveFileName.append(prop.getProperty("finalFileLocation")).append(prop.getProperty("endFileName")).append(".zip");
			ZipUtil.zipit(mergedPDFs, archiveFileName.toString());
			
			//OutputStream output = output = new FileOutputStream(exportLocation + prop.getProperty("endFileName") + "_1.pdf");
			//ConcatPdf.concatPDFs(pdfs, output, false);
			if(msg == null) {
				msg = new StringBuffer();
				msg.append("Merged PDF Successfully");
			}
			response.setContentType("application/json");
			response.getWriter().write( "{\"status\":\"Success\", \"message\":\""+msg.toString()+"\"}" );
		}
		catch (Exception e) {
		  e.printStackTrace ();
		} finally {
			if(is != null) try {is.close();}catch (Exception e) {}
		}
		return null;
	}
	
	/**
	 * Clean old PDFs from system
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public @ResponseBody String cleanFiles(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("mergePdfs method called");
		File file = null;
		InputStream is = null;
		StringBuffer msg = null;
		OutputStream output = null;
		try {
			Properties prop = PropertyFile.loadProperties("nd.properties");
			String exportLocation = prop.getProperty("imageSaveLoc");
			exportLocation = exportLocation + File.separator;
			
			List<OrgTO> allNodes = (List<OrgTO>) request.getSession().getAttribute("allNodes");
			java.util.List<String> projectionFileNames = new ArrayList<String>();
			java.util.List<String> rosterFileNames = new ArrayList<String>();
			java.util.List<String> oldFiles = new ArrayList<String>();
			int count = 0;
			for(OrgTO node : allNodes) {
				count++;
				if(node.getPdfFileName() != null && node.getPdfFileName().trim().length() > 0) {
					projectionFileNames.add(node.getPdfFileName());
				} 
				if(node.getPdfFileNameRoster() != null && node.getPdfFileNameRoster().trim().length() > 0) {
					rosterFileNames.add(node.getPdfFileNameRoster());
				}
			}
			File folder = new File(exportLocation);
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if(listOfFiles[i].getName().startsWith("North_Dakota_Growth")) continue;
				
				if( !projectionFileNames.contains(listOfFiles[i].getAbsolutePath()) ) {
					oldFiles.add(listOfFiles[i].getAbsolutePath());
				}
				
				if( !projectionFileNames.contains(listOfFiles[i].getAbsolutePath()) ) {
					oldFiles.add(listOfFiles[i].getAbsolutePath());
				}
			}
			
			for(String remove : oldFiles) {
				System.out.println("removing the following old files ... ");
				System.out.println(remove);
				
				removeFile(remove);
			}
			
		}
		catch (Exception e) {
		  e.printStackTrace ();
		} finally {
			if(is != null) try {is.close();}catch (Exception e) {}
		}
		return null;
	}
	
	
	/**
	 * Merge all roster reports
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private @ResponseBody String mergeRosterPdfs(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("mergePdfs method called");
		File file = null;
		InputStream is = null;
		StringBuffer msg = null;
		try {
			Properties prop = PropertyFile.loadProperties("nd.properties");
			String exportLocation = prop.getProperty("imageSaveLoc");
			exportLocation = exportLocation + File.separator;
			
			List<OrgTO> allNodes = (List<OrgTO>) request.getSession().getAttribute("allNodes");
			java.util.List<InputStream> pdfs = new ArrayList<InputStream>();
			for(OrgTO node : allNodes) {
				if(node.getPdfFileNameRoster() != null && node.getPdfFileNameRoster().trim().length() > 0) {
					try{     
						is = new FileInputStream(node.getPdfFileNameRoster());
					} catch (Exception ex) {
						if(msg == null) msg = new StringBuffer();
						msg.append("<br/>Node-District-School : ").append(node.getNodeId()).append("-");
						msg.append(node.getDistrictNumber()).append("-").append(node.getSchoolNumber());
						msg.append(" -- Error getting Roster PDF file for school : ").append(node.getSchoolName());
					}
					pdfs.add(is);
				} else {
					if(msg == null) msg = new StringBuffer();
					msg.append("<br/>Node-District-School : ").append(node.getNodeId()).append("-");
					msg.append(node.getDistrictNumber()).append("-").append(node.getSchoolNumber());
					msg.append(" -- No Roster PDF present for school : ").append(node.getSchoolName());
				}
			}
			OutputStream output = output = new FileOutputStream(exportLocation + prop.getProperty("endRosterFileName"));
			ConcatPdf.concatPDFs(pdfs, output, false);
			if(msg == null) {
				msg = new StringBuffer();
				msg.append("Merged PDF Successfully");
			}
			response.setContentType("application/json");
			response.getWriter().write( "{\"status\":\"Success\", \"message\":\""+msg.toString()+"\"}" );
		}
		catch (Exception e) {
		  e.printStackTrace ();
		} finally {
			if(is != null) try {is.close();}catch (Exception e) {}
		}
		return null;
	}
	
	/**
	 * Method to remove files
	 * @param file
	 */
	private void removeFile(File file) {
		try {
			if(file != null && file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void removeFile(String fileName) {
		removeFile(new File(fileName));
	}
	
	/**
	 * Utility method to convert file to byte[]
	 * @param file
	 * @return
	 */
	private byte[] getFileData(final File file) {
		byte[] fileData = null;
		java.io.InputStream inputStrem = null;
		int i = 0;
		if (file.isFile()) {
			java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
			try {
				inputStrem = file.toURL().openStream();
				while ((i = inputStrem.read()) != -1) {
					baos.write(i);
				}
				fileData = baos.toByteArray();
			} catch (java.net.MalformedURLException exMalformedURLException) {
				exMalformedURLException.printStackTrace();
			} catch (java.io.IOException exIOException) {
				exIOException.printStackTrace();
			} finally {
				try {
					if (inputStrem != null)
						inputStrem.close();
					baos.close();
				} catch (IOException exIOException) {
					exIOException.printStackTrace();
				}
			}
		}
		return fileData;
	}
	
	/**
	 * This method is to get Roster reports
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public @ResponseBody String getRosterReport(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("processDetails method called");
		try {
			Properties prop = PropertyFile.loadProperties("nd.properties");
			String exportLocation = prop.getProperty("imageSaveLoc");
			exportLocation = exportLocation + File.separator + request.getParameter("rosterFileName");
			String msg = null;
			String nodeId = request.getParameter("nodeId");
			
			CommonDAOImpl commonDao = new CommonDAOImpl();
			commonDao.updatePdfLocation("", nodeId, true);
			
			List<ContentTO> allContents = (List<ContentTO>) request.getSession().getAttribute("allContents");
			java.util.List<InputStream> pdfs = new ArrayList<InputStream>();
			int contentCount = 0;
			for(ContentTO content : allContents) {
				contentCount++;
				//if(contentCount == 3) break; // TODO remove this in next phase
				String fileName = exportLocation + content.getContentId() + ".pdf";
				msg = RosterPDF.saveRosterFromJasper(nodeId, ""+content.getContentId(), fileName, true);
				if(msg == null) {
					// success
					pdfs.add(new FileInputStream( fileName ));
				} else {
					break;
				}
			}
			if(msg == null) {
				OutputStream output = new FileOutputStream(exportLocation + "_temp.pdf");
				ConcatPdf.concatPDFs(pdfs, output, false);
				
				// rotate the PDF to Landscape
				ConcatPdf.rotatePdf(exportLocation + "_temp.pdf", exportLocation + ".pdf");
				
				commonDao.updatePdfLocation(exportLocation + ".pdf", nodeId, true);
				request.getSession().setAttribute("rosterPdfFile_"+nodeId, exportLocation + ".pdf");
				// remove roster by content files
				for(ContentTO content : allContents) {
					removeFile(exportLocation + content.getContentId() + ".pdf");
				}
				removeFile(exportLocation + "_temp.pdf");
			}
			
			response.setContentType("application/json");
			response.getWriter().write( "{\"status\":\"Success\", \"message\":\""+msg+"\"}" );
		} catch (Exception e) {
			e.printStackTrace();
			response.setContentType("application/json");
			response.getWriter().write( "{\"status\":\"Error\", \"message\":\"Error\"}" );
		}
		
		return null;
	}
	
	/*
	private static String getTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateformatter = new SimpleDateFormat("hh:mm:ss a");
		StringBuffer time = new StringBuffer();
		time.append("[");
		time.append(dateformatter.format(cal.getTime()));
		time.append("] [PDF Util] ");
		return time.toString();
	}
	
	public void convertToJson(List<OrgProcess> processes) {
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("product", OrgProcess.class);
		for(Iterator<OrgProcess> itr = processes.iterator(); itr.hasNext();) {
			jsonStr = xstream.toXML(itr.next());
		}
	}
	
	public String convertToJson(OrgTO processes) {
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("process", OrgTO.class);
		return xstream.toXML(processes);
	}*/
	
	
}
