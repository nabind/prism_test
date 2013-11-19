package com.ctb.prism.report.transferobject;

import java.util.List;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * Transfer object holds the input controls all level users
 */
public class ReportFilterTOTemp extends BaseTO {

	private static final long serialVersionUID = 1L;

	private String LoggedInUserJasperOrgId;
	private String LoggedInUserName;
	
	// ISTEP inputcontrol
	private List<ObjectValueTO> ip_product_id_eod;
	private List<ObjectValueTO> ip_customer_name_eod;
	private List<ObjectValueTO> ip_product_name;
	private List<ObjectValueTO> ip_customer_name;
	private List<ObjectValueTO> ip_adminid;
	private List<ObjectValueTO> Days;
	
	public List<ObjectValueTO> getIp_product_name() {
		return ip_product_name;
	}
	public void setIp_product_name(List<ObjectValueTO> ip_product_name) {
		this.ip_product_name = ip_product_name;
	}
	public List<ObjectValueTO> getIp_customer_name() {
		return ip_customer_name;
	}
	public void setIp_customer_name(List<ObjectValueTO> ip_customer_name) {
		this.ip_customer_name = ip_customer_name;
	}
	public List<ObjectValueTO> getDays() {
		return Days;
	}
	public void setDays(List<ObjectValueTO> days) {
		Days = days;
	}
	public List<ObjectValueTO> getIp_product_id_eod() {
		return ip_product_id_eod;
	}
	public void setIp_product_id_eod(List<ObjectValueTO> ip_product_id_eod) {
		this.ip_product_id_eod = ip_product_id_eod;
	}
	public List<ObjectValueTO> getIp_customer_name_eod() {
		return ip_customer_name_eod;
	}
	public void setIp_customer_name_eod(List<ObjectValueTO> ip_customer_name_eod) {
		this.ip_customer_name_eod = ip_customer_name_eod;
	}
	public List<ObjectValueTO> getIp_adminid() {
		return ip_adminid;
	}
	public void setIp_adminid(List<ObjectValueTO> ip_adminid) {
		this.ip_adminid = ip_adminid;
	}
	// common fields
	private List<ObjectValueTO> p_Level_Form;
	private List<ObjectValueTO> p_AdminYear;
	private List<ObjectValueTO> p_Ethnicity;
	private List<ObjectValueTO> p_Gender;
	private List<ObjectValueTO> p_Giftedtalented;
	private List<ObjectValueTO> p_ScoreType;
	private List<ObjectValueTO> p_SpecialProgram;
	private List<ObjectValueTO> p_Subtest_Type;
	private List<ObjectValueTO> p_YearsinChristianschools;
	private List<ObjectValueTO> p_AdminYearMultiselect;
	private List<ObjectValueTO> p_ExtendedAdminYear;
	
	
	// CTB level fields
	private List<ObjectValueTO> p_Acsi_Form;
	private List<ObjectValueTO> p_Acsi_Grade;
	private List<ObjectValueTO> p_Acsi_Grade_Bible;
	private List<ObjectValueTO> p_Acsi_Level;
	private List<ObjectValueTO> p_Acsi_Level_Bible;
	private List<ObjectValueTO> p_All_Subtest;
	private List<ObjectValueTO> p_Bible_Subtest;
	private List<ObjectValueTO> p_Inview_Form_Level_Acsi;
	private List<ObjectValueTO> p_Inview_Grade_Acsi;
	private List<ObjectValueTO> p_Inview_Subtest;
	private List<ObjectValueTO> p_Inview_Subtest_MultiSelect;
	private List<ObjectValueTO> p_PTCS_Form_Level_Acsi;
	private List<ObjectValueTO> p_PTCS_Grade_Acsi;
	private List<ObjectValueTO> p_PTCS_Subtest;
	private List<ObjectValueTO> p_PTCS_Subtest_MultiSelect;
	private List<ObjectValueTO> p_ScoreType_ranking;
	private List<ObjectValueTO> p_Subtest_SS;
	private List<ObjectValueTO> p_Textbook_Subtest;
	private List<ObjectValueTO> p_Hierarchy_Table;
	private List<ObjectValueTO> p_Acsi_Grade_Cohort;
	private List<ObjectValueTO> p_ScoreType_Acsi_Cohort;
	
	
	// SCHOOL level fields
	//private List<ObjectValueTO> p_All_Subtest;
	private List<ObjectValueTO> p_Bible_Roster_Rank_Order;
	private List<ObjectValueTO> p_Bible_Roster_Score_Type;
	private List<ObjectValueTO> p_Combination_ScoreType;
	private List<ObjectValueTO> p_Grade_Bible;
	private List<ObjectValueTO> p_Grade_School;
	//private List<ObjectValueTO> p_Inview_Form_Level_Acsi;
	//private List<ObjectValueTO> p_Inview_Grade_Acsi;
	private List<ObjectValueTO> p_Inview_Roster_Rank_Order;
	private List<ObjectValueTO> p_Inview_Roster_Score_School;
	//private List<ObjectValueTO> p_Inview_Subtest_MultiSelect;
	private List<ObjectValueTO> p_level_Bible;
	//private List<ObjectValueTO> p_PTCS_Form_Level_Acsi;
	//private List<ObjectValueTO> p_PTCS_Grade_Acsi;
	private List<ObjectValueTO> p_PTCS_Roster_Rank_Order;
	private List<ObjectValueTO> p_PTCS_Roster_Score_School;
	//private List<ObjectValueTO> p_PTCS_Subtest_MultiSelect;
	private List<ObjectValueTO> p_Roster_Grade;
	private List<ObjectValueTO> p_Roster_Level_Form;
	private List<ObjectValueTO> p_Roster_Rank_Order;
	private List<ObjectValueTO> p_Roster_Score_List;
	private List<ObjectValueTO> p_Roster_Subtest;
	private List<ObjectValueTO> p_Roster_Subtest_MultiSelect;
	private List<ObjectValueTO> p_Score_Type_List;
	private List<ObjectValueTO> p_Selected_Score_Type;
	private List<ObjectValueTO> p_Subtest_MultiSelect;
	private List<ObjectValueTO> p_MinScore;
	private List<ObjectValueTO> p_MaxScore;
	private List<ObjectValueTO> p_ach_assessmentId;
	private List<ObjectValueTO> p_ScoreTypeSelect;
	private List<ObjectValueTO> p_ScoreType_InView_And_PTCS;
	private List<ObjectValueTO> p_Form_School;
	private List<ObjectValueTO> p_Level_School;
	private List<ObjectValueTO> p_Longitudinal_Roster_Score_List;
	private List<ObjectValueTO> p_Longitudinal_Roster_Rank_Order;
	private List<ObjectValueTO> p_Longitudinal_Roster_Subtest_MultiSelect;
	private List<ObjectValueTO> p_Grade_School_Longitudinal_Summary;
	private List<ObjectValueTO> p_Subtest_MultiSelect_School_Longitudinal_Summary;
	private List<ObjectValueTO> p_Grade_School_Cohort;
	private List<ObjectValueTO> p_Subtest_Multiselect_School;
	private List<ObjectValueTO> p_Roster_Subtest1;
	private List<ObjectValueTO> p_Roster_Subtest2;
	private List<ObjectValueTO> p_Roster_Subtest3;
	private List<ObjectValueTO> p_Subtest1_MinScore;
	private List<ObjectValueTO> p_Subtest2_MinScore;
	private List<ObjectValueTO> p_Subtest3_MinScore;
	private List<ObjectValueTO> p_Subtest1_MaxScore;
	private List<ObjectValueTO> p_Subtest2_MaxScore;
	private List<ObjectValueTO> p_Subtest3_MaxScore;
	private List<ObjectValueTO> p_Score_Type_Select;
	//private List<ObjectValueTO> p_Subtest_SS;
		
