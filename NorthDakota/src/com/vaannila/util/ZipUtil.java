package com.vaannila.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

public class ZipUtil {
    public static void zipit(List<String> files, String zipName) throws Exception {
    	ZipOutputStream zos = null;
    	FileOutputStream fos = null;
    	BufferedInputStream bis = null;
        try {
        	System.out.println("Compressing pdfs ... ");
			File zipFile = new File(zipName);
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(fos);
			int bytesRead;
			byte[] buffer = new byte[1024];
			CRC32 crc = new CRC32();
			int compressCount = 0;
			for (String name : files) {
				System.out.println("Compressing "+ ++compressCount + " of " + files.size());
			    File file = new File(name);
			    if (!file.exists()) {
			        System.err.println("Skipping: " + name);
			        continue;
			    }
			    bis = new BufferedInputStream(new FileInputStream(file));
			    crc.reset();
			    while ((bytesRead = bis.read(buffer)) != -1) {
			        crc.update(buffer, 0, bytesRead);
			    }
			    bis.close();
			    // Reset to beginning of input stream
			    bis = new BufferedInputStream(new FileInputStream(file));
			    ZipEntry entry = new ZipEntry(file.getName());
			    entry.setMethod(ZipEntry.STORED);
			    entry.setCompressedSize(file.length());
			    entry.setSize(file.length());
			    entry.setCrc(crc.getValue());
			    zos.putNextEntry(entry);
			    while ((bytesRead = bis.read(buffer)) != -1) {
			        zos.write(buffer, 0, bytesRead);
			    }
			    bis.close();
			}
			zos.close();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			IOUtils.closeQuietly(zos);
			IOUtils.closeQuietly(fos);
			IOUtils.closeQuietly(bis);
		}
		System.out.println("Compressing completed ... ");
    }
    
    public static void main(String[] args) {
    	List<String> files = new ArrayList<String>();
    	try {
    		files.add("C:\\Temp\\Invitation_Pdf.pdf");
    		files.add("C:\\Temp\\Invitation_Pdf (1).pdf");
    		files.add("C:\\Temp\\Invitation_Pdf (2).pdf");
			zipit(files, "C:\\Temp\\Final_projection.zip");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
