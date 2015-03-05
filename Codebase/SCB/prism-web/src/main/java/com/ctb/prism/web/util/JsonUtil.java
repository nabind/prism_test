package com.ctb.prism.web.util;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

public class JsonUtil {

	/**
	 * Convert ObjectValueTO list to json array
	 * @param objects
	 * @return
	 */
	public static String convertToJson(List<ObjectValueTO> objects) {
		StringBuilder jsonStr = new StringBuilder(); 
		int count = 0;
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("objectValue", ObjectValueTO.class);
		for(Iterator<ObjectValueTO> itr = objects.iterator(); itr.hasNext();) {
			if(count > 0) {
				jsonStr.append(",");
			}
			jsonStr.append( xstream.toXML(itr.next()) );
			count++;
		}
		return jsonStr.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String convertToJsonAdmin(List toList) {
		if(toList == null){
			return "[]";
		}
		StringBuilder jsonStr = new StringBuilder(); 
		int count = 0;
		jsonStr.append("[");
		//XStream xstream = new XStream(new JettisonMappedXmlDriver());//new JettisonMappedXmlDriver()
		XStream xstream = new XStream(new JsonHierarchicalStreamDriver() {
		    public HierarchicalStreamWriter createWriter(Writer writer) {
		        return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
		    }
		});
        xstream.setMode(XStream.NO_REFERENCES);
		for(Iterator itr = toList.iterator(); itr.hasNext();) {
			if(count > 0) {
				jsonStr.append(",");
			}
			jsonStr.append( xstream.toXML(itr.next()) );
			count++;
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
	
}
