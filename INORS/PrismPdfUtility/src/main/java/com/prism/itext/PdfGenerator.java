package com.prism.itext;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.prism.to.OrgTO;
import com.prism.to.UserTO;
import com.prism.util.CustomStringUtil;

public class PdfGenerator {

	private static final Logger logger = Logger.getLogger(PdfGenerator.class);

	private Font font = FontFactory.getFont("Arial", 9.0F, 0, new Color(0, 0, 0));
	private static Font fontCourier = FontFactory.getFont("Arial", 7.0F, 0, new Color(0, 0, 0));
	private Font fontBold = FontFactory.getFont("Arial", 9.0F, Font.BOLD, new Color(0, 0, 0));
	private Font tableHeaderFont = FontFactory.getFont("Arial", 9.0F, 1, new Color(0, 0, 0));
	private static Font tableFont = FontFactory.getFont("Arial", 8.0F, 0, new Color(0, 0, 0));
	private final String ORG_NODE_LEVEL_STATE = "1";
	private final String ORG_NODE_LEVEL_DISTRICT = "2";
	private final String ORG_NODE_LEVEL_SCHOOL = "3";
	private final String ORG_NODE_LEVEL_CLASS = "4"; // TODO : Implement
														// Logic

	private final String ORG_NODE_LEVEL_EDU = "-99";
	private boolean issueFound = false;
	private Image _img = null;
	private Font smallFont = FontFactory.getFont("Arial", 4.0F, 1, new Color(0, 0, 0));

	// Tasc
	private final String ORG_NODE_LEVEL_COUNTY = "2";
	private final String ORG_NODE_LEVEL_SITE = "3";

	private PrismPageEventListener prismPageEventListener;

	static {
		BaseFont bf2 = null;
		try {
			bf2 = BaseFont.createFont("COUR.TTF", BaseFont.CP1252, BaseFont.EMBEDDED);
			fontCourier = new Font(bf2, 12);
			tableFont = new Font(bf2, 12);
		} catch (Exception e) {
			fontCourier = FontFactory.getFont("Arial", 9.0F, 0, new Color(0, 0, 0));
			tableFont = FontFactory.getFont("Arial", 9.0F, 0, new Color(0, 0, 0));
			logger.warn("COUR.TTF file not found. Using default font.");
		}
	}

	public PdfGenerator(String propertyFileName) {
		prismPageEventListener = new PrismPageEventListener(propertyFileName);
	}

	public boolean isIssueFound() {
		return issueFound;
	}

	public void setIssueFound(boolean issueFound) {
		this.issueFound = issueFound;
	}

