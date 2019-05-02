package com.vedran.itsapp.util.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ValidationErrorResponse extends ErrorResponse{
  private List<String> errors;

  ValidationErrorResponse(String message, HttpStatus status, String error, HttpServletRequest request,
                          List<String> errors) {
    super(message, status, error, request);
    this.errors = errors;
  }

  static ValidationErrorResponse getResponse(MethodArgumentNotValidException ex ,
                                             HttpStatus status,
                                             WebRequest webRequest){
    List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + " " + err.getDefaultMessage())
            .collect(Collectors.toList());
    HttpServletRequest request = ((ServletWebRequest)webRequest).getRequest();
    return new ValidationErrorResponse("Request fields not valid",status,"Un processable entity",request,errors);
  }
}
