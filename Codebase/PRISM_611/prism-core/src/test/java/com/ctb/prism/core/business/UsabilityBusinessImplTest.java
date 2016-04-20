/**
 * 
 */
package com.ctb.prism.core.business;

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

import com.ctb.prism.core.transferobject.MUsabilityTO;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;
import com.ctb.prism.test.UsabilityTestHelper;
import com.ctb.prism.webservice.transferobject.CustHierarchyDetailsTO;
import com.ctb.prism.webservice.transferobject.RosterDetailsTO;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;
import com.ctb.prism.webservice.transferobject.StudentListTO;



/**
 * @author 541841
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class UsabilityBusinessImplTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private IUsabilityBusiness usabilityBusiness;
	
	TestParams testParams;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testParams = TestUtil.getTestParams();
		TestUtil.byPassLogin(testParams);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.ctb.prism.core.business.UsabilityBusinessImpl#saveUsabilityData(com.ctb.prism.core.transferobject.UsabilityTO)}.
	 * @throws Exception 
	 */
	@Test
	public final void testSaveUsabilityData() throws Exception {
		MUsabilityTO usability = UsabilityTestHelper.helpSaveUsabilityData(testParams);
		assertNotNull(usabilityBusiness.saveUsabilityData(usability));		
	}

	/**
	 * Test method for {@link com.ctb.prism.core.business.UsabilityBusinessImpl#updateStagingData(com.ctb.prism.webservice.transferobject.StudentListTO, com.ctb.prism.webservice.transferobject.StudentDataLoadTO)}.
	 * @throws Exception 
	 */
	@Test
	public final void testUpdateStagingData() throws Exception {
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
		assertNotNull(usabilityBusiness.updateStagingData(studentListTO, studentDataLoadTO));
	}

	/**
	 * Test method for {@link com.ctb.prism.core.business.UsabilityBusinessImpl#updatePartition(java.lang.String)}.
	 */
	@Test
	public final void testUpdatePartition() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.ctb.prism.core.business.UsabilityBusinessImpl#checkPartition()}.
	 */
	@Test
	public final void testCheckPartition() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.ctb.prism.core.business.UsabilityBusinessImpl#createProces(com.ctb.prism.webservice.transferobject.StudentListTO, com.ctb.prism.webservice.transferobject.StudentDataLoadTO)}.
	 */
	@Test
	public final void testCreateProces() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.ctb.prism.core.business.UsabilityBusinessImpl#insertIntoJobTracking(com.ctb.prism.core.transferobject.JobTrackingTO)}.
	 */
	@Test
	public final void testInsertIntoJobTracking() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.ctb.prism.core.business.UsabilityBusinessImpl#getProces(com.ctb.prism.core.transferobject.ProcessTO)}.
	 */
	@Test
	public final void testGetProces() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.ctb.prism.core.business.UsabilityBusinessImpl#getStudentListTO()}.
	 */
	@Test
	public final void testGetStudentListTO() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.ctb.prism.core.business.UsabilityBusinessImpl#updateWSRosterData(com.ctb.prism.webservice.transferobject.StudentDataLoadTO, long)}.
	 */
	@Test
	public final void testUpdateWSRosterData() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.ctb.prism.core.business.UsabilityBusinessImpl#getFileSize(java.lang.String)}.
	 */
	@Test
	public final void testGetFileSize() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.ctb.prism.core.business.UsabilityBusinessImpl#updateFileSize(com.ctb.prism.core.transferobject.JobTrackingTO)}.
	 */
	@Test
	public final void testUpdateFileSize() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.ctb.prism.core.business.UsabilityBusinessImpl#generateStudentXMLExtract(java.util.Map)}.
	 */
	@Test
	public final void testGenerateStudentXMLExtract() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.ctb.prism.core.business.UsabilityBusinessImpl#getClobXMLFile(java.util.Map)}.
	 */
	@Test
	public final void testGetClobXMLFile() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.ctb.prism.core.business.UsabilityBusinessImpl#createRoster(com.ctb.prism.webservice.transferobject.StudentListTO)}.
	 */
	@Test
	public final void testCreateRoster() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.ctb.prism.core.business.UsabilityBusinessImpl#removeRoster(com.ctb.prism.webservice.transferobject.StudentListTO)}.
	 */
	@Test
	public final void testRemoveRoster() {
		fail("Not yet implemented"); // TODO
	}

}
