package com.ctb.prism.core.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.util.Utils;

/**
 * This class produces messages for asynchronous jobs
 * @author amit_dhara
 */

@Component
public class JmsMessageProducer {
	private static final Logger LOG = Logger.getLogger(JmsMessageProducer.class);

	@Autowired
	protected JmsTemplate jmsTemplate;
	
	@Autowired protected JmsTemplate jmsInorsTemplate;
	
	@Autowired protected JmsTemplate jmsTascTemplate;

	protected int numberOfMessages = 100;
	StringBuilder payload = null;
	int i;

	public void sendMessages() throws JMSException {
		
		for ( i = 0; i < numberOfMessages; ++i) {
			payload = new StringBuilder();
			payload.append("Message [").append(i).append("] sent at: ").append(Utils.getDateTime());
			jmsTemplate.send(new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					TextMessage message = session.createTextMessage(payload.toString());
					message.setIntProperty("messageCount", i);
					LOG.info("Sending message number [" + i + "]");
					return message;
				}
			});
		}
	}
	
	/**
	 * This method is creates message and push into amazon SQS
	 * @param jobId
	 * @throws JMSException
	 */
	public void sendJobForProcessing(final String jobId, String contractName) throws JMSException {
		LOG.info("Sending message to SQS for job : " + jobId);
		/*jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("job-id:" + jobId);
			}
		});
		*/
		String msgJson = "{\"jobId\" : " + jobId + ", \"contractName\" : " + contractName + "}";
		LOG.info("JmsMessageProducer.sendJobForProcessing(), msgJson = " + msgJson);
		jmsTemplate.convertAndSend(msgJson);
		LOG.info("Message sent to SQS");
	}
	
	/**
	 * Get cache JMS template
	 * @return
	 */
	public JmsTemplate getJmsCacheTemplate(String contractName) {
		if(contractName == null || "".equals(contractName)) contractName = Utils.getContractName();
		if("inors".equals(contractName)) return jmsInorsTemplate;
		if("tasc".equals(contractName)) return jmsTascTemplate;
		else return null;
	}
	
	/**
	 * Put key into queue
	 * @param key
	 */
	public void putCacheKey(String key, String contractName) {
		try {
			getJmsCacheTemplate(contractName).convertAndSend(key);
		} catch(Exception ex) {
			ex.printStackTrace();
			LOG.error(ex.getMessage() + "----------------- Failed to store key into cache. Key:"+key);
		}
	}
	
	
}
