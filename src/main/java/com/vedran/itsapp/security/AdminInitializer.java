package com.vedran.itsapp.security;

import com.vedran.itsapp.model.ItsUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;

import static com.vedran.itsapp.model.embedded.Role.ROLE_HEAD_ADMINISTRATOR;

@Slf4j
@Component
public class AdminInitializer implements ApplicationRunner {

  private final String userName;
  private final String email;
  private final String password;
  private final MongoTemplate template;
  private final PasswordEncoder passwordEncoder;

  public AdminInitializer(
          @Value("${its.admin-initializer.username}") String userName,
          @Value("${its.admin-initializer.email}") String email,
          @Value("${its.admin-initializer.password}") String password,
          MongoTemplate template,
          PasswordEncoder passwordEncoder) {
    this.userName = userName;
    this.email = email;
    this.password = password;
    this.template = template;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    ItsUser user = template.findOne(Query.query(Criteria.where("roles")
            .in(ROLE_HEAD_ADMINISTRATOR.toString()).exists(true)), ItsUser.class);

    if(user == null){
      log.debug(ROLE_HEAD_ADMINISTRATOR.toString() + " not present.");
      ItsUser admin = new ItsUser();
      admin.setFirstName("Admin");
      admin.setLastName("Admin");
      admin.setEmail(email);
      admin.setUserName(userName);
      admin.setEnabled(true);
      admin.setSpecial(true);
      String password = passwordEncoder.encode(this.password);
      admin.setPassword(password);
      admin.setRoles(new HashSet<>(Collections.singletonList(ROLE_HEAD_ADMINISTRATOR)));
      admin.setPicture("profile-picture.png");
      template.save(admin);
      log.debug(String.format("Created admin. \n username: %s \n email: %s \n password: %s",
              userName, email, this.password));
    }
  }
}
