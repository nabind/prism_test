/**
 * 
 */
package com.ctb.prism.inors.transferobject;

import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author 165505
 * 
 */

@Record(minOccurs = 0)
public class InvitationCodeTO extends BaseTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1267397904584405251L;

	@Field(at = 0)
	private String corporationorDioceseName;

	@Field(at = 1)
	private String corporationorDioceseNumber;

	@Field(at = 2)
	private String schoolName;

	@Field(at = 3)
	private String schoolNumber;

	@Field(at = 4)
	private String grade;

	@Field(at = 5)
	private String administrationName;

	@Field(at = 6)
	private String iSTEPInvitationCode;

	@Field(at = 7)
	private String invitationCodeExpirationDate;

	@Field(at = 8)
	private String studentLastName;

	@Field(at = 9)
	private String studentFirstName;

	@Field(at = 10)
	private String studentMiddleInitial;

	@Field(at = 11)
	private String studentsGender;

	@Field(at = 12)
	private String birthDate;

	@Field(at = 13)
	private String studentTestNumber;

	@Field(at = 14)
	private String corporationStudentID;

	@Field(at = 15)
	private String cTBUSEBarcodeID;

	@Field(at = 16)
	private String teacherName;

	@Field(at = 17)
	private String cTBUSEOrgtstgpgm;

	@Field(at = 18)
	private String cTBUSETeacherElementNumber;

	@Field(at = 19)
	private String cTBUSEStudentElementNumber;

	/**
	 * @return the corporationorDioceseName
	 */
	public String getCorporationorDioceseName() {
		return corporationorDioceseName;
	}

	/**
	 * @param corporationorDioceseName
	 *            the corporationorDioceseName to set
	 */
	public void setCorporationorDioceseName(String corporationorDioceseName) {
		this.corporationorDioceseName = corporationorDioceseName;
	}

	/**
	 * @return the corporationorDioceseNumber
	 */
	public String getCorporationorDioceseNumber() {
		return corporationorDioceseNumber;
	}

	/**
	 * @param corporationorDioceseNumber
	 *            the corporationorDioceseNumber to set
	 */
	public void setCorporationorDioceseNumber(String corporationorDioceseNumber) {
		this.corporationorDioceseNumber = corporationorDioceseNumber;
	}

	/**
	 * @return the schoolName
	 */
	public String getSchoolName() {
		return schoolName;
	}

	/**
	 * @param schoolName
	 *            the schoolName to set
	 */
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	/**
	 * @return the schoolNumber
	 */
	public String getSchoolNumber() {
		return schoolNumber;
	}

	/**
	 * @param schoolNumber
	 *            the schoolNumber to set
	 */
	public void setSchoolNumber(String schoolNumber) {
		this.schoolNumber = schoolNumber;
	}

	/**
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}

	/**
	 * @param grade
	 *            the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/**
	 * @return the administrationName
	 */
	public String getAdministrationName() {
		return administrationName;
	}

	/**
	 * @param administrationName
	 *            the administrationName to set
	 */
	public void setAdministrationName(String administrationName) {
		this.administrationName = administrationName;
	}

	/**
	 * @return the iSTEPInvitationCode
	 */
	public String getiSTEPInvitationCode() {
		return iSTEPInvitationCode;
	}

	/**
	 * @param iSTEPInvitationCode
	 *            the iSTEPInvitationCode to set
	 */
	public void setiSTEPInvitationCode(String iSTEPInvitationCode) {
		this.iSTEPInvitationCode = iSTEPInvitationCode;
	}

	/**
	 * @return the invitationCodeExpirationDate
	 */
	public String getInvitationCodeExpirationDate() {
		return invitationCodeExpirationDate;
	}

	/**
	 * @param invitationCodeExpirationDate
	 *            the invitationCodeExpirationDate to set
	 */
	public void setInvitationCodeExpirationDate(
			String invitationCodeExpirationDate) {
		this.invitationCodeExpirationDate = invitationCodeExpirationDate;
	}

	/**
	 * @return the studentLastName
	 */
	public String getStudentLastName() {
		return studentLastName;
	}

	/**
	 * @param studentLastName
	 *            the studentLastName to set
	 */
	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}

	/**
	 * @return the studentFirstName
	 */
	public String getStudentFirstName() {
		return studentFirstName;
	}

	/**
	 * @param studentFirstName
	 *            the studentFirstName to set
	 */
	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}

	/**
	 * @return the studentMiddleInitial
	 */
	public String getStudentMiddleInitial() {
		return studentMiddleInitial;
	}

	/**
	 * @param studentMiddleInitial
	 *            the studentMiddleInitial to set
	 */
	public void setStudentMiddleInitial(String studentMiddleInitial) {
		this.studentMiddleInitial = studentMiddleInitial;
	}

	/**
	 * @return the studentsGender
	 */
	public String getStudentsGender() {
		return studentsGender;
	}

	/**
	 * @param studentsGender
	 *            the studentsGender to set
	 */
	public void setStudentsGender(String studentsGender) {
		this.studentsGender = studentsGender;
	}

	/**
	 * @return the birthDate
	 */
	public String getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate
	 *            the birthDate to set
	 */
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the studentTestNumber
	 */
	public String getStudentTestNumber() {
		return studentTestNumber;
	}

	/**
	 * @param studentTestNumber
	 *            the studentTestNumber to set
	 */
	public void setStudentTestNumber(String studentTestNumber) {
		this.studentTestNumber = studentTestNumber;
	}

	/**
	 * @return the corporationStudentID
	 */
	public String getCorporationStudentID() {
		return corporationStudentID;
	}

	/**
	 * @param corporationStudentID
	 *            the corporationStudentID to set
	 */
	public void setCorporationStudentID(String corporationStudentID) {
		this.corporationStudentID = corporationStudentID;
	}

	/**
	 * @return the cTBUSEBarcodeID
	 */
	public String getcTBUSEBarcodeID() {
		return cTBUSEBarcodeID;
	}

	/**
	 * @param cTBUSEBarcodeID
	 *            the cTBUSEBarcodeID to set
	 */
	public void setcTBUSEBarcodeID(String cTBUSEBarcodeID) {
		this.cTBUSEBarcodeID = cTBUSEBarcodeID;
	}

	/**
	 * @return the teacherName
	 */
	public String getTeacherName() {
		return teacherName;
	}

	/**
	 * @param teacherName
	 *            the teacherName to set
	 */
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	/**
	 * @return the cTBUSEOrgtstgpgm
	 */
	public String getcTBUSEOrgtstgpgm() {
		return cTBUSEOrgtstgpgm;
	}

	/**
	 * @param cTBUSEOrgtstgpgm
	 *            the cTBUSEOrgtstgpgm to set
	 */
	public void setcTBUSEOrgtstgpgm(String cTBUSEOrgtstgpgm) {
		this.cTBUSEOrgtstgpgm = cTBUSEOrgtstgpgm;
	}

	/**
	 * @return the cTBUSETeacherElementNumber
	 */
	public String getcTBUSETeacherElementNumber() {
		return cTBUSETeacherElementNumber;
	}

	/**
	 * @param cTBUSETeacherElementNumber
	 *            the cTBUSETeacherElementNumber to set
	 */
	public void setcTBUSETeacherElementNumber(String cTBUSETeacherElementNumber) {
		this.cTBUSETeacherElementNumber = cTBUSETeacherElementNumber;
	}

	/**
	 * @return the cTBUSEStudentElementNumber
	 */
	public String getcTBUSEStudentElementNumber() {
		return cTBUSEStudentElementNumber;
	}

	/**
	 * @param cTBUSEStudentElementNumber
	 *            the cTBUSEStudentElementNumber to set
	 */
	public void setcTBUSEStudentElementNumber(String cTBUSEStudentElementNumber) {
		this.cTBUSEStudentElementNumber = cTBUSEStudentElementNumber;
	}
}