	/**
	 * Generates pdf in specified format
	 * 
	 * @param prop
	 * @param school
	 * @param teachers
	 * @param schoolUserPresent
	 * @param isInitialLoad
	 * @param migration
	 * @param state
	 * @param orgLabelMap
	 * @return
	 */
	public String generatePdfInors(Properties prop, OrgTO school, java.util.List<OrgTO> teachers, boolean schoolUserPresent, boolean isInitialLoad,
			boolean migration, boolean state, Map<String, String> orgLabelMap) {
		Document document = null;
		String docName = null;
		try {
			document = new Document(PageSize.A4, 50.0F, 50.0F, 20.0F, 75.0F);
			docName = prop.getProperty("pdfGenPath") + File.separator + prop.getProperty("tempPdfLocation") + prop.getProperty("districtText")
					+ prop.getProperty("schoolText") + school.getDateStrWtYear() + ".pdf";

			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(docName));
			writer.setPageEvent(prismPageEventListener);
			addMetaDataInors(document);

			document.open();

			boolean pageBreakRequired = false;
			if (migration) {
				addLoginInstruction(document, prop, school, false, migration, state);
				addUserLoginsInors(document, school, state, orgLabelMap);
				document.add(new Paragraph(prop.getProperty("HELP"), font));
				pageBreakRequired = true;
			} else if (schoolUserPresent) {
				addLoginInstruction(document, prop, school, false, false, false);
				addUserLoginsInors(document, school, false, orgLabelMap);
				document.add(new Paragraph(prop.getProperty("HELP"), font));
				// next page
				if (teachers != null && teachers.get(0).getUsers() != null)
					addTeacherLoginsInors(document, teachers, false);
				pageBreakRequired = true;
			} else if (isInitialLoad) {
				// page for returning schools
				addLoginInstruction(document, prop, school, true, false, false);
				addTeacherLoginsInors(document, teachers, true);
				pageBreakRequired = true;
			}
			// new page for each teacher
			if (teachers != null) {
				for (OrgTO tech : teachers) {
					boolean studentListRequired = (tech.getUsers() == null) ? false : true;
					addTeacherPageInors(document, prop, tech, school.getElementName(), schoolUserPresent, pageBreakRequired, studentListRequired, orgLabelMap);
				}
			}
			logger.debug("PDF generated");
		} catch (Exception e2) {
			logger.error(e2.getMessage());
			e2.printStackTrace();
			issueFound = true;
			return null;
		} finally {
			if (document != null)
				document.close();
		}
		return docName;
	}

	/**
	 * Add metadata to PDF file
	 * 
	 * @param document
	 */
	private void addMetaDataInors(Document document) {
		document.addTitle("ISTEP");
		document.addAuthor("ISTEP");
		document.addCreator("ISTEP-CTB");
		document.addSubject("Login credential details");
	}

	/**
	 * Add login information and title in the first screen
	 * 
	 * @param document
	 * @param prop
	 * @param school
	 * @param returningSchool
	 * @param migration
	 * @param state
	 * @throws DocumentException
	 */
	private void addLoginInstruction(Document document, Properties prop, OrgTO school, boolean returningSchool, boolean migration, boolean state)
			throws DocumentException {
		document.add(new Paragraph(prop.getProperty("title") + school.getElementName()));
		List list = null;
		ListItem listItem;

		if (state) {
			document.add(new Paragraph(prop.getProperty("welcome_state"), fontBold));
		} else {
			document.add(new Paragraph(prop.getProperty("welcome"), fontBold));
		}
		if (migration) {
			if (state) {
				document.add(new Paragraph(prop.getProperty("TXT_ONE_MGR_STATE"), font));
			} else {
				document.add(new Paragraph(prop.getProperty("TXT_ONE_MGR"), font));
			}
		} else if (returningSchool) {
			document.add(new Paragraph(prop.getProperty("TXT_ONE_RET"), font));
			document.add(new Paragraph(prop.getProperty("TXT_TWO_RET"), font));
			document.add(new Paragraph(prop.getProperty("TXT_THR_RET"), font));
		} else {
			document.add(new Paragraph(prop.getProperty("TXT_ONE"), font));
			document.add(new Paragraph(prop.getProperty("TXT_TWO_HD"), fontBold));
			document.add(new Paragraph(prop.getProperty("TXT_TWO"), font));
			document.add(new Paragraph(prop.getProperty("TXT_THREE_HD"), fontBold));
			document.add(new Paragraph(prop.getProperty("TXT_THREE"), font));
			document.add(new Paragraph(prop.getProperty("TXT_FOUR"), font));
		}

		document.add(new Paragraph("\nInstructions to Log-in:", fontBold));
		list = new List(false, 20);
		listItem = new ListItem(prop.getProperty("URL"), font);
		list.add(listItem);

		if (migration) {
			listItem = new ListItem(prop.getProperty("BLT1_MGR"), font);
			list.add(listItem);

			listItem = new ListItem(prop.getProperty("BLT2_MGR"), font);
			list.add(listItem);

			listItem = new ListItem(prop.getProperty("BLT3_MGR"), font);
			list.add(listItem);

			listItem = new ListItem(prop.getProperty("BLT3A_MGR"), fontBold);
			list.add(listItem);
		} else {
			listItem = new ListItem(prop.getProperty("BLT1"), font);
			list.add(listItem);

			listItem = new ListItem(prop.getProperty("BLT2"), font);
			list.add(listItem);

			listItem = new ListItem(prop.getProperty("BLT3"), font);
			list.add(listItem);

			listItem = new ListItem(prop.getProperty("BLT3A"), font);
			list.add(listItem);
		}
		document.add(list);
		document.add(new Paragraph("\nUser Accounts", fontBold));
	}

	/**
	 * Create one PDF table with 5 column
	 * 
	 * @return
	 * @throws DocumentException
	 */
	private PdfPTable getTeacherTableInors() throws DocumentException {
		float[] colsWidth = { 4f, 2f, 3f, 4f, 3f };
		PdfPTable table = new PdfPTable(5);
		table.setWidths(colsWidth);
		table.setWidthPercentage(100);
		return table;
	}

	/**
	 * Create itext PDF table with 4, 6 or 8 columns based on orgNodeLevel
	 * 
	 * @param orgNodeLevel
	 * @return
	 * @throws DocumentException
	 */
	private PdfPTable getUserAccountsTableInors(String orgNodeLevel) throws DocumentException {
		float[] colsWidthState = { 3f, 3f, 3f, 3f };
		float[] colsWidthStateCounty = { 3f, 3f, 3f, 3f, 3f };
		float[] colsWidthTestingSite = { 2.5f, 3f, 3f, 3f, 4f, 2.5f };
		if (ORG_NODE_LEVEL_EDU.equals(orgNodeLevel)) {
			PdfPTable table = new PdfPTable(4);
			table.setWidths(colsWidthState);
			table.setWidthPercentage(100);
			return table;
		} else if (ORG_NODE_LEVEL_STATE.equals(orgNodeLevel)) {
			PdfPTable table = new PdfPTable(4);
			table.setWidths(colsWidthState);
			table.setWidthPercentage(100);
			return table;
		} else if (ORG_NODE_LEVEL_DISTRICT.equals(orgNodeLevel)) {
			PdfPTable table = new PdfPTable(5);
			table.setWidths(colsWidthStateCounty);
			table.setWidthPercentage(100);
			return table;
		} else if (ORG_NODE_LEVEL_SCHOOL.equals(orgNodeLevel) || ORG_NODE_LEVEL_CLASS.equals(orgNodeLevel)) { // TODO
			PdfPTable table = new PdfPTable(6);
			table.setWidths(colsWidthTestingSite);
			table.setWidthPercentage(100);
			return table;
		}
		return null;
	}

	/**
	 * Add school login informations in tabular format
	 * 
	 * @param document
	 * @param school
	 * @param state
	 * @throws DocumentException
	 */
	private void addUserLoginsInors(Document document, OrgTO school, boolean state, Map<String, String> orgLabelMap) throws DocumentException {
		String orgNodeLevel = school.getOrgNodeLevel();
		PdfPTable table = getUserAccountsTableInors(orgNodeLevel);
		document.add(new Paragraph("\n"));

		String labelOneText = orgLabelMap.get(ORG_NODE_LEVEL_STATE);
		String labelTwoText = orgLabelMap.get(ORG_NODE_LEVEL_DISTRICT);
		String labelThreeText = orgLabelMap.get(ORG_NODE_LEVEL_SCHOOL);
		String userType = "";

		PdfPCell c1 = null;
		if (ORG_NODE_LEVEL_EDU.equals(orgNodeLevel)) {
			userType = "Education Center";
			c1 = new PdfPCell(new Phrase(userType, tableHeaderFont));
		} else {
			userType = labelOneText;
			c1 = new PdfPCell(new Phrase(userType, tableHeaderFont));
		}
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		if (ORG_NODE_LEVEL_DISTRICT.equals(orgNodeLevel)) {
			userType = labelTwoText;
			c1 = new PdfPCell(new Phrase(userType + " Name", tableHeaderFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBackgroundColor(Color.lightGray);
			table.addCell(c1);
		} else if (ORG_NODE_LEVEL_SCHOOL.equals(orgNodeLevel) || ORG_NODE_LEVEL_CLASS.equals(orgNodeLevel)) { // TODO
			userType = labelThreeText;
			c1 = new PdfPCell(new Phrase(labelTwoText + " Name", tableHeaderFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBackgroundColor(Color.lightGray);
			table.addCell(c1);
			c1 = new PdfPCell(new Phrase(userType + " Name", tableHeaderFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBackgroundColor(Color.lightGray);
			table.addCell(c1);
		}

		c1 = new PdfPCell(new Phrase("User Roles", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("User Name", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("User Password", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);
		table.setHeaderRows(1);

		java.util.List<UserTO> schoolUsers = school.getUsers();
		if (schoolUsers != null && schoolUsers.size() == 0) {
			logger.warn("pdf generation : issueFound : no school user found");
			issueFound = true;
		}
		for (UserTO schUser : schoolUsers) {
			if (schUser.getPassword() == null) {
				logger.warn("pdf generation : issueFound : school tenantId or password is blank");
				issueFound = true;
			}
			String stateName = schUser.getStateName();
			if (stateName == null)
				stateName = "";
			String orgNodeName = schUser.getOrgNodeName();
			if (orgNodeName == null)
				orgNodeName = "";
			if (ORG_NODE_LEVEL_EDU.equals(orgNodeLevel))
				table.addCell(new Phrase(orgNodeName, tableFont));
			else
				table.addCell(new Phrase(stateName, tableFont));

			String countyName = schUser.getCountyName();
			if (countyName == null)
				countyName = "";

			Integer countyCode = schUser.getCountyCode();
			String countryCodeString = "";
			if (countyCode != null)
				countryCodeString = countyCode.toString();

			Integer testingSiteCode = schUser.getTestingSiteCode();
			String testingSiteCodeString = "";
			if (testingSiteCode != null)
				testingSiteCodeString = testingSiteCode.toString();

			String testingSiteName = schUser.getTestingSiteName();
			if (testingSiteName == null)
				testingSiteName = "";
			if (ORG_NODE_LEVEL_DISTRICT.equals(orgNodeLevel)) {
				table.addCell(new Phrase(countyName, tableFont));
			} else if (ORG_NODE_LEVEL_SCHOOL.equals(orgNodeLevel) || ORG_NODE_LEVEL_CLASS.equals(orgNodeLevel)) { // TODO
				table.addCell(new Phrase(countyName, tableFont));
				table.addCell(new Phrase(testingSiteName, tableFont));
			}

			int adminIndex = getAdminIndexInors(schUser.getRoles());
			if (adminIndex != -1) {
				table.addCell(new Phrase(userType + " " + schUser.getRoles().get(adminIndex), tableFont));
			} else {
				String roleString = getUserListAsStringInors(schUser.getRoles(), userType);
				table.addCell(new Phrase(roleString, tableFont));
			}
			table.addCell(new Phrase(schUser.getUserName(), tableFont));
			table.addCell(new Phrase((schUser.getPassword() == null) ? "" : schUser.getPassword(), tableFont));
		}
		document.add(table);
	}

	/**
	 * Creates a comma separated string from a List
	 * 
	 * @param roles
	 * @param userType
	 * @return
	 */
	private String getUserListAsStringInors(java.util.List<String> roles, String userType) {
		StringBuilder roleString = new StringBuilder();
		for (String role : roles) {
			roleString.insert(roleString.length(), CustomStringUtil.appendString(userType, " ", role, ", "));
		}
		return roleString.toString().substring(0, roleString.length() - 2);
	}

	/**
	 * Checks whether the user has admin role or not
	 * 
	 * @param roles
	 * @return
	 */
	private int getAdminIndexInors(java.util.List<String> roles) {
		int adminIndex = -1;
		for (int i = 0; i < roles.size(); i++) {
			if (roles.get(i).toLowerCase().contains("admin")) {
				return i;
			}
		}
		return adminIndex;
	}

	/**
	 * Add teacher login information in next page
	 * 
	 * @param document
	 * @param teachers
	 * @param returningSchool
	 * @throws DocumentException
	 */
	private void addTeacherLoginsInors(Document document, java.util.List<OrgTO> teachers, boolean returningSchool) throws DocumentException {
		if (!returningSchool) {
			document.newPage();
		}
		document.add(Chunk.NEWLINE);

		document.add(new Paragraph("Teacher Logins:\n", font));
		document.add(new Paragraph("\n"));

		PdfPTable table = getTeacherTableInors();

		PdfPCell c1 = new PdfPCell(new Phrase("User Name", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("User Password", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);
		table.setHeaderRows(1);

		if (teachers != null && teachers.size() == 0) {
			logger.warn("pdf generation : issueFound : teachers are blank");
			issueFound = true;
		} else {
			for (OrgTO tech : teachers) {
				if (tech.getOrgNodeId() == null || tech.getUserName() == null || tech.getPassword() == null) {
					logger.warn("pdf generation : issueFound : teacher org or teacher username or teacher password is null");
					issueFound = true;
				}

				table.addCell(new Phrase(tech.getUserName(), tableFont));
				table.addCell(new Phrase((tech.getPassword() == null) ? "" : tech.getPassword(), tableFont));
			}
		}

		document.add(table);
	}

	/**
	 * Create new page for each teacher user and add login information along
	 * with list of 5 students (alphabetically)
	 * 
	 * @param document
	 * @param prop
	 * @param tech
	 * @param schoolName
	 * @param schoolUserPresent
	 * @param newPagerRequired
	 * @param studentRequired
	 * @throws DocumentException
	 */
	private void addTeacherPageInors(Document document, Properties prop, OrgTO tech, String schoolName, boolean schoolUserPresent, boolean newPagerRequired,
			boolean studentRequired, Map<String, String> orgLabelMap) throws DocumentException {
		if (newPagerRequired) {
			document.newPage();
		}
		String userType = "";
		String labelOneText = orgLabelMap.get(ORG_NODE_LEVEL_STATE);
		String labelTwoText = orgLabelMap.get(ORG_NODE_LEVEL_DISTRICT);
		String labelThreeText = orgLabelMap.get(ORG_NODE_LEVEL_SCHOOL);

		String stateName = tech.getStateName();
		if (stateName == null)
			stateName = "";
		String testingSiteName = tech.getTestingSiteName();
		if (testingSiteName == null)
			testingSiteName = "";
		String countyName = tech.getCountyName();
		if (countyName == null)
			countyName = "";

		if (!schoolUserPresent) {
			userType = labelThreeText;
			document.add(new Paragraph("\n" + userType + ": " + schoolName, font));
		}
		if (ORG_NODE_LEVEL_EDU.equals(tech.getOrgNodeLevel())) {
			userType = "Education Center";
			document.add(new Paragraph(userType + " Name: " + tech.getElementName(), font));
		} else {
			userType = labelOneText;
			document.add(new Paragraph(userType + " Name: " + stateName, font));
		}
		if (ORG_NODE_LEVEL_DISTRICT.equals(tech.getOrgNodeLevel())) {
			userType = labelTwoText;
			document.add(new Paragraph(userType + " Name: " + countyName, font));
		} else if (ORG_NODE_LEVEL_SCHOOL.equals(tech.getOrgNodeLevel()) || ORG_NODE_LEVEL_CLASS.equals(tech.getOrgNodeLevel())) { // TODO
			userType = labelThreeText;
			document.add(new Paragraph(labelTwoText + " Name: " + countyName, font));
			document.add(new Paragraph(userType + " Name: " + testingSiteName, font));
		}

		String roleString = getUserListAsStringInors(tech.getRoleList(), userType);

		document.add(new Paragraph("User Roles: " + roleString, font));
		document.add(new Paragraph(" ", fontCourier));
		document.add(new Paragraph("User Name: " + tech.getUserName(), fontCourier));
		document.add(new Paragraph("User Password: " + tech.getPassword(), fontCourier));

		document.add(new Paragraph("\n" + prop.getProperty("TECH_TEXT"), font));

		document.add(new Paragraph("\nInstructions to Log-in:", font));
		List list = new List(false, 20);
		ListItem listItem = new ListItem(prop.getProperty("URL"), font);
		list.add(listItem);

		listItem = new ListItem(prop.getProperty("BLT1"), font);
		list.add(listItem);

		listItem = new ListItem(prop.getProperty("BLT2"), font);
		list.add(listItem);

		listItem = new ListItem(prop.getProperty("BLT3"), font);
		list.add(listItem);

		listItem = new ListItem(prop.getProperty("BLT3A"), font);
		list.add(listItem);

		document.add(list);

		if (studentRequired) {
			Table t = new Table(1, 6);
			t.setPadding(1.0F);
			float[] studentTableWidth = { 10f };
			t.setWidths(studentTableWidth);
			t.setAlignment(Element.ALIGN_LEFT);
			t.setBorderWidth(0.5F);

			java.util.List<UserTO> students = tech.getUsers();
			Cell c1 = new Cell(new Phrase("First " + students.size() + " students in the class (alphabetical order)", font));
			t.addCell(c1);
			if (students != null) {
				for (UserTO student : students) {
					c1 = new Cell(new Phrase(student.getFullName(), tableFont));
					t.addCell(c1);
				}
			}
			document.add(t);
		}
		document.add(new Paragraph(prop.getProperty("HELP"), font));
	}

	/**
	 * Generates pdf in specified format
	 * 
	 * @param prop
	 * @param school
	 * @param teachers
	 * @return
	 */
	public String generatePdfAcsiInors(Properties prop, OrgTO school, java.util.List<OrgTO> teachers, boolean schoolUserPresent, boolean isInitialLoad,
			boolean migration, boolean state, String docLoc) {
		Document document = null;
		String docName = docLoc;
		try {
			_img = Image.getInstance(prop.getProperty("imageLogo"));
			_img.scalePercent(50);

			document = new Document(PageSize.A4, 50.0F, 50.0F, 50.0F, 50.0F);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(docName));
			addMetaDataInors(document);
			document.open();
			document.setFooter(new HeaderFooter(new Phrase(prop.getProperty("footer"), smallFont), false));

			_img.scalePercent(50);
			document.add(_img);
			document.add(Chunk.NEWLINE);
			boolean pageBreakRequired = false;
			if (migration) {
				addLoginInstructionAcsiInors(document, prop, school, false, migration, state);
				addUserLoginsAcsiInors(document, school, state);
				pageBreakRequired = true;
			} else if (schoolUserPresent) {
				addLoginInstruction(document, prop, school, false, false, false);
				addUserLoginsAcsiInors(document, school, false);
				// next page
				addTeacherLoginsAcsiInors(document, teachers, false);
				pageBreakRequired = true;
			} else if (isInitialLoad) {
				// page for returning schools
				addLoginInstructionAcsiInors(document, prop, school, true, false, false);
				addTeacherLoginsAcsiInors(document, teachers, true);
				pageBreakRequired = true;
			}

			// new page for each teacher
			for (OrgTO tech : teachers) {
				addTeacherPageAcsiInors(document, prop, tech, school.getElementName(), schoolUserPresent, pageBreakRequired);
			}
			logger.info("Done !!!");
		} catch (Exception e2) {
			logger.error(e2.getMessage());
			e2.printStackTrace();
			issueFound = true;
			return null;
		} finally {
			if (document != null)
				document.close();
		}
		return docName;
	}

	/**
	 * Add login information and title in the first screen
	 * 
	 * @param document
	 * @param prop
	 * @param school
	 * @param font
	 * @throws DocumentException
	 */
	private void addLoginInstructionAcsiInors(Document document, Properties prop, OrgTO school, boolean returningSchool, boolean migration, boolean state)
			throws DocumentException {
		document.add(new Paragraph(CustomStringUtil.appendString(prop.getProperty("title"), prop.getProperty("testAdministrator"), " ",
				prop.getProperty("title1"), school.getElementName())));
		List list = null;
		ListItem listItem;

		if (state)
			document.add(new Paragraph(prop.getProperty("welcome_state"), fontBold));
		else
			document.add(new Paragraph(prop.getProperty("welcome"), fontBold));
		if (migration) {
			if (state)
				document.add(new Paragraph(prop.getProperty("TXT_ONE_MGR_STATE"), font));
			else
				document.add(new Paragraph(prop.getProperty("TXT_ONE_MGR"), font));
		} else if (returningSchool) {
			document.add(new Paragraph(prop.getProperty("TXT_ONE"), font));
			document.add(new Paragraph(prop.getProperty("TXT_TWO"), font));
		} else {
			document.add(new Paragraph(prop.getProperty("TXT_ONE"), font));
			document.add(new Paragraph(prop.getProperty("TXT_TWO"), font));
			document.add(new Paragraph(prop.getProperty("TXT_THREE"), font));
		}

		document.add(new Paragraph("\nUser Log-ins", fontBold));
		document.add(new Paragraph("\nInstructions to Log-in:", font));
		list = new List(false, 20);
		listItem = new ListItem(prop.getProperty("URL"), font);
		list.add(listItem);

		if (migration) {
			listItem = new ListItem(prop.getProperty("BLT1_MGR"), font);
			list.add(listItem);
			listItem = new ListItem(prop.getProperty("BLT2_MGR"), font);
			list.add(listItem);
			listItem = new ListItem(prop.getProperty("BLT3_MGR"), font);
			list.add(listItem);
			listItem = new ListItem(prop.getProperty("BLT3A_MGR"), fontBold);
			list.add(listItem);
		} else {
			listItem = new ListItem(prop.getProperty("BLT1"), font);
			list.add(listItem);
			listItem = new ListItem(prop.getProperty("BLT2"), font);
			list.add(listItem);
		}
		document.add(list);
	}

	/**
	 * Add school login informations in tabular format
	 * 
	 * @param document
	 * @param school
	 * @param font
	 * @param tableFont
	 * @throws DocumentException
	 */
	private void addUserLoginsAcsiInors(Document document, OrgTO school, boolean state) throws DocumentException {
		PdfPTable table = getTableAcsiInors();
		document.add(new Paragraph("\n"));
		document.add(new Paragraph("\n"));

		PdfPCell c1 = new PdfPCell(new Phrase("User Log-in Type", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Grade", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("User Name", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("User Password", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);
		table.setHeaderRows(1);

		java.util.List<UserTO> schoolUsers = school.getUsers();
		if (schoolUsers != null && schoolUsers.size() == 0) {
			logger.info("pdf generation : issueFound : no school user found");
			issueFound = true;
		}
		String orgType = (state) ? "ACSI" : "School";
		for (UserTO schUser : schoolUsers) {
			if (schUser.isAdminUser()) {
				table.addCell(new Phrase(orgType + " Administrative", tableFont));
			} else {
				continue;
			}
			if (school.getTenantId() == null || schUser.getPassword() == null) {
				logger.info("pdf generation : issueFound : school tenantId or password is blank");
				issueFound = true;
			}
			table.addCell(new Phrase("All", tableFont));
			table.addCell(new Phrase(schUser.getUserName(), tableFont));
			table.addCell(new Phrase((schUser.getPassword() == null) ? "" : schUser.getPassword(), tableFont));
		}
		for (UserTO schUser : schoolUsers) {
			if (schUser.isAdminUser()) {
				continue;
			} else {
				table.addCell(new Phrase(orgType + " Regular", tableFont));
			}
			if (school.getTenantId() == null || schUser.getPassword() == null) {
				logger.info("pdf generation : issueFound : school tenantId or password is blank");
				issueFound = true;
			}
			table.addCell(new Phrase("All", tableFont));
			table.addCell(new Phrase(schUser.getUserName(), tableFont));
			table.addCell(new Phrase((schUser.getPassword() == null) ? "" : schUser.getPassword(), tableFont));
		}
		document.add(table);
	}

	/**
	 * Create one PDF table
	 * 
	 * @return
	 * @throws DocumentException
	 */
	private PdfPTable getTableAcsiInors() throws DocumentException {
		float[] colsWidth = { 4f, 2f, 3f, 3f };
		PdfPTable table = new PdfPTable(4);
		table.setWidths(colsWidth);
		table.setWidthPercentage(100);
		return table;
	}

	/**
	 * Add teacher login information in next page
	 * 
	 * @param document
	 * @param teachers
	 * @param font
	 * @param tableFont
	 * @throws DocumentException
	 */
	private void addTeacherLoginsAcsiInors(Document document, java.util.List<OrgTO> teachers, boolean returningSchool) throws DocumentException {
		if (!returningSchool) {
			document.newPage();
			document.add(_img);
		}
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("Teacher Log-ins:\n", font));
		document.add(new Paragraph("\n"));

		PdfPTable table = getTeacherTableInors();

		PdfPCell c1 = new PdfPCell(new Phrase("Teacher Name", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Grade", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("No of Students in Class", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("User Name", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("User Password", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);
		table.setHeaderRows(1);

		if (teachers != null && teachers.size() == 0) {
			logger.info("pdf generation : issueFound : teachers are blank");
			issueFound = true;
		}
		for (OrgTO tech : teachers) {
			if (tech.getFullName() == null || tech.getTenantId() == null || tech.getPassword() == null) {
				logger.info("pdf generation : issueFound : teacher fullname or teacher tenant id or teacher password is null");
				issueFound = true;
			}
			table.addCell(new Phrase((tech.getFullName() == null) ? "" : tech.getFullName(), tableFont));
			table.addCell(new Phrase(tech.getGrade(), tableFont));
			table.addCell(new Phrase(tech.getStudentCount(), tableFont));
			table.addCell(new Phrase(tech.getUserName(), tableFont));
			table.addCell(new Phrase((tech.getPassword() == null) ? "" : tech.getPassword(), tableFont));
		}
		document.add(table);
	}

	/**
	 * Create new page for each teacher user and add login information along
	 * with list of 5 students (alphabetically)
	 * 
	 * @param document
	 * @param prop
	 * @param tech
	 * @param font
	 * @param tableFont
	 * @throws DocumentException
	 */
	private void addTeacherPageAcsiInors(Document document, Properties prop, OrgTO tech, String schoolName, boolean schoolUserPresent, boolean newPagerRequired)
			throws DocumentException {
		if (newPagerRequired) {
			document.newPage();
			document.add(_img);
			document.add(Chunk.NEWLINE);
		}
		if (!schoolUserPresent) {
			document.add(new Paragraph(CustomStringUtil.appendString("\nSchool: ", schoolName), fontBold));
		}
		document.add(new Paragraph("Teacher: " + tech.getFullName() + ", Grade: " + tech.getGrade(), font));
		document.add(new Paragraph("Test Administration: " + prop.getProperty("testAdministrator"), font));
		document.add(new Paragraph("\nUsername: " + tech.getUserName(), fontCourier));
		document.add(new Paragraph("User Password: " + tech.getPassword(), fontCourier));

		document.add(new Paragraph("\n" + prop.getProperty("TECH_TEXT"), font));

		document.add(new Paragraph("\nInstructions to Log-in:", font));
		List list = new List(false, 20);
		ListItem listItem = new ListItem(prop.getProperty("URL"), font);
		list.add(listItem);

		listItem = new ListItem(prop.getProperty("BLT4"), font);
		list.add(listItem);

		listItem = new ListItem(prop.getProperty("BLT5"), font);
		list.add(listItem);

		document.add(list);

		Table t = new Table(2, 6);
		t.setPadding(1.0F);
		float[] studentTableWidth = { 3f, 10f };
		t.setWidths(studentTableWidth);
		t.setAlignment(Element.ALIGN_LEFT);
		t.setBorderWidth(0.5F);

		java.util.List<UserTO> students = tech.getUsers();
		Cell c1 = new Cell(new Phrase("Grade", font));
		t.addCell(c1);
		c1 = new Cell(new Phrase("First " + students.size() + " students in the class (alphabetical order)", font));
		t.addCell(c1);
		if (students != null) {
			for (UserTO student : students) {
				c1 = new Cell(new Phrase(student.getGrade(), tableFont));
				t.addCell(c1);
				c1 = new Cell(new Phrase(student.getStudentName(), tableFont));
				t.addCell(c1);
			}
		}
		document.add(t);
	}

	/**
	 * Generates pdf in specified format
	 * 
	 * @param prop
	 * @param school
	 * @param teachers
	 * @return
	 */
	public String generatePdfIstep(Properties configProperties, Properties istepProperties, OrgTO school, java.util.List<OrgTO> teachers,
			boolean schoolUserPresent, boolean isInitialLoad, boolean migration, boolean state) {
		Document document = null;
		String docName = null;
		try {
			document = new Document(PageSize.A4, 50.0F, 50.0F, 20.0F, 75.0F);
			docName = getPdfNameIstep(configProperties, istepProperties, school);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(docName));
			writer.setPageEvent(prismPageEventListener);
			addMetaDataIstep(document);

			document.open();
			boolean pageBreakRequired = false;
			if (migration) {
				addLoginInstructionIstep(document, istepProperties, school, false, migration, state);
				addUserLoginsIstep(document, school, state);
				pageBreakRequired = true;
			} else if (schoolUserPresent) {
				addLoginInstructionIstep(document, istepProperties, school, false, false, false);

				addUserLoginsIstep(document, school, false);

				// next page
				if (teachers != null && teachers.get(0).getUsers() != null) {
					addTeacherLoginsIstep(document, teachers, false);
				}
				pageBreakRequired = true;
			} else if (isInitialLoad) {
				// page for returning schools
				addLoginInstructionIstep(document, istepProperties, school, true, false, false);
				addTeacherLoginsIstep(document, teachers, true);
				pageBreakRequired = true;
			}

			// new page for each teacher
			if (teachers != null) {
				for (OrgTO tech : teachers) {
					boolean studentListRequired = (tech.getUsers() == null) ? false : true;
					addTeacherPageIstep(document, istepProperties, tech, school.getElementName(), schoolUserPresent, pageBreakRequired, studentListRequired);
				}
			}
			logger.info("Pdf generated.");
		} catch (Exception e2) {
			logger.info(e2.getMessage());
			e2.printStackTrace();
			issueFound = true;
			return null;
		} finally {
			if (document != null) {
				document.close();
			}
		}
		return docName;
	}

	public String generateDistrictPdfIstep(Properties configProperties, Properties istepProperties, OrgTO district, java.util.List<OrgTO> teachers,
			boolean districtUserPresent, boolean isInitialLoad, boolean migration, boolean state) {
		Document document = null;
		String docName = null;
		try {
			document = new Document(PageSize.A4, 50.0F, 50.0F, 20.0F, 75.0F);
			docName = getDistrictPdfNameIstep(configProperties, istepProperties, district);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(docName));
			writer.setPageEvent(prismPageEventListener);
			addMetaDataIstep(document);

			document.open();
			boolean pageBreakRequired = false;
			if (districtUserPresent) {
				addDistrictLoginInstructionIstep(document, istepProperties, district, false, false, false);

				addDistrictLoginsIstep(document, district, false);

				// next page
				if (teachers != null && teachers.get(0).getUsers() != null) {
					addTeacherLoginsIstep(document, teachers, false);
				}
				pageBreakRequired = true;
			}

			// new page for each teacher
			if (teachers != null) {
				for (OrgTO tech : teachers) {
					boolean studentListRequired = (tech.getUsers() == null) ? false : true;
					addDistrictPageIstep(document, istepProperties, tech, district.getElementName() + " (" + district.getOrgNodeCode() + ")",
							districtUserPresent, pageBreakRequired, studentListRequired);
				}
			}
			logger.info("Pdf generated.");
		} catch (Exception e2) {
			logger.info(e2.getMessage());
			e2.printStackTrace();
			issueFound = true;
			return null;
		} finally {
			if (document != null) {
				document.close();
			}
		}
		return docName;
	}

	private String getPdfNameIstep(Properties configProperties, Properties istepProperties, OrgTO school) {
		return configProperties.getProperty("pdfGenPath") + File.separator + istepProperties.getProperty("tempPdfLocation")
				+ istepProperties.getProperty("districtText") + school.getParentOrgNodeCode() + istepProperties.getProperty("schoolText")
				+ school.getOrgNodeCode() + "_" + school.getDateStrWtYear() + ".pdf";
	}

	private String getDistrictPdfNameIstep(Properties configProperties, Properties istepProperties, OrgTO school) {
		return configProperties.getProperty("pdfGenPathDist") + File.separator + istepProperties.getProperty("tempPdfLocation")
				+ istepProperties.getProperty("districtText")
				// + school.getParentOrgNodeCode()
				// + istepProperties.getProperty("schoolText")
				+ school.getOrgNodeCode() + "_" + school.getDateStrWtYear() + ".pdf";
	}

	/**
	 * Add metadata to PDF file
	 * 
	 * @param document
	 */
	private void addMetaDataIstep(Document document) {
		document.addTitle("ISTEP");
		document.addAuthor("ISTEP");
		document.addCreator("ISTEP-CTB");
		document.addSubject("Login credential details");
	}

	/**
	 * Add login information and title in the first screen
	 * 
	 * @param document
	 * @param prop
	 * @param school
	 * @param font
	 * @throws DocumentException
	 */
	private void addLoginInstructionIstep(Document document, Properties prop, OrgTO school, boolean returningSchool, boolean migration, boolean state)
			throws DocumentException {
		document.add(new Paragraph(prop.getProperty("title") + school.getElementName()));
		List list = null;
		ListItem listItem;

		if (state) {
			document.add(new Paragraph(prop.getProperty("welcome_state"), fontBold));
		} else {
			document.add(new Paragraph(prop.getProperty("welcome"), fontBold));
		}
		if (migration) {
			if (state) {
				document.add(new Paragraph(prop.getProperty("TXT_ONE_MGR_STATE"), font));
			} else {
				document.add(new Paragraph(prop.getProperty("TXT_ONE_MGR"), font));
			}
		} else if (returningSchool) {
			document.add(new Paragraph(prop.getProperty("TXT_ONE_RET"), font));
			document.add(new Paragraph(prop.getProperty("TXT_TWO_RET"), font));
			document.add(new Paragraph(prop.getProperty("TXT_THR_RET"), font));
		} else {
			document.add(new Paragraph(prop.getProperty("TXT_ONE"), font));
			document.add(new Paragraph(prop.getProperty("TXT_TWO"), font));
			document.add(new Paragraph(prop.getProperty("TXT_THREE"), font));
		}

		document.add(new Paragraph("\nUser Log-ins", fontBold));
		document.add(new Paragraph("\nInstructions to Log-in to the Growth Reports on the Indiana Online Reporting System:", font));
		list = new List(false, 20);
		listItem = new ListItem(prop.getProperty("URL"), font);
		list.add(listItem);

		if (migration) {
			listItem = new ListItem(prop.getProperty("BLT1_MGR"), font);
			list.add(listItem);

			listItem = new ListItem(prop.getProperty("BLT2_MGR"), font);
			list.add(listItem);

			listItem = new ListItem(prop.getProperty("BLT3_MGR"), font);
			list.add(listItem);

			listItem = new ListItem(prop.getProperty("BLT3A_MGR"), fontBold);
			list.add(listItem);
		} else {
			listItem = new ListItem(prop.getProperty("BLT1"), font);
			list.add(listItem);

			listItem = new ListItem(prop.getProperty("BLT2"), font);
			list.add(listItem);
		}
		document.add(list);
	}

	private void addDistrictLoginInstructionIstep(Document document, Properties prop, OrgTO district, boolean returningDistrict, boolean migration,
			boolean state) throws DocumentException {
		document.add(new Paragraph(prop.getProperty("districtTitle") + district.getElementName() + " (" + district.getOrgNodeCode() + ")"));
		List list = null;
		ListItem listItem;

		document.add(new Paragraph(prop.getProperty("districtWelcome"), fontBold));
		document.add(new Paragraph(prop.getProperty("TXT_ONE_DISTRICT"), font));
		document.add(new Paragraph(prop.getProperty("TXT_TWO_DISTRICT"), font));
		document.add(new Paragraph(prop.getProperty("TXT_THR_DISTRICT"), font));

		document.add(new Paragraph("\nInstructions to Log-in to the Growth Reports on the Indiana Online Reporting System:", font));
		list = new List(false, 20);
		listItem = new ListItem(prop.getProperty("URL"), font);
		list.add(listItem);

		if (migration) {
			listItem = new ListItem(prop.getProperty("BLT1_MGR"), font);
			list.add(listItem);

			listItem = new ListItem(prop.getProperty("BLT2_MGR"), font);
			list.add(listItem);

			listItem = new ListItem(prop.getProperty("BLT3_MGR"), font);
			list.add(listItem);

			listItem = new ListItem(prop.getProperty("BLT3A_MGR"), fontBold);
			list.add(listItem);
		} else {
			listItem = new ListItem(prop.getProperty("BLT1"), font);
			list.add(listItem);

			listItem = new ListItem(prop.getProperty("BLT2"), font);
			list.add(listItem);
		}
		document.add(list);
	}

	/**
	 * Create one PDF table
	 * 
	 * @return
	 * @throws DocumentException
	 */
	private PdfPTable getTableIstep() throws DocumentException {
		float[] colsWidth = { 3f, 3f, 3f };
		PdfPTable table = new PdfPTable(3);
		table.setWidths(colsWidth);
		table.setWidthPercentage(100);
		return table;
	}

	/**
	 * Create one PDF table with 5 column
	 * 
	 * @return
	 * @throws DocumentException
	 */
	private PdfPTable getTeacherTableIstep() throws DocumentException {
		float[] colsWidth = { 4f, 1f, 2f, 3f, 3f };
		PdfPTable table = new PdfPTable(5);
		table.setWidths(colsWidth);
		table.setWidthPercentage(100);
		return table;
	}

	/**
	 * Add school login informations in tabular format
	 * 
	 * @param document
	 * @param school
	 * @param font
	 * @param tableFont
	 * @throws DocumentException
	 */
	private void addUserLoginsIstep(Document document, OrgTO school, boolean state) throws DocumentException {
		PdfPTable table = getTableIstep();
		document.add(new Paragraph("\n"));
		document.add(new Paragraph("\nTeacher Log-ins", fontBold));
		document.add(new Paragraph("\n"));

		PdfPCell c1 = new PdfPCell(new Phrase("User Name", tableHeaderFont));
		c1.setMinimumHeight(18);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("User Password", tableHeaderFont));
		c1.setMinimumHeight(18);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("User Type", tableHeaderFont));
		c1.setMinimumHeight(18);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);
		table.setHeaderRows(1);

		java.util.List<UserTO> schoolUsers = school.getUsers();
		if (schoolUsers != null && schoolUsers.size() == 0) {
			logger.info("pdf generation : issueFound : no school user found");
			issueFound = true;
		}

		for (UserTO schUser : schoolUsers) {
			if (school.getTenantId() == null || schUser.getPassword() == null) {
				logger.info("pdf generation : issueFound : school tenantId or password is blank");
				issueFound = true;
			}
			PdfPCell userNameCell = new PdfPCell(new Phrase(schUser.getUserName(), tableFont));
			// userNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			userNameCell.setMinimumHeight(20);
			table.addCell(userNameCell);
			table.addCell(new Phrase((schUser.getPassword() == null) ? "" : schUser.getPassword(), tableFont));
			table.addCell(new Phrase(schUser.getUserType(), tableFont));
		}

		document.add(table);
	}

	private void addDistrictLoginsIstep(Document document, OrgTO district, boolean state) throws DocumentException {
		PdfPTable table = getTableIstep();
		document.add(new Paragraph("\n"));
		document.add(new Paragraph("\nDistrict User Log-in Account:", fontBold));
		document.add(new Paragraph("\n"));

		PdfPCell c1 = new PdfPCell(new Phrase("User Name", tableHeaderFont));
		c1.setMinimumHeight(18);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("User Password", tableHeaderFont));
		c1.setMinimumHeight(18);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("User Type", tableHeaderFont));
		c1.setMinimumHeight(18);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);
		table.setHeaderRows(1);

		java.util.List<UserTO> schoolUsers = district.getUsers();
		if (schoolUsers != null && schoolUsers.size() == 0) {
			logger.info("pdf generation : issueFound : no school user found");
			issueFound = true;
		}

		for (UserTO schUser : schoolUsers) {
			if (district.getTenantId() == null || schUser.getPassword() == null) {
				logger.info("pdf generation : issueFound : school tenantId or password is blank");
				issueFound = true;
			}
			// table.addCell(new Phrase(schUser.getUserName(), tableFont));
			PdfPCell userNameCell = new PdfPCell(new Phrase(schUser.getUserName(), tableFont));
			// userNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			userNameCell.setMinimumHeight(20);
			table.addCell(userNameCell);
			table.addCell(new Phrase((schUser.getPassword() == null) ? "" : schUser.getPassword(), tableFont));
			if ("2".equals(schUser.getOrgLevel())) {
				table.addCell(new Phrase("District User", tableFont));
			} else {
				table.addCell(new Phrase(schUser.getUserType(), tableFont));
			}
		}

		document.add(table);
	}

	/**
	 * Add teacher login information in next page
	 * 
	 * @param document
	 * @param teachers
	 * @param font
	 * @param tableFont
	 * @throws DocumentException
	 */
	private void addTeacherLoginsIstep(Document document, java.util.List<OrgTO> teachers, boolean returningSchool) throws DocumentException {
		if (!returningSchool) {
			document.newPage();
		}
		document.add(Chunk.NEWLINE);

		document.add(new Paragraph("Teacher Log-ins:\n", font));
		document.add(new Paragraph("\n"));

		PdfPTable table = getTeacherTableIstep();

		PdfPCell c1 = new PdfPCell(new Phrase("User Name", tableHeaderFont));
		c1.setMinimumHeight(18);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("User Password", tableHeaderFont));
		c1.setMinimumHeight(18);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);
		table.setHeaderRows(1);

		if (teachers != null && teachers.size() == 0) {
			logger.info("pdf generation : issueFound : teachers are blank");
			issueFound = true;
		} else {
			for (OrgTO tech : teachers) {
				if (tech.getTenantId() == null || tech.getUserName() == null || tech.getPassword() == null) {
					logger.info("pdf generation : issueFound : teacher org or teacher username or teacher password is null");
					issueFound = true;
				}
				// table.addCell(new Phrase(tech.getUserName(), tableFont));
				PdfPCell userNameCell = new PdfPCell(new Phrase(tech.getUserName(), tableFont));
				// userNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				userNameCell.setMinimumHeight(20);
				table.addCell(userNameCell);
				table.addCell(new Phrase((tech.getPassword() == null) ? "" : tech.getPassword(), tableFont));
			}
		}
		document.add(table);
	}

	/**
	 * Create new page for each teacher user and add login information along
	 * with list of 5 students (alphabetically)
	 * 
	 * @param document
	 * @param prop
	 * @param tech
	 * @param font
	 * @param tableFont
	 * @throws DocumentException
	 */
	private void addTeacherPageIstep(Document document, Properties prop, OrgTO tech, String schoolName, boolean schoolUserPresent, boolean newPagerRequired,
			boolean studentRequired) throws DocumentException {
		if (newPagerRequired) {
			document.newPage();
		}

		if (prop.getProperty("userTypeP").equals(tech.getUserType())) {
			document.add(new Paragraph(prop.getProperty("title_principal")));
		} else {
			document.add(new Paragraph(prop.getProperty("title")));
			if (!schoolUserPresent) {
				document.add(new Paragraph("\nSchool: " + schoolName, font));
			}
			document.add(new Paragraph("\nSchool Personnel Number: " + tech.getFullName(), fontBold));
		}

		document.add(new Paragraph("User Name: " + tech.getUserName(), fontCourier));
		document.add(new Paragraph("User Password: " + tech.getPassword(), fontCourier));

		if (prop.getProperty("userTypeP").equals(tech.getUserType())) {
			document.add(new Paragraph("\n" + prop.getProperty("PRINCIPAL_TEXT"), font));
		} else {
			document.add(new Paragraph("\n" + prop.getProperty("TECH_TEXT"), font));
		}

		document.add(new Paragraph("\nInstructions to Log-in to the Growth Reports on the Indiana Online Reporting System:", font));
		List list = new List(false, 20);
		ListItem listItem = new ListItem(prop.getProperty("URL"), font);
		list.add(listItem);

		listItem = new ListItem(prop.getProperty("BLT4"), font);
		list.add(listItem);

		listItem = new ListItem(prop.getProperty("BLT5"), font);
		list.add(listItem);

		document.add(list);

		if (studentRequired) {
			Table t = new Table(1, 6);

			t.setPadding(1.0F);
			float[] studentTableWidth = { 10f };
			t.setWidths(studentTableWidth);
			t.setAlignment(Element.ALIGN_LEFT);

			t.setBorderWidth(0.5F);

			java.util.List<UserTO> students = tech.getUsers();
			Cell c1 = new Cell(new Phrase("First " + students.size() + " students in the class (alphabetical order)", font));
			t.addCell(c1);
			if (students != null) {
				for (UserTO student : students) {
					c1 = new Cell(new Phrase(student.getStudentName(), tableFont));
					t.addCell(c1);
				}
			}
			document.add(t);
		}
	}

	private void addDistrictPageIstep(Document document, Properties prop, OrgTO tech, String districtName, boolean districtUserPresent,
			boolean newPagerRequired, boolean studentRequired) throws DocumentException {
		if (newPagerRequired) {
			document.newPage();
		}
		document.add(new Paragraph(prop.getProperty("districtTitle") + districtName));
		if (!districtUserPresent) {
			document.add(new Paragraph("\nDistrict: " + districtName, font));
		}
		document.add(new Paragraph("\nSchool Personnel Number: " + tech.getFullName(), fontBold));

		document.add(new Paragraph("User Name: " + tech.getUserName(), fontCourier));
		document.add(new Paragraph("User Password: " + tech.getPassword(), fontCourier));

		document.add(new Paragraph("\n" + prop.getProperty("TXT_ONE_DISTRICT"), font));

		document.add(new Paragraph("\nInstructions to Log-in to the Growth Reports on the Indiana Online Reporting System:", font));
		List list = new List(false, 20);
		ListItem listItem = new ListItem(prop.getProperty("URL"), font);
		list.add(listItem);

		listItem = new ListItem(prop.getProperty("BLT4"), font);
		list.add(listItem);

		listItem = new ListItem(prop.getProperty("BLT5"), font);
		list.add(listItem);

		document.add(list);

		if (studentRequired) {
			Table t = new Table(1, 6);

			t.setPadding(1.0F);
			float[] studentTableWidth = { 10f };
			t.setWidths(studentTableWidth);
			t.setAlignment(Element.ALIGN_LEFT);

			t.setBorderWidth(0.5F);

			java.util.List<UserTO> students = tech.getUsers();
			Cell c1 = new Cell(new Phrase("First " + students.size() + " students in the class (alphabetical order)", font));
			t.addCell(c1);
			if (students != null) {
				for (UserTO student : students) {
					c1 = new Cell(new Phrase(student.getStudentName(), tableFont));
					t.addCell(c1);
				}
			}
			document.add(t);
		}
	}

	/**
	 * Generates pdf in specified format
	 * 
	 * @param prop
	 * @param school
	 * @param teachers
	 * @param schoolUserPresent
	 * @param isInitialLoad
	 * @param migration
	 * @param state
	 * @param orgLabelMap
	 * @return
	 */
	public String generatePdfTasc(Properties prop, OrgTO school, java.util.List<OrgTO> teachers, boolean schoolUserPresent, boolean isInitialLoad,
			boolean migration, boolean state, Map<String, String> orgLabelMap) {
		Document document = null;
		String docName = null;
		try {
			document = new Document(PageSize.A4, 50.0F, 50.0F, 20.0F, 75.0F);
			docName = prop.getProperty("pdfGenPath") + File.separator + prop.getProperty("tempPdfLocation") + prop.getProperty("districtText")
					+ prop.getProperty("schoolText") + school.getDateStrWtYear() + ".pdf";

			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(docName));
			writer.setPageEvent(prismPageEventListener);
			addMetaDataTasc(document);

			document.open();

			boolean pageBreakRequired = false;
			if (migration) {
				addLoginInstruction(document, prop, school, false, migration, state);
				addUserLoginsTasc(document, school, state, orgLabelMap);
				document.add(new Paragraph(prop.getProperty("HELP"), font));
				pageBreakRequired = true;
			} else if (schoolUserPresent) {
				addLoginInstruction(document, prop, school, false, false, false);
				addUserLoginsTasc(document, school, false, orgLabelMap);
				document.add(new Paragraph(prop.getProperty("HELP"), font));
				// next page
				if (teachers != null && teachers.get(0).getUsers() != null)
					addTeacherLoginsTasc(document, teachers, false);
				pageBreakRequired = true;
			} else if (isInitialLoad) {
				// page for returning schools
				addLoginInstruction(document, prop, school, true, false, false);
				addTeacherLoginsTasc(document, teachers, true);
				pageBreakRequired = true;
			}
			// new page for each teacher
			if (teachers != null) {
				for (OrgTO tech : teachers) {
					boolean studentListRequired = (tech.getUsers() == null) ? false : true;
					addTeacherPageTasc(document, prop, tech, school.getElementName(), schoolUserPresent, pageBreakRequired, studentListRequired, orgLabelMap);
				}
			}
			logger.debug("PDF generated");
		} catch (Exception e2) {
			logger.error(e2.getMessage());
			e2.printStackTrace();
			issueFound = true;
			return null;
		} finally {
			if (document != null)
				document.close();
		}
		return docName;
	}

	/**
	 * Add metadata to PDF file
	 * 
	 * @param document
	 */
	private void addMetaDataTasc(Document document) {
		document.addTitle("ISTEP");
		document.addAuthor("ISTEP");
		document.addCreator("ISTEP-CTB");
		document.addSubject("Login credential details");
	}

	/**
	 * Create one PDF table with 5 column
	 * 
	 * @return
	 * @throws DocumentException
	 */
	private PdfPTable getTeacherTableTasc() throws DocumentException {
		float[] colsWidth = { 4f, 1f, 2f, 3f, 3f };
		PdfPTable table = new PdfPTable(5);
		table.setWidths(colsWidth);
		table.setWidthPercentage(100);
		return table;
	}

	/**
	 * Create itext PDF table with 4, 6 or 8 columns based on orgNodeLevel
	 * 
	 * @param orgNodeLevel
	 * @return
	 * @throws DocumentException
	 */
	private PdfPTable getUserAccountsTableTasc(String orgNodeLevel) throws DocumentException {
		float[] colsWidthState = { 3f, 3f, 3f, 3f };
		float[] colsWidthStateCounty = { 3f, 3f, 3f, 3f, 3f };
		float[] colsWidthTestingSite = { 2.5f, 3f, 3f, 3f, 4f, 2.5f };
		if (ORG_NODE_LEVEL_EDU.equals(orgNodeLevel)) {
			PdfPTable table = new PdfPTable(4);
			table.setWidths(colsWidthState);
			table.setWidthPercentage(100);
			return table;
		} else if (ORG_NODE_LEVEL_STATE.equals(orgNodeLevel)) {
			PdfPTable table = new PdfPTable(4);
			table.setWidths(colsWidthState);
			table.setWidthPercentage(100);
			return table;
		} else if (ORG_NODE_LEVEL_COUNTY.equals(orgNodeLevel)) {
			PdfPTable table = new PdfPTable(5);
			table.setWidths(colsWidthStateCounty);
			table.setWidthPercentage(100);
			return table;
		} else if (ORG_NODE_LEVEL_SITE.equals(orgNodeLevel)) {
			PdfPTable table = new PdfPTable(6);
			table.setWidths(colsWidthTestingSite);
			table.setWidthPercentage(100);
			return table;
		}
		return null;
	}

	/**
	 * Add school login informations in tabular format
	 * 
	 * @param document
	 * @param school
	 * @param state
	 * @throws DocumentException
	 */
	private void addUserLoginsTasc(Document document, OrgTO school, boolean state, Map<String, String> orgLabelMap) throws DocumentException {
		String orgNodeLevel = school.getOrgNodeLevel();
		PdfPTable table = getUserAccountsTableTasc(orgNodeLevel);
		document.add(new Paragraph("\n"));

		String labelOneText = orgLabelMap.get(ORG_NODE_LEVEL_STATE);
		String labelTwoText = orgLabelMap.get(ORG_NODE_LEVEL_COUNTY);
		String labelThreeText = orgLabelMap.get(ORG_NODE_LEVEL_SITE);
		String userType = "";

		PdfPCell c1 = null;
		if (ORG_NODE_LEVEL_EDU.equals(orgNodeLevel)) {
			userType = "Education Center";
			c1 = new PdfPCell(new Phrase(userType, tableHeaderFont));
		} else {
			userType = labelOneText;
			c1 = new PdfPCell(new Phrase(userType, tableHeaderFont));
		}
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		if (ORG_NODE_LEVEL_COUNTY.equals(orgNodeLevel)) {
			userType = labelTwoText;
			c1 = new PdfPCell(new Phrase(userType + " Name", tableHeaderFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBackgroundColor(Color.lightGray);
			table.addCell(c1);

			/*
			 * c1 = new PdfPCell(new Phrase(userType + " Code",
			 * tableHeaderFont));
			 * c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			 * c1.setBackgroundColor(Color.lightGray); table.addCell(c1);
			 */
		} else if (ORG_NODE_LEVEL_SITE.equals(orgNodeLevel)) {
			userType = labelThreeText;
			c1 = new PdfPCell(new Phrase(labelTwoText + " Name", tableHeaderFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBackgroundColor(Color.lightGray);
			table.addCell(c1);

			/*
			 * c1 = new PdfPCell(new Phrase(labelTwoText + " Code",
			 * tableHeaderFont));
			 * c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			 * c1.setBackgroundColor(Color.lightGray); table.addCell(c1);
			 */

			c1 = new PdfPCell(new Phrase(userType + " Name", tableHeaderFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBackgroundColor(Color.lightGray);
			table.addCell(c1);

			/*
			 * c1 = new PdfPCell(new Phrase(userType + " Code",
			 * tableHeaderFont));
			 * c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			 * c1.setBackgroundColor(Color.lightGray); table.addCell(c1);
			 */
		}

		c1 = new PdfPCell(new Phrase("User Roles", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("User Name", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("User Password", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);
		table.setHeaderRows(1);

		java.util.List<UserTO> schoolUsers = school.getUsers();
		if (schoolUsers != null && schoolUsers.size() == 0) {
			logger.warn("pdf generation : issueFound : no school user found");
			issueFound = true;
		}
		for (UserTO schUser : schoolUsers) {
			if (schUser.getPassword() == null) {
				logger.warn("pdf generation : issueFound : school tenantId or password is blank");
				issueFound = true;
			}
			String stateName = schUser.getStateName();
			if (stateName == null)
				stateName = "";
			String orgNodeName = schUser.getOrgNodeName();
			if (orgNodeName == null)
				orgNodeName = "";
			if (ORG_NODE_LEVEL_EDU.equals(orgNodeLevel))
				table.addCell(new Phrase(orgNodeName, tableFont));
			else
				table.addCell(new Phrase(stateName, tableFont));

			String countyName = schUser.getCountyName();
			if (countyName == null)
				countyName = "";

			Integer countyCode = schUser.getCountyCode();
			String countryCodeString = "";
			if (countyCode != null)
				countryCodeString = countyCode.toString();

			Integer testingSiteCode = schUser.getTestingSiteCode();
			String testingSiteCodeString = "";
			if (testingSiteCode != null)
				testingSiteCodeString = testingSiteCode.toString();

			String testingSiteName = schUser.getTestingSiteName();
			if (testingSiteName == null)
				testingSiteName = "";
			if (ORG_NODE_LEVEL_COUNTY.equals(orgNodeLevel)) {
				table.addCell(new Phrase(countyName, tableFont));
				// table.addCell(new Phrase(countryCodeString, tableFont));
			} else if (ORG_NODE_LEVEL_SITE.equals(orgNodeLevel)) {
				table.addCell(new Phrase(countyName, tableFont));
				// table.addCell(new Phrase(countryCodeString, tableFont));
				table.addCell(new Phrase(testingSiteName, tableFont));
				// table.addCell(new Phrase(testingSiteCodeString, tableFont));
			}

			int adminIndex = getAdminIndexTasc(schUser.getRoles());
			if (adminIndex != -1) {
				table.addCell(new Phrase(userType + " " + schUser.getRoles().get(adminIndex), tableFont));
			} else {
				String roleString = getUserListAsStringTasc(schUser.getRoles(), userType);
				table.addCell(new Phrase(roleString, tableFont));
			}
			table.addCell(new Phrase(schUser.getUserName(), tableFont));
			table.addCell(new Phrase((schUser.getPassword() == null) ? "" : schUser.getPassword(), tableFont));
		}
		document.add(table);
	}

	/**
	 * Creates a comma separated string from a List
	 * 
	 * @param roles
	 * @param userType
	 * @return
	 */
	private String getUserListAsStringTasc(java.util.List<String> roles, String userType) {
		System.out.println(roles);
		StringBuilder roleString = new StringBuilder();
		for (String role : roles) {
			roleString.insert(roleString.length(), CustomStringUtil.appendString(userType, " ", role, ", "));
		}
		return roleString.toString().substring(0, roleString.length() - 2);
	}

	/**
	 * Checks whether the user has admin role or not
	 * 
	 * @param roles
	 * @return
	 */
	private int getAdminIndexTasc(java.util.List<String> roles) {
		int adminIndex = -1;
		for (int i = 0; i < roles.size(); i++) {
			if (roles.get(i).toLowerCase().contains("admin")) {
				return i;
			}
		}
		return adminIndex;
	}

	/**
	 * Add teacher login information in next page
	 * 
	 * @param document
	 * @param teachers
	 * @param returningSchool
	 * @throws DocumentException
	 */
	private void addTeacherLoginsTasc(Document document, java.util.List<OrgTO> teachers, boolean returningSchool) throws DocumentException {
		if (!returningSchool) {
			document.newPage();
		}
		document.add(Chunk.NEWLINE);

		document.add(new Paragraph("Teacher Logins:\n", font));
		document.add(new Paragraph("\n"));

		PdfPTable table = getTeacherTableTasc();

		PdfPCell c1 = new PdfPCell(new Phrase("User Name", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("User Password", tableHeaderFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(Color.lightGray);
		table.addCell(c1);
		table.setHeaderRows(1);

		if (teachers != null && teachers.size() == 0) {
			logger.warn("pdf generation : issueFound : teachers are blank");
			issueFound = true;
		} else {
			for (OrgTO tech : teachers) {
				if (tech.getOrgNodeId() == null || tech.getUserName() == null || tech.getPassword() == null) {
					logger.warn("pdf generation : issueFound : teacher org or teacher username or teacher password is null");
					issueFound = true;
				}

				table.addCell(new Phrase(tech.getUserName(), tableFont));
				table.addCell(new Phrase((tech.getPassword() == null) ? "" : tech.getPassword(), tableFont));
			}
		}

		document.add(table);
	}

	/**
	 * Create new page for each teacher user and add login information along
	 * with list of 5 students (alphabetically)
	 * 
	 * @param document
	 * @param prop
	 * @param tech
	 * @param schoolName
	 * @param schoolUserPresent
	 * @param newPagerRequired
	 * @param studentRequired
	 * @throws DocumentException
	 */
	private void addTeacherPageTasc(Document document, Properties prop, OrgTO tech, String schoolName, boolean schoolUserPresent, boolean newPagerRequired,
			boolean studentRequired, Map<String, String> orgLabelMap) throws DocumentException {
		if (newPagerRequired) {
			document.newPage();
		}
		String userType = "";
		String labelOneText = orgLabelMap.get(ORG_NODE_LEVEL_STATE);
		String labelTwoText = orgLabelMap.get(ORG_NODE_LEVEL_COUNTY);
		String labelThreeText = orgLabelMap.get(ORG_NODE_LEVEL_SITE);

		String stateName = tech.getStateName();
		if (stateName == null)
			stateName = "";
		String testingSiteName = tech.getTestingSiteName();
		if (testingSiteName == null)
			testingSiteName = "";
		String countyName = tech.getCountyName();
		if (countyName == null)
			countyName = "";

		if (!schoolUserPresent) {
			userType = labelThreeText;
			document.add(new Paragraph("\n" + userType + ": " + schoolName, font));
		}
		if (ORG_NODE_LEVEL_EDU.equals(tech.getOrgNodeLevel())) {
			userType = "Education Center";
			document.add(new Paragraph(userType + " Name: " + tech.getElementName(), font));
		} else {
			userType = labelOneText;
			document.add(new Paragraph(userType + " Name: " + stateName, font));
		}
		if (ORG_NODE_LEVEL_COUNTY.equals(tech.getOrgNodeLevel())) {
			userType = labelTwoText;
			document.add(new Paragraph(userType + " Name: " + countyName, font));
			// document.add(new Paragraph(userType + " Code: " +
			// tech.getCountyCode(), font));
		} else if (ORG_NODE_LEVEL_SITE.equals(tech.getOrgNodeLevel())) {
			userType = labelThreeText;
			document.add(new Paragraph(labelTwoText + " Name: " + countyName, font));
			// document.add(new Paragraph(labelTwoText + " Code: " +
			// tech.getCountyCode(), font));
			document.add(new Paragraph(userType + " Name: " + testingSiteName, font));
			// document.add(new Paragraph(userType + " Code: " +
			// tech.getTestingSiteCode(), font));
		}

		String roleString = getUserListAsStringTasc(tech.getRoleList(), userType);

		document.add(new Paragraph("User Roles: " + roleString, font));
		document.add(new Paragraph(" ", fontCourier));
		document.add(new Paragraph("User Name: " + tech.getUserName(), fontCourier));
		document.add(new Paragraph("User Password: " + tech.getPassword(), fontCourier));

		document.add(new Paragraph("\n" + prop.getProperty("TECH_TEXT"), font));

		document.add(new Paragraph("\nInstructions to Log-in:", font));
		List list = new List(false, 20);
		ListItem listItem = new ListItem(prop.getProperty("URL"), font);
		list.add(listItem);

		listItem = new ListItem(prop.getProperty("BLT1"), font);
		list.add(listItem);

		listItem = new ListItem(prop.getProperty("BLT2"), font);
		list.add(listItem);

		listItem = new ListItem(prop.getProperty("BLT3"), font);
		list.add(listItem);

		listItem = new ListItem(prop.getProperty("BLT3A"), font);
		list.add(listItem);

		document.add(list);

		if (studentRequired) {
			Table t = new Table(1, 6);
			// t.setBorderColor(new Color(220, 255, 100));
			t.setPadding(1.0F);
			float[] studentTableWidth = { 10f };
			t.setWidths(studentTableWidth);
			t.setAlignment(Element.ALIGN_LEFT);
			// t.setSpacing(5.0F);
			t.setBorderWidth(0.5F);

			java.util.List<UserTO> students = tech.getUsers();
			Cell c1 = new Cell(new Phrase("First " + students.size() + " students in the class (alphabetical order)", font));
			t.addCell(c1);
			if (students != null) {
				for (UserTO student : students) {
					c1 = new Cell(new Phrase(student.getFullName(), tableFont));
					t.addCell(c1);
				}
			}
			document.add(t);
		}

		document.add(new Paragraph(prop.getProperty("HELP"), font));
	}

}
