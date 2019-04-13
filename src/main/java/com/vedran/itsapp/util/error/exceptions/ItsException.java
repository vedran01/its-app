package com.vedran.itsapp.util.error.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public abstract class ItsException extends Error {
  private String error;
  private HttpStatus status;

  public ItsException(String message, HttpStatus status, String error) {
    super(message);
    this.status = status;
    this.error = error;
  }
}
