package com.vedran.itsapp.controller;

import com.querydsl.core.types.Predicate;
import com.vedran.itsapp.model.ItsUser;
import com.vedran.itsapp.model.embedded.Role;
import com.vedran.itsapp.security.ItsUserDetails;
import com.vedran.itsapp.service.ItsUserService;
import com.vedran.itsapp.util.Response;
import com.vedran.itsapp.util.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
  Page<ItsUser> find(@QuerydslPredicate(root = ItsUser.class) Predicate predicate,
                         Pageable pageable){
    return service.find(predicate, pageable);
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


  @GetMapping("/principal")
  ItsUser itsUser(@AuthenticationPrincipal ItsUserDetails user){
    return user;
  }


  @PreAuthorize("hasAnyRole('ROLE_HEAD_ADMINISTRATOR', 'ROLE_ADMINISTRATOR')")
  @PostMapping
  ResponseEntity<ResponseBody<ItsUser>> saveUser(@RequestBody @Valid ItsUser user,
                                                 @AuthenticationPrincipal ItsUser principal){
    ItsUser savedUser = service.saveUser(user,principal);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(savedUser.getId()).toUri();
    return ResponseEntity.created(uri).body(new ResponseBody<>("New user has been registered",savedUser));
  }

  @PutMapping("/{id}")
  ResponseEntity<ResponseBody<ItsUser>> updateUser(@PathVariable String id ,
                                                   @Valid @RequestBody UpdateUserRequest request){
    ItsUser user = service.updateUser(id, request);
    return ResponseEntity.ok(new ResponseBody<>("User has been updated",user));
  }

  @PutMapping(value = "/password", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordRequest request){
    String message = service.updatePassword(request);
    return ResponseEntity.ok(String.format("{\"message\":\"%s\"}", message));
  }

  @PutMapping(value = "/{id}/role", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseEntity<String> updateRole(@PathVariable String id,
                                    @RequestBody @NotNull Set<Role> roles,
                                    @AuthenticationPrincipal ItsUser user){
    String message = service.updateRoles(id,roles,user);
    return ResponseEntity.ok(String.format("{\"message\":\"%s\"}", message));
  }

  @PostMapping(value = "/{id}/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE ,
                                        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseBody<String> updateUserPicture(@PathVariable String id, MultipartFile picture){
    ItsUser user = service.updatePicture(id, picture);
    return new ResponseBody<>("Picture has been uploaded", user.getPicture());
  }

  @PatchMapping("/{userId}/{officeId}")
  ResponseBody<ItsUser> setOffice(@PathVariable String userId, @PathVariable String officeId){
    ItsUser user = service.setOffice(userId, officeId);
    return new ResponseBody<>("User has been assigned to " + user.getOffice().getName(), user);
  }

  @DeleteMapping("/{id}")
  Response deleteUserById(@PathVariable String id, @AuthenticationPrincipal ItsUser principal){
    return service.deleteUser(id,principal);
  }
}
