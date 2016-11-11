
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import bean.PortalTO;
import bean.RootTO;


public class ExcelFileGenerator {

	public void writeToFile(String fileName, RootTO rootTO)
			throws FileNotFoundException, IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();

		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderTop((short) 6); // double lines border
		style.setBorderBottom((short) 1); // single line border
		style.setBorderLeft((short) 1);
		style.setBorderRight((short) 1);

		HSSFFont txtFont = (HSSFFont) workbook.createFont();
		txtFont.setFontName("Arial");
		txtFont.setFontHeightInPoints((short) 12);
		txtFont.setColor((short) HSSFColor.DARK_BLUE.index);
		style.setFont(txtFont);

		int rowCount = 0;
		List<PortalTO> portalList = rootTO.getPortalList();

		for (PortalTO portalTO : portalList) {
			HSSFRow row = sheet.createRow(rowCount);
			if (rowCount == 0) {
				// row for header
				writeBookHeader(row, style);
				// row for the content
				rowCount++;
				row = sheet.createRow(rowCount);
			}
			writeBook(portalTO, row);
			rowCount++;
		}

		try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
			workbook.write(outputStream);
		}
	}

	/**
	 * Set the header
	 * 
	 * @param row
	 * @param style
	 */
	private void writeBookHeader(HSSFRow row, HSSFCellStyle style) {
		int count = 0;

		HSSFCell cell = row.createCell(count++);
		cell.setCellValue(new HSSFRichTextString("ID"));
		cell.setCellStyle(style);

		cell = row.createCell(count++);
		cell.setCellValue(new HSSFRichTextString("Attachments"));
		cell.setCellStyle(style);

		cell = row.createCell(count++);
		cell.setCellValue(new HSSFRichTextString("LinkTitle"));
		cell.setCellStyle(style);

		cell = row.createCell(count++);
		cell.setCellValue(new HSSFRichTextString("Status"));
		cell.setCellStyle(style);

		cell = row.createCell(count++);
		cell.setCellValue(new HSSFRichTextString("Date_Requested"));
		cell.setCellStyle(style);

		cell = row.createCell(count++);
		cell.setCellValue(new HSSFRichTextString("Requestor"));
		cell.setCellStyle(style);

		cell = row.createCell(count++);
		cell.setCellValue(new HSSFRichTextString("Description"));
		cell.setCellStyle(style);

		cell = row.createCell(count++);
		cell.setCellValue(new HSSFRichTextString("Description_Formatted"));
		cell.setCellStyle(style);

		cell = row.createCell(count++);
		cell.setCellValue(new HSSFRichTextString("Priority"));
		cell.setCellStyle(style);

		cell = row.createCell(count++);
		cell.setCellValue(new HSSFRichTextString("CAMIS"));
		cell.setCellStyle(style);

		cell = row.createCell(count++);
		cell.setCellValue(new HSSFRichTextString("Hours"));
		cell.setCellStyle(style);

		cell = row.createCell(count++);
		cell.setCellValue(new HSSFRichTextString("Requested_Completion"));
		cell.setCellStyle(style);

		cell = row.createCell(count++);
		cell.setCellValue(new HSSFRichTextString("Customer"));
		cell.setCellStyle(style);

		cell = row.createCell(count++);
		cell.setCellValue(new HSSFRichTextString("Tier_Resource"));
		cell.setCellStyle(style);

		cell = row.createCell(count++);
		cell.setCellValue(new HSSFRichTextString("Completion_Date"));
		cell.setCellStyle(style);

		cell = row.createCell(count++);
		cell.setCellValue(new HSSFRichTextString("Comments"));
		cell.setCellStyle(style);

		cell = row.createCell(count++);
		cell.setCellValue(new HSSFRichTextString("Comments_Formatted"));
		cell.setCellStyle(style);
	}

	/**
	 * Set the rows with cell values
	 * 
	 * @param portalTO
	 * @param row
	 */
	private void writeBook(PortalTO portalTO, HSSFRow row) {
		int count = 0;

		HSSFCell cell = row.createCell(count++);
		cell.setCellValue(portalTO.getOws_ID());

		cell = row.createCell(count++);
		cell.setCellValue(portalTO.getOws_Attachments());

		cell = row.createCell(count++);
		cell.setCellValue(portalTO.getOws_LinkTitle());

		cell = row.createCell(count++);
		cell.setCellValue(portalTO.getOws__Status());

		cell = row.createCell(count++);
		cell.setCellValue(portalTO.getOws_Date_x0020_Requested());

		cell = row.createCell(count++);
		cell.setCellValue(portalTO.getOws_Requestor());

		cell = row.createCell(count++);
		cell.setCellValue(portalTO.getOws_Breif_x0020_Description());

		// formatted
		cell = row.createCell(count++);
		cell.setCellValue(replaceHTMLTags(portalTO
				.getOws_Breif_x0020_Description()));

		cell = row.createCell(count++);
		cell.setCellValue(portalTO.getOws_Priority());

		cell = row.createCell(count++);
		cell.setCellValue(portalTO.getOws_CAMIS());

		cell = row.createCell(count++);
		cell.setCellValue(portalTO.getOws_Hours());

		cell = row.createCell(count++);
		cell.setCellValue(portalTO.getOws_Requested_x0020_Completion_x0020());

		cell = row.createCell(count++);
		cell.setCellValue(portalTO.getOws_Customer());

		cell = row.createCell(count++);
		cell.setCellValue(portalTO.getOws_Tier_x0020_II_x0020_Resource_x00());

		cell = row.createCell(count++);
		cell.setCellValue(portalTO.getOws_Completion_x0020_Date());

		cell = row.createCell(count++);
		cell.setCellValue(portalTO.getOws_Comments());

		// formatted
		cell = row.createCell(count++);
		cell.setCellValue(replaceHTMLTags(portalTO.getOws_Comments()));
	}

	/**
	 * Replace the html tags
	 * 
	 * @param sourceString
	 * @return
	 */
	private String replaceHTMLTags(String sourceString) {
		if (sourceString == null) {
			return "";
		}
		// System.out.println("Before : " + sourceString);
		sourceString = sourceString.replaceAll("&nbsp;", " ")
				.replaceAll("<div>", "").replaceAll("</div>", "\n")
				.replaceAll("<br>", "\n").replaceAll("</p>", "")
				.replaceAll("<p>", "").replaceAll("&quot;", "\"")
				.replaceAll("</font>", "");

		while (sourceString.indexOf("<font") >= 0) {
			if (sourceString.indexOf("<font") >= 0) {
				int startIndex = sourceString.indexOf("<font");
				String replaceString = sourceString.substring(startIndex,
						sourceString.indexOf(">", startIndex) + 1);
				sourceString = sourceString.replaceAll(replaceString, "");
			}
		}
		// System.out.println("After : " + sourceString);
		return sourceString;
	}

}
