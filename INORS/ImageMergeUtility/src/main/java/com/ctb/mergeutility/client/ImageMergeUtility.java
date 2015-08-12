package com.ctb.mergeutility.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.ctb.mergeutility.common.AppZip;
import com.ctb.mergeutility.common.ApplicationConfig;
import com.ctb.mergeutility.common.ApplicationConstants;
//import com.ctb.mergeutility.common.FileMove;
import com.ctb.mergeutility.common.GrtFileReader;
import com.ctb.mergeutility.common.S3Uploader;
import com.ctb.mergeutility.common.UnZip;
import com.ctb.mergeutility.merge.FileMerger;
import com.ctb.mergeutility.common.AWSStorageUtil;

public final class ImageMergeUtility {

	private static final Logger logger = Logger.getLogger(ImageMergeUtility.class);
	// private static final File File = null;
	private static Properties applicationProperies = ApplicationConfig.loadApplicationConfig().getConfigProp();
	private List<Map<String, String>> valueList;
	// public ArrayList<String> missingImages;
	private FileMerger merger;

	public ImageMergeUtility() {
		/* Default constructor */
	}

	public ImageMergeUtility(List<Map<String, String>> fileValueList) {
		this.valueList = fileValueList;
	}

	/* Main method */
	public static void main(String[] args) {
		//String[] args = { "C:\\temp\\img\\control", "C:\\temp\\img\\zip", "C:\\temp\\img\\bk", "one_copy" };
		/* Getting read path from the property file */
		// String readPath =
		// applicationProperies.getProperty(ApplicationConstants.READ_DIRECTORY).trim();
		String readPath = args[0];
		String zipDestination = args[1];
		String bkupDest = args[2];
		String final_file = args[3];

		File folder = new File(readPath);
		// String zipDestination =
		// applicationProperies.getProperty(ApplicationConstants.FINAL_DESINATION).trim();

		logger.info("read directory			:" + args[0]);
		logger.info("dest directory			:" + args[1]);
		logger.info("bkup directory 		:" + args[2]);
		logger.info("two_copies or one_copy	:" + args[3]);

		String inzip = null;
		String outzip = null;
		String pdfziparchivefile = null;
		ArrayList<String> missingImages = new ArrayList<String>();
		ArrayList<String> allFiles = new ArrayList<String>();
		List<String> imgErrLog = new ArrayList<String>();

		logger.info("Reading file from " + readPath);
		GrtFileReader grtFileReader = new GrtFileReader();
		System.out.println("here");
		File[] fileList1 = folder.listFiles();
		try {
			if (fileList1.length > 0) {
				File tempdir = createTempDirectory();
				File file = fileList1[0];
				String fileName = file.getName();
				inzip = readPath + "/" + fileName;
				outzip = tempdir + "/" + fileName;
				File inFile = new File(inzip);
				File outFile = new File(outzip);
				//if (inFile.renameTo(outFile)) {
				Path inPath = FileSystems.getDefault().getPath(inzip);
				Path outPath = FileSystems.getDefault().getPath(tempdir.getAbsolutePath());
				if(Files.move(inPath, outPath.resolve(inPath.getFileName()),  StandardCopyOption.ATOMIC_MOVE) != null) {
					logger.info("file has been moved to temp folder");
				} else {
					logger.info("file has not been moved to temp folder");
				}
				// move.movefile(inzip, outzip, "zip");
				UnZip unzip = new UnZip();
				String tempTP = null, corp = null;
				unzip.unZipIt(outzip, tempdir.toString());
				Vector<File> fileList2 = grtFileReader.findGrtFiles(tempdir.toString());

				for (File grtFile : fileList2) {
					List<Map<String, String>> fileValueList = grtFileReader.read(grtFile);
					long start = System.currentTimeMillis();
					ImageMergeUtility client = new ImageMergeUtility(fileValueList);
					for (Map<String, String> tempList : fileValueList) {
						tempTP = tempList.get(ApplicationConstants.ORGTP_NO);
						corp = tempList.get(ApplicationConstants.CORP_NO);
						if (!tempTP.isEmpty() && !corp.isEmpty()) {
							logger.info("org tp information:" + tempTP);
							break;
						}

					}
					if (client.processGrtFile(grtFile.getName(), tempdir, zipDestination, final_file, missingImages, allFiles)) {

						AppZip appZip = new AppZip();
						appZip.fileList = new ArrayList<String>();
						// appZip.buildTxtFile(tempdir, tempdir.toString(),
						// "PDF",fileName);

						appZip.generateFileList(tempdir, tempdir.toString(), zipDestination, tempTP, final_file, "PDF");
						pdfziparchivefile = "FILE_LIST_AND_SUMMARY_FOR_" + tempTP + ".ZIP";

						appZip.zipIt(tempdir.toString(), bkupDest + pdfziparchivefile);

						logger.info("File " + grtFile.getName() + " GRT File Processed sucessfully...");
					} else
						logger.info("File " + grtFile.getName() + " Problem in GRT File Processing..");

					// **************************** NEW  ***************************
					// put the merged PDF files to S3
					String s3Path = applicationProperies.getProperty("s3.location");
					//AWSStorageUtil aWSStorageUtil = AWSStorageUtil.getInstance();
					
					if(!allFiles.isEmpty()) {
						long count = 1;
						long startS3 = System.currentTimeMillis();
						ExecutorService executor = Executors.newFixedThreadPool(Integer.parseInt(applicationProperies.getProperty("s3.numberof.thread")));
						for(String fi : allFiles) {
							String[] fileToMove = fi.split(",");
							try {
								Runnable worker = new S3Uploader(s3Path, fileToMove[2], count++, allFiles.size());
								executor.execute(worker);
								
								//aWSStorageUtil.uploadObject(s3Path, fileToMove[2]);
							} catch (Exception ex) {
								System.out.println("ERROR uploading file to S3: " + fi + " " + ex.getMessage());
							}
						}
						executor.shutdown();
						while (!executor.isTerminated()) {
							// still upload is in proress
						}
						System.out.println("    Uploading file to s3 ------------------------------ 100% --- completed" );
						System.out.println("Finished all threads");
						long endS3 = System.currentTimeMillis();
						float executionTime = (endS3 - startS3) / 1000f;
						DecimalFormat df = new DecimalFormat("0.00");
						System.out.println("Total Time taken to upload " + allFiles.size() + " files in S3 is " + df.format(executionTime));
					}

					/*if (!s3Error.isEmpty()) {
						FileWriter writer = new FileWriter(tempdir + File.separator + "S3Error.csv");
						writer.append("ORG-TP");
						writer.append(',');
						writer.append("CORP");
						writer.append(',');
						writer.append("File");
						writer.append('\n');
						for (String s3Err : s3Error) {
							writer.append(s3Err);
							writer.append('\n');
						}
						writer.flush();
						writer.close();
					}*/
					// **************************** END NEW ***************************

					long end = System.currentTimeMillis();
					float executionTime = (end - start) / 1000f;
					DecimalFormat df = new DecimalFormat("0.00");
					System.out.println("Total Time taken to process files...." + grtFile.getName() + " is " + df.format(executionTime));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// end of else block
	}// end of main method

	public boolean processGrtFile(String grtFileName, File dest1, String finalDest, String zipflag, ArrayList<String> missingImages, ArrayList<String> allFiles) throws IOException {
		boolean flag = false;
		merger = new FileMerger();
		int count = 1;
		// ArrayList<String> missingImages = new ArrayList<String>();
		int ppAndOas, onlyPP, onlyOas, notPpNorOas;
		ppAndOas = onlyPP = onlyOas = notPpNorOas = 0;
		String pdfInputExtPP = applicationProperies.getProperty(ApplicationConstants.PDF_INPUT_EXT_PP).trim();
		String pdfInputExtOas = applicationProperies.getProperty(ApplicationConstants.PDF_INPUT_EXT_OAS).trim();
		String dest = dest1.toString();

		for (Map<String, String> valueMap : valueList) {
			long start = System.currentTimeMillis();
			int result = merger.findOperation(valueMap);
			
			String orgTp = valueMap.get(ApplicationConstants.ORGTP_NO);
			boolean success = false;
			switch (result) {
			case ApplicationConstants.ONLY_OAS_FILE_FOUND:
				onlyOas += 1;

				String corpId = valueMap.get(ApplicationConstants.CORP_NO);
				String fileName = valueMap.get(ApplicationConstants.OAS_IMAGE_ID) + "." + pdfInputExtOas;
				String src = applicationProperies.getProperty(ApplicationConstants.OAS_ROOT) + corpId + File.separator + fileName;
				success = merger.copyFile(src, dest, "");
				if (!success) {
					setMissingImages(valueMap, missingImages, "YES", "NO");
					System.out.println("missing case 1:" + success);
				} else {
					allFiles.add(orgTp + "," + corpId + "," +dest + File.separator + fileName);
				}
				break;
			case ApplicationConstants.ONLY_PP_FILE_FOUND:
				onlyPP += 1;
				corpId = valueMap.get(ApplicationConstants.CORP_NO);
				fileName = valueMap.get(ApplicationConstants.PP_IMAGE_ID) + "." + pdfInputExtPP;
				src = applicationProperies.getProperty(ApplicationConstants.PP_ROOT) + corpId + File.separator + fileName;
				success = merger.copyFile(src, dest, "");
				if (!success) {
					setMissingImages(valueMap, missingImages, "NO", "YES");
					System.out.println("missing case 2:" + success);
				} else {
					allFiles.add(orgTp + "," + corpId + "," +dest + File.separator + fileName);
				}
				break;
			case ApplicationConstants.PP_AND_OAS_FILE_FOUND:
				ppAndOas += 1;
				corpId = valueMap.get(ApplicationConstants.CORP_NO);
				fileName = valueMap.get(ApplicationConstants.PP_IMAGE_ID) + "." + pdfInputExtPP;
				int flagVal = merger.mergePaperPancilAndOas(valueMap, dest);
				if (flagVal == 0) {
					System.out.println("missing list:" + valueMap);
					setMissingImages(valueMap, missingImages, "YES", "YES");
					System.out.println("missing case 3:" + success);
				} else if (flagVal == 11) {
					setMissingImages(valueMap, missingImages, "NO", "YES");
				} else if (flagVal == 22) {
					setMissingImages(valueMap, missingImages, "YES", "NO");
				} else if (flagVal == 33) {
					setMissingImages(valueMap, missingImages, "YES", "YES");
				} else if (flagVal == 1) {
					success = true;
				}
				if(success) {
					allFiles.add(orgTp + "," + corpId + "," +dest + File.separator + fileName);
				}
				break;
			case ApplicationConstants.PP_AND_OAS_IDS_ARE_SAME:
				ppAndOas += 1;
				corpId = valueMap.get(ApplicationConstants.CORP_NO);
				fileName = valueMap.get(ApplicationConstants.PP_IMAGE_ID) + "." + pdfInputExtPP;
				//src = applicationProperies.getProperty(ApplicationConstants.PP_ROOT) + corpId + File.separator + fileName;
				src = applicationProperies.getProperty(ApplicationConstants.OAS_ROOT) + corpId + File.separator + fileName;
				success = merger.copyFile(src, dest, "");
				if (!success) {
					setMissingImages(valueMap, missingImages, "YES", "Same image id (PP and OAS)");
					System.out.println("missing case 4:" + success);
				} else {
					allFiles.add(orgTp + "," + corpId + "," +dest + File.separator + fileName);
				} 
				break;
			case ApplicationConstants.PP_AND_OAS_FILE_NOT_FOUND:
				notPpNorOas += 1;
				logger.info("Line no " + count + " is not having paper pencil Id as well as OAS ID");
				success = false;
				if (!success) {
					setMissingImages(valueMap, missingImages, "NO", "NO");
					System.out.println("missing case 5:" + success);
				} 
				break;
			default:
				break;
			}
			if (success) {
				logger.info("Record " + count++ + " processed sucessffully");
			} else
				logger.info("Processing Problem Record No " + count++);
			long end = System.currentTimeMillis();
			float executionTime = (end - start) / 1000f;
			DecimalFormat df = new DecimalFormat("0.00");
			logger.info("Time taken to merge/Copy .." + df.format(executionTime));
		}

		merger.createCsvReport(onlyPP, onlyOas, ppAndOas, notPpNorOas, grtFileName, dest, missingImages);
		buildTxtFile(dest1, "PDF", grtFileName);
		try {
			File file_ind = new File(dest);

			String status = "COMPLETED-";
			java.io.File.createTempFile(status.trim(), ".ind", file_ind);
		} catch (IOException e) {
			e.printStackTrace();
		}
		flag = true;
		return flag;
	}

	private static File createTempDirectory() throws IOException {

		final File temp;
		File tempDir = new File(applicationProperies.getProperty(ApplicationConstants.WORK_DIR));
		temp = java.io.File.createTempFile("imageMerge", Long.toString(System.nanoTime()), tempDir);
		if (!(temp.delete())) {
			throw new IOException("Could not delete pdfsplit file: " + temp.getAbsolutePath());
		}
		if (!(temp.mkdir())) {
			throw new IOException("Could not create pdfsplit directory: " + temp.getAbsolutePath());
		}
		return (temp);
	}

	/*
	 * public static void setzipout(String temp) { zipout = temp; }
	 */

	/**
	 * creating LST(text file with the all PDF file names init. This is for
	 * PRISM jobs and add the file into fileList
	 * 
	 * @param node
	 *            file or directory
	 */
	public void buildTxtFile(File node, String type, String zipFile) {

		try {
			File csvFile = new File(node + "/" + "LIST_FILE" + ".LST");
			FileWriter fw = new FileWriter(csvFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			File[] listOfFiles = node.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile() && listOfFiles[i].getName().substring(listOfFiles[i].getName().length() - 4).equalsIgnoreCase(".PDF")) {
					bw.append(listOfFiles[i].getName());
					bw.append(System.getProperty("line.separator"));
				} else {
					logger.info("non PDF files " + listOfFiles[i].getName());
				}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setMissingImages(Map<String, String> valueMap, ArrayList<String> missingImages, String OAS_Image, String PP_Image) throws IOException {
		// ArrayList missingImages = new ArrayList();

		System.out.println("value map values in set method" + valueMap.get(ApplicationConstants.ORGTP_NO));
		missingImages.add(valueMap.get(ApplicationConstants.ORGTP_NO).toString());

		missingImages.add(valueMap.get(ApplicationConstants.CORP_NO));
		missingImages.add(valueMap.get(ApplicationConstants.OAS_IMAGE_ID));
		missingImages.add(valueMap.get(ApplicationConstants.PP_IMAGE_ID));
		missingImages.add(OAS_Image);
		missingImages.add(PP_Image);

	}

	/*
	 * public ArrayList getMissingImages() throws IOException {
	 * System.out.println(missingImages); return missingImages; }
	 */
}// end of class
