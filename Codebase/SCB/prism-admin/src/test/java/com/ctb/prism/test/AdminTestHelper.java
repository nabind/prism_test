package com.ctb.prism.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ctb.prism.admin.transferobject.EduCenterTO;
import com.ctb.prism.admin.transferobject.EduCenterTreeTO;
import com.ctb.prism.admin.transferobject.ObjectValueTO;
import com.ctb.prism.admin.transferobject.OrgTO;
import com.ctb.prism.admin.transferobject.OrgTreeTO;
import com.ctb.prism.admin.transferobject.PwdHintTO;
import com.ctb.prism.admin.transferobject.RoleTO;
import com.ctb.prism.admin.transferobject.StgOrgTO;
import com.ctb.prism.admin.transferobject.StudentDataTO;
import com.ctb.prism.admin.transferobject.UserDataTO;
import com.ctb.prism.admin.transferobject.UserTO;
import com.ctb.prism.admin.transferobject.studentdata.ContentDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.ContentScoreDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.ContentScoreTO;
import com.ctb.prism.admin.transferobject.studentdata.CustHierarchyDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.DemoTO;
import com.ctb.prism.admin.transferobject.studentdata.ItemResponseTO;
import com.ctb.prism.admin.transferobject.studentdata.ItemResponsesDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.ObjectiveScoreDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.ObjectiveScoreTO;
import com.ctb.prism.admin.transferobject.studentdata.OrgDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.RosterDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.StudentBioTO;
import com.ctb.prism.admin.transferobject.studentdata.StudentDemoTO;
import com.ctb.prism.admin.transferobject.studentdata.StudentDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.StudentListTO;
import com.ctb.prism.admin.transferobject.studentdata.StudentSurveyBioTO;
import com.ctb.prism.admin.transferobject.studentdata.SubtestAccommodationTO;
import com.ctb.prism.admin.transferobject.studentdata.SubtestAccommodationsTO;
import com.ctb.prism.admin.transferobject.studentdata.SurveyBioTO;

/**
 * This class provides input parameters for test methods.
 * 
 * @author TCS
 *
 */
public class AdminTestHelper {
	public static EduCenterTO getEduCenterTO(TestParams testParams) {
		EduCenterTO to = new EduCenterTO();
		to.setEduCenterId(0L);
		to.setEduCenterCode("");
		to.setEduCenterName("");
		to.setUserId(0L);
		to.setUserName("");
		to.setFullName("");
		to.setStatus("");
		List<RoleTO> roleList = new ArrayList<RoleTO>();
		roleList.add(new RoleTO());
		to.setRoleList(roleList);
		return to;
	}

	public static EduCenterTreeTO getEduCenterTreeTO(TestParams testParams) {
		EduCenterTreeTO to = new EduCenterTreeTO();
		to.setState("");
		to.setData("");
		to.setEduTreeId(0L);
		to.setMetadata(new EduCenterTO());
		to.setAttr(new EduCenterTO());
		to.setEduCenterName("");
		to.setId("");
		return to;
	}

	public static ObjectValueTO getObjectValueTO(TestParams testParams) {
		ObjectValueTO to = new ObjectValueTO();
		to.setName("");
		to.setValue("");
		to.setOther("");
		return to;
	}

	public static OrgTO getOrgTO(TestParams testParams) {
		OrgTO to = new OrgTO();
		to.setTenantId(0L);
		to.setTenantName("");
		to.setParentNameAlias("");
		to.setSelectedOrgId("");
		to.setAdminName("");
		to.setParentTenantId(0L);
		to.setNoOfChildOrgs(0L);
		to.setNoOfUsers(0L);
		to.setOrgLevel(0L);
		to.setId(0L);
		to.setCustomerId(0L);
		to.setAdminId(0L);
		to.setClassName("");
		return to;
	}

	public static PwdHintTO getPwdHintTO(TestParams testParams) {
		PwdHintTO to = new PwdHintTO();
		to.setUserId(0L);
		to.setQuestionId(0L);
		to.setQuestionValue("");
		to.setQuestionSequence(0L);
		to.setQuestionActivationStatus("");
		to.setAnswerId(0L);
		to.setAnswerValue("");
		return to;
	}

	public static RoleTO getRoleTO(TestParams testParams) {
		RoleTO to = new RoleTO();
		to.setRoleId(0L);
		to.setRoleName("");
		to.setRoleDescription("");
		to.setLabel("");
		to.setDefaultSelection("");
		to.setEnabled(false);
		List<UserTO> userList = new ArrayList<UserTO>();
		to.setUserList(userList);
		return to;
	}

