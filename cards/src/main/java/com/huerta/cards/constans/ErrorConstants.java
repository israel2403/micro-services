package com.huerta.cards.constans;

public class ErrorConstants {
  public static final String CARD_ALREADY_EXISTS_ERROR =
      "{\n"
          + "  \"apiPath\": \"/api/v1/cards\",\n"
          + "  \"errorCode\": \"409\",\n"
          + "  \"errorMessage\": \"Card already exists\",\n"
          + "  \"errorTime\": \"2023-06-26T10:00:00\",\n"
          + "  \"errors\": []\n"
          + "}";

  public static final String CARD_NOT_FOUND_ERROR =
      "{\n"
          + "  \"apiPath\": \"/api/v1/cards\",\n"
          + "  \"errorCode\": \"404\",\n"
          + "  \"errorMessage\": \"Card not found\",\n"
          + "  \"errorTime\": \"2023-06-26T10:00:00\",\n"
          + "  \"errors\": []\n"
          + "}";

  public static final String INVALID_CARD_PHONE_NUMBER =
      "{\n"
          + "  \"apiPath\": \"/api/v1/cards\",\n"
          + "  \"errorCode\": \"400\",\n"
          + "  \"errorMessage\": \"Invalid card phone number\",\n"
          + "  \"errorTime\": \"2023-06-26T10:00:00\",\n"
          + "  \"errors\": []\n"
          + "}";

  // Private constructor to prevent instantiation
  private ErrorConstants() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
}
