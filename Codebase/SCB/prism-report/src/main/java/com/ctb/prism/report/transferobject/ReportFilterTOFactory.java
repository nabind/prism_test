package com.ctb.prism.report.transferobject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctb.prism.report.business.IReportBusiness;

/**
 * This class is used to generate pojo runtime
 * @author Amit Dhara
 *
 */

@Component("reportFilterFactory")
public class ReportFilterTOFactory implements IReportFilterTOFactory {
	
	@Autowired 
	private IReportBusiness reportBusiness;
	
	private Class<?> clazz = null;

	public ReportFilterTOFactory() {
		try {
			
			//generateClass("com.ctb.prism.report.transferobject.ReportFilterTO");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void generateClass(String className) throws NotFoundException, CannotCompileException {
		Class<List<ObjectValueTO>> genericList = (Class) List.class;
		
		Map<String, Class<?>> props = new HashMap<String, Class<?>>();
		List<InputControlTO> inputControls = reportBusiness.getAllInputControls();
		for(InputControlTO inputControl : inputControls) {
			props.put(inputControl.getLabelId(), genericList);
		}
		props.put("LoggedInUserName", String.class);
		props.put("LoggedInUserJasperOrgId", String.class);
		props.put("LoggedInUserId",  String.class);
		props.put("p_customerid",  String.class);
		
		//ReportFilterTOFactory fact = new ReportFilterTOFactory();
		//clazz = ReportFilterTOFactory.generate("com.ctb.prism.report.transferobject.ReportFilterTO", props);
		
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.makeClass(className);

		// add this to define a super class to extend
		// cc.setSuperclass(resolveCtClass(MySuperClass.class));

		// add this to define an interface to implement
		cc.addInterface(resolveCtClass(Serializable.class));

		for (Entry entry : props.entrySet()) {
			cc.addField(new CtField(resolveCtClass((Class) entry.getValue()), (String) entry.getKey(), cc));

			// add getter
			cc.addMethod(generateGetter(cc, (String) entry.getKey(), (Class) entry.getValue()));

			// add setter
			cc.addMethod(generateSetter(cc, (String) entry.getKey(), (Class) entry.getValue()));
		}
		
		clazz = cc.toClass();
	}
	
	public Class<?> getReportFilterTO() {
		if(clazz == null)
			try {
				generateClass("com.ctb.prism.report.transferobject.ReportFilterTO");
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (CannotCompileException e) {
				e.printStackTrace();
			} 
		return clazz;
	}
	
	public Class generate(String className, Map<String, Class<?>>  properties) throws NotFoundException,
			CannotCompileException {

		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.makeClass(className);

		// add this to define a super class to extend
		// cc.setSuperclass(resolveCtClass(MySuperClass.class));

		// add this to define an interface to implement
		cc.addInterface(resolveCtClass(Serializable.class));

		for (Entry entry : properties.entrySet()) {
			cc.addField(new CtField(resolveCtClass((Class) entry.getValue()), (String) entry.getKey(), cc));

			// add getter
			cc.addMethod(generateGetter(cc, (String) entry.getKey(), (Class) entry.getValue()));

			// add setter
			cc.addMethod(generateSetter(cc, (String) entry.getKey(), (Class) entry.getValue()));
		}

		return cc.toClass();
	}

	private static CtMethod generateGetter(CtClass declaringClass, String fieldName, Class fieldClass)
			throws CannotCompileException {

		String getterName = "get" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);

		StringBuffer sb = new StringBuffer();
		sb.append("public ").append(fieldClass.getName()).append(" ")
				.append(getterName).append("(){").append("return this.")
				.append(fieldName).append(";").append("}");
		return CtMethod.make(sb.toString(), declaringClass);
	}

	private static CtMethod generateSetter(CtClass declaringClass, String fieldName, Class fieldClass)
			throws CannotCompileException {

		String setterName = "set" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);

		StringBuffer sb = new StringBuffer();
		sb.append("public void ").append(setterName).append("(")
				.append(fieldClass.getName()).append(" ").append(fieldName)
				.append(")").append("{").append("this.").append(fieldName)
				.append("=").append(fieldName).append(";").append("}");
		return CtMethod.make(sb.toString(), declaringClass);
	}

	private static CtClass resolveCtClass(Class clazz) throws NotFoundException {
		ClassPool pool = ClassPool.getDefault();
		return pool.get(clazz.getName());
	}
	
	
}