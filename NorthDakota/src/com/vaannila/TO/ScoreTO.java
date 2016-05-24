package com.vaannila.TO;

import java.util.List;

public class ScoreTO {
	
	private long studentBioId;
	
	private List<ContentTO> content;

	public List<ContentTO> getContent() {
		return content;
	}

	public void setContent(List<ContentTO> content) {
		this.content = content;
	}

	public long getStudentBioId() {
		return studentBioId;
	}

	public void setStudentBioId(long studentBioId) {
		this.studentBioId = studentBioId;
	}
	
	
	
}
