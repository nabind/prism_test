package com.ctb.mergeutility.ISR;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
 
public class LargeUnzip {
	   final static int BUFFER = 2048;
	   
	   public static String unzipFile(String compressFilePath, String uncompressFolderPath){
		   
		   String uncompressedFilePath=null;
		   
		   try {
			   System.out.println("Extracting ...");
		         BufferedOutputStream dest = null;
		         FileInputStream fis = new FileInputStream(compressFilePath);
		         ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
		         ZipEntry entry;
		         long i = 0;
		         while((entry = zis.getNextEntry()) != null) {
		        	 uncompressedFilePath=uncompressFolderPath+File.separator+entry.getName();
		        	 i++;
		        	 if(i > 1000) {
		        		 System.out.print(".");
		        		 i = 0;
		        	 }
		            int count;
		            byte data[] = new byte[BUFFER];
		            // write the files to the disk
		            FileOutputStream fos = new FileOutputStream(uncompressedFilePath);
		            dest = new BufferedOutputStream(fos, BUFFER);
		            while ((count = zis.read(data, 0, BUFFER))  != -1) {
		               dest.write(data, 0, count);
		            }
		            dest.flush();
		            dest.close();
		         }
		         zis.close();
		      } catch(Exception e) {
		         e.printStackTrace();
		      }
		   System.out.println("Extracting done");
		   return uncompressedFilePath;
		   
	   }
}