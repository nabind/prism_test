package com.ctb.prism.test;

import java.util.HashMap;
import java.util.Map;

import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.transferobject.GrtTO;
import com.ctb.prism.inors.transferobject.InvitationCodeTO;
import com.ctb.prism.inors.transferobject.JobTO;
import com.ctb.prism.inors.transferobject.ObjectValueTO;

/**
 * This class provides input parameters for test methods.
 * 
 * @author TCS
 *
 */
public class InorsTestHelper {

	public static BulkDownloadTO getBulkDownloadTO(TestParams testParams) {
		BulkDownloadTO to = new BulkDownloadTO();
		to.setTestAdministration("");
		to.setTestProgram("");
		to.setCorp("");
		to.setSchool("");
		to.setGrade("");
		to.setOrgClass("");
		to.setFileName("");
		to.setDateRequested("");
		to.setStudentCount(0L);
		to.setSchoolCount(0L);
		to.setClassCount(0L);
		to.setQuerysheetFile("");
		to.setFileLocation("");
		to.setSchoolNodes("");
		to.setClassNodes("");
		to.setStudentBioIds("");
		to.setUsername("");
		to.setEmail("");
		to.setGroupFile("");
		to.setCollationHierarchy("");
		to.setSelectedNodes("");
		to.setIstepPlus(false);
		to.setCustomerId("");
		to.setJobId(0L);
		to.setReportUrl("");
		to.setStatus("");
		to.setFileSize("");
		to.setPercentageDone("");
		to.setRequestedDate("");
		to.setCompletedDate("");
		to.setRequestType("");
		to.setLog("");
		to.setDownloadMode("");
		to.setDbStatus(false);
		to.setStartDate("");
		to.setEndDate("");
		return to;
	}

