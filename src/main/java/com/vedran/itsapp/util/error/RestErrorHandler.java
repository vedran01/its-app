package com.vedran.itsapp.util.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestErrorHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ErrorResponse handleResourceNotFound(ResourceNotFoundException e){
    return new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
  }

}
