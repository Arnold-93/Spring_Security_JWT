package com.example.SpringSecurityJWT.config;

import com.example.SpringSecurityJWT.config.filter.JwtAuthenticationFIlter;
import com.example.SpringSecurityJWT.config.filter.JwtAuthorizationFilter;
import com.example.SpringSecurityJWT.config.jwt.JwUtil;
import com.example.SpringSecurityJWT.infrastructure.servicios.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) //habilitamos las anotaciones de spring security para nuestros controladores esto para poder validar los roles para permitir la ejecucion de los servicios segun sus permisos
public class SecurityConfig {

    private final JwUtil jwUtil;

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {

        //le decimos como sera el ingreso del token a nuestros servicios
        JwtAuthenticationFIlter JwtAuthenticationFIlter = new JwtAuthenticationFIlter(jwUtil); //extendemos el AbstractAuthenticationProcessingFilter para poder decirle la autenticacion y configurar la ruta del servicio login
        JwtAuthenticationFIlter.setAuthenticationManager(authenticationManager); //confirmacimos el path de login ejem: http://localhost:8081/login  , POST
        JwtAuthenticationFIlter.setFilterProcessesUrl("/login");

        // le decimo que es lo que va a hacer spring security
        return httpSecurity
                .csrf(e -> e.disable()) //le indicamos que desabilita, Cross-Site Request Forgery intercepta la comunicacion
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/user/hello").permitAll(); // le decimos que permita el acceso sin pedir login al path descripto


                    auth.anyRequest().authenticated(); // le decimos que niegue el acceso a los demás servicios, se deben autenticar
                })
                .sessionManagement(session -> { //nos sirve para configurar el comportamiento de las sesiones
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS); //no trabaja con sessiones
                })
                .addFilter(JwtAuthenticationFIlter) // AGREGAMOS EL METODO DE ingreso al servicio (como autenticarnos al servicio)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class) //le decimos que el filtro jwtAuthorizationFilter se va a ejecutar antes de la clase UsernamePasswordAuthenticationFilter
                .build();

        /**
         * el addFilterBefore nos dice que va a validar el token antes de autenticarse al servicio
         * **/

    }

    /**
     * Creamos un usuario en memoria
     */
   /* @Bean
    UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager mannager = new InMemoryUserDetailsManager();
        mannager.createUser(User.withUsername("asaavedra")
                .password("1234")
                .roles()
                .build());

        return mannager;
    }*/


    /**
     * Creamos una politica de encriptacion de password con PasswordEncoder
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        //Encriptamos la constraseña
        return new BCryptPasswordEncoder();
    }


    /**
     * Se encarga de administrar la autenticacion en nuestra aplicacion
     * Esta autenticacion nos obliga a tener una contraseña encriptada
     */
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsServiceImpl) //Agregamos el obtenido desde la base de datos
                .passwordEncoder(passwordEncoder)
                .and().build();

    }
}
