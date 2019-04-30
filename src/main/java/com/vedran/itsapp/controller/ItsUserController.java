package com.vedran.itsapp.controller;

import com.vedran.itsapp.model.ItsUser;
import com.vedran.itsapp.model.embedded.Role;
import com.vedran.itsapp.security.ItsUserDetails;
import com.vedran.itsapp.service.ItsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Set;

import static com.vedran.itsapp.service.ItsUserService.UpdatePasswordRequest;
import static com.vedran.itsapp.service.ItsUserService.UpdateUserRequest;


@RestController
@RequestMapping("/user")
public class ItsUserController {

  @Autowired
  private ItsUserService service;

  @GetMapping
  Page<ItsUser> findAll(@RequestParam(required = false, defaultValue = "0") int page,
                        @RequestParam(required = false, defaultValue = "10") int size){
    return service.findAll(PageRequest.of(page,size));
  }

  @GetMapping("/{id}")
  ItsUser findOne(@PathVariable String id){
    return service.findById(id);
  }

  @GetMapping("/email/{email}")
  ItsUser findOneByEmail(@PathVariable String email){
    return service.findOneByEmail(email);
  }

  @GetMapping("/email/{email}/exists")
  boolean existsByEmail(@PathVariable String email){
    return service.existsByEmail(email);
  }

  @GetMapping("/search")
  Page<ItsUser> searchByFirstNameOrLastName(@RequestParam(required = false, defaultValue = "") String name,
                                            @RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "10") int size){
    return service.findByFirstNameOrLastName(name, PageRequest.of(page,size));
  }

  @GetMapping("/principal")
  ItsUser itsUser(@AuthenticationPrincipal ItsUserDetails user){
    return user;
  }

  @PostMapping
  ResponseEntity<ItsUser> saveUser(@Valid @RequestBody ItsUser user, @AuthenticationPrincipal ItsUser principal){
    ItsUser savedUser = service.saveUser(user,principal);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(savedUser.getId()).toUri();
    return ResponseEntity.created(uri).body(savedUser);
  }

  @PutMapping("/{id}")
  ItsUser updateUser(@PathVariable String id , @Valid @RequestBody UpdateUserRequest request){
    return service.updateUser(id,request);
  }

  @PutMapping(value = "/password", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordRequest request){
    String message = service.updatePassword(request);
    return ResponseEntity.ok(String.format("{\"message\":\"%s\"}", message));
  }

  @PutMapping(value = "/{id}/role", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseEntity<String> updateRole(@PathVariable String id, @RequestBody @NotNull Set<Role> roles, @AuthenticationPrincipal ItsUser user){
    String message = service.updateRoles(id,roles,user);
    return ResponseEntity.ok(String.format("{\"message\":\"%s\"}", message));
  }

  @PostMapping(value = "/{id}/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE ,
                                        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ItsUser updateUserPicture(@PathVariable String id, MultipartFile picture){
    return service.updatePicture(id,picture);
  }

  @PatchMapping("/{userId}/{officeId}")
  ItsUser setOffice(@PathVariable String userId, @PathVariable String officeId){
    return service.setOffice(userId,officeId);
  }

  @DeleteMapping("/{id}")
  ResponseEntity<?> deleteUserById(@PathVariable String id, @AuthenticationPrincipal ItsUser principal){
    service.deleteUser(id,principal);
    return ResponseEntity.noContent().build();
  }
}
