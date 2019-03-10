package com.vedran.itsapp.security;

import com.vedran.itsapp.model.ItsUser;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
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

@Log
@Component
public class AdminInitilizer implements ApplicationRunner {

  @Autowired
  private MongoTemplate template;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Override

  public void run(ApplicationArguments args) throws Exception {
    ItsUser user = template.findOne(Query.query(Criteria.where("roles")
            .in(ROLE_HEAD_ADMINISTRATOR.toString()).exists(true)), ItsUser.class);

    if(user == null){
      log.info(ROLE_HEAD_ADMINISTRATOR.toString() + " not present.");
      ItsUser admin = new ItsUser();
      String email = "admin@admin.com";
      admin.setEmail(email);
      String password = "password";
      admin.setPassword(passwordEncoder.encode(password));
      admin.setRoles(new HashSet<>(Collections.singletonList(ROLE_HEAD_ADMINISTRATOR)));
      admin.setPicture("default-profile.picture.png");
      template.save(admin);
      log.info(String.format("Created admin with username %s and password %s",email,password));
    }

  }
}
