package com.huerta.cards.utils;

public interface GenerateCardNumber {

  static Long generateCardNumber() {
    return (long) (Math.random() * 10000000000000000L);
  }
}
