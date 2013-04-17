package com.ctb.prism.report.transferobject;

import java.util.List;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.transferobject.BaseTO;


/**
 * Transfer object which holds the details of a input control of a report
 */
public class InputControlTO extends BaseTO {
	
	private static final long serialVersionUID = 1L;
	
	private long controlId;
	private String labelId;
	private String label;
	private boolean visible;
	private boolean readonly;
	private boolean mandatory;
	private int sequence;
	private String dataType;
	private String type;
	private String query;
	private List<String> listOfValues;
	private String queryValueColumn;
	private String datasource;
	
	public boolean isCollection() {
		if(IApplicationConstants.DATA_TYPE_COLLECTION.equals(getType())) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isInputBox() {
		if(IApplicationConstants.DATA_TYPE_TESTBOX.equals(getType())) {
			return true;
		} else {
			return false;
		}
	}
	public long getControlId() {
		return controlId;
	}
	public void setControlId(long controlId) {
		this.controlId = controlId;
	}
	public String getLabelId() {
		return labelId;
	}
	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public boolean isReadonly() {
		return readonly;
	}
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public List<String> getListOfValues() {
		return listOfValues;
	}
	public void setListOfValues(List<String> listOfValues) {
		this.listOfValues = listOfValues;
	}
	public String getQueryValueColumn() {
		return queryValueColumn;
	}
	public void setQueryValueColumn(String queryValueColumn) {
		this.queryValueColumn = queryValueColumn;
	}
	public String getDatasource() {
		return datasource;
	}
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
}
