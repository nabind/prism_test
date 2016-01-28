package com.prism.util;

import java.io.File;

public class TestBulkS3 {
	
	public static void main(String args[]){
		AWSStorageUtil aWSStorageUtil = AWSStorageUtil.getInstance();
		File directory = new File("C:\\mnt\\ACSIREPORTS\\test");
		aWSStorageUtil.uploadBulkAssest("bulk",directory);
		
	}
	
}
