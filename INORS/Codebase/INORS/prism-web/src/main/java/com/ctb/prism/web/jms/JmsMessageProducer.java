package com.ctb.prism.web.jms;

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
	public void sendJobForProcessing(final String jobId) throws JMSException {
		LOG.info("Sending message to SQS for job : " + jobId);
		/*jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("job-id:" + jobId);
			}
		});
		*/
		
		jmsTemplate.convertAndSend("job-id:" + jobId);
		LOG.info("Message sent to SQS");
	}
}
