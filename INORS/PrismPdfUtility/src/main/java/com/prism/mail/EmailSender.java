package com.prism.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * <p>
 * Title: EmailSender.java
 * </p>
 * <p>
 * Description:This Class is used for sending e-mail and/or attachments via a
 * SMTP server
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * @author TATA Consultancy Services
 * @version 1.0
 */

public class EmailSender {

	/**
	 * For sending the mail to the tagged mail id of the respective org and
	 * educenter for log in info pdf opening.
	 * 
	 * @param prop
	 * @param recipientEmail
	 * @param subject
	 * @param mailBody
	 * @param supportEmail
	 * @throws Exception
	 */
	public static void sendMailInors(final Properties prop, String recipientEmail, String subject, String mailBody, String supportEmail) throws Exception {
		String host = prop.getProperty("mail.smtp.host");
		String port = prop.getProperty("mail.smtp.port");
		String SMTP_USERNAME = prop.getProperty("mail.smtp.user");
		String SMTP_PASSWORD = prop.getProperty("mail.smtp.pass");
		String sender = prop.getProperty("senderMail");
		// String supportEmail = prop.getProperty("supportEmail");
		if (host == null || port == null) {
			throw new Exception("Make sure smtp parametres are correctly defined");
		}

		/*********************IMPORTANT*************************
		 * This checking is introduced in order to prevent mail send to customer during QA time
		 * Please make sure mailToCustomer property in config.properties always set to false in QA and Development environment
		 *******************************************************/
		if(prop.getProperty("mailToCustomer").equals("true")) {
			sendHTMLMail(false, host, port, sender, recipientEmail, "", supportEmail, subject, mailBody, null, SMTP_USERNAME, SMTP_PASSWORD);
		} else {
			sendHTMLMail(false, host, port, sender, prop.getProperty("supportEmail"), "", supportEmail, subject, mailBody, null, SMTP_USERNAME, SMTP_PASSWORD);
		}	
	}

	public static void sendMailIstep(final Properties prop, String recipientEmail, String attachment, String attachmentTwo, String subject, String mailBody)
			throws Exception {
		String host = prop.getProperty("mail.smtp.host");
		String port = prop.getProperty("mail.smtp.port");
		String SMTP_USERNAME = prop.getProperty("mail.smtp.user");
		String SMTP_PASSWORD = prop.getProperty("mail.smtp.pass");
		String sender = prop.getProperty("senderMail");
		String supportEmail = prop.getProperty("supportEmail");
		ArrayList<String> attach = new ArrayList<String>();
		attach.add(attachment);
		if (attachmentTwo != null) {
			attach.add(attachmentTwo);
		}
		if (host == null || port == null) {
			throw new Exception("Make sure smtp parametres are correctly defined");
		}
		
		/*********************IMPORTANT*************************
		 * This checking is introduced in order to prevent mail send to customer during QA time
		 * Please make sure mailToCustomer property in config.properties always set to false in QA and Development environment
		 *******************************************************/
		if(prop.getProperty("mailToCustomer").equals("true")) {
			sendHTMLMail(false, host, port, sender, recipientEmail, "", supportEmail, subject, mailBody, attach, SMTP_USERNAME, SMTP_PASSWORD);
		} else {
			sendHTMLMail(false, host, port, sender, prop.getProperty("mailToCustomer"), "", supportEmail, subject, mailBody, attach, SMTP_USERNAME, SMTP_PASSWORD);
		}
	}

	public static void sendMailTasc(final Properties prop, String recipientEmail, String attachment, String attachmentTwo, String subject, String mailBody,
			String supportEmail) throws Exception {
		String host = prop.getProperty("mail.smtp.host");
		String port = prop.getProperty("mail.smtp.port");
		String sender = prop.getProperty("senderMail");
		String SMTP_USERNAME = prop.getProperty("mail.smtp.user");
		String SMTP_PASSWORD = prop.getProperty("mail.smtp.pass");
		ArrayList<String> attach = new ArrayList<String>();
		attach.add(attachment);
		if (attachmentTwo != null) {
			attach.add(attachmentTwo);
		}
		if (host == null || port == null) {
			throw new Exception("Make sure smtp parametres are correctly defined");
		}
		
		/*********************IMPORTANT*************************
		 * This checking is introduced in order to prevent mail send to customer during QA time
		 * Please make sure mailToCustomer property in config.properties always set to false in QA and Development environment
		 *******************************************************/
		if(prop.getProperty("mailToCustomer").equals("true")) {
			sendHTMLMail(true, host, port, sender, recipientEmail, "", supportEmail, subject, mailBody, attach, SMTP_USERNAME, SMTP_PASSWORD);
		} else {
			sendHTMLMail(true, host, port, sender, prop.getProperty("mailToCustomer"), "", supportEmail, subject, mailBody, attach, SMTP_USERNAME, SMTP_PASSWORD);
		}
	}

