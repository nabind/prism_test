import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import bean.RootTO;

public class XmlParser {

	public RootTO getObjectFromXML(File file) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(RootTO.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		RootTO rootTO = (RootTO) jaxbUnmarshaller.unmarshal(file);
		return rootTO;
	}

}
