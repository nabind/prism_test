/**
 * 
 */
package com.ctb.prism.inors.util;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.beanio.stream.RecordIOException;

import com.ctb.prism.admin.transferobject.UserDataTO;
import com.ctb.prism.admin.transferobject.UserTO;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.inors.constant.InorsDownloadConstants;
import com.ctb.prism.inors.transferobject.GrtTO;
import com.ctb.prism.inors.transferobject.InvitationCodeTO;
import com.ctb.prism.inors.transferobject.LayoutTO;

/**
 * @author Amitabha Roy
 * 
 */
public class InorsDownloadUtil {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(InorsDownloadUtil.class.getName());

	public static byte[] getTableDataBytes(String year, ArrayList<ArrayList<LayoutTO>> tableData, final String delimiter) {
		if (tableData != null && !tableData.isEmpty()) {
			CharArrayWriter out = new CharArrayWriter();
			try {
				// Write the Header
				for (LayoutTO to : tableData.get(0)) {
					out.write(to.getHeaderText());
					out.write(delimiter);
				}
				out.write("\n");

				// Write the row wise data
				for (ArrayList<LayoutTO> rowData : tableData) {
					for (LayoutTO to : rowData) {
						out.write(to.getColumnData());
						out.write(delimiter);
					}
					out.write("\n");
				}
				out.flush();
				out.close();
				logger.log(IAppLogger.INFO, "Table Data bytes created");
			} catch (IOException e) {
				logger.log(IAppLogger.ERROR, "", e);
				e.printStackTrace();

			}
			return out.toString().getBytes();
		} else {
			return "No Records Found".getBytes();
		}
	}

	/**
	 * Creates a byte array from the IC list based of layout identified by the year. There will be a single <code>InvitationCodeTO</code> for all layouts. If a layout does not contain any particular
	 * field then that field will not be written to to <code>CharArrayWriter</code>.
	 * 
	 * It is assumed that valid values had been set to the <code>InvitationCodeTO</code> from <code>DAO layer</code> using setter methods. This method will only call the getter methods.
	 * <em>Thus there will be only one setter call from DAO and one getter
	 * call from here.</em>
	 * 
	 * @param year
	 * @param icList
	 * @param delimiter
	 * @return
	 */
	public static byte[] getICBytes(final String year, final List<InvitationCodeTO> icList, final String delimiter) {
		if (icList != null) {
			logger.log(IAppLogger.INFO, "IC : " + icList.size());
			logger.log(IAppLogger.INFO, "year : " + year);
			icList.add(0, getInvitationCodeTOHeader());

			CharArrayWriter out = new CharArrayWriter();
			try {
				for (InvitationCodeTO ic : icList) {
					out.write(ic.getCorporationorDioceseName());
					out.write(delimiter);
					out.write(ic.getCorporationorDioceseNumber());
					out.write(delimiter);
					out.write(ic.getSchoolName());
					out.write(delimiter);
					out.write(ic.getSchoolNumber());
					out.write(delimiter);
					out.write(ic.getGrade());
					out.write(delimiter);
					out.write(ic.getAdministrationName());
					out.write(delimiter);
					out.write(ic.getISTEPInvitationCode());
					out.write(delimiter);
					out.write(ic.getInvitationCodeExpirationDate());
					out.write(delimiter);
					out.write(ic.getStudentLastName());
					out.write(delimiter);
					out.write(ic.getStudentFirstName());
					out.write(delimiter);
					out.write(ic.getStudentMiddleInitial());
					out.write(delimiter);
					out.write(ic.getStudentsGender());
					out.write(delimiter);
					out.write(ic.getBirthDate());
					out.write(delimiter);
					out.write(ic.getStudentTestNumber());
					out.write(delimiter);
					out.write(ic.getCorporationStudentID());
					out.write(delimiter);
					out.write(ic.getCTBUSEBarcodeID());
					out.write(delimiter);
					out.write(ic.getTeacherName());
					out.write(delimiter);
					out.write(ic.getCTBUSEOrgtstgpgm());
					out.write(delimiter);
					out.write(ic.getCTBUSETeacherElementNumber());
					out.write(delimiter);
					out.write(ic.getCTBUSEStudentElementNumber());
					out.write("\n");
				}
				out.flush();
				out.close();
				logger.log(IAppLogger.INFO, "IC bytes created");
			} catch (RecordIOException e) {
				logger.log(IAppLogger.ERROR, "", e);
				e.printStackTrace();
			} catch (IOException e) {
				logger.log(IAppLogger.ERROR, "", e);
				e.printStackTrace();
			}
			return out.toString().getBytes();
		} else {
			return "No Records Found".getBytes();
		}
	}