	// CLASS level fields
	//public List<ObjectValueTO> p_Bible_Roster_Rank_Order;
	//public List<ObjectValueTO> p_Bible_Roster_Score_Type;
	private List<ObjectValueTO> p_Inview_Comb_Rank_Order;
	private List<ObjectValueTO> p_Inview_Comb_Score;
	private List<ObjectValueTO> p_Inview_Comb_Subtest_Multiselect;
	private List<ObjectValueTO> p_Inview_Level;
	private List<ObjectValueTO> p_Inview_Level_Form;
	//public List<ObjectValueTO> p_Inview_Subtest;
	private List<ObjectValueTO> p_Level_Bible_Class;
	private List<ObjectValueTO> p_PTCS_Comb_Rank_Order;
	private List<ObjectValueTO> p_PTCS_Comb_Score;
	private List<ObjectValueTO> p_PTCS_Comb_Subtest_MultiSelect;
	private List<ObjectValueTO> p_PTCS_Form_Level;
	private List<ObjectValueTO> p_PTCS_Level;
	private List<ObjectValueTO> p_PTCS_Roster;
	private List<ObjectValueTO> p_Ranking_Order_List;
	private List<ObjectValueTO> p_Roster_Combination_Score_List;
	private List<ObjectValueTO> p_Subtest_Class_MultiSelect;
	private List<ObjectValueTO> p_Longitudinal_Roster_Class_Rank_Order;

	
	//public List<ObjectValueTO> p_Score_Type_List;
	//Parent Level fields
	private List<ObjectValueTO> p_Student_Bio_Id;
	private List<ObjectValueTO> p_L3_Jasper_Org_Id;
	
	private List<ObjectValueTO> Subreport_1;
	private List<ObjectValueTO> Subreport_2;
	private List<ObjectValueTO> Subreport_3;
	
	private List<ObjectValueTO> Image_1;
	private List<ObjectValueTO> Image_2;
	private List<ObjectValueTO> Image_3;
	
	private List<ObjectValueTO> p_Is3D;
	
	
	public List<ObjectValueTO> getP_Level_School() {
		return p_Level_School;
	}
	public void setP_Level_School(List<ObjectValueTO> p_Level_School) {
		this.p_Level_School = p_Level_School;
	}
	public List<ObjectValueTO> getP_Form_School() {
		return p_Form_School;
	}
	public void setP_Form_School(List<ObjectValueTO> p_Form_School) {
		this.p_Form_School = p_Form_School;
	}
	public List<ObjectValueTO> getP_Is3D() {
		return p_Is3D;
	}
	public void setP_Is3D(List<ObjectValueTO> p_Is3D) {
		this.p_Is3D = p_Is3D;
	}
	
