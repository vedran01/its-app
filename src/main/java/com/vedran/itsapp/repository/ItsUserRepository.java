package com.vedran.itsapp.repository;

import com.vedran.itsapp.model.ItsUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItsUserRepository extends MongoRepository<ItsUser, String> {
  Optional<ItsUser> findByEmail(String email);

  boolean existsByEmail(String email);

  @Query("{'firstName' : { $regex: ?0, $options: 'i'} , 'lastName': { $regex: ?1, $options: 'i'} }")
  Page<ItsUser> searchByFirstOrLastName(String firstName, String lastName, Pageable pageable);

}

