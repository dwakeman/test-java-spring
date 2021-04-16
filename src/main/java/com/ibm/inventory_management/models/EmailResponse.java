package com.ibm.inventory_management.models;

import java.io.Serializable;

public class EmailResponse implements Serializable {
  private String responseMessage;

  public EmailResponse() {
    super();
  }

  public String getResponseMessage() {
    return responseMessage;
  }

  public void setResponseMessage(String message) {
    this.responseMessage = message;
  }


}