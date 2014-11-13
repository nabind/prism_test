package com.prism.runner;

import org.apache.log4j.Logger;

import com.prism.service.InorsService;
import com.prism.service.IstepService;
import com.prism.service.PrismPdfService;
import com.prism.service.TascService;
import com.prism.util.Constants;
import com.prism.util.CustomStringUtil;

/**
 * It is the entry point of the process flow. It contains the main() method.
 * 
 * @author 796763
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
		if (args.length < 3) {
			logger.error("Arguments are Invalid. Please provide all command line arguments.");
		} else {
			String arg0 = args[0];
			if (arg0 != null) {
				logger.info("Creating Service...");
				PrismPdfService prismPdfService = getService(arg0);
				try {
					prismPdfService.mainMethod(CustomStringUtil.getAllButFirstArg(args));
					logger.info("Service execution completed.");
					logger.info("It may take some time to release all resources. Please wait...");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				logger.error("First argument is Invalid. Please provide: I = Inors / S = Istep / T = Tasc");
			}
		}
	}

	/**
	 * This method checks the first commandline argument and decides the
	 * services for Inors / Istep / Tasc.
	 * 
	 * @param arg0
	 * @return
	 */
	public static PrismPdfService getService(String arg0) {
		PrismPdfService service = null;
		if (Constants.ARG_0_INORS.equalsIgnoreCase(arg0)) {
			service = new InorsService();
		} else if (Constants.ARG_0_ISTEP.equalsIgnoreCase(arg0)) {
			service = new IstepService();
		} else if (Constants.ARG_0_TASC.equalsIgnoreCase(arg0)) {
			service = new TascService();
		}
		return service;
	}

}