	public static OrgTreeTO getOrgTreeTO(TestParams testParams) {
		OrgTreeTO to = new OrgTreeTO();
		to.setState("");
		to.setData("");
		to.setOrgTreeId(0L);
		to.setMetadata(getOrgTO(testParams));
		to.setAttr(getOrgTO(testParams));
		return to;
	}

	public static UserTO getUserTO(TestParams testParams) {
		UserTO to = new UserTO();
		to.setUserId(0L);
		to.setUserName("");
		to.setUserDisplayName("");
		to.setTenantId(0L);
		to.setParentId(0L);
		to.setLoggedInOrgId(0L);
		to.setTenantName("");
		to.setFirstName("");
		to.setMiddleName("");
		to.setLastName("");
		to.setPassword("");
		to.setStatus("");
		to.setEmailId("");
		to.setUserType("");
		to.setFirstTimeUser(false);
		List<RoleTO> availableRoleList = new ArrayList<RoleTO>();
		to.setAvailableRoleList(availableRoleList);
		List<RoleTO> masterRoleList = new ArrayList<RoleTO>();
		to.setMasterRoleList(masterRoleList);

		to.setId(0L);
		to.setEduCenterCode("");
		to.setEduCenterName("");
		to.setPhoneNumber("");
		to.setStreet("");
		to.setCity("");
		to.setState("");
		to.setZip("");
		to.setCountry("");
		to.setData("");
		to.setEduTreeId(0L);
		to.setMetadata(new UserTO());
		to.setAttr(new UserTO());
		List<PwdHintTO> pwdHintList = new ArrayList<PwdHintTO>();
		to.setPwdHintList(pwdHintList);
		return to;
	}

