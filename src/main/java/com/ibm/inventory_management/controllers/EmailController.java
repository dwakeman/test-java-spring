package com.ibm.inventory_management.controllers;

import com.ibm.inventory_management.models.EmailRequest;
import com.ibm.inventory_management.models.EmailResponse;
import com.ibm.inventory_management.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.util.Properties;


import javax.mail.*;
import javax.mail.internet.*;

@RestController
public class EmailController {

  @Autowired
  private EmailConfig emailConfig;

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

  @PostMapping(
    path = "/email",
    consumes = "application/json",
    produces = "application/json"
  )
  public ResponseEntity<EmailResponse> sendEmail(@RequestBody EmailRequest emailRequest ) {


    LOGGER.info("The SMTP host is " + emailConfig.getHost());
    LOGGER.info("the SMTP From address is " + emailConfig.getFromEmail());
    LOGGER.info("the SMTP From Name is " + emailConfig.getFromName());
    LOGGER.info("Received request to send email with subject: " + emailRequest.getSubject());
    LOGGER.info("Recipients: " + emailRequest.getRecipients().toString());
    LOGGER.info("Body: " + emailRequest.getMessageBody());
    
    EmailResponse emailResponse = new EmailResponse();
    
    // Recipient's email ID needs to be mentioned.
    //Todo:   Need to update this to use the full array and not just the first item
    String to = emailRequest.getRecipients()[0];

    // Sender's email ID needs to be mentioned
    String from = emailConfig.getFromName();

    // Get the host name from application.yml
    String host = emailConfig.getHost();

    // Get system properties
    Properties properties = System.getProperties();

    // Setup mail server
    String apikey = System.getenv("MAIL_PASSWORD");
    //LOGGER.info("the passord is: " + apikey);
    properties.setProperty("mail.smtp.host", host);
    properties.setProperty("mail.smtp.auth", "true");

    // Get the Session object.
    Session session = Session.getInstance(properties,
      new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("apikey", apikey);
        }
      }
    );

    try {
        // Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(session);

        // Set From: header field of the header.
        message.setFrom(new InternetAddress(from, emailConfig.getFromName()));

        // Set To: header field of the header.
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        // Set Subject: header field
        message.setSubject(emailRequest.getSubject());

        // Now set the actual message
        message.setText(emailRequest.getMessageBody());

        // Send message
        Transport.send(message);
        //System.out.println("Sent message successfully....");

        emailResponse.setResponseMessage("Email sent");

//        return ResponseEntity.ok().body(emailResponse);

    } catch (MessagingException | UnsupportedEncodingException mex) {
        mex.printStackTrace();

        emailResponse.setResponseMessage("Error: " + mex.toString());
        
    }

    return ResponseEntity.ok().body(emailResponse);
        
  }

}