package com.vedran.itsapp.service;

import com.vedran.itsapp.model.ItsUser;
import com.vedran.itsapp.model.embedded.Address;
import com.vedran.itsapp.model.embedded.Contact;
import com.vedran.itsapp.model.embedded.Gender;
import com.vedran.itsapp.model.embedded.Role;
import com.vedran.itsapp.repository.ItsUserRepository;
import com.vedran.itsapp.util.error.BadRequestException;
import com.vedran.itsapp.util.error.ResourceNotFoundException;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.BiPredicate;

@Log
@Service
public class ItsUserService {

  private final ItsUserRepository repository;
  private final PasswordEncoder passwordEncoder;
  public ItsUserService(ItsUserRepository repository, PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
  }

  public Page<ItsUser> findAll(Pageable pageable){
    return repository.findAll(pageable);
  }

  public ItsUser findOne(String id){
    return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ItsUser.class,"id",id));
  }

  public ItsUser save(ItsUser user){
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return repository.save(user);
  }

  public ItsUser update(String id, UpdateUserRequest request){
    ItsUser user = findOne(id);
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setContact(request.getContact());
    user.setAddress(request.getAddress());
    user.setGender(request.getGender());
    return repository.save(user);
  }

  public String updatePassword(UpdatePasswordRequest request){
    ItsUser user = findOne(request.getUserId());
    if(isValidPasswordRequest(request, user.getPassword())){
      user.setPassword(passwordEncoder.encode(request.getNewPassword()));
      repository.save(user);
      return "Password successfully updated";
    }
    throw new BadRequestException("Invalid update password request.");
  }

  @PreAuthorize("hasRole('ROLE_HEAD_ADMINISTRATOR') or hasRole('ROLE_USER_ADMINISTRATOR')")
  public String updateRoles(String id, Set<Role> roles, ItsUser principal){
    ItsUser subject = findOne(id);

    OptionalInt changerLevel = findRoleMaxLevel(principal.getRoles());
    OptionalInt subjectLevel = findRoleMaxLevel(subject.getRoles());
    OptionalInt rolesLevel = findRoleMaxLevel(roles);

    if(!rolesLevel.isPresent()){
      throw new BadRequestException("No roles supplied, please add at least one role.");
    }

    if(subject.getId().equals(principal.getId())){
      if(testRole(Integer::equals,changerLevel.getAsInt(),rolesLevel.getAsInt())){
        subject.setRoles(roles);
        repository.save(subject);
        return "Successfully updated your roles.";
      }
      else {
        throw new BadRequestException("You can not downgrade your role.");
      }
    }

    if(!testRole((s,c) -> !s.equals(c) && s < c,subjectLevel.getAsInt(),changerLevel.getAsInt())){
      throw new BadRequestException("You don't have permission to change this subject role.");
    }

    if(!testRole((r,c) -> r < c,rolesLevel.getAsInt(),changerLevel.getAsInt())){
      throw new BadRequestException("You can not assign role grater than yours.");
    }

    subject.setRoles(roles);
    repository.save(subject);
    return "Subject role updated.";
  }

  private boolean testRole(BiPredicate<Integer ,Integer> predicate, int r1, int r2){
    return predicate.test(r1,r2);
  }

  private OptionalInt findRoleMaxLevel(Set<Role> roles){
    return roles.stream().mapToInt(Role::ordinal).max();
  }

  private boolean isValidPasswordRequest(UpdatePasswordRequest request, String oldPassword){
    BiPredicate<String,String> oldPasswordMatch = passwordEncoder::matches;
    BiPredicate<String, String> confirmPasswordMatch = String::equals;
    return confirmPasswordMatch.test(request.getNewPassword(), request.getConfirmPassword())
            && oldPasswordMatch.test(request.getOldPassword(),oldPassword);
  }

  @Data
  public static class UpdatePasswordRequest{
    @NotEmpty
    private String userId;
    @NotEmpty
    private String oldPassword;
    @NotEmpty
    @Size(min = 6, max = 25)
    private String newPassword;
    @NotEmpty
    private String confirmPassword;
  }

  @Data
  public static class UpdateUserRequest{
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @Valid
    @NotNull
    private Contact contact;
    @NotNull
    @Valid
    private Address address;
    @NotNull
    private Gender gender;
  }
}

