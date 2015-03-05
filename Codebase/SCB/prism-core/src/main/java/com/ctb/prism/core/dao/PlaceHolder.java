package com.ctb.prism.core.dao;

public class PlaceHolder {
	private Integer paramNumber;
	private String paramType;
	private Integer valueType;
	private Object value;

	public PlaceHolder(Integer paramNumber, String paramType,
			Integer valueType, Object value) {
		super();
		this.paramNumber = paramNumber;
		this.paramType = paramType;
		this.valueType = valueType;
		this.value = value;
	}

	public Integer getParamNumber() {
		return paramNumber;
	}

	public void setParamNumber(Integer paramNumber) {
		this.paramNumber = paramNumber;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public Integer getValueType() {
		return valueType;
	}

	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