	public static StudentDataTO getStudentDataTO(TestParams testParams) {
		StudentDataTO to = new StudentDataTO();
		to.setCustID("");
		to.setRosterID("");
		to.setHierarchyA_Name("");
		to.setHierarchyA_Code("");
		to.setHierarchyA_SpCodes("");
		to.setHierarchyB_Name("");
		to.setHierarchyB_Code("");
		to.setHierarchyB_SpCodes("");
		to.setHierarchyC_Name("");
		to.setHierarchyC_Code("");
		to.setHierarchyC_SpCodes("");
		to.setHierarchyD_Name("");
		to.setHierarchyD_Code("");
		to.setHierarchyD_SpCodes("");
		to.setHierarchyE_Name("");
		to.setHierarchyE_Code("");
		to.setHierarchyE_SpCodes("");
		to.setHierarchyF_Name("");
		to.setHierarchyF_Code("");
		to.setHierarchyF_SpCodes("");
		to.setHierarchyG_Name("");
		to.setHierarchyG_Code("");
		to.setHierarchyG_SpCodes("");
		to.setLastName("");
		to.setFirstName("");
		to.setMiddleInitial("");
		to.setDateOfBirth("");
		to.setForm("");
		to.setChangedFormFlag("");
		to.setTestLang("");
		to.setTestMode("");
		to.setGender("");
		to.setResolvedEthnicity("");
		to.setStudentID("");
		to.setPhoneNumber("");
		to.setLithocode("");
		to.setImagingID("");
		to.setStreetAddress("");
		to.setAptNo("");
		to.setCity("");
		to.setState("");
		to.setZipCode("");
		to.setCounty_Code("");
		to.setEduc_Center_Code("");
		to.setAcc_Audio_Rd("");
		to.setAcc_Audio_Wr("");
		to.setAcc_Audio_Math1("");
		to.setAcc_Audio_Math2("");
		to.setAcc_Audio_Sc("");
		to.setAcc_Audio_SS("");
		to.setAcc_Breaks_Rd("");
		to.setAcc_Breaks_Wr("");
		to.setAcc_Breaks_Math1("");
		to.setAcc_Breaks_Math2("");
		to.setAcc_Breaks_SC("");
		to.setAcc_Breaks_SS("");
		to.setAcc_Calculator_Rd("");
		to.setAcc_Calculator_Wr("");
		to.setAcc_Calculator_Math1("");
		to.setAcc_Calculator_SS("");
		to.setAcc_Duration1_25_Rd("");
		to.setAcc_Duration1_25_Wr("");
		to.setAcc_Duration1_25_Math1("");
		to.setAcc_Duration1_25_Math2("");
		to.setAcc_Duration1_25_Sc("");
		to.setAcc_Duration1_25_SS("");
		to.setAcc_Duration1_5_Rd("");
		to.setAcc_Duration1_5_Wr("");
		to.setAcc_Duration1_5_Math1("");
		to.setAcc_Duration1_5_Math2("");
		to.setAcc_Duration1_5_Sc("");
		to.setAcc_Duration1_5_SS("");
		to.setAcc_Duration2_0_Rd("");
		to.setAcc_Duration2_0_Wr("");
		to.setAcc_Duration2_0_Math1("");
		to.setAcc_Duration2_0_Math2("");
		to.setAcc_Duration2_0_Sc("");
		to.setAcc_Duration2_0_SS("");
		to.setAcc_PhysicalSupport_Rd("");
		to.setAcc_PhysicalSupport_Wr("");
		to.setAcc_PhysicalSupport_Math1("");
		to.setAcc_PhysicalSupport_Math2("");
		to.setAcc_PhysicalSupport_Sc("");
		to.setAcc_PhysicalSupport_SS("");
		to.setAcc_Scribe_Rd("");
		to.setAcc_Scribe_Wr("");
		to.setAcc_Scribe_Math1("");
		to.setAcc_Scribe_Math2("");
		to.setAcc_Scribe_Sc("");
		to.setAcc_Scribe_SS("");
		to.setAcc_TechDevice_Rd("");
		to.setAcc_TechDevice_Wr("");
		to.setAcc_TechDevice_Math1("");
		to.setAcc_TechDevice_Math2("");
		to.setAcc_TechDevice_Sc("");
		to.setAcc_TechDevice_SS("");
		to.setAcc_SepRoom_Rd("");
		to.setAcc_SepRoom_Wr("");
		to.setAcc_SepRoom_Math1("");
		to.setAcc_SepRoom_Math2("");
		to.setAcc_SepRoom_Sc("");
		to.setAcc_SepRoom_SS("");
		to.setAcc_SmGroup_Rd("");
		to.setAcc_SmGroup_Wr("");
		to.setAcc_SmGroup_Math1("");
		to.setAcc_SmGroup_Math2("");
		to.setAcc_SmGroup_Sc("");
		to.setAcc_SmGroup_SS("");
		to.setAcc_Other_Rd("");
		to.setAcc_Other_Wr("");
		to.setAcc_Other_Math1("");
		to.setAcc_Other_Math2("");
		to.setAcc_Other_Sc("");
		to.setAcc_Other_SS("");
		to.setExaminer_Ack_Accomm("");
		to.setHomeLang("");
		to.setK_12_Educ_Completed("");
		to.setSubj_Taken_Passed_Rd("");
		to.setSubj_Taken_Passed_Wr("");
		to.setSubj_Taken_Passed_Math("");
		to.setSubj_Taken_Passed_Sc("");
		to.setSubj_Taken_Passed_SS("");
		to.setSubj_Taken_Passed_None("");
		to.setNo_Times_Taken_Rd("");
		to.setNo_Times_Taken_Wr("");
		to.setNo_Times_Taken_Math("");
		to.setNo_Times_Taken_Sc("");
		to.setNo_Times_Taken_SS("");
		to.setRetake_Rd("");
		to.setRetake_Wr("");
		to.setRetake_Math("");
		to.setRetake_Sc("");
		to.setRetake_SS("");
		to.setRetake_None("");
		to.setTake_Readiness_Assessment("");
		to.setPrepare_County_Prog("");
		to.setPrepare_Sch_Dist_Prog("");
		to.setPrepare_Military_Prog("");
		to.setPrepare_Religious_Prog("");
		to.setPrepare_Purchased_My_Own_Study_Books("");
		to.setPrepare_Online_Study_Prog("");
		to.setPrepare_Homeschool("");
		to.setPrepare_Tutor("");
		to.setPrepare_Self_Taught("");
		to.setRecent_Class_Rd("");
		to.setRecent_Class_Wr("");
		to.setRecent_Class_Math("");
		to.setRecent_Class_Sc("");
		to.setRecent_Class_SS("");
		to.setMonths_Studied_Subj_Rd("");
		to.setMonths_Studied_Subj_Wr("");
		to.setMonths_Studied_Subj_Math("");
		to.setMonths_Studied_Subj_Sc("");
		to.setMonths_Studied_Subj_SS("");
		to.setGrade_in_Subj_Rd("");
		to.setGrade_in_Subj_Wr("");
		to.setGrade_in_Subj_Math("");
		to.setGrade_in_Subj_Sc("");
		to.setGrade_in_Subj_SS("");
		to.setTestFormat_Braille("");
		to.setTestFormat_LP("");
		to.setLocal_Field_1("");
		to.setLocal_Field_2("");
		to.setLocal_Field_3("");
		to.setLocal_Field_4("");
		to.setLocal_Field_5("");
		to.setLocal_Field_6("");
		to.setLocal_Field_7("");
		to.setLocal_Field_8("");
		to.setLocal_Field_9("");
		to.setLocal_Field_10("");
		to.setLocal_Field_11("");
		to.setLocal_Field_12("");
		to.setLocal_Field_13("");
		to.setLocal_Field_14("");
		to.setLocal_Field_15("");
		to.setLocal_Field_16("");
		to.setLocal_Field_17("");
		to.setLocal_Field_18("");
		to.setLocal_Field_19("");
		to.setLocal_Field_20("");
		to.setCandidate_Acknowledgement("");
		// Reading , len
		to.setReading_dateTestTaken("");
		to.setReading_numberCorrect("");
		to.setReading_scaleScore("");
		to.setReading_hSE_Score("");
		to.setReading_percentileRank("");
		to.setReading_nCE("");
		to.setReading_std_Obj_Mstry_Scr_1("");
		to.setReading_not_All_items_atmpt_1("");
		to.setReading_std_Obj_Mstry_Scr_2("");
		to.setReading_not_All_items_atmpt_2("");
		to.setReading_std_Obj_Mstry_Scr_3("");
		to.setReading_not_All_items_atmpt_3("");
		to.setReading_std_Obj_Mstry_Scr_4("");
		to.setReading_not_All_items_atmpt_4("");
		to.setReading_std_Obj_Mstry_Scr_5("");
		to.setReading_not_All_items_atmpt_5("");
		to.setReading_std_Obj_Mstry_Scr_6("");
		to.setReading_not_All_items_atmpt_6("");
		to.setReading_std_Obj_Mstry_Scr_7("");
		to.setReading_not_All_items_atmpt_7("");
		to.setReading_std_Obj_Mstry_Scr_8("");
		to.setReading_not_All_items_atmpt_8("");
		// Writing , len
		to.setWriting_dateTestTaken("");
		to.setWriting_numberCorrect("");
		to.setWriting_scaleScore("");
		to.setWriting_hSE_Score("");
		to.setWriting_percentileRank("");
		to.setWriting_nCE("");
		to.setWriting_std_Obj_Mstry_Scr_1("");
		to.setWriting_not_All_items_atmpt_1("");
		to.setWriting_std_Obj_Mstry_Scr_2("");
		to.setWriting_not_All_items_atmpt_2("");
		to.setWriting_std_Obj_Mstry_Scr_3("");
		to.setWriting_not_All_items_atmpt_3("");
		to.setWriting_std_Obj_Mstry_Scr_4("");
		to.setWriting_not_All_items_atmpt_4("");
		// ELA Test , leh
		to.setEla_scaleScore("");
		to.setEla_hSE_Score("");
		to.setEla_percentileRank("");
		to.setEla_nCE("");
		// Mathemat , leh
		to.setMath_dateTestTaken("");
		to.setMath_numberCorrect("");
		to.setMath_scaleScore("");
		to.setMath_hSE_Score("");
		to.setMath_percentileRank("");
		to.setMath_nCE("");
		to.setMath_std_Obj_Mstry_Scr_1("");
		to.setMath_not_All_items_atmpt_1("");
		to.setMath_std_Obj_Mstry_Scr_2("");
		to.setMath_not_All_items_atmpt_2("");
		to.setMath_std_Obj_Mstry_Scr_3("");
		to.setMath_not_All_items_atmpt_3("");
		to.setMath_std_Obj_Mstry_Scr_4("");
		to.setMath_not_All_items_atmpt_4("");
		to.setMath_std_Obj_Mstry_Scr_5("");
		to.setMath_not_All_items_atmpt_5("");
		to.setMath_std_Obj_Mstry_Scr_6("");
		to.setMath_not_All_items_atmpt_6("");
		to.setMath_std_Obj_Mstry_Scr_7("");
		to.setMath_not_All_items_atmpt_7("");
		// Science , len
		to.setScience_dateTestTaken("");
		to.setScience_numberCorrect("");
		to.setScience_scaleScore("");
		to.setScience_hSE_Score("");
		to.setScience_percentileRank("");
		to.setScience_nCE("");
		to.setScience_std_Obj_Mstry_Scr_1("");
		to.setScience_not_All_items_atmpt_1("");
		to.setScience_std_Obj_Mstry_Scr_2("");
		to.setScience_not_All_items_atmpt_2("");
		to.setScience_std_Obj_Mstry_Scr_3("");
		to.setScience_not_All_items_atmpt_3("");
		to.setScience_std_Obj_Mstry_Scr_4("");
		to.setScience_not_All_items_atmpt_4("");
		to.setScience_std_Obj_Mstry_Scr_5("");
		to.setScience_not_All_items_atmpt_5("");
		// Social S , leh
		to.setSocial_dateTestTaken("");
		to.setSocial_numberCorrect("");
		to.setSocial_scaleScore("");
		to.setSocial_hSE_Score("");
		to.setSocial_percentileRank("");
		to.setSocial_nCE("");
		to.setSocial_std_Obj_Mstry_Scr_1("");
		to.setSocial_not_All_items_atmpt_1("");
		to.setSocial_std_Obj_Mstry_Scr_2("");
		to.setSocial_not_All_items_atmpt_2("");
		to.setSocial_std_Obj_Mstry_Scr_3("");
		to.setSocial_not_All_items_atmpt_3("");
		to.setSocial_std_Obj_Mstry_Scr_4("");
		to.setSocial_not_All_items_atmpt_4("");
		to.setSocial_std_Obj_Mstry_Scr_5("");
		to.setSocial_not_All_items_atmpt_5("");
		to.setSocial_std_Obj_Mstry_Scr_6("");
		to.setSocial_not_All_items_atmpt_6("");
		to.setSocial_std_Obj_Mstry_Scr_7("");
		to.setSocial_not_All_items_atmpt_7("");
		// Overall , len
		to.setOverall_scaleScore("");
		to.setOverall_hSE_Score("");
		to.setOverall_percentileRank("");
		to.setOverall_nCE("");
		// OAS Sett , leh
		to.setOasReading_screenReader("");
		to.setOasReading_onlineCalc("");
		to.setOasReading_testPause("");
		to.setOasReading_highlighter("");
		to.setOasReading_blockingRuler("");
		to.setOasReading_magnifyingGlass("");
		to.setOasReading_fontAndBkgndClr("");
		to.setOasReading_largeFont("");
		to.setOasReading_musicPlayer("");
		to.setOasReading_extendedTime("");
		to.setOasReading_maskingTool("");
		// OAS Sett , leh
		to.setOasWriting_screenReader("");
		to.setOasWriting_onlineCalc("");
		to.setOasWriting_testPause("");
		to.setOasWriting_highlighter("");
		to.setOasWriting_blockingRuler("");
		to.setOasWriting_magnifyingGlass("");
		to.setOasWriting_fontAndBkgndClr("");
		to.setOasWriting_largeFont("");
		to.setOasWriting_musicPlayer("");
		to.setOasWriting_extendedTime("");
		to.setOasWriting_maskingTool("");
		// OAS Sett , leh
		to.setOasMath_screenReader("");
		to.setOasMath_onlineCalc("");
		to.setOasMath_testPause("");
		to.setOasMath_highlighter("");
		to.setOasMath_blockingRuler("");
		to.setOasMath_magnifyingGlass("");
		to.setOasMath_fontAndBkgndClr("");
		to.setOasMath_largeFont("");
		to.setOasMath_musicPlayer("");
		to.setOasMath_extendedTime("");
		to.setOasMath_maskingTool("");
		// OAS Sett , leh
		to.setOasScience_screenReader("");
		to.setOasScience_onlineCalc("");
		to.setOasScience_testPause("");
		to.setOasScience_highlighter("");
		to.setOasScience_blockingRuler("");
		to.setOasScience_magnifyingGlass("");
		to.setOasScience_fontAndBkgndClr("");
		to.setOasScience_largeFont("");
		to.setOasScience_musicPlayer("");
		to.setOasScience_extendedTime("");
		to.setOasScience_maskingTool("");
		// OAS Sett , leh
		to.setOasSocial_screenReader("");
		to.setOasSocial_onlineCalc("");
		to.setOasSocial_testPause("");
		to.setOasSocial_highlighter("");
		to.setOasSocial_blockingRuler("");
		to.setOasSocial_magnifyingGlass("");
		to.setOasSocial_fontAndBkgndClr("");
		to.setOasSocial_largeFont("");
		to.setOasSocial_musicPlayer("");
		to.setOasSocial_extendedTime("");
		to.setOasSocial_maskingTool("");
		// Item Res , leh
		to.setReadingItems_SR("");
		to.setReadingItems_FU("");
		to.setWritingItems_SR("");
		to.setWritingItems_CR("");
		to.setWritingItems_FU("");
		to.setMathItems_SR("");
		to.setMathItems_GR_Status("");
		to.setMathItems_GR_Edited("");
		to.setMathItems_FU("");
		to.setScienceItems_SR("");
		to.setScienceItems_FU("");
		to.setSocialStudies_SR("");
		to.setSocialStudies_FU("");
		to.setcTBUseField("");
		return to;
	}

