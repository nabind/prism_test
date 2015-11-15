package com.ctb.prism.webservice.erTransferobject;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
@XStreamAlias("StudentList")
public class StudentList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5477773094826787095L;

	private List<StudentDetails> studentDetails;

	public List<StudentDetails> getStudentDetails() {
		return studentDetails;
	}

	public void setStudentDetails(List<StudentDetails> studentDetails) {
		this.studentDetails = studentDetails;
	}
	
}
