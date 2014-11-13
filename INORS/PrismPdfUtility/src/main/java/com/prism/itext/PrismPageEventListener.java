package com.prism.itext;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.prism.util.PropertyFile;

public class PrismPageEventListener extends PdfPageEventHelper {
	private static Font smallFont = FontFactory.getFont("Arial", 5.0F, 1, new Color(0, 0, 0));
	private Properties prop;

	public PrismPageEventListener(String propertyFile) {
		prop = PropertyFile.loadProperties(propertyFile);
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		try {
			PdfPTable tbl = new PdfPTable(1);
			PdfPCell c1 = new PdfPCell(new Phrase(prop.getProperty("footer"), smallFont));
			c1.setBorder(0);
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setVerticalAlignment(Element.ALIGN_TOP);
			tbl.addCell(c1);
			tbl.setTotalWidth(435);
			tbl.writeSelectedRows(0, -1, 40, 20, writer.getDirectContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStartPage(PdfWriter writer, Document document) {
		try {
			Image _img = Image.getInstance(String.format("logoCTBTASC.png"));
			_img.scalePercent(50);
			document.add(_img);
		} catch (BadElementException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}