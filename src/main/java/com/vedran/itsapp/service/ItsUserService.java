package com.vedran.itsapp.service;

import com.vedran.itsapp.model.ItsUser;
import com.vedran.itsapp.model.embedded.Address;
import com.vedran.itsapp.model.embedded.Contact;
import com.vedran.itsapp.model.embedded.Gender;
import com.vedran.itsapp.repository.ItsUserRepository;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

  public ItsUser save(ItsUser user){
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return repository.save(user);
  }

  public ItsUser update(String id, UpdateUserRequest request){
    return null;
  }

  @Data
  public static class UpdateUserRequest{
    private String firstName;
    private String lastName;
    private Contact contact;
    private Address address;
    private Gender gender;
  }
}

