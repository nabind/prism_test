package com.ctb.prism.report.ipcontrol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.constant.IDomObject;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.ObjectValueTO;

public class InputControlFactoryImpl implements InputControlFactory {

	public String getSelectInputControlEmptyBox() {
		StringBuilder str = new StringBuilder();
		str.append( getInputControlContainer(true) );
		str.append("&nbsp;");
		str.append( getInputControlContainer(false) );
		
		return str.toString();
	}
	/**
	 * Returns DOM object for single/multiple select
	 * @param objectValueToList
	 * @return
	 */
	public String getSelectInputControl(List<ObjectValueTO> objectValueToList, 
			String title, String name, String reportUrl, String tabCount, boolean isMultiselect, 
			boolean isTextBox, String assessmentId, Map<String, Object> parameters, boolean seperated) {
		StringBuilder str = new StringBuilder();
		if(seperated) str.append( getInputControlContainerSeparated(true) );
		else str.append( getInputControlContainer(true) );
		str.append( getSelectBoxLabel(title) );
		if(isMultiselect) {
			str.append( String.format(IDomObject.SELECT_MULTIPLE_EXPANDABLE, name, name, reportUrl, tabCount, assessmentId, name) );
		} else {
			str.append( String.format(IDomObject.SELECT_SINGLE_EXPANDABLE, name, name, reportUrl, tabCount, assessmentId, name) );
		}
		
		if(objectValueToList == null) {
			objectValueToList = new ArrayList<ObjectValueTO>();
			ObjectValueTO objectValueTo = new ObjectValueTO();
			objectValueTo.setName(" ");
			objectValueTo.setValue("0");
			objectValueToList.add(objectValueTo);
		}
		Map<String, String[]> defaultValues = 
			(Map<String, String[]>) parameters.get(CustomStringUtil.appendString(IApplicationConstants.CHECK_DEFAULT, name));
		List<String> defaultInputNames = 
			(List<String>) parameters.get(CustomStringUtil.appendString(IApplicationConstants.CHECK_DEFAULT_NAME, name));
		
		Map<String, String[]> selectValues = 
			(Map<String, String[]>) parameters.get(CustomStringUtil.appendString(IApplicationConstants.CHECK_SELECTED, name));
		List<String> selectInputNames = 
			(List<String>) parameters.get(CustomStringUtil.appendString(IApplicationConstants.CHECK_SELECTED_NAME, name));
		
		List<String> defaultValueList = null;
		List<String> selectValueList = null;
		/*if(defaultValues != null) {
			defaultValueList = Arrays.asList(defaultValues);
		}*/
		// patch for Longitudinal
		boolean isLongitudinal = false;
		boolean isFirstSelected = false;
		if(IApplicationConstants.EXTENDED_YEAR.equals(name)) {
			isLongitudinal = true;
		}
		// End : patch for Longitudinal
		for (ObjectValueTO objectValueTo : objectValueToList) {
			if(isMultiselect) {
				if(selectValues != null && selectValues.get(name) != null) selectValueList = Arrays.asList(selectValues.get(name));
				// this input control has default value ... we need to check selected object based on this default value
				if(selectValueList != null && !selectValueList.isEmpty()) {
					if(selectValueList.contains(objectValueTo.getValue())) {
						str.append( String.format(IDomObject.SELECT_OPTION, 
							objectValueTo.getValue(), IDomObject.OPTION_SELECTED, objectValueTo.getName()) );
					} else {
						str.append( String.format(IDomObject.SELECT_OPTION, 
								objectValueTo.getValue(), "", objectValueTo.getName()) );
					}
				} else {
					if(defaultInputNames != null && defaultInputNames.contains(name)) {
						if(defaultValues.get(name) != null) defaultValueList = Arrays.asList(defaultValues.get(name));
						// this input control has default value ... we need to check selected object based on this default value
						if(defaultValueList != null && defaultValueList.contains(objectValueTo.getValue())) {
							str.append( String.format(IDomObject.SELECT_OPTION, 
									objectValueTo.getValue(), IDomObject.OPTION_SELECTED, objectValueTo.getName()) );
						} else {
							str.append( String.format(IDomObject.SELECT_OPTION, 
									objectValueTo.getValue(), "", objectValueTo.getName()) );
						}
					} else {
						if(isLongitudinal && isFirstSelected) {
							str.append( String.format(IDomObject.SELECT_OPTION, 
									objectValueTo.getValue(), "", objectValueTo.getName()) );
						} else {
							str.append( String.format(IDomObject.SELECT_OPTION, 
									objectValueTo.getValue(), IDomObject.OPTION_SELECTED, objectValueTo.getName()) );
						}
						if(isLongitudinal && !isFirstSelected) {
							isFirstSelected = true;
						}
					}
				}
			} else {
				if(selectValues != null && selectValues.get(name) != null) selectValueList = Arrays.asList(selectValues.get(name));
				// this input control has default value ... we need to check selected object based on this default value
				if(selectValueList != null && selectValueList.contains(objectValueTo.getValue())) {
					str.append( String.format(IDomObject.SELECT_OPTION, 
							objectValueTo.getValue(), IDomObject.OPTION_SELECTED, objectValueTo.getName()) );
				} else if(selectValueList != null && selectValueList.isEmpty() && objectValueTo.getName() != null && objectValueTo.getName().indexOf("Default") != -1) {
					// this block is for form/level
					str.append( String.format(IDomObject.SELECT_OPTION, 
							objectValueTo.getValue(), IDomObject.OPTION_SELECTED, objectValueTo.getName()) );
				} else {
					str.append( String.format(IDomObject.SELECT_OPTION, 
							objectValueTo.getValue(), "", objectValueTo.getName()) );
				}
			}
		}
		str.append( IDomObject.SELECT_END );
		if(isMultiselect) {
			str.append( String.format(IDomObject.SELECT_ALL, name, tabCount) );
		}
		if(seperated) str.append( getInputControlContainerSeparated(false) );
		else str.append( getInputControlContainer(false) );
		return str.toString();
	}
	
