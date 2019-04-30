package com.vedran.itsapp.util.error.exceptions;

import org.springframework.http.HttpStatus;


public class ResourceNotFoundException extends ItsException {

  public ResourceNotFoundException(String message){
    super(message, HttpStatus.NOT_FOUND, "");
  }

  public ResourceNotFoundException(Class model, String field, String value) {
    super(String.format("%s with %s %s not found.",model.getSimpleName(),field,value), HttpStatus.NOT_FOUND, "Not found");
  }
}
