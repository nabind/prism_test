package com.ctb.prism.web.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ctb.prism.inors.service.IInorsService;

/**
 * This is a JMS message listener class. 
 * This will be triggered on receiving any message to queue.
 * Configuration is done in jms-config.xml file
 * @author amit_dhara
 *
 */
public class JmsMessageListener /*implements MessageListener*/ {

	private static final Logger LOG = Logger.getLogger(JmsMessageListener.class);
	
	@Autowired private IInorsService inorsService;

	/**
	 * Trigger this method on receive any message from amazon SQS
	 * @param message
	 */
	public void onSQSMessage(String message) {
		try {
			LOG.info(" >>> Consumed message: " + message);
			
			// code for get job details
			
			String jobId = "0";
			// code for download pdf 
			LOG.info("-- call code to generate PDF - async");
			if(message != null && message.indexOf(":") != -1 ) {
				jobId = message.substring(message.indexOf(":")+1, message.length());
				inorsService.batchPDFDownload( jobId );
			}
			LOG.info("-- END call code to generate PDF - async");
			
			
		} catch (Exception e) {
			// code to log error
			LOG.error(" >>> ERROR getting message: " + message);
			e.printStackTrace();
		}
	}
	
	/**
	 * Trigger this message if any message comes from Active MQ
	 * Note: implementation of MessageListener is required for this.
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
			if(msg.getText() != null && msg.getText().indexOf(":") != -1 ) {
				jobId = msg.getText().substring(msg.getText().indexOf(":")+1, msg.getText().length());
				inorsService.batchPDFDownload( jobId );
			}
			LOG.info("-- END call code to generate PDF - async");
			
			
		} catch (JMSException e) {
			// code to log error
			e.printStackTrace();
		}
	}
}
