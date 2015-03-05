package com.ctb.prism.report.transferobject;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import oracle.sql.DATE;

import net.sf.jasperreports.engine.JasperReport;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.transferobject.BaseTO;

/**
 * Transfer object holds details of a report. Report name, report URL, the roles who can access the report etc.
 */
public class GroupDownload extends BaseTO {
	
	private static final long serialVersionUID = 1L;
	
	private Timestamp expirationDate;
	private String fileType;
	private String generatedFile;
	private String size;
	private long jobId;
	private String download;
	private String object3;
	private String object4;
	public Timestamp getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getGeneratedFile() {
		return generatedFile;
	}
	public void setGeneratedFile(String generatedFile) {
		this.generatedFile = generatedFile;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public long getJobId() {
		return jobId;
	}
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
	public String getDownload() {
		return download;
	}
	public void setDownload(String download) {
		this.download = download;
	}
	public String getObject3() {
		return object3;
	}
	public void setObject3(String object3) {
		this.object3 = object3;
	}
	public String getObject4() {
		return object4;
	}
	public void setObject4(String object4) {
		this.object4 = object4;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
