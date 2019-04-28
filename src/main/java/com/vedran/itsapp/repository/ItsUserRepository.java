package com.vedran.itsapp.repository;

import com.vedran.itsapp.model.ItsUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ItsUserRepository extends MongoRepository<ItsUser, String> {

  boolean existsByEmail(String email);

  Optional<ItsUser> findByUserName(String userName);

  Optional<ItsUser> findByEmail(String email);

  Optional<ItsUser> findByIdAndPasswordResetToken(String id ,String token);

  @Query("{ $or: [ {'firstName' : { $regex: ?0, $options: 'i'}} , {'lastName': { $regex: ?0, $options: 'i'}}]}")
  Page<ItsUser> searchByFirstOrLastName(String name, Pageable pageable);


}

