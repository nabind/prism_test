package com.prism.mail;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.PasswordAuthentication;

public class SendMailSSL {
	public static void sendMail(final Properties prop, String recipientEmail, String attachment, String attachmentName,
			String subject, String mailBody) {
		Properties props = new Properties();
		props.put("mail.smtp.host", prop.getProperty("mail.smtp.host"));
		props.put("mail.smtp.socketFactory.port", prop.getProperty("mail.smtp.socketFactory.port"));
		props.put("mail.smtp.socketFactory.class", prop.getProperty("mail.smtp.socketFactory.class"));
		props.put("mail.smtp.auth", prop.getProperty("mail.smtp.auth"));
		props.put("mail.smtp.port", prop.getProperty("mail.smtp.port"));

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(prop.getProperty("smtp.username"),
								prop.getProperty("smtp.password"));
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(prop.getProperty("senderMail")));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
			message.setSubject(subject);

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			StringBuffer msgBody = new StringBuffer();
			msgBody.append(mailBody);
			messageBodyPart.setContent(msgBody.toString(), "text/html");

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();

			DataSource source = new FileDataSource(attachment);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(attachmentName);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			message.setContent(multipart);

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
