package com.vedran.itsapp.util.error.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ItsException {

  public BadRequestException(String message) {
    super(message, HttpStatus.BAD_REQUEST, "Bad request");

  }
}
