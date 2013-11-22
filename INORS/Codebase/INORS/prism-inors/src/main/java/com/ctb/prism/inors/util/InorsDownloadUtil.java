/**
 * 
 */
package com.ctb.prism.inors.util;

import java.io.StringWriter;
import java.util.List;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.beanio.builder.DelimitedParserBuilder;
import org.beanio.builder.StreamBuilder;
import org.beanio.stream.RecordIOException;

import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.inors.transferobject.GrtTO;
import com.ctb.prism.inors.transferobject.InvitationCodeTO;

/**
 * @author 165505
 * 
 */
public class InorsDownloadUtil {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(InorsDownloadUtil.class.getName());

	/**
	 * This method does not create any file in disk. It converts a List of
	 * InvitationCodeTO to a byte array.
	 * 
	 * @param icList
	 * @param delimiter
	 * @return
	 */
	public static byte[] createICByteArray(final List<InvitationCodeTO> icList, final char delimiter) {
		icList.add(0, getInvitationCodeTOHeader());
		StreamFactory factory = StreamFactory.newInstance();
		StreamBuilder builder = new StreamBuilder("IC").format("delimited")
				.parser(new DelimitedParserBuilder(delimiter))
				.addRecord(InvitationCodeTO.class);
		factory.define(builder);
		StringWriter stringWriter = new StringWriter();
		BeanWriter out = factory.createWriter("IC", stringWriter);
		try {
			for (InvitationCodeTO ic : icList) {
				out.write(ic);
			}
			out.flush();
			out.close();
			logger.log(IAppLogger.INFO, "IC Byte Array Created Successfully");
		} catch (RecordIOException e) {
			logger.log(IAppLogger.ERROR, "", e);
			e.printStackTrace();
		}
		return stringWriter.getBuffer().toString().getBytes();
	}

	/**
	 * This method does not create any file in disk. It converts a List of GrtTO
	 * to a byte array.
	 * 
	 * @param grtList
	 * @param delimiter
	 * @return
	 */
	public static byte[] createGRTByteArray(final List<GrtTO> grtList, final char delimiter) {
		grtList.add(0, getGRTTOHeader());
		StreamFactory factory = StreamFactory.newInstance();
		StreamBuilder builder = new StreamBuilder("GRT").format("delimited")
				.parser(new DelimitedParserBuilder(delimiter))
				.addRecord(GrtTO.class);
		factory.define(builder);
		StringWriter stringWriter = new StringWriter();
		BeanWriter out = factory.createWriter("GRT", stringWriter);
		try {
			for (GrtTO grt : grtList) {
				out.write(grt);
			}
			out.flush();
			out.close();
			logger.log(IAppLogger.INFO, "GRT Byte Array Created Successfully");
		} catch (RecordIOException e) {
			logger.log(IAppLogger.ERROR, "", e);
			e.printStackTrace();
		}
		return stringWriter.getBuffer().toString().getBytes();
	}

	/**
	 * Method to create TO representation of the header record.
	 * 
	 * @return
	 */
	private static InvitationCodeTO getInvitationCodeTOHeader() {
		InvitationCodeTO header = new InvitationCodeTO();
		header.setCorporationorDioceseName("Corporation or Diocese Name");
		header.setCorporationorDioceseNumber("Corporation or Diocese Number");
		header.setSchoolName("School Name");
		header.setSchoolNumber("School Number");
		header.setGrade("Grade");
		header.setAdministrationName("Administration Name");
		header.setiSTEPInvitationCode("ISTEP Invitation Code");
		header.setInvitationCodeExpirationDate("Invitation Code Expiration Date (MMDDYY)");
		header.setStudentLastName("Student Last Name");
		header.setStudentFirstName("Student First Name");
		header.setStudentMiddleInitial("Student Middle Initial");
		header.setStudentsGender("Student's Gender");
		header.setBirthDate("Birth Date (MMDDYY)");
		header.setStudentTestNumber("Student Test Number");
		header.setCorporationStudentID("Corporation Student ID");
		header.setcTBUSEBarcodeID("CTB USE: Barcode ID (PreID)");
		header.setTeacherName("Teacher Name");
		header.setcTBUSEOrgtstgpgm("CTB USE: Org-tstg-pgm");
		header.setcTBUSETeacherElementNumber("CTB USE: Teacher Element Number");
		header.setcTBUSEStudentElementNumber("CTB USE: Student Element Number");
		return header;
	}

