package com.ctb.prism.web.util;


import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class ManageStringwithTooltipTest extends AbstractJUnit4SpringContextTests {
	@Autowired
	 ManageStringwithTooltip  manageStringwithTooltip;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testManageString() {
		String test = ManageStringwithTooltip.manageString("abcdefghijklmnopqrstuvwxyz");
		assertNotNull(test);
	}

}
