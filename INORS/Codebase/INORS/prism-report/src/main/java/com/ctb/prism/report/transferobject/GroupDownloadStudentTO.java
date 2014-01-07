/**
 * 
 */
package com.ctb.prism.report.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author 796763
 * 
 */
public class GroupDownloadStudentTO extends BaseTO {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String klass;
	private String grade;
	private String isr;
	private String ip;
	private String both;
	private String ic;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the klass
	 */
	public String getKlass() {
		return klass;
	}

	/**
	 * @param klass
	 *            the klass to set
	 */
	public void setKlass(String klass) {
		this.klass = klass;
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
	 * @return the isr
	 */
	public String getIsr() {
		return isr;
	}

	/**
	 * @param isr
	 *            the isr to set
	 */
	public void setIsr(String isr) {
		this.isr = isr;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 *            the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the both
	 */
	public String getBoth() {
		return both;
	}

	/**
	 * @param both
	 *            the both to set
	 */
	public void setBoth(String both) {
		this.both = both;
	}

	/**
	 * @return the ic
	 */
	public String getIc() {
		return ic;
	}

	/**
	 * @param ic
	 *            the ic to set
	 */
	public void setIc(String ic) {
		this.ic = ic;
	}

}
