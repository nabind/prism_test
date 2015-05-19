package com.ctb.prism.core.Service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
		path= null;
		assestList = repositorySerivice.getAssetList(path);
		assertNull(assestList);
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
		String key = "/MAPREPORTS/MISSOURI/GRF";
		File file = new File("test");
		repositorySerivice.uploadAsset(key, file);
		assertNotNull("");
	}

	@Test
	public void testUploadMapAsset() {
		String key = "/MAPREPORTS/MISSOURI/GRF";
		File file = new File("test.txt");
		repositorySerivice.uploadMapAsset(key, file);
		assertNotNull("");
	}

	@Test
	public void testUploadAssetStringInputStream() throws FileNotFoundException {
		String key = "/MAPREPORTS/MISSOURI/GRF";
		InputStream is = new FileInputStream("test.txt");	
		repositorySerivice.uploadAsset(key, is);
		assertNotNull("");
	}

	@Test
	public void testUploadAssetByS3Key() throws Exception {
		String fullyQualifiedS3Key = "/MAPREPORTS/MISSOURI/GRF"; 
		File file = new File("test.txt");
		repositorySerivice.uploadAssetByS3Key(fullyQualifiedS3Key, file);
		assertNotNull("");
	}

	@Test
	public void testRemoveAsset() {
		String key = "/MAPREPORTS/MISSOURI/GRF";
		repositorySerivice.removeAsset(key);
		assertNotNull("");
	}

	@Test
	public void testGetAssetInputStream() throws IOException {
		String assetPath = "/MAPREPORTS/MISSOURI/GRF";
		InputStream is =  repositorySerivice.getAssetInputStream(assetPath);
		assertNotNull(is);
	}

	@Test
	public void testUploadAssetAsync() {
		String key = "/MAPREPORTS/MISSOURI/GRF";
		File file = new File("test.txt");
		repositorySerivice.uploadAssetAsync(key, file);
		assertNotNull("");
	}

}