	/**
	 * Returns mock TO
	 * 
	 * @return
	 */
	public static InvitationCodeTO getMockInvitationCodeTO() {
		InvitationCodeTO to = new InvitationCodeTO();
		to.setCorporationorDioceseName(CustomStringUtil.wrap("ADAMS CENTRAL", '"'));
		to.setCorporationorDioceseNumber(CustomStringUtil.wrap("0015", '"'));
		to.setSchoolName(CustomStringUtil.wrap("ADAMS C ES M", '"'));
		to.setSchoolNumber(CustomStringUtil.wrap("0020", '"'));
		to.setGrade(CustomStringUtil.wrap("3", '"'));
		to.setAdministrationName(CustomStringUtil.wrap("ISTEPS13", '"'));
		to.setiSTEPInvitationCode(CustomStringUtil.wrap("36JP-YECJ-RTJU-8GN6", '"'));
		to.setInvitationCodeExpirationDate(CustomStringUtil.wrap("050614", '"'));
		to.setStudentLastName(CustomStringUtil.wrap("ABELL", '"'));
		to.setStudentFirstName(CustomStringUtil.wrap("ASHLYN", '"'));
		to.setStudentMiddleInitial(CustomStringUtil.wrap("", '"'));
		to.setStudentsGender(CustomStringUtil.wrap("F", '"'));
		to.setBirthDate(CustomStringUtil.wrap("113003", '"'));
		to.setStudentTestNumber(CustomStringUtil.wrap("002010001", '"'));
		to.setCorporationStudentID(CustomStringUtil.wrap("10008", '"'));
		to.setcTBUSEBarcodeID(CustomStringUtil.wrap("21944483", '"'));
		to.setTeacherName(CustomStringUtil.wrap("STEINER", '"'));
		to.setcTBUSEOrgtstgpgm(CustomStringUtil.wrap("M013883003", '"'));
		to.setcTBUSETeacherElementNumber(CustomStringUtil.wrap("0016973", '"'));
		to.setcTBUSEStudentElementNumber(CustomStringUtil.wrap("0482387", '"'));
		return to;
	}

