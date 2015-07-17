package com.ctb.prism.inors.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.test.InorsTestHelper;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;
import com.lowagie.text.DocumentException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class PdfGeneratorTest extends AbstractJUnit4SpringContextTests  {
	
	
	@Autowired
	IPropertyLookup propertyLookup; 
	
	TestParams testParams;
	
	
	@Before
	public void setUp() throws Exception {
		testParams = TestUtil.getTestParams();
		TestUtil.byPassLogin(testParams);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenerateQuerysheet() {
		BulkDownloadTO bulkDownloadTO = InorsTestHelper.getBulkDownloadTO(testParams);
		String docLoc = PdfGenerator.generateQuerysheet(bulkDownloadTO, propertyLookup);
		assertNotNull(docLoc);
	}

	@Test
	public void testGenerateQuerysheetForCR() {
		BulkDownloadTO bulkDownloadTO = InorsTestHelper.getBulkDownloadTO(testParams);
		String docLoc = PdfGenerator.generateQuerysheetForCR(bulkDownloadTO, propertyLookup);
		assertNotNull(docLoc);
	}

	@SuppressWarnings("static-access")
	@Test
	public void testSplitPDF() throws FileNotFoundException {
		InputStream inputStream = new FileInputStream("Candidate_Report_10.pdf");
        OutputStream outputStream = new FileOutputStream("Candidate_Report_10.pdf");
        int fromPage = 1;
        int toPage  = 2;
        PdfGenerator.splitPDF(inputStream, outputStream, fromPage, toPage);
		assertNotNull("");
	}

	@Test
	public void testConcatPDFs() throws FileNotFoundException {
		List<String> listOfPDFFiles = new ArrayList<String>();
		listOfPDFFiles.add("Candidate_Report_10.pdf");
		listOfPDFFiles.add("Candidate_Report_11.pdf");
        OutputStream outputStream = new FileOutputStream("Candidate_Report_10.pdf");
        boolean paginate = true;
        PdfGenerator.concatPDFs(listOfPDFFiles, outputStream, paginate);
        assertNotNull("");
        
	}

	@Test
	public void testZipit() throws Exception {
		List<String> files = new ArrayList<String>();
		files.add("Candidate_Report_10.pdf");
		List<String> arcFiles = new ArrayList<String>();
		files.add("Candidate_Report_10.pdf");
		String zipName = "test.zip";
		PdfGenerator.zipit(files, arcFiles, zipName);
		assertNotNull("");
	}

	@Test
	public void testRotatePdf() throws IOException, DocumentException {
		String pdfFileSrc = "Candidate_Report_10.pdf";
		PdfGenerator.rotatePdf(pdfFileSrc);
		assertNotNull("");
	}
	
	@Ignore
	@Test
	public void testMain() {
		fail("Not yet implemented");
	}

}
