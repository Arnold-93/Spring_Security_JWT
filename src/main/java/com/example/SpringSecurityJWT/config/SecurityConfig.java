package com.example.SpringSecurityJWT.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity)throws Exception{
        return httpSecurity
                .csrf( e -> e.disable() ) //le indicamos que desabilita, Cross-Site Request Forgery intercepta la comunicacion
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/user/hello").permitAll(); // le decimos que permita el acceso sin pedir login al path descripto
                    auth.anyRequest().authenticated(); // le decimos que niegue el acceso a los demás servicios, se deben autenticar
                })
                .sessionManagement(session -> { //nos sirve para configurar el comportamiento de las sesiones
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS); //no trabaja con sessiones
                })
                .httpBasic()
                .and()
                .build();
    }

    /**
     * Creamos un usuario en memoria
     * */
    @Bean
    UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager mannager = new InMemoryUserDetailsManager();
        mannager.createUser(User.withUsername("asaavedra")
                .password("1234")
                .roles()
                .build());

        return mannager;
    }


    /**
     * Creamos una politica de encriptacion de password con PasswordEncoder
     * */
    @Bean
    PasswordEncoder passwordEncoder(){
        //Encriptamos la constraseña
        return new BCryptPasswordEncoder();
    }


    /**
     * Se encarga de administrar la autenticacion en nuestra aplicacion
     * Esta autenticacion nos obliga a tener una contraseña encriptada
     * */
    @Bean
    AuthenticationManager authenticationManager( HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception{
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder)
                .and().build();

    }
}
