import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

	public static final String AES_DECRYPTION_PROPERTIES_FILE = "aesDecryption.properties";
	public static final String XML_FILE_PATH_KEY = "XML_FILE_PATH";
	public static final String PRIVATEKEY_KEY = "PRIVATE_KEY";
	public static final String DEMO_ENCRYPTED_ATTRIBUTE_KEY = "DEMO_ENCRYPTED_ATTR";
	public static final String LOG4J_FILE_APPENDER_KEY = "LOG4J_FILE_APPENDER_FILE";
	public static final String LOG4J_FILE_THRESHOLD_KEY = "LOG4J_FILE_THRESHOLD_KEY";
	public static Properties prop;
	
	private static final Logger logger = Logger.getLogger(DecryptXMLAttribute.class);
	
	public static void main(String[] args) {
		try {
			prop = getProperties();
			initLogger();
			if (args.length < 1) {
				logger.error("Please provide XML file name");
			} else {		
			AESEncryptionDecryption aes = new AESEncryptionDecryption();			
			
			String finalXmlFilePath = prop.getProperty(XML_FILE_PATH_KEY);
			if (finalXmlFilePath != null) {
				finalXmlFilePath = finalXmlFilePath + args[0];
			}
			String[] attrs = prop.getProperty(DEMO_ENCRYPTED_ATTRIBUTE_KEY).split(",");
			
			logger.info("Starting the parsing of the file :"+args[0]);
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(finalXmlFilePath);

			// Get the root element
			Node report = doc.getFirstChild();
			NodeList list = report.getChildNodes();

			for (int i = 0; i < list.getLength(); i++) {
				Node candidate = list.item(i);
				if ("Candidate".equalsIgnoreCase(candidate.getNodeName())) {
					NodeList demoList = candidate.getChildNodes();
					for (int j = 0; j < demoList.getLength(); j++) {
						Node demographic = demoList.item(j);
						if ("Demographic".equalsIgnoreCase(demographic.getNodeName())) {
							NamedNodeMap attributes = demographic.getAttributes();
							
							for (String encAttr : attrs) {
								Node encryptedAttribute = attributes.getNamedItem(encAttr);
								if (encryptedAttribute != null) {
									logger.info("Decrypting the attribute :"+encryptedAttribute.getNodeName());
									String decryptedValue = aes.decryptText(encryptedAttribute.getNodeValue(), prop.getProperty(PRIVATEKEY_KEY));
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
			StreamResult result = new StreamResult(new File(finalXmlFilePath));
			transformer.transform(source, result);
			logger.info("Process complete");

			}
		}			
		catch (Exception e) {			
			logger.error("Error occurred - "+e.getMessage());
			System.exit(1);
		}

		System.exit(0);
	}
	
	public static Properties getProperties() throws IOException{
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream in = loader.getResourceAsStream(AES_DECRYPTION_PROPERTIES_FILE);
		Properties prop = null;
		if (in != null) {
			prop = new Properties();
			prop.load(in);
		}
		return prop;
	}
	
	public static void initLogger() throws IOException{		 
		 RollingFileAppender rfapender = new RollingFileAppender();
		 
		 String conversionPattern = "%d{yyyy-MM-dd HH:mm:ss} %m%n";
		 PatternLayout layout = new PatternLayout(conversionPattern);
		 
		 rfapender.setFile(prop.getProperty(LOG4J_FILE_APPENDER_KEY));
		 rfapender.setLayout(layout);
		 rfapender.setThreshold(Level.toLevel(prop.getProperty(LOG4J_FILE_THRESHOLD_KEY)));
		 rfapender.setMaxFileSize("10MB");
		 rfapender.activateOptions();
	     logger.addAppender(rfapender);
	}

}
