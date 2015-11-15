package com.ctb.prism.admin.transferobject.studentdata;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class StudentDemoTO {

	@XStreamAsAttribute
	@XStreamAlias("IsDataChange")
	private boolean dataChanged = false;

	@XStreamImplicit
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
