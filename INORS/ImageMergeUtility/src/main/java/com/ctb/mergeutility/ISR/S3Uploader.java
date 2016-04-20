package com.ctb.mergeutility.ISR;


public class S3Uploader implements Runnable {

	private String s3Path;
	private String filePath;
	private long count;
	private long total;
	

	public S3Uploader(String s3Path, String filePath, long count, long total){
        this.s3Path = s3Path;
        this.filePath = filePath;
        this.count = count;
        this.total = total;
    }

	public void run() {
		//System.out.println(Thread.currentThread().getName() + " Start. Command = " + filePath);
		processCommand();
		System.out.println("    Uploading file to s3 ------------------------------ " + (count * 100)/total + "%" );
		//System.out.println(Thread.currentThread().getName() + " End.");
	}

	private void processCommand() {
		try {
			AWSStorageUtil aWSStorageUtil = AWSStorageUtil.getInstance();
			aWSStorageUtil.uploadObject(s3Path, filePath);
		} catch (Exception e) {
			System.out.println("Error in uploading file" + filePath);
		}
	}

}