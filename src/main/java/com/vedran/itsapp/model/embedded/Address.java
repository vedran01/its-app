package com.vedran.itsapp.model.embedded;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Address {
  @NotEmpty
  private String country;
  @NotEmpty
  private String city;
  @NotEmpty
  private String street;
  @NotEmpty
  private String postalCode;
}
