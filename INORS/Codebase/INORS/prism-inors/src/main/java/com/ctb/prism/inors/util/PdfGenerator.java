package com.ctb.prism.inors.util;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public class PdfGenerator {
	
	private static Font font = FontFactory.getFont("Arial", 9.0F, 0, new Color(0, 0, 0));
	private static Font fontCourier = FontFactory.getFont("Arial", 9.0F, 0, new Color(0, 0, 0));
	private static Font fontBold = FontFactory.getFont("Arial", 9.0F, Font.BOLD, new Color(0, 0, 0));
	private static Font tableHeaderFont = FontFactory.getFont("Arial", 9.0F, 1, new Color(0, 0, 0));
	private static Font tableFont = FontFactory.getFont("Arial", 9.0F, 0, new Color(0, 0, 0));
	
	/**
	 * Generates PDF for query sheet
	 * @param bulkDownloadTO
	 * @param propertyLookup
	 * @return
	 */
	public static String generateQuerysheet(BulkDownloadTO bulkDownloadTO, IPropertyLookup propertyLookup) {
		Document document = null;
		String docName, docLoc = null;
		try {
			document = new Document(PageSize.A4, 50.0F, 50.0F, 20.0F, 75.0F);
			docLoc = CustomStringUtil.appendString(propertyLookup.get("pdfGenPath"), File.separator, bulkDownloadTO.getQuerysheetFile());
			
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(docLoc));
			addMetaData(document);
			
			document.open();
			document.add(new Paragraph("Generated File Name: " + bulkDownloadTO.getQuerysheetFile(), font));
			document.add(new Paragraph("Date of File Generation Request: " + Utils.getDateTime("dd/MM/yyyy hh:mm:ss a"), font));
			document.add(new Paragraph("Test Administration: " + bulkDownloadTO.getTestAdministration(), font));
			document.add(new Paragraph("Test Program: " + bulkDownloadTO.getTestProgram(), font));
			document.add(new Paragraph("Corp/Diocese: " + bulkDownloadTO.getCorp(), font));
			document.add(new Paragraph("School: " + bulkDownloadTO.getSchool(), font));
			document.add(new Paragraph("Grade: " + bulkDownloadTO.getGrade(), font));
			document.add(new Paragraph("Class: " + bulkDownloadTO.getOrgClass(), font));
			
			document.add(new Paragraph("\n\n"));
			
			document.add(new Paragraph(CustomStringUtil.appendString(
					""+bulkDownloadTO.getStudentCount(), " student(s) in ",
					""+bulkDownloadTO.getClassCount(), " class(es) in ",
					""+bulkDownloadTO.getSchoolCount(), " school(s) have been selected."), font));
			
			document.add(new Paragraph("\n\n"));
			
			document.add(new Paragraph("School Name: ", font));
			document.add(new Paragraph(bulkDownloadTO.getSchool(), font));
			
			document.add(new Paragraph("\n\n"));
			
			document.add(new Paragraph("Classes: ", font));
			document.add(new Paragraph(bulkDownloadTO.getOrgClass(), font));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(document != null) document.close();
		}
		return docLoc;
	}
	
	/**
	 * generate query sheet for bulk candidate report download
	 * @param bulkDownloadTO
	 * @param propertyLookup
	 * @return
	 */
	public static String generateQuerysheetForCR(BulkDownloadTO bulkDownloadTO, IPropertyLookup propertyLookup) {
		Document document = null;
		String docName, docLoc = null;
		try {
			document = new Document(PageSize.A4, 50.0F, 50.0F, 20.0F, 75.0F);
			docLoc = CustomStringUtil.appendString(propertyLookup.get("pdfGenPath"), File.separator, bulkDownloadTO.getQuerysheetFile());
			
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(docLoc));
			addMetaData(document);
			
			document.open();
			document.add(new Paragraph("Generated File Name: " + bulkDownloadTO.getQuerysheetFile(), font));
			document.add(new Paragraph("Date of File Generation Request: " + Utils.getDateTime("dd/MM/yyyy hh:mm:ss a"), font));
			
			document.add(new Paragraph("\n\n"));
			
			document.add(new Paragraph("Start Date: " + bulkDownloadTO.getTestAdministration(), font));
			document.add(new Paragraph("End Date: " + bulkDownloadTO.getTestProgram(), font));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(document != null) document.close();
		}
		return docLoc;
	}
	
	/**
	 * Add metadata to PDF file
	 * @param document
	 */
	private static void addMetaData(Document document) {
		document.addTitle("Inors");
		document.addAuthor("Inors");
		document.addCreator("Inors-CTB");
		document.addSubject("Query Sheet");
	}
	
	public static void main(String[] args) {
		BulkDownloadTO bulkDownloadTO = new BulkDownloadTO();
		generateQuerysheet(bulkDownloadTO, null);
	}
	
	/**
	 * split pdfs into multiple pages
	 * 
	 * @param inputStream Input PDF file
	 * @param outputStream Output PDF file
	 * @param fromPage start page from input PDF file
	 * @param toPage end page from input PDF file
	 */
	public static void splitPDF(InputStream inputStream,
	        OutputStream outputStream, int fromPage, int toPage) {
	    Document document = new Document();
	    try {
	        PdfReader inputPDF = new PdfReader(inputStream);
	 
	        int totalPages = inputPDF.getNumberOfPages();
	 
	        //make fromPage equals to toPage if it is greater
	        if(fromPage > toPage ) {
	            fromPage = toPage;
	        }
	        if(toPage > totalPages) {
	            toPage = totalPages;
	        }
	 
	 
	        // Create a writer for the outputstream
	        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
	 
	        document.open();
	        PdfContentByte cb = writer.getDirectContent(); // Holds the PDF data
	        PdfImportedPage page;
	 
	        while(fromPage <= toPage) {
	            document.newPage();
	            page = writer.getImportedPage(inputPDF, fromPage);
	            cb.addTemplate(page, 0, 0);
	            fromPage++;
	        }
	        outputStream.flush();
	        document.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	    	if (document.isOpen()) document.close();
			IOUtils.closeQuietly(outputStream);
			IOUtils.closeQuietly(inputStream);
	    }
	}
	
	/**
	 * concat pdfs 
	 * @param listOfPDFFiles
	 * @param outputStream
	 * @param paginate
	 */
	public static void concatPDFs(List<String> listOfPDFFiles,
            OutputStream outputStream, boolean paginate) {
 
        Document document = new Document();
        try {
            List<String> pdfs = listOfPDFFiles;
            List<PdfReader> readers = new ArrayList<PdfReader>();
            int totalPages = 0;
            Iterator<String> iteratorPDFs = pdfs.iterator();
 
            // Create Readers for the pdfs.
            while (iteratorPDFs.hasNext()) {
                InputStream pdf = new FileInputStream(iteratorPDFs.next());
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages += pdfReader.getNumberOfPages();
            }
            // Create a writer for the outputstream
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
 
            document.open();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
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
                    page = writer.getImportedPage(pdfReader,
                            pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);
 
                    // Code for pagination.
                    if (paginate) {
                        cb.beginText();
                        cb.setFontAndSize(bf, 9);
                        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, ""
                                + currentPageNumber + " of " + totalPages, 520,
                                5, 0);
                        cb.endText();
                    }
                }
                pageOfCurrentReaderPDF = 0;
            }
            outputStream.flush();
            document.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	if (document.isOpen()) document.close();
			IOUtils.closeQuietly(outputStream);
        }
    }
	
	/**
	 * Archive files
	 * @param files
	 * @param zipName
	 * @throws Exception
	 */
	public static void zipit(List<String> files, List<String> arcFiles, String zipName) throws Exception {
    	ZipOutputStream zos = null;
    	FileOutputStream fos = null;
    	BufferedInputStream bis = null;
        try {
        	//System.out.println("Compressing pdfs ... ");
			File zipFile = new File(zipName);
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(fos);
			int bytesRead;
			byte[] buffer = new byte[1024];
			CRC32 crc = new CRC32();
			int compressCount = 0;
			for (String name : files) {
				String archiveFileName = arcFiles.get(compressCount);
				//System.out.println("Compressing "+ ++compressCount + " of " + files.size());
			    File file = new File(name);
			    if (!file.exists()) {
			        System.err.println("Skipping: " + name);
			        continue;
			    }
			    bis = new BufferedInputStream(new FileInputStream(file));
			    crc.reset();
			    while ((bytesRead = bis.read(buffer)) != -1) {
			        crc.update(buffer, 0, bytesRead);
			    }
			    bis.close();
			    // Reset to beginning of input stream
			    bis = new BufferedInputStream(new FileInputStream(file));
			    ZipEntry entry = new ZipEntry(archiveFileName/*file.getName()*/);
			    entry.setMethod(ZipEntry.STORED);
			    entry.setCompressedSize(file.length());
			    entry.setSize(file.length());
			    entry.setCrc(crc.getValue());
			    zos.putNextEntry(entry);
			    while ((bytesRead = bis.read(buffer)) != -1) {
			        zos.write(buffer, 0, bytesRead);
			    }
			    bis.close();
			}
			zos.close();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			IOUtils.closeQuietly(zos);
			IOUtils.closeQuietly(fos);
			IOUtils.closeQuietly(bis);
		}
		//System.out.println("Compressing completed ... ");
    }
	
}
