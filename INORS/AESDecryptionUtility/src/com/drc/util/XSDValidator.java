package com.drc.util;

import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XSDValidator {

	private static final Logger logger = Logger.getLogger(XSDValidator.class);

	public static boolean validateXMLSchema(Document doc) {
		try {
			SchemaFactory factory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			
			//XSD path is not needed as it will be validated against xsi:noNamespaceSchemaLocation attribute of XML
			Schema schema = factory.newSchema();
			
			Validator validator = schema.newValidator();
			
			//validate the DOM XML instead of saved file
			validator.validate(new DOMSource(doc));
		} catch (IOException e) {
			logger.error("Error in XSD validation : " + e.getMessage());
			return false;
		} catch (SAXException e1) {
			logger.error("Error in XSD validation : " + e1.getMessage());
			return false;
		}

		return true;

	}

}
