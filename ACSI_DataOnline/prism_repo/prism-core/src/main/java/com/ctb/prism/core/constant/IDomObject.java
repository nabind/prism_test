package com.ctb.prism.core.constant;

/**
 * This interface contains the constants used in the application
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public interface IDomObject {
	
	public static final String SELECT_SINGLE = "<select style='width:150px' name='%s' id='%s' param='%s' count='%s' assessment='%s' rel='%s' class='select navy-gradient compact' onChange='getCascading($(this))'>";
	public static final String SELECT_SINGLE_EXPANDABLE = "<select style='width:150px' name='%s' id='%s' param='%s' count='%s' assessment='%s' rel='%s' class='select navy-gradient compact expandable-list' onChange='getCascading($(this))'>";
	public static final String SELECT_OPTION = "<option value='%s' %s>%s</option>";
	public static final String SELECT_END = "</select>";
	public static final String SELECT_MULTIPLE = "<select style='width:150px' name='%s' id='%s' param='%s' count='%s' assessment='%s' rel='%s' class='select navy-gradient compact multiple-as-single easy-multiple-selection check-list' onChange='getCascading($(this))' multiple>";
	public static final String SELECT_MULTIPLE_EXPANDABLE = "<select style='width:150px' name='%s' id='%s' param='%s' count='%s' assessment='%s' rel='%s' class='select navy-gradient compact expandable-list multiple-as-single easy-multiple-selection check-list' onChange='getCascading($(this))' multiple>";
	
	public static final String IC_TITLE = "<h6 class='margin-bottom-small'>%s</h6>";
	
	public static final String IC_CONTAINER = "<div class='three-columns report-inputs'>";
	public static final String IC_CONTAINER_NEW = "<div class='six-columns report-inputs'>";
	
	public static final String DIV_END = "</div>";
	
	public static final String OPTION_SELECTED = "selected";
	
	public static final String TEXT_BOX = "<input type='text' name='%s' id='%s' value='%s' style='width:128px' class='input input-compact navy-gradient'>";
	
	public static final String IC_BOX_ONE = "<div class='six-columns input-control-box'>";
	public static final String IC_BOX_TWO = "<div class='six-columns input-control-box-2'>";
	public static final String IC_SECTION_WRAPPER = "<div class='columns margin-bottom-medium margin-bottom-medium-ve'>";
}
