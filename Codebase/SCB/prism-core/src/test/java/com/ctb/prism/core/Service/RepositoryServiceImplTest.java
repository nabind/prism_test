package com.ctb.prism.core.Service;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctb.prism.core.transferobject.ObjectValueTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class RepositoryServiceImplTest  extends AbstractJUnit4SpringContextTests  {

	private String bucket = null;
	@Autowired
	private IRepositoryService repositorySerivice;
	
	@Before
	public void setUp() throws Exception {
		bucket = "prismdev";
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetS3Client() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetBucket() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAssetList() {
		String path= "/MAPREPORTS/MISSOURI/GRF";
	    List<String> assestList = repositorySerivice.getAssetList(path);
		assertNotNull(assestList);
	}

	@Test
	public void testGetAssetListWithPath() {
		String path= "/MAPREPORTS/MISSOURI/GRF";
	    List<ObjectValueTO> assestList = repositorySerivice.getAssetListWithPath(path);
		assertNotNull(assestList);
	}

	@Test
	public void testGetAssetBytes() throws IOException {
		String assetPath= "/MAPREPORTS/MISSOURI/GRF";
		byte[] bytedata = repositorySerivice.getAssetBytes(assetPath);
		assertNotNull(bytedata);
	}

	@Test
	public void testGetAssetBytesByS3Key() throws Exception {
		String assetPath= "/DEV/MAPREPORTS/MISSOURI/GRF";
		byte[] bytedata = repositorySerivice.getAssetBytesByS3Key(assetPath);
		assertNotNull(bytedata);
	}

	@Test
	public void testUploadAssetStringFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testUploadMapAsset() {
		fail("Not yet implemented");
	}

	@Test
	public void testUploadAssetStringInputStream() {
		fail("Not yet implemented");
	}

	@Test
	public void testUploadAssetByS3Key() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveAsset() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAssetInputStream() {
		fail("Not yet implemented");
	}

	@Test
	public void testUploadAssetAsync() {
		fail("Not yet implemented");
	}

}
