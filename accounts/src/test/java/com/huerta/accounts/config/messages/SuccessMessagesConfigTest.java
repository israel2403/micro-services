package com.huerta.accounts.config.messages;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SuccessMessagesConfig.class)
public class SuccessMessagesConfigTest {
  @Autowired private SuccessMessagesConfig successMessagesConfig;

  @Test
  public void testMESSAGE_201Injection() {
    assertEquals("Account created successfully", successMessagesConfig.getMESSAGE_201());
  }

  @Test
  public void testMESSAGE_200Injection() {
    assertEquals("Account processed successfully", successMessagesConfig.getMESSAGE_200());
  }
}