	/**
	 * For sending the mail to the tagged mail id of the respective org and
	 * educenter for log in info pdf opening.
	 * 
	 * @param prop
	 * @param recipientEmail
	 * @param subject
	 * @param mailBody
	 * @param supportEmail
	 * @throws Exception
	 */
	public static void sendMailTasc(final Properties prop, String recipientEmail, String subject, String mailBody, String supportEmail) throws Exception {
		String host = prop.getProperty("mail.smtp.host");
		String port = prop.getProperty("mail.smtp.port");
		String sender = prop.getProperty("senderMail");
		// String supportEmail = prop.getProperty("supportEmail");
		String SMTP_USERNAME = prop.getProperty("mail.smtp.user");
		String SMTP_PASSWORD = prop.getProperty("mail.smtp.pass");
		if (host == null || port == null) {
			throw new Exception("Make sure smtp parametres are correctly defined");
		}
				
		/*********************IMPORTANT*************************
		 * This checking is introduced in order to prevent mail send to customer during QA time
		 * Please make sure mailToCustomer property in config.properties always set to false in QA and Development environment
		 *******************************************************/
		if(prop.getProperty("mailToCustomer").equals("true")) {
			sendHTMLMail(true, host, port, sender, recipientEmail, "", supportEmail, subject, mailBody, null, SMTP_USERNAME, SMTP_PASSWORD);
		} else {
			sendHTMLMail(true, host, port, sender, prop.getProperty("mailToCustomer"), "", supportEmail, subject, mailBody, null, SMTP_USERNAME, SMTP_PASSWORD);
		}
	}

	public static String sendHTMLMail(boolean authRequired, String asSmtpServer, String port, String asSender, String asRecipient, String asCcRecipient,
			String asBccRecipient, String asSubject, String asBody, ArrayList arAttachments, final String SMTP_USERNAME, final String SMTP_PASSWORD)
			throws Exception {

		// Initialise the return value
		String msErrorStatus = "Faliure";
		// Create some properties and get the default Session.
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", asSmtpServer);
			props.put("mail.smtp.port", port);
			Session mvSession = Session.getDefaultInstance(props, null);
			if (authRequired) {
				props.put("mail.smtp.auth", "true");
				mvSession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
					}
				});
			}
			// Create a message;
			MimeMessage mvMimeMessage = new MimeMessage(mvSession);
			// Extracts the senders and adds them to the message.
			// Sender is a comma-separated list of e-mail addresses as per
			// RFC822.
			if (asSender != null && asSender.length() > 0) {
				InternetAddress[] marrvInternetAddresses = InternetAddress.parse(asSender);
				mvMimeMessage.addFrom(marrvInternetAddresses);
			}
			// Extract the recipients and assign them to the message.
			// Recipient is a comma-separated list of e-mail addresses as per
			// RFC822.
			if (asRecipient != null && asRecipient.length() > 0) {
				InternetAddress[] marrvInternetAddresses = InternetAddress.parse(asRecipient);
				mvMimeMessage.addRecipients(Message.RecipientType.TO, marrvInternetAddresses);
			}
			// Extract the Cc-recipients and assign them to the message.
			// CcRecipient is a comma-separated list of e-mail addresses as per
			// RFC822.
			if (asCcRecipient != null && asCcRecipient.length() > 0) {
				InternetAddress[] marrvInternetAddresses = InternetAddress.parse(asCcRecipient);
				mvMimeMessage.addRecipients(Message.RecipientType.CC, marrvInternetAddresses);
			}
			// Extract the Bcc-recipients and assign them to the message.
			// BccRecipient is a comma-separated list of e-mail addresses as per
			// RFC822.
			if (asBccRecipient != null && asBccRecipient.length() > 0) {
				InternetAddress[] marrvInternetAddresses = InternetAddress.parse(asBccRecipient);
				mvMimeMessage.addRecipients(Message.RecipientType.BCC, marrvInternetAddresses);
			}
			// Set the subject field;
			mvMimeMessage.setSubject(asSubject);
			// Create the Multipart to be added the parts to.
			Multipart mvMp = new MimeMultipart();
			// Create and fill the first message part.
			if (asBody != null) {
				MimeBodyPart mvBp = new MimeBodyPart();
				mvBp.setContent(asBody, "text/html");
				// Attach the part to the multipart.
				mvMp.addBodyPart(mvBp);
			}
			// Attach the files to the message;
			if (arAttachments != null && arAttachments.size() > 0) {
				for (int i = 0; i < arAttachments.size(); i++) {
					if (arAttachments.get(i) == null || arAttachments.get(i).toString().length() == 0) {
						continue;
					}
					// Create and fill other message parts.
					MimeBodyPart mvBp = new MimeBodyPart();
					FileDataSource mvFds = new FileDataSource(arAttachments.get(i).toString());
					mvBp.setDataHandler(new DataHandler(mvFds));
					mvBp.setFileName(mvFds.getName());
					mvMp.addBodyPart(mvBp);
				}
			}
			// Add the Multipart to the message.
			mvMimeMessage.setContent(mvMp);
			// Set the Date: header.
			mvMimeMessage.setSentDate(new Date());
			// Send the message;
			Transport.send(mvMimeMessage);
		} catch (Exception exException) {
			throw new Exception(exException.getMessage());
		}
		msErrorStatus = "Success";
		return msErrorStatus;
	}

}