	public static GrtTO getGrtTO(TestParams testParams) {
		GrtTO to = new GrtTO();
		to.setL_TapeMode("");
		to.setL_Orgtstgpgm("");
		to.setL_CorporationorDioceseName("");
		to.setL_CorporationorDioceseNumber("");
		to.setL_SchoolName("");
		to.setL_SchoolNumber("");
		to.setL_TeacherName("");
		to.setL_TeacherElementNumberCTBUse("");
		to.setL_Grade("");
		to.setL_City("");
		to.setL_State("");
		to.setL_ISTEPTestName("");
		to.setL_ISTEPBook("");
		to.setL_ISTEPForm("");
		to.setL_TestDateMMDDYY("");
		to.setL_StudentLastName("");
		to.setL_StudentFirstName("");
		to.setL_StudentMiddleInitial("");
		to.setL_StudentsGender("");
		to.setL_BirthDateMMDDYY("");
		to.setL_ChronologicalAgeInMonths("");
		to.setL_Ethnicity("");
		to.setL_RaceAmericanIndianAlaskaNative("");
		to.setL_RaceAsian("");
		to.setL_RaceBlackorAfricanAmerican("");
		to.setL_RaceNativeHawaiianorOtherPacificIslander("");
		to.setL_RaceWhite("");
		to.setL_Filler1("");
		to.setL_StudentTestNumberAI("");
		to.setL_SpecialCodeJ("");
		to.setL_ResolvedEthnicityK("");
		to.setL_SpecialEducationL("");
		to.setL_ExceptionalityM("");
		to.setL_SocioEconomicStatusN("");
		to.setL_Section504O("");
		to.setL_EnglishLearnerELP("");
		to.setL_MigrantQ("");
		to.setL_LocaluseR("");
		to.setL_LocaluseS("");
		to.setL_LocaluseT("");
		to.setL_MatchUnmatchU("");
		to.setL_DuplicateV("");
		to.setL_CTBUseOnlyW("");
		to.setL_SpecialCodeX("");
		to.setL_SpecialCodeY("");
		to.setL_SpecialCodeZ("");
		to.setL_AccommodationsEla("");
		to.setL_AccommodationsMath("");
		to.setL_AccommodationsScience("");
		to.setL_AccommodationsSocialStudies("");
		to.setL_CorporationUseID("");
		to.setL_CustomerUse("");
		to.setL_Filler2("");
		to.setL_Filler3("");
		to.setL_ElaPFIndicator("");
		to.setL_MathPFIndicator("");
		to.setL_SciencePFIndicator("");
		to.setL_SocialStudiesPFIndicator("");
		to.setL_ElaNumberCorrect("");
		to.setL_MathNumberCorrect("");
		to.setL_ScienceNumberCorrect("");
		to.setL_SocialStudiesNumberCorrect("");
		to.setL_ElaScaleScore("");
		to.setL_MathScaleScore("");
		to.setL_ScienceScaleScore("");
		to.setL_SocialStudiesScaleScore("");
		to.setL_ElaScaleScoreSEM("");
		to.setL_MathScaleScoreSEM("");
		to.setL_ScienceScaleScoreSEM("");
		to.setL_SocialStudiesScaleScoreSEM("");
		to.setL_MasteryIndicator1("");
		to.setL_MasteryIndicator2("");
		to.setL_MasteryIndicator3("");
		to.setL_MasteryIndicator4("");
		to.setL_MasteryIndicator5("");
		to.setL_MasteryIndicator6("");
		to.setL_MasteryIndicator7("");
		to.setL_MasteryIndicator8("");
		to.setL_MasteryIndicator9("");
		to.setL_MasteryIndicator10("");
		to.setL_MasteryIndicator11("");
		to.setL_MasteryIndicator12("");
		to.setL_MasteryIndicator13("");
		to.setL_MasteryIndicator14("");
		to.setL_MasteryIndicator15("");
		to.setL_MasteryIndicator16("");
		to.setL_MasteryIndicator17("");
		to.setL_MasteryIndicator18("");
		to.setL_MasteryIndicator19("");
		to.setL_MasteryIndicator20("");
		to.setL_MasteryIndicator21("");
		to.setL_MasteryIndicator22("");
		to.setL_MasteryIndicator23("");
		to.setL_MasteryIndicator24("");
		to.setL_MasteryIndicator25("");
		to.setL_MasteryIndicator26("");
		to.setL_MasteryIndicator27("");
		to.setL_MasteryIndicator28("");
		to.setL_MasteryIndicator29("");
		to.setL_MasteryIndicator30("");
		to.setL_MasteryIndicator31("");
		to.setL_MasteryIndicator32("");
		to.setL_MasteryIndicator33("");
		to.setL_MasteryIndicator34("");
		to.setL_MasteryIndicator35("");
		to.setL_MasteryIndicator36("");
		to.setL_MasteryIndicator37("");
		to.setL_MasteryIndicator38("");
		to.setL_MasteryIndicator39("");
		to.setL_MasteryIndicator40("");
		to.setL_OPIIPI1("");
		to.setL_OPIIPI2("");
		to.setL_OPIIPI3("");
		to.setL_OPIIPI4("");
		to.setL_OPIIPI5("");
		to.setL_OPIIPI6("");
		to.setL_OPIIPI7("");
		to.setL_OPIIPI8("");
		to.setL_OPIIPI9("");
		to.setL_OPIIPI10("");
		to.setL_OPIIPI11("");
		to.setL_OPIIPI12("");
		to.setL_OPIIPI13("");
		to.setL_OPIIPI14("");
		to.setL_OPIIPI15("");
		to.setL_OPIIPI16("");
		to.setL_OPIIPI17("");
		to.setL_OPIIPI18("");
		to.setL_OPIIPI19("");
		to.setL_OPIIPI20("");
		to.setL_OPIIPI21("");
		to.setL_OPIIPI22("");
		to.setL_OPIIPI23("");
		to.setL_OPIIPI24("");
		to.setL_OPIIPI25("");
		to.setL_OPIIPI26("");
		to.setL_OPIIPI27("");
		to.setL_OPIIPI28("");
		to.setL_OPIIPI29("");
		to.setL_OPIIPI30("");
		to.setL_OPIIPI31("");
		to.setL_OPIIPI32("");
		to.setL_OPIIPI33("");
		to.setL_OPIIPI34("");
		to.setL_OPIIPI35("");
		to.setL_OPIIPI36("");
		to.setL_OPIIPI37("");
		to.setL_OPIIPI38("");
		to.setL_OPIIPI39("");
		to.setL_OPIIPI40("");
		to.setL_ELACRSession2ItemResponses("");
		to.setL_ELACRSession3ItemResponses("");
		to.setL_MathCRSession1ItemResponses("");
		to.setL_ScienceCRSession4ItemResponses("");
		to.setL_SocialStudiesCRSession4ItemResponses("");
		to.setL_Filler4("");
		to.setL_SchoolPersonnelNumberSPN1("");
		to.setL_SchoolPersonnelNumberSPN2("");
		to.setL_SchoolPersonnelNumberSPN3("");
		to.setL_SchoolPersonnelNumberSPN4("");
		to.setL_SchoolPersonnelNumberSPN5("");
		to.setL_Filler5("");
		to.setL_CGRComputerGeneratedResponsefrom("");
		to.setL_CTBUseOnlyAppliedSkillsPPImageID("");
		to.setL_CTBUseOnlyAppliedSkillsOASImageID("");
		to.setL_CTBUseOnlyMCPPImagingid("");
		to.setL_CTBUseOnlyMCOASimagingid("");
		to.setL_BarcodeIDAppliedSkills("");
		to.setL_BarcodeIDMultipleChoice("");
		to.setL_CTBUseOnlyTestForm("");
		to.setL_CTBUseOnlyMCBlankbookFlag("");
		to.setL_CTBUseOnlyTestFormAppliedSkillsFieldPilotTest("");
		to.setL_CTBUseOnlyTestFormMCFieldPilotTest("");
		to.setL_CTBUseOnlyFutureUseOASTestedIndicatorAppliedSkillstest("");
		to.setL_CTBUseOnlyOASTestedIndicatorMultipleChoiceTest("");
		to.setL_CTBUseOnlyStructureLevel("");
		to.setL_CTBUseOnlyElementNumber("");
		to.setL_ResolvedReportingStatusEla("");
		to.setL_ResolvedReportingStatusMath("");
		to.setL_ResolvedReportingStatusScience("");
		to.setL_ResolvedReportingStatusSocialStudies("");
		to.setL_OPIIPICut1("");
		to.setL_OPIIPICut2("");
		to.setL_OPIIPICut3("");
		to.setL_OPIIPICut4("");
		to.setL_OPIIPICut5("");
		to.setL_OPIIPICut6("");
		to.setL_OPIIPICut7("");
		to.setL_OPIIPICut8("");
		to.setL_OPIIPICut9("");
		to.setL_OPIIPICut10("");
		to.setL_OPIIPICut11("");
		to.setL_OPIIPICut12("");
		to.setL_OPIIPICut13("");
		to.setL_OPIIPICut14("");
		to.setL_OPIIPICut15("");
		to.setL_OPIIPICut16("");
		to.setL_OPIIPICut17("");
		to.setL_OPIIPICut18("");
		to.setL_OPIIPICut19("");
		to.setL_OPIIPICut20("");
		to.setL_OPIIPICut21("");
		to.setL_OPIIPICut22("");
		to.setL_OPIIPICut23("");
		to.setL_OPIIPICut24("");
		to.setL_OPIIPICut25("");
		to.setL_OPIIPICut26("");
		to.setL_OPIIPICut27("");
		to.setL_OPIIPICut28("");
		to.setL_OPIIPICut29("");
		to.setL_OPIIPICut30("");
		to.setL_OPIIPICut31("");
		to.setL_OPIIPICut32("");
		to.setL_OPIIPICut33("");
		to.setL_OPIIPICut34("");
		to.setL_OPIIPICut35("");
		to.setL_OPIIPICut36("");
		to.setL_OPIIPICut37("");
		to.setL_OPIIPICut38("");
		to.setL_OPIIPICut39("");
		to.setL_OPIIPICut40("");
		return to;
	}