	/**
	 * Method to create TO representation of the header record.
	 * 
	 * @return
	 */
	public static GrtTO getGRTTOHeader() {
		GrtTO header = new GrtTO();
		header.setL_TapeMode("Tape Mode");
		header.setL_Orgtstgpgm("Org-tstg-pgm");
		header.setL_CorporationorDioceseName("Corporation or Diocese Name");
		header.setL_CorporationorDioceseNumber("Corporation or Diocese Number");
		header.setL_SchoolName("School Name");
		header.setL_SchoolNumber("School Number");
		header.setL_TeacherName("Teacher Name");
		header.setL_TeacherElementNumberCTBUse("Teacher Element Number (CTB Use)");
		header.setL_Grade("Grade");
		header.setL_City("City");
		header.setL_State("State");
		header.setL_ISTEPTestName("ISTEP Test Name");
		header.setL_ISTEPBook("ISTEP Book #");
		header.setL_ISTEPForm("ISTEP Form");
		header.setL_TestDateMMDDYY("Test Date (MMDDYY)");
		header.setL_StudentLastName("Student Last Name");
		header.setL_StudentFirstName("Student First Name");
		header.setL_StudentMiddleInitial("Student Middle Initial");
		header.setL_StudentsGender("Student's Gender");
		header.setL_BirthDateMMDDYY("Birth Date (MMDDYY)");
		header.setL_ChronologicalAgeInMonths("Chronological Age (In Months)");
		header.setL_Ethnicity("Ethnicity");
		header.setL_RaceAmericanIndianAlaskaNative("Race: American Indian/Alaska Native");
		header.setL_RaceAsian("Race: Asian");
		header.setL_RaceBlackorAfricanAmerican("Race: Black or African American");
		header.setL_RaceNativeHawaiianorOtherPacificIslander("Race: Native Hawaiian or Other Pacific Islander");
		header.setL_RaceWhite("Race: White");
		header.setL_Filler1("Filler 1");
		header.setL_StudentTestNumberAI("Student Test Number: A-I");
		header.setL_SpecialCodeJ("Special Code:  J");
		header.setL_ResolvedEthnicityK("Resolved Ethnicity: K");
		header.setL_SpecialEducationL("Special Education: L");
		header.setL_ExceptionalityM("Exceptionality: M");
		header.setL_SocioEconomicStatusN("Socio-Economic Status: N");
		header.setL_Section504O("Section 504: O");
		header.setL_EnglishLearnerELP("English Learner (EL) P");
		header.setL_MigrantQ("Migrant: Q");
		header.setL_LocaluseR("Local use: R");
		header.setL_LocaluseS("Local use: S");
		header.setL_LocaluseT("Local use: T");
		header.setL_MatchUnmatchU("Match/Unmatch: U");
		header.setL_DuplicateV("Duplicate: V");
		header.setL_CTBUseOnlyW("CTB Use Only: W");
		header.setL_SpecialCodeX("Special Code:  X");
		header.setL_SpecialCodeY("Special Code:  Y");
		header.setL_SpecialCodeZ("Special Code:  Z");
		header.setL_AccommodationsEla("Accommodations E/la");
		header.setL_AccommodationsMath("Accommodations Math");
		header.setL_AccommodationsScience("Accommodations Science");
		header.setL_AccommodationsSocialStudies("Accommodations Social Studies");
		header.setL_CorporationUseID("Corporation Use - ID");
		header.setL_CustomerUse("Customer Use");
		header.setL_Filler2("Filler 2");
		header.setL_Filler3("Filler 3");
		header.setL_ElaPFIndicator("E/la P/F Indicator");
		header.setL_MathPFIndicator("Math P/F Indicator");
		header.setL_SciencePFIndicator("Science P/F Indicator");
		header.setL_SocialStudiesPFIndicator("Social Studies P/F Indicator");
		header.setL_ElaNumberCorrect("E/la Number Correct");
		header.setL_MathNumberCorrect("Math Number Correct");
		header.setL_ScienceNumberCorrect("Science Number Correct");
		header.setL_SocialStudiesNumberCorrect("Social Studies Number Correct");
		header.setL_ElaScaleScore("E/la Scale Score");
		header.setL_MathScaleScore("Math Scale Score");
		header.setL_ScienceScaleScore("Science Scale Score");
		header.setL_SocialStudiesScaleScore("Social Studies Scale Score");
		header.setL_ElaScaleScoreSEM("E/la Scale Score SEM");
		header.setL_MathScaleScoreSEM("Math Scale Score SEM");
		header.setL_ScienceScaleScoreSEM("Science Scale Score SEM");
		header.setL_SocialStudiesScaleScoreSEM("Social Studies Scale Score SEM");
		header.setL_MasteryIndicator1("Mastery Indicator 1");
		header.setL_MasteryIndicator2("Mastery Indicator 2");
		header.setL_MasteryIndicator3("Mastery Indicator 3");
		header.setL_MasteryIndicator4("Mastery Indicator 4");
		header.setL_MasteryIndicator5("Mastery Indicator 5");
		header.setL_MasteryIndicator6("Mastery Indicator 6");
		header.setL_MasteryIndicator7("Mastery Indicator 7");
		header.setL_MasteryIndicator8("Mastery Indicator 8");
		header.setL_MasteryIndicator9("Mastery Indicator 9");
		header.setL_MasteryIndicator10("Mastery Indicator 10");
		header.setL_MasteryIndicator11("Mastery Indicator 11");
		header.setL_MasteryIndicator12("Mastery Indicator 12");
		header.setL_MasteryIndicator13("Mastery Indicator 13");
		header.setL_MasteryIndicator14("Mastery Indicator 14");
		header.setL_MasteryIndicator15("Mastery Indicator 15");
		header.setL_MasteryIndicator16("Mastery Indicator 16");
		header.setL_MasteryIndicator17("Mastery Indicator 17");
		header.setL_MasteryIndicator18("Mastery Indicator 18");
		header.setL_MasteryIndicator19("Mastery Indicator 19");
		header.setL_MasteryIndicator20("Mastery Indicator 20");
		header.setL_MasteryIndicator21("Mastery Indicator 21");
		header.setL_MasteryIndicator22("Mastery Indicator 22");
		header.setL_MasteryIndicator23("Mastery Indicator 23");
		header.setL_MasteryIndicator24("Mastery Indicator 24");
		header.setL_MasteryIndicator25("Mastery Indicator 25");
		header.setL_MasteryIndicator26("Mastery Indicator 26");
		header.setL_MasteryIndicator27("Mastery Indicator 27");
		header.setL_MasteryIndicator28("Mastery Indicator 28");
		header.setL_MasteryIndicator29("Mastery Indicator 29");
		header.setL_MasteryIndicator30("Mastery Indicator 30");
		header.setL_MasteryIndicator31("Mastery Indicator 31");
		header.setL_MasteryIndicator32("Mastery Indicator 32");
		header.setL_MasteryIndicator33("Mastery Indicator 33");
		header.setL_MasteryIndicator34("Mastery Indicator 34");
		header.setL_MasteryIndicator35("Mastery Indicator 35");
		header.setL_MasteryIndicator36("Mastery Indicator 36");
		header.setL_MasteryIndicator37("Mastery Indicator 37");
		header.setL_MasteryIndicator38("Mastery Indicator 38");
		header.setL_MasteryIndicator39("Mastery Indicator 39");
		header.setL_MasteryIndicator40("Mastery Indicator 40");
		header.setL_OPIIPI1("OPI/IPI 1");
		header.setL_OPIIPI2("OPI/IPI 2");
		header.setL_OPIIPI3("OPI/IPI 3");
		header.setL_OPIIPI4("OPI/IPI 4");
		header.setL_OPIIPI5("OPI/IPI 5");
		header.setL_OPIIPI6("OPI/IPI 6");
		header.setL_OPIIPI7("OPI/IPI 7");
		header.setL_OPIIPI8("OPI/IPI 8");
		header.setL_OPIIPI9("OPI/IPI 9");
		header.setL_OPIIPI10("OPI/IPI 10");
		header.setL_OPIIPI11("OPI/IPI 11");
		header.setL_OPIIPI12("OPI/IPI 12");
		header.setL_OPIIPI13("OPI/IPI 13");
		header.setL_OPIIPI14("OPI/IPI 14");
		header.setL_OPIIPI15("OPI/IPI 15");
		header.setL_OPIIPI16("OPI/IPI 16");
		header.setL_OPIIPI17("OPI/IPI 17");
		header.setL_OPIIPI18("OPI/IPI 18");
		header.setL_OPIIPI19("OPI/IPI 19");
		header.setL_OPIIPI20("OPI/IPI 20");
		header.setL_OPIIPI21("OPI/IPI 21");
		header.setL_OPIIPI22("OPI/IPI 22");
		header.setL_OPIIPI23("OPI/IPI 23");
		header.setL_OPIIPI24("OPI/IPI 24");
		header.setL_OPIIPI25("OPI/IPI 25");
		header.setL_OPIIPI26("OPI/IPI 26");
		header.setL_OPIIPI27("OPI/IPI 27");
		header.setL_OPIIPI28("OPI/IPI 28");
		header.setL_OPIIPI29("OPI/IPI 29");
		header.setL_OPIIPI30("OPI/IPI 30");
		header.setL_OPIIPI31("OPI/IPI 31");
		header.setL_OPIIPI32("OPI/IPI 32");
		header.setL_OPIIPI33("OPI/IPI 33");
		header.setL_OPIIPI34("OPI/IPI 34");
		header.setL_OPIIPI35("OPI/IPI 35");
		header.setL_OPIIPI36("OPI/IPI 36");
		header.setL_OPIIPI37("OPI/IPI 37");
		header.setL_OPIIPI38("OPI/IPI 38");
		header.setL_OPIIPI39("OPI/IPI 39");
		header.setL_OPIIPI40("OPI/IPI 40");
		header.setL_ELACRSession2ItemResponses("E/LA CR Session 2 Item Responses");
		header.setL_ELACRSession3ItemResponses("E/LA CR Session 3 Item Responses");
		header.setL_MathCRSession1ItemResponses("Math CR Session 1 Item Responses");
		header.setL_ScienceCRSession4ItemResponses("Science CR Session 4 Item Responses");
		header.setL_SocialStudiesCRSession4ItemResponses("Social Studies CR Session 4 Item Responses");
		header.setL_Filler4("Filler 4");
		header.setL_SchoolPersonnelNumberSPN1("School Personnel Number (SPN) #1");
		header.setL_SchoolPersonnelNumberSPN2("School Personnel Number (SPN) #2");
		header.setL_SchoolPersonnelNumberSPN3("School Personnel Number (SPN) #3");
		header.setL_SchoolPersonnelNumberSPN4("School Personnel Number (SPN) #4");
		header.setL_SchoolPersonnelNumberSPN5("School Personnel Number (SPN) #5");
		header.setL_Filler5("Filler 5");
		header.setL_CGRComputerGeneratedResponsefrom("CGR (Computer Generated Response) from Ans. Doc.");
		header.setL_CTBUseOnlyAppliedSkillsPPImageID("CTB Use Only: Applied Skills P/P Image ID");
		header.setL_CTBUseOnlyAppliedSkillsOASImageID("CTB Use Only: Future USE/CTB Use Only: Applied Skills OAS Image ID");
		header.setL_CTBUseOnlyMCPPImagingid("CTB Use Only: MC P/P Imaging id");
		header.setL_CTBUseOnlyMCOASimagingid("CTB Use Only: MC OAS imaging id");
		header.setL_BarcodeIDAppliedSkills("Barcode ID: Applied Skills");
		header.setL_BarcodeIDMultipleChoice("Barcode ID: Multiple Choice");
		header.setL_CTBUseOnlyTestForm("CTB Use Only: Test Form set to default Flag");
		header.setL_CTBUseOnlyMCBlankbookFlag("CTB Use Only: MC Blank book Flag");
		header.setL_CTBUseOnlyTestFormAppliedSkillsFieldPilotTest("CTB Use Only: Test Form (Applied Skills Field Test)");
		header.setL_CTBUseOnlyTestFormMCFieldPilotTest("CTB Use Only: Test Form (MC Field Test)");
		header.setL_CTBUseOnlyFutureUseOASTestedIndicatorAppliedSkillstest("CTB Use Only: Future Use (OAS Tested Indicator Applied Skills test)");
		header.setL_CTBUseOnlyOASTestedIndicatorMultipleChoiceTest("CTB Use Only: OAS Tested Indicator Multiple Choice Test");
		header.setL_CTBUseOnlyStructureLevel("CTB Use Only: Structure Level");
		header.setL_CTBUseOnlyElementNumber("CTB Use Only: Element Number");
		header.setL_ResolvedReportingStatusEla("Resolved Reporting Status E/la");
		header.setL_ResolvedReportingStatusMath("Resolved Reporting Status Math");
		header.setL_ResolvedReportingStatusScience("Resolved Reporting Status Science");
		header.setL_ResolvedReportingStatusSocialStudies("Resolved Reporting Status Social Studies");
		header.setL_OPIIPICut1("OPI/IPI CUT 1");
		header.setL_OPIIPICut2("OPI/IPI CUT 2");
		header.setL_OPIIPICut3("OPI/IPI CUT 3");
		header.setL_OPIIPICut4("OPI/IPI CUT 4");
		header.setL_OPIIPICut5("OPI/IPI CUT 5");
		header.setL_OPIIPICut6("OPI/IPI CUT 6");
		header.setL_OPIIPICut7("OPI/IPI CUT 7");
		header.setL_OPIIPICut8("OPI/IPI CUT 8");
		header.setL_OPIIPICut9("OPI/IPI CUT 9");
		header.setL_OPIIPICut10("OPI/IPI CUT 10");
		header.setL_OPIIPICut11("OPI/IPI CUT 11");
		header.setL_OPIIPICut12("OPI/IPI CUT 12");
		header.setL_OPIIPICut13("OPI/IPI CUT 13");
		header.setL_OPIIPICut14("OPI/IPI CUT 14");
		header.setL_OPIIPICut15("OPI/IPI CUT 15");
		header.setL_OPIIPICut16("OPI/IPI CUT 16");
		header.setL_OPIIPICut17("OPI/IPI CUT 17");
		header.setL_OPIIPICut18("OPI/IPI CUT 18");
		header.setL_OPIIPICut19("OPI/IPI CUT 19");
		header.setL_OPIIPICut20("OPI/IPI CUT 20");
		header.setL_OPIIPICut21("OPI/IPI CUT 21");
		header.setL_OPIIPICut22("OPI/IPI CUT 22");
		header.setL_OPIIPICut23("OPI/IPI CUT 23");
		header.setL_OPIIPICut24("OPI/IPI CUT 24");
		header.setL_OPIIPICut25("OPI/IPI CUT 25");
		header.setL_OPIIPICut26("OPI/IPI CUT 26");
		header.setL_OPIIPICut27("OPI/IPI CUT 27");
		header.setL_OPIIPICut28("OPI/IPI CUT 28");
		header.setL_OPIIPICut29("OPI/IPI CUT 29");
		header.setL_OPIIPICut30("OPI/IPI CUT 30");
		header.setL_OPIIPICut31("OPI/IPI CUT 31");
		header.setL_OPIIPICut32("OPI/IPI CUT 32");
		header.setL_OPIIPICut33("OPI/IPI CUT 33");
		header.setL_OPIIPICut34("OPI/IPI CUT 34");
		header.setL_OPIIPICut35("OPI/IPI CUT 35");
		header.setL_OPIIPICut36("OPI/IPI CUT 36");
		header.setL_OPIIPICut37("OPI/IPI CUT 37");
		header.setL_OPIIPICut38("OPI/IPI CUT 38");
		header.setL_OPIIPICut39("OPI/IPI CUT 39");
		header.setL_OPIIPICut40("OPI/IPI CUT 40");
		return header;
	}

