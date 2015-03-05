package com.ctb.prism.inors.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.inors.constant.InorsDownloadConstants;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.transferobject.GrtTO;
import com.ctb.prism.inors.transferobject.InvitationCodeTO;
import com.ctb.prism.inors.transferobject.JobTO;
import com.ctb.prism.inors.transferobject.ObjectValueTO;
import com.ctb.prism.inors.util.InorsDownloadUtil;
import com.ctb.prism.test.InorsTestHelper;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class InorsServiceImplTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private IInorsService inorsService;

	TestParams testParams;

	@Before
	public void setUp() throws Exception {
		testParams = TestUtil.getTestParams();
		TestUtil.byPassLogin(testParams);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAsyncPDFDownload() {
		String jobId = "0";
		String contractName = testParams.getContractName();
		inorsService.asyncPDFDownload(jobId, contractName);
	}

	@Test(expected=org.springframework.jdbc.BadSqlGrammarException.class)
	public void testCreateJob() {
		BulkDownloadTO outputTO = inorsService.createJob(InorsTestHelper.getBulkDownloadTO(testParams));
		assertNotNull(outputTO);
	}

	@Test
	public void testGetJob() {
		String jobId = "0";
		String contractName = testParams.getContractName();
		JobTrackingTO outputTO = inorsService.getJob(jobId, contractName);
		assertNull(outputTO);
	}

	@Test
	public void testGetDownloadData() {
		Map<String, String> paramMap = InorsTestHelper.helpTestGetDownloadData(testParams);
		List<? extends BaseTO> downloadData = inorsService.getDownloadData(paramMap);
		assertNull(downloadData);
	}

	@Test
	public void testPopulateDistrictGrt() {
		Map<String, String> paramMap = InorsTestHelper.helpTestPopulateDistrictGrt(testParams);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> districtList = inorsService.populateDistrictGrt(paramMap);
		assertNotNull(districtList);
	}

	@Test
	public void testPopulateSchoolGrt() {
		Map<String, String> paramMap = InorsTestHelper.helpTestPopulateSchoolGrt(testParams);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> schoolList = inorsService.populateSchoolGrt(paramMap);
		assertNotNull(schoolList);
	}

	@Test
	public void testGetProductNameById() {
		String productName = inorsService.getProductNameById(0L);
		assertNull(productName);
	}

	@Test
	public void testGetTabulerData() {
		Map<String, String> paramMap = InorsTestHelper.helpTestGetTabulerData(testParams);
		String headers = testParams.getTabulerDataHeaders();
		String aliases = testParams.getTabulerDataAliases();
		ArrayList<String> headerList = InorsDownloadUtil.getRowDataLayout(headers);
		ArrayList<String> aliasList = InorsDownloadUtil.getRowDataLayout(aliases);
		ArrayList<ArrayList<String>> tabulerData = inorsService.getTabulerData(paramMap, aliasList, headerList);
		assertNull(tabulerData);
	}

	@Test
	public void testGetCurrentAdminYear() {
		String currentAdminYear = inorsService.getCurrentAdminYear();
		assertNotNull(currentAdminYear);
	}

	@Test
	public void testBulkDownloadTO() {
		BulkDownloadTO to = InorsTestHelper.getBulkDownloadTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getTestAdministration());
		assertNotNull(to.getTestProgram());
		assertNotNull(to.getCorp());
		assertNotNull(to.getSchool());
		assertNotNull(to.getGrade());
		assertNotNull(to.getOrgClass());
		assertNotNull(to.getFileName());
		assertNotNull(to.getDateRequested());
		assertNotNull(to.getStudentCount());
		assertNotNull(to.getSchoolCount());
		assertNotNull(to.getClassCount());
		assertNotNull(to.getQuerysheetFile());
		assertNotNull(to.getFileLocation());
		assertNotNull(to.getSchoolNodes());
		assertNotNull(to.getClassNodes());
		assertNotNull(to.getStudentBioIds());
		assertNotNull(to.getUsername());
		assertNotNull(to.getEmail());
		assertNotNull(to.getGroupFile());
		assertNotNull(to.getCollationHierarchy());
		assertNotNull(to.getSelectedNodes());
		assertNotNull(to.isIstepPlus());
		assertNotNull(to.getCustomerId());
		assertNotNull(to.getJobId());
		assertNotNull(to.getReportUrl());
		assertNotNull(to.getStatus());
		assertNotNull(to.getFileSize());
		assertNotNull(to.getPercentageDone());
		assertNotNull(to.getRequestedDate());
		assertNotNull(to.getCompletedDate());
		assertNotNull(to.getRequestType());
		assertNotNull(to.getLog());
		assertNotNull(to.getDownloadMode());
		assertNotNull(to.isDbStatus());
		assertNotNull(to.getStartDate());
		assertNotNull(to.getEndDate());
	}

	@Test
	public void testGrtTO() {
		GrtTO to = InorsTestHelper.getGrtTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getL_TapeMode());
		assertNotNull(to.getL_Orgtstgpgm());
		assertNotNull(to.getL_CorporationorDioceseName());
		assertNotNull(to.getL_CorporationorDioceseNumber());
		assertNotNull(to.getL_SchoolName());
		assertNotNull(to.getL_SchoolNumber());
		assertNotNull(to.getL_TeacherName());
		assertNotNull(to.getL_TeacherElementNumberCTBUse());
		assertNotNull(to.getL_Grade());
		assertNotNull(to.getL_City());
		assertNotNull(to.getL_State());
		assertNotNull(to.getL_ISTEPTestName());
		assertNotNull(to.getL_ISTEPBook());
		assertNotNull(to.getL_ISTEPForm());
		assertNotNull(to.getL_TestDateMMDDYY());
		assertNotNull(to.getL_StudentLastName());
		assertNotNull(to.getL_StudentFirstName());
		assertNotNull(to.getL_StudentMiddleInitial());
		assertNotNull(to.getL_StudentsGender());
		assertNotNull(to.getL_BirthDateMMDDYY());
		assertNotNull(to.getL_ChronologicalAgeInMonths());
		assertNotNull(to.getL_Ethnicity());
		assertNotNull(to.getL_RaceAmericanIndianAlaskaNative());
		assertNotNull(to.getL_RaceAsian());
		assertNotNull(to.getL_RaceBlackorAfricanAmerican());
		assertNotNull(to.getL_RaceNativeHawaiianorOtherPacificIslander());
		assertNotNull(to.getL_RaceWhite());
		assertNotNull(to.getL_Filler1());
		assertNotNull(to.getL_StudentTestNumberAI());
		assertNotNull(to.getL_SpecialCodeJ());
		assertNotNull(to.getL_ResolvedEthnicityK());
		assertNotNull(to.getL_SpecialEducationL());
		assertNotNull(to.getL_ExceptionalityM());
		assertNotNull(to.getL_SocioEconomicStatusN());
		assertNotNull(to.getL_Section504O());
		assertNotNull(to.getL_EnglishLearnerELP());
		assertNotNull(to.getL_MigrantQ());
		assertNotNull(to.getL_LocaluseR());
		assertNotNull(to.getL_LocaluseS());
		assertNotNull(to.getL_LocaluseT());
		assertNotNull(to.getL_MatchUnmatchU());
		assertNotNull(to.getL_DuplicateV());
		assertNotNull(to.getL_CTBUseOnlyW());
		assertNotNull(to.getL_SpecialCodeX());
		assertNotNull(to.getL_SpecialCodeY());
		assertNotNull(to.getL_SpecialCodeZ());
		assertNotNull(to.getL_AccommodationsEla());
		assertNotNull(to.getL_AccommodationsMath());
		assertNotNull(to.getL_AccommodationsScience());
		assertNotNull(to.getL_AccommodationsSocialStudies());
		assertNotNull(to.getL_CorporationUseID());
		assertNotNull(to.getL_CustomerUse());
		assertNotNull(to.getL_Filler2());
		assertNotNull(to.getL_Filler3());
		assertNotNull(to.getL_ElaPFIndicator());
		assertNotNull(to.getL_MathPFIndicator());
		assertNotNull(to.getL_SciencePFIndicator());
		assertNotNull(to.getL_SocialStudiesPFIndicator());
		assertNotNull(to.getL_ElaNumberCorrect());
		assertNotNull(to.getL_MathNumberCorrect());
		assertNotNull(to.getL_ScienceNumberCorrect());
		assertNotNull(to.getL_SocialStudiesNumberCorrect());
		assertNotNull(to.getL_ElaScaleScore());
		assertNotNull(to.getL_MathScaleScore());
		assertNotNull(to.getL_ScienceScaleScore());
		assertNotNull(to.getL_SocialStudiesScaleScore());
		assertNotNull(to.getL_ElaScaleScoreSEM());
		assertNotNull(to.getL_MathScaleScoreSEM());
		assertNotNull(to.getL_ScienceScaleScoreSEM());
		assertNotNull(to.getL_SocialStudiesScaleScoreSEM());
		assertNotNull(to.getL_MasteryIndicator1());
		assertNotNull(to.getL_MasteryIndicator2());
		assertNotNull(to.getL_MasteryIndicator3());
		assertNotNull(to.getL_MasteryIndicator4());
		assertNotNull(to.getL_MasteryIndicator5());
		assertNotNull(to.getL_MasteryIndicator6());
		assertNotNull(to.getL_MasteryIndicator7());
		assertNotNull(to.getL_MasteryIndicator8());
		assertNotNull(to.getL_MasteryIndicator9());
		assertNotNull(to.getL_MasteryIndicator10());
		assertNotNull(to.getL_MasteryIndicator11());
		assertNotNull(to.getL_MasteryIndicator12());
		assertNotNull(to.getL_MasteryIndicator13());
		assertNotNull(to.getL_MasteryIndicator14());
		assertNotNull(to.getL_MasteryIndicator15());
		assertNotNull(to.getL_MasteryIndicator16());
		assertNotNull(to.getL_MasteryIndicator17());
		assertNotNull(to.getL_MasteryIndicator18());
		assertNotNull(to.getL_MasteryIndicator19());
		assertNotNull(to.getL_MasteryIndicator20());
		assertNotNull(to.getL_MasteryIndicator21());
		assertNotNull(to.getL_MasteryIndicator22());
		assertNotNull(to.getL_MasteryIndicator23());
		assertNotNull(to.getL_MasteryIndicator24());
		assertNotNull(to.getL_MasteryIndicator25());
		assertNotNull(to.getL_MasteryIndicator26());
		assertNotNull(to.getL_MasteryIndicator27());
		assertNotNull(to.getL_MasteryIndicator28());
		assertNotNull(to.getL_MasteryIndicator29());
		assertNotNull(to.getL_MasteryIndicator30());
		assertNotNull(to.getL_MasteryIndicator31());
		assertNotNull(to.getL_MasteryIndicator32());
		assertNotNull(to.getL_MasteryIndicator33());
		assertNotNull(to.getL_MasteryIndicator34());
		assertNotNull(to.getL_MasteryIndicator35());
		assertNotNull(to.getL_MasteryIndicator36());
		assertNotNull(to.getL_MasteryIndicator37());
		assertNotNull(to.getL_MasteryIndicator38());
		assertNotNull(to.getL_MasteryIndicator39());
		assertNotNull(to.getL_MasteryIndicator40());
		assertNotNull(to.getL_OPIIPI1());
		assertNotNull(to.getL_OPIIPI2());
		assertNotNull(to.getL_OPIIPI3());
		assertNotNull(to.getL_OPIIPI4());
		assertNotNull(to.getL_OPIIPI5());
		assertNotNull(to.getL_OPIIPI6());
		assertNotNull(to.getL_OPIIPI7());
		assertNotNull(to.getL_OPIIPI8());
		assertNotNull(to.getL_OPIIPI9());
		assertNotNull(to.getL_OPIIPI10());
		assertNotNull(to.getL_OPIIPI11());
		assertNotNull(to.getL_OPIIPI12());
		assertNotNull(to.getL_OPIIPI13());
		assertNotNull(to.getL_OPIIPI14());
		assertNotNull(to.getL_OPIIPI15());
		assertNotNull(to.getL_OPIIPI16());
		assertNotNull(to.getL_OPIIPI17());
		assertNotNull(to.getL_OPIIPI18());
		assertNotNull(to.getL_OPIIPI19());
		assertNotNull(to.getL_OPIIPI20());
		assertNotNull(to.getL_OPIIPI21());
		assertNotNull(to.getL_OPIIPI22());
		assertNotNull(to.getL_OPIIPI23());
		assertNotNull(to.getL_OPIIPI24());
		assertNotNull(to.getL_OPIIPI25());
		assertNotNull(to.getL_OPIIPI26());
		assertNotNull(to.getL_OPIIPI27());
		assertNotNull(to.getL_OPIIPI28());
		assertNotNull(to.getL_OPIIPI29());
		assertNotNull(to.getL_OPIIPI30());
		assertNotNull(to.getL_OPIIPI31());
		assertNotNull(to.getL_OPIIPI32());
		assertNotNull(to.getL_OPIIPI33());
		assertNotNull(to.getL_OPIIPI34());
		assertNotNull(to.getL_OPIIPI35());
		assertNotNull(to.getL_OPIIPI36());
		assertNotNull(to.getL_OPIIPI37());
		assertNotNull(to.getL_OPIIPI38());
		assertNotNull(to.getL_OPIIPI39());
		assertNotNull(to.getL_OPIIPI40());
		assertNotNull(to.getL_ELACRSession2ItemResponses());
		assertNotNull(to.getL_ELACRSession3ItemResponses());
		assertNotNull(to.getL_MathCRSession1ItemResponses());
		assertNotNull(to.getL_ScienceCRSession4ItemResponses());
		assertNotNull(to.getL_SocialStudiesCRSession4ItemResponses());
		assertNotNull(to.getL_Filler4());
		assertNotNull(to.getL_SchoolPersonnelNumberSPN1());
		assertNotNull(to.getL_SchoolPersonnelNumberSPN2());
		assertNotNull(to.getL_SchoolPersonnelNumberSPN3());
		assertNotNull(to.getL_SchoolPersonnelNumberSPN4());
		assertNotNull(to.getL_SchoolPersonnelNumberSPN5());
		assertNotNull(to.getL_Filler5());
		assertNotNull(to.getL_CGRComputerGeneratedResponsefrom());
		assertNotNull(to.getL_CTBUseOnlyAppliedSkillsPPImageID());
		assertNotNull(to.getL_CTBUseOnlyAppliedSkillsOASImageID());
		assertNotNull(to.getL_CTBUseOnlyMCPPImagingid());
		assertNotNull(to.getL_CTBUseOnlyMCOASimagingid());
		assertNotNull(to.getL_BarcodeIDAppliedSkills());
		assertNotNull(to.getL_BarcodeIDMultipleChoice());
		assertNotNull(to.getL_CTBUseOnlyTestForm());
		assertNotNull(to.getL_CTBUseOnlyMCBlankbookFlag());
		assertNotNull(to.getL_CTBUseOnlyTestFormAppliedSkillsFieldPilotTest());
		assertNotNull(to.getL_CTBUseOnlyTestFormMCFieldPilotTest());
		assertNotNull(to.getL_CTBUseOnlyFutureUseOASTestedIndicatorAppliedSkillstest());
		assertNotNull(to.getL_CTBUseOnlyOASTestedIndicatorMultipleChoiceTest());
		assertNotNull(to.getL_CTBUseOnlyStructureLevel());
		assertNotNull(to.getL_CTBUseOnlyElementNumber());
		assertNotNull(to.getL_ResolvedReportingStatusEla());
		assertNotNull(to.getL_ResolvedReportingStatusMath());
		assertNotNull(to.getL_ResolvedReportingStatusScience());
		assertNotNull(to.getL_ResolvedReportingStatusSocialStudies());
		assertNotNull(to.getL_OPIIPICut1());
		assertNotNull(to.getL_OPIIPICut2());
		assertNotNull(to.getL_OPIIPICut3());
		assertNotNull(to.getL_OPIIPICut4());
		assertNotNull(to.getL_OPIIPICut5());
		assertNotNull(to.getL_OPIIPICut6());
		assertNotNull(to.getL_OPIIPICut7());
		assertNotNull(to.getL_OPIIPICut8());
		assertNotNull(to.getL_OPIIPICut9());
		assertNotNull(to.getL_OPIIPICut10());
		assertNotNull(to.getL_OPIIPICut11());
		assertNotNull(to.getL_OPIIPICut12());
		assertNotNull(to.getL_OPIIPICut13());
		assertNotNull(to.getL_OPIIPICut14());
		assertNotNull(to.getL_OPIIPICut15());
		assertNotNull(to.getL_OPIIPICut16());
		assertNotNull(to.getL_OPIIPICut17());
		assertNotNull(to.getL_OPIIPICut18());
		assertNotNull(to.getL_OPIIPICut19());
		assertNotNull(to.getL_OPIIPICut20());
		assertNotNull(to.getL_OPIIPICut21());
		assertNotNull(to.getL_OPIIPICut22());
		assertNotNull(to.getL_OPIIPICut23());
		assertNotNull(to.getL_OPIIPICut24());
		assertNotNull(to.getL_OPIIPICut25());
		assertNotNull(to.getL_OPIIPICut26());
		assertNotNull(to.getL_OPIIPICut27());
		assertNotNull(to.getL_OPIIPICut28());
		assertNotNull(to.getL_OPIIPICut29());
		assertNotNull(to.getL_OPIIPICut30());
		assertNotNull(to.getL_OPIIPICut31());
		assertNotNull(to.getL_OPIIPICut32());
		assertNotNull(to.getL_OPIIPICut33());
		assertNotNull(to.getL_OPIIPICut34());
		assertNotNull(to.getL_OPIIPICut35());
		assertNotNull(to.getL_OPIIPICut36());
		assertNotNull(to.getL_OPIIPICut37());
		assertNotNull(to.getL_OPIIPICut38());
		assertNotNull(to.getL_OPIIPICut39());
		assertNotNull(to.getL_OPIIPICut40());
	}

	@Test
	public void testInvitationCodeTO() {
		InvitationCodeTO to = InorsTestHelper.getInvitationCodeTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getCorporationorDioceseName());
		assertNotNull(to.getCorporationorDioceseNumber());
		assertNotNull(to.getSchoolName());
		assertNotNull(to.getSchoolNumber());
		assertNotNull(to.getGrade());
		assertNotNull(to.getAdministrationName());
		assertNotNull(to.getISTEPInvitationCode());
		assertNotNull(to.getInvitationCodeExpirationDate());
		assertNotNull(to.getStudentLastName());
		assertNotNull(to.getStudentFirstName());
		assertNotNull(to.getStudentMiddleInitial());
		assertNotNull(to.getStudentsGender());
		assertNotNull(to.getBirthDate());
		assertNotNull(to.getStudentTestNumber());
		assertNotNull(to.getCorporationStudentID());
		assertNotNull(to.getCTBUSEBarcodeID());
		assertNotNull(to.getTeacherName());
		assertNotNull(to.getCTBUSEOrgtstgpgm());
		assertNotNull(to.getCTBUSETeacherElementNumber());
		assertNotNull(to.getCTBUSEStudentElementNumber());
	}

	@Test
	public void testJobTO() {
		JobTO to = InorsTestHelper.getJobTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getJobid());
		assertNotNull(to.getPdfFileName());
		assertNotNull(to.getFileLocation());
		assertNotNull(to.getQuerySheetFile());
		assertNotNull(to.getUserId());
		assertNotNull(to.getJobDetails());
		assertNotNull(to.getStatus());
		assertNotNull(to.getPercentageDone());
		assertNotNull(to.getRequestDate());
		assertNotNull(to.getCompletionDate());
		assertNotNull(to.getFileSize());
		assertNotNull(to.getEmail());
		assertNotNull(to.getTestAdmin());
		assertNotNull(to.getCustomerId());
		assertNotNull(to.getGradeId());
		assertNotNull(to.getReportUrl());
	}

	@Test
	public void testObjectValueTO() {
		ObjectValueTO to = InorsTestHelper.getObjectValueTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getName());
		assertNotNull(to.getValue());
	}

	@Test
	public void testInorsDownloadConstants() {
		InorsDownloadConstants inorsDownloadConstants = new InorsDownloadConstants();
		assertNotNull(inorsDownloadConstants);
	}

}
