package com.vedran.itsapp.security;

import com.vedran.itsapp.repository.ItsUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ItsUserDetailsService implements UserDetailsService {

  private final ItsUserRepository repository;

  public ItsUserDetailsService(ItsUserRepository repository) {
    this.repository = repository;
  }
  //TODO IMPLEMENT LOADING BY USERNAME
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    log.debug("Loading user with email or username: " + email );
    return repository.findByEmail(email)
            .map(ItsUserDetails::new)
            .orElseThrow(() -> new UsernameNotFoundException("User not found."));
  }
}
