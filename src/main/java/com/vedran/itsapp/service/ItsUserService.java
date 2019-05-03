package com.vedran.itsapp.service;

import com.querydsl.core.types.Predicate;
import com.vedran.itsapp.model.ItsUser;
import com.vedran.itsapp.model.Office;
import com.vedran.itsapp.model.embedded.Address;
import com.vedran.itsapp.model.embedded.Contact;
import com.vedran.itsapp.model.embedded.Gender;
import com.vedran.itsapp.model.embedded.Role;
import com.vedran.itsapp.repository.ItsUserRepository;
import com.vedran.itsapp.security.ItsUserDetails;
import com.vedran.itsapp.util.error.exceptions.BadRequestException;
import com.vedran.itsapp.util.error.exceptions.ResourceNotFoundException;
import com.vedran.itsapp.util.storage.ImageStore;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;
import java.util.function.BiPredicate;

@Log
@Service
public class ItsUserService {

  private final ItsUserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final OfficeService officeService;
  @Autowired
  private ImageStore imageStore;

  public ItsUserService(ItsUserRepository userRepository, PasswordEncoder passwordEncoder, OfficeService officeService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.officeService = officeService;
  }

  public Page<ItsUser> find(Predicate predicate, Pageable pageable){
    if(predicate != null){
      return userRepository.findAll(predicate, pageable);
    }
    return userRepository.findAll(pageable);
  }

  public ItsUser findById(String id){
    return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ItsUser.class,"id",id));
  }

  public ItsUser findOneByEmail(String email){
    return userRepository.findByEmail(email)
            .orElse(null);
  }

  public boolean existsByEmail(String email){
    return userRepository.existsByEmail(email);
  }


  public ItsUser saveUser(ItsUser user, ItsUser principal){

    if(hasAuthorityOver(principal, user)){
      user.setPicture("profile-picture.png");
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      return userRepository.save(user);
    }
    throw new BadRequestException("You don't have permission to saveUser this user.");
  }

  @PreAuthorize("@itsUserService.isOwner(#id, authentication) or\n" +
          "hasAnyRole('ROLE_HEAD_ADMINISTRATOR', 'ROLE_ADMINISTRATOR')")
  public ItsUser updateUser(String id, UpdateUserRequest request){
    ItsUser user = findById(id);
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setContact(request.getContact());
    user.setAddress(request.getAddress());
    user.setGender(request.getGender());
    user.setBirthDate(request.getBirthDate());
    return userRepository.save(user);
  }

  @PreAuthorize("@itsUserService.isOwner(#request.userId, authentication)")
  public String updatePassword(UpdatePasswordRequest request){
    ItsUser user = findById(request.getUserId());
    if(isValidPasswordRequest(request, user.getPassword())){
      user.setPassword(passwordEncoder.encode(request.getNewPassword()));
      userRepository.save(user);
      return "Password successfully updated";
    }
    throw new BadRequestException("Invalid update password request.");
  }

  @PreAuthorize("hasAnyRole('ROLE_HEAD_ADMINISTRATOR', 'ROLE_ADMINISTRATOR')")
  public String updateRoles(String id, Set<Role> roles, ItsUser principal){
    ItsUser subject = findById(id);

    if(subject.getId().equals(principal.getId())){
      int principalMaxLevel = findRoleMaxLevel(principal.getRoles());
      int suppliedRolesLevel = findRoleMaxLevel(roles);

      if(principalMaxLevel != suppliedRolesLevel) {
        throw new BadRequestException("You can not downgrade your Authority.");
      }
    }

    else if(!hasAuthorityOver(principal,subject)){
      throw new BadRequestException("You don't have permission to change this subject authority");
    }

    subject.setRoles(roles);
    userRepository.save(subject);
    return "Roles successfully updated.";
  }

  @PreAuthorize("@itsUserService.isOwner(#id, authentication) or\n" +
          "hasAnyRole('ROLE_HEAD_ADMINISTRATOR', 'ROLE_USER_ADMINISTRATOR')")
  public ItsUser updatePicture(String id, MultipartFile picture) {
    ItsUser user = findById(id);
    imageStore.deleteImage("users/" + user.getPicture());
    String newPicture = imageStore.storeImage(picture, "users/",id);
    user.setPicture(newPicture);
    return userRepository.save(user);
  }

  public ItsUser setOffice(String userId, String officeId){
    Office office = officeService.findById(officeId);
    ItsUser user = findById(userId);
    user.setOffice(office);
    return userRepository.save(user);
  }

  @PreAuthorize("hasAnyRole('ROLE_HEAD_ADMINISTRATOR, ROLE_ADMINISTRATOR')")
  public void deleteUser(String id, ItsUser principal){
    ItsUser subject = findById(id);
    if(hasAuthorityOver(principal,subject)){
      userRepository.delete(subject);
    }
    throw new BadRequestException("You don't have permission to delete this user.");
  }

  public Iterable<ItsUser> findAllById(Set<String> idS){
    return userRepository.findAllById(idS);
  }

  public boolean isOwner(String id, Authentication authentication){
    if(authentication.getPrincipal() instanceof ItsUserDetails) {
      String principalId = ((ItsUser) authentication.getPrincipal()).getId();
      return id.equals(principalId);
    }
    else return false;
  }

  private int findRoleMaxLevel(Set<Role> roles){
    if(roles.isEmpty()){
      throw new BadRequestException("Roles not present");
    }
    return roles.stream().mapToInt(Role::ordinal).max().getAsInt();
  }

  private boolean isValidPasswordRequest(UpdatePasswordRequest request, String oldPassword){
    BiPredicate<String,String> oldPasswordMatch = passwordEncoder::matches;
    BiPredicate<String, String> confirmPasswordMatch = String::equals;
    return confirmPasswordMatch.test(request.getNewPassword(), request.getConfirmPassword())
            && oldPasswordMatch.test(request.getOldPassword(),oldPassword);
  }

  private boolean hasAuthorityOver(ItsUser principal, ItsUser subject){

    int principalAuthority = findRoleMaxLevel(principal.getRoles());
    int subjectAuthority = findRoleMaxLevel(subject.getRoles());

    if(principalAuthority > subjectAuthority){
      return true;
    }
    else if(principalAuthority == subjectAuthority) {
      return principal.isSpecial() && !subject.isSpecial();
    }
    else return false;
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
    @NotNull
    private Date birthDate;
  }
}

