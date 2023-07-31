package com.example.SpringSecurityJWT.domain.repositories;

import com.example.SpringSecurityJWT.domain.entities.RoleEntity;
import org.springframework.data.repository.CrudRepository;

public interface RolRepository extends CrudRepository<RoleEntity, Long> {
}