	public static UserDataTO getUserDataTO(TestParams testParams) {
		UserDataTO to = new UserDataTO();
		to.setUserId("0");
		to.setFullName("");
		to.setStatus("");
		to.setOrgName("");
		to.setUserRoles("");
		return to;
	}

	public static StgOrgTO getStgOrgTO(TestParams testParams) {
		StgOrgTO to = new StgOrgTO();
		to.setOrgNodeId(0L);
		to.setOrgNodeName("");
		to.setOrgNodeCode("");
		to.setOrgNodeLevel(0L);
		to.setStrucElement("");
		to.setSpecialCodes("");
		to.setOrgMode("");
		to.setParentOrgNodeId(0L);
		to.setOrgNodeCodePath("");
		to.setEmail("abc@abc.com");
		to.setAdminId(0L);
		to.setCustomerId(0L);
		return to;
	}

	public static ContentDetailsTO getContentDetailsTO(TestParams testParams) {
		ContentDetailsTO to = new ContentDetailsTO();
		to.setContentCode("");
		to.setScoringMethod("");
		to.setStatusCode("");
		to.setDateTestTaken("");
		to.setDataChanged(false);
		to.setSubtestAccommodationsTO(getSubtestAccommodationsTO(testParams));
		to.setItemResponsesDetailsTO(getItemResponsesDetailsTO(testParams));
		to.setContentScoreDetailsTO(getContentScoreDetailsTO(testParams));
		List<ObjectiveScoreDetailsTO> collObjectiveScoreDetailsTO = new ArrayList<ObjectiveScoreDetailsTO>();
		collObjectiveScoreDetailsTO.add(getObjectiveScoreDetailsTO(testParams));
		to.setCollObjectiveScoreDetailsTO(collObjectiveScoreDetailsTO);
		return to;
	}