	public String getLoggedInUserName() {
		return LoggedInUserName;
	}
	public void setLoggedInUserName(String loggedInUserName) {
		LoggedInUserName = loggedInUserName;
	}
	public List<ObjectValueTO> getImage_1() {
		return Image_1;
	}
	public void setImage_1(List<ObjectValueTO> image_1) {
		Image_1 = image_1;
	}
	public List<ObjectValueTO> getImage_2() {
		return Image_2;
	}
	public void setImage_2(List<ObjectValueTO> image_2) {
		Image_2 = image_2;
	}
	public List<ObjectValueTO> getImage_3() {
		return Image_3;
	}
	public void setImage_3(List<ObjectValueTO> image_3) {
		Image_3 = image_3;
	}
	public List<ObjectValueTO> getSubreport_1() {
		return Subreport_1;
	}
	public void setSubreport_1(List<ObjectValueTO> subreport_1) {
		Subreport_1 = subreport_1;
	}
	public List<ObjectValueTO> getSubreport_2() {
		return Subreport_2;
	}
	public void setSubreport_2(List<ObjectValueTO> subreport_2) {
		Subreport_2 = subreport_2;
	}
	public List<ObjectValueTO> getSubreport_3() {
		return Subreport_3;
	}
	public void setSubreport_3(List<ObjectValueTO> subreport_3) {
		Subreport_3 = subreport_3;
	}
	public List<ObjectValueTO> getP_L3_Jasper_Org_Id() {
		return p_L3_Jasper_Org_Id;
	}
	public void setP_L3_Jasper_Org_Id(List<ObjectValueTO> p_L3_Jasper_Org_Id) {
		this.p_L3_Jasper_Org_Id = p_L3_Jasper_Org_Id;
	}
	public String getLoggedInUserJasperOrgId() {
		return LoggedInUserJasperOrgId;
	}
	public void setLoggedInUserJasperOrgId(String loggedInUserJasperOrgId) {
		LoggedInUserJasperOrgId = loggedInUserJasperOrgId;
	}
	public List<ObjectValueTO> getP_Level_Form() {
		return p_Level_Form;
	}
	public List<ObjectValueTO> getP_Acsi_Form() {
		return p_Acsi_Form;
	}
	public void setP_Acsi_Form(List<ObjectValueTO> p_Acsi_Form) {
		this.p_Acsi_Form = p_Acsi_Form;
	}
	public List<ObjectValueTO> getP_Acsi_Grade() {
		return p_Acsi_Grade;
	}
	public void setP_Acsi_Grade(List<ObjectValueTO> p_Acsi_Grade) {
		this.p_Acsi_Grade = p_Acsi_Grade;
	}
	public List<ObjectValueTO> getP_Acsi_Grade_Bible() {
		return p_Acsi_Grade_Bible;
	}
	public void setP_Acsi_Grade_Bible(List<ObjectValueTO> p_Acsi_Grade_Bible) {
		this.p_Acsi_Grade_Bible = p_Acsi_Grade_Bible;
	}
	public List<ObjectValueTO> getP_Acsi_Level() {
		return p_Acsi_Level;
	}
	public void setP_Acsi_Level(List<ObjectValueTO> p_Acsi_Level) {
		this.p_Acsi_Level = p_Acsi_Level;
	}
	public List<ObjectValueTO> getP_Acsi_Level_Bible() {
		return p_Acsi_Level_Bible;
	}
	public void setP_Acsi_Level_Bible(List<ObjectValueTO> p_Acsi_Level_Bible) {
		this.p_Acsi_Level_Bible = p_Acsi_Level_Bible;
	}
	public List<ObjectValueTO> getP_All_Subtest() {
		return p_All_Subtest;
	}
	public void setP_All_Subtest(List<ObjectValueTO> p_All_Subtest) {
		this.p_All_Subtest = p_All_Subtest;
	}
	public List<ObjectValueTO> getP_Bible_Subtest() {
		return p_Bible_Subtest;
	}
	public void setP_Bible_Subtest(List<ObjectValueTO> p_Bible_Subtest) {
		this.p_Bible_Subtest = p_Bible_Subtest;
	}
	public List<ObjectValueTO> getP_Inview_Form_Level_Acsi() {
		return p_Inview_Form_Level_Acsi;
	}
	public void setP_Inview_Form_Level_Acsi(List<ObjectValueTO> p_Inview_Form_Level_Acsi) {
		this.p_Inview_Form_Level_Acsi = p_Inview_Form_Level_Acsi;
	}
	public List<ObjectValueTO> getP_Inview_Grade_Acsi() {
		return p_Inview_Grade_Acsi;
	}
	public void setP_Inview_Grade_Acsi(List<ObjectValueTO> p_Inview_Grade_Acsi) {
		this.p_Inview_Grade_Acsi = p_Inview_Grade_Acsi;
	}
	public List<ObjectValueTO> getP_Inview_Subtest() {
		return p_Inview_Subtest;
	}
	public void setP_Inview_Subtest(List<ObjectValueTO> p_Inview_Subtest) {
		this.p_Inview_Subtest = p_Inview_Subtest;
	}
	public List<ObjectValueTO> getP_Inview_Subtest_MultiSelect() {
		return p_Inview_Subtest_MultiSelect;
	}
	public void setP_Inview_Subtest_MultiSelect(List<ObjectValueTO> p_Inview_Subtest_MultiSelect) {
		this.p_Inview_Subtest_MultiSelect = p_Inview_Subtest_MultiSelect;
	}
	public List<ObjectValueTO> getP_PTCS_Form_Level_Acsi() {
		return p_PTCS_Form_Level_Acsi;
	}
	public void setP_PTCS_Form_Level_Acsi(List<ObjectValueTO> p_PTCS_Form_Level_Acsi) {
		this.p_PTCS_Form_Level_Acsi = p_PTCS_Form_Level_Acsi;
	}
	public List<ObjectValueTO> getP_PTCS_Grade_Acsi() {
		return p_PTCS_Grade_Acsi;
	}
	public void setP_PTCS_Grade_Acsi(List<ObjectValueTO> p_PTCS_Grade_Acsi) {
		this.p_PTCS_Grade_Acsi = p_PTCS_Grade_Acsi;
	}
	public List<ObjectValueTO> getP_PTCS_Subtest() {
		return p_PTCS_Subtest;
	}
	public void setP_PTCS_Subtest(List<ObjectValueTO> p_PTCS_Subtest) {
		this.p_PTCS_Subtest = p_PTCS_Subtest;
	}
	public List<ObjectValueTO> getP_PTCS_Subtest_MultiSelect() {
		return p_PTCS_Subtest_MultiSelect;
	}
	public void setP_PTCS_Subtest_MultiSelect(List<ObjectValueTO> p_PTCS_Subtest_MultiSelect) {
		this.p_PTCS_Subtest_MultiSelect = p_PTCS_Subtest_MultiSelect;
	}
	public List<ObjectValueTO> getP_ScoreType_ranking() {
		return p_ScoreType_ranking;
	}
	public void setP_ScoreType_ranking(List<ObjectValueTO> p_ScoreType_ranking) {
		this.p_ScoreType_ranking = p_ScoreType_ranking;
	}
	public List<ObjectValueTO> getP_Subtest_SS() {
		return p_Subtest_SS;
	}
	public void setP_Subtest_SS(List<ObjectValueTO> p_Subtest_SS) {
		this.p_Subtest_SS = p_Subtest_SS;
	}
	public List<ObjectValueTO> getP_Textbook_Subtest() {
		return p_Textbook_Subtest;
	}
	public void setP_Textbook_Subtest(List<ObjectValueTO> p_Textbook_Subtest) {
		this.p_Textbook_Subtest = p_Textbook_Subtest;
	}
	public List<ObjectValueTO> getP_Bible_Roster_Rank_Order() {
		return p_Bible_Roster_Rank_Order;
	}
	public void setP_Bible_Roster_Rank_Order(List<ObjectValueTO> p_Bible_Roster_Rank_Order) {
		this.p_Bible_Roster_Rank_Order = p_Bible_Roster_Rank_Order;
	}
	public List<ObjectValueTO> getP_Bible_Roster_Score_Type() {
		return p_Bible_Roster_Score_Type;
	}
	public void setP_Bible_Roster_Score_Type(List<ObjectValueTO> p_Bible_Roster_Score_Type) {
		this.p_Bible_Roster_Score_Type = p_Bible_Roster_Score_Type;
	}
	public List<ObjectValueTO> getP_Combination_ScoreType() {
		return p_Combination_ScoreType;
	}
	public void setP_Combination_ScoreType(List<ObjectValueTO> p_Combination_ScoreType) {
		this.p_Combination_ScoreType = p_Combination_ScoreType;
	}
	public List<ObjectValueTO> getP_Grade_Bible() {
		return p_Grade_Bible;
	}
	public void setP_Grade_Bible(List<ObjectValueTO> p_Grade_Bible) {
		this.p_Grade_Bible = p_Grade_Bible;
	}
	public List<ObjectValueTO> getP_Grade_School() {
		return p_Grade_School;
	}
	public void setP_Grade_School(List<ObjectValueTO> p_Grade_School) {
		this.p_Grade_School = p_Grade_School;
	}
	public List<ObjectValueTO> getP_Inview_Roster_Rank_Order() {
		return p_Inview_Roster_Rank_Order;
	}
	public void setP_Inview_Roster_Rank_Order(List<ObjectValueTO> p_Inview_Roster_Rank_Order) {
		this.p_Inview_Roster_Rank_Order = p_Inview_Roster_Rank_Order;
	}
	public List<ObjectValueTO> getP_Inview_Roster_Score_School() {
		return p_Inview_Roster_Score_School;
	}
	public void setP_Inview_Roster_Score_School(List<ObjectValueTO> p_Inview_Roster_Score_School) {
		this.p_Inview_Roster_Score_School = p_Inview_Roster_Score_School;
	}
	public List<ObjectValueTO> getP_level_Bible() {
		return p_level_Bible;
	}
	public void setP_level_Bible(List<ObjectValueTO> p_level_Bible) {
		this.p_level_Bible = p_level_Bible;
	}
	public List<ObjectValueTO> getP_PTCS_Roster_Rank_Order() {
		return p_PTCS_Roster_Rank_Order;
	}
	public void setP_PTCS_Roster_Rank_Order(List<ObjectValueTO> p_PTCS_Roster_Rank_Order) {
		this.p_PTCS_Roster_Rank_Order = p_PTCS_Roster_Rank_Order;
	}
	public List<ObjectValueTO> getP_PTCS_Roster_Score_School() {
		return p_PTCS_Roster_Score_School;
	}
	public void setP_PTCS_Roster_Score_School(List<ObjectValueTO> p_PTCS_Roster_Score_School) {
		this.p_PTCS_Roster_Score_School = p_PTCS_Roster_Score_School;
	}
	public List<ObjectValueTO> getP_Roster_Grade() {
		return p_Roster_Grade;
	}
	public void setP_Roster_Grade(List<ObjectValueTO> p_Roster_Grade) {
		this.p_Roster_Grade = p_Roster_Grade;
	}
	public List<ObjectValueTO> getP_Roster_Level_Form() {
		return p_Roster_Level_Form;
	}
	public void setP_Roster_Level_Form(List<ObjectValueTO> p_Roster_Level_Form) {
		this.p_Roster_Level_Form = p_Roster_Level_Form;
	}
	public List<ObjectValueTO> getP_Roster_Rank_Order() {
		return p_Roster_Rank_Order;
	}
	public void setP_Roster_Rank_Order(List<ObjectValueTO> p_Roster_Rank_Order) {
		this.p_Roster_Rank_Order = p_Roster_Rank_Order;
	}
	public List<ObjectValueTO> getP_Roster_Score_List() {
		return p_Roster_Score_List;
	}
	public void setP_Roster_Score_List(List<ObjectValueTO> p_Roster_Score_List) {
		this.p_Roster_Score_List = p_Roster_Score_List;
	}
	public List<ObjectValueTO> getP_Roster_Subtest() {
		return p_Roster_Subtest;
	}
	public void setP_Roster_Subtest(List<ObjectValueTO> p_Roster_Subtest) {
		this.p_Roster_Subtest = p_Roster_Subtest;
	}
	public List<ObjectValueTO> getP_Roster_Subtest_MultiSelect() {
		return p_Roster_Subtest_MultiSelect;
	}
	public void setP_Roster_Subtest_MultiSelect(List<ObjectValueTO> p_Roster_Subtest_MultiSelect) {
		this.p_Roster_Subtest_MultiSelect = p_Roster_Subtest_MultiSelect;
	}
	public List<ObjectValueTO> getP_Score_Type_List() {
		return p_Score_Type_List;
	}
	public void setP_Score_Type_List(List<ObjectValueTO> p_Score_Type_List) {
		this.p_Score_Type_List = p_Score_Type_List;
	}
	public List<ObjectValueTO> getP_Selected_Score_Type() {
		return p_Selected_Score_Type;
	}
	public void setP_Selected_Score_Type(List<ObjectValueTO> p_Selected_Score_Type) {
		this.p_Selected_Score_Type = p_Selected_Score_Type;
	}
	public List<ObjectValueTO> getP_Subtest_MultiSelect() {
		return p_Subtest_MultiSelect;
	}
	public void setP_Subtest_MultiSelect(List<ObjectValueTO> p_Subtest_MultiSelect) {
		this.p_Subtest_MultiSelect = p_Subtest_MultiSelect;
	}
	public List<ObjectValueTO> getP_MinScore() {
		return p_MinScore;
	}
	public void setP_MinScore(List<ObjectValueTO> p_MinScore) {
		this.p_MinScore = p_MinScore;
	}
	public List<ObjectValueTO> getP_MaxScore() {
		return p_MaxScore;
	}
	public void setP_MaxScore(List<ObjectValueTO> p_MaxScore) {
		this.p_MaxScore = p_MaxScore;
	}
	public List<ObjectValueTO> getP_Ach_AssessmentId() {
		return p_ach_assessmentId;
	}
	public void setP_Ach_AssessmentId(List<ObjectValueTO> p_ach_assessmentId) {
		this.p_ach_assessmentId = p_ach_assessmentId;
	}
	public List<ObjectValueTO> getP_ach_AssessmentId() {
		return p_ach_assessmentId;
	}
	public void setP_ach_AssessmentId(List<ObjectValueTO> p_ach_assessmentId) {
		this.p_ach_assessmentId = p_ach_assessmentId;
	}
	public List<ObjectValueTO> getP_Inview_Comb_Rank_Order() {
		return p_Inview_Comb_Rank_Order;
	}
	public void setP_Inview_Comb_Rank_Order(List<ObjectValueTO> p_Inview_Comb_Rank_Order) {
		this.p_Inview_Comb_Rank_Order = p_Inview_Comb_Rank_Order;
	}
	public List<ObjectValueTO> getP_Inview_Comb_Score() {
		return p_Inview_Comb_Score;
	}
	public void setP_Inview_Comb_Score(List<ObjectValueTO> p_Inview_Comb_Score) {
		this.p_Inview_Comb_Score = p_Inview_Comb_Score;
	}
	public List<ObjectValueTO> getP_Inview_Comb_Subtest_Multiselect() {
		return p_Inview_Comb_Subtest_Multiselect;
	}
	public void setP_Inview_Comb_Subtest_Multiselect(
			List<ObjectValueTO> p_Inview_Comb_Subtest_Multiselect) {
		this.p_Inview_Comb_Subtest_Multiselect = p_Inview_Comb_Subtest_Multiselect;
	}
	public List<ObjectValueTO> getP_Inview_Level() {
		return p_Inview_Level;
	}
	public void setP_Inview_Level(List<ObjectValueTO> p_Inview_Level) {
		this.p_Inview_Level = p_Inview_Level;
	}
	public List<ObjectValueTO> getP_Inview_Level_Form() {
		return p_Inview_Level_Form;
	}
	public void setP_Inview_Level_Form(List<ObjectValueTO> p_Inview_Level_Form) {
		this.p_Inview_Level_Form = p_Inview_Level_Form;
	}
	public List<ObjectValueTO> getP_Level_Bible_Class() {
		return p_Level_Bible_Class;
	}
	public void setP_Level_Bible_Class(List<ObjectValueTO> p_Level_Bible_Class) {
		this.p_Level_Bible_Class = p_Level_Bible_Class;
	}
	public List<ObjectValueTO> getP_PTCS_Comb_Rank_Order() {
		return p_PTCS_Comb_Rank_Order;
	}
	public void setP_PTCS_Comb_Rank_Order(List<ObjectValueTO> p_PTCS_Comb_Rank_Order) {
		this.p_PTCS_Comb_Rank_Order = p_PTCS_Comb_Rank_Order;
	}
	public List<ObjectValueTO> getP_PTCS_Comb_Score() {
		return p_PTCS_Comb_Score;
	}
	public void setP_PTCS_Comb_Score(List<ObjectValueTO> p_PTCS_Comb_Score) {
		this.p_PTCS_Comb_Score = p_PTCS_Comb_Score;
	}
	public List<ObjectValueTO> getP_PTCS_Comb_Subtest_MultiSelect() {
		return p_PTCS_Comb_Subtest_MultiSelect;
	}
	public void setP_PTCS_Comb_Subtest_MultiSelect(
			List<ObjectValueTO> p_PTCS_Comb_Subtest_MultiSelect) {
		this.p_PTCS_Comb_Subtest_MultiSelect = p_PTCS_Comb_Subtest_MultiSelect;
	}
	public List<ObjectValueTO> getP_PTCS_Form_Level() {
		return p_PTCS_Form_Level;
	}
	public void setP_PTCS_Form_Level(List<ObjectValueTO> p_PTCS_Form_Level) {
		this.p_PTCS_Form_Level = p_PTCS_Form_Level;
	}
	public List<ObjectValueTO> getP_PTCS_Level() {
		return p_PTCS_Level;
	}
	public void setP_PTCS_Level(List<ObjectValueTO> p_PTCS_Level) {
		this.p_PTCS_Level = p_PTCS_Level;
	}
	public List<ObjectValueTO> getP_PTCS_Roster() {
		return p_PTCS_Roster;
	}
	public void setP_PTCS_Roster(List<ObjectValueTO> p_PTCS_Roster) {
		this.p_PTCS_Roster = p_PTCS_Roster;
	}
	public List<ObjectValueTO> getP_Ranking_Order_List() {
		return p_Ranking_Order_List;
	}
	public void setP_Ranking_Order_List(List<ObjectValueTO> p_Ranking_Order_List) {
		this.p_Ranking_Order_List = p_Ranking_Order_List;
	}
	public List<ObjectValueTO> getP_Roster_Combination_Score_List() {
		return p_Roster_Combination_Score_List;
	}
	public void setP_Roster_Combination_Score_List(
			List<ObjectValueTO> p_Roster_Combination_Score_List) {
		this.p_Roster_Combination_Score_List = p_Roster_Combination_Score_List;
	}
	public void setP_Level_Form(List<ObjectValueTO> p_Level_Form) {
		this.p_Level_Form = p_Level_Form;
	}
	public List<ObjectValueTO> getP_AdminYear() {
		return p_AdminYear;
	}
	public void setP_AdminYear(List<ObjectValueTO> p_AdminYear) {
		this.p_AdminYear = p_AdminYear;
	}
	public List<ObjectValueTO> getP_Ethnicity() {
		return p_Ethnicity;
	}
	public void setP_Ethnicity(List<ObjectValueTO> p_Ethnicity) {
		this.p_Ethnicity = p_Ethnicity;
	}
	public List<ObjectValueTO> getP_Gender() {
		return p_Gender;
	}
	public void setP_Gender(List<ObjectValueTO> p_Gender) {
		this.p_Gender = p_Gender;
	}
	public List<ObjectValueTO> getP_Giftedtalented() {
		return p_Giftedtalented;
	}
	public void setP_Giftedtalented(List<ObjectValueTO> p_Giftedtalented) {
		this.p_Giftedtalented = p_Giftedtalented;
	}
	public List<ObjectValueTO> getP_ScoreType() {
		return p_ScoreType;
	}
	public void setP_ScoreType(List<ObjectValueTO> p_ScoreType) {
		this.p_ScoreType = p_ScoreType;
	}
	public List<ObjectValueTO> getP_ScoreTypeSelect() {
		return p_ScoreTypeSelect;
	}
		public void setP_ScoreTypeSelect(List<ObjectValueTO> p_ScoreTypeSelect) {
		this.p_ScoreTypeSelect = p_ScoreTypeSelect;
	}
	public List<ObjectValueTO> getP_ScoreType_InView_And_PTCS() {
		return p_ScoreType_InView_And_PTCS;
	}
	public void setP_ScoreType_InView_And_PTCS(
			List<ObjectValueTO> p_ScoreType_InView_And_PTCS) {
		this.p_ScoreType_InView_And_PTCS = p_ScoreType_InView_And_PTCS;
	}
	public List<ObjectValueTO> getP_SpecialProgram() {
		return p_SpecialProgram;
	}
	public void setP_SpecialProgram(List<ObjectValueTO> p_SpecialProgram) {
		this.p_SpecialProgram = p_SpecialProgram;
	}
	public List<ObjectValueTO> getP_Subtest_Type() {
		return p_Subtest_Type;
	}
	public void setP_Subtest_Type(List<ObjectValueTO> p_Subtest_Type) {
		this.p_Subtest_Type = p_Subtest_Type;
	}
	public List<ObjectValueTO> getP_YearsinChristianschools() {
		return p_YearsinChristianschools;
	}
	public void setP_YearsinChristianschools(List<ObjectValueTO> p_YearsinChristianschools) {
		this.p_YearsinChristianschools = p_YearsinChristianschools;
	}
	public List<ObjectValueTO> getP_Hierarchy_Table() {
		return p_Hierarchy_Table;
	}
	public void setP_Hierarchy_Table(List<ObjectValueTO> p_Hierarchy_Table) {
		this.p_Hierarchy_Table = p_Hierarchy_Table;
	}
	public List<ObjectValueTO> getP_Student_Bio_Id() {
		return p_Student_Bio_Id;
	}
	public void setP_Student_Bio_Id(List<ObjectValueTO> p_Student_Bio_Id) {
		this.p_Student_Bio_Id = p_Student_Bio_Id;
	}
	public List<ObjectValueTO> getP_AdminYearMultiselect() {
		return p_AdminYearMultiselect;
	}
	public void setP_AdminYearMultiselect(List<ObjectValueTO> p_AdminYearMultiselect) {
		this.p_AdminYearMultiselect = p_AdminYearMultiselect;
	}
	public List<ObjectValueTO> getP_ExtendedAdminYear() {
		return p_ExtendedAdminYear;
	}
	public void setP_ExtendedAdminYear(List<ObjectValueTO> p_ExtendedAdminYear) {
		this.p_ExtendedAdminYear = p_ExtendedAdminYear;
	}
	public List<ObjectValueTO> getP_Longitudinal_Roster_Score_List() {
		return p_Longitudinal_Roster_Score_List;
	}
	public void setP_Longitudinal_Roster_Score_List(
			List<ObjectValueTO> p_Longitudinal_Roster_Score_List) {
		this.p_Longitudinal_Roster_Score_List = p_Longitudinal_Roster_Score_List;
	}
	public List<ObjectValueTO> getP_Longitudinal_Roster_Rank_Order() {
		return p_Longitudinal_Roster_Rank_Order;
	}
	public void setP_Longitudinal_Roster_Rank_Order(
			List<ObjectValueTO> p_Longitudinal_Roster_Rank_Order) {
		this.p_Longitudinal_Roster_Rank_Order = p_Longitudinal_Roster_Rank_Order;
	}
	public List<ObjectValueTO> getP_Longitudinal_Roster_Subtest_MultiSelect() {
		return p_Longitudinal_Roster_Subtest_MultiSelect;
	}
	public void setP_Longitudinal_Roster_Subtest_MultiSelect(
			List<ObjectValueTO> p_Longitudinal_Roster_Subtest_MultiSelect) {
		this.p_Longitudinal_Roster_Subtest_MultiSelect = p_Longitudinal_Roster_Subtest_MultiSelect;
	}
	public List<ObjectValueTO> getP_Subtest_Class_MultiSelect() {
		return p_Subtest_Class_MultiSelect;
	}
	public void setP_Subtest_Class_MultiSelect(
			List<ObjectValueTO> p_Subtest_Class_MultiSelect) {
		this.p_Subtest_Class_MultiSelect = p_Subtest_Class_MultiSelect;
	}
	public List<ObjectValueTO> getP_Longitudinal_Roster_Class_Rank_Order() {
		return p_Longitudinal_Roster_Class_Rank_Order;
	}
	public void setP_Longitudinal_Roster_Class_Rank_Order(
			List<ObjectValueTO> p_Longitudinal_Roster_Class_Rank_Order) {
		this.p_Longitudinal_Roster_Class_Rank_Order = p_Longitudinal_Roster_Class_Rank_Order;
	}
	