	/**
	 * Returns mock TO
	 * 
	 * @return
	 */
	public static GrtTO getMockGRTTO() {
		GrtTO to = new GrtTO();
		to.setL_TapeMode(CustomStringUtil.wrap("5", '"'));
		to.setL_Orgtstgpgm(CustomStringUtil.wrap("M013883003", '"'));
		to.setL_CorporationorDioceseName(CustomStringUtil.wrap("ADAMS CENTRAL", '"'));
		to.setL_CorporationorDioceseNumber(CustomStringUtil.wrap("0015", '"'));
		to.setL_SchoolName(CustomStringUtil.wrap("ADAMS C ES M", '"'));
		to.setL_SchoolNumber(CustomStringUtil.wrap("0020", '"'));
		to.setL_TeacherName(CustomStringUtil.wrap("STEINER", '"'));
		to.setL_TeacherElementNumberCTBUse(CustomStringUtil.wrap("0016973", '"'));
		to.setL_Grade(CustomStringUtil.wrap("03", '"'));
		to.setL_City(CustomStringUtil.wrap("MONROE", '"'));
		to.setL_State(CustomStringUtil.wrap("IN", '"'));
		to.setL_ISTEPTestName(CustomStringUtil.wrap("ISTEPS13", '"'));
		to.setL_ISTEPBook(CustomStringUtil.wrap("54300", '"'));
		to.setL_ISTEPForm(CustomStringUtil.wrap("3", '"'));
		to.setL_TestDateMMDDYY(CustomStringUtil.wrap("30413", '"'));
		to.setL_StudentLastName(CustomStringUtil.wrap("ABELL", '"'));
		to.setL_StudentFirstName(CustomStringUtil.wrap("ASHLYN", '"'));
		to.setL_StudentMiddleInitial(CustomStringUtil.wrap("  ", '"'));
		to.setL_StudentsGender(CustomStringUtil.wrap("F", '"'));
		to.setL_BirthDateMMDDYY(CustomStringUtil.wrap("113003", '"'));
		to.setL_ChronologicalAgeInMonths(CustomStringUtil.wrap("112", '"'));
		to.setL_Ethnicity(CustomStringUtil.wrap("1", '"'));
		to.setL_RaceAmericanIndianAlaskaNative(CustomStringUtil.wrap("1", '"'));
		to.setL_RaceAsian(CustomStringUtil.wrap("1", '"'));
		to.setL_RaceBlackorAfricanAmerican(CustomStringUtil.wrap("1", '"'));
		to.setL_RaceNativeHawaiianorOtherPacificIslander(CustomStringUtil.wrap("1", '"'));
		to.setL_RaceWhite(CustomStringUtil.wrap("1", '"'));
		to.setL_Filler1(CustomStringUtil.wrap("  ", '"'));
		to.setL_StudentTestNumberAI(CustomStringUtil.wrap("2010001", '"'));
		to.setL_SpecialCodeJ(CustomStringUtil.wrap("  ", '"'));
		to.setL_ResolvedEthnicityK(CustomStringUtil.wrap("5", '"'));
		to.setL_SpecialEducationL(CustomStringUtil.wrap("1", '"'));
		to.setL_ExceptionalityM(CustomStringUtil.wrap("  ", '"'));
		to.setL_SocioEconomicStatusN(CustomStringUtil.wrap("1", '"'));
		to.setL_Section504O(CustomStringUtil.wrap("1", '"'));
		to.setL_EnglishLearnerELP(CustomStringUtil.wrap("1", '"'));
		to.setL_MigrantQ(CustomStringUtil.wrap("1", '"'));
		to.setL_LocaluseR(CustomStringUtil.wrap("  ", '"'));
		to.setL_LocaluseS(CustomStringUtil.wrap("  ", '"'));
		to.setL_LocaluseT(CustomStringUtil.wrap("  ", '"'));
		to.setL_MatchUnmatchU(CustomStringUtil.wrap("1", '"'));
		to.setL_DuplicateV(CustomStringUtil.wrap("  ", '"'));
		to.setL_CTBUseOnlyW(CustomStringUtil.wrap("  ", '"'));
		to.setL_SpecialCodeX(CustomStringUtil.wrap("  ", '"'));
		to.setL_SpecialCodeY(CustomStringUtil.wrap("  ", '"'));
		to.setL_SpecialCodeZ(CustomStringUtil.wrap("  ", '"'));
		to.setL_AccommodationsEla(CustomStringUtil.wrap("1", '"'));
		to.setL_AccommodationsMath(CustomStringUtil.wrap("1", '"'));
		to.setL_AccommodationsScience(CustomStringUtil.wrap("1", '"'));
		to.setL_AccommodationsSocialStudies(CustomStringUtil.wrap("1", '"'));
		to.setL_CorporationUseID(CustomStringUtil.wrap("10008", '"'));
		to.setL_CustomerUse(CustomStringUtil.wrap("  ", '"'));
		to.setL_Filler2(CustomStringUtil.wrap("  ", '"'));
		to.setL_Filler3(CustomStringUtil.wrap("  ", '"'));
		to.setL_ElaPFIndicator(CustomStringUtil.wrap("A", '"'));
		to.setL_MathPFIndicator(CustomStringUtil.wrap("P", '"'));
		to.setL_SciencePFIndicator(CustomStringUtil.wrap("  ", '"'));
		to.setL_SocialStudiesPFIndicator(CustomStringUtil.wrap("  ", '"'));
		to.setL_ElaNumberCorrect(CustomStringUtil.wrap("58", '"'));
		to.setL_MathNumberCorrect(CustomStringUtil.wrap("53", '"'));
		to.setL_ScienceNumberCorrect(CustomStringUtil.wrap("  ", '"'));
		to.setL_SocialStudiesNumberCorrect(CustomStringUtil.wrap("  ", '"'));
		to.setL_ElaScaleScore(CustomStringUtil.wrap("481", '"'));
		to.setL_MathScaleScore(CustomStringUtil.wrap("514", '"'));
		to.setL_ScienceScaleScore(CustomStringUtil.wrap("  ", '"'));
		to.setL_SocialStudiesScaleScore(CustomStringUtil.wrap("  ", '"'));
		to.setL_ElaScaleScoreSEM(CustomStringUtil.wrap("13", '"'));
		to.setL_MathScaleScoreSEM(CustomStringUtil.wrap("19", '"'));
		to.setL_ScienceScaleScoreSEM(CustomStringUtil.wrap("  ", '"'));
		to.setL_SocialStudiesScaleScoreSEM(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator1(CustomStringUtil.wrap("+", '"'));
		to.setL_MasteryIndicator2(CustomStringUtil.wrap("+", '"'));
		to.setL_MasteryIndicator3(CustomStringUtil.wrap("+", '"'));
		to.setL_MasteryIndicator4(CustomStringUtil.wrap("+", '"'));
		to.setL_MasteryIndicator5(CustomStringUtil.wrap("+", '"'));
		to.setL_MasteryIndicator6(CustomStringUtil.wrap("+", '"'));
		to.setL_MasteryIndicator7(CustomStringUtil.wrap("+", '"'));
		to.setL_MasteryIndicator8(CustomStringUtil.wrap("+", '"'));
		to.setL_MasteryIndicator9(CustomStringUtil.wrap("+", '"'));
		to.setL_MasteryIndicator10(CustomStringUtil.wrap("+", '"'));
		to.setL_MasteryIndicator11(CustomStringUtil.wrap("+", '"'));
		to.setL_MasteryIndicator12(CustomStringUtil.wrap("+", '"'));
		to.setL_MasteryIndicator13(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator14(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator15(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator16(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator17(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator18(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator19(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator20(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator21(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator22(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator23(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator24(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator25(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator26(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator27(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator28(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator29(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator30(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator31(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator32(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator33(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator34(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator35(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator36(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator37(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator38(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator39(CustomStringUtil.wrap("  ", '"'));
		to.setL_MasteryIndicator40(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI1(CustomStringUtil.wrap("84", '"'));
		to.setL_OPIIPI2(CustomStringUtil.wrap("79", '"'));
		to.setL_OPIIPI3(CustomStringUtil.wrap("74", '"'));
		to.setL_OPIIPI4(CustomStringUtil.wrap("78", '"'));
		to.setL_OPIIPI5(CustomStringUtil.wrap("73", '"'));
		to.setL_OPIIPI6(CustomStringUtil.wrap("85", '"'));
		to.setL_OPIIPI7(CustomStringUtil.wrap("87", '"'));
		to.setL_OPIIPI8(CustomStringUtil.wrap("90", '"'));
		to.setL_OPIIPI9(CustomStringUtil.wrap("91", '"'));
		to.setL_OPIIPI10(CustomStringUtil.wrap("80", '"'));
		to.setL_OPIIPI11(CustomStringUtil.wrap("77", '"'));
		to.setL_OPIIPI12(CustomStringUtil.wrap("67", '"'));
		to.setL_OPIIPI13(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI14(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI15(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI16(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI17(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI18(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI19(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI20(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI21(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI22(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI23(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI24(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI25(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI26(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI27(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI28(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI29(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI30(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI31(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI32(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI33(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI34(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI35(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI36(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI37(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI38(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI39(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPI40(CustomStringUtil.wrap("  ", '"'));
		to.setL_ELACRSession2ItemResponses(CustomStringUtil.wrap("54", '"'));
		to.setL_ELACRSession3ItemResponses(CustomStringUtil.wrap("22244", '"'));
		to.setL_MathCRSession1ItemResponses(CustomStringUtil.wrap("22121013", '"'));
		to.setL_ScienceCRSession4ItemResponses(CustomStringUtil.wrap("  ", '"'));
		to.setL_SocialStudiesCRSession4ItemResponses(CustomStringUtil.wrap("  ", '"'));
		to.setL_Filler4(CustomStringUtil.wrap("  ", '"'));
		to.setL_SchoolPersonnelNumberSPN1(CustomStringUtil.wrap("  ", '"'));
		to.setL_SchoolPersonnelNumberSPN2(CustomStringUtil.wrap("  ", '"'));
		to.setL_SchoolPersonnelNumberSPN3(CustomStringUtil.wrap("  ", '"'));
		to.setL_SchoolPersonnelNumberSPN4(CustomStringUtil.wrap("  ", '"'));
		to.setL_SchoolPersonnelNumberSPN5(CustomStringUtil.wrap("  ", '"'));
		to.setL_Filler5(CustomStringUtil.wrap("  ", '"'));
		to.setL_CGRComputerGeneratedResponsefrom(CustomStringUtil.wrap("  ", '"'));
		to.setL_CTBUseOnlyAppliedSkillsPPImageID(CustomStringUtil.wrap("FV24MHV7QUEHPDH8CQOC8R8RT5", '"'));
		to.setL_CTBUseOnlyAppliedSkillsOASImageID(CustomStringUtil.wrap("  ", '"'));
		to.setL_CTBUseOnlyMCPPImagingid(CustomStringUtil.wrap("  ", '"'));
		to.setL_CTBUseOnlyMCOASimagingid(CustomStringUtil.wrap("OAS270442318266806", '"'));
		to.setL_BarcodeIDAppliedSkills(CustomStringUtil.wrap("21944483", '"'));
		to.setL_BarcodeIDMultipleChoice(CustomStringUtil.wrap("21944483", '"'));
		to.setL_CTBUseOnlyTestForm(CustomStringUtil.wrap("  ", '"'));
		to.setL_CTBUseOnlyMCBlankbookFlag(CustomStringUtil.wrap("  ", '"'));
		to.setL_CTBUseOnlyTestFormAppliedSkillsFieldPilotTest(CustomStringUtil.wrap("  ", '"'));
		to.setL_CTBUseOnlyTestFormMCFieldPilotTest(CustomStringUtil.wrap("  ", '"'));
		to.setL_CTBUseOnlyFutureUseOASTestedIndicatorAppliedSkillstest(CustomStringUtil.wrap("  ", '"'));
		to.setL_CTBUseOnlyOASTestedIndicatorMultipleChoiceTest(CustomStringUtil.wrap("0", '"'));
		to.setL_CTBUseOnlyStructureLevel(CustomStringUtil.wrap("6", '"'));
		to.setL_CTBUseOnlyElementNumber(CustomStringUtil.wrap("482387", '"'));
		to.setL_ResolvedReportingStatusEla(CustomStringUtil.wrap("  ", '"'));
		to.setL_ResolvedReportingStatusMath(CustomStringUtil.wrap("  ", '"'));
		to.setL_ResolvedReportingStatusScience(CustomStringUtil.wrap("  ", '"'));
		to.setL_ResolvedReportingStatusSocialStudies(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut1(CustomStringUtil.wrap("58", '"'));
		to.setL_OPIIPICut2(CustomStringUtil.wrap("50", '"'));
		to.setL_OPIIPICut3(CustomStringUtil.wrap("40", '"'));
		to.setL_OPIIPICut4(CustomStringUtil.wrap("50", '"'));
		to.setL_OPIIPICut5(CustomStringUtil.wrap("57", '"'));
		to.setL_OPIIPICut6(CustomStringUtil.wrap("66", '"'));
		to.setL_OPIIPICut7(CustomStringUtil.wrap("60", '"'));
		to.setL_OPIIPICut8(CustomStringUtil.wrap("64", '"'));
		to.setL_OPIIPICut9(CustomStringUtil.wrap("57", '"'));
		to.setL_OPIIPICut10(CustomStringUtil.wrap("53", '"'));
		to.setL_OPIIPICut11(CustomStringUtil.wrap("46", '"'));
		to.setL_OPIIPICut12(CustomStringUtil.wrap("31", '"'));
		to.setL_OPIIPICut13(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut14(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut15(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut16(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut17(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut18(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut19(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut20(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut21(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut22(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut23(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut24(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut25(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut26(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut27(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut28(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut29(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut30(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut31(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut32(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut33(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut34(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut35(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut36(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut37(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut38(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut39(CustomStringUtil.wrap("  ", '"'));
		to.setL_OPIIPICut40(CustomStringUtil.wrap("  ", '"'));
		return to;
	}
}