	public static ContentScoreTO getContentScoreTO(TestParams testParams) {
		ContentScoreTO to = new ContentScoreTO();
		to.setScoreType("");
		to.setScoreValue("");
		return to;
	}

	public static ContentScoreDetailsTO getContentScoreDetailsTO(TestParams testParams) {
		ContentScoreDetailsTO to = new ContentScoreDetailsTO();
		List<ContentScoreTO> collContentScoreTO = new ArrayList<ContentScoreTO>();
		collContentScoreTO.add(getContentScoreTO(testParams));
		to.setCollContentScoreTO(collContentScoreTO);
		return to;
	}

	public static CustHierarchyDetailsTO getCustHierarchyDetailsTO(TestParams testParams) {
		CustHierarchyDetailsTO to = new CustHierarchyDetailsTO();
		to.setCustomerId("");
		to.setMaxHierarchy("");
		to.setDataChanged(false);
		List<OrgDetailsTO> collOrgDetailsTO = new ArrayList<OrgDetailsTO>();
		collOrgDetailsTO.add(getOrgDetailsTO(testParams));
		to.setCollOrgDetailsTO(collOrgDetailsTO);
		return to;
	}

	public static DemoTO getDemoTO(TestParams testParams) {
		DemoTO to = new DemoTO();
		to.setDemoName("");
		to.setDemovalue("");
		return to;
	}

