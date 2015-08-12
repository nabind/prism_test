package com.ctb.mergeutility.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
 
public class UnZip
{
    List<String> fileList;
    private static String INPUT_ZIP_FILE = "C:\\MyFile.zip";
    private static String OUTPUT_FOLDER = "C:\\tmp";
 
    public static void main( String[] args )
    {
    	UnZip unZip = new UnZip();
    	unZip.unZipIt(INPUT_ZIP_FILE,OUTPUT_FOLDER);
    }
 
    /**
     * Unzip it
     * @param zipFile input zip file
     * @param output zip file output folder
     */
    public void unZipIt(String zipFile, String outputFolder){
 
     byte[] buffer = new byte[1024];
 
     try{
 

    	//get the zip file content
    	ZipInputStream zis = 
    		new ZipInputStream(new FileInputStream(zipFile));
    	//get the zipped file list entry
    	ZipEntry ze = zis.getNextEntry();
 
    	//ctbLog.log(Level.INFO,"***  zip entry is : "  + ze.getName() + "\r\n");
    	while(ze!=null){
    		
    		
    		String fileName = ze.getName();
    		File newFile = new File(outputFolder + File.separator + fileName);          
 
            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();
 
            FileOutputStream fos = new FileOutputStream(newFile);             
 
            int len;
            while ((len = zis.read(buffer)) > 0) {
       		fos.write(buffer, 0, len);
            }
 
            fos.close();   
            ze = zis.getNextEntry();
    	}
 
        zis.closeEntry();
    	zis.close();
 
    	//System.out.println("UnZip -- Done");
 
    }catch(IOException ex){
       ex.printStackTrace(); 
    }
   }    
}