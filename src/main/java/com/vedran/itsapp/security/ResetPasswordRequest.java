package com.vedran.itsapp.security;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ResetPasswordRequest {
  @NotEmpty
  String token;
  @NotEmpty
  String password;
}
