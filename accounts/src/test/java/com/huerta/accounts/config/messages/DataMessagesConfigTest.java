package com.huerta.accounts.config.messages;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DataMessagesConfig.class)
public class DataMessagesConfigTest {

  @Autowired private DataMessagesConfig dataMessagesConfig;

  @Test
  public void testSavingsFieldInjection() {
    assertEquals("SAVINGS", dataMessagesConfig.getSavings());
  }

  @Test
  public void testAddressFieldInjection() {
    assertEquals("123 Main Street, New York", dataMessagesConfig.getAddress());
  }
}
