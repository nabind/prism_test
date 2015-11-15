package com.ctb.prism.admin.transferobject.studentdata;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Demo")
public class DemoTO {

	@XStreamAsAttribute
	@XStreamAlias("demo_name")
	private String demoName;

	@XStreamAsAttribute
	@XStreamAlias("demo_value")
	private String demovalue;

	public String getDemoName() {
		return demoName;
	}

	public void setDemoName(String demoName) {
		this.demoName = demoName;
	}

	public String getDemovalue() {
		return demovalue;
	}

	public void setDemovalue(String demovalue) {
		this.demovalue = demovalue;
	}

}