	public static ItemResponseTO getItemResponseTO(TestParams testParams) {
		ItemResponseTO to = new ItemResponseTO();
		to.setItemSetType("");
		to.setScoreValue("");
		to.setItemCode("");
		return to;
	}

	public static ItemResponsesDetailsTO getItemResponsesDetailsTO(TestParams testParams) {
		ItemResponsesDetailsTO to = new ItemResponsesDetailsTO();
		List<ItemResponseTO> itemResponseTO = new ArrayList<ItemResponseTO>();
		itemResponseTO.add(getItemResponseTO(testParams));
		to.setItemResponseTO(itemResponseTO);
		return to;
	}

	public static ObjectiveScoreDetailsTO getObjectiveScoreDetailsTO(TestParams testParams) {
		ObjectiveScoreDetailsTO to = new ObjectiveScoreDetailsTO();
		to.setObjectiveName("");
		to.setObjectiveCode("");
		List<ObjectiveScoreTO> collObjectiveScoreTO = new ArrayList<ObjectiveScoreTO>();
		collObjectiveScoreTO.add(getObjectiveScoreTO(testParams));
		to.setCollObjectiveScoreTO(collObjectiveScoreTO);
		return to;
	}

	public static ObjectiveScoreTO getObjectiveScoreTO(TestParams testParams) {
		ObjectiveScoreTO to = new ObjectiveScoreTO();
		to.setScoreType("");
		to.setValue("");
		return to;
	}

	public static OrgDetailsTO getOrgDetailsTO(TestParams testParams) {
		OrgDetailsTO to = new OrgDetailsTO();
		to.setOrgName("");
		to.setOrgLabel("");
		to.setOrgLevel("");
		to.setOrgNodeId("");
		to.setOrgCode("");
		to.setParentOrgCode("");
		return to;
	}