	public List<ObjectValueTO> getP_Grade_School_Longitudinal_Summary() {
		return p_Grade_School_Longitudinal_Summary;
	}
	public void setP_Grade_School_Longitudinal_Summary(
			List<ObjectValueTO> p_Grade_School_Longitudinal_Summary) {
		this.p_Grade_School_Longitudinal_Summary = p_Grade_School_Longitudinal_Summary;
	}
	public List<ObjectValueTO> getP_Subtest_MultiSelect_School_Longitudinal_Summary() {
		return p_Subtest_MultiSelect_School_Longitudinal_Summary;
	}
	public void setP_Subtest_MultiSelect_School_Longitudinal_Summary(
			List<ObjectValueTO> p_Subtest_MultiSelect_School_Longitudinal_Summary) {
		this.p_Subtest_MultiSelect_School_Longitudinal_Summary = p_Subtest_MultiSelect_School_Longitudinal_Summary;
	}

	public List<ObjectValueTO> getP_Acsi_Grade_Cohort() {
		return p_Acsi_Grade_Cohort;
	}
	public void setP_Acsi_Grade_Cohort(List<ObjectValueTO> p_Acsi_Grade_Cohort) {
		this.p_Acsi_Grade_Cohort = p_Acsi_Grade_Cohort;
	}
	public List<ObjectValueTO> getP_Grade_School_Cohort() {
		return p_Grade_School_Cohort;
	}
	public void setP_Grade_School_Cohort(List<ObjectValueTO> p_Grade_School_Cohort) {
		this.p_Grade_School_Cohort = p_Grade_School_Cohort;
	}
	public List<ObjectValueTO> getP_ScoreType_Acsi_Cohort() {
		return p_ScoreType_Acsi_Cohort;
	}
	public void setP_ScoreType_Acsi_Cohort(
			List<ObjectValueTO> p_ScoreType_Acsi_Cohort) {
		this.p_ScoreType_Acsi_Cohort = p_ScoreType_Acsi_Cohort;
	}
	
