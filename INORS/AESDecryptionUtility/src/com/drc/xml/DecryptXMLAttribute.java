package com.drc.xml;

import com.drc.aes.AESEncryptionDecryption;
import com.drc.util.ApplicationConstants;
import com.drc.util.PropertyLoader;
import com.drc.util.XSDValidator;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class DecryptXMLAttribute {

	private static final Logger logger = Logger.getLogger(DecryptXMLAttribute.class);
	
	public static Properties prop = PropertyLoader.loadProperties (ApplicationConstants.AES_DECRYPTION_PROPERTIES_FILE);
	
	public static void main(String[] args) {
		boolean isValid= false;
		try {
			initLogger();
			if (args.length < 1) {
				logger.error("Please provide XML file name");
			} else {		
				AESEncryptionDecryption aes = new AESEncryptionDecryption();			
				
				//get the xml file path from property file and append with the file name provided as input
				String finalXmlFilePath = prop.getProperty(ApplicationConstants.XML_FILE_PATH_KEY);
				if (finalXmlFilePath != null) {
					finalXmlFilePath = finalXmlFilePath + "\\" + args[0];
				}
				
				//this is the decrypted file which will be FTP for TX and then it will be renamed to remove .DECRYPTED suffix.
				String decryptedXMLFilePath = finalXmlFilePath.replace(".xml", ApplicationConstants.DECRYPTED_FILE_SUFFIX + ".xml");
				
				//get the encrypted attribute name which need to be decrypted under Demographic tag from properties file
				String[] attrs = prop.getProperty(ApplicationConstants.DEMO_ENCRYPTED_ATTRIBUTE_KEY).split(",");
				
				logger.info("Starting the parsing of the file : "+args[0]);
				
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(finalXmlFilePath);
	
				// Get the root element
				Node report = doc.getFirstChild();
				NodeList list = report.getChildNodes();
	
				for (int i = 0; i < list.getLength(); i++) {
					Node candidate = list.item(i);
					if (ApplicationConstants.CANDIDATE_TAG.equalsIgnoreCase(candidate.getNodeName())) {
						NamedNodeMap candidateAttributes = candidate.getAttributes();
						
						NodeList demoList = candidate.getChildNodes();
						for (int j = 0; j < demoList.getLength(); j++) {
							Node demographic = demoList.item(j);
							if (ApplicationConstants.DEMOGRAPHIC_TAG.equalsIgnoreCase(demographic.getNodeName())) {
								NamedNodeMap attributes = demographic.getAttributes();
								
								for (String encAttr : attrs) {
									Node encryptedAttribute = attributes.getNamedItem(encAttr);
									if (encryptedAttribute != null) {
										logger.info("Decrypting the attributes of Demographic for Canditate id : "+candidateAttributes.getNamedItem(ApplicationConstants.CANDIDATE_ID_ATTRIBUTE).getNodeValue());
										logger.info("Decrypting the attribute : "+encryptedAttribute.getNodeName());
										String decryptedValue = aes.decryptText(encryptedAttribute.getNodeValue(), prop.getProperty(ApplicationConstants.PRIVATEKEY_KEY));
										
										encryptedAttribute.setNodeValue(decryptedValue);
									}
								}
								
							}
						}
					}
	
				}
				
				logger.info("Writing the XML file with decrypted data");
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(decryptedXMLFilePath));
				transformer.transform(source, result);
				
				
				isValid = XSDValidator.validateXMLSchema(prop.getProperty(ApplicationConstants.XSD_FILE) + "\\" + args[1],
						decryptedXMLFilePath);
				if(isValid){
					logger.warn("XML validation success");
				} else {
					logger.warn("XML validation failed");
				}
				logger.info("Process complete");

			}
		}			
		catch (Exception e) {			
			logger.error("Error occurred - "+e.getMessage());
			System.exit(1);
		}

		System.exit(0);
	}
	
	
	
	public static void initLogger() throws IOException{		 
		 RollingFileAppender rfapender = new RollingFileAppender();
		 
		 String conversionPattern = "%d{yyyy-MM-dd HH:mm:ss} %m%n";
		 PatternLayout layout = new PatternLayout(conversionPattern);
		 
		 rfapender.setFile(prop.getProperty(ApplicationConstants.LOG4J_FILE_APPENDER_KEY));
		 rfapender.setLayout(layout);
		 rfapender.setThreshold(Level.toLevel(prop.getProperty(ApplicationConstants.LOG4J_FILE_THRESHOLD_KEY)));
		 rfapender.setMaxFileSize("10MB");
		 rfapender.activateOptions();
	     logger.addAppender(rfapender);
	}

}
