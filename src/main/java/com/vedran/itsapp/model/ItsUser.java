package com.vedran.itsapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vedran.itsapp.model.embedded.Address;
import com.vedran.itsapp.model.embedded.Contact;
import com.vedran.itsapp.model.embedded.Gender;
import com.vedran.itsapp.model.embedded.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Document(collection = "users")
public class ItsUser extends AbstractDocument {

  @NotEmpty
  private String firstName;

  @NotEmpty
  private String lastName;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String picture;

  @NotNull
  private Gender gender;

  @NotNull
  private Date birthDate;

  @Valid
  @NotNull
  private Contact contact;

  @Valid
  @NotNull
  private Address address;

  @DBRef
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Office office;

  @NotEmpty
  @Indexed(unique = true)
  private String userName;

  @Email
  @Indexed(unique = true)
  private String email;

  @NotEmpty
  @Size(min = 6, max = 25)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String passwordResetToken;

  @NotNull
  private boolean enabled;

  @NotNull
  private boolean special;

  @NotEmpty
  private Set<Role> roles;

  public ItsUser(ItsUser user){
    setId(user.getId());
    setEmail(user.getEmail());
    setPassword(user.getPassword());
    setRoles(user.getRoles());
    setFirstName(user.getFirstName());
    setLastName(user.getLastName());
    setPicture(user.getPicture());
    setEnabled(user.isEnabled());
    setSpecial(user.isSpecial());
    setOffice(user.getOffice());
  }
}
