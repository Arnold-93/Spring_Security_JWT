package com.example.SpringSecurityJWT.infrastructure.servicios;

import com.example.SpringSecurityJWT.api.models.request.UserRequest;
import com.example.SpringSecurityJWT.api.models.response.UserResponse;
import com.example.SpringSecurityJWT.domain.entities.ERole;
import com.example.SpringSecurityJWT.domain.entities.RoleEntity;
import com.example.SpringSecurityJWT.domain.entities.UserEntity;
import com.example.SpringSecurityJWT.domain.repositories.UserRepository;
import com.example.SpringSecurityJWT.infrastructure.abstract_services.IUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Override
    public UserResponse create(UserRequest request) {
      log.info("Inicio creacion User" );
        var user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(
                    request.getRoles()
                        .stream()
                        .map(rol -> RoleEntity.builder()
                                .name(ERole.valueOf(rol))
                                .build())
                        .collect(Collectors.toSet())
                )
                .build();

       var usetEntity = userRepository.save(user);

        return entityToResponse(usetEntity);
    }

    @Override
    public UserResponse read(Long aLong) {
        return null;
    }

    @Override
    public UserResponse update(UserRequest request, Long aLong) {
        return null;
    }

    @Override
    public void delete(Long aLong) {
        var user = userRepository.findById(aLong).orElseThrow();
        userRepository.delete(user);
    }

    private UserResponse entityToResponse(UserEntity entity){
        var response = new UserResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
