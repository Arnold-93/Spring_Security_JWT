package com.example.SpringSecurityJWT.domain.repositories;

import com.example.SpringSecurityJWT.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query("select u from UserEntity u where u.username = :username")
    Optional<UserRepository> getName(String username);

}
