package com.ctb.prism.report.service;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.report.transferobject.RescoreRequestTO;
import com.ctb.prism.test.ReportTestHelper;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class RescoreRequestServiceImplTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private IRescoreRequestService rescoreRequestService;

	TestParams testParams;

	@Before
	public void setUp() throws Exception {
		testParams = TestUtil.getTestParams();
		TestUtil.byPassLogin(testParams);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=com.ctb.prism.core.exception.BusinessException.class)
	public void testGetRescoreRequestForm() throws BusinessException {
		Map<String, Object> paramMap = ReportTestHelper.helpGetRescoreRequestForm(testParams);
		Map<String, Object> map = rescoreRequestService.getRescoreRequestForm(paramMap);
		assertNotNull(map);
	}

	@Test
	public void testSubmitRescoreRequest() throws BusinessException {
		Map<String, Object> paramMap = ReportTestHelper.helpSubmitRescoreRequest(testParams);
		com.ctb.prism.core.transferobject.ObjectValueTO to = rescoreRequestService.submitRescoreRequest(paramMap);
		assertNotNull(to);
	}

	@Test
	public void testResetItemState() throws BusinessException {
		Map<String, Object> paramMap = ReportTestHelper.helpResetItemState(testParams);
		com.ctb.prism.core.transferobject.ObjectValueTO to = rescoreRequestService.resetItemState(paramMap);
		assertNotNull(to);
	}

	@Test
	public void testResetItemDate() throws BusinessException {
		Map<String, Object> paramMap = ReportTestHelper.helpResetItemDate(testParams);
		com.ctb.prism.core.transferobject.ObjectValueTO to = rescoreRequestService.resetItemDate(paramMap);
		assertNotNull(to);
	}

	@Test(expected=java.lang.IndexOutOfBoundsException.class)
	public void testGetNotDnpStudentDetails() throws BusinessException {
		Map<String, Object> paramMap = ReportTestHelper.helpGetNotDnpStudentDetails(testParams);
		RescoreRequestTO to = rescoreRequestService.getNotDnpStudentDetails(paramMap);
		assertNotNull(to);
	}

}
