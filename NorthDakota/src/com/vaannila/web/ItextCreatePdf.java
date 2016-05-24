package com.vaannila.web;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.print.PrintTranscoder;



import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgTemplate;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaannila.DAO.CommonDAOImpl;
import com.vaannila.TO.ContentTO;
import com.vaannila.TO.OrgTO;
import com.vaannila.TO.ScoreTO;
import com.vaannila.TO.StudentScoresTO;
import com.vaannila.TO.StudentTO;
import com.vaannila.util.PropertyFile;


public class ItextCreatePdf {
	
	
	private static BaseFont bf2 = null;
	private static CommonDAOImpl commonDao = null;
	
	private HttpServletRequest request;
	public ItextCreatePdf(HttpServletRequest req) {
		request = req;
	}
	
	private String districtNumber = "";
	private String schoolNumber = "";
	
	static {
		try {
			commonDao = new CommonDAOImpl();
			bf2 = BaseFont.createFont("CALIBRI.TTF", BaseFont.CP1252, BaseFont.EMBEDDED);
		} catch (Exception e) {
			try {bf2 = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);} catch (Exception e1) {}
			System.out.println("CALIBRI.TTF file not found. Using default font.");
		}
	}
	
	private static Font font =  new Font(bf2, 11);
	private static Font fontBold =  new Font(bf2, 11, 1);
	private static Font fontScore =  new Font(bf2, 10);
	private static Font fontBoldScore =  new Font(bf2, 10, 1);
	private static Font font1 =  new Font(bf2, 18, 1);
	private static Font font2 =  new Font(bf2, 14);
	private static Font font3 =  new Font(bf2, 14, 1);
	private static Font footerFont =  new Font(bf2, 9);
	
	private static final BaseColor headerBorder = new BaseColor(70, 170, 197);
	private static final BaseColor tableShading = new BaseColor(238,236,225);
	private static final BaseColor tableShadingWhite = new BaseColor(255, 255, 255);
	private static final BaseColor tableBorder = new BaseColor(0,0,0);
	private static final BaseColor footerBorder = new BaseColor(221, 221, 221);
	
	private static StringBuffer fileLoc = null;
	
	private PdfWriter writer = null;
	
	/**
	 * Returns Document object, with new PDF name
	 * @param nodeId
	 * @return
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 */
	private Document getDocument(String nodeId, String fileName) throws FileNotFoundException, DocumentException {
		Document document = new Document();
		
		/*if(request != null) {
			java.util.List<OrgTO> allNodes = (java.util.List<OrgTO>) request.getSession().getAttribute("allNodes");
			for(OrgTO node : allNodes) {
				if(nodeId.equals(""+node.getNodeId())) {
					districtNumber = node.getDistrictNumber();
					schoolNumber = node.getSchoolNumber();
					break;
				}
			}
		}
		
		Properties prop = PropertyFile.loadProperties("nd.properties");
		String exportLocation = prop.getProperty("imageSaveLoc");
		
		fileLoc = new StringBuffer();
		fileLoc.append(exportLocation).append(File.separator);
		fileLoc.append("Projection_").append(nodeId).append("_").append(districtNumber).append("_").append(schoolNumber);
		fileLoc.append("_").append(System.currentTimeMillis()).append(".pdf");*/
		
		writer = PdfWriter.getInstance(document, new FileOutputStream( fileName ));
		if(request != null) request.getSession().setAttribute("pdfFileName_"+nodeId, fileName/*fileLoc.toString()*/);
		document.setPageSize(PageSize.A4);
		document.setMargins(30, 30, 5, 10);
		document.setMarginMirroring(true);
		
		return document;
	}
	
	/**
	 * Create PDF for students 
	 * @param allStudents
	 * @param nodeId
	 * @return
	 * @throws Exception
	 */
	public String generateGrowthPdf(java.util.List<StudentTO> allStudents, String nodeId, String fileName) throws Exception {
		java.util.List<String> allGraph = new ArrayList<String>();
		Document document = getDocument(nodeId, fileName);
		document.open();
		int pageCount = 0;
		
		if(commonDao == null) commonDao = new CommonDAOImpl();
		int totalPages = allStudents.size()*2;
		// TODO REMOVE *************************
		//totalPages = 4;
		// TODO REMOVE *************************
		if(request != null) {
			request.getSession().setAttribute("pdf_totalpage_"+nodeId, totalPages);
		}
		
		StringBuffer studBioId = new StringBuffer();
		boolean firstRec = true;
		for(StudentTO studentTO : allStudents) {
			if(!firstRec) studBioId.append(",");
			else firstRec = false;
			studBioId.append(studentTO.getStudentBioId());
		}
		List<StudentScoresTO> allStudScore = commonDao.getStudentScore(studBioId.toString());
		
		String contentName = "";
		for(StudentTO studentTO : allStudents) {
			
			for(ContentTO tempCont : studentTO.getContent()) {
				
				
				/*pageCount++;
				if(pageCount < 140) continue;
				if(pageCount > 150) break;*/
				
				
				List<StudentScoresTO> currentStudentScore = 
					NDUtil.getStudentScoreByBioAndContent(allStudScore, studentTO.getStudentBioId(), tempCont.getContentId());
				ContentTO contentTO = NDUtil.populateContentTO(request, currentStudentScore, tempCont.getContentId());
					
				contentName = tempCont.getContentName();
				
				if(pageCount > 0) document.newPage();
				pageCount++;
				
				System.out.println(nodeId+" : creating page # "+pageCount);
				/***** Main Table *****/
				
				PdfPTable mainTable = getTable(1);
				PdfPCell mainCell1 = getCell("", false, null);
				PdfPCell mainCell2 = getCell("", false, null);
				PdfPCell mainCell3 = getCell("", false, null);
				PdfPCell mainCell4 = getCell("", false, null);
				PdfPCell mainCell6 = getCell("", false, null);
				
				mainCell1.setFixedHeight(65);
				mainCell2.setFixedHeight(425);
				
				/***** Add Header *****/
				
				PdfPTable headerTable1 = getHeader(studentTO.getStudentName(), 
						contentName, studentTO.getGrade());
				
				mainCell1.addElement(headerTable1);
				mainTable.addCell(mainCell1);
				
				document.add(new Paragraph("\n"));
				
				
				/****** Add Graph ********/
				
				PdfPTable imageTable = new PdfPTable(1);
				imageTable.setWidthPercentage(100);
				
				NDUtil ndUtil = new NDUtil();
				
				String graph = "";/*ndUtil.createProjectionGraph(contentTO, 
						contentTO.getContentId(), studentTO.getStudentBioId(), true);*/
				
				Reader reader = ndUtil.getProjectionSvgReader(contentTO, 
						contentTO.getContentId(), studentTO.getStudentBioId(), true);

				allGraph.add(graph);
				// add svg image
				//imageTable.addCell(addSvgImage(graph));
				imageTable.addCell(addSvgImage(reader));
				
				PdfPCell graphText = getCell(tempCont.getGraphText(), false, fontScore);
				graphText.setPaddingTop(2);
				imageTable.addCell(graphText);
				
				mainCell2.addElement(imageTable);
				mainTable.addCell(mainCell2);
				
				/****** Add Table ********/
				
				// add header 
				PdfPTable studHeader = getStudentHeader(studentTO.getStudentName(), 
						studentTO.getCurrentDistrict(), studentTO.getCurrentSchool());
				
				mainCell3.addElement(studHeader);
				mainCell3.setFixedHeight(51f);
				mainTable.addCell(mainCell3);
				
				// add table 
				PdfPTable scoreTable = getTable(7);
				float[] colsWidth = {1f, 1.3f, 1.8f, 1.9f, 1f, 1.2f, 5.8f};
				scoreTable.setWidths(colsWidth);
				
				scoreTable = getScoreTableHeader(scoreTable, contentName);
				
				scoreTable = populateScoreTable(scoreTable, contentTO);
				
				mainCell4.addElement(scoreTable);
				mainTable.addCell(mainCell4);
				
				PdfPCell mainCell5 = getCell(tempCont.getTableText(), false, fontScore);
				mainCell5.setPaddingLeft(3f);
				mainCell5.setFixedHeight(54);
				mainTable.addCell(mainCell5);
				
				/******* add footer *******/
				
				PdfPTable footer = getFooter(totalPages, pageCount);
				
				mainCell6.setBorderWidthTop(0.5f);
				mainCell6.setBorderColorTop(footerBorder);
				mainCell6.addElement(footer);
				mainTable.addCell(mainCell6);
				
				document.add(mainTable);
			}
			if(studentTO.getContent().size()%2 != 0) {
				document.newPage();
				pageCount++;
				
				PdfPTable blankPage = addBlankPage(totalPages, pageCount);
				document.add(blankPage);
			}
			if(request != null) {
				request.getSession().setAttribute("pdfStatus_"+nodeId, pageCount);
			}
			// TODO REMOVE *************************
			//if(pageCount == 2) break;
			// TODO REMOVE *************************
		}
		/*
		for(StudentTO studentTO : allStudents) {
			ScoreTO scoreTO = commonDao.getStudentScore(studentTO.getStudentBioId());
			if(scoreTO != null) {
				for(ContentTO contentTO : scoreTO.getContent()) {
					String contentName = "";
					for(ContentTO tempCont : studentTO.getContent()) {
						if(tempCont.getContentId() == contentTO.getContentId()) {
							contentName = tempCont.getContentName();
						
							if(pageCount > 0) document.newPage();
							pageCount++;
							
							System.out.println("creating page # "+pageCount);
							*//***** Main Table *****//*
							
							PdfPTable mainTable = getTable(1);
							PdfPCell mainCell1 = getCell("", false, null);
							PdfPCell mainCell2 = getCell("", false, null);
							PdfPCell mainCell3 = getCell("", false, null);
							PdfPCell mainCell4 = getCell("", false, null);
							PdfPCell mainCell6 = getCell("", false, null);
							
							mainCell1.setFixedHeight(70);
							mainCell2.setFixedHeight(395);
							
							*//***** Add Header *****//*
							
							PdfPTable headerTable1 = getHeader(studentTO.getStudentName(), 
									contentName, studentTO.getGrade());
							
							mainCell1.addElement(headerTable1);
							mainTable.addCell(mainCell1);
							
							document.add(new Paragraph("\n"));
							
							
							*//****** Add Graph ********//*
							
							PdfPTable imageTable = new PdfPTable(1);
							imageTable.setWidthPercentage(100);
							
							String graph = UserController.createStudentGraph(scoreTO, 
									contentTO.getContentId(), studentTO.getStudentBioId(), true);
							allGraph.add(graph);
							// add svg image
							imageTable.addCell(addSvgImage(graph));
							
							PdfPCell graphText = getCell(tempCont.getGraphText(), false, font);
							graphText.setPaddingTop(2);
							imageTable.addCell(graphText);
							
							mainCell2.addElement(imageTable);
							mainTable.addCell(mainCell2);
							
							*//****** Add Table ********//*
							
							// add header 
							PdfPTable studHeader = getStudentHeader(studentTO.getStudentName(), 
									studentTO.getCurrentDistrict(), studentTO.getCurrentSchool());
							
							mainCell3.addElement(studHeader);
							mainTable.addCell(mainCell3);
							
							// add table 
							PdfPTable scoreTable = getTable(7);
							float[] colsWidth = {1f, 1.3f, 1.8f, 1.9f, 1f, 1.2f, 5.8f};
							scoreTable.setWidths(colsWidth);
							
							scoreTable = getScoreTableHeader(scoreTable, contentName);
							
							scoreTable = populateScoreTable(scoreTable, contentTO);
							
							mainCell4.addElement(scoreTable);
							mainTable.addCell(mainCell4);
							
							PdfPCell mainCell5 = getCell(tempCont.getTableText(), false, font);
							mainCell5.setPaddingLeft(3f);
							mainCell5.setFixedHeight(65);
							mainTable.addCell(mainCell5);
							
							*//******* add footer *******//*
							
							PdfPTable footer = getFooter(totalPages, pageCount);
							
							mainCell6.setBorderWidthTop(0.5f);
							mainCell6.setBorderColorTop(footerBorder);
							mainCell6.addElement(footer);
							mainTable.addCell(mainCell6);
							
							document.add(mainTable);
							break;
						}
					}
				}
				if(studentTO.getContent().size()%2 != 0) {
					document.newPage();
					pageCount++;
					
					PdfPTable blankPage = addBlankPage(totalPages, pageCount);
					document.add(blankPage);
				}
				if(request != null) {
					request.getSession().setAttribute("pdfStatus_"+nodeId, pageCount);
				}
			}
			// TODO REMOVE *************************
			//if(pageCount == 4) break;
			// TODO REMOVE *************************
		}*/
		document.close();
		removeTempGraphs(allGraph);
		System.out.println("Done");
		
		return (fileLoc == null)? "" : fileLoc.toString();
	}
	
	/**
	 * This adds svg section 
	 * @param svgFileName
	 * @return
	 * @throws BadElementException
	 */
	private ImgTemplate addSvgImage(String svgFileName) throws BadElementException {
		int width = 340;
        int height = 205;
        PdfContentByte cb = writer.getDirectContent();
        PdfTemplate template = cb.createTemplate(width,height);         
        Graphics2D g2 = template.createGraphics(width,height);          
        
        PrintTranscoder prm = new PrintTranscoder();
        TranscoderInput ti = new TranscoderInput("file:///"+svgFileName);
        prm.transcode(ti, null);
       
        PageFormat pg = new PageFormat();
        Paper pp= new Paper();
        pp.setSize(width, height);
        pp.setImageableArea(0, 0, width, height);
        pg.setPaper(pp);
        prm.print(g2, pg, 0); 
        g2.dispose(); 

        ImgTemplate img = new ImgTemplate(template);           
        return img;
	}
	
	private ImgTemplate addSvgImage(Reader reader) throws BadElementException {
		int width = 350;
        int height = 250;
        PdfContentByte cb = writer.getDirectContent();
        PdfTemplate template = cb.createTemplate(width,height-8);         
        Graphics2D g2 = template.createGraphics(width,height-8);          
        
        PrintTranscoder prm = new PrintTranscoder();
        TranscoderInput ti = new TranscoderInput(reader);
        prm.transcode(ti, null);
       
        PageFormat pg = new PageFormat();
        Paper pp= new Paper();
        pp.setSize(width, height);
        pp.setImageableArea(0, -8, width, height);
        pg.setPaper(pp);
        prm.print(g2, pg, 0); 
        g2.dispose(); 

        ImgTemplate img = new ImgTemplate(template);           
        return img;
	}
	
	private PdfPTable getTable(int columns) {
		PdfPTable table = new PdfPTable(columns);
		table.setWidthPercentage(100);
		return table;
	}
	
	private PdfPCell getCell(String text, boolean border, Font font) {
		PdfPCell cell = null;
		if(font != null) cell = new PdfPCell(new Paragraph(text, font));
		else cell = new PdfPCell(new Paragraph(text));
		if(!border) cell.setBorder(PdfPCell.NO_BORDER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER); 
		return cell;
	}
	
	private PdfPTable getHeader(String studentName, String contentName, String grade) throws DocumentException {
		PdfPTable headerTable1 = getTable(5);
		float[] headerWidth = {1.1f, 1.8f, 0.9f, 1.1f, 6f};
		headerTable1.setWidths(headerWidth);
		//headerTable1.setWidthPercentage(100);
		
		PdfPCell headerPCell1 = getCell("Student Growth Projection", false, font1);
		headerPCell1.setPaddingLeft(0);
		headerPCell1.setColspan(5);
		headerTable1.addCell(headerPCell1);
				
		headerPCell1 = getCell(studentName, true, font2);
		headerPCell1.setBorder(Rectangle.BOTTOM);
		headerPCell1.setBorderWidth(2);
		headerPCell1.setBorderColor(headerBorder);
		headerPCell1.setPaddingBottom(4);
		headerPCell1.setColspan(5);
		headerPCell1.setPaddingLeft(0);
		headerTable1.addCell(headerPCell1);

		headerPCell1 = getCell("Content: ", false, font3);
		headerPCell1.setNoWrap(true);
		headerPCell1.setPaddingLeft(0);
		headerTable1.addCell(headerPCell1);
		
		headerPCell1 = getCell(contentName, false, font2);
		headerPCell1.setNoWrap(true);
		headerTable1.addCell(headerPCell1);
		
		headerPCell1 = getCell("Grade: ", false, font3);
		headerPCell1.setNoWrap(true);
		headerTable1.addCell(headerPCell1);
		
		headerPCell1 = getCell(grade, false, font2);
		headerPCell1.setNoWrap(true);
		headerTable1.addCell(headerPCell1);
		
		headerPCell1 = getCell("", false, font2);
		headerTable1.addCell(headerPCell1);
		
		return headerTable1;
	}
	
	private PdfPTable addBlankPage(long totalPages, long pageCount) throws DocumentException {
		PdfPTable blankTable = getTable(1);
		PdfPCell studCell = getCell("", false, font1);
		studCell.setFixedHeight(800f);
		blankTable.addCell(studCell);
		
		studCell = getCell("", false, font1);
		PdfPTable footer = getFooter(totalPages, pageCount);
		studCell.setBorderWidthTop(0.5f);
		studCell.setBorderColorTop(footerBorder);
		studCell.addElement(footer);
		blankTable.addCell(studCell);
		return blankTable;
	}
	
	private PdfPTable getStudentHeader(String studentName, String currDistrict, String currSchool) throws DocumentException {
		PdfPTable studHeader = new PdfPTable(2);
		float[] studWidth = {10f, 52f};
		studHeader.setWidths(studWidth);
		studHeader.setWidthPercentage(100);
		studHeader.setSpacingAfter(7);
		
		PdfPCell studCell = new PdfPCell(new Phrase("", font));
		studCell.setBorder(Rectangle.TOP);
		studCell.setFixedHeight(1f);
		studCell.setColspan(2);
		studCell.setBorderColor(footerBorder);
		studCell.setPaddingLeft(0);
		studHeader.addCell(studCell);
		
		studCell = getCell("Student:", false, fontBold);
		studCell.setPaddingLeft(0);
		studHeader.addCell(studCell);
		
		studCell = getCell(studentName, false, font);
		studCell.setPaddingLeft(0);
		studHeader.addCell(studCell);
		
		studCell = getCell("Current District:", false, fontBold);
		studCell.setPaddingLeft(0);
		studHeader.addCell(studCell);
		
		studCell = getCell(currDistrict, false, font);
		studCell.setPaddingLeft(0);
		studHeader.addCell(studCell);
		
		studCell = getCell("Current School:", false, fontBold);
		studCell.setPaddingLeft(0);
		studHeader.addCell(studCell);
		
		studCell = getCell(currSchool, false, font);
		studCell.setPaddingLeft(0);
		studHeader.addCell(studCell);
		
		return studHeader;
	}
	
	private static void removeTempGraphs(java.util.List<String> allGraph) {
		try {
			java.util.List<String> tempGraphList = new java.util.ArrayList<String>();
			for(String graph : tempGraphList) {
				tempGraphList.add(graph);
			}
			for(String graph : tempGraphList) {
				File file = new File(graph);
				try {
					if(file != null && file.exists()) {
						file.delete();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private PdfPTable populateScoreTable(PdfPTable scoreTable, ContentTO contentTO) {
		PdfPCell pdfPCell = null;
		int i = 2;
		for(StudentScoresTO studentScoresTO : contentTO.getStudentScores()) {
			if( i == 12) i = 2;
			else i++;
			pdfPCell = getCell(studentScoresTO.getGradeName(), false, true, false, false, false);
			pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			addBorderBottom(pdfPCell, i);
			scoreTable.addCell(pdfPCell);
			
			pdfPCell = getCell(studentScoresTO.getStudentScore(), false, false, false, false, false);
			pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			addBorderBottom(pdfPCell, i);
			scoreTable.addCell(pdfPCell);
			
			pdfPCell = getCell(studentScoresTO.getAchvLevel(), false, false, false, false, false);
			pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			addBorderBottom(pdfPCell, i);
			scoreTable.addCell(pdfPCell);
			
			pdfPCell = getCell(studentScoresTO.getAchvPercentile(), false, false, false, false, false);
			pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			addBorderBottom(pdfPCell, i);
			scoreTable.addCell(pdfPCell);
			
			pdfPCell = getCell(studentScoresTO.getGrowthScore(), false, false, false, false, false);
			pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			addBorderBottom(pdfPCell, i);
			scoreTable.addCell(pdfPCell);
			
			pdfPCell = getCell(studentScoresTO.getDateAttended(), false, false, false, false, true);
			addBorderBottom(pdfPCell, i);
			pdfPCell.setNoWrap(true);
			scoreTable.addCell(pdfPCell);
			
			pdfPCell = getCell(studentScoresTO.getSchoolAttended(), false, false, false, false, false);
			addBorderBottom(pdfPCell, i);
			scoreTable.addCell(pdfPCell);
		}
		
		return scoreTable;
	}
	
	private PdfPTable getScoreTableHeader(PdfPTable scoreTable, String contentName) {
		PdfPCell pdfPCell = getCell("Grade", true, true, true, false, false);
		pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		scoreTable.addCell(pdfPCell);
		
		pdfPCell = getCell(contentName, true, false, true, false, false);
		pdfPCell.setColspan(4);
		pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		scoreTable.addCell(pdfPCell);
		
		pdfPCell = getCell("North Dakota State Assessment Information", true, false, true, false, false);
		pdfPCell.setColspan(2);
		pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		scoreTable.addCell(pdfPCell);
		
		pdfPCell = getCell("", true, true, false, false, false);
		scoreTable.addCell(pdfPCell);
		
		pdfPCell = getCell("Student Score", true, false, false, false, false);
		pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		scoreTable.addCell(pdfPCell);
		
		pdfPCell = getCell("Achievement Level", true, false, false, false, false);
		pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		scoreTable.addCell(pdfPCell);
		
		pdfPCell = getCell("Achievement Percentile", true, false, false, false, false);
		pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		scoreTable.addCell(pdfPCell);
		
		pdfPCell = getCell("3-Year Path", true, false, false, false, false);
		pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		scoreTable.addCell(pdfPCell);
		
		pdfPCell = getCell("\nDate", true, false, false, false, true);
		scoreTable.addCell(pdfPCell);
		
		pdfPCell = getCell("\nSchool Attended", true, false, false, false, false);
		scoreTable.addCell(pdfPCell);
		
		return scoreTable;
	}
	
	private PdfPTable getFooter(long totalPages, long currentPage) throws DocumentException {
		PdfPTable footer = getTable(3);
		float[] footerWidth = {4f, 2f, 3f};
		footer.setWidths(footerWidth);
		
		PdfPCell footerCell = getCell("North Dakota Department of Public Instruction", false, footerFont);
		footer.addCell(footerCell);
		footerCell = getCell(UserController.getDate(), false, footerFont);
		footer.addCell(footerCell);
		footerCell = getCell("Page "+currentPage+" of "+totalPages, false, footerFont);
		footerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		footer.addCell(footerCell);
		
		return footer;
	}
	
	private static PdfPCell addBorderBottom(PdfPCell cell, int row) {
		if(row == 11) {
			cell.setBorderWidthBottom(0.5f);
		}
		if(row%2==0) {
			cell.setBackgroundColor(tableShading);
		} else {
			cell.setBackgroundColor(tableShadingWhite);
		}
		
		//cell.setBorder(PdfPCell.NO_BORDER);
		
		
		return cell;
	}
	
	private static PdfPCell getCell(String text, boolean isHeader, boolean isFirstColumn, 
			boolean isFirstRow, boolean isLastRow, boolean noBorder) {
		if(text == null) text = "";
		PdfPCell cell = null;
		if(isHeader) {
			cell = new PdfPCell(new Paragraph(text, fontBoldScore));
		} else {
			cell = new PdfPCell(new Paragraph(text, fontScore));
		}
		cell.setBorder(PdfPCell.NO_BORDER);
		if(!noBorder) cell.setBorderWidthRight(0.5f);
		if(isFirstColumn) cell.setBorderWidthLeft(0.5f);
		if(isFirstRow) cell.setBorderWidthTop(0.5f);
		if(isHeader || isLastRow) cell.setBorderWidthBottom(0.5f);
		
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		return cell;
	}

	public static void main(String[] args) {	
		try{
			ItextCreatePdf pdfTable = new ItextCreatePdf(null);
			
			if(commonDao == null) commonDao = new CommonDAOImpl();
			java.util.List<OrgTO> allSchools = commonDao.getAllNodes();
			java.util.List<StudentTO> allStudents = null;
			/*for(OrgTO orgTO : allSchools) {
				allStudents = commonDao.getScores(""+orgTO.getNodeId());
				pdfTable.generateGrowthPdf(allStudents, ""+orgTO.getNodeId());
				break;
			}*/
			String nodeId = "70005";
			allStudents = commonDao.getScores(nodeId);
			pdfTable.generateGrowthPdf(allStudents, nodeId, "C:\\Temp\\"+nodeId+ "_"+System.currentTimeMillis()+".pdf");
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
