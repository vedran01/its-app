package com.vedran.itsapp.util.error;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(Class model, String field, String value) {
    super(String.format("%s with %s %s not found.",model.getSimpleName(),field,value));
  }
}
