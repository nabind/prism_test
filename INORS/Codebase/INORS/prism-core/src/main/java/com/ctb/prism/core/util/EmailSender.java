package com.ctb.prism.core.util;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.constant.IEmailConstants;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;


/**
 * <p>
 * Title: EmailSender.java
 * </p>
 * <p>
 * Description:This Class is used for sending e-mail and/or attachments via a SMTP server
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * @author TATA Consultancy Services
 * @version 1.0
 */
@Component("emailSender")
public class EmailSender {
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(EmailSender.class.getName());
	
	@Autowired
	private IPropertyLookup propertyLookup;

	/**
	 * @param args
	 */
	// public static void main(String[] args) {
	// ArrayList attachment = new ArrayList();
	// try {
	// sendMail("prism_dev_informatica", "25", "acsi@ctb.com", "amit_dhara@ctb.com", "", "", "Subj test", "body test", attachment);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * @param prop
	 * @param recipientEmail
	 * @param attachment
	 * @param attachmentTwo
	 * @param subject
	 * @param mailBody
	 * @throws Exception
	 */
	public void sendMail(final Properties prop, String recipientEmail, String attachment, String attachmentTwo, String subject, String mailBody) throws Exception {
		String host = prop.getProperty("mail.smtp.host");
		String port = prop.getProperty("mail.smtp.port");
		String sender = prop.getProperty("senderMail");
		String supportEmail = prop.getProperty("supportEmail");
		String SMTP_USERNAME = propertyLookup.get("mail.smtp.user");
		String SMTP_PASSWORD = propertyLookup.get("mail.smtp.pass");
		ArrayList<String> attach = new ArrayList<String>();
		attach.add(attachment);
		if (attachmentTwo != null) {
			attach.add(attachmentTwo);
		}
		if (host == null || port == null) {
			throw new Exception("Make sure smtp parametres are correctly defined");
		}

		// sendMail(host, port, sender, recipientEmail, "", supportEmail,
		// subject, mailBody, attach);
		sendHTMLMail(host, port, sender, recipientEmail, "", supportEmail, subject, mailBody, attach, SMTP_USERNAME, SMTP_PASSWORD);
	}

