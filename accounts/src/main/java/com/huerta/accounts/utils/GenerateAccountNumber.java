package com.huerta.accounts.utils;

public interface GenerateAccountNumber {
  static Integer generateAccountNumber() {
    return (int) (Math.random() * 10000000000L);
  }
}
