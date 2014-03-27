package com.prism.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.prism.constant.Constants;
import com.prism.util.PropertyFile;

public class _SendMailSSL {
	private static final Logger logger = Logger.getLogger(_SendMailSSL.class);
	public static void sendmail(String mailTo, String body) {
		final Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		props.put("username", "amitdhara.test");
		props.put("password", "Kolkata@1");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(props.getProperty("username"), props.getProperty("password"));
			}
		});

		try {

			Properties prop = PropertyFile.loadProperties(Constants.PROPERTIES_FILE);

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("amitdhara.test@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
			message.setSubject("Testing Subject");

			message.setText("Test mail to check ETL is calling java class ... <br><br>" + body);

			Transport.send(message);

			logger.info("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		try {
			Properties prop = PropertyFile.loadProperties(Constants.PROPERTIES_FILE);
			String mailSubject = prop.getProperty("mailSubject");
			String mailBody = prop.getProperty("messageBody") + prop.getProperty("messageFooter");

			logger.info("Sending ...");
			sendHTMLMail("amit_dhara@ctb.com", mailSubject, mailBody);
			logger.info("Done");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * if(args == null || args.length < 3) { logger.info("Please provide all arguments ... "); logger.info("Usage -> mailTo yourGmailId yourGmailPassword message "); } else {
		 * //sendmail(args[0], args[1], args[2], args[3]); }
		 */
	}

	public static String sendHTMLMail(String asRecipient, String asSubject, String asBody) throws Exception {

		String asSender = "amitdhara.test@gmail.com";
		// Initialise the return value
		String msErrorStatus = "Faliure";
		// Create some properties and get the default Session.
		try {
			final Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");

			props.put("username", "amitdhara.test");
			props.put("password", "Kolkata@1");

			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(props.getProperty("username"), props.getProperty("password"));
				}
			});

			// Create a message;
			MimeMessage mvMimeMessage = new MimeMessage(session);
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
			/*
			 * if (asCcRecipient != null && asCcRecipient.length() > 0) { InternetAddress[] marrvInternetAddresses = InternetAddress .parse(asCcRecipient);
			 * mvMimeMessage.addRecipients(Message.RecipientType.CC, marrvInternetAddresses); } // Extract the Bcc-recipients and assign them to the message. // BccRecipient is a comma-separated list
			 * of e-mail addresses as per // RFC822. if (asBccRecipient != null && asBccRecipient.length() > 0) { InternetAddress[] marrvInternetAddresses = InternetAddress .parse(asBccRecipient);
			 * mvMimeMessage.addRecipients(Message.RecipientType.BCC, marrvInternetAddresses); }
			 */
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
			/*
			 * if (arAttachments != null && arAttachments.size() > 0) { for (int i = 0; i < arAttachments.size(); i++) { if (arAttachments.get(i) == null || arAttachments.get(i).toString().length() ==
			 * 0) { continue; } // Create and fill other message parts. MimeBodyPart mvBp = new MimeBodyPart(); FileDataSource mvFds = new FileDataSource(arAttachments .get(i).toString());
			 * mvBp.setDataHandler(new DataHandler(mvFds)); mvBp.setFileName(mvFds.getName()); mvMp.addBodyPart(mvBp); } }
			 */
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
