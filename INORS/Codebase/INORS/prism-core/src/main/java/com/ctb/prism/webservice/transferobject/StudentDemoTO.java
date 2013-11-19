package com.ctb.prism.webservice.transferobject;

import java.util.List;

public class StudentDemoTO {

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
