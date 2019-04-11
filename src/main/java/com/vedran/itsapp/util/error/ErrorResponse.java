package com.vedran.itsapp.util.error;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {
  private String message;
  private HttpStatus status;
  private int code;

  public ErrorResponse(String message, HttpStatus status){
    this.message = message;
    this.status = status;
    this.code = status.value();
  }
}
