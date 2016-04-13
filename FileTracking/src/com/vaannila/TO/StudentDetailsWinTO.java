package com.vaannila.TO;

import java.io.Serializable;


public class StudentDetailsWinTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String scanBatch = "";
	private String districtNumber = "";
	private String schoolNumber = "";
	private String uuid = "";
	private String barcode = "";
	private String form = "";
	private String braille = "";
	private String largePrint = "";
	private String dateTestTaken = "";
	private String loginDate = "";
	private String scanDate = "";
	private String winsExportDate = "";
	private String imagingId = "";
	private String orgTpName = "";
	private String lastName = "";
	private String firstName = "";
	private String middleInitial = "";
	private String lithoCode = "";
	private String scanStack = "";
	private String scanSequence = "";
	private String winsDocId = "";
	private String comodityCode = "";
	private String winStatus = "";
	private String prismProcessStatus = "";
	private String imageFilePath = "";
	private String imageFileName = "";
	
	public String getScanBatch() {
		return scanBatch;
	}
	public void setScanBatch(String scanBatch) {
		this.scanBatch = scanBatch;
	}
	public String getDistrictNumber() {
		return districtNumber;
	}
	public void setDistrictNumber(String districtNumber) {
		this.districtNumber = districtNumber;
	}
	public String getSchoolNumber() {
		return schoolNumber;
	}
	public void setSchoolNumber(String schoolNumber) {
		this.schoolNumber = schoolNumber;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	public String getBraille() {
		return braille;
	}
	public void setBraille(String braille) {
		this.braille = braille;
	}
	public String getLargePrint() {
		return largePrint;
	}
	public void setLargePrint(String largePrint) {
		this.largePrint = largePrint;
	}
	public String getDateTestTaken() {
		return dateTestTaken;
	}
	public void setDateTestTaken(String dateTestTaken) {
		this.dateTestTaken = dateTestTaken;
	}
	public String getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}
	public String getScanDate() {
		return scanDate;
	}
	public void setScanDate(String scanDate) {
		this.scanDate = scanDate;
	}
	public String getWinsExportDate() {
		return winsExportDate;
	}
	public void setWinsExportDate(String winsExportDate) {
		this.winsExportDate = winsExportDate;
	}
	public String getImagingId() {
		return imagingId;
	}
	public void setImagingId(String imagingId) {
		this.imagingId = imagingId;
	}
	public String getOrgTpName() {
		return orgTpName;
	}
	public void setOrgTpName(String orgTpName) {
		this.orgTpName = orgTpName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleInitial() {
		return middleInitial;
	}
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}
	public String getLithoCode() {
		return lithoCode;
	}
	public void setLithoCode(String lithoCode) {
		this.lithoCode = lithoCode;
	}
	public String getScanStack() {
		return scanStack;
	}
	public void setScanStack(String scanStack) {
		this.scanStack = scanStack;
	}
	public String getScanSequence() {
		return scanSequence;
	}
	public void setScanSequence(String scanSequence) {
		this.scanSequence = scanSequence;
	}
	public String getWinsDocId() {
		return winsDocId;
	}
	public void setWinsDocId(String winsDocId) {
		this.winsDocId = winsDocId;
	}
	public String getComodityCode() {
		return comodityCode;
	}
	public void setComodityCode(String comodityCode) {
		this.comodityCode = comodityCode;
	}
	public String getWinStatus() {
		return winStatus;
	}
	public void setWinStatus(String winStatus) {
		this.winStatus = winStatus;
	}
	public String getPrismProcessStatus() {
		return prismProcessStatus;
	}
	public void setPrismProcessStatus(String prismProcessStatus) {
		this.prismProcessStatus = prismProcessStatus;
	}
	public String getImageFilePath() {
		return imageFilePath;
	}
	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\"").append(scanBatch).append("\"")
		.append(",").append("\"").append(districtNumber).append("\"")
		.append(",").append("\"").append(schoolNumber).append("\"")
		.append(",").append("\"").append(uuid).append("\"")
		.append(",").append("\"").append(barcode).append("\"")
		.append(",").append("\"").append(form).append("\"")
		.append(",").append("\"").append(braille).append("\"")
		.append(",").append("\"").append(largePrint).append("\"")
		.append(",").append("\"").append(dateTestTaken).append("\"")
		.append(",").append("\"").append(loginDate).append("\"")
		.append(",").append("\"").append(scanDate).append("\"")
		.append(",").append("\"").append(winsExportDate).append("\"")
		.append(",").append("\"").append(imagingId).append("\"")
		.append(",").append("\"").append(orgTpName).append("\"")
		.append(",").append("\"").append(lastName).append("\"")
		.append(",").append("\"").append(firstName).append("\"")
		.append(",").append("\"").append(middleInitial).append("\"")
		.append(",").append("\"").append(lithoCode).append("\"")
		.append(",").append("\"").append(scanStack).append("\"")
		.append(",").append("\"").append(scanSequence).append("\"")
		.append(",").append("\"").append(winsDocId).append("\"")
		.append(",").append("\"").append(comodityCode).append("\"")
		.append(",").append("\"").append(winStatus).append("\"")
		.append(",").append("\"").append(prismProcessStatus).append("\"")
		.append(",").append("\"").append(imageFilePath).append("\"")
		.append(",").append("\"").append(imageFileName).append("\"");
		return buffer.toString();
	}
	
	
	
}
