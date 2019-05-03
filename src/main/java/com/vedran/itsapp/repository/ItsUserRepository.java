package com.vedran.itsapp.repository;

import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.vedran.itsapp.model.ItsUser;
import com.vedran.itsapp.model.QItsUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;

import java.util.Optional;

public interface ItsUserRepository extends MongoRepository<ItsUser, String>, QuerydslPredicateExecutor<ItsUser> ,
        QuerydslBinderCustomizer<QItsUser> {

  boolean existsByEmail(String email);

  Optional<ItsUser> findByUserName(String userName);

  Optional<ItsUser> findByEmail(String email);

  Optional<ItsUser> findByIdAndPasswordResetToken(String id ,String token);

  @Override
  default void customize(QuerydslBindings bindings, QItsUser qItsUser){

    bindings.bind(String.class)
            .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);

    bindings.excluding(qItsUser.password, qItsUser.passwordResetToken, qItsUser.email, qItsUser.userName);
  }

}

