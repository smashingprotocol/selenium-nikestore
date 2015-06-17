package com.pcm.utility;

import java.io.FileReader;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;




public class SendMail {

	
	
	public static Properties properties;
	
	static Properties mailServerProperties;
	static String host;
	static String emailLogin;
	static String pass;
	static String to;
	static MimeMessage message;
	    
	public static void sendEmail(String subject, String textmsg) throws Exception {
		
		FileReader reader = new FileReader("properties.properties");
		properties = new Properties();
		properties.load(reader);
		
		host = properties.getProperty("mailhost");
		emailLogin = properties.getProperty("mailuser");
		pass = properties.getProperty("mailpassword");
		to = properties.getProperty("tolist");
		
		mailServerProperties = System.getProperties();
		//mailServerProperties.put("mail.smtps.auth", "true");
		mailServerProperties.put("mail.smtp.auth", "true"); //enable authentication
		mailServerProperties.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		mailServerProperties.put("mail.smtp.host", host);
		mailServerProperties.put("mail.smtp.user", emailLogin);
		mailServerProperties.put("mail.smtp.password", pass);
		mailServerProperties.put("mail.smtp.port", "587");
        to = "marlonpalo@hotmail.com";
		
		Session session = Session.getDefaultInstance(mailServerProperties, new GmailAuthenticator(emailLogin,pass));
        message = new MimeMessage(session);
        
        try {
            message.setFrom(new InternetAddress(emailLogin));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Test Email");
            message.setText(textmsg);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, emailLogin, pass);
            Transport.send(message);
            transport.close();
            
            System.out.println("\n\n ===> Email sent successfully!");
            
        } catch (MessagingException me) {
            System.out.println("There has been an email error!");
            me.printStackTrace();           
        }
		
	}

	

}
