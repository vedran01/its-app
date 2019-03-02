package com.vedran.itsapp.repository;

import com.vedran.itsapp.model.ItsUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ItsUserRepository extends MongoRepository<ItsUser, String> {
  Optional<ItsUser> findByEmail(String email);
}
