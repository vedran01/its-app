package com.vedran.itsapp.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseBody<T> extends Response {

  private T data;

  public ResponseBody(String message, T data){
    super(message);
    this.data = data;
  }
}
