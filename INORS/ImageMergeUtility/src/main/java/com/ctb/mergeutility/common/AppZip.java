package com.ctb.mergeutility.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
//import com.ctb.mergeutility.common.FileMove;
 
public class AppZip
{
    public List<String> fileList;
    
    
    public AppZip(){
	fileList = new ArrayList<String>();
    }
 
    public static void main( String[] args )
    {
    	String SOURCE_FOLDER = args[0];
    	String OUTPUT_ZIP_FILE = args[1];
    	String type = args[2];
    	String final_file = args[3];
    	 if (args.length == 2) {
    		OUTPUT_ZIP_FILE = args[0];
    		SOURCE_FOLDER = args[1];
    		System.out.println("AppZip OUTPUT_ZIP_FILE ---> " + OUTPUT_ZIP_FILE);
    		System.out.println("AppZip SOURCE_FOLDER ---> " + OUTPUT_ZIP_FILE);
    		} else {
    			System.out.println("AppZip ---> No input and output");
    			System.exit(0);
    		}
    		
    	
    	AppZip appZip = new AppZip();
    	appZip.generateFileList(new File(SOURCE_FOLDER), SOURCE_FOLDER, OUTPUT_ZIP_FILE, "org-tp",final_file, type);
    	appZip.zipIt(SOURCE_FOLDER, OUTPUT_ZIP_FILE);
    }
 
    /**
     * Zip it
     * @param zipFile output ZIP file location
     */
    public void zipIt(String SOURCE_FOLDER, String zipFile){
 
    	// System.out.println("from zipIt\n");   
     byte[] buffer = new byte[1024];
 
     try{
    	 ////System.out.println("AppZip -- Source folder : " + SOURCE_FOLDER);
    	 ////System.out.println("AppZip -- Output to Zip : " + zipFile);
 
    	 FileOutputStream fos = new FileOutputStream(zipFile);
    	ZipOutputStream zos = new ZipOutputStream(fos);
    	
 
    	for(String file : this.fileList){
 
    		System.out.println("indicator file issue:" + SOURCE_FOLDER  + ":::::" + File.separator+"::::::::" + file);
    		ZipEntry ze= new ZipEntry(file);
        	zos.putNextEntry(ze);
 
        	FileInputStream in = 
                       new FileInputStream(SOURCE_FOLDER + File.separator + file);
        	
        //	System.out.println("File Added : " + SOURCE_FOLDER + File.separator + file);
 
        	int len;
        	while ((len = in.read(buffer)) > 0) {
        		zos.write(buffer, 0, len);
        	}
 
        	in.close();
    	}
 
    	zos.closeEntry();
    	//remember close it
    	zos.close();
 
    	// System.out.println("Done");   
    }catch(IOException ex){
       ex.printStackTrace();   
    }
   }
 
    /**
     * Traverse a directory and get all files,
     * and add the file into fileList  
     * @param node file or directory
     */
    public void generateFileList(File node, String SOURCE_FOLDER, String OUTPUT_ZIP_FILE, String orgTP,String final_file, String type){
    	File outFile = new File(OUTPUT_ZIP_FILE + "/" + orgTP);
    	//add file only
	if(node.isFile()){
		if (node.getName().endsWith("."+type) || (node.getName().endsWith("." + "LST")) || (node.getName().endsWith("." + "csv")) || (node.getName().endsWith("." + "ind"))) {
		
			fileList.add(generateZipEntry(SOURCE_FOLDER, node.getAbsoluteFile().toString()));
			if (final_file.toUpperCase().contains("TWO_COPIES")){
				//move.copyfile(node.getAbsolutePath().toString(), OUTPUT_ZIP_FILE, "null");
				try{
					if (outFile.toString().contains("COMPLETED") & (node.getName().endsWith("." + "ind"))){
						System.out.println("the ind file already exists in the folder");
					}else{
						FileUtils.copyFileToDirectory(node.getAbsoluteFile(), outFile);}
				} catch(IOException e) {
				    System.err.println("Caught IOException: " + e.getMessage());
				}
				
			} 
			}
	}
 
	if(node.isDirectory()){
		String[] subNote = node.list();
		for(String filename : subNote){
			generateFileList(new File(node, filename), SOURCE_FOLDER,OUTPUT_ZIP_FILE, orgTP, final_file, type);
		}
	}}

 	
    /**
     * Format the file path for zip
     * @param file file path
     * @return Formatted file path
     */
    private String generateZipEntry(String SOURCE_FOLDER, String file){
    	return file.substring(SOURCE_FOLDER.length()+1, file.length());
    }
}