	/**
	 * Returns DOM object for text box - including date and min and max validation classes
	 * @param inputControlTO
	 * @return
	 */
	public String getTextInputControl(InputControlTO inputControlTO, boolean seperated) {
		StringBuilder str = new StringBuilder();
		if(seperated) str.append( getInputControlContainerSeparated(true) );
		else str.append( getInputControlContainer(true) );
		//str.append( getInputControlContainer(true) );
		str.append( getSelectBoxLabel(inputControlTO.getLabel()) );
		StringBuilder classes = new StringBuilder();
		if(IApplicationConstants.FIELD_TYPE_DATE.equals(inputControlTO.getFieldType())) {
			classes.append(" jqdatepicker validate[custom[date]]");
			classes.append(" jqdatepicker-" + inputControlTO.getTabCount());
		}
		String validationText = "";
		if(inputControlTO.getMinLength() != null && inputControlTO.getMaxLength() != null) {
			validationText = "minSize["+inputControlTO.getMinLength()+"],maxSize["+inputControlTO.getMaxLength()+"]";
		} else if(inputControlTO.getMinLength() != null) {
			validationText = "minSize["+inputControlTO.getMinLength()+"]";
		} else if(inputControlTO.getMaxLength() != null) {
			validationText = "maxSize["+inputControlTO.getMaxLength()+"]";
		}
		if(inputControlTO.isMandatory() && validationText.length() > 0) {
			classes.append(" validate[required, "+validationText+"]");
		} else if(validationText.length() > 0) {
			classes.append(" validate["+validationText+"]");
		}
		str.append( String.format(
				IDomObject.TEXT_BOX_UPDATED, inputControlTO.getLabelId(), 
				inputControlTO.getLabelId()+inputControlTO.getTabCount(), 
				inputControlTO.getTextValue(),classes.toString())
				);
		str.append( getInputControlContainer(false) );
		return str.toString();
	}
	
	@Deprecated
	/**
	 * New method accepts only one parameter - inputControlTO
	 */
	public String getTextInputControl(String value, String title, String name, boolean isTextBox, String assessmentId) {
		StringBuilder str = new StringBuilder();
		str.append( getInputControlContainer(true) );
		str.append( getSelectBoxLabel(title) );
		str.append( String.format(IDomObject.TEXT_BOX, name, name, value) );
		str.append( getInputControlContainer(false) );
		return str.toString();
	}
	
