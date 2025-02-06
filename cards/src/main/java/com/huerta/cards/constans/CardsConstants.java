package com.huerta.cards.constans;

public enum CardsConstants {
  CREDIT_CARD("Credit Card"),
  NEW_CARD_LIMIT("1000"),
  STATUS_201("201"),
  MESSAGE_201("Card created successfully"),
  STATUS_200("200"),
  MESSAGE_200("Request processed successfully"),
  STATUS_417("417"),
  MESSAGE_417_UPDATE("Update operation failed. Please try again or contact support team"),
  MESSAGE_417_DELETE("Delete operation failed. Please try again or contact support team"),
  ;

  private final String description;

  CardsConstants(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
