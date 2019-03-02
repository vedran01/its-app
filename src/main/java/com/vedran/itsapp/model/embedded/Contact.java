package com.vedran.itsapp.model.embedded;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Contact {
  @NotEmpty
  private String contactMail;
  @NotEmpty
  private String cell;
  @NotEmpty
  private String phone;
}
