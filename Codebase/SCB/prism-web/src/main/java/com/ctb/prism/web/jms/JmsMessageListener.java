package com.ctb.prism.web.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ctb.prism.inors.service.IInorsService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * This is a JMS message listener class. This will be triggered on receiving any
 * message to queue. Configuration is done in jms-config.xml file
 * 
 * @author amit_dhara
 *
 */
public class JmsMessageListener /* implements MessageListener */{

	private static final Logger LOG = Logger.getLogger(JmsMessageListener.class);

	@Autowired
	private IInorsService inorsService;

	/**
	 * Trigger this method on receive any message from amazon SQS
	 * 
	 * @param message
	 */
	public void onSQSMessage(String message) {
		try {
			LOG.info(" >>> Consumed message: " + message);

			// code for get job details

			String jobId = "0";
			// code for download pdf
			LOG.info("-- call code to generate PDF - async");
			LOG.info("JmsMessageProducer.onMessage(), message = " + message);
			if (message != null && message.indexOf("jobId") != -1 && message.indexOf("contractName") != -1) {
				try {
					JobIdTO to = (JobIdTO) new Gson().fromJson(message, JobIdTO.class);
					jobId = to.getJobId();
					String contractName = to.getContractName();
					LOG.info("jobId = " + jobId);
					LOG.info("contractName = " + contractName);
					inorsService.asyncPDFDownload(jobId, contractName);
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
			}
			LOG.info("-- END call code to generate PDF - async");

		} catch (Exception e) {
			// code to log error
			LOG.error(" >>> ERROR getting message: " + message);
			e.printStackTrace();
		}
	}

	/**
	 * Trigger this message if any message comes from Active MQ Note:
	 * implementation of MessageListener is required for this.
	 * 
	 * @param message
	 */
	public void onMessage(Message message) {
		try {
			TextMessage msg = (TextMessage) message;
			LOG.info(" >>> Consumed message: " + msg.getText());

			// code for get job details

			String jobId = "0";
			// code for download pdf
			LOG.info("-- call code to generate PDF - async");
			LOG.info("JmsMessageProducer.onMessage(), msg = " + msg);
			if (msg.getText() != null && msg.getText().indexOf("jobId") != -1 && msg.getText().indexOf("contractName") != -1) {
				try {
					JobIdTO to = (JobIdTO) new Gson().fromJson(msg.getText(), JobIdTO.class);
					jobId = to.getJobId();
					String contractName = to.getContractName();
					LOG.info("jobId = " + jobId);
					LOG.info("contractName = " + contractName);
					inorsService.asyncPDFDownload(jobId, contractName);
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
			}
			LOG.info("-- END call code to generate PDF - async");

		} catch (JMSException e) {
			// code to log error
			e.printStackTrace();
		}
	}

	class JobIdTO {
		private String jobId;
		private String contractName;

		public String getJobId() {
			return jobId;
		}

		public void setJobId(String jobId) {
			this.jobId = jobId;
		}

		public String getContractName() {
			return contractName;
		}

		public void setContractName(String contractName) {
			this.contractName = contractName;
		}
	}
}
