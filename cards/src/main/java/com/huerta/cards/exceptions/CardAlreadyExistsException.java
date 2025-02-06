package com.huerta.cards.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class CardAlreadyExistsException extends RuntimeException {

  public CardAlreadyExistsException(String message) {
    super(message);
  }
}