	public void setP_Subtest_Multiselect_School(
			List<ObjectValueTO> p_Subtest_Multiselect_School) {
		this.p_Subtest_Multiselect_School = p_Subtest_Multiselect_School;
	}
	public List<ObjectValueTO> getP_Subtest_Multiselect_School() {
		return p_Subtest_Multiselect_School;
	}
	public List<ObjectValueTO> getP_Roster_Subtest1() {
		return p_Roster_Subtest1;
	}
	public void setP_Roster_Subtest1(List<ObjectValueTO> p_Roster_Subtest1) {
		this.p_Roster_Subtest1 = p_Roster_Subtest1;
	}
	public List<ObjectValueTO> getP_Roster_Subtest2() {
		return p_Roster_Subtest2;
	}
	public void setP_Roster_Subtest2(List<ObjectValueTO> p_Roster_Subtest2) {
		this.p_Roster_Subtest2 = p_Roster_Subtest2;
	}
	public List<ObjectValueTO> getP_Roster_Subtest3() {
		return p_Roster_Subtest3;
	}
	public void setP_Roster_Subtest3(List<ObjectValueTO> p_Roster_Subtest3) {
		this.p_Roster_Subtest3 = p_Roster_Subtest3;
	}
	public List<ObjectValueTO> getP_Subtest1_MinScore() {
		return p_Subtest1_MinScore;
	}
	public void setP_Subtest1_MinScore(List<ObjectValueTO> p_Subtest1_MinScore) {
		this.p_Subtest1_MinScore = p_Subtest1_MinScore;
	}
	public List<ObjectValueTO> getP_Subtest2_MinScore() {
		return p_Subtest2_MinScore;
	}
	public void setP_Subtest2_MinScore(List<ObjectValueTO> p_Subtest2_MinScore) {
		this.p_Subtest2_MinScore = p_Subtest2_MinScore;
	}
	public List<ObjectValueTO> getP_Subtest3_MinScore() {
		return p_Subtest3_MinScore;
	}
	public void setP_Subtest3_MinScore(List<ObjectValueTO> p_Subtest3_MinScore) {
		this.p_Subtest3_MinScore = p_Subtest3_MinScore;
	}
	public List<ObjectValueTO> getP_Subtest1_MaxScore() {
		return p_Subtest1_MaxScore;
	}
	public void setP_Subtest1_MaxScore(List<ObjectValueTO> p_Subtest1_MaxScore) {
		this.p_Subtest1_MaxScore = p_Subtest1_MaxScore;
	}
	public List<ObjectValueTO> getP_Subtest2_MaxScore() {
		return p_Subtest2_MaxScore;
	}
	public void setP_Subtest2_MaxScore(List<ObjectValueTO> p_Subtest2_MaxScore) {
		this.p_Subtest2_MaxScore = p_Subtest2_MaxScore;
	}
	public List<ObjectValueTO> getP_Score_Type_Select() {
		return p_Score_Type_Select;
	}
	public void setP_Score_Type_Select(List<ObjectValueTO> p_Score_Type_Select) {
		this.p_Score_Type_Select = p_Score_Type_Select;
	}
	public List<ObjectValueTO> getP_Subtest3_MaxScore() {
		return p_Subtest3_MaxScore;
	}
	public void setP_Subtest3_MaxScore(List<ObjectValueTO> p_Subtest3_MaxScore) {
		this.p_Subtest3_MaxScore = p_Subtest3_MaxScore;
	}
}
