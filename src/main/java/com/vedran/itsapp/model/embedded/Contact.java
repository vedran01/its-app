package com.vedran.itsapp.model.embedded;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class Contact {
  @Email
  private String contactEmail;
  @NotEmpty
  private String cell;
  private String phone;
}
