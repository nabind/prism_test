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

import com.ctb.prism.admin.transferobject.UserDataTO;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.inors.constant.InorsDownloadConstants;
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
		if (icList != null) {
			logger.log(IAppLogger.INFO, "IC : " + icList.size());
			icList.add(0, getInvitationCodeTOHeader());
			StreamFactory factory = StreamFactory.newInstance();
			StreamBuilder builder = new StreamBuilder(InorsDownloadConstants.IC).format("delimited")
					.parser(new DelimitedParserBuilder(delimiter))
					.addRecord(InvitationCodeTO.class);
			factory.define(builder);
			StringWriter stringWriter = new StringWriter();
			BeanWriter out = factory.createWriter(InorsDownloadConstants.IC, stringWriter);
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
		} else {
			return "No Records Found".getBytes();
		}
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
		if (grtList != null) {
			logger.log(IAppLogger.INFO, "GRT : " + grtList.size());
			grtList.add(0, getGRTTOHeader());
			StreamFactory factory = StreamFactory.newInstance();
			StreamBuilder builder = new StreamBuilder(InorsDownloadConstants.GRT).format("delimited")
					.parser(new DelimitedParserBuilder(delimiter))
					.addRecord(GrtTO.class);
			factory.define(builder);
			StringWriter stringWriter = new StringWriter();
			BeanWriter out = factory.createWriter(InorsDownloadConstants.GRT, stringWriter);
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
		} else {
			return "No Records Found".getBytes();
		}
	}
	
	/**
	 * 
	 * @param data
	 * @param wrapChar
	 * @return
	 */
	public static String wrap(Object data, char wrapChar) {
		if ((data == null) || ("NULL").equalsIgnoreCase(data.toString().trim())) {
			data = "";
		}
		StringBuilder sb = new StringBuilder(data.toString());
		sb.insert(0, wrapChar);
		sb.insert(sb.length(), wrapChar);
		return sb.toString();
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
		to.setCorporationorDioceseName(wrap("ADAMS CENTRAL", '"'));
		to.setCorporationorDioceseNumber(wrap("0015", '"'));
		to.setSchoolName(wrap("ADAMS C ES M", '"'));
		to.setSchoolNumber(wrap("0020", '"'));
		to.setGrade(wrap("3", '"'));
		to.setAdministrationName(wrap("ISTEPS13", '"'));
		to.setiSTEPInvitationCode(wrap("36JP-YECJ-RTJU-8GN6", '"'));
		to.setInvitationCodeExpirationDate(wrap("050614", '"'));
		to.setStudentLastName(wrap("ABELL", '"'));
		to.setStudentFirstName(wrap("ASHLYN", '"'));
		to.setStudentMiddleInitial(wrap("", '"'));
		to.setStudentsGender(wrap("F", '"'));
		to.setBirthDate(wrap("113003", '"'));
		to.setStudentTestNumber(wrap("002010001", '"'));
		to.setCorporationStudentID(wrap("10008", '"'));
		to.setcTBUSEBarcodeID(wrap("21944483", '"'));
		to.setTeacherName(wrap("STEINER", '"'));
		to.setcTBUSEOrgtstgpgm(wrap("M013883003", '"'));
		to.setcTBUSETeacherElementNumber(wrap("0016973", '"'));
		to.setcTBUSEStudentElementNumber(wrap("0482387", '"'));
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
		to.setL_TapeMode(wrap("5", '"'));
		to.setL_Orgtstgpgm(wrap("M013883003", '"'));
		to.setL_CorporationorDioceseName(wrap("ADAMS CENTRAL", '"'));
		to.setL_CorporationorDioceseNumber(wrap("0015", '"'));
		to.setL_SchoolName(wrap("ADAMS C ES M", '"'));
		to.setL_SchoolNumber(wrap("0020", '"'));
		to.setL_TeacherName(wrap("STEINER", '"'));
		to.setL_TeacherElementNumberCTBUse(wrap("0016973", '"'));
		to.setL_Grade(wrap("03", '"'));
		to.setL_City(wrap("MONROE", '"'));
		to.setL_State(wrap("IN", '"'));
		to.setL_ISTEPTestName(wrap("ISTEPS13", '"'));
		to.setL_ISTEPBook(wrap("54300", '"'));
		to.setL_ISTEPForm(wrap("3", '"'));
		to.setL_TestDateMMDDYY(wrap("30413", '"'));
		to.setL_StudentLastName(wrap("ABELL", '"'));
		to.setL_StudentFirstName(wrap("ASHLYN", '"'));
		to.setL_StudentMiddleInitial(wrap("", '"'));
		to.setL_StudentsGender(wrap("F", '"'));
		to.setL_BirthDateMMDDYY(wrap("113003", '"'));
		to.setL_ChronologicalAgeInMonths(wrap("112", '"'));
		to.setL_Ethnicity(wrap("1", '"'));
		to.setL_RaceAmericanIndianAlaskaNative(wrap("1", '"'));
		to.setL_RaceAsian(wrap("1", '"'));
		to.setL_RaceBlackorAfricanAmerican(wrap("1", '"'));
		to.setL_RaceNativeHawaiianorOtherPacificIslander(wrap("1", '"'));
		to.setL_RaceWhite(wrap("1", '"'));
		to.setL_Filler1(wrap("", '"'));
		to.setL_StudentTestNumberAI(wrap("2010001", '"'));
		to.setL_SpecialCodeJ(wrap("", '"'));
		to.setL_ResolvedEthnicityK(wrap("5", '"'));
		to.setL_SpecialEducationL(wrap("1", '"'));
		to.setL_ExceptionalityM(wrap("", '"'));
		to.setL_SocioEconomicStatusN(wrap("1", '"'));
		to.setL_Section504O(wrap("1", '"'));
		to.setL_EnglishLearnerELP(wrap("1", '"'));
		to.setL_MigrantQ(wrap("1", '"'));
		to.setL_LocaluseR(wrap("", '"'));
		to.setL_LocaluseS(wrap("", '"'));
		to.setL_LocaluseT(wrap("", '"'));
		to.setL_MatchUnmatchU(wrap("1", '"'));
		to.setL_DuplicateV(wrap("", '"'));
		to.setL_CTBUseOnlyW(wrap("", '"'));
		to.setL_SpecialCodeX(wrap("", '"'));
		to.setL_SpecialCodeY(wrap("", '"'));
		to.setL_SpecialCodeZ(wrap("", '"'));
		to.setL_AccommodationsEla(wrap("1", '"'));
		to.setL_AccommodationsMath(wrap("1", '"'));
		to.setL_AccommodationsScience(wrap("1", '"'));
		to.setL_AccommodationsSocialStudies(wrap("1", '"'));
		to.setL_CorporationUseID(wrap("10008", '"'));
		to.setL_CustomerUse(wrap("", '"'));
		to.setL_Filler2(wrap("", '"'));
		to.setL_Filler3(wrap("", '"'));
		to.setL_ElaPFIndicator(wrap("A", '"'));
		to.setL_MathPFIndicator(wrap("P", '"'));
		to.setL_SciencePFIndicator(wrap("", '"'));
		to.setL_SocialStudiesPFIndicator(wrap("", '"'));
		to.setL_ElaNumberCorrect(wrap("58", '"'));
		to.setL_MathNumberCorrect(wrap("53", '"'));
		to.setL_ScienceNumberCorrect(wrap("", '"'));
		to.setL_SocialStudiesNumberCorrect(wrap("", '"'));
		to.setL_ElaScaleScore(wrap("481", '"'));
		to.setL_MathScaleScore(wrap("514", '"'));
		to.setL_ScienceScaleScore(wrap("", '"'));
		to.setL_SocialStudiesScaleScore(wrap("", '"'));
		to.setL_ElaScaleScoreSEM(wrap("13", '"'));
		to.setL_MathScaleScoreSEM(wrap("19", '"'));
		to.setL_ScienceScaleScoreSEM(wrap("", '"'));
		to.setL_SocialStudiesScaleScoreSEM(wrap("", '"'));
		to.setL_MasteryIndicator1(wrap("+", '"'));
		to.setL_MasteryIndicator2(wrap("+", '"'));
		to.setL_MasteryIndicator3(wrap("+", '"'));
		to.setL_MasteryIndicator4(wrap("+", '"'));
		to.setL_MasteryIndicator5(wrap("+", '"'));
		to.setL_MasteryIndicator6(wrap("+", '"'));
		to.setL_MasteryIndicator7(wrap("+", '"'));
		to.setL_MasteryIndicator8(wrap("+", '"'));
		to.setL_MasteryIndicator9(wrap("+", '"'));
		to.setL_MasteryIndicator10(wrap("+", '"'));
		to.setL_MasteryIndicator11(wrap("+", '"'));
		to.setL_MasteryIndicator12(wrap("+", '"'));
		to.setL_MasteryIndicator13(wrap("", '"'));
		to.setL_MasteryIndicator14(wrap("", '"'));
		to.setL_MasteryIndicator15(wrap("", '"'));
		to.setL_MasteryIndicator16(wrap("", '"'));
		to.setL_MasteryIndicator17(wrap("", '"'));
		to.setL_MasteryIndicator18(wrap("", '"'));
		to.setL_MasteryIndicator19(wrap("", '"'));
		to.setL_MasteryIndicator20(wrap("", '"'));
		to.setL_MasteryIndicator21(wrap("", '"'));
		to.setL_MasteryIndicator22(wrap("", '"'));
		to.setL_MasteryIndicator23(wrap("", '"'));
		to.setL_MasteryIndicator24(wrap("", '"'));
		to.setL_MasteryIndicator25(wrap("", '"'));
		to.setL_MasteryIndicator26(wrap("", '"'));
		to.setL_MasteryIndicator27(wrap("", '"'));
		to.setL_MasteryIndicator28(wrap("", '"'));
		to.setL_MasteryIndicator29(wrap("", '"'));
		to.setL_MasteryIndicator30(wrap("", '"'));
		to.setL_MasteryIndicator31(wrap("", '"'));
		to.setL_MasteryIndicator32(wrap("", '"'));
		to.setL_MasteryIndicator33(wrap("", '"'));
		to.setL_MasteryIndicator34(wrap("", '"'));
		to.setL_MasteryIndicator35(wrap("", '"'));
		to.setL_MasteryIndicator36(wrap("", '"'));
		to.setL_MasteryIndicator37(wrap("", '"'));
		to.setL_MasteryIndicator38(wrap("", '"'));
		to.setL_MasteryIndicator39(wrap("", '"'));
		to.setL_MasteryIndicator40(wrap("", '"'));
		to.setL_OPIIPI1(wrap("84", '"'));
		to.setL_OPIIPI2(wrap("79", '"'));
		to.setL_OPIIPI3(wrap("74", '"'));
		to.setL_OPIIPI4(wrap("78", '"'));
		to.setL_OPIIPI5(wrap("73", '"'));
		to.setL_OPIIPI6(wrap("85", '"'));
		to.setL_OPIIPI7(wrap("87", '"'));
		to.setL_OPIIPI8(wrap("90", '"'));
		to.setL_OPIIPI9(wrap("91", '"'));
		to.setL_OPIIPI10(wrap("80", '"'));
		to.setL_OPIIPI11(wrap("77", '"'));
		to.setL_OPIIPI12(wrap("67", '"'));
		to.setL_OPIIPI13(wrap("", '"'));
		to.setL_OPIIPI14(wrap("", '"'));
		to.setL_OPIIPI15(wrap("", '"'));
		to.setL_OPIIPI16(wrap("", '"'));
		to.setL_OPIIPI17(wrap("", '"'));
		to.setL_OPIIPI18(wrap("", '"'));
		to.setL_OPIIPI19(wrap("", '"'));
		to.setL_OPIIPI20(wrap("", '"'));
		to.setL_OPIIPI21(wrap("", '"'));
		to.setL_OPIIPI22(wrap("", '"'));
		to.setL_OPIIPI23(wrap("", '"'));
		to.setL_OPIIPI24(wrap("", '"'));
		to.setL_OPIIPI25(wrap("", '"'));
		to.setL_OPIIPI26(wrap("", '"'));
		to.setL_OPIIPI27(wrap("", '"'));
		to.setL_OPIIPI28(wrap("", '"'));
		to.setL_OPIIPI29(wrap("", '"'));
		to.setL_OPIIPI30(wrap("", '"'));
		to.setL_OPIIPI31(wrap("", '"'));
		to.setL_OPIIPI32(wrap("", '"'));
		to.setL_OPIIPI33(wrap("", '"'));
		to.setL_OPIIPI34(wrap("", '"'));
		to.setL_OPIIPI35(wrap("", '"'));
		to.setL_OPIIPI36(wrap("", '"'));
		to.setL_OPIIPI37(wrap("", '"'));
		to.setL_OPIIPI38(wrap("", '"'));
		to.setL_OPIIPI39(wrap("", '"'));
		to.setL_OPIIPI40(wrap("", '"'));
		to.setL_ELACRSession2ItemResponses(wrap("54", '"'));
		to.setL_ELACRSession3ItemResponses(wrap("22244", '"'));
		to.setL_MathCRSession1ItemResponses(wrap("22121013", '"'));
		to.setL_ScienceCRSession4ItemResponses(wrap("", '"'));
		to.setL_SocialStudiesCRSession4ItemResponses(wrap("", '"'));
		to.setL_Filler4(wrap("", '"'));
		to.setL_SchoolPersonnelNumberSPN1(wrap("", '"'));
		to.setL_SchoolPersonnelNumberSPN2(wrap("", '"'));
		to.setL_SchoolPersonnelNumberSPN3(wrap("", '"'));
		to.setL_SchoolPersonnelNumberSPN4(wrap("", '"'));
		to.setL_SchoolPersonnelNumberSPN5(wrap("", '"'));
		to.setL_Filler5(wrap("", '"'));
		to.setL_CGRComputerGeneratedResponsefrom(wrap("", '"'));
		to.setL_CTBUseOnlyAppliedSkillsPPImageID(wrap("FV24MHV7QUEHPDH8CQOC8R8RT5", '"'));
		to.setL_CTBUseOnlyAppliedSkillsOASImageID(wrap("", '"'));
		to.setL_CTBUseOnlyMCPPImagingid(wrap("", '"'));
		to.setL_CTBUseOnlyMCOASimagingid(wrap("OAS270442318266806", '"'));
		to.setL_BarcodeIDAppliedSkills(wrap("21944483", '"'));
		to.setL_BarcodeIDMultipleChoice(wrap("21944483", '"'));
		to.setL_CTBUseOnlyTestForm(wrap("", '"'));
		to.setL_CTBUseOnlyMCBlankbookFlag(wrap("", '"'));
		to.setL_CTBUseOnlyTestFormAppliedSkillsFieldPilotTest(wrap("", '"'));
		to.setL_CTBUseOnlyTestFormMCFieldPilotTest(wrap("", '"'));
		to.setL_CTBUseOnlyFutureUseOASTestedIndicatorAppliedSkillstest(wrap("", '"'));
		to.setL_CTBUseOnlyOASTestedIndicatorMultipleChoiceTest(wrap("0", '"'));
		to.setL_CTBUseOnlyStructureLevel(wrap("6", '"'));
		to.setL_CTBUseOnlyElementNumber(wrap("482387", '"'));
		to.setL_ResolvedReportingStatusEla(wrap("", '"'));
		to.setL_ResolvedReportingStatusMath(wrap("", '"'));
		to.setL_ResolvedReportingStatusScience(wrap("", '"'));
		to.setL_ResolvedReportingStatusSocialStudies(wrap("", '"'));
		to.setL_OPIIPICut1(wrap("58", '"'));
		to.setL_OPIIPICut2(wrap("50", '"'));
		to.setL_OPIIPICut3(wrap("40", '"'));
		to.setL_OPIIPICut4(wrap("50", '"'));
		to.setL_OPIIPICut5(wrap("57", '"'));
		to.setL_OPIIPICut6(wrap("66", '"'));
		to.setL_OPIIPICut7(wrap("60", '"'));
		to.setL_OPIIPICut8(wrap("64", '"'));
		to.setL_OPIIPICut9(wrap("57", '"'));
		to.setL_OPIIPICut10(wrap("53", '"'));
		to.setL_OPIIPICut11(wrap("46", '"'));
		to.setL_OPIIPICut12(wrap("31", '"'));
		to.setL_OPIIPICut13(wrap("", '"'));
		to.setL_OPIIPICut14(wrap("", '"'));
		to.setL_OPIIPICut15(wrap("", '"'));
		to.setL_OPIIPICut16(wrap("", '"'));
		to.setL_OPIIPICut17(wrap("", '"'));
		to.setL_OPIIPICut18(wrap("", '"'));
		to.setL_OPIIPICut19(wrap("", '"'));
		to.setL_OPIIPICut20(wrap("", '"'));
		to.setL_OPIIPICut21(wrap("", '"'));
		to.setL_OPIIPICut22(wrap("", '"'));
		to.setL_OPIIPICut23(wrap("", '"'));
		to.setL_OPIIPICut24(wrap("", '"'));
		to.setL_OPIIPICut25(wrap("", '"'));
		to.setL_OPIIPICut26(wrap("", '"'));
		to.setL_OPIIPICut27(wrap("", '"'));
		to.setL_OPIIPICut28(wrap("", '"'));
		to.setL_OPIIPICut29(wrap("", '"'));
		to.setL_OPIIPICut30(wrap("", '"'));
		to.setL_OPIIPICut31(wrap("", '"'));
		to.setL_OPIIPICut32(wrap("", '"'));
		to.setL_OPIIPICut33(wrap("", '"'));
		to.setL_OPIIPICut34(wrap("", '"'));
		to.setL_OPIIPICut35(wrap("", '"'));
		to.setL_OPIIPICut36(wrap("", '"'));
		to.setL_OPIIPICut37(wrap("", '"'));
		to.setL_OPIIPICut38(wrap("", '"'));
		to.setL_OPIIPICut39(wrap("", '"'));
		to.setL_OPIIPICut40(wrap("", '"'));
		return to;
	}
	
	public static byte[] createUserByteArray(List<UserDataTO> userList, char delimiter){
		if (userList != null) {
			logger.log(IAppLogger.INFO, "Users : " + userList.size());
			userList.add(0, getUserDataTOHeader());
			StreamFactory factory = StreamFactory.newInstance();
			StreamBuilder builder = new StreamBuilder(InorsDownloadConstants.IC).format("delimited")
					.parser(new DelimitedParserBuilder(delimiter))
					.addRecord(UserDataTO.class);
			factory.define(builder);
			StringWriter stringWriter = new StringWriter();
			BeanWriter out = factory.createWriter(InorsDownloadConstants.IC, stringWriter);
			try {
				for (UserDataTO user : userList) {
					out.write(user);
				}
				out.flush();
				out.close();
				logger.log(IAppLogger.INFO, "User Byte Array Created Successfully");
			} catch (RecordIOException e) {
				logger.log(IAppLogger.ERROR, "", e);
				e.printStackTrace();
			}
			return stringWriter.getBuffer().toString().getBytes();
		} else {
			return "No Records Found".getBytes();
		}
	}

	private static UserDataTO getUserDataTOHeader() {
		UserDataTO userDataTO = new UserDataTO();
		userDataTO.setUserId("User Id");
		userDataTO.setFullName("Full Name");
		userDataTO.setStatus("Status");
		userDataTO.setOrgName("Org Name");
		userDataTO.setUserRoles("User Roles");
		return userDataTO;
	}
}
