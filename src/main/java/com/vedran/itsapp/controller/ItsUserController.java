package com.vedran.itsapp.controller;

import com.vedran.itsapp.model.ItsUser;
import com.vedran.itsapp.model.embedded.Role;
import com.vedran.itsapp.service.ItsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.URI;
import java.security.Principal;
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
    return service.findOne(id);
  }

  @GetMapping("/principal")
  Principal itsUser(@AuthenticationPrincipal Principal user){
    return user;
  }

  @PostMapping
  ResponseEntity<ItsUser> save(@Valid @RequestBody ItsUser user){
    ItsUser savedUser = service.save(user);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(savedUser.getId()).toUri();
    return ResponseEntity.created(uri).body(savedUser);
  }

  @PutMapping("/{id}")
  ItsUser update(@PathVariable String id , @Valid @RequestBody UpdateUserRequest request){
    return service.update(id,request);
  }

  @PutMapping("/password")
  String updatePassword(@Valid @RequestBody UpdatePasswordRequest request){
    return service.updatePassword(request);
  }

  @PatchMapping("/{id}")
  String updateRole(@PathVariable String id, @RequestBody @NotNull Set<Role> role, @AuthenticationPrincipal ItsUser user){
    return service.updateRoles(id,role,user);
  }

}