	public static InvitationCodeTO getInvitationCodeTO(TestParams testParams) {
		InvitationCodeTO to = new InvitationCodeTO();
		to.setCorporationorDioceseName("");
		to.setCorporationorDioceseNumber("");
		to.setSchoolName("");
		to.setSchoolNumber("");
		to.setGrade("");
		to.setAdministrationName("");
		to.setISTEPInvitationCode("");
		to.setInvitationCodeExpirationDate("");
		to.setStudentLastName("");
		to.setStudentFirstName("");
		to.setStudentMiddleInitial("");
		to.setStudentsGender("");
		to.setBirthDate("");
		to.setStudentTestNumber("");
		to.setCorporationStudentID("");
		to.setCTBUSEBarcodeID("");
		to.setTeacherName("");
		to.setCTBUSEOrgtstgpgm("");
		to.setCTBUSETeacherElementNumber("");
		to.setCTBUSEStudentElementNumber("");
		return to;
	}

	public static JobTO getJobTO(TestParams testParams) {
		JobTO to = new JobTO();
		to.setJobid("");
		to.setPdfFileName("");
		to.setFileLocation("");
		to.setQuerySheetFile("");
		to.setUserId("");
		to.setJobDetails("");
		to.setStatus("");
		to.setPercentageDone("");
		to.setRequestDate("");
		to.setCompletionDate("");
		to.setFileSize("");
		to.setEmail("");
		to.setTestAdmin("");
		to.setCustomerId("");
		to.setGradeId("");
		to.setReportUrl("");
		return to;
	}

	public static ObjectValueTO getObjectValueTO(TestParams testParams) {
		ObjectValueTO to = new ObjectValueTO();
		to.setName("");
		to.setValue("");
		return to;
	}

	public static Map<String, String> helpTestGetDownloadData(TestParams testParams) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("type", "");
		paramMap.put("customerId", "0");
		paramMap.put("productId", "0");
		paramMap.put("testProgram", "0");
		paramMap.put("parentOrgNodeId", "0");
		paramMap.put("orgNodeId", "0");
		return paramMap;
	}

	public static Map<String, String> helpTestPopulateDistrictGrt(TestParams testParams) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("testProgram", "0");
		return paramMap;
	}

	public static Map<String, String> helpTestPopulateSchoolGrt(TestParams testParams) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("testProgram", "0");
		paramMap.put("districtId", "0");
		return paramMap;
	}

	public static Map<String, String> helpTestGetTabulerData(TestParams testParams) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("type", "");
		paramMap.put("product", "");
		paramMap.put("term", "");
		paramMap.put("year", "");
		paramMap.put("productId", "0");
		paramMap.put("testProgram", "0");
		paramMap.put("parentOrgNodeId", "0");
		paramMap.put("orgNodeId", "0");
		paramMap.put("customerId", "0");
		return paramMap;
	}
}
