package com.ctb.prism.report.ipcontrol;

import java.util.List;
import java.util.Map;

import com.ctb.prism.report.transferobject.ObjectValueTO;



public interface InputControlFactory {

	public String getSelectInputControlEmptyBox();
	public String getSelectInputControl(List<ObjectValueTO> objectValueToList, 
			String title, String name, String reportUrl, String tabCount, boolean isMultiselect, 
			boolean isTextBox, String assessmentId, Map<String, Object> parameters, boolean seperated);
	
	public String getTextInputControl(String value, String title, String name, boolean isTextBox, String assessmentId);
	
	public String getOptionsForSelect(List<ObjectValueTO> objectValueToList, 
			String title, String name, String reportUrl, boolean isMultiselect, 
			Map<String, String[]> defaultValues, List<String> defaultInputNames);
	
	public String getInputSectionWrapper(boolean startTag);
	public String getInputSectionWrapperTwo(boolean startTag);
}
