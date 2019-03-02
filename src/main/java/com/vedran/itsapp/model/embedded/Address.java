package com.vedran.itsapp.model.embedded;

import lombok.Data;

@Data
public class Address {
  private String county;
  private String city;
  private String street;
  private String postalCode;
}