	/**
	 * Get options for a select box. This method is called for cascading.
	 * @param objectValueToList
	 * @param title
	 * @param name
	 * @param reportUrl
	 * @param isMultiselect
	 * @return
	 */
	public String getOptionsForSelect(List<ObjectValueTO> objectValueToList, 
			String title, String name, String reportUrl, boolean isMultiselect, 
			Map<String, String[]> defaultValues, List<String> defaultInputNames, Map<String, String[]> sessionParams) {
		StringBuilder str = new StringBuilder();
		if(objectValueToList == null) {
			objectValueToList = new ArrayList<ObjectValueTO>();
			ObjectValueTO objectValueTo = new ObjectValueTO();
			objectValueTo.setName(" ");
			objectValueTo.setValue("0");
			objectValueToList.add(objectValueTo);
		}
		List<String> defaultValueList = null;
		/*if(defaultValues != null) {
			defaultValueList = Arrays.asList(defaultValues);
		}*/
		// patch for Longitudinal
		boolean isLongitudinal = false;
		boolean isFirstSelected = false;
		if(IApplicationConstants.EXTENDED_YEAR.equals(name)) {
			isLongitudinal = true;
		}
		// End : patch for Longitudinal
		for (ObjectValueTO objectValueTo : objectValueToList) {
			if(isMultiselect) {
				if(defaultInputNames != null && defaultInputNames.contains(name)) {
					if(defaultValues.get(name) != null) defaultValueList = Arrays.asList(defaultValues.get(name));
					if(defaultValueList != null && defaultValueList.contains(objectValueTo.getValue())) {
						str.append( String.format(IDomObject.SELECT_OPTION, 
								objectValueTo.getValue(), IDomObject.OPTION_SELECTED, objectValueTo.getName()) );
					} else {
						str.append( String.format(IDomObject.SELECT_OPTION, 
								objectValueTo.getValue(), "", objectValueTo.getName()) );
					}
				} else {
					if(isLongitudinal && isFirstSelected) {
						str.append( String.format(IDomObject.SELECT_OPTION, 
								objectValueTo.getValue(), "", objectValueTo.getName()) );
					} else {
						str.append( String.format(IDomObject.SELECT_OPTION, 
								objectValueTo.getValue(), IDomObject.OPTION_SELECTED, objectValueTo.getName()) );
					}
					if(isLongitudinal && !isFirstSelected) {
						isFirstSelected = true;
					}
				}
			} else {
				if(objectValueTo.getName() != null && objectValueTo.getName().indexOf("Default") != -1) {
					// this block is for form/level
					str.append( String.format(IDomObject.SELECT_OPTION, 
							objectValueTo.getValue(), IDomObject.OPTION_SELECTED, objectValueTo.getName()) );
				} else {
					if(sessionParams != null && sessionParams.get(name) != null) {
						String[] sessionVal = sessionParams.get(name);
						if(sessionVal.length > 0 && objectValueTo.getValue().equals(sessionVal[0])) {
							str.append( String.format(IDomObject.SELECT_OPTION, 
									objectValueTo.getValue(), IDomObject.OPTION_SELECTED, objectValueTo.getName()) );
						} else {
							str.append( String.format(IDomObject.SELECT_OPTION, 
									objectValueTo.getValue(), "", objectValueTo.getName()) );
						}
					} else {
						str.append( String.format(IDomObject.SELECT_OPTION, 
								objectValueTo.getValue(), "", objectValueTo.getName()) );
					}
				}
			}
		}
		return str.toString();
	}
	
	/**
	 * This method returns input control label
	 * @param title
	 * @return
	 */
	private String getSelectBoxLabel(String title) {
		return  String.format(IDomObject.IC_TITLE, title);
	}
	
	/**
	 * This box returns select box container
	 * @param title
	 * @return
	 */
	private String getInputControlContainer(boolean startTag) {
		if(startTag) return IDomObject.IC_CONTAINER;
		else return IDomObject.DIV_END;
	}
	
	/**
	 * This box returns select box container separated in two section
	 * @param title
	 * @return
	 */
	private String getInputControlContainerSeparated(boolean startTag) {
		if(startTag) return IDomObject.IC_CONTAINER_NEW;
		else return IDomObject.DIV_END;
	}
	
	/**
	 * This box returns select section container separated in two section
	 * @param title
	 * @return
	 */
	public String getInputSectionWrapper(boolean startTag) {
		if(startTag) return CustomStringUtil.appendString(IDomObject.IC_BOX_ONE, IDomObject.IC_SECTION_WRAPPER);
		else return CustomStringUtil.appendString(IDomObject.DIV_END, IDomObject.DIV_END);
	}
	
	/**
	 * This box returns select section container separated in two section
	 * @param title
	 * @return
	 */
	public String getInputSectionWrapperTwo(boolean startTag) {
		if(startTag) return CustomStringUtil.appendString(IDomObject.IC_BOX_TWO, IDomObject.IC_SECTION_WRAPPER);
		else return CustomStringUtil.appendString(IDomObject.DIV_END, IDomObject.DIV_END);
	}
	
}
