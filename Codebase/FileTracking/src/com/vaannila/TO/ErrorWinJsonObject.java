package com.vaannila.TO;

import java.util.List;

public class ErrorWinJsonObject {
	
	long iTotalRecords;

    long iTotalDisplayRecords;

    String sEcho;

    String sColumns;

    List<ErrorDetailsWinTO> aaData;

    public long getiTotalRecords() {
    	return iTotalRecords;
    }

    public void setiTotalRecords(long iTotalRecords) {
    	this.iTotalRecords = iTotalRecords;
    }

    public long getiTotalDisplayRecords() {
    	return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
    	this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public String getsEcho() {
    	return sEcho;
    }

    public void setsEcho(String sEcho) {
    	this.sEcho = sEcho;
    }

    public String getsColumns() {
    	return sColumns;
    }

    public void setsColumns(String sColumns) {
    	this.sColumns = sColumns;
    }

    public List<ErrorDetailsWinTO> getAaData() {
        return aaData;
    }

    public void setAaData(List<ErrorDetailsWinTO> aaData) {
        this.aaData = aaData;
    }
	
}
