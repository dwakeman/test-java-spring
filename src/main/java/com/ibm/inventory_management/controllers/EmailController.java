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
    
    Properties properties = new Properties();

    // Get the API Key to use as the password for the mail server
    String apikey = System.getenv("MAIL_PASSWORD");

    //Get the Preshared Key to use for authorization to use this service
    String presharedKey = System.getenv("MAIL_PRESHARED_KEY");

    // Check and see if the preshared key was provided
    String inputKey = emailRequest.getPresharedKey();
    LOGGER.info("the provided key is " + inputKey);
    LOGGER.info("the preshared key is " + presharedKey);
    if (!inputKey.equals(presharedKey)) {
      emailResponse.setResponseMessage("Error: No Preshared Key provided or key is not valid");
      return ResponseEntity.status(403).body(emailResponse);
    }

    properties.setProperty("mail.smtp.host", emailConfig.getHost());
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
      message.setFrom(new InternetAddress(emailConfig.getFromEmail(), emailConfig.getFromName()));

      // Set To: header field of the header.
      String[] recipients = emailRequest.getRecipients();
      for (int i=0;i<recipients.length;i++) {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipients[i]));
      }
      // Set Subject: header field
      message.setSubject(emailRequest.getSubject());

      // Now set the actual message
      message.setText(emailRequest.getMessageBody());

      // Send message
      Transport.send(message);
      //System.out.println("Sent message successfully....");

      emailResponse.setResponseMessage("Email sent");

    } catch (MessagingException | UnsupportedEncodingException | IllegalStateException mex) {
        mex.printStackTrace();

        emailResponse.setResponseMessage("Error: " + mex.toString());
        
    }

    return ResponseEntity.ok().body(emailResponse);
        
  }

}