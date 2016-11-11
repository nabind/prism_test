
import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import bean.RootTO;

public class FileProcessor {

	public static void main(String[] args) {
		File file = new File("D:\\Somenath\\PortalXMLOutput.xml");
		XmlParser xmlParser = new XmlParser();
		try {
			RootTO rootTO = xmlParser.getObjectFromXML(file);
			System.out.println(rootTO);
			ExcelFileGenerator excelFileGenerator = new ExcelFileGenerator();
			excelFileGenerator.writeToFile(
					"D:\\Somenath\\PortalExcelOutput.xls", rootTO);
			System.out.println("File generated successfully..");
		} catch (JAXBException | IOException e) {
			e.printStackTrace();
		}
	}

}
