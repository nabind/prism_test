package com.ctb.prism.inors.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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

	@Test
	public void testCreateJob() {
		BulkDownloadTO inputTO = InorsTestHelper.getBulkDownloadTO(testParams);
		BulkDownloadTO outputTO = inorsDao.createJob(inputTO);
		assertNotNull(outputTO);
	}

	@Test
	public void testUpdateStatus() {
		BulkDownloadTO inputTO = InorsTestHelper.getBulkDownloadTO(testParams);
		BulkDownloadTO outputTO = inorsDao.updateStatus(inputTO);
		assertNotNull(outputTO);
	}

	@Test
	public void testUpdateJobLog() {
		BulkDownloadTO inputTO = InorsTestHelper.getBulkDownloadTO(testParams);
		BulkDownloadTO outputTO = inorsDao.updateJobLog(inputTO);
		assertNotNull(outputTO);
	}

	@Test
	public void testUpdateJob() {
		JobTrackingTO inputTO = new JobTrackingTO();
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
		String jobId = "0";
		String contractName = testParams.getContractName();
		JobTrackingTO outputTO = inorsDao.getJob(jobId, contractName);
		assertNotNull(outputTO);
	}

	@Test
	public void testGetSchoolClass() {
		BulkDownloadTO outputTO = inorsDao.getSchoolClass("0");
		assertNotNull(outputTO);
	}

	@Test
	public void testGetNodeName() {
		BulkDownloadTO outputTO = inorsDao.getNodeName("0");
		assertNotNull(outputTO);
	}

	@Test
	public void testGetDownloadData() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTabulerData() {
		fail("Not yet implemented");
	}

	@Test
	public void testPopulateDistrictGrt() {
		fail("Not yet implemented");
	}

	@Test
	public void testPopulateSchoolGrt() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetProductNameById() {
		String productName = inorsDao.getProductNameById(0L);
		assertNotNull(productName);
	}

	@Test
	public void testGetCurrentAdminYear() {
		String currentAdminYear = inorsDao.getCurrentAdminYear();
		assertNotNull(currentAdminYear);
	}

}
