import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

import org.apache.commons.io.FileUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;


public class HTMLToPDF {
	 public static void main(String[] args) {
		/*try {
		    String k = "<html><body> This is my Project </body></html>";
		    OutputStream file = new FileOutputStream(new File("D:\\PoC\\Test1.pdf"));
		    Document document = new Document();
		    PdfWriter writer = PdfWriter.getInstance(document, file);
		    document.open();
		    InputStream is = new ByteArrayInputStream(k.getBytes());
		    XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
		    document.close();
		    file.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}*/
		 
		 try {
			    String str = "<html><body> This is my Project </body></html>";
			  //  File inputFile = new File("D:\\PoC\\TASCDataonline.html");
			  //  String str = FileUtils.readFileToString(inputFile, "UTF-8");
			    OutputStream file = new FileOutputStream(new File("D:\\PoC\\Test3.pdf"));
			    Document document = new Document();
			    PdfWriter.getInstance(document, file);
			    document.open();
			    HTMLWorker htmlWorker = new HTMLWorker(document);
			    htmlWorker.parse(new StringReader(str));
			    document.close();
			    file.close();
			} catch (Exception e) {
			    e.printStackTrace();
			} 
	 }
}
