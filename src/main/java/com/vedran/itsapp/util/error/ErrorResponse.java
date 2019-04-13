package com.vedran.itsapp.util.error;

import com.vedran.itsapp.util.error.exceptions.ItsException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Data
public class ErrorResponse {

  private LocalDateTime timestamp;
  private int status;
  private String error;
  private String message;
  private String path;

  public ErrorResponse(String message, HttpStatus status, String error, HttpServletRequest request){
    this.timestamp = LocalDateTime.now();
    this.message = message;
    this.status = status.value();
    this.error = error;
    this.path = request.getRequestURI();

  }

  public static ResponseEntity<ErrorResponse> of(String message, HttpStatus status, String error, HttpServletRequest request){
    return ResponseEntity.status(status)
            .body(new ErrorResponse(message, status, error, request));
  }

  public static ResponseEntity<ErrorResponse> of(ItsException e, HttpServletRequest request){
    HttpStatus status = e.getStatus();
    return ResponseEntity.status(status).body(new ErrorResponse(e.getMessage(), status, e.getError(), request));
  }

}
