package com.ctb.prism.inors.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;


public class ConcatPdf {
	private static String BLANK_PDF = "blank.pdf";
	
	/*static {
		Properties prop = PropertyFile.loadProperties("nd.properties");
		String exportLocation = prop.getProperty("imageSaveLoc");
		BLANK_PDF = exportLocation + File.separator + BLANK_PDF;
		BLANK_THREE_PDF = exportLocation + File.separator + BLANK_THREE_PDF;
	}*/
	

	/**
	 * Concat multiple PDFs - inserts blank page between two pdfs 
	 * @param streamOfPDFFiles
	 * @param outputStream
	 * @param paginate
	 * @throws Exception 
	 */
	public static void concatPDFs(java.util.List<String> streamOfPDFFiles,
			OutputStream outputStream, boolean paginate, String blankPdfLoc) throws Exception {

		Document document = new Document();
		try {
			java.util.List<String> pdfs = streamOfPDFFiles;
			java.util.List<PdfReader> readers = new ArrayList<PdfReader>();
			int totalPages = 0;
			int currentPageCount = 0;
			int pdfCount = 0;
			Iterator<String> iteratorPDFs = pdfs.iterator();

			// Create Readers for the pdfs.
			while (iteratorPDFs.hasNext()) {
				pdfCount++;
				System.out.println("merging pdf # "+pdfCount);
				InputStream pdf = new FileInputStream(iteratorPDFs.next());
				PdfReader pdfReader = new PdfReader(pdf);
				//PdfReader pdfReader = rotatePdf(pdf);
				readers.add(pdfReader);
				currentPageCount = pdfReader.getNumberOfPages();
				totalPages += pdfReader.getNumberOfPages();
				
				// Add blankPage
				if(pdfCount < pdfs.size()) {
					if(currentPageCount%2 != 0) {
						// current pdf ends with odd page -- need to add One blank page
						InputStream blankPdf = new FileInputStream(blankPdfLoc+File.separator+BLANK_PDF);
						pdfReader = new PdfReader(blankPdf);
						readers.add(pdfReader);
					} 
				}
			}
			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
			// data

			PdfImportedPage page;
			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();

				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					currentPageNumber++;
					page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);
				}
				pageOfCurrentReaderPDF = 0;
			}
			outputStream.flush();
			document.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
			
		} finally {
			if (document.isOpen())
				document.close();
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		OutputStream output = null;
		try {
			/*java.util.List<InputStream> pdfs = new ArrayList<InputStream>();
			pdfs.add(new FileInputStream("c:/Temp/School_32381_08001_7900_1332784178044.pdf"));
			pdfs.add(new FileInputStream("c:/Temp/School_32345_02046_5483_1332784175185.pdf"));
			output = new FileOutputStream("c:/Temp/merge1.pdf");
			ConcatPdf.concatPDFs(pdfs, output, false);*/
			
			java.util.List<String> pdfs = new ArrayList<String>();
			String path = "C:\\Temp\\AllPDF";
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles(); 
			for (int i = 0; i < listOfFiles.length; i++) {
				pdfs.add(listOfFiles[i].getAbsolutePath());
			}
			output = new FileOutputStream(path+File.separator+"mergeAll.pdf");
			ConcatPdf.concatPDFs(pdfs, output, false, "c:\\temp\\");
			  
			  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Rotate the PDF @ 90 degree
	 * @param src
	 * @param dest
	 * @throws Exception
	 */
	public static void rotatePdf(String src, String dest) throws Exception {
		PdfStamper stamper = null;
		PdfReader pdfReader = null;
		try {
			pdfReader = new PdfReader(src);
			int currentPageCount = pdfReader.getNumberOfPages();
			int rotaion;
			PdfDictionary pageDict;
			for (int i=1; i <= currentPageCount; i++){
				rotaion = pdfReader.getPageRotation(i);
				pageDict = pdfReader.getPageN(i);
				pageDict.put(PdfName.ROTATE, new PdfNumber(rotaion + 90));
			}
			stamper = new PdfStamper(pdfReader, new FileOutputStream(dest));
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if(stamper != null) stamper.close();
			if(pdfReader != null) pdfReader.close();
		}
	}
}