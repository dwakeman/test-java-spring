package com.ibm.inventory_management.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "email")
public class EmailConfig {
  private String host;
  private String fromEmail;
  private String fromName;

  public String getHost() {
    return this.host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getFromEmail() {
    return this.fromEmail;
  }

  public void setFromEmail(String email) {
    this.fromEmail = email;
  }

  public String getFromName() {
    return this.fromName;
  }

  public void setFromName(String name) {
    this.fromName = name;
  }
}
