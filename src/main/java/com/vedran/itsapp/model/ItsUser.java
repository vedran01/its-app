package com.vedran.itsapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vedran.itsapp.model.embedded.Address;
import com.vedran.itsapp.model.embedded.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "users")
public class ItsUser extends AbstractDocument {


  private String firstName;
  private String lastName;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String picture;

  private String phone;
  private String cell;

  @Valid
  @NotNull
  private Address address;

  private String email;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
  private Set<Role> roles;




}
