package com.ctb.prism.webservice.transferobject;

import java.io.Serializable;
import java.util.List;

public class StudentDemoTO implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4477773094826787095L;

	private boolean dataChanged = false;
	private List<DemoTO> collDemoTO;
	
	public boolean isDataChanged() {
		return dataChanged;
	}
	public void setDataChanged(boolean dataChanged) {
		this.dataChanged = dataChanged;
	}
	public List<DemoTO> getCollDemoTO() {
		return collDemoTO;
	}
	public void setCollDemoTO(List<DemoTO> collDemoTO) {
		this.collDemoTO = collDemoTO;
	}
	
	
}
