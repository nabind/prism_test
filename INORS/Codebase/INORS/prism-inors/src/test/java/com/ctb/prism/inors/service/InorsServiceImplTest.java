package com.ctb.prism.inors.service;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.util.InorsDownloadUtil;
import com.ctb.prism.test.InorsTestHelper;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class InorsServiceImplTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private IInorsService inorsService;

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
	public void testAsyncPDFDownload() {
		String jobId = "0";
		String contractName = testParams.getContractName();
		inorsService.asyncPDFDownload(jobId, contractName);
	}

	@Test
	public void testCreateJob() {
		BulkDownloadTO outputTO = inorsService.createJob(InorsTestHelper.getBulkDownloadTO(testParams));
		assertNotNull(outputTO);
	}

	@Test
	public void testGetJob() {
		String jobId = "0";
		String contractName = testParams.getContractName();
		JobTrackingTO outputTO = inorsService.getJob(jobId, contractName);
		assertNotNull(outputTO);
	}

	@Test
	public void testGetDownloadData() {
		Map<String, String> paramMap = InorsTestHelper.helpTestGetDownloadData(testParams);
		List<? extends BaseTO> downloadData = inorsService.getDownloadData(paramMap);
		assertNotNull(downloadData);
	}

	@Test
	public void testPopulateDistrictGrt() {
		Map<String, String> paramMap = InorsTestHelper.helpTestPopulateDistrictGrt(testParams);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> districtList = inorsService.populateDistrictGrt(paramMap);
		assertNotNull(districtList);
	}

	@Test
	public void testPopulateSchoolGrt() {
		Map<String, String> paramMap = InorsTestHelper.helpTestPopulateSchoolGrt(testParams);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> schoolList = inorsService.populateSchoolGrt(paramMap);
		assertNotNull(schoolList);
	}

	@Test
	public void testGetProductNameById() {
		String productName = inorsService.getProductNameById(0L);
		assertNotNull(productName);
	}

	@Test
	public void testGetTabulerData() {
		Map<String, String> paramMap = InorsTestHelper.helpTestGetTabulerData(testParams);
		String headers = testParams.getTabulerDataHeaders();
		String aliases = testParams.getTabulerDataAliases();
		ArrayList<String> headerList = InorsDownloadUtil.getRowDataLayout(headers);
		ArrayList<String> aliasList = InorsDownloadUtil.getRowDataLayout(aliases);
		ArrayList<ArrayList<String>> tabulerData = inorsService.getTabulerData(paramMap, aliasList, headerList);
		assertNotNull(tabulerData);
	}

	@Test
	public void testGetCurrentAdminYear() {
		String currentAdminYear = inorsService.getCurrentAdminYear();
		assertNotNull(currentAdminYear);
	}

}
