package com.huerta.accounts.config.messages;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ErrorMessagesConfig.class)
public class ErrorMessagesConfigTest {

  @Autowired private ErrorMessagesConfig errorMessagesConfig;

  @Test
  public void testSTATUS_500Injection() {
    assertEquals(
        "An error accurred. Please try again or contact support.",
        errorMessagesConfig.getSTATUS_500());
  }

  @Test
  public void testCUSTUMER_ALREADY_EXISTSInjection() {
    assertEquals(
        "Customer already exists registered with given mobile number",
        errorMessagesConfig.getCUSTUMER_ALREADY_EXISTS());
  }
}
