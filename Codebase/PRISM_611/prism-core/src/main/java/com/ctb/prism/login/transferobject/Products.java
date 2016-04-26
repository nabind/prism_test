package com.ctb.prism.login.transferobject;

import java.io.Serializable;

public class Products implements Serializable
{
    private String name;

    private String seq;

    private String fileLoc;

    private String code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getFileLoc() {
		return fileLoc;
	}

	public void setFileLoc(String fileLoc) {
		this.fileLoc = fileLoc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

    
}
