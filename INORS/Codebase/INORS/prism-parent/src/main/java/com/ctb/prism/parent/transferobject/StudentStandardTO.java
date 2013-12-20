package com.ctb.prism.parent.transferobject;


import java.util.ArrayList;
import java.util.List;
import com.ctb.prism.core.transferobject.BaseTO;

public class StudentStandardTO extends BaseTO{


	private static final long serialVersionUID = 1L;
	
	
	private ArrayList<String> standard;
	private ArrayList<String> indicators;
	
	
	public ArrayList<String> getStandard() {
		return standard;
	}
	public void setStandard(ArrayList<String> standard) {
		this.standard = standard;
	}
	public ArrayList<String> getIndicators() {
		return indicators;
	}
	public void setIndicators(ArrayList<String> indicators) {
		this.indicators = indicators;
	}
	
}
