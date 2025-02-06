package com.huerta.cards.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CardWithMobileNumberNotFoundException extends RuntimeException {

  public CardWithMobileNumberNotFoundException(String mobileNumber) {
    super("Card with mobile number " + mobileNumber + " not found");
  }
}
