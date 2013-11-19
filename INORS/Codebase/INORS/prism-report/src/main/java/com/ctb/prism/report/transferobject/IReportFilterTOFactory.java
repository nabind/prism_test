package com.ctb.prism.report.transferobject;

import java.util.Map;

import javassist.CannotCompileException;
import javassist.NotFoundException;

public interface IReportFilterTOFactory {
	public Class generate(String className, Map<String, Class<?>>  properties) throws NotFoundException, CannotCompileException;

	public Class<?> getReportFilterTO();
	
}
