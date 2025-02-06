package com.huerta.accounts.config.messages;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "errors")
@Data
public class ErrorMessagesConfig {

  private String STATUS_500;
  private String CUSTUMER_ALREADY_EXISTS;
}
