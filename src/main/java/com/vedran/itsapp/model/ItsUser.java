package com.vedran.itsapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vedran.itsapp.model.embedded.Address;
import com.vedran.itsapp.model.embedded.Contact;
import com.vedran.itsapp.model.embedded.Gender;
import com.vedran.itsapp.model.embedded.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "users")
public class ItsUser extends AbstractDocument {

  @NotEmpty
  private String firstName;

  @NotEmpty
  private String lastName;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String picture;

  @NotEmpty
  private Gender gender;

  @Valid
  @NotNull
  private Contact contact;

  @Valid
  @NotNull
  private Address address;

  @NotEmpty
  private String email;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

  @NotEmpty
  @Size(min = 6)
  private String password;

  @NotEmpty
  private Set<Role> roles;
}