	/**
	 * Creates a byte array from the GRT list based of layout identified by the year. There will be a single <code>GrtTO</code> for all layouts. If a layout does not contain any particular field then
	 * that field will not be written to to <code>CharArrayWriter</code>.
	 * 
	 * It is assumed that valid values had been set to the <code>InvitationCodeTO</code> from <code>DAO layer</code> using setter methods. This method will only call the getter methods.
	 * <em>Thus there will be only one setter call from DAO and one getter
	 * call from here.</em>
	 * 
	 * @param year
	 * @param grtList
	 * @param delimiter
	 * @return
	 */
	public static byte[] getGRTBytes(String year, final List<GrtTO> grtList, final String delimiter) {
		if (grtList != null) {
			logger.log(IAppLogger.INFO, "GRT : " + grtList.size());
			logger.log(IAppLogger.INFO, "year : " + year);
			grtList.add(0, getGRTTOHeader());

			CharArrayWriter out = new CharArrayWriter();
			try {
				for (GrtTO grt : grtList) {
					// Biographic Data
					out.write(grt.getL_TapeMode());
					out.write(delimiter);
					out.write(grt.getL_Orgtstgpgm());
					out.write(delimiter);
					out.write(grt.getL_CorporationorDioceseName());
					out.write(delimiter);
					out.write(grt.getL_CorporationorDioceseNumber());
					out.write(delimiter);
					out.write(grt.getL_SchoolName());
					out.write(delimiter);
					out.write(grt.getL_SchoolNumber());
					out.write(delimiter);
					out.write(grt.getL_TeacherName());
					out.write(delimiter);
					out.write(grt.getL_TeacherElementNumberCTBUse());
					out.write(delimiter);
					out.write(grt.getL_Grade());
					out.write(delimiter);
					out.write(grt.getL_City());
					out.write(delimiter);
					out.write(grt.getL_State());
					out.write(delimiter);
					out.write(grt.getL_ISTEPTestName());
					out.write(delimiter);
					out.write(grt.getL_ISTEPBook());
					out.write(delimiter);
					out.write(grt.getL_ISTEPForm());
					out.write(delimiter);
					out.write(grt.getL_TestDateMMDDYY());
					out.write(delimiter);
					out.write(grt.getL_StudentLastName());
					out.write(delimiter);
					out.write(grt.getL_StudentFirstName());
					out.write(delimiter);
					out.write(grt.getL_StudentMiddleInitial());
					out.write(delimiter);
					out.write(grt.getL_StudentsGender());
					out.write(delimiter);
					out.write(grt.getL_BirthDateMMDDYY());
					out.write(delimiter);
					out.write(grt.getL_ChronologicalAgeInMonths());
					out.write(delimiter);
					// Added in 2011
					if ((InorsDownloadConstants.YEAR_2013.equals(year)) || (InorsDownloadConstants.YEAR_2012.equals(year)) || (InorsDownloadConstants.YEAR_2011.equals(year))) {
						out.write(grt.getL_Ethnicity());
						out.write(delimiter);
						out.write(grt.getL_RaceAmericanIndianAlaskaNative());
						out.write(delimiter);
						out.write(grt.getL_RaceAsian());
						out.write(delimiter);
						out.write(grt.getL_RaceBlackorAfricanAmerican());
						out.write(delimiter);
						out.write(grt.getL_RaceNativeHawaiianorOtherPacificIslander());
						out.write(delimiter);
						out.write(grt.getL_RaceWhite());
						out.write(delimiter);
					}
					out.write(grt.getL_Filler1());
					out.write(delimiter);

					// Student Special Codes
					out.write(grt.getL_StudentTestNumberAI());
					out.write(delimiter);
					out.write(grt.getL_SpecialCodeJ());
					out.write(delimiter);
					out.write(grt.getL_ResolvedEthnicityK());
					out.write(delimiter);
					out.write(grt.getL_SpecialEducationL());
					out.write(delimiter);
					out.write(grt.getL_ExceptionalityM());
					out.write(delimiter);
					out.write(grt.getL_SocioEconomicStatusN());
					out.write(delimiter);
					out.write(grt.getL_Section504O());
					out.write(delimiter);
					out.write(grt.getL_EnglishLearnerELP()); // TODO : Header has a different name in 2010
					out.write(delimiter);
					out.write(grt.getL_MigrantQ());
					out.write(delimiter);
					out.write(grt.getL_LocaluseR());
					out.write(delimiter);
					out.write(grt.getL_LocaluseS());
					out.write(delimiter);
					out.write(grt.getL_LocaluseT());
					out.write(delimiter);
					out.write(grt.getL_MatchUnmatchU());
					out.write(delimiter);
					out.write(grt.getL_DuplicateV());
					out.write(delimiter);
					out.write(grt.getL_CTBUseOnlyW());
					out.write(delimiter);
					out.write(grt.getL_SpecialCodeX());
					out.write(delimiter);
					out.write(grt.getL_SpecialCodeY());
					out.write(delimiter);
					out.write(grt.getL_SpecialCodeZ());
					out.write(delimiter);
					out.write(grt.getL_AccommodationsEla());
					out.write(delimiter);
					out.write(grt.getL_AccommodationsMath());
					out.write(delimiter);
					out.write(grt.getL_AccommodationsScience());
					out.write(delimiter);
					out.write(grt.getL_AccommodationsSocialStudies());
					out.write(delimiter);
					out.write(grt.getL_CorporationUseID());
					out.write(delimiter);
					out.write(grt.getL_CustomerUse());
					out.write(delimiter);
					out.write(grt.getL_Filler2());
					out.write(delimiter);
					out.write(grt.getL_Filler3());
					out.write(delimiter);
					out.write(grt.getL_ElaPFIndicator());
					out.write(delimiter);
					out.write(grt.getL_MathPFIndicator());
					out.write(delimiter);
					out.write(grt.getL_SciencePFIndicator());
					out.write(delimiter);
					out.write(grt.getL_SocialStudiesPFIndicator());
					out.write(delimiter);

					// ISTEP Subject Area Number Correct
					out.write(grt.getL_ElaNumberCorrect());
					out.write(delimiter);
					out.write(grt.getL_MathNumberCorrect());
					out.write(delimiter);
					out.write(grt.getL_ScienceNumberCorrect());
					out.write(delimiter);
					out.write(grt.getL_SocialStudiesNumberCorrect());
					out.write(delimiter);

					// ISTEP Subject Area Scale Score
					out.write(grt.getL_ElaScaleScore());
					out.write(delimiter);
					out.write(grt.getL_MathScaleScore());
					out.write(delimiter);
					out.write(grt.getL_ScienceScaleScore());
					out.write(delimiter);
					out.write(grt.getL_SocialStudiesScaleScore());
					out.write(delimiter);
					out.write(grt.getL_ElaScaleScoreSEM());
					out.write(delimiter);
					out.write(grt.getL_MathScaleScoreSEM());
					out.write(delimiter);
					out.write(grt.getL_ScienceScaleScoreSEM());
					out.write(delimiter);
					out.write(grt.getL_SocialStudiesScaleScoreSEM());
					out.write(delimiter);

					// ISTEP Academic Standards
					out.write(grt.getL_MasteryIndicator1());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator2());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator3());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator4());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator5());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator6());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator7());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator8());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator9());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator10());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator11());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator12());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator13());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator14());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator15());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator16());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator17());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator18());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator19());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator20());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator21());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator22());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator23());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator24());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator25());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator26());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator27());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator28());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator29());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator30());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator31());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator32());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator33());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator34());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator35());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator36());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator37());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator38());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator39());
					out.write(delimiter);
					out.write(grt.getL_MasteryIndicator40());
					out.write(delimiter);

					// ISTEP OPI/IPI
					out.write(grt.getL_OPIIPI1());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI2());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI3());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI4());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI5());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI6());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI7());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI8());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI9());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI10());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI11());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI12());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI13());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI14());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI15());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI16());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI17());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI18());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI19());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI20());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI21());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI22());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI23());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI24());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI25());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI26());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI27());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI28());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI29());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI30());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI31());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI32());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI33());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI34());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI35());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI36());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI37());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI38());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI39());
					out.write(delimiter);
					out.write(grt.getL_OPIIPI40());
					out.write(delimiter);

					// ISTEP Item Responses (G 3, 4, 5, 6, 7, 8)
					out.write(grt.getL_ELACRSession2ItemResponses());
					out.write(delimiter);
					out.write(grt.getL_ELACRSession3ItemResponses());
					out.write(delimiter);
					out.write(grt.getL_MathCRSession1ItemResponses());
					out.write(delimiter);
					out.write(grt.getL_ScienceCRSession4ItemResponses());
					out.write(delimiter);
					out.write(grt.getL_SocialStudiesCRSession4ItemResponses());
					out.write(delimiter);
					out.write(grt.getL_Filler4());
					out.write(delimiter);

					// CTB Use & ID Fields
					out.write(grt.getL_SchoolPersonnelNumberSPN1());
					out.write(delimiter);
					out.write(grt.getL_SchoolPersonnelNumberSPN2());
					out.write(delimiter);
					out.write(grt.getL_SchoolPersonnelNumberSPN3());
					out.write(delimiter);
					out.write(grt.getL_SchoolPersonnelNumberSPN4());
					out.write(delimiter);
					out.write(grt.getL_SchoolPersonnelNumberSPN5());
					out.write(delimiter);
					out.write(grt.getL_Filler5());
					out.write(delimiter);
					out.write(grt.getL_CGRComputerGeneratedResponsefrom());
					out.write(delimiter);
					out.write(grt.getL_CTBUseOnlyAppliedSkillsPPImageID());
					out.write(delimiter);
					out.write(grt.getL_CTBUseOnlyAppliedSkillsOASImageID());
					out.write(delimiter);
					out.write(grt.getL_CTBUseOnlyMCPPImagingid());
					out.write(delimiter);
					out.write(grt.getL_CTBUseOnlyMCOASimagingid());
					out.write(delimiter);
					out.write(grt.getL_BarcodeIDAppliedSkills());
					out.write(delimiter);
					out.write(grt.getL_BarcodeIDMultipleChoice());
					out.write(delimiter);
					out.write(grt.getL_CTBUseOnlyTestForm());
					out.write(delimiter);
					out.write(grt.getL_CTBUseOnlyMCBlankbookFlag());
					out.write(delimiter);
					out.write(grt.getL_CTBUseOnlyTestFormAppliedSkillsFieldPilotTest());
					out.write(delimiter);
					out.write(grt.getL_CTBUseOnlyTestFormMCFieldPilotTest());
					out.write(delimiter);
					out.write(grt.getL_CTBUseOnlyFutureUseOASTestedIndicatorAppliedSkillstest());
					out.write(delimiter);
					out.write(grt.getL_CTBUseOnlyOASTestedIndicatorMultipleChoiceTest());
					out.write(delimiter);
					out.write(grt.getL_CTBUseOnlyStructureLevel());
					out.write(delimiter);
					out.write(grt.getL_CTBUseOnlyElementNumber());
					out.write(delimiter);
					// Added in 2011
					if ((InorsDownloadConstants.YEAR_2013.equals(year)) || (InorsDownloadConstants.YEAR_2012.equals(year)) || (InorsDownloadConstants.YEAR_2011.equals(year))) {
						out.write(grt.getL_ResolvedReportingStatusEla());
						out.write(delimiter);
						out.write(grt.getL_ResolvedReportingStatusMath());
						out.write(delimiter);
						out.write(grt.getL_ResolvedReportingStatusScience());
						out.write(delimiter);
						out.write(grt.getL_ResolvedReportingStatusSocialStudies());
						out.write(delimiter);
					}
					// Added in 2013
					if (InorsDownloadConstants.YEAR_2013.equals(year)) {
						out.write(grt.getL_OPIIPICut1());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut2());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut3());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut4());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut5());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut6());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut7());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut8());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut9());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut10());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut11());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut12());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut13());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut14());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut15());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut16());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut17());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut18());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut19());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut20());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut21());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut22());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut23());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut24());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut25());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut26());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut27());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut28());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut29());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut30());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut31());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut32());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut33());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut34());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut35());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut36());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut37());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut38());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut39());
						out.write(delimiter);
						out.write(grt.getL_OPIIPICut40());
						out.write(delimiter);
					}
					out.write("\n");
				}
				out.flush();
				out.close();
				logger.log(IAppLogger.INFO, "GRT bytes created");
			} catch (RecordIOException e) {
				logger.log(IAppLogger.ERROR, "", e);
				e.printStackTrace();
			} catch (IOException e) {
				logger.log(IAppLogger.ERROR, "", e);
				e.printStackTrace();
			}
			return out.toString().getBytes();
		} else {
			return "No Records Found".getBytes();
		}
	}

	/**
	 * This method is used to enclose the data with double quote
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
	 * Method to create TO representation of the header record. It is used to write the header inside the file.
	 * 
	 * @return TO representation of the header record
	 */
	private static InvitationCodeTO getInvitationCodeTOHeader() {
		InvitationCodeTO header = new InvitationCodeTO();
		header.setCorporationorDioceseName("Corporation or Diocese Name");
		header.setCorporationorDioceseNumber("Corporation or Diocese Number");
		header.setSchoolName("School Name");
		header.setSchoolNumber("School Number");
		header.setGrade("Grade");
		header.setAdministrationName("Administration Name");
		header.setISTEPInvitationCode("ISTEP Invitation Code");
		header.setInvitationCodeExpirationDate("Invitation Code Expiration Date (MMDDYY)");
		header.setStudentLastName("Student Last Name");
		header.setStudentFirstName("Student First Name");
		header.setStudentMiddleInitial("Student Middle Initial");
		header.setStudentsGender("Student's Gender");
		header.setBirthDate("Birth Date (MMDDYY)");
		header.setStudentTestNumber("Student Test Number");
		header.setCorporationStudentID("Corporation Student ID");
		header.setCTBUSEBarcodeID("CTB USE: Barcode ID (PreID)");
		header.setTeacherName("Teacher Name");
		header.setCTBUSEOrgtstgpgm("CTB USE: Org-tstg-pgm");
		header.setCTBUSETeacherElementNumber("CTB USE: Teacher Element Number");
		header.setCTBUSEStudentElementNumber("CTB USE: Student Element Number");
		return header;
	}

	/**
	 * Returns mock TO. It should be used for test purpose only where database connection is not available.
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
		to.setISTEPInvitationCode(wrap("36JP-YECJ-RTJU-8GN6", '"'));
		to.setInvitationCodeExpirationDate(wrap("050614", '"'));
		to.setStudentLastName(wrap("ABELL", '"'));
		to.setStudentFirstName(wrap("ASHLYN", '"'));
		to.setStudentMiddleInitial(wrap("", '"'));
		to.setStudentsGender(wrap("F", '"'));
		to.setBirthDate(wrap("113003", '"'));
		to.setStudentTestNumber(wrap("002010001", '"'));
		to.setCorporationStudentID(wrap("10008", '"'));
		to.setCTBUSEBarcodeID(wrap("21944483", '"'));
		to.setTeacherName(wrap("STEINER", '"'));
		to.setCTBUSEOrgtstgpgm(wrap("M013883003", '"'));
		to.setCTBUSETeacherElementNumber(wrap("0016973", '"'));
		to.setCTBUSEStudentElementNumber(wrap("0482387", '"'));
		return to;
	}

	/**
	 * Method to create TO representation of the header record. It is used to write the header inside the file.
	 * 
	 * @return TO representation of the header record
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
	 * Returns mock TO. It should be used for test purpose only where database connection is not available.
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

	/**
	 * Creates a byte array from the UserData list
	 * 
	 * @param userList
	 * @param delimiter
	 * @return
	 */
	public static byte[] getUserDataBytes(List<UserDataTO> userList, String delimiter) {
		if (userList != null) {
			logger.log(IAppLogger.INFO, "User : " + userList.size());
			userList.add(0, getUserDataTOHeader());

			CharArrayWriter out = new CharArrayWriter();
			try {
				for (UserDataTO user : userList) {
					out.write(user.getUserId());
					out.write(delimiter);
					out.write(user.getFullName());
					out.write(delimiter);
					out.write(user.getStatus());
					out.write(delimiter);
					out.write(user.getOrgName());
					out.write(delimiter);
					out.write(user.getUserRoles());
					out.write("\n");
				}
				out.flush();
				out.close();
				logger.log(IAppLogger.INFO, "User Byte Array [" + out.size() + "] Created Successfully");
			} catch (RecordIOException e) {
				logger.log(IAppLogger.ERROR, "", e);
				e.printStackTrace();
			} catch (IOException e) {
				logger.log(IAppLogger.ERROR, "", e);
				e.printStackTrace();
			}
			return out.toString().getBytes();
		} else {
			return "No Records Found".getBytes();
		}
	}

	/**
	 * Method to create TO representation of the header record. It is used to write the header inside the file.
	 * 
	 * @return TO representation of the header record
	 */
	private static UserDataTO getUserDataTOHeader() {
		UserDataTO header = new UserDataTO();
		header.setUserId("User Id");
		header.setFullName("Full Name");
		header.setStatus("Status");
		header.setOrgName("Org Name");
		header.setUserRoles("User Roles");
		return header;
	}

	/**
	 * Converts List<UserTO> to List<UserDataTO>.
	 * 
	 * @param userList
	 * @return
	 */
	public static List<UserDataTO> getUserDataList(List<UserTO> userList) {
		List<UserDataTO> userDataList = new ArrayList<UserDataTO>();
		for (UserTO userTO : userList) {
			UserDataTO userDataTO = new UserDataTO();
			userDataTO.setUserId(String.valueOf(userTO.getUserId()));
			userDataTO.setFullName(userTO.getUserName());
			userDataTO.setStatus(userTO.getStatus());
			userDataTO.setOrgName(userTO.getTenantName());
			String userRoles = userTO.getAvailableRoleList().toString();
			userRoles = CustomStringUtil.replaceCharacterInString('[', "", userRoles);
			userRoles = CustomStringUtil.replaceCharacterInString(']', "", userRoles);
			userDataTO.setUserRoles("User Roles");
			userDataList.add(userDataTO);
		}
		return userDataList;
	}

	public static ArrayList<ArrayList<LayoutTO>> getTableDataFromResultSet(ResultSet rs, ArrayList<LayoutTO> layoutTOList) throws SQLException {
		ArrayList<ArrayList<LayoutTO>> tableData = new ArrayList<ArrayList<LayoutTO>>();
		while (rs.next()) {
			for (int i = 0; i < layoutTOList.size(); i++) {
				LayoutTO to = layoutTOList.get(i);
				String columnAlias = to.getColumnAlias();
				if ("*".equals(columnAlias)) {
					to.setColumnData(wrap("", '"'));
				} else {
					to.setColumnData(wrap(rs.getString(columnAlias), '"'));
				}
			}
			tableData.add(layoutTOList);
		}
		return tableData;
	}

	public static ArrayList<LayoutTO> getRowDataLayout(String headers, String aliases) {
		ArrayList<LayoutTO> layoutTOList = new ArrayList<LayoutTO>();
		String[] headerTokens = headers.split("\\|");
		String[] aliasTokens = aliases.split("\\|");
		logger.log(IAppLogger.INFO, "Headers = " + headerTokens.length);
		logger.log(IAppLogger.INFO, "Aliases = " + aliasTokens.length);
		for (int i = 0; i < headerTokens.length; i++) {
			layoutTOList.add(new LayoutTO(i + 1, headerTokens[i].trim(), aliasTokens[i].trim(), ""));
		}
		return layoutTOList;
	}

	public static void print(ArrayList<LayoutTO> layoutTOList) {
		for (LayoutTO to : layoutTOList) {
			System.out.println(to);
		}
	}

	public static void printTable(ArrayList<ArrayList<LayoutTO>> tableData) {
		for (ArrayList<LayoutTO> to : tableData) {
			print(to);
		}
	}
}
