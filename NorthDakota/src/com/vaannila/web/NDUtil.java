package com.vaannila.web;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.one2team.highcharts.server.export.ExportType;
import org.one2team.highcharts.server.export.HighchartsExporter;

import com.vaannila.DAO.CommonDAOImpl;
import com.vaannila.TO.ContentTO;
import com.vaannila.TO.StudentScoresTO;
import com.vaannila.util.PropertyFile;


public class NDUtil {

	/**
	 * This method is to create PNG file for graph
	 * @param scoreTO
	 * @param contentid
	 * @param studBioId
	 * @return
	 */
	public String createProjectionGraph(ContentTO contentTO, long contentid, long studBioId, boolean svg) {
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
	
	
	
	public Reader getProjectionSvgReader(ContentTO contentTO, long contentid, long studBioId, boolean svg) {
		final SamplesFactory highchartsSamples = SamplesFactory.getSingleton ();
		String chartOption = "";
		String line1, line2, line3, line4, line5;
		
		Reader reader = null;
		
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
				
		
		// export graph as svg file
		Properties prop = PropertyFile.loadProperties("nd.properties");
		String exportLocation = prop.getProperty("imageSaveLoc");
		String exportFileName = "";
		HighchartsExporter<String> pngFromJsonExporter = null;
		exportFileName = contentid + "_" + studBioId + "_" + System.currentTimeMillis() + ".svg";
		pngFromJsonExporter = ExportType.svg.createJsonExporter ();
		
		File file = new File(exportLocation, exportFileName);
		pngFromJsonExporter.export (chartOption, null, file/*new File (exportLocation, exportFileName)*/);
		
		reader = pngFromJsonExporter.getReader();
		return reader;
	}
	
	
	public static ContentTO populateContentTO(HttpServletRequest request, List<StudentScoresTO> currentStudentScore, long contentId) throws Exception {
		
		CommonDAOImpl commonDao = new CommonDAOImpl();
		List<StudentScoresTO> cutScoreList = (request == null)? null : (List<StudentScoresTO>) request.getSession().getAttribute("cutScoreList");
		if(cutScoreList == null) {
			System.out.println("Fetching cutScoreList");
			cutScoreList = commonDao.populateCutScore();
			if(request != null) request.getSession().setAttribute("cutScoreList", cutScoreList);
		}
		List<StudentScoresTO> cutScoreByContent = (request == null)? null : (List<StudentScoresTO>) request.getSession().getAttribute("cutScoreList_"+contentId);
		if( cutScoreByContent == null ) {
			System.out.println("Fetching cutScoreList_ by content");
			cutScoreByContent = getCutScoreByContent(cutScoreList, contentId);
			if(request != null) request.getSession().setAttribute("cutScoreList_"+contentId, cutScoreByContent);
		}
		
		List<StudentScoresTO> newCutScoreByContent = new ArrayList<StudentScoresTO>();
		int growth = -1;
		int loop = 0;
		StudentScoresTO cutScore = null;
		boolean newcutScore = false;
		for(StudentScoresTO tempcutScore : cutScoreByContent) {
			loop++;
			for(StudentScoresTO studentScoresTO : currentStudentScore) {
				cutScore = null;
				for(StudentScoresTO studScore : newCutScoreByContent) {
					if(studScore.getGradeName().equals( tempcutScore.getGradeName()) ) {
						cutScore = studScore;
						newcutScore = false;
						break;
					}
				}
				if(cutScore == null) {
					newcutScore = true;
					cutScore = new StudentScoresTO();
				}
				cutScore.setStudentBioId(studentScoresTO.getStudentBioId());
				
				if(tempcutScore.getGradeSeq() == studentScoresTO.getGradeSeq()) {
					cutScore.setStudentScore(studentScoresTO.getStudentScore());
					cutScore.setAchvLevel(studentScoresTO.getAchvLevel());
					cutScore.setAchvPercentile(studentScoresTO.getAchvPercentile());
					cutScore.setDateAttended(studentScoresTO.getDateAttended());
					cutScore.setSchoolAttended(studentScoresTO.getSchoolAttended());
				}
				if(newcutScore) {
					if(tempcutScore.getGradeName().equals(studentScoresTO.getCurrentGrade()) && growth < 0) {
						growth++;
						if(growth == 0) cutScore.setGrowthScore(studentScoresTO.getStep0());
						
					} else if(growth >= 0) {
						growth++;
						if(growth == 1) cutScore.setGrowthScore(studentScoresTO.getStep1());
						if(growth == 2) cutScore.setGrowthScore(studentScoresTO.getStep2());
						if(growth == 3) cutScore.setGrowthScore(studentScoresTO.getStep3());
					}
				}
				cutScore.setGradeName(tempcutScore.getGradeName());
				cutScore.setpCutScore(tempcutScore.getpCutScore());
				cutScore.setPpCutScore(tempcutScore.getPpCutScore());
				cutScore.setaCutScore(tempcutScore.getaCutScore());
				
				/*cutScore.setContentName(studentScoresTO.getContentName());
				cutScore.setStudentName(studentScoresTO.getStudentName());
				cutScore.setCurrentGrade(studentScoresTO.getCurrentGrade());
				cutScore.setCurrentDistrict(studentScoresTO.getCurrentDistrict());
				cutScore.setCurrentSchool(studentScoresTO.getCurrentSchool());
				cutScore.setGraphText(studentScoresTO.getGraphText());
				cutScore.setTableText(studentScoresTO.getTableText());*/
				
				if(newcutScore) newCutScoreByContent.add(cutScore);
			}
			
		}
		ContentTO contentTo = new ContentTO();
		contentTo.setContentId(contentId);
		contentTo.setStudentScores(newCutScoreByContent);
		
		return contentTo;
	}
	
	public static List<StudentScoresTO> getStudentScoreByContent(List<StudentScoresTO> allStudents, long contentId) {
		List<StudentScoresTO> tempStudents = new ArrayList<StudentScoresTO>();
		for(StudentScoresTO student : allStudents) {
			if(student.getContentId() == contentId) {
				tempStudents.add(student);
			}
		}
		return tempStudents;
	}
	
	public static List<StudentScoresTO> getStudentScoreByStudentBio(List<StudentScoresTO> allStudents, long studentBioId) {
		List<StudentScoresTO> tempStudents = new ArrayList<StudentScoresTO>();
		for(StudentScoresTO student : allStudents) {
			if(student.getStudentBioId() == studentBioId) {
				tempStudents.add(student);
			}
		}
		return tempStudents;
	}
	
	public static List<StudentScoresTO> getStudentScoreByBioAndContent(List<StudentScoresTO> allStudents, long studentBioId, long contentId) {
		List<StudentScoresTO> tempStudents = new ArrayList<StudentScoresTO>();
		for(StudentScoresTO student : allStudents) {
			if(student.getContentId() == contentId && student.getStudentBioId() == studentBioId) {
				tempStudents.add(student);
			}
		}
		return tempStudents;
	}

	public static List<StudentScoresTO> getCutScoreByContent(List<StudentScoresTO> cutScoreTO, long contentId) {
		List<StudentScoresTO> tempCutScoreTO = new ArrayList<StudentScoresTO>();
		for(StudentScoresTO cutScore : cutScoreTO) {
			if(contentId == cutScore.getContentId()) {
				tempCutScoreTO.add(cutScore);
			}
		}
		return tempCutScoreTO;
	}


}