	/**
	 * This Method is for sending e-mail and/or attachments via a SMTP server.
	 * 
	 * @param asSmtpServer
	 *            IP address of the SMTP server
	 * @param port
	 * @param asSender
	 *            A comma-separated list of e-mail addresses as per RFC822
	 * @param asRecipient
	 *            A comma-separated list of e-mail addresses as per RFC822
	 * @param asCcRecipient
	 *            A comma-separated list of e-mail addresses as per RFC822
	 * @param asBccRecipient
	 *            A comma-separated list of e-mail addresses as per RFC822
	 * @param asSubject
	 *            Subject of the mail
	 * @param asBody
	 *            Message Body of the mail
	 * @param arAttachments
	 *            An ArrayList containing the files to be attached with the mail
	 * @return String <strong>Success</strong> if sent successfully, else the error message
	 * @throws Exception
	 */
	public String sendMail(String asSmtpServer, String port, String asSender, String asRecipient, String asCcRecipient, String asBccRecipient, String asSubject, String asBody,
			ArrayList arAttachments) throws Exception {

		// Initialize the return value
		String msErrorStatus = "Faliure";
		// Create some properties and get the default Session.
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", asSmtpServer);
			props.put("mail.smtp.port", port);
			Session mvSession = Session.getDefaultInstance(props, null);
			// Create a message;
			MimeMessage mvMimeMessage = new MimeMessage(mvSession);
			/**
			 * Extracts the senders and adds them to the message. Sender is a comma-separated list of e-mail addresses as per RFC822.
			 */
			if (asSender != null && asSender.length() > 0) {
				InternetAddress[] marrvInternetAddresses = InternetAddress.parse(asSender);
				mvMimeMessage.addFrom(marrvInternetAddresses);
			}
			/**
			 * Extract the recipients and assign them to the message. Recipient is a comma-separated list of e-mail addresses as per RFC822.
			 */
			if (asRecipient != null && asRecipient.length() > 0) {
				InternetAddress[] marrvInternetAddresses = InternetAddress.parse(asRecipient);
				mvMimeMessage.addRecipients(Message.RecipientType.TO, marrvInternetAddresses);
			}
			/**
			 * Extract the Cc-recipients and assign them to the message. CcRecipient is a comma-separated list of e-mail addresses as per RFC822.
			 */
			if (asCcRecipient != null && asCcRecipient.length() > 0) {
				InternetAddress[] marrvInternetAddresses = InternetAddress.parse(asCcRecipient);
				mvMimeMessage.addRecipients(Message.RecipientType.CC, marrvInternetAddresses);
			}
			/**
			 * Extract the Bcc-recipients and assign them to the message. BccRecipient is a comma-separated list of e-mail addresses as per RFC822.
			 */
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
				mvBp.setText(asBody);
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

	/**
	 * @param asSmtpServer
	 * @param port
	 * @param asSender
	 * @param asRecipient
	 * @param asCcRecipient
	 * @param asBccRecipient
	 * @param asSubject
	 * @param asBody
	 * @param arAttachments
	 * @return
	 * @throws Exception
	 */
	public String sendHTMLMail(String asSmtpServer, String port, String asSender, String asRecipient, 
			String asCcRecipient, String asBccRecipient, String asSubject, String asBody,
			ArrayList arAttachments, final String SMTP_USERNAME, final String SMTP_PASSWORD) throws Exception {

		// Initialise the return value
		String msErrorStatus = "Faliure";
		// Create some properties and get the default Session.
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", asSmtpServer);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.auth", "true");
			Session mvSession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
				}
			});
			// Create a message;
			MimeMessage mvMimeMessage = new MimeMessage(mvSession);
			/**
			 * Extracts the senders and adds them to the message. Sender is a comma-separated list of e-mail addresses as per RFC822.
			 */
			if (asSender != null && asSender.length() > 0) {
				InternetAddress[] marrvInternetAddresses = InternetAddress.parse(asSender);
				mvMimeMessage.addFrom(marrvInternetAddresses);
			}
			/**
			 * Extract the recipients and assign them to the message. Recipient is a comma-separated list of e-mail addresses as per RFC822.
			 */
			if (asRecipient != null && asRecipient.length() > 0) {
				InternetAddress[] marrvInternetAddresses = InternetAddress.parse(asRecipient);
				mvMimeMessage.addRecipients(Message.RecipientType.TO, marrvInternetAddresses);
			}
			/**
			 * Extract the Cc-recipients and assign them to the message. CcRecipient is a comma-separated list of e-mail addresses as per RFC822.
			 */
			if (asCcRecipient != null && asCcRecipient.length() > 0) {
				InternetAddress[] marrvInternetAddresses = InternetAddress.parse(asCcRecipient);
				mvMimeMessage.addRecipients(Message.RecipientType.CC, marrvInternetAddresses);
			}
			/**
			 * Extract the Bcc-recipients and assign them to the message. BccRecipient is a comma-separated list of e-mail addresses as per RFC822.
			 */
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
			exException.printStackTrace();
			throw new Exception(exException.getMessage());
		}
		msErrorStatus = "Success";
		return msErrorStatus;
	}
	
	/**
	 * Sends a mail to the user after Reset Password.
	 * 
	 * @param email
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	public void sendUserPasswordEmail(String email, String username, String password) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: notificationMailGD()");
		Properties prop = new Properties();
		prop.setProperty(IEmailConstants.SMTP_HOST, propertyLookup.get(IEmailConstants.SMTP_HOST));
		prop.setProperty(IEmailConstants.SMTP_PORT, propertyLookup.get(IEmailConstants.SMTP_PORT));
		prop.setProperty("senderMail", propertyLookup.get("senderMail"));
		prop.setProperty("supportEmail", propertyLookup.get("supportEmail"));
		String subject = propertyLookup.get("mail.rp.subject");
		String mailBody = propertyLookup.get("mail.rp.body");
		// subject = CustomStringUtil.replaceCharacterInString('#', username, subject);
		// mailBody = CustomStringUtil.replaceCharacterInString('#', username, mailBody);
		mailBody = CustomStringUtil.replaceCharacterInString('?', password, mailBody);
		logger.log(IAppLogger.INFO, "---------------------------------------------------------------");
		logger.log(IAppLogger.INFO, "SMTP_HOST: " + prop.getProperty(IEmailConstants.SMTP_HOST));
		logger.log(IAppLogger.INFO, "SMTP_PORT: " + prop.getProperty(IEmailConstants.SMTP_PORT));
		logger.log(IAppLogger.INFO, "---------------------------------------------------------------");
		logger.log(IAppLogger.INFO, "Subject: " + subject);
		logger.log(IAppLogger.INFO, "From: " + prop.getProperty("senderMail"));
		logger.log(IAppLogger.INFO, "To: " + email);
		logger.log(IAppLogger.INFO, "Body: " + mailBody);
		logger.log(IAppLogger.INFO, "---------------------------------------------------------------");
		logger.log(IAppLogger.INFO, "Email triggered to: " + email);
		if(IApplicationConstants.ACTIVE_FLAG.equals(propertyLookup.get(IEmailConstants.REALTIME_EMAIL_FLAG))) {
			sendMail(prop, email, null, null, subject, mailBody);
		} else if(IApplicationConstants.INACTIVE_FLAG.equals(propertyLookup.get(IEmailConstants.REALTIME_EMAIL_FLAG))) {
			logger.log(IAppLogger.WARN, "Skipping Email Sending.");
		} else {
			logger.log(IAppLogger.ERROR, "Invalid property value. " + IEmailConstants.REALTIME_EMAIL_FLAG + "=" + propertyLookup.get(IEmailConstants.REALTIME_EMAIL_FLAG));
		}
		logger.log(IAppLogger.INFO, "Email sent to: " + email);
		logger.log(IAppLogger.INFO, "Exit: notificationMailGD()");
	}

}
