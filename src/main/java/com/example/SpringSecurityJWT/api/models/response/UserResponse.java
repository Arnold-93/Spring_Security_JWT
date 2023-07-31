package com.example.SpringSecurityJWT.api.models.response;

import com.example.SpringSecurityJWT.domain.entities.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResponse implements Serializable {
    private Long id;

    private String email;

    private String username;

    private String password;

    private Set<RoleEntity> roles;
}
