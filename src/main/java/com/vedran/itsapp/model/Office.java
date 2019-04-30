package com.vedran.itsapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vedran.itsapp.model.embedded.Address;
import com.vedran.itsapp.model.embedded.Contact;
import com.vedran.itsapp.model.embedded.OfficeType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "offices")
public class Office extends AbstractDocument {

  @NotEmpty
  private String name;

  @Valid
  @NotNull
  private Contact contact;

  @Valid
  @NotNull
  private Address address;

  @NotNull
  private OfficeType type;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String picture;

  @DBRef
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Stock stock;

}
