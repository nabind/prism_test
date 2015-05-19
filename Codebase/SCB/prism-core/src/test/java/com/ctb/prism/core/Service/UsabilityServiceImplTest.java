package com.ctb.prism.core.Service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctb.prism.core.transferobject.UsabilityTO;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;
import com.ctb.prism.test.UsabilityTestHelper;
import com.ctb.prism.webservice.transferobject.CustHierarchyDetailsTO;
import com.ctb.prism.webservice.transferobject.RosterDetailsTO;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;
import com.ctb.prism.webservice.transferobject.StudentListTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class UsabilityServiceImplTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private IUsabilityService usabilityService;
	
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
	public void testSaveUsabilityData() throws Exception {
		UsabilityTO usability = UsabilityTestHelper.helpSaveUsabilityData(testParams);
		assertNotNull(usabilityService.saveUsabilityData(usability));		
	}

	@Test
	public void testUpdateStagingData() throws Exception {
		StudentListTO studentListTO = new StudentListTO();
		
		RosterDetailsTO rosterDetailsTO = new RosterDetailsTO();
		rosterDetailsTO.setRosterId("2086420082");		
		List<RosterDetailsTO> rosterDetailsTOList = new ArrayList<RosterDetailsTO>();
		
		CustHierarchyDetailsTO custHierarchyDetailsTO = new CustHierarchyDetailsTO();
		custHierarchyDetailsTO.setMaxHierarchy("3");
		rosterDetailsTO.setCustHierarchyDetailsTO(custHierarchyDetailsTO);
		
		studentListTO.setRosterDetailsTO(rosterDetailsTOList);

		StudentDataLoadTO studentDataLoadTO = new StudentDataLoadTO();
		studentDataLoadTO.setProcessId(24);
		studentDataLoadTO.setPartitionName("abc");
		assertNotNull(usabilityService.updateStagingData(studentListTO, studentDataLoadTO));
		
	}

	@Test
	public void testUpdatePartition() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckPartition() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateProces() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertIntoJobTracking() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetProces() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetStudentListTO() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateWSRosterData() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSetCache() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveFromCache() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFileSize() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateFileSize() {
		fail("Not yet implemented");
	}

	@Test
	public void testStoreERWSObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testStoreOASWSObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testStoreWSResponse() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateStudentXMLExtract() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetClobXMLFile() {
		fail("Not yet implemented");
	}

}
