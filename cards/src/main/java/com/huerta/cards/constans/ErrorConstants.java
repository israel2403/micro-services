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

  public static final String INVALID_CARD_PHONE_NUMBER_REQUEST =
      "{\n"
          + "  \"apiPath\": \"uri=/api/v1/cards\",\n"
          + "  \"errorCode\": \"BAD_REQUEST\",\n"
          + "  \"errorMessage\": \"Validation failed\",\n"
          + "  \"errorTime\": \"2025-04-10T09:42:54.127865406\",\n"
          + "  \"errors\": [\n"
          + "    {\n"
          + "      \"field\": \"mobileNumber\",\n"
          + "      \"message\": \"Mobile number is required\"\n"
          + "    },\n"
          + "    {\n"
          + "      \"field\": \"mobileNumber\",\n"
          + "      \"message\": \"Mobile number must be 10 digits\"\n"
          + "    }\n"
          + "  ]\n"
          + "}";

  public static final String INVALID_CARD_PHONE_NUMBER =
      "{\n"
          + "  \"cardNumber\": \"1234567890123456\",\n"
          + "  \"cardType\": \"Credit Card\",\n"
          + "  \"mobileNumber\": \"1234567890\",\n"
          + "  \"totalLimit\": 20000,\n"
          + "  \"amountUsed\": 1000,\n"
          + "  \"availableAmount\": 19000\n"
          + "}";

  // Private constructor to prevent instantiation
  private ErrorConstants() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
}
