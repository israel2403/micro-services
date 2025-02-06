package com.huerta.accounts.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class GenerateAccountNumberTest {

  @Test
  public void testGenerateAccountNumber() {
    Integer accountNumber = GenerateAccountNumber.generateAccountNumber();
    assertTrue(
        accountNumber >= 0 && accountNumber < 10000000000L,
        "Account number should be within the range 0 to 9999999999");
  }
}
