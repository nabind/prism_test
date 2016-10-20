package com.drc.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public class XSDValidator {
	
	private static final Logger logger = Logger.getLogger(XSDValidator.class);
	public static Properties prop = PropertyLoader.loadProperties (ApplicationConstants.AES_DECRYPTION_PROPERTIES_FILE);
		   
	   public static boolean validateXMLSchema(String xsdPath, String xmlPath){
		   System.out.println();
		   logger.info("Usage : XSDValidator xsd: " +xsdPath+ " xml: " +xmlPath);
	      try {
	         SchemaFactory factory =
	            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	            Schema schema = factory.newSchema(new File(xsdPath));
	            Validator validator = schema.newValidator();
	            validator.validate(new StreamSource(new File(xmlPath)));
	      } catch (IOException e){
	         System.out.println("Exception: "+e.getMessage());
	         return false;
	      }catch(SAXException e1){
	         System.out.println("SAX Exception: "+e1.getMessage());
	         return false;
	      }
			
	      return true;
		
	   }
	   
}
