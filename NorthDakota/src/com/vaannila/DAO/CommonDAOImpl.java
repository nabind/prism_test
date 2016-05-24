package com.vaannila.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.vaannila.TO.AdminTO;
import com.vaannila.TO.ContentTO;
import com.vaannila.TO.OrgTO;
import com.vaannila.TO.ScoreTO;
import com.vaannila.TO.StudentScoresTO;
import com.vaannila.TO.StudentTO;
import com.vaannila.util.JDCConnectionDriver;
import com.vaannila.util.PropertyFile;


public class CommonDAOImpl {
	static JDCConnectionDriver driver = null;
	static String DATA_SOURCE = "jdbc:jdc:acsi";
	private static long lStartTime, lStartTime2; //start time
	private static long lEndTime, lEndTime2; //end time
	
	private int totalContentCount = 0;
	
	public int getTotalContentCount() {
		return totalContentCount;
	}
	public void setTotalContentCount(int totalContentCount) {
		this.totalContentCount = totalContentCount;
	}


	/**
	 * This method fetch whole dataset for all students
	 * @param orgNodeId
	 * @return
	 * @throws Exception
	 */
	public List<StudentTO> getScores(String orgNodeId) throws Exception {
		lStartTime2 = new Date().getTime(); /** Log time difference*/ 
		
		// getting student details
		lStartTime = new Date().getTime(); /** Log time difference*/ 
		List<StudentTO> allStudents = getStudentDetails(orgNodeId);
		lEndTime = new Date().getTime();   /** Log time difference*/ 
		logElapsedTime("getStudentDetails : ");
		
		/*for(StudentTO studentTO : allStudents) {
			// getting student score for each grade
			lStartTime = new Date().getTime(); *//** Log time difference*//* 
			ScoreTO scoreTO = getStudentScore(studentTO.getStudentBioId());
			lEndTime = new Date().getTime();   *//** Log time difference*//* 
			logElapsedTime("    getStudentScore : ");
			
			// getting content name and table/graph text
			for(ContentTO contentTO : scoreTO.getContent()) {
				lStartTime = new Date().getTime(); *//** Log time difference*//* 
				contentTO = getContentDetails(contentTO, studentTO.getStudentBioId());
				lEndTime = new Date().getTime();   *//** Log time difference*//* 
				logElapsedTime("    getContentDetails : ");
			}
			studentTO.setScoreTO(scoreTO);
		}*/
		lEndTime2 = new Date().getTime();   /** Log time difference*/ 
		lStartTime = lStartTime2;
		lEndTime = lEndTime2;
		logElapsedTime("Total Time : ");
		return allStudents;
	}
	
	/**
	 * This method fetch whole dataset for all students
	 * @param orgNodeId
	 * @return
	 * @throws Exception
	 */
	public List<StudentTO> getScoresForAllStudents(String orgNodeId) throws Exception {
		lStartTime2 = new Date().getTime(); /** Log time difference*/ 
		
		// getting student details
		lStartTime = new Date().getTime(); /** Log time difference*/ 
		List<StudentTO> allStudents = getStudentDetails(orgNodeId);
		lEndTime = new Date().getTime();   /** Log time difference*/ 
		logElapsedTime("getStudentDetails : ");
		
		for(StudentTO studentTO : allStudents) {
			// getting student score for each grade
			lStartTime = new Date().getTime(); /** Log time difference*/ 
			ScoreTO scoreTO = getStudentScore(studentTO.getStudentBioId());
			lEndTime = new Date().getTime();   /** Log time difference*/ 
			logElapsedTime("    getStudentScore : ");
			
			// getting content name and table/graph text
			for(ContentTO contentTO : scoreTO.getContent()) {
				lStartTime = new Date().getTime(); /** Log time difference*/ 
				contentTO = getContentDetails(contentTO, studentTO.getStudentBioId());
				lEndTime = new Date().getTime();   /** Log time difference*/ 
				logElapsedTime("    getContentDetails : ");
			}
			studentTO.setScoreTO(scoreTO);
		}
		lEndTime2 = new Date().getTime();   /** Log time difference*/ 
		lStartTime = lStartTime2;
		lEndTime = lEndTime2;
		logElapsedTime("Total Time : ");
		return allStudents;
	}
	
