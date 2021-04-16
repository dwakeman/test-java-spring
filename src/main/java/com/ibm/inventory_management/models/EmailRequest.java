package com.ibm.inventory_management.models;

import java.io.Serializable;


public class EmailRequest implements Serializable {
  private String subject;
  private String[] recipients;
  private String messageBody;

  public EmailRequest() {
    super();
  }

  public String getSubject() {
    return subject;
  }

  /**
   * Sets the Subject field for the email to be sent
   * @param subject
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * Returns the list of recipients
   * @return the list of recipients for the email to be sent
   */
  public String[] getRecipients() {
    return recipients;
  }

  public void setRecipients(String[] recipients) {
    this.recipients = recipients;
  }

  public String getMessageBody() {
    return messageBody;
  }

  public void setMessageBody(String messageBody) {
    this.messageBody = messageBody;
  }


}