package com.ctb.prism.inors.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.test.InorsTestHelper;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
@TransactionConfiguration(transactionManager = "txManager")
public class InorsDAOImplTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private InorsDAOImpl inorsDao;

	TestParams testParams;

	@Before
	public void setUp() throws Exception {
		testParams = TestUtil.getTestParams();
		TestUtil.byPassLogin(testParams);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=org.springframework.jdbc.BadSqlGrammarException.class)
	public void testCreateJob() {
		BulkDownloadTO inputTO = InorsTestHelper.getBulkDownloadTO(testParams);
		BulkDownloadTO outputTO = inorsDao.createJob(inputTO);
		assertNotNull(outputTO.getJobId());		
	}

	@Test(expected=org.springframework.jdbc.BadSqlGrammarException.class)
	public void testUpdateStatus() {
		BulkDownloadTO inputTO = InorsTestHelper.getBulkDownloadTO(testParams);
		BulkDownloadTO outputTO = inorsDao.updateStatus(inputTO);
		assertTrue(outputTO.isDbStatus());
	}

	@Test(expected=org.springframework.jdbc.BadSqlGrammarException.class)
	public void testUpdateJobLog() {
		BulkDownloadTO inputTO = InorsTestHelper.getBulkDownloadTO(testParams);
		BulkDownloadTO outputTO = inorsDao.updateJobLog(inputTO);
		assertTrue(outputTO.isDbStatus());
	}

	@Test
	public void testUpdateJob() {
		JobTrackingTO inputTO = InorsTestHelper.getJobTrackingTO(testParams);
		JobTrackingTO outputTO = inorsDao.updateJob(inputTO);
		assertNotNull(outputTO);
	}

	@Test
	public void testUpdateJobStatusAndLog() {
		JobTrackingTO inputTO = new JobTrackingTO();
		JobTrackingTO outputTO = inorsDao.updateJobStatusAndLog(inputTO);
		assertNotNull(outputTO);
	}

	@Test
	public void testGetJob() {
		String jobId = "3096836";
		String contractName = testParams.getContractName();
		JobTrackingTO outputTO = inorsDao.getJob(jobId, contractName);
		assertNull(outputTO.getJobId());
		
	}

	@Test
	public void testGetSchoolClass() {
		BulkDownloadTO outputTO = inorsDao.getSchoolClass("7356");
		assertNull(outputTO.getSchool());
	}

	@Test
	public void testGetNodeName() {
		BulkDownloadTO outputTO = inorsDao.getNodeName("10195");
		assertNotNull(outputTO.getOrgClass());
	}

	@Test
	public void testGetDownloadData() {
		Map<String, String> paramMap = InorsTestHelper.helpTestGetDownloadData(testParams);
		assertNotNull(inorsDao.getDownloadData(paramMap));
	}

	@Test
	public void testGetTabulerData() {
		Map<String, String> paramMap = InorsTestHelper.helpTestGetDownloadData(testParams);
		ArrayList<String> aliasList = new ArrayList<String>();
		ArrayList<String> headerList = new ArrayList<String>();
		ArrayList<ArrayList<String>> data = inorsDao.getTabulerData(paramMap, aliasList, headerList);
		assertNotNull(data);
	}

	@Test
	public void testPopulateDistrictGrt() {
		Map<String, String> paramMap = InorsTestHelper.helpPopulateGrt(testParams);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> districtList =  inorsDao.populateDistrictGrt(paramMap);
		assertNotNull(districtList);
	}

	@Test
	public void testPopulateSchoolGrt() {
		Map<String, String> paramMap = InorsTestHelper.helpPopulateGrt(testParams);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> districtList =  inorsDao.populateSchoolGrt(paramMap);
		assertNotNull(districtList);		
	}

	@Test
	public void testGetProductNameById() {
		String productName = inorsDao.getProductNameById(2001L);
		assertNull(productName);
	}

	@Test
	public void testGetCurrentAdminYear() {
		String currentAdminYear = inorsDao.getCurrentAdminYear();
		assertNotNull(currentAdminYear);
	}

}
