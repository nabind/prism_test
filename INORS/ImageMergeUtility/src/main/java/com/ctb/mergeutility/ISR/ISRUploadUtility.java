package com.ctb.mergeutility.ISR;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.ctb.mergeutility.client.ImageMergeUtility;
import com.ctb.mergeutility.common.ApplicationConstants;
import com.ctb.mergeutility.common.UnZip;

public final class ISRUploadUtility {

	private static Properties applicationProperies = ApplicationConfig.loadApplicationConfig().getConfigProp();
	private static final Logger logger = Logger.getLogger(ISRUploadUtility.class);

	public ISRUploadUtility() {
		/* Default constructor */
	}

	/* Main method */
	public static void main(String[] args) {
		
		try {
			String folderLoc = applicationProperies.getProperty("isr.source.location");
			String lstFolderLoc = applicationProperies.getProperty("isr.lst.location");
			Path lstPath = FileSystems.getDefault().getPath(lstFolderLoc);
			File[] fileList = new File(folderLoc).listFiles();
			int size = 0;
			for(File file : fileList) {
				size++;
				if ( file.isFile() && (file.getName().endsWith(".ZIP") || file.getName().endsWith(".zip")) ) {
					File tempdir = createTempDirectory();
					
					
					String inzip = folderLoc + File.separator + file.getName();
					String outzip = tempdir + File.separator + file.getName();
					Path inPath = FileSystems.getDefault().getPath(inzip);
					Path outPath = FileSystems.getDefault().getPath(tempdir.getAbsolutePath());
					if(Files.move(inPath, outPath.resolve(inPath.getFileName()),  StandardCopyOption.ATOMIC_MOVE) != null) {
						logger.info("file has been moved to temp folder" + tempdir.getAbsolutePath());
						// unzip the file
						//UnZip unzip = new UnZip();
						//unzip.unZipIt(outzip, tempdir.toString());
						LargeUnzip.unzipFile(outzip, tempdir.toString());
						
						// upload the file 
						ArrayList<String> allFiles = new ArrayList<String>();
						for (final File fileEntry : tempdir.listFiles()) {
							if(fileEntry.isFile() && ( fileEntry.getName().endsWith(".PDF") || fileEntry.getName().endsWith(".pdf") ) ) {
								// get all PDF files
								allFiles.add(fileEntry.getAbsolutePath());
							} else if ( fileEntry.getName().endsWith(".LST") || fileEntry.getName().endsWith(".lst") ) {
								Path lstSource = FileSystems.getDefault().getPath(fileEntry.getAbsolutePath());
								if(Files.copy(lstSource, lstPath.resolve(lstSource.getFileName()),  StandardCopyOption.REPLACE_EXISTING) != null) {
									logger.info("List file copied to " + lstFolderLoc);
								} else {
									logger.error("x x x Unable to copy list file " + fileEntry.getAbsolutePath() + " " + fileEntry.getName());
								}
							}
						}
						// **************************** NEW  ***************************
						// put the merged PDF files to S3
						String s3Path = applicationProperies.getProperty("s3.location");
						
						if(!allFiles.isEmpty()) {
							long count = 1;
							long start = System.currentTimeMillis();
							ExecutorService executor = Executors.newFixedThreadPool(Integer.parseInt(applicationProperies.getProperty("s3.numberof.thread")));
							for(String fi : allFiles) {
								try {
									Runnable worker = new S3Uploader(s3Path, fi, count++, allFiles.size());
									executor.execute(worker);
								} catch (Exception ex) {
									logger.error("x x x ERROR uploading file to S3: " + fi + " " + ex.getMessage());
								}
							}
							executor.shutdown();
							while (!executor.isTerminated()) {
								// still upload is in proress
							}
							System.out.println("    Uploading file to s3 ------------------------------ 100% --- completed" );
							System.out.println("Finished all threads");
							long endS3 = System.currentTimeMillis();
							float executionTime = (endS3 - start) / 1000f;
							DecimalFormat df = new DecimalFormat("0.00");
							logger.info("--------> (" + size + "/" + fileList.length + ") Total Time taken to upload " + allFiles.size() + " files in S3 is " + df.format(executionTime));
						}
					} else {
						logger.info("file failed to moved to temp folder" + tempdir.getAbsolutePath());
					}
				}
			}
			
			
			// **************************** END NEW ***************************
			System.out.println("x --- Completed ---- x");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// end of main method
	
	private static File createTempDirectory() throws IOException {

		final File temp;
		File tempDir = new File(applicationProperies.getProperty("isr.work.location"));
		temp = java.io.File.createTempFile("ISRS3Upload", Long.toString(System.nanoTime()), tempDir);
		if (!(temp.delete())) {
			throw new IOException("Could not delete pdfsplit file: " + temp.getAbsolutePath());
		}
		if (!(temp.mkdir())) {
			throw new IOException("Could not create pdfsplit directory: " + temp.getAbsolutePath());
		}
		return (temp);
	}

}// end of class