	public static RosterDetailsTO getRosterDetailsTO(TestParams testParams) {
		RosterDetailsTO to = new RosterDetailsTO();
		to.setRosterId("");
		to.setCustHierarchyDetailsTO(getCustHierarchyDetailsTO(testParams));
		to.setStudentDetailsTO(getStudentDetailsTO(testParams));
		List<ContentDetailsTO> collContentDetailsTO = new ArrayList<ContentDetailsTO>();
		collContentDetailsTO.add(getContentDetailsTO(testParams));
		to.setCollContentDetailsTO(collContentDetailsTO);
		return to;
	}

	public static StudentBioTO getStudentBioTO(TestParams testParams) {
		StudentBioTO to = new StudentBioTO();
		to.setDataChanged(false);
		to.setOasStudentId("");
		to.setLastName("");
		to.setFirstName("");
		to.setMiddleInit("");
		to.setGender("");
		to.setGrade("");
		to.setExamineeId("");
		to.setBirthDate("");
		to.setChrnlgclAge("");
		return to;
	}

	public static StudentDemoTO getStudentDemoTO(TestParams testParams) {
		StudentDemoTO to = new StudentDemoTO();
		to.setDataChanged(false);
		List<DemoTO> collDemoTO = new ArrayList<DemoTO>();
		collDemoTO.add(getDemoTO(testParams));
		to.setCollDemoTO(collDemoTO);
		return to;
	}

	public static StudentDetailsTO getStudentDetailsTO(TestParams testParams) {
		StudentDetailsTO to = new StudentDetailsTO();
		to.setStudentBioTO(getStudentBioTO(testParams));
		to.setStudentDemoTO(getStudentDemoTO(testParams));
		to.setStudentSurveyBioTO(getStudentSurveyBioTO(testParams));
		return to;
	}

	public static StudentListTO getStudentListTO(TestParams testParams) {
		StudentListTO to = new StudentListTO();
		List<RosterDetailsTO> rosterDetailsTO = new ArrayList<RosterDetailsTO>();
		rosterDetailsTO.add(getRosterDetailsTO(testParams));
		to.setRosterDetailsTO(rosterDetailsTO);
		return to;
	}

	public static StudentSurveyBioTO getStudentSurveyBioTO(TestParams testParams) {
		StudentSurveyBioTO to = new StudentSurveyBioTO();
		to.setDataChanged(false);
		List<SurveyBioTO> collSurveyBioTO = new ArrayList<SurveyBioTO>();
		collSurveyBioTO.add(getSurveyBioTO(testParams));
		to.setCollSurveyBioTO(collSurveyBioTO);
		return to;
	}

	public static SubtestAccommodationTO getSubtestAccommodationTO(TestParams testParams) {
		SubtestAccommodationTO to = new SubtestAccommodationTO();
		to.setName("");
		to.setValue("");
		return to;
	}

	public static SubtestAccommodationsTO getSubtestAccommodationsTO(TestParams testParams) {
		SubtestAccommodationsTO to = new SubtestAccommodationsTO();
		List<SubtestAccommodationTO> collSubtestAccommodationTO = new ArrayList<SubtestAccommodationTO>();
		collSubtestAccommodationTO.add(getSubtestAccommodationTO(testParams));
		to.setCollSubtestAccommodationTO(collSubtestAccommodationTO);
		return to;
	}

	public static SurveyBioTO getSurveyBioTO(TestParams testParams) {
		SurveyBioTO to = new SurveyBioTO();
		to.setSurveyName("");
		to.setSurveyValue("");
		return to;
	}

	public static Map<String, Object> helpGetOrganizationDetailsOnFirstLoad(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("nodeid", "0");
		// paramMap.put("adminYear", "0");
		paramMap.put("customerId", 0L);
		return paramMap;
	}

	public static Map<String, Object> helpGetOrganizationDetailsOnClick(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("nodeid", "0");
		paramMap.put("adminYear", "0");
		paramMap.put("customerId", 0L);
		return paramMap;
	}

	public static Map<String, Object> helpGetUserDetailsOnClick(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("NODEID", "0");
		paramMap.put("CURRENTORG", "0");
		paramMap.put("ADMINYEAR", "0");
		paramMap.put("SEARCHPARAM", null);
		paramMap.put("customerId", "0");
		paramMap.put("CUSTOMERID", "0");
		paramMap.put("ORGMODE", "0");
		paramMap.put("moreCount", "15");
		return paramMap;
	}

	public static Map<String, Object> helpEditUserData(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", "0");
		paramMap.put("customer", "0");
		return paramMap;
	}

	public static Map<String, Object> helpDeleteUser(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("Id", "0");
		paramMap.put("userName", "abc");
		paramMap.put("purpose", "abc");
		return paramMap;
	}

