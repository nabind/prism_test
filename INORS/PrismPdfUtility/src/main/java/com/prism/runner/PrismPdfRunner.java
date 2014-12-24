package com.prism.runner;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.prism.service.InorsGrowthService;
import com.prism.service.InorsService;
import com.prism.service.PrismPdfService;
import com.prism.service.TascService;
import com.prism.util.Constants;
import com.prism.util.CustomStringUtil;

/**
 * It is the entry point of the process flow. It contains the main() method.
 * 
 * @author TCS
 *
 */
public class PrismPdfRunner {

	private static final Logger logger = Logger.getLogger(PrismPdfRunner.class);

	/**
	 * The main method of the utility.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		boolean isValidArgs = false;
		if (args.length < 2) {
			logger.error("Please provide all command line arguments.");
		} else {
			String arg0 = args[0];
			if (arg0 != null) {
				if (Constants.ARG_0_INORS.equalsIgnoreCase(arg0)) {
					String arg1 = args[1];
					if (arg1 != null) {
						isValidArgs = true;
					} else {
						logger.error("Second argument for INORS is Invalid. Please provide:");
						logger.error("L = Login Pdf");
						logger.error("I = IC Letter Pdf");
						logger.error("A = Both Login Pdf and IC Letter Pdf");
						logger.error("S = Individual IC Letter Pdf");
					}
				} else if (Constants.ARG_0_INORS_GRWOTH.equalsIgnoreCase(arg0)) {
					isValidArgs = true;
				} else if (Constants.ARG_0_TASC.equalsIgnoreCase(arg0)) {
					String arg1 = args[1];
					if (arg1 != null) {
						isValidArgs = true;
					} else {
						logger.error("Second argument for TASC is Invalid. Please provide:");
						logger.error("O = Org Node Id");
						logger.error("E = Education Center Id");
					}
				}
			} else {
				logger.error("First argument is Invalid. Please provide: I = Inors / S = INORS Growth / T = Tasc");
			}
		}
		if (isValidArgs) {
			logger.info("Arguments are valid. Creating Service...");
			PrismPdfService prismPdfService = getService(args[0]);
			try {
				prismPdfService.mainMethod(CustomStringUtil.getAllButFirstArg(args));
				logger.info("Service execution completed.");
				logger.info("It may take some time to release all resources. Please wait...");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			logger.error("Arguments are Invalid. Please provide all command line arguments.");
		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		String timeTaken = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(timeDiff), TimeUnit.MILLISECONDS.toSeconds(timeDiff)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeDiff)));
		logger.info("Time Taken = " + timeTaken);
	}

	/**
	 * This method checks the first commandline argument and decides the
	 * services for Inors / Inors Growth / Tasc.
	 * 
	 * @param arg0
	 * @return
	 */
	public static PrismPdfService getService(String arg0) {
		PrismPdfService service = null;
		if (Constants.ARG_0_INORS.equalsIgnoreCase(arg0)) {
			service = new InorsService();
		} else if (Constants.ARG_0_INORS_GRWOTH.equalsIgnoreCase(arg0)) {
			service = new InorsGrowthService();
		} else if (Constants.ARG_0_TASC.equalsIgnoreCase(arg0)) {
			service = new TascService();
		}
		return service;
	}

}
