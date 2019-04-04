package com.vedran.itsapp.security;

import com.vedran.itsapp.repository.ItsUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Optionals;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ItsUserDetailsService implements UserDetailsService {

  @Autowired
  private ItsUserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return repository.findByEmail(email)
            .map(ItsUserDetails::new)
            .orElseThrow(() -> new UsernameNotFoundException("User not found."));
  }
}
