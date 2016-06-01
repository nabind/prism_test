package com.ctb.prism.core.Service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.core.transferobject.ProcessTO;
import com.ctb.prism.core.transferobject.UsabilityTO;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;
import com.ctb.prism.test.UsabilityTestHelper;
import com.ctb.prism.webservice.erTransferobject.StudentList;
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
		UsabilityTO usability = UsabilityTestHelper.helpSaveUsabilityData();
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
	public void testUpdatePartition() throws Exception {
		usabilityService.updatePartition("");
		assertNotNull("Return type is void");
	}

	@Test
	public void testCheckPartition() throws Exception {
		try{
			usabilityService.checkPartition();
		}catch(Exception e){}
	}

	@Test
	public void testCreateProces() throws Exception {
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
		
		try{
			usabilityService.createProces(studentListTO, studentDataLoadTO);
		}catch(Exception e){}
	}

	@Test
	public void testInsertIntoJobTracking() throws Exception {
		JobTrackingTO jobTrackingTO = new JobTrackingTO();
		try{
			usabilityService.insertIntoJobTracking(jobTrackingTO);
		}catch(Exception e){}
	}

	@Test
	public void testGetProces() throws Exception {
		ProcessTO processTO = new ProcessTO();
		try{
			usabilityService.getProces(processTO);
		}catch(Exception e){}
	}

	@Test
	public void testGetStudentListTO() {
		try{
			usabilityService.getStudentListTO();
		}catch(Exception e){}
	}

	@Test
	public void testUpdateWSRosterData() {
		StudentDataLoadTO studentDataLoadTO = new StudentDataLoadTO();
		studentDataLoadTO.setProcessId(24);
		studentDataLoadTO.setPartitionName("abc"); 
		long wsRosterDataId = 0;
		try{
			usabilityService.updateWSRosterData(studentDataLoadTO, wsRosterDataId);
		}catch(Exception e){}
	}

	@Test
	public void testGetSetCache() {
		String username = ""; 
		String sessionName = "";
		Object sessionParam = new Object();
		assertNotNull(usabilityService.getSetCache(username, sessionName, sessionParam));
	}

	@Test
	public void testRemoveFromCache() {
		String username = "";
		String sessionName = "";
		assertNull(usabilityService.removeFromCache(username, sessionName));
	}

	@Test
	public void testGetFileSize() {
		assertNotNull(usabilityService.getFileSize("1"));
	}

	@Test
	public void testUpdateFileSize() {
		JobTrackingTO jobTrackingTO = new JobTrackingTO();
		assertNotNull(usabilityService.updateFileSize(jobTrackingTO));
	}

	@Test
	public void testStoreERWSObject() {
		StudentList studentList = new StudentList();
		long processId = 0;
		boolean requestObj = false;
		String source = "Test";
		usabilityService.storeERWSObject(studentList, processId, requestObj, source);
	}

	@Test
	public void testStoreOASWSObject() {
		StudentListTO studentListTO = new StudentListTO();
		RosterDetailsTO rosterDetailsTO = new RosterDetailsTO();
		rosterDetailsTO.setRosterId("2086420082");		
		List<RosterDetailsTO> rosterDetailsTOList = new ArrayList<RosterDetailsTO>();
		
		CustHierarchyDetailsTO custHierarchyDetailsTO = new CustHierarchyDetailsTO();
		custHierarchyDetailsTO.setMaxHierarchy("3");
		rosterDetailsTO.setCustHierarchyDetailsTO(custHierarchyDetailsTO);
		
		studentListTO.setRosterDetailsTO(rosterDetailsTOList);
		long processId = 0;
		boolean requestObj = false;
		String source = "Test";
		usabilityService.storeOASWSObject(studentListTO, processId, requestObj, source);
	}

	@Test
	public void testStoreWSResponse() {
		StudentDataLoadTO studentDataLoadTO = new StudentDataLoadTO();
		studentDataLoadTO.setProcessId(24);
		studentDataLoadTO.setPartitionName("abc");
		long processId = 0;
		boolean requestObj = false;
		String source = "Test";
		usabilityService.storeWSResponse(studentDataLoadTO, processId, requestObj, source);
	}

	@Test
	public void testGenerateStudentXMLExtract() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try{
			usabilityService.generateStudentXMLExtract(paramMap);
		}catch(Exception e){}
	}

	@Test
	public void testGetClobXMLFile() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try{
			usabilityService.getClobXMLFile(paramMap);
		}catch(Exception e){}
	}
	
	@Test
	public void testCreateRoster() throws Exception {
		StudentListTO studentListTO = new StudentListTO();
		RosterDetailsTO rosterDetailsTO = new RosterDetailsTO();
		rosterDetailsTO.setRosterId("2086420082");		
		List<RosterDetailsTO> rosterDetailsTOList = new ArrayList<RosterDetailsTO>();
		
		CustHierarchyDetailsTO custHierarchyDetailsTO = new CustHierarchyDetailsTO();
		custHierarchyDetailsTO.setMaxHierarchy("3");
		rosterDetailsTO.setCustHierarchyDetailsTO(custHierarchyDetailsTO);
		
		studentListTO.setRosterDetailsTO(rosterDetailsTOList);
		assertNotNull(usabilityService.createRoster(studentListTO));
	}
	
	@Test
	public void testRemoveRoster() throws Exception {
		StudentListTO studentListTO = new StudentListTO();
		RosterDetailsTO rosterDetailsTO = new RosterDetailsTO();
		rosterDetailsTO.setRosterId("2086420082");		
		List<RosterDetailsTO> rosterDetailsTOList = new ArrayList<RosterDetailsTO>();
		
		CustHierarchyDetailsTO custHierarchyDetailsTO = new CustHierarchyDetailsTO();
		custHierarchyDetailsTO.setMaxHierarchy("3");
		rosterDetailsTO.setCustHierarchyDetailsTO(custHierarchyDetailsTO);
		
		studentListTO.setRosterDetailsTO(rosterDetailsTOList);
		usabilityService.removeRoster(studentListTO);
	}

}
