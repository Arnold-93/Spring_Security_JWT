package com.example.SpringSecurityJWT.infrastructure.servicios;

import com.example.SpringSecurityJWT.domain.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Esto nos sirve para poder obtener el usuario registrado en nuestra BAse de datos y luego devolverlo como un usuario de Spring security(JWT)
 *
 * */

@Service
@AllArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //obetenr el usuario por el nombre
        var userEntity = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("El usuario "+ username + "no existe"));

        //sacamos la coleccion de roles de tipo SimpleGrantedAuthority propio de spring security
        Collection<? extends GrantedAuthority> roles = userEntity.getRoles()
                .stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_".concat(rol.getName().name())))
                .collect(Collectors.toSet());


        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                roles

        );
    }
}
