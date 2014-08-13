package com.ctb.prism.core.util;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.login.transferobject.UserTO;

public final class SESMailService {

	@Autowired private static IPropertyLookup propertyLookup;
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(SESMailService.class.getName());
	

	// Supply your SMTP credentials below. Note that your SMTP credentials are different from your AWS credentials.
	static final String SMTP_USERNAME = "AKIAINAOXUXPEVMIVA5Q"; //propertyLookup.get("mail.ses.username");   // Replace with your SMTP username.
	static final String SMTP_PASSWORD ="Ah2sMLaqDTh+KwVEzsDt0mTGAw2/UBcVi2SUEar+xnTF"; //propertyLookup.get("mail.ses.password"); // Replace with your SMTP password.

	// Amazon SES SMTP host name. This example uses the us-east-1 region.
	static final String HOST = "email-smtp.us-east-1.amazonaws.com"; //propertyLookup.get("mail.ses.host");	

	// Port we will connect to on the Amazon SES SMTP endpoint. We are choosing port 25 because we will use
	// STARTTLS to encrypt the connection.
	static final int PORT = 25; //Integer.parseInt(propertyLookup.get("mail.ses.port"));	
	
	
	static final String FROM = "CTB_ISTEP_Helpdesk@ctb.com"; // propertyLookup.get("senderMail");   // Replace with your "From" address. This address must be verified.
	static final String supportEmail = "abir.d@tcs.com"; // propertyLookup.get("supportEmail"); // support email will go as BCC
	
	//Hard coded for the time, this will be moved into the method and will be argument based
	static final String BODY = "This email was sent through the Amazon SES SMTP interface by using Java.";
	static final String SUBJECT = "Amazon SES test (SMTP interface accessed using Java)";
	

	public static void sendUserPasswordEmail(String email, List<UserTO> userToList, String password) throws Exception {

		// Create a Properties object to contain connection configuration information.
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.port", PORT); 


	 /****Set properties indicating that we want to use STARTTLS to encrypt the connection.
	 The SMTP session will begin on an unencrypted connection, and then the client
	 will issue a STARTTLS command to upgrade to an encrypted connection.****/

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");


		// Create a Session object to represent a mail session with the specified properties. 

		Session session = Session.getDefaultInstance(props);

		// Create a message with the specified information. 

		MimeMessage msg = new MimeMessage(session);

		msg.setFrom(new InternetAddress(FROM));

		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
		
		msg.setRecipient(Message.RecipientType.BCC, new InternetAddress(supportEmail));

		msg.setSubject(SUBJECT);

		msg.setContent(BODY,"text/plain");
		// Create a transport.        

		Transport transport = session.getTransport();
		// Send the message.

		try	{
			logger.log(IAppLogger.INFO, "Attempting to send an email through the Amazon SES SMTP interface...");

			// Connect to Amazon SES using the SMTP username and password you specified above.
			transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

			// Send the email.
			transport.sendMessage(msg, msg.getAllRecipients());
			logger.log(IAppLogger.INFO,"Email sent!");

		}

		catch (Exception ex) {
			logger.log(IAppLogger.INFO,"The email was not sent.");
			logger.log(IAppLogger.INFO,"Error message: " + ex.getMessage());
		}

		finally	{
			// Close and terminate the connection.
			transport.close();          
		}
	}
}
