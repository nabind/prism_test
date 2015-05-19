package com.ctb.prism.core.Service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.amazonaws.services.simpledb.model.Item;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class SimpleDBServiceImplTest extends AbstractJUnit4SpringContextTests  {
	
	@Autowired
	ISimpleDBService simpleDB; 
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetSdb() {
		fail("Not yet implemented");
	}
	
	
	@Test
	public void testAddItem() {
		String contract = "tasc";
		String item = "test";
		simpleDB.addItem(contract, item);
		assertNotNull("");
	}
	
	
	@Test
	public void testGetAllItems() {
		String contract = "tasc";
		List<Item>  items = simpleDB.getAllItems(contract);
		assertNotNull(items);
	}

	
	@Test
	public void testGetItems() {
		String query = "select * from testDomain";
		List<Item> items = simpleDB.getItems(query);
		assertNotNull(items);
	}

	@Test
	public void testDeleteItem() {
		String contract= "tasc";
		String key = "DES11QSA$$2";
		boolean result = simpleDB.deleteItem(contract, key);
		assertTrue(result);
	}

}
