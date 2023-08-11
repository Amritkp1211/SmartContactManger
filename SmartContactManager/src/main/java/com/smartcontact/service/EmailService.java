package com.smartcontact.service;


import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
  
	public boolean sendEmail(String subject,String message,String to) {
		
		boolean f=false;
		
		String from="";
		
		//variable for gmail
		String host="smtp.gmail.com";
		
		//get the system properties
		Properties properties=System.getProperties();
		
		//setting imp info to properties object
		properties.put("mail.smtp.host",host);
		properties.put("mail.smtp.port","465");
		properties.put("mail.smtp.ssl.enable",true);
		properties.put("mail.smtp.auth","true");
		
		
		//step 1 to get session object
		Session session=Session.getInstance(properties, new Authenticator() {
		
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("amritkptectona@gmail.com", "rsccguavtgtgaamy");
			}		
		});
		
		  session.setDebug(true);
		  
		  
		  //step 2 compose the mail [text.multimedia]
		      MimeMessage m=new MimeMessage(session);
		  
		  try {
			  //from email
			  m.setFrom(from);
			  
			  //add recipient to Message
			  m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			  
			  //adding subject to mail
			  m.setSubject(subject);
			  
			  //adding text message
			 // m.setText(message);
			  m.setContent(message,"text/html");
			  
			  //step3 send the message using transport class
			  Transport.send(m);
			  
			 
			  
			  System.out.println("sent successfull...");
			  
			  f=true;
			  
			  
		} catch (Exception e) {

			e.printStackTrace();
		}
		  
		return f;
		  
		
		
		
	}
}
