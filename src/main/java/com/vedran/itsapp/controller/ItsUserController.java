package com.vedran.itsapp.controller;

import com.vedran.itsapp.model.ItsUser;
import com.vedran.itsapp.service.ItsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.vedran.itsapp.service.ItsUserService.*;


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

  @PostMapping
  ResponseEntity<ItsUser> save(@RequestBody ItsUser user){
    ItsUser savedUser = service.save(user);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(savedUser.getId()).toUri();
    return ResponseEntity.created(uri).body(savedUser);
  }

  @PutMapping("/{id}")
  ItsUser update(@PathVariable String id ,@RequestBody UpdateUserRequest request){
    return service.update(id,request);
  }

}
