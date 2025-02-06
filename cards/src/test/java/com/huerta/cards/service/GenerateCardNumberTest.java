package com.huerta.cards.service;

import static org.junit.jupiter.api.Assertions.*;

import com.huerta.cards.utils.GenerateCardNumber;
import org.junit.jupiter.api.Test;

public class GenerateCardNumberTest {
  @Test
  public void testGenerateCardNumber() {
    Long cardNumber = GenerateCardNumber.generateCardNumber();
    assertTrue(
        cardNumber >= 0 && cardNumber < 10000000000000000L,
        "Card number should be within the range 0 to 9999999999999999");
  }
}
