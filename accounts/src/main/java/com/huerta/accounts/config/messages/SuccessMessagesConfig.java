package com.huerta.accounts.config.messages;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "success")
@Data
public class SuccessMessagesConfig {
  private String MESSAGE_201;
  private String MESSAGE_200;
}
