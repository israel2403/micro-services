package com.huerta.cards;

import static org.junit.jupiter.api.Assertions.*;

import com.huerta.cards.constans.ErrorConstants;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;

public class ErrorConstantsTest {

  @Test
  public void testCardAlreadyExistsError() {
    String expected =
        "{\n"
            + "  \"apiPath\": \"/api/v1/cards\",\n"
            + "  \"errorCode\": \"409\",\n"
            + "  \"errorMessage\": \"Card already exists\",\n"
            + "  \"errorTime\": \"2023-06-26T10:00:00\",\n"
            + "  \"errors\": []\n"
            + "}";
    assertEquals(expected, ErrorConstants.CARD_ALREADY_EXISTS_ERROR);
  }

  @Test
  public void testCardNotFoundError() {
    String expected =
        "{\n"
            + "  \"apiPath\": \"/api/v1/cards\",\n"
            + "  \"errorCode\": \"404\",\n"
            + "  \"errorMessage\": \"Card not found\",\n"
            + "  \"errorTime\": \"2023-06-26T10:00:00\",\n"
            + "  \"errors\": []\n"
            + "}";
    assertEquals(expected, ErrorConstants.CARD_NOT_FOUND_ERROR);
  }

  @Test
  public void tesInvalidtCardPhoneNumberError() {
    String expected =
        "{\n"
            + "  \"apiPath\": \"/api/v1/cards\",\n"
            + "  \"errorCode\": \"400\",\n"
            + "  \"errorMessage\": \"Invalid card phone number\",\n"
            + "  \"errorTime\": \"2023-06-26T10:00:00\",\n"
            + "  \"errors\": []\n"
            + "}";
    assertEquals(expected, ErrorConstants.INVALID_CARD_PHONE_NUMBER);
  }

  @Test
  public void testPrivateConstructor() {
    assertThrows(
        InvocationTargetException.class,
        () -> {
          java.lang.reflect.Constructor<ErrorConstants> constructor =
              ErrorConstants.class.getDeclaredConstructor();
          constructor.setAccessible(true);
          constructor.newInstance();
        });
  }
}