	/**
	 * This method is used to get school details
	 * @return
	 * @throws Exception
	 */
	public List<OrgTO> getAllNodes() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrgTO school = null;
		List<OrgTO> allSchools = null;
		try {
			if(driver == null) driver = LoadConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			
			String query = "SELECT org.org_nodeid, org.cur_school_name, PROJECTION_FILE_NAME, " +
					"org.cur_district_number, org.cur_school_number, ROSTER_FILE_NAME, org.cur_school_full_name FROM org_node_dim org " +
					"ORDER BY org.cur_district_number, org.cur_school_number ";
			
			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			allSchools = new ArrayList<OrgTO>();
			while(rs.next()) {
				school = new OrgTO();
				school.setNodeId(rs.getLong(1));
				school.setSchoolName(rs.getString(2).replace("/", "-"));
				school.setPdfFileName(rs.getString(3));
				school.setDistrictNumber(rs.getString(4));
				school.setSchoolNumber(rs.getString(5));
				school.setPdfFileNameRoster(rs.getString(6));
				school.setSchoolFullName(rs.getString(7).replace("/", "-"));
				allSchools.add(school); 
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		return allSchools;
	}
	
	/**
	 * This method is used to get school details that has got error while 
	 * Projection Pdf generation
	 * @return
	 * @throws Exception
	 */
	public List<OrgTO> getAllUngenaratedProjectionPdfNodes() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrgTO school = null;
		List<OrgTO> allUngenaratedSchools = null;
		try {
			if(driver == null) driver = LoadConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			
			String query = "SELECT org.org_nodeid, org.cur_school_name, PROJECTION_FILE_NAME, " +
					" org.cur_district_number, org.cur_school_number, ROSTER_FILE_NAME, org.cur_school_full_name FROM org_node_dim org " +
					" WHERE PROJECTION_FILE_NAME IS NULL"+
					" ORDER BY org.cur_district_number, org.cur_school_number ";
			
			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			allUngenaratedSchools = new ArrayList<OrgTO>();
			while(rs.next()) {
				school = new OrgTO();
				school.setNodeId(rs.getLong(1));
				school.setSchoolName(rs.getString(2).replace("/", "-"));
				school.setPdfFileName(rs.getString(3));
				school.setDistrictNumber(rs.getString(4));
				school.setSchoolNumber(rs.getString(5));
				school.setPdfFileNameRoster(rs.getString(6));
				school.setSchoolFullName(rs.getString(7).replace("/", "-"));
				allUngenaratedSchools.add(school); 
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		return allUngenaratedSchools;
	}
	/**
	 * This method is used to get school details that has got error while 
	 * Roster Pdf generation
	 * @return
	 * @throws Exception
	 */
	public List<OrgTO> getAllUngenaratedRosterPdfNodes() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrgTO school = null;
		List<OrgTO> allUngenaratedSchools = null;
		try {
			if(driver == null) driver = LoadConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			
			String query = "SELECT org.org_nodeid, org.cur_school_name, PROJECTION_FILE_NAME, " +
					" org.cur_district_number, org.cur_school_number, ROSTER_FILE_NAME, org.cur_school_full_name FROM org_node_dim org " +
					" WHERE ROSTER_FILE_NAME IS NULL"+
					" ORDER BY org.cur_district_number, org.cur_school_number ";
			
			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			allUngenaratedSchools = new ArrayList<OrgTO>();
			while(rs.next()) {
				school = new OrgTO();
				school.setNodeId(rs.getLong(1));
				school.setSchoolName(rs.getString(2).replace("/", "-"));
				school.setPdfFileName(rs.getString(3));
				school.setDistrictNumber(rs.getString(4));
				school.setSchoolNumber(rs.getString(5));
				school.setPdfFileNameRoster(rs.getString(6));
				school.setSchoolFullName(rs.getString(7).replace("/", "-"));
				allUngenaratedSchools.add(school); 
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		return allUngenaratedSchools;
	}
	/**
	 * This method is used to find the count of the ungenerated Projection Pdf
	 * @return
	 * @throws Exception
	 */
	public int[] getAllProjectionPdfNodeCount() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		 int[] ProjectionPDFCount =new int[3];
		//int errorCount=0;
		try {
			if(driver == null) driver = LoadConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			
			String query = "select A.ALL_COUNT, B.ALL_GEN,C.ALL_UNGEN"+
							" from (select 1 as id, count(1)AS ALL_COUNT from org_node_dim)A,"+
							 " (select 1 as id, count(1) AS ALL_GEN from org_node_dim  where PROJECTION_FILE_NAME is not null)B,"+
							 " (select 1 as id, count(1) AS ALL_UNGEN  from org_node_dim  where PROJECTION_FILE_NAME is null)C"+
							 " where  A.id=B.id"+
							 " and   A.id=C.id";
			
			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				if ((rs.getInt("ALL_COUNT")==rs.getInt("ALL_UNGEN"))||(rs.getInt("ALL_COUNT")==rs.getInt("ALL_GEN")))
				{
					ProjectionPDFCount[0]=0;
					ProjectionPDFCount[1] = rs.getInt("ALL_GEN");
					ProjectionPDFCount[2] = rs.getInt("ALL_COUNT");
				}
				else{
					ProjectionPDFCount[0] = rs.getInt("ALL_UNGEN");
					ProjectionPDFCount[1] = rs.getInt("ALL_GEN");
					ProjectionPDFCount[2] = rs.getInt("ALL_COUNT");
				}
				
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		return ProjectionPDFCount;
	}
	
	/**
	 * This method is used to find the count of the ungenerated Roster Pdf
	 * @return
	 * @throws Exception
	 */
	public int[] getAllRosterPdfNodeCount() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		 int[] rosterPDFCount =new int[3];
		//int errorCount=0;
		try {
			if(driver == null) driver = LoadConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			
			String query = "select A.ALL_COUNT, B.ALL_GEN,C.ALL_UNGEN"+
							" from (select 1 as id, count(1)AS ALL_COUNT from org_node_dim)A,"+
							 " (select 1 as id, count(1) AS ALL_GEN from org_node_dim  where ROSTER_FILE_NAME is not null)B,"+
							 " (select 1 as id, count(1) AS ALL_UNGEN  from org_node_dim  where ROSTER_FILE_NAME is null)C"+
							 " where  A.id=B.id"+
							 " and   A.id=C.id";
			
			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				if ((rs.getInt("ALL_COUNT")==rs.getInt("ALL_UNGEN"))||(rs.getInt("ALL_COUNT")==rs.getInt("ALL_GEN")))
				{
					rosterPDFCount[0]=0;
					rosterPDFCount[1] = rs.getInt("ALL_GEN");
					rosterPDFCount[2] = rs.getInt("ALL_COUNT");
				}
				else{
					rosterPDFCount[0] = rs.getInt("ALL_UNGEN");
					rosterPDFCount[1] = rs.getInt("ALL_GEN");
					rosterPDFCount[2] = rs.getInt("ALL_COUNT");
				}
				
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		return rosterPDFCount;
	}
	
	
	/**
	 * oracle does not support more than 1000 ids in 'IN' clause
	 * so dividing students ids into chunk
	 * @param studentBioId
	 * @return
	 * @throws Exception
	 */
	private String divideIdIntoChunk(String studentBioIds) {
		String[] arrStudBioId = studentBioIds.split(",");
		
		if(arrStudBioId.length < 900) {
			return " Sbd.Student_Bio_Id In (" + studentBioIds + ") ";
		}
		
		StringBuffer tempStudIdList = new StringBuffer();
		int count = 0;
		StringBuffer innerQuery  = new StringBuffer();
		innerQuery.append(" ( ");
		int loop = 0;
		for(int i=0; i<arrStudBioId.length; i++) {
			if(count > 0) tempStudIdList.append(",");
			count++;
			tempStudIdList.append(arrStudBioId[i]);
			if(count == 900) {
				// add to query
				if(loop > 0) innerQuery.append(" OR ");
				innerQuery.append(" Sbd.Student_Bio_Id In ( " + tempStudIdList.toString() + " ) ");
				loop++;
				
				//reset
				tempStudIdList = new StringBuffer();
				count = 0;
			}
		}
		if(loop > 0) innerQuery.append(" OR ");
		innerQuery.append(" Sbd.Student_Bio_Id In ( " + tempStudIdList.toString() + " ) ");
		innerQuery.append(" ) ");
		//System.out.println("########## student bio list : " + innerQuery.toString());
		
		return innerQuery.toString();
	}
	
	/**
	 * This method is to fetch student scores
	 * @param studentBioId
	 * @return
	 * @throws Exception
	 */
	public List<StudentScoresTO> getStudentScore(String studentBioIdList) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<StudentScoresTO> scoreByGrade = new ArrayList<StudentScoresTO>(); 
		StudentScoresTO studentScores = null;
		
		try {
			if(driver == null) driver = LoadConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			
			String query = "Select Sbd.Student_Bio_Id, Ssf.Contentid, grd.grade_seq, grd.grade_name,  " +
				      "          Ssf.Sa_Ss, Gsf.Step_0, Gsf.Step_1, Gsf.Step_2, Gsf.Step_3, " +
				      "          DECODE(Ssf.SA_ACHV_LEVEL, 4, 'A', 3, 'P', 2, 'PP', 1, 'N', '') ACHV_LVL, " +
				      "          Ssf.Sa_Achv_Pct, Ssf.Date_Atnd, Ssf.School_Name_Atnd,cd.content_name,  " +
				      "          Sbd.FIRST_NAME || ' ' || Sbd.LAST_NAME || ' - ' || Sbd.STUDENT_ID STUD_NAME, " +
				      "          (SELECT GD.GRADE_NAME FROM GRADE_DIM GD WHERE GD.GRADEID = Sbd.CUR_GRADEID) CURR_GRD, " +
				      "          Ond.Cur_School_Name,Ond.Cur_District_Name,Gsf.Text1_Graph, Gsf.Text2_Table, Ssf.Assessment_Flag " +
				      "   From Org_Node_Dim Ond, Student_Bio_Dim Sbd, Student_Score_Fact Ssf, " +
				      "          Admin_Dim Ad, Growth_Score_Fact Gsf, content_dim cd, grade_dim grd " +
				      "    Where Ssf.Org_Nodeid = Sbd.Org_Nodeid " +
				      "      And Ssf.Student_Bio_Id = Sbd.Student_Bio_Id " +
				      "      And Ssf.Org_Nodeid = Ond.Org_Nodeid " +
				      "      And Ssf.Adminid = Ad.Adminid " +
				      "      And Ssf.Student_Bio_Id = Gsf.Student_Bio_Id " +
				      "      And Ssf.Contentid = Gsf.Contentid " +
				      "      And Ssf.Contentid= cd.Contentid " +
				      "      AND ssf.gradeid = grd.gradeid " +
				      "      AND Gsf.projection_rpt_flag = 1 " +
				      //"      And Sbd.Student_Bio_Id In (" + studentBioIdList + ") " +
				      "		 AND " + divideIdIntoChunk(studentBioIdList) + 
					  "		Order By  Sbd.student_bio_id, cd.Content_seq, grd.Grade_seq ";
			
			/*String query = "Select b.student_bio_id, a.Contentid, a.grade_seq, a.grade_name, b.sa_ss, b.Step_0, b.Step_1, b.Step_2, b.Step_3 " +
						"		,decode(b.sa_achv_level, 4, 'A', 3, 'P', 2, 'PP', 1, 'N', '') achv_lvl " +
						"		,b.sa_achv_pct achv_pct ,b.date_atnd date_atnd ,b.school_name_atnd sch_atnd " + 
						"		,b.content_name, b.FIRST_NAME || ' ' || b.LAST_NAME || ' - ' || b.STUDENT_ID stud_name " + 
					    "  		,(SELECT Gd.Grade_Name FROM grade_dim Gd WHERE Gd.Gradeid = b.Cur_Gradeid) curr_grd " + 
					    "  		,b.Cur_School_Name, b.Cur_District_Name, b.Text1_Graph, b.Text2_Table " + 
						"	  From (Select Gd.Gradeid, Gd.Grade_Name, Gd.Grade_Seq, Csf.Contentid, " + 
						"	                Csf.Pp_Cut_Score, Csf.p_Cut_Score, Csf.a_Cut_Score " + 
						"	           From Grade_Dim Gd, Cut_Score_Fact Csf " + 
						"	          Where Csf.Gradeid = Gd.Gradeid) a, " + 
						"	       (Select Ond.Cur_District_Number, Ond.Cur_District_Name, " + 
						"	                Ond.Cur_School_Number, Ond.Cur_School_Name, Sbd.Student_Bio_Id, " + 
						"	                Sbd.First_Name, Sbd.Last_Name, Sbd.STUDENT_ID, Sbd.Cur_Gradeid, Ssf.Adminid, " + 
						"	                Ssf.Gradeid, Ssf.Contentid,cd.content_name, cd.content_seq, Ssf.Sa_Ss, Ssf.Sa_Achv_Level, " + 
						"	                Ssf.Sa_Achv_Pct, Ssf. School_Name_Atnd, Ssf.Date_Atnd, " + 
						"	                Gsf.Step_0, Gsf.Step_1, Gsf.Step_2, Gsf.Step_3, " + 
						"	                Gsf.Text1_Graph, Gsf.Text2_Table, Gsf.Roster_Rpt_Flag, " + 
						"	                Ad.Cur_Admin, ad.admin_seq " + 
						"	           From Org_Node_Dim Ond, Student_Bio_Dim Sbd, Student_Score_Fact Ssf, " + 
						"	                Admin_Dim Ad, Growth_Score_Fact Gsf, content_dim cd " + 
						"	          Where Ssf.Org_Nodeid = Sbd.Org_Nodeid " + 
						"	            And Ssf.Student_Bio_Id = Sbd.Student_Bio_Id " + 
						"	            And Ssf.Org_Nodeid = Ond.Org_Nodeid " + 
						"	            And Ssf.Adminid = Ad.Adminid " + 
						"	            And Ssf.Student_Bio_Id = Gsf.Student_Bio_Id " + 
						"	            And Ssf.Contentid = Gsf.Contentid " + 
						"	            And Ssf.Contentid= cd.Contentid " + 
						"	            AND Gsf.projection_rpt_flag = 1 "+
						"	            And Sbd.Student_Bio_Id In (" + studentBioIdList + ")) b " +  
						"	Where a.Gradeid = b.Gradeid(+) " + 
						"	   And a.Contentid = b.Contentid(+) " + 
						"	   AND b.sa_ss IS NOT NULL " + 
						"	Order By  b.student_bio_id, b.Content_seq,a.Gradeid";*/
			
			
			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				studentScores = new StudentScoresTO();
				studentScores.setStudentBioId(rs.getLong(1));
				studentScores.setContentId(rs.getLong(2));
				studentScores.setGradeSeq(rs.getLong(3));
				studentScores.setGradeName(rs.getString(4));
				studentScores.setStudentScore(rs.getString(5));
				studentScores.setStep0(rs.getString(6));
				studentScores.setStep1(rs.getString(7));
				studentScores.setStep2(rs.getString(8));
				studentScores.setStep3(rs.getString(9));
				studentScores.setAchvLevel(rs.getString(10));
				studentScores.setAchvPercentile(rs.getString(11));
				studentScores.setDateAttended(rs.getString(12));
				studentScores.setSchoolAttended(rs.getString(13));
				studentScores.setContentName(rs.getString(14));
				studentScores.setStudentName(rs.getString(15));
				studentScores.setCurrentGrade(rs.getString(16));
				studentScores.setCurrentSchool(rs.getString(17));
				studentScores.setCurrentDistrict(rs.getString(18));
				studentScores.setGraphText(rs.getString(19));
				studentScores.setTableText(rs.getString(20));
				studentScores.setAssessmentFlag(rs.getString(21));
				
				scoreByGrade.add(studentScores);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		return scoreByGrade;
	}
	
	@Deprecated
	public ScoreTO getStudentScore(long studentBioId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long oldContentId = 0;
		long newContentId = 0;
		List<StudentScoresTO> scoreByGrade = null; 
		StudentScoresTO studentScores = null;
		ContentTO content = null;
		List<ContentTO> allContent = new ArrayList<ContentTO>();
		ScoreTO scoreTO = new ScoreTO();
		try {
			if(driver == null) driver = LoadConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			
			scoreTO.setStudentBioId(studentBioId);
			
			Properties prop = PropertyFile.loadProperties("query.properties");
			String query = prop.getProperty("queryStudentScore");
			
			
			pstmt = conn.prepareCall(query);
			pstmt.setLong(1, studentBioId);
			pstmt.setLong(2, studentBioId);
			pstmt.setLong(3, studentBioId);
			pstmt.setLong(4, studentBioId);
			pstmt.setLong(5, studentBioId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				newContentId = rs.getLong(1);
				if(oldContentId == newContentId) {
					
				} else {
					if( oldContentId > 0) {
						content.setStudentScores(scoreByGrade);
						allContent.add(content);
					}
					
					oldContentId = newContentId;
					content = new ContentTO();
					content.setContentId(newContentId);
					scoreByGrade = new ArrayList<StudentScoresTO>();
				}
				studentScores = new StudentScoresTO();
				studentScores.setContentId(oldContentId);
				studentScores.setGradeSeq(rs.getLong(2));
				studentScores.setGradeName(rs.getString(3));
				studentScores.setPpCutScore(rs.getLong(4));
				studentScores.setpCutScore(rs.getLong(5));
				studentScores.setaCutScore(rs.getLong(6));
				studentScores.setStudentScore(rs.getString(7));
				studentScores.setGrowthScore(rs.getString(8));
				studentScores.setAchvLevel(rs.getString(9));
				studentScores.setAchvPercentile(rs.getString(10));
				studentScores.setDateAttended(rs.getString(11));
				studentScores.setSchoolAttended(rs.getString(12));
				studentScores.setStudentBioId(rs.getLong(13));
				
				scoreByGrade.add(studentScores);
			}
			
			content.setStudentScores(scoreByGrade);
			allContent.add(content);
			
			scoreTO.setContent(allContent);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		return scoreTO;
	}
	
	/**
	 * This method is to get content name and graph/table text
	 * @param contentTO
	 * @param studentBioId
	 * @return
	 * @throws Exception
	 */
	public ContentTO getContentDetails(ContentTO contentTO, long studentBioId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if(driver == null) driver = LoadConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);

			String query = "SELECT CNT.CONTENT_NAME, GSF.TEXT1_GRAPH, GSF.TEXT2_TABLE " + 
							  "FROM GROWTH_SCORE_FACT GSF, CONTENT_DIM CNT " + 
							 "WHERE GSF.STUDENT_BIO_ID = ? " + 
							   "AND CNT.CONTENTID = ? " + 
							   "AND CNT.CONTENTID = GSF.CONTENTID "; 
			
			pstmt = conn.prepareCall(query);
			pstmt.setLong(1, studentBioId);
			pstmt.setLong(2, contentTO.getContentId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				contentTO.setContentName(rs.getString(1));
				contentTO.setGraphText(rs.getString(2));
				contentTO.setTableText(rs.getString(3));
			} else {
				// no text present for this student -- need to get content name atleast
				query = "SELECT CNT.CONTENT_NAME " + 
						  "FROM CONTENT_DIM CNT " + 
						 "WHERE CNT.CONTENTID = ? "; 
				pstmt = conn.prepareCall(query);
				pstmt.setLong(1, contentTO.getContentId());
				rs = pstmt.executeQuery();
				if(rs.next()) {
					contentTO.setContentName(rs.getString(1));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		return contentTO;
	}
	
	private static void logElapsedTime(String message) {
		long difference = lEndTime - lStartTime; //check different
		StringBuffer timeDiff = new StringBuffer();
		 
        if(difference > 1000) {
        	timeDiff.append("### ").append(message).append(" Elapsed : ");
        	timeDiff.append(difference/1000).append(" second(s) ");
        	timeDiff.append(difference%1000).append(" milliseconds");
        } else {
        	timeDiff.append("### ").append(message).append(" Elapsed : ");
        	timeDiff.append(difference).append(" milliseconds");
        }
        System.out.println(timeDiff.toString());
	}
	
	public List<StudentTO> getStudentDetails(String orgNodeId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StudentTO student = null;
		List<StudentTO> allStudents = null;
		
		long oldStudentBioId = 0;
		long newStudentBioId = 0;
		
		long oldContentId = 0;
		long newContentId = 0;
		
		ContentTO content = new ContentTO();
		List<ContentTO> allContent = new ArrayList<ContentTO>();
		try {
			if(driver == null) driver = LoadConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			
			String query = "SELECT DISTINCT STD.STUDENT_BIO_ID, " +
						       "STD.FIRST_NAME || ' ' || STD.LAST_NAME || ' - ' || STD.STUDENT_ID stud_name, " +
						       "STD.BIRTHDATE, " +
						       "STD.GENDER, " +
						       "grd.grade_name, " +
						       "org.CUR_SCHOOL_FULL_NAME, " +
						       "org.CUR_DISTRICT_FULL_NAME, " +
						       "std.org_nodeid, " +
						       "scr.contentid, " +
						       "cnt.content_name, GSF.TEXT1_GRAPH, GSF.TEXT2_TABLE, " +
						       "cnt.content_seq, STD.LAST_NAME, STD.FIRST_NAME,std.CUR_GRADEID " +
						  "FROM STUDENT_BIO_DIM STD, " +
						       "org_node_dim org, " +
						       "grade_dim grd, " +
						       "student_score_fact scr, " +
						       "content_dim cnt, " +
						       "GROWTH_SCORE_FACT GSF " +
						  "WHERE STD.org_nodeid = org.org_nodeid " +
						        "AND STD.cur_gradeid = grd.gradeid " +
						        "AND cnt.contentid = scr.contentid " +
						        "AND GSF.CONTENTID = cnt.contentid " +
						        "AND Gsf.projection_rpt_flag = 1 " +
						        //"AND STD.STUDENT_BIO_ID in (52875,52876,52880) " +
						        "AND GSF.Student_Bio_Id = STD.STUDENT_BIO_ID ";
						        if(orgNodeId != null) query = query + "AND STD.org_nodeid = ? ";
						        query = query + "AND scr.student_bio_id = STD.STUDENT_BIO_ID " +
						        		//"AND ROWNUM < 10 " +
						        		"ORDER BY cnt.content_seq, STD.CUR_GRADEID, STD.LAST_NAME, STD.FIRST_NAME";
			
			pstmt = conn.prepareCall(query);
			if(orgNodeId != null) pstmt.setString(1, orgNodeId);
			rs = pstmt.executeQuery();
			allStudents = new ArrayList<StudentTO>();
			int count = 0;
			boolean oldStud = false;
			boolean newContent = false;
			while(rs.next()) {
				count++;
				newStudentBioId = rs.getLong(1);
				newContentId = rs.getLong(9);
				
				if(count == 1) oldContentId = newContentId;
				if(newContentId == oldContentId) {
					newContent = false;
				} else {
					newContent = true;
				}
				
				if(newContent) {
					for(StudentTO stud : allStudents) {
						if(stud.getStudentBioId() == newStudentBioId ) {
							oldStud = true;
							content = new ContentTO();
							content.setContentId(newContentId);
							content.setContentName(rs.getString(10));
							content.setGraphText(rs.getString(11));
							content.setTableText(rs.getString(12));
							if(stud.getContent() != null) {
								stud.getContent().add(content);
							} else {
								allContent = new ArrayList<ContentTO>();
								allContent.add(content);
								stud.setContent(allContent);
							}
							break;
						}
						oldStud = false;
					}
					
				} 
				if(oldStud) {
					// only need to update content list
					
					
				} else {
					// new student row 
					
					if(count > 1) allStudents.add(student); 
					
					allContent = new ArrayList<ContentTO>();
					
					oldStudentBioId = newStudentBioId;
					
					student = new StudentTO();
					student.setStudentBioId(rs.getLong(1));
					student.setStudentName(rs.getString(2));
					student.setBirthDate(rs.getString(3));
					student.setGender(rs.getString(4));
					student.setGrade(rs.getString(5));
					student.setCurrentSchool(rs.getString(6));
					student.setCurrentDistrict(rs.getString(7));
					student.setOrgNodeId(rs.getLong(8));
					
					content = new ContentTO();
					content.setContentId(newContentId);
					content.setContentName(rs.getString(10));
					content.setGraphText(rs.getString(11));
					content.setTableText(rs.getString(12));
					allContent.add(content);
					
					student.setContent(allContent);
					
				}
			}
			//adding last student/content
			boolean addLast = true;
			boolean addLastContent = true;
			for(StudentTO stud : allStudents) {
				if(stud.getStudentBioId() == student.getStudentBioId() ) {
					allContent = stud.getContent();
					if(allContent == null) allContent = new ArrayList<ContentTO>();
					for(ContentTO cont : stud.getContent()) {
						if(cont.getContentId() == content.getContentId()) {
							addLastContent = false;
							break;
						}
					}
					if(addLastContent) {
						allContent.add(content);
						stud.setContent(allContent);
					}
					addLast = false;
					break;
				} 
			}
			if(addLast) allStudents.add(student); 
			totalContentCount = count;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		return allStudents;
	}
	
	/*public List<StudentTO> getStudentDetails(String orgNodeId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StudentTO student = null;
		List<StudentTO> allStudents = null;
		
		long oldStudentBioId = 0;
		long newStudentBioId = 0;
		ContentTO content = new ContentTO();
		List<ContentTO> allContent = new ArrayList<ContentTO>();
		
		try {
			if(driver == null) driver = LoadConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			
			String query = "SELECT DISTINCT STD.STUDENT_BIO_ID, " +
						       "STD.FIRST_NAME || ' ' || STD.LAST_NAME || ' - ' || STD.STUDENT_ID stud_name, " +
						       "STD.BIRTHDATE, " +
						       "STD.GENDER, " +
						       "grd.grade_name, " +
						       "org.cur_school_name, " +
						       "org.cur_district_name, " +
						       "std.org_nodeid, " +
						       "scr.contentid, " +
						       "cnt.content_name, GSF.TEXT1_GRAPH, GSF.TEXT2_TABLE " +
						  "FROM STUDENT_BIO_DIM STD, " +
						       "org_node_dim org, " +
						       "grade_dim grd, " +
						       "student_score_fact scr, " +
						       "content_dim cnt, " +
						       "GROWTH_SCORE_FACT GSF " +
						  "WHERE STD.org_nodeid = org.org_nodeid " +
						        "AND STD.cur_gradeid = grd.gradeid " +
						        "AND cnt.contentid = scr.contentid " +
						        "AND GSF.CONTENTID = cnt.contentid " +
						        //"AND STD.STUDENT_BIO_ID in (52875,52876,52880) " +
						        "AND GSF.Student_Bio_Id = STD.STUDENT_BIO_ID ";
						        if(orgNodeId != null) query = query + "AND STD.org_nodeid = ? ";
						        query = query + "AND scr.student_bio_id = STD.STUDENT_BIO_ID ORDER BY 1";
			
			pstmt = conn.prepareCall(query);
			if(orgNodeId != null) pstmt.setString(1, orgNodeId);
			rs = pstmt.executeQuery();
			allStudents = new ArrayList<StudentTO>();
			int count = 0;
			while(rs.next()) {
				count++;
				newStudentBioId = rs.getLong(1);
				if(oldStudentBioId == newStudentBioId) {
					// only need to update content list
					
					content = new ContentTO();
					content.setContentId(rs.getLong(9));
					content.setContentName(rs.getString(10));
					content.setGraphText(rs.getString(11));
					content.setTableText(rs.getString(12));

					allContent.add(content);
					
					student.setContent(allContent);
					
				} else {
					// new student row / or first row
					
					if(count > 1) allStudents.add(student); 
					
					allContent = new ArrayList<ContentTO>();
					
					oldStudentBioId = newStudentBioId;
					
					student = new StudentTO();
					student.setStudentBioId(rs.getLong(1));
					student.setStudentName(rs.getString(2));
					student.setBirthDate(rs.getString(3));
					student.setGender(rs.getString(4));
					student.setGrade(rs.getString(5));
					student.setCurrentSchool(rs.getString(6));
					student.setCurrentDistrict(rs.getString(7));
					student.setOrgNodeId(rs.getLong(8));
					
					content = new ContentTO();
					content.setContentId(rs.getLong(9));
					content.setContentName(rs.getString(10));
					content.setGraphText(rs.getString(11));
					content.setTableText(rs.getString(12));
					allContent.add(content);
					
					student.setContent(allContent);
					
				}
			}
			//adding last student
			allStudents.add(student); 
			totalContentCount = count;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		return allStudents;
	}*/
	
	/**
	 * This method is to update pdfFileName of a school
	 * @param email
	 * @param structureElement
	 * @return
	 * @throws Exception
	 */
	public long updatePdfLocation(String fileName, String nodeId, boolean roster) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		long updateCount = 0;
		try {
			conn = driver.connect(DATA_SOURCE, null);
			String query = "";
			if(roster) {
				query = "update org_node_dim set roster_file_name = ? where org_nodeid = ? "; 
			} else {
				query = "update org_node_dim set PROJECTION_FILE_NAME = ? where org_nodeid = ? "; 
			}
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, fileName);
			pstmt.setString(2, nodeId);
			updateCount = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		return updateCount;
	}
	
	
	/**
	 * This method is used to get all contents
	 * @return
	 * @throws Exception
	 */
	public List<ContentTO> getAllContents() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ContentTO content = null;
		List<ContentTO> allContent = null;
		try {
			if(driver == null) driver = LoadConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			
			String query = "SELECT cnt.contentid, cnt.content_name FROM content_dim cnt where cnt.contentid != 10000 ORDER BY content_seq ";
			
			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			allContent = new ArrayList<ContentTO>();
			while(rs.next()) {
				content = new ContentTO();
				content.setContentId(rs.getLong(1));
				content.setContentName(rs.getString(2));
				allContent.add(content);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		return allContent;
	}
	
	/**
	 * This method populates cut-score by content
	 * @return
	 * @throws Exception
	 */
	public List<StudentScoresTO> populateCutScore() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StudentScoresTO studentScore = null;
		List<StudentScoresTO> allStudent = null;
		try {
			if(driver == null) driver = LoadConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			
			String query = "SELECT CSF.CONTENTID, " +
						       "GRD.GRADE_SEQ, " +
						       "GRD.GRADE_NAME, " +
						       "CSF.PP_CUT_SCORE, " +
						       "CSF.P_CUT_SCORE, " +
						       "CSF.A_CUT_SCORE " +
						       ",CNT.CONTENT_SEQ " +
						  "FROM CUT_SCORE_FACT CSF LEFT " +
						 "OUTER JOIN CONTENT_DIM CNT ON CNT.CONTENTID = CSF.CONTENTID LEFT " +
						 "OUTER JOIN GRADE_DIM GRD ON GRD.GRADEID = CSF.GRADEID " +
						 "ORDER BY 7, 2";
			
			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			allStudent = new ArrayList<StudentScoresTO>();
			while(rs.next()) {
				studentScore = new StudentScoresTO();
				studentScore.setContentId(rs.getLong(1));
				studentScore.setGradeSeq(rs.getLong(2));
				studentScore.setGradeName(rs.getString(3));
				studentScore.setPpCutScore(rs.getLong(4));
				studentScore.setpCutScore(rs.getLong(5));
				studentScore.setaCutScore(rs.getLong(6));
				allStudent.add(studentScore);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		return allStudent;
	}
	
	/**
	 * This method is used to get Current administration
	 * @return
	 * @throws Exception
	 */
	public AdminTO getCurrentAdmin() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AdminTO admin = new AdminTO();
		try {
			if(driver == null) driver = LoadConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			
			String query = "Select d.adminid,d.admin_name,d.admin_season_year,d.admin_seq From Admin_Dim d Where d.cur_admin = 'Y'";
			
			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				admin.setAdminId(rs.getLong(1));
				admin.setAdminName(rs.getString(2));
				admin.setAdminSeasonYear(rs.getString(3));
				admin.setAdminSeq(rs.getLong(4));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		return admin;
	}
	
}