	public static Map<String, Object> helpAddNewUser(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userName", "0");
		paramMap.put("password", "0");
		paramMap.put("userDisplayName", "0");
		paramMap.put("emailId", "0");
		paramMap.put("userStatus", "0");
		paramMap.put("customer", "0");
		paramMap.put("tenantId", "0");
		paramMap.put("orgLevel", "0");
		paramMap.put("adminYear", "0");
		paramMap.put("purpose", "0");
		paramMap.put("eduCenterId", "0");
		paramMap.put("userRoles", new String[] { "CTB_USER" });
		return paramMap;
	}

	public static Map<String, Object> helpSearchEduUser(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userName", "0");
		paramMap.put("tenantId", "0");
		paramMap.put("isExactSearch", "N");
		return paramMap;
	}

	public static Map<String, Object> helpSearchUserAutoComplete(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("term", "0");
		paramMap.put("selectedOrg", "0");
		paramMap.put("adminYear", "0");
		paramMap.put("orgMode", "0");
		paramMap.put("moreCount", "15");
		paramMap.put("purpose", "0");
		return paramMap;
	}

	public static Map<String, Object> helpSearchOrganization(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("orgName", "0");
		paramMap.put("tenantId", 0L);
		paramMap.put("custProdId", 0L);
		paramMap.put("customerId", 0L);
		paramMap.put("orgMode", "0");
		return paramMap;
	}

	public static Map<String, Object> helpSearchOrgAutoComplete(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("orgName", "0");
		paramMap.put("tenantId", 0L);
		paramMap.put("custProdId", 0L);
		paramMap.put("customerId", 0L);
		paramMap.put("orgMode", "0");
		return paramMap;
	}

	public static Map<String, Object> helpGetRoleDetailsById(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("roleId", "0");
		paramMap.put("currentOrg", "0");
		paramMap.put("customer", "0");
		paramMap.put("moreRole", "true");
		paramMap.put("lastUserId", "0");
		return paramMap;
	}

	public static Map<String, Object> helpGetOrganizationTree(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("nodeid", "0");
		paramMap.put("currOrg", "0");
		paramMap.put("isFirstLoad", true);
		paramMap.put("adminYear", "0");
		paramMap.put("customerId", 0L);
		paramMap.put("orgMode", "0");
		return paramMap;
	}

	public static Map<String, Object> helpGetOrgTree(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("nodeid", "0");
		paramMap.put("isFirstLoad", true);
		paramMap.put("adminYear", "0");
		paramMap.put("customerId", 0L);
		paramMap.put("orgMode", "0");
		return paramMap;
	}

	public static Map<String, Object> helpGetOrganizationTreeOnRedirect(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("nodeid", "0");
		paramMap.put("currOrg", "0");
		paramMap.put("userId", "0");
		paramMap.put("customerId", 0L);
		paramMap.put("isRedirected", true);
		return paramMap;
	}

	public static Map<String, Object> helpResetPassword(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("username", ""); 
		paramMap.put("contractName", testParams.getContractName()); 
		return paramMap;
	}

	public static Map<String, Object> helpGetHierarchy(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("nodeid", "0");
		paramMap.put("adminYear", "0");
		paramMap.put("customerId", 0L);
		paramMap.put("orgMode", "0");
		paramMap.put("selectedLevelOrgId", "0");
		return paramMap;
	}

	public static Map<String, Object> helpDownloadStudentFile(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static Map<String, Object> helpGetEducationCenter(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = new com.ctb.prism.login.transferobject.UserTO();
		paramMap.put("loggedinUserTO", loggedinUserTO);
		return paramMap;
	}

	public static Map<String, Object> helpLoadEduCenterUsers(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		com.ctb.prism.login.transferobject.UserTO to = getAdminUserTO(testParams);
		paramMap.put("loggedinUserTO", to);
		paramMap.put("eduCenterId", 0L);
		paramMap.put("searchParam", "0");
		paramMap.put("lastEduCenterId_username", "0");
		paramMap.put("moreCount", "15");
		return paramMap;
	}

	private static com.ctb.prism.login.transferobject.UserTO getAdminUserTO(TestParams testParams) {
		com.ctb.prism.login.transferobject.UserTO to = new com.ctb.prism.login.transferobject.UserTO();
		return to;
	}

	public static Map<String, String> helpGetUserData(TestParams testParams) {
		Map<String, String> paramMap = new HashMap<String, String>();
		return paramMap;
	}

	public static Map<String, Object> helpGetCustomerProduct(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loggedInCustomer", "0");
		paramMap.put("loggedInOrgId", "0");
		return paramMap;
	}

	public static Map<String, String> helpGetUserForResetPassword(TestParams testParams) {
		Map<String, String> paramMap = new HashMap<String, String>();
		return paramMap;
	}

	public static Map<String, String> helpGetEduUserRoleList(TestParams testParams) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userId", "0");
		return paramMap;
	}

}
