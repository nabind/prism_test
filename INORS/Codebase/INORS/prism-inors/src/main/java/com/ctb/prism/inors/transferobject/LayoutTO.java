package com.ctb.prism.inors.transferobject;

/**
 * @author Amitabha Roy
 * 
 */
public class LayoutTO {
	Integer columnNum;
	String headerText;
	String columnAlias;
	String columnData;

	public LayoutTO(Integer columnNum, String headerText, String columnAlias, String columnData) {
		super();
		this.columnNum = columnNum;
		this.headerText = headerText;
		this.columnAlias = columnAlias;
		this.columnData = columnData;
	}

	/**
	 * @return the columnNum
	 */
	public Integer getColumnNum() {
		return columnNum;
	}

	/**
	 * @param columnNum
	 *            the columnNum to set
	 */
	public void setColumnNum(Integer columnNum) {
		this.columnNum = columnNum;
	}

	/**
	 * @return the headerText
	 */
	public String getHeaderText() {
		return headerText;
	}

	/**
	 * @param headerText
	 *            the headerText to set
	 */
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

	/**
	 * @return the columnAlias
	 */
	public String getColumnAlias() {
		return columnAlias;
	}

	/**
	 * @param columnAlias
	 *            the columnAlias to set
	 */
	public void setColumnAlias(String columnAlias) {
		this.columnAlias = columnAlias;
	}

	/**
	 * @return the columnData
	 */
	public String getColumnData() {
		return columnData;
	}

	/**
	 * @param columnData
	 *            the columnData to set
	 */
	public void setColumnData(String columnData) {
		this.columnData = columnData;
	}

	public String toString() {
		return "LayoutTO [columnNum=" + columnNum + ", headerText=" + headerText + ", columnAlias=" + columnAlias + ", columnData=" + columnData + "]";
	}

}
