/**
 * 
 */
package com.ctb.prism.core.Service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;
import com.ctb.prism.webservice.erTransferobject.StudentList;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;

/**
 * @author 541841
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class ERServiceImplTest {
	
	@Autowired
	private IERService eRService;

	TestParams testParams;
	
	@Before
	public void setUp() throws Exception {
		testParams = TestUtil.getTestParams();
		TestUtil.byPassLogin(testParams);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.ctb.prism.core.Service.ERServiceImpl#updateStagingData(com.ctb.prism.webservice.erTransferobject.StudentList, com.ctb.prism.webservice.transferobject.StudentDataLoadTO)}.
	 * @throws Exception 
	 */
	@Test
	public void testUpdateStagingData() throws Exception {
		StudentList studentList = new StudentList();
		StudentDataLoadTO studentDataLoadTO = new StudentDataLoadTO();
		studentDataLoadTO.setProcessId(24);
		studentDataLoadTO.setPartitionName("abc");
		assertNotNull(eRService.updateStagingData(studentList, studentDataLoadTO));
	}

	/**
	 * Test method for {@link com.ctb.prism.core.Service.ERServiceImpl#getStudent(com.ctb.prism.core.transferobject.JobTrackingTO)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetStudent() throws Exception {
		JobTrackingTO jobTrackingTO = new JobTrackingTO();
		assertNotNull(eRService.getStudent(jobTrackingTO));
	}

}